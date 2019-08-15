package com.pojo.newschool;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wang_xinxin on 2017/1/15.
 */
public class SubjectEntry extends BaseDBObject {
    private static final long serialVersionUID = 4971398085429536346L;

    public SubjectEntry(DBObject dbo) {
        this((BasicDBObject)dbo);
    }

    public SubjectEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public SubjectEntry(ObjectId schoolId, String subjectName, ObjectId leadUser, List<ObjectId> userList) {
        super();
        BasicDBObject dbo =new BasicDBObject()
                .append("sid", schoolId)
                .append("snm", subjectName)//学科名称
                .append("luid", leadUser)//学科组长
                .append("suids", MongoUtils.convert(userList))//学科组成员
                .append("ir", Constant.ZERO);
        setBaseEntry(dbo);
    }

    public ObjectId getSchoolId() {
        return getSimpleObjecIDValue("sid");
    }

    public void setSchoolId(ObjectId schoolId) {
        setSimpleValue("sid", schoolId);
    }

    public String getSubjectName() {
        return getSimpleStringValue("snm");
    }

    public void setSubjectName(String subjectName) {
        setSimpleValue("snm",subjectName);
    }

    public ObjectId getLeadUser() {
        return getSimpleObjecIDValue("luid");
    }

    public void setLeadUser(ObjectId leadUser) {
        setSimpleValue("luid",leadUser);
    }

    public List<ObjectId> getUserList() {
        List<ObjectId> retList =new ArrayList<ObjectId>();
        BasicDBList list =(BasicDBList)getSimpleObjectValue("suids");
        if(null!=list && !list.isEmpty())
        {
            for(Object o:list)
            {
                retList.add(((ObjectId)o));
            }
        }
        return retList;
    }

    public void setUserList(List<ObjectId> userList) {
        setSimpleValue("suids", MongoUtils.convert(userList));
    }
}
