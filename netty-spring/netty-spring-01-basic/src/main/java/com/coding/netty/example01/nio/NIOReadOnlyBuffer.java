package com.coding.netty.example01.nio;

import java.nio.ByteBuffer;

public class NIOReadOnlyBuffer {
    public static void main(String[] args) {
        // 创建一个Buffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(64);
        for (int i = 0; i < 64; i++) {
            byteBuffer.put((byte)i);
        }

        // 读取
        byteBuffer.flip();
        // 得到一个只读Buffer
        ByteBuffer readOnlyBuffer = byteBuffer.asReadOnlyBuffer();
        while (readOnlyBuffer.hasRemaining()) {
            System.out.println(readOnlyBuffer.get());
        }

        // ReadOnlyBufferException
        // readOnlyBuffer.put((byte) 1);
    }
}
