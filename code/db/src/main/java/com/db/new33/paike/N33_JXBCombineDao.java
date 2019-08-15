package com.db.new33.paike;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.new33.N33_XuNiBanEntry;
import com.pojo.new33.paike.N33_JXBCombineEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class N33_JXBCombineDao extends BaseDao {

	/**
	 * 批量保存
	 *
	 * @param entry
	 */
	public void addN33_JXBCombine(Collection<N33_JXBCombineEntry> entry) {
		save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXBCombine, MongoUtils.fetchDBObjectList(entry));
	}

	public void removeN33_JXBCombine(ObjectId ciId,ObjectId sid,ObjectId gradeId,Integer type) {
		DBObject query = new BasicDBObject().append("xqid", ciId).append("sid", sid).append("gradeId",gradeId).append("type",type);
		remove(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXBCombine, query);
	}

	public List<N33_JXBCombineEntry> getN33_JXBCombineEntry(ObjectId ciId,ObjectId sid,ObjectId gradeId,Integer type,ObjectId jxbId){
		List<N33_JXBCombineEntry> returnList = new ArrayList<N33_JXBCombineEntry>();
		DBObject query = new BasicDBObject("jxbList.intersectJXB.intersectJxbId", jxbId).append("type",type).append("gradeId",gradeId).append("sid",sid).append("xqid",ciId)/*.append("intersectJXB.intersectJxbId",jxbId)*/;
		List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXBCombine, query, Constant.FIELDS);
		if(dblist != null){
			for (DBObject dbo : dblist) {
				returnList.add(new N33_JXBCombineEntry((BasicDBObject) dbo));
			}
		}
		return returnList;
	}

}
