package com.example.devutils.dep;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Created by AMe on 2020-06-18 16:40.
 */
public class ObjectWrapper<T> {

    private T object;
    private Function<T, Integer> hashCodeFunc;
    private BiFunction<T, T, Boolean> equalsBiFunc;
    private boolean force = false;

    public ObjectWrapper(T object, Function<T, Integer> hashCodeFunc, BiFunction<T, T, Boolean> equalsBiFunc) {
        this.object = object;
        this.hashCodeFunc = hashCodeFunc;
        this.equalsBiFunc = equalsBiFunc;
    }

    public ObjectWrapper force(boolean force) {
        this.force = force;
        return this;
    }

    public T wrapped() {
        return object;
    }

    @Override
    public int hashCode() {
        return hashCodeFunc.apply(object);
    }

    @Override
    public boolean equals(Object o2) {
        if (o2 instanceof ObjectWrapper) {
            o2 = ((ObjectWrapper) o2).object;
        }
        if (o2.getClass() == object.getClass() || force) {
            return equalsBiFunc.apply(object, (T) o2);
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return object.toString();
    }
}
