package com.example.netty.client;

import com.example.netty.client.handler.NettyClientHandler;
import com.example.netty.codec.MessageCodec;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class NettyClient {

    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup clientGroup = new NioEventLoopGroup();
        //设置客户端启动助手
        Bootstrap bootstrap = new Bootstrap();
        //设置配置参数
        bootstrap.group(clientGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
//                        socketChannel.pipeline().addLast(new LineBasedFrameDecoder(2048));
                        socketChannel.pipeline().addLast(new DelimiterBasedFrameDecoder(2048, Unpooled.copiedBuffer("$".getBytes(StandardCharsets.UTF_8))));
//                        socketChannel.pipeline().addLast(new MessageCodec());
                        socketChannel.pipeline().addLast(new NettyClientHandler());
                    }
                });
        //设置连接服务器
        ChannelFuture future = bootstrap.connect(new InetSocketAddress("localhost", 8888)).sync();
        //设置回调
        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                if (channelFuture.isSuccess()) {
                    System.out.println("服务器连接成功");
                } else {
                    System.out.println("服务器连接失败");
                }
            }
        });
        //获取本地输入
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String nextLine = scanner.nextLine();
            //输出（行分隔符）
//            future.channel().writeAndFlush(nextLine + "\n");
            //输出（自定义分隔符）
            future.channel().writeAndFlush(Unpooled.copiedBuffer(nextLine + "$",CharsetUtil.UTF_8));
        }
        //关闭通道
        //关闭客户端
    }
}
