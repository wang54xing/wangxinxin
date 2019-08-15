package com.pojo.new33.paike;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by wang_xinxin on 2018/6/28.
 */
public class N33_FaBuLogEntry  extends BaseDBObject {

    private static final long serialVersionUID = 2182769008302439995L;

    public N33_FaBuLogEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public N33_FaBuLogEntry() {}


    public N33_FaBuLogEntry(ObjectId schoolId,ObjectId userId,ObjectId gradeId,ObjectId termId,ObjectId ciId,int startTime,int endTime,int status) {
        super();
        BasicDBObject dbo =new BasicDBObject()
                .append("sid",schoolId)
                .append("termId",termId)
                .append("gid",gradeId)
                .append("uid",userId)
                .append("ciId",ciId)
                .append("st",startTime)
                .append("et",endTime)
                .append("stu",status)
                .append("time",System.currentTimeMillis())
                .append("ir", Constant.ZERO);
        setBaseEntry(dbo);
    }
    public long getTime() {
        return getSimpleLongValue("time");
    }
    public void setTime(int time) {
        setSimpleValue("time", time);
    }
    public int getStatus() {
        return getSimpleIntegerValue("stu");
    }
    public void setStatus(int status) {
        setSimpleValue("stu", status);
    }
    public int getEndTime() {
        return getSimpleIntegerValue("et");
    }
    public void setEndTime(int endTime) {
        setSimpleValue("et", endTime);
    }
    public int getStartTime() {
        return getSimpleIntegerValue("st");
    }
    public void setStartTime(int startTime) {
        setSimpleValue("st", startTime);
    }
    public ObjectId getCiId() {
        return getSimpleObjecIDValue("ciId");
    }
    public void setCiId(ObjectId ciId) {
        setSimpleValue("ciId", ciId);
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
    public ObjectId getUserId() {
        return getSimpleObjecIDValue("uid");
    }
    public void setUserId(ObjectId userId) {
        setSimpleValue("uid", userId);
    }
    public ObjectId getShenQingId() {
        return getSimpleObjecIDValue("sqId");
    }
    public void setShenQingId(ObjectId sqId) {
        setSimpleValue("sqId", sqId);
    }

}
