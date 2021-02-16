package com.lnf.systemio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;

public class C10kClient {
    public static void main(String[] args) {
        LinkedList<SocketChannel> clients = new LinkedList<>();
        InetSocketAddress socketAddress = new InetSocketAddress("192.168.1.41", 9090);
        for (int i = 10000; i <125000 ; i++) {
            try {

                SocketChannel client2 = SocketChannel.open();
                client2.bind(new InetSocketAddress("192.168.1.41", i));
                client2.connect(socketAddress);
                boolean c2 = client2.isOpen();
                clients.add(client2);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("clients"+clients.size());
    }
}
