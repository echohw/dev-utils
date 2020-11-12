package com.example.devutils.utils.access;

import com.example.devutils.constant.CharsetConsts;
import com.example.devutils.utils.codec.URLUtils;
import com.example.devutils.utils.collection.MapUtils;
import com.example.devutils.utils.io.StreamUtils;
import com.example.devutils.utils.text.StringUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.jooq.lambda.Unchecked;
import org.jooq.lambda.tuple.Tuple2;

/**
 * Created by AMe on 2020-06-21 14:52.
 */
public class ServletRequestUtils {

    public static String getUri(HttpServletRequest request) {
        return request.getRequestURI();
    }

    public static String getUrl(HttpServletRequest request) {
        return request.getRequestURL().toString();
    }

    public static String getQueryString(HttpServletRequest request, boolean decode) {
        String queryStr = request.getQueryString();
        return queryStr != null && decode ? Unchecked.supplier(() -> URLUtils.decode(queryStr, CharsetConsts.UTF_8)).get() : queryStr;
    }

    public static Map<String, List<String>> parseQueryString(String queryStr) {
        if (StringUtils.isBlank(queryStr)) {
            return Collections.emptyMap();
        } else {
            Map<String, List<Tuple2<String, String>>> collect = Arrays.stream(queryStr.split("&"))
                .filter(StringUtils::isNotBlank)
                .map(nameValue -> {
                    int position = nameValue.indexOf("=");
                    if (position < 0) {
                        return new Tuple2<>(nameValue, "");
                    } else {
                        return new Tuple2<>(nameValue.substring(0, position), nameValue.substring(position + 1));
                    }
                }).collect(Collectors.groupingBy(tuple -> tuple.v1));
            return collect.keySet().stream()
                .collect(LinkedHashMap::new, (map, name) -> {
                    List<String> values = collect.get(name).stream().flatMap(tuple -> Arrays.stream(tuple.v2.split(","))).collect(Collectors.toList());
                    map.put(name, values);
                }, LinkedHashMap::putAll);
        }
    }

    public static String toQueryString(Map<String, List<String>> paramMap, boolean separatedByComma) {
        if (MapUtils.isEmpty(paramMap)) {
            return "";
        }
        Stream<Entry<String, List<String>>> entryStream = paramMap.entrySet().stream();
        if (separatedByComma) {
            return entryStream.map(entry -> entry.getKey() + "=" + String.join(",", entry.getValue())).collect(Collectors.joining("&"));
        } else {
            return entryStream.flatMap(entry -> entry.getValue().stream().map(item -> entry.getKey() + "=" + item)).collect(Collectors.joining("&"));
        }
    }

    public static Map<String, List<String>> getRequestParams(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        return parameterMap.entrySet().stream().collect(
            LinkedHashMap::new,
            (map, entry) -> map.put(entry.getKey(), Arrays.asList(entry.getValue())),
            LinkedHashMap::putAll
        );
    }

    public static byte[] getBodyAsBytes(HttpServletRequest request) throws IOException {
        try (
            ServletInputStream inputStream = request.getInputStream();
        ) {
            return StreamUtils.readBytes(inputStream);
        }
    }

    public static String getBodyAsString(HttpServletRequest request) throws IOException {
        return new String(getBodyAsBytes(request), CharsetConsts.UTF_8);
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

    public static List<String> getHeaders(HttpServletRequest request, String headerName) {
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

    @SuppressWarnings("unchecked")
    public static <T> T getSessionAttributeValue(HttpServletRequest request, String attrName) {
        return (T) Optional.ofNullable(getSession(request, false)).map(session -> session.getAttribute(attrName)).orElse(null);
    }

}
