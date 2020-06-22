package com.example.devutils.utils.access;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by AMe on 2020-06-22 14:39.
 */
public class ResponseUtils {

    private static final String DEFAULT_CONTENT_TYPE = "text/html;charset=utf-8";

    public static void write(HttpServletResponse response, String content) throws IOException {
        response.setContentType(DEFAULT_CONTENT_TYPE);
        response.getWriter().write(content);
    }

}
