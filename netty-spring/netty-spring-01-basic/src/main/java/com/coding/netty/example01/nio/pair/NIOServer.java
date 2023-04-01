package com.coding.netty.example01.nio.pair;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

public class NIOServer {
    public static void main(String[] args) throws IOException {
        // 创建 ServerSocketChannel -> 对应ServerSocket
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        // 绑定一个端口 6666，在服务器端监听
        serverSocketChannel.socket().bind(new InetSocketAddress(6666));
        // 设置为非阻塞
        serverSocketChannel.configureBlocking(false);

        // 得到一个 Selector 对象，实际是：WindowsSelectorImpl
        Selector selector = Selector.open();

        // 把 serverSocketChannel 注册到 selector 并关注 OP_ACCEPT 事件
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("注册后的 selectionKeys 数量=" + selector.keys().size());

        // 循环等待客户端连接
        while (true) {
            // 这里我们等待1s，如果1s内没有事件发生
            if (selector.select(1000) == 0) {
                System.out.println("服务器等待了1s，无连接！");
                continue;
            }

            // 如果返回的>0，就获取到相关的 selectionKeys 集合
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            System.out.println("监听到的 selectionKeys 数量=" + selectionKeys.size());

            // 遍历 Set<SelectionKey>，使用迭代器遍历
            Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
            while (keyIterator.hasNext()) {
                // 获取到 selectionKey
                SelectionKey selectionKey = keyIterator.next();
                // 如果是 OP_ACCEPT，表示有新的客户端连接
                if (selectionKey.isAcceptable()) {
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    System.out.println("客户端连接成功，生成了一个 socketChannel:" + socketChannel.hashCode());
                    // 设置为非阻塞，否则register时会提示：java.nio.channels.IllegalBlockingModeException
                    socketChannel.configureBlocking(false);
                    // 将 socketChannel 注册到 selecotr，关注事件为 OP_READ
                    socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(10));
                    System.out.println("注册后的 selectionKeys 数量=" + selector.keys().size());
                }
                // 如果是 OP_READ
                else if (selectionKey.isReadable()) {
                    // SocketChannelImpl
                    SocketChannel channel = (SocketChannel)selectionKey.channel();
                    // 获取到该 channel 关联的buffer
                    ByteBuffer buffer = (ByteBuffer)selectionKey.attachment();

                    int totalRead = 0;
                    int read;
                    StringBuilder sb = new StringBuilder();
                    // 只要还能读出，就尽力读；如果一次读入的量超过 totalRead 仍未读完，超过的部分会在下次的事件通知中继续读取
                    while ((read = channel.read(buffer)) > 0) {
                        sb.append(new String(buffer.array(), 0, read, Charset.forName("GB18030")));

                        // 这里有一个重要操作，一定不要忘了
                        buffer.clear(); // 清空buffer，避免read=0

                        // 可能读取到的字节数不满足一个汉字，产生乱码
                        totalRead += read;
                        if (totalRead > 15) {
                            break;
                        }
                    }
                    System.out.println("from client " + sb);
                }

                // 手动从集合中移除当前的 selectionKey，防止重复操作
                keyIterator.remove();
            }
        }

    }
}
