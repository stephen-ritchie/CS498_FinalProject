// This file contains the creation of the mail message itself...does not appear to send it

/*
 * The MIT License
 *
 * Copyright (c) 2004-2010, Sun Microsystems, Inc., Kohsuke Kawaguchi,
 * Bruce Chapman, Daniel Dyer, Jean-Baptiste Quenot
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package hudson.tasks;

// -----------------------------------------------------------------------------
// Importing a bunch of stuff
// -frankly no one understands what any of this is, but it's all needed
// -----------------------------------------------------------------------------
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import hudson.FilePath;
import hudson.Util;
import hudson.Functions;
import hudson.model.*;
import hudson.scm.ChangeLogSet;
import jenkins.plugins.mailer.tasks.i18n.Messages;
import jenkins.model.Jenkins;
import jenkins.plugins.mailer.tasks.MailAddressFilter;
import jenkins.plugins.mailer.tasks.MimeMessageBuilder;
import org.jenkinsci.plugins.displayurlapi.DisplayURLProvider;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.AddressException;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import org.acegisecurity.Authentication;
import org.acegisecurity.userdetails.UsernameNotFoundException;

// ** Stephen Code - Start *****************************************************
import java.util.Calendar;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;
import java.net.*;
import java.io.*;
// ** Stephen Code - END *******************************************************


/**
 * Core logic of sending out notification e-mail.
 *
 * @author Jesse Glick
 * @author Kohsuke Kawaguchi
 */
public class MailSender {
    // -------------------------------------------------------------------------
    // Declaring some initial variables...
    // -------------------------------------------------------------------------
    private String recipients; // Whitespace-separated list of e-mail addresses that represent recipients.
    private List<AbstractProject> includeUpstreamCommitters = new ArrayList<AbstractProject>();
    private boolean dontNotifyEveryUnstableBuild; // If true, only the first unstable build will be reported.
    private boolean sendToIndividuals; //If true, individuals will receive e-mails regarding who broke the build.
    private String charset; // The charset to use for the text and subject.
    // ** ------------ Aton code start ------------ **
    private String relevantDevelopers;
    private boolean relevantOnly;
    // ** ------------ Aton code end ------------ **
    // ** Stephen Code - START *************************************************
    //private boolean notify50Percent;
    // ** Stephen Code - END ***************************************************


    // -------------------------------------------------------------------------
    // A few different constructors?
    // -------------------------------------------------------------------------
    public MailSender(String recipients, boolean dontNotifyEveryUnstableBuild, boolean sendToIndividuals, String relevantDevelopers, boolean relevantOnly) {
    	this(recipients, dontNotifyEveryUnstableBuild, sendToIndividuals, relevantDevelopers, relevantOnly, "UTF-8");
    }
    public MailSender(String recipients, boolean dontNotifyEveryUnstableBuild, boolean sendToIndividuals, String relevantDevelopers, boolean relevantOnly, String charset) {
        this(recipients,dontNotifyEveryUnstableBuild,sendToIndividuals, relevantDevelopers, relevantOnly, charset, Collections.<AbstractProject>emptyList());
    }

    //------------------
    //This constructor used exclusively if the notify upstream commiters option is selected
    //------------------
    public MailSender(String recipients, boolean dontNotifyEveryUnstableBuild, boolean sendToIndividuals, String relevantDevelopers, boolean relevantOnly, String charset, Collection<AbstractProject> includeUpstreamCommitters) {
        this.recipients = Util.fixNull(recipients);
        this.dontNotifyEveryUnstableBuild = dontNotifyEveryUnstableBuild;
        this.sendToIndividuals = sendToIndividuals;
        this.relevantDevelopers = Util.fixNull(relevantDevelopers);
        this.relevantOnly = relevantOnly;
        this.charset = charset;
        this.includeUpstreamCommitters.addAll(includeUpstreamCommitters);
    }

    @Deprecated
    public boolean execute(AbstractBuild<?, ?> build, BuildListener listener) throws InterruptedException {
        run(build, listener);
        return true;
    }

