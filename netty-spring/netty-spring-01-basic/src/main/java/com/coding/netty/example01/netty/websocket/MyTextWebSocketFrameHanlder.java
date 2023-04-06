package com.coding.netty.example01.netty.websocket;

import java.time.LocalDateTime;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

/**
 * TextWebSocketFrame 表示文本帧（frame）
 */
public class MyTextWebSocketFrameHanlder extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    // Handler 被加入 Pipeline 时触发（仅仅触发一次）
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        // id 表示唯一的值，LongText 是唯一的 ShortText 不是唯一的
        System.out.println("handlerAdded 被调用：" + ctx.channel().id().asLongText());
        System.out.println("handlerAdded 被调用：" + ctx.channel().id().asShortText());
    }

    // handler 被从 Pipeline 移除时触发
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("handlerRemoved 被调用：" + ctx.channel().id().asLongText());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("异常发生：" + cause.getMessage());

        // 关闭
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        System.out.println("服务器收到消息：" + msg.text());
        // 回复消息
        ctx.channel().writeAndFlush(new TextWebSocketFrame("服务器事件：" + LocalDateTime.now() + " " + msg.text()));
    }
}
