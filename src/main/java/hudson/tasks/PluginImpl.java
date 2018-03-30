package hudson.tasks;

// -----------------------------------------------------------------------------
// Importing things...
// -----------------------------------------------------------------------------
import hudson.Plugin;
import java.util.Arrays;

// -----------------------------------------------------------------------------
// Not really sure what's going on here... seems like it's a patch maybe?
// -----------------------------------------------------------------------------
public class PluginImpl extends Plugin {
    static {
        // Fix JENKINS-9006
        // When sending to multiple recipients, send to valid recipients even if some are
        // invalid, unless we have explicitly said otherwise.
        for (String property : Arrays.asList("mail.smtp.sendpartial", "mail.smtps.sendpartial")) {
            if (System.getProperty(property) == null) {
                System.setProperty(property, "true");
            }
        }
    }
}
