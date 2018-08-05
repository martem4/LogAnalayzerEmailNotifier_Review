package com.century.logssender.template;

import com.century.logssender.model.Tag;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashSet;
import java.util.Set;

@Service
public class TemplateParsingService {

    private static final Path TEMPLATES_PATH = Paths.get("templates");
    private static final String TEMPLATE_PARSING_ERROR_MESSAGE =
            "Problem with templates parsing. Please check yaml correctness";
    private static final String FILE_ERROR_MESSAGE_POSTFIX = "with file - %s";

    private final TemplateWatchingService watchingService;
    private final Set<Tag> tags = new HashSet<>();

    private final Yaml yaml;
    private Disposable watchSubscription;

    @Autowired
    public TemplateParsingService(TemplateWatchingService watchingService) {
        this.watchingService = watchingService;

        Constructor constructor = initYamlConstructor();
        yaml = new Yaml(constructor);
    }

    @PostConstruct
    public void onInit() {
        try {
            Files.walkFileTree(TEMPLATES_PATH, new TemplatesVisitor());
            subscribeOnFileChanges();
        } catch (IOException e) {
            System.err.println(TEMPLATE_PARSING_ERROR_MESSAGE);
        }
    }

    @PreDestroy
    public void onDestroy() {
        if (watchSubscription != null) {
            this.watchSubscription.dispose();
        }
    }

    public Set<Tag> getTags() {
        return this.tags;
    }

    private void subscribeOnFileChanges() {
        watchSubscription = watchingService
                .observeTemplates()
                .subscribeOn(Schedulers.newThread())
                .subscribe(this::parseTagFromPath);
    }

    private Constructor initYamlConstructor() {
        Constructor constructor = new Constructor(Tag.class);
        TypeDescription typeDescription = new TypeDescription(Tag.class);
        typeDescription.addPropertyParameters("recipients", String.class, Object.class);
        constructor.addTypeDescription(typeDescription);
        return constructor;
    }

    private void parseTagFromPath(Path path) {
        final Tag tag;
        try (InputStream inputStream = Files.newInputStream(path)) {
            tag = yaml.load(inputStream);
            tags.add(tag);
        } catch (IOException e) {
            showParsingError(path);
        }
    }

    private void showParsingError(Path path) {
        System.err.println(String.format(
                String.join(" ", TEMPLATE_PARSING_ERROR_MESSAGE, FILE_ERROR_MESSAGE_POSTFIX),
                path.getFileName())
        );
    }

    private class TemplatesVisitor implements FileVisitor<Path> {
        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
            parseTagFromPath(file);
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFileFailed(Path file, IOException exc) {
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
            return FileVisitResult.CONTINUE;
        }
    }

}
