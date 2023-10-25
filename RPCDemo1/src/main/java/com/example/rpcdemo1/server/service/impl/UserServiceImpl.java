package com.example.rpcdemo1.server.service.impl;

import com.example.rpcdemo1.server.annotation.RpcService;
import com.example.rpcdemo1.server.service.UserService;
import org.springframework.stereotype.Service;

@RpcService
@Service
public class UserServiceImpl implements UserService {

    public String getName() {
        return "我是张三";
    }
}
