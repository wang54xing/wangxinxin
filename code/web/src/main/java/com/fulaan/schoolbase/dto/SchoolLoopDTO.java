package com.fulaan.schoolbase.dto;

import org.bson.types.ObjectId;

/**
 * Created by wang_xinxin on 2017/1/18.
 */
public class SchoolLoopDTO {
    private String id;

    private String schoolId;

    private String number;

    private String name;

    private int count;

    public SchoolLoopDTO() {

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

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
