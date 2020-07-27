package com.example.devutils.utils.access;

import com.example.devutils.dep.Charsets;
import com.example.devutils.utils.codec.URLUtils;
import com.example.devutils.utils.io.StreamUtils;
import com.example.devutils.utils.text.StringUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.jooq.lambda.Unchecked;

/**
 * Created by AMe on 2020-06-21 14:52.
 */
public class RequestUtils {

    public static String getUri(HttpServletRequest request) {
        return request.getRequestURI();
    }

    public static String getUrl(HttpServletRequest request) {
        return request.getRequestURL().toString();
    }

    public static String getReqParams(HttpServletRequest request, String httpMethod) throws IOException {
        if ("GET".equalsIgnoreCase(httpMethod)) {
            return Optional.ofNullable(request.getQueryString()).map(queryStr -> Unchecked.supplier(() -> URLUtils.decode(queryStr, Charsets.UTF_8)).get()).orElse(null);
        } else if ("POST".equalsIgnoreCase(httpMethod)) {
            try (
                ServletInputStream inputStream = request.getInputStream();
            ) {
                return StreamUtils.readAsString(inputStream, Charsets.UTF_8);
            }
        }
        return null;
    }

    public static String getIp(HttpServletRequest request) {
        List<String> ipHeaders = Arrays.asList(
            "x-forwarded-for", "Proxy-Client-IP", "WL-Proxy-Client-IP", "HTTP_CLIENT_IP", "HTTP_X_FORWARDED_FOR"
        );
        String ip = null;
        for (String ipHeader : ipHeaders) {
            ip = request.getHeader(ipHeader);
            if (StringUtils.isNotBlank(ip) && !"unknown".equalsIgnoreCase(ip)) break;
        }
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
    }

    public static String getHeader(HttpServletRequest request, String headerName) {
        return request.getHeader(headerName);
    }

    public static ArrayList<String> getHeaders(HttpServletRequest request, String headerName) {
        Enumeration<String> headers = request.getHeaders(headerName);
        ArrayList<String> headerList = new ArrayList<>();
        while (headers.hasMoreElements()) {
            headerList.add(headers.nextElement());
        }
        return headerList;
    }

    public static String getUserAgent(HttpServletRequest request) {
        return getHeader(request, "User-Agent");
    }

    public static Cookie getCookie(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookieName.equals(cookie.getName())) {
                    return cookie;
                }
            }
        }
        return null;
    }

    public static String getCookieValue(HttpServletRequest request, String cookieName) {
        return Optional.ofNullable(getCookie(request, cookieName)).map(Cookie::getValue).orElse(null);
    }

    public static HttpSession getSession(HttpServletRequest request, boolean create) {
        return request.getSession(create);
    }

    public static String getSessionId(HttpServletRequest request) {
        return Optional.ofNullable(getSession(request, false)).map(HttpSession::getId).orElse(null);
    }

    public static Object getSessionAttrValue(HttpServletRequest request, String attrName) {
        return Optional.ofNullable(getSession(request, false)).map(session -> session.getAttribute(attrName)).orElse(null);
    }

}
