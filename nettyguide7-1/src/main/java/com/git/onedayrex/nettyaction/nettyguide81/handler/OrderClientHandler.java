package com.git.onedayrex.nettyaction.nettyguide81.handler;

import com.git.onedayrex.nettyaction.nettyguide81.dto.OrderReq;
import com.git.onedayrex.nettyaction.nettyguide81.dto.OrderResp;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class OrderClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        for (int i = 0; i < 9999; i++) {
            OrderReq orderReq = new OrderReq();
            orderReq.setOrderNo("046688977" + i);
            orderReq.setGoodName("apple");
            ctx.writeAndFlush(orderReq);
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        OrderResp orderResp = (OrderResp) msg;
        System.out.println("receive server order info==>" + orderResp);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
