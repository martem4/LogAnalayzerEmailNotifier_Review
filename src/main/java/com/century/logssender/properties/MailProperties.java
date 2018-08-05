package com.century.logssender.properties;

import com.sun.jmx.snmp.defaults.SnmpProperties;
import jdk.nashorn.internal.codegen.types.ArrayType;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@Component
public class MailProperties extends Properties {

    private static final String MAIL_SETTINGS_FILE = "properties/mail.properties";

    @PostConstruct
    public void onInit() {
        readMailProperties();
    }

    private void readMailProperties() {
        try (FileInputStream inputStream =
                     new FileInputStream(MAIL_SETTINGS_FILE)){
            load(inputStream);
        } catch (IOException e) {
            System.err.println("Problem with mail properties loading");
        }
    }

    public String getUser() {
        return getProperty("mail.smtp.user");
    }

    public String getPassword() {
        return getProperty("mail.smtp.password");
    }



}
