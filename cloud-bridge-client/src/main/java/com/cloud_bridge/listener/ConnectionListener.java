package com.cloud_bridge.listener;

import com.cloud_bridge.client.SubRecorder;
import com.cloud_bridge.client.TCPClient;
import com.cloud_bridge.halder.ChannelHolder;
import com.cloud_bridge.model.FuncodeEnum;
import com.cloud_bridge.model.LastLoginRecord;
import com.cloud_bridge.model.Message;
import com.cloud_bridge.utils.DateUtil;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoop;
import jodd.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author 王战伟
 * @email wangzhanwei@lumlord.com
 * @date 2020/3/31  9:51
 */
@Slf4j
public class ConnectionListener implements ChannelFutureListener {

    private final TCPClient CLIENT=TCPClient.INSTANCE;

    private  static AtomicInteger retryCount=new AtomicInteger(0);

    private final int TRY_LIMITE=15; //重连次数最大限制

    private int dalayTime=1; //定时延期时间

    @Override
    public void operationComplete(ChannelFuture future) throws Exception {
        if(!future.isSuccess()){
            EventLoop eventExecutors = future.channel().eventLoop();
            future.channel().close();
            if(!eventExecutors.isShuttingDown()){
                eventExecutors.schedule(()->{
                    if(retryCount.get()<=TRY_LIMITE) {
                        log.error("【客户端状态】STATUS=failed,TIME="+ DateUtil.getCurrentDateTime()+",msg=正在尝试重连,retrys="+retryCount.getAndIncrement());
                        TCPClient.INSTANCE.start();
                    }else{
                        TCPClient.INSTANCE.stop();
                        log.error("【重连警告】已超过最大重连次数，程序关闭");
                    }
                },dalayTime, TimeUnit.SECONDS);
                dalayTime=dalayTime<<1;//重连次数越多，延迟时间越长
            }
        }else{
            log.info("【客户端状态】STATUS=ACTIVE,TIME="+ DateUtil.getCurrentDateTime());
            ChannelHolder.setChannel(future.channel());
           //判断上次是否登陆
           if(!StringUtil.isEmpty(LastLoginRecord.INSTANCE().getLastToken())){
               //向server发送认证凭证
               log.info("发送登陆凭证");
               future.channel().writeAndFlush(new Message(FuncodeEnum.AUTH_USER,(byte)0 , null, LastLoginRecord.INSTANCE().getLastToken().getBytes().length, LastLoginRecord.INSTANCE().getLastToken().getBytes()));
           }
            //若重连成功恢复重连间隔
            SubRecorder.recover();
            dalayTime=1;
            retryCount.set(0);
        }

    }



}
