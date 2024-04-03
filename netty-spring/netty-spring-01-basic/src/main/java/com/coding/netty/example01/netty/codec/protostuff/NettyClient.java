package com.coding.netty.example01.netty.codec.protostuff;

import com.coding.netty.example01.netty.codec.protostuff.codec.MessageDecoder;
import com.coding.netty.example01.netty.codec.protostuff.codec.MessageEncoder;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyClient {
    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup eventExecutors = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();

        try {
            bootstrap.group(eventExecutors).channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast("decoder", new MessageDecoder());
                        pipeline.addLast("encoder", new MessageEncoder());
                        pipeline.addLast(new NettyClientHandler());
                    }
                });

            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 6668).sync();
            System.out.println("客户端 ok......");

            // 对关闭通道进行监听
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            eventExecutors.shutdownGracefully();
        }
    }
}
