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
 * Created by albin on 2018/7/10.
 */
public class N33_PaiKeZuHeEntry extends BaseDBObject {
    public N33_PaiKeZuHeEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public N33_PaiKeZuHeEntry() {
    }

    public N33_PaiKeZuHeEntry(String name, ObjectId schoolId, ObjectId termId, List<ObjectId> jxbIds, Integer type, ObjectId gid) {
        super();
        BasicDBObject dbo = new BasicDBObject()
                .append("name", name)
                .append("sid", schoolId)
                .append("termId", termId)
                .append("jxbIds", MongoUtils.convert(jxbIds))
                .append("type", type)
                .append("gid", gid)
                .append("ir", Constant.ZERO);
        setBaseEntry(dbo);
    }

    public String getName() {
        return getSimpleStringValue("name");
    }

    public void setName(String name) {
        setSimpleValue("name", name);
    }


    public Integer getType() {
        return getSimpleIntegerValue("type");
    }

    public void setType(Integer type) {
        setSimpleValue("type", type);
    }

    public ObjectId getSchoolId() {
        return getSimpleObjecIDValue("sid");
    }

    public void setSchoolId(ObjectId schoolId) {
        setSimpleValue("sid", schoolId);
    }

    public ObjectId getGId() {
        return getSimpleObjecIDValue("gid");
    }

    public void setGId(ObjectId gId) {
        setSimpleValue("gid", gId);
    }

    public ObjectId getTermId() {
        return getSimpleObjecIDValue("termId");
    }

    public void setTermId(ObjectId termId) {
        setSimpleValue("termId", termId);
    }

    public void setJxbIds(List<ObjectId> jxbIds) {
        setSimpleValue("jxbIds", MongoUtils.convert(jxbIds));
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
}
