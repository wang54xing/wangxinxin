package com.pojo.app;

/**
 * 带有排序的IdNameValuePairDTO；
 * 可用于IdNameValuePairDTO 排序操作
 * @author fourer
 *
 */
public class IdNameValueSortPairDTO extends IdNameValuePairDTO {

	private int sort;

	
	
	
	
	public IdNameValueSortPairDTO(Object id, String name, Object value,int sort) {
		super(id, name, value);
		this.sort=sort;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}
	
	
}
