package com.git.onedayrex.nettyaction.nettyguide81.handler;

import com.git.onedayrex.nettyaction.nettyguide81.dto.OrderReq;
import com.git.onedayrex.nettyaction.nettyguide81.dto.OrderResp;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Date;

public class OrderServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        OrderReq orderReq = (OrderReq) msg;
        if (orderReq != null) {
            System.out.println("receive client order req==>" + orderReq);
            OrderResp orderResp = new OrderResp();
            orderResp.setOrderNo(orderReq.getOrderNo());
            orderResp.setOrderTime(new Date());
            ctx.writeAndFlush(orderResp);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
