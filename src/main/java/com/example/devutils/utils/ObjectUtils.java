package com.example.devutils.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Created by AMe on 2020-06-08 15:34.
 */
public class ObjectUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T extends Serializable> byte[] writeObject(T object) throws IOException {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        try (
            ObjectOutputStream objOut = new ObjectOutputStream(byteOut);
        ) {
            objOut.writeObject(object);
        } catch (IOException ex) {
            throw ex;
        }
        return byteOut.toByteArray();
    }

    public static <T extends Serializable> T readObject(byte[] objByteArr) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteIn = new ByteArrayInputStream(objByteArr);
        try (
            ObjectInputStream objIn = new ObjectInputStream(byteIn);
        ) {
            return (T) objIn.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            throw ex;
        }
    }

    public static String toJsonStr(Object object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }

    public static <T> T parseJsonStr(String jsonStr, Class<T> clazz) throws IOException {
        return objectMapper.readValue(jsonStr, clazz);
    }

    public static <T> int hashCode(T object, Function<T, Integer> hashCodeFunc) {
        return hashCodeFunc.apply(object);
    }

    public static <T> boolean equals(T o1, T o2, BiFunction<T, T, Boolean> equalsBiFunc) {
        return equalsBiFunc.apply(o1, o2);
    }
}
