package com.sigmoid.gravity.common.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum MessageTypeEnum {

    Heartbeat(1),

    Push(2),

    PushAck(3),

    Read(4),

    ReadAck(5),

    ReadPrepare(6),

    ReadPrepareAck(7),

    StatusSync(8),

    Write(9),

    WriteAck(10),

    WritePrepare(11),

    WritePrepareAck(12);

    @Getter
    private int value;

}
