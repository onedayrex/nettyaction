package com.git.onedayrex.nettyaction.nettyguide81.handler;

import com.git.onedayrex.nettyaction.nettyguide81.dto.OrderInfoReqOuterClass;
import com.git.onedayrex.nettyaction.nettyguide81.dto.OrderInfoRespOuterClass;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ProtoServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        OrderInfoReqOuterClass.OrderInfoReq orderInfoReq = (OrderInfoReqOuterClass.OrderInfoReq) msg;
        System.out.println("receive order resp" + orderInfoReq);
        OrderInfoRespOuterClass.OrderInfoResp.Builder builder = OrderInfoRespOuterClass.OrderInfoResp.newBuilder();
        builder.setReqId(orderInfoReq.getReqId());
        builder.setOrderNo(orderInfoReq.getOrderNo());
        builder.setGoodName("apple");
        OrderInfoRespOuterClass.OrderInfoResp orderInfoResp = builder.build();
        ctx.writeAndFlush(orderInfoResp);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
