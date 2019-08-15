package com.pojo.school;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;


/**
 * Created by albin on 2017/8/2.
 *
 * jid 教案id
 * uid 修改人
 */
public class JiaoAnUpEnty extends BaseDBObject {
    public JiaoAnUpEnty(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public JiaoAnUpEnty(ObjectId jid, ObjectId userId) {
        super();

        BasicDBObject baseEntry = new BasicDBObject()
                .append("ui", userId)
                .append("ji", jid);

        setBaseEntry(baseEntry);
    }
    public ObjectId getUId() {
        return getSimpleObjecIDValue("ui");
    }

    public void setUId(ObjectId uid) {
        setSimpleValue("ui", uid);
    }

    public ObjectId getJId() {
        return getSimpleObjecIDValue("ji");
    }

    public void setJId(ObjectId ji) {
        setSimpleValue("ji", ji);
    }

}
