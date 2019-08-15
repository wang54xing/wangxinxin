package com.pojo.app;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;

/**
 * Created by fl on 2016/1/26.
 */
public class NameValuePair extends BaseDBObject {

    /**
	 * 
	 */
	private static final long serialVersionUID = -7120327633589612119L;

	public NameValuePair(){}

    public NameValuePair(BasicDBObject baseEntry){
        setBaseEntry(baseEntry);
    }

    public NameValuePair(String name, Object value){
        BasicDBObject baseEntry = new BasicDBObject()
                .append("nm", name)
                .append("v", value);
        setBaseEntry(baseEntry);
    }

    public String getName(){
        return getSimpleStringValue("nm");
    }

    public void setName(String name){
        setSimpleValue("nm", name);
    }

    public Object getValue(){
        return getSimpleObjectValue("v");
    }

    public void setValue(Object value){
        setSimpleValue("v", value);
    }

}
