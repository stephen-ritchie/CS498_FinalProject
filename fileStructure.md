

### MailCommand.java
Actually sends e-mail after it's been constructed using Hudson
```src/main/java/hudson/cli/MailCommand.java```
**Do not edit**

### MailAddressResolver.java
Infers e-mail addresses for the user when none is specified.
```src/main/java/hudson/tasks/MailAddressResolver.java```
**Do not edit**

### Mailer.java
Also sends emails? Need to look at this again.
```src/main/java/hudson/tasks/Mailer.java```

### MailMessageIdAction.java
Allows updates to be sent as replies to an original message and not as new emails.  It may or may not work.
```src/main/java/hudson/tasks/MailMessageIdAction.java```
**Do not edit**

### MailSender.java
Core logic of sending out notification e-mail. This is probably where a lot of our code will end up going.
```src/main/java/hudson/tasks/MailSender.java```

### PluginImpl.java
Appears to just be a patch for something...nothing big
```src/main/java/hudson/tasks/PluginImpl.java```
**Do not edit**
