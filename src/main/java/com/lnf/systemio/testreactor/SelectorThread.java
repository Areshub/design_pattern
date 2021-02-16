package com.lnf.systemio.testreactor;

import org.apache.catalina.Server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.LinkedBlockingDeque;

public class SelectorThread implements Runnable {
    // 每线程对应 一个selector,多线程情况下，该主机，该程序的并发客户端被分配到多个selector上
    // 注意每个客户端，只绑定到其中一个selector
    // 其实不会有交互问题
    Selector selector = null;
    LinkedBlockingDeque<Channel> lbq = new LinkedBlockingDeque<>();
    SelectorThreadGroup stg = null;

    public SelectorThread(SelectorThreadGroup stg) {
        try {
            this.stg = stg;
            this.selector = Selector.open();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        //Loop
        while (true) {
            try {
                // 1、select()
                System.out.println("before" + selector.keys().size());
                int nums = selector.select();      // 阻塞 wakeup()
                System.out.println("after" + selector.keys().size());

                // 2、处理selectkeys
                if (nums > 0) {
                    Set<SelectionKey> keys = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = keys.iterator();
                    while (iterator.hasNext()) {
                        // 线性处理
                        SelectionKey key = iterator.next();
                        iterator.remove();
                        if (key.isAcceptable()) {
                            // 复杂，接受客户端的过程（接受之后，要注册，多线程，新的客户端，注册到哪？）
                            acceptHandler(key);
                        } else if (key.isReadable()) {
                            readHandler(key);
                        } else if (key.isWritable()) {

                        }
                    }
                }
                // 3、处理一些task
                if (!lbq.isEmpty()) {
                    Channel c = lbq.take();
                    if (c instanceof ServerSocketChannel) {
                        ServerSocketChannel server = (ServerSocketChannel) c;
                        server.register(selector, SelectionKey.OP_ACCEPT);
                    } else if (c instanceof SocketChannel) {
                        SocketChannel client = (SocketChannel) c;
                        ByteBuffer buffer = ByteBuffer.allocateDirect(4096);
                        client.register(selector, SelectionKey.OP_READ, buffer);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private void readHandler(SelectionKey key) {
        ByteBuffer buffer = (ByteBuffer) key.attachment();
        SocketChannel client = (SocketChannel) key.channel();
        buffer.clear();
        while (true) {
            try {
                int num = client.read(buffer);
                if (num > 0) {
                    buffer.flip(); // 将读到的内容翻转，然后写出
                    while (buffer.hasRemaining()) {
                        client.write(buffer);
                    }
                    buffer.clear();
                } else if (num == 0) {
                    break;
                } else if (num < 0) {
                    // 客户端断开了
                    System.out.println("client： " + client.getRemoteAddress() + "closed.....");
                    key.cancel();
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void acceptHandler(SelectionKey key) {
        try {
            ServerSocketChannel server = (ServerSocketChannel) key.channel();
            SocketChannel client = server.accept();
            client.configureBlocking(false);
            //需要choose a selector and register!!
            stg.nextSelector(server);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
