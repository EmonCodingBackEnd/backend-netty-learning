package com.coding.netty.example01.netty.http;

import java.net.URI;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

// @formatter:off
/**
 * 说明
 * 1.SimpleChannelInBoundHandler 是 ChannelInBoundHandlerAdapter
 * 2.HttpObject 客户单和服务器端相关通讯的数据被封装成 HttpObject
 */
// @formatter:on
public class NettyHttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {

    // 读取客户端数据
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        // 判断 msg 是不是 httprequest 请求
        if (msg instanceof HttpRequest) {
            System.out.println(
                "pipeline hashCode=" + ctx.pipeline().hashCode() + " TestHttpServerHandler hashCode=" + this.hashCode());

            System.out.println("msg 类型=" + msg.getClass());
            System.out.println("客户端地址" + ctx.channel().remoteAddress());

            // 获取uri
            HttpRequest httpRequest = (HttpRequest)msg;
            URI uri = new URI(httpRequest.uri());
            if ("/favicon.ico".equalsIgnoreCase(uri.getPath())) {
                System.out.println("请求了/favicon.ico，忽略响应！");
            }

            // 回复信息给浏览器[http协议]
            ByteBuf content = Unpooled.copiedBuffer("hello， 我是服务器", CharsetUtil.UTF_8);

            // 构造一个 http 的响应，即 response
            FullHttpResponse response =
                new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());

            // 将构建好的 response 返回
            ctx.writeAndFlush(response);
        }
    }
}
