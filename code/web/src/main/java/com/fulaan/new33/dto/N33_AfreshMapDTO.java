package com.fulaan.new33.dto;

import com.pojo.new33.isolate.StudentEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2019-05-14.
 *
 * //临时分班对象
 *
 */
public class N33_AfreshMapDTO  {
    //末尾端扩充
    private int order;

    private int number;

    private int volume;

    private int swim;

    private int type;//分班类型  0  默认蛇形    1  插尖蛇形

    private String compose;

    //所属班级
    private List<N33_AfreshLinDTO> classList = new ArrayList<N33_AfreshLinDTO>();

    private List<StudentEntry> studentEntryList = new ArrayList<StudentEntry>();


    public List<StudentEntry> getStudentEntryList() {
        return studentEntryList;
    }

    public void setStudentEntryList(List<StudentEntry> studentEntryList) {
        this.studentEntryList = studentEntryList;
    }

    public List<N33_AfreshLinDTO> getClassList() {
        return classList;
    }

    public void setClassList(List<N33_AfreshLinDTO> classList) {
        this.classList = classList;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public int getSwim() {
        return swim;
    }

    public void setSwim(int swim) {
        this.swim = swim;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCompose() {
        return compose;
    }

    public void setCompose(String compose) {
        this.compose = compose;
    }
    @Override
    public String toString() {
        return "段：" +
                "序号=" + order +
                ", 班级数量=" + number +
                ", 班级容量=" + volume +
                ", 班级浮动=" + swim +
                ", 班级人数=" + number*volume+
                ", 分班策略=" + type +
                ",班级"+
                ", 组合='" + compose + '\'' +
                "\n";
    }
}
