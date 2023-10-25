package com.example.rpcdemo1.client.proxy;

import com.example.rpcdemo1.client.RpcClient;
import com.example.rpcdemo1.request.RpcRequest;
import com.example.rpcdemo1.response.RpcResponse;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;

/**
 * 代理类
 */
public class RpcClientProxy {

    public static Object createProxy(Class<?> clazz) {
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{clazz}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                //创建客户端
                RpcClient rpcClient = new RpcClient("localhost", 8899);
                //创建请求实体
                RpcRequest rpcRequest = new RpcRequest();
                rpcRequest.setRequestId(UUID.randomUUID().toString());
                rpcRequest.setClassName(clazz.getSimpleName());
                rpcRequest.setMethodName(method.getName());
                rpcRequest.setParameters(args);
                rpcRequest.setParameterTypes(method.getParameterTypes());
                //设置请求实体
                RpcResponse rpcResponse = rpcClient.send(rpcRequest);
                return rpcResponse.getResult();
            }
        });
    }
}
