package com.pojo.new33;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.new33.paike.N33_ZKBEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * 增减调课记录
 * si : 学校ID
 * jxbid : 教学班id
 * tid:教学班老师
 * week:教学周
 * clsid:教室id
 * x:
 * y:
 * suid:学科id
 *
 */
public class N33_ZjkjEntry extends BaseDBObject {

    private static final long serialVersionUID = 768725993407144988L;

    public N33_ZjkjEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public N33_ZjkjEntry(int x, int y, ObjectId classroomId, ObjectId schoolId, ObjectId termId, Integer week, ObjectId userId,ObjectId jxbId,ObjectId subjectId,
                         ObjectId teacherId,ObjectId gradeId,ObjectId cid) {
        super();
        BasicDBObject dbo =new BasicDBObject()
                .append("x", x)
                .append("y", y)
                .append("clsrmId", classroomId)
                .append("jxbId", jxbId)
                .append("isUse",0)
                .append("tid",teacherId)
                .append("subId", subjectId)
                .append("sid",schoolId)
                .append("cid",cid)
                .append("termId",termId)
                .append("gid",gradeId)
                .append("us",userId)
                .append("ir", Constant.ZERO)
                .append("week",week);
        setBaseEntry(dbo);
    }

    public int getWeek() {
        return getSimpleIntegerValue("week");
    }
    public void setWeek(int week) {
        setSimpleValue("week", week);
    }
    public int getX() {
        return getSimpleIntegerValue("x");
    }
    public void setX(int x) {
        setSimpleValue("x",x);
    }
    public int getY() {
        return getSimpleIntegerValue("y");
    }
    public void setY(int y) {
        setSimpleValue("y",y);
    }
    public int getIsUse() {
        return getSimpleIntegerValue("isUse");
    }
    public void setIsUse(int isUse) {
        setSimpleValue("isUse",isUse);
    }
    public ObjectId getJxbId() {
        return getSimpleObjecIDValue("jxbId");
    }
    public void setJxbId(ObjectId jxbId) {
        setSimpleValue("jxbId",jxbId);
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
        return getSimpleObjecIDValue("us");
    }
    public void setUserId(ObjectId userId) {
        setSimpleValue("us", userId);
    }
    public ObjectId getCId() {
        return getSimpleObjecIDValue("cid");
    }
    public void setCId(ObjectId cid) {
        setSimpleValue("cid", cid);
    }

}
