package com.example.rmi.service;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface UserService extends Remote {

    String getName() throws RemoteException;
}
