// CHECKSTYLE:OFF

package jenkins.plugins.mailer.tasks.i18n;

import org.jvnet.localizer.Localizable;
import org.jvnet.localizer.ResourceBundleHolder;


/**
 * Generated localization support class.
 * 
 */
@SuppressWarnings({
    "",
    "PMD",
    "all"
})
public class Messages {

    /**
     * The resource bundle reference
     * 
     */
    private final static ResourceBundleHolder holder = ResourceBundleHolder.get(Messages.class);

    /**
     * Key {@code MailSender.FailureMail.FailedToAccessBuildLog}: {@code
     * Failed to access build log}.
     * 
     * @return
     *     {@code Failed to access build log}
     */
    public static String MailSender_FailureMail_FailedToAccessBuildLog() {
        return holder.format("MailSender.FailureMail.FailedToAccessBuildLog");
    }

    /**
     * Key {@code MailSender.FailureMail.FailedToAccessBuildLog}: {@code
     * Failed to access build log}.
     * 
     * @return
     *     {@code Failed to access build log}
     */
    public static Localizable _MailSender_FailureMail_FailedToAccessBuildLog() {
        return new Localizable(holder, "MailSender.FailureMail.FailedToAccessBuildLog");
    }

    /**
     * Key {@code MailSender.BackToNormalMail.Subject}: {@code Jenkins build
     * is back to {0} :}.
     * 
     * @param arg0
     *      1st format parameter, {@code {0}}, as {@link String#valueOf(Object)}.
     * @return
     *     {@code Jenkins build is back to {0} :}
     */
    public static String MailSender_BackToNormalMail_Subject(Object arg0) {
        return holder.format("MailSender.BackToNormalMail.Subject", arg0);
    }

    /**
     * Key {@code MailSender.BackToNormalMail.Subject}: {@code Jenkins build
     * is back to {0} :}.
     * 
     * @param arg0
     *      1st format parameter, {@code {0}}, as {@link String#valueOf(Object)}.
     * @return
     *     {@code Jenkins build is back to {0} :}
     */
    public static Localizable _MailSender_BackToNormalMail_Subject(Object arg0) {
        return new Localizable(holder, "MailSender.BackToNormalMail.Subject", arg0);
    }

    /**
     * Key {@code MailSender.Link}: {@code See <{0}>}.
     * 
     * @param arg0
     *      1st format parameter, {@code {0}}, as {@link String#valueOf(Object)}.
     * @return
     *     {@code See <{0}>}
     */
    public static String MailSender_Link(Object arg0) {
        return holder.format("MailSender.Link", arg0);
    }

    /**
     * Key {@code MailSender.Link}: {@code See <{0}>}.
     * 
     * @param arg0
     *      1st format parameter, {@code {0}}, as {@link String#valueOf(Object)}.
     * @return
     *     {@code See <{0}>}
     */
    public static Localizable _MailSender_Link(Object arg0) {
        return new Localizable(holder, "MailSender.Link", arg0);
    }

    /**
     * Key {@code MailSender.UnstableMail.Subject}: {@code Jenkins build is
     * unstable:}.
     * 
     * @return
     *     {@code Jenkins build is unstable:}
     */
    public static String MailSender_UnstableMail_Subject() {
        return holder.format("MailSender.UnstableMail.Subject");
    }

    /**
     * Key {@code MailSender.UnstableMail.Subject}: {@code Jenkins build is
     * unstable:}.
     * 
     * @return
     *     {@code Jenkins build is unstable:}
     */
    public static Localizable _MailSender_UnstableMail_Subject() {
        return new Localizable(holder, "MailSender.UnstableMail.Subject");
    }

    /**
     * Key {@code MailSender.UnstableMail.StillUnstable.Subject}: {@code
     * Jenkins build is still unstable:}.
     * 
     * @return
     *     {@code Jenkins build is still unstable:}
     */
    public static String MailSender_UnstableMail_StillUnstable_Subject() {
        return holder.format("MailSender.UnstableMail.StillUnstable.Subject");
    }

