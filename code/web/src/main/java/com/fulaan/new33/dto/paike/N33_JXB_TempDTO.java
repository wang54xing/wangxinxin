package com.fulaan.new33.dto.paike;

import com.pojo.new33.paike.N33_JXBEntry;
import com.pojo.new33.paike.N33_JXB_TempEntry;
import com.pojo.utils.MongoUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by wang_xinxin on 2018/3/13.
 */
public class N33_JXB_TempDTO {
    private String id;

    private Integer type;
    private Integer jxbks;

    private String name;

    private String nickName;

    private String subjectId;

    private String subjectName;

    private String classroomId;

    private String classroomName;

    private String teacherId;

    private String teacherName;

    private List<String> studentIds;

    private String schoolId;

    private String termId;

    private String gradeId;

    private String gradeName;

    private int index;

    private int zksCount;

    private int ypksCount;

    private int studentCount;
    private int rl;

    private int view;

    private String remarks;

    private String relativeId;
    private Integer danOrShuang;

    private Integer thisGradeCarryNum;

    private Integer carryNum;

    private boolean zuHeRepeatFlag;     //true 和其他教学班组合有重复值   false  无重复

    public boolean isZuHeRepeatFlag() {
        return zuHeRepeatFlag;
    }

    public void setZuHeRepeatFlag(boolean zuHeRepeatFlag) {
        this.zuHeRepeatFlag = zuHeRepeatFlag;
    }

    public String getRelativeId() {
        return relativeId;
    }

    public void setRelativeId(String relativeId) {
        this.relativeId = relativeId;
    }

    public Integer getDanOrShuang() {
        return danOrShuang;
    }

    public void setDanOrShuang(Integer danOrShuang) {
        this.danOrShuang = danOrShuang;
    }

    public int getView() {
        return view;
    }

    public void setView(int view) {
        this.view = view;
    }

    public int getRl() {
        return rl;
    }

    public void setRl(int rl) {
        this.rl = rl;
    }

    private List<String> tagIds;

    private List<Map<String, Object>> tags = new ArrayList<Map<String, Object>>();

    private List<Map<String, Object>> class_count = new ArrayList<Map<String, Object>>();// 行政班——人数

    public N33_JXB_TempDTO() {
        super();
    }

    public N33_JXB_TempDTO(N33_JXB_TempEntry e) {
        this.id = e.getID().toString();
        this.name = e.getName();
        this.nickName = e.getNickName();
        this.subjectId = e.getSubjectId().toString();
        this.classroomId = e.getClassroomId() == null ? "" : e.getClassroomId().toString();
        this.teacherId = e.getTercherId() == null ? "" : e.getTercherId().toString();
        ;
        this.termId = e.getTermId().toString();
        this.gradeId = e.getGradeId().toString();
        this.type = e.getType();
        this.studentCount = e.getStudentIds() != null ? e.getStudentIds().size() : 0;
        if (e.getTagIds() != null) {
            this.tagIds = MongoUtils.convertToStringList(e.getTagIds());
        }
        this.rl = e.getRongLiang();
        this.view = e.getView();
        this.danOrShuang = e.getDanOrShuang();
        this.jxbks=e.getJXBKS();
        this.relativeId = e.getRelativeId() == null ? "" : e.getRelativeId().toHexString();
    }

    public Integer getJxbks() {
        return jxbks;
    }

    public void setJxbks(Integer jxbks) {
        this.jxbks = jxbks;
    }

    public List<String> getTagIds() {
        return tagIds;
    }

    public void setTagIds(List<String> tagIds) {
        this.tagIds = tagIds;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getClassroomId() {
        return classroomId;
    }

    public void setClassroomId(String classroomId) {
        this.classroomId = classroomId;
    }

    public String getClassroomName() {
        return classroomName;
    }

    public void setClassroomName(String classroomName) {
        this.classroomName = classroomName;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public List<String> getStudentIds() {
        return studentIds;
    }

    public void setStudentIds(List<String> studentIds) {
        this.studentIds = studentIds;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getTermId() {
        return termId;
    }

    public void setTermId(String termId) {
        this.termId = termId;
    }

    public String getGradeId() {
        return gradeId;
    }

    public void setGradeId(String gradeId) {
        this.gradeId = gradeId;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getZksCount() {
        return zksCount;
    }

    public void setZksCount(int zksCount) {
        this.zksCount = zksCount;
    }

    public int getYpksCount() {
        return ypksCount;
    }

    public void setYpksCount(int ypksCount) {
        this.ypksCount = ypksCount;
    }

    public int getStudentCount() {
        return studentCount;
    }

    public void setStudentCount(int studentCount) {
        this.studentCount = studentCount;
    }

    public List<Map<String, Object>> getClass_count() {
        return class_count;
    }

    public void setClass_count(List<Map<String, Object>> class_count) {
        this.class_count = class_count;
    }

    public List<Map<String, Object>> getTags() {
        return tags;
    }

    public void setTags(List<Map<String, Object>> tags) {
        this.tags = tags;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Integer getThisGradeCarryNum() {
        return thisGradeCarryNum;
    }

    public void setThisGradeCarryNum(Integer thisGradeCarryNum) {
        this.thisGradeCarryNum = thisGradeCarryNum;
    }

    public Integer getCarryNum() {
        return carryNum;
    }

    public void setCarryNum(Integer carryNum) {
        this.carryNum = carryNum;
    }
}
