package com.fulaan.schoolbase.dto;

import org.bson.types.ObjectId;

/**
 * Created by wang_xinxin on 2017/1/18.
 */
public class ClassRoomDTO {

    private String id;

    private String schoolId;

    private String loopId;

    private String number;

    private int loop;

    private String type;

    private String typeName;

    private int count;

    private String remark;

    public ClassRoomDTO() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getLoopId() {
        return loopId;
    }

    public void setLoopId(String loopId) {
        this.loopId = loopId;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getLoop() {
        return loop;
    }

    public void setLoop(int loop) {
        this.loop = loop;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
