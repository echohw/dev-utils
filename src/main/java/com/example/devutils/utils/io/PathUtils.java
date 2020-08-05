package com.example.devutils.utils.io;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import javax.swing.filechooser.FileSystemView;

/**
 * 路径操作工具类
 * Created by AMe on 2020-06-11 00:51.
 */
public class PathUtils {

    public static Path getPath(String first, String ...more) {
        return Paths.get(first, more);
    }

    public static Path getPath(File file) {
        return file.toPath();
    }

    public static Path getDesktopPath() {
        FileSystemView fileSystemView = FileSystemView.getFileSystemView();
        File homeDirectory = fileSystemView.getHomeDirectory();
        return homeDirectory.toPath();
    }

    public static Path getSystemDrive() {
        return getDesktopPath().getRoot();
    }

    public static List<Path> getAllParentPath(Path path) {
        LinkedList<Path> parentPathList = new LinkedList<>();
        while ((path = path.getParent()) != null) {
            parentPathList.addFirst(path);
        }
        return parentPathList;
    }

    public static boolean isFile(Path path) {
        return !isDirectory(path);
    }

    public static boolean isDirectory(Path path) {
        return Files.isDirectory(path);
    }

    public static boolean exists(Path path) {
        return Files.exists(path);
    }

    public static boolean notExists(Path path) {
        return Files.notExists(path);
    }

    public static Path graftPath(Path originPath, Path destPath, Path targetPath) {
        Path relaPath = originPath.relativize(destPath);
        return targetPath.resolve(relaPath);
    }

}
