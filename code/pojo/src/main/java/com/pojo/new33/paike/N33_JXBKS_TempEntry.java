package com.pojo.new33.paike;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 *教学班课时表，记录教学班的计划课时与已排课时
 * {
 *     jxbId 教学班ID
 *     subjectId 学科ID
 *     ypCount 已排课时数量
 *     gradeId 年级
 *     termId 学期ID
 *     sid 学校ID
 *     ir
 * }
 * Created by wang_xinxin on 2018/3/13.
 */
public class N33_JXBKS_TempEntry extends BaseDBObject {


    private static final long serialVersionUID = -704261577986849368L;

    public N33_JXBKS_TempEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public N33_JXBKS_TempEntry() {}

    public N33_JXBKS_TempEntry(ObjectId jxbId, ObjectId subjectId, int ypCount, ObjectId gradeId, ObjectId schoolId,
                               ObjectId termId, int danOrShuang,int week,Integer type) {
        super();
        BasicDBObject dbo =new BasicDBObject()
                .append("jxbId", jxbId)
                .append("subId", subjectId)
                .append("sid",schoolId)
                .append("termId",termId)
                .append("gid",gradeId)
                .append("ypcnt",ypCount)
                .append("dos",danOrShuang)
                .append("week",week)
                .append("ir", Constant.ZERO)
                .append("type",type);           //0   短期调课          // 1   长期调课
        setBaseEntry(dbo);
    }

    public N33_JXBKS_TempEntry(ObjectId jxbId, ObjectId subjectId, ObjectId gradeId, ObjectId schoolId,
                               ObjectId termId, int week,Integer ks,Integer type) {
        super();
        BasicDBObject dbo =new BasicDBObject()
                .append("jxbId", jxbId)
                .append("subId", subjectId)
                .append("sid",schoolId)
                .append("termId",termId)
                .append("gid",gradeId)
                .append("week",week)
                .append("ks",ks)
                .append("ir", Constant.ZERO).append("type",type);
        setBaseEntry(dbo);
    }

    public ObjectId getSchoolId() {
        return getSimpleObjecIDValue("sid");
    }
    public void setSchoolId(ObjectId schoolId) {
        setSimpleValue("sid", schoolId);
    }

    public ObjectId getSubjectId() {
        return getSimpleObjecIDValue("subId");
    }
    public void setSubjectId(ObjectId subjectId) {
        setSimpleValue("subId", subjectId);
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

    public ObjectId getJxbId() {
        return getSimpleObjecIDValue("jxbId");
    }
    public void setJxbId(ObjectId jxbId) {
        setSimpleValue("jxbId",jxbId);
    }

    public int getKs() {
        return getSimpleIntegerValue("ks");
    }
    public void setKs(int ks) {
        setSimpleValue("ks",ks);
    }

    public int getYpCount() {
        return getSimpleIntegerValue("ypcnt");
    }
    public void setYpCount(int ypCount) {
        setSimpleValue("ypcnt",ypCount);
    }


    public int getDanOrShuang() {
        return getSimpleIntegerValueDef("dos",0);
    }
    public void setDanOrShuang(Integer danOrShuang) {
        setSimpleValue("dos",danOrShuang);
    }

    public Integer getType() {
        return getSimpleIntegerValue("type");
    }
    public void setType(Integer type) {
        setSimpleValue("type",type);
    }

    public int getWeek() {
        return getSimpleIntegerValueDef("week",0);
    }
    public void setWeek(Integer week) {
        setSimpleValue("week",week);
    }
}
