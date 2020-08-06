package com.example.devutils.utils.io;

import com.example.devutils.inter.Process;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.WatchEvent;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * 目录操作工具类
 * Created by AMe on 2020-06-11 00:52.
 */
public class DirectoryUtils {

    public static Path createDir(Path dirPath) throws IOException {
        return Files.createDirectories(dirPath);
    }

    public static Path createDirs(Path dirPath) throws IOException {
        return Files.createDirectories(dirPath);
    }

    public static Path createPlaceDir(Path filePath) throws IOException {
        Path parentPath = filePath.isAbsolute() ? filePath.getParent() : filePath.toAbsolutePath().getParent();
        if (parentPath != null) createDir(parentPath);
        return parentPath;
    }

    public static Path createPlaceDirs(Path filePath) throws IOException {
        Path parentPath = filePath.isAbsolute() ? filePath.getParent() : filePath.toAbsolutePath().getParent();
        if (parentPath != null) createDirs(parentPath);
        return parentPath;
    }

    public static Path createTempDir(String prefix) throws IOException {
        return Files.createTempDirectory(prefix);
    }

    public static Path createTempDir(Path dirPath, String prefix) throws IOException {
        return Files.createTempDirectory(dirPath, prefix);
    }

    public static Stream<Path> list(Path dirPath) throws IOException {
        return Files.list(dirPath);
    }

    public static Stream<Path> walk(Path dirPath) throws IOException {
        return Files.walk(dirPath);
    }

    public static Stream<Path> walk(Path dirPath, int maxDepth) throws IOException {
        return Files.walk(dirPath, maxDepth);
    }

    public static Path walkFileTree(Path dirPath, FileVisitor<? super Path> fileVisitor) throws IOException {
        return Files.walkFileTree(dirPath, fileVisitor);
    }

    public static WatchService getWatchService(Path dirPath, WatchEvent.Kind<?>[] events) throws IOException {
        WatchService watchService = FileSystems.getDefault().newWatchService();
        dirPath.register(watchService, events);
        return watchService;
    }

    public static <R> void copy(Path srcDirPath, Path destDirPath, Process<Path, R> process, CopyOption... options) throws IOException {
        createDirs(destDirPath);
        walkFileTree(srcDirPath, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dirPath, BasicFileAttributes attrs) throws IOException {
                Path graftedPath = PathUtils.graftPath(srcDirPath, dirPath, destDirPath);
                createDir(graftedPath);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path filePath, BasicFileAttributes attrs) throws IOException {
                Path graftedPath = PathUtils.graftPath(srcDirPath, filePath, destDirPath);
                R r = Optional.ofNullable(process).map(p -> process.pre(graftedPath)).orElse(null);
                NioFileUtils.copy(filePath, graftedPath, options);
                Optional.ofNullable(process).ifPresent(p -> process.post(graftedPath, r));
                return FileVisitResult.CONTINUE;
            }
        });
    }

}