    public final void run(Run<?,?> build, TaskListener listener) throws InterruptedException {
        try {
            MimeMessage mail = createMail(build, listener);
            if (mail != null) {
                // if the previous e-mail was sent for a success, this new e-mail
                // is not a follow up
                Run<?, ?> pb = build.getPreviousBuild();
                if(pb!=null && pb.getResult()==Result.SUCCESS) {
                    mail.removeHeader("In-Reply-To");
                    mail.removeHeader("References");
                }

                Address[] allRecipients = mail.getAllRecipients();
                if (allRecipients != null) {
                    StringBuilder buf = new StringBuilder("Sending e-mails to:");
                    for (Address a : allRecipients) {
                        if (a!=null) {
                            buf.append(' ').append(a);
                        }
                    }
                    listener.getLogger().println(buf);
                    Transport.send(mail);

                    build.addAction(new MailMessageIdAction(mail.getMessageID()));
                } else {
                    listener.getLogger().println(Messages.MailSender_ListEmpty());
                }
            }
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace(listener.error(e.getMessage()));
        }
    }


    // -------------------------------------------------------------------------
    // Getting the result of the previous build
    // -------------------------------------------------------------------------
    /**
     * To correctly compute the state change from the previous build to this build,
     * we need to ignore aborted builds.
     * See http://www.nabble.com/Losing-build-state-after-aborts--td24335949.html
     *
     * <p>
     * And since we are consulting the earlier result, if the previous build is still running, behave as if this were the first build.
     */
    private Result findPreviousBuildResult(Run<?,?> b) throws InterruptedException {
        do {
            b=b.getPreviousBuild();
            if (b == null || b.isBuilding()) {
                return null;
            }
        } while((b.getResult()==Result.ABORTED) || (b.getResult()==Result.NOT_BUILT));
        return b.getResult();
    }

    @Deprecated
    protected MimeMessage getMail(AbstractBuild<?, ?> build, BuildListener listener) throws MessagingException, UnsupportedEncodingException, InterruptedException {
        return createMail(build, listener);
    }

    protected @CheckForNull MimeMessage createMail(Run<?,?> build, TaskListener listener) throws MessagingException, UnsupportedEncodingException, InterruptedException {
        // In case getMail was overridden elsewhere. Cannot use Util.isOverridden since it only works on public methods.
        try {
            Method m = getClass().getDeclaredMethod("getMail", AbstractBuild.class, BuildListener.class);
            if (m.getDeclaringClass() != MailSender.class) { // so, overridden
                if (build instanceof AbstractBuild && listener instanceof BuildListener) {
                    return getMail((AbstractBuild) build, (BuildListener) listener);
                } else {
                    throw new AbstractMethodError("you must override createMail rather than getMail");
                }
            } // else using MailSender itself (or overridden in an intermediate superclass, too obscure to check)
        } catch (NoSuchMethodException x) {
            // non-deprecated subclass
        }

        // ** Stephen Code - START *********************************************
        createWeeklyReport(build, listener); // updating weekly report regardless of how the build did.  This way it is always updated
        // ** Stephen Code - END ***********************************************

        // ---------------------------------------------------------------------
        // Checking if the build FAILED
        // - will need to calculate failure percentage
        // - will need to figure out who failed it?
        // ---------------------------------------------------------------------
        if (build.getResult() == Result.FAILURE) {
            return createFailureMail(build, listener); // returning a failure email
        }
        // ---------------------------------------------------------------------
        // Checking if build is UNSTABLE
        // ---------------------------------------------------------------------
        if (build.getResult() == Result.UNSTABLE) {
            if (!dontNotifyEveryUnstableBuild)
                return createUnstableMail(build, listener);
            Result prev = findPreviousBuildResult(build);
            if (prev == Result.SUCCESS || prev == null)
                return createUnstableMail(build, listener);
        }
        // ---------------------------------------------------------------------
        // Checking if the build was a SUCCESS
        // ---------------------------------------------------------------------
        if (build.getResult() == Result.SUCCESS) {
            Result prev = findPreviousBuildResult(build);
            if (prev == Result.FAILURE)
                return createBackToNormalMail(build, Messages.MailSender_BackToNormal_Normal(), listener);
            if (prev == Result.UNSTABLE)
                return createBackToNormalMail(build, Messages.MailSender_BackToNormal_Stable(), listener);
        }



        return null;
    }

