package com.cloud_bridge.halder;

import com.cloud_bridge.event.EventBus;
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
        EventBus.handler.sendMsg(msg, funCode);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        super.exceptionCaught(ctx, cause);
    }

}
