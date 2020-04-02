package com.cloud_bridge.halder;

import com.cloud_bridge.event.MyEventBus;
import com.cloud_bridge.handler.ClientChannelHolder;
import com.cloud_bridge.model.FuncodeEnum;
import com.cloud_bridge.model.LastLoginRecords;
import com.cloud_bridge.model.Message;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 王战伟
 * @email wangzhanwei@lumlord.com
 * @date 2020/4/2  17:40
 */
@Slf4j
public class HandlerImpl extends Handler{
    private String key;
    public HandlerImpl(String key){
        this.key = key;
    }
    @Override
    public void onMsg(Message obj, FuncodeEnum funcodeEnum) {
        MyEventBus myEventBusMap = ChannelHolder.getMyEventBusMap(key);
        log.info(">>>>>>>>>>>>"+key);
        switch (funcodeEnum) {
            case NOTICE_AUTH_OK:
                //认证成功保存登陆凭证
                LastLoginRecords lastLoginRecords = new LastLoginRecords();
                lastLoginRecords.setToken(obj);
                ClientChannelHolder.setLastLoginRecords(key,lastLoginRecords);
                if(myEventBusMap.autuListener!=null)
                    myEventBusMap.autuListener.authOk(obj);
                break;
            case NOTICE_AUTH_FAIL:
                myEventBusMap.autuListener.authFail(obj);
                break;
            case NOTICE_SUBSCRIBE_OK:
                myEventBusMap.selectSubscribListener(obj).subOk(obj);
                break;
            case NOTICE_UNSUBSCRIBE_OK:
                myEventBusMap.selectSubscribListener(obj).unSubOk(obj);
                break;
            case MESSAGE_SEND:
                myEventBusMap.selectSubscribListener(obj).msgActive(obj);
                break;
            default:
                break;
        }
    }



}
