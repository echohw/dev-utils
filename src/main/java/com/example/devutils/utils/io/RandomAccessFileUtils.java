package com.example.devutils.utils.io;

import com.example.devutils.inter.DataAgency;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.Collection;

/**
 * 文件随机读写操作工具类
 * Created by AMe on 2020-06-08 01:16.
 */
public class RandomAccessFileUtils {

    public static final int BUFFER_SIZE = 4096;

    public static RandomAccessFile getRandomAccessFile(File file, String accessMode) throws FileNotFoundException {
        return new RandomAccessFile(file, accessMode);
    }

    public static long getSize(RandomAccessFile randomAccessFile) throws IOException {
        return randomAccessFile.length();
    }

    /**
     * 插入字节数据
     */
    public static long insertBytes(File file, long position, InputStream dataStream, DataAgency<InputStream, OutputStream> dataAgency) throws IOException {
        long fileSize = file.length();
        try (
            RandomAccessFile randomAccessFile = getRandomAccessFile(file, "rw");
        ) {
            boolean backup = position < fileSize;
            if (backup) {
                try (
                    OutputStream backOutStream = dataAgency.getOut();
                ) {
                    copy(randomAccessFile, backOutStream, position, fileSize - position); // 备份position位置之后的数据
                }
            }
            long insertCount = copy(dataStream, randomAccessFile, position); // 插入数据
            if (backup) {
                try (
                    InputStream backInStream = dataAgency.getIn();
                ) {
                    copy(backInStream, randomAccessFile, position + insertCount); // 回填数据
                }
            }
            return insertCount;
        }
    }

    /**
     * 移除字节数据
     */
    public static void removeBytes(File file, long position, long size) throws IOException {
        long fileSize = file.length();
        if (position + size > fileSize) {
            throw new IllegalArgumentException("The read position will exceed the file size");
        }
        try (
            RandomAccessFile readFile = getRandomAccessFile(file, "rw");
            RandomAccessFile writeFile = getRandomAccessFile(file, "rw");
        ) {
            copy(readFile, position + size, writeFile, position, fileSize - (position + size));
            writeFile.setLength(fileSize - size);
        }
    }

    public static byte[] readAsBytes(File file, long position, long size) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (
            RandomAccessFile randomAccessFile = getRandomAccessFile(file, "r");
        ) {
            copy(randomAccessFile, outputStream, position, size);
            return outputStream.toByteArray();
        }
    }

    /**
     * 合并文件
     */
    public static long mergeFile(Collection<File> srcFiles, File destFile) throws IOException {
        try (
            RandomAccessFile randomAccessFile = getRandomAccessFile(destFile, "rw");
        ) {
            long readCount = 0;
            for (File srcFile : srcFiles) {
                try (
                    BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(srcFile));
                ) {
                    copy(inputStream, randomAccessFile, readCount);
                }
                readCount += srcFile.length();
            }
            return readCount;
        }
    }

    /**
     * 按块数量拆分文件
     */
    public static void splitFile(File file, String basePath, int blockNum) throws IOException {
        splitFile(file, basePath, blockNum, -1);
    }

    /**
     * 按块大小拆分文件
     */
    public static void splitFile(File file, String basePath, long blockSize) throws IOException {
        splitFile(file, basePath, -1, blockSize);
    }

    private static void splitFile(File file, String basePath, int blockNum, long blockSize) throws IOException {
        long fileSize = file.length();
        if (blockNum == -1 && blockSize == -1) {
            return;
        } else if (blockNum == -1) { // 只关注块大小
            blockNum = (int) Math.ceil((double) fileSize / blockSize);
        } else { // 只关注块数量
            blockSize = (long) Math.ceil((double) fileSize / blockNum);
        }
        if (blockNum * blockSize - fileSize >= blockSize) {
            throw new IllegalArgumentException("The read position will exceed the file size");
        }
        DirectoryUtils.createDirs(PathUtils.getPath(basePath));
        try (
            RandomAccessFile randomAccessFile = getRandomAccessFile(file, "rw");
        ) {
            for (int i = 0; i < blockNum; i++) {
                long position = i * blockSize;
                long size = (i == blockNum - 1) ? fileSize - position : blockSize;
                try (
                    BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(new File(basePath, i + "")));
                ) {
                    copy(randomAccessFile, outputStream, position, size);
                }
            }
        }
    }

    public static long copy(RandomAccessFile randomAccessFile, OutputStream outputStream, long position, long size) throws IOException {
        return copy(randomAccessFile, outputStream, position, size, BUFFER_SIZE);
    }

    public static long copy(RandomAccessFile randomAccessFile, OutputStream outputStream, long position, long size, int batchSize) throws IOException {
        if (position + size > getSize(randomAccessFile)) {
            throw new IllegalArgumentException("The read position will exceed the file size");
        }
        long readCount = 0;
        byte[] bytes = new byte[batchSize];
        int len;
        randomAccessFile.seek(position);
        while (readCount < size) {
            if (readCount + batchSize > size) {
                len = randomAccessFile.read(bytes, 0, (int) (size - readCount));
            } else {
                len = randomAccessFile.read(bytes);
            }
            if (len <= 0) {
                break;
            }
            outputStream.write(bytes, 0, len);
            readCount += len;
        }
        return readCount;
    }

    public static long copy(InputStream inputStream, RandomAccessFile randomAccessFile, long position) throws IOException {
        return copy(inputStream, randomAccessFile, position, BUFFER_SIZE);
    }

    public static long copy(InputStream inputStream, RandomAccessFile randomAccessFile, long position, int batchSize) throws IOException {
        long readCount = 0;
        byte[] bytes = new byte[batchSize];
        int len;
        randomAccessFile.seek(position);
        while ((len = inputStream.read(bytes)) > 0) {
            randomAccessFile.write(bytes, 0, len);
            readCount += len;
        }
        return readCount;
    }

    public static long copy(RandomAccessFile readFile, long readPosition, RandomAccessFile writeFile, long writePosition, long size) throws IOException {
        return copy(readFile, readPosition, writeFile, writePosition, size, BUFFER_SIZE);
    }

    public static long copy(RandomAccessFile readFile, long readPosition, RandomAccessFile writeFile, long writePosition, long size, int batchSize) throws IOException {
        if (readPosition + size > getSize(readFile)) {
            throw new IllegalArgumentException("The read position will exceed the file size");
        }
        long readCount = 0;
        byte[] bytes = new byte[batchSize];
        int len;
        readFile.seek(readPosition);
        writeFile.seek(writePosition);
        while (readCount < size) {
            if (readCount + batchSize > size) {
                len = readFile.read(bytes, 0, (int) (size - readCount));
            } else {
                len = readFile.read(bytes);
            }
            if (len <= 0) {
                break;
            }
            writeFile.write(bytes, 0, len);
            readCount += len;
        }
        return readCount;
    }

    public static void allocateSize(RandomAccessFile randomAccessFile, long size) throws IOException {
        randomAccessFile.setLength(size);
    }

}
