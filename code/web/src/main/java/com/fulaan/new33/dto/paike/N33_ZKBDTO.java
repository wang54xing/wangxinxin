package com.fulaan.new33.dto.paike;

import com.pojo.new33.paike.N33_YKBEntry;
import com.pojo.new33.paike.N33_ZKBEntry;
import com.pojo.new33.paike.N33_ZKBEntry_Temp;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by albin on 2018/3/20.
 */
public class N33_ZKBDTO {
    private String id;

    private int x;

    private int y;

    private String classroomId;

    private String sid;

    private String jxbId;

    private int isUse;

    private String teacherId;

    private String subjectId;

    private String termId;

    private String gradeId;

    private Integer week;
    private String userId;
    private Long time;

    private String gradeName;
    private String nGradeName;

    private String teacherName;

    private String nTeacherName;

    private String subjectName;

    private String nSubjectName;

    private String jxbName;

    private String nJxbName;

    private int studentCount;

    private int nStudentCount;
    private int swType;

    private int jxbType;

    private String remarks;

    private String roomName;

    private String nRoomName;

    private String swDesc;

    private Integer type;//如果是走班，type设置为2，否则设置为1


    private String nJxbId;
    private String nsubId;
    private String ntid;
    private String cid;

    private List<String> stuIds;

    private Integer isSWAndCourse;      //0   只存在事务或者只存在课程         1       既存在事务也存在课程

    private List<String> zouBanType = new ArrayList<String>();

    public List<String> getZouBanType() {
        return zouBanType;
    }

    public void setZouBanType(List<String> zouBanType) {
        this.zouBanType = zouBanType;
    }

    public int getnStudentCount() {
        return nStudentCount;
    }

