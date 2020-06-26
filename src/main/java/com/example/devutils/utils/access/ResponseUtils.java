package com.example.devutils.utils.access;

import com.example.devutils.dep.Charsets;
import com.example.devutils.dep.MediaTypes;
import com.example.devutils.utils.collection.MapUtils;
import com.example.devutils.utils.io.StreamUtils;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import org.jooq.lambda.Unchecked;

/**
 * Created by AMe on 2020-06-22 14:39.
 */
public class ResponseUtils {

    public static void redirect(HttpServletResponse response, String url) throws IOException {
        response.sendRedirect(url);
    }

    public static void setHeaders(HttpServletResponse response, Map<String, String> respHeaders) {
        if (MapUtils.isNotEmpty(respHeaders)) {
            respHeaders.forEach(response::setHeader);
        }
    }

    public static void writeText(HttpServletResponse response, String content) throws IOException {
        writeText(response, content, MediaTypes.TEXT_PLAIN);
    }

    public static void writeText(HttpServletResponse response, String content, String contentType) throws IOException {
        response.setContentType(contentType);
        response.setCharacterEncoding(Charsets.UTF_8.name());
        response.getWriter().write(content);
    }

    public static void writeWord(HttpServletResponse response, File file, String fileName) throws IOException {
        writeFile(response, file, true, fileName, MediaTypes.APPLICATION_WORD_DOC);
    }

    public static void writeExcel(HttpServletResponse response, File file, String fileName) throws IOException {
        writeFile(response, file, true, fileName, MediaTypes.APPLICATION_EXCEL_XLS);
    }

    public static void writePdf(HttpServletResponse response, File file, boolean attachment, String fileName) throws IOException {
        writeFile(response, file, attachment, fileName, MediaTypes.APPLICATION_PDF);
    }

    public static void writeFile(HttpServletResponse response, File file, boolean attachment, String fileName, String contentType) throws IOException {
        Map<String, String> respHeaders = MapUtils.of(LinkedHashMap::new,
            "Content-Disposition", (attachment ? "attachment;" : "") + "filename=" + fileName,
            "Content-Length", file.length() + "",
            "Content-Type", contentType
        );
        writeFile(response, respHeaders, file);
    }

    public static void writeFile(HttpServletResponse response, Map<String, String> respHeaders, File file) throws IOException {
        try (
            BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(file));
        ) {
            write(response, respHeaders, inputStream);
        }
    }

    public static void write(HttpServletResponse response, Map<String, String> respHeaders, InputStream inputStream) throws IOException {
        write(response, respHeaders, Unchecked.consumer(outputStream -> StreamUtils.copy(inputStream, outputStream)));
    }

    public static void write(HttpServletResponse response, Map<String, String> respHeaders, Consumer<OutputStream> consumer) throws IOException {
        response.reset();
        setHeaders(response, respHeaders);
        response.setCharacterEncoding(Charsets.UTF_8.name());
        try (
            ServletOutputStream outputStream = response.getOutputStream();
        ) {
            consumer.accept(outputStream);
        }
    }

}
