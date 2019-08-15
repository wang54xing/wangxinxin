package com.pojo.user;

import com.sys.utils.AvatarUtils;

import java.io.Serializable;

/**
 * Created by qiangm on 2015/11/27.
 * 简化版用户信息表，用于投票选举，简化数据发送量，并隐藏密码等机密信息
 */
public class SimpleUserInfo implements Serializable {
    private String id;
    private int sex;
    private String imgUrl;
    private String name;
    private String nickName;
    private int experienceValue;
    private int role;
    private String positionDec;
    private String schoolName;


    public SimpleUserInfo()
    {

    }



    public SimpleUserInfo(UserEntry e)
    {
        this.id =e.getID().toString();
        this.sex=e.getSex();
        this.imgUrl= AvatarUtils.getAvatar(e.getAvatar(), 1);
        this.name = e.getUserName();
        this.nickName = e.getNickName();
        this.experienceValue = e.getExperiencevalue();
        this.role=e.getRole();
        this.positionDec=e.getPostionDec();
    }
    public SimpleUserInfo(UserInfoDTO userInfoDTO)
    {
        this.id=userInfoDTO.getId();
        this.name=userInfoDTO.getName();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getExperienceValue() {
        return experienceValue;
    }

    public void setExperienceValue(int experienceValue) {
        this.experienceValue = experienceValue;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getPositionDec() {
        return positionDec;
    }

    public void setPositionDec(String positionDec) {
        this.positionDec = positionDec;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
