package com.pojo.new33.isolate;


import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import com.sys.utils.NameShowUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by albin on 2017/10/16.
 * <p/>
 * 用户表：User3+3(隔离层)
 * {
 * Sid: 	    学校id    Objectid
 * Xqid         学期id    Objectid
 * uid          用户id
 * unm          用户名
 * sex          性别
 * cid          班级id
 * cnm          班级名
 * gid          年级id
 * gnm          年级名
 * sn           学号
 * ty           类型  0同步    1非同步
 *
 *
 * level        层级
 * subid1       选科id1
 * subid2
 * subid3
 */
public class StudentEntry extends BaseDBObject {

    private static final long serialVersionUID = -6733036985026118060L;



    public StudentEntry(BasicDBObject baseEntry) {
        setBaseEntry(baseEntry);
    }

    public StudentEntry() {
    }

    public StudentEntry(ObjectId schoolId, ObjectId xqid, ObjectId uid, String unm,
                     ObjectId cid, String cnm,
                    ObjectId gid, String gnm,  Integer sex,
                     String sn,Integer type,Integer level,String combiname,ObjectId subjectId1,ObjectId subjectId2,ObjectId subjectId3) {
        BasicDBObject dbObject = new BasicDBObject()
                .append("sid", schoolId)
                .append("xqid", xqid)
                .append("uid", uid)
                .append("unm", unm)
                .append("cid", cid)
                .append("cnm", cnm)
                .append("gid", gid)
                .append("gnm", gnm)
                .append("sex", sex)
                .append("sn", sn)
                .append("ty", type)
                .append("level", level)
                .append("combiname", combiname)
                .append("subid1", subjectId1)
                .append("subid2", subjectId2)
                .append("subid3", subjectId3);
        setBaseEntry(dbObject);
    }

    public StudentEntry(ObjectId schoolId, ObjectId xqid, ObjectId uid, String unm,
            ObjectId cid, String cnm,
           ObjectId gid, String gnm,  Integer sex,
            String sn,Integer type,Integer level) {
        BasicDBObject dbObject = new BasicDBObject()
               .append("sid", schoolId)
               .append("xqid", xqid)
               .append("uid", uid)
               .append("unm", unm)
               .append("cid", cid)
               .append("cnm", cnm)
               .append("gid", gid)
               .append("gnm", gnm)
               .append("sex", sex)
               .append("sn", sn)
               .append("ty", type)
               .append("level", level)
               ;
        setBaseEntry(dbObject);
    }
    
    public ObjectId getSchoolId() {
        return getSimpleObjecIDValue("sid");
    }

    public void getSchoolId(ObjectId sid) {
        setSimpleValue("sid", sid);
    }

    public ObjectId getSid() {
        return getSimpleObjecIDValue("sid");
    }

    public void setSid(ObjectId sid) {
        setSimpleValue("sid", sid);
    }

    public ObjectId getXqid() {
        return getSimpleObjecIDValue("xqid");
    }

    public void setXqid(ObjectId xqid) {
        setSimpleValue("xqid", xqid);
    }

    public ObjectId getUserId() {
        return getSimpleObjecIDValue("uid");
    }

    public void setUserid(ObjectId uid) {
        setSimpleValue("uid", uid);
    }

    public String getUserName() {
        String nm= getSimpleStringValue("unm");
        return NameShowUtils.showName(nm);
    }

    public String getUserName2() {
        String nm= getSimpleStringValue("unm");
        return nm;
    }

    public String getRealName() {
        String nm= getSimpleStringValue("unm");
        return nm;
    }

    public void setUName(String unm) {
        setSimpleValue("unm", unm);
    }

    public void setUserName(String unm) {
        setSimpleValue("unm", unm);
    }
    public String getStudyNum() {
        if(this.getBaseEntry().containsField("sn"))
        {
            return getSimpleStringValue("sn");
        }
        return "";
    }

    public void setStudyNum(String studyNum) {
        setSimpleValue("sn", studyNum);
    }

    public int getSex() {
        return getSimpleIntegerValue("sex");
    }

    public void setSex(int sex) {
        setSimpleValue("sex", sex);
    }

    public List<ObjectId> getClassList() {
        List<ObjectId> retList = new ArrayList<ObjectId>();
        BasicDBList list = (BasicDBList) getSimpleObjectValue("cid");
        if (null != list && !list.isEmpty()) {
            for (Object o : list) {
                retList.add(((ObjectId) o));
            }
        }
        return retList;
    }

    public ObjectId getClassId() {
    	return getSimpleObjecIDValue("cid");
    }

    public void setClassId(ObjectId classId) {
    	setSimpleValue("cid", classId);
    }

    public ObjectId getGradeId() {
    	return getSimpleObjecIDValue("gid");
    }

    public void setGradeId(ObjectId GradeId) {
    	setSimpleValue("gid", GradeId);
    }

    public String getGradeName() {
    	return getSimpleStringValue("gnm");
    }

    public void setGradeName(String gradeName) {
    	setSimpleValue("gnm", gradeName);
    }

    public String getClassName() {
    	return getSimpleStringValue("cnm");
    }

    public void setClassName(String ClassName) {
    	setSimpleValue("cnm", ClassName);
    }

    public void setType(Integer type) {
    	setSimpleValue("ty", type);
    }

    public Integer getType() {
    	return getSimpleIntegerValue("ty");
    }

    public void setLevel(Integer level) {
    	setSimpleValue("level", level);
    }

	public Integer getLevel() {
		return getSimpleIntegerValueDef("level",0);
	}
    public String getCombiname() {
    	return getSimpleStringValueDef("combiname","");
    }
    public void setCombiname(String combiname) {
    	setSimpleValue("combiname", combiname);
    }
    public ObjectId getSubjectId1() {
    	return getSimpleObjecIDValue("subid1");
    }
    public void setSubjectId1(ObjectId SubjectId1) {
    	setSimpleValue("subid1", SubjectId1);
    }
    public ObjectId getSubjectId2() {
    	return getSimpleObjecIDValue("subid2");
    }
    public void setSubjectId2(ObjectId SubjectId2) {
    	setSimpleValue("subid2", SubjectId2);
    }
    public ObjectId getSubjectId3() {
    	return getSimpleObjecIDValue("subid3");
    }
    public void setSubjectId3(ObjectId SubjectId3) {
    	setSimpleValue("subid3", SubjectId3);
    }
}
