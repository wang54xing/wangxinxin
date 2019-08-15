package com.sql.oldDataPojo;

import java.io.Serializable;

/**
 * Created by qinbo on 15/3/18.
 */
public class SchoolInfo implements Serializable {


    private static final long serialVersionUID = -2791829442094243367L;
    private int id;
    private String name;
    private String brief;
    private int geoId;
    private String geoName;
    private String address;
    private String homePage;
    private String postCode;
    private String phone;
    private String detail;
    private String imageUrl;
    private int sequence;
    private String recruitUrl;
    private String badgeUrl;
    private int level;
    private boolean hasRecruit;
    private int videoId;
    // 杨浦小学 20140415 START
    private int userId;
    /** logo 路径 */
    private String logoUrl;
    /** picture 路径 */
    private String pictureUrl;

    private String pictureUrl2;

    private String pictureUrl3;

    private String bigpictureUrl;

    private String bigpictureUrl2;

    private String bigpictureUrl3;
    /** 公告 */
    private String announcement;

    /** 域名 */
    private String domain;

    /** 学期 */
    private int termtype;

    private String englishName;

    private String starNumber;

    private String initialPassword;

    private int isPrimary;

    private int isMiddle;

    private int isHigh;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public int getGeoId() {
        return geoId;
    }

    public void setGeoId(int geoId) {
        this.geoId = geoId;
    }

    public String getGeoName() {
        return geoName;
    }

    public void setGeoName(String geoName) {
        this.geoName = geoName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getHomePage() {
        return homePage;
    }

    public void setHomePage(String homePage) {
        this.homePage = homePage;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public String getRecruitUrl() {
        return recruitUrl;
    }

    public void setRecruitUrl(String recruitUrl) {
        this.recruitUrl = recruitUrl;
    }

    public String getBadgeUrl() {
        return badgeUrl;
    }

    public void setBadgeUrl(String badgeUrl) {
        this.badgeUrl = badgeUrl;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean isHasRecruit() {
        return hasRecruit;
    }

    public void setHasRecruit(boolean hasRecruit) {
        this.hasRecruit = hasRecruit;
    }

    public int getVideoId() {
        return videoId;
    }

    public void setVideoId(int videoId) {
        this.videoId = videoId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getPictureUrl2() {
        return pictureUrl2;
    }

    public void setPictureUrl2(String pictureUrl2) {
        this.pictureUrl2 = pictureUrl2;
    }

    public String getPictureUrl3() {
        return pictureUrl3;
    }

    public void setPictureUrl3(String pictureUrl3) {
        this.pictureUrl3 = pictureUrl3;
    }

    public String getBigpictureUrl() {
        return bigpictureUrl;
    }

    public void setBigpictureUrl(String bigpictureUrl) {
        this.bigpictureUrl = bigpictureUrl;
    }

    public String getBigpictureUrl2() {
        return bigpictureUrl2;
    }

    public void setBigpictureUrl2(String bigpictureUrl2) {
        this.bigpictureUrl2 = bigpictureUrl2;
    }

    public String getBigpictureUrl3() {
        return bigpictureUrl3;
    }

    public void setBigpictureUrl3(String bigpictureUrl3) {
        this.bigpictureUrl3 = bigpictureUrl3;
    }

    public String getAnnouncement() {
        return announcement;
    }

    public void setAnnouncement(String announcement) {
        this.announcement = announcement;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public int getTermtype() {
        return termtype;
    }

    public void setTermtype(int termtype) {
        this.termtype = termtype;
    }

    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public String getStarNumber() {
        return starNumber;
    }

    public void setStarNumber(String starNumber) {
        this.starNumber = starNumber;
    }

    public String getInitialPassword() {
        return initialPassword;
    }

    public void setInitialPassword(String initialPassword) {
        this.initialPassword = initialPassword;
    }

    public int getIsPrimary() {
        return isPrimary;
    }

    public void setIsPrimary(int isPrimary) {
        this.isPrimary = isPrimary;
    }

    public int getIsMiddle() {
        return isMiddle;
    }

    public void setIsMiddle(int isMiddle) {
        this.isMiddle = isMiddle;
    }

    public int getIsHigh() {
        return isHigh;
    }

    public void setIsHigh(int isHigh) {
        this.isHigh = isHigh;
    }
}
