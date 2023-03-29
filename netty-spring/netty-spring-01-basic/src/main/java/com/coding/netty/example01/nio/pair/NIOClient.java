package com.coding.netty.example01.nio.pair;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

public class NIOClient {

    public static void main(String[] args) throws IOException {
        // 得到一个网络通道
        SocketChannel socketChannel = SocketChannel.open();
        // 设置非阻塞
        socketChannel.configureBlocking(false);
        // 提供服务器端的IP和端口
        InetSocketAddress inetSocketAddress = new InetSocketAddress(6666);
        // 连接服务器
        if (!socketChannel.connect(inetSocketAddress)) {
            while (!socketChannel.finishConnect()) {
                System.out.println("因为连接需要时间，客户端不会阻塞，可以做其他工作");
            }
        }

        // 如果连接成功，就发送数据
        String str = "hello, 问";
        // Wraps a byte array into a buffer
        byte[] bytes = str.getBytes(Charset.forName("GB18030"));
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        // 发送数据，将 buffer 数据写入 channel
        socketChannel.write(byteBuffer);

        // 阻塞
        System.in.read();
    }
}
