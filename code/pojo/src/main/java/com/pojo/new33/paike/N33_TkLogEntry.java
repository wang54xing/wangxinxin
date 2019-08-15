package com.pojo.new33.paike;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by wang_xinxin on 2018/6/25.
 */
public class N33_TkLogEntry extends BaseDBObject {

    private static final long serialVersionUID = -4805481735871648914L;

    public N33_TkLogEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public N33_TkLogEntry() {}

    public N33_TkLogEntry(ObjectId oYkbId, ObjectId nYkbId, ObjectId schoolId,ObjectId gradeId,
                          ObjectId termId, ObjectId userId,ObjectId oSubjectId,ObjectId nSubjectId,ObjectId oTeacherId,ObjectId nTeacherId,int type,String week,int cimType,int xcty,String olocal,String nlocal) {
        super();
        BasicDBObject dbo =new BasicDBObject()
                .append("sid",schoolId)
                .append("termId",termId)
                .append("gid",gradeId)
                .append("oYkbId",oYkbId)
                .append("nYkbId",nYkbId)
                .append("olocal",olocal)
                .append("nlocal",nlocal)
                .append("osid",oSubjectId)
                .append("nsid",nSubjectId)
                .append("uid",userId)
                .append("otid",oTeacherId)
                .append("ntid",nTeacherId)
                .append("type",type)//调课代课  0调课 1代课2增课3减课
                .append("week",week)//作用周
                .append("cty",cimType)//是否确认 0,1确认
                .append("xcty",xcty)//0短期1长期调课
                .append("time",System.currentTimeMillis())
                .append("ir", Constant.ZERO);
        setBaseEntry(dbo);
    }

    public N33_TkLogEntry(ObjectId oYkbId, ObjectId nYkbId, ObjectId schoolId,ObjectId gradeId,
                          ObjectId termId, ObjectId userId,ObjectId oSubjectId,ObjectId nSubjectId,ObjectId oTeacherId,ObjectId nTeacherId,int type,String week,int cimType,int xcty,String olocal,String nlocal,ObjectId jxbId,ObjectId oJxbId) {
        super();
        BasicDBObject dbo =new BasicDBObject()
                .append("sid",schoolId)
                .append("termId",termId)
                .append("gid",gradeId)
                .append("oYkbId",oYkbId)
                .append("nYkbId",nYkbId)
                .append("olocal",olocal)
                .append("nlocal",nlocal)
                .append("osid",oSubjectId)
                .append("nsid",nSubjectId)
                .append("uid",userId)
                .append("otid",oTeacherId)
                .append("ntid",nTeacherId)
                .append("type",type)//调课代课  0调课 1代课2,3,
                .append("week",week)//作用周
                .append("cty",cimType)//是否确认 0,1确认
                .append("xcty",xcty)//0短期1长期调课
                .append("time",System.currentTimeMillis())
                .append("ir", Constant.ZERO)
                .append("jxbId",jxbId)
                .append("oJxbId",oJxbId);
        setBaseEntry(dbo);
    }

    public int getXcty() {
        return getSimpleIntegerValueDef("xcty",0);
    }
    public void setXcty(int xcty) {
        setSimpleValue("xcty",xcty);
    }
    public int getType() {
        return getSimpleIntegerValue("type");
    }
    public void setType(int type) {
        setSimpleValue("type",type);
    }
    public String getWeek() {
        return getSimpleStringValue("week");
    }
    public void setWeek(String week) {
        setSimpleValue("week",week);
    }
    public int getCimType() {
        return getSimpleIntegerValue("cty");
    }
    public void setCimType(int cimType) {
        setSimpleValue("cty",cimType);
    }
    public long getTime() {
        return getSimpleLongValue("time");
    }
    public void setTime(long time) {
        setSimpleValue("time",time);
    }
    public ObjectId getOTeacherId() {
        return getSimpleObjecIDValue("otid");
    }
    public void setOTeacherId(ObjectId oTeacherId) {
        setSimpleValue("otid", oTeacherId);
    }
    public ObjectId getNTeacherId() {
        return getSimpleObjecIDValue("ntid");
    }
    public void setNTeacherId(ObjectId nTeacherId) {
        setSimpleValue("ntid", nTeacherId);
    }
    public ObjectId getUserId() {
        return getSimpleObjecIDValue("uid");
    }
    public void setUserId(ObjectId userId) {
        setSimpleValue("uid", userId);
    }
    public ObjectId getOYkbId() {
        return getSimpleObjecIDValue("oYkbId");
    }
    public void setOYkbId(ObjectId oYkbId) {
        setSimpleValue("oYkbId", oYkbId);
    }
    public ObjectId getNYkbId() {
        return getSimpleObjecIDValue("nYkbId");
    }
    public void setNYkbId(ObjectId nYkbId) {
        setSimpleValue("nYkbId", nYkbId);
    }
    public ObjectId getSchoolId() {
        return getSimpleObjecIDValue("sid");
    }
    public void setSchoolId(ObjectId schoolId) {
        setSimpleValue("sid", schoolId);
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

    public ObjectId getOSubjectId() {
        return getSimpleObjecIDValue("osid");
    }
    public void setOSubjectId(ObjectId oSubjectId) {
        setSimpleValue("osid", oSubjectId);
    }
    public ObjectId getNSubjectId() {
        return getSimpleObjecIDValue("nsid");
    }
    public void setNSubjectId(ObjectId nSubjectId) {
        setSimpleValue("nsid", nSubjectId);
    }


    public ObjectId getSqId() {
        return getSimpleObjecIDValue("sqId");
    }
    public void setSqId(ObjectId sqId) {
        setSimpleValue("sqId", sqId);
    }

    public String getOlocal() {
        return getSimpleStringValue("olocal");
    }
    public void setOlocal(ObjectId olocal) {
        setSimpleValue("olocal", olocal);
    }

    public String getNlocal() {
        return getSimpleStringValue("nlocal");
    }
    public void setNlocal(ObjectId nlocal) {
        setSimpleValue("nlocal", nlocal);
    }

    public ObjectId getJxbId() {
        return getSimpleObjecIDValue("jxbId");
    }
    public void setJxbId(ObjectId jxbId) {
        setSimpleValue("jxbId", jxbId);
    }

    public ObjectId getoJxbId() {
        return getSimpleObjecIDValue("oJxbId");
    }
    public void setoJxbId(ObjectId ojxbId) {
        setSimpleValue("oJxbId", ojxbId);
    }
}
