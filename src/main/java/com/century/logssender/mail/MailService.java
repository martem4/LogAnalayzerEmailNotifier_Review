package com.century.logssender.mail;

import com.century.logssender.model.LogEvent;
import com.century.logssender.model.Tag;
import com.century.logssender.properties.ApplicationProperties;
import com.century.logssender.properties.MailProperties;
import com.century.logssender.template.TemplateParsingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Set;

@Service
public class MailService {

    private static final String MESSAGE_SENDING_ERROR_MESSAGE = "Error while message sending.";

    private final Logger logger = LoggerFactory.getLogger(MailService.class);
    private final ApplicationProperties applicationProperties;
    private final TemplateParsingService templateParsingService;
    private final MailProperties mailProperties;

    @Autowired
    public MailService(ApplicationProperties applicationProperties, TemplateParsingService templateParsingService, MailProperties mailProperties) {
        this.applicationProperties = applicationProperties;
        this.templateParsingService = templateParsingService;
        this.mailProperties = mailProperties;
    }

    public void sendLogMailForSelectedTags(LogEvent logEvent) {
        for (Tag tag : templateParsingService.getTags()) {
            if (tag.getTagName().toLowerCase().contains(logEvent.getSysLogTag().toLowerCase())) {
                sendMail(logEvent.getMessage(), logEvent.getSysLogTag(), logEvent.getId(), tag.getRecipients());
            }
        }
    }

    private void sendMail(String message, String programName, int id, Set<String> recipientsList) {
        Session session = getMailingSession();

        MimeMessage mimeMessage = new MimeMessage(session);
        send(message, programName, id, recipientsList, mimeMessage);
    }

    private Session getMailingSession() {
        return Session.getDefaultInstance(mailProperties);
    }

    private void send(String message, String programName, int id, Set<String> recipientsList, MimeMessage mimeMessage) {
        try {
            sendMessage(message, programName, id, recipientsList, mimeMessage);
        } catch (MessagingException e) {
            logger.error(MESSAGE_SENDING_ERROR_MESSAGE, e);
        }
    }

    private void sendMessage(String message, String programName, int id, Set<String> recipientsList, MimeMessage mimeMessage) throws MessagingException {
        setRecipients(recipientsList, mimeMessage);
        mimeMessage.setSubject(programName);
        mimeMessage.setText(String.format("%s%s\n%s", applicationProperties.getLogAnalyzerLinkTemplate(), id, message));
        Transport.send(mimeMessage, mailProperties.getUser(), mailProperties.getPassword());
    }

    private void setRecipients(Set<String> recipientsList, MimeMessage mimeMessage) throws MessagingException {
        for (String emailAddress : recipientsList) {
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(emailAddress));
        }
    }
}
