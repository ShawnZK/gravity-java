package com.sigmoid.gravity.handler.server;

import com.google.common.collect.Maps;
import com.sigmoid.gravity.handler.server.processor.*;
import com.sigmoid.gravity.proto.GravityMessageProto;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Optional;

import static com.sigmoid.gravity.common.enums.MessageTypeEnum.*;

@Slf4j
@ChannelHandler.Sharable
public class ServerHandler extends SimpleChannelInboundHandler<GravityMessageProto.GravityMessage> {

    private static final ServerHandler serverHandler = new ServerHandler();

    public static ServerHandler get() {
        return serverHandler;
    }

    private Map<Integer, MessageProcessor> processorMap;

    private ServerHandler() {
        processorMap = Maps.newHashMap();
        processorMap.put(Heartbeat.getValue(), new HeartbeatProcessor());
        processorMap.put(Push.getValue(), new PushProcessor());
        processorMap.put(PushAck.getValue(), new PushAckProcessor());
        processorMap.put(Read.getValue(), new ReadProcessor());
        processorMap.put(ReadAck.getValue(), new ReadAckProcessor());
        processorMap.put(ReadPrepare.getValue(), new ReadPrepareProcessor());
        processorMap.put(ReadPrepareAck.getValue(), new ReadPrepareAckProcessor());
        processorMap.put(StatusSync.getValue(), new StatusSyncProcessor());
        processorMap.put(Write.getValue(), new WriteProcessor());
        processorMap.put(WriteAck.getValue(), new WriteAckProcessor());
        processorMap.put(WritePrepare.getValue(), new WritePrepareProcessor());
        processorMap.put(WritePrepareAck.getValue(), new WritePrepareAckProcessor());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GravityMessageProto.GravityMessage msg) throws Exception {
        //TODO any exception or check here?
        Optional.ofNullable(processorMap.get(msg.getType()))
                .ifPresent(messageProcessor -> messageProcessor.process(msg));
    }

}
