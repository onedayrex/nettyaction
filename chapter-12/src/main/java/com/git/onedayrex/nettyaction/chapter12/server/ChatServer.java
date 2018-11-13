package com.git.onedayrex.nettyaction.chapter12.server;

import com.git.onedayrex.nettyaction.chapter12.init.WebSocketChannelInit;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.ImmediateEventExecutor;

import java.net.InetSocketAddress;

public class ChatServer {

    private final ChannelGroup group = new DefaultChannelGroup(ImmediateEventExecutor.INSTANCE);

    private final EventLoopGroup eventExecutors = new NioEventLoopGroup();

    private Channel channel;

    public ChannelFuture start(InetSocketAddress inetSocketAddress) {
        ServerBootstrap b = new ServerBootstrap();
        b.group(eventExecutors).channel(NioServerSocketChannel.class)
                .childHandler(new WebSocketChannelInit(group));
        ChannelFuture channelFuture = b.bind(inetSocketAddress).syncUninterruptibly();
        return channelFuture;
    }

    public static void main(String[] args) {
        ChatServer chatServer = new ChatServer();
        ChannelFuture start = chatServer.start(new InetSocketAddress(2048));
        start.channel().closeFuture().syncUninterruptibly();
    }
}
