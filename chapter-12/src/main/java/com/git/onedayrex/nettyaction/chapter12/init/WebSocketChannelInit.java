package com.git.onedayrex.nettyaction.chapter12.init;

import com.git.onedayrex.nettyaction.chapter12.handle.HttpRequestHandler;
import com.git.onedayrex.nettyaction.chapter12.handle.TextWebSocketHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

public class WebSocketChannelInit extends ChannelInitializer<Channel> {
    private final ChannelGroup channels;

    public WebSocketChannelInit(ChannelGroup channels) {
        this.channels = channels;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        //编码处理
        pipeline.addLast(new HttpServerCodec());
        //写文件
        pipeline.addLast(new ChunkedWriteHandler());
        //聚合解码
        pipeline.addLast(new HttpObjectAggregator(64 * 1024));
        //处理webSocket
        pipeline.addLast(new HttpRequestHandler("/ws"));
        //处理frame
        pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));
        //处理group
        pipeline.addLast(new TextWebSocketHandler(channels));
    }
}
