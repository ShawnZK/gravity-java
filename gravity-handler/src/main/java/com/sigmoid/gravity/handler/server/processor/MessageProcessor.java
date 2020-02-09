package com.sigmoid.gravity.handler.server.processor;

import com.google.common.util.concurrent.FutureCallback;
import com.sigmoid.gravity.handler.server.thread.ServerThreadPool;
import com.sigmoid.gravity.proto.GravityMessageProto;

import java.util.concurrent.Callable;

public abstract class MessageProcessor {

    protected abstract FutureCallback buildCallback(GravityMessageProto.GravityMessage gravityMessage);

    protected abstract Callable buildTask(GravityMessageProto.GravityMessage gravityMessage);

    public void process(GravityMessageProto.GravityMessage gravityMessage) {
        ServerThreadPool.submit(buildTask(gravityMessage), buildCallback(gravityMessage));
    }

}
