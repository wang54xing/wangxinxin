package com.pojo.new33.paike;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class N33_SynRecordEntry extends BaseDBObject {

    public N33_SynRecordEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public N33_SynRecordEntry() {}

    public N33_SynRecordEntry(ObjectId sid,ObjectId ciId,ObjectId oldCiId,List<Integer> records) {
        super();
        BasicDBObject dbo =new BasicDBObject()
                .append("ciId",ciId)
                .append("oldciId",oldCiId)
                .append("sid",sid)
                .append("records",MongoUtils.convert(records));
        setBaseEntry(dbo);
    }

    public ObjectId getSchoolId() {
        return getSimpleObjecIDValue("sid");
    }
    public void setSchoolId(ObjectId schoolId) {
        setSimpleValue("sid", schoolId);
    }

    public ObjectId getCiId(){
        return getSimpleObjecIDValue("ciId");
    }

    public void setCiId(ObjectId ciId){
        setSimpleValue("ciId", ciId);
    }

    public ObjectId getOldCiId(){
        return getSimpleObjecIDValue("oldciId");
    }

    public void setOldCiId(ObjectId ciId){
        setSimpleValue("oldciId", ciId);
    }

    public List<Integer> getRecords() {
        List<Integer> retList =new ArrayList<Integer>();
        BasicDBList list =(BasicDBList)getSimpleObjectValue("records");
        if(null!=list && !list.isEmpty())
        {
            for(Object o:list)
            {
                retList.add(((Integer)o));
            }
        }
        return retList;
    }

    public void setRecords(List<Integer> records) {
        setSimpleValue("records", MongoUtils.convert(records));
    }

}
