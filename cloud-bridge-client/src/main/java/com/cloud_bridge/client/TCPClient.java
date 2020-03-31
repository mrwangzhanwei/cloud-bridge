package com.cloud_bridge.client;

import com.cloud_bridge.conf.ServerConfig;
import com.cloud_bridge.halder.IdleStateTrigger;
import com.cloud_bridge.halder.MessageProcessHandler;
import com.cloud_bridge.listener.ConnectionListener;
import com.cloud_bridge.model.MessageEncoder;
import com.cloud_bridge.model.MessageToPoDecoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author 王战伟
 * @email wangzhanwei@lumlord.com
 * @date 2020/3/31  9:46
 */
@Slf4j
public class TCPClient implements Client{

    private NioEventLoopGroup loopGroup=null;

    private ServerConfig serverConfig=null;

    public final static TCPClient INSTANCE = new TCPClient();

    private final ConnectionListener connectionListener=new ConnectionListener();
    // 使用线程池启动
    private ExecutorService executorService= Executors.newSingleThreadExecutor();

    public TCPClient setConfig(ServerConfig serverConfig){
        this.serverConfig=serverConfig;
        return this;
    }
    @Override
    public void start() {
        executorService.submit(()->{
            try {
                loopGroup=new NioEventLoopGroup();
                Bootstrap bootstrap = new Bootstrap();
                bootstrap.group(loopGroup)
                        .channel(NioSocketChannel.class)
                        .option(ChannelOption.SO_KEEPALIVE,true)
                        .handler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel ch) throws Exception {
                                ChannelPipeline pipeline = ch.pipeline();
                                pipeline.addLast(new IdleStateHandler(30,0,0));
                                pipeline.addLast(new IdleStateTrigger());// 心跳处理类
                                //设计包的格式 1字节固定包头  1字节功能码  1字节（判断是否包含topic字段） 4字节固定长度字段   12字节固定topic（非必须）  剩余字节数据
                                pipeline.addLast(new LengthFieldBasedFrameDecoder(2048, 3, 4, 0, 0));
                                pipeline.addLast(new MessageToPoDecoder());
                                pipeline.addLast(new MessageProcessHandler());
                                pipeline.addLast(new MessageEncoder());
                            }
                        });
                //192.168.1.3
                ChannelFuture channel = bootstrap.connect(serverConfig.getHost(), Integer.parseInt(serverConfig.getPort()));
                channel.addListener(connectionListener);
                channel.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

    }

    @Override
    public void stop() {
        if(loopGroup!=null){
            loopGroup.shutdownGracefully();
            executorService.shutdown();
        }
    }

}
