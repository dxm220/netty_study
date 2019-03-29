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
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();、
            // 将channel状态设置为非阻塞状态
            serverSocketChannel.configureBlocking(false);
            // 将channel与socket对象绑定并监听对应的端口号
            ServerSocket serverSocket = serverSocketChannel.socket();
            InetSocketAddress address = new InetSocketAddress(ports[i]);
            serverSocket.bind(address);
            // 监听结束后，将channel注册到selector上，此时SelectionKey的状态为监听请求
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("监听端口： " + ports[i]);
        }

        while (true) {
            int numbers = selector.select();
            System.out.println("numbers: " + numbers);

            Set<SelectionKey> selectionKeys = selector.selectedKeys();

            System.out.println("selectedKeys: " + selectionKeys);

            Iterator<SelectionKey> iter = selectionKeys.iterator();

            while (iter.hasNext()) {
                SelectionKey selectionKey = iter.next();

                if (selectionKey.isAcceptable()) {
                    ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);

                    socketChannel.register(selector, SelectionKey.OP_READ);
                    // 每次对一个selectionKey操作完毕后，都要将其从iter中移除
                    iter.remove();

                    System.out.println("获得客户端连接： " + socketChannel);
                } else if (selectionKey.isReadable()) {
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();

                    int bytesRead = 0;

                    while (true) {
                        ByteBuffer byteBuffer = ByteBuffer.allocate(512);

                        byteBuffer.clear();

                        int read = socketChannel.read(byteBuffer);

                        if (read <= 0) {
                            break;
                        }

                        byteBuffer.flip();

                        socketChannel.write(byteBuffer);

                        bytesRead += read;
                    }

                    System.out.println("读取： " + bytesRead + "，来自于： " + socketChannel);

                    iter.remove();
                }
            }
        }
    }
}
