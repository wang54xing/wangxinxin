package com.pojo.school;

import org.bson.types.ObjectId;

public class InterestClassLessonScore {
    private String id;

    private String classid;

    private String userid;

    private String studentAvatar;

    private int lessonindex;

    private Integer stuscore;

    private String studentName;
    
    private String coursetype;

    private int attendance;
    private String teacherComment;
    private String pictureUrl;
    private String lessonName;

    public InterestClassLessonScore(){}
    public InterestClassLessonScore(InterestClassLessonScoreEntry lessonScoreEntry) {
        this.id=lessonScoreEntry.getID()==null?"":lessonScoreEntry.getID().toString();
        this.classid=lessonScoreEntry.getClassId()==null?"":lessonScoreEntry.getClassId().toString();
        this.userid=lessonScoreEntry.getUserId()==null?"":lessonScoreEntry.getUserId().toString();
        this.lessonindex=lessonScoreEntry.getLessonIndex();
        this.stuscore=lessonScoreEntry.getStudentScore();
//        this.stuscore=1;
        this.attendance = lessonScoreEntry.getAttendance();
        this.teacherComment = lessonScoreEntry.getTeacherComment();
        this.pictureUrl = lessonScoreEntry.getLessonPictureUrl();
        this.lessonName = lessonScoreEntry.getLessonName();
    }

    public String getStudentAvatar() {
        return studentAvatar;
    }
    public void setStudentAvatar(String studentAvatar) {
        this.studentAvatar = studentAvatar;
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

    public int getLessonindex() {
        return lessonindex;
    }

    public void setLessonindex(int lessonindex) {
        this.lessonindex = lessonindex;
    }

    public Integer getStuscore() {
        return stuscore;
    }

    public void setStuscore(Integer stuscore) {
        this.stuscore = stuscore;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

	public String getCoursetype() {
		return coursetype;
	}

	public void setCoursetype(String coursetype) {
		this.coursetype = coursetype;
	}

    public int getAttendance() {
        return attendance;
    }

    public void setAttendance(int attendance) {
        this.attendance = attendance;
    }

    public String getTeacherComment() {
        return teacherComment;
    }

    public void setTeacherComment(String teacherComment) {
        this.teacherComment = teacherComment;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String rpu) {
        this.pictureUrl = rpu;
    }

    public String getLessonName() {
        return lessonName;
    }

    public void setLessonName(String lessonName) {
        this.lessonName = lessonName;
    }

    public InterestClassLessonScoreEntry exportEntry() {
        InterestClassLessonScoreEntry lessonScoreEntry=new InterestClassLessonScoreEntry();
        lessonScoreEntry.setClassId(new ObjectId(this.classid));
        lessonScoreEntry.setLessonIndex(this.lessonindex);
        lessonScoreEntry.setStudentScore(this.stuscore);
        lessonScoreEntry.setUserId(new ObjectId(this.userid));
        lessonScoreEntry.setLessonPictureUrl(this.pictureUrl);
        lessonScoreEntry.setTeacherComment(this.teacherComment);
        lessonScoreEntry.setAttendance(this.attendance);
        lessonScoreEntry.setLessonName(this.lessonName);
        return lessonScoreEntry;
    }
}