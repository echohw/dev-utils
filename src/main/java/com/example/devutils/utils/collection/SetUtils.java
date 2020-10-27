package com.example.devutils.utils.collection;

import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.function.Supplier;

/**
 * Set集合操作工具类
 * Created by AMe on 2020-06-13 11:28.
 */
public class SetUtils extends CollectionUtils {

    public static <T> LinkedHashSet<T> sort(HashSet<T> hashSet, Comparator<T> comparator) {
        return CollectionUtils.sort(hashSet, comparator, LinkedHashSet::new);
    }

    public static <T, R extends List<T>> R sort(HashSet<T> hashSet, Comparator<T> comparator, Supplier<R> supplier) {
        return CollectionUtils.sort(hashSet, comparator, supplier);
    }
}
