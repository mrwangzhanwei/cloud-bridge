package com.cloud_bridge.handler;

import com.cloud_bridge.castable.TopicManager;
import com.cloud_bridge.model.FuncodeEnum;
import com.cloud_bridge.model.Message;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 王战伟
 * @email wangzhanwei@lumlord.com
 * @date 2020/3/25  9:16
 */
@Slf4j
public class TopicManagerHandler extends SimpleChannelInboundHandler<Message> {

    private final static TopicManager topicManager=new TopicManager(TopicManager.TopicHolderType.MemoryTopicHolder);

    protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
        FuncodeEnum funCode = msg.getFunCode();
        switch (funCode) {
            case TOPIC_SUBSCRIBE:
                topicManager.subscribe(msg.getTopic(),ctx.channel());
                ctx.channel().writeAndFlush(new Message(FuncodeEnum.NOTICE_SUBSCRIBE_OK, (byte)1, msg.getTopic(),"SUBOK".getBytes().length , "SUBOK".getBytes()));
                log.info("【主题订阅】"+ctx.channel().remoteAddress().toString()+" topic-》"+new String(msg.getTopic(),"utf-8"));
                break;
            case TOPIC_UNSUBSCRIBE:
                topicManager.remove(msg.getTopic(), ctx.channel());
                ctx.channel().writeAndFlush(new Message(FuncodeEnum.NOTICE_UNSUBSCRIBE_OK, (byte)1, msg.getTopic(),"UNSUBOK".getBytes().length , "UNSUBOK".getBytes()));
                log.info("【主题取消订阅】"+ctx.channel().remoteAddress().toString()+" topic-》"+new String(msg.getTopic()));
                break;
            case MESSAGE_SEND:
                //LOGGER.info("【发布消息】"+ctx.channel().remoteAddress().toString()+" topic-》"+new String(msg.getTopic(),"utf-8")+" msg-》"+new String(msg.getData(),"utf-8"));
                topicManager.publish(msg.getTopic(),msg.getData());
                break;
            case MESSAGE_BROAD:
                //广播消息
                topicManager.brodcast(msg.getData());
                break;
        }
    }
}
