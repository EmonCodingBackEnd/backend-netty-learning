package com.coding.netty.example01.netty.chatroom;

import java.util.Scanner;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class ChatRoomClient {
    private final String host;
    private final int port;

    public ChatRoomClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void run() throws InterruptedException {
        NioEventLoopGroup eventExecutors = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();

        try {
            bootstrap.group(eventExecutors).channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        // 得到 pipeline
                        ChannelPipeline pipeline = ch.pipeline();
                        // 向 pipeline 加入解码器
                        pipeline.addLast("decoder", new StringDecoder());
                        // 向 pipeline 加入编码器
                        pipeline.addLast("encoder", new StringEncoder());
                        // 加入自己的业务处理 handler
                        pipeline.addLast(new ChatRoomClientHandler());
                    }
                });

            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
            System.out.println("netty 客户端启动:" + "----------" + channelFuture.channel().localAddress() + "----------");

            // 客户端需要输入信息，创建一个扫描器
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNextLine()) {
                String msg = scanner.next();
                // 通过channel 发送到服务器端
                channelFuture.channel().writeAndFlush(msg + "\r\n");
            }
        } finally {
            eventExecutors.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new ChatRoomClient("127.0.0.1", 7000).run();
    }
}
