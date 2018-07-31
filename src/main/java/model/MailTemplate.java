package model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "mailTemplate")
public class MailTemplate {
    public MailTemplate() {}

    public MailTemplate(String logName, List<String> recipients) {
        this.logName = logName;
        this.recipients = recipients;
    }

    String logName;
    List<String> recipients;

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
}
