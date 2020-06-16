package com.example.devutils.utils.collection;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class CollectionUtils {

    /**
     * 分组
     * Collection<T> -> Map<K, List<T>>
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
     * Collection<T> -> Map<K, Integer>
     */
    public static <T, K> Map<K, Integer> count(Collection<T> datas, Function<T, K> function) {
        return datas.stream().collect(HashMap::new, (map, item) -> {
            K key = function.apply(item);
            map.put(key, map.getOrDefault(key, 0) + 1);
        }, HashMap::putAll);
    }

    /**
     * 拉平Collection
     * Collection<List<String>> -> R<String>
     */
    public static <T extends Collection<I>, I, R extends Collection<I>> R flat(Collection<T> collection, Supplier<R> supplier) {
        return collection.stream().flatMap(Collection::stream).collect(Collectors.toCollection(supplier));
    }

}
