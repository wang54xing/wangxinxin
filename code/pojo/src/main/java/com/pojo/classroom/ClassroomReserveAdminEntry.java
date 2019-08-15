package com.pojo.classroom;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

/**
 * Created by albin on 2017/12/14.
 * sid 学校id
 * uid 用户id
 */
public class ClassroomReserveAdminEntry extends BaseDBObject {

    public ClassroomReserveAdminEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public ClassroomReserveAdminEntry(ObjectId schoolID, ObjectId userId) {
        super();
        BasicDBObject basicDBObject = new BasicDBObject()
                .append("si", schoolID)
                .append("uid", userId)
                .append("ir", 0);
        setBaseEntry(basicDBObject);
    }

    public ObjectId getSchoolID() {
        return getSimpleObjecIDValue("si");
    }

    public void setSchoolID(ObjectId schoolID) {
        setSimpleValue("si", schoolID);
    }

    public ObjectId getUserId() {
        return getSimpleObjecIDValue("uid");
    }

    public void setUserId(ObjectId userid) {
        setSimpleValue("uid", userid);
    }
}
