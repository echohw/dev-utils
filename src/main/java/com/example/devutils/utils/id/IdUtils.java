package com.example.devutils.utils.id;

import java.util.UUID;

/**
 * Created by AMe on 2020-07-20 01:21.
 */
public class IdUtils {

    private static final IdGeneratorHolder<UUID, String> UUID_GENERATOR = new IdGeneratorHolder<>((UUID) null, uuid -> UUID.randomUUID().toString());
    private static final IdGeneratorHolder<SnowFlake, Long> SNOWID_GENERATOR = new IdGeneratorHolder<>(new SnowFlake(), SnowFlake::nextId);

    public static String nextUuid() {
        return nextUuid(false);
    }

    public static String nextUuid(boolean withSeparator) {
        String uuid = UUID_GENERATOR.next();
        return withSeparator ? uuid : uuid.replace("-", "");
    }

    public static Long nextSnowId() {
        return SNOWID_GENERATOR.next();
    }
}
