package com.fulaan.utils.pojo;

/**
 * Created by guojing on 2015/4/9.
 */
public class KeyValue {
    private int key;
    private String value;
    public KeyValue(){

    }

    public KeyValue(int key, String value){
        this.key=key;
        this.value=value;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
