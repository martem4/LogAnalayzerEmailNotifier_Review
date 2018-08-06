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
public class TemplateFilesService {

    private static final Logger logger = LoggerFactory.getLogger(TemplateFilesService.class);
    private static final Path templatesPath = Paths.get("templates");

    private final ApplicationProperties applicationProperties;
    private final TemplateFileVisitor templateFileVisitor;

    private WatchKey registeredWatchKey;

    @Autowired
    public TemplateFilesService(ApplicationProperties applicationProperties, TemplateFileVisitor templateFileVisitor) {
        this.applicationProperties = applicationProperties;
        this.templateFileVisitor = templateFileVisitor;
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

    Observable<Path> observeChangedTemplates() {
        return Observable.interval(applicationProperties.getTemplatesUpdatePollingInterval(), TimeUnit.SECONDS)
                .map(interval -> registeredWatchKey.pollEvents())
                .flatMapIterable(i -> i)
                .map(WatchEvent::context)
                .cast(Path.class)
                .map(templatesPath::resolve);
    }

    Observable<Path> observeInitialTemplates() {
        return templateFileVisitor.observeInitialTemplates();
    }

}
