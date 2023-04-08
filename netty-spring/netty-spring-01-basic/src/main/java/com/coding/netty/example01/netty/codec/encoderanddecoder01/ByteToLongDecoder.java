package com.coding.netty.example01.netty.codec.encoderanddecoder01;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class ByteToLongDecoder extends ByteToMessageDecoder {

    /*
    decode 会根据接收到的数据，被调用多次，直到确定没有新的元素被添加到 list，或者是 ByteBuf 没有更多的可读字节为止。
    如果 list out 不为空，就会将 list 的内容传递给下一个 channelInboundHandler 处理，该处理器的方法也会被调用多次。
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("入站<==ByteToLongDecoder decoder 被调用");
        // 因为 long 8 个字节，需要判断有 8 个字节，才能读取一个 long
        if (in.readableBytes() >= 8) {
            out.add(in.readLong());
        }
    }
}