    /**
     * Key {@code MailSender.UnstableMail.StillUnstable.Subject}: {@code
     * Jenkins build is still unstable:}.
     * 
     * @return
     *     {@code Jenkins build is still unstable:}
     */
    public static Localizable _MailSender_UnstableMail_StillUnstable_Subject() {
        return new Localizable(holder, "MailSender.UnstableMail.StillUnstable.Subject");
    }

    /**
     * Key {@code Mailer.TestMail.Content}: {@code This is test email #{0}
     * sent from {1}}.
     * 
     * @param arg1
     *      2nd format parameter, {@code {1}}, as {@link String#valueOf(Object)}.
     * @param arg0
     *      1st format parameter, {@code {0}}, as {@link String#valueOf(Object)}.
     * @return
     *     {@code This is test email #{0} sent from {1}}
     */
    public static String Mailer_TestMail_Content(Object arg0, Object arg1) {
        return holder.format("Mailer.TestMail.Content", arg0, arg1);
    }

    /**
     * Key {@code Mailer.TestMail.Content}: {@code This is test email #{0}
     * sent from {1}}.
     * 
     * @param arg1
     *      2nd format parameter, {@code {1}}, as {@link String#valueOf(Object)}.
     * @param arg0
     *      1st format parameter, {@code {0}}, as {@link String#valueOf(Object)}.
     * @return
     *     {@code This is test email #{0} sent from {1}}
     */
    public static Localizable _Mailer_TestMail_Content(Object arg0, Object arg1) {
        return new Localizable(holder, "Mailer.TestMail.Content", arg0, arg1);
    }

    /**
     * Key {@code MailSender.ListEmpty}: {@code An attempt to send an e-mail
     * to empty list of recipients, ignored.}.
     * 
     * @return
     *     {@code An attempt to send an e-mail to empty list of recipients,
     *     ignored.}
     */
    public static String MailSender_ListEmpty() {
        return holder.format("MailSender.ListEmpty");
    }

    /**
     * Key {@code MailSender.ListEmpty}: {@code An attempt to send an e-mail
     * to empty list of recipients, ignored.}.
     * 
     * @return
     *     {@code An attempt to send an e-mail to empty list of recipients,
     *     ignored.}
     */
    public static Localizable _MailSender_ListEmpty() {
        return new Localizable(holder, "MailSender.ListEmpty");
    }

    /**
     * Key {@code Mailer.Suffix.Error}: {@code This field should be ''@''
     * followed by a domain name.}.
     * 
     * @return
     *     {@code This field should be ''@'' followed by a domain name.}
     */
    public static String Mailer_Suffix_Error() {
        return holder.format("Mailer.Suffix.Error");
    }

    /**
     * Key {@code Mailer.Suffix.Error}: {@code This field should be ''@''
     * followed by a domain name.}.
     * 
     * @return
     *     {@code This field should be ''@'' followed by a domain name.}
     */
    public static Localizable _Mailer_Suffix_Error() {
        return new Localizable(holder, "Mailer.Suffix.Error");
    }

    /**
     * Key {@code Mailer.Unknown.Host.Name}: {@code Unknown host name:}.
     * 
     * @return
     *     {@code Unknown host name:}
     */
    public static String Mailer_Unknown_Host_Name() {
        return holder.format("Mailer.Unknown.Host.Name");
    }

    /**
     * Key {@code Mailer.Unknown.Host.Name}: {@code Unknown host name:}.
     * 
     * @return
     *     {@code Unknown host name:}
     */
    public static Localizable _Mailer_Unknown_Host_Name() {
        return new Localizable(holder, "Mailer.Unknown.Host.Name");
    }

    /**
     * Key {@code Mailer.EmailSentSuccessfully}: {@code Email was
     * successfully sent}.
     * 
     * @return
     *     {@code Email was successfully sent}
     */
    public static String Mailer_EmailSentSuccessfully() {
        return holder.format("Mailer.EmailSentSuccessfully");
    }

