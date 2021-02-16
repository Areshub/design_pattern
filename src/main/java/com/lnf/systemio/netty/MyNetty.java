package com.lnf.systemio.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.junit.Test;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class MyNetty {
    @Test
    public void myByteBuf() {
        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer(8, 20);

    }

    public static void print(ByteBuf buf) {

        System.out.println(buf.isReadable());
        System.out.println(buf.readerIndex());
        System.out.println(buf.readableBytes());

        System.out.println(buf.isWritable());
        System.out.println(buf.writerIndex());
        System.out.println(buf.writableBytes());

        System.out.println(buf.capacity());
        System.out.println(buf.maxCapacity());

        System.out.println(buf.isDirect());
    }

    @Test
    public void loopExecutor() {
        NioEventLoopGroup selector = new NioEventLoopGroup(1);
        selector.execute(() -> {
            while (true) {
                System.out.println("hello word!");
            }
        });
    }

    @Test
    public void clientMode() throws InterruptedException {
        NioEventLoopGroup selector = new NioEventLoopGroup(1);
        // 客户端模式
        NioSocketChannel client = new NioSocketChannel();
        selector.register(client);

        // 响应式
        ChannelPipeline p = client.pipeline();
        p.addLast(new MyInHandler());

        // reactor 异步的特征
        ChannelFuture connect = client.connect(new InetSocketAddress("192.168.1.41", 9090));
        ChannelFuture sync = connect.sync();
        ByteBuf buf = Unpooled.copiedBuffer("hello world".getBytes());

        ChannelFuture channelFuture = client.writeAndFlush(buf);
        channelFuture.sync();


        sync.channel().closeFuture().sync();
        System.out.println("client over........");
    }

    @Test
    public void serverMode() {

        NioEventLoopGroup selector = new NioEventLoopGroup(1);

        NioServerSocketChannel server = new NioServerSocketChannel();
        selector.register(server);
        ChannelPipeline p = server.pipeline();
        p.addLast(new MyAcceptHandler()); // 接受客户端 并注册到
        server.bind(new InetSocketAddress("192.168.1.41", 9090));
    }



}

class MyAcceptHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        SocketChannel client = (SocketChannel) msg;  // accept 我怎么没调用？
        //1、注册

        //2、响应式 handler
    }
}