package com.pojo.school;

import java.util.ArrayList;
import java.util.List;
import org.bson.types.ObjectId;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;

/**
 * 学校部门信息
 * <pre>
 * collectionName:departments
 * </pre>
 * <pre>
 * {
 *  sid:学校ID
 *  nm:名字
 *  des:描述
 *  ma:部门主管
 *  mems[]:
 *  fs: //文件
 *  [
	 *  {
	 *  id:
	 *  nm:名字
	 *  ui:描述
	 *  pa:路径
	 * }
 *  ]
 * }
 * </pre>
 * @author fourer
 */
public class DepartmentEntry extends BaseDBObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9040324823644915540L;
	

	
	public DepartmentEntry(BasicDBObject baseEntry) {
		super(baseEntry);
		// TODO Auto-generated constructor stub
	}

	public DepartmentEntry(ObjectId schoolId,  String name, String des, ObjectId masterID,List<ObjectId> members
			 ) {
		BasicDBObject dbo =new BasicDBObject()
		.append("sid", schoolId)
		.append("nm", name)
		.append("des", des)
		.append("ma", masterID)
		.append("mems", MongoUtils.convert(members))
		.append("fs", new BasicDBList())
		;
		setBaseEntry(dbo);
		}

	public ObjectId getSchoolId() {
		return getSimpleObjecIDValue("sid");
	}

	public void setSchoolId(ObjectId schoolId) {
		setSimpleValue("sid", schoolId);
	}

	public String getName() {
		return getSimpleStringValue("nm");
	}

	public void setName(String name) {
		setSimpleValue("nm", name);
	}

	public String getDes() {
		return getSimpleStringValue("des");
	}

	public void setDes(String des) {
		setSimpleValue("des", des);
	}

	public ObjectId getMaster() {
		return getSimpleObjecIDValue("ma");
	}

	public void setMaster(ObjectId master) {
		setSimpleValue("ma", master);
	}

	public List<ObjectId> getMembers() {
		List<ObjectId> retList =new ArrayList<ObjectId>();
		BasicDBList list =(BasicDBList)getSimpleObjectValue("mems");
		if(null!=list && !list.isEmpty())
		{
			for(Object o:list)
			{
				retList.add((ObjectId)o);
			}
		}
		return retList;
	}

	public void setMembers(List<ObjectId> members) {
		setSimpleValue("mems", MongoUtils.convert(members));
	}
	
	
	public List<DepartmentFile> getDepartmentFiles() {
		List<DepartmentFile> retList =new ArrayList<DepartmentFile>();
		BasicDBList list =(BasicDBList)getSimpleObjectValue("fs");
		if(null!=list && !list.isEmpty())
		{
			for(Object o:list)
			{
				retList.add(new DepartmentFile((BasicDBObject)o));
			}
		}
		return retList;
	}

	public void setDepartmentFiles(List<DepartmentFile> members) {
		setSimpleValue("fs", MongoUtils.convert(members));
	}
	

}
