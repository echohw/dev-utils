package com.example.devutils.utils.id;

import java.util.Objects;
import java.util.function.Function;

/**
 * Created by AMe on 2020-10-23 20:03.
 */
public class IdGeneratorHolder<T, R> {

    private final T idGenerator;
    private final Function<T, R> generateRule;

    public IdGeneratorHolder(T idGenerator, Function<T, R> generateRule) {
        this.idGenerator = idGenerator;
        this.generateRule = Objects.requireNonNull(generateRule);
    }

    public R next() {
        return generateRule.apply(idGenerator);
    }
}
