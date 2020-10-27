package com.example.devutils.utils.number;

import com.example.devutils.deps.CommonRange.LongRange;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import org.springframework.util.Assert;

/**
 * Created by AMe on 2020-06-11 17:32.
 */
public class NumberUtils {

    private static final ThreadLocalRandom random = ThreadLocalRandom.current();

    public static List<List<Integer>> section(List<Integer> list, int every) {
        Assert.isTrue(every > 0, "every must be greater than 0");
        List<List<Integer>> secList = new ArrayList<>();
        int size = list.size();
        for (int from = 0; from < size; from += every) {
            int to = from + every;
            to = to < size ? to : size;
            secList.add(list.subList(from, to));
        }
        return secList;
    }

    public static List<LongRange> section(long start, long end, long every) {
        Assert.isTrue(every > 0, "every must be greater than 0");
        ArrayList<LongRange> secList = new ArrayList<>();
        for (long from = start; from <= end; from += every) {
            long to = from + every - 1;
            to = to < end ? to : end;
            secList.add(new LongRange(from, to));
        }
        return secList;
    }

    public static long random(long origin, long bound) {
        return random.nextLong(origin, bound);
    }

    public static boolean equals(double d1, double d2, double diff) {
        return Math.abs(d1 - d2) <= diff;
    }

    public static boolean equals(BigDecimal d1, BigDecimal d2, BigDecimal diff) {
        return d1.subtract(d2).abs().compareTo(diff) <= 0;
    }
}
