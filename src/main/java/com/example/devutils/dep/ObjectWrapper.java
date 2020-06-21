package com.example.devutils.dep;

import java.util.Optional;
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
        return Optional.ofNullable(hashCodeFunc).map(func -> func.apply(object)).orElseGet(() -> object.hashCode());
    }

    @Override
    public boolean equals(Object object2) {
        if (object2 instanceof ObjectWrapper) {
            object2 = ((ObjectWrapper) object2).object;
        }
        if (object == object2) {
            return true;
        }
        if (object == null || object2 == null) {
            return false;
        }
        if (object2.getClass() == object.getClass() || force) {
            Object o2 = object2;
            return Optional.ofNullable(equalsBiFunc).map(func -> func.apply(object, (T) o2)).orElseGet(() -> object.equals(o2));
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return object.toString();
    }
}
