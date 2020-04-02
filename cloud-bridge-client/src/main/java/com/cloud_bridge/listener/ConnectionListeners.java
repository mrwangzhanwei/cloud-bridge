package com.cloud_bridge.listener;

import com.cloud_bridge.client.SubRecorder;
import com.cloud_bridge.client.SubRecorders;
import com.cloud_bridge.client.TCPClients;
import com.cloud_bridge.halder.ChannelHolder;
import com.cloud_bridge.handler.ClientChannelHolder;
import com.cloud_bridge.model.FuncodeEnum;
import com.cloud_bridge.model.LastLoginRecord;
import com.cloud_bridge.model.LastLoginRecords;
import com.cloud_bridge.model.Message;
import com.cloud_bridge.utils.DateUtil;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoop;
import jodd.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

import io.netty.channel.Channel;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author 王战伟
 * @email wangzhanwei@lumlord.com
 * @date 2020/4/2  15:34
 */
@Slf4j
public class ConnectionListeners implements ChannelFutureListener {

    private String key;

    private TCPClients clients = null;

    public ConnectionListeners(String key,TCPClients tcpClients){
        this.key = key;
        this.clients = tcpClients;
    }

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
                        clients.start();
                    }else{
                        clients.stop();
                        log.error("【重连警告】已超过最大重连次数，程序关闭");
                    }
                },dalayTime, TimeUnit.SECONDS);
                dalayTime=dalayTime<<1;//重连次数越多，延迟时间越长
            }
        }else{
            log.info(key + " 【客户端状态】STATUS=ACTIVE,TIME="+ DateUtil.getCurrentDateTime());
            ClientChannelHolder.setMAP(key,future.channel());
            //判断上次是否登陆
            LastLoginRecords lastLoginRecords = ClientChannelHolder.getLastLoginRecords(key);
            if(null != lastLoginRecords){
                //向server发送认证凭证
                log.info("发送登陆凭证");
                future.channel().writeAndFlush(new Message(FuncodeEnum.AUTH_USER,(byte)0 , null, lastLoginRecords.getLastToken().getBytes().length, LastLoginRecord.INSTANCE().getLastToken().getBytes()));
            }
            SubRecorders subRecorders = ChannelHolder.getSubRecordersMap(key);
            if (null == subRecorders){
                subRecorders = new SubRecorders();
                ChannelHolder.setSubRecordersMap(key,subRecorders);
            }
            subRecorders.recover();
            //若重连成功恢复重连间隔

            dalayTime=1;
            retryCount.set(0);
        }

    }



}
