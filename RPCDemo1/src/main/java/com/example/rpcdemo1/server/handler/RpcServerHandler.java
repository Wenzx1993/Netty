package com.example.rpcdemo1.server.handler;

import com.alibaba.fastjson2.JSON;
import com.example.rpcdemo1.request.RpcRequest;
import com.example.rpcdemo1.response.RpcResponse;
import com.example.rpcdemo1.server.annotation.RpcService;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.springframework.beans.BeansException;
import org.springframework.cglib.reflect.FastClass;
import org.springframework.cglib.reflect.FastMethod;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Component
@ChannelHandler.Sharable
public class RpcServerHandler extends SimpleChannelInboundHandler<String> implements ApplicationContextAware {

    private Map<String, Object> nameMap = new HashMap<>();

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String o) throws Exception {
        RpcResponse rpcResponse = new RpcResponse();
        try {
            RpcRequest rpcRequest = JSON.parseObject(o, RpcRequest.class);
            String className = rpcRequest.getClassName();
            Object beanService = nameMap.get(className);
            FastClass fastClass = FastClass.create(beanService.getClass());
            FastMethod method = fastClass.getMethod(rpcRequest.getMethodName(), rpcRequest.getParameterTypes());
            Object result = method.invoke(beanService, rpcRequest.getParameters());
            rpcResponse.setResult(result);
            rpcResponse.setStatus(200);
        } catch (Exception exception) {
            rpcResponse.setStatus(500);
        }
        channelHandlerContext.writeAndFlush(JSON.toJSONString(rpcResponse));
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        String[] beanNamesForAnnotation = applicationContext.getBeanNamesForAnnotation(RpcService.class);
        if (beanNamesForAnnotation.length < 1) {
            return;
        }
        Arrays.asList(beanNamesForAnnotation).parallelStream().forEach(item -> {
            Object bean = applicationContext.getBean(item);
            nameMap.put(bean.getClass().getInterfaces()[0].getSimpleName(), applicationContext.getBean(item));
        });
    }
}