    // ** Stephen Code - START *****************************************************
    // -------------------------------------------------------------------------
    // Creating/Updating HTML Weekly Report File
    // -------------------------------------------------------------------------
    private void createWeeklyReport(Run<?, ?> build, TaskListener listener) throws MessagingException, UnsupportedEncodingException {

        listener.getLogger().println("\n[INFO] ------------------------------------------------------------------------");
        listener.getLogger().println("[INFO] Updating Weekly Report HTML File");
        listener.getLogger().println("[INFO] ------------------------------------------------------------------------");

        //----------------------------------------------------------------------
        // Declaring variables
        //----------------------------------------------------------------------
        int buildNumber; //the specific build number of the project
        String fullDisplayName; // the entire project name with build number
        ArrayList<String> weekJunit = new ArrayList<String>(10);
        ArrayList<String> weekJacoco = new ArrayList<String>(10);
        String filename; //path and name of HTML file to be created
        StringBuilder buf = new StringBuilder();

        // Figuring out build number
        fullDisplayName = getSubject(build, ""); // getting the full build name (i.e TestProject #31)
        buildNumber = Integer.parseInt(fullDisplayName.substring(fullDisplayName.indexOf('#')+1));
        listener.getLogger().println("[INFO] BUILD NUMBER: " + Integer.toString(buildNumber));

        // Figuring out URL of workspace
        String baseUrl = Mailer.descriptor().getUrl();
        String workspaceUrl = baseUrl + Util.encode(build.getParent().getUrl()) + "ws/";
        //filename = "/Users/Shared/Jenkins/Home/workspace/TestProject/report.html";
        filename = Messages.MailSender_reportFilePath();
        listener.getLogger().println("[INFO] FILE PATH: " + filename);

        // Prepping the data
        buf.append("data: [");

        //----------------------------------------------------------------------
        // Getting past week of JUnit test reports
        //----------------------------------------------------------------------
        for (int x=1; x<=7; x++) {
          weekJunit.add(Integer.toString(x));
          //filename = "/Users/Shared/Jenkins/Home/jobs/TestProject/builds/"+Integer.toString(buildNumber-x)+"/build.xml";
          filename = Messages.MailSender_pathToBuilds()+Integer.toString(buildNumber-x)+"/build.xml";
          listener.getLogger().println("[INFO] " + filename);
          try {
              File f = new File(filename);
          		BufferedReader b = new BufferedReader(new FileReader(f));
              String readLine = "";
              while ((readLine = b.readLine()) != null) {
              		int targetLine = readLine.indexOf("<failCount>");
              		if (targetLine != -1) {
              			//System.out.println(readLine);
              			CharSequence chr = readLine.subSequence(17,readLine.length()-12);
              			//System.out.println(chr.toString());
                    weekJunit.add(chr.toString());
                    listener.getLogger().println("[INFO] Build " + Integer.toString(buildNumber - x) + " had " + chr.toString() + " failures.");
                    buf.append(chr.toString());
                    buf.append(",");
              		}
              }
              b.close();
          } catch (IOException e) {
              //e.printStackTrace();
              listener.getLogger().println("[ERROR] Co8uld not find file: " + filename + ":" + e);
          }
        }
        buf.append("]");
        //----------------------------------------------------------------------
        // Getting past week of JaCoCo test reports
        //----------------------------------------------------------------------
        /*for (int x=1; x<=7; x++) {
          weekJacoco.add("10");
          weekJacoco.add("9");
          weekJacoco.add("8");
          weekJacoco.add("7");
          weekJacoco.add("6");
          weekJacoco.add("5");
          weekJacoco.add("4");
          weekJacoco.add("3");
          weekJacoco.add("2");
          weekJacoco.add("1");
        }*/

        //----------------------------------------------------------------------
        // Creating HTML file
        //----------------------------------------------------------------------
        //listener.getLogger().println(weekJunit);
        //filename = "/Users/Shared/Jenkins/Home/workspace/TestProject/report.html";
        filename = Messages.MailSender_reportFilePath();
        try {
          File file = new File(filename);
          FileWriter fr = null;
          fr = new FileWriter(file);

          /* HTML Document Setup */
          fr.write("<!doctype html>");
          fr.write("<html lang=\"en\">");

          /* Head */
          fr.write("<head>");
          // Meta
          fr.write("<meta charset=\"utf-8\">");
          fr.write("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1, shrink-to-fit=no\">");
          // Title
          fr.write("<title>Weekly Jenkins Report</title>");
          // Links
          fr.write("<link rel=\"stylesheet\" href=\"https://stackpath.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css\" integrity=\"sha384-9gVQ4dYFwwWSjIDZnLEWnxCjeSWFphJiwGPXr1jddIhOegiu1FwO5qRGvFXOdJZ4\" crossorigin=\"anonymous\">");
          // Style
          fr.write("<style>");
          fr.write("body {padding-top: 2rem;padding-bottom: 2rem;}h3 {margin-top: 2rem;}.row {margin-bottom: 1rem;}.row .row {margin-top: 1rem;margin-bottom: 0;}[class*=\"col-\"] {padding-top: 1rem;padding-bottom: 1rem;background-color: rgba(86, 61, 124, .15);border: 1px solid rgba(86, 61, 124, .2);}hr {margin-top: 2rem;margin-bottom: 2rem;}");
          fr.write("</style>");
          fr.write("</head>");

          /* Body */
          fr.write("<body>");
          fr.write("<div class=\"container\">");
          fr.write("<h1>Jenkins Weekly Report</h1><p class=\"lead\">Here is your weekly report for "+fullDisplayName+".</p>");
          fr.write("<h3>JUnit Test Results</h3>");
          fr.write("<p>The JUnit plugin provides a publisher that consumes XML test reports generated during the builds and provides some graphical visualization of the historical test results (see JUnit graph for a sample) as well as a web UI for viewing test reports, tracking failures, and so on.</p><p>Below are the failure results of the past seven builds.</p>");
          fr.write("<canvas id=\"junit\"></canvas>");
          //fr.write("<h3>JaCoCo Code Coverage Results</h3><p>This plugin allows you to capture code coverage report from JaCoCo. Jenkins will generate the trend report of coverage.</p>");
          //fr.write("<canvas id=\"jacoco\"></canvas>");
          fr.write("</div>");
          // Javascript
          fr.write("<script src=\"https://code.jquery.com/jquery-3.3.1.slim.min.js\" integrity=\"sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo\" crossorigin=\"anonymous\"></script>");
          fr.write("<script src=\"https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.0/umd/popper.min.js\" integrity=\"sha384-cs/chFZiN24E4KMATLdqdvsezGxaGsi4hLGOzlXwp5UZB1LY//20VyM2taTB4QvJ\" crossorigin=\"anonymous\"></script>");
          fr.write("<script src=\"https://stackpath.bootstrapcdn.com/bootstrap/4.1.0/js/bootstrap.min.js\" integrity=\"sha384-uefMccjFJAIv6A+rW+L4AHf99KvxDjWSu1z9VI8SKNVmz4sk7buKt/6v9KI65qnm\" crossorigin=\"anonymous\"></script>");
          fr.write("<script src=\"https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.7.1/Chart.min.js\"></script>");
          // Creating graphs
          fr.write("<script>");
          fr.write("var ctx1 = document.getElementById('junit').getContext('2d');");
          fr.write("var chart = new Chart(ctx1, {type: 'bar', data: {labels: [\"" + Integer.toString(buildNumber-6) + "\", \"" + Integer.toString(buildNumber-5) + "\", \"" + Integer.toString(buildNumber-4) + "\", \"" + Integer.toString(buildNumber-3) + "\", \"" + Integer.toString(buildNumber-2) + "\", \"" + Integer.toString(buildNumber-1) + "\", \"" + Integer.toString(buildNumber) + "\"], datasets: [{label: \"Test Failures\",backgroundColor: 'rgb(255, 99, 132)',borderColor: 'rgb(255, 99, 132)',"+buf.toString()+",}]},});");

          fr.write("var ctx2 = document.getElementById('jacoco').getContext('2d');");
          fr.write("var chart = new Chart(ctx2, {type: 'bar',data: {labels: [\"98\", \"99\", \"100\", \"101\", \"102\", \"103\", \"104\"],datasets: [{label: \"Test Failures\",backgroundColor: 'rgb(255, 99, 132)',borderColor: 'rgb(255, 99, 132)',data: [0, 10, 5, 2, 20, 30, 45],}]},});");
          fr.write("</script>");
          fr.write("</body>");
          fr.write("</html>");

          fr.close();

          listener.getLogger().println("[SUCCESS] File was updated!");

        } catch (IOException e) {
            listener.getLogger().println("[ERROR] HTML file could not be created: " + filename + " : " + e);
        }
        listener.getLogger().println("\n");
    }
    // ** Stephen Code - END *******************************************************

