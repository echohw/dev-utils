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

}
