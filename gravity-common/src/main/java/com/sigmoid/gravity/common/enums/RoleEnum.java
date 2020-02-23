package com.sigmoid.gravity.common.enums;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum RoleEnum {

    Tracer_Master(1),

    Tracer_Slave(2),

    Storage_Master(3),

    Storage_Slave(4);

    @Getter
    private int value;

    private static Map<Integer, RoleEnum> roleMapping = Maps.newHashMap();

    static {
        Lists.newArrayList(RoleEnum.values()).forEach(roleEnum -> roleMapping.put(roleEnum.getValue(), roleEnum));
    }

    public static RoleEnum getByValue(int type) {
        return roleMapping.get(type);
    }

}
