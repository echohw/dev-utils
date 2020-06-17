package com.example.devutils.utils.io;

import com.example.devutils.utils.text.StringUtils;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by AMe on 2020-06-05 23:40.
 */
public class FileUtils {

    private static Logger logger = LoggerFactory.getLogger(FileUtils.class);

    public static final int BUFFER_SIZE = 4096;

    public static Optional<String> getFileExtName(String fileName) {
        int posi;
        if (StringUtils.isBlank(fileName) || (posi = fileName.lastIndexOf(".")) == -1 || posi == fileName.length() - 1) {
            return Optional.empty();
        }
        return Optional.of(fileName.substring(posi + 1));
    }

    /**
     * 压缩文件
     */
    public static File zipFile(Collection<File> srcFileList, File destFile,
        Function<File, String> namingHandler) {
        if (namingHandler == null) {
            namingHandler = File::getName;
        }
        try (
            ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(destFile));
        ) {
            for (File srcFile : srcFileList) {
                String fileName = namingHandler.apply(srcFile);
                zipOutputStream.putNextEntry(new ZipEntry(fileName));
                try (
                    FileInputStream fileInputStream = new FileInputStream(srcFile);
                ) {
                    copy(fileInputStream, zipOutputStream);
                } catch (Exception ex) {
                    logger.error(ex.getMessage());
                }
                zipOutputStream.closeEntry();
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex.getMessage());
        }
        return destFile;
    }

    /**
     * 解压文件
     */
    public static List<File> unzipFile(File srcFile, String basePath,
        Function<ZipEntry, String> namingHandler) {
        List<File> destFileList = new ArrayList<>();
        if (namingHandler == null) {
            namingHandler = ZipEntry::getName;
        }
        try (
            ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(srcFile));
        ) {
            ZipEntry nextEntry;
            while ((nextEntry = zipInputStream.getNextEntry()) != null) {
                String fileName = namingHandler.apply(nextEntry);
                File destFile = new File(basePath, fileName);
                try (
                    FileOutputStream fileOutputStream = new FileOutputStream(destFile);
                ) {
                    copy(zipInputStream, fileOutputStream);
                } catch (IOException ex) {
                    logger.error(ex.getMessage());
                }
                destFileList.add(destFile);
                zipInputStream.closeEntry();
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex.getMessage());
        }
        return destFileList;
    }

    public static void copy(InputStream inputStream, OutputStream outputStream) throws IOException {
        copy(inputStream, outputStream, 4096);
    }

    public static void copy(InputStream inputStream, OutputStream outputStream, int bufSize)
        throws IOException {
        byte[] byteBuf = new byte[bufSize];
        int len;
        while ((len = inputStream.read(byteBuf)) != -1) {
            outputStream.write(byteBuf, 0, len);
        }
    }

    public static String readAllAsString(InputStream inputStream, Charset charset) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        copy(inputStream, outputStream);
        return new String(outputStream.toByteArray(), charset);
    }
}
