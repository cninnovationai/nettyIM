package com.just.im.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.Scanner;

/**
 * Created by bruce on 2019/6/10.
 */
public class Client {

    //duheng 's ip
    private static final String HOST = System.getProperty("host", "10.1.132.194");
    //port 8090
    private static final int PORT = Integer.parseInt(System.getProperty("port", "8090"));

    public static void main(String[] args) throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>(){
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();
                            p.addLast("decoder", new StringDecoder());
                            p.addLast("encoder", new StringEncoder());
                            p.addLast(new ClientHandler());
                        }
                    });
            ChannelFuture future = b.connect(HOST, PORT).sync();
            //控制台输入消息给服务端让服务端转给给另外一个客户端
            //消息如:  认识你真高兴我的小伙伴@10.1.8.30
            //消息就转发给了10.1.8.30
            Scanner sc = new Scanner(System.in);
            while(sc.hasNext()){
                String message = sc.nextLine();
                future.channel().writeAndFlush(message);
            }
            future.channel().closeFuture().sync();
        } finally {
            group.spliterator();
        }
    }

}
