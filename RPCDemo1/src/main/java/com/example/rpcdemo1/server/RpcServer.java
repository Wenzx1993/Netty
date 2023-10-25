package com.example.rpcdemo1.server;

import com.example.rpcdemo1.server.handler.RpcServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Objects;

@Slf4j
@Component
public class RpcServer implements DisposableBean {

    private NioEventLoopGroup bossGroup;

    private NioEventLoopGroup workerGroup;

    @Autowired
    private RpcServerHandler rpcServerHandler;

    @PostConstruct
    private void initServer() throws InterruptedException {
        bossGroup = new NioEventLoopGroup(1);
        workerGroup = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, Boolean.TRUE)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        //添加编解码器
                        pipeline.addLast(new StringEncoder());
                        pipeline.addLast(new StringDecoder());
                        //添加处理器
                        pipeline.addLast(rpcServerHandler);
                    }
                });
        ChannelFuture channelFuture = bootstrap.bind(8899).sync();
        channelFuture.addListener((ChannelFutureListener) channelFuture1 -> {
            if (channelFuture1.isSuccess()) {
                log.info("rpc服务启动成功");
            } else {
                log.error("rpc服务启动失败");
            }
        });
        channelFuture.channel().closeFuture().sync();
    }

    @Override
    public void destroy() {
        if (Objects.nonNull(bossGroup)) {
            bossGroup.shutdownGracefully();
        }
        if (Objects.nonNull(workerGroup)) {
            workerGroup.shutdownGracefully();
        }
    }

}
