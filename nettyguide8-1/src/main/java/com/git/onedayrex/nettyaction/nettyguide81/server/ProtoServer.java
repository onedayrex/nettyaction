package com.git.onedayrex.nettyaction.nettyguide81.server;

import com.git.onedayrex.nettyaction.nettyguide81.dto.OrderInfoReqOuterClass;
import com.git.onedayrex.nettyaction.nettyguide81.handler.ProtoServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.logging.LoggingHandler;

public class ProtoServer {
    private final int port;

    public ProtoServer(int port) {
        this.port = port;
    }

    public void start() {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try{
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workGroup).channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 100)
                    .handler(new LoggingHandler())
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //添加protobuf按消息长度解码
                            ch.pipeline().addLast(new ProtobufVarint32FrameDecoder());
                            //添加protobuf按消息解码成pojo
                            ch.pipeline().addLast(new ProtobufDecoder(OrderInfoReqOuterClass.OrderInfoReq.getDefaultInstance()));
                            //添加把protobuf按消息长度来编码
                            ch.pipeline().addLast(new ProtobufVarint32LengthFieldPrepender());
                            //添加把protobuf按pojo来编码
                            ch.pipeline().addLast(new ProtobufEncoder());
                            ch.pipeline().addLast(new ProtoServerHandler());
                        }
                    });
            ChannelFuture sync = b.bind(port).sync();
            sync.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        ProtoServer protoServer = new ProtoServer(22488);
        protoServer.start();
    }
}
