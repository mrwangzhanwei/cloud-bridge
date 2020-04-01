package com.cloud_bridge.handler;

import com.cloud_bridge.castable.TopicManager;
import com.cloud_bridge.model.FuncodeEnum;
import com.cloud_bridge.model.Message;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author 王战伟
 * @email wangzhanwei@lumlord.com
 * @date 2020/3/25  9:14
 */
public class MessageProcessHandler extends SimpleChannelInboundHandler<Message> {

    protected void channelRead0(ChannelHandlerContext ctx, Message message) throws Exception {
        FuncodeEnum funCode = message.getFunCode();
        switch (funCode) {
            case HEART_BEAT:
                //若是心跳包则不处理
                message=null;
                break;
            case TOPIC_SUBSCRIBE:
            case TOPIC_UNSUBSCRIBE:
            case MESSAGE_SEND:
            case MESSAGE_BROAD:
                if(!isHaveChanelCheck(ctx, "topic-manage")){
                    ctx.pipeline().addLast("topic-manage",new TopicManagerHandler());
                }
                ctx.fireChannelRead(message);
                break;
            default:
                //其它情况则直接断开连接
                ctx.close();
                break;
        }

    }
    private boolean isHaveChanelCheck(ChannelHandlerContext ctx,String name){
        ChannelHandler channelHandler = ctx.pipeline().get(name);
        return channelHandler==null?false:true;
    }
}