    public void setnStudentCount(int nStudentCount) {
        this.nStudentCount = nStudentCount;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public String getnGradeName() {
        return nGradeName;
    }

    public void setnGradeName(String nGradeName) {
        this.nGradeName = nGradeName;
    }

    public String getnRoomName() {
        return nRoomName;
    }

    public void setnRoomName(String nRoomName) {
        this.nRoomName = nRoomName;
    }

    public String getnTeacherName() {
        return nTeacherName;
    }

    public void setnTeacherName(String nTeacherName) {
        this.nTeacherName = nTeacherName;
    }

    public String getnSubjectName() {
        return nSubjectName;
    }

    public void setnSubjectName(String nSubjectName) {
        this.nSubjectName = nSubjectName;
    }

    public String getnJxbName() {
        return nJxbName;
    }

    public void setnJxbName(String nJxbName) {
        this.nJxbName = nJxbName;
    }

    public Integer getIsSWAndCourse() {
        return isSWAndCourse;
    }

    public void setIsSWAndCourse(Integer isSWAndCourse) {
        this.isSWAndCourse = isSWAndCourse;
    }

    public List<String> getStuIds() {
        return stuIds;
    }

    public void setStuIds(List<String> stuIds) {
        this.stuIds = stuIds;
    }

    public String getnJxbId() {
        return nJxbId;
    }

    public void setnJxbId(String nJxbId) {
        this.nJxbId = nJxbId;
    }

    public String getNsubId() {
        return nsubId;
    }

    public void setNsubId(String nsubId) {
        this.nsubId = nsubId;
    }

    public String getNtid() {
        return ntid;
    }

    public void setNtid(String ntid) {
        this.ntid = ntid;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getSwDesc() {
        return swDesc;
    }

    public void setSwDesc(String swDesc) {
        this.swDesc = swDesc;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
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

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
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

    public Integer getWeek() {
        return week;
    }

    public void setWeek(Integer week) {
        this.week = week;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public int getJxbType() {
        return jxbType;
    }

    public void setJxbType(int jxbType) {
        this.jxbType = jxbType;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public N33_ZKBDTO() {
    }

    public N33_ZKBDTO(String id, int x, int y, String classroomId, String sid, String jxbId, int isUse, String teacherId, String subjectId, String termId, String gradeId, Integer week, String userId, Long time, String teacherName, String subjectName, String jxbName, int studentCount, int swType, int jxbType, String remarks, String roomName, String swDesc, Integer type, String nJxbId, String nsubId, String ntid) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.classroomId = classroomId;
        this.sid = sid;
        this.jxbId = jxbId;
        this.isUse = isUse;
        this.teacherId = teacherId;
        this.subjectId = subjectId;
        this.termId = termId;
        this.gradeId = gradeId;
        this.week = week;
        this.userId = userId;
        this.time = time;
        this.teacherName = teacherName;
        this.subjectName = subjectName;
        this.jxbName = jxbName;
        this.studentCount = studentCount;
        this.swType = swType;
        this.jxbType = jxbType;
        this.remarks = remarks;
        this.roomName = roomName;
        this.swDesc = swDesc;
        this.type = type;
        this.nJxbId = nJxbId;
        this.nsubId = nsubId;
        this.ntid = ntid;
    }

    public N33_ZKBDTO(N33_ZKBEntry zkbEntry) {
        this.sid = zkbEntry.getSchoolId().toString();
        this.id = zkbEntry.getID().toString();
        this.x = zkbEntry.getX();
        this.y = zkbEntry.getY();
        this.classroomId = zkbEntry.getClassroomId() == null ? null : zkbEntry.getClassroomId().toHexString();
        this.jxbId = zkbEntry.getJxbId() == null ? null : zkbEntry.getJxbId().toHexString();
        this.isUse = zkbEntry.getIsUse();
        this.teacherId = zkbEntry.getTeacherId() == null ? null : zkbEntry.getTeacherId().toHexString();
        this.subjectId = zkbEntry.getSubjectId() == null ? null : zkbEntry.getSubjectId().toHexString();
        this.termId = zkbEntry.getTermId().toString();
        this.gradeId = zkbEntry.getGradeId() == null ? null : zkbEntry.getGradeId().toHexString();
        this.week = zkbEntry.getWeek();
        this.userId = zkbEntry.getUserId().toString();
        this.time = zkbEntry.getTime();
        this.nJxbId=zkbEntry.getNJxbId()==null?null:zkbEntry.getNJxbId().toHexString();
        this.nsubId=zkbEntry.getNSubjectId()==null?null:zkbEntry.getNSubjectId().toHexString();
        this.ntid=zkbEntry.getNTeacherId()==null?null:zkbEntry.getNTeacherId().toHexString();
        this.cid=zkbEntry.getCId().toHexString();
        this.type=zkbEntry.getType();
    }

    public N33_ZKBDTO(N33_YKBEntry ykbEntry, Integer week, ObjectId userId, Long time,ObjectId cid,ObjectId xqid) {
        this.id = ykbEntry.getID().toString();
        this.x = ykbEntry.getX();
        this.sid = ykbEntry.getSchoolId().toString();
        this.y = ykbEntry.getY();
        this.classroomId = ykbEntry.getClassroomId() == null ? null : ykbEntry.getClassroomId().toHexString();
        this.jxbId = ykbEntry.getJxbId() == null ? null : ykbEntry.getJxbId().toHexString();
        this.isUse = ykbEntry.getIsUse();
        this.teacherId = ykbEntry.getTeacherId() == null ? null : ykbEntry.getTeacherId().toHexString();
        this.subjectId = ykbEntry.getSubjectId() == null ? null : ykbEntry.getSubjectId().toHexString();
        this.termId = xqid.toHexString();
        this.gradeId = ykbEntry.getGradeId() == null ? null : ykbEntry.getGradeId().toHexString();
        this.week = week;
        this.userId = userId.toString();
        this.time = time;
        this.nJxbId=ykbEntry.getNJxbId()==null?null:ykbEntry.getNJxbId().toHexString();
        this.nsubId=ykbEntry.getNSubjectId()==null?null:ykbEntry.getNSubjectId().toHexString();
        this.ntid=ykbEntry.getNTeacherId()==null?null:ykbEntry.getNTeacherId().toHexString();
        this.cid=cid.toHexString();
        this.type=ykbEntry.getType();
    }

    public N33_ZKBDTO(N33_ZKBEntry_Temp zkbEntry_temp, Integer week, ObjectId userId, Long time, ObjectId cid, ObjectId xqid) {
        this.id = zkbEntry_temp.getID().toString();
        this.x = zkbEntry_temp.getX();
        this.sid = zkbEntry_temp.getSchoolId().toString();
        this.y = zkbEntry_temp.getY();
        this.classroomId = zkbEntry_temp.getClassroomId() == null ? null : zkbEntry_temp.getClassroomId().toHexString();
        this.jxbId = zkbEntry_temp.getJxbId() == null ? null : zkbEntry_temp.getJxbId().toHexString();
        this.isUse = zkbEntry_temp.getIsUse();
        this.teacherId = zkbEntry_temp.getTeacherId() == null ? null : zkbEntry_temp.getTeacherId().toHexString();
        this.subjectId = zkbEntry_temp.getSubjectId() == null ? null : zkbEntry_temp.getSubjectId().toHexString();
        this.termId = xqid.toHexString();
        this.gradeId = zkbEntry_temp.getGradeId() == null ? null : zkbEntry_temp.getGradeId().toHexString();
        this.week = week;
        this.userId = userId.toString();
        this.time = time;
        this.nJxbId=zkbEntry_temp.getNJxbId()==null?null:zkbEntry_temp.getNJxbId().toHexString();
        this.nsubId=zkbEntry_temp.getNSubjectId()==null?null:zkbEntry_temp.getNSubjectId().toHexString();
        this.ntid=zkbEntry_temp.getNTeacherId()==null?null:zkbEntry_temp.getNTeacherId().toHexString();
        this.cid=cid.toHexString();
        this.type=zkbEntry_temp.getType();
    }


    public N33_ZKBEntry buildEntry() {
        N33_ZKBEntry zkbEntry = new N33_ZKBEntry(
                x,
                y,
                classroomId != null ? new ObjectId(classroomId) : null,
                jxbId != null ? new ObjectId(jxbId) : null,
                isUse,
                subjectId != null ? new ObjectId(subjectId) : null,
                teacherId != null ? new ObjectId(teacherId) : null,
                gradeId != null ? new ObjectId(gradeId) : null,
                new ObjectId(sid),
                new ObjectId(termId),
                week,
                new ObjectId(userId), time,
                nJxbId != null ? new ObjectId(nJxbId) : null,
                nsubId != null ? new ObjectId(nsubId) : null,
                ntid != null ? new ObjectId(ntid) : null,new ObjectId(cid));
        return zkbEntry;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof N33_ZKBDTO)) return false;

        N33_ZKBDTO that = (N33_ZKBDTO) o;

        if (getX() != that.getX()) return false;
        return getY() == that.getY();

    }

    @Override
    public int hashCode() {
        int result = getX();
        result = 31 * result + getY();
        return result;
    }
}
