package com.cloud_bridge.model;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author 王战伟
 * @email wangzhanwei@lumlord.com
 * @date 2020/3/25  10:01
 */
public class MessageEncoder extends MessageToByteEncoder<Message> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception {
        out.writeByte(msg.getBODY_HEAD());
        out.writeByte(msg.getFunCode().getCode());
        out.writeByte(msg.getIsHaveTopic());
        if(msg.getIsHaveTopic()==1){
            out.writeInt(msg.getBodyLength()+12);
            out.writeBytes(msg.getTopic());
        }else{
            out.writeInt(msg.getBodyLength());
        }
        out.writeBytes(msg.getData());
    }
}
