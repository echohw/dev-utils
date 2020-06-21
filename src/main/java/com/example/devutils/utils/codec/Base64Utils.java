package com.example.devutils.utils.codec;

import com.example.devutils.dep.Charsets;
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

    private static final String IMG_TYPE_FLAG = "${img_type}";
    private static final String SRC_PROP_PREFIX = "data:image/" + IMG_TYPE_FLAG +";base64,";
    private static final String DEFAULT_IMG_TYPE = "jpeg";

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
        imageType = StringUtils.isBlank(imageType) ? DEFAULT_IMG_TYPE : imageType;
        String base64Str = encodeToString(bytes);
        return SRC_PROP_PREFIX.replace(IMG_TYPE_FLAG, imageType) + base64Str;
    }

}
