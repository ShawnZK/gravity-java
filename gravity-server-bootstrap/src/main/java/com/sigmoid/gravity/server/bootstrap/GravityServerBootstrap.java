package com.sigmoid.gravity.server.bootstrap;

import com.sigmoid.gravity.common.config.ServerConfig;
import com.sigmoid.gravity.handler.server.ServerHandler;
import com.sigmoid.gravity.handler.server.thread.ServerThreadPool;
import com.sigmoid.gravity.proto.GravityMessageProto;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GravityServerBootstrap {

    private static final int bossCount = 1;

    private static final boolean isLinux = System.getProperty("os.name").toLowerCase().indexOf("linux") >= 0;

    private static void addOptions(ServerBootstrap serverBootstrap) {
        //TODO
    }

    private static void createThreadPool(ServerConfig serverConfig) {
        ServerThreadPool.init(serverConfig);
    }

    public static void create(ServerConfig serverConfig) {

        createThreadPool(serverConfig);

        EventLoopGroup bossGroup = isLinux ? new EpollEventLoopGroup(bossCount) : new NioEventLoopGroup(bossCount);
        EventLoopGroup workerGroup = isLinux ? new EpollEventLoopGroup() : new NioEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap();

        serverBootstrap.group(bossGroup, workerGroup)
                .channel(isLinux ? EpollServerSocketChannel.class : NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new ProtobufVarint32FrameDecoder())
                                .addLast(new ProtobufDecoder(GravityMessageProto.GravityMessage.getDefaultInstance()))
                                .addLast(new ProtobufVarint32LengthFieldPrepender())
                                .addLast(new ProtobufEncoder())
                                .addLast(ServerHandler.get());
                    }
                });
        addOptions(serverBootstrap);

        try {
            ChannelFuture channelFuture = serverBootstrap.bind(serverConfig.getPort()).sync()
                    .addListener(future -> {
                        if (future.isSuccess()) {
                            log.info("Start server success");
                            //TODO
                        } else {
                            log.info("Start server fail");
                            //TODO
                        }
                    });
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            log.error("ServerBootstrap error", e);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }

}
