<p align="center"><img width=12.5% src="https://wiki.jenkins.io/download/attachments/2916393/logo.png?version=1&modificationDate=1302753947000&api=v2"></p>

# CS 498 Email Notification Plugin

## Basic Overview
Jenkin is an open source automation server which can be used to automate all sorts of tasks related to building, testing, and delivering or deploying software.  This plugin is designed to allow configuration and customization of email notifications that can be triggered automatically based upon the build status of a specific job running within Jenkins.

The remainder of this document contains relevant information and documentation to get this plugin installe, configured, and working within an exisiting Jenkins server.  This plugin is designed to be used in tandem with the [JUnit Plugin](https://wiki.jenkins.io/display/JENKINS/JUnit+Plugin) for test case reporting, and the [JaCoCo Plugin](https://wiki.jenkins.io/display/JENKINS/JaCoCo+Plugin) for code coverage reporting.  While not within the scope of this project, documentation for installing and setting up these plugins is included for proper testing purposes.

## Requirements
This plugin was developed and tested with the software listed below.  Using untested software with this plugin may lead to unexpected or incorrect behavior.
```
Jenkins ver. 2.89.3
Maven 3.5.3
Java 1.8.0_162
```

## Installing
This plugin will need to be manually installed into your Jenkins server via an .hpi file.  This file can be found at ```/Plugin/target/CS498_Group3.hpi```.  Documentation on installing a plugin in this way can be found [here](https://github.com/stephen-ritchie/CS498_FinalProject/wiki/Packaging-up-a-plugin).  There are a few configuration settings that need to be made before the plugin can properly operated, and they are outlined below.
### Global Configurations
The two items outlined below are Jenkins systems settings that are set at the global level, and are required for this plugin to operate correctly.  The first item to be configured is giving Jenkins access to an SMTP server, and the second item is defining a specific version of Maven to be used by Jenkins.
#### SMTP Email
To send an email via Jenkins authorized access to a valid SMTP server is required.  Set this up by navigating to *Jenkins -> Manage Jenkins -> Configure System -> CS 498 | Email Notifications*.  For testing of this plugin a Google gmail account was used as the SMTP server, and the specific configuration used is outlined below.
* STMP server: <your email's server> (ex. Google's is smtp.gmail.com)
* Use SMTP Authentication: [x] Checked
* User Name: <youremail@whatever.com>
* Password: <your email password>
* Use SSL: [x] Checked (at least for Gmail)
* Charset: UTF-8

#### Maven
Maven is a software project management and comprehension tool.  Official documentation on Maven can be found [here](https://maven.apache.org).  Because this plugin is designed to be used with Java development, Maven can be integrated into the project to assist with building, testing, and packaging source code.  This plugin was tested using Maven 3.5.3 and steps for setting this up in Jenkins are outlined below.  Alternative versions of Maven *may* be used, but plugin behavior cannot be guaranteed if another version of Maven is used.

Setting up Maven within Jenkins is relatively straightforward, and can be done by navigating to *Jenkins -> Manage Jenkins -> Global Tool Configuration -> Maven*.  The following fields need to be filled out for this plugin to function as expected.
* Name: <anything> (I recommend just naming it Maven 3.5.3)
* Install automatically: [x] Checked
* Install from Apache Version: 3.5.3
  
### Build Specific Configurations
Now that Jenkins has been configured at the global level we need to configure a specific build within Jenkins to use our plugin.  This boils down to setting up build actions to create all the necessary report files that our plugin is looking for.  Each time a build is initiatied, these actions will be run automatically by Jenkins.
#### Build: Invoke top-level Maven targets
As mentioned above, Maven is used for software project management and comprehension tools.  By running Maven as a first step with a goal of 'install' it compiles the build, runs JUnit, and runs JaCoCo as well.  JUnit and JaCoCo both produce a mixture of txt, html, and xml files that will be parsed by our plugin for report analytics.  The goal of 'clean' is appended to the Maven command to clean out any old report files so old data is not mistaken for current data. You will want to configure this build option as shown below.

<p align="center"><img src="https://github.com/stephen-ritchie/CS498_FinalProject/blob/Stephen/img/maven.png"></p>

#### Post-build Actions: Publish JUnit test result report
After the build has run, Jenkins has the ability to initiatiate post build actions.  One of these actions is to publish the results of the JUnit tests. You will want to configure this build option as shown below.

<p align="center"><img src="https://github.com/stephen-ritchie/CS498_FinalProject/blob/Stephen/img/junit.png"></p>

#### Post-build Actions: Record JaCoCo coverage report
The final report that our plugin analyzes is code coverage via JaCoCo.  The JaCoCo plugin runs code coverage on a build, and publishes the results as a set of navigatable HTML files.  You will want to configure this build option as shown below.

<p align="center"><img src="https://github.com/stephen-ritchie/CS498_FinalProject/blob/Stephen/img/jacoco.png"></p>

#### Post-build Actions: CS498 | Email Notification
This is the UI of our actual plugin.  It is built off the UI of the default Mailer Plugin, but has been extended with extra inputs and options.  As shown below, some additonal UI elements are a text input for Relevant Developers and additional checkbox options for emails.  This pane can be configured as you wish, but an example is shown below.

<p align="center"><img src="https://github.com/stephen-ritchie/CS498_FinalProject/blob/Stephen/img/email.png"></p>

## Known Bugs
Jenkins has proven to be a difficult animal to tame.  Being open-source but not widely used has lead to a relatively small niche community maintaining it.  Working through this project we have found that there are many bugs and legacy support issues, many which still have not been resolved.  For example, in reading through comments in the Mailer Plugin from a contributor I found this.
> Not really sure what's going on here... seems like it's a patch maybe?

This has lead to us having a few bugs in our plugin.  Some are the result of us personally not having enough knowledge of Jenkins, but other bugs are a result of limitations or features of the Jenkins source code.  These bugs are being tracked via our [Issues Page](https://github.com/stephen-ritchie/CS498_FinalProject/issues).  Please refer to it for currently known bugs.

## Contributors
Stephen Ritchie

Atanas Golev

Grace Oparinde

Joshua Ray
