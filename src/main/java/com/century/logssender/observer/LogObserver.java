package com.century.logssender.observer;

import com.century.logssender.mail.MailService;
import com.century.logssender.model.LogEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogObserver {

    private final MailService mailService;

    @Autowired
    public LogObserver(MailService mailService) {
        this.mailService = mailService;
    }

    public void onNext(LogEvent logEvent) {
        this.mailService.sendLogMailForSelectedTags(logEvent);
    }

    public void onError(Throwable e) {
        e.printStackTrace();
    }

    public void onComplete() {
        System.out.println("Polling stopped.");
    }
}