    private MimeMessage createBackToNormalMail(Run<?, ?> build, String subject, TaskListener listener) throws MessagingException, UnsupportedEncodingException {
        MimeMessage msg = createEmptyMail(build, listener);

        msg.setSubject(getSubject(build, Messages.MailSender_BackToNormalMail_Subject(subject)),charset);
        StringBuilder buf = new StringBuilder();
        appendBuildUrl(build, buf);
        msg.setText(buf.toString(),charset);

        return msg;
    }

    private static ChangeLogSet<? extends ChangeLogSet.Entry> getChangeSet(Run<?,?> build) {
        if (build instanceof AbstractBuild) {
            return ((AbstractBuild<?,?>) build).getChangeSet();
        } else {
            // TODO JENKINS-24141 call getChangeSets in general
            return ChangeLogSet.createEmpty(build);
        }
    }

    // -------------------------------------------------------------------------
    // Creating mail message for an UNSTABLE build
    // -------------------------------------------------------------------------
    private MimeMessage createUnstableMail(Run<?, ?> build, TaskListener listener) throws MessagingException, UnsupportedEncodingException {
        MimeMessage msg = createEmptyMail(build, listener); // starting with an empty mail message

        String subject = Messages.MailSender_UnstableMail_Subject();

        Run<?, ?> prev = build.getPreviousBuild();
        boolean still = false;
        if(prev!=null) {
            if(prev.getResult()==Result.SUCCESS)
                subject =Messages.MailSender_UnstableMail_ToUnStable_Subject();
            else if(prev.getResult()==Result.UNSTABLE) {
                subject = Messages.MailSender_UnstableMail_StillUnstable_Subject();
                still = true;
            }
        }

        msg.setSubject(getSubject(build, subject), charset);
        StringBuilder buf = new StringBuilder();
        DisplayURLProvider displayURLProvider = DisplayURLProvider.get();
        // Link to project changes summary for "still unstable" if this or last build has changes
        if (still && !(getChangeSet(build).isEmptySet() && getChangeSet(prev).isEmptySet())) {
            appendUrl(displayURLProvider.getChangesURL(build), buf);
        } else {
            appendBuildUrl(build, buf);
        }
        msg.setText(buf.toString(), charset);

        return msg;
    }

