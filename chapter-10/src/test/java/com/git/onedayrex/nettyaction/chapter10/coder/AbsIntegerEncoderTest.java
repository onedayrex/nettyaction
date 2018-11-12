package com.git.onedayrex.nettyaction.chapter10.coder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class AbsIntegerEncoderTest {

    @Test
    public void encode() {
        ByteBuf buffer = Unpooled.buffer();
        for (int i = 0; i < 9; i++) {
            buffer.writeByte(-i);
        }
        EmbeddedChannel embeddedChannel = new EmbeddedChannel(new AbsIntegerEncoder());
        //写入出站数据
        Assert.assertTrue(embeddedChannel.writeOutbound(buffer));
        Assert.assertTrue(embeddedChannel.finish());
        for (int i = 0; i < 9; i++) {
            Integer integer = embeddedChannel.readOutbound();
            System.out.println(integer);
            Assert.assertEquals(i, integer.intValue());
        }
        Assert.assertEquals(null,embeddedChannel.readOutbound());
    }
}