package com.example.rpcdemo1.response;

import lombok.Data;

/**
 * 响应实体
 */
@Data
public class RpcResponse {

    private Object result;

    private Integer status;

}
