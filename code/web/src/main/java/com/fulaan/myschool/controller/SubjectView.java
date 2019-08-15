package com.fulaan.myschool.controller;

import com.pojo.school.Subject;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hao on 2015/4/23.
 */
public class SubjectView {
    private String id;
    private String name;
    private List<String> gradeIds;
    private String userName;
    private String userId;

    public  SubjectView(){}
    public SubjectView(Subject subject) {
        this.id=subject.getSubjectId().toString();
        this.name=subject.getName();
        List<ObjectId> oids=subject.getGradeIds();
        List<String> stringList=new ArrayList<String>();
        if(oids!=null){
            for(ObjectId objectId:oids){
                if(objectId!=null) {
                    stringList.add(objectId.toString());
                }
            }
        }
        this.gradeIds=stringList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getGradeIds() {
        return gradeIds;
    }

    public void setGradeIds(List<String> gradeIds) {
        this.gradeIds = gradeIds;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
