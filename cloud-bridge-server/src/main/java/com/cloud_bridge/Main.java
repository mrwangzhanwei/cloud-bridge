package com.cloud_bridge;

import ch.qos.logback.core.joran.spi.InterpretationContext;
import com.cloud_bridge.handler.BusinessHandler;
import com.cloud_bridge.http.HttpHandler;
import com.cloud_bridge.server.HttpServer;
import com.cloud_bridge.server.TCPServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.cloud_bridge.cluster.ConnectServers.connect;

/**
 * 启动类
 * @author 王战伟
 * @email wangzhanwei@lumlord.com
 * @date 2020/3/24  14:40
 */
@Slf4j
public class Main {


    public static void main(String[] args) {
        if (args.length > 0 && "client".equals(args[0])){
            connect();// 启动客户端
        }else {
            run();// 启动服务端
        }

    }

    /**
     * 创建线程池
     */
    private static final ExecutorService executorService= Executors.newFixedThreadPool(2);
    /**
     * 使用线程池启动
     */
    private static void run(){

        executorService.submit(()->{
            TCPServer.INSTANCE.start();
        });
        executorService.submit(()->{
            HttpServer.INSTANCE.start();
        });
    }

}
