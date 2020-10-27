package com.example.devutils.utils.io;

import com.example.devutils.deps.Progress;
import com.example.devutils.deps.ProgressAdapter;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.NotDirectoryException;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.WatchEvent;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.stream.Stream;

/**
 * 目录操作工具类
 * Created by AMe on 2020-06-11 00:52.
 */
public class DirectoryUtils {

    public static Path createDir(Path dirPath) throws IOException {
        return Files.createDirectory(dirPath);
    }

    public static Path createDirs(Path dirPath) throws IOException {
        return Files.createDirectories(dirPath);
    }

    public static Path createPlaceDir(Path filePath) throws IOException {
        Path parentPath = filePath.toAbsolutePath().getParent();
        if (parentPath != null) createDir(parentPath);
        return parentPath;
    }

    public static Path createPlaceDirs(Path filePath) throws IOException {
        Path parentPath = filePath.toAbsolutePath().getParent();
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

    public static void copy(Path srcDirPath, Path destDirPath, CopyOption... options) throws IOException {
        copy(srcDirPath, destDirPath, null, options);
    }

    public static void copy(Path srcDirPath, Path destDirPath, Progress<Path, Void> progress, CopyOption... options) throws IOException {
        if (PathUtils.isFile(srcDirPath)) {
            throw new NotDirectoryException(srcDirPath.toString());
        }
        Progress<Path, Void> copyProgress = progress == null ? new ProgressAdapter<>() : progress;
        walkFileTree(srcDirPath, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dirPath, BasicFileAttributes attrs) throws IOException {
                copyProgress.before(dirPath);
                Path graftedPath = PathUtils.graftPath(srcDirPath, dirPath, destDirPath);
                createDirs(graftedPath);
                return super.preVisitDirectory(dirPath, attrs);
            }

            @Override
            public FileVisitResult visitFile(Path filePath, BasicFileAttributes attrs) throws IOException {
                Path graftedPath = PathUtils.graftPath(srcDirPath, filePath, destDirPath);
                copyProgress.before(filePath);
                NioFileUtils.copy(filePath, graftedPath, options);
                copyProgress.after(filePath);
                return super.visitFile(filePath, attrs);
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dirPath, IOException exc) throws IOException {
                copyProgress.after(dirPath);
                return super.postVisitDirectory(dirPath, exc);
            }
        });
    }

    public static void delete(Path dirPath) throws IOException {
        delete(dirPath, null);
    }

    public static void delete(Path dirPath, Progress<Path, Void> progress) throws IOException {
        if (PathUtils.notExists(dirPath)) {
            return;
        }
        if (PathUtils.isFile(dirPath)) {
            throw new NotDirectoryException(dirPath.toString());
        }
        Progress<Path, Void> delProgress = progress == null ? new ProgressAdapter<>() : progress;
        walkFileTree(dirPath, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                delProgress.before(dir);
                return super.preVisitDirectory(dir, attrs);
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                delProgress.before(file);
                NioFileUtils.delete(file);
                delProgress.after(file);
                return super.visitFile(file, attrs);
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                NioFileUtils.delete(dir);
                delProgress.after(dir);
                return super.postVisitDirectory(dir, exc);
            }
        });
    }
}
