package com.coding.netty.example01.netty.tcp.protocoltcp;

import java.util.UUID;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

public class NettyServerHandler extends SimpleChannelInboundHandler<MessageProtocol> {
    private int count;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageProtocol msg) throws Exception {
        // 接收到数据，并处理
        int len = msg.getLen();
        byte[] bytes = msg.getContent();

        System.out.println("服务器接收到信息如下：");
        System.out.println("长度=" + len + " 内容=" + new String(bytes, 0, len, CharsetUtil.UTF_8));
        System.out.println("服务器接收到信息包数量 " + (++this.count));

        // 服务器回送数据给客户端，回送一个随机id
        String responseContent = UUID.randomUUID().toString() + "\r\n";
        byte[] responseBytes = responseContent.getBytes(CharsetUtil.UTF_8);
        int responseLen = responseBytes.length;
        // 构造一个协议包
        MessageProtocol messageProtocol = new MessageProtocol();
        messageProtocol.setLen(responseLen);
        messageProtocol.setContent(responseBytes);
        ctx.writeAndFlush(messageProtocol);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
