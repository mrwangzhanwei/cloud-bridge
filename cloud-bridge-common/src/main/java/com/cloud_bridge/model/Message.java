package com.cloud_bridge.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author 王战伟
 * @email wangzhanwei@lumlord.com
 * @date 2020/3/25  9:09
 */
@Data
public class Message implements Serializable,Cloneable{
    private String id;

    private final byte BODY_HEAD=(byte) 0xA8; //固定头部字段

    private FuncodeEnum funCode; //功能码

    private byte isHaveTopic; //是否包含12字节定长主题字段  0 不包含  1包含

    private byte[] topic;  //固定12字节长度主题

    private int bodyLength; //body数据长度

    private byte[] data;  //包体数据

    public Message(FuncodeEnum funCode, byte isHaveTopic, byte[] topic,int length, byte[] data) {
        this.funCode = funCode;
        this.isHaveTopic = isHaveTopic;
        this.topic = topic;
        this.data = data;
        this.bodyLength=length;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }


}
