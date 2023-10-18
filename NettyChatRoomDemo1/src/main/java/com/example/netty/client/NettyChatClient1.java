package com.example.netty.client;

import com.example.netty.client.handler.NettyCharClientHandler;
import com.example.netty.codec.MessageCodec;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Scanner;

public class NettyChatClient1 {

    public static void main(String[] args) throws InterruptedException {
        //设置clientGroup
        NioEventLoopGroup clientGroup = new NioEventLoopGroup();
        //设置客户端启动助手
        Bootstrap bootstrap = new Bootstrap();
        //设置启动配置
        bootstrap.group(clientGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        //设置编解码器
                        socketChannel.pipeline().addLast(new MessageCodec());
                        //设置客户端处理类
                        socketChannel.pipeline().addLast(new NettyCharClientHandler());
                    }
                });
        //连接客户端
        ChannelFuture channelFuture = bootstrap.connect("localhost", 8888).sync();
        channelFuture.addListener((ChannelFutureListener) channelFuture1 -> {
            if (channelFuture1.isSuccess()) {
                System.out.println("服务器连接成功");
            } else {
                System.out.println("服务器连接失败");
            }
        });
        //发送消息
        Scanner scanner = new Scanner(System.in);
        System.out.println("请发送消息：");
        Channel channel = channelFuture.channel();
        while (scanner.hasNextLine()) {
            String nextLine = scanner.nextLine();
            channel.writeAndFlush(nextLine);
        }

        //关闭通道
        ChannelFuture closeFuture = channel.closeFuture().sync();
        closeFuture.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                if(channelFuture.isSuccess()) {
                    System.out.println("通道关闭成功");
                }else {
                    System.out.println("通道关闭失败");
                }
            }
        });
        //关闭客户端
        clientGroup.shutdownGracefully();
    }
}
