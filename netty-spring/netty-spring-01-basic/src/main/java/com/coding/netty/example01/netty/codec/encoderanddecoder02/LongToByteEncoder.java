package com.coding.netty.example01.netty.codec.encoderanddecoder02;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class LongToByteEncoder extends MessageToByteEncoder<Long> {

    /*
    注意：发送的数据类型，如果不是 Long，默认不会触发编码
     */
    @Override
    protected void encode(ChannelHandlerContext ctx, Long msg, ByteBuf out) throws Exception {
        System.out.println("出站==>LongToByteEncoder encoder 被调用");
        System.out.println("msg=" + msg);
        out.writeLong(msg);
    }

}
