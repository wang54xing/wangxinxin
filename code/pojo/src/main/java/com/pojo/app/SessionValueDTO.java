package com.pojo.app;

import java.io.Serializable;
import java.util.HashMap;

public class SessionValueDTO   implements Serializable {

	private HashMap<String,String> map =new HashMap<String, String>();
	/**
	 * 
	 */
	private static final long serialVersionUID = 2786714258271457362L;
	
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public HashMap<String, String> getMap() {
		return map;
	}

	public void setMap(HashMap<String, String> map) {
		this.map = map;
	}
	
	
	
}
