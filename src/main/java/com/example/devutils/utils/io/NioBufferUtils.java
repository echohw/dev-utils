package com.example.devutils.utils.io;

import com.example.devutils.dep.Charsets;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

/**
 * NIO下的缓冲区操作工具类
 * Created by AMe on 2020-06-12 14:09.
 */
public class NioBufferUtils {

    public static ByteBuffer getByteBuffer(int capacity, boolean direct) {
        return direct ? ByteBuffer.allocateDirect(capacity) : ByteBuffer.allocate(capacity);
    }

    public static ByteBuffer getByteBuffer(String content) {
        return getByteBuffer(content, Charsets.UTF_8);
    }

    public static ByteBuffer getByteBuffer(String content, Charset charset) {
        return charset.encode(content);
    }

    public static ByteBuffer getByteBuffer(CharBuffer charBuffer, Charset charset) {
        return charset.encode(charBuffer);
    }

    public static ByteBuffer getByteBuffer(byte[] bytes) {
        return ByteBuffer.wrap(bytes);
    }

    public static CharBuffer getCharBuffer(int capacity) {
        return CharBuffer.allocate(capacity);
    }

    public static CharBuffer getCharBuffer(ByteBuffer byteBuffer, Charset charset) {
        return charset.decode(byteBuffer);
    }

}
