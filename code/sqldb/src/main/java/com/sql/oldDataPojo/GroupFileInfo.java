package com.sql.oldDataPojo;

import java.util.Date;

/**
 * Created by qinbo on 15/4/15.
 */
public class GroupFileInfo {
    private int id;
    private String roomid;
    private String fileName;
    private String filePath;
    private int filesize;
    private int uploadUserid;
    private Date uploadDate;
    private int count;
    private int delflg;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRoomid() {
        return roomid;
    }

    public void setRoomid(String roomid) {
        this.roomid = roomid;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getFilesize() {
        return filesize;
    }

    public void setFilesize(int filesize) {
        this.filesize = filesize;
    }

    public int getUploadUserid() {
        return uploadUserid;
    }

    public void setUploadUserid(int uploadUserid) {
        this.uploadUserid = uploadUserid;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getDelflg() {
        return delflg;
    }

    public void setDelflg(int delflg) {
        this.delflg = delflg;
    }
}
