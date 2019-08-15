package com.pojo.new33.isolate;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;


/**
 * Created by albin on 2018/3/8.
 */
public class N33_KSEntry extends BaseDBObject {
    private static final long serialVersionUID = 147823904136519630L;

    public N33_KSEntry(DBObject dbo) {
        this((BasicDBObject) dbo);
    }

    public N33_KSEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public N33_KSEntry(ObjectId xqid, ObjectId schoolId, String subjectName, ObjectId subid, Integer time, Integer dtime, Integer type,Integer type1, ObjectId gid,Integer isZouBan,Integer dan) {
        super();
        BasicDBObject dbo = new BasicDBObject()
                .append("sid", schoolId)
                .append("xqid", xqid)
                .append("subid", subid)//学科id
                .append("snm", subjectName)//学科名称
                .append("type", type)//学科类别  0 全部时间  1单周双周 如果走班 是合格的
                .append("type1", type1)//学科类别  0 全部时间  1单周双周  如果走班 是等级的
                .append("gid", gid)//附属学科ID
                .append("time", time)//课时时间 (无等级课时和合格课时都是这个)
                .append("dtime", dtime)//等级课时时间
                .append("isZouBan",isZouBan)
                .append("dan",dan) // 1是专项
                .append("ir", 0);
        setBaseEntry(dbo);
    }

    public ObjectId getSubjectId() {
        return getSimpleObjecIDValue("subid");
    }

    public void setSubjectId(ObjectId id) {
        setSimpleValue("subid", id);
    }

    public ObjectId getGradeId() {
        return getSimpleObjecIDValue("gid");
    }

    public void setGradeId(ObjectId gradeId) {
        setSimpleValue("gid", gradeId);
    }

    public int getTime() {
        return getSimpleIntegerValueDef("time", 0);
    }

    public void setTime(int time) {
        setSimpleValue("time", time);
    }

    public int getDan() {
        return getSimpleIntegerValueDef("dan", 0);
    }

    public void setDan(int dan) {
        setSimpleValue("dan", dan);
    }
    public int getDTime() {
        return getSimpleIntegerValueDef("dtime", 0);
    }

    public void setDTime(int dtime) {
        setSimpleValue("dtime", dtime);
    }

    public int getType() {
        return getSimpleIntegerValueDef("type", 0);
    }

    public void setType(int type) {
        setSimpleValue("type", type);
    }


    public int getType1() {
        return getSimpleIntegerValueDef("type1", 0);
    }

    public void setType1(int type1) {
        setSimpleValue("type1", type1);
    }

    public ObjectId getXQId() {
        return getSimpleObjecIDValue("xqid");
    }

    public void setXQId(ObjectId xqid) {
        setSimpleValue("xqid", xqid);
    }


    public String getSubjectName() {
        return getSimpleStringValueDef("snm","");
    }

    public void setSubjectName(String subjectName) {
        setSimpleValue("snm", subjectName);
    }


    public ObjectId getSchoolId() {
        return getSimpleObjecIDValue("sid");
    }

    public void setSchoolId(ObjectId schoolId) {
        setSimpleValue("sid", schoolId);
    }



    public int getIsZouBan() {
        return getSimpleIntegerValueDef("isZouBan", 0);
    }

    public void setIsZouBan(int isZouBan) {
        setSimpleValue("isZouBan", isZouBan);
    }
}
