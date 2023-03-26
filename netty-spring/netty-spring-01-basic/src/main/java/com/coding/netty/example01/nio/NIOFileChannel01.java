package com.coding.netty.example01.nio;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

public class NIOFileChannel01 {
    public static void main(String[] args) throws IOException {
        String path = System.getProperty("user.dir") + "/netty/spring/example01/file01.txt";
        File file = new File(path);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
        }

        // 创建一个缓冲区 ByteBuffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        byteBuffer.put("hello,emon!\n".getBytes(StandardCharsets.UTF_8));
        byteBuffer.put("hello,world!".getBytes(StandardCharsets.UTF_8));
        // 对 byteBuffer 执行flip
        byteBuffer.flip();

        // 创建一个输出流->channel
        FileOutputStream fileOutputStream = new FileOutputStream(path);
        // 通过 fileOutputStream 获取对应的 FileChannel，真实类型是 FileChannelImpl
        FileChannel fileChannel = fileOutputStream.getChannel();

        // 将 byteBuffer 数据写入到 fileChannel
        fileChannel.write(byteBuffer);

        fileOutputStream.close();
    }
}
