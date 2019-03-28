package pers.dxm.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * Created by douxm on 2019\3\28 0028.
 */
public class NioTest9 {
    public static void main(String[] args) throws Exception {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        InetSocketAddress address = new InetSocketAddress(8899);
        serverSocketChannel.socket().bind(address);
        // 将字节长度分为三部分
        int messageLength = 1 + 2 + 3;
        // 创建三个buffer对象
        ByteBuffer[] buffers = new ByteBuffer[3];
        buffers[0] = ByteBuffer.allocate(1);
        buffers[1] = ByteBuffer.allocate(2);
        buffers[2] = ByteBuffer.allocate(3);
        // 开启channel接收请求
        SocketChannel socketChannel = serverSocketChannel.accept();
        while (true) {
            int bytesRead = 0;
            // 当没有读完客户端发来的数据时就不停地读
            while (bytesRead < messageLength) {
                long r = socketChannel.read(buffers);
                bytesRead += r;
                System.out.println("bytesRead: " + bytesRead);
                Arrays.asList(buffers).stream().
                        map(buffer -> "position: " + buffer.position() + ", limit: " + buffer.limit()).
                        forEach(System.out::println);
            }
            // 反转状态开始写出
            Arrays.asList(buffers).forEach(buffer -> {
                buffer.flip();
            });
            // 将buffer中的数据写出到channel中
            long bytesWritten = 0;
            while (bytesWritten < messageLength) {
                long r = socketChannel.write(buffers);
                bytesWritten += r;
            }
            Arrays.asList(buffers).forEach(buffer -> {
                buffer.clear();
            });
            System.out.println("bytesRead: " + bytesRead + ", bytesWritten: " + bytesWritten + ", messageLength: " + messageLength);
        }

    }
}
