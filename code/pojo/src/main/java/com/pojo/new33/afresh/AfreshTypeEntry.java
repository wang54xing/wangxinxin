package com.pojo.new33.afresh;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

/**
 * Created by hero on 2019-07-19.
 *
 * 重分行政班 重分方式
 *
 *  sid 学校id
 *  gid  年级id
 *  ciId 次id
 *  aty 重分方式 1: 定二走一,2:单科优选
 *  use 1:使用中， 0 未使用
 */
public class AfreshTypeEntry extends BaseDBObject {

    private static final long serialVersionUID = 1463120166376130498L;

    public AfreshTypeEntry(DBObject dbo) {
        this((BasicDBObject) dbo);
    }

    public AfreshTypeEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public AfreshTypeEntry() {

    }

    public AfreshTypeEntry(
            ObjectId schoolId,
            ObjectId gradeId,
            ObjectId ciId,
            int afreshType,
            int use
    ) {
        BasicDBObject baseEntry = new BasicDBObject()
                .append("sid", schoolId)
                .append("gid", gradeId)
                .append("ciId", ciId)
                .append("aty", afreshType)
                .append("use", use);
        setBaseEntry(baseEntry);
    }

    public ObjectId getSchoolId() {
        return getSimpleObjecIDValue("sid");
    }

    public void setSchoolId(ObjectId schoolId) {
        setSimpleValue("sid", schoolId);
    }

    public ObjectId getGradeId() {
        return getSimpleObjecIDValue("gid");
    }

    public void setGradeId(ObjectId gradeId) {
        setSimpleValue("gid", gradeId);
    }

    public ObjectId getCiId() {
        return getSimpleObjecIDValue("ciId");
    }

    public void setCiId(ObjectId ciId) {
        setSimpleValue("ciId", ciId);
    }

    public int getAfreshType() {
        return getSimpleIntegerValue("aty");
    }

    public void setAfreshType(int afreshType) {
        setSimpleValue("aty", afreshType);
    }

    public int getUse() {
        return getSimpleIntegerValue("use");
    }

    public void setUse(int use) {
        setSimpleValue("use", use);
    }

}
