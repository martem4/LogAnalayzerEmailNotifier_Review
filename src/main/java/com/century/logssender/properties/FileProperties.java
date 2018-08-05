package com.century.logssender.properties;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public abstract class FileProperties extends Properties {

    private static final String LOADING_ERROR_MESSAGE = "Problem with properties loading. Class: %s";

    FileProperties() {
        this.readConnectionProperties();
    }

    public abstract String getFileName();

    private void readConnectionProperties() {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(String.format("properties/%s.properties", getFileName()));
            load(inputStream);
        } catch (IOException e) {
            System.err.println(String.format(LOADING_ERROR_MESSAGE, this.getClass().getCanonicalName()));
        }
    }

}
