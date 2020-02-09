package com.sigmoid.gravity.common.thread;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

public class GravityThreadFactory implements ThreadFactory {

    private static final String prefix = "gravity-thread-";

    private static final AtomicLong index = new AtomicLong(-1L);

    private String getName() {
        return prefix + index.incrementAndGet();
    }

    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r);
        thread.setDaemon(false);
        thread.setName(getName());
        return thread;
    }

}
