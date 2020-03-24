package com.cloud_bridge.handler;

import com.cloud_bridge.exception.DataHeaderException;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * @author 王战伟
 * @email wangzhanwei@lumlord.com
 * @date 2020/3/24  19:27
 */
public class MessageToPoDecoder extends ReplayingDecoder<Void> {

    private final Logger log = Logger.getLogger(MessageToPoDecoder.class);

    private final byte BODY_HEAD=(byte) 0xA8;

    /**
     * 协议格式:
     *  1字节固定包头
     *  1字节功能码  1字节（判断是否包含topic字段）
     *  4字节固定长度字段
     *  12字节固定topic（非必须）
     *  剩余字节数据
     * @param ctx
     * @param byteBuf
     * @param list
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> list) throws Exception {
        log.info("服务器收到数据");
        // 读取固定头部信息
        byte b = byteBuf.readByte();
        if (!verifyHead(b)){
            log.error("服务器头部:"+b);
            ctx.channel().close();
            throw new DataHeaderException(b);
        }
        byte funCode = byteBuf.readByte();//读取功能码
        byte ishaveTopic = byteBuf.readByte(); //读取判断字段
        int bodyLength=byteBuf.readInt(); //读取固定数据长度字段
        byte[] topic=null;
        byte[] body=null;

    }

    /**
     * 校验头部信息
     * @param b
     * @return
     */
    private Boolean verifyHead(byte b){
        if (b == BODY_HEAD)
            return true;
        return false;
    }
}
