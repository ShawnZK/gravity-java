package com.sigmoid.gravity.tracer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class FileMeta {

    private String uuid;

    private String storageNodeId;

    private String storagePath;

    private int sizeInByte;

    private long prebuildTime;

    private long createTime;

    private long lastReadTime;

    private int readCount;

    private int status;

    private static final AtomicIntegerFieldUpdater fieldUpdater =
            AtomicIntegerFieldUpdater.newUpdater(FileMeta.class, "readCount");

    public void incReadCount(int delta) {
        int newValue = readCount + delta;
        fieldUpdater.compareAndSet(this, readCount, newValue);
    }

    public void incReadCount() {
        fieldUpdater.incrementAndGet(this);
    }

}
