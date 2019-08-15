package com.sql.oldDataPojo;

import java.io.Serializable;
import java.util.Date;

public class UserPetInfo implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = 8575184415364111938L;

	private int id;

	private int petid;

	private int userid;

	private String petname;

	private int selecttype;

	private Date createdate;

	private int ishatch;
	
	private Date updatedate;
	
	private String petexplain;

	private String petimage;
	
	private String minpetimage;
	
	private String maxpetimage;
	
	private String middlepetimage;
	
	private Date curtime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPetid() {
		return petid;
	}

	public void setPetid(int petid) {
		this.petid = petid;
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public String getPetname() {
		return petname;
	}

	public void setPetname(String petname) {
		this.petname = petname;
	}

	public int getSelecttype() {
		return selecttype;
	}

	public void setSelecttype(int selecttype) {
		this.selecttype = selecttype;
	}

	public Date getCreatedate() {
		return createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

	public int getIshatch() {
		return ishatch;
	}

	public void setIshatch(int ishatch) {
		this.ishatch = ishatch;
	}

	public Date getUpdatedate() {
		return updatedate;
	}

	public void setUpdatedate(Date updatedate) {
		this.updatedate = updatedate;
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

	public Date getCurtime() {
		return curtime;
	}

	public void setCurtime(Date curtime) {
		this.curtime = curtime;
	}
	
	
}
