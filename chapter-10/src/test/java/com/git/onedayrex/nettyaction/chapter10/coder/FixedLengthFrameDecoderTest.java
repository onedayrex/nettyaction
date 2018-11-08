package com.git.onedayrex.nettyaction.chapter10.coder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class FixedLengthFrameDecoderTest {

    @Test
    public void decode() {
        //使用EmbeddedChannel来模拟测试
        ByteBuf buffer = Unpooled.buffer();
        for (int i = 0; i < 9; i++) {
            buffer.writeByte(i);
        }
        //只复制出有数据的字节buf，当前是9位
        /**
         * 坑，原书使用 buffer.duplicate() 复制buffer，
         *  if (buffer instanceof DuplicatedByteBuf) {
         *      this.buffer = ((DuplicatedByteBuf)buffer).buffer;
         *  } else if (buffer instanceof AbstractPooledDerivedByteBuf) {
         *      this.buffer = buffer.unwrap();
         *  } else {
         *      this.buffer = buffer;
         *  }
         * 此处如果不是duplicatedByteBug与pooledDeriedByteBuf 直接会得到对象引用
         * 这样相当于对象的引用，当把 duplicate写入站后，refcnt从1变成0
         * 则表示使用过，netty看成是资源已经释放，在读取数据作对比时则无法
         * 使用readBytes，这里使用copy代替，直接拷贝出对象来
         */
        ByteBuf duplicate = buffer.copy();
        EmbeddedChannel embeddedChannel = new EmbeddedChannel(new FixedLengthFrameDecoder(4));
        //把字节模拟入站写入  写入数据
        embeddedChannel.writeInbound(duplicate);
        Assert.assertTrue(embeddedChannel.finish());
        //读取数据
        Assert.assertEquals(buffer.readBytes(4), embeddedChannel.readInbound());
        Assert.assertEquals(buffer.readBytes(4), embeddedChannel.readInbound());
        Assert.assertEquals(null, embeddedChannel.readInbound());
    }
}