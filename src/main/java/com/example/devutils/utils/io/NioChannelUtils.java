package com.example.devutils.utils.io;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousByteChannel;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.channels.DatagramChannel;
import java.nio.channels.FileChannel;
import java.nio.channels.Pipe;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.Charset;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.concurrent.Future;

/**
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

    public static String readAsString(ReadableByteChannel channel, int bufferSize, Charset charset) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = readAsBytes(channel, bufferSize, new ByteArrayOutputStream());
        return new String(byteArrayOutputStream.toByteArray(), charset);
    }

    public static ByteArrayOutputStream readAsBytes(ReadableByteChannel channel, int bufferSize, ByteArrayOutputStream outputStream) throws IOException {
        ByteBuffer byteBuffer = NioBufferUtils.getByteBuffer(bufferSize, false);
        while (channel.read(byteBuffer) > 0) {
            byteBuffer.flip();
            outputStream.write(byteBuffer.array(), 0, byteBuffer.limit());
            byteBuffer.clear();
        }
        return outputStream;
    }

    public static int writeString(WritableByteChannel channel, String content, Charset charset) throws IOException {
        return writeBytes(channel, content.getBytes(charset));
    }

    public static Future<Integer> writeString(AsynchronousByteChannel channel, String content, Charset charset) throws IOException {
        return writeBytes(channel, content.getBytes(charset));
    }

    public static <A> void writeString(AsynchronousByteChannel channel, String content, Charset charset, A attachment, CompletionHandler<Integer, ? super A> handler) {
        writeBytes(channel, content.getBytes(charset), attachment, handler);
    }

    public static int writeBytes(WritableByteChannel channel, byte[] bytes) throws IOException {
        ByteBuffer byteBuffer = NioBufferUtils.getByteBuffer(bytes);
        return channel.write(byteBuffer);
    }

    public static Future<Integer> writeBytes(AsynchronousByteChannel channel, byte[] bytes) {
        ByteBuffer byteBuffer = NioBufferUtils.getByteBuffer(bytes);
        return channel.write(byteBuffer);
    }

    public static <A> void writeBytes(AsynchronousByteChannel channel, byte[] bytes, A attachment, CompletionHandler<Integer,? super A> handler) {
        ByteBuffer byteBuffer = NioBufferUtils.getByteBuffer(bytes);
        channel.write(byteBuffer, attachment, handler);
    }

}
