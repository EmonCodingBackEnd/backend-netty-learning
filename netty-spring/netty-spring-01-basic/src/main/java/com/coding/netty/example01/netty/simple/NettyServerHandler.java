package com.coding.netty.example01.netty.simple;

import java.util.concurrent.TimeUnit;

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
        // shortTask(ctx, (ByteBuf)msg);

        // 比如这里我们有一个非常耗时的业务 -> 异步执行 -> 提交该 channel 对应的 NioEventLoop 的 taskQueue中
        // longTask1(ctx);
        // longTask2(ctx);
        // longTask3(ctx);

        // 用户自定义定时任务 -> 该任务是提交到 scheduleTaskQueue 中
        scheduleTask(ctx);

        super.channelRead(ctx, msg);
    }

    private void shortTask(ChannelHandlerContext ctx, ByteBuf msg) {
        log.info("服务器读取线程");
        System.out.println("server ctx=" + ctx);
        System.out.println("看看 channel 和 pipeline 的关系");
        Channel channel = ctx.channel();
        ChannelPipeline pipeline = ctx.pipeline(); // 本质是一个双向链表，出站入站

        // 将 msg 转成一个 ByteBuf，注意不是 NIO 的 ByteBuffer
        ByteBuf byteBuf = msg;
        System.out.println("客户端发送的消息是：" + byteBuf.toString(CharsetUtil.UTF_8));
        System.out.println("客户端地址：" + ctx.channel().remoteAddress());
    }

    private void longTask1(ChannelHandlerContext ctx) throws InterruptedException {
        Thread.sleep(10 * 1000);
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello,客户端~喵2", CharsetUtil.UTF_8));
        System.out.println("go on......");
    }

    private void longTask2(ChannelHandlerContext ctx) throws InterruptedException {
        ctx.channel().eventLoop().execute(() -> {
            try {
                Thread.sleep(10 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ctx.writeAndFlush(Unpooled.copiedBuffer("hello,客户端~喵2", CharsetUtil.UTF_8));
        });
        System.out.println("go on......");
    }

    private void longTask3(ChannelHandlerContext ctx) throws InterruptedException {
        // 10秒后可见
        ctx.channel().eventLoop().execute(() -> {
            try {
                Thread.sleep(10 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ctx.writeAndFlush(Unpooled.copiedBuffer("hello,客户端~喵2", CharsetUtil.UTF_8));
        });
        // 10+10秒后可见
        ctx.channel().eventLoop().execute(() -> {
            try {
                Thread.sleep(10 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ctx.writeAndFlush(Unpooled.copiedBuffer("hello,客户端~喵3", CharsetUtil.UTF_8));
        });
        System.out.println("go on......");
    }

    private void scheduleTask(ChannelHandlerContext ctx) {
        ctx.channel().eventLoop().schedule(() -> {
            try {
                Thread.sleep(5 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ctx.writeAndFlush(Unpooled.copiedBuffer("hello,客户端~喵4", CharsetUtil.UTF_8));
        }, 5, TimeUnit.SECONDS);
        System.out.println("go on......");
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
