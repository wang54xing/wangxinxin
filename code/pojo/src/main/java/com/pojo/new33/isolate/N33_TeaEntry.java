package com.pojo.new33.isolate;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import com.sys.utils.NameShowUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by albin on 2018/3/8.
 */
public class N33_TeaEntry extends BaseDBObject {
    private static final long serialVersionUID = -7114952971835593896L;

    public N33_TeaEntry(BasicDBObject baseEntry) {
        setBaseEntry(baseEntry);
    }

    public N33_TeaEntry() {
    }

    public N33_TeaEntry(ObjectId schoolId, ObjectId xqid, ObjectId uid, String unm,
                        List<ObjectId> gid, Integer sex, List<ObjectId> subids,Integer classNum,Integer isBanZhuRen) {
        BasicDBObject dbObject = new BasicDBObject()
                .append("sid", schoolId)
                .append("xqid", xqid)
                .append("uid", uid)
                .append("unm", unm)
                .append("cnm", classNum)
                .append("isBzr", isBanZhuRen)
                .append("unm", unm)
                .append("gid", MongoUtils.convert(gid))
                .append("sub", MongoUtils.convert(subids))
                .append("sex", sex);
        setBaseEntry(dbObject);
    }

    public ObjectId getSchoolId() {
        return getSimpleObjecIDValue("sid");
    }

    public void setSchoolId(ObjectId sid) {
        setSimpleValue("sid", sid);
    }

    public ObjectId getXqid() {
        return getSimpleObjecIDValue("xqid");
    }

    public void setXqid(ObjectId xqid) {
        setSimpleValue("xqid", xqid);
    }

    public ObjectId getUserId() {
        return getSimpleObjecIDValue("uid");
    }

    public void setUserid(ObjectId uid) {
        setSimpleValue("uid", uid);
    }

    public void setUserName(String unm) {
        setSimpleValue("unm", unm);
    }

    public String getUserName() {
        String nm= getSimpleStringValue("unm");
        return NameShowUtils.showName(nm);
    }

    public int getSex() {
        return getSimpleIntegerValue("sex");
    }

    public void setSex(int sex) {
        setSimpleValue("sex", sex);
    }

    public int getClassNum() {
        return getSimpleIntegerValueDef("cnm",0);
    }

    public void setClassNum(int classNum) {
        setSimpleValue("cnm", classNum);
    }

    public int getIsBanZhuRen() {
        return getSimpleIntegerValueDef("isBzr",0);
    }

    public void setIsBanZhuRen(int isBanZhuRen) {
        setSimpleValue("isBzr", isBanZhuRen);
    }

    public List<ObjectId> getGradeList() {
        List<ObjectId> retList = new ArrayList<ObjectId>();
        BasicDBList list = (BasicDBList) getSimpleObjectValue("gid");
        if (null != list && !list.isEmpty()) {
            for (Object o : list) {
                retList.add(((ObjectId) o));
            }
        }
        return retList;
    }

    public void setGradeList(List<ObjectId> gids) {
        setSimpleValue("gid", MongoUtils.convert(gids));
    }

    public List<ObjectId> getSubjectList() {
        List<ObjectId> retList = new ArrayList<ObjectId>();
        BasicDBList list = (BasicDBList) getSimpleObjectValue("sub");
        if (null != list && !list.isEmpty()) {
            for (Object o : list) {
                retList.add(((ObjectId) o));
            }
        }
        return retList;
    }

    public void setSubjectList(List<ObjectId> subjectList) {
        setSimpleValue("sub", MongoUtils.convert(subjectList));
    }
}
