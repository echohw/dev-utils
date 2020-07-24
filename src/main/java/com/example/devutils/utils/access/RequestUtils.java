package com.example.devutils.utils.access;

import com.example.devutils.utils.text.StringUtils;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Optional;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.web.util.WebUtils;

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

    public static String getIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
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

    public static String getCookieValue(HttpServletRequest request, String cookieName) {
        return Optional.ofNullable(WebUtils.getCookie(request, cookieName)).map(Cookie::getValue).orElse(null);
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
