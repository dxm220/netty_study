package pers.dxm.nio;

import java.nio.ByteBuffer;

/**
 * Created by douxm on 2019\3\27 0027.
 */
public class NioTest6 {
    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(10);
        // 构造老buffer
        for (int i = 0; i < buffer.capacity(); ++i) {
            buffer.put((byte) i);
        }
        // 指定新buffer的截取区间
        buffer.position(2);
        buffer.limit(6);
        // 截取新buffer
        ByteBuffer sliceBuffer = buffer.slice();
        // 在新buffer中对数据进行修改
        for (int i = 0; i < sliceBuffer.capacity(); ++i) {
            byte b = sliceBuffer.get(i);
            b *= 2;
            sliceBuffer.put(i, b);
        }
        // 重新定位老buffer的position和limit，以便对老buffer进行从头读取
        buffer.position(0);
        buffer.limit(buffer.capacity());
        while (buffer.hasRemaining()) {
            System.out.println(buffer.get());
        }
    }
}
