package wy.netty.server;

import java.net.InetSocketAddress;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @Author: wangyu
 * @Description: 
 * @CodeReviewer: 
 */
public class EchoServer {
    
    private int port;
    
    public EchoServer(int port) {
        super();
        this.port = port;
    }

    public void start() throws InterruptedException {
        EventLoopGroup parentGroup = new NioEventLoopGroup();
        EventLoopGroup childGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(parentGroup, childGroup);
            serverBootstrap.localAddress(new InetSocketAddress(port));
            serverBootstrap.channel(NioServerSocketChannel.class);
            serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {

                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new EchoServerHandler());
                }
            });
            
            ChannelFuture channelFuture = serverBootstrap.bind().sync();
            System.out.println("EchoServer started, listen on port: " + channelFuture.channel().localAddress());
            channelFuture.channel().close();
        } finally {
            parentGroup.shutdownGracefully().sync();
            childGroup.shutdownGracefully().sync();
        }
    }

    public static void main(String[] args) {
        new EchoServer(8000);
    }
}
