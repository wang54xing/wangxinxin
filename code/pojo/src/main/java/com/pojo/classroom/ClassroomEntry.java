package com.pojo.classroom;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

/**
 * 教室表
 * {
 * sid:学校id----------->schoolId,
 * nm:教室名----------->className
 * cid:班级id--------->classId
 * }
 * Created by qiangm on 2015/10/10.
 */
public class ClassroomEntry extends BaseDBObject {
    public ClassroomEntry() {
        super();
    }

    public ClassroomEntry(BasicDBObject basicDBObject) {
        super(basicDBObject);
    }

    public ClassroomEntry(ObjectId schoolId, String name, ObjectId classId) {
        super();
        BasicDBObject baseEntry = new BasicDBObject()
                .append("sid", schoolId)
                .append("nm", name)
                .append("cid", classId);
        setBaseEntry(baseEntry);
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

    public void setClassId(ObjectId classId) {
        setSimpleValue("cid", classId);
    }

    public String getRoomName() {
        return getSimpleStringValue("nm");
    }

    public void setRoomName(String name) {
        setSimpleValue("nm", name);
    }
}
