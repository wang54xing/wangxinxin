package com.fulaan.new33.dto;

import com.pojo.new33.isolate.StudentEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2019-05-14.
 */
public class N33_AfreshLinDTO {
    //冗余人数二次分配


    //实际数量
    private int number;
    //所属组合
    private int subjectType;   //类型   1  物理    2  历史

    private int mainLevel;    //  3  化    4  生   5  政    6 地

    private int level;    //  3  化    4  生   5  政    6  地
    //所属段
    private int order;
    //最大值
    private int min;
    //最小值
    private int max;
    // 0   默认蛇形    1  插尖蛇形
    private int type;

    private String stringList;  //  compare#number,compare#number

    private int index;


    //所有学生数
    private List<StudentEntry> studentEntryList = new ArrayList<StudentEntry>();


    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getStringList() {
        return stringList;
    }

    public void setStringList(String stringList) {
        this.stringList = stringList;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getSubjectType() {
        return subjectType;
    }

    public void setSubjectType(int subjectType) {
        this.subjectType = subjectType;
    }

    public int getMainLevel() {
        return mainLevel;
    }

    public void setMainLevel(int mainLevel) {
        this.mainLevel = mainLevel;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<StudentEntry> getStudentEntryList() {
        return studentEntryList;
    }

    public void setStudentEntryList(List<StudentEntry> studentEntryList) {
        this.studentEntryList = studentEntryList;
    }

    @Override
    public String toString() {
        return "N33_AfreshLinDTO{" +
                "实际数量=" + number +
                ", 主科目=" + subjectType +
                ", 副科目=" + mainLevel +
                ", 三科目=" + level +
                ", 序号=" + order +
                ", 最小值=" + min +
                ", 最大值=" + max +
                ", 类型=" + type +
                ", 组成占比='" + stringList + '\'' +
                ", studentEntryList=" + studentEntryList +
                '}';
    }
}
