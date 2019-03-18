package pers.dxm.netty.keepalive;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;


/**
 * Created by douxm on 2019\3\18 0018.
 */
public class MyServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        // readerIdleTime=5，writerIdleTime=7，allIdleTime=10，代表若服务端5秒没有读取客户端的消息，7秒没有向客户端发送消息，或10秒既没有读也没有写的话，会被视为连接断开
        pipeline.addLast(new IdleStateHandler(5, 3, 10, TimeUnit.SECONDS));
        // 当连接被IdleStateHandler处理后，再通过自定义的handler进行进一步个性化的处理
        pipeline.addLast(new MyServerHandler());
    }
}
