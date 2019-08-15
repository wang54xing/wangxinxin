package com.fulaan.new33.dto.paike;

import com.pojo.new33.paike.N33_NameKBEntry;
import com.pojo.new33.paike.N33_YKBEntry;
import com.pojo.new33.paike.N33_ZKBEntry;
import org.bson.types.ObjectId;

/**
 * Created by albin on 2018/3/20.
 */
public class N33_NameKBDTO {
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

    private String name;
    private String nid;

    private String nJxbId;
    private String nsubId;
    private String ntid;


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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
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


    public N33_NameKBDTO(N33_NameKBEntry zkbEntry) {
        this.sid = zkbEntry.getSchoolId().toString();
        this.id = zkbEntry.getID().toString();
        this.x = zkbEntry.getX();
        this.y = zkbEntry.getY();
        this.classroomId = zkbEntry.getClassroomId()== null ? null : zkbEntry.getClassroomId().toHexString();
        this.jxbId = zkbEntry.getJxbId() == null ? null : zkbEntry.getJxbId().toHexString();
        this.isUse = zkbEntry.getIsUse();
        this.teacherId = zkbEntry.getTeacherId()== null ? null : zkbEntry.getTeacherId().toHexString();
        this.subjectId = zkbEntry.getSubjectId() == null ? null : zkbEntry.getSubjectId().toHexString();
        this.termId = zkbEntry.getTermId().toString();
        this.gradeId = zkbEntry.getGradeId()== null ? null : zkbEntry.getGradeId().toHexString();
        this.name = zkbEntry.getName();
        this.nid = zkbEntry.getNameId().toHexString();
        this.nJxbId=zkbEntry.getNJxbId()==null?null:zkbEntry.getNJxbId().toHexString();
        this.nsubId=zkbEntry.getNSubjectId()==null?null:zkbEntry.getNSubjectId().toHexString();
        this.ntid=zkbEntry.getNTeacherId()==null?null:zkbEntry.getNTeacherId().toHexString();
    }

    public N33_NameKBDTO(N33_YKBEntry ykbEntry, String name, String nameId) {
        this.id = ykbEntry.getID().toString();
        this.x = ykbEntry.getX();
        this.sid = ykbEntry.getSchoolId().toString();
        this.y = ykbEntry.getY();
        this.classroomId = ykbEntry.getClassroomId()== null ? null : ykbEntry.getClassroomId().toHexString();
        this.jxbId = ykbEntry.getJxbId() == null ? null : ykbEntry.getJxbId().toHexString();
        this.isUse = ykbEntry.getIsUse();
        this.teacherId = ykbEntry.getTeacherId()== null ? null : ykbEntry.getTeacherId().toHexString();
        this.subjectId = ykbEntry.getSubjectId() == null ? null : ykbEntry.getSubjectId().toHexString();
        this.termId = ykbEntry.getTermId().toString();
        this.gradeId = ykbEntry.getGradeId()== null ? null : ykbEntry.getGradeId().toHexString();
        this.name = name;
        this.nid = nameId;
        this.nJxbId=ykbEntry.getNJxbId()==null?null:ykbEntry.getNJxbId().toHexString();
        this.nsubId=ykbEntry.getNSubjectId()==null?null:ykbEntry.getNSubjectId().toHexString();
        this.ntid=ykbEntry.getNTeacherId()==null?null:ykbEntry.getNTeacherId().toHexString();
    }

    public N33_NameKBDTO(String id, int x, int y, String classroomId, String sid, String jxbId, int isUse, String teacherId, String subjectId, String termId, String gradeId, String name, String nid, String nJxbId, String nsubId, String ntid) {
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
        this.name = name;
        this.nid = nid;
        this.nJxbId = nJxbId;
        this.nsubId = nsubId;
        this.ntid = ntid;
    }

    public N33_NameKBEntry getEntry() {
        N33_NameKBEntry zkbEntry = new N33_NameKBEntry(
                x, y,
                classroomId != null ? new ObjectId(classroomId) : null,
                jxbId != null ? new ObjectId(jxbId) : null,
                isUse,
                subjectId != null ? new ObjectId(subjectId) : null,
                teacherId != null ? new ObjectId(teacherId) : null,
                gradeId != null ? new ObjectId(gradeId) : null,
                new ObjectId(sid), new ObjectId(termId), new ObjectId(nid), name,
                nJxbId != null ? new ObjectId(nJxbId) : null,
                nsubId != null ? new ObjectId(nsubId) : null,
                ntid != null ? new ObjectId(ntid) : null);
        return zkbEntry;
    }
}
