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
public class MessageCodec extends MessageToMessageCodec<ByteBuf,String> {

    /**
     * 编码
     * @param channelHandlerContext
     * @param s
     * @param list
     * @throws Exception
     */
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, String s, List<Object> list) throws Exception {
        System.out.println("消息编码");
        list.add(Unpooled.copiedBuffer(s, CharsetUtil.UTF_8));
    }

    /**
     * 解码
     * @param channelHandlerContext
     * @param byteBuf
     * @param list
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        System.out.println("消息解码");
        list.add(byteBuf.toString(CharsetUtil.UTF_8));
    }
}
