package com.example.rmi.service;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class UserServiceImpl extends UnicastRemoteObject implements UserService {

    public UserServiceImpl() throws RemoteException {

    }

    @Override
    public String getName() throws RemoteException {
        return "靓仔啊，你终于找到我了！";
    }

}
