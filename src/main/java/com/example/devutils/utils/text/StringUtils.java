package com.example.devutils.utils.text;

import java.util.UUID;

/**
 * Created by AMe on 2020-06-09 13:09.
 */
public class StringUtils {

    public static boolean isBlank(String str) {
        return !isNotBlank(str);
    }

    public static boolean isNotBlank(String str) {
        return str != null && !"".equals(str.trim());
    }

    public static String repeat(String str, int cycle, String separator) {
        int length = str.concat(separator).length() * Math.max(cycle, 0);
        StringBuilder stringBuilder = new StringBuilder(length);
        for (int i = 0; i < cycle; i++) {
            stringBuilder.append(str);
            if (i < cycle - 1) {
                stringBuilder.append(separator);
            }
        }
        return stringBuilder.toString();
    }

    public static String getUuid(boolean delimiter) {
        String uuid = UUID.randomUUID().toString();
        return delimiter ? uuid : uuid.replace("-", "");
    }

    public static String getUuid() {
        return getUuid(true);
    }

}
