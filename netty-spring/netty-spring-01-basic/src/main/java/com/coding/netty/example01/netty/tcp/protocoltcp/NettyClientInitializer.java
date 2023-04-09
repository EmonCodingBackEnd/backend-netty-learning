package com.coding.netty.example01.netty.tcp.protocoltcp;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class NettyClientInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new MessageEncoder());
        pipeline.addLast(new MessageDecoder());
        pipeline.addLast(new NettyClientHandler());
    }
}
