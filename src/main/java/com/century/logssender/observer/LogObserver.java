package com.century.logssender.observer;

import com.century.logssender.mail.MailService;
import com.century.logssender.model.LogEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogObserver {

    private static final String PROBLEM_ON_EVENT_GETTING_MESSAGE = "Problem on event getting.";
    private static final String POLLING_STOPPED_MESSAGE = "Polling stopped.";

    private final Logger logger = LoggerFactory.getLogger(LogObserver.class);
    private final MailService mailService;

    @Autowired
    public LogObserver(MailService mailService) {
        this.mailService = mailService;
    }

    public void onNext(LogEvent logEvent) {
        this.mailService.sendLogMailForSelectedTags(logEvent);
    }

    public void onError(Throwable e) {
        logger.error(PROBLEM_ON_EVENT_GETTING_MESSAGE);
    }

    public void onComplete() {
        logger.info(POLLING_STOPPED_MESSAGE);
    }
}
