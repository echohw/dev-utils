package com.example.devutils.deps;

/**
 * Created by AMe on 2020-06-08 23:40.
 */
public interface DataAgency<I, O> {

    I getIn();

    O getOut();
}