    /**
     * Key {@code Mailer.EmailSentSuccessfully}: {@code Email was
     * successfully sent}.
     * 
     * @return
     *     {@code Email was successfully sent}
     */
    public static Localizable _Mailer_EmailSentSuccessfully() {
        return new Localizable(holder, "Mailer.EmailSentSuccessfully");
    }

    /**
     * Key {@code Mailer.TestMail.Subject}: {@code Test email #{0}}.
     * 
     * @param arg0
     *      1st format parameter, {@code {0}}, as {@link String#valueOf(Object)}.
     * @return
     *     {@code Test email #{0}}
     */
    public static String Mailer_TestMail_Subject(Object arg0) {
        return holder.format("Mailer.TestMail.Subject", arg0);
    }

    /**
     * Key {@code Mailer.TestMail.Subject}: {@code Test email #{0}}.
     * 
     * @param arg0
     *      1st format parameter, {@code {0}}, as {@link String#valueOf(Object)}.
     * @return
     *     {@code Test email #{0}}
     */
    public static Localizable _Mailer_TestMail_Subject(Object arg0) {
        return new Localizable(holder, "Mailer.TestMail.Subject", arg0);
    }

    /**
     * Key {@code MailSender.FailureMail.Subject}: {@code Build failed in
     * Jenkins:}.
     * 
     * @return
     *     {@code Build failed in Jenkins:}
     */
    public static String MailSender_FailureMail_Subject() {
        return holder.format("MailSender.FailureMail.Subject");
    }

    /**
     * Key {@code MailSender.FailureMail.Subject}: {@code Build failed in
     * Jenkins:}.
     * 
     * @return
     *     {@code Build failed in Jenkins:}
     */
    public static Localizable _MailSender_FailureMail_Subject() {
        return new Localizable(holder, "MailSender.FailureMail.Subject");
    }

    /**
     * Key {@code MailSender.BackToNormal.Stable}: {@code stable}.
     * 
     * @return
     *     {@code stable}
     */
    public static String MailSender_BackToNormal_Stable() {
        return holder.format("MailSender.BackToNormal.Stable");
    }

    /**
     * Key {@code MailSender.BackToNormal.Stable}: {@code stable}.
     * 
     * @return
     *     {@code stable}
     */
    public static Localizable _MailSender_BackToNormal_Stable() {
        return new Localizable(holder, "MailSender.BackToNormal.Stable");
    }

    /**
     * Key {@code MailSender.BackToNormal.Normal}: {@code normal}.
     * 
     * @return
     *     {@code normal}
     */
    public static String MailSender_BackToNormal_Normal() {
        return holder.format("MailSender.BackToNormal.Normal");
    }

    /**
     * Key {@code MailSender.BackToNormal.Normal}: {@code normal}.
     * 
     * @return
     *     {@code normal}
     */
    public static Localizable _MailSender_BackToNormal_Normal() {
        return new Localizable(holder, "MailSender.BackToNormal.Normal");
    }

    /**
     * Key {@code MailSender.NoAddress}: {@code Failed to send e-mail to {0}
     * because no e-mail address is known, and no default e-mail domain is
     * configured}.
     * 
     * @param arg0
     *      1st format parameter, {@code {0}}, as {@link String#valueOf(Object)}.
     * @return
     *     {@code Failed to send e-mail to {0} because no e-mail address is
     *     known, and no default e-mail domain is configured}
     */
    public static String MailSender_NoAddress(Object arg0) {
        return holder.format("MailSender.NoAddress", arg0);
    }

    /**
     * Key {@code MailSender.NoAddress}: {@code Failed to send e-mail to {0}
     * because no e-mail address is known, and no default e-mail domain is
     * configured}.
     * 
     * @param arg0
     *      1st format parameter, {@code {0}}, as {@link String#valueOf(Object)}.
     * @return
     *     {@code Failed to send e-mail to {0} because no e-mail address is
     *     known, and no default e-mail domain is configured}
     */
    public static Localizable _MailSender_NoAddress(Object arg0) {
        return new Localizable(holder, "MailSender.NoAddress", arg0);
    }

