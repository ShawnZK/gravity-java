package com.sigmoid.gravity.common.enums;

import com.google.common.collect.Maps;
import com.google.common.collect.Streams;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import java.util.Map;
import java.util.Optional;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ServerTypeEnum {

    Tracer(1, "tracer"),

    Storage(2, "storage");

    private int value;

    private String desc;

    private static Map<Integer, ServerTypeEnum> typeMapping = Maps.newHashMap();

    public static ServerTypeEnum getByValue(@NonNull Integer type) {
        return Optional.ofNullable(typeMapping.get(type)).orElse(null);
    }

}
