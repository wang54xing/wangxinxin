package com.fulaan.myclass.controller;

import java.util.List;

/**
 * Created by Hao on 2015/4/16.
 * 老师view层对象
 */
public class TeacherInfoView {
    private String id;
    private String userName;
    private int role;
    private String imageUrl;
    private List<String> subjectNameList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<String> getSubjectNameList() {
        return subjectNameList;
    }

    public void setSubjectNameList(List<String> subjectNameList) {
        this.subjectNameList = subjectNameList;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }
}
