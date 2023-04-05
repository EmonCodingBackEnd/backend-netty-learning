package com.coding.netty.example01.netty.chatroom;

// @formatter:off

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * Netty应用实例-群聊系统
 * 1）编写一个 Netty 群聊系统，实现服务器端和客户端之间的数据简单通讯（费阻塞）
 * 2）实现多人群聊
 * 3）服务器端：可以 检测用户上线、离线、并实现消息转发功能
 * 4）客户端：通过 channel 可以无阻塞发送消息给其他所有用户，同时可以接受其他用户发送的消息（有服务器转发得到）
 * 5）目的：进一步理解 Netty 非阻塞网络编程机制
 */
// @formatter:on
public class ChatRoomServer {

    private final int port; // 监听端口

    public ChatRoomServer(int port) {
        this.port = port;
    }

    // 编写 run 方法，处理客户端的请求
    public void run() throws InterruptedException {
        // 创建两个线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap();
        try {
            bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 128).childOption(ChannelOption.SO_KEEPALIVE, true)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        // 获取到 pipeline
                        ChannelPipeline pipeline = ch.pipeline();
                        // 向 pipeline 加入解码器
                        pipeline.addLast("decoder", new StringDecoder());
                        // 向 pipeline 加入编码器
                        pipeline.addLast("encoder", new StringEncoder());
                        // 加入自己的业务处理 handler
                        pipeline.addLast(new ChatRoomServerHandler());
                    }
                });

            ChannelFuture channelFuture = bootstrap.bind(port).sync();

            System.out.println("netty 服务器启动");
            // 监听关闭事件
            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new ChatRoomServer(7000).run();
    }
}
