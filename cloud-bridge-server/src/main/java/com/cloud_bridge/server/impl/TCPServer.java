package com.cloud_bridge.server.impl;

import com.cloud_bridge.handler.ChanelInitializerHandler;
import com.cloud_bridge.server.Server;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.log4j.Logger;


/**
 * @author 王战伟
 * @email wangzhanwei@lumlord.com
 * @date 2020/3/24  19:16
 */
public class TCPServer implements Server{
    EventLoopGroup bossGroup = new NioEventLoopGroup();
    EventLoopGroup workerGroup = new NioEventLoopGroup();

    public  final static TCPServer INSTANCE=new TCPServer();


    private final Logger log= Logger.getLogger(TCPServer.class);

    public void start() {
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(workerGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChanelInitializerHandler())
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true);

    }

    public void stop() {

    }
}
