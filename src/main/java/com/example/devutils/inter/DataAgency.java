package com.example.devutils.inter;

/**
 * Created by AMe on 2020-06-08 23:40.
 */
public interface DataAgency<I, O> {

    I getIn();

    O getOut();
}
