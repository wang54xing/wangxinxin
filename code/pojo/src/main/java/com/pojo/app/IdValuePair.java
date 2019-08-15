package com.pojo.app;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;

/**
 * 存储Id与其对应的值
 * {
 *  id:
 *  v:
 * }
 * @author fourer
 *
 */
public class IdValuePair extends BaseDBObject{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6136300787287249107L;

	public IdValuePair(BasicDBObject baseEntry) {
		super(baseEntry);
	}

	public IdValuePair(ObjectId id, Object value) {
		super();
		BasicDBObject baseEntry =new BasicDBObject()
		.append("id", id)
		.append("v", value);
		setBaseEntry(baseEntry);
	}

	public IdValuePair(String id,String value) {
		super();
		BasicDBObject baseEntry =new BasicDBObject()
				.append("id", id)
				.append("v", value);
		setBaseEntry(baseEntry);
	}
	
	public ObjectId getId() {
		return getSimpleObjecIDValue("id");
	}
	public void setId(ObjectId id) {
		setSimpleValue("id", id);
	}
	public Object getValue() {
		return getSimpleObjectValue("v");
	}
	public void setValue(Object value) {
		setSimpleValue("v", value);
	}


	/**
	 * id为null 表示没有选题
	 * v为null 表示答题
	 * 不建议这样创建BaseDBObject对象
	 * @return
	 */
	@Deprecated
	public static IdValuePair createNullIdValuePair()
	{
		BasicDBObject baseEntry =new BasicDBObject()
		.append("id", null)
		.append("v", null);
		return new IdValuePair(baseEntry);
	}
	
	
}
