package com.cloud_bridge.halder;

import com.cloud_bridge.model.FuncodeEnum;
import com.cloud_bridge.model.Message;

/**
 * @author 王战伟
 * @email wangzhanwei@lumlord.com
 * @date 2020/3/31  10:11
 */
public abstract class Handler {


    public void sendMsg(Message obj, FuncodeEnum funcodeEnum){
        onMsg(obj, funcodeEnum);
    }


    public abstract void onMsg(Message obj,FuncodeEnum funcodeEnum);

}
