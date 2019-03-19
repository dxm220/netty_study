package pers.dxm.netty.websocket;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * Created by douxm on 2019\3\19 0019.
 */
public class WebSocketServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        // 因为WebSocket归根结底也是Http升级而来的，因此首先加上对Http的处理器
        pipeline.addLast(new HttpServerCodec());
        // 处理大文件传输的处理器，大文件以分块的方式写（每次固定大小），不会造成服务端内存溢出
        pipeline.addLast(new ChunkedWriteHandler());
        // 将同一个Http请求或响应的多个消息对象变成一个完整的fullHttpRequest或fullHttpResponse对象
        pipeline.addLast(new HttpObjectAggregator(8192));
        // 自定义处理器，将经过上述处理器处理后的请求再进行个性化的处理
        pipeline.addLast(new TextWebSocketFrameHandler());
    }
}
