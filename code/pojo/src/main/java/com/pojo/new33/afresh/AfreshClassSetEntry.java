package com.pojo.new33.afresh;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2019-05-13.
 *
 * 重分行政班（设置表）---保存每次重分的设置属性
 *
 *  sid 学校id
 *  xqid 学期id
 *  gid  年级id
 *  rid  成绩单id
 *  ty   0 随机  1 成绩
 *  ld:  是否已同步学生表 0 未  1 已同步
 *  clt  所分班级信息
 *  new:  是否已分班    0  未  1 已分班
 *
 *
 */
public class AfreshClassSetEntry extends BaseDBObject {

    public AfreshClassSetEntry(){

    }

    public AfreshClassSetEntry(DBObject dbObject){
        super((BasicDBObject) dbObject);
    }

    public AfreshClassSetEntry(BasicDBObject dbObject){
        super(dbObject);
    }

    public AfreshClassSetEntry(ObjectId xqid,
                               ObjectId schoolId,
                               ObjectId gradeId,
                               ObjectId reportId,
                               Integer type,
                               Integer load,
                               List<ObjectId> classList){
        BasicDBObject dbObject = new BasicDBObject()
                .append("sid", schoolId)
                .append("xqid", xqid)
                .append("gid", gradeId)
                .append("rid", reportId)
                .append("ty", type)
                .append("ld", load)
                .append("clt",classList)
                .append("new",Constant.ZERO)
                .append("isr", Constant.ZERO);
        setBaseEntry(dbObject);
    }

    public List<ObjectId> getClassList() {
        List<ObjectId> classList = new ArrayList<ObjectId>();
        BasicDBList list = (BasicDBList) getSimpleObjectValue("clt");
        if (null != list && !list.isEmpty()) {
            for (Object o : list) {
                classList.add(((ObjectId) o));
            }
        }
        return classList;
    }

    public void setClassList(List<ObjectId> classList) {
        setSimpleValue("clt", MongoUtils.convert(classList));
    }

    public ObjectId getGradeId() {
        return getSimpleObjecIDValue("gid");
    }

    public void setGradeId(ObjectId gid) {
        setSimpleValue("gid", gid);
    }
    public ObjectId getReportId() {
        return getSimpleObjecIDValue("rid");
    }

    public void setReportId(ObjectId reportId) {
        setSimpleValue("rid", reportId);
    }

    public ObjectId getSchoolId() {
        return getSimpleObjecIDValue("sid");
    }

    public void setSchoolId(ObjectId schoolId) {
        setSimpleValue("sid", schoolId);
    }

    public ObjectId getXQId() {
        return getSimpleObjecIDValue("xqid");
    }

    public void setXQId(ObjectId xqid) {
        setSimpleValue("xqid", xqid);
    }

    public Integer getType() {
        return getSimpleIntegerValue("ty");
    }
    public void setType(Integer type) {
        setSimpleValue("ty", type);
    }


    public Integer getLoad() {
        return getSimpleIntegerValue("ld");
    }
    public void setLoad(Integer load) {
        setSimpleValue("ld", load);
    }

    public void setIsRemove(int isRemove){
        setSimpleValue("isr",isRemove);
    }


}
