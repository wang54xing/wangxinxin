package com.fulaan.myschool.controller;

import com.pojo.school.Grade;

/**
 * Created by Hao on 2015/4/22.
 */
public class GradeView {
    private String id;
    private String name;
    private String leader;
    private String cleader;
    private String leaderName;
    private String cleaderName;
    private int gradeType;


    public GradeView (){}
    public GradeView(Grade grade){
        this.id=grade.getGradeId().toString();
        this.name=grade.getName();
        this.leader=grade.getLeader()!=null?grade.getLeader().toString():null;
        this.cleader = grade.getCleader()!=null?grade.getCleader().toString():null;
        this.gradeType=grade.getGradeType();
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

    public String getLeader() {
        return leader;
    }

    public void setLeader(String leader) {
        this.leader = leader;
    }

    public int getGradeType() {
        return gradeType;
    }

    public void setGradeType(int gradeType) {
        this.gradeType = gradeType;
    }

    public String getLeaderName() {
        return leaderName;
    }

    public void setLeaderName(String leaderName) {
        this.leaderName = leaderName;
    }

    public String getCleader() {
        return cleader;
    }

    public void setCleader(String cleader) {
        this.cleader = cleader;
    }

    public String getCleaderName() {
        return cleaderName;
    }

    public void setCleaderName(String cleaderName) {
        this.cleaderName = cleaderName;
    }
}
