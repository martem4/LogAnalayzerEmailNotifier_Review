package com.century.logssender.properties;

import org.springframework.stereotype.Component;

@Component
public class MailProperties extends FileProperties {

    private static final String MAIL_SETTINGS_FILE = "mail";

    public String getUser() {
        return getProperty("mail.smtp.user");
    }

    public String getPassword() {
        return getProperty("mail.smtp.password");
    }

    @Override
    public String getFileName() {
        return MAIL_SETTINGS_FILE;
    }
}
