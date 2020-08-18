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
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import org.jooq.lambda.Unchecked;

/**
 * Created by AMe on 2020-06-22 14:39.
 */
public class ServletResponseUtils {

    public static void redirect(HttpServletResponse response, String url) throws IOException {
        response.sendRedirect(url);
    }

    public static void setHeaders(HttpServletResponse response, Map<String, String> respHeaders) {
        if (MapUtils.isNotEmpty(respHeaders)) {
            respHeaders.forEach(response::setHeader);
        }
    }

    public static void addCookie(HttpServletResponse response, String cookieName, String cookieValue, String path, int expiry) {
        Cookie cookie = new Cookie(cookieName, cookieValue);
        cookie.setPath(path);
        cookie.setMaxAge(expiry);
        response.addCookie(cookie);
    }

    public static void deleteCookie(HttpServletResponse response, String cookieName, String path) {
        addCookie(response, cookieName, "", path, 0);
    }

    public static void writeText(HttpServletResponse response, String content) throws IOException {
        writeText(response, content, MediaTypes.TEXT_PLAIN);
    }

    public static void writeHtml(HttpServletResponse response, String content) throws IOException {
        writeText(response, content, MediaTypes.TEXT_HTML);
    }

    public static void writeJson(HttpServletResponse response, String content) throws IOException {
        writeText(response, content, MediaTypes.APPLICATION_JSON_UTF8);
    }

    public static void writeText(HttpServletResponse response, String content, String contentType) throws IOException {
        response.setContentType(contentType);
        response.setCharacterEncoding(Charsets.UTF_8.name());
        response.getWriter().write(content);
    }

    public static void writeWord(HttpServletResponse response, File file, String fileName) throws IOException {
        writeFile(response, file, true, fileName, MediaTypes.APPLICATION_WORD_DOCX);
    }

    public static void writeExcel(HttpServletResponse response, File file, String fileName) throws IOException {
        writeFile(response, file, true, fileName, MediaTypes.APPLICATION_EXCEL_XLSX);
    }

    public static void writePdf(HttpServletResponse response, File file, boolean asAttachment, String fileName) throws IOException {
        writeFile(response, file, asAttachment, fileName, MediaTypes.APPLICATION_PDF);
    }

    public static void writeImg(HttpServletResponse response, File file, boolean asAttachment, String fileName, String contentType) throws IOException {
        writeFile(response, file, asAttachment, fileName, contentType);
    }

    public static void writeFile(HttpServletResponse response, File file, boolean asAttachment, String fileName, String contentType) throws IOException {
        Map<String, String> respHeaders = getDownloadRespHeaders(asAttachment, fileName, file.length(), contentType);
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

    public static Map<String, String> getDownloadRespHeaders(boolean asAttachment, String fileName, long contentSize, String contentType) {
        return MapUtils.of(LinkedHashMap::new,
            "Content-Disposition", (asAttachment ? "attachment;" : "") + "filename=" + fileName,
            "Content-Length", contentSize + "",
            "Content-Type", contentType
        );
    }

}
