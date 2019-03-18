package pers.dxm.netty.httpexample;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Created by IntelliJ IDEA.
 * User: dxm
 * Date: 2019\3\16 0016
 * Time: 0:23
 */
public class TestServer {
    public static void main(String[] args) throws Exception {
        // EventLoopGroup是事件循环组，可以将其看作是一个死循环类，因为服务器肯定要死循环接收客户端的请求
        // bossGroup只负责接收连接，workerGroup则负责真正的处理连接
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            //ServerBootstrap是负责启动服务器的类，其底层封装的也是服务器启动的代码
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            // 链式编程模式，ServerBootstrap启动类绑定线程处理循环组EventLoopGroup对象，
            // 绑定处理管道NioServerSocketChannel，绑定管道中的处理器类（处理器类可以是自定义的）
            // childhandler方法中的处理器处理的是workergroup中的请求，而handler方法处理的事bossgroup中的请求，因此这里用childhandler
            serverBootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).
                    childHandler(new TestServerInitializer());
            // 设置服务端启动时绑定的端口号，返回ChannelFeature对象
            ChannelFuture channelFuture = serverBootstrap.bind(8899).sync();
            channelFuture.channel().closeFuture().sync();
        } finally {
            // 使用优雅关闭的方式关闭
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
