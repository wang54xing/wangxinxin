package com.pojo.new33.paike;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * 周课表
 * {
 * x
 * y
 * clsrmid : classroomId 相对 教学班的执行教室而言
 * jxbId : jxbId 教学班
 * isUse : 是否被排课
 * subId : subjectId 学科
 * tid : teacherId 教师
 * gid : gradeId 年级
 * sid : schoolId 学校ID
 * termId : termId 学期
 * nameId:名字id
 * name:名字
 * userId:发布人id
 * time:发布时间
 * ty：類型
 * ir
 * }
 * Created by wang_xinxin on 2018/3/7.
 */
public class N33_NameKBEntry extends BaseDBObject {


    private static final long serialVersionUID = -8469434805717122190L;

    public N33_NameKBEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public N33_NameKBEntry() {
    }

    public N33_NameKBEntry(int x, int y, ObjectId classroomId, ObjectId jxbId, int isUse, ObjectId subjectId,
                           ObjectId teacherId, ObjectId gradeId, ObjectId schoolId, ObjectId termId, ObjectId nameId, String name,ObjectId nJxbId,ObjectId nsubId,ObjectId ntid) {
        super();
        BasicDBObject dbo = new BasicDBObject()
                .append("x", x)
                .append("y", y)
                .append("clsrmId", classroomId)
                .append("jxbId", jxbId)
                .append("isUse", isUse)
                .append("tid", teacherId)
                .append("subId", subjectId)
                .append("sid", schoolId)
                .append("termId", termId)
                .append("gid", gradeId)
                .append("nJxbId",nJxbId)
                .append("nsubId",nsubId)
                .append("ntid",ntid)
                .append("ir", Constant.ZERO)
                .append("name", name)
                .append("nid", nameId);
        setBaseEntry(dbo);
    }

    public int getX() {
        return getSimpleIntegerValue("x");
    }

    public void setX(int x) {
        setSimpleValue("x", x);
    }

    public int getY() {
        return getSimpleIntegerValue("y");
    }

    public void setY(int y) {
        setSimpleValue("y", y);
    }


    public int getType() {
        return getSimpleIntegerValue("type");
    }

    public void setType(int y) {
        setSimpleValue("type", y);
    }

    public int getIsUse() {
        return getSimpleIntegerValue("isUse");
    }

    public void setIsUse(int isUse) {
        setSimpleValue("isUse", isUse);
    }

    public ObjectId getJxbId() {
        return getSimpleObjecIDValue("jxbId");
    }

    public void setJxbId(ObjectId jxbId) {
        setSimpleValue("jxbId", jxbId);
    }

    public ObjectId getSchoolId() {
        return getSimpleObjecIDValue("sid");
    }

    public void setSchoolId(ObjectId schoolId) {
        setSimpleValue("sid", schoolId);
    }

    public ObjectId getSubjectId() {
        return getSimpleObjecIDValue("subId");
    }

    public void setSubjectId(ObjectId subjectId) {
        setSimpleValue("subId", subjectId);
    }

    public ObjectId getClassroomId() {
        return getSimpleObjecIDValue("clsrmId");
    }

    public void setClassroomId(ObjectId classroomId) {
        setSimpleValue("clsrmId", classroomId);
    }

    public ObjectId getTeacherId() {
        return getSimpleObjecIDValue("tid");
    }

    public void setTeacherId(ObjectId teacherId) {
        setSimpleValue("tid", teacherId);
    }

    public ObjectId getTermId() {
        return getSimpleObjecIDValue("termId");
    }

    public void setTermId(ObjectId termId) {
        setSimpleValue("termId", termId);
    }

    public ObjectId getGradeId() {
        return getSimpleObjecIDValue("gid");
    }

    public void setGradeId(ObjectId gradeId) {
        setSimpleValue("gid", gradeId);
    }

    public ObjectId getUserId() {
        return getSimpleObjecIDValue("uid");
    }

    public void setUserId(ObjectId userId) {
        setSimpleValue("uid", userId);
    }

    public ObjectId getNameId() {
        return getSimpleObjecIDValue("nid");
    }

    public void setNameId(ObjectId nameId) {
        setSimpleValue("nid", nameId);
    }

    public String getName() {
        return getSimpleStringValue("name");
    }

    public void setName(String name) {
        setSimpleValue("name", name);
    }

    public long getTime() {
        return getSimpleIntegerValue("time");
    }

    public void setTime(long time) {
        setSimpleValue("time", time);
    }

    public ObjectId getNTeacherId() {
        return getSimpleObjecIDValue("ntid");
    }
    public void setNTeacherId(ObjectId nTeacherId) {
        setSimpleValue("ntid", nTeacherId);
    }


    public ObjectId getNSubjectId() {
        return getSimpleObjecIDValue("nsubId");
    }
    public void setNSubjectId(ObjectId nSubjectId) {
        setSimpleValue("nsubId", nSubjectId);
    }


    public ObjectId getNJxbId() {
        return getSimpleObjecIDValue("nJxbId");
    }
    public void setNJxbId(ObjectId nJxbId) {
        setSimpleValue("nJxbId",nJxbId);
    }
}
