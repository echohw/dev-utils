package com.example.devutils.utils.collection;

import com.example.devutils.dep.ObjectWrapper;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * 单列集合操作通用工具类
 * Created by AMe on 2020-06-13 11:28.
 */
public class CollectionUtils {

    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }

    /**
     * 分组
     */
    public static <T, K, V extends Collection<T>, M extends Map<K, V>> M grouping(Collection<T> datas, Supplier<M> mapSupplier, Supplier<V> collectionSupplier, Function<T, List<K>> keysFunc) {
        return datas.stream().collect(mapSupplier, (map, item) -> {
            List<K> keys = keysFunc.apply(item);
            if (keys != null && keys.size() > 0) {
                for (K key : keys) {
                    V v = Optional.ofNullable(map.get(key)).orElseGet(collectionSupplier);
                    v.add(item);
                    map.put(key, v);
                }
            }
        }, M::putAll);
    }

    /**
     * 计数
     */
    public static <T, K> Map<K, Integer> count(Collection<T> datas, Function<T, K> function) {
        return datas.stream().collect(HashMap::new, (map, item) -> {
            K key = function.apply(item);
            map.put(key, map.getOrDefault(key, 0) + 1);
        }, HashMap::putAll);
    }

    /**
     * 拉平
     */
    public static <T extends Collection<I>, I, R extends Collection<I>> R flat(Collection<T> collection, Supplier<R> supplier) {
        return collection.stream().flatMap(Collection::stream).collect(Collectors.toCollection(supplier));
    }

    /**
     * 去重[手动定义hashCode及equals策略]
     */
    public static <I, C extends Collection<I>> C distinct(Collection<I> collection, Function<I, Integer> hashCodeFunc, BiFunction<I, I, Boolean> equalsBiFunc, Supplier<C> supplier) {
        return collection.stream().map(item -> new ObjectWrapper<>(item, hashCodeFunc, equalsBiFunc)).distinct().map(
            ObjectWrapper::wrapped).collect(Collectors.toCollection(supplier));
    }

    /**
     * 交集
     */
    public static <I, C extends Collection<I>> C intersection(Collection<I> c1, Collection<I> c2, Supplier<C> supplier) {
        HashSet<I> hashSet = new HashSet<>(c2);
        return c1.stream().filter(hashSet::contains).collect(Collectors.toCollection(supplier));
    }

    /**
     * 并集
     */
    public static <I, C extends Collection<I>> C union(Collection<I> c1, Collection c2, Supplier<C> supplier) {
        C c = supplier.get();
        c.addAll(c1);
        c.addAll(c2);
        return c;
    }

    /**
     * 差集
     */
    public static <I, C extends Collection<I>> C subtract(Collection<I> c1, Collection<I> c2, Supplier<C> supplier) {
        HashSet<I> hashSet = new HashSet<>(c2);
        return c1.stream().filter(item -> !hashSet.contains(item)).collect(Collectors.toCollection(supplier));
    }

}
