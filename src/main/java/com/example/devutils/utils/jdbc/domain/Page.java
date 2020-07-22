package com.example.devutils.utils.jdbc.domain;

import java.util.List;

/**
 * Created by AMe on 2020-07-22 16:41.
 */
public interface Page<T> extends Iterable<T> {

    default boolean isEmpty() {
        return !iterator().hasNext();
    }

    int getNumber();

    int getSize();

    int getNumberOfElements();

    List<T> getContent();

    boolean hasContent();

    boolean isFirst();

    boolean isLast();

    boolean hasNext();

    boolean hasPrevious();

    default Pageable getPageable() {
        return PageRequest.of(getNumber(), getSize());
    }

    Pageable nextPageable();

    Pageable previousPageable();

    default Pageable nextOrLastPageable() {
        return hasNext() ? nextPageable() : getPageable();
    }

    default Pageable previousOrFirstPageable() {
        return hasPrevious() ? previousPageable() : getPageable();
    }

    int getTotalPages();

    long getTotalElements();

}
