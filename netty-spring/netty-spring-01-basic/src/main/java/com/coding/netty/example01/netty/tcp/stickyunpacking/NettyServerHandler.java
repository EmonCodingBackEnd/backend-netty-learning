package com.coding.netty.example01.netty.tcp.stickyunpacking;

import java.util.UUID;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

public class NettyServerHandler extends SimpleChannelInboundHandler<ByteBuf> {
    private int count;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        byte[] bytes = new byte[msg.readableBytes()];
        msg.readBytes(bytes);

        // 将 bytes 转换成字符串
        String message = new String(bytes, 0, bytes.length, CharsetUtil.UTF_8);

        System.out.println("服务器接收到数据 " + message);
        System.out.println("服务器接收到消息量 " + (++this.count));

        // 服务器回送数据给客户端，回送一个随机id
        ByteBuf responseByteBuf = Unpooled.copiedBuffer(UUID.randomUUID().toString().concat("\r\n"), CharsetUtil.UTF_8);
        ctx.writeAndFlush(responseByteBuf);

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
