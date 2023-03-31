package com.coding.netty.example01.nio.zerocopy;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class NewIOServer {
    public static void main(String[] args) throws IOException {
        InetSocketAddress inetSocketAddress = new InetSocketAddress(7001);
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(inetSocketAddress);

        // 创建 buffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(4096);
        while (true) {
            SocketChannel socketChannel = serverSocketChannel.accept();

            int readCount = 0;
            while (readCount != -1) {
                try {
                    readCount = socketChannel.read(byteBuffer);
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }

                // 倒带
                byteBuffer.rewind();
            }
        }
    }
}
