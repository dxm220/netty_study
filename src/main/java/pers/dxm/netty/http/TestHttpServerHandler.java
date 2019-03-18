package pers.dxm.netty.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.URI;

/**
 * Created by IntelliJ IDEA.
 * User: dxm
 * Date: 2019\3\16 0016
 * Time: 1:04
 * 自定义处理器
 */
public class TestHttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {
    /**
     * channelRead0：代表处理器接收到客户端请求并向客户端返回响应，同样也是回调方法
     * 执行到该方法时代表服务端已经接收到请求
     */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, HttpObject httpObject) throws Exception {
        // 如果请求时http请求，进入判断处理chrome重复请求网站图标的问题
        if (httpObject instanceof HttpRequest) {
            HttpRequest request = (HttpRequest) httpObject;
            System.out.println("请求方法名：" + request.method().name());
            // 获取用户请求的uri，判断uri中是否包含favicon.icon
            URI uri = new URI(request.uri());
            System.out.println(uri);
            if ("/favicon.ico".equals(uri)) {
                // 如果请求的是icon，直接跳出方法，不再返回response
                System.out.println("请求的是icon");
                return;
            }
            //ByteBuf对象：返回给客户端的内容
            ByteBuf content = Unpooled.copiedBuffer("helloword", CharsetUtil.UTF_8);
            //FullHttpResponse对象：返回给客户端的相应对象
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                    HttpResponseStatus.OK, content);
            //设置response对象的头信息
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());
            // 调用writeandflush才会真正返回给客户端，如果调用write只能把响应放在缓冲区中并不会返回
            channelHandlerContext.writeAndFlush(response);
            // 关闭channel连接，防止Http1.1时长时间占用
            channelHandlerContext.channel().close();
        }
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println("模拟handler added被回调");
        super.handlerAdded(ctx);
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("模拟channel registered被回调");
        super.channelRegistered(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("模拟channel active被回调");
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("模拟channel inactive被回调");
        super.channelInactive(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("模拟channel unregistered被回调");
        super.channelUnregistered(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("模拟channel read被回调");
        super.channelRead(ctx, msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("模拟channel readcomplete被回调");
        super.channelReadComplete(ctx);
    }
}
