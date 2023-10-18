package com.example.netty.server.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * 服务器端处理类
 */
public class NettyChatServerHandler implements ChannelInboundHandler {

    private final static List<Channel> channelList = new ArrayList<>();

    @Override
    public void channelRegistered(ChannelHandlerContext channelHandlerContext) throws Exception {

    }

    @Override
    public void channelUnregistered(ChannelHandlerContext channelHandlerContext) throws Exception {

    }

    @Override
    public void channelActive(ChannelHandlerContext channelHandlerContext) throws Exception {
        //添加通道
        Channel channel = channelHandlerContext.channel();
        channelList.add(channel);
        System.out.println("【client】:" + channel.remoteAddress().toString() + " 加入群聊");
    }

    @Override
    public void channelInactive(ChannelHandlerContext channelHandlerContext) throws Exception {
        //移除通道
        Channel channel = channelHandlerContext.channel();
        channelList.remove(channel);
        System.out.println("【client】:" + channel.remoteAddress().toString() + " 退出群聊");
    }

    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        //接收消息并推送
        if (channelList.isEmpty()) {
            return;
        }
        //过滤当前发送消息的通道本身，然后发送消息
        Channel channel = channelHandlerContext.channel();
        channelList.parallelStream().forEach(item -> {
            if (item != channel) {
                item.writeAndFlush(o);
            }
        });
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext channelHandlerContext) throws Exception {

    }

    @Override
    public void userEventTriggered(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {

    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext channelHandlerContext) throws Exception {

    }

    @Override
    public void handlerAdded(ChannelHandlerContext channelHandlerContext) throws Exception {

    }

    @Override
    public void handlerRemoved(ChannelHandlerContext channelHandlerContext) throws Exception {

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable throwable) throws Exception {

    }
}
