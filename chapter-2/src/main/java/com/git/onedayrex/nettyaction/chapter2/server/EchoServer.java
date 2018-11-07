package com.git.onedayrex.nettyaction.chapter2.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class EchoServer {

    private final int port;

    public EchoServer(int port) {
        this.port = port;
    }

    public void start() {
        //创建多线程处理连接池，用户多线程处理接收到的连接信息
        EventLoopGroup eventExecutors = new NioEventLoopGroup();
        try {
            //服务器启动引导
            ServerBootstrap bootstrap = new ServerBootstrap();
            //设置线程池，设置channel为NIO，绑定监听端口，设置事件处理handle
            bootstrap.group(eventExecutors).channel(NioServerSocketChannel.class).localAddress(port)
                    .childHandler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel channel) throws Exception {
                            channel.pipeline().addLast(new EchoServerHandle());
                        }
                    });
            //设置启动器异步监听端口
            ChannelFuture channelFuture = bootstrap.bind().sync();
            System.out.println("-->start and listen on" + channelFuture.channel().localAddress());
            //设置channel异步处理，阻塞直到channel完成
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {

        }finally {
            try {
                //释放所有线程池中的资源
                eventExecutors.shutdownGracefully().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        EchoServer echoServer = new EchoServer(65535);
        echoServer.start();
    }
}
