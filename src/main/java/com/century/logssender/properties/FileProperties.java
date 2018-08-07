package com.century.logssender.properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public abstract class FileProperties extends Properties {

    private static final String LOADING_ERROR_MESSAGE = "Problem with properties loading. Class: %s";
    private static final Logger logger = LoggerFactory.getLogger(FileProperties.class);
    private static final String PROPERTIES_TEMPLATE = "properties/%s.properties";

    FileProperties() {
        this.readConnectionProperties();
    }

    public abstract String getFileName();

    private void readConnectionProperties() {
        try (InputStream inputStream = new FileInputStream(String.format(PROPERTIES_TEMPLATE, getFileName()))) {
            load(inputStream);
        } catch (IOException e) {
            logger.error(String.format(LOADING_ERROR_MESSAGE, this.getClass().getCanonicalName()), e);
        }
    }

    public int getIntProperty(String propertyKey, int defaultValue) {
        final String value = getProperty(propertyKey);
        return value == null ? defaultValue : Integer.parseInt(value);
    }

}
