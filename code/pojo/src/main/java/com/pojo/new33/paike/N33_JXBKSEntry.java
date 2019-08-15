package com.pojo.new33.paike;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.List;

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
public class N33_JXBKSEntry extends BaseDBObject {

    private static final long serialVersionUID = -9171932074934491890L;

    public N33_JXBKSEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public N33_JXBKSEntry() {}

    public N33_JXBKSEntry(ObjectId jxbId, ObjectId subjectId, int ypCount, ObjectId gradeId, ObjectId schoolId,
                        ObjectId termId,int danOrShuang) {
        super();
        BasicDBObject dbo =new BasicDBObject()
                .append("jxbId", jxbId)
                .append("subId", subjectId)
                .append("sid",schoolId)
                .append("termId",termId)
                .append("gid",gradeId)
                .append("ypcnt",ypCount)
                .append("dos",danOrShuang)
                .append("ir", Constant.ZERO);
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

    public int getYpCount() {
        return getSimpleIntegerValueDef("ypcnt", 0);
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
}
