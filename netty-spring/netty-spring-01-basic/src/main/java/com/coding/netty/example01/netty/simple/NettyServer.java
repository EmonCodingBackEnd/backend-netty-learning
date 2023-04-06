package com.coding.netty.example01.netty.simple;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyServer {
    public static void main(String[] args) throws InterruptedException {
        /*
        1.创建两个线程组 BossGroup 和 WorkerGroup
        2.BossGroup 只是处理连接请求，真正的和客户端业务处理，会交给 WorkerGroup 完成
        3.两个都是无限循环
        4.BossGroup 和 WorkerGroup 含有的子线程（NioEventLoop）的个数，默认实际 CPU 核数 * 2
         */
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup(4);

        // 创建服务器端的启动对象，配置参数
        ServerBootstrap bootstrap = new ServerBootstrap();

        try {
            // 使用链式编程来进行设置
            bootstrap.group(bossGroup, workerGroup) // 设置两个线程组
                .channel(NioServerSocketChannel.class) // 使用 NioSocketChannel 作为服务器的通道
                .option(ChannelOption.SO_BACKLOG, 128) // 设置线程队列得到连接个数
                .childOption(ChannelOption.SO_KEEPALIVE, true) // 设置保持活动连接状态
                // .handler(null) // 该 handler 对应的 boosGroup，childHandler 对应的是 workerGroup
                .childHandler(new ChannelInitializer<SocketChannel>() { // 创建一个通道测试对象（匿名对象）
                    // 给 Pipeline 设置处理器
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        // 可以使用一个集合管理 SocketChannel，再推送消息时，可以将业务加入到各个 channel 对应的 NioEventLoop 的 taskQueue 或者
                        // scheduleTaskQueue
                        System.out.println("客户 socketChannel 的 hashCode=" + ch.hashCode());
                        ch.pipeline().addLast(new NettyServerHandler());
                    }
                }); // 给我们的 WorkerGroup 的 EventLoop 对应的管道设置处理器

            // 绑定一个端口并且同步，生成了一个 ChannelFuture 对象
            ChannelFuture channelFuture = bootstrap.bind(6668).sync();
            System.out.println("netty 服务器启动在端口：" + 6668);

            // 注册监听器，监控我们关心的事件
            channelFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (channelFuture.isSuccess()) {
                        System.out.println("监听端口 6668 成功");
                    } else {
                        System.out.println("监听端口 6668 失败");
                    }
                }
            });

            // 对关闭通道进行监听
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
}
