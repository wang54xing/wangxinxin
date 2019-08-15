package com.fulaan.extensionclass.dto;

import com.pojo.app.IdNameValuePairDTO;

import java.util.List;

/**
 * Created by wang_xinxin on 2017/1/11.
 */
public class ExpandDTO {

	private String id;
	
    private String schoolId;

    private String userId;

    private String name;

    private String content;

    private String teacherIds;

    private String labels;

    private String dt;

    private String uname;
    private String uimg;

    public String getDt() {
        return dt;
    }

    public void setDt(String dt) {
        this.dt = dt;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getUimg() {
        return uimg;
    }

    public void setUimg(String uimg) {
        this.uimg = uimg;
    }

    private List<String> nn;

    public List<String> getNn() {
        return nn;
    }

    public void setNn(List<String> nn) {
        this.nn = nn;
    }

    private List<IdNameValuePairDTO> fileDto;
    
    private boolean applied;

    private int zt;

    private String nr;

    public String getNr() {
        return nr;
    }

    public void setNr(String nr) {
        this.nr = nr;
    }

    public int getZt() {
        return zt;
    }

    public void setZt(int zt) {
        this.zt = zt;
    }

    public ExpandDTO() {

    }
    
    public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTeacherIds() {
        return teacherIds;
    }

    public void setTeacherIds(String teacherIds) {
        this.teacherIds = teacherIds;
    }

    public String getLabels() {
        return labels;
    }

    public void setLabels(String labels) {
        this.labels = labels;
    }

    public List<IdNameValuePairDTO> getFileDto() {
        return fileDto;
    }

    public void setFileDto(List<IdNameValuePairDTO> fileDto) {
        this.fileDto = fileDto;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

	public boolean isApplied() {
		return applied;
	}

	public void setApplied(boolean applied) {
		this.applied = applied;
	}

    /**
     * iscz: 0存在非必选 1不存在
     * snm:可报名人数
     * ged:性别要求 1:男 2:女 3:男、女
     * tcs:可报名班级列表
     * ads:上课地点
     * adsid:教室id
     * 非必填项
     * @return
     */
    private  int iscz;
    private  int snm;
    private  int ged;
    private  List<String> tcs;
    private  String ads;
    private  String adsid;

    private int ks;

    public int getKs() {
        return ks;
    }

    public void setKs(int ks) {
        this.ks = ks;
    }

    public int getIscz() {
        return iscz;
    }

    public void setIscz(int iscz) {
        this.iscz = iscz;
    }

    public int getSnm() {
        return snm;
    }

    public void setSnm(int snm) {
        this.snm = snm;
    }

    public int getGed() {
        return ged;
    }

    public void setGed(int ged) {
        this.ged = ged;
    }

    public List<String> getTcs() {
        return tcs;
    }

    public void setTcs(List<String> tcs) {
        this.tcs = tcs;
    }

    public String getAds() {
        return ads;
    }

    public void setAds(String ads) {
        this.ads = ads;
    }

    public String getAdsid() {
        return adsid;
    }

    public void setAdsid(String adsid) {
        this.adsid = adsid;
    }

}
