package com.fulaan.schoolbase.dto;

import org.bson.types.ObjectId;

/**
 * Created by guojing on 2017/2/14.
 */
public class TeacherGroupSubjectDTO {
    private String id;
    private String teacherId;
    private String groupId;
    private String subjectId;

    public TeacherGroupSubjectDTO(){

    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

}
