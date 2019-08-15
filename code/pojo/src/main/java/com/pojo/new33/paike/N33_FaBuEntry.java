package com.pojo.new33.paike;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

/**
 * Created by albin on 2018/6/19.
 */
public class N33_FaBuEntry extends BaseDBObject {
    private static final long serialVersionUID = 767419300390478812L;

    public N33_FaBuEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public N33_FaBuEntry() {
    }

    public N33_FaBuEntry(ObjectId ciId, ObjectId sid,ObjectId xqid) {
        super();
        BasicDBObject dbo = new BasicDBObject()
                .append("ciId", ciId)//次id
                .append("xqid", xqid)
                .append("sid", sid);
        setBaseEntry(dbo);
    }

    public ObjectId getSchoolId() {
        return getSimpleObjecIDValue("sid");
    }

    public void setSchoolId(ObjectId schoolId) {
        setSimpleValue("sid", schoolId);
    }

    public ObjectId getCiId() {
        return getSimpleObjecIDValue("ciId");
    }

    public void setCiId(ObjectId ciId) {
        setSimpleValue("ciId", ciId);
    }


    public ObjectId getXqId() {
        return getSimpleObjecIDValue("xqId");
    }

    public void setXqId(ObjectId xqId) {
        setSimpleValue("xqId", xqId);
    }
}
