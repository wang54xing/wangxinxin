package com.pojo.new33.isolate;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class N33_TeaRules extends BaseDBObject {

	private static final long serialVersionUID = 4080434098675838708L;

	public N33_TeaRules(BasicDBObject baseEntry) {
		setBaseEntry(baseEntry);
	}

	public N33_TeaRules() {
	}

	public N33_TeaRules(ObjectId sid, ObjectId xqid, ObjectId teaId, List<TeaRulesList> teaRulesList, Integer lev) {
		BasicDBObject dbObject = new BasicDBObject()
				.append("xqid", xqid) //学期id
				.append("sid", sid)//学校id
				.append("teaId", teaId)//老师
				.append("teaRulesList", MongoUtils.convert(MongoUtils.fetchDBObjectList(teaRulesList)))//xy的点的list
				.append("lev",lev);//排序级别  类型勾选的1  学科2  老师3
		setBaseEntry(dbObject);
	}

	public ObjectId getSchoolId() {
		return getSimpleObjecIDValue("sid");
	}

	public void setSchoolId(ObjectId sid) {
		setSimpleValue("sid", sid);
	}


	public ObjectId getXqid() {
		return getSimpleObjecIDValue("xqid");
	}

	public void setXqid(ObjectId xqid) {
		setSimpleValue("xqid", xqid);
	}

	public ObjectId getTeaId() {
		return getSimpleObjecIDValue("teaId");
	}

	public void setTeaId(ObjectId teaId) {
		setSimpleValue("teaId", teaId);
	}

	public void setTeaRulesList(List<TeaRulesList> list) {
		setSimpleValue("teaRulesList", MongoUtils.convert(MongoUtils.fetchDBObjectList(list)));
	}

	public List<TeaRulesList> getTeaRulesList() {
		List<TeaRulesList> retList = new ArrayList<TeaRulesList>();
		BasicDBList list = (BasicDBList) getSimpleObjectValue("teaRulesList");
		if (null != list && !list.isEmpty()) {
			for (Object o : list) {
				retList.add(new TeaRulesList((BasicDBObject) o));
			}
		}
		return retList;
	}

	/*public void setSubIds(List<ObjectId> subIds) {
		setSimpleValue("sub", MongoUtils.convert(subIds));
	}

	public List<ObjectId> getSubIds() {
		List<ObjectId> result = new ArrayList<ObjectId>();
		BasicDBList list = (BasicDBList) getSimpleObjectValue("sub");
		if (null != list && !list.isEmpty()) {
			for (Object o : list) {
				result.add((ObjectId) o);
			}
		}
		return result;
	}*/

	public Integer getLev() {
		return getSimpleIntegerValue("lev");
	}

	public void setLev(Integer lev) {
		setSimpleValue("lev", lev);
	}

	/**
	 * 内部类
	 * 老师的需求
	 */
	public static class TeaRulesList extends BaseDBObject {
		public TeaRulesList() {
		}

		public TeaRulesList(BasicDBObject baseEntry) {
			super(baseEntry);
		}

		public TeaRulesList(Integer x, Integer y,String desc,Integer require,Integer status,List<ObjectId> gid) {
			super();
			BasicDBObject dbo = new BasicDBObject()
					.append("x", x)
					.append("y", y)
					.append("desc",desc)
					.append("require",require) //0待审批   1同意    2拒绝
					.append("status", status) // 1必须 2优先 3拒绝;
					.append("gid", MongoUtils.convert(gid));
			setBaseEntry(dbo);
		}

		public Integer getX() {
			return getSimpleIntegerValueDef("x", 0);
		}

		public void setX(Integer x) {
			setSimpleValue("x", x);
		}

		public Integer getY() {
			return getSimpleIntegerValueDef("y", 0);
		}

		public void setY(Integer y) {
			setSimpleValue("y", y);
		}

		public Integer getRequire() {
			return getSimpleIntegerValue("require");
		}

		public void setRequire(Integer require) {
			setSimpleValue("require", require);
		}

		public String getDesc() {
			return getSimpleStringValue("desc");
		}

		public void setDesc(Integer desc) {
			setSimpleValue("desc", desc);
		}

		public Integer getStatus() {
			return getSimpleIntegerValue("status");
		}

		public void setStatus(Integer status) {
			setSimpleValue("status", status);
		}

		public void setGids(List<ObjectId> gids) {
			setSimpleValue("gid", MongoUtils.convert(gids));
		}

		public List<ObjectId> getGids() {
			List<ObjectId> result = new ArrayList<ObjectId>();
			BasicDBList list = (BasicDBList) getSimpleObjectValue("gid");
			if (null != list && !list.isEmpty()) {
				for (Object o : list) {
					result.add((ObjectId) o);
				}
			}
			return result;
		}
	}

}
