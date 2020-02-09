package com.sigmoid.gravity.common.sequence;

import java.util.UUID;

public class UuidGenerator {

    public static final String generateUuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

}
