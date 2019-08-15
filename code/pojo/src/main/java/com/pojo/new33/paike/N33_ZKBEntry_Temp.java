package com.pojo.new33.paike;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * 周课表
 * {
 *     x
 *     y
 *     clsrmid : classroomId 相对 教学班的执行教室而言
 *     jxbId : jxbId 教学班
 *     isUse : 是否被排课
 *     subId : subjectId 学科
 *     tid : teacherId 教师
 *     gid : gradeId 年级
 *     sid : schoolId 学校ID
 *     termId : termId 学期
 *     cid   :次id
 *     week:第几周
 *     userId:发布人id
 *     time:发布时间
 *     ir
 *     typr:类型
 *
 *
 * }
 * Created by wang_xinxin on 2018/3/7.
 */
public class N33_ZKBEntry_Temp extends BaseDBObject {

    private static final long serialVersionUID = -7817415463622071683L;

    public N33_ZKBEntry_Temp(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public N33_ZKBEntry_Temp() {}


    public N33_ZKBEntry_Temp(int x, int y, ObjectId classroomId, ObjectId schoolId, ObjectId termId, Integer week, ObjectId userId, ObjectId nJxbId, ObjectId nsubId, ObjectId ntid, ObjectId cid) {
        this(x,y,classroomId,null,0,null,null,null,schoolId,termId,week,userId,System.currentTimeMillis(),nJxbId,nsubId,ntid,cid);
    }

    public N33_ZKBEntry_Temp(int x, int y, ObjectId classroomId,ObjectId schoolId,ObjectId termId,int type,ObjectId ciId,Integer week,ObjectId userId,long time) {
        this(x,y,classroomId,null,0,null,null,null,schoolId,termId,week,userId,time,null,null,null,ciId,type);
    }

    public N33_ZKBEntry_Temp(int x, int y, ObjectId classroomId, ObjectId jxbId, int isUse, ObjectId subjectId,
                             ObjectId teacherId, ObjectId gradeId, ObjectId schoolId, ObjectId termId, Integer week, ObjectId userId, long time, ObjectId nJxbId, ObjectId nsubId, ObjectId ntid, ObjectId cid) {
        super();
        BasicDBObject dbo =new BasicDBObject()
                .append("x", x)
                .append("y", y)
                .append("clsrmId", classroomId)
                .append("jxbId", jxbId)
                .append("isUse",isUse)
                .append("tid",teacherId)
                .append("subId", subjectId)
                .append("sid",schoolId)
                .append("cid",cid)
                .append("nJxbId", nJxbId)
                .append("nsubId",nsubId)
                .append("ntid",ntid)
                .append("termId",termId)
                .append("gid",gradeId)
                .append("us",userId)
                .append("time",time)
                .append("ir", Constant.ZERO)
                .append("week",week);
        setBaseEntry(dbo);
    }

    public N33_ZKBEntry_Temp(int x, int y, ObjectId classroomId, ObjectId jxbId, int isUse, ObjectId subjectId,
                             ObjectId teacherId, ObjectId gradeId, ObjectId schoolId, ObjectId termId, Integer week, ObjectId userId, long time, ObjectId nJxbId, ObjectId nsubId, ObjectId ntid, ObjectId cid,Integer type) {
        super();
        BasicDBObject dbo =new BasicDBObject()
                .append("x", x)
                .append("y", y)
                .append("clsrmId", classroomId)
                .append("jxbId", jxbId)
                .append("isUse",isUse)
                .append("tid",teacherId)
                .append("subId", subjectId)
                .append("sid",schoolId)
                .append("cid",cid)
                .append("nJxbId", nJxbId)
                .append("nsubId",nsubId)
                .append("ntid",ntid)
                .append("termId",termId)
                .append("gid",gradeId)
                .append("us",userId)
                .append("time",time)
                .append("ir", Constant.ZERO)
                .append("week",week)
                .append("type",type);
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


    public long getTime() {
        return getSimpleIntegerValue("time");
    }
    public void setTime(long time) {
        setSimpleValue("time",time);
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


    public ObjectId getCId() {
        return getSimpleObjecIDValue("cid");
    }
    public void setCId(ObjectId cid) {
        setSimpleValue("cid", cid);
    }


    public int getType() {
        return getSimpleIntegerValueDef("type",0);
    }

    public void setType(int y) {
        setSimpleValue("type", y);
    }
}
