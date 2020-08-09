package com.example.devutils.inter;

/**
 * Created by AMe on 2020-06-11 15:42.
 */
public interface Progress<T, R, P> {

    R pre(T t);

    void progress(P p);

    void post(T t, R r);
}
