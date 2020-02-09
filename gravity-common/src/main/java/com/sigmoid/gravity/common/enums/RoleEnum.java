package com.sigmoid.gravity.common.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum RoleEnum {

    Tracer_Master(1),

    Tracer_Slave(2),

    Storage_Master(3),

    Storage_Slave(4);

    @Getter
    private int value;

}
