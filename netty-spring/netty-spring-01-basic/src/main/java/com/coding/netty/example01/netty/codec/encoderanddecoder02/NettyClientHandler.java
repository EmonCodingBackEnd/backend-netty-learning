package com.coding.netty.example01.netty.codec.encoderanddecoder02;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

public class NettyClientHandler extends SimpleChannelInboundHandler<Long> {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("出站==>NettyClientHandler 发送数据");
        // ctx.writeAndFlush(123456L);

        // 因为不是 long 类型，所以不会触发 LongToByteEncoder
        ctx.writeAndFlush(Unpooled.copiedBuffer("abcdabcdabcdabcd", CharsetUtil.UTF_8)); // 16个字节，2个long
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Long msg) throws Exception {
        System.out.println("入站<==服务端响应 " + ctx.channel().remoteAddress() + " 读取到long " + msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

}
