package com.example.devutils.utils.time;

import com.example.devutils.dep.ZoneIds;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalUnit;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.jooq.lambda.tuple.Tuple2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by AMe on 2020-06-18 12:58.
 */
public class TimeUtils {

    private static final Logger logger = LoggerFactory.getLogger(TimeUtils.class);

    /* ====================当前==================== */
    public static long nowTimestamp() {
        return System.currentTimeMillis();
    }

    public static Date nowDate() {
        return new Date();
    }

    public static LocalDateTime nowLocalDateTime() {
        return LocalDateTime.now();
    }

    public static LocalDate nowLocalDate() {
        return LocalDate.now();
    }

    public static LocalTime nowLocalTime() {
        return LocalTime.now();
    }

    public static YearMonth nowYearMonth() {
        return YearMonth.now();
    }

    public static Instant nowInstant() {
        return Instant.now();
    }

    public static ZonedDateTime nowZonedDateTime() {
        return ZonedDateTime.now();
    }

    /* ====================构造==================== */
    public static Date ofDate(long timestamp) {
        return new Date(timestamp);
    }

    public static LocalDateTime ofLocalDateTime(long timestamp) {
        return toLocalDateTime(ofInstant(timestamp));
    }

    public static LocalDateTime ofLocalDateTime(int year, int month, int dayOfMonth, int hour, int minute, int second) {
        return LocalDateTime.of(year, month, dayOfMonth, hour, minute, second);
    }

    public static LocalDate ofLocalDate(int year, int month, int dayOfMonth) {
        return LocalDate.of(year, month, dayOfMonth);
    }

    public static LocalTime ofLocalTime(int hour, int minute, int second) {
        return LocalTime.of(hour, minute, second);
    }

    public static YearMonth ofYearMonth(int year, int month) {
        return YearMonth.of(year, month);
    }

    public static Instant ofInstant(long epochMilli) {
        return Instant.ofEpochMilli(epochMilli);
    }

    public static ZonedDateTime ofZonedDateTime(LocalDateTime localDateTime, ZoneId zoneId) {
        return ZonedDateTime.of(localDateTime, zoneId);
    }

    /* ====================转换==================== */
    public static long toTimestamp(Date date) {
        return date.getTime();
    }

    public static long toTimestamp(LocalDateTime localDateTime) {
        return toTimestamp(toInstant(localDateTime));
    }

    public static long toTimestamp(Instant instant) {
        return instant.toEpochMilli();
    }

    public static Date toDate(LocalDateTime localDateTime) {
        return toDate(toInstant(localDateTime, ZoneIds.SYSTEM_ZONE));
    }

    public static Date toDate(Instant instant) {
        return Date.from(instant);
    }

    public static LocalDateTime toLocalDateTime(Date date) {
        return toLocalDateTime(toInstant(date), ZoneIds.SYSTEM_ZONE);
    }

    public static LocalDateTime toLocalDateTime(Instant instant) {
        return toLocalDateTime(instant, ZoneIds.DEFAULT_ZONE);
    }

    public static LocalDateTime toLocalDateTime(Instant instant, ZoneId zoneId) {
        return LocalDateTime.ofInstant(instant, zoneId);
    }

    public static Instant toInstant(Date date) {
        return date.toInstant();
    }

    public static Instant toInstant(LocalDateTime localDateTime) {
        return toInstant(localDateTime, ZoneIds.DEFAULT_ZONE);
    }

    public static Instant toInstant(LocalDateTime localDateTime, ZoneId zoneId) {
        return localDateTime.atZone(zoneId).toInstant();
    }

    /* ====================调整==================== */
    public static Date plus(Date date, int amountToAdd, TemporalUnit unit) {
        Instant instant = plus(date.toInstant(), amountToAdd, unit);
        return toDate(instant);
    }

    public static LocalDateTime plus(LocalDateTime localDateTime, int amountToAdd, TemporalUnit unit) {
        return localDateTime.plus(amountToAdd, unit);
    }

    public static Instant plus(Instant instant, int amountToAdd, TemporalUnit unit) {
        return instant.plus(amountToAdd, unit);
    }

    public static LocalDateTime adjustToYear(LocalDateTime localDateTime, int year) {
        return localDateTime.withYear(year);
    }

    public static LocalDateTime adjustToMonthOfYear(LocalDateTime localDateTime, int month) {
        return localDateTime.withMonth(month);
    }

    public static LocalDateTime adjustToDayOfMonth(LocalDateTime localDateTime, int dayOfMonth) {
        return localDateTime.withDayOfMonth(dayOfMonth);
    }

    public static LocalDateTime adjustToFirstDayOfMonth(LocalDateTime localDateTime) {
        return localDateTime.with(TemporalAdjusters.firstDayOfMonth());
    }

    public static LocalDateTime adjustToLastDayOfMonth(LocalDateTime localDateTime) {
        return localDateTime.with(TemporalAdjusters.lastDayOfMonth());
    }

    public static Date adjustToStartOfDay(Date date) {
        return toDate(adjustToStartOfDay(toLocalDateTime(date)));
    }

    public static LocalDateTime adjustToStartOfDay(LocalDateTime localDateTime) {
        return LocalDateTime.of(localDateTime.toLocalDate(), LocalTime.MIN);
    }

    public static Date adjustToEndOfDay(Date date) {
        return toDate(adjustToEndOfDay(toLocalDateTime(date)));
    }

    public static LocalDateTime adjustToEndOfDay(LocalDateTime localDateTime) {
        return LocalDateTime.of(localDateTime.toLocalDate(), LocalTime.MAX);
    }

