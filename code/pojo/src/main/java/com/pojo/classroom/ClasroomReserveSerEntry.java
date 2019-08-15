package com.pojo.classroom;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by albin on 2017/12/18.
 * nm 组名
 * us 服务人员
 * si 学校id
 */
public class ClasroomReserveSerEntry extends BaseDBObject {

    public ClasroomReserveSerEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public ClasroomReserveSerEntry(ObjectId schoolID, List<ObjectId> userList, String nm) {
        super();
        BasicDBObject basicDBObject = new BasicDBObject()
                .append("si", schoolID)
                .append("users", MongoUtils.convert(userList))
                .append("nm", nm)
                .append("ir", 0);
        setBaseEntry(basicDBObject);
    }

    public ObjectId getSchoolID() {
        return getSimpleObjecIDValue("si");
    }

    public void setSchoolID(ObjectId schoolID) {
        setSimpleValue("si", schoolID);
    }

    public String getName() {
        return getSimpleStringValue("nm");
    }

    public void setName(String name) {
        setSimpleValue("nm", name);
    }


    public List<ObjectId> getUserList() {
        List<ObjectId> retList = new ArrayList<ObjectId>();
        BasicDBList list = (BasicDBList) getSimpleObjectValue("users");
        if (null != list && !list.isEmpty()) {
            for (Object o : list) {
                retList.add((ObjectId) o);
            }
        }
        return retList;
    }

    public void setUserList(List<ObjectId> userList) {
        setSimpleValue("users", MongoUtils.convert(userList));
    }

}
