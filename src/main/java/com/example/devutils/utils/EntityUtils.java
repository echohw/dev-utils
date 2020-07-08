package com.example.devutils.utils;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.jooq.lambda.Unchecked;

/**
 * Created by AMe on 2020-07-08 03:17.
 */
public class EntityUtils {

    private static Map<Class, List<Field>> fieldCache = new ConcurrentHashMap<>();

    public static String[] getNullPropertyNames(Object obj) {
        List<Field> fieldList = getAllFields(obj.getClass());
        List<String> nullFieldList = new LinkedList<>();
        for (Field field : fieldList) {
            Object fieldValue = Unchecked.supplier(() -> ReflectUtils.getFieldValue(obj, field)).get();
            if (fieldValue == null) {
                nullFieldList.add(field.getName());
            }
        }
        return nullFieldList.toArray(new String[0]);
    }

    public static List<Field> getAllFields(Class<?> clazz) {
        return fieldCache.computeIfAbsent(clazz, clz -> ReflectUtils.getAllFields(clz, false, false));
    }

}
