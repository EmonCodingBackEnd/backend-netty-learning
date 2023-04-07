package com.coding.netty.example01.netty.codec2;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NettyServerHandler extends SimpleChannelInboundHandler<MyDataInfo.MyMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MyDataInfo.MyMessage myMessage) throws Exception {
        // 读取从客户端发送的 MyDataInfo.MyMessage
        if (myMessage.getDataType() == MyDataInfo.MyMessage.DataType.StudentType) {
            System.out
                .println("客户端发送的数据 id=" + myMessage.getStudent().getId() + " 名字=" + myMessage.getStudent().getName());
        } else if (myMessage.getDataType() == MyDataInfo.MyMessage.DataType.WorkerType) {
            System.out
                .println("客户端发送的数据 age=" + myMessage.getWorker().getAge() + " 名字=" + myMessage.getWorker().getName());
        } else {
            System.out.println("客户端发送的数据类型不正确");
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello,客户端~喵", CharsetUtil.UTF_8));
        super.channelReadComplete(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        super.exceptionCaught(ctx, cause);
    }
}
