package com.coding.netty.example01.netty.codec.protostuff.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class MessageEncoder extends MessageToByteEncoder<Message> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception {
        System.out.println("出站==>MessageEncoder encoder 被调用");
        byte[] bytes = ProtostuffUtils.serialize(msg);
        out.writeInt(bytes.length);
        out.writeBytes(bytes);
    }
}
