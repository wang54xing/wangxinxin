package com.db.new33.paike;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.new33.paike.N33_FaBuLogEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wang_xinxin on 2018/6/28.
 */
public class N33_FaBuLogDao extends BaseDao {

    /**
     * 添加发布
     * @param entry
     */
    public void addN33_FaBuLogEntry(N33_FaBuLogEntry entry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_FABULOG, entry.getBaseEntry());
    }

    /**
     *
     * @param termId
     * @param schoolId
     * @param gradeId
     * @return
     */
    public List<N33_FaBuLogEntry> getFaBuLogs(ObjectId termId, ObjectId schoolId, ObjectId gradeId,ObjectId ciId) {
        List<N33_FaBuLogEntry> result = new ArrayList<N33_FaBuLogEntry>();
        BasicDBObject query =new BasicDBObject("termId",termId).append("ciId",ciId).append("sid",schoolId).append("gid",gradeId).append("ir",Constant.ZERO);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_FABULOG, query, Constant.FIELDS, Constant.MONGO_SORTBY_DESC);
        for(DBObject dbo:dblist) {
            result.add(new N33_FaBuLogEntry((BasicDBObject) dbo));
        }
        return result;
    }


}
