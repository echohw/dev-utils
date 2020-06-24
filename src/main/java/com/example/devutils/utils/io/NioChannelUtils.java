package com.example.devutils.utils.io;

import com.example.devutils.dep.Range;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.AsynchronousByteChannel;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.channels.DatagramChannel;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.channels.Pipe;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.Charset;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.concurrent.Future;
import java.util.function.Consumer;

/**
 * NIO下的通道操作工具类
 * Created by AMe on 2020-06-12 13:51.
 */
public class NioChannelUtils {

    public static FileChannel getFileChannel(Path filePath, OpenOption... options) throws IOException {
        return FileChannel.open(filePath, options);
    }

    public static FileChannel getFileChannel(FileInputStream fileInputStream) {
        return fileInputStream.getChannel();
    }

    public static FileChannel getFileChannel(FileOutputStream fileOutputStream) {
        return fileOutputStream.getChannel();
    }

    public static FileChannel getFileChannel(RandomAccessFile randomAccessFile) {
        return randomAccessFile.getChannel();
    }

    public static DatagramChannel getDatagramChannel() throws IOException {
        return DatagramChannel.open();
    }

    public static Pipe getPipe() throws IOException {
        return Pipe.open();
    }

    public static SocketChannel getSocketChannel() throws IOException {
        return SocketChannel.open();
    }

    public static SocketChannel getSocketChannel(SocketAddress remote) throws IOException {
        return SocketChannel.open(remote);
    }

    public static ServerSocketChannel getServerSocketChannel() throws IOException {
        return ServerSocketChannel.open();
    }

    public static AsynchronousSocketChannel getAsynchronousSocketChannel() throws IOException {
        return AsynchronousSocketChannel.open();
    }

    public static AsynchronousSocketChannel getAsynchronousSocketChannel(AsynchronousChannelGroup channelGroup) throws IOException {
        return AsynchronousSocketChannel.open(channelGroup);
    }

    public static AsynchronousServerSocketChannel getAsynchronousServerSocketChannel() throws IOException {
        return AsynchronousServerSocketChannel.open();
    }

    public static AsynchronousServerSocketChannel getAsynchronousServerSocketChannel(AsynchronousChannelGroup channelGroup) throws IOException {
        return AsynchronousServerSocketChannel.open(channelGroup);
    }

