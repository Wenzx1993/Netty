package com.example.rmi.server;

import com.example.rmi.service.UserService;
import com.example.rmi.service.UserServiceImpl;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIServer {

    public static void main(String[] args) throws RemoteException {
        //创建注册中心
        Registry registry = LocateRegistry.createRegistry(9999);
        UserService userService = new UserServiceImpl();
        //绑定服务
        registry.rebind("userService", userService);
    }
}
