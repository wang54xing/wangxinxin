package com.pojo.school;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;

/**
 * 学校部门文件
 * <pre>
 * {
 *  id:
 *  nm:名字
 *  ui:描述
 *  pa:路径
 *  si:大小
 * }
 * </pre>
 * @author fourer
 */
public class DepartmentFile extends BaseDBObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2249742195914718982L;
	
	
	
	
	public DepartmentFile(BasicDBObject baseEntry) {
		super(baseEntry);
		// TODO Auto-generated constructor stub
	}


	public DepartmentFile(String name,ObjectId userId,String path,long size)
	{
		BasicDBObject dbo =new BasicDBObject()
		.append("id", new ObjectId())
		.append("nm", name)
		.append("ui", userId)
		.append("pa", path)
		.append("si", size)
		;
		setBaseEntry(dbo);
	}


	public String getName() {
		return getSimpleStringValue("nm");
	}


	public void setName(String name) {
		setSimpleValue("nm", name);
	}


	public ObjectId getUserId() {
		return getSimpleObjecIDValue("ui");
	}


	public void setUserId(ObjectId userId) {
		setSimpleValue("ui", userId);
	}
	public ObjectId getId() {
		return getSimpleObjecIDValue("id");
	}

	public void setId(ObjectId id) {
		setSimpleValue("id", id);
	}

	public String getPath() {
		return getSimpleStringValue("pa");
	}

	public void setPath(String path) {
		setSimpleValue("pa", path);
	}
	public Long getSize() {
		return getSimpleLongValue("si");
	}
	public void setSize(Long size) {
		setSimpleValue("si", size);
	}
	
	
}
