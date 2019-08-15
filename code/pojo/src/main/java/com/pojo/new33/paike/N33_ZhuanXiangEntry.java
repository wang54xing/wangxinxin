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
 * Created by albin on 2018/5/14.
 * ciId 次id
 * name 名称
 * jid 对应的专项教学班Id
 * stus 学生集合
 * rId  教室Id
 * tId  老师id
 * sid  学校id
 */
public class N33_ZhuanXiangEntry extends BaseDBObject {

    private static final long serialVersionUID = -8469434805717122190L;

    public N33_ZhuanXiangEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public N33_ZhuanXiangEntry() {
    }

    public N33_ZhuanXiangEntry(ObjectId ciId, String name, ObjectId jxbId, ObjectId sid, ObjectId roomid, ObjectId teaId, List<ObjectId> studentId) {
        super();
        BasicDBObject dbo = new BasicDBObject()
                .append("ciId", ciId)
                .append("jxbId", jxbId)
                .append("name", name)
                .append("sid", sid)
                .append("rid", roomid)
                .append("stu", MongoUtils.convert(studentId))
                .append("tid", teaId)
                .append("ir", Constant.ZERO);
        setBaseEntry(dbo);
    }


    public void setName(String name) {
        setSimpleValue("name", name);
    }

    public String getName() {
        return getSimpleStringValue("name");
    }

    public ObjectId getSchoolId() {
        return getSimpleObjecIDValue("sid");
    }

    public void setSchoolId(ObjectId schoolId) {
        setSimpleValue("sid", schoolId);
    }


    public ObjectId getCiId() {
        return getSimpleObjecIDValue("ciId");
    }

    public void setCiId(ObjectId ciId) {
        setSimpleValue("ciId", ciId);
    }

    public ObjectId getJxbId() {
        return getSimpleObjecIDValue("jxbId");
    }

    public void setJxbId(ObjectId jxbId) {
        setSimpleValue("jxbId", jxbId);
    }


    public ObjectId getRoomId() {
        return getSimpleObjecIDValue("rid");
    }

    public void setRoomId(ObjectId roomId) {
        setSimpleValue("rid", roomId);
    }


    public ObjectId getTeaId() {
        return getSimpleObjecIDValue("tid");
    }

    public void setTeaId(ObjectId teaId) {
        setSimpleValue("tid", teaId);
    }

    public List<ObjectId> getStudentId() {
        List<ObjectId> retList = new ArrayList<ObjectId>();
        BasicDBList list = (BasicDBList) getSimpleObjectValue("stu");
        if (null != list && !list.isEmpty()) {
            for (Object o : list) {
                retList.add(((ObjectId) o));
            }
        }
        return retList;
    }

    public void setStudentId(List<ObjectId> studentId) {
        setSimpleValue("stu", MongoUtils.convert(studentId));
    }

}
