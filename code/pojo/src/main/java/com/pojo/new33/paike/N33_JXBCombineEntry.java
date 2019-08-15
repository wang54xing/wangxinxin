package com.pojo.new33.paike;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by albin on 2018/7/30.
 * 教学班组合
 *
 * JxbList
 * [
 *   jxbId:教学班ID
 *   roomName:教室名
 *   teaName:教师名
 *   IntersectJXB[
 *         intersectJxbId:和jxbId相交的教学班
 *         intersectRoomName:教室名
 *         intersectTeaName:教师名
 *         IsNoCTJXB[
 *              jxbId:教学班ID
 *              oJxbId:该组合中另外一个教学班Id
 *              flag:是否冲突   false : 不冲突
 *         ]
 *   ]
 * ]
 * sid:学校id
 * xqid:排课次id
 * gid:年级id
 * type 1等级    2合格     3行政班
 * name 组合名
 */
public class N33_JXBCombineEntry extends BaseDBObject {


	private static final long serialVersionUID = 2502264815376663342L;

	public N33_JXBCombineEntry(BasicDBObject baseEntry) {
		setBaseEntry(baseEntry);
	}

	public N33_JXBCombineEntry() {
	}

	public N33_JXBCombineEntry(List<JxbList> jxbList, ObjectId sid, ObjectId xqid, ObjectId gid, Integer type,String name) {
		BasicDBObject dbObject = new BasicDBObject()
				.append("sid", sid)
				.append("xqid", xqid)
				.append("gradeId", gid)
				.append("jxbList", MongoUtils.convert(MongoUtils.fetchDBObjectList(jxbList)))
				.append("type",type)
				.append("name",name);
		setBaseEntry(dbObject);
	}

	public void setType(Integer type){
		setSimpleValue("type", type);
	}
	public Integer getType(){
		return getSimpleIntegerValue("type");
	}
	public ObjectId getSchoolId() {
		return getSimpleObjecIDValue("sid");
	}
	public void setSchoolId(ObjectId sid) {
		setSimpleValue("sid", sid);
	}
	public ObjectId getXqId() {
		return getSimpleObjecIDValue("xqid");
	}
	public void setXqId(ObjectId xqid) {
		setSimpleValue("xqid", xqid);
	}
	public String getName() {
		return getSimpleStringValue("name");
	}
	public void setName(String name) {
		setSimpleValue("name", name);
	}

	public ObjectId getGradeId() {
		return getSimpleObjecIDValue("gradeId");
	}
	public void setGradeId(ObjectId gid) {
		setSimpleValue("gradeId", gid);
	}

	public void setJxbList(List<JxbList> jxbList){
		List<DBObject> list = MongoUtils.fetchDBObjectList(jxbList);
		setSimpleValue("jxbList",  MongoUtils.convert(list));
	}

	public List<JxbList> getJxbList(){
		List<JxbList> retList =new ArrayList<JxbList>();
		BasicDBList list =(BasicDBList)getSimpleObjectValue("jxbList");
		if(null!=list && !list.isEmpty())
		{
			for(Object o:list)
			{
				retList.add(new JxbList((BasicDBObject)o));
			}
		}
		return retList;
	}

	/**
	 * 内部类
	 * 完全冲突的教学班
	 */
	public static class JxbList extends BaseDBObject {
		public JxbList() {
		}

		public JxbList(BasicDBObject baseEntry) {
			super(baseEntry);
		}

		public JxbList(ObjectId jxbId, String roomName, String teaName, List<IntersectJXB> intersectJXB) {
			super();
			BasicDBObject dbo = new BasicDBObject()
					.append("jxbId", jxbId)
					.append("roomName", roomName)
					.append("teaName", teaName)
					.append("intersectJXB", MongoUtils.convert(MongoUtils.fetchDBObjectList(intersectJXB)));
			setBaseEntry(dbo);
		}

		public void setJxbId(ObjectId jxbId){
			setSimpleValue("jxbId", jxbId);
		}
		public ObjectId getJxbId(){
			return getSimpleObjecIDValue("jxbId");
		}
		public String getRoomName() {
			return getSimpleStringValue("roomName");
		}
		public void setRoomName(String roomName) {
			setSimpleValue("roomName", roomName);
		}
		public String getTeaName() {
			return getSimpleStringValue("teaName");
		}
		public void setTeaName(String teaName) {
			setSimpleValue("teaName", teaName);
		}

