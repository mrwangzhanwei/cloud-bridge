package com.cloud_bridge.halder;

import com.cloud_bridge.client.TCPClient;
import com.cloud_bridge.model.FuncodeEnum;
import com.cloud_bridge.model.Message;
import com.cloud_bridge.utils.DateUtil;
import io.netty.channel.*;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * @author 王战伟
 * @email wangzhanwei@lumlord.com
 * @date 2020/3/31  9:58
 */
@ChannelHandler.Sharable
public class IdleStateTrigger extends ChannelInboundHandlerAdapter {


    private final static TCPClient CLIENT=TCPClient.INSTANCE;

    //private static  final ByteBuf HEARTBEATE= Unpooled.unreleasableBuffer(Unpooled.copiedBuffer("$FF$", CharsetUtil.UTF_8));
    private static final Message HEARTBEATE=new Message(FuncodeEnum.HEART_BEAT, (byte)0,null,"$FF$".getBytes().length , "$FF$".getBytes());
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof IdleStateEvent){
            IdleState state = ((IdleStateEvent) evt).state();
            switch (state){
                case READER_IDLE:
                    //触发事件发送心跳包
                    //System.out.println("发送心跳包\t"+ DateUtils.getCurrentDateTime());
                    //ctx.channel().writeAndFlush直接从out尾端最后一个handler出栈
                    //ctx.writeAndFlush 从当前链逐一到达尾端出栈
                    ChannelFuture channelFuture = ctx.channel().writeAndFlush(HEARTBEATE.clone());
                    channelFuture.addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
                    break;
            }
        }else{
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        if(!ctx.executor().isShuttingDown()){
            System.out.println("连接已断开尝试重连\t"+ DateUtil.getCurrentDateTime());
            super.channelInactive(ctx);
            ChannelHolder.setChannel(null);
            CLIENT.start();
        }else{
            System.out.println("系统正在关闭...");
        }
    }
}
