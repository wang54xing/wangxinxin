package com.db.school;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.school.DepartmentEntry;
import com.pojo.school.DepartmentFile;
import com.sys.constants.Constant;

/**
 * 学校部门操作类
 * @author fourer
 *
 */
public class DepartmentDao extends BaseDao {

	/**
	 * 添加
	 * @param e
	 * @return
	 */
	public ObjectId addDepartmentEntry(DepartmentEntry e)
	{
		save(MongoFacroty.getAppDB(), Constant.COLLECTION_DEPARTMENT, e.getBaseEntry());
		return e.getID();
	}
	
	/**
	 * 部门详情
	 * @param id
	 * @return
	 */
	public DepartmentEntry getDepartmentEntry(ObjectId id)
	{
		DBObject query =new BasicDBObject(Constant.ID,id);
		DBObject dbo =findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_DEPARTMENT, query, Constant.FIELDS);
		if(null!=dbo)
		{
			return new DepartmentEntry((BasicDBObject)dbo);
		}
		return null;
	}
	
	
	/**
	 * 部门详情
	 * @param id
	 * @return
	 */
	@Deprecated
	public DepartmentEntry getDepartmentEntry(ObjectId sid,String name)
	{
		DBObject query =new BasicDBObject("sid",sid).append("nm", name);
		DBObject dbo =findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_DEPARTMENT, query, Constant.FIELDS);
		if(null!=dbo)
		{
			return new DepartmentEntry((BasicDBObject)dbo);
		}
		return null;
	}
	
	
	
	/**
	 * 根据部门文件查询部门
	 * @param id
	 * @return
	 */
	public DepartmentEntry getDepartmentEntryByFileId(ObjectId fileid)
	{
		DBObject query =new BasicDBObject("fs.id",fileid);
		DBObject dbo =findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_DEPARTMENT, query, Constant.FIELDS);
		if(null!=dbo)
		{
			return new DepartmentEntry((BasicDBObject)dbo);
		}
		return null;
	}
	
	/**
	 * 删除部门
	 * @param id
	 */
	public void removeDepartmentEntry(ObjectId id)
	{
		DBObject query =new BasicDBObject(Constant.ID,id);
		remove(MongoFacroty.getAppDB(), Constant.COLLECTION_DEPARTMENT, query);
	}
	
	
	
	/**
	 * 通过学校ID查询所有部门
	 * @param sid
	 * @return
	 */
	public List<DepartmentEntry> getDepartmentEntrys(ObjectId sid)
	{
		List<DepartmentEntry> retList =new ArrayList<DepartmentEntry>();
		DBObject query =new BasicDBObject("sid",sid);
		List<DBObject> list=find(MongoFacroty.getAppDB(), Constant.COLLECTION_DEPARTMENT, query, new BasicDBObject("fs",0));
		if(null!=list && !list.isEmpty())
		{
			DepartmentEntry e;
			for(DBObject dbo:list)
			{
				e=new DepartmentEntry((BasicDBObject)dbo);
				retList.add(e);
			}
		}
		return retList;
	}
	/**
	 * 添加一个成员
	 * @param id
	 * @param memberId
	 */
	public void addMember(ObjectId id,ObjectId memberId)
	{
		DBObject query =new BasicDBObject(Constant.ID,id);
		DBObject updateValue =new BasicDBObject(Constant.MONGO_ADDTOSET,new BasicDBObject("mems",memberId));
		update(MongoFacroty.getAppDB(), Constant.COLLECTION_DEPARTMENT, query, updateValue);
	}
	/**
	 * 删除成员
	 * @param id
	 * @param memberId
	 */
	public void deleteMember(ObjectId id,ObjectId memberId)
	{
		DBObject query =new BasicDBObject(Constant.ID,id);
		DBObject updateValue =new BasicDBObject(Constant.MONGO_PULL,new BasicDBObject("mems",memberId));
		update(MongoFacroty.getAppDB(), Constant.COLLECTION_DEPARTMENT, query, updateValue);
	}
	
	/**
	 * 添加一个文件
	 * @param id
	 * @param file
	 */
	public void addDepartmentFile(ObjectId id,DepartmentFile file)
	{
		DBObject query =new BasicDBObject(Constant.ID,id);
		DBObject updateValue =new BasicDBObject(Constant.MONGO_PUSH,new BasicDBObject("fs",file.getBaseEntry()));
		update(MongoFacroty.getAppDB(), Constant.COLLECTION_DEPARTMENT, query, updateValue);
	}
	
	/**
	 * 删除一个文件
	 * @param id
	 * @param file
	 */
	public void removeDepartmentFile(ObjectId id,DepartmentFile file)
	{
		DBObject query =new BasicDBObject(Constant.ID,id);
		DBObject updateValue =new BasicDBObject(Constant.MONGO_PULL,new BasicDBObject("fs",file.getBaseEntry()));
		update(MongoFacroty.getAppDB(), Constant.COLLECTION_DEPARTMENT, query, updateValue);
	}
	
	
	/**
	 * 查询该用户所处的所有部门
	 * @param userId
	 * @return
	 */
	public List<DepartmentEntry> getDepartmentsByUserId(ObjectId userId)
	{
		List<DepartmentEntry> retList =new ArrayList<DepartmentEntry>();
		DBObject query =new BasicDBObject("mems",userId);
		List<DBObject> list=find(MongoFacroty.getAppDB(), Constant.COLLECTION_DEPARTMENT, query, Constant.FIELDS);
		if(null!=list && !list.isEmpty())
		{
			DepartmentEntry e;
			for(DBObject dbo:list)
			{
				e=new DepartmentEntry((BasicDBObject)dbo);
				retList.add(e);
			}
		}
		return retList;
	}

	/**
	 * 根据部门id列表批量获取部门
	 * add by qiangm
	 * @param departmentIds
	 * @return
	 */
	public List<DepartmentEntry> getDepartmentsByDepIds(List<ObjectId> departmentIds)
	{
		List<DepartmentEntry> retList =new ArrayList<DepartmentEntry>();
		DBObject query =new BasicDBObject(Constant.ID,new BasicDBObject(Constant.MONGO_IN,departmentIds));
		List<DBObject> list=find(MongoFacroty.getAppDB(), Constant.COLLECTION_DEPARTMENT, query, Constant.FIELDS);
		if(null!=list && !list.isEmpty())
		{
			DepartmentEntry e;
			for(DBObject dbo:list)
			{
				e=new DepartmentEntry((BasicDBObject)dbo);
				retList.add(e);
			}
		}
		return retList;
	}
}
