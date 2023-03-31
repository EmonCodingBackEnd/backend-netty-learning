package com.coding.netty.example01.nio.zerocopy;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

import com.coding.netty.common.EnvUtils;

public class NewIOClient {
    public static void main(String[] args) throws IOException {
        InetSocketAddress inetSocketAddress = new InetSocketAddress("localhost", 7001);
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(inetSocketAddress);

        String filePath = System.getProperty("user.dir") + "/netty/spring/example01/small.tgz";
        filePath = System.getProperty("user.dir") + "/netty/spring/example01/big.zip";

        // 得到一个文件 channel
        FileInputStream fileInputStream = new FileInputStream(filePath);
        FileChannel fileChannel = fileInputStream.getChannel();

        // 准备发送
        long startTime = System.currentTimeMillis();

        // transferTo 底层使用到零拷贝
        // 在linux下一个 transferTo 方法就可以完成传输
        // 在windows下一个 transferTo 方法调用一次只能发送 8M，就需要分段传输文件，而且要注意传输时的位置
        long total = 0;
        if (EnvUtils.isWin) {
            long fileTotalLength = fileInputStream.available();
            int maxTransferCountInWinPerTimes = 8 * 1024 * 1024;
            if (fileTotalLength > maxTransferCountInWinPerTimes) {
                while (total < fileTotalLength) {
                    long transferCount = fileChannel.transferTo(total, maxTransferCountInWinPerTimes, socketChannel);
                    total += transferCount;
                }
            } else {
                total = fileChannel.transferTo(0, fileInputStream.available(), socketChannel);
            }
        } else {
            total = fileChannel.transferTo(0, fileInputStream.available(), socketChannel);
        }

        // 发送总字节数：65826045，耗时：180ms
        // 发送总字节数：2147483648，耗时：3542ms
        System.out.println("发送总字节数：" + total + "，耗时：" + (System.currentTimeMillis() - startTime) + "ms");

        fileChannel.close();
        fileInputStream.close();
        socketChannel.close();
    }
}
