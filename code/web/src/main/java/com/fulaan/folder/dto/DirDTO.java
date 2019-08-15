package com.fulaan.folder.dto;


import java.io.Serializable;

/**
 * 
 * @author fourer
 *
 */
public class DirDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4644573757196339123L;

	private String id;
	private String name;
	private String parentId;
	private String userId;
	private int sort;
	private boolean open;

	
	//@todo
	public DirDTO() {
		super();
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}
}
