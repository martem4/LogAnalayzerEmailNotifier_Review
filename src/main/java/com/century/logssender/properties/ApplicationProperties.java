package com.century.logssender.properties;

import org.springframework.stereotype.Component;

@Component
public class ApplicationProperties extends FileProperties {

    private static final String PROPERTIES_KEY = "application";
    private static final String POLLING_INTERVAL_PROPERTY_KEY = "polling.interval";
    private static final String FILE_UPDATES_INTERVAL_KEY = "file.check.updates.interval";
    private static final String LOG_ANALYZER_LINK_TEMPLATE_KEY = "log.analyzer.link.template";
    private static final String DEFAULT_POLLING_INTERVAL = "60";

    @Override
    public String getFileName() {
        return PROPERTIES_KEY;
    }

    public int getPollingInterval() {
        return Integer.parseInt(getProperty(POLLING_INTERVAL_PROPERTY_KEY, DEFAULT_POLLING_INTERVAL));
    }

    public int getTemplatesUpdatePollingInterval() {
        return Integer.parseInt(getProperty(FILE_UPDATES_INTERVAL_KEY, DEFAULT_POLLING_INTERVAL));
    }

    public String getLogAnalyzerLinkTemplate() {
        return getProperty(LOG_ANALYZER_LINK_TEMPLATE_KEY);
    }
}
