<p align="center"><img width=12.5% src="https://wiki.jenkins.io/download/attachments/2916393/logo.png?version=1&modificationDate=1302753947000&api=v2"></p>

## Basic Overview hello
A plugin for Jenkins to extend the capabilities of the built-in email service so that a customer will have more control over what types of emails are sent, when they are sent, and to whom they are sent.

## Installing
To install this plugin upload the ```CS498_Group3.hpi``` file.  Documentation on installing a plugin in this way can be found [here](https://github.com/stephen-ritchie/CS498_FinalProject/wiki/Packaging-up-a-plugin).  There are a few configuration settings that need to be made before the plugin can properly operated, and they are outlined below.
### Global Jenkins Configurations
For this plugin to function as expected, you must set up your email server within Jenkins as well as specifying the specific version of Maven you will want to use.  As mentioned, other versions of Maven *may* be substituted in place of what is described below.  However, unexpected behavior may occur in that case.
#### SMTP Email
This plugin requires access to a valid SMTP server to be able to sent emails.  Set this up by navigating to *Jenkins -> Manage Jenkins -> Configure System -> CS 498 | Email Notifications*.  The following fields are required to be filled out:
```
STMP server: <your email's server> (ex. Google's is smtp.gmail.com)
[x] Use SMTP Authentication: Checked
User Name: <youremail@whatever.com>
Password: <your email password>
[x] Use SSL: Checked (at least for Gmail)
Charset: UTF-8
```
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
