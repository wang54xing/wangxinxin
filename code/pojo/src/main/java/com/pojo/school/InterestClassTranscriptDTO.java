package com.pojo.school;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;

public class InterestClassTranscriptDTO {
    private String id;

    private String classid;

    private String userid;

    private String resultspicsrc;

    private String teachercomments;

    private Integer semesterscore;

    private Integer finalresult;

    private int coursetype;

    private String attendance;
    private String userName;
    private String userAvatar;

    public InterestClassTranscriptDTO(){}

    public InterestClassTranscriptDTO(String classid, String userid){
        this.classid = classid;
        this.userid = userid;
        this.resultspicsrc = null;
        this.teachercomments = null;
        this.semesterscore = 0;
        this.finalresult = 0;
        this.attendance = "0/0";
    }

    public InterestClassTranscriptDTO(InterestClassTranscriptEntry transcriptEntry) {
        this.id=transcriptEntry.getID().toString();
        ObjectId userId=transcriptEntry.getUserId();
        if(userId!=null){
            this.userid=userId.toString();
        }
        ObjectId classId=transcriptEntry.getClassId();
        if(classId!=null){
            this.classid=classId.toString();
        }
        this.resultspicsrc=transcriptEntry.getResultPicUrl();
        this.teachercomments=transcriptEntry.getTeacherComment();
        this.semesterscore=transcriptEntry.getTotalLessonScore();
        this.finalresult=transcriptEntry.getFinalResult();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClassid() {
        return classid;
    }

    public void setClassid(String classid) {
        this.classid = classid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getResultspicsrc() {
        return resultspicsrc;
    }

    public void setResultspicsrc(String resultspicsrc) {
        this.resultspicsrc = resultspicsrc == null ? null : resultspicsrc.trim();
    }

    public String getTeachercomments() {
        return teachercomments;
    }

    public void setTeachercomments(String teachercomments) {
        this.teachercomments = teachercomments == null ? null : teachercomments.trim();
    }

    public Integer getSemesterscore() {
        return semesterscore;
    }

    public void setSemesterscore(Integer semesterscore) {
        this.semesterscore = semesterscore;
    }

    public Integer getFinalresult() {
        return finalresult;
    }

    public void setFinalresult(Integer finalresult) {
        this.finalresult = finalresult;
    }

    public InterestClassTranscriptEntry exportEntry() {
        InterestClassTranscriptEntry transcriptEntry=new InterestClassTranscriptEntry();
        if(!StringUtils.isBlank(id))
            transcriptEntry.setID(new ObjectId(this.id));
        transcriptEntry.setUserId(new ObjectId(this.userid));
        transcriptEntry.setClassId(new ObjectId(this.getClassid()));
        if(finalresult!=null){
            transcriptEntry.setFinalResult(this.finalresult);
        }else{
            transcriptEntry.setFinalResult(0);
        }

        transcriptEntry.setResultPictureUrl(this.resultspicsrc);
        transcriptEntry.setTeacherComment(this.teachercomments);
        if(semesterscore!=null){
            transcriptEntry.setTotalLessonScore(this.semesterscore);
        }else{
            transcriptEntry.setTotalLessonScore(0);
        }
        return transcriptEntry;
    }

    public int getCoursetype() {
        return coursetype;
    }

    public void setCoursetype(int coursetype) {
        this.coursetype = coursetype;
    }

    public String getAttendance() {
        return attendance;
    }

    public void setAttendance(String attendance) {
        this.attendance = attendance;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }
}