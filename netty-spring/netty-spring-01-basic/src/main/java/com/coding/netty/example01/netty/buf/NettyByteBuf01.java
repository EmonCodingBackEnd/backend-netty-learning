package com.coding.netty.example01.netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class NettyByteBuf01 {
    public static void main(String[] args) {
        // 创建一个 ByteBuf
        /*
        说明：
        1.创建对象，该对象包含一个数组 arr，是一个 byte[10]
        2.在 netty 的 buffer 中，不需要使用 flip 进行反转，因为底层维护了 rederIndex 和 writerIndex，且 0<=readerIndex<=writerIndex<=capacity
        3.通过 readerIndex 、 writerIndex 和 capacity，将 buffer 分成 3 个区域
        0 --- readerIndex 已经读取的区域
        readerIndex --- writerIndex 可读区域
        writerIndex --- capacity 可写的区域
        +--------------------+--------------------+--------------------+
        | discardable bytes  | readable bytes     | writable bytes     |
        |                    |    (CONTENT)       |                    |
        +--------------------+--------------------+--------------------+
        |                    |                    |                    |
        0       <=      readerIndex     <=    writerIndex     <=    capacity
         */
        ByteBuf byteBuf = Unpooled.buffer(10);

        for (int i = 0; i < 10; i++) {
            byteBuf.writeByte(i);
        }

        System.out.println("capacity=" + byteBuf.capacity());
        // 输出
        for (int i = 0; i < byteBuf.capacity(); i++) {
            System.out.println(byteBuf.readerIndex(i));
        }
    }
}
