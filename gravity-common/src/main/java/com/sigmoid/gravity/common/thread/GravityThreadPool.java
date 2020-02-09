package com.sigmoid.gravity.common.thread;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class GravityThreadPool extends ThreadPoolExecutor {

    public GravityThreadPool(int poolSize, int queueSize) {
        super(poolSize,
                poolSize,
                0L,
                TimeUnit.MILLISECONDS,
                queueSize == 0 ? new SynchronousQueue<Runnable>() : queueSize < 0 ?
                        new LinkedBlockingQueue<Runnable>() : new LinkedBlockingQueue<Runnable>(queueSize),
                new GravityThreadFactory(),
                new GravityThreadPoolAbortPolicy());
    }

}
