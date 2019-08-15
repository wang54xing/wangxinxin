package com.pojo.classroom;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

/**
 * Created by albin on 2017/12/19.
 * res 要求
 * sid 学校id
 * reid 预约id
 */
public class ClassroomReserveYaoEntry extends BaseDBObject {
    public ClassroomReserveYaoEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public ClassroomReserveYaoEntry(ObjectId schoolID, ObjectId reid, String res) {
        super();
        BasicDBObject basicDBObject = new BasicDBObject()
                .append("si", schoolID)
                .append("res", res)
                .append("reid", reid)
                .append("ir", 0);
        setBaseEntry(basicDBObject);
    }


    public ObjectId getSchoolID() {
        return getSimpleObjecIDValue("si");
    }

    public void setSchoolID(ObjectId schoolID) {
        setSimpleValue("si", schoolID);
    }

    public String getRes() {
        return getSimpleStringValue("res");
    }

    public void setRes(String res) {
        setSimpleValue("res", res);
    }


    public ObjectId getReID() {
        return getSimpleObjecIDValue("reid");
    }

    public void setReID(ObjectId reid) {
        setSimpleValue("reid", reid);
    }
}
