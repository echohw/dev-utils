package com.example.devutils.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.joor.Reflect;

/**
 * Created by AMe on 2020-06-18 13:24.
 */
public class ReflectUtils {

    public static <T> Class<? extends T> getClass(T obj) {
        return (Class<? extends T>) obj.getClass();
    }

    public static Class<?> getClass(String className) throws ClassNotFoundException {
        return Class.forName(className);
    }

    public static <T> T newInstance(String className, Object[] args) {
        return Reflect.onClass(className).create(args).get();
    }

    public static <T> T newInstance(Class<T> clazz, Object[] args) {
        return Reflect.onClass(clazz).create(args).get();
    }

    public static boolean fieldExist(Class<?> clazz, String fieldName) {
        return getAllFieldNames(clazz).contains(fieldName);
    }

    public static Set<String> getAllFieldNames(Class<?> clazz) {
        return getAllFields(clazz, false, false).stream().map(Field::getName).collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public static Set<Field> getAllFields(Class<?> clazz, boolean onlyPublic, boolean onlySelf) {
        return getAllFields(clazz, onlyPublic, onlySelf, field -> true);
    }

    public static Set<Field> getAllFields(Class<?> clazz, boolean onlyPublic, boolean onlySelf, Predicate<Field> fieldPredicate) {
       return getAllFields(clazz, onlyPublic, onlySelf, fieldPredicate, LinkedHashSet::new);
    }

    public static <C extends Collection<Field>> C getAllFields(Class<?> clazz, boolean onlyPublic, boolean onlySelf, Predicate<Field> fieldPredicate, Supplier<C> collectionSupplier) {
        List<Stream<Field>> fieldStreamList = new ArrayList<>();
        do {
            Stream<Field> fieldStream = Arrays.stream(onlyPublic ? clazz.getFields() : clazz.getDeclaredFields()).filter(fieldPredicate);
            fieldStreamList.add(fieldStream);
            clazz = onlySelf ? null : clazz.getSuperclass();
        } while (clazz != null);
        return fieldStreamList.stream().flatMap(stream -> stream).collect(Collectors.toCollection(collectionSupplier));
    }

    public static <T> T getFieldValue(Object obj, String fieldName) {
        return Reflect.on(obj).get(fieldName);
    }

    public static <T> T setFieldValue(Object obj, String fieldName, Object fieldValue) {
        return Reflect.on(obj).set(fieldName, fieldValue).get();
    }

    public static Object invokeMethod(Object obj, String methodName, Object... args) {
        return Reflect.on(obj).call(methodName, args).get();
    }

}
