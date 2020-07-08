package com.example.devutils.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.joor.Reflect;

/**
 * Created by AMe on 2020-06-18 13:24.
 */
public class ReflectUtils {

    @SuppressWarnings("unchecked")
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

    public static List<String> getAllFieldNames(Class<?> clazz) {
        return getAllFields(clazz, false, false).stream().map(Field::getName).collect(Collectors.toCollection(ArrayList::new));
    }

    public static List<Field> getAllFields(Class<?> clazz, boolean onlyPublic, boolean onlySelf) {
        return getAllFields(clazz, onlyPublic, onlySelf, field -> true);
    }

    public static List<Field> getAllFields(Class<?> clazz, boolean onlyPublic, boolean onlySelf, Predicate<Field> fieldPredicate) {
       return getAllFields(clazz, onlyPublic, onlySelf, fieldPredicate, ArrayList::new);
    }

    public static <C extends Collection<Field>> C getAllFields(Class<?> clazz, boolean onlyPublic, boolean onlySelf, Predicate<Field> fieldPredicate, Supplier<C> collectionSupplier) {
        List<Stream<Field>> fieldStreamList = new ArrayList<>();
        while (clazz != null) {
            Stream<Field> fieldStream = Arrays.stream(onlyPublic ? clazz.getFields() : clazz.getDeclaredFields()).filter(fieldPredicate);
            fieldStreamList.add(fieldStream);
            clazz = onlySelf ? null : clazz.getSuperclass();
        }
        return fieldStreamList.stream().flatMap(stream -> stream).collect(Collectors.toCollection(collectionSupplier));
    }

    public static <T> T getFieldValue(Object obj, String fieldName) {
        return Reflect.on(obj).get(fieldName);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getFieldValue(Object obj, Field field) throws IllegalAccessException {
        if (!field.isAccessible()) {
            field.setAccessible(true);
        }
        return (T) field.get(obj);
    }

    public static <T> T setFieldValue(Object obj, String fieldName, Object fieldValue) {
        return Reflect.on(obj).set(fieldName, fieldValue).get();
    }

    public static Object invokeMethod(Object obj, String methodName, Object... args) {
        return Reflect.on(obj).call(methodName, args).get();
    }

}
