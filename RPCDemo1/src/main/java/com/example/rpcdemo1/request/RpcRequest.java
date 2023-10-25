package com.example.rpcdemo1.request;

import lombok.Data;

/**
 * 请求实体
 */
@Data
public class RpcRequest {

    private String className;

    private String methodName;

    private String requestId;

    private Class[] parameterTypes;

    private Object[] parameters;

}
