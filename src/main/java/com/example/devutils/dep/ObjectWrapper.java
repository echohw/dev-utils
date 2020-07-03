package com.example.devutils.dep;

import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.ToIntFunction;

/**
 * Created by AMe on 2020-06-18 16:40.
 */
public class ObjectWrapper<T> {

    private T object;
    private ToIntFunction<T> hashCodeFunc;
    private BiPredicate<T, T> equalsPred;
    private boolean force = false;

    public ObjectWrapper(T object, ToIntFunction<T> hashCodeFunc, BiPredicate<T, T> equalsPred) {
        this.object = object;
        this.hashCodeFunc = hashCodeFunc;
        this.equalsPred = equalsPred;
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
        return Optional.ofNullable(hashCodeFunc).map(func -> func.applyAsInt(object)).orElseGet(() -> object.hashCode());
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
            return Optional.ofNullable(equalsPred).map(func -> func.test(object, (T) o2)).orElseGet(() -> object.equals(o2));
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return object.toString();
    }
}
