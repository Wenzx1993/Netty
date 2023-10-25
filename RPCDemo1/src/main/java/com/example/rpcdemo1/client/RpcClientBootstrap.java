package com.example.rpcdemo1.client;

import com.example.rpcdemo1.client.proxy.RpcClientProxy;
import com.example.rpcdemo1.server.service.UserService;

public class RpcClientBootstrap {

    public static void main(String[] args) {
        UserService userService = (UserService) RpcClientProxy.createProxy(UserService.class);
        String name = userService.getName();
        System.out.println(name);
    }
}
