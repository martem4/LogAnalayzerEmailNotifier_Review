package com.century.logssender;

import com.century.logssender.observer.LogObserver;
import com.century.logssender.polling.LogPollingService;
import com.century.logssender.template.TemplateManager;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class Main {
    private final LogPollingService logPollingService;
    private final LogObserver logObserver;
    private final TemplateManager templateManager;

    @Autowired
    public Main(LogPollingService logPollingService, LogObserver logObserver, TemplateManager templateManager) {
        this.logPollingService = logPollingService;
        this.logObserver = logObserver;
        this.templateManager = templateManager;
    }

    public static void main(String[] args) throws IOException {
        final AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext("com.century.logssender");

        final Main main = context.getBean(Main.class);
        main.start();

        blockMainThread();
    }

    private void start() {
        final Disposable subscription = logPollingService
                .poll()
                .subscribe(logObserver::onNext, logObserver::onError, logObserver::onComplete);

        Runtime.getRuntime().addShutdownHook(new Thread(subscription::dispose));
    }

    private static void blockMainThread() throws IOException {
        System.in.read();
    }
}
