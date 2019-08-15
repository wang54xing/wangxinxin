package com.sql.oldDataPojo;

/**
 * Created by qinbo on 15/4/22.
 */
public class InterestClassTranscriptInfo {
    private int id;
    private int classId;
    private int userId;
    private String resultsPicSrc;
    private String teacherComments;
    private int semesterScore;
    private int finalResult;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getResultsPicSrc() {
        return resultsPicSrc;
    }

    public void setResultsPicSrc(String resultsPicSrc) {
        this.resultsPicSrc = resultsPicSrc;
    }

    public String getTeacherComments() {
        return teacherComments;
    }

    public void setTeacherComments(String teacherComments) {
        this.teacherComments = teacherComments;
    }

    public int getSemesterScore() {
        return semesterScore;
    }

    public void setSemesterScore(int semesterScore) {
        this.semesterScore = semesterScore;
    }

    public int getFinalResult() {
        return finalResult;
    }

    public void setFinalResult(int finalResult) {
        this.finalResult = finalResult;
    }
}