    /**
     * Key {@code Mailer.UserProperty.DisplayName}: {@code E-mail}.
     * 
     * @return
     *     {@code E-mail}
     */
    public static String Mailer_UserProperty_DisplayName() {
        return holder.format("Mailer.UserProperty.DisplayName");
    }

    /**
     * Key {@code Mailer.UserProperty.DisplayName}: {@code E-mail}.
     * 
     * @return
     *     {@code E-mail}
     */
    public static Localizable _Mailer_UserProperty_DisplayName() {
        return new Localizable(holder, "Mailer.UserProperty.DisplayName");
    }

    /**
     * Key {@code Mailer.FailedToSendEmail}: {@code Failed to send out
     * e-mail}.
     * 
     * @return
     *     {@code Failed to send out e-mail}
     */
    public static String Mailer_FailedToSendEmail() {
        return holder.format("Mailer.FailedToSendEmail");
    }

    /**
     * Key {@code Mailer.FailedToSendEmail}: {@code Failed to send out
     * e-mail}.
     * 
     * @return
     *     {@code Failed to send out e-mail}
     */
    public static Localizable _Mailer_FailedToSendEmail() {
        return new Localizable(holder, "Mailer.FailedToSendEmail");
    }

    /**
     * Key {@code MailSender.unknown_user}: {@code Not sending mail to
     * unregistered user {0}}.
     * 
     * @param arg0
     *      1st format parameter, {@code {0}}, as {@link String#valueOf(Object)}.
     * @return
     *     {@code Not sending mail to unregistered user {0}}
     */
    public static String MailSender_unknown_user(Object arg0) {
        return holder.format("MailSender.unknown_user", arg0);
    }

    /**
     * Key {@code MailSender.unknown_user}: {@code Not sending mail to
     * unregistered user {0}}.
     * 
     * @param arg0
     *      1st format parameter, {@code {0}}, as {@link String#valueOf(Object)}.
     * @return
     *     {@code Not sending mail to unregistered user {0}}
     */
    public static Localizable _MailSender_unknown_user(Object arg0) {
        return new Localizable(holder, "MailSender.unknown_user", arg0);
    }

    /**
     * Key {@code Mailer.DisplayName}: {@code CS498 | E-mail Notification}.
     * 
     * @return
     *     {@code CS498 | E-mail Notification}
     */
    public static String Mailer_DisplayName() {
        return holder.format("Mailer.DisplayName");
    }

    /**
     * Key {@code Mailer.DisplayName}: {@code CS498 | E-mail Notification}.
     * 
     * @return
     *     {@code CS498 | E-mail Notification}
     */
    public static Localizable _Mailer_DisplayName() {
        return new Localizable(holder, "Mailer.DisplayName");
    }

    /**
     * Key {@code MailSender.FailureMail.Changes}: {@code Changes:}.
     * 
     * @return
     *     {@code Changes:}
     */
    public static String MailSender_FailureMail_Changes() {
        return holder.format("MailSender.FailureMail.Changes");
    }

    /**
     * Key {@code MailSender.FailureMail.Changes}: {@code Changes:}.
     * 
     * @return
     *     {@code Changes:}
     */
    public static Localizable _MailSender_FailureMail_Changes() {
        return new Localizable(holder, "MailSender.FailureMail.Changes");
    }

    /**
     * Key {@code MailSender.warning_unknown_user}: {@code Warning: {0} is
     * not a recognized user, but sending mail anyway}.
     * 
     * @param arg0
     *      1st format parameter, {@code {0}}, as {@link String#valueOf(Object)}.
     * @return
     *     {@code Warning: {0} is not a recognized user, but sending mail anyway}
     */
    public static String MailSender_warning_unknown_user(Object arg0) {
        return holder.format("MailSender.warning_unknown_user", arg0);
    }

