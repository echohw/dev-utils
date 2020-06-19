package com.example.devutils.utils.time;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;

/**
 * Created by AMe on 2020-06-19 16:21.
 */
public class TimeFormatterUtils {

    public static SimpleDateFormat getSimpleDateFormat(String pattern) {
        return new SimpleDateFormat(pattern);
    }

    public static DateTimeFormatter getDateTimeFormatter(String pattern) {
        return DateTimeFormatter.ofPattern(pattern);
    }

}
