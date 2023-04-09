package com.coding.netty.example01.netty.codec.encoderanddecoder02;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

// @formatter:off
/**
 * ReplayingDecoder 扩展了 ByteToMessageDecoder 类，使用这个类，我们不必调用 readableBytes() 方法。
 * 参数 S 指定了用户状态管理的类型，其中 Void 代表不需要状态管理。
 *
 * ReplayingDecoder 使用方便，但也有局限性：
 * 1、并非所有的 ByteBuf 操作都被支持，如果调用了一个不被支持的方法，将会抛出一个 UnsupportedOperationException。
 * 2、ReplayingDecoder 在某些情况下可能稍慢于 ByteToMessageDecoder；例如：网络缓慢并且消息格式复杂时，消息会被拆成了多个碎片，速度变慢。
 */
// @formatter:on
public class ByteToLongDecoder extends ReplayingDecoder<Void> {

    /*
    decode 会根据接收到的数据，被调用多次，直到确定没有新的元素被添加到 list，或者是 ByteBuf 没有更多的可读字节为止。
    如果 list out 不为空，就会将 list 的内容传递给下一个 channelInboundHandler 处理，该处理器的方法也会被调用多次。
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("入站<==ByteToLongDecoder decoder 被调用");
        // 在 ReplayingDecoder 不需要判断数据是否足够读取，内部会进行处理判断
        out.add(in.readLong());
    }
}