    private void appendBuildUrl(Run<?, ?> build, StringBuilder buf) {
        if (getChangeSet(build).isEmptySet()) {
            appendUrl(DisplayURLProvider.get().getRunURL(build), buf);
        } else {
            appendUrl(DisplayURLProvider.get().getChangesURL(build), buf);
        }
    }

    // ** Stephen Code - START *************************************************
    //--------------------------------------------------------------------------
    // Append header to the buffer
    //--------------------------------------------------------------------------
    private void appendHeader(Run<?, ?> build, StringBuilder buf, TaskListener listener) {
        buf.append("============================================================\n");
        buf.append("CS 498 Email Notification for ");
        buf.append(getSubject(build, " "));
        buf.append("\n============================================================\n");
        buf.append("PROJECT AT A GLANCE\n\n");
        listener.getLogger().println("[WEEKLY REPORT]" + Messages.MailSender_reportFilePath());
        buf.append('\n');
    }
    // ** Stephen Code - END ***************************************************

    private void appendUrl(String url, StringBuilder buf) {
        buf.append(Messages.MailSender_Link(url)).append("\n\n");
    }


    // -------------------------------------------------------------------------
    // Creating mail message for a FAILED build
    // -------------------------------------------------------------------------
    private MimeMessage createFailureMail(Run<?, ?> build, TaskListener listener) throws MessagingException, UnsupportedEncodingException, InterruptedException {
        MimeMessage msg = createEmptyMail(build, listener);

        msg.setSubject(getSubject(build, Messages.MailSender_FailureMail_Subject()),charset); // setting the subject

        StringBuilder buf = new StringBuilder();

        // ** Stephen Code - START *********************************************
        appendHeader(build, buf, listener);

        //----------------------------------------------------------------------
        // Figuring out the percentage of JUnit tests that failed
        //----------------------------------------------------------------------
        buf.append("* JUnit Fail Percentage: ");
        try {
          // Building URL to directory that contains JUnit report
          String baseUrl = Mailer.descriptor().getUrl();
          String workspaceUrl = baseUrl + Util.encode(build.getParent().getUrl()) + "ws/";
          String filename = workspaceUrl + "/target/surefire-reports/uky.cs498.AppTest.txt";

          // Opening URL into stream...replicating behavior as if it's being opened as a file
          URL oracle = new URL(filename);
          BufferedReader b = new BufferedReader(new InputStreamReader(oracle.openStream()));
          String readLine = "";

          // Reading through file line by line.
          while ((readLine = b.readLine()) != null) {

              // Looking for the specific line of the report file we want
              int targetLine = readLine.indexOf("Tests run:"); // using indexOf to see if the current line is the one wanted
          		if (targetLine != -1) {
          			String[] data = readLine.split(","); // breaking up line into array - it will always be comma delimited for each category
          			int testsRun = Integer.parseInt(data[0].substring(11)); // parsing string to get the integer value of the number of tests run
          			int testsFailed = Integer.parseInt(data[1].substring(11)); // parsing string to get the integer value of the number of tests that failed
          			float failurePercentage = ((float)testsFailed/testsRun) * 100; // determing percentage of tests failed

                buf.append(failurePercentage); // adding failure percentage to email


                  if (failurePercentage > Integer.parseInt(Messages.MailSender_failurePercentage())){
                    buf.append("    WARNING! Over ");
                    buf.append(Messages.MailSender_failurePercentage());
                    buf.append(" percent of JUnit tests failed!");
                  }
                
          		}
          }
        } catch (IOException e) {
            buf.append("ERROR! JUnit Report file could not be found");
        }

        //----------------------------------------------------------------------
        // Figuring out code coverage with JaCoCo
        //----------------------------------------------------------------------
        buf.append('\n');
        buf.append("* Current JaCoCo Code Coverage: ");
        try {
          String baseUrl = Mailer.descriptor().getUrl();
          String workspaceUrl = baseUrl + Util.encode(build.getParent().getUrl()) + "ws/";
          buf.append(workspaceUrl + "target/site/jacoco/index.html");
        } catch (Exception e) {
          buf.append("ERROR! JaCoCo Report files could not be found");
        }

        buf.append("\n\n");
        // ** Stephen Code - END ***********************************************

        appendBuildUrl(build, buf);

        boolean firstChange = true;
        for (ChangeLogSet.Entry entry : getChangeSet(build)) {
            if (firstChange) {
                firstChange = false;
                buf.append(Messages.MailSender_FailureMail_Changes()).append("\n\n");
            }
            buf.append('[');
            buf.append(entry.getAuthor().getFullName());
            buf.append("] ");
            String m = entry.getMsg();
            if (m!=null) {
                buf.append(m);
                if (!m.endsWith("\n")) {
                    buf.append('\n');
                }
            }

            buf.append('\n');
        }


        buf.append("----------------------------------------------------------\n");

        try {
            // Restrict max log size to avoid sending enormous logs over email.
            // Interested users can always look at the log on the web server.
            List<String> lines = build.getLog(MAX_LOG_LINES);

            String workspaceUrl = null, artifactUrl = null;
            Pattern wsPattern = null;
            String baseUrl = Mailer.descriptor().getUrl();
            if (baseUrl != null) {
                // Hyperlink local file paths to the repository workspace or build artifacts.
                // Note that it is possible for a failure mail to refer to a file using a workspace
                // URL which has already been corrected in a subsequent build. To fix, archive.
                workspaceUrl = baseUrl + Util.encode(build.getParent().getUrl()) + "ws/";
                artifactUrl = baseUrl + Util.encode(build.getUrl()) + "artifact/";
                FilePath ws = build instanceof AbstractBuild ? ((AbstractBuild) build).getWorkspace() : null;
                // Match either file or URL patterns, i.e. either
                // c:\hudson\workdir\jobs\foo\workspace\src\Foo.java
                // file:/c:/hudson/workdir/jobs/foo/workspace/src/Foo.java
                // will be mapped to one of:
                // http://host/hudson/job/foo/ws/src/Foo.java
                // http://host/hudson/job/foo/123/artifact/src/Foo.java
                // Careful with path separator between $1 and $2:
                // workspaceDir will not normally end with one;
                // workspaceDir.toURI() will end with '/' if and only if workspaceDir.exists() at time of call
                wsPattern = ws == null ? null : Pattern.compile("(" +
                    Pattern.quote(ws.getRemote()) + "|" + Pattern.quote(ws.toURI().toString()) + ")[/\\\\]?([^:#\\s]*)");
            }
            for (String line : lines) {
                line = line.replace('\0',' '); // shall we replace other control code? This one is motivated by http://www.nabble.com/Problems-with-NULL-characters-in-generated-output-td25005177.html
                if (wsPattern != null) {
                    // Perl: $line =~ s{$rx}{$path = $2; $path =~ s!\\\\!/!g; $workspaceUrl . $path}eg;
                    Matcher m = wsPattern.matcher(line);
                    int pos = 0;
                    while (m.find(pos)) {
                        String path = m.group(2).replace(File.separatorChar, '/');
                        String linkUrl = artifactMatches(path, (AbstractBuild) build) ? artifactUrl : workspaceUrl;
                        String prefix = line.substring(0, m.start()) + '<' + linkUrl + Util.encode(path) + '>';
                        pos = prefix.length();
                        line = prefix + line.substring(m.end());
                        // XXX better style to reuse Matcher and fix offsets, but more work
                        m = wsPattern.matcher(line);
                    }
                }
                buf.append(line);
                buf.append('\n');
            }
        } catch (IOException e) {
            // somehow failed to read the contents of the log
            buf.append(Messages.MailSender_FailureMail_FailedToAccessBuildLog()).append("\n\n").append(Functions.printThrowable(e));
        }

        buf.append('\n');

        // ** Stephen Code - START *********************************************
        buf.append("Disclaimer: This email message sent with the help of the Jenkins Mailer plugin.");
        // ** Stephen Code - END ***********************************************

        msg.setText(buf.toString(),charset);



        return msg;
    }

