package com.century.logssender.template;

import io.reactivex.Observable;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.*;
import java.util.concurrent.TimeUnit;

@Service
public class TemplateWatchingService {

    private static final Path templatesPath = Paths.get("templates");
    private static final int FILE_UPDATE_INTERVAL = 60;

    private WatchKey registeredWatchKey;

    @PostConstruct
    public void onInit() {
        try {
            WatchService watchService = FileSystems.getDefault().newWatchService();
            registeredWatchKey = templatesPath.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);
        } catch (IOException e) {
            System.err.println("Problem with template files update watching.");
        }
    }

    Observable<Path> observeTemplates() {
        return Observable.interval(FILE_UPDATE_INTERVAL, TimeUnit.SECONDS)
                .map(interval -> registeredWatchKey.pollEvents())
                .flatMapIterable(i -> i)
                .map(WatchEvent::context)
                .cast(Path.class)
                .map(templatesPath::resolve);
    }

}
