package com.sigmoid.gravity.handler.server.thread;

import com.google.common.util.concurrent.*;
import com.sigmoid.gravity.common.config.ServerConfig;
import com.sigmoid.gravity.common.thread.GravityThreadPool;

import java.util.concurrent.Callable;

public class ServerThreadPool {

    private static ListeningExecutorService listeningExecutorService;

    private static boolean initialized = false;

    public static synchronized void init(ServerConfig serverConfig) {
        if (initialized) {
            return;
        }
        GravityThreadPool threadPool = new GravityThreadPool(serverConfig.getThreadCount(), serverConfig.getQueueSize());
        listeningExecutorService = MoreExecutors.listeningDecorator(threadPool);
        initialized = true;
    }

    public static void submit(Callable callable, FutureCallback futureCallback) {
        if (callable == null || futureCallback == null) {
            return;
        }
        ListenableFuture listenableFuture = listeningExecutorService.submit(callable);
        Futures.addCallback(listenableFuture, futureCallback, listeningExecutorService);
    }

}
