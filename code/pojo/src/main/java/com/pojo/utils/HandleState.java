package com.pojo.utils;

/**
 * Created by guojing on 2015/6/18.
 */
public enum HandleState {

    TREATED(1,"已处理"),
    UNTREATED(0,"未处理"),
    ;
    private int state;
    private String des;


    private HandleState(int state, String des) {
        this.state = state;
        this.des = des;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getDes() {
        return des;
    }
    public void setDes(String des) {
        this.des = des;
    }
}
