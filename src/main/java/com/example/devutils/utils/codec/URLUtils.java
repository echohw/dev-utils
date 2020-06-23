package com.example.devutils.utils.codec;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;

/**
 * Created by AMe on 2020-06-24 01:02.
 */
public class URLUtils {

    public static String encode(String content, Charset charset) throws UnsupportedEncodingException {
        return URLEncoder.encode(content, charset.name());
    }

    public static String decode(String content, Charset charset) throws UnsupportedEncodingException {
        return URLDecoder.decode(content, charset.name());
    }

}