    // -------------------------------------------------------------------------
    // Creating an empty mail message
    // -------------------------------------------------------------------------
    private MimeMessage createEmptyMail(final Run<?, ?> run, final TaskListener listener) throws MessagingException, UnsupportedEncodingException {
        MimeMessageBuilder messageBuilder = new MimeMessageBuilder()
                .setCharset(charset)
                .setListener(listener);

        // TODO: I'd like to put the URL to the page in here,
        // but how do I obtain that?

        final AbstractBuild<?, ?> build = run instanceof AbstractBuild ? ((AbstractBuild<?, ?>)run) : null;

        // ** ------------ Aton code begin ------------ **
        StringTokenizer tokens = new StringTokenizer(recipients);

        if(relevantOnly){
            tokens = new StringTokenizer(relevantDevelopers);
        }

         // ** ------------ Aton code end ------------ **

        //StringTokenizer tokens = new StringTokenizer(recipients);
        while (tokens.hasMoreTokens()) {
            String address = tokens.nextToken();
            if (build != null && address.startsWith("upstream-individuals:") && !relevantOnly) {
                // people who made a change in the upstream
                String projectName = address.substring("upstream-individuals:".length());
                // TODO 1.590+ Jenkins.getActiveInstance
                final Jenkins jenkins = Jenkins.getInstance();
                if (jenkins == null) {
                    listener.getLogger().println("Jenkins is not ready. Cannot retrieve project "+projectName);
                    continue;
                }
                final AbstractProject up = jenkins.getItem(projectName, run.getParent(), AbstractProject.class);
                if(up==null) {
                    listener.getLogger().println("No such project exist: "+projectName);
                    continue;
                }
                messageBuilder.addRecipients(getCulpritsOfEmailList(up, build, listener));
            } else {
                // ordinary address
                messageBuilder.addRecipients(address);
            }
        }

        if (build != null) {
            for (AbstractProject project : includeUpstreamCommitters) {
                messageBuilder.addRecipients(getCulpritsOfEmailList(project, build, listener));
            }
            if (sendToIndividuals) {
                messageBuilder.addRecipients(getUserEmailList(listener, build));
            }
        }

        // set recipients after filtering out recipients that should not receive emails
        messageBuilder.setRecipientFilter(new MimeMessageBuilder.AddressFilter() {
            @Override
            public Set<InternetAddress> apply(Set<InternetAddress> recipients) {
                return MailAddressFilter.filterRecipients(run, listener, recipients);
            }
        });

        MimeMessage msg = messageBuilder.buildMimeMessage();

        msg.addHeader("X-Jenkins-Job", run.getParent().getFullName());

        final Result result = run.getResult();
        msg.addHeader("X-Jenkins-Result", result != null ? result.toString() : "in progress");

        Run<?, ?> pb = run.getPreviousBuild();
        if(pb!=null) {
            MailMessageIdAction b = pb.getAction(MailMessageIdAction.class);
            if(b!=null) {
                MimeMessageBuilder.setInReplyTo(msg, b.messageId);
            }
        }

        return msg;
    }

