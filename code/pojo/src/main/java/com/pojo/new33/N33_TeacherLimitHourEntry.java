package com.pojo.new33;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

public class N33_TeacherLimitHourEntry extends BaseDBObject {

    private static final long serialVersionUID = -4506824974509598790L;

    public N33_TeacherLimitHourEntry(DBObject dbo) {
        this((BasicDBObject) dbo);
    }

    public N33_TeacherLimitHourEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public N33_TeacherLimitHourEntry(){}

    public N33_TeacherLimitHourEntry(ObjectId xqid, ObjectId schoolId,ObjectId gid,int hour){
        super();
        BasicDBObject dbo = new BasicDBObject()
                .append("sid", schoolId)
                .append("xqid", xqid)
                .append("gid", gid)
                .append("hour",hour);
        setBaseEntry(dbo);
    }

    public ObjectId getGradeId() {
        return getSimpleObjecIDValue("gid");
    }

    public void setGradeId(ObjectId gradeId) {
        setSimpleValue("gid", gradeId);
    }

    public ObjectId getSchoolId() {
        return getSimpleObjecIDValue("sid");
    }

    public void setSchoolId(ObjectId schoolId) {
        setSimpleValue("sid", schoolId);
    }

    public ObjectId getXQId() {
        return getSimpleObjecIDValue("xqid");
    }

    public void setXQId(ObjectId xqid) {
        setSimpleValue("xqid", xqid);
    }

    public int getHour() {
        return getSimpleIntegerValueDef("hour", 0);
    }

    public void setHour(int hour) {
        setSimpleValue("hour", hour);
    }

}
