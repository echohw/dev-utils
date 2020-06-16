package com.example.devutils.utils;

import com.example.devutils.dep.Range;
import java.util.ArrayList;
import java.util.List;
import org.springframework.util.Assert;

/**
 * Created by AMe on 2020-06-11 17:32.
 */
public class NumberUtils {

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

    public static List<Range<Long>> section(long start, long end, long every) {
        Assert.isTrue(every > 0, "every must be greater than 0");
        ArrayList<Range<Long>> secList = new ArrayList<>();
        for (long from = start; from <= end; from += every) {
            long to = from + every - 1;
            to = to < end ? to : end;
            secList.add(new Range<>(from, to));
        }
        return secList;
    }

    public static long random(long lower, long upper) {
        return (long) ((Math.random() * (upper - lower)) + lower);
    }

}
