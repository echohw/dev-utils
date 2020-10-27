package com.example.devutils.utils;

import com.example.devutils.utils.text.StringUtils;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.joor.Reflect;
import org.joor.ReflectException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by AMe on 2020-06-18 13:24.
 */
public class ReflectUtils {

    private static final Logger logger = LoggerFactory.getLogger(ReflectUtils.class);

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

    public static boolean fieldExists(Class<?> clazz, String fieldName) {
        return fieldExists(clazz, fieldName, false, false);
    }

    public static boolean fieldExists(Class<?> clazz, String fieldName, boolean onlyPublic, boolean onlySelf) {
        return getField(clazz, fieldName, onlyPublic, onlySelf) != null;
    }

    public static Field getField(Class<?> clazz, String fieldName) {
        return getField(clazz, fieldName, false, false);
    }

    public static Field getField(Class<?> clazz, String fieldName, boolean onlyPublic, boolean onlySelf) {
        while (clazz != null) {
            try {
                return onlyPublic ? clazz.getField(fieldName) : clazz.getDeclaredField(fieldName);
            } catch (NoSuchFieldException ex) {
                clazz = onlySelf ? null : clazz.getSuperclass();
            }
        }
        return null;
    }

    public static List<String> getAllFieldNames(Class<?> clazz) {
        return getAllFieldNames(clazz, false, false);
    }

