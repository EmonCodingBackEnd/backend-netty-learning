package com.coding.netty.example01.netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

// @formatter:off
/**
 * 说明
 * 1.我们自定义一个 Handler 需要继承 Netty 规定好的某个 HandlerAdapter
 * 2.这时我们自定义一个 Handler，才能称为一个 Handler
 */
// @formatter:on
@Slf4j
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 读取数据（这里我们可以读取客户端发送的消息）
     * 
     * @param ctx - 上下文对象，含有管道 Pipeline，通道 Channel，地址
     * @param msg - 客户端发送的数据，默认 Object
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.info("服务器读取线程");
        System.out.println("server ctx=" + ctx);
        System.out.println("看看 channel 和 pipeline 的关系");
        Channel channel = ctx.channel();
        ChannelPipeline pipeline = ctx.pipeline(); // 本质是一个双向链表，出站入站

        // 将 msg 转成一个 ByteBuf，注意不是 NIO 的 ByteBuffer
        ByteBuf byteBuf = (ByteBuf)msg;
        System.out.println("客户端发送的消息是：" + byteBuf.toString(CharsetUtil.UTF_8));
        System.out.println("客户端地址：" + ctx.channel().remoteAddress());
        super.channelRead(ctx, msg);
    }

    /**
     * 数据读取完毕
     * 
     * @param ctx - 上下文对象，含有管道 Pipeline，通道 Channel，地址
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // writeAndFlush 是 write + flush，将数据写入缓存，并刷新
        // 一般来讲，我们对这个发送的数据进行编码
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello,客户端~喵", CharsetUtil.UTF_8));
        super.channelReadComplete(ctx);
    }

    /**
     * 处理异常，一般是需要关闭通道
     * 
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        super.exceptionCaught(ctx, cause);
    }
}
