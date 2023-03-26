package com.coding.netty.example01.nio;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

public class NIOFileChannel02 {
    public static void main(String[] args) throws IOException {
        String path = System.getProperty("user.dir") + "/netty/spring/example01/file01.txt";
        File file = new File(path);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
        }

        // 创建一个输入流->channel
        FileInputStream fileInputStream = new FileInputStream(path);
        // 通过 fileInputStream 获取对应的 FileChannel，真实类型是 FileChannelImpl
        FileChannel fileChannel = fileInputStream.getChannel();

        // 创建一个缓冲区 ByteBuffer
        ByteBuffer byteBuffer = ByteBuffer.allocate((int)file.length());

        // 将通道的数据读入到 byteBuffer
        fileChannel.read(byteBuffer);
        // 将 byteBuffer 的字节数据转换成 String
        System.out.println(new String(byteBuffer.array(), StandardCharsets.UTF_8));

        fileInputStream.close();
    }
}
