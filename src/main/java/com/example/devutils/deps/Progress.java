package com.example.devutils.deps;

/**
 * Created by AMe on 2020-06-11 15:42.
 */
public interface Progress<T, P> {

    void before(T t);

    void progress(P p);

    void after(T t);
}
