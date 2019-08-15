package com.pojo.app;

/**
 * 区域
 * @author guojing
 *
 */
public class RegionDTO {

	private String id;
	private int level;
	private String parentId;
	private String name;
	private int sort;

	public RegionDTO(){

	}

	public RegionDTO(RegionEntry entry) {
		if(entry !=null) {
			this.id=entry.getID().toString();

			this.level=entry.getLevel();

			this.parentId=entry.getParentId()==null?"":entry.getParentId().toString();

			this.name=entry.getName();

			this.sort=entry.getSort();

		}else{
			new RegionDTO();
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}
}
