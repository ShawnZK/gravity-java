package com.sigmoid.gravity.tracer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class FileMeta {

    private String uuid;

    private String storageNodeId;

    private int sizeInByte;

    private long createTime;

    private long lastReadTime;

}
