package pers.dxm.netty.socketchat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * Created by douxm on 2019\3\18 0018.
 */
public class MyChatServerHandler extends SimpleChannelInboundHandler<String> {

    // 初始化channelGroup对象，用于存放多个与服务端已经建立连接的channel对象
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    /**
     * 服务端接收到任意一个客户端的消息后，该方法会被回调
     */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        // 获取到发送消息的客户端channel对象
        Channel channel = channelHandlerContext.channel();
        // 区分该channel对象自己还是别人
        channelGroup.forEach(ch -> {
            if (channel != ch) {
                ch.writeAndFlush(channel.remoteAddress() + " 发送的消息：" + s + "\n");
            } else {
                ch.writeAndFlush("【自己】" + s + "\n");
            }
        });
    }

    /**
     * 当客户端与服务端建立好连接后，该方法被回调执行
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        // 获取到客户端与服务端连接的channel对象
        Channel channel = ctx.channel();
        // 将新连接加入的消息广播给channelGroup中其他所有的channel对象，模拟新用户上线进行广播的效果
        channelGroup.writeAndFlush("【服务器】-" + channel.remoteAddress() + "加入\n");
        // 广播完成后，将新连接加入到channelGroup中
        channelGroup.add(channel);
    }

    /**
     * 当连接断开后，该方法被回调执行
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        // 将某个连接断开的消息广播给其他channel
        channelGroup.writeAndFlush("【服务器】- " + channel.remoteAddress() + " 离开\n");
        // 当某个连接断开后，Netty会自动执行该行代码
        //channelGroup.remove(channel);
        System.out.println(channelGroup.size());
    }

    /**
     * 连接处于活动状态时，该方法被回调
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        System.out.println(channel.remoteAddress() + " 上线");
    }

    /**
     * 连接处于非活动状态时，该方法被回调
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        System.out.println(channel.remoteAddress() + " 下线");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
