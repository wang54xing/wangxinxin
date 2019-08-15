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
 *  subId 学科id
 *  fn 班级数量
 *  fv 班级容量
 */
public class AfreshSubRuleEntry extends BaseDBObject {

    private static final long serialVersionUID = -6545565470676647374L;

    public AfreshSubRuleEntry(DBObject dbo) {
        this((BasicDBObject) dbo);
    }

    public AfreshSubRuleEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public AfreshSubRuleEntry() {

    }

    public AfreshSubRuleEntry(
            ObjectId schoolId,
            ObjectId gradeId,
            ObjectId ciId,
            ObjectId subId,
            int afreshType,
            int number,
            int volume
    ) {
        BasicDBObject baseEntry = new BasicDBObject()
                .append("sid", schoolId)
                .append("gid", gradeId)
                .append("ciId", ciId)
                .append("subId", subId)
                .append("aty", afreshType)
                .append("fn", number)
                .append("fv", volume);
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

    public ObjectId getSubId() {
        return getSimpleObjecIDValue("subId");
    }

    public void setSubId(ObjectId subId) {
        setSimpleValue("subId", subId);
    }

    public int getAfreshType() {
        return getSimpleIntegerValue("aty");
    }

    public void setAfreshType(int afreshType) {
        setSimpleValue("aty", afreshType);
    }

    public int getNumber() {
        return getSimpleIntegerValueDef("fn", 0);
    }

    public void setNumber(int number) {
        setSimpleValue("fn", number);
    }

    public int getVolume() {
        return getSimpleIntegerValueDef("fv", 0);
    }

    public void setVolume(int volume) {
        setSimpleValue("fv", volume);
    }

}
