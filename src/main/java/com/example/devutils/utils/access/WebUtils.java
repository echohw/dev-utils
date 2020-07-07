package com.example.devutils.utils.access;

import java.util.Optional;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.support.RequestContextUtils;

/**
 * Created by AMe on 2020-07-06 11:11.
 */
public class WebUtils {

    public static String[] getAttributeNames(int scope) {
        return Optional.ofNullable(getServletRequestAttributes()).map(attrs -> attrs.getAttributeNames(scope)).orElse(new String[0]);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getAttribute(String name, int scope) {
        return (T) Optional.ofNullable(getServletRequestAttributes()).map(attrs -> attrs.getAttribute(name, scope)).orElse(null);
    }

    public static HttpServletRequest getServletRequest() {
        return Optional.ofNullable(getServletRequestAttributes()).map(ServletRequestAttributes::getRequest).orElse(null);
    }

    public static HttpServletResponse getServletResponse() {
        return Optional.ofNullable(getServletRequestAttributes()).map(ServletRequestAttributes::getResponse).orElse(null);
    }

    public static ServletContext getServletContext() {
        return Optional.ofNullable(getServletRequest()).map(HttpServletRequest::getServletContext).orElse(null);
    }

    public static String getSessionId() {
        return Optional.ofNullable(getServletRequestAttributes()).map(ServletRequestAttributes::getSessionId).orElse(null);
    }

    public static WebApplicationContext getWebApplicationContext(ServletContext servletContext) {
        return WebApplicationContextUtils.findWebApplicationContext(servletContext);
    }

    public static WebApplicationContext getWebApplicationContext(HttpServletRequest servletRequest) {
        return RequestContextUtils.findWebApplicationContext(servletRequest);
    }

    public static ServletRequestAttributes getServletRequestAttributes() {
        return  (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    }

}
