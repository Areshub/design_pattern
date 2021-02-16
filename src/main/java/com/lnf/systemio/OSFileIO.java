package com.lnf.systemio;

import org.junit.Test;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;

public class OSFileIO {
    static String path ="F:/test/test.txt";
    static byte[] data ="123456789\n".getBytes();
    public static void main(String[] args) throws IOException {
        File f = new File("F:/text");
        f.mkdirs();
        f = new File("F:/text/test.txt");
        f.createNewFile();
        FileOutputStream fileOutputStream = new FileOutputStream(f);
        for (int i = 0; i < 10; i++) {
            fileOutputStream.write("123456789\n".getBytes());
        }

    }

    // 最基本的file写
    @Test
    public static void testBasicFileIO() throws IOException, InterruptedException {
        File file = new File(path);
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        while (true){
            TimeUnit.SECONDS.sleep(1);
            fileOutputStream.write(data);
        }
    }


    // Bufferfile写
    @Test
    public static void testBufferFileIO() throws IOException, InterruptedException {
        File file = new File(path);
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
        while (true){
            TimeUnit.SECONDS.sleep(1);
            bufferedOutputStream.write(data);
        }
    }


    // Bufferfile写
    @Test
    public static void whatByteBuffer(){
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        System.out.println("postition:  "+buffer.position());
        System.out.println("limit:  "+buffer.limit());
        System.out.println("capacity  "+buffer.capacity());
        System.out.println("mark    "  +buffer);

        buffer.put("123".getBytes());

        System.out.println("--------------put:123---------------");
        System.out.println("mark    "  +buffer);
    }
}
