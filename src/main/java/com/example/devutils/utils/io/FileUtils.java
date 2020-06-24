package com.example.devutils.utils.io;

import com.example.devutils.utils.text.StringUtils;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * 文件操作工具类
 * Created by AMe on 2020-06-05 23:40.
 */
public class FileUtils {

    public static final int BUFFER_SIZE = 4096;

    public static File getFile(String filePath) {
        return new File(filePath);
    }

    public static boolean createFile(File file) throws IOException {
        return file.createNewFile();
    }

    public static String getExtName(String fileName) {
        int posi;
        if (StringUtils.isBlank(fileName) || (posi = fileName.lastIndexOf(".")) == -1 || posi == fileName.length() - 1) {
            return null;
        }
        return fileName.substring(posi + 1);
    }

    public static long getSize(File file) {
        return file.length();
    }

    public static byte[] readAsBytes(File file) throws IOException {
        try (
            BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(file));
        ) {
            return StreamUtils.readAsBytes(inputStream, BUFFER_SIZE);
        }
    }

    public static String readAsString(File file, Charset charset) throws IOException {
        try (
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), charset));
        ) {
            return StreamUtils.readAsString(bufferedReader, BUFFER_SIZE);
        }
    }

    public static List<String> readAsLines(File file, Charset charset) throws IOException {
        try (
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), charset));
        ) {
            return StreamUtils.readAsLines(bufferedReader);
        }
    }

    public static long writeBytes(File file, byte[] bytes) throws IOException {
        try (
            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file));
        ) {
            return StreamUtils.writeBytes(outputStream, bytes);
        }
    }

    public static long writeString(File file, String content, Charset charset) throws IOException {
        try (
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), charset));
        ) {
            return StreamUtils.writeString(bufferedWriter, content);
        }
    }

    public static long writeLines(File file, List<String> lineList, Charset charset) throws IOException {
        try (
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), charset));
        ) {
            return StreamUtils.writeLines(bufferedWriter, lineList);
        }
    }

    /**
     * 压缩文件
     */
    public static File zipFile(Collection<File> srcFileList, File destFile, Function<File, String> namingHandler) throws IOException {
        namingHandler = namingHandler == null ? File::getName : namingHandler;
        try (
            FileOutputStream fileOutputStream = new FileOutputStream(destFile);
            ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream);
        ) {
            for (File srcFile : srcFileList) {
                String fileName = namingHandler.apply(srcFile);
                zipOutputStream.putNextEntry(new ZipEntry(fileName));
                try (
                    BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(srcFile));
                ) {
                    StreamUtils.copy(inputStream, zipOutputStream, BUFFER_SIZE, null);
                }
                zipOutputStream.closeEntry();
            }
        }
        return destFile;
    }

    /**
     * 解压文件
     */
    public static List<File> unzipFile(File srcFile, String basePath, Function<ZipEntry, String> namingHandler) throws IOException {
        List<File> destFileList = new ArrayList<>();
        namingHandler = namingHandler == null ? ZipEntry::getName : namingHandler;
        try (
            FileInputStream fileInputStream = new FileInputStream(srcFile);
            ZipInputStream zipInputStream = new ZipInputStream(fileInputStream);
        ) {
            ZipEntry nextEntry;
            while ((nextEntry = zipInputStream.getNextEntry()) != null) {
                String fileName = namingHandler.apply(nextEntry);
                File destFile = new File(basePath, fileName);
                try (
                    BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(destFile));
                ) {
                    StreamUtils.copy(zipInputStream, outputStream, BUFFER_SIZE, null);
                }
                zipInputStream.closeEntry();
                destFileList.add(destFile);
            }
        }
        return destFileList;
    }

}
