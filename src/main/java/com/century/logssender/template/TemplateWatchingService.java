package com.century.logssender.template;

import com.century.logssender.properties.ApplicationProperties;
import io.reactivex.Observable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.*;
import java.util.concurrent.TimeUnit;

@Service
public class TemplateWatchingService {

    private static final Path templatesPath = Paths.get("templates");

    private final Logger logger = LoggerFactory.getLogger(TemplateWatchingService.class);
    private final ApplicationProperties applicationProperties;
    private WatchKey registeredWatchKey;

    @Autowired
    public TemplateWatchingService(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    @PostConstruct
    public void onInit() {
        try {
            WatchService watchService = FileSystems.getDefault().newWatchService();
            registeredWatchKey = templatesPath.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);
        } catch (IOException e) {
            logger.error("Problem with template files update watching.", e);
        }
    }

    Observable<Path> observeTemplates() {
        return Observable.interval(applicationProperties.getTemplatesUpdatePollingInterval(), TimeUnit.SECONDS)
                .map(interval -> registeredWatchKey.pollEvents())
                .flatMapIterable(i -> i)
                .map(WatchEvent::context)
                .cast(Path.class)
                .map(templatesPath::resolve);
    }

}
