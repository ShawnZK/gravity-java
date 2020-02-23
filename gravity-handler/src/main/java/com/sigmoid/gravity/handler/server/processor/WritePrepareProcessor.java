package com.sigmoid.gravity.handler.server.processor;

import com.google.common.util.concurrent.FutureCallback;
import com.sigmoid.gravity.proto.GravityMessageProto;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.concurrent.Callable;

public class WritePrepareProcessor extends MessageProcessor {
    @Override
    protected FutureCallback buildCallback(GravityMessageProto.GravityMessage gravityMessage) {
        return new FutureCallback() {
            @Override
            public void onSuccess(@Nullable Object o) {

            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        };
    }

    @Override
    protected Callable buildTask(GravityMessageProto.GravityMessage gravityMessage) {
        return () -> {return 0;};
    }
}
