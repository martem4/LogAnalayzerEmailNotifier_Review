package com.century.logssender.mail;

import com.century.logssender.model.LogEvent;
import com.century.logssender.model.Tag;
import com.century.logssender.properties.MailProperties;
import com.century.logssender.template.TemplateParsingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Set;

@Service
public class MailService {

    private static final String LOG_ANALYZER_LOG_LINK_TEMPLATE ="http://172.172.174.100/loganalyzer/details.php?uid=";

    private final TemplateParsingService templateParsingService;
    private final MailProperties mailProperties;

    @Autowired
    public MailService(TemplateParsingService templateParsingService, MailProperties mailProperties) {
        this.templateParsingService = templateParsingService;
        this.mailProperties = mailProperties;
    }

    public void sendLogMailForSelectedTags(LogEvent logEvent) {
        for (Tag mailTemplate : templateParsingService.getTags()) {
            if (mailTemplate.getTagName().toLowerCase().contains(logEvent.getSysLogTag().toLowerCase())) {
                sendMail(logEvent.getMessage(), logEvent.getSysLogTag(), logEvent.getId(), mailTemplate.getRecipients());
            }
        }
    }

    private void sendMail(String message, String programName, int id, Set<String> recipientsList) {
        Session session = getMailingSession();

        MimeMessage mimeMessage = new MimeMessage(session);
        send(message, programName, id, recipientsList, mimeMessage);
    }

    private Session getMailingSession() {
        return Session.getDefaultInstance(mailProperties,
                    new Authenticator() {
                        @Override
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(mailProperties.getUser(), mailProperties.getPassword());
                        }
                    });
    }

    private void send(String message, String programName, int id, Set<String> recipientsList, MimeMessage mimeMessage) {
        try {
            sendMessage(message, programName, id, recipientsList, mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(String message, String programName, int id, Set<String> recipientsList, MimeMessage mimeMessage) throws MessagingException {
        setRecipients(recipientsList, mimeMessage);
        mimeMessage.setSubject(programName);
        mimeMessage.setText(LOG_ANALYZER_LOG_LINK_TEMPLATE + id + "\n" + message);
        Transport.send(mimeMessage);
    }

    private void setRecipients(Set<String> recipientsList, MimeMessage mimeMessage) throws MessagingException {
        for (String emailAddress : recipientsList) {
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(emailAddress));
        }
    }
}
