package com.pojo.classroom;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

/**
 * 教室预定entry
 * <pre>
 * collection:crreserve
 * </pre>
 * <pre>
 * {
 *  si:学校
 *  crid:教室ID
 *  tti:课程Id
 *  ti:上课时间
 *  ui:预订人
 *  des:预定说明
 *  
 *  chui:审核人
 *  st:状态  0 没有审核 1审核通过 2拒绝 
 *  res:说明
 *  ir:是否取消 0 没有 1取消
 * }
 * </pre>
 * @author fourer
 *
 */
public class ClassroomReserveEntry extends BaseDBObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    
    
	public ClassroomReserveEntry(BasicDBObject baseEntry) {
		super(baseEntry);
	}
	
	public ClassroomReserveEntry(ObjectId schoolID, ObjectId classRoomID,
								 ObjectId timeTableID, long time, ObjectId userId, String des,
								 int state, ObjectId chechUserID, String result) {
		    super();
	        BasicDBObject basicDBObject = new BasicDBObject()
	                .append("si", schoolID)
	                .append("crid", classRoomID)
	                .append("tti", timeTableID)
	                .append("ti", time)
	                .append("ui",userId)
	                .append("des", des)
	                .append("chui", chechUserID)
	                .append("st", state)
	                .append("res", result)
	                .append("ir", 0);
	                ;
	        setBaseEntry(basicDBObject);
	}


	public ObjectId getSchoolID() {
		return getSimpleObjecIDValue("si");
	}
	public void setSchoolID(ObjectId schoolID) {
		setSimpleValue("si", schoolID);
	}
	public ObjectId getClassRoomID() {
		return getSimpleObjecIDValue("crid");
	}
	public void setClassRoomID(ObjectId classRoomID) {
		setSimpleValue("crid", classRoomID);
	}
	public ObjectId getTimeTableID() {
		return getSimpleObjecIDValue("tti");
	}
	public void setTimeTableID(ObjectId timeTableID) {
		setSimpleValue("tti", timeTableID);
	}
	public long getTime() {
		return getSimpleLongValue("ti");
	}
	public void setTime(long time) {
		setSimpleValue("ti", time);
	}
	public ObjectId getUserId() {
		return getSimpleObjecIDValue("ui");
	}
	public void setUserId(ObjectId userId) {
		setSimpleValue("ui", userId);
	}
	public String getDes() {
		return getSimpleStringValue("des");
	}
	public void setDes(String des) {
		setSimpleValue("des", des);
	}
	public int getState() {
		return getSimpleIntegerValue("st");
	}
	public void setState(int state) {
		setSimpleValue("st", state);
	}
	public ObjectId getChechUserID() {
		return getSimpleObjecIDValue("chui");
	}
	public void setChechUserID(ObjectId chechUserID) {
		setSimpleValue("chui", chechUserID);
	}
	public String getResult() {
		return getSimpleStringValue("res");
	}
	public void setResult(String result) {
		setSimpleValue("res", result);
	}

	
	
}
