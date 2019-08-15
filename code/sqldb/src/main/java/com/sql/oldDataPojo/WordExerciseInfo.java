package com.sql.oldDataPojo;

import java.util.Date;

/**
 * Created by qinbo on 15/3/27.
 */
public class WordExerciseInfo {
    private Integer id;

    private String questwordpath;

    private String questpdfpath;

    private String questswfpath;

    private String practisename;

    private String answerwordpath;

    private String answerpdfpath;

    private String answerswfpath;

    private Integer lessionid;

    private Byte wordtype;

    private Integer exerciseTime;

    private Short totalScore;

    private Date uploadTime;

    private int isConfiging;

    private Integer userId;

    private Byte version;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getQuestwordpath() {
        return questwordpath;
    }

    public void setQuestwordpath(String questwordpath) {
        this.questwordpath = questwordpath;
    }

    public String getQuestpdfpath() {
        return questpdfpath;
    }

    public void setQuestpdfpath(String questpdfpath) {
        this.questpdfpath = questpdfpath;
    }

    public String getQuestswfpath() {
        return questswfpath;
    }

    public void setQuestswfpath(String questswfpath) {
        this.questswfpath = questswfpath;
    }

    public String getPractisename() {
        return practisename;
    }

    public void setPractisename(String practisename) {
        this.practisename = practisename;
    }

    public String getAnswerwordpath() {
        return answerwordpath;
    }

    public void setAnswerwordpath(String answerwordpath) {
        this.answerwordpath = answerwordpath;
    }

    public String getAnswerpdfpath() {
        return answerpdfpath;
    }

    public void setAnswerpdfpath(String answerpdfpath) {
        this.answerpdfpath = answerpdfpath;
    }

    public String getAnswerswfpath() {
        return answerswfpath;
    }

    public void setAnswerswfpath(String answerswfpath) {
        this.answerswfpath = answerswfpath;
    }

    public Integer getLessionid() {
        return lessionid;
    }

    public void setLessionid(Integer lessionid) {
        this.lessionid = lessionid;
    }

    public Byte getWordtype() {
        return wordtype;
    }

    public void setWordtype(Byte wordtype) {
        this.wordtype = wordtype;
    }

    public Integer getExerciseTime() {
        return exerciseTime;
    }

    public void setExerciseTime(Integer exerciseTime) {
        this.exerciseTime = exerciseTime;
    }

    public Short getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Short totalScore) {
        this.totalScore = totalScore;
    }

    public Date getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Date uploadTime) {
        this.uploadTime = uploadTime;
    }

    public int getIsConfiging() {
        return isConfiging;
    }

    public void setIsConfiging(int isConfiging) {
        this.isConfiging = isConfiging;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Byte getVersion() {
        return version;
    }

    public void setVersion(Byte version) {
        this.version = version;
    }
}
