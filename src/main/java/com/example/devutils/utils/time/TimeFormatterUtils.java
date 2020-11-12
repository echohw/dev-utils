package com.example.devutils.utils.time;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import org.jooq.lambda.tuple.Tuple2;

/**
 * Created by AMe on 2020-06-19 16:21.
 */
public class TimeFormatterUtils {

    private static final ThreadLocal<Map<Tuple2<String, Locale>, SimpleDateFormat>> simpleDateFormatCacheThreadLocal = ThreadLocal.withInitial(ConcurrentHashMap::new);
    private static final Map<Tuple2<String, Locale>, DateTimeFormatter> dateTimeFormatterCache = new ConcurrentHashMap<>();

    public static SimpleDateFormat getSimpleDateFormat(String pattern) {
        return getSimpleDateFormat(pattern, getDefaultLocale());
    }

    public static SimpleDateFormat getSimpleDateFormat(String pattern, Locale locale) {
        return getSimpleDateFormat(new Tuple2<>(pattern, locale));
    }

    public static SimpleDateFormat getSimpleDateFormat(Tuple2<String, Locale> tuple2) {
        Objects.requireNonNull(tuple2.v1);
        if (tuple2.v2 == null) {
            tuple2 = new Tuple2<>(tuple2.v1, getDefaultLocale());
        }
        return simpleDateFormatCacheThreadLocal.get().computeIfAbsent(tuple2, tp2 -> new SimpleDateFormat(tp2.v1, tp2.v2));
    }

    public static DateTimeFormatter getDateTimeFormatter(String pattern) {
        return getDateTimeFormatter(pattern, getDefaultLocale());
    }

    public static DateTimeFormatter getDateTimeFormatter(String pattern, Locale locale) {
        return getDateTimeFormatter(new Tuple2<>(pattern, locale));
    }

    public static DateTimeFormatter getDateTimeFormatter(Tuple2<String, Locale> tuple2) {
        Objects.requireNonNull(tuple2.v1);
        if (tuple2.v2 == null) {
            tuple2 = new Tuple2<>(tuple2.v1, getDefaultLocale());
        }
        return dateTimeFormatterCache.computeIfAbsent(tuple2, tp2 -> DateTimeFormatter.ofPattern(tp2.v1, tp2.v2));
    }

    public static Locale getDefaultLocale() {
        return Locale.getDefault(Locale.Category.FORMAT);
    }
}
