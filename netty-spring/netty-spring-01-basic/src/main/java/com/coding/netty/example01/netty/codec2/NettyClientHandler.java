package com.coding.netty.example01.netty.codec2;

import java.util.Random;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NettyClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 随机发送一个 Student 或者 Worker 对象到服务器
        int random = new Random().nextInt(2);
        MyDataInfo.MyMessage myMessage;
        if (0 == random) { // 发送 Student 对象
            myMessage = MyDataInfo.MyMessage.newBuilder().setDataType(MyDataInfo.MyMessage.DataType.StudentType)
                .setStudent(MyDataInfo.Student.newBuilder().setId(1).setName("netty").build()).build();
        } else {
            myMessage = MyDataInfo.MyMessage.newBuilder().setDataType(MyDataInfo.MyMessage.DataType.WorkerType)
                .setWorker(MyDataInfo.Worker.newBuilder().setAge(18).setName("netty").build()).build();
        }
        ctx.writeAndFlush(myMessage);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf)msg;
        log.info("服务器回复的消息：" + byteBuf.toString(CharsetUtil.UTF_8));
        log.info("服务器的地址：" + ctx.channel().remoteAddress());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

    public static void main(String[] args) {
        System.out.println(new Random().nextInt(3));
        System.out.println(new Random().nextInt(3));
        System.out.println(new Random().nextInt(3));
        System.out.println(new Random().nextInt(3));
        System.out.println(new Random().nextInt(3));
        System.out.println(new Random().nextInt(3));
        System.out.println(new Random().nextInt(3));
    }
}
