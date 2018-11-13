package com.git.onedayrex.nettyaction.chapter12.handle;

import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedNioFile;

import java.io.RandomAccessFile;

public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    /**
     * webSocket 标识路径
     */
    private final String wsUri;

    public HttpRequestHandler(String wsUri) {
        this.wsUri = wsUri;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
        if (wsUri.equalsIgnoreCase(msg.uri())) {
            //websocket连接，转到下一个handler处理
            /**
             * msg需要做一个retain 由于firechannel会释放一次，导致refcnt为1，
             * 后续处理中simpleChannelInbound 也会处理一次，这样refcnt为0则会
             * 无法使用msg资源
             */
            ctx.fireChannelRead(msg.retain());
        }else {
            // http 请求
            String path = this.getClass().getResource("/").getPath();
            RandomAccessFile randomAccessFile = new RandomAccessFile(path + "/index.html", "r");
            HttpResponse httpResponse = new DefaultHttpResponse(msg.protocolVersion(), HttpResponseStatus.OK);
            httpResponse.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html; charset=UTF-8");
            boolean keepAlive = HttpUtil.isKeepAlive(msg);
            if (keepAlive) {
                httpResponse.headers().set(HttpHeaderNames.CONTENT_LENGTH, randomAccessFile.length());
                httpResponse.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
            }
            ctx.write(httpResponse);
            //不是https，直接写入通道
            if (ctx.pipeline().get(SslHandler.class) == null) {
                ctx.write(new DefaultFileRegion(randomAccessFile.getChannel(), 0, randomAccessFile.length()));
            }else {
                ctx.write(new ChunkedNioFile(randomAccessFile.getChannel()));
            }
            ChannelFuture channelFuture = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
            //如果不常用，关闭连接
            if (!keepAlive) {
                channelFuture.addListener(ChannelFutureListener.CLOSE);
            }
            randomAccessFile.close();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
