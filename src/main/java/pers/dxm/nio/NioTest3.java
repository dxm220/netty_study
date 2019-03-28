package pers.dxm.nio;

import java.nio.IntBuffer;
import java.util.Random;

/**
 * Created by douxm on 2019\3\27 0027.
 */
public class NioTest3 {
    public static void main(String[] args) {
        IntBuffer buffer = IntBuffer.allocate(10);
        System.out.println("capacity: " + buffer.capacity());
        for (int i = 0; i < 5; i++) {
            Random random = new Random();
            int randomNumber = random.nextInt(20);
            buffer.put(randomNumber);
        }
        System.out.println("before flip limit: " + buffer.limit());
        buffer.flip();
        System.out.println("after flip limit: " + buffer.limit());
        System.out.println("enter while loop");
        while (buffer.hasRemaining()) {
            System.out.println("position: " + buffer.position());
            System.out.println("limit: " + buffer.limit());
            System.out.println("capacity: " + buffer.capacity());
            System.out.println(buffer.get());
        }
        System.out.println("--------------");
        buffer.flip();
        System.out.println("position: " + buffer.position());
        System.out.println("limit: " + buffer.limit());
        System.out.println("capacity: " + buffer.capacity());
    }
}