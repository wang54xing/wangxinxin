package com.sql.oldDataPojo;

/**
 * Created by guojing on 2015/3/23.
 */

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
public class CloudLessonTypeInfo {
    private int id;
    private int schoolType;
    private int subjectId;
    private int gradeId;
    private String chapter;
    private String description;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSchoolType() {
        return schoolType;
    }

    public void setSchoolType(int schoolType) {
        this.schoolType = schoolType;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public int getGradeId() {
        return gradeId;
    }

    public void setGradeId(int gradeId) {
        this.gradeId = gradeId;
    }

    public String getChapter() {
        return chapter;
    }

    public void setChapter(String chapter) {
        this.chapter = chapter;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
