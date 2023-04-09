package com.coding.netty.example01.netty.tcp.protocoltcp;

import lombok.Data;

/**
 * 协议包
 */
@Data
public class MessageProtocol {
    private int len;
    private byte[] content;
}
