package com.example.rpcdemo1.client.handler;

import com.alibaba.fastjson2.JSON;
import com.example.rpcdemo1.request.RpcRequest;
import com.example.rpcdemo1.response.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.concurrent.Callable;

/**
 * 客户端处理类
 */
public class RpcClientHandler extends SimpleChannelInboundHandler<String> implements Callable {

    private ChannelHandlerContext ctx;

    private RpcResponse rpcResponse;

    private RpcRequest request;

    public void setRequest(RpcRequest request) {
        this.request = request;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        this.ctx = ctx;
    }

    @Override
    protected synchronized void channelRead0(ChannelHandlerContext channelHandlerContext, String o) throws Exception {
        rpcResponse = JSON.parseObject(o, RpcResponse.class);
        notify();
    }

    @Override
    public synchronized RpcResponse call() throws Exception {
        ctx.writeAndFlush(JSON.toJSONString(request));
        wait();
        return rpcResponse;
    }
}
