<p align="center"><img width=12.5% src="https://wiki.jenkins.io/download/attachments/2916393/logo.png?version=1&modificationDate=1302753947000&api=v2"></p>

# Basic Overview
A plugin for Jenkins to extend the capabilities of the built-in email service so that a customer will have more control over what types of emails are sent, when they are sent, and to whom they are sent.
# Key Features
Receive email alerts on the following conditions...
## Error percentage is over 50%
## Anytime anyone uploads changes
## Notified of all line numbers that have been changed since last build
## Select projects to be notified about and ones to be muted
## Send emails to everyone or only relevant developers
## Let user add email to mailing list
## Select percentage threshold for errors to be notified about
## Notified of which lines numbers cause errors
## Progress report each week
For this I'd say we have a function that just check the datetime each time a build is run.  If the datetime is what we want, it creates and sends the progress report, otherwise not worry about it.
## Notified of how code coverage changes after each change
## Congratulations to team when code coverage reaches 100%
So each time a 'build' runs it looks like we can run our own shell scripts.  If this is the case, we could have gcov get run each time, and if we figure out how to get jenkins to read the output from the console we can get access to the code coverage.
# Installing
# Examples
# Contributors
Atanas Golev<br>
Grace Oparinde<br>
Joshua Ray<br>
Stephen Ritchie<br>
