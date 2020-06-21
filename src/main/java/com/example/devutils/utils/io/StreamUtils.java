package com.example.devutils.utils.io;

import com.example.devutils.dep.Range;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Created by AMe on 2020-06-20 15:21.
 */
public class StreamUtils {

    public static final int BUFFER_SIZE = 4096;

    public static FileInputStream getFileInputStream(File file) throws FileNotFoundException {
        return new FileInputStream(file);
    }

    public static BufferedInputStream getBufferedInputStream(InputStream inputStream) {
        return new BufferedInputStream(inputStream);
    }

    public static BufferedInputStream getBufferedInputStream(InputStream inputStream, int bufferSize) {
        return new BufferedInputStream(inputStream, bufferSize);
    }

    public static ZipInputStream getZipInputStream(InputStream inputStream) {
        return new ZipInputStream(inputStream);
    }

    public static ZipInputStream getZipInputStream(InputStream inputStream, Charset charset) {
        return new ZipInputStream(inputStream, charset);
    }

    public static ByteArrayInputStream getByteArrayInputStream(byte[] bytes) {
        return new ByteArrayInputStream(bytes);
    }

    public static FileOutputStream getFileOutputStream(File file) throws FileNotFoundException {
        return new FileOutputStream(file);
    }

    public static BufferedOutputStream getBufferedOutputStream(OutputStream outputStream) {
        return new BufferedOutputStream(outputStream);
    }

    public static BufferedOutputStream getBufferedOutputStream(OutputStream outputStream, int bufferSize) {
        return new BufferedOutputStream(outputStream, bufferSize);
    }

    public static ZipOutputStream getZipOutputStream(OutputStream outputStream) {
        return new ZipOutputStream(outputStream);
    }

    public static ZipOutputStream getZipOutputStream(OutputStream outputStream, Charset charset) {
        return new ZipOutputStream(outputStream, charset);
    }

    public static ByteArrayOutputStream getByteArrayOutputStream() {
        return new ByteArrayOutputStream();
    }

    public static InputStreamReader getInputStreamReader(InputStream inputStream, Charset charset) {
        return new InputStreamReader(inputStream, charset);
    }

    public static BufferedReader getBufferedReader(Reader reader) {
        return new BufferedReader(reader);
    }

    public static BufferedReader getBufferedReader(Reader reader, int bufferSize) {
        return new BufferedReader(reader, bufferSize);
    }

    public static StringReader getStringReader(String content) {
        return new StringReader(content);
    }

    public static OutputStreamWriter getOutputStreamWriter(OutputStream outputStream, Charset charset) {
        return new OutputStreamWriter(outputStream, charset);
    }

    public static BufferedWriter getBufferedWriter(Writer writer) {
        return new BufferedWriter(writer);
    }

    public static BufferedWriter getBufferedWriter(Writer writer, int bufferSize) {
        return new BufferedWriter(writer, bufferSize);
    }

    public static StringWriter getStringWriter() {
        return new StringWriter();
    }

    public static byte[] readAsBytes(InputStream inputStream) throws IOException {
        return readAsBytes(inputStream, BUFFER_SIZE);
    }

    public static byte[] readAsBytes(InputStream inputStream, int bufferSize) throws IOException {
        ByteArrayOutputStream outputStream = getByteArrayOutputStream();
        byte[] bytes = new byte[bufferSize];
        int len;
        while ((len = inputStream.read(bytes)) > 0) {
            outputStream.write(bytes, 0, len);
        }
        return outputStream.toByteArray();
    }

    public static String readAsString(InputStream inputStream, Charset charset) throws IOException {
        return readAsString(inputStream, BUFFER_SIZE, charset);
    }

    public static String readAsString(InputStream inputStream, int bufferSize, Charset charset) throws IOException {
        return new String(readAsBytes(inputStream, bufferSize), charset);
    }

    public static String readAsString(Reader reader) throws IOException {
        return readAsString(reader, BUFFER_SIZE);
    }

    public static String readAsString(Reader reader, int bufferSize) throws IOException {
        StringWriter writer = getStringWriter();
        char[] chars = new char[bufferSize];
        int len;
        while ((len = reader.read(chars)) > 0) {
            writer.write(chars, 0, len);
        }
        return writer.toString();
    }

    public static List<String> readAsLines(BufferedReader bufferedReader) throws IOException {
        ArrayList<String> lineList = new ArrayList<>();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            lineList.add(line);
        }
        return lineList;
    }

    public static int writeBytes(OutputStream outputStream, byte[] bytes) throws IOException {
        outputStream.write(bytes);
        return bytes.length;
    }

    public static int writeString(OutputStream outputStream, String content, Charset charset) throws IOException {
        return writeBytes(outputStream, content.getBytes(charset));
    }

    public static int writeString(Writer writer, String content) throws IOException {
        writer.write(content);
        return content.length();
    }

    public static long copy(InputStream inputStream, OutputStream outputStream) throws IOException {
        return copy(inputStream, outputStream, BUFFER_SIZE, null);
    }

    public static long copy(InputStream inputStream, OutputStream outputStream, int bufferSize, Consumer<Range<Long>> noticeConsumer) throws IOException {
        noticeConsumer = noticeConsumer == null ? obj -> {} : noticeConsumer;
        byte[] bytes = new byte[bufferSize];
        long readCount = 0;
        int len;
        while ((len = inputStream.read(bytes)) > 0) {
            outputStream.write(bytes, 0, len);
            noticeConsumer.accept(new Range<>(readCount + 1, readCount + len));
            readCount += len;
        }
        return readCount;
    }

    public static long copy(Reader reader, Writer writer) throws IOException {
        return copy(reader, writer, BUFFER_SIZE, null);
    }

    public static long copy(Reader reader, Writer writer, int bufferSize, Consumer<Range<Long>> noticeConsumer) throws IOException {
        noticeConsumer = noticeConsumer == null ? obj -> {} : noticeConsumer;
        char[] chars = new char[bufferSize];
        long readCount = 0;
        int len;
        while ((len = reader.read(chars)) > 0) {
            writer.write(chars, 0, len);
            noticeConsumer.accept(new Range<>(readCount + 1, readCount + len));
            readCount += len;
        }
        return readCount;
    }

}
