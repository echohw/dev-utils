package com.example.devutils.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by AMe on 2020-06-08 15:34.
 */
public class ObjectUtils {

    public static <T extends Serializable> byte[] writeObject(T object) throws IOException {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        try (
            ObjectOutputStream objOut = new ObjectOutputStream(byteOut);
        ) {
            objOut.writeObject(object);
        }
        return byteOut.toByteArray();
    }

    public static <T extends Serializable> T readObject(byte[] objByteArr) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteIn = new ByteArrayInputStream(objByteArr);
        try (
            ObjectInputStream objIn = new ObjectInputStream(byteIn);
        ) {
            return (T) objIn.readObject();
        }
    }

    public static <T extends Serializable> T deepClone(T object) throws IOException, ClassNotFoundException {
        return readObject(writeObject(object));
    }

}
