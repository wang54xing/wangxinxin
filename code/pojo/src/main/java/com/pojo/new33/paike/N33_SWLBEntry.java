package com.pojo.new33.paike;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * 事务类别
 * {
 *     desc : desc 描述
 *     sid : schoolId 学校ID
 *     termId : 学期ID
 *     ir
 * }
 * Created by wang_xinxin on 2018/3/8.
 */
public class N33_SWLBEntry extends BaseDBObject {
    private static final long serialVersionUID = -72896121440568739L;

    public N33_SWLBEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public N33_SWLBEntry() {}

    public N33_SWLBEntry(String desc, ObjectId schoolId,ObjectId termId) {
        super();
        BasicDBObject dbo =new BasicDBObject()
                .append("desc", desc)
                .append("sid",schoolId)
                .append("termId",termId)
                .append("ir", Constant.ZERO);
        setBaseEntry(dbo);
    }

    public String getDesc() {
        return getSimpleStringValue("desc");
    }
    public void setDesc(String desc) {
        setSimpleValue("desc",desc);
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


}
