package com.coding.netty.example01.netty.codec.encoderanddecoder;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class NettyServerHandler extends SimpleChannelInboundHandler<Long> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Long msg) throws Exception {
        System.out.println("入站<==从客户端 " + ctx.channel().remoteAddress() + " 读取到long " + msg);

        // 给客户端发送一个 long
        System.out.println("出站==>NettyServerHandler 发送数据");
        ctx.writeAndFlush(98765L);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
