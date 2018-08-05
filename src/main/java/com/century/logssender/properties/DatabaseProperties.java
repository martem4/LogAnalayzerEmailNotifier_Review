package com.century.logssender.properties;

import org.springframework.stereotype.Component;

@Component
public class DatabaseProperties extends FileProperties {

    private static final String JDBC_PROPERTIES_FILE = "database";

    @Override
    public String getFileName() {
        return JDBC_PROPERTIES_FILE;
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

    public String getDriverClassPath() {
        return getProperty("db.driver.class.name");
    }
}
