package com.example.devutils.utils.jdbc.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import lombok.Data;

/**
 * Created by AMe on 2020-07-22 15:46.
 */
@Data
public class PageImpl<T> implements Page<T> {

    private final List<T> content = new ArrayList<>();
    private final Pageable pageable;
    private final long total;

    public PageImpl(List<T> content, Pageable pageable, long total) {
        this.content.addAll(content);
        this.pageable = pageable;
        this.total = pageable.toOptional().filter(it -> !content.isEmpty())
            .filter(it -> it.getOffset() + it.getPageSize() > total)
            .map(it -> it.getOffset() + content.size())
            .orElse(total);
    }

    public PageImpl(List<T> content) {
        this(content == null ? Collections.emptyList() : content, Pageable.unpaged(), null == content ? 0 : content.size());
    }

    @Override
    public long getTotalElements() {
        return total;
    }

    @Override
    public int getTotalPages() {
        return getSize() == 0 ? 1 : (int) Math.ceil((double) total / (double) getSize());
    }

    @Override
    public boolean hasPrevious() {
        return getNumber() > 0;
    }

    @Override
    public boolean isFirst() {
        return !hasPrevious();
    }

    @Override
    public boolean hasNext() {
        return getNumber() + 1 < getTotalPages();
    }

    @Override
    public boolean isLast() {
        return !hasNext();
    }

    @Override
    public int getNumber() {
        return pageable.isPaged() ? pageable.getPageNumber() : 0;
    }

    @Override
    public int getSize() {
        return pageable.isPaged() ? pageable.getPageSize() : content.size();
    }

    @Override
    public int getNumberOfElements() {
        return content.size();
    }

    @Override
    public boolean hasContent() {
        return !content.isEmpty();
    }

    @Override
    public Pageable nextPageable() {
        return hasNext() ? pageable.next() : Pageable.unpaged();
    }

    @Override
    public Pageable previousPageable() {
        return hasPrevious() ? pageable.previousOrFirst() : Pageable.unpaged();
    }

    @Override
    public Iterator<T> iterator() {
        return content.iterator();
    }
}
