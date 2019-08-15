package com.pojo.newschool;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * 老师工作信息
 * <pre>
 * collectionName:resume
 * </pre>
 * <pre>
 * {
 * ui
 *  org工作单位organization
 *  st 开始时间 startTime
 *  et 结束时间 endTime
 *  detail具体事项detail
 *
 * }
 * </pre>
 * Created by wang_xinxin on 2016/2/29.
 */
public class JobEntry extends BaseDBObject {
    private static final long serialVersionUID = 5588685930345918433L;

    public JobEntry(BasicDBObject basicDBObject) {
        super(basicDBObject);
    }

    public JobEntry(ObjectId userId, long startTime, long endTime, String organization, String detail) {
        BasicDBObject dbo =new BasicDBObject()
                .append("uid", userId)
                .append("st", startTime)
                .append("et", endTime)
                .append("org", organization)
                .append("detail", detail)
                .append("ir", Constant.ZERO);
        setBaseEntry(dbo);
    }
    public ObjectId getUserId() {
        return getSimpleObjecIDValue("uid");
    }
    public void setUserId(ObjectId userId) {
        setSimpleValue("uid", userId);
    }
    public String getDetail() {
        return getSimpleStringValue("detail");
    }
    public void setDetail(String detail) {
        setSimpleValue("detail",detail);
    }
    public String getOrganization() {
        return getSimpleStringValue("org");
    }
    public void setOrganization(String organization) {
        setSimpleValue("org",organization);
    }
    public long getStartTime() {
        return getSimpleLongValue("st");
    }
    public void setStartTime(long startTime) {
        setSimpleValue("st",startTime);
    }
    public long getEndTime() {
        return getSimpleIntegerValue("et");
    }
    public void setEndTime(long endTime) {
        setSimpleValue("et",endTime);
    }
}
