package com.century.logssender.xml;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.stream.Collectors;

@XmlRootElement(name = "mailTemplate")
public class MailTemplate {
    public MailTemplate() {}

    public MailTemplate(String logName, List<String> recipients) {
        this.logName = logName;
        this.recipients = recipients;
    }

    private String logName;
    private List<String> recipients;

    public String getLogName() {
        return logName;
    }

    public void setLogName(String logName) {
        this.logName = logName;
    }

    @XmlElementWrapper
    @XmlElement(name = "recipient")
    public List<String> getRecipients() {
        return recipients;
    }

    public void setRecipients(List<String> recipients) {
        this.recipients = recipients;
    }

    @Override
    public String toString() {
        return "Tag: " + logName + "\n | Recipients: \n" + recipients.stream().collect(Collectors.joining("\n\t"));
    }
}
