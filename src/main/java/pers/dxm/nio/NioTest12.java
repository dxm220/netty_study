package pers.dxm.nio;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

/**
 * Created by douxm on 2019\4\1 0001.
 */
public class NioTest12 {
    public static void main(String[] args) throws Exception {
        String inputFile = "NioTest13_In.txt";
        String outputFile = "NioTest13_Out.txt";
        RandomAccessFile inputRandomAccessFile = new RandomAccessFile(inputFile, "r");
        RandomAccessFile outputRandomAccessFile = new RandomAccessFile(outputFile, "rw");
        long inputLength = new File(inputFile).length();
        FileChannel inputFileChannel = inputRandomAccessFile.getChannel();
        FileChannel outputFileChannel = outputRandomAccessFile.getChannel();
        // 将文件全部映射到内存中进行操作
        MappedByteBuffer inputData = inputFileChannel.map(FileChannel.MapMode.READ_ONLY, 0, inputLength);
        // 定义编解码集
        Charset charset = Charset.forName("iso-8859-1");
        // 初始化编码器
        CharsetDecoder decoder = charset.newDecoder();
        // 初始化解码器
        CharsetEncoder encoder = charset.newEncoder();
        // 将buffer中的数据进行编码
        CharBuffer charBuffer = decoder.decode(inputData);
        // 将编码后的数据进行解码
        ByteBuffer outputData = encoder.encode(charBuffer);
        // 将解码后的数据写出到目的文件中
        outputFileChannel.write(outputData);
        inputRandomAccessFile.close();
        outputRandomAccessFile.close();
    }
}
