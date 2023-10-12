package com.example.netty.client;

import com.example.netty.client.handler.ClientChannelHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyClient {

    /**
     * 1. 创建线程组
     * 2. 创建客户端启动助手
     * 3. 设置线程组
     * 4. 设置客户端通道实现为NIO
     * 5. 创建一个通道初始化对象
     * 6. 向pipeline中添加自定义业务处理handler
     * 7. 启动客户端,等待连接服务端,同时将异步改为同步
     * 8. 关闭通道和关闭连接池
     *
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        //设置客户端线程组
        NioEventLoopGroup clientGroup = new NioEventLoopGroup();
        //设置服务启动类
        Bootstrap bootstrap = new Bootstrap();
        //设置线程组
        bootstrap.group(clientGroup)
                //设置通道类型
                .channel(NioSocketChannel.class)
                //设置通道初始化对象
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        //设置自定义通道事件处理器
                        socketChannel.pipeline().addLast(new ClientChannelHandler());
                    }
                });
        //启动客户端
        ChannelFuture channelFuture = bootstrap.connect("localhost", 8888).sync();
        System.out.println("客户端已启动");
        //关闭客户端线程组
        clientGroup.shutdownGracefully();
    }
}
