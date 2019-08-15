package com.pojo.new33.paike;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by albin on 2018/5/14.
 * <p/>
 * 同步使用
 * <p/>
 * ciId 同步的新的次id
 * oId  原的Id
 * nId  新的Id
 * sid  学校Id
 */
public class N33_SysnEntry extends BaseDBObject {

	private static final long serialVersionUID = 6216506845568693018L;

	public N33_SysnEntry(BasicDBObject baseEntry) {
		setBaseEntry(baseEntry);
	}

	public N33_SysnEntry() {
	}

	public N33_SysnEntry(ObjectId ciId,ObjectId oId,ObjectId nId,ObjectId sid) {
		BasicDBObject dbObject = new BasicDBObject()
				.append("ciId", ciId)
				.append("oId", oId)
				.append("nId", nId)
				.append("sid",sid)
				.append("ir", Constant.ZERO);
		setBaseEntry(dbObject);
	}

	public ObjectId getSid() {
		return getSimpleObjecIDValue("sid");
	}
	public void setSid(ObjectId sid) {
		setSimpleValue("sid", sid);
	}

	public ObjectId getCiId(){
		return getSimpleObjecIDValue("ciId");
	}
	public void setCiId(ObjectId ciId){
		setSimpleValue("ciId",ciId);
	}

	public ObjectId getOId(){
		return getSimpleObjecIDValue("oId");
	}
	public void setOId(ObjectId oId){
		setSimpleValue("oId",oId);
	}

	public ObjectId getNId(){
		return getSimpleObjecIDValue("nId");
	}
	public void setNId(ObjectId nId){
		setSimpleValue("nId",nId);
	}
}
