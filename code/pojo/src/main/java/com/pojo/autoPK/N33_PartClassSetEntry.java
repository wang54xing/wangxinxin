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
 *	fenDuan							    int								    第几分段
 *	classId							    ObjectId							班级id
 */
public class N33_PartClassSetEntry extends BaseDBObject {

    private static final long serialVersionUID = -2981626914330653835L;

    public N33_PartClassSetEntry() {}

    public N33_PartClassSetEntry(DBObject dbo) {
        this((BasicDBObject) dbo);
    }

    public N33_PartClassSetEntry(BasicDBObject baseEntry) {
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

    public void setFenDuan(int fenDuan) {
        setSimpleValue("fenDuan", fenDuan);
    }

    public int getFenDuan() {
        return getSimpleIntegerValueDef("fenDuan", 1);
    }

    public ObjectId getClassId() {
        return getSimpleObjecIDValue("classId");
    }

    public void setClassId(ObjectId classId) {
        setSimpleValue("classId", classId);
    }
}
