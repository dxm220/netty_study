package pers.dxm.nio;

import java.nio.ByteBuffer;

/**
 * Created by douxm on 2019\3\27 0027.
 */
public class NioTest5 {
    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(64);
        // 各种类型的put操作
        buffer.putInt(15);
        buffer.putLong(500000000L);
        buffer.putDouble(14.123456);
        buffer.putChar('你');
        buffer.putShort((short) 2);
        buffer.putChar('我');
        // 读写状态反转
        buffer.flip();
        // 各种类型的get操作
        System.out.println(buffer.getInt());
        System.out.println(buffer.getLong());
        System.out.println(buffer.getDouble());
        System.out.println(buffer.getChar());
        System.out.println(buffer.getShort());
        System.out.println(buffer.getChar());
    }
}
