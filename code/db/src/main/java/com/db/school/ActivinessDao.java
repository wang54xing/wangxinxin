package com.db.school;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.school.ActivinessEntry;
import com.pojo.utils.DeleteState;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guojing on 2015/11/19.
 */
public class ActivinessDao extends BaseDao {

    /**
     * 增加
     *
     * @param e
     * @return
     */
    public ObjectId addActivinessEntry(ActivinessEntry e) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_ACTIVINESS, e.getBaseEntry());
        return e.getID();
    }

    /**
     * 增加
     *
     * @param list
     * @return
     */
    public void addActivinessEntryList(List<DBObject> list) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_ACTIVINESS, list);
    }

    /**
     * 查询学生活跃度
     * @param lessonId
     * @param studentId
     * @param type
     * @return
     */
    public ActivinessEntry findActivinessEntry(ObjectId lessonId, ObjectId studentId, int type, DeleteState ds) {
        BasicDBObject query = new BasicDBObject("lid", lessonId);
        query.append("stid", studentId);
        query.append("ty", type);
        query.append("st", ds.getState());
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_ACTIVINESS, query, Constant.FIELDS);
        if (null != dbo) {
            ActivinessEntry e = new ActivinessEntry((BasicDBObject) dbo);
            return e;
        }
        return null;
    }

    /**
     * 查询学生活跃度
     * @param lessonId
     * @param studentId
     * @param type
     * @return
     */
    public List<ActivinessEntry> findActivinessList(ObjectId lessonId, ObjectId studentId, int type, DeleteState ds) {
        List<ActivinessEntry> retList = new ArrayList<ActivinessEntry>();
        BasicDBObject query = new BasicDBObject();
        if (null != lessonId) {
            query.append("lid", lessonId);
        }
        if (null != studentId) {
            query.append("stid", studentId);
        }
        if (0 != type) {
            query.append("ty", type);
        }
        query.append("st", ds.getState());
        List<DBObject> list = find(MongoFacroty.getAppDB(),
                Constant.COLLECTION_ACTIVINESS, query, Constant.FIELDS,
                Constant.MONGO_SORTBY_DESC);
        if (null != list && !list.isEmpty()) {
            ActivinessEntry e = null;
            for (DBObject dbo1 : list) {
                e = new ActivinessEntry((BasicDBObject) dbo1);
                retList.add(e);
            }
        }
        return retList;
    }

    /**
     * 修改学生活跃度
     * @param e
     * @return
     */
    public void updActivinessEntry(ActivinessEntry e) {
        BasicDBObject query =new BasicDBObject(Constant.ID,e.getID());
        BasicDBObject update=new BasicDBObject(Constant.MONGO_SET, new BasicDBObject(e.getBaseEntry()));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_ACTIVINESS, query, update);
    }
}
