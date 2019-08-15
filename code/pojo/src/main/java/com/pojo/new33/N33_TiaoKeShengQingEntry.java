package com.pojo.new33;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

/**
 * Created by albin on 2018/8/3.
 */
public class N33_TiaoKeShengQingEntry extends BaseDBObject {
    private static final long serialVersionUID = -7114952971835593896L;

    public N33_TiaoKeShengQingEntry(BasicDBObject baseEntry) {
        setBaseEntry(baseEntry);
    }

    public N33_TiaoKeShengQingEntry() {
    }

    public N33_TiaoKeShengQingEntry(ObjectId sid, ObjectId xqid, Integer x, Integer y, ObjectId jxbId, ObjectId njxbId, Integer sta,Integer nx,Integer ny,ObjectId teaId,Integer week) {
        BasicDBObject dbObject = new BasicDBObject()
                .append("sid", sid)
                .append("xqid", xqid)
                .append("sta", sta)
                .append("teaId",teaId)
                .append("jxbId", jxbId)
                .append("njxbId",njxbId)
                .append("y", y)
                .append("x", x)
                .append("ny", ny)
                .append("nx", nx)
                .append("week",week);
        setBaseEntry(dbObject);
    }

    public ObjectId getSchoolId() {
        return getSimpleObjecIDValue("sid");
    }

    public void setSchoolId(ObjectId sid) {
        setSimpleValue("sid", sid);
    }

    public ObjectId getXqId() {
        return getSimpleObjecIDValue("xqid");
    }

    public void setXqId(ObjectId xqid) {
        setSimpleValue("xqid", xqid);
    }

    public ObjectId getJxbId() {
        return getSimpleObjecIDValue("jxbId");
    }

    public void setJxbId(ObjectId jxbId) {
        setSimpleValue("jxbId", jxbId);
    }

    public ObjectId getNJxbId() {
        return getSimpleObjecIDValue("njxbId");
    }

    public void setTeaId(ObjectId teaId) {
        setSimpleValue("teaId", teaId);
    }


    public ObjectId getTeaId() {
        return getSimpleObjecIDValue("teaId");
    }

    public void setNJxbId(ObjectId njxbId) {
        setSimpleValue("njxbId", njxbId);
    }

    public void setSta(Integer sta) {
        setSimpleValue("sta", sta);
    }

    public Integer getSta() {
        return getSimpleIntegerValue("sta");
    }

    public void setX(Integer x) {
        setSimpleValue("x", x);
    }

    public Integer getX() {
        return getSimpleIntegerValue("x");
    }

    public void setY(Integer y) {
        setSimpleValue("y", y);
    }

    public Integer getY() {
        return getSimpleIntegerValue("y");
    }


    public void setNX(Integer nx) {
        setSimpleValue("nx", nx);
    }

    public Integer getNX() {
        return getSimpleIntegerValue("nx");
    }

    public void setNY(Integer ny) {
        setSimpleValue("ny", ny);
    }

    public Integer getNY() {
        return getSimpleIntegerValue("ny");
    }


    public void setWeek(Integer week) {
        setSimpleValue("week", week);
    }

    public Integer getWeek() {
        return getSimpleIntegerValue("week");
    }
}
