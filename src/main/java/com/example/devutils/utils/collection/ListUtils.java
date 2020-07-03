package com.example.devutils.utils.collection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * List集合操作工具类
 * Created by AMe on 2020-06-13 11:28.
 */
public class ListUtils extends CollectionUtils {

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

    /**
     * 集合元素分组(元素在顺序上应该存在一定的关联)
     * @param list 元素集
     * @param predicate 关联断言函数
     * @param <T> 元素类型
     * @return List<ArrayList<Item>>
     */
    public static <T> List<ArrayList<T>> grouping(List<T> list, BiPredicate<T, T> predicate) {
        if (list.size() < 2) {
            return Collections.singletonList(new ArrayList<>(list));
        }
        List<ArrayList<T>> resList = new ArrayList<>();
        int start = 0, end = 0;
        for (int i = 1; i < list.size(); i++) {
            end = i;
            if (!predicate.test(list.get(i - 1), list.get(i))) {
                resList.add(new ArrayList<>(list.subList(start, end)));
                start = end;
            }
            if (i == list.size() - 1) {
                resList.add(new ArrayList<>(list.subList(start, end + 1)));
            }
        }
        return resList;
    }
}
