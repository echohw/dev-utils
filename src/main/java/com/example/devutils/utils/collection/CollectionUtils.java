package com.example.devutils.utils.collection;

import com.example.devutils.dep.ObjectWrapper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

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
     * 集合元素分组
     * @param stream 元素集
     * @param mapSupplier Map提供者
     * @param keysComputeFunc key的生成规则
     * @param keyMappingFunc key映射函数
     * @param itemMappingFunc item转换函数
     * @param <T> 流中元素的类型
     * @param <K> Map中Key的类型
     * @param <V> Map中Value的类型
     * @param <I> Value所表示的集合中的元素类型
     * @param <M> Map的类型
     * @return
     */
    public static <T, K, V extends Collection<I>, I, M extends Map<K, V>> M grouping(
        Stream<T> stream,
        Supplier<M> mapSupplier,
        Function<T, List<K>> keysComputeFunc,
        Function<K, V> keyMappingFunc,
        Function<T, I> itemMappingFunc
    ) {
        return stream.collect(mapSupplier, (map, item) -> {
            List<K> keys = keysComputeFunc.apply(item);
            if (isNotEmpty(keys)) {
                I i = itemMappingFunc.apply(item);
                for (K key : keys) {
                    V v = map.computeIfAbsent(key, keyMappingFunc);
                    v.add(i);
                }
            }
        }, M::putAll);
    }

    public static <T, K, M extends Map<K, List<T>>> M grouping(Collection<T> collection, Supplier<M> mapSupplier, Function<T, List<K>> keysComputeFunc) {
        return grouping(collection.stream(), mapSupplier, keysComputeFunc, key -> new ArrayList<>(), item -> item);
    }

    public static <T, K> Map<K, List<T>> grouping(Collection<T> collection, Function<T, List<K>> keysComputeFunc) {
         return grouping(collection, LinkedHashMap::new, keysComputeFunc);
    }

    /**
     * 集合元素分组(元素在顺序上应该存在一定的关联)
     * @param collection 元素集
     * @param predicate 关联断言函数
     * @param <T> 元素类型
     * @return
     */
    public static <T, C extends Collection<T>> List<C> grouping(Collection<T> collection, BiPredicate<T, T> predicate, Supplier<C> collectionSupplier) {
        AtomicReference<T> preRef = new AtomicReference<>();
        AtomicInteger keyRef = new AtomicInteger(0);
        Function<T, List<String>> keysComputeFunc = item -> {
            if (keyRef.get() == 0 || !predicate.test(preRef.get(), item)) {
                keyRef.incrementAndGet();
            }
            preRef.set(item);
            return Collections.singletonList(String.valueOf(keyRef.get()));
        };
        return new ArrayList<>(grouping(collection.stream(), LinkedHashMap::new, keysComputeFunc, key -> collectionSupplier.get(), item -> item).values());
    }

    /**
     * 集合元素计数
     * @param collection 元素集
     * @param keyFunc key的生成规则
     * @param <T> Collection中元素的类型
     * @param <K> Map中Key的类型
     * @return Map<Key, Integer>
     */
    public static <T, K> Map<K, Integer> count(Collection<T> collection, Function<T, K> keyFunc) {
        return collection.stream().collect(HashMap::new, (map, item) -> {
            K key = keyFunc.apply(item);
            map.put(key, map.getOrDefault(key, 0) + 1);
        }, HashMap::putAll);
    }

    /**
     * 集合元素拉平
     * @param collections 元素集
     * @param collectionSupplier Collection提供者
     * @param <T> Collection中元素的类型
     * @param <I> Collection中元素的类型
     * @param <R> Collection的类型
     * @return Collection<Item>
     */
    public static <T extends Collection<I>, I, R extends Collection<I>> R flat(Supplier<R> collectionSupplier, Collection<T> collections) {
        return collections.stream().flatMap(Collection::stream).collect(Collectors.toCollection(collectionSupplier));
    }

    /**
     * 集合元素去重[手动定义hashCode及equals策略]
     * @param collection 元素集
     * @param hashCodeFunc hashCode计算函数
     * @param equalsPred equals比较函数
     * @param collectionSupplier Collection提供者
     * @param <I> Collection中元素的类型
     * @param <C> Collection的类型
     * @return Collection<Item>
     */
    public static <I, C extends Collection<I>> C distinct(Collection<I> collection, ToIntFunction<I> hashCodeFunc, BiPredicate<I, Object> equalsPred, Supplier<C> collectionSupplier) {
        return collection.stream().map(item -> new ObjectWrapper<>(item, hashCodeFunc, equalsPred)).distinct().map(
            ObjectWrapper::wrapped).collect(Collectors.toCollection(collectionSupplier));
    }

    /**
     * 计算交集
     */
    public static <I, C extends Collection<I>, R extends Collection<I>> R intersection(Collection<C> collections, Supplier<R> collectionSupplier) {
        return collections.stream().map(Collection::stream).reduce((s1, s2) -> {
            HashSet<I> hashSet = s2.collect(Collectors.toCollection(HashSet::new));
            return s1.filter(hashSet::contains);
        }).orElse(Stream.empty()).collect(Collectors.toCollection(collectionSupplier));
    }

    /**
     * 计算并集
     */
    public static <I, C extends Collection<I>, R extends Collection<I>> R union(Collection<C> collections, Supplier<R> collectionSupplier) {
        return collections.stream().flatMap(Collection::stream).distinct().collect(Collectors.toCollection(collectionSupplier));
    }

    /**
     * 计算差集
     */
    public static <I, C extends Collection<I>> C subtract(Collection<I> c1, Collection<I> c2, Supplier<C> collectionSupplier) {
        HashSet<I> hashSet = new HashSet<>(c2);
        return c1.stream().filter(item -> !hashSet.contains(item)).collect(Collectors.toCollection(collectionSupplier));
    }

    /**
     * 集合元素组合
     */
    public static <T> List<List<T>> combine(Collection<T> c1, Collection<T> c2) {
        return c1.stream().flatMap(item1 -> c2.stream().map(item2 -> Arrays.asList(item1, item2))).collect(Collectors.toList());
    }

    /**
     * 将每个Collection中对应位置的元素进行打包
     */
    public static <I, C extends Collection<I>, R extends Collection<I>> List<R> zip(Collection<C> collections, Supplier<R> collectionSupplier) {
        int min = collections.stream().mapToInt(Collection::size).min().orElse(0);
        List<Iterator<I>> iteratorList = collections.stream().map(Collection::iterator).collect(Collectors.toList());
        return IntStream.range(0, min).boxed().map(t_ -> iteratorList.stream().map(Iterator::next).collect(Collectors.toCollection(collectionSupplier))).collect(Collectors.toList());
    }

    public static <I, C extends Collection<I>, R extends Collection<I>> R sort(C collection, Comparator<I> comparator, Supplier<R> supplier) {
        return collection.stream().sorted(comparator).collect(Collectors.toCollection(supplier));
    }
}
