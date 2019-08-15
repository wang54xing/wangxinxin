package com.pojo.app;

import java.util.LinkedHashMap;
import java.util.Map;

import com.sys.constants.Constant;

/**
 * 平台
 * @author fourer
 *
 */
public enum Platform {

	PC(1,"PC"),
	Android(2,"Android"),
	IOS(3,"IOS"),
	;
	private int type;
	private String name;
	
	
	private Platform(int type, String name) {
		this.type = type;
		this.name = name;
	}
	
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

    public static Map<Integer, String> getPlatformMap() {
        Map<Integer, String> map = new LinkedHashMap<Integer, String>();
        for (Platform thisEnum : Platform.values()) {
            map.put(thisEnum.getType(), thisEnum.getName());
        }
        return map;
    }
    
    
    public  static Platform getPlatform(int type)
    {
    	if(Constant.THREE==type)
    	{
    		return Platform.IOS;
    	}
    	if(Constant.TWO==type) 
    	{
    		return Platform.Android;
    	}
    	return Platform.PC;
    }
}
