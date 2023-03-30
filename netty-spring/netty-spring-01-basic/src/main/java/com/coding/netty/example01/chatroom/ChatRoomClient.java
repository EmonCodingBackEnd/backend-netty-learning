package com.coding.netty.example01.chatroom;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Scanner;

public class ChatRoomClient {
    // 定义相关的属性
    private final String HOST = "127.0.0.1"; // 服务器的IP
    private final int PORT = 6667; // 服务器端口

    private Selector selector;
    private SocketChannel socketChannel;
    private String username;

    public ChatRoomClient() {
        try {
            selector = Selector.open();
            socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", PORT));
            socketChannel.configureBlocking(false);
            socketChannel.register(selector, SelectionKey.OP_READ);
            username = socketChannel.getLocalAddress().toString().substring(1);

            System.out.println(username + " is ok......");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 向服务器发送消息
    public void sendInfo(String info) throws IOException {
        info = username + " 说：" + info;
        socketChannel.write(ByteBuffer.wrap(info.getBytes(StandardCharsets.UTF_8)));
    }

    // 读取从服务器端返回的消息
    public void readInfo() {
        try {
            int readChannels = selector.select();

            // 有可以用的通道
            if (readChannels > 0) {
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey selectionKey = iterator.next();
                    if (selectionKey.isReadable()) {
                        SocketChannel socketChannel = (SocketChannel)selectionKey.channel();
                        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                        int read = socketChannel.read(byteBuffer);
                        String msg = new String(byteBuffer.array(), 0, read, StandardCharsets.UTF_8);
                        System.out.println(msg.trim());
                    }

                    // 手动从集合中移除当前的 selectionKey，防止重复操作
                    iterator.remove();
                }
            } else {
                System.out.println("没有可用的通道！");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        ChatRoomClient chatRoomClient = new ChatRoomClient();
        new Thread(() -> {
            while (true) {
                try {
                    chatRoomClient.readInfo();
                    Thread.sleep(3000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        Scanner scanner = new Scanner(System.in);
        try {
            while (scanner.hasNext()) {
                String next = scanner.next();
                chatRoomClient.sendInfo(next);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}