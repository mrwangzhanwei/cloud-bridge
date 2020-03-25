package com.cloud_bridge.castable;

import com.cloud_bridge.model.FuncodeEnum;
import com.cloud_bridge.utils.MD5Util;

/**
 * @author 王战伟
 * @email wangzhanwei@lumlord.com
 * @date 2020/3/25  9:20
 */
public interface TopicBroadcastable {
    final byte [] BROAD_TOPIC= MD5Util.getPwd(FuncodeEnum.MESSAGE_BROAD.name().concat("$")).substring(0, 12).getBytes();

    void brodcast(byte[] data);
}
