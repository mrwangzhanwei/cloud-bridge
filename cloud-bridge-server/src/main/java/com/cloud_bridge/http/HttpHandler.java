package com.cloud_bridge.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cloud_bridge.castable.TopicManager;
import com.cloud_bridge.utils.MD5Util;
import com.cloud_bridge.utils.UrlUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.io.UnsupportedEncodingException;

/**
 * @author 王战伟
 * @email wangzhanwei@lumlord.com
 * @date 2020/4/2  9:41
 */
public class HttpHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private final static TopicManager topicManager=new TopicManager(TopicManager.TopicHolderType.MemoryTopicHolder);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
        try {
            ByteBuf content = msg.content();
            byte[] bts = new byte[content.readableBytes()];
            content.readBytes(bts);
            String result = null;
            if(msg.method() == HttpMethod.GET) {
                String url = msg.uri().toString();
                result = JSON.toJSONString(UrlUtil.parse(url).params);
                doGet(result);

            }else if(msg.method() == HttpMethod.POST) {
                //result = "post method and paramters is "+ new String(bts);
                doPost(new String(bts,"utf-8"));
            }
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
            response.headers().set("content-Type","text/html;charset=UTF-8");
            StringBuilder sb = new StringBuilder();
            sb.append("OK");
            ByteBuf responseBuf = Unpooled.copiedBuffer(sb, CharsetUtil.UTF_8);
            response.content().writeBytes(responseBuf);
            responseBuf.release();
            ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void doGet(String jsonStr){
        doPost(jsonStr);
    }


    private void doPost(String jsonStr){
        JSONObject res = JSON.parseObject(jsonStr);
        String topic = res.getString("topic");
        String data=res.getString("data");
        System.out.println("topic->"+topic+" data->"+data);
        topic= MD5Util.getPwd(topic).substring(0, 12);//加密topic
        //发布消息
        try {
            topicManager.publish(topic.getBytes(),data.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
