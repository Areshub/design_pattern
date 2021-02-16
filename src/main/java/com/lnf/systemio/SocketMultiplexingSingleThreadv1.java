package com.lnf.systemio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class SocketMultiplexingSingleThreadv1 {
    private ServerSocketChannel server = null;
    //  linux 多路复用器 (select poll epoll) nginx event{}
    private Selector selector = null;
    int port = 9090;

    private void initServer() {
        try {
            server = ServerSocketChannel.open();
            server.configureBlocking(false);
            server.bind(new InetSocketAddress(port));
            // 如果在epoll模型下 open--> epoll_create
            selector = Selector.open();
            // sever 约等于listen状态的 fd
            /**
             * 如果select poll jvm 里开辟一个数组fd 放进去
             * epoll epoll_ctl(fd3)
             *
             */
            server.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        initServer();
        System.out.println("服务器启动了。。。。。。。");
        try {
            while (true) {
                // 死循环
                Set<SelectionKey> keys = selector.keys();
                System.out.println(keys.size() + "  size");
                // 1、调用多路复用器（select ,poll or epoll(epoll_wait)）
                // 没有时间阻塞 selector.wakeup()
                while (selector.select(500) > 0) {
                    // 返回有状态的fd 结果集
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = selectionKeys.iterator();
                    // so 管你什么多路复用器，你只能给我状态，我还得一个个的处理他们的r/w 同步好辛苦
                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();
                        iterator.remove();
                        if (key.isAcceptable()) {
                            // 如果接受了一个新的连接 语义上 accept 接受了连接并且返回新的fd
                            // select poll 因为内核没有空间，那么jvm 中保存和前边的数组中
                            // epoll 我们希望通过epoll_ctl把新的注册进去
                            acceptHandler(key);
                        } else if (key.isReadable()) {
                            // 连读带写
                            readHandler(key); // 当前线程,这个方法可能是阻塞
                            // 所以为什么提出了IO THREADS
                            // redis 是不是用了epoll redis是不是有个io threads 的概念 redis 是不是单线程的
                            // tomcat 8,9   异步的处理方式  IO和 处理上 解耦

                        }
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void acceptHandler(SelectionKey key) {
        try {
            ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
            SocketChannel client = ssc.accept();
            client.configureBlocking(false);

            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024);

            client.register(selector, SelectionKey.OP_READ, byteBuffer);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void readHandler(SelectionKey key) {
        SocketChannel client = (SocketChannel) key.channel();
        ByteBuffer buffer = (ByteBuffer) key.attachment();
        buffer.clear();
        int read =0;
        try {
            while (true){
                // 基于时间，有事才读，没事不瞎看
                read = client.read(buffer);
                if(read>0){
                    buffer.flip();
                    while (buffer.hasRemaining()){
                        client.write(buffer);
                    }
                    buffer.clear();
                }else if(read==0){
                    break;
                }else {
                    client.close();
                    break;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new SocketMultiplexingSingleThreadv1().start();
    }
}
