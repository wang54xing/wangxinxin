package com.pojo.new33.isolate;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
/**
 * @author rick
 */
public class N33_ClassroomEntry extends BaseDBObject {

	private static final long serialVersionUID = 8267892087661520354L;
	
	public N33_ClassroomEntry() {
    }

    public N33_ClassroomEntry(DBObject dbo) {
        this((BasicDBObject) dbo);
    }

    public N33_ClassroomEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public N33_ClassroomEntry(ObjectId xqid, ObjectId schoolId,ObjectId gradeId,ObjectId roomId, String roomName,String description, Integer capacity,Integer type,
                          ObjectId classId, String className, Integer arrange) {
        super();
        BasicDBObject dbo = new BasicDBObject()
                .append("sid", schoolId)
                .append("xqid", xqid)
                .append("grade", gradeId)
                .append("roomid", roomId)
                .append("roomname", roomName)// 教室名称
                .append("desc", description) // 描述
                .append("capacity", capacity)
                .append("type",type) // 教室类型(教室，操场，功能教室)
                .append("classid", classId)// 班级id
                .append("arrange", arrange) //是否可排课
                .append("classname", className);
        setBaseEntry(dbo);
    }

    
    public ObjectId getSchoolId() {
        return getSimpleObjecIDValue("sid");
    }

    public void setSchoolId(ObjectId schoolId) {
        setSimpleValue("sid", schoolId);
    }

    public void setXQId(ObjectId xqid) {
        setSimpleValue("xqid", xqid);
    }
    public ObjectId getXQId() {
        return getSimpleObjecIDValue("xqid");
    }
    
    public void setGradeId(ObjectId grade) {
        setSimpleValue("grade", grade);
    }
    public ObjectId getGradeId() {
        return getSimpleObjecIDValue("grade");
    }
    public void setRoomId(ObjectId roomid) {
        setSimpleValue("roomid", roomid);
    }
    public ObjectId getRoomId() {
        return getSimpleObjecIDValue("roomid");
    }
    public ObjectId getClassId() {
        return getSimpleObjecIDValue("classid");
    }

    public void setClassId(ObjectId cid) {
        setSimpleValue("classid", cid);
    }

   

    public String getClassName() {
        return getSimpleStringValue("classname");
    }
    public void setClassName(String cnm) {
        setSimpleValue("classname", cnm);
    }

    public String getRoomName() {
        return getSimpleStringValue("roomname");
    }

    public void setRoomName(String nm) {
        setSimpleValue("roomname", nm);
    }

    public String getDescription() {
        return getSimpleStringValue("desc");
    }

    public void setDescription(String desc) {
        setSimpleValue("desc", desc);
    }

    public Integer getCapacity() {

    		 return getSimpleIntegerValueDef("capacity",0);

    }

    public void setCapacity(Integer capacity) {
        setSimpleValue("capacity", capacity);
    }

    public Integer getType() {
            return getSimpleIntegerValueDef("type",1);
    }

    public void setType(Integer type) {
        setSimpleValue("type", type);
    }

    public Integer getArrange() {
    	try {
    		 return getSimpleIntegerValue("arrange");
    	}
   	catch (Exception e) {
			return null;
		}
       
    }

    public void setArrange(Integer arrange) {
        setSimpleValue("arrange", arrange);
    }


}
