package pers.dxm.netty.httpexample;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;


/**
 * Created by IntelliJ IDEA.
 * User: dxm
 * Date: 2019\3\16 0016
 * Time: 1:03
 */
public class TestServerInitializer extends ChannelInitializer<SocketChannel> {
    //重写回调方法，初始化管道时netty会自动执行initChannel方法（事件驱动）
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        //ChannelPipeline相当于管道中的一个拦截器，可以在ChannelPipeline中定义多个处理器，处理该拦截器所拦截的请求
        ChannelPipeline pipeline = socketChannel.pipeline();
        //为拦截器绑定处理器，倒数第二个处理器为netty自带的HttpServerCodec处理器，倒数第一个处理器为自定义的处理器
        //ChannelPipeline提供了很多addbefore，addlast这种方法，相当于给一个拦截器中的多个处理器排队
        pipeline.addLast("HttpServerCodec", new HttpServerCodec());
        pipeline.addLast("testHttpServer", new TestHttpServerHandler());
    }
}
