package com.example.netty.client.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 客户端处理类
 */
public class NettyCharClientHandler extends SimpleChannelInboundHandler<String> {

    //只读取消息
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String o) throws Exception {
        Channel channel = channelHandlerContext.channel();
        System.out.println("【Client】:" + channel.remoteAddress().toString() + " 说：" + o);
    }
}
