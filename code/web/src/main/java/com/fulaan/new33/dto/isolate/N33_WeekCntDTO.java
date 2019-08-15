package com.fulaan.new33.dto.isolate;

public class N33_WeekCntDTO {

    private String weekName;

    private int count;

    private int x;

    public N33_WeekCntDTO(String arg, int i,int x) {
        this.weekName = arg;
        this.count = i;
        this.x = x;
    }

    public String getWeekName() {
        return weekName;
    }

    public void setWeekName(String weekName) {
        this.weekName = weekName;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }
}
