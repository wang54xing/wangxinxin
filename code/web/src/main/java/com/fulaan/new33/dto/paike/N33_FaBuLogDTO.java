package com.fulaan.new33.dto.paike;

import com.pojo.new33.paike.N33_FaBuLogEntry;
import com.sys.utils.DateTimeUtils;

/**
 * Created by wang_xinxin on 2018/6/28.
 */
public class N33_FaBuLogDTO {

    private String time;

    private String week;

    private int stautus;

    public N33_FaBuLogDTO(){

    }

    public N33_FaBuLogDTO(N33_FaBuLogEntry entry) {
        this.time = DateTimeUtils.getLongToStrTime(entry.getTime(),DateTimeUtils.DATE_YYYY_MM_DD_HH_MM);
        this.week = entry.getStartTime()+"-"+entry.getEndTime();
        this.stautus = entry.getStatus();
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public int getStautus() {
        return stautus;
    }

    public void setStautus(int stautus) {
        this.stautus = stautus;
    }
}
