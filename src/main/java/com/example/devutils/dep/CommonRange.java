package com.example.devutils.dep;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

/**
 * Created by AMe on 2020-10-21 16:57.
 */
public class CommonRange {

    private CommonRange() {
    }

    public static class ByteRange extends Range<Byte> {

        public ByteRange() {
        }

        public ByteRange(Byte from, Byte to) {
            super(from, to);
        }
    }

    public static class IntegerRange extends Range<Integer> {

        public IntegerRange() {
        }

        public IntegerRange(Integer from, Integer to) {
            super(from, to);
        }
    }

    public static class LongRange extends Range<Long> {

        public LongRange() {
        }

        public LongRange(Long from, Long to) {
            super(from, to);
        }
    }

    public static class FloatRange extends Range<Float> {

        public FloatRange() {
        }

        public FloatRange(Float from, Float to) {
            super(from, to);
        }
    }

    public static class DoubleRange extends Range<Double> {

        public DoubleRange() {
        }

        public DoubleRange(Double from, Double to) {
            super(from, to);
        }
    }

    public static class BigIntegerRange extends Range<BigInteger> {

        public BigIntegerRange() {
        }

        public BigIntegerRange(BigInteger from, BigInteger to) {
            super(from, to);
        }
    }

    public static class BigDecimalRange extends Range<BigDecimal> {

        public BigDecimalRange() {
        }

        public BigDecimalRange(BigDecimal from, BigDecimal to) {
            super(from, to);
        }
    }

    public static class StringRange extends Range<String> {

        public StringRange() {
        }

        public StringRange(String from, String to) {
            super(from, to);
        }
    }

    public static class DateRange extends Range<Date> {

        public DateRange() {
        }

        public DateRange(Date from, Date to) {
            super(from, to);
        }
    }

    public static class LocalDateTimeRange extends Range<LocalDateTime> {

        public LocalDateTimeRange() {
        }

        public LocalDateTimeRange(LocalDateTime from, LocalDateTime to) {
            super(from, to);
        }
    }

    public static class LocalDateRange extends Range<LocalDate> {

        public LocalDateRange() {
        }

        public LocalDateRange(LocalDate from, LocalDate to) {
            super(from, to);
        }
    }

    public static class LocalTimeRange extends Range<LocalTime> {

        public LocalTimeRange() {
        }

        public LocalTimeRange(LocalTime from, LocalTime to) {
            super(from, to);
        }
    }

    public static class InstantRange extends Range<Instant> {

        public InstantRange() {
        }

        public InstantRange(Instant from, Instant to) {
            super(from, to);
        }
    }
}
