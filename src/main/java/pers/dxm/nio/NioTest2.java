package pers.dxm.nio;

import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by douxm on 2019\3\26 0026.
 */
public class NioTest2 {
    public static void main(String[] args) throws Exception {
        FileInputStream fileInputStream = new FileInputStream("NioTest2.txt");
        // 初始化channel对象
        FileChannel fileChannel = fileInputStream.getChannel();
        // 初始化Buffer对象
        ByteBuffer byteBuffer = ByteBuffer.allocate(512);
        // channel从buffer中读取而不是直接通过channel读取源文件
        fileChannel.read(byteBuffer);
        // buffer状态反转
        byteBuffer.flip();
        // 从buffer中读取数据打印
        while (byteBuffer.remaining() > 0) {
            byte b = byteBuffer.get();
            System.out.println("Character: " + (char) b);
        }
        // 关闭流
        fileInputStream.close();
    }
}
