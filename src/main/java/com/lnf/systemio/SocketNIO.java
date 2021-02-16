package com.lnf.systemio;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class SocketNIO {
    public static void main(String[] args) throws Exception {
        List<SocketChannel> clients = new LinkedList<>();
        ServerSocketChannel ss = ServerSocketChannel.open();
        ss.bind(new InetSocketAddress(9090));
        ss.configureBlocking(false); // 重点  OS  NONBLOCKING

/*
        ss.setOption(StandardSocketOptions.TCP_NODELAY,false);
*/
        while (true){
            
            TimeUnit.SECONDS.sleep(1);
            SocketChannel client = ss.accept();
            // accept 调用内核了 1 没有客户端连接进来，返回值？再BIO的时候一直阻塞着，但是在NIO，不阻塞，返回 -1
            // 如果来了客服端的连接 ，accept 返回的是这个客户端的fd 5 client object
            if(client==null){
                System.out.println("null_________");
            }else {
                client.configureBlocking(false); // 重点 socket (服务端的listen socket<连接请求三次握手后，往我这里扔，我去通过accept 得到连接的socket>,连接 socket<连接后的数据数据读写>)
                int port = client.socket().getPort();
                System.out.println("client....port"+port);
                clients.add(client);
            }
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4096);

            // 遍历已经连接进来的客户端能不能读写数据
            for (SocketChannel c: clients) { // 串行化 ！！！！  多线程
                int num = c.read(byteBuffer);
                if(num>0){
                    byteBuffer.flip();
                    byte[] aaa= new byte[byteBuffer.limit()];
                    byteBuffer.get(aaa);
                    String b = new String(aaa);
                    System.out.println(c.socket().getPort()+"  :  "+b);
                    byteBuffer.clear();
                }
            }
        }
    }
}
