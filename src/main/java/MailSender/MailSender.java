package MailSender;

import Model.LogSysEvent;
import Model.MailTemplate;
import Model.MailTemplates;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.util.List;
import java.util.Properties;

public class MailSender {

    private static final String MAIL_SETTINGS_FILE = "app.properties";
    private static String LOGANALYZER_LOG_LINK_TEMPLATE="http://172.172.174.100/loganalyzer/details.php?uid=";
    private static final String MAIL_TEMPLATE_RECIPIENTS = "log_mail_recipient.xml";
    private static Properties mailProperties = new Properties();

    public static void sendMailToRecipient(List<MailTemplate> mailTemplateList, LogSysEvent logSysEvent) {
        if (mailTemplateList != null) {
            for (MailTemplate mailTemplate : mailTemplateList) {
                if (mailTemplate.getLogName().toLowerCase().contains(logSysEvent.getSysLogTag().toLowerCase())) {
                    for (String recipient : mailTemplate.getRecipients()) {
                        sendMail(recipient, logSysEvent.getMessage(), logSysEvent.getSysLogTag(), logSysEvent.getId());
                    }
                }
            }
        }
    }

    private static Properties readEmailSettings() {
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(MAIL_SETTINGS_FILE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            mailProperties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mailProperties;
    }

    private static void sendMail(String recipientMail, String messgage, String programName, int id) {
        mailProperties = readEmailSettings();
        Session session = Session.getDefaultInstance(mailProperties,
                new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(mailProperties
                                .getProperty("mail.smtp.user"), mailProperties
                                .getProperty("mail.smtp.password"));
                    }
                });

        MimeMessage mimeMessage = new MimeMessage(session);
        try {
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(recipientMail));
            mimeMessage.setSubject(programName);
            mimeMessage.setText(LOGANALYZER_LOG_LINK_TEMPLATE + id + "\n" + messgage);
            Transport.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public static List<MailTemplate> readMailTemplate() {
        List<MailTemplate> mailTemplateList = null;
        try {
            File mailTemplateXml = new File(MAIL_TEMPLATE_RECIPIENTS);
            JAXBContext jaxbContext = JAXBContext.newInstance(MailTemplates.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            mailTemplateList = ((MailTemplates)unmarshaller.unmarshal(mailTemplateXml)).getMailTemplate();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return mailTemplateList;
    }
}
