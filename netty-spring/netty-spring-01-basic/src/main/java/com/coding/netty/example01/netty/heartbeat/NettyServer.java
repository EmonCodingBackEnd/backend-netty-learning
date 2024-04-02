package com.coding.netty.example01.netty.heartbeat;

import java.util.concurrent.TimeUnit;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

// @formatter:off
/**
 * Netty 心跳检测机制
 * 案例要求：
 * 1）编写一个 Netty 心跳检测机制案例，当服务器超过 30 秒没有读取时，就提示读空闲
 * 2）当服务器超过 50 秒没有写操作时，就提示写空闲
 * 3）实现当服务器超过 10 秒没有读或者写操作时，就提示读写空闲
 */
// @formatter:on
public class NettyServer {
    public static void main(String[] args) throws InterruptedException {
        // 创建两个线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap();

        try {
            bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO)).childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        // 获取到 pipeline
                        ChannelPipeline pipeline = ch.pipeline();
                        // 加入一个 netty 提供 IdleStateHandler
                        /*
                        1. IdleStateHandler 是 netty 提供的处理空闲状态的处理器
                        2. int readerIdleTimeSeconds: 表示多长时间没有读，就会发送一个心跳检测包检测是否连接
                        3. int writerIdleTimeSeconds: 表示多长时间没有写，就会发送一个心跳检测包检测是否连接
                        4. int allIdleTimeSeconds: 表示多长时间没有读或写，就会发送一个心跳检测包检测是否连接
                        5. 当 IdleStateEvent 触发后，就会传递给管道的下一个 handler；通过调用（触发）下一个 handler 的 userEventTriggered，在该
                        方法中去处理
                         */
                        pipeline.addLast(new IdleStateHandler(30, 50, 10, TimeUnit.SECONDS));
                        // 加入一个对空闲检测进一步处理的 handler（自定义）
                        pipeline.addLast(new NettyServerHandler());
                    }
                });

            ChannelFuture channelFuture = bootstrap.bind(7000).sync();
            System.out.println("netty 服务器启动在端口：" + 7000);

            // 监听关闭事件
            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
