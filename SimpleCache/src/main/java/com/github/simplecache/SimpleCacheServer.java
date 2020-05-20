package com.github.simplecache;

import com.github.simplecache.handler.CommandDecoder;
import com.github.simplecache.handler.CommandHandler;
import com.github.simplecache.handler.FrameDecoder;
import com.github.simplecache.handler.ResponseHandler;
import com.github.simplecache.storage.Cache;
import com.github.simplecache.storage.MemoryCache;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

public class SimpleCacheServer {

    private int port;

    private Cache cache;

    public SimpleCacheServer(int port, Cache cache){
        this.port = port;
        this.cache = cache;
    }

    public void star() throws InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup();
        ServerBootstrap b = new ServerBootstrap();

        b.group(group)
                .channel(NioServerSocketChannel.class)
                .localAddress(new InetSocketAddress(port))
                .childHandler(new ChannelInitializer<SocketChannel>() { //7

                    @Override
                    public void initChannel(SocketChannel ch)
                            throws Exception {
                        SessionStatus status = new SessionStatus().ready();
                        ch.pipeline().addLast(
                                new FrameDecoder(status), new CommandDecoder(status), new CommandHandler(status, cache), new ResponseHandler());
                    }
                });
        ChannelFuture f = b.bind().sync();            //8
        f.channel().closeFuture().sync();
    }

}
