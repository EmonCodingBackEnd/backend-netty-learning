package com.coding.netty.example01.nio.zerocopy;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;

public class OldIOClient {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 7001);

        String filePath = System.getProperty("user.dir") + "/netty/spring/example01/small.tgz";
        FileInputStream fileInputStream = new FileInputStream(filePath);

        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
        byte[] bytes = new byte[4096];
        long readCount;
        long total = 0;
        long startTime = System.currentTimeMillis();

        // 从硬盘读取数据到Socket
        while ((readCount = fileInputStream.read(bytes)) >= 0) {
            total += readCount;
            dataOutputStream.write(bytes);
        }

        // 发送总字节数：65826045，耗时：538ms
        System.out.println("发送总字节数：" + total + "，耗时：" + (System.currentTimeMillis() - startTime) + "ms");

        dataOutputStream.close();
        socket.close();
        fileInputStream.close();
    }
}
