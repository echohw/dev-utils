package com.example.devutils.utils.io;

import com.example.devutils.inter.DataAgency;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Collection;
import java.util.Comparator;
import java.util.stream.IntStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

/**
 * Created by AMe on 2020-06-08 01:16.
 */
public class RandomAccessFileUtils {

    private static final Logger logger = LoggerFactory.getLogger(RandomAccessFileUtils.class);
    private static final int BUF_SIZE = 4096;

    /**
     * 插入数据
     */
    public static void insert(File file, long position, InputStream dataStream, DataAgency<InputStream, OutputStream> dataAgency) throws IOException {
        long fileSize = file.length();
        Assert.isTrue(position >= -1, "position ...");
        if (position == -1) {
            position = fileSize;
        }
        try (
            RandomAccessFile randomAccessFile = getRandomAccessFile(file, "rw");
        ) {
            if (position < fileSize) {
                try (
                    OutputStream backOutStream = dataAgency.getOut();
                ) {
                    copy(randomAccessFile, backOutStream, position, -1); // 备份position位置之后数据
                } catch (IOException ex) {
                    throw ex;
                }
            }
            long copyCount = copy(dataStream, randomAccessFile, position);// 插入数据
            if (position < fileSize) {
                try (
                    InputStream backInStream = dataAgency.getIn();
                ) {
                    copy(backInStream, randomAccessFile, position + copyCount); // 回填数据
                } catch (IOException ex) {
                    throw ex;
                }
            }
        } catch (IOException ex) {
            throw ex;
        }
    }

    public static void remove(File file, long position, long length, DataAgency<InputStream, OutputStream> dataAgency) throws IOException {
        long fileSize = file.length();
        Assert.isTrue(position >= 0 && position < fileSize, "position ...");
        Assert.isTrue(length > 0 && length <= fileSize, "fileSize ...");
        try (
            RandomAccessFile readFile = getRandomAccessFile(file, "rw");
            RandomAccessFile writeFile = getRandomAccessFile(file, "rw");
        ) {
            OutputStream outputStream = dataAgency.getOut();
            if (outputStream != null) {
                copy(readFile, outputStream, position, length);
            }
            copy(readFile, position + length, writeFile, position, fileSize - (position + length));
            writeFile.setLength(fileSize - length);
        } catch (IOException ex) {
            throw ex;
        }
    }

    /**
     * 合并文件
     */
    public static void merge(Collection<File> srcFiles, File destFile, Comparator<File> comparator) {
        try (
            RandomAccessFile randomAccessFile = getRandomAccessFile(destFile, "rw");
        ) {
            long readCount = 0;
            for (File srcFile : srcFiles) {
                try (
                    FileInputStream fileInputStream = new FileInputStream(srcFile);
                ) {
                    copy(fileInputStream, randomAccessFile, readCount);
                } catch (IOException ex) {
                    throw new RuntimeException(ex.getMessage());
                }
                readCount += srcFile.length();
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex.getMessage());
        }

    }

    /**
     * 按块数量切分文件
     */
    public static void part(File file, String basePath, int blockNum) throws IOException {
        part(file, basePath, blockNum, -1);
    }

    /**
     * 按块大小切分文件
     */
    public static void part(File file, String basePath, long blockSize) throws IOException {
        part(file, basePath, -1, blockSize);
    }

    private static void part(File file, String basePath, int blockNum, long blockSize) throws IOException {
        long length = file.length();
        RandomAccessFile randomAccessFile = getRandomAccessFile(file, "rw");
        if (blockNum == -1 && blockSize == -1) {
            return;
        } else if (blockNum == -1) { // 块大小
            blockNum = (int) (length / blockSize);
            blockNum = blockNum * blockSize < length ? blockNum + 1 : blockNum;
        } else { // 块数量
            blockSize = length / blockNum;
        }
        for (int i = 0; i < blockNum; i++) {
            long start = i * blockSize;
            long end = (i == blockNum - 1) ? length : start + blockSize;
            String fileName = i + "";
            File partFile = new File(basePath, fileName);
            try (
                FileOutputStream fileOutputStream = new FileOutputStream(partFile);
            ) {
                copy(randomAccessFile, fileOutputStream, start, end - start);
            } catch (Exception ex) {
                logger.error(ex.getMessage());
            }
        }
    }

