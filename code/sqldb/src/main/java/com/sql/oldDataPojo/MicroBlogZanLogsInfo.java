package com.sql.oldDataPojo;

import java.io.Serializable;
import java.util.Date;

public class MicroBlogZanLogsInfo implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = -1074464677211712514L;

	private int userid;
	private int blogid;
	private int commentid;
	private Date createtime;
	private int delflg;
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	public int getBlogid() {
		return blogid;
	}
	public void setBlogid(int blogid) {
		this.blogid = blogid;
	}
	public int getCommentid() {
		return commentid;
	}
	public void setCommentid(int commentid) {
		this.commentid = commentid;
	}
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	public int getDelflg() {
		return delflg;
	}
	public void setDelflg(int delflg) {
		this.delflg = delflg;
	}
	
	
}
