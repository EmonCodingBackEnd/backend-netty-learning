package com.coding.netty.example01.nio;

import java.nio.ByteBuffer;

public class NIOByteBufferPutGet {
    public static void main(String[] args) {
        // 创建一个Buffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(64);

        // 类型化方式放入数据
        byteBuffer.putInt(100);
        byteBuffer.putLong(9);
        byteBuffer.putChar('问');
        byteBuffer.putShort((short)4);

        // 取出
        byteBuffer.flip();
        System.out.println(byteBuffer.getInt());
        System.out.println(byteBuffer.getLong());
        System.out.println(byteBuffer.getChar()); // 如果该位置使用 getLong() 会报错：BufferUnderflowException
        System.out.println(byteBuffer.getShort());
    }
}
