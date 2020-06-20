package com.example.devutils.utils.io;

import com.example.devutils.dep.Range;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.FileAttribute;
import java.util.function.Consumer;

/**
 * Created by AMe on 2020-06-12 14:00.
 */
public class NioFileUtils {

    public static final int BUFFER_SIZE = 4096;

    public static Path createFile(Path filePath, FileAttribute<?>... attrs) throws IOException {
        DirectoryUtils.createPlaceDirs(filePath);
        return Files.createFile(filePath, attrs);
    }

    public static Path createFileIfNotExists(Path filePath, FileAttribute<?>... attrs) throws IOException {
        if (PathUtils.notExists(filePath)) {
            return createFile(filePath, attrs);
        }
        return filePath;
    }

    public static Path createTempFile(String prefix, String suffix, FileAttribute<?>... attrs) throws IOException {
        return Files.createTempFile(prefix, suffix, attrs);
    }

    public static Path createTempFile(Path basePath, String prefix, String suffix, FileAttribute<?>... attrs) throws IOException {
        DirectoryUtils.createPlaceDirs(basePath);
        return Files.createTempFile(basePath, prefix, suffix, attrs);
    }

    public static long getSize(Path filePath) throws IOException {
        return Files.size(filePath);
    }

    public static byte[] readAsBytes(Path filePath) throws IOException {
        return Files.readAllBytes(filePath);
    }

    public static byte[] readAsBytes(Path filePath, long position, int size) throws IOException {
        try (
            FileChannel channel = NioChannelUtils.getFileChannel(filePath, StandardOpenOption.READ);
        ) {
            ByteBuffer byteBuffer = NioBufferUtils.getByteBuffer(BUFFER_SIZE, false);
            return NioChannelUtils.readAsBytes(channel, byteBuffer, position, size);
        } catch (IOException ex) {
            throw ex;
        }
    }

    public static String readAsString(Path filePath, Charset charset) throws IOException {
        return new String(readAsBytes(filePath), charset);
    }

    public static String readAsString(Path filePath, long position, int size, Charset charset) throws IOException {
        return new String(readAsBytes(filePath, position, size), charset);
    }

    public static Path writeBytes(Path filePath, byte[] bytes) throws IOException {
        return Files.write(filePath, bytes);
    }

    public static int writeBytes(Path filePath, byte[] bytes, long position) throws IOException {
        FileChannel fileChannel = NioChannelUtils.getFileChannel(filePath, StandardOpenOption.WRITE);
        return NioChannelUtils.writeBytes(fileChannel, bytes, position);
    }

    public static Path writeString(Path filePath, String content, Charset charset) throws IOException {
        return writeBytes(filePath, content.getBytes(charset));
    }

    public static int writeString(Path filePath, String content, long position, Charset charset) throws IOException {
        return writeBytes(filePath, content.getBytes(charset), position);
    }

    public static Path copy(Path srcFilePath, Path destFilePath, CopyOption... options) throws IOException {
        return Files.copy(srcFilePath, destFilePath, options);
    }

    public static long copy(Path srcFilePath, Path destFilePath, ByteBuffer byteBuffer, Consumer<Range<Long>> noticeConsumer) throws IOException {
        return copy(srcFilePath, destFilePath, byteBuffer, 0, 0, getSize(srcFilePath), noticeConsumer);
    }

    public static long copy(Path srcFilePath, Path destFilePath, ByteBuffer byteBuffer, long srcFilePosition, long destFilePosition, long size, Consumer<Range<Long>> noticeConsumer) throws IOException {
        createFileIfNotExists(destFilePath);
        long fileSize = getSize(srcFilePath);
        if (fileSize == 0) {
            return 0;
        }
        try (
            FileChannel inChannel = FileChannel.open(srcFilePath, StandardOpenOption.READ);
            FileChannel outChannel = FileChannel.open(destFilePath, StandardOpenOption.READ, StandardOpenOption.WRITE, StandardOpenOption.CREATE);
        ) {
            return NioChannelUtils.copy(inChannel, outChannel, byteBuffer, srcFilePosition, destFilePosition, size, noticeConsumer);
        } catch (IOException ex) {
            throw ex;
        }
    }

    public static Path move(Path srcFilePath, Path destFilePath, CopyOption... options) throws IOException {
        return Files.move(srcFilePath, destFilePath, options);
    }

    public static boolean delete(Path filePath) throws IOException {
        return Files.deleteIfExists(filePath);
    }

    public static boolean isReadable(Path filePath) {
        return Files.isReadable(filePath);
    }

    public static boolean isWritable(Path filePath) {
        return Files.isWritable(filePath);
    }

    public static boolean isExecutable(Path filePath) {
        return Files.isExecutable(filePath);
    }

    public static boolean isHidden(Path filePath) throws IOException {
        return Files.isHidden(filePath);
    }

    public static void allocateSize(FileChannel outChannel, long size) throws IOException {
        outChannel.position(size - 1);
        outChannel.write(NioBufferUtils.getByteBuffer(1, false));
    }

}
