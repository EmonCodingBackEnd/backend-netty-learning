package com.coding.netty.example01.nio;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 1.MappedByteBuffer 可以让文件直接在内存（堆外内存）修改，操作系统不需要copy一次。
 */
public class NIOMappedByteBuffer {

    public static void main(String[] args) throws IOException {
        String path = System.getProperty("user.dir") + "/netty/spring/example01/file01.txt";
        File file = new File(path);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
        }

        RandomAccessFile randomAccessFile = new RandomAccessFile(path, "rw");
        // 获取对应的通道
        FileChannel channel = randomAccessFile.getChannel();
        /*
        参数1：使用读写模式
        参数2：可以直接修改的起始位置
        参数3：映射到内存的大小，即将 文件 的多少个字节映射到内存，可以直接修改的范围就是[0-5)共5个位置，不包含5，否则会抛出异常：IndexOutOfBoundsException
        MappedByteBuffer实际类型：DirectByteBuffer
         */
        MappedByteBuffer map = channel.map(FileChannel.MapMode.READ_WRITE, 0, 5);
        map.put(0, (byte)'H');
        map.put(3, (byte)'9');

        // IndexOutOfBoundsException
        // map.put(5, (byte)'Y');

        randomAccessFile.close();
    }
}
