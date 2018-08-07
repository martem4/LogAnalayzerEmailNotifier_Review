package com.century.logssender.template;


import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import org.springframework.stereotype.Component;

import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

@Component
public class TemplateFileVisitor extends SimpleFileVisitor<Path> {

    private final PublishSubject<Path> initialTemplatesSubject = PublishSubject.create();

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
        initialTemplatesSubject.onNext(file);
        return FileVisitResult.CONTINUE;
    }

    public Observable<Path> observeInitialTemplates() {
        return initialTemplatesSubject;
    }
}