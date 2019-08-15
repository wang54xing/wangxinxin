package com.pojo.autoPK;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

/**
 *自动排课-排课规则-其他设置-分段设置
 *	schoolId							ObjectId							学校id
 *  ciId								ObjectId							次id
 *	gradeId								ObjectId							年级id
 *	maxFenDuan							    int								最大分段数量
 */
public class N33_PartClassFenDuanEntry extends BaseDBObject {

    private static final long serialVersionUID = -8063041009468922373L;

    public N33_PartClassFenDuanEntry() {}

    public N33_PartClassFenDuanEntry(DBObject dbo) {
        this((BasicDBObject) dbo);
    }

    public N33_PartClassFenDuanEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public ObjectId getSchoolId() {
        return getSimpleObjecIDValue("schoolId");
    }

    public void setSchoolId(ObjectId schoolId) {
        setSimpleValue("schoolId", schoolId);
    }

    public ObjectId getCiId() {
        return getSimpleObjecIDValue("ciId");
    }

    public void setCiId(ObjectId ciId) {
        setSimpleValue("ciId", ciId);
    }

    public ObjectId getGradeId() {
        return getSimpleObjecIDValue("gradeId");
    }

    public void setGradeId(ObjectId gradeId) {
        setSimpleValue("gradeId", gradeId);
    }

    public void setMaxFenDuan(int maxFenDuan) {
        setSimpleValue("maxFenDuan", maxFenDuan);
    }

    public int getMaxFenDuan() {
        return getSimpleIntegerValueDef("maxFenDuan", 1);
    }
}
