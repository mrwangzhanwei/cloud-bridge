package com.cloud_bridge;

import com.cloud_bridge.handler.BusinessHandler;
import com.cloud_bridge.server.impl.HttpServer;
import com.cloud_bridge.server.impl.TCPServer;
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
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 启动类
 * @author 王战伟
 * @email wangzhanwei@lumlord.com
 * @date 2020/3/24  14:40
 */
public class Main {


    public static void main(String[] args) {
        Main main = new Main();
        main.run();
    }

    /**
     * 创建线程池
     */
    private final ExecutorService executorService= Executors.newFixedThreadPool(2);
    /**
     * 使用线程池启动
     */
    private void run(){

        executorService.submit(()->{
            TCPServer.INSTANCE.start();
        });
        executorService.submit(()->{
            HttpServer.INSTANCE.start();
        });
    }

    /**
     * 启动
     */
    public static void start(){
        Main main = new Main();
        int prot = 5656;
        main.run(prot);
//        if (args.length>0 && args[0].matches("\\d{4}"))
//            prot = Integer.parseInt(args[0]);
    }

    /**
     * 初始启动方法
     * @param prot
     */
    public void run(int prot){
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInit())  // netty 初始化类
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true);
        try {
            ChannelFuture f = bootstrap.bind(prot).sync();
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
    /**
     * 静态内部类,channel 初始化
     */
    static class ChannelInit extends ChannelInitializer<SocketChannel> {
        @Override
        protected void initChannel(SocketChannel socketChannel) throws Exception {
            socketChannel.pipeline().addLast(new IdleStateHandler(10, 0, 0, TimeUnit.SECONDS));
            ByteBuf delimiter = Unpooled.copiedBuffer("\r\n".getBytes()); // 设置 传输分割符  防止粘包
            socketChannel.pipeline().addLast(new DelimiterBasedFrameDecoder(1024*10,delimiter));
            socketChannel.pipeline().addLast(new StringEncoder());
            socketChannel.pipeline().addLast(new StringDecoder());
//            socketChannel.pipeline().addLast(new LineBasedFrameDecoder(1024));  定长传输
            socketChannel.pipeline().addLast("handler",new BusinessHandler()); // 业务处理类
        }
    }
}
