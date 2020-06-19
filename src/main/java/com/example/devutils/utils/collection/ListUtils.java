package com.example.devutils.utils.collection;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Created by AMe on 2020-06-13 11:28.
 */
public class ListUtils extends CollectionUtils {

    /**
     * 排序
     */
    public static <I, L extends List<I>> L sort(L list, Comparator<I> comparator) {
        return sort(list, comparator, null);
    }

    /**
     * 排序
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
