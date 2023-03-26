package com.coding.netty.example01.nio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIOFileChannel03 {
    public static void main(String[] args) throws IOException {
        String path = System.getProperty("user.dir") + "/netty/spring/example01/file01.txt";
        File file = new File(path);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
        }

        String path2 = System.getProperty("user.dir") + "/netty/spring/example01/file02.txt";
        File file2 = new File(path);
        if (!file2.exists()) {
            file2.getParentFile().mkdirs();
        }

        // 创建一个输入流->channel
        FileInputStream fileInputStream = new FileInputStream(path);
        // 通过 fileInputStream 获取对应的 FileChannel，真实类型是 FileChannelImpl
        FileChannel fileChannel01 = fileInputStream.getChannel();

        // 创建一个输出流->channel
        FileOutputStream fileOutputStream = new FileOutputStream(path2);
        // 通过 fileOutputStream 获取对应的 FileChannel，真实类型是 FileChannelImpl
        FileChannel fileChannel02 = fileOutputStream.getChannel();

        // 创建一个缓冲区 ByteBuffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(512);

        while (true) {
            // 这里有一个重要操作，一定不要忘了
            byteBuffer.clear(); // 清空buffer，避免read=0
            int read = fileChannel01.read(byteBuffer);
            // 表示读完
            if (read == -1) {
                break;
            }
            byteBuffer.flip();
            fileChannel02.write(byteBuffer);
        }

        fileInputStream.close();
        fileOutputStream.close();
    }
}
