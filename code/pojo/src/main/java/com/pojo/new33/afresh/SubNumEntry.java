package com.pojo.new33.afresh;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 *subList   走班学科id
 *num		学生人数
 *
 */
public class SubNumEntry extends BaseDBObject {

    private static final long serialVersionUID = 1242991562318562073L;

    
    public SubNumEntry() {

    }
    
    public Integer getNum() {
        return getSimpleIntegerValueDef("num",0);

    }

    public void setNum(Integer num) {
        setSimpleValue("num", num);
    }


    public List<ObjectId> getSubList() {
        List<ObjectId> retList = new ArrayList<ObjectId>();
        BasicDBList list = (BasicDBList) getSimpleObjectValue("subList");
        if (null != list && !list.isEmpty()) {
            for (Object o : list) {
                retList.add(((ObjectId) o));
            }
        }
        return retList;
    }

    public void setSubList(List<ObjectId> subList) {
        setSimpleValue("subList", MongoUtils.convert(subList));
    }

   
}
