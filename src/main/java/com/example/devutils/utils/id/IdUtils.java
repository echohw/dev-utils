package com.example.devutils.utils.id;

import java.util.Objects;
import java.util.UUID;

/**
 * Created by AMe on 2020-07-20 01:21.
 */
public class IdUtils {

    private SnowFlake snowFlake;

    public IdUtils(SnowFlake snowFlake) {
        this.snowFlake = Objects.requireNonNull(snowFlake);
    }

    public static String nextUuid() {
        return nextUuid(false);
    }

    public static String nextUuid(boolean withSeparator) {
        String uuid = UUID.randomUUID().toString();
        return withSeparator ? uuid : uuid.replaceAll("-", "");
    }

    public long nextSnowId() {
        return this.snowFlake.nextId();
    }

}
