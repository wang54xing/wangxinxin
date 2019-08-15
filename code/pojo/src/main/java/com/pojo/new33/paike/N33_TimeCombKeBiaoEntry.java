package com.pojo.new33.paike;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hero on 2019/5/20.
 */
public class N33_TimeCombKeBiaoEntry extends BaseDBObject {
    public N33_TimeCombKeBiaoEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public N33_TimeCombKeBiaoEntry() {
    }

    public N33_TimeCombKeBiaoEntry(
            ObjectId schoolId,
            ObjectId gradeId,
            ObjectId ciId,
            int type,
            String serialName,
            int serial,
            int x,
            int y
            ) {
        super();
        BasicDBObject dbo = new BasicDBObject()
                .append("sid", schoolId)
                .append("gradeId", gradeId)
                .append("ciId", ciId)
                .append("type", type)
                .append("serialName", serialName)
                .append("serial", serial)
                .append("x", x)
                .append("y", y);
        setBaseEntry(dbo);
    }

    public ObjectId getSchoolId() {
        return getSimpleObjecIDValue("sid");
    }

    public void setSchoolId(ObjectId schoolId) {
        setSimpleValue("sid", schoolId);
    }

    public ObjectId getGradeId() {
        return getSimpleObjecIDValue("gradeId");
    }

    public void setGradeId(ObjectId gradeId) {
        setSimpleValue("gradeId", gradeId);
    }

    public ObjectId getCiId() {
        return getSimpleObjecIDValue("ciId");
    }

    public void setCiId(ObjectId ciId) {
        setSimpleValue("ciId", ciId);
    }

    public int getType() {
        return getSimpleIntegerValue("type");
    }

    public void setType(int type) {
        setSimpleValue("type", type);
    }

    public String getSerialName() {
        return getSimpleStringValue("serialName");
    }

    public void setSerialName(String serialName) {
        setSimpleValue("serialName", serialName);
    }

    public int getSerial() {
        return getSimpleIntegerValue("serial");
    }

    public void setSerial(int serial) {
        setSimpleValue("serial", serial);
    }

    public int getX() {
        return getSimpleIntegerValue("x");
    }

    public void setX(int x) {
        setSimpleValue("x",x);
    }

    public int getY() {
        return getSimpleIntegerValue("y");
    }

    public void setY(int y) {
        setSimpleValue("y",y);
    }
}
