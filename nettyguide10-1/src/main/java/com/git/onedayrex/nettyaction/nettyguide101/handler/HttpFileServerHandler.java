package com.git.onedayrex.nettyaction.nettyguide101.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.stream.ChunkedNioFile;
import io.netty.util.CharsetUtil;
import io.netty.util.internal.StringUtil;

import java.io.File;
import java.io.RandomAccessFile;

public class HttpFileServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    private String url;

    public HttpFileServerHandler(String url) {
        this.url = url;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
        String uri = msg.uri();
        if (StringUtil.isNullOrEmpty(uri)) {
            sendErr(ctx, "path is null", msg);
            return;
        }
        System.out.println("URI==>" + uri);
        String path = url + uri.replace("/", File.separator);
        File file = new File(path);
        if (file.isHidden() || !file.exists()) {
            sendErr(ctx, "file not exist", msg);
            return;
        }
        if (file.isDirectory()) {
            sendList(ctx,file);
            return;
        }else {
            RandomAccessFile randomAccessFile = new RandomAccessFile(file,"r");
            DefaultHttpResponse defaultHttpResponse = new DefaultHttpResponse(HttpVersion.HTTP_1_1,HttpResponseStatus.OK);
            defaultHttpResponse.headers().add(HttpHeaderNames.CONTENT_LENGTH, randomAccessFile.length());
            defaultHttpResponse.headers().add(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
            ctx.write(defaultHttpResponse);
            ctx.write(new ChunkedNioFile(randomAccessFile.getChannel()));
            ChannelFuture channelFuture = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
            boolean keepAlive = HttpUtil.isKeepAlive(msg);
            if (!keepAlive) {
                channelFuture.addListener(ChannelFutureListener.CLOSE);
            }
            randomAccessFile.close();
        }
        sendErr(ctx, "error", msg);
//        if (!msg.decoderResult().isSuccess()) {
//
//        }
    }

    private void sendErr(ChannelHandlerContext ctx,String reason,FullHttpRequest msg) {
        DefaultFullHttpResponse defaultFullHttpResponse = new DefaultFullHttpResponse(msg.protocolVersion(), HttpResponseStatus.BAD_REQUEST);
        defaultFullHttpResponse.headers().add(HttpHeaderNames.CONTENT_TYPE, "text/html; charset=UTF-8");
        StringBuilder sb = new StringBuilder();
        sb.append("<div>");
        sb.append(reason);
        sb.append("</div>");
        ByteBuf byteBuf = Unpooled.copiedBuffer(sb, CharsetUtil.UTF_8);
        defaultFullHttpResponse.content().writeBytes(byteBuf);
        ctx.write(defaultFullHttpResponse);
        ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT).addListener(ChannelFutureListener.CLOSE);
    }

    private void sendList(ChannelHandlerContext ctx,File files) {
        DefaultFullHttpResponse defaultFullHttpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        defaultFullHttpResponse.headers().add(HttpHeaderNames.CONTENT_TYPE, "text/html; charset=UTF-8");
        StringBuilder sb = new StringBuilder();
        sb.append("<div>");
        for (File file : files.listFiles()) {
            sb.append("<div>");
//            String absolutePath = file.getAbsolutePath();
//            sb.append("<a href=");
            sb.append(file.getName());
            sb.append("</div>");
        }
        ByteBuf byteBuf = Unpooled.copiedBuffer(sb,CharsetUtil.UTF_8);
        defaultFullHttpResponse.content().writeBytes(byteBuf);
        ctx.write(defaultFullHttpResponse);
        ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT).addListener(ChannelFutureListener.CLOSE);
    }
}
