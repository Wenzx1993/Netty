package com.example.netty.server;

import com.example.netty.codec.MessageCodec;
import com.example.netty.server.handler.NettyChatServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyChatServer {

    public static void main(String[] args) throws InterruptedException {
        //Reactor
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        //workerGroup
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        //初始化服务器启动类
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        //设置启动配置
        serverBootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel channel) throws Exception {
                        //设置编解码器
                        channel.pipeline().addLast(new MessageCodec());
                        //设置服务器端处理类
                        channel.pipeline().addLast(new NettyChatServerHandler());
                    }
                });
        //绑定端口
        ChannelFuture channelFuture = serverBootstrap.bind(8888).sync();
        channelFuture.addListener((ChannelFutureListener) channelFuture1 -> {
            if (channelFuture1.isSuccess()) {
                System.out.println("端口绑定成功！");
            } else {
                System.out.println("端口绑定失败！");
            }
        });
        //关闭通道
        channelFuture.channel().closeFuture().sync();
        //关闭服务器
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }
}
