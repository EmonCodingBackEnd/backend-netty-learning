package com.coding.netty.example01.netty.tcp.protocoltcp;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

public class NettyClientHandler extends SimpleChannelInboundHandler<MessageProtocol> {
    private int count;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 使用客户端发送10条数据 hello,问秋
        for (int i = 0; i < 10; i++) {
            String msg = "hello,问秋" + i;
            byte[] bytes = msg.getBytes(CharsetUtil.UTF_8);
            int len = bytes.length;

            // 创建协议包
            MessageProtocol messageProtocol = new MessageProtocol();
            messageProtocol.setLen(len);
            messageProtocol.setContent(bytes);

            ctx.writeAndFlush(messageProtocol);
        }

    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageProtocol msg) throws Exception {
        // 接收到数据，并处理
        int len = msg.getLen();
        byte[] bytes = msg.getContent();

        System.out.println("客户端接收到信息如下：");
        System.out.println("长度=" + len + " 内容=" + new String(bytes, 0, len, CharsetUtil.UTF_8));
        System.out.println("客户端接收到信息包数量 " + (++this.count));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
