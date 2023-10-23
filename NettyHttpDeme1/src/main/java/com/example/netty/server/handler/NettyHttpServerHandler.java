package com.example.netty.server.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

public class NettyHttpServerHandler extends SimpleChannelInboundHandler<Object> {

    /**
     * 处理http请求
     * 1.判断请求内容对象类型
     * 2.处理业务逻辑
     * 3.初始化返回对象
     * 4.设置响应内容
     * 5.设置响应头（传输类型，传输长度）
     * 6.输出响应
     *
     * @param channelHandlerContext
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object msg) throws Exception {
        if (msg instanceof HttpRequest) {
            Channel channel = channelHandlerContext.channel();
            DefaultHttpRequest httpRequest = (DefaultHttpRequest) msg;
            System.out.println("接收到请求,请求地址：" + httpRequest.uri());
            System.out.println("请求来源：" + channel.remoteAddress().toString());
            //设置返回
            String content = "你好啊，客户端";
            ByteBuf byteBuf = Unpooled.copiedBuffer(content, CharsetUtil.UTF_8);
            DefaultFullHttpResponse httpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,HttpResponseStatus.OK, byteBuf);
            //设置响应头
            httpResponse.headers().set(HttpHeaderNames.CONTENT_TYPE,"application/json");
            httpResponse.headers().set(HttpHeaderNames.CONTENT_LENGTH,byteBuf.array().length);
            channelHandlerContext.writeAndFlush(httpResponse);
        }
    }
}
