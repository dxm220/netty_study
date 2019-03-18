package pers.dxm.netty.httpexample;

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
            if ("/favicon.icon".equals(uri)) {
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
            //调用writeandflush才会真正返回给客户端，如果调用write只能把响应放在缓冲区中并不会返回
            channelHandlerContext.writeAndFlush(response);
        }
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println("handler added");
        super.handlerAdded(ctx);
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel registered");
        super.channelRegistered(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel active");
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel inactive");
        super.channelInactive(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel unregistered");
        super.channelUnregistered(ctx);
    }
}
