package com.coding.netty.example01.netty.chatroom;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

public class ChatRoomServerHandler extends SimpleChannelInboundHandler<String> {
    // 定义一个 channel 组，管理所有的 channel
    // GlobalEventExecutor.INSTANCE 是全局事件执行器，一个单例
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private String prefix() {
        return LocalDateTime.now().format(dtf) + "=>";
    }

    // Handler 被加入 Pipeline 时触发（仅仅触发一次）
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        /*
        即将该客户端加入聊天室的信息，推送给其他在线的客户端
        该方法会将 channelGroup 中所有的 channel 遍历，并发送消息，我们不需要遍历
         */
        channelGroup.writeAndFlush(prefix() + "[客户端]" + ctx.channel().remoteAddress() + " 加入聊天\n");

        channelGroup.add(ctx.channel());
    }

    // 通道就绪事件
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(prefix() + ctx.channel().remoteAddress() + " 上线了~");
    }

    // 通道断开时触发
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(prefix() + ctx.channel().remoteAddress() + " 离线了");
    }

    // handler 被从 Pipeline 移除时触发
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        // 这里，不需要从 channelGroup 排除 channel，Netty 会自动排除
        channelGroup.writeAndFlush(prefix() + "[客户端]" + ctx.channel().remoteAddress() + " 离开了\n");
        System.out.println(prefix() + "当前 channelGroup size=" + channelGroup.size());
    }

    // 读取数据
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        // 获取到当前 channel
        Channel channel = ctx.channel();

        // 这时我们遍历 channelGroup，根据不同的情况，回送不同的消息
        channelGroup.forEach(ch -> {
            if (channel != ch) {
                ch.writeAndFlush(prefix() + "[客户]" + channel.remoteAddress() + " 发送了消息:" + msg + "\n");
            } else {
                ch.writeAndFlush(prefix() + "[自己]发送了消息:" + msg + "\n");
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 关闭
        ctx.close();
    }
}
