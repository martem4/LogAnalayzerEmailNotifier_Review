package com.century.logssender.mail;

import lombok.SneakyThrows;
import com.century.logssender.model.LogEvent;
import com.century.logssender.xml.MailTemplate;
import com.century.logssender.xml.MailTemplates;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.util.List;
import java.util.Properties;

@Service
public class MailService {

    private static final String MAIL_SETTINGS_FILE = "app.properties";
    private static final String LOG_ANALYZER_LOG_LINK_TEMPLATE ="http://172.172.174.100/loganalyzer/details.php?uid=";
    private static final String MAIL_TEMPLATE_RECIPIENTS = "log_mail_recipient.xml";

    private Properties mailProperties = new Properties();
    private List<MailTemplate> mailTemplates;

    public MailService() {
        readMailTemplates();
        readEmailSettings();
    }

    public void sendLogMailForSelectedTags(LogEvent logEvent) {
        if (mailTemplates != null) {
            for (MailTemplate mailTemplate : mailTemplates) {
                if (mailTemplate.getLogName().toLowerCase().contains(logEvent.getSysLogTag().toLowerCase())) {
                    sendMail(logEvent.getMessage(), logEvent.getSysLogTag(), logEvent.getId(), mailTemplate.getRecipients());
                }
            }
        }
    }

    @SneakyThrows
    private void readMailTemplates() {
        File mailTemplateXml = new File(MAIL_TEMPLATE_RECIPIENTS);
        JAXBContext jaxbContext = JAXBContext.newInstance(MailTemplates.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        mailTemplates = ((MailTemplates) unmarshaller.unmarshal(mailTemplateXml)).getMailTemplate();
    }

    private void readEmailSettings() {
        try (FileInputStream inputStream =
                     new FileInputStream(MAIL_SETTINGS_FILE)){
            mailProperties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMail(String message, String programName, int id, List<String> recipientsList) {
        Session session = getMailingSession();

        MimeMessage mimeMessage = new MimeMessage(session);
        send(message, programName, id, recipientsList, mimeMessage);
    }

    private Session getMailingSession() {
        return Session.getDefaultInstance(mailProperties,
                    new Authenticator() {
                        @Override
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(mailProperties
                                    .getProperty("mail.smtp.user"), mailProperties
                                    .getProperty("mail.smtp.password"));
                        }
                    });
    }

    private void send(String message, String programName, int id, List<String> recipientsList, MimeMessage mimeMessage) {
        try {
            for (String emailAddress : recipientsList) {
                mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(emailAddress));
            }
            mimeMessage.setSubject(programName);
            mimeMessage.setText(LOG_ANALYZER_LOG_LINK_TEMPLATE + id + "\n" + message);
            Transport.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
