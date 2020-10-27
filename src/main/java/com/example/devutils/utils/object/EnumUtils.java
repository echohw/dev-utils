package com.example.devutils.utils.object;

import com.example.devutils.utils.ReflectUtils;
import com.example.devutils.utils.collection.CollectionUtils;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by AMe on 2020-10-22 14:17.
 */
public class EnumUtils {

    public static final List<String> ENUM_NATIVE_FIELDS = Arrays.asList("name", "ordinal");
    public static final String DEFAULT_ARRAY_NAME = "$VALUES";

    public static boolean isEnumClass(Class<?> clazz) {
        return clazz != null && Enum.class.isAssignableFrom(clazz);
    }

    public static boolean isEnumNativeArray(Class<? extends Enum> clazz, String fieldName) {
        Object fieldValue = ReflectUtils.getFieldValue(clazz, fieldName);
        if (!(fieldValue.getClass().isArray())) {
            return false;
        } else {
            return Arrays.equals(((Object[]) fieldValue), clazz.getEnumConstants());
        }
    }

    public static <T extends Enum> List<T> getEnumValues(Class<T> clazz) {
        if (!isEnumClass(clazz)) {
            return Collections.emptyList();
        } else {
            return Arrays.asList(clazz.getEnumConstants());
        }
    }

    public static List<String> getEnumValueNames(Class<? extends Enum> clazz) {
        List<? extends Enum> enumValues = getEnumValues(clazz);
        return enumValues.stream().map(Enum::name).collect(Collectors.toList());
    }

    public static List<Field> getEnumFields(Class<? extends Enum> clazz, boolean onlyPublic, boolean onlySelf) {
        List<? extends Enum> enumValues = getEnumValues(clazz);
        if (CollectionUtils.isEmpty(enumValues)) {
            return Collections.emptyList();
        } else {
            List<Field> allFields = ReflectUtils.getAllFields(clazz, onlyPublic, onlySelf);
            return allFields.subList(enumValues.size(), allFields.size());
        }
    }

    public static List<String> getEnumFieldNames(Class<? extends Enum> clazz, boolean onlyPublic, boolean onlySelf) {
        List<Field> enumFields = getEnumFields(clazz, onlyPublic, onlySelf);
        return enumFields.stream().map(Field::getName).collect(Collectors.toList());
    }
}