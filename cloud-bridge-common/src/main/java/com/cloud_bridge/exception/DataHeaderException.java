package com.cloud_bridge.exception;

/**
 * @author 王战伟
 * @email wangzhanwei@lumlord.com
 * @date 2020/3/24  19:43
 */
public class DataHeaderException extends RuntimeException{
    public DataHeaderException(byte b) {
        super("非法头部数据->"+b);
    }
}
