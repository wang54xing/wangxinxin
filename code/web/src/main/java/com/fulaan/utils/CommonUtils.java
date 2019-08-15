package com.fulaan.utils;

import com.fulaan.utils.pojo.KeyValue;

import java.util.*;

/**
 * Created by guojing on 2015/4/10.
 */
public class CommonUtils {

    /**
     * 把Map处理成List<KeyValue>
     * @param map
     * @return List<KeyValue>
     */
    public static List<KeyValue> enumMapToList(Map<Integer, String> map){
        List<KeyValue> list=new ArrayList<KeyValue>();
        Set<Map.Entry<Integer, String>> set = map.entrySet();
        for (Iterator<Map.Entry<Integer, String>> it = set.iterator(); it.hasNext();) {
            KeyValue keyValue = new KeyValue();
            Map.Entry<Integer, String> entry = (Map.Entry<Integer, String>) it.next();
            keyValue.setKey(entry.getKey());
            keyValue.setValue(entry.getValue());
            list.add(keyValue);
        }
        return list;
    }
}
