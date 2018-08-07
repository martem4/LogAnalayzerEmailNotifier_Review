package com.century.logssender.mail;

import com.century.logssender.model.LogEvent;
import com.century.logssender.properties.ApplicationProperties;
import com.century.logssender.properties.MailProperties;
import com.century.logssender.template.TemplateManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Set;

@Service
public class MailService {

    private static final String MESSAGE_SENDING_ERROR_MESSAGE = "Error while message sending.";

    private final Logger logger = LoggerFactory.getLogger(MailService.class);
    private final ApplicationProperties applicationProperties;
    private final TemplateManager templateManager;
    private final MailProperties mailProperties;

    @Autowired
    public MailService(ApplicationProperties applicationProperties,
                       TemplateManager templateManager,
                       MailProperties mailProperties) {
        this.applicationProperties = applicationProperties;
        this.templateManager = templateManager;
        this.mailProperties = mailProperties;
    }

    public void sendLogMailForSelectedTags(LogEvent logEvent) {
        templateManager.getTemplates()
                .stream()
                .filter(template -> template.isFor(logEvent))
                .forEach(template -> createMimeMessageAndSend(logEvent, template.getRecipients()));
    }

    private void createMimeMessageAndSend(LogEvent logEvent, Set<String> recipientsList) {
        MimeMessage mimeMessage = new MimeMessage(getMailingSession());
        try {
            processMessageAndSend(logEvent, recipientsList, mimeMessage);
        } catch (MessagingException e) {
            logger.error(MESSAGE_SENDING_ERROR_MESSAGE, e);
        }
    }

    private Session getMailingSession() {
        return Session.getDefaultInstance(mailProperties);
    }

    private void processMessageAndSend(LogEvent logEvent, Set<String> recipientsList, MimeMessage mimeMessage) throws MessagingException {
        setRecipients(recipientsList, mimeMessage);
        mimeMessage.setSubject(logEvent.getSysLogTag());
        mimeMessage.setText(String.format("%s%s\n%s", applicationProperties.getLogAnalyzerLinkTemplate(), logEvent.getId(), logEvent.getMessage()));
        Transport.send(mimeMessage, mailProperties.getUser(), mailProperties.getPassword());
    }

    private void setRecipients(Set<String> recipientsList, MimeMessage mimeMessage) throws MessagingException {
        for (String emailAddress : recipientsList) {
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(emailAddress));
        }
    }
}
