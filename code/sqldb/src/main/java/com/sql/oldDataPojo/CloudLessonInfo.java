package com.sql.oldDataPojo;

/**
 * Created by guojing on 2015/3/23.
 */

import org.bson.types.ObjectId;

import java.util.List;

/**
 * 云课程
 * <pre>
 * collectionName:cloudclasses
 * </pre>
 * <pre>
 * {
 *  nm:名字
 *  con:内容
 *  tid:老师ID
 *  or:排序
 *  im:图片地址
 *  vi:视频ID
 *  sub:科目ID；对应SubjectType
 *  ccgt:云课程年级；对应CloudLessonGradeType
 *  ccty:云课程类别；对应CloudLessonTypeEntry
 *
 * }
 * </pre>
 * @author fourer
 */
public class CloudLessonInfo {
    private int id;
    private String lessonName;
    private String lessonContent;
    private int teacherId;
    private int order;
    private String imageUrl;
    private int videoId;
    private int subjectId;

    private List<Integer> gradeList=null;// cloudLessonGradeType;

    private List<ObjectId> lessonList=null;//cloudLessonTypeEntry;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLessonName() {
        return lessonName;
    }

    public void setLessonName(String lessonName) {
        this.lessonName = lessonName;
    }

    public String getLessonContent() {
        return lessonContent;
    }

    public void setLessonContent(String lessonContent) {
        this.lessonContent = lessonContent;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getVideoId() {
        return videoId;
    }

    public void setVideoId(int videoId) {
        this.videoId = videoId;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public List<Integer> getGradeList() {
        return gradeList;
    }

    public void setGradeList(List<Integer> gradeList) {
        this.gradeList = gradeList;
    }

    public List<ObjectId> getLessonList() {
        return lessonList;
    }

    public void setLessonList(List<ObjectId> lessonList) {
        this.lessonList = lessonList;
    }
}
