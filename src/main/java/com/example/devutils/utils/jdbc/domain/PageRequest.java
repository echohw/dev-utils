package com.example.devutils.utils.jdbc.domain;

import java.io.Serializable;
import org.springframework.data.domain.Sort;

/**
 * Created by AMe on 2020-07-22 16:26.
 */
public class PageRequest implements Pageable, Serializable {

    private final int page;
    private final int size;

    protected PageRequest(int page, int size) {
        if (page < 0) {
            throw new IllegalArgumentException("PageImpl index must not be less than zero!");
        }
        if (size < 1) {
            throw new IllegalArgumentException("PageImpl size must not be less than one!");
        }
        this.page = page;
        this.size = size;
    }

    public static PageRequest of(int page, int size) {
        return new PageRequest(page, size);
    }

    @Override
    public int getPageSize() {
        return size;
    }

    @Override
    public int getPageNumber() {
        return page;
    }

    @Override
    public long getOffset() {
        return page * size;
    }

    @Override
    public boolean hasPrevious() {
        return page > 0;
    }

    @Override
    public PageRequest previousOrFirst() {
        return hasPrevious() ? previous() : first();
    }

    @Override
    public PageRequest next() {
        return new PageRequest(getPageNumber() + 1, getPageSize());
    }

    public PageRequest previous() {
        return getPageNumber() == 0 ? this : new PageRequest(getPageNumber() - 1, getPageSize());
    }

    @Override
    public PageRequest first() {
        return new PageRequest(0, getPageSize());
    }

}