    String getCulpritsOfEmailList(AbstractProject upstreamProject, AbstractBuild<?, ?> currentBuild, TaskListener listener) throws AddressException, UnsupportedEncodingException {
        AbstractBuild<?,?> upstreamBuild = currentBuild.getUpstreamRelationshipBuild(upstreamProject);
        AbstractBuild<?,?> previousBuild = currentBuild.getPreviousBuild();
        AbstractBuild<?,?> previousBuildUpstreamBuild = previousBuild!=null ? previousBuild.getUpstreamRelationshipBuild(upstreamProject) : null;
        if(previousBuild==null && upstreamBuild==null && previousBuildUpstreamBuild==null) {
            listener.getLogger().println("Unable to compute the changesets in "+ upstreamProject +". Is the fingerprint configured?");
            return null;
        }
        if(previousBuild==null || upstreamBuild==null || previousBuildUpstreamBuild==null) {
            listener.getLogger().println("Unable to compute the changesets in "+ upstreamProject);
            return null;
        }
        AbstractBuild<?,?> b=previousBuildUpstreamBuild;

        StringBuilder culpritEmails = new StringBuilder();
        do {
            b = b.getNextBuild();
            if (b != null) {
                String userEmails = getUserEmailList(listener, b);
                if (culpritEmails.length() > 0) {
                    culpritEmails.append(",");
                }
                culpritEmails.append(userEmails);
            }
        } while ( b != upstreamBuild && b != null );

        return culpritEmails.toString();
    }

