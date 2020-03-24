package com.cloud_bridge.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import org.apache.log4j.Logger;
import utils.DateUtil;

/**
 * @author 王战伟
 * @email wangzhanwei@lumlord.com
 * @date 2020/3/24  19:50
 */
public class ChannelInboundHandler extends ChannelInboundHandlerAdapter {

    private final Logger LOGGER=Logger.getLogger(ChannelInboundHandler.class);


    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof IdleStateEvent){
            IdleStateEvent event= (IdleStateEvent) evt;
            switch (event.state()){
                case READER_IDLE:
                    //服务端触发事件，说明客户端已经掉线，关闭失效连接
                    LOGGER.error("【客户端已断开】"+ctx.channel().remoteAddress()+"  "+ DateUtil.getCurrentDateTime());
                    ctx.close();
                    break;
            }
        }
        super.userEventTriggered(ctx, evt);
    }

}
