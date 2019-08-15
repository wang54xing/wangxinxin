package com.pojo.school;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;

/**校区信息
 * nm:校区名称
 * addr:地址
 * ph:联系电话
 * mer:负责人
 * Created by fl on 2015/7/23.
 */
public class CampusEntry extends BaseDBObject {
    /**
	 * 
	 */
	private static final long serialVersionUID = 6003893958523313923L;

	public CampusEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public CampusEntry() {

    }

    public CampusEntry(String nm, String addr, String ph, String mer) {
        super();
        BasicDBObject basicDBObject = new BasicDBObject()
                .append("nm", nm)
                .append("addr", addr)
                .append("ph", ph)
                .append("mer", mer);
        setBaseEntry(basicDBObject);
    }

    public String getName() {
        return getSimpleStringValue("nm");
    }

    public void setName(String name) {
        setSimpleValue("nm", name);
    }

    public String getAddr() {
        return getSimpleStringValue("addr");
    }

    public void setAddr(String addr) {
        setSimpleValue("addr", addr);
    }

    public String getPhone() {
        return getSimpleStringValue("ph");
    }

    public void setPhone(String phone) {
        setSimpleValue("ph", phone);
    }

    public String getManager() {
        return getSimpleStringValue("mer");
    }

    public void setManager(String manager) {
        setSimpleValue("mer", manager);
    }
}
