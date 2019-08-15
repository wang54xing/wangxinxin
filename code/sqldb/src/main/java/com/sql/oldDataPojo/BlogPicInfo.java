package com.sql.oldDataPojo;

import java.io.Serializable;
import java.util.Date;

public class BlogPicInfo implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = -1353741319128778240L;

	private int id;
	private int blogid;
	private Date uploaddate;
	private String pathname;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getBlogid() {
		return blogid;
	}
	public void setBlogid(int blogid) {
		this.blogid = blogid;
	}
	public Date getUploaddate() {
		return uploaddate;
	}
	public void setUploaddate(Date uploaddate) {
		this.uploaddate = uploaddate;
	}
	public String getPathname() {
		return pathname;
	}
	public void setPathname(String pathname) {
		this.pathname = pathname;
	}
	
	

}
