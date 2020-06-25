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
import java.util.Map.Entry;
import java.util.function.Consumer;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import org.jooq.lambda.Unchecked;

/**
 * Created by AMe on 2020-06-22 14:39.
 */
public class ResponseUtils {

    public static void writeText(HttpServletResponse response, String content) throws IOException {
        response.setContentType(MediaTypes.TEXT_PLAIN);
        response.setCharacterEncoding(Charsets.UTF_8.name());
        response.getWriter().write(content);
    }

    public static void writeWord(HttpServletResponse response, File file, String fileName) throws IOException {
        writeFile(response, file, fileName, MediaTypes.APPLICATION_WORD_DOC);
    }

    public static void writeExcel(HttpServletResponse response, File file, String fileName) throws IOException {
        writeFile(response, file, fileName, MediaTypes.APPLICATION_EXCEL_XLS);
    }

    public static void writePdf(HttpServletResponse response, File file, String fileName) throws IOException {
        writeFile(response, file, fileName, MediaTypes.APPLICATION_PDF);
    }

    public static void writeFile(HttpServletResponse response, File file, String fileName, String contentType) throws IOException {
        Map<String, String> respHeaders = MapUtils.of(LinkedHashMap::new,
            "Content-Disposition", "attachment;filename=" + fileName,
            "Content-Length", file.length() + "",
            "Content-Type", contentType
        );
        writeFile(response, respHeaders, file);
    }

    public static void writeFile(HttpServletResponse response, Map<String, String> respHeaders, File file) throws IOException {
        try (
            BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(file));
        ) {
            writeFile(response, respHeaders, inputStream);
        }
    }

    public static void writeFile(HttpServletResponse response, Map<String, String> respHeaders, InputStream inputStream) throws IOException {
        writeFile(response, respHeaders, Unchecked.consumer(outputStream -> StreamUtils.copy(inputStream, outputStream)));
    }

    public static void writeFile(HttpServletResponse response, Map<String, String> respHeaders, Consumer<OutputStream> consumer) throws IOException {
        response.reset();
        if (MapUtils.isNotEmpty(respHeaders)) {
            for (Entry<String, String> entry : respHeaders.entrySet()) {
                response.setHeader(entry.getKey(), entry.getValue());
            }
        }
        response.setCharacterEncoding(Charsets.UTF_8.name());
        try (
            ServletOutputStream outputStream = response.getOutputStream();
        ) {
            consumer.accept(outputStream);
        }
    }

}
