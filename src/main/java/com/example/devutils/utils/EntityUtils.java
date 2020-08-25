package com.example.devutils.utils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.jooq.lambda.Unchecked;
import org.jooq.lambda.tuple.Tuple3;

/**
 * Created by AMe on 2020-07-08 03:17.
 */
public class EntityUtils {

    private static Map<Tuple3<Class<?>, Boolean, Boolean>, List<Field>> fieldCache = new ConcurrentHashMap<>();

    public static List<String> getAllNullPropNames(Object obj) {
        return getAllPropNames(obj, field -> Unchecked.supplier(() -> ReflectUtils.getFieldValue(obj, field)).get() == null);
    }

    public static List<String> getAllNotNullPropNames(Object obj) {
        return getAllPropNames(obj, field -> Unchecked.supplier(() -> ReflectUtils.getFieldValue(obj, field)).get() != null);
    }

    public static List<String> getAllPropNames(Object obj, Predicate<Field> predicate) {
        return getAllFields(obj.getClass()).stream().filter(predicate).map(Field::getName).collect(Collectors.toList());
    }

    public static List<Field> getAllFields(Class<?> clazz) {
        return getAllFields(clazz, false, false);
    }

    public static List<Field> getAllFields(Class<?> clazz, boolean onlyPublic, boolean onlySelf) {
        Tuple3<Class<?>, Boolean, Boolean> tuple3 = new Tuple3<>(clazz, onlyPublic, onlySelf);
        return fieldCache.computeIfAbsent(tuple3, tuple -> ReflectUtils.getAllFields(tuple.v1, tuple.v2, tuple.v3));
    }
}
