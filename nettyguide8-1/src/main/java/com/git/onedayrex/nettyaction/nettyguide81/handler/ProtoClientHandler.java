package com.git.onedayrex.nettyaction.nettyguide81.handler;

import com.git.onedayrex.nettyaction.nettyguide81.dto.OrderInfoReqOuterClass;
import com.git.onedayrex.nettyaction.nettyguide81.dto.OrderInfoRespOuterClass;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ProtoClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        OrderInfoReqOuterClass.OrderInfoReq.Builder builder = OrderInfoReqOuterClass.OrderInfoReq.newBuilder();
        for (int i = 0; i < 999; i++) {
            builder.setReqId(i);
            builder.setOrderNo("20181117" + i);
            OrderInfoReqOuterClass.OrderInfoReq orderInfoReq = builder.build();
            ctx.write(orderInfoReq);
        }
        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        OrderInfoRespOuterClass.OrderInfoResp orderInfoResp = (OrderInfoRespOuterClass.OrderInfoResp) msg;
        System.out.println("receive server order:" + orderInfoResp);
    }
}
