package com.pojo.new33.paike;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by albin on 2018/6/29.
 */
public class N33_AutoPaiKeEntry extends BaseDBObject {
    private static final long serialVersionUID = 767419300390478812L;

    public N33_AutoPaiKeEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public N33_AutoPaiKeEntry() {
    }

    public N33_AutoPaiKeEntry(ObjectId sid, ObjectId xqid, Integer type, List<ObjectId> teaList, List<ObjectId> subList, Integer status, List<PaiKeXy> xyList, Integer lev,ObjectId gid) {
        super();
        BasicDBObject dbo = new BasicDBObject()
                .append("xqid", xqid) //学期id
                .append("sid", sid)//学校id
                .append("type", type)//类型  3行政  1走班  4专项
                .append("tea", MongoUtils.convert(teaList))//老师list
                .append("sub", MongoUtils.convert(subList))//学科list
                .append("xyList", MongoUtils.convert(MongoUtils.fetchDBObjectList(xyList)))//xy的点的list
                .append("lev",lev)//排序级别  类型勾选的1  学科2  老师3
                .append("status", status) // 1必须 2优先 3拒绝
                .append("gid",gid);
        setBaseEntry(dbo);
    }

    public void setXYList(List<PaiKeXy> list) {
        setSimpleValue("xyList", MongoUtils.convert(MongoUtils.fetchDBObjectList(list)));
    }

    public List<PaiKeXy> getXYList() {
        List<PaiKeXy> retList = new ArrayList<PaiKeXy>();
        BasicDBList list = (BasicDBList) getSimpleObjectValue("xyList");
        if (null != list && !list.isEmpty()) {
            for (Object o : list) {
                retList.add(new PaiKeXy((BasicDBObject) o));
            }
        }
        return retList;
    }

    public void setTeaIds(List<ObjectId> teaList) {
        setSimpleValue("tea", MongoUtils.convert(teaList));
    }

    public List<ObjectId> getTeaIds() {
        List<ObjectId> result = new ArrayList<ObjectId>();
        BasicDBList list = (BasicDBList) getSimpleObjectValue("tea");
        if (null != list && !list.isEmpty()) {
            for (Object o : list) {
                result.add((ObjectId) o);
            }
        }
        return result;
    }

    public void setSubIds(List<ObjectId> subIds) {
        setSimpleValue("sub", MongoUtils.convert(subIds));
    }

    public List<ObjectId> getSubIds() {
        List<ObjectId> result = new ArrayList<ObjectId>();
        BasicDBList list = (BasicDBList) getSimpleObjectValue("sub");
        if (null != list && !list.isEmpty()) {
            for (Object o : list) {
                result.add((ObjectId) o);
            }
        }
        return result;
    }
    public ObjectId getGradeId(){
        return getSimpleObjecIDValue("gid");
    }
    public void setGradeId(ObjectId gid) {
        setSimpleValue("gid", gid);
    }

    public ObjectId getXqId() {
        return getSimpleObjecIDValue("xqid");
    }

    public void setXqId(ObjectId xqid) {
        setSimpleValue("xqid", xqid);
    }

    public ObjectId getSId() {
        return getSimpleObjecIDValue("sid");
    }

    public void setSId(ObjectId sid) {
        setSimpleValue("sid", sid);
    }

    public Integer getType() {
        return getSimpleIntegerValue("type");
    }

    public void setType(Integer type) {
        setSimpleValue("type", type);
    }


    public Integer getLev() {
        return getSimpleIntegerValue("lev");
    }

    public void setLev(Integer lev) {
        setSimpleValue("lev", lev);
    }

    public Integer getStatus() {
        return getSimpleIntegerValue("status");
    }

    public void setStatus(Integer status) {
        setSimpleValue("status", status);
    }


    /**
     * 内部类
     * 虚拟班对应的标签类
     */
    public static class PaiKeXy extends BaseDBObject {
        public PaiKeXy() {
        }

        public PaiKeXy(BasicDBObject baseEntry) {
            super(baseEntry);
        }

        public PaiKeXy(Integer x, Integer y) {
            super();
            BasicDBObject dbo = new BasicDBObject()
                    .append("x", x)
                    .append("y", y);
            setBaseEntry(dbo);
        }

        public Integer getX() {
            return getSimpleIntegerValueDef("x", 0);
        }

        public void setX(Integer x) {
            setSimpleValue("x", x);
        }

        public Integer getY() {
            return getSimpleIntegerValueDef("y", 0);
        }

        public void setY(Integer y) {
            setSimpleValue("y", y);
        }

        @Override
        public boolean equals(Object obj) {
            if(obj instanceof PaiKeXy){
                PaiKeXy o = (PaiKeXy) obj;
                if(getX() == o.getX() && getY() == o.getY()){
                    return true;
                }else {
                    return false;
                }
            }else {
                return false;
            }
        }
    }
}