    public static List<String> getAllFieldNames(Class<?> clazz, boolean onlyPublic, boolean onlySelf) {
        return getAllFields(clazz, onlyPublic, onlySelf).stream().map(Field::getName).collect(Collectors.toList());
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

    public static <T> T getFieldValue(Class<?> clazz, String fieldName) {
        return Reflect.onClass(clazz).get(fieldName);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getFieldValue(Object obj, Field field) {
        try {
            return (T) accessible(field).get(obj);
        } catch (Exception ex) {
            throw new ReflectException(ex);
        }
    }

    public static <T> T getFieldValue(Class<?> clazz, Field field) {
        return getFieldValue((Object) clazz, field);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getFieldValuePreferredMethod(Object obj, String fieldName, Object... args) {
        Class<?> clazz = obj.getClass();
        Class<?> fieldType = getField(clazz, fieldName).getType();
        String methodName;
        String capFieldName = StringUtils.capitalize(fieldName);
        if (fieldType == Boolean.class || fieldType == boolean.class) {
            methodName = "is" + capFieldName;
            try {
                return (T) invokeMethod(obj, methodName, args);
            } catch (ReflectException ex) {
                logger.warn("Method invoke failed. class: {}, methodName: {}, args: {}", clazz.getName(), methodName, args);
            }
        }
        methodName = "get" + capFieldName;
        try {
            return (T) invokeMethod(obj, methodName, args);
        } catch (ReflectException ex) {
            logger.warn("Method invoke failed. class: {}, methodName: {}, args: {}", clazz.getName(), methodName, args);
        };
        return getFieldValue(obj, fieldName);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getFieldValuePreferredMethod(Class<?> clazz, String fieldName, Object... args) {
        Class<?> fieldType = getField(clazz, fieldName).getType();
        String methodName;
        String capFieldName = StringUtils.capitalize(fieldName);
        if (fieldType == Boolean.class || fieldType == boolean.class) {
            methodName = "is" + capFieldName;
            try {
                return (T) invokeMethod(clazz, methodName, args);
            } catch (ReflectException ex) {
                logger.warn("Method invoke failed. class: {}, methodName: {}, args: {}", clazz.getName(), methodName, args);;
            }
        }
        methodName = "get" + capFieldName;
        try {
            return (T) invokeMethod(clazz, methodName, args);
        } catch (ReflectException ex) {
            logger.warn("Method invoke failed. class: {}, methodName: {}, args: {}", clazz.getName(), methodName, args);;
        }
        return getFieldValue(clazz, fieldName);
    }

    public static void setFieldValue(Object obj, String fieldName, Object fieldValue) {
        Reflect.on(obj).set(fieldName, fieldValue);
    }

    public static void setFieldValue(Class<?> clazz, String fieldName, Object fieldValue) {
        Reflect.onClass(clazz).set(fieldName, fieldValue);
    }

    public static boolean methodExists(Class<?> clazz, String methodName, Class<?>... types) {
        return methodExists(clazz, methodName, false, false, types);
    }

    public static boolean methodExists(Class<?> clazz, String methodName, boolean onlyPublic, boolean onlySelf, Class<?>... types) {
        return getMethod(clazz, methodName, onlyPublic, onlySelf, types) != null;
    }

    public static Method getMethod(Class<?> clazz, String methodName, Class<?>... types) {
        return getMethod(clazz, methodName, false, false, types);
    }

    public static Method getMethod(Class<?> clazz, String methodName, boolean onlyPublic, boolean onlySelf, Class<?>... types) {
        while (clazz != null) {
            try {
                return onlyPublic ? clazz.getMethod(methodName, types) : clazz.getDeclaredMethod(methodName, types);
            } catch (NoSuchMethodException ex) {
                clazz = onlySelf ? null : clazz.getSuperclass();
            }
        }
        return null;
    }

    public static List<String> getAllMethodNames(Class<?> clazz) {
        return getAllMethodNames(clazz, false, false);
    }

    public static List<String> getAllMethodNames(Class<?> clazz, boolean onlyPublic, boolean onlySelf) {
        return getAllMethods(clazz, onlyPublic, onlySelf).stream().map(Method::getName).collect(Collectors.toList());
    }

    public static List<Method> getAllMethods(Class<?> clazz, boolean onlyPublic, boolean onlySelf) {
        return getAllMethods(clazz, onlyPublic, onlySelf, method -> true);
    }

    public static List<Method> getAllMethods(Class<?> clazz, boolean onlyPublic, boolean onlySelf, Predicate<Method> methodPredicate) {
        return getAllMethods(clazz, onlyPublic, onlySelf, methodPredicate, ArrayList::new);
    }

    public static <C extends Collection<Method>> C getAllMethods(Class<?> clazz, boolean onlyPublic, boolean onlySelf, Predicate<Method> methodPredicate, Supplier<C> collectionSupplier) {
        List<Stream<Method>> methodStreamList = new ArrayList<>();
        while (clazz != null) {
            Stream<Method> methodStream = Arrays.stream(onlyPublic ? clazz.getMethods() : clazz.getDeclaredMethods()).filter(methodPredicate);
            methodStreamList.add(methodStream);
            clazz = onlySelf ? null : clazz.getSuperclass();
        }
        return methodStreamList.stream().flatMap(stream -> stream).collect(Collectors.toCollection(collectionSupplier));
    }

    public static <T> T invokeMethod(Object obj, String methodName, Object... args) {
        return Reflect.on(obj).call(methodName, args).get();
    }

    public static <T> T invokeMethod(Class<?> clazz, String methodName, Object... args) {
        return Reflect.onClass(clazz).call(methodName, args).get();
    }

    @SuppressWarnings("unchecked")
    public static <T> T invokeMethod(Object obj, Method method, Object... args) {
        try {
            Object returnVal = accessible(method).invoke(obj, args);
            return (T) (methodHasReturn(method) ? returnVal : obj);
        } catch (Exception ex) {
            throw new ReflectException(ex);
        }
    }

    public static <T> T invokeMethod(Class<?> clazz, Method method, Object... args) {
        return invokeMethod((Object) clazz, method, args);
    }

    public static boolean methodHasReturn(Method method) {
        return method.getReturnType() != Void.TYPE;
    }

    public static <T extends AccessibleObject> T accessible(T accessible) {
        return Reflect.accessible(accessible);
    }
}
