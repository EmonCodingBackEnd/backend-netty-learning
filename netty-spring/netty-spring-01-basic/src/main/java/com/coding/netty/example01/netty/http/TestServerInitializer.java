package com.coding.netty.example01.netty.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

public class TestServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        // 向管道加入处理器
        // 得到管道
        ChannelPipeline pipeline = ch.pipeline();

        // 加入一个 netty 提供的 httpServerCodec codec => [coder - decoder]
        // 1.HttpServerCodec 是 Netty 提供的编码、解码器
        pipeline.addLast("MyHttpServerCoder", new HttpServerCodec());
        // 2.增加一个自定义的 handler
        pipeline.addLast("MyTestHttpServerHandler", new TestHttpServerHandler());
    }
}
