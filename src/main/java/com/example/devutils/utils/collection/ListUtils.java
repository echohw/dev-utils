package com.example.devutils.utils.collection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * List集合操作工具类
 * Created by AMe on 2020-06-13 11:28.
 */
public class ListUtils extends CollectionUtils {

    public static <I> ArrayList<I> of(int size, I filler) {
        return IntStream.range(0, size).boxed().map(t_ -> filler).collect(Collectors.toCollection(ArrayList::new));
    }

    public static <T extends List<I>, I> T fill(T list, I filler) {
        Collections.fill(list, filler);
        return list;
    }

    /**
     * 排序[自身排序]
     */
    public static <I, L extends List<I>> L sort(L list, Comparator<I> comparator) {
        return sort(list, comparator, null);
    }

    /**
     * 排序[自身排序 or 返回新的列表]
     */
    public static <I, L extends List<I>> L sort(L list, Comparator<I> comparator, Supplier<L> listSupplier) {
        if (listSupplier == null) {
            list.sort(comparator);
            return list;
        } else {
            return list.stream().sorted(comparator).collect(Collectors.toCollection(listSupplier));
        }
    }

    /**
     * 去重
     */
    public static <I, C extends Collection<I>> C distinct(List<I> list, Supplier<C> supplier) {
        return list.stream().distinct().collect(Collectors.toCollection(supplier));
    }
}
