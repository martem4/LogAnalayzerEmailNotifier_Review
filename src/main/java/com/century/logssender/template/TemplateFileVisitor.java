package com.century.logssender.template;


import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

@Component
public class TemplateFileVisitor implements FileVisitor<Path> {

    private final PublishSubject<Path> initialTemplatesSubject = PublishSubject.create();

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
        initialTemplatesSubject.onNext(file);
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

    public Observable<Path> observeInitialTemplates() {
        return initialTemplatesSubject;
    }
}