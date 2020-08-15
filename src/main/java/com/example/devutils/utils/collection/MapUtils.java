package com.example.devutils.utils.collection;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BinaryOperator;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Map集合操作工具类
 * Created by AMe on 2020-06-13 11:28.
 */
public class MapUtils {

    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }

    @SuppressWarnings(value = "unchecked")
    public static <I, K, V, M extends Map<K, V>> M of(Supplier<M> mapSupplier, I... items) {
        M map = mapSupplier.get();
        int len = items.length;
        for (int i = 0; i < len - 1; i += 2) {
            map.put((K) items[i], (V) items[i + 1]);
        }
        return map;
    }

    public static <K, V, M extends Map<K, V>> M of(K[] keys, V[] values, Supplier<M> mapSupplier) {
        M map = mapSupplier.get();
        int len = Math.min(keys.length, values.length);
        for (int i = 0; i < len; i++) {
            map.put(keys[i], values[i]);
        }
        return map;
    }

    /**
     * 泛型擦除转换
     */
    @SuppressWarnings(value = "unchecked")
    public static <K, V, M extends Map<K, V>> M actual(Map map) {
        return (M) map;
    }

    /**
     * HashMap排序
     */
    public static <K, V> LinkedHashMap<K, V> sort(HashMap<K, V> hashMap, Comparator<Entry<K, V>> comparator) {
        return hashMap.entrySet().stream().sorted(comparator).collect(Collectors.toMap(Entry::getKey, Entry::getValue, (oldItem, newItem) -> newItem, LinkedHashMap::new));
    }

    /**
     * 合并Map元素
     */
    @SafeVarargs
    public static <K, V, M extends Map<K, V>> M merge(Supplier<M> mapSupplier, BinaryOperator<V> mergeFunc, Map<K, V>... maps) {
        return Arrays.stream(maps).flatMap(map -> map.entrySet().stream()).collect(mapSupplier, (map, entry) -> {
            map.put(entry.getKey(), map.containsKey(entry.getKey()) ? mergeFunc.apply(map.get(entry.getKey()), entry.getValue()) : entry.getValue());
        }, M::putAll);
    }

    public static <K, V, M extends Map<K, V>> M merge(Supplier<M> mapSupplier, BinaryOperator<V> mergeFunc, Collection<? extends Map<K, V>> maps) {
        return maps.stream().flatMap(map -> map.entrySet().stream()).collect(mapSupplier, (map, entry) -> {
            map.put(entry.getKey(), map.containsKey(entry.getKey()) ? mergeFunc.apply(map.get(entry.getKey()), entry.getValue()) : entry.getValue());
        }, M::putAll);
    }
}