		public void setIntersectJXB(List<IntersectJXB> intersectJXB){
			List<DBObject> list = MongoUtils.fetchDBObjectList(intersectJXB);
			setSimpleValue("intersectJXB",  MongoUtils.convert(list));
		}

		public List<IntersectJXB> getIntersectJXB(){
			List<IntersectJXB> retList =new ArrayList<IntersectJXB>();
			BasicDBList list =(BasicDBList)getSimpleObjectValue("intersectJXB");
			if(null!=list && !list.isEmpty())
			{
				for(Object o:list)
				{
					retList.add(new IntersectJXB((BasicDBObject)o));
				}
			}
			return retList;
		}
	}


	/**
	 *  内部类
	 *  每一个完全相交的教学班对应的部分相交的教学班
	 */
	public static class IntersectJXB extends BaseDBObject{
		public IntersectJXB() {
		}

		public IntersectJXB(BasicDBObject baseEntry) {
			super(baseEntry);
		}

		public IntersectJXB(ObjectId intersectJxbId,String intersectRoomName,String intersectTeaName,List<IsNoCTJXB> isNoCTJXB) {
			BasicDBObject dbObject = new BasicDBObject()
					.append("intersectJxbId", intersectJxbId)
					.append("intersectRoomName", intersectRoomName)
					.append("intersectTeaName", intersectTeaName)
					.append("intersectJXB", MongoUtils.convert(MongoUtils.fetchDBObjectList(isNoCTJXB)));
			setBaseEntry(dbObject);
		}

		public void setIntersectJxbId(ObjectId intersectJxbId){
			setSimpleValue("intersectJxbId", intersectJxbId);
		}
		public ObjectId getIntersectJxbId(){
			return getSimpleObjecIDValue("intersectJxbId");
		}
		public String getIntersectRoomName() {
			return getSimpleStringValue("intersectRoomName");
		}
		public void setIntersectRoomName(String intersectRoomName) {
			setSimpleValue("intersectRoomName", intersectRoomName);
		}
		public String getIntersectTeaName() {
			return getSimpleStringValue("intersectTeaName");
		}
		public void setIntersectTeaName(String intersectTeaName) {
			setSimpleValue("intersectTeaName", intersectTeaName);
		}

		public void setIsNoCTJXB(List<IsNoCTJXB> isNoCTJXB){
			List<DBObject> list = MongoUtils.fetchDBObjectList(isNoCTJXB);
			setSimpleValue("isNoCTJXB",  MongoUtils.convert(list));
		}

		public List<IsNoCTJXB> getIsNoCTJXB(){
			List<IsNoCTJXB> retList =new ArrayList<IsNoCTJXB>();
			BasicDBList list =(BasicDBList)getSimpleObjectValue("isNoCTJXB");
			if(null!=list && !list.isEmpty())
			{
				for(Object o:list)
				{
					retList.add(new IsNoCTJXB((BasicDBObject)o));
				}
			}
			return retList;
		}
	}

	/**
	 *  内部类
	 *  每一个完全相交的教学班对应的部分相交的教学班
	 */
	public static class IsNoCTJXB extends BaseDBObject{
		public IsNoCTJXB() {
		}

		public IsNoCTJXB(BasicDBObject baseEntry) {
			super(baseEntry);
		}

		public IsNoCTJXB(ObjectId jxbId,ObjectId oJxbId,boolean flag) {
			BasicDBObject dbObject = new BasicDBObject()
					.append("jxbId", jxbId)
					.append("oJxbId", oJxbId)
					.append("flag", flag);
			setBaseEntry(dbObject);
		}

		public void setJxbId(ObjectId jxbId){
			setSimpleValue("jxbId", jxbId);
		}
		public ObjectId getJxbId(){
			return getSimpleObjecIDValue("jxbId");
		}
		public void setoJxbId(ObjectId oJxbId){
			setSimpleValue("oJxbId", oJxbId);
		}
		public ObjectId getoJxbId(){
			return getSimpleObjecIDValue("oJxbId");
		}

		public void setFlag(Boolean flag){
			setSimpleValue("flag", flag);
		}
		public Boolean getFlag(){
			return getSimpleBoolean("flag");
		}
	}

}
