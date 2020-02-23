package com.sigmoid.gravity.common.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum FileStatusEnum {

    VALID(1),

    INVALID(2),

    PRE_BUILD(3);

    @Getter
    private int value;

}
