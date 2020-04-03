package com.cloud_bridge.server;

import com.cloud_bridge.conf.PropertyConfigFactory;
import com.cloud_bridge.http.HttpHandler;
import com.cloud_bridge.interfaces.server.Server;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 王战伟
 * @email wangzhanwei@lumlord.com
 * @date 2020/4/2  9:44
 */
@Slf4j
public class HttpServer implements Server {

    private EventLoopGroup bossGroup = new NioEventLoopGroup();
    private EventLoopGroup workerGroup = new NioEventLoopGroup();

    private PropertyConfigFactory config=new PropertyConfigFactory();

    public final static HttpServer INSTANCE=new HttpServer();

    @Override
    public void start() {

        Integer port = config.getConfig().getHttpProt();

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {

                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast("http-decoder",new HttpRequestDecoder());
                        ch.pipeline().addLast("http-aggregator",new HttpObjectAggregator(65535));//将多个消息转化成一个
                        ch.pipeline().addLast("http-encoder",new HttpResponseEncoder());
                        ch.pipeline().addLast("http-chunked",new ChunkedWriteHandler());//解决大码流的问题
                        ch.pipeline().addLast("http-server",new HttpHandler());
                    }
                });

        try {
            ChannelFuture channelFuture = bootstrap.bind(port).sync();
            channelFuture.addListener(new GenericFutureListener<Future<? super Void>>() {
                @Override
                public void operationComplete(Future<? super Void> future) throws Exception {

                    if(future.isSuccess()||future.isDone()){
                        log.info("【http-server已启动】 ip-> "+config.getConfig(null).getHost()+" port -> "+port);
                    }else{
                        log.info("【http-server】启动失败");
                    }

                }
            });
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }

    @Override
    public void stop() {
        if(workerGroup!=null){
            workerGroup.shutdownGracefully();
        }
        if(bossGroup!=null){
            bossGroup.shutdownGracefully();
        }

        log.info("【http-server】关闭成功");

    }

}
