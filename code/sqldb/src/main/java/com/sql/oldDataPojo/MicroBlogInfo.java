package com.sql.oldDataPojo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class MicroBlogInfo implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = -1308872429458437918L;

	private int id;
	private String blogcontent;
	private int userid;
	private Date createtime;
	private int delflg;
	private int isread;
	private int mreply;
	private int zancount;
	private String clienttype;
	private String nickname;
	private String truenickname;
	private String minImageURL;
	private List<String> picpathList;
    private int schoolId;
	private int iszan;
	private int role;
	private int sendtype;
	private int isdelete;
	private Date updatetime;
	private int istop;
	private int blogtype;

	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getBlogcontent() {
		return blogcontent;
	}
	public void setBlogcontent(String blogcontent) {
		this.blogcontent = blogcontent;
	}
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
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
	public int getIsread() {
		return isread;
	}
	public void setIsread(int isread) {
		this.isread = isread;
	}
	public int getMreply() {
		return mreply;
	}
	public void setMreply(int mreply) {
		this.mreply = mreply;
	}
	public int getZancount() {
		return zancount;
	}
	public void setZancount(int zancount) {
		this.zancount = zancount;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getMinImageURL() {
		return minImageURL;
	}
	public void setMinImageURL(String minImageURL) {
		this.minImageURL = minImageURL;
	}
	public String getClienttype() {
		return clienttype;
	}
	public void setClienttype(String clienttype) {
		this.clienttype = clienttype;
	}
	public List<String> getPicpathList() {
		return picpathList;
	}
	public void setPicpathList(List<String> picpathList) {
		this.picpathList = picpathList;
	}
    public int getSchoolId() {
        return schoolId;
    }
    public void setSchoolId(int schoolId) {
        this.schoolId = schoolId;
    }
    public int getIszan() {
		return iszan;
	}
	public void setIszan(int iszan) {
		this.iszan = iszan;
	}
	public int getRole() {
		return role;
	}
	public void setRole(int role) {
		this.role = role;
	}
	public int getSendtype() {
		return sendtype;
	}
	public void setSendtype(int sendtype) {
		this.sendtype = sendtype;
	}
	public int getIsdelete() {
		return isdelete;
	}
	public void setIsdelete(int isdelete) {
		this.isdelete = isdelete;
	}
	public Date getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}
	public int getIstop() {
		return istop;
	}
	public void setIstop(int istop) {
		this.istop = istop;
	}
	public int getBlogtype() {
		return blogtype;
	}
	public void setBlogtype(int blogtype) {
		this.blogtype = blogtype;
	}

	public String getTruenickname() {
		return truenickname;
	}

	public void setTruenickname(String truenickname) {
		this.truenickname = truenickname;
	}
}
