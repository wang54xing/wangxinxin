package com.sql.oldDataPojo;

import java.util.Date;

/**
 * Created by guojing on 2015/3/24.
 */
public class VideoInfo {
    private int id;
    private String name;
    private Date uploadDate;
    private int thumbup;
    private String thumbdown;
    private int length;
    private String imageUrl;
    private int viewNumber;
    private int starNumber;
    private int commentNumber;
    private int paidNumber;
    private double price;
    private double discount;
    private int uploadState;
    private int outId;
    private int videoType;
    private int deleteFlag;
    private String temporaryName;
    private int videoNum;
    private String bucketkey;
    private String persistentId;
    private int videoSourceType;

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


    public int getThumbup() {
        return thumbup;
    }

    public void setThumbup(int thumbup) {
        this.thumbup = thumbup;
    }

    public String getThumbdown() {
        return thumbdown;
    }

    public void setThumbdown(String thumbdown) {
        this.thumbdown = thumbdown;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getViewNumber() {
        return viewNumber;
    }

    public void setViewNumber(int viewNumber) {
        this.viewNumber = viewNumber;
    }

    public int getStarNumber() {
        return starNumber;
    }

    public void setStarNumber(int starNumber) {
        this.starNumber = starNumber;
    }

    public int getCommentNumber() {
        return commentNumber;
    }

    public void setCommentNumber(int commentNumber) {
        this.commentNumber = commentNumber;
    }

    public int getPaidNumber() {
        return paidNumber;
    }

    public void setPaidNumber(int paidNumber) {
        this.paidNumber = paidNumber;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public int getUploadState() {
        return uploadState;
    }

    public void setUploadState(int uploadState) {
        this.uploadState = uploadState;
    }

    public int getOutId() {
        return outId;
    }

    public void setOutId(int outId) {
        this.outId = outId;
    }

    public int getVideoType() {
        return videoType;
    }

    public void setVideoType(int videoType) {
        this.videoType = videoType;
    }

    public int getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(int deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public String getTemporaryName() {
        return temporaryName;
    }

    public void setTemporaryName(String temporaryName) {
        this.temporaryName = temporaryName;
    }

    public int getVideoNum() {
        return videoNum;
    }

    public void setVideoNum(int videoNum) {
        this.videoNum = videoNum;
    }

    public String getBucketkey() {
        return bucketkey;
    }

    public void setBucketkey(String bucketkey) {
        this.bucketkey = bucketkey;
    }

    public String getPersistentId() {
        return persistentId;
    }

    public void setPersistentId(String persistentId) {
        this.persistentId = persistentId;
    }

    public int getVideoSourceType() {
        return videoSourceType;
    }

    public void setVideoSourceType(int videoSourceType) {
        this.videoSourceType = videoSourceType;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }
}
