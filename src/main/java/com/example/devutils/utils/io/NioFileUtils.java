package com.example.devutils.utils.io;

import com.example.devutils.dep.Range;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.FileAttribute;
import java.util.function.BiConsumer;

/**
 * Created by AMe on 2020-06-12 14:00.
 */
public class NioFileUtils {

    public static Path createFile(Path filePath, FileAttribute<?>... attrs) throws IOException {
        DirectoryUtils.createPlaceDirs(filePath);
        return Files.createFile(filePath, attrs);
    }

    public static Path createTempFile(String prefix, String suffix, FileAttribute<?>... attrs)
        throws IOException {
        return Files.createTempFile(prefix, suffix, attrs);
    }

    public static Path createTempFile(Path basePath, String prefix, String suffix,
        FileAttribute<?>... attrs) throws IOException {
        DirectoryUtils.createPlaceDirs(basePath);
        return Files.createTempFile(basePath, prefix, suffix, attrs);
    }

    public static Path copy(Path srcFilePath, Path destFilePath) throws IOException {
        return Files.copy(srcFilePath, destFilePath, StandardCopyOption.REPLACE_EXISTING);
    }

    public static Path move(Path srcFilePath, Path destFilePath) throws IOException {
        return Files.move(srcFilePath, destFilePath, StandardCopyOption.ATOMIC_MOVE);
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

    public static long getSize(Path filePath) throws IOException {
        return Files.size(filePath);
    }

    public static byte[] readAllBytes(Path filePath) throws IOException {
        return Files.readAllBytes(filePath);
    }

    public static String readAllText(Path filePath, Charset charset) throws IOException {
        byte[] bytes = Files.readAllBytes(filePath);
        return new String(bytes, charset);
    }

    public static Path writeBytes(Path filePath, byte[] bytes) throws IOException {
        return Files.write(filePath, bytes);
    }

    public static Path writeText(Path filePath, String text) throws IOException {
        return Files.write(filePath, text.getBytes(StandardCharsets.UTF_8));
    }

    private static long preCopy(Path srcFilePath, Path destFilePath) throws IOException {
        if (PathUtils.notExists(destFilePath)) {
            createFile(destFilePath);
        }
        return Files.size(srcFilePath);
    }

    public static long copy(Path srcFilePath, Path destFilePath, int batchSize, BiConsumer<Long, Range<Long>> noticeConsumer) throws IOException {
        long fileSize = preCopy(srcFilePath, destFilePath);
        if (fileSize == 0) {
            return 0;
        }
        try (
            FileChannel inChannel = FileChannel.open(srcFilePath, StandardOpenOption.READ);
            FileChannel outChannel = FileChannel.open(destFilePath, StandardOpenOption.READ, StandardOpenOption.WRITE, StandardOpenOption.CREATE);
        ) {
            return copy(inChannel, outChannel, batchSize, noticeConsumer);
        } catch (IOException ex) {
            throw ex;
        }
    }

    public static long copy(FileChannel inChannel, FileChannel outChannel, int batchSize, BiConsumer<Long, Range<Long>> noticeConsumer) throws IOException {
        long fileSize = inChannel.size();
        long readCount = 0;
        long len;
        for (;readCount < fileSize; readCount += len) {
            len = inChannel.transferTo(readCount, batchSize, outChannel);
            if (noticeConsumer != null) {
                noticeConsumer.accept(fileSize, new Range<>(readCount + 1, readCount + len));
            }
        }
        return readCount;
    }

    public static long copy(Path srcFilePath, Path destFilePath, int bufferSize, boolean direct, BiConsumer<Long, Range<Long>> noticeConsumer) throws IOException {
        long fileSize = preCopy(srcFilePath, destFilePath);
        if (fileSize == 0) {
            return 0;
        }
        try (
            FileChannel inChannel = FileChannel.open(srcFilePath, StandardOpenOption.READ);
            FileChannel outChannel = FileChannel.open(destFilePath, StandardOpenOption.READ, StandardOpenOption.WRITE, StandardOpenOption.CREATE);
        ) {
            return copy(inChannel, outChannel, bufferSize, direct, noticeConsumer);
        } catch (IOException ex) {
            throw ex;
        }
    }

    public static long copy(FileChannel inChannel, FileChannel outChannel, int bufferSize, boolean direct, BiConsumer<Long, Range<Long>> noticeConsumer) throws IOException {
        long fileSize = inChannel.size();
        ByteBuffer byteBuffer = NioBufferUtils.getByteBuffer(bufferSize, direct);
        long readCount = 0;
        int len;
        while ((len = inChannel.read(byteBuffer)) > 0) {
            byteBuffer.flip();
            outChannel.write(byteBuffer);
            byteBuffer.clear();
            if (noticeConsumer != null) {
                noticeConsumer.accept(fileSize, new Range<>(readCount + 1, readCount + len));
            }
            readCount += len;
        }
        return readCount;
    }

}
