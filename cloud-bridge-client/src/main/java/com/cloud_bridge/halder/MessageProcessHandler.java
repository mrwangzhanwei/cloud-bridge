package com.cloud_bridge.halder;

import com.cloud_bridge.event.EventBus;
import com.cloud_bridge.event.MyEventBus;
import com.cloud_bridge.model.FuncodeEnum;
import com.cloud_bridge.model.Message;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author 王战伟
 * @email wangzhanwei@lumlord.com
 * @date 2020/3/31  10:02
 */
public class MessageProcessHandler extends SimpleChannelInboundHandler<Message> {


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
        FuncodeEnum funCode = msg.getFunCode();
        String key = ctx.channel().remoteAddress().toString().substring(1);
        HandlerImpl handler = new HandlerImpl(key);
        handler.sendMsg(msg, funCode);
//        ChannelHolder.getMyEventBusMap(key).
//        EventBus.handler.sendMsg(msg, funCode);

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        super.exceptionCaught(ctx, cause);
    }

}