    public static LocalDateTime adjustTimeZone(LocalDateTime srcDateTime, ZoneId srcZoneId, ZoneId targetZoneId) {
        return toLocalDateTime(toInstant(srcDateTime, srcZoneId), targetZoneId);
    }

    /* ====================解析==================== */
    public static Date parseToDate(String dateStr, String pattern) throws ParseException {
        return parseToDate(dateStr, TimeFormatterUtils.getSimpleDateFormat(pattern));
    }

    public static Date parseToDate(String dateStr, String pattern, Locale locale) throws ParseException {
        return parseToDate(dateStr, TimeFormatterUtils.getSimpleDateFormat(pattern, locale));
    }

    public static Date parseToDate(String dateStr, SimpleDateFormat simpleDateFormat) throws ParseException {
        return simpleDateFormat.parse(dateStr);
    }

    public static Date parseToDate(String dateStr, String... patterns) throws ParseException {
        List<Tuple2<String, Locale>> tuple2s = Arrays.stream(patterns)
            .map(pattern -> new Tuple2<>(pattern, TimeFormatterUtils.getDefaultLocale())).collect(Collectors.toList());
        return parseToDate(dateStr, tuple2s);
    }

    public static Date parseToDate(String dateStr, List<Tuple2<String, Locale>> patternLocales) throws ParseException {
        if (patternLocales.size() == 0) throw new IllegalArgumentException();
        ParseException exception = null;
        for (Tuple2<String, Locale> tuple2 : patternLocales) {
            try {
                Date date = parseToDate(dateStr, tuple2.v1, tuple2.v2);
                logger.info("解析成功, date:{}, pattern:{}", dateStr, tuple2.v1);
                return date;
            } catch (ParseException ex) {
                exception = ex;
                logger.error("解析失败, date:{}, pattern:{}", dateStr, tuple2.v1);
            }
        }
        throw exception;
    }

    public static LocalDateTime parseToLocalDateTime(String dateTimeStr, String pattern) {
        return parseToLocalDateTime(dateTimeStr, TimeFormatterUtils.getDateTimeFormatter(pattern));
    }

    public static LocalDateTime parseToLocalDateTime(String dateTimeStr, String pattern, Locale locale) {
        return parseToLocalDateTime(dateTimeStr, TimeFormatterUtils.getDateTimeFormatter(pattern, locale));
    }

    public static LocalDateTime parseToLocalDateTime(String dateTimeStr, DateTimeFormatter dateTimeFormatter) {
        return LocalDateTime.parse(dateTimeStr, dateTimeFormatter);
    }

    public static Instant parseToInstant(String instantStr) {
        return Instant.parse(instantStr);
    }

    public static ZonedDateTime parseToZonedDateTime(String zonedDateTimeStr) {
        return ZonedDateTime.parse(zonedDateTimeStr);
    }

    /* ====================格式化==================== */
    public static String formatDate(Date date, String pattern) {
        return formatDate(date, TimeFormatterUtils.getSimpleDateFormat(pattern));
    }

    public static String formatDate(Date date, String pattern, Locale locale) {
        return formatDate(date, TimeFormatterUtils.getSimpleDateFormat(pattern, locale));
    }

    public static String formatDate(Date date, SimpleDateFormat simpleDateFormat) {
        return simpleDateFormat.format(date);
    }

    public static String formatLocalDateTime(LocalDateTime localDateTime, String pattern) {
        return formatLocalDateTime(localDateTime, TimeFormatterUtils.getDateTimeFormatter(pattern));
    }

    public static String formatLocalDateTime(LocalDateTime localDateTime, String pattern, Locale locale) {
        return formatLocalDateTime(localDateTime, TimeFormatterUtils.getDateTimeFormatter(pattern, locale));
    }

    public static String formatLocalDateTime(LocalDateTime localDateTime, DateTimeFormatter dateTimeFormatter) {
        return localDateTime.format(dateTimeFormatter);
    }

    public static String formatInstant(Instant instant, String pattern) {
        return formatInstant(instant, pattern, ZoneIds.DEFAULT_ZONE);
    }

    public static String formatInstant(Instant instant, String pattern, ZoneId zoneId) {
        return formatInstant(instant, pattern, zoneId, TimeFormatterUtils.getDefaultLocale());
    }

    public static String formatInstant(Instant instant, String pattern, ZoneId zoneId, Locale locale) {
        return TimeFormatterUtils.getDateTimeFormatter(pattern, locale).withZone(zoneId).format(instant);
    }

    /* ====================相差==================== */
    public static long betweenDays(LocalDateTime startInclusive, LocalDateTime endExclusive) {
        return Duration.between(startInclusive, endExclusive).toDays();
    }

    public static long betweenHours(LocalDateTime startInclusive, LocalDateTime endExclusive) {
        return Duration.between(startInclusive, endExclusive).toHours();
    }

    public static long betweenMinutes(LocalDateTime startInclusive, LocalDateTime endExclusive) {
        return Duration.between(startInclusive, endExclusive).toMinutes();
    }

    public static long between(Temporal startInclusive, Temporal endExclusive, TimeUnit timeUnit) {
        Duration duration = Duration.between(startInclusive, endExclusive);
        switch (timeUnit) {
            case NANOSECONDS:
                return duration.toNanos();
            case MICROSECONDS:
                return TimeUnit.NANOSECONDS.toMicros(duration.toNanos());
            case MILLISECONDS:
                return duration.toMillis();
            case SECONDS:
                return TimeUnit.MILLISECONDS.toSeconds(duration.toMillis());
            case MINUTES:
                return duration.toMinutes();
            case HOURS:
                return duration.toHours();
            case DAYS:
                return duration.toDays();
            default:
                throw new IllegalArgumentException();
        }
    }
}
