package com.just.im.netty.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.util.internal.PlatformDependent;

import java.util.concurrent.ConcurrentMap;

/**
 * Created by bruce on 2019/6/10.
 */
public class ServerHandler implements ChannelInboundHandler {

    //存放客户端和服务端之间的连接
    private static ConcurrentMap<String,ChannelHandlerContext> channelConcurrentMap = PlatformDependent.newConcurrentHashMap();


    @Override
    public void channelRegistered(ChannelHandlerContext channelHandlerContext) throws Exception {

    }

    @Override
    public void channelUnregistered(ChannelHandlerContext channelHandlerContext) throws Exception {

    }

    @Override
    public void channelActive(ChannelHandlerContext channelHandlerContext) throws Exception {
        //获取客户端的ip
        String hostString = ((SocketChannel)channelHandlerContext.channel()).remoteAddress().getHostString();
        //将客户端和服务端之间的连接存放在concurrentHashMap中
        channelConcurrentMap.put(hostString,channelHandlerContext);
    }

    @Override
    public void channelInactive(ChannelHandlerContext channelHandlerContext) throws Exception {

    }

    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        System.out.println("Server channelRead....");
        String messageString = o.toString();
        String[] messages = messageString.split("@");
        String message = messages[0];
        String targetHost = messages[1];
        System.out.println(channelHandlerContext.channel().remoteAddress()+"->Server :"+o.toString());
        ChannelHandlerContext targetChannelHandlerContext = channelConcurrentMap.get(targetHost);
        targetChannelHandlerContext.write(channelHandlerContext.channel().remoteAddress() + " say : " + message);
        targetChannelHandlerContext.flush();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext channelHandlerContext) throws Exception {

    }

    @Override
    public void userEventTriggered(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {

    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext channelHandlerContext) throws Exception {

    }

    @Override
    public void handlerAdded(ChannelHandlerContext channelHandlerContext) throws Exception {

    }

    @Override
    public void handlerRemoved(ChannelHandlerContext channelHandlerContext) throws Exception {

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable throwable) throws Exception {

    }
}
