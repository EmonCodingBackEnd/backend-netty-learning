package com.coding.netty.example01.netty.codec.protostuff;

import com.coding.netty.example01.netty.codec.protostuff.codec.Message;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NettyServerHandler extends SimpleChannelInboundHandler<Message> {

    @Override
    public void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
        log.info("客户端发送的数据 内容=" + msg.toString());
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        Message message = new Message();
        message.setMsgId(2L);
        message.setType(2);
        message.setData("我是服务端数据！");
        ctx.writeAndFlush(message);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