    /**
     * 流数据拷贝
     * @param randomAccessFile
     * @param outputStream
     * @param position 文件指针位置
     * @param readSize 拷贝的字节数
     * @return
     * @throws IOException
     */
    public static long copy(RandomAccessFile randomAccessFile, OutputStream outputStream, long position, long readSize) throws IOException {
        return copy(randomAccessFile, outputStream, position, readSize, BUF_SIZE);
    }

    /**
     * 流数据拷贝
     * @param randomAccessFile
     * @param outputStream
     * @param position 文件指针位置
     * @param readSize 拷贝的字节数
     * @param bufSize 缓冲区大小
     * @return
     * @throws IOException
     */
    public static long copy(RandomAccessFile randomAccessFile, OutputStream outputStream, long position, long readSize, int bufSize) throws IOException {
        long fileSize = randomAccessFile.length();
        Assert.isTrue(position >= 0 && position < fileSize, "position ...");
        Assert.isTrue(readSize >= -1 && readSize <= fileSize, "readSize ...");
        if (readSize == -1) {
            readSize = fileSize - position;
        }
        long readCount = 0;
        boolean readOver = false;
        byte[] byteBuf = new byte[bufSize];
        int len;
        randomAccessFile.seek(position);
        while (!readOver) {
            if (readSize - readCount < bufSize) {
                len = randomAccessFile.read(byteBuf, 0, (int) (readSize - readCount));
                readOver = true;
            } else {
                len = randomAccessFile.read(byteBuf);
            }
            if (len > 0) {
                outputStream.write(byteBuf, 0, len);
                readCount += len;
            }
        }
        return readCount;
    }

    public static long copy(InputStream inputStream, RandomAccessFile randomAccessFile, long position) throws IOException {
        return copy(inputStream, randomAccessFile, position, BUF_SIZE);
    }

    public static long copy(InputStream inputStream, RandomAccessFile randomAccessFile, long position, int bufSize) throws IOException {
        long readCount = 0;
        byte[] byteBuf = new byte[bufSize];
        int len;
        randomAccessFile.seek(position);
        while ((len = inputStream.read(byteBuf)) != -1) {
            randomAccessFile.write(byteBuf, 0, len);
            readCount += len;
        }
        return readCount;
    }

    public static long copy(RandomAccessFile readFile, long readFilePosi, RandomAccessFile writeFile, long writeFilePosi, long length) throws IOException {
        return copy(readFile, readFilePosi, writeFile, writeFilePosi, length, BUF_SIZE);
    }

    public static long copy(RandomAccessFile readFile, long readFilePosi, RandomAccessFile writeFile, long writeFilePosi, long length, int bufSize) throws IOException {
        long readCount = 0;
        boolean readOver = false;
        byte[] byteBuf = new byte[bufSize];
        int len;
        readFile.seek(readFilePosi);
        writeFile.seek(writeFilePosi);
        while (!readOver) {
            if (length - readCount < bufSize) {
                len = readFile.read(byteBuf, 0, (int) (length - readCount));
                readOver = true;
            } else {
                len = readFile.read(byteBuf);
            }
            if (len > 0) {
                writeFile.write(byteBuf, 0, len);
                readCount += len;
            }
        }
        return readCount;
    }

    public static long copy(RandomAccessFile readRandomFile, RandomAccessFile writeRandomFile, int bufferSize, int bufferNum, boolean direct) throws IOException {
        try (
            FileChannel inChannel = readRandomFile.getChannel();
            FileChannel outChannel = writeRandomFile.getChannel();
        ) {
            ByteBuffer[] buffers = IntStream.range(0, bufferNum).mapToObj(num -> direct ? ByteBuffer.allocateDirect(bufferSize) : ByteBuffer.allocate(bufferSize)).toArray(ByteBuffer[]::new);
            long len;
            long readCount = 0;
            while ((len = inChannel.read(buffers)) != -1) {
                outChannel.write(buffers);
                readCount += len;
            }
            return readCount;
        } catch (IOException ex) {
            throw ex;
        }
    }


    public static RandomAccessFile getRandomAccessFile(File file, String accessMode) throws FileNotFoundException {
        return new RandomAccessFile(file, accessMode);
    }

}
