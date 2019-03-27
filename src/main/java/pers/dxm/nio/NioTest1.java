package pers.dxm.nio;

import java.nio.IntBuffer;
import java.util.Random;

/**
 * Created by douxm on 2019\3\26 0026.
 */
public class NioTest1 {
    public static void main(String[] args) {
        // IntBuffer是nio中对int类型的封装，初始化时为其传分配大小
        IntBuffer buffer = IntBuffer.allocate(10);
        System.out.println("capacity: " + buffer.capacity());
        // 程序生成随机数，写入到buffer中
        for (int i = 0; i < buffer.capacity(); i++) {
            Random random = new Random();
            int randomNumber = random.nextInt(20);
            buffer.put(randomNumber);
        }
        // buffer状态反转，从输入变为输出
        buffer.flip();
        // 程序从buffer中获取数据并打印
        while (buffer.hasRemaining()) {
            System.out.println(buffer.get());
        }
    }
}
