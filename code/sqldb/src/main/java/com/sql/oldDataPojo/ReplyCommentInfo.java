package com.sql.oldDataPojo;

import java.io.Serializable;
import java.util.Date;

public class ReplyCommentInfo implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = 3763794650633096531L;

	private int id;
	private int blogid;
	private String commentcontent;
	private int userid;
	private int buserid;
	private int commentid;
	private Date createtime;
	private int isread;
	private int zancount;
	private String nickname;
	private String bnickname;
	private String minImageURL;
	private String blogcontent;
    private int schoolId;
	private int delflg;
	private int iszan;
	private String bcommentcontent;
	private String blogtime;
	private int role;
	private String truenickname;
	private String btruenickname;
	
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
	public String getCommentcontent() {
		return commentcontent;
	}
	public void setCommentcontent(String commentcontent) {
		this.commentcontent = commentcontent;
	}
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	public int getBuserid() {
		return buserid;
	}
	public void setBuserid(int buserid) {
		this.buserid = buserid;
	}
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	public int getIsread() {
		return isread;
	}
	public void setIsread(int isread) {
		this.isread = isread;
	}
	public int getZancount() {
		return zancount;
	}
	public void setZancount(int zancount) {
		this.zancount = zancount;
	}
	public int getCommentid() {
		return commentid;
	}
	public void setCommentid(int commentid) {
		this.commentid = commentid;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getBnickname() {
		return bnickname;
	}
	public void setBnickname(String bnickname) {
		this.bnickname = bnickname;
	}
	public String getMinImageURL() {
		return minImageURL;
	}
	public void setMinImageURL(String minImageURL) {
		this.minImageURL = minImageURL;
	}
	public String getBlogcontent() {
		return blogcontent;
	}
	public void setBlogcontent(String blogcontent) {
		this.blogcontent = blogcontent;
	}
    public int getSchoolId() {
        return schoolId;
    }
    public void setSchoolId(int schoolId) {
        this.schoolId = schoolId;
    }
	public int getDelflg() {
		return delflg;
	}
	public void setDelflg(int delflg) {
		this.delflg = delflg;
	}
	public int getIszan() {
		return iszan;
	}
	public void setIszan(int iszan) {
		this.iszan = iszan;
	}
	public String getBcommentcontent() {
		return bcommentcontent;
	}
	public void setBcommentcontent(String bcommentcontent) {
		this.bcommentcontent = bcommentcontent;
	}
	public String getBlogtime() {
		return blogtime;
	}
	public void setBlogtime(String blogtime) {
		this.blogtime = blogtime;
	}
	public int getRole() {
		return role;
	}
	public void setRole(int role) {
		this.role = role;
	}

	public String getTruenickname() {
		return truenickname;
	}

	public String getBtruenickname() {
		return btruenickname;
	}

	public void setTruenickname(String truenickname) {
		this.truenickname = truenickname;
	}

	public void setBtruenickname(String btruenickname) {
		this.btruenickname = btruenickname;
	}
}
