package com.pojo.dzbp;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

/**
 * 班牌和班级关系
 * {
 * 		sid:学校id（schoolId）
 *      clientId:班牌id(clientId)
 *      classId:班级id(classId)
 * }
 * @author rick
 *
 */
public class ClientClassRelaEntry extends BaseDBObject {

    private static final long serialVersionUID = -2168263101872219121L;


    public ClientClassRelaEntry(DBObject dbo) {
        this((BasicDBObject) dbo);
    }

    public ClientClassRelaEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public ClientClassRelaEntry() {

    }

    public ClientClassRelaEntry(ObjectId schoolId, String clientId, ObjectId classId) {
        BasicDBObject baseEntry = new BasicDBObject()
                .append("sid", schoolId)
                .append("clientId", clientId)
                .append("classId", classId);
        setBaseEntry(baseEntry);
    }

    public ObjectId getSchoolId() {
        return getSimpleObjecIDValue("sid");
    }

    public void setSchoolId(ObjectId schoolId) {
        setSimpleValue("sid", schoolId);
    }

    public String getClientId() {
        return getSimpleStringValue("clientId");
    }

    public void setClientId(String clientId) {
        setSimpleValue("clientId", clientId);
    }

    public ObjectId getClassId() {
        return getSimpleObjecIDValue("classId");
    }

    public void setClassId(ObjectId classId) {
        setSimpleValue("classId", classId);
    }

}
