package com.pojo.classroom;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

/**
 * Created by albin on 2017/12/14.
 * sid 学校id
 * roomId 教室 id
 * st 0面审核 1面预约
 * fid 教室负责人id
 */
public class NotClassroomReserveEntry extends BaseDBObject {
    public NotClassroomReserveEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public NotClassroomReserveEntry(ObjectId schoolID, ObjectId classRoomID, String crnm, Integer sta, ObjectId fid) {
        super();
        BasicDBObject basicDBObject = new BasicDBObject()
                .append("si", schoolID)
                .append("fid", fid)
                .append("crid", classRoomID)
                .append("crnm", crnm)
                .append("st", sta);
        setBaseEntry(basicDBObject);
    }

    public ObjectId getSchoolID() {
        return getSimpleObjecIDValue("si");
    }

    public void setSchoolID(ObjectId schoolID) {
        setSimpleValue("si", schoolID);
    }


    public ObjectId getFID() {
        return getSimpleObjecIDValue("fid");
    }

    public void setFID(ObjectId fid) {
        setSimpleValue("fid", fid);
    }

    public ObjectId getClassRoomID() {
        return getSimpleObjecIDValue("crid");
    }

    public void setClassRoomID(ObjectId classRoomID) {
        setSimpleValue("crid", classRoomID);
    }


    public String getClassRoomName() {
        return getSimpleStringValue("crnm");
    }

    public void setClassRoomName(String classRoomName) {
        setSimpleValue("crnm", classRoomName);
    }


    public Integer getSta() {
        return getSimpleIntegerValueDef("st", 2);
    }

    public void setSta(String sta) {
        setSimpleValue("st", sta);
    }
}
