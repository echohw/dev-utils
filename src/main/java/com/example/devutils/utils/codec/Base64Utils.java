package com.example.devutils.utils.codec;

import com.example.devutils.dep.Charsets;
import com.example.devutils.dep.MediaTypes;
import com.example.devutils.utils.text.StringUtils;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

/**
 * Created by AMe on 2020-06-16 17:22.
 */
public class Base64Utils {

    private static final Charset DEFAULT_CHARSET = Charsets.UTF_8;

    public static Encoder getEncoder() {
        return Base64.getEncoder();
    }

    public static Encoder getUrlEncoder() {
        return Base64.getUrlEncoder();
    }

    public static Decoder getDecoder() {
        return Base64.getDecoder();
    }

    public static Decoder getUrlDecoder() {
        return Base64.getUrlDecoder();
    }

    public static byte[] encode(byte[] bytes) {
        if (bytes.length == 0) {
            return bytes;
        }
        return getEncoder().encode(bytes);
    }

    public static byte[] encodeUrl(byte[] bytes) {
        if (bytes.length == 0) {
            return bytes;
        }
        return getUrlEncoder().encode(bytes);
    }

    public static String encodeToString(byte[] bytes) {
        if (bytes.length == 0) {
            return "";
        }
        return new String(encode(bytes), DEFAULT_CHARSET);
    }

    public static String encodeToUrlString(byte[] bytes) {
        return new String(encodeUrl(bytes), DEFAULT_CHARSET);
    }

    public static byte[] decode(byte[] bytes) {
        if (bytes.length == 0) {
            return bytes;
        }
        return getDecoder().decode(bytes);
    }

    public static byte[] decodeUrl(byte[] bytes) {
        if (bytes.length == 0) {
            return bytes;
        }
        return getUrlDecoder().decode(bytes);
    }

    public static byte[] decodeFromString(String base64Str) {
        if (base64Str.isEmpty()) {
            return new byte[0];
        }
        return decode(base64Str.getBytes(DEFAULT_CHARSET));
    }


    public static byte[] decodeFromUrlString(String base64Str) {
        if (base64Str.isEmpty()) {
            return new byte[0];
        }
        return decodeUrl(base64Str.getBytes(DEFAULT_CHARSET));
    }

    public static String encodeImageToString(byte[] bytes, String imageType) {
        imageType = StringUtils.isBlank(imageType) ? MediaTypes.IMAGE_JPEG : imageType;
        String base64Str = encodeToString(bytes);
        return "data:" + imageType + ";base64," + base64Str;
    }

}
