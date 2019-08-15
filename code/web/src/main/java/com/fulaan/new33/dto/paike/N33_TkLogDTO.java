package com.fulaan.new33.dto.paike;

import com.pojo.new33.paike.N33_TkLogEntry;
import com.sys.utils.DateTimeUtils;

/**
 * Created by wang_xinxin on 2018/6/25.
 */
public class N33_TkLogDTO {
    private String id;

    private String teacherName;

    private String ykbId;

    private String keJie;

    private String newTeacherName;

    private String newKeJie;

    private String oYkbId;

    private String time;

    private int type;

    private String week;

    private int cimType;

    private String termId;

    private int xcty;

    private String jxbName;
    private String newJxbName;

    public String getJxbName() {
        return jxbName;
    }

    public void setJxbName(String jxbName) {
        this.jxbName = jxbName;
    }

    public String getNewJxbName() {
        return newJxbName;
    }

    public void setNewJxbName(String newJxbName) {
        this.newJxbName = newJxbName;
    }

    public N33_TkLogDTO() {}

    public N33_TkLogDTO(N33_TkLogEntry entry) {
        this.id = entry.getID().toString();
        this.ykbId = entry.getOYkbId()!=null?entry.getOYkbId().toString():"";
        this.oYkbId = entry.getNYkbId()!=null?entry.getNYkbId().toString():"";
        this.week = entry.getWeek();
        this.type = entry.getType();
        this.cimType = entry.getCimType();
        this.termId = entry.getTermId().toString();
        this.time = DateTimeUtils.getLongToStrTime(entry.getTime(),DateTimeUtils.DATE_YYYY_MM_DD_HH_MM);
        this.newKeJie = "";
        this.keJie = "";
        this.newTeacherName = "";
        this.xcty = entry.getXcty();
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getYkbId() {
        return ykbId;
    }

    public void setYkbId(String ykbId) {
        this.ykbId = ykbId;
    }

    public String getKeJie() {
        return keJie;
    }

    public void setKeJie(String keJie) {
        this.keJie = keJie;
    }

    public String getNewTeacherName() {
        return newTeacherName;
    }

    public void setNewTeacherName(String newTeacherName) {
        this.newTeacherName = newTeacherName;
    }

    public String getNewKeJie() {
        return newKeJie;
    }

    public void setNewKeJie(String newKeJie) {
        this.newKeJie = newKeJie;
    }

    public String getoYkbId() {
        return oYkbId;
    }

    public void setoYkbId(String oYkbId) {
        this.oYkbId = oYkbId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public int getCimType() {
        return cimType;
    }

    public void setCimType(int cimType) {
        this.cimType = cimType;
    }

    public String getTermId() {
        return termId;
    }

    public void setTermId(String termId) {
        this.termId = termId;
    }

    public int getXcty() {
        return xcty;
    }

    public void setXcty(int xcty) {
        this.xcty = xcty;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
