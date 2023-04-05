package com.coding.netty.example01.netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;

public class NettyByteBuf02 {
    public static void main(String[] args) {
        // 创建 ByteBuf
        ByteBuf byteBuf = Unpooled.copiedBuffer("hello，问秋！", CharsetUtil.UTF_8);
        System.out.println("byteBuf=" + byteBuf);
        System.out.println("byteBuf.arrayOffset=" + byteBuf.arrayOffset());
        System.out.println("byteBuf.readerIndex=" + byteBuf.readerIndex());
        System.out.println("byteBuf.writerIndex=" + byteBuf.writerIndex());
        System.out.println("byteBuf.capacity=" + byteBuf.capacity());

        int readableBytes = byteBuf.readableBytes();
        System.out.println("byteBuf.readableBytes=" + readableBytes); // 17
        System.out.println("byteBuf.readByte=" + byteBuf.readByte()); // 读取之后，会导致 readableBytes 少1个字节
        System.out.println("byteBuf.readableBytes=" + byteBuf.readableBytes()); // 16

        // 使用相关的方法
        if (byteBuf.hasArray()) {
            byte[] content = byteBuf.array();
            // 将 content 转成字符串
            System.out.println(new String(content, 0, readableBytes, CharsetUtil.UTF_8));

            for (int i = 0; i < readableBytes; i++) {
                System.out.println((char)byteBuf.getByte(i));
            }

            System.out.println(byteBuf.getCharSequence(0, 5, CharsetUtil.UTF_8)); // hello
            System.out.println(byteBuf.getCharSequence(2, 3, CharsetUtil.UTF_8)); // llo
        }
    }
}
