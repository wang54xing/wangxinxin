package com.fulaan.utils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by wang_xinxin on 2017/1/3.
 */
public enum Platform {

    NOTICE(1,"通知"),
    FILE(2,"文件管理"),
    APPFILE(3,"互动课堂"),
    EXPAND(4,"拓展课"),
    EVALUATION(5,"教师评价"),
    ;
    private int type;
    private String name;


    private Platform(int type, String name) {
        this.type = type;
        this.name = name;
    }

    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @param type
     * @return
     */
    public static String getPlatformName(int type) {
        for (Platform thisEnum : Platform.values()) {
            if (thisEnum.getType()==type) {
                return thisEnum.getName();
            }
        }
        return "";
    }

    public static Map<Integer, String> getPlatformMap() {
        Map<Integer, String> map = new LinkedHashMap<Integer, String>();
        for (Platform thisEnum : Platform.values()) {
            map.put(thisEnum.getType(), thisEnum.getName());
        }
        return map;
    }
}
