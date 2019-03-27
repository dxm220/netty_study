package pers.dxm.nio;

import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by douxm on 2019\3\27 0027.
 */
public class NioTest4 {
    public static void main(String[] args) throws Exception {
        // 初始化文件读写流对象
        FileOutputStream fileOutputStream = new FileOutputStream("NioTest3.txt");
        FileOutputStream outputStream = new FileOutputStream("output.txt");
        // 通过文件流对象获取channel读取写出对象
        FileChannel inputChannel = fileOutputStream.getChannel();
        FileChannel outputChannel = outputStream.getChannel();
        // 初始化buffer对象,最多一次可读取512个字节
        ByteBuffer byteBuffer = ByteBuffer.allocate(512);
        while (true) {
            byteBuffer.clear(); // 如果注释掉该行代码会发生什么情况？
            // buffer从文件中读取数据
            int read = inputChannel.read(byteBuffer);
            System.out.println("read: " + read);
            // 如果文件读取完毕跳出循环
            if (-1 == read) {
                break;
            }
            // 读写状态反转
            byteBuffer.flip();
            // 将buffer中的数据写出到文件中
            outputChannel.write(byteBuffer);
        }
        inputChannel.close();
        outputChannel.close();
    }
}
