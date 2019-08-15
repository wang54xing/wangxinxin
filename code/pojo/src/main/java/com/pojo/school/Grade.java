package com.pojo.school;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
/**
 * 年级信息，依附于SchoolEntry
 * <pre>
 * {
 *  gid:年级ID
 *  nm:年级名称
 *  ty:年级类型
 *  ld:年级组长
 *  cld:备课组长
 * }
 * </pre>
 * @author fourer
 */
public class Grade extends BaseDBObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8302708871471580618L;
	
    
	public Grade(BasicDBObject baseEntry) {
		super(baseEntry);
	}


	public Grade(String name, int gradeType,ObjectId leader,ObjectId cleader) {
		super();
		BasicDBObject baseEntry =new BasicDBObject()
		.append("gid", new ObjectId())
		.append("nm", name)
		.append("ty", gradeType)
		.append("ld", leader)
		.append("cld",cleader)
		;
		setBaseEntry(baseEntry);
	}
	
	
	public Grade(ObjectId gid,String name, int gradeType,ObjectId leader,ObjectId cleader) {
		super();
		BasicDBObject baseEntry =new BasicDBObject()
		.append("gid", gid)
		.append("nm", name)
		.append("ty", gradeType)
		.append("ld", leader)
		.append("cld",cleader)
		;
		setBaseEntry(baseEntry);
	}
	
	
	//private ObjectId leader;
	//private String attribute;
	
	public ObjectId getLeader() {
		return getSimpleObjecIDValue("ld");
	}


	public void setLeader(ObjectId leader) {
		setSimpleValue("ld", leader);
	}

	public ObjectId getCleader() {
		return getSimpleObjecIDValue("cld");
	}


	public void setCleader(ObjectId cleader) {
		setSimpleValue("cld", cleader);
	}

	public ObjectId getGradeId() {
		return getSimpleObjecIDValue("gid");
	}
	public void setGradeId(ObjectId gradeId) {
		setSimpleValue("gid", gradeId);
	}
	public String getName() {
		return getSimpleStringValue("nm");
	}
	public void setName(String name) {
		setSimpleValue("nm", name);
	}
	public int getGradeType() {
		return getSimpleIntegerValue("ty");
	}
	public void setGradeType(int gradeType) {
		setSimpleValue("ty", gradeType);
	}
    
}
