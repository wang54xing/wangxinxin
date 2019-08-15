package com.fulaan.new33.dto.paike;

import com.pojo.new33.paike.N33_YKBEntry;
import com.pojo.utils.MongoUtils;
import org.bson.types.ObjectId;

import java.util.List;

/**
 * Created by wang_xinxin on 2018/3/12.
 */
public class N33_YKBDTO {
    private String id;

    private int x;

    private int y;

    private String classroomId;

    private String classRoomName;

    private String jxbId;

    private int isUse;

    private String teacherId;

    private String subjectId;

    private String termId;

    private String gradeId;

    private String teacherName;

    private String subjectName;

    private String jxbName;

    private int studentCount;

    private int swType;

    private String remarks;

    private int weekCount;

    private String nteacherName;

    private String nsubjectName;

    private String njxbName;

    private String nteacherId;

    private String nsubjectId;

    private String njxbId;

    private int nstudentCount;

    private String nremarks;

    private int kbType;

    private List<String> ctString;

    private Integer isSWAndCourse;          //0   只存在事务或者只存在课程         1       既存在事务也存在课程

    public Integer getIsSWAndCourse() {
        return isSWAndCourse;
    }

    public void setIsSWAndCourse(Integer isSWAndCourse) {
        this.isSWAndCourse = isSWAndCourse;
    }

    public List<String> getCtString() {
        return ctString;
    }

    public void setCtString(List<String> ctString) {
        this.ctString = ctString;
    }

    public String getClassRoomName() {
        return classRoomName;
    }

    public void setClassRoomName(String classRoomName) {
        this.classRoomName = classRoomName;
    }

    public N33_YKBDTO(N33_YKBEntry ykbEntry) {
        this.id = ykbEntry.getID().toString();
        this.x = ykbEntry.getX();
        this.y = ykbEntry.getY();
        this.classroomId = ykbEntry.getClassroomId()!=null?ykbEntry.getClassroomId().toString():"";
        this.jxbId = ykbEntry.getJxbId()!=null?ykbEntry.getJxbId().toString():"";
        this.isUse = ykbEntry.getIsUse();
        this.teacherId = ykbEntry.getTeacherId()!=null?ykbEntry.getTeacherId().toString():"";
        this.subjectId = ykbEntry.getSubjectId()!=null?ykbEntry.getSubjectId().toString():"";
        this.termId = ykbEntry.getTermId().toString();
        this.gradeId = ykbEntry.getGradeId()!=null?ykbEntry.getGradeId().toString():"";
        this.nteacherId = ykbEntry.getNTeacherId()!=null?ykbEntry.getNTeacherId().toString():"";
        this.nsubjectId = ykbEntry.getNSubjectId()!=null?ykbEntry.getNSubjectId().toString():"";
        this.njxbId = ykbEntry.getNJxbId()!=null?ykbEntry.getNJxbId().toString():"";
        this.kbType = ykbEntry.getType();
        this.remarks="";
    }

    public N33_YKBEntry buildEntry() {
        N33_YKBEntry entry = new N33_YKBEntry(x,y,new ObjectId(classroomId),new ObjectId(jxbId),isUse,new ObjectId(subjectId),new ObjectId(gradeId),null,new ObjectId(termId),kbType);
        entry.setID(new ObjectId(id));
        if(!"".equals(njxbId) && njxbId != null){
            entry.setNJxbId(new ObjectId(njxbId));
        }
        if(!"".equals(teacherId) && teacherId != null){
            entry.setTeacherId(new ObjectId(teacherId));
        }
        return entry;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getClassroomId() {
        return classroomId;
    }

    public void setClassroomId(String classroomId) {
        this.classroomId = classroomId;
    }

    public String getJxbId() {
        return jxbId;
    }

    public void setJxbId(String jxbId) {
        this.jxbId = jxbId;
    }

    public int getIsUse() {
        return isUse;
    }

    public void setIsUse(int isUse) {
        this.isUse = isUse;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
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

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getJxbName() {
        return jxbName;
    }

    public void setJxbName(String jxbName) {
        this.jxbName = jxbName;
    }

    public int getStudentCount() {
        return studentCount;
    }

    public void setStudentCount(int studentCount) {
        this.studentCount = studentCount;
    }

    public int getSwType() {
        return swType;
    }

    public void setSwType(int swType) {
        this.swType = swType;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public int getWeekCount() {
        return weekCount;
    }

    public void setWeekCount(int weekCount) {
        this.weekCount = weekCount;
    }

    public String getNteacherName() {
        return nteacherName;
    }

    public void setNteacherName(String nteacherName) {
        this.nteacherName = nteacherName;
    }

    public String getNsubjectName() {
        return nsubjectName;
    }

    public void setNsubjectName(String nsubjectName) {
        this.nsubjectName = nsubjectName;
    }

    public String getNjxbName() {
        return njxbName;
    }

    public void setNjxbName(String njxbName) {
        this.njxbName = njxbName;
    }

    public String getNteacherId() {
        return nteacherId;
    }

    public void setNteacherId(String nteacherId) {
        this.nteacherId = nteacherId;
    }

    public String getNsubjectId() {
        return nsubjectId;
    }

    public void setNsubjectId(String nsubjectId) {
        this.nsubjectId = nsubjectId;
    }

    public String getNjxbId() {
        return njxbId;
    }

    public void setNjxbId(String njxbId) {
        this.njxbId = njxbId;
    }

    public int getNstudentCount() {
        return nstudentCount;
    }

    public void setNstudentCount(int nstudentCount) {
        this.nstudentCount = nstudentCount;
    }

    public String getNremarks() {
        return nremarks;
    }

    public void setNremarks(String nremarks) {
        this.nremarks = nremarks;
    }

    public int getKbType() {
        return kbType;
    }

    public void setKbType(int kbType) {
        this.kbType = kbType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof N33_YKBDTO)) return false;

        N33_YKBDTO ykbdto = (N33_YKBDTO) o;

        if (getX() != ykbdto.getX()) return false;
        return getY() == ykbdto.getY();

    }

    @Override
    public int hashCode() {
        int result = getX();
        result = 31 * result + getY();
        return result;
    }
}
