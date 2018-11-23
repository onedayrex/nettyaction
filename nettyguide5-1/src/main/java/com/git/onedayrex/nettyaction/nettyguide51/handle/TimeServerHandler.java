package com.git.onedayrex.nettyaction.nettyguide51.handle;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeServerHandler extends SimpleChannelInboundHandler<Object> {
    private int counter;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        String body = (String) msg;
        System.out.println("Time receive order " + body + "; the counter is " + ++counter);
        if ("query time".equalsIgnoreCase(body)) {
            Date date = new Date();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //注意，此处需要加上\n,否则客户端的LineBaseFrameHandle不会把消息向下传递
            String format = simpleDateFormat.format(date)+"$_";
            ByteBuf buffer = Unpooled.copiedBuffer(format.getBytes());
            ctx.writeAndFlush(buffer);
        }
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
