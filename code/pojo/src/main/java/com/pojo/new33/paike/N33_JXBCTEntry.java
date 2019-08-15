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
 * 教学班冲突
 * {
 *     ciId  次id
 *     jxbId : jxbId 冲突的教学班
 *     ojxbId : ojxbId 冲突的教学班
 *     ctType : ctType 学生冲突1， 教师冲突2，场地冲突3 …
 *     ctCnt : ctCount 仅学生冲突类型使用，明确拥有共同学生的具体人数
 *     ctxsIds : ctxsIds 具体的冲突学生列表，包含学生ID
 *     flag  学生是否完全冲突 1 完全 0不完全
 *     ir 删除状态
 * }
 * Created by wang_xinxin on 2018/3/7.
 */
public class N33_JXBCTEntry extends BaseDBObject {

    private static final long serialVersionUID = 2587273439531749434L;

    public N33_JXBCTEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public N33_JXBCTEntry() {}

    public N33_JXBCTEntry(ObjectId ciId,ObjectId schoolId,ObjectId jxbId, ObjectId ojxbId, int ctType, int ctCount, List<ObjectId> ctxsIds) {
        super();
        BasicDBObject dbo =new BasicDBObject()
                .append("ciId",ciId)
                .append("sid",schoolId)
                .append("jxbId", jxbId)
                .append("ojxbId", ojxbId)
                .append("ctType", ctType)
                .append("ctCnt",ctCount)
                .append("ctxsIds", MongoUtils.convert(ctxsIds))
                .append("ir", Constant.ZERO);
        setBaseEntry(dbo);
    }
    public N33_JXBCTEntry(ObjectId ciId,ObjectId schoolId,ObjectId jxbId, ObjectId ojxbId, int ctType, int ctCount, List<ObjectId> ctxsIds,Integer flag) {
        super();
        BasicDBObject dbo =new BasicDBObject()
                .append("ciId",ciId)
                .append("sid",schoolId)
                .append("jxbId", jxbId)
                .append("ojxbId", ojxbId)
                .append("ctType", ctType)
                .append("ctCnt",ctCount)
                .append("ctxsIds", MongoUtils.convert(ctxsIds))
                .append("flag",flag)
                .append("ir", Constant.ZERO);
        setBaseEntry(dbo);
    }
    public ObjectId getSchoolId() {
        return getSimpleObjecIDValue("sid");
    }
    public void setSchoolId(ObjectId schoolId) {
        setSimpleValue("sid",schoolId);
    }

    public ObjectId getJxbId() {
        return getSimpleObjecIDValue("jxbId");
    }
    public void setJxbId(ObjectId jxbId) {
        setSimpleValue("jxbId", jxbId);
    }

    public ObjectId getOjxbId() {
        return getSimpleObjecIDValue("ojxbId");
    }
    public void setOjxbId(ObjectId ojxbId) {
        setSimpleValue("ojxbId", ojxbId);
    }

    public int getCtType() {
        return getSimpleIntegerValue("ctType");
    }
    public void setCtType(int ctType) {
        setSimpleValue("ctType",ctType);
    }

    public int getCtCount() {
        return getSimpleIntegerValue("ctCnt");
    }
    public void setCtCount(int ctCount) {
        setSimpleValue("ctCnt",ctCount);
    }

    public void setFlag(Integer flag){
        setSimpleValue("flag",flag);
    }

    public Integer getFlag(){
        return getSimpleIntegerValueDef("flag",-1);
    }

    public List<ObjectId> getCtxsIds() {
        List<ObjectId> retList =new ArrayList<ObjectId>();
        BasicDBList list =(BasicDBList)getSimpleObjectValue("ctxsIds");
        if(null!=list && !list.isEmpty())
        {
            for(Object o:list)
            {
                retList.add(((ObjectId)o));
            }
        }
        return retList;
    }

    public void setCtxsIds(List<ObjectId> ctxsIds) {
        setSimpleValue("ctxsIds", MongoUtils.convert(ctxsIds));
    }
}
