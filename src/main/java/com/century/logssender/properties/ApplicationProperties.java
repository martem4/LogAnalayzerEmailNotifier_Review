package com.century.logssender.properties;

import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

@Component
public class ApplicationProperties {

    private static final String JDBC_PROPERTIES_FILE = "app.properties";
    private static final String POLLING_INTERVAL_PROPERTY_KEY = "polling.interval";
    private static final String DEFAULT_POLLING_INTERVAL = "60";

    private final Properties applicationProperties = new Properties();

    @PostConstruct
    public void init() {
        readConnectionProperties();
    }

    public int getPollingInterval() {
        return Integer.parseInt(applicationProperties.getProperty(
                POLLING_INTERVAL_PROPERTY_KEY,
                DEFAULT_POLLING_INTERVAL)
        );
    }

    public String getUrl() {
        return applicationProperties.getProperty("db.url");
    }

    public String getPassword() {
        return applicationProperties.getProperty("db.password");
    }

    public String getUser() {
        return applicationProperties.getProperty("db.user");
    }

    @SneakyThrows
    private void readConnectionProperties() {
        InputStream inputStream = new FileInputStream(JDBC_PROPERTIES_FILE);
        applicationProperties.load(inputStream);
    }

}
