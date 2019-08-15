package com.fulaan.schoolbase.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guojing on 2016/12/30.
 */
public class GroupUsersDTO {
    private String groupId;
    private List<String> leaderIds = new ArrayList<String>();
    private List<String> teacherIds = new ArrayList<String>();
    private List<String> studentIds = new ArrayList<String>();
    private List<String> parentIds = new ArrayList<String>();
    private List<String> staffIds = new ArrayList<String>();
    private List<String> otherIds = new ArrayList<String>();

    public GroupUsersDTO(){

    }


    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public List<String> getLeaderIds() {
        return leaderIds;
    }

    public void setLeaderIds(List<String> leaderIds) {
        this.leaderIds = leaderIds;
    }

    public List<String> getTeacherIds() {
        return teacherIds;
    }

    public void setTeacherIds(List<String> teacherIds) {
        this.teacherIds = teacherIds;
    }

    public List<String> getStudentIds() {
        return studentIds;
    }

    public void setStudentIds(List<String> studentIds) {
        this.studentIds = studentIds;
    }

    public List<String> getParentIds() {
        return parentIds;
    }

    public void setParentIds(List<String> parentIds) {
        this.parentIds = parentIds;
    }

    public List<String> getStaffIds() {
        return staffIds;
    }

    public void setStaffIds(List<String> staffIds) {
        this.staffIds = staffIds;
    }

    public List<String> getOtherIds() {
        return otherIds;
    }

    public void setOtherIds(List<String> otherIds) {
        this.otherIds = otherIds;
    }


}
