package com.coding.netty.example01.netty.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

// @formatter:off

/**
 * Netty 通过 WebSocket 编程实现服务器和客户端长连接
 * 实例要求：
 * 1）Http 协议是无状态的，浏览器和服务器直接的请求相应一次，下一次会重新创建连接；
 * 2）要求：实现基于 webSocket 的长连接的全双工的交互；
 * 3）改变 Http 协议多次请求的约束，实现长连接了，服务器可以发送消息给浏览器；
 * 4）客户端浏览器和服务器端会相互感知，比如服务器关闭了，浏览器会感知，同样浏览器关闭了，服务器会感知。
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

                        // 因为基于 http 协议，使用 http 的编码和解码器
                        pipeline.addLast(new HttpServerCodec());
                        // 是以块方式写，添加 ChunkedWriteHandler
                        pipeline.addLast(new ChunkedWriteHandler());
                        /*
                        说明：
                        1.http 数据在传输过程中是分段，HttpObjectAggregator，就是可以将多个段聚合起来
                        2.这就是为什么，当浏览器发送大量数据时，就会发送多次 http 请求
                         */
                        pipeline.addLast(new HttpObjectAggregator(8192));
                        /*
                        说明：
                        1.对应 websocket，它的数据时以 帧（frame） 形式传递
                        2.可以看到 WebSocketFrame 下面有6个子类
                        3.浏览器请求时 ws://localhost:7000/hello 表示请求的uri
                        4.WebSocketServerProtocolHandler 核心功能是将 http 协议升级为 ws 协议，保持长连接
                        5.是通过一个状态码 101
                         */
                        pipeline.addLast(new WebSocketServerProtocolHandler("/hello"));
                        // 自定义的 handler，处理业务逻辑
                        pipeline.addLast(new TextWebSocketFrameHanlder());
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
