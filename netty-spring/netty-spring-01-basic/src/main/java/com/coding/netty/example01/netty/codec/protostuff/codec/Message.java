package com.coding.netty.example01.netty.codec.protostuff.codec;

import java.io.Serializable;

import lombok.Data;

@Data
public class Message implements Serializable {

    private static final long serialVersionUID = -2524217347345862771L;

    private Long msgId;

    private Integer type; // 1 请求 2 响应 3 心跳 4 路由

    private String routerKey; // 路由Key

    private Object data;

}
