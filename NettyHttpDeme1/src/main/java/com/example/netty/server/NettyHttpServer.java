package com.example.netty.server;

import com.example.netty.server.handler.NettyHttpServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

public class NettyHttpServer {

    private int port;

    public NettyHttpServer(int port) {
        this.port = port;
    }

    /**
     * 最主要的，设置http编解码器
     */
    public void run() {
        //初始化bossgroup
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        //初始化workGroup
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            //创建服务器启动助手
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            //配置启动参数
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, Boolean.TRUE)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            //添加编解码器
                            socketChannel.pipeline().addLast(new HttpServerCodec());
                            //添加自定义业务处理类
                            socketChannel.pipeline().addLast(new NettyHttpServerHandler());
                        }
                    });
            //绑定端口
            ChannelFuture channelFuture = serverBootstrap.bind(port);
            //绑定回调
            channelFuture.addListener((ChannelFutureListener) channelFuture1 -> {
                if (channelFuture1.isSuccess()) {
                    System.out.println("端口绑定成功");
                } else {
                    System.out.println("端口绑定失败");
                }
            });
            //关闭通道
            channelFuture.channel().closeFuture().sync();
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        } finally {
            //关闭线程组
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        new NettyHttpServer(8888).run();
    }
}
