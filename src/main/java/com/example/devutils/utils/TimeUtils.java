package com.example.devutils.utils;

import com.example.devutils.dep.Zones;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * Created by AMe on 2020-06-18 12:58.
 */
public class TimeUtils {

    public static Date getNowDate() {
        return new Date();
    }

    public static LocalDateTime getNowLocalDateTime() {
        return LocalDateTime.now();
    }

    public static LocalDate getNowLocalDate() {
        return LocalDate.now();
    }

    public static LocalTime getNowLocalTime() {
        return LocalTime.now();
    }

    public static YearMonth getNowYearMonth() {
        return YearMonth.now();
    }

    public static Instant getNowInstant() {
        return Instant.now();
    }

    public static LocalDateTime toLocalDateTime(Instant instant) {
        return toLocalDateTime(instant, Zones.DEFAULT_ZONE);
    }

    public static LocalDateTime toLocalDateTime(Instant instant, ZoneId zoneId) {
        return LocalDateTime.ofInstant(instant, zoneId);
    }

    public static LocalDateTime toLocalDateTime(Date date) {
        return toLocalDateTime(toInstant(date), Zones.SYSTEM_ZONE);
    }

    public static Instant toInstant(LocalDateTime localDateTime) {
        return toInstant(localDateTime, Zones.DEFAULT_ZONE);
    }

    public static Instant toInstant(LocalDateTime localDateTime, ZoneId zoneId) {
        return localDateTime.atZone(zoneId).toInstant();
    }

    public static Instant toInstant(Date date) {
        return date.toInstant();
    }

    public static Date toDate(LocalDateTime localDateTime) {
        return toDate(toInstant(localDateTime, Zones.SYSTEM_ZONE));
    }

    public static Date toDate(Instant instant) {
        return Date.from(instant);
    }

    public static LocalDateTime adjustTimeZone(LocalDateTime srcDateTime, ZoneId srcZoneId, ZoneId targetZoneId) {
        return toLocalDateTime(toInstant(srcDateTime, srcZoneId), targetZoneId);
    }

    public static LocalDateTime parseToLocalDateTime(String dateTimeStr, String pattern) {
        return LocalDateTime.parse(dateTimeStr, getDateTimeFormatter(pattern));
    }

    public static Instant parseToInstant(String instantStr, String pattern) {
        return ZonedDateTime.parse(instantStr, getDateTimeFormatter(pattern)).toInstant();
    }

    public static Date parseToDate(String dateStr, String pattern) throws ParseException {
        return getSimpleDateFormat(pattern).parse(dateStr);
    }

    public static SimpleDateFormat getSimpleDateFormat(String pattern) {
        return new SimpleDateFormat(pattern);
    }

    public static DateTimeFormatter getDateTimeFormatter(String pattern) {
        return DateTimeFormatter.ofPattern(pattern);
    }

    public static String formatLocalDateTime(LocalDateTime localDateTime, String pattern) {
        return localDateTime.format(getDateTimeFormatter(pattern));
    }

    public static String formatLocalDate(LocalDate localDate, String pattern) {
        return localDate.format(getDateTimeFormatter(pattern));
    }

    public static String formatInstant(Instant instant, String pattern) {
        return formatInstant(instant, Zones.DEFAULT_ZONE, pattern);
    }

    public static String formatInstant(Instant instant, ZoneId zoneId, String pattern) {
        return DateTimeFormatter.ofPattern(pattern).withZone(zoneId).format(instant);
    }

    public static String formatDate(Date date, String pattern) {
        return getSimpleDateFormat(pattern).format(date);
    }

}
