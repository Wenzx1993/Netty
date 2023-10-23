package com.example.netty.server;

import com.example.netty.codec.MessageCodec;
import com.example.netty.server.handler.NettyServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.util.CharsetUtil;

import java.nio.charset.StandardCharsets;

public class NettyServer {

    public static void main(String[] args) throws InterruptedException {
        //设置主reactor
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        //设置工作reactor
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        //创建服务器启动助手
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        //设置配置参数
        serverBootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, Boolean.TRUE)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        //设置解码器行拆包器
//                        socketChannel.pipeline().addLast(new LineBasedFrameDecoder(2048));
                        //设置分隔符自定义分隔符
                        socketChannel.pipeline().addLast(new DelimiterBasedFrameDecoder(2048, Unpooled.copiedBuffer("$".getBytes(StandardCharsets.UTF_8))));
                        //设置编解码器
//                        socketChannel.pipeline().addLast(new MessageCodec());
                        //设置消息处理器
                        socketChannel.pipeline().addLast(new NettyServerHandler());
                    }
                });
        //绑定端口
        ChannelFuture future = serverBootstrap.bind(8888).sync();
        //判断是否绑定成功
        future.addListener((ChannelFutureListener) channelFuture -> {
            if (channelFuture.isSuccess()) {
                System.out.println("绑定端口成功");
            }else {
                System.out.println("绑定端口失败");
            }
        });
        //关闭通道
        future.channel().closeFuture().sync();
        //关闭服务器
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }
}
