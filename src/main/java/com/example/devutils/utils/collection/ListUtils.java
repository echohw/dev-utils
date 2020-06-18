package com.example.devutils.utils.collection;

import com.example.devutils.dep.ObjectWrapper;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Created by AMe on 2020-06-13 11:28.
 */
public class ListUtils extends CollectionUtils {

    public static <I, L extends List<I>> L sort(L list, Comparator<I> comparator) {
        return sort(list, comparator, null);
    }

    public static <I, L extends List<I>> L sort(L list, Comparator<I> comparator, Supplier<L> listSupplier) {
        if (listSupplier == null) {
            list.sort(comparator);
            return list;
        } else {
            return list.stream().sorted(comparator).collect(Collectors.toCollection(listSupplier));
        }
    }

    public static <T, L extends Collection<T>> L distinct(List<T> list, Supplier<L> supplier) {
        return list.stream().distinct().collect(Collectors.toCollection(supplier));
    }

    public static <T, L extends Collection<T>> L distinct(List<T> list, Function<T, Integer> hashCodeFunc, BiFunction<T, T, Boolean> equalsBiFunc, Supplier<L> supplier) {
        return list.stream().map(item -> new ObjectWrapper<>(item, hashCodeFunc, equalsBiFunc)).distinct().map(
            ObjectWrapper::wrapped).collect(Collectors.toCollection(supplier));
    }
}
