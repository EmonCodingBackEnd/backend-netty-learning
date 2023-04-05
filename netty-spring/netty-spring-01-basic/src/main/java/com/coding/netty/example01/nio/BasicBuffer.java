package com.coding.netty.example01.nio;

import java.nio.IntBuffer;

public class BasicBuffer {
    public static void main(String[] args) {
        // 创建一个Buffer，大小位5，即可以存放 5 个int
        IntBuffer intBuffer = IntBuffer.allocate(5);
        // 向buffer存放数据
        for (int i = 0; i < intBuffer.capacity(); i++) {
            intBuffer.put(i * 2);
        }
        // 如何从buffer读取数据
        // 将buffer转换，读写切换
        /*
        flip操作：
        limit = position
        position = 0
        mark = -1
        capacity = 5
         */
        intBuffer.flip();
        // 设置从索引1开始读取
        intBuffer.position(1);
        // 设置当前极限是3个元素
        intBuffer.limit(3);
        while (intBuffer.hasRemaining()) {
            System.out.println(intBuffer.get());
        }

        // 重绕此缓冲区：方法将position设置为0，但是不会动buffer里的数据，这样可以从头开始重新读取数据，limit的值不会变，这意味着limit依旧标志着能读多少数据。
        intBuffer.rewind();
        System.out.println(intBuffer.get()); // 返回索引0 的数据，值为 0
    }
}
