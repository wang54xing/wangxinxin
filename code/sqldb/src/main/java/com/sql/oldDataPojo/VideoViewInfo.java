package com.sql.oldDataPojo;

import java.util.Date;

/**
 * Created by qinbo on 15/4/22.
 */
public class VideoViewInfo {

    private int id;
    private int courseId;
    private int userID;
    private String userName;
    private int teacherID;
    private Date playTime;
    private int playVideo;
    private int viewNum;
    private int endViewType;
    private int videoId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getTeacherID() {
        return teacherID;
    }

    public void setTeacherID(int teacherID) {
        this.teacherID = teacherID;
    }

    public Date getPlayTime() {
        return playTime;
    }

    public void setPlayTime(Date playTime) {
        this.playTime = playTime;
    }

    public int getPlayVideo() {
        return playVideo;
    }

    public void setPlayVideo(int playVideo) {
        this.playVideo = playVideo;
    }

    public int getViewNum() {
        return viewNum;
    }

    public void setViewNum(int viewNum) {
        this.viewNum = viewNum;
    }

    public int getEndViewType() {
        return endViewType;
    }

    public void setEndViewType(int endViewType) {
        this.endViewType = endViewType;
    }

    public int getVideoId() {
        return videoId;
    }

    public void setVideoId(int videoId) {
        this.videoId = videoId;
    }
}
