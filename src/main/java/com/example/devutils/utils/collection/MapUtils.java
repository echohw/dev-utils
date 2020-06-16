package com.example.devutils.utils.collection;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Created by AMe on 2020-06-13 11:28.
 */
public class MapUtils {

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

    public static <K, V, M extends Map<K, V>> M actual(Map map, Supplier<M> templateSupplier) {
        return (M) map;
    }

    public static <K, V> LinkedHashMap<K, V> sort(HashMap<K, V> hashMap, Comparator<Entry<K, V>> comparator) {
        return hashMap.entrySet().stream().sorted(comparator).collect(Collectors.toMap(Entry::getKey, Entry::getValue, (oldItem, newItem) -> newItem, LinkedHashMap::new));
    }

    /**
     * 拉平Map
     * Map<String, List<String>> -> R<String>
     */
    public static <K, V extends Collection<VI>, VI, R extends Collection<VI>> R flat(Map<K, V> map, Supplier<R> supplier) {
        return map.values().stream().flatMap(Collection::stream).collect(Collectors.toCollection(supplier));
    }

}