package com.pojo.new33;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

public class N33_GradeWeekRangeEntry extends BaseDBObject {
    private static final long serialVersionUID = 2769636212915057471L;

    public N33_GradeWeekRangeEntry(DBObject dbo) {
        this((BasicDBObject) dbo);
    }

    public N33_GradeWeekRangeEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public N33_GradeWeekRangeEntry(){}

    public N33_GradeWeekRangeEntry(ObjectId xqid, ObjectId schoolId, ObjectId gid, int start,int end){
        super();
        BasicDBObject dbo = new BasicDBObject()
                .append("sid", schoolId)
                .append("xqid", xqid)
                .append("gid", gid)
                .append("start",start)
                .append("end",end);
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

    public int getStart() {
        return getSimpleIntegerValueDef("start", 0);
    }

    public void setStart(int start) {
        setSimpleValue("start", start);
    }

    public int getEnd() {
        return getSimpleIntegerValueDef("end", 0);
    }

    public void setEnd(int end) {
        setSimpleValue("end", end);
    }
}
