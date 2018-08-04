package com.century.logssender.xml;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "mailTemplates")
public class MailTemplates {
    public MailTemplates() { }

    List<MailTemplate> mailTemplate;

    public MailTemplates(List<MailTemplate> mailTemplate) {
        super();
        this.mailTemplate = mailTemplate;
    }

    @XmlElement
    public List<MailTemplate> getMailTemplate() {
        return mailTemplate;
    }

    public void setMailTemplate(List<MailTemplate> mailTemplate) {
        this.mailTemplate = mailTemplate;
    }
}
