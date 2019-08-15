package com.pojo.new33.isolate;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
/**
 * @author rick
 * xqid
 * sid 学校滴
 * gnm 年级名
 * cids 班级ids
 * gtea 年级老师
 * gstu 年级学生
 * ty   年级类型
 * jie  届
 */
public class Grade extends BaseDBObject{

	private static final long serialVersionUID = 3761767120049496905L;
	
	 public Grade(DBObject dbo) {
	        this((BasicDBObject) dbo);
	    }

	    public Grade(BasicDBObject baseEntry) {
	        super(baseEntry);
	    }


	    public Grade(ObjectId xqid, ObjectId schoolId, String gnm, List<ObjectId> cids, List<ObjectId> gtea,
	                 List<ObjectId> gstu, String sy, ObjectId gid, int type,String jie) {
	        super();
	        BasicDBObject dbo = new BasicDBObject()
	                .append("sid", schoolId)
	                .append("xqid", xqid)
	                .append("gid", gid)
	                .append("gnm", gnm)
	                .append("cids", MongoUtils.convert(cids))
	                .append("gtea", MongoUtils.convert(gtea))
	                .append("gstu", MongoUtils.convert(gstu))
	                .append("sy",sy)
	                .append("ty", type)
	                .append("jie", jie);
	        setBaseEntry(dbo);
	    }


	    public String getSy() {
	        return getSimpleStringValue("sy");
	    }

	    public void setSy(String sy) {
	        setSimpleValue("sy", sy);
	    }

	    public ObjectId getSchoolId() {
	        return getSimpleObjecIDValue("sid");
	    }

	    public void setSchoolId(ObjectId schoolId) {
	        setSimpleValue("sid", schoolId);
	    }

	    public void setGradeId(ObjectId gid) {
	        setSimpleValue("gid", gid);
	    }

	    public ObjectId getGradeId() {
	        return getSimpleObjecIDValue("gid");
	    }

	    public ObjectId getXQId() {
	        return getSimpleObjecIDValue("xqid");
	    }

	    public void setXQId(ObjectId xqid) {
	        setSimpleValue("xqid", xqid);
	    }


	    public String getName() {
	        return getSimpleStringValue("gnm");
	    }

	    public void setName(String gnm) {
	        setSimpleValue("gnm", gnm);
	    }


	    public String getJie() {
	        return getSimpleStringValue("jie");
	    }

	    public void setJie(String jie) {
	        setSimpleValue("jie", jie);
	    }

	    public List<ObjectId> getClassList() {
	        List<ObjectId> retList = new ArrayList<ObjectId>();
	        BasicDBList list = (BasicDBList) getSimpleObjectValue("cids");
	        if (null != list && !list.isEmpty()) {
	            for (Object o : list) {
	                retList.add(((ObjectId) o));
	            }
	        }
	        return retList;
	    }

	    public void setCList(List<ObjectId> cids) {
	        setSimpleValue("cids", MongoUtils.convert(cids));
	    }


	    public void setClassList(List<ObjectId> cids) {
	        setSimpleValue("cids", MongoUtils.convert(cids));
	    }


	    public List<ObjectId> getTeacherList() {
	        List<ObjectId> retList = new ArrayList<ObjectId>();
	        BasicDBList list = (BasicDBList) getSimpleObjectValue("gtea");
	        if (null != list && !list.isEmpty()) {
	            for (Object o : list) {
	                retList.add(((ObjectId) o));
	            }
	        }
	        return retList;
	    }

	    public void setTeacherList(List<ObjectId> gtea) {
	        setSimpleValue("gtea", MongoUtils.convert(gtea));
	    }


	    public List<ObjectId> getStudentList() {
	        List<ObjectId> retList = new ArrayList<ObjectId>();
	        BasicDBList list = (BasicDBList) getSimpleObjectValue("gstu");
	        if (null != list && !list.isEmpty()) {
	            for (Object o : list) {
	                retList.add(((ObjectId) o));
	            }
	        }
	        return retList;
	    }

	    public void setGstuList(List<ObjectId> gstu) {
	        setSimpleValue("gstu", MongoUtils.convert(gstu));
	    }


	    public void setStudentList(List<ObjectId> gstu) {
	        setSimpleValue("gstu", MongoUtils.convert(gstu));
	    }


	    public int getType() {
	        if (!getBaseEntry().containsField("ty")) {
	            return -1;
	        } else {
	            return getSimpleIntegerValue("ty");
	        }
	    }

	    public void setType(int type) {
	        setSimpleValue("ty", type);
	    }

}
