package com.fulaan.homeschool.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pojo.app.IdNameValuePairDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * Created by wang_xinxin on 2016/10/27.
 */
public class FileDTO implements Serializable{

    private static final long serialVersionUID = -2602720728751977574L;

    public FileDTO() {

    }


    private String id;

    private String name;

    private String ownerId;

    private String userId;

    private String dir;

    private String filePath;

    private String backetKey;

    private String parentId;

    private String uploadTime;

    private String imageUrl;

    private String fileType;

    private int type;

    private String createTime;

    private List<FileDTO> fileDTOs;

    private String schoolId;

    private String dirId;

    private String dirName;

    private String orgFileId;

    private String newFileId;

    private int platform;

    private long size;

    private String realPath;

    public FileDTO(String name,String filePath,String schoolId,String userId,String dirId,int type,int platform,long size) {
        this.name = name;
        this.filePath = filePath;
        this.schoolId = schoolId;
        this.userId = userId;
        this.dirId = dirId;
        this.type = type;
        this.platform = platform;
        this.size = size;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getBacketKey() {
        return backetKey;
    }

    public void setBacketKey(String backetKey) {
        this.backetKey = backetKey;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(String uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public List<FileDTO> getFileDTOs() {
        return fileDTOs;
    }

    public void setFileDTOs(List<FileDTO> fileDTOs) {
        this.fileDTOs = fileDTOs;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getDirId() {
        return dirId;
    }

    public void setDirId(String dirId) {
        this.dirId = dirId;
    }

    public String getDirName() {
        return dirName;
    }

    public void setDirName(String dirName) {
        this.dirName = dirName;
    }

    public String getNewFileId() {
        return newFileId;
    }

    public void setNewFileId(String newFileId) {
        this.newFileId = newFileId;
    }

    public String getOrgFileId() {
        return orgFileId;
    }

    public void setOrgFileId(String orgFileId) {
        this.orgFileId = orgFileId;
    }

    public int getPlatform() {
        return platform;
    }

    public void setPlatform(int platform) {
        this.platform = platform;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getRealPath() {
        return realPath;
    }

    public void setRealPath(String realPath) {
        this.realPath = realPath;
    }
}
