package com.pojo.new33.afresh;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2019-05-13.
 *
 *  *  重分行政班（班级表）
 *
 * sid 学校id
 * nm 班级名
 * buid 班主任id
 * bunm 班主任名
 * stus 学生集合
 * xqid 学期id
 * xh	班级序号
 * ty:是否同步0同步  1非同步
 * cid  班级id
 * gid  年级id
 * bz   备注
 * rid  ruleId 所属规则 --(AfreshClassRuleEntry)  //setId
 *
 */
public class AfreshClassEntry extends BaseDBObject {

    private static final long serialVersionUID = 1242991562318562073L;

    public AfreshClassEntry(DBObject dbo) {
        this((BasicDBObject) dbo);
    }

    public AfreshClassEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }
    public AfreshClassEntry() {

    }

    public AfreshClassEntry(ObjectId xqid, ObjectId schoolId,
                      String nm, List<ObjectId> uis,
                      String bunm,
                      ObjectId cid,Integer type,ObjectId gid,String bz,Integer xh,ObjectId ruleId) {
        super();
        BasicDBObject dbo = new BasicDBObject()
                .append("sid", schoolId)
                .append("xqid", xqid)
                .append("nm", nm)
                .append("uis", MongoUtils.convert(uis))
                .append("bunm", bunm)
                .append("ty", type)
                .append("cid",cid)
                .append("gid",gid)
                .append("bz", bz)
                .append("xh", xh)
                .append("rid", ruleId);
        setBaseEntry(dbo);
    }

    public AfreshClassEntry(ObjectId xqid, ObjectId schoolId, ObjectId gradeId, String className) {
        BasicDBObject baseEntry = new BasicDBObject()
                .append("sid", schoolId)
                .append("xqid", xqid)
                .append("gid", gradeId)
                .append("cid", new ObjectId())
                .append("nm", className);
        setBaseEntry(baseEntry);
    }

    public String getSy() {
        return getSimpleStringValue("sy");
    }

    public void setSy(String sy) {
        setSimpleValue("sy", sy);
    }

    public Integer getXh() {
        return getSimpleIntegerValueDef("xh",0);

    }

    public void setXh(Integer xh) {
        setSimpleValue("xh", xh);
    }


    public ObjectId getGradeId() {
        return getSimpleObjecIDValue("gid");
    }

    public void setGradeId(ObjectId gid) {
        setSimpleValue("gid", gid);
    }

    public ObjectId getSchoolId() {
        return getSimpleObjecIDValue("sid");
    }

    public void setSchoolId(ObjectId schoolId) {
        setSimpleValue("sid", schoolId);
    }

    public ObjectId getClassId() {
        return getSimpleObjecIDValue("cid");
    }

    public void setCId(ObjectId cid) {
        setSimpleValue("cid", cid);
    }
   public ObjectId getRuleId() {
        return getSimpleObjecIDValue("rid");
    }

    public void setRuleId(ObjectId ruleId) {
        setSimpleValue("rid", ruleId);
    }


    public ObjectId getXQId() {
        return getSimpleObjecIDValue("xqid");
    }

    public void setXQId(ObjectId xqid) {
        setSimpleValue("xqid", xqid);
    }



    public void setClassName(String nm) {
        setSimpleValue("nm", nm);
    }

    public String getClassName() {
        return getSimpleStringValue("nm");
    }

    public void setCName(String nm) {
        setSimpleValue("nm", nm);
    }

    public String getName() {
        return getSimpleStringValue("nm");
    }

    public void setName(String nm) {
        setSimpleValue("nm", nm);
    }


    public List<ObjectId> getStudentList() {
        List<ObjectId> retList = new ArrayList<ObjectId>();
        BasicDBList list = (BasicDBList) getSimpleObjectValue("uis");
        if (null != list && !list.isEmpty()) {
            for (Object o : list) {
                retList.add(((ObjectId) o));
            }
        }
        return retList;
    }

    public void setUserList(List<ObjectId> uis) {
        setSimpleValue("uis", MongoUtils.convert(uis));
    }

    public ObjectId getMentorId() {
        return getSimpleObjecIDValue("buid");
    }

    public void setBuId(ObjectId buid) {
        setSimpleValue("buid", buid);
    }


    public void setMentorId(ObjectId buid) {
        setSimpleValue("buid", buid);
    }



    public String getMentorName() {
        return getSimpleStringValueDef("bunm","");
    }

    public void setBuName(String bunm) {
        setSimpleValue("bunm", bunm);
    }



    public void setMentorName(String bunm) {
        setSimpleValue("bunm", bunm);
    }

    public ObjectId getRoomId() {
        return getSimpleObjecIDValue("jid");
    }

    public void setJId(ObjectId jid) {
        setSimpleValue("jid", jid);
    }

    public void setRoomId(ObjectId jid) {
        setSimpleValue("jid", jid);
    }

    public String getRoomName() {
        return getSimpleStringValue("jnm");
    }

    public void setJName(String jnm) {
        setSimpleValue("jnm", jnm);
    }

    public void setRoomName(String jnm) {
        setSimpleValue("jnm", jnm);
    }
    public String getBeiZhu() {
        return getSimpleStringValueDef("bz", "");
    }
    public void setBeiZhu(String bz) {
        setSimpleValue("bz", bz);
    }
    public Integer getType() {
        return getSimpleIntegerValue("ty");
    }
    public void setType(Integer type) {
        setSimpleValue("ty", type);
    }
}
