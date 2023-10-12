package com.example.netty.server;

import com.example.netty.server.handler.ServerChannelHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * netty服务器端
 */
public class NettyServer {

    /**
     * 1.创建bossGroup线程组
     * 2.创建workerGroup线程组
     * 3.创建服务器辅助启动类
     * 4.辅助启动类配置线程组
     * 5.设置服务端通道实现
     * 6.设置服务端队列等待个数
     * 7.设置活跃状态
     * 8.设置初始化通道对象
     * 9.设置自定义通道事件处理对象
     * 10.启动服务端并设置为同步
     * 11.关闭通道和关闭bossGroup线程组、workerGroup线程组
     *
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        //创建bossGroup线程组
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        //创建workerGroup线程组
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        //创建服务端启动助手
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        //设置线程组
        serverBootstrap.group(bossGroup, workerGroup)
                //设置服务端通道类型
                .channel(NioServerSocketChannel.class)
                //设置线程队列等待连接个数
                .option(ChannelOption.SO_BACKLOG, 128)
                //设置活跃状态
                .childOption(ChannelOption.SO_KEEPALIVE, Boolean.TRUE)
                //设置worker线程的通道初始化对象
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel channel) throws Exception {
                        //设置自定义通道事件处理器
                        channel.pipeline().addLast(new ServerChannelHandler());
                    }
                });
        //启动服务器并绑定端口
        ChannelFuture channelFuture = serverBootstrap.bind(8888).sync();
        System.out.println("服务器已启动");
        //关闭通道
        channelFuture.channel().closeFuture().sync();
        //关闭bossGroup线程组和workerGroup线程组
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }
}
