package com.sql.oldDataPojo;

import java.io.Serializable;

/**
 * Created by qinbo on 15/3/23.
 */
public class RefParentStudent implements Serializable{

    private static final long serialVersionUID = 2440553167851215657L;
    private int parentId;
    private int studentId;

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }
}
