package com.example.rmi.client;

import com.example.rmi.service.UserService;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIClient {

    public static void main(String[] args) throws RemoteException, NotBoundException {
        //获取注册中心
        Registry registry = LocateRegistry.getRegistry("localhost", 9999);
        //查找服务
        UserService userService = (UserService) registry.lookup("userService");
        //调用方法
        String name = userService.getName();
        System.out.println(name);
    }
}
