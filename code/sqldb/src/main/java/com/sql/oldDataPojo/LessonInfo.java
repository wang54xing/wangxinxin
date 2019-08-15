package com.sql.oldDataPojo;

import java.util.Date;

/**
 * Created by qinbo on 15/3/24.
 */
public class LessonInfo {


    private int id;
    private String courseName;
    private String courseContent;
    private String imageUrl;
    private int dirId;
    private Date createtime;
    private int isFromCloud;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseContent() {
        return courseContent;
    }

    public void setCourseContent(String courseContent) {
        this.courseContent = courseContent;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getDirId() {
        return dirId;
    }

    public void setDirId(int dirId) {
        this.dirId = dirId;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public int getIsFromCloud() {
        return isFromCloud;
    }

    public void setIsFromCloud(int isFromCloud) {
        this.isFromCloud = isFromCloud;
    }
}
