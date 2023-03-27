package com.coding.netty.example01.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

// @formatter:off
/**
 * Scattering：将数据写入到buffer时，可以采用buffer数组，依次写入【分散】
 * Gathering：将数据从buffer读取出来时，可以采用buffer数组，依次读【聚合】
 * 
 * 服务启动后，Ctrl+R打开命令行： 进入telnet命令行：
 * --------------------------------------------------
 * telnet localhost 7000
 * 然后点击快捷键：Ctrl+]，进入telnet命令行。
 * --------------------------------------------------
 */
// @formatter:on
public class NIOScatteringAndGethering {
    public static void main(String[] args) throws IOException {
        // 使用 ServerSocketChannel 和 SocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        InetSocketAddress inetSocketAddress = new InetSocketAddress(7000);

        // 绑定端口到 serverSocketChannel
        serverSocketChannel.socket().bind(inetSocketAddress);

        // 创建 buffer 数组
        ByteBuffer[] byteBuffers = new ByteBuffer[2];
        byteBuffers[0] = ByteBuffer.allocate(5);
        byteBuffers[1] = ByteBuffer.allocate(3);

        // 等待客户端连接(telnet)
        SocketChannel socketChannel = serverSocketChannel.accept(); // 阻塞等待

        // 设定从客户端接收8个字节
        int messageLength = 8;
        // 循环读取
        while (true) {
            int byteRead = 0;
            while (byteRead < messageLength) {
                long readLen = socketChannel.read(byteBuffers); // 阻塞等待
                // 累计读取的字节数
                byteRead += readLen;
                System.out.println("byteRead=" + byteRead);
                // 使用流打印，看看当前这个 buffer 的 position 和 limit
                Arrays.stream(byteBuffers).map(buffer -> "potision=" + buffer.position() + ", limit=" + buffer.limit())
                    .forEach(System.out::println);
            }

            // 将所有的 buffer 进行flip
            Arrays.stream(byteBuffers).forEach(Buffer::flip);

            // 将数据读出显示到客户端
            long byteWrite = 0;
            while (byteWrite < messageLength) {
                long writeLen = socketChannel.write(byteBuffers);
                byteWrite += writeLen;
            }

            // 即将所有的 buffer 进行 clear
            Arrays.stream(byteBuffers).forEach(Buffer::clear);

            System.out
                .println("byteRead=" + byteRead + ", byteWrite=" + byteWrite + ", messageLength=" + messageLength);
        }

    }
}
