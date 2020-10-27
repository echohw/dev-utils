package com.example.devutils.utils.object;

import java.util.function.Supplier;

/**
 * Created by AMe on 2020-09-05 23:13.
 */
public class SingletonObjectHolder<T> {

    private volatile T instance;

    private Supplier<T> supplier;

    public SingletonObjectHolder(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    public T getInstance() {
        if (instance == null) {
            synchronized (SingletonObjectHolder.class) {
                if (instance == null) {
                    instance = supplier.get();
                }
            }
        }
        return instance;
    }
}
