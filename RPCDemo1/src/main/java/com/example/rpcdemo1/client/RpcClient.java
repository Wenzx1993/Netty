package com.example.rpcdemo1.client;

import com.example.rpcdemo1.client.handler.RpcClientHandler;
import com.example.rpcdemo1.request.RpcRequest;
import com.example.rpcdemo1.response.RpcResponse;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Slf4j
public class RpcClient {

    private String host;

    private Integer port;

    private NioEventLoopGroup clientGroup;

    private Channel channel;

    private RpcClientHandler clientHandler = new RpcClientHandler();

    private ExecutorService executorService = Executors.newCachedThreadPool();

    public RpcClient(String host, Integer port) {
        try {
            this.host = host;
            this.port = port;
            initClient();
        } catch (Exception e) {
            log.error("客户端初始化失败！");
        }

    }

    private void initClient() throws InterruptedException {
        clientGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(clientGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) {
                        //添加编解码器
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        pipeline.addLast(new StringEncoder());
                        pipeline.addLast(new StringDecoder());
                        pipeline.addLast(clientHandler);
                    }
                });
        ChannelFuture channelFuture = bootstrap.connect(new InetSocketAddress(host, port)).sync();
        channelFuture.addListener((ChannelFutureListener) channelFuture1 -> {
            if (channelFuture1.isSuccess()) {
                log.info("客户端初始化成功");
            } else {
                log.error("客户端初始化失败");
            }
        });
        channel = channelFuture.channel();
    }

    private void close() {
        if (Objects.nonNull(channel)) {
            channel.close();
        }
        if (Objects.nonNull(clientGroup)) {
            clientGroup.shutdownGracefully();
        }
    }

    public RpcResponse send(RpcRequest rpcRequest) throws ExecutionException, InterruptedException {
        clientHandler.setRequest(rpcRequest);
        Future future = executorService.submit(clientHandler);
        return (RpcResponse) future.get();
    }
}
