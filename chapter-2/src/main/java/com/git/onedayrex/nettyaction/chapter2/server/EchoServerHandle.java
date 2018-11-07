package com.git.onedayrex.nettyaction.chapter2.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * 通过继承 ChannelInboundHandleAdapter 重写
 */
//标识一个Handle 可以被多个channel共享
@ChannelHandler.Sharable
public class EchoServerHandle extends ChannelInboundHandlerAdapter {


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String s = ((ByteBuf) msg).toString(CharsetUtil.UTF_8);
        System.out.println("Server receive " + s);
        //向客户端发送数据，但并不冲刷出站消息
        ctx.write("server push " + s);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //把末尾消息冲刷到远程节点，并关闭连接
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
