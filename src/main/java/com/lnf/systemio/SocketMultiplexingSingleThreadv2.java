package com.lnf.systemio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class SocketMultiplexingSingleThreadv2 {
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
//                            key.cancel();
                            readHandler(key); // 只处理了 read 并祖册，关心这个key 的write事件

                        } else if (key.isWritable()) {
                            // 写时间 <-- send-queue 只要是空的，就一定回个你返回可以写的时间，就会回调我们的写事件
                            // 1、你准备好要写什么，这是第一步
                            // 2、第二步你才关心send-queue是否有空间
                            // 3、so 读 redad 一开始就要注册，但是write 依赖以上关系，什么时候用什么时候调用
                            // 4、如果一开始就注册了write 的事件，进入死循环，一直调起！！！！
//                            key.cancel();
                            writeHandler(key);
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
        System.out.println("read handler......");
        SocketChannel client = (SocketChannel) key.channel();
        ByteBuffer buffer = (ByteBuffer) key.attachment();
        buffer.clear();
        int read = 0;
        try {
            while (true) {
                read = client.read(buffer);
                if (read > 0) {
                    // OP_WRITE 其实只关心 send-queue是不是有空间
                    client.register(key.selector(), SelectionKey.OP_WRITE, buffer);
                } else if (read == 0) {
                    break;
                } else {
                    client.close();
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writeHandler(SelectionKey key) {
        System.out.println("write handler....");
        SocketChannel client = (SocketChannel) key.channel();
        ByteBuffer buffer = (ByteBuffer) key.attachment();
        buffer.flip();
        while (buffer.hasRemaining()) {
            try {
                client.write(buffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        buffer.clear();
        key.cancel();
        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new SocketMultiplexingSingleThreadv2().start();
    }
}
