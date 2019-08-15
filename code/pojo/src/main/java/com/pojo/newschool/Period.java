package com.pojo.newschool;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;

/**
 * Created by guojing on 2017/1/10.
 */
public class Period extends BaseDBObject {

    private static final long serialVersionUID = -6136300787287249107L;

    public Period(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public Period(String period, int year) {
        super();
        BasicDBObject baseEntry =new BasicDBObject()
                .append("pe", period)
                .append("yr", year);
        setBaseEntry(baseEntry);
    }

    public String getPeriod() {
        return getSimpleStringValue("pe");
    }
    public void setPeriod(String period) {
        setSimpleValue("pe", period);
    }

    public int getYear() {
        return getSimpleIntegerValue("yr");
    }
    public void setYear(int year) {
        setSimpleValue("yr", year);
    }

}
