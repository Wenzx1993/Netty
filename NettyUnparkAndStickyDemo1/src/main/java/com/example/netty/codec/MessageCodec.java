package com.example.netty.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import io.netty.util.CharsetUtil;

import java.util.List;

/**
 * 编解码器
 */
public class MessageCodec extends MessageToMessageCodec<ByteBuf, String> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, String o, List list) throws Exception {
        list.add(Unpooled.copiedBuffer(o, CharsetUtil.UTF_8));
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf o, List list) throws Exception {
        list.add(o.toString(CharsetUtil.UTF_8));
    }
}
