package com.coding.netty.example01.netty.tcp.protocoltcp;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

public class MessageDecoder extends ReplayingDecoder<Void> {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("入站<==ByteToLongDecoder decoder 被调用");
        // 在 ReplayingDecoder 不需要判断数据是否足够读取，内部会进行处理判断
        // 需要将得到的二进制字节码 => MessageProtocol 数据包（对象）
        int len = in.readInt();
        byte[] bytes = new byte[len];
        in.readBytes(bytes);

        // 封装成 MessageProtocol 对象，放入 out，传递下一个 handler 业务处理
        MessageProtocol messageProtocol = new MessageProtocol();
        messageProtocol.setLen(len);
        messageProtocol.setContent(bytes);
        out.add(messageProtocol);
    }
}
