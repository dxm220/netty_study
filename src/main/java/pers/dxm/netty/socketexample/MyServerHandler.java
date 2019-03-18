package pers.dxm.netty.socketexample;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.UUID;

/**
 * Created by IntelliJ IDEA.
 * User: dxm
 * Date: 2019\3\17 0017
 * Time: 21:14
 */
public class MyServerHandler extends SimpleChannelInboundHandler<String> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        // 打印客户端发送过来的消息
        System.out.println(channelHandlerContext.channel().remoteAddress() + "" + s);
        // writeAndFlush = write + flush；将服务端消息返回给客户端
        channelHandlerContext.writeAndFlush("from server"+ UUID.randomUUID());
    }

    /**
     * 处理异常的回调方法，当处理器发生异常时回调
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 打印异常信息
        cause.printStackTrace();
        ctx.close();
    }
}
