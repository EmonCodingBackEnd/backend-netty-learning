package com.coding.netty.example01.netty.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

// @formatter:off
/**
 * 【重要】当一个 NettyClient 进来时，NettyServer 使用到的 NettyHttpServerInitializer 是同一个对象，
 * 但由于 initChannel 每次都初始化，所以 pipeline.addLast 加入的其他 ChannelHandler 是新的实例。  
 */
// @formatter:on
public class NettyHttpServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        // 向管道加入处理器
        // 得到管道
        ChannelPipeline pipeline = ch.pipeline();

        // 加入一个 netty 提供的 httpServerCodec codec => [coder - decoder]
        // 1.HttpServerCodec 是 Netty 提供的编码、解码器
        pipeline.addLast("MyHttpServerCoder", new HttpServerCodec());
        // 2.增加一个自定义的 handler
        pipeline.addLast("MyTestHttpServerHandler", new NettyHttpServerHandler());

        System.out.println("ok~~~~~");
    }
}
