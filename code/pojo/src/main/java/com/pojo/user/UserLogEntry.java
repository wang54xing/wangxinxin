package com.pojo.user;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import org.bson.types.ObjectId;

/**
 * <pre>
 * collectionName:ulog
 * </pre>
 *
 * <pre>
 * {
 *  ui:用户ID
 *  con:内容
 *  v:分值
 *  ty:1经验值2余额
 * }
 * </pre>
 *
 * Created by wang_xinxin on 2016/5/4.
 */
public class UserLogEntry  extends BaseDBObject {

    private static final long serialVersionUID = -8135998045668853067L;

    public UserLogEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public UserLogEntry(ObjectId userId,String connent,String value,int type) {
        super();
        BasicDBObject baseEntry =new BasicDBObject()
                .append("ui", userId)
                .append("con", connent)
                .append("v", value)
                .append("ty",type);
        setBaseEntry(baseEntry);
    }

    public int getType() {
        return getSimpleIntegerValue("ty");
    }

    public void setType(int type) {
        setSimpleValue("ty", type);
    }

    public ObjectId getUserId() {
        return getSimpleObjecIDValue("ui");
    }

    public void setUserId(ObjectId userId) {
        setSimpleValue("ui", userId);
    }

    public String getConnent() {
        return getSimpleStringValue("con");
    }

    public void setConnent(String connent) {
        setSimpleValue("con", connent);
    }

    public String getValue() {
        return getSimpleStringValue("v");
    }

    public void setValue(String value) {
        setSimpleValue("v",value);
    }
}
