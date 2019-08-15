package com.pojo.log;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;

/**
 * 简单日志
 * 
 * <pre>
 * collectionName:simplelogs
 * </pre>
 * 
 * <pre>
 * {
 *  ui
 *  r:角色
 *  si:
 *  pf:
 *  app:应用
 *  path:
 * }
 * </pre>
 * @author fourer
 *
 */
public class SimpleLogEntry extends BaseDBObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6184316660537108325L;
	
	public SimpleLogEntry(BasicDBObject baseEntry) {
		super(baseEntry);
	}
	public SimpleLogEntry(ObjectId ui,int role, ObjectId si, int pf,int app, String path) {
		super();
      BasicDBObject dbo =new BasicDBObject()
      .append("ui", ui)
      .append("r", role)
      .append("si", si)
      .append("pf", pf)
      .append("app", app)
      .append("path", path);
      
      setBaseEntry(dbo);
	}
	

	public int getApp() {
		return getSimpleIntegerValueDef("app",1);
	}
	public void setApp(int app) {
		setSimpleValue("app", app);
	}
	public ObjectId getUi() {
		return getSimpleObjecIDValue("ui");
	}
	public void setUi(ObjectId ui) {
		setSimpleValue("ui", ui);
	}
	
	public int getRole() {
		return getSimpleIntegerValueDef("r",0);
	}
	public void setRole(int role) {
		setSimpleValue("r", role);
	}
	public ObjectId getSi() {
		return getSimpleObjecIDValue("si");
	}
	public void setSi(ObjectId si) {
		setSimpleValue("si", si);
	}
	public int getPf() {
		return getSimpleIntegerValue("pf");
	}
	public void setPf(int pf) {
		setSimpleValue("pf", pf);
	}
	public String getPath() {
		return getSimpleStringValue("path");
	}
	public void setPath(String path) {
		setSimpleValue("path", path);
	}
}
