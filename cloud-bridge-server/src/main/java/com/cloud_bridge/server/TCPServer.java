package com.cloud_bridge.server;

import com.cloud_bridge.conf.PropertyConfigFactory;
import com.cloud_bridge.handler.ChanelInitializerHandler;
import com.cloud_bridge.interfaces.server.Server;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import jodd.util.StringUtil;
import lombok.extern.slf4j.Slf4j;


/**
 * @author 王战伟
 * @email wangzhanwei@lumlord.com
 * @date 2020/3/24  19:16
 */
@Slf4j
public class TCPServer implements Server {
    EventLoopGroup bossGroup = new NioEventLoopGroup();
    EventLoopGroup workerGroup = new NioEventLoopGroup();

    private PropertyConfigFactory config=new PropertyConfigFactory();

    public  final static TCPServer INSTANCE=new TCPServer();

    public void start() {
        log.info("服务端启动");
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(workerGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChanelInitializerHandler())//添加处理类
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true);
        try {
            ChannelFuture channelFuture;
            if(!StringUtil.isEmpty(config.getConfig().getHost())){
                channelFuture=bootstrap.bind(config.getConfig().getHost(),Integer.parseInt(config.getConfig().getPort())).sync();
            }else{
                channelFuture = bootstrap.bind(Integer.parseInt(config.getConfig().getPort())).sync();
            }
            channelFuture.addListener(new GenericFutureListener<Future<? super Void>>() {
                @Override
                public void operationComplete(Future<? super Void> future) throws Exception {
                    if(future.isSuccess()||future.isDone()){
                        log.info("【server已启动】ip->"+config.getConfig().getHost()+" port->"+config.getConfig().getPort());
                    }else{
                        log.info("【server】启动失败");
                    }
                }
            });
//            //判断是否开启集群
//            if(config.getConfig().getEnableCluster()){
//                //若开启集群模式，则启动注册zookeeper
//                ServerConfig serverConfig = config.getConfig();
//                ZkRegister.getInstance().register(serverConfig.getZkRootPath()+"/broker_",serverConfig.getHost()+":"+serverConfig.getPort());
//            }
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            stop();
        }

    }

    public void stop() {
        if(workerGroup!=null){
            workerGroup.shutdownGracefully();
        }
        if(bossGroup!=null){
            bossGroup.shutdownGracefully();
        }
        log.info("【TCP-server】关闭成功");
    }
}
