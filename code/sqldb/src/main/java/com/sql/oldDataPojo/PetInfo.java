package com.sql.oldDataPojo;

import java.io.Serializable;

public class PetInfo implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = -1126606340179577885L;

	private int id;

	private String petname;

	private String petexplain;

	private String petimage;
	
	private String minpetimage;
	
	private String maxpetimage;
	
	private String middlepetimage;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPetname() {
		return petname;
	}

	public void setPetname(String petname) {
		this.petname = petname;
	}

	public String getPetexplain() {
		return petexplain;
	}

	public void setPetexplain(String petexplain) {
		this.petexplain = petexplain;
	}

	public String getPetimage() {
		return petimage;
	}

	public void setPetimage(String petimage) {
		this.petimage = petimage;
	}

	public String getMinpetimage() {
		return minpetimage;
	}

	public void setMinpetimage(String minpetimage) {
		this.minpetimage = minpetimage;
	}

	public String getMaxpetimage() {
		return maxpetimage;
	}

	public void setMaxpetimage(String maxpetimage) {
		this.maxpetimage = maxpetimage;
	}

	public String getMiddlepetimage() {
		return middlepetimage;
	}

	public void setMiddlepetimage(String middlepetimage) {
		this.middlepetimage = middlepetimage;
	}

}
