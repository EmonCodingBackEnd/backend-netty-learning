package com.coding.netty.example01.netty.codec.encoderanddecoder01;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class NettyServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        /*
        入站事件会从链表 head 往后传递到最后一个入站的 handler，出站事件会总链表的 tail 往前传递到最前一个出站的 handler
        入站读取：先解码=>后业务；所以，ByteToLongDecoder 要在 NettyServerHandler 之前加入
        出站写入：先业务=>后编码；所以，LongToByteEncoder 要在 NettyServerHandler 之前加入
        */
        pipeline.addLast(new ByteToLongDecoder());
        pipeline.addLast(new LongToByteEncoder());
        pipeline.addLast(new NettyServerHandler());
    }
}
