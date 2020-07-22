package com.example.devutils.utils.jdbc.domain;

import java.util.Optional;

/**
 * Created by AMe on 2020-07-22 16:52.
 */
public interface Pageable {

    static Pageable unpaged() {
        return Unpaged.INSTANCE;
    }

    default boolean isPaged() {
        return true;
    }

    default boolean isUnpaged() {
        return !isPaged();
    }

    int getPageNumber();

    int getPageSize();

    long getOffset();

    Pageable next();

    Pageable previousOrFirst();

    Pageable first();

    boolean hasPrevious();

    default Optional<Pageable> toOptional() {
        return isUnpaged() ? Optional.empty() : Optional.of(this);
    }
}
