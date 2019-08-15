package com.pojo.new33;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;

/**
 * 学校选课设置表
 * {
 *  t：学期
 *  si:学校id
 *  gi：年级id
 *  start   启动时间
 *  end     结束时间
 *  sets：
 *  [
 *   {
 *    id:id标识
 *    nm:名字
 *    s1:学科1
 *    s2:学科2
 *    s3:学科3
 *    st:0启用 1禁止
 *   }
 *  ]
 * }
 * @author fourer
 *
 */
public class SchoolSelectLessonSetEntry extends BaseDBObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7653439466893785071L;

	public SchoolSelectLessonSetEntry(BasicDBObject baseEntry) {
		super(baseEntry);
	}

	
	public SchoolSelectLessonSetEntry(ObjectId term, ObjectId schoolId,
			ObjectId gradeId, List<SelectLessons> sets) {
		super();
		 BasicDBObject dbo =new BasicDBObject()
		 .append("t", term)
		 .append("si", schoolId)
                    .append("gi", gradeId)
                    .append("sets", MongoUtils.convert(MongoUtils.fetchDBObjectList(sets)))
                    ;
        setBaseEntry(dbo);
	}


	public ObjectId getTerm() {
		return getSimpleObjecIDValue("t");
	}

	public void setTerm(ObjectId term) {
		setSimpleValue("t", term);
	}

	public ObjectId getSchoolId() {
		return getSimpleObjecIDValue("si");
	}

	public void setSchoolId(ObjectId schoolId) {
		setSimpleValue("si", schoolId);
	}

	public ObjectId getGradeId() {
		return getSimpleObjecIDValue("gi");
	}

	public void setGradeId(ObjectId gradeId) {
		setSimpleValue("gi", gradeId);
	}
	public Long getStart() {
		try {
			return getSimpleLongValue("start");
		}
		catch (Exception e) {
			return null;
		}
		
	}
	public void setStart(long start) {
		setSimpleValue("start", start);
	}
	
	public Long getEnd() {
		try {
			return getSimpleLongValue("end");
		}
		catch (Exception e) {
			return null;
		}
	}
	public void setEnd(long start) {
		setSimpleValue("start", start);
	}
	public List<SelectLessons> getSets() {
		List<SelectLessons> retList =new ArrayList<SelectLessons>();
		BasicDBList list =(BasicDBList)getSimpleObjectValue("sets");
		if(null!=list && !list.isEmpty())
		{
			for(Object o:list)
			{
				retList.add(new SelectLessons((BasicDBObject)o));
			}
		}
		return retList;
	}

	public void setSets(List<SelectLessons> sets) {
		List<DBObject> list = MongoUtils.fetchDBObjectList(sets);
		setSimpleValue("sets",  MongoUtils.convert(list));
	}

	public static class SelectLessons extends BaseDBObject {
		/**
		 * 
		 */
		private static final long serialVersionUID = 229595397107982279L;

		public SelectLessons(BasicDBObject baseEntry) {
			super(baseEntry);
		}

		public SelectLessons(String id,String name, ObjectId subject1, ObjectId subject2,
				ObjectId subject3,int state) {
			super();
			BasicDBObject baseEntry = new BasicDBObject("id",id).append("nm", name)
					.append("s1", subject1).append("s2", subject2)
					.append("s3", subject3).append("st", state);
			setBaseEntry(baseEntry);
		}

		public SelectLessons(String id,String name, ObjectId subject1, ObjectId subject2,
							 ObjectId subject3,int state,Integer num) {
			super();
			BasicDBObject baseEntry = new BasicDBObject("id",id).append("nm", name)
					.append("s1", subject1).append("s2", subject2)
					.append("s3", subject3).append("st", state).append("num",num);
			setBaseEntry(baseEntry);
		}

		public String getId() {
			return getSimpleStringValue("id");
		}

		public void setId(String id) {
			setSimpleValue("id", id);
		}

		public int getState() {
			return getSimpleIntegerValue("st");
		}

		public void setState(int state) {
			setSimpleValue("st", state);
		}

		public String getName() {
			return getSimpleStringValue("nm");
		}

		public void setName(String name) {
			setSimpleValue("nm", name);
		}

		public ObjectId getSubject1() {
			return getSimpleObjecIDValue("s1");
		}

		public void setSubject1(ObjectId subject1) {
			setSimpleValue("s1", subject1);
		}

		public ObjectId getSubject2() {
			return getSimpleObjecIDValue("s2");
		}

		public void setSubject2(ObjectId subject2) {
			setSimpleValue("s2", subject2);
		}

		public ObjectId getSubject3() {
			return getSimpleObjecIDValue("s3");
		}

		public void setSubject3(ObjectId subject3) {
			setSimpleValue("s3", subject3);
		}

		public Integer getNum(){
			try {
				return getSimpleIntegerValue("num");
			}
			catch (NullPointerException e){
				return null;
			}
		}
		public void setNum(Integer num){
			setSimpleValue("num",num) ;
		}
	}
}
