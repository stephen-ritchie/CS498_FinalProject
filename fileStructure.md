

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

### MailAddressFilter.java
Checks email addresses if they should be excluded from sent emails.  I don't know yet how this works, but it could come in handy.
```src/main/java/jenkins/plugins/mailer/tasks/MailAddressFilter.java```

### MailAddressFilterDescriptor.java
Idk, there's really nothing here.  Maybe this is where we put logic for determining which emails to filter out? Let's leave it alone for now.
```src/main/java/jenkins/plugins/mailer/tasks/MailAddressFilterDescriptor.java```

**Do not edit**

### MimeMessageBuilder.java
This looks like the place where the email is actually assembled from its parts.  Probably will need to edit things here.
```src/main/java/jenkins/plugins/mailer/tasks/MimeMessageBuilder.java```
