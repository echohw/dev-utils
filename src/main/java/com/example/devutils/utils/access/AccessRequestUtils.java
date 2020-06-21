package com.example.devutils.utils.access;

import com.example.devutils.utils.text.StringUtils;
import eu.bitwalker.useragentutils.UserAgent;
import java.util.ArrayList;
import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by AMe on 2020-06-21 14:52.
 */
public class AccessRequestUtils {

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

    public static UserAgent parseUserAgent(String userAgent) {
        return UserAgent.parseUserAgentString(userAgent);
    }

}
