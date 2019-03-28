package pers.dxm.nio;

import java.nio.ByteBuffer;

/**
 * Created by douxm on 2019\3\28 0028.
 */
public class NioTest7 {
    public static void main(String[] args) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(10);
        System.out.println(byteBuffer.getClass());
        for (int i = 0; i < byteBuffer.capacity(); i++) {
            byteBuffer.put((byte) i);
        }
        ByteBuffer readonlyBuffer = byteBuffer.asReadOnlyBuffer();
        System.out.println(readonlyBuffer.getClass());
        System.out.println(readonlyBuffer.get(3));
        readonlyBuffer.put((byte) 10);
    }
}
