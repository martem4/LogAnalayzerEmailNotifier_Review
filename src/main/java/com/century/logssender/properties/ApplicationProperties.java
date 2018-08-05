package com.century.logssender.properties;

import org.springframework.stereotype.Component;

@Component
public class ApplicationProperties extends FileProperties {

    private static final String PROPERTIES_KEY = "application";
    private static final String POLLING_INTERVAL_PROPERTY_KEY = "polling.interval";
    private static final String DEFAULT_POLLING_INTERVAL = "60";

    public int getPollingInterval() {
        return Integer.parseInt(getProperty(POLLING_INTERVAL_PROPERTY_KEY, DEFAULT_POLLING_INTERVAL));
    }

    @Override
    public String getFileName() {
        return PROPERTIES_KEY;
    }
}
