package com.sigmoid.gravity.handler.server.processor;

import com.google.common.util.concurrent.FutureCallback;
import com.sigmoid.gravity.proto.GravityMessageProto;

import java.util.concurrent.Callable;

public class PushProcessor extends MessageProcessor {
    @Override
    protected FutureCallback buildCallback(GravityMessageProto.GravityMessage gravityMessage) {
        return null;
    }

    @Override
    protected Callable buildTask(GravityMessageProto.GravityMessage gravityMessage) {
        return null;
    }
}
