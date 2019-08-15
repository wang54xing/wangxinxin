package com.db.new33;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.new33.StudentSelectLessonsEntry;
import com.sys.constants.Constant;

/**
 * 学生选课操作类
 * @author fourer
 *
 */
public class StudentSelectLessonsDao extends BaseDao {
	
	/**
	 * 添加
	 * @param e
	 * @return
	 */
	public ObjectId addStudentSelectLessonsEntry(StudentSelectLessonsEntry e)
	{
		save(MongoFacroty.getAppDB(), Constant.COLLECTION_STUDENT_SELECT_LESSONS, e.getBaseEntry());
        return e.getID();
	}
	
	
	
	/**
	 * 查询
	 * @param schoolId 学校id
	 * @param classId 班级id
	 * @param studentId 学生id
	 * @return
	 */
	public List<StudentSelectLessonsEntry> getStudentSelectLessonsEntrys(ObjectId term,ObjectId schoolId,ObjectId classId,ObjectId studentId)
	{
		 List<StudentSelectLessonsEntry> retList = new ArrayList<StudentSelectLessonsEntry>();
	     BasicDBObject query = new BasicDBObject();
	     if(null!=(term))
	     {
	    	 query.append("t", term);
	     }
	     if(null!=schoolId)
	     {
	    	 query.append("si", schoolId);
	     }
	     if(null!=classId)
	     {
	    	 query.append("ci", classId);
	     }
	     if(null!=studentId)
	     {
	    	 query.append("sti", studentId);
	     }
	        
	     List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_STUDENT_SELECT_LESSONS, query, Constant.FIELDS);
	     if (null != list && !list.isEmpty()) {
	            for (DBObject dbo : list) {
	                retList.add(new StudentSelectLessonsEntry((BasicDBObject) dbo));
	            }
	        }
	     return retList;
	}

}