    /** If set, send to known users who lack {@link Item#READ} access to the job. */
    static /* not final */ boolean SEND_TO_USERS_WITHOUT_READ = Boolean.getBoolean(MailSender.class.getName() + ".SEND_TO_USERS_WITHOUT_READ");
    /** If set, send to unknown users. */
    static /* not final */ boolean SEND_TO_UNKNOWN_USERS = Boolean.getBoolean(MailSender.class.getName() + ".SEND_TO_UNKNOWN_USERS");

    @Nonnull
    String getUserEmailList(TaskListener listener, AbstractBuild<?, ?> build) throws AddressException, UnsupportedEncodingException {
        Set<User> users = build.getCulprits();
        StringBuilder userEmails = new StringBuilder();
        for (User a : users) {
            String adrs = Util.fixEmpty(a.getProperty(Mailer.UserProperty.class).getAddress());
            if(debug)
                listener.getLogger().println("  User "+a.getId()+" -> "+adrs);
            if (adrs != null) {
                if (Jenkins.getActiveInstance().isUseSecurity()) {
                    try {
                        Authentication auth = a.impersonate();
                        if (!build.getACL().hasPermission(auth, Item.READ)) {
                            if (SEND_TO_USERS_WITHOUT_READ) {
                                listener.getLogger().println(Messages.MailSender_warning_user_without_read(adrs, build.getFullDisplayName()));
                            } else {
                                listener.getLogger().println(Messages.MailSender_user_without_read(adrs, build.getFullDisplayName()));
                                continue;
                            }
                        }
                    } catch (UsernameNotFoundException x) {
                        if (SEND_TO_UNKNOWN_USERS) {
                            listener.getLogger().println(Messages.MailSender_warning_unknown_user(adrs));
                        } else {
                            listener.getLogger().println(Messages.MailSender_unknown_user(adrs));
                            continue;
                        }
                    }
                }
                if (userEmails.length() > 0) {
                    userEmails.append(",");
                }
                userEmails.append(adrs);
            } else {
                listener.getLogger().println(Messages.MailSender_NoAddress(a.getFullName()));
            }
        }
        return userEmails.toString();
    }

    private String getSubject(Run<?, ?> build, String caption) {
        return caption + ' ' + build.getFullDisplayName();
    }

    /**
     * Check whether a path (/-separated) will be archived.
     */
    protected boolean artifactMatches(String path, AbstractBuild<?, ?> build) {
        return false;
    }

    /**
     * Debug probe point to be activated by the scripting console.
     * @deprecated This hack may be removed in future versions
     */
    @SuppressFBWarnings(value = "MS_SHOULD_BE_FINAL",
            justification = "It may used for debugging purposes. We have to keep it for the sake of the binary copatibility")
    @Deprecated
    public static boolean debug = false;

    private static final int MAX_LOG_LINES = Integer.getInteger(MailSender.class.getName()+".maxLogLines",250);

}
