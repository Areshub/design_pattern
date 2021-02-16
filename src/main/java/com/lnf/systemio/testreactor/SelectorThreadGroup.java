package com.lnf.systemio.testreactor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.Channel;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.util.concurrent.atomic.AtomicInteger;

public class SelectorThreadGroup {
    SelectorThread[] sts;
    ServerSocketChannel server=null;
    AtomicInteger xid = new AtomicInteger(0);

    SelectorThreadGroup(int num) {
        sts= new SelectorThread[num];
        for (int i = 0; i < num; i++) {
            sts[i] = new SelectorThread(this);
            new Thread(sts[i]).start();
        }
    }
    //
    public void bind(int port) {
        try {
            server = ServerSocketChannel.open();
            server.configureBlocking(false);
            server.bind(new InetSocketAddress(port));

            // 注册到哪个seletor?
            nextSelector(server);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void nextSelector(Channel c) {
        SelectorThread st = next(); // 再main 线程中，取到堆里的selectorThread对象
        // 1、通过队列传递数据 消息
        st.lbq.add(c);
        // 2、通过打断阻塞，让对应的线程去自己再打断后
        st.selector.wakeup();

/*        // 重点： c 有可能是server 有可能是client
        ServerSocketChannel s = (ServerSocketChannel) c;
        // 呼应上 int nums = selector.select() // 阻塞  wakeup()
        try {
            s.register(st.selector, SelectionKey.OP_ACCEPT);  // 会被阻塞的
            st.selector.wakeup();// 功能是让 selector ()方法立刻返回，不阻塞！

        } catch (ClosedChannelException e) {
            e.printStackTrace();
        }*/

    }


    // 无论serverSocker socket 都复用这个方法
    private SelectorThread next() {
        int index=xid.incrementAndGet() % sts.length;
        return sts[index];
    }
}
