package com.pojo.base;

import com.mongodb.BasicDBObject;
import com.sys.constants.Constant;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;

import java.io.Serializable;

/**
 * 所有数据实体（entry）的基类
 * get***Value(String key) key类似于a.b.c，表示从baseEntry取出a,在a取出b,最后从b中取出c值返回，最大支持
 * a.b.c,不支持a.b.c.d
 * @author IBM
 *
 */
public class BaseDBObject implements Serializable {

	private static final Logger logger =Logger.getLogger(BaseDBObject.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private BasicDBObject baseEntry =new BasicDBObject();
	
	public BaseDBObject(){}

    public BaseDBObject(BasicDBObject baseEntry)
    {
    	this.baseEntry=baseEntry;
    }
    
    public BasicDBObject getBaseEntry() {
		return baseEntry;
	}

	public void setBaseEntry(BasicDBObject baseEntry) {
		this.baseEntry = baseEntry;
	}
   
    
    protected BasicDBObject getDBObject(String keys)
    {
    	String[] keyArr= keys.split(Constant.SEPARATOR);
    	if(keyArr.length==Constant.ONE)
    	{
    		return (BasicDBObject)baseEntry.get(keyArr[Constant.ZERO]);
    	}
    	if(keyArr.length==Constant.TWO)
    	{
    		BasicDBObject subDBObject =(BasicDBObject)baseEntry.get(keyArr[Constant.ZERO]);
    		if(null!=subDBObject)
    		{
    			return (BasicDBObject)subDBObject.get(keyArr[Constant.ONE]);
    		}
    	}
    	logger.error("the system not support this method;the key=["+ StringUtils.join(keyArr,Constant.SEPARATOR)+"]");
    	return null;
    }
    
    
    public ObjectId getID()
    {
    	return baseEntry.getObjectId(Constant.ID);
    }
    
    public void setID(ObjectId id)
    {
    	 baseEntry.put(Constant.ID, id);
    }
    
	
    protected int getSimpleIntegerValue(String key)
    {
		return baseEntry.getInt(key);
    }

	protected int getSimpleIntegerValueDef(String key,int defValue)
	{
		return baseEntry.getInt(key,defValue);
	}
    
    protected String getSimpleStringValue(String key) {
    	return baseEntry.getString(key);
    }
	protected String getSimpleStringValueDef(String key, String defValue) {
		return baseEntry.getString(key, defValue);
	}
    
    protected Double getSimpleDoubleValue(String key) {
    	return baseEntry.getDouble(key);
    }

	protected Double getSimpleDoubleValueDef(String key, double defValue) {
		return baseEntry.getDouble(key, defValue);
	}
    
    protected Long getSimpleLongValue (String key)
    {
    	return baseEntry.getLong(key);
    }
    
    protected Long getSimpleLongValueDef (String key,long def)
    {
    	return baseEntry.getLong(key,def);
    }
    
    protected Object getSimpleObjectValue (String key)
    {
    	return baseEntry.get(key);
    }
    
    protected ObjectId getSimpleObjecIDValue (String key)
    {
    	return baseEntry.getObjectId(key);
    }

	protected Boolean getSimpleBoolean(String key){
		return baseEntry.getBoolean(key);
	}
   

    protected int getComplexIntegerValue(String key)
    {
    	ComplexValueDTO dto =getComplexValueDTO(key);
    	if(null!=dto)
    	{
    		return dto.getDbobject().getInt(dto.getValueKey());
    	}
    	return Constant.DEFAULT_VALUE_INT;
    }
    
    
    
    protected String getComplexStringValue(String key)
    {
    	ComplexValueDTO dto =getComplexValueDTO(key);
    	if(null!=dto)
    	{
    		return dto.getDbobject().getString(dto.getValueKey());
    	}
    	return Constant.DEFAULT_VALUE_STRING;
    }
    
    protected Double getComplexDoubleValue(String key)
    {
    	ComplexValueDTO dto =getComplexValueDTO(key);
    	if(null!=dto)
    	{
    		return dto.getDbobject().getDouble(dto.getValueKey());
    	}
    	return Constant.DEFAULT_VALUE_DOUBLE;
    }
    
    protected Long getComplexLongValue (String key)
    {
    	ComplexValueDTO dto =getComplexValueDTO(key);
    	if(null!=dto)
    	{
    		return dto.getDbobject().getLong(dto.getValueKey());
    	}
    	return Constant.DEFAULT_VALUE_LONG;
    }
    
    protected Object getComplexObjectValue (String key)
    {
    	ComplexValueDTO dto =getComplexValueDTO(key);
    	if(null!=dto)
    	{
    		return dto.getDbobject().get(dto.getValueKey());
    	}
    	return Constant.DEFAULT_VALUE_OBJECT;
    }
    
    private ComplexValueDTO getComplexValueDTO(String key)
    {
    	if(StringUtils.isNotBlank(key))
    	{
			int index = key.lastIndexOf(Constant.POINT);
			String keys = key.substring(Constant.ZERO, index);
			BasicDBObject dbobject = getDBObject(keys);
			String keyValue = key.substring(index + Constant.ONE);
			if (null != dbobject) {
				return new ComplexValueDTO(dbobject, keyValue);
			}
    	}
		return null;
    }
    
  
    protected void setSimpleValue(String key,Object value)
    {
    	 this.baseEntry.append(key, value);
    }
    
    protected void setComplexValue(String key,Object value) 
    {
    	String[] keyArr=key.split(Constant.SEPARATOR);
    	BasicDBObject firstSubDBO;
    	if(keyArr.length>Constant.ENTRY_MAX_FIELD_ARRAY_LENGTH )
    	{
    		logger.error("the system not support this method;the key=["+ key+"]");
    		return;
    	}
    	if(keyArr.length==Constant.TWO)
    	{
    		firstSubDBO=(BasicDBObject)baseEntry.get(keyArr[Constant.ZERO]);
    		if(null!=firstSubDBO)
    		{
    			firstSubDBO.append(keyArr[Constant.ONE], value);
    		}
    		else
    		{
    			firstSubDBO=new BasicDBObject(keyArr[Constant.ONE], value);
    			baseEntry.append(keyArr[Constant.ZERO], firstSubDBO);
    		}
    		return;
    	}
    	if(keyArr.length==Constant.THREE)
    	{
    		BasicDBObject secondSubDBO=null;
    		firstSubDBO=(BasicDBObject)baseEntry.get(keyArr[Constant.ZERO]);
    		if(null!=firstSubDBO)
    		{
    			secondSubDBO=(BasicDBObject)firstSubDBO.get(Constant.ONE);
    			if(null!=secondSubDBO)
    			{
    				secondSubDBO.append(keyArr[Constant.TWO], value);
    			}
    			else
    			{
    				secondSubDBO =new BasicDBObject(keyArr[Constant.TWO], value);
    				firstSubDBO.append(keyArr[Constant.ONE], secondSubDBO);
    			}
    		}
    		else
    		{
    			secondSubDBO =new BasicDBObject(keyArr[Constant.TWO], value);
    			firstSubDBO=new BasicDBObject(keyArr[Constant.ONE],secondSubDBO);
    			baseEntry.append(keyArr[Constant.ZERO], firstSubDBO);
    		}
    		return;
    	}
    	setSimpleValue(key,value);
    }
    
   
    @Override
	public String toString() {
		return baseEntry.toString();
	}


	class ComplexValueDTO
    {
    	private BasicDBObject dbobject;
    	private String valueKey;
    	
		public ComplexValueDTO(BasicDBObject dbobject, String valueKey) {
			super();
			this.dbobject = dbobject;
			this.valueKey = valueKey;
		}
		
		public ComplexValueDTO(BasicDBObject dbobject) {
			super();
			this.dbobject = dbobject;
		
		}
		
		public ComplexValueDTO( String valueKey) {
			super();
			this.valueKey = valueKey;
		}
		
		public BasicDBObject getDbobject() {
			return dbobject;
		}
		public void setDbobject(BasicDBObject dbobject) {
			this.dbobject = dbobject;
		}
		public String getValueKey() {
			return valueKey;
		}
		public void setValueKey(String valueKey) {
			this.valueKey = valueKey;
		}
    }

}
