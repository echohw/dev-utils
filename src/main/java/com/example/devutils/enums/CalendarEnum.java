package com.example.devutils.enums;

import java.util.function.Predicate;

/**
 * Created by AMe on 2020-09-02 00:43.
 */
public enum CalendarEnum {

    JANUARY("JANUARY", 0, "一月"),
    FEBRUARY("FEBRUARY", 1, "二月"),
    MARCH("MARCH", 2, "三月"),
    APRIL("APRIL", 3, "四月"),
    MAY("MAY", 4, "五月"),
    JUNE("JUNE", 5, "六月"),
    JULY("JULY", 6, "七月"),
    AUGUST("AUGUST", 7, "八月"),
    SEPTEMBER("SEPTEMBER", 8, "九月"),
    OCTOBER("OCTOBER", 9, "十月"),
    NOVEMBER("NOVEMBER", 10, "十一月"),
    DECEMBER("DECEMBER", 11, "十二月"),
    ;

    public final String name;
    public final int value;
    public final String description;

    CalendarEnum(String name, int value, String description) {
        this.name = name;
        this.value = value;
        this.description = description;
    }

    public static CalendarEnum find(Predicate<CalendarEnum> predicate) {
        for (CalendarEnum item : values()) {
            if (predicate.test(item)) {
                return item;
            }
        }
        return null;
    }

    public static CalendarEnum findByName(String name) {
        return find(item -> item.name.equals(name));
    }

    public static CalendarEnum findByValue(int value) {
        return find(item -> item.value == value);
    }
}