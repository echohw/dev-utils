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
import java.util.stream.Stream;

/**
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
        if (PathUtils.notExists(parentPath)) {
            return createDir(parentPath);
        }
        return filePath;
    }

    public static Path createPlaceDirs(Path filePath) throws IOException {
        Path parentPath = filePath.isAbsolute() ? filePath.getParent() : filePath.toAbsolutePath().getParent();
        if (PathUtils.notExists(parentPath)) {
            return createDirs(parentPath);
        }
        return filePath;
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
        if (PathUtils.notExists(destDirPath)) {
            createDirs(destDirPath);
        }
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
                R r = process.pre(graftedPath);
                Files.copy(filePath, graftedPath, options);
                process.post(graftedPath, r);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    public static <R> void quickCopy(Path srcDirPath, Path destDirPath, Process<Path, R> process, CopyOption... options) throws IOException {
        if (PathUtils.notExists(destDirPath)) {
            createDirs(destDirPath);
        }

    }

}
