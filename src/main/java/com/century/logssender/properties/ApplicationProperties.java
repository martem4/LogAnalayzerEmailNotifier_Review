package com.century.logssender.properties;

import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Component
public class ApplicationProperties extends Properties {

    private static final String JDBC_PROPERTIES_FILE = "properties/app.properties";
    private static final String POLLING_INTERVAL_PROPERTY_KEY = "polling.interval";
    private static final String DEFAULT_POLLING_INTERVAL = "60";

    @PostConstruct
    public void init() {
        readConnectionProperties();
    }

    public int getPollingInterval() {
        return Integer.parseInt(getProperty(POLLING_INTERVAL_PROPERTY_KEY, DEFAULT_POLLING_INTERVAL));
    }

    public String getUrl() {
        return getProperty("db.url");
    }

    public String getPassword() {
        return getProperty("db.password");
    }

    public String getUser() {
        return getProperty("db.user");
    }

    private void readConnectionProperties() {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(JDBC_PROPERTIES_FILE);
            load(inputStream);
        } catch (IOException e) {
            System.err.println("Problem with application properties loading");
        }
    }

}