    public static byte[] readAsBytes(ReadableByteChannel channel, ByteBuffer byteBuffer) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byteBuffer.clear();
        while (channel.read(byteBuffer) > 0) {
            byteBuffer.flip();
            outputStream.write(byteBuffer.array(), 0, byteBuffer.limit());
            byteBuffer.clear();
        }
        return outputStream.toByteArray();
    }

    public static byte[] readAsBytes(FileChannel channel, ByteBuffer byteBuffer, long position, long size) throws IOException {
        if (position + size > channel.size()) {
            throw new IllegalArgumentException("The read position will exceed the file size");
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        long readCount = 0;
        long len;
        byteBuffer.clear();
        while (readCount < size) {
            if (readCount + byteBuffer.limit() > size) {
                int limit = (int) (size - readCount);
                byteBuffer.limit(limit);
            }
            if ((len = channel.read(byteBuffer, position)) <= 0) {
                break;
            }
            byteBuffer.flip();
            outputStream.write(byteBuffer.array(), 0, byteBuffer.limit());
            byteBuffer.clear();
            readCount += len;
            position += len;
        }
        return outputStream.toByteArray();
    }

    public static byte[] readAsBytes(FileChannel channel, long position, int size) throws IOException {
        MappedByteBuffer mappedByteBuffer = channel.map(MapMode.READ_ONLY, position, size);
        return mappedByteBuffer.array();
    }

    public static String readAsString(ReadableByteChannel channel, ByteBuffer byteBuffer, Charset charset) throws IOException {
        byte[] bytes = readAsBytes(channel, byteBuffer);
        return new String(bytes, charset);
    }

    public static int writeBytes(WritableByteChannel channel, byte[] bytes) throws IOException {
        ByteBuffer byteBuffer = NioBufferUtils.getByteBuffer(bytes);
        return channel.write(byteBuffer);
    }

    public static int writeBytes(FileChannel channel, byte[] bytes, long position) throws IOException {
        ByteBuffer byteBuffer = NioBufferUtils.getByteBuffer(bytes);
        return channel.write(byteBuffer, position);
    }

    public static Future<Integer> writeBytes(AsynchronousByteChannel channel, byte[] bytes) {
        ByteBuffer byteBuffer = NioBufferUtils.getByteBuffer(bytes);
        return channel.write(byteBuffer);
    }

    public static <A> int writeBytes(AsynchronousByteChannel channel, byte[] bytes, A attachment, CompletionHandler<Integer,? super A> handler) {
        ByteBuffer byteBuffer = NioBufferUtils.getByteBuffer(bytes);
        channel.write(byteBuffer, attachment, handler);
        return bytes.length;
    }

    public static int writeString(WritableByteChannel channel, String content, Charset charset) throws IOException {
        writeBytes(channel, content.getBytes(charset));
        return content.length();
    }

    public static int writeString(FileChannel channel, String content, long position, Charset charset) throws IOException {
        writeBytes(channel, content.getBytes(charset), position);
        return content.length();
    }

    public static long copy(ReadableByteChannel readChannel, WritableByteChannel writeChannel, ByteBuffer byteBuffer) throws IOException {
        return copy(readChannel, writeChannel, byteBuffer, null);
    }

    public static long copy(ReadableByteChannel readChannel, WritableByteChannel writeChannel, ByteBuffer byteBuffer, Consumer<Range<Long>> noticeConsumer) throws IOException {
        noticeConsumer = noticeConsumer == null ? obj -> {} : noticeConsumer;
        long readCount = 0;
        int len;
        byteBuffer.clear();
        while ((len = readChannel.read(byteBuffer)) > 0) {
            byteBuffer.flip();
            writeChannel.write(byteBuffer);
            byteBuffer.clear();
            noticeConsumer.accept(new Range<>(readCount + 1, readCount + len));
            readCount += len;
        }
        return readCount;
    }

    public static long copy(FileChannel readChannel, FileChannel writeChannel, long readPosition, int size) throws IOException {
        return readChannel.transferTo(readPosition, size, writeChannel);
    }

    public static long copy(FileChannel readChannel, FileChannel writeChannel, long readPosition, long writePosition, int size) throws IOException {
        MappedByteBuffer readMappedByteBuffer = readChannel.map(MapMode.READ_ONLY, readPosition, size);
        MappedByteBuffer writeMappedByteBuffer = writeChannel.map(MapMode.READ_WRITE, writePosition, size);
        writeMappedByteBuffer.put(readMappedByteBuffer);
        return readMappedByteBuffer.limit();
    }

    public static long copy(FileChannel readChannel, FileChannel writeChannel, ByteBuffer byteBuffer, long readPosition, long writePosition, long size) throws IOException {
        return copy(readChannel, writeChannel, byteBuffer, readPosition, writePosition, size, null);
    }

    public static long copy(FileChannel readChannel, FileChannel writeChannel, ByteBuffer byteBuffer, long readPosition, long writePosition, long size, Consumer<Range<Long>> noticeConsumer) throws IOException {
        if (readPosition + size > readChannel.size()) {
            throw new IllegalArgumentException("The read position will exceed the file size");
        }
        noticeConsumer = noticeConsumer == null ? obj -> {} : noticeConsumer;
        long readCount = 0;
        long len;
        byteBuffer.clear();
        while (readCount < size) {
            if (readCount + byteBuffer.limit() > size) {
                int limit = (int) (size - readCount);
                byteBuffer.limit(limit);
            }
            if ((len = readChannel.read(byteBuffer, readPosition)) <= 0) {
                break;
            }
            byteBuffer.flip();
            writeChannel.write(byteBuffer, writePosition);
            byteBuffer.clear();
            noticeConsumer.accept(new Range<>(readCount + 1, readCount + len));
            readCount += len;
            readPosition += len;
            writePosition += len;
        }
        return readCount;
    }

}
