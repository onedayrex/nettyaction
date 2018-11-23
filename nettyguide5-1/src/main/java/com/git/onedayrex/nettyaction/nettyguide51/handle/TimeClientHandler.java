package com.git.onedayrex.nettyaction.nettyguide51.handle;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class TimeClientHandler extends SimpleChannelInboundHandler<Object> {
    //使用 $_来做为分隔符
    private static final byte[] req = ("query time$_").getBytes();

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ByteBuf buffer = Unpooled.buffer(req.length);
        for (int i = 0; i < 100; i++) {
            buffer.writeBytes(req);
            buffer.retain();
            ctx.writeAndFlush(buffer);
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        String timeStr = (String) msg;
        System.out.println("receive server msg ==>" + timeStr);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
