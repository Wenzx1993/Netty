package com.example.netty.encoder;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.util.CharsetUtil;

import java.util.List;

public class MessageEncoder extends MessageToMessageEncoder<String> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, String msg, List<Object> list) throws Exception {
        System.out.println("消息开始编码");
        list.add(Unpooled.copiedBuffer(msg, CharsetUtil.UTF_8));
    }
}
