package com.coding.netty.example01.netty.tcp.protocoltcp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class MessageEncoder extends MessageToByteEncoder<MessageProtocol> {
    @Override
    protected void encode(ChannelHandlerContext ctx, MessageProtocol msg, ByteBuf out) throws Exception {
        System.out.println("出站==>MessageEncoder encoder 被调用");
        out.writeInt(msg.getLen());
        out.writeBytes(msg.getContent());
    }
}
