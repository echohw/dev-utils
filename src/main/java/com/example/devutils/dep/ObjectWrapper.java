package com.example.devutils.dep;

import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.ToIntFunction;

/**
 * Created by AMe on 2020-06-18 16:40.
 */
public class ObjectWrapper<T> {

    private final T object;
    private final ToIntFunction<T> hashCodeFunc;
    private final BiPredicate<T, Object> equalsPred;
    private final boolean forceEquals;

    public ObjectWrapper(T object, ToIntFunction<T> hashCodeFunc, BiPredicate<T, Object> equalsPred) {
        this(object, hashCodeFunc, equalsPred, false);
    }

    public ObjectWrapper(T object, ToIntFunction<T> hashCodeFunc, BiPredicate<T, Object> equalsPred, boolean forceEquals) {
        this.object = Objects.requireNonNull(object);
        this.hashCodeFunc = hashCodeFunc;
        this.equalsPred = equalsPred;
        this.forceEquals = forceEquals;
    }

    public T wrapped() {
        return object;
    }

    public ToIntFunction<T> hashCodeFunc() {
        return hashCodeFunc;
    }

    public BiPredicate<T, Object> equalsPred() {
        return equalsPred;
    }

    public boolean forceEquals() {
        return forceEquals;
    }

    @Override
    public int hashCode() {
        return hashCodeFunc != null ? hashCodeFunc.applyAsInt(object) : object.hashCode();
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
        if (object2.getClass() == object.getClass() || forceEquals) {
            return equalsPred != null ? equalsPred.test(object, object2) : object.equals(object2);
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return object.toString();
    }
}