<p align="center"><img width=12.5% src="https://wiki.jenkins.io/download/attachments/2916393/logo.png?version=1&modificationDate=1302753947000&api=v2"></p>

## Basic Overview
Jenkin is an open source automation server which can be used to automate all sorts of tasks related to building, testing, and delivering or deploying software.  This plugin is designed to allow configuration and customization of email notifications that can be triggered automatically based upon the build status of a specific job running within Jenkins.

The remainder of this document contains relevant information and documentation to get this plugin installe, configured, and working within an exisiting Jenkins server.  This plugin is designed to be used in tandem with the [JUnit Plugin](https://wiki.jenkins.io/display/JENKINS/JUnit+Plugin) for test case reporting, and the [JaCoCo Plugin](https://wiki.jenkins.io/display/JENKINS/JaCoCo+Plugin) for code coverage reporting.  While not within the scope of this project, documentation for installing and setting up these plugins is included for proper testing purposes.

## Installing
This plugin will need to be manually installed into your Jenkins server via an .hpi file.  This file can be found at ```/Plugin/target/CS498_Group3.hpi```.  Documentation on installing a plugin in this way can be found [here](https://github.com/stephen-ritchie/CS498_FinalProject/wiki/Packaging-up-a-plugin).  There are a few configuration settings that need to be made before the plugin can properly operated, and they are outlined below.
### Global Configurations
For this plugin to function as expected, you must set up your email server within Jenkins as well as specifying the specific version of Maven you will want to use.  As mentioned, other versions of Maven *may* be substituted in place of what is described below.  However, unexpected behavior may occur in that case.
#### SMTP Email
To send an email via Jenkins authorized access to a valid SMTP server is required.  Set this up by navigating to *Jenkins -> Manage Jenkins -> Configure System -> CS 498 | Email Notifications*.  For testing of this plugin a Google gmail account was used as the SMTP server, and the specific configuration used is outlined below.
* STMP server: <your email's server> (ex. Google's is smtp.gmail.com)
* Use SMTP Authentication: [x] Checked
* User Name: <youremail@whatever.com>
* Password: <your email password>
* Use SSL: [x] Checked (at least for Gmail)
* Charset: UTF-8

#### Maven
This plugin utilized ```Maven 3.5.3```. Set this up by navigating to *Jenkins -> Manage Jenkins -> Global Tool Configuration -> Maven*.  The following fields are required to be filled out:
```
Name: <anything> (I recommend just naming it Maven 3.5.3)
[x] Install automatically
Install from Apache Version: 3.5.3
```

### Build Specific Configurations
#### Build: Invoke top-level Maven targets
```
Maven Version: 3.5.3
Goals: clean install
```
#### Post-build Actions: Publish JUnit test result report
```
Test report XMLs: **/target/surefire-reports/*.xml
Health report amplification indicator: 1.0
```
#### Post-build Actions: Record JaCoCo coverage report
```
Path to exec files: **/**.exec
Path to class directories: **/classes/uky/cs498
Path to source directories: **/src/main/java/uky/cs498
```
#### Post-build Actions: CS498 | Email Notification

## Requirements
This plugin was developed and tested with the software listed below.  Using untested software with this plugin may lead to unexpected or incorrect behavior.
```
Jenkins ver. 2.89.3
Maven 3.5.3
Java 1.8.0_162
```
## Contributors
Stephen Ritchie

Atanas Golev

Grace Oparinde

Joshua Ray
