package com.pojo.new33.paike;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * 事务，是线上可以加载至redis 做快速检测
 * {
 *     x
 *     y
 *     swlbId : swlbId 事务类型ID
 *     desc : desc 描述
 *     teacherIds : teacherIds // 事务影响的老师
 *     level : level // 有些事务可以妥协，有些事务即使定义了，仍然可以排课
 *     st : startTime 开始时间
 *     et : endTime 结束时间
 *     sid : schoolId 学校ID
 *     termId : termId 学期ID
 *     ir
 *     sk 学生是否自习
 *     reId:关联的id
 * }
 * Created by wang_xinxin on 2018/3/8.
 */
public class N33_SWEntry extends BaseDBObject {

    private static final long serialVersionUID = 4517066542368882868L;

    public N33_SWEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public N33_SWEntry() {}

    public N33_SWEntry(int x, int y, ObjectId swlbId, String desc, List<ObjectId> teacherIds,int level,long startTime,long endTime,
                       ObjectId schoolId,ObjectId termId,Integer sk) {
        super();
        BasicDBObject dbo = new BasicDBObject()
                .append("x",x)
                .append("y",y)
                .append("swlbId",swlbId)
                .append("desc",desc)
                .append("tids", MongoUtils.convert(teacherIds))
                .append("level",level)
                .append("st",startTime)
                .append("et",endTime)
                .append("sid",schoolId)
                .append("termId",termId)
                .append("sk",sk)
                .append("ir", Constant.ZERO);
        setBaseEntry(dbo);
    }
    public int getX() {
        return getSimpleIntegerValue("x");
    }
    public void setX(int x) {
        setSimpleValue("x",x);
    }
    public int getY() {
        return getSimpleIntegerValue("y");
    }
    public void setY(int y) {
        setSimpleValue("y",y);
    }
    public ObjectId getSwlbId() {
        return getSimpleObjecIDValue("swlbId");
    }
    public void setSwlbId(ObjectId swlbId) {
        setSimpleValue("swlbId",swlbId);
    }

    public ObjectId getReId() {
        return getSimpleObjecIDValue("reId");
    }
    public void setReId(ObjectId reId) {
        setSimpleValue("reId",reId);
    }
    public String getDesc() {
        return getSimpleStringValue("desc");
    }
    public void setDesc(String desc) {
        setSimpleValue("desc",desc);
    }
    public List<ObjectId> getTeacherIds() {
        List<ObjectId> retList =new ArrayList<ObjectId>();
        BasicDBList list =(BasicDBList)getSimpleObjectValue("tids");
        if(null!=list && !list.isEmpty())
        {
            for(Object o:list)
            {
                retList.add(((ObjectId)o));
            }
        }
        return retList;
    }

    public void setTeacherIds(List<ObjectId> teacherIds) {
        setSimpleValue("tids", MongoUtils.convert(teacherIds));
    }

    public int getLevel() {
        return getSimpleIntegerValue("level");
    }
    public void setLevel(int level) {
        setSimpleValue("level",level);
    }
    public ObjectId getSchoolId() {
        return getSimpleObjecIDValue("sid");
    }
    public void setSchoolId(ObjectId schoolId) {
        setSimpleValue("sid", schoolId);
    }
    public ObjectId getTermId() {
        return getSimpleObjecIDValue("termId");
    }
    public void setTermId(ObjectId termId) {
        setSimpleValue("termId", termId);
    }

    public long getStartTime() {
        return getSimpleLongValue("st");
    }
    public void setStartTime(long startTime) {
        setSimpleValue("st",startTime);
    }

    public long getEndTime() {
        return getSimpleLongValue("et");
    }
    public void setEndTime(long endTime) {
        setSimpleValue("et",endTime);
    }

    public int getStudentKe() {
        return getSimpleIntegerValueDef("sk", 0);
    }
    public void setStudentKe(int studentKe) {
        setSimpleValue("sk",studentKe);
    }
}
