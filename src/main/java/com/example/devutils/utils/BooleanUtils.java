package com.example.devutils.utils;

/**
 * Created by AMe on 2020-07-17 14:24.
 */
public class BooleanUtils {

    public static boolean isTrue(Boolean value) {
        return value != null && value;
    }

    public static boolean isFalse(Boolean value) {
        return value != null && !value;
    }

    public static boolean isNullOrFalse(Boolean value) {
        return !isTrue(value);
    }

    public static boolean isTrue(Object value) {
        if (value == null) {
            return false;
        } else {
            if (value instanceof Number) {
                return ((Number) value).longValue() != 0;
            } else if (value instanceof String) {
                String trim = ((String) value).trim();
                return trim.length() > 0 && !"0".equals(trim) && !"false".equalsIgnoreCase(trim);
            } else if (value instanceof Boolean) {
                return (Boolean) value;
            }
            return true;
        }
    }

    public static boolean isFalse(Object value) {
        return !isTrue(value);
    }
}
