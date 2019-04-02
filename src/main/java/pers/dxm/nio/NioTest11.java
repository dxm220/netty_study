package pers.dxm.nio;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by douxm on 2019\3\29 0029.
 */
public class NioTest11 {
    public static void main(String[] args) throws Exception {
        // 初始化5个不同的端口号
        int[] ports = new int[5];
        ports[0] = 5000;
        ports[1] = 5001;
        ports[2] = 5002;
        ports[3] = 5003;
        ports[4] = 5004;
        // 初始化一个selector对象
        Selector selector = Selector.open();
        // 为每个端口绑定一个channel
        for (int i = 0; i < ports.length; ++i) {
            // 初始化channel对象
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            // 将channel状态设置为非阻塞状态
            serverSocketChannel.configureBlocking(false);
            // 获取channel对应的socket对象并将channel与socket对象绑定，监听对应的端口号
            ServerSocket serverSocket = serverSocketChannel.socket();
            InetSocketAddress address = new InetSocketAddress(ports[i]);
            serverSocket.bind(address);
            // 监听结束后，将channel注册到selector上，此时SelectionKey的状态为监听请求
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("监听端口： " + ports[i]);
        }

        while (true) {
            // 返回准备就绪的通道数，若没有符合条件的返回，select方法将一直阻塞
            int numbers = selector.select();
            System.out.println("numbers: " + numbers);
            // 获取之前所有已经注册过的SelectionKey对象集合
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            System.out.println("selectedKeys: " + selectionKeys);
            // 将获取到的集合进行遍历分类，根据不同类型的SelectionKey做不同的操作
            Iterator<SelectionKey> iter = selectionKeys.iterator();

            while (iter.hasNext()) {
                SelectionKey selectionKey = iter.next();
                if (selectionKey.isAcceptable()) {
                    // 获取selectionKey为accept状态的channel
                    ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
                    // 开启通道的连接
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);
                    // 将channel从accept状态重新注册为read状态，即准备读取客户端发送的消息
                    socketChannel.register(selector, SelectionKey.OP_READ);
                    // 将iter中accept状态对应的selectionKey移除，否则该selectionKey将被重复获取，报空指针异常
                    iter.remove();
                    System.out.println("获得客户端连接： " + socketChannel);
                } else if (selectionKey.isReadable()) {
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                    // 获取到selectionKey为read状态的channel，并通过channel的buffer读取来自客户端的数据
                    int bytesRead = 0;
                    while (true) {
                        ByteBuffer byteBuffer = ByteBuffer.allocate(512);
                        byteBuffer.clear();
                        int read = socketChannel.read(byteBuffer);
                        if (read <= 0) {
                            break;
                        }
                        // 读取完毕后翻转buffer状态
                        byteBuffer.flip();
                        // channel再通过buffer将数据写出到此次io操作的目的地
                        socketChannel.write(byteBuffer);
                        bytesRead += read;
                    }
                    System.out.println("读取： " + bytesRead + "，来自于： " + socketChannel);
                    // 清除对应状态为read的selectionKey
                    iter.remove();
                }
            }
        }
    }
}
