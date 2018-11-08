package com.git.onedayrex.nettyaction.chapter10.coder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * 字节入站处理handler，循环判断字节是否大于给定字节数，如果是才放入到
 * 下一个handler中去处理
 */
public class FixedLengthFrameDecoder extends ByteToMessageDecoder {

    /**
     * 给定字节数
     */
    private final int fixedLength;

    public FixedLengthFrameDecoder(int fixedLength) {
        if (fixedLength <= 0) {
            throw new IllegalArgumentException("fixedLength must gretter 0");
        }
        this.fixedLength = fixedLength;
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        while (byteBuf.readableBytes() > fixedLength) {
            //从buffer中取出定长字节
            ByteBuf fixedLengthByte = byteBuf.readBytes(fixedLength);
            //放入到list中给到下一个handler处理
            list.add(fixedLengthByte);
        }
    }
}
