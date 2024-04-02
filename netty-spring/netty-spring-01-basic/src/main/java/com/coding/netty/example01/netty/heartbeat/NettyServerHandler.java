package com.coding.netty.example01.netty.heartbeat;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private String prefix() {
        return LocalDateTime.now().format(dtf) + "=>";
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf)msg;
        System.out.println(
            prefix() + "入站<==从客户端 " + ctx.channel().remoteAddress() + " 读取到消息 " + byteBuf.toString(CharsetUtil.UTF_8));
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            // 将 evt 向下转型
            IdleStateEvent event = (IdleStateEvent)evt;
            String eventType = null;
            switch (event.state()) {
                case READER_IDLE:
                    eventType = "读空闲";
                    break;
                case WRITER_IDLE:
                    eventType = "写空闲";
                    break;
                case ALL_IDLE:
                    eventType = "读写空闲";
                    break;
            }
            System.out.println(ctx.channel().remoteAddress() + "--超时空闲--" + eventType);

            if (event.state() == IdleState.READER_IDLE) {
                System.out.println("服务器做相应处理......");

                // 如果发生空闲，我们关闭通道，之后就不再处理该客户端的请求了
                ctx.channel().close();
            }
        }
    }
}
