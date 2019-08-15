package com.pojo.new33;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * @author rick
 * @date 2018/3/22 13:54
 */
public class N33_StudentTagEntry extends BaseDBObject {

    public N33_StudentTagEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public N33_StudentTagEntry() {
    }

    public N33_StudentTagEntry(ObjectId xqid, ObjectId schoolId, ObjectId gradeId, String name, List<StudentInfoEntry> students, List<ObjectId> jxbIds, Integer view, int fenDuan) {
        super();
        BasicDBObject dbo = new BasicDBObject()
                .append("xqid", xqid)
                .append("sid", schoolId)
                .append("gradeId", gradeId)
                .append("name", name)
                .append("xuni",0)
                .append("view", view)
                .append("students", MongoUtils.convert(MongoUtils.fetchDBObjectList(students))) // 学生列表
                .append("jxbIds", MongoUtils.convert(jxbIds))// 教学班id
                .append("fenDuan", fenDuan);
        setBaseEntry(dbo);
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

    public ObjectId getGradeId() {
        return getSimpleObjecIDValue("gradeId");
    }

    public void setGradeId(ObjectId gid) {
        setSimpleValue("gradeId", gid);
    }

    public Integer getView() {
        return getSimpleIntegerValueDef("view", 0);
    }

    public void setView(Integer view) {
        setSimpleValue("view", view);
    }

    public Integer getXuNi() {
        return getSimpleIntegerValueDef("xuni", 0);
    }

    public void setXuNi(Integer xuni) {
        setSimpleValue("xuni", xuni);
    }

    public String getName() {
        return getSimpleStringValue("name");
    }

    public void setName(String name) {
        setSimpleValue("name", name);
    }

    public void setFenDuan(int fenDuan) {
        setSimpleValue("fenDuan", fenDuan);
    }

    public int getFenDuan() {
        return getSimpleIntegerValueDef("fenDuan", 1);
    }

    public List<ObjectId> getJxbIds() {
        List<ObjectId> result = new ArrayList<ObjectId>();
        BasicDBList list = (BasicDBList) getSimpleObjectValue("jxbIds");
        if (null != list && !list.isEmpty()) {
            for (Object o : list) {
                result.add((ObjectId) o);
            }
        }
        return result;
    }

    public void setJxbIds(List<ObjectId> list) {
        setSimpleValue("jxbIds", MongoUtils.convert(list));
    }

    public List<StudentInfoEntry> getStudents() {
        List<StudentInfoEntry> result = new ArrayList<StudentInfoEntry>();
        BasicDBList list = (BasicDBList) getSimpleObjectValue("students");
        if (null != list && !list.isEmpty()) {
            for (Object o : list) {
                result.add(new StudentInfoEntry((BasicDBObject) o));
            }
        }
        return result;
    }

    public void setStudents(List<StudentInfoEntry> list) {
        setSimpleValue("students", MongoUtils.convert(MongoUtils.fetchDBObjectList(list)));
    }

    public static class StudentInfoEntry extends BaseDBObject {
        public StudentInfoEntry() {
        }

        public StudentInfoEntry(BasicDBObject baseEntry) {
            super(baseEntry);
        }

        /* *
                * studentid 学生id
                * classId 行政班id
                * className 行政班名称
                * @param [studentId, classId, className]
                * @return
                */
        public StudentInfoEntry(ObjectId studentId, ObjectId classId, String className) {
            super();
            BasicDBObject dbo = new BasicDBObject()
                    .append("stuid", studentId)
                    .append("classId", classId)
                    .append("className", className);
            setBaseEntry(dbo);
        }

        public ObjectId getStuId() {
            return getSimpleObjecIDValue("stuid");
        }

        public void setStuId(ObjectId stuId) {
            setSimpleValue("stuid", stuId);
        }

        public ObjectId getClassId() {
            return getSimpleObjecIDValue("classId");
        }

        public void setClassId(ObjectId classId) {
            setSimpleValue("classId", classId);
        }

        public String getClassName() {
            return getSimpleStringValue("className");
        }

        public void setClassName(String className) {
            setSimpleValue("className", className);
        }
    }
}