    /**
     * Key {@code MailSender.warning_unknown_user}: {@code Warning: {0} is
     * not a recognized user, but sending mail anyway}.
     * 
     * @param arg0
     *      1st format parameter, {@code {0}}, as {@link String#valueOf(Object)}.
     * @return
     *     {@code Warning: {0} is not a recognized user, but sending mail anyway}
     */
    public static Localizable _MailSender_warning_unknown_user(Object arg0) {
        return new Localizable(holder, "MailSender.warning_unknown_user", arg0);
    }

    /**
     * Key {@code MailSender.UnstableMail.ToUnStable.Subject}: {@code Jenkins
     * build became unstable:}.
     * 
     * @return
     *     {@code Jenkins build became unstable:}
     */
    public static String MailSender_UnstableMail_ToUnStable_Subject() {
        return holder.format("MailSender.UnstableMail.ToUnStable.Subject");
    }

    /**
     * Key {@code MailSender.UnstableMail.ToUnStable.Subject}: {@code Jenkins
     * build became unstable:}.
     * 
     * @return
     *     {@code Jenkins build became unstable:}
     */
    public static Localizable _MailSender_UnstableMail_ToUnStable_Subject() {
        return new Localizable(holder, "MailSender.UnstableMail.ToUnStable.Subject");
    }

    /**
     * Key {@code MailSender.warning_user_without_read}: {@code Warning: user
     * {0} has no permission to view {1}, but sending mail anyway}.
     * 
     * @param arg1
     *      2nd format parameter, {@code {1}}, as {@link String#valueOf(Object)}.
     * @param arg0
     *      1st format parameter, {@code {0}}, as {@link String#valueOf(Object)}.
     * @return
     *     {@code Warning: user {0} has no permission to view {1}, but sending
     *     mail anyway}
     */
    public static String MailSender_warning_user_without_read(Object arg0, Object arg1) {
        return holder.format("MailSender.warning_user_without_read", arg0, arg1);
    }

    /**
     * Key {@code MailSender.warning_user_without_read}: {@code Warning: user
     * {0} has no permission to view {1}, but sending mail anyway}.
     * 
     * @param arg1
     *      2nd format parameter, {@code {1}}, as {@link String#valueOf(Object)}.
     * @param arg0
     *      1st format parameter, {@code {0}}, as {@link String#valueOf(Object)}.
     * @return
     *     {@code Warning: user {0} has no permission to view {1}, but sending
     *     mail anyway}
     */
    public static Localizable _MailSender_warning_user_without_read(Object arg0, Object arg1) {
        return new Localizable(holder, "MailSender.warning_user_without_read", arg0, arg1);
    }

    /**
     * Key {@code MailSender.user_without_read}: {@code Not sending mail to
     * user {0} with no permission to view {1}}.
     * 
     * @param arg1
     *      2nd format parameter, {@code {1}}, as {@link String#valueOf(Object)}.
     * @param arg0
     *      1st format parameter, {@code {0}}, as {@link String#valueOf(Object)}.
     * @return
     *     {@code Not sending mail to user {0} with no permission to view {1}}
     */
    public static String MailSender_user_without_read(Object arg0, Object arg1) {
        return holder.format("MailSender.user_without_read", arg0, arg1);
    }

    /**
     * Key {@code MailSender.user_without_read}: {@code Not sending mail to
     * user {0} with no permission to view {1}}.
     * 
     * @param arg1
     *      2nd format parameter, {@code {1}}, as {@link String#valueOf(Object)}.
     * @param arg0
     *      1st format parameter, {@code {0}}, as {@link String#valueOf(Object)}.
     * @return
     *     {@code Not sending mail to user {0} with no permission to view {1}}
     */
    public static Localizable _MailSender_user_without_read(Object arg0, Object arg1) {
        return new Localizable(holder, "MailSender.user_without_read", arg0, arg1);
    }

}
