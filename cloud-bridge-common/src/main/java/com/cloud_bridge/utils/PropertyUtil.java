package com.cloud_bridge.utils;

import jodd.props.Props;
import jodd.props.PropsEntries;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * @author 王战伟
 * @email wangzhanwei@lumlord.com
 * @date 2020/3/25  10:05
 */
public class PropertyUtil {
    public static HashMap load() throws IOException {
        Props props = new Props();
        HashMap<String, String> hashMap = new HashMap<>();
        InputStream in=PropertyUtil.class.getClassLoader().getResourceAsStream("config.properties");
        props.load(in);
        PropsEntries entries = props.entries();
        entries.forEach((r)->{
            hashMap.put(r.getKey(), r.getValue());
        });
        return hashMap;

    }
}
