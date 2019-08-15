package com.pojo.app;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;

/**
 * 地域地理信息
 * <pre>
 * collectionName:regions
 * </pre>
 * <pre>
 * {
 *  lel: 1国家   2省以及自治区以及直辖市    3地市   4区县
 *  pid:父级地域代码,当lel=1时，pid为""
 *  nm:名称
 *  so:排序代码，仅仅用于同级地域排序
 * }
 * </pre>
 * @author fourer
 */
public class RegionEntry extends BaseDBObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3231758154243974662L;
	
	
	
	public RegionEntry(BasicDBObject baseEntry) {
		super(baseEntry);
	}
	
	public RegionEntry(int level,ObjectId parentId,String name) {
		this(level,parentId,name,0);
	}
	
	public RegionEntry(int level,ObjectId parentId,String name,int sort) {
		BasicDBObject dbo =new BasicDBObject()
		                    .append("lel", level)
		                    .append("pid", parentId)
		                    .append("nm", name)
		                    .append("so", sort)
		                  ;
		setBaseEntry(dbo);
	}
	
	
	public int getLevel() {
		return getSimpleIntegerValue("lel");
	}
	public void setLevel(int level) {
		setSimpleValue("lel", level);
	}
//	public String getCode() {
//		return getSimpleStringValue("cd");
//	}
//	public void setCode(String code) {
//		setSimpleValue("cd", code);
//	}
	public ObjectId getParentId() {
		return getSimpleObjecIDValue("pid");
	}
	public void setParentId(ObjectId parentid) {
		setSimpleValue("pid", parentid);
	}
	public String getName() {
		return getSimpleStringValue("nm");
	}
	public void setName(String name) {
		setSimpleValue("nm", name);
	}
	public int getSort() {
		return getSimpleIntegerValue("so");
	}
	public void setSort(int sort) {
		setSimpleValue("so", sort);
	}
	
}
