package com.example.devutils.dep;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Created by AMe on 2020-07-08 17:09.
 */
public class MyFileVisitor<T> extends SimpleFileVisitor<T> {

    private Predicate<T> scanPredicate;
    private Consumer<T> fileConsumer;

    public MyFileVisitor(Predicate<T> scanPredicate, Consumer<T> fileConsumer) {
        this.scanPredicate = Objects.requireNonNull(scanPredicate);
        this.fileConsumer = Objects.requireNonNull(fileConsumer);
    }

    @Override
    public FileVisitResult preVisitDirectory(T dir, BasicFileAttributes attrs) {
        return scanPredicate.test(dir) ? FileVisitResult.CONTINUE : FileVisitResult.SKIP_SUBTREE;
    }

    @Override
    public FileVisitResult visitFile(T file, BasicFileAttributes attrs) {
        fileConsumer.accept(file);
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(T file, IOException exc) throws IOException {
        return super.visitFileFailed(file, exc);
    }

    @Override
    public FileVisitResult postVisitDirectory(T dir, IOException exc) throws IOException {
        return super.postVisitDirectory(dir, exc);
    }
}