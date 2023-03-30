package com.coding.netty.example01.chatroom;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

// @formatter:off
/**
 NIO网络编程应用实例-群聊系统
 实例要求：
 1）编写一个NIO群聊系统，实现服务器端和客户端之间的数据简单通讯（非阻塞）
 2）实现多人群聊
 3）服务器端：可以检测用户上线、离线，并实现消息转发功能
 4）客户端：通过 channnel 可以无阻塞发送消息给其他所有用户，同时可以接受其它用户发送的消息（有服务器转发得到）
 5）目的：进一步理解NIO非阻塞网络编程机制
 */
// @formatter:on
public class ChatRoomServer {
    // 定义属性
    private final Selector selector;
    private final ServerSocketChannel listenChannel;
    private static final int PORT = 6667;

    // 构造器
    // 初始化工作
    public ChatRoomServer() {
        try {
            selector = Selector.open();
            listenChannel = ServerSocketChannel.open();
            listenChannel.socket().bind(new InetSocketAddress(PORT));
            listenChannel.configureBlocking(false);
            listenChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 监听
    private void listen() {
        try {
            while (true) {
                int count = selector.select();
                if (count > 0) {
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()) {
                        SelectionKey selectionKey = iterator.next();
                        if (selectionKey.isAcceptable()) {
                            SocketChannel socketChannel = listenChannel.accept();
                            socketChannel.configureBlocking(false);
                            socketChannel.register(selector, SelectionKey.OP_READ);
                            System.out.println(socketChannel.getRemoteAddress() + " 上线");
                        } else if (selectionKey.isReadable()) {
                            readData(selectionKey);
                        }

                        // 手动从集合中移除当前的 selectionKey，防止重复操作
                        iterator.remove();
                    }
                } else {
                    System.out.println("等待......");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 读取客户端消息
    private void readData(SelectionKey selectionKey) {
        // 定义一个SocketChannel
        SocketChannel channel = null;
        try {
            channel = (SocketChannel)selectionKey.channel();
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

            int count = channel.read(byteBuffer);
            if (count > 0) {
                String msg = new String(byteBuffer.array(), 0, count, StandardCharsets.UTF_8);
                System.out.println("from 客户端：" + msg);

                // 转发消息到其他客户端
                sendInfoToOther(msg, channel);
            }
        } catch (IOException e) {
            try {
                System.out.println(channel.getRemoteAddress() + " 离线了......");
                // 取消注册
                selectionKey.cancel();
                // 关闭通道
                channel.close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    // 转发消息给其他客户（通道）
    private void sendInfoToOther(String msg, SocketChannel self) throws IOException {
        System.out.println("服务器转发消息中......");

        // 遍历所有注册到 selector 上的 SocketChannel，并排除 self
        for (SelectionKey selectionKey : selector.keys()) {
            Channel channel = selectionKey.channel();
            if (channel instanceof SocketChannel && channel != self) {
                SocketChannel socketChannel = (SocketChannel)channel;
                socketChannel.write(ByteBuffer.wrap(msg.getBytes(StandardCharsets.UTF_8)));
            }
        }
    }

    public static void main(String[] args) {
        ChatRoomServer chatRoomServer = new ChatRoomServer();
        chatRoomServer.listen();
    }

}
