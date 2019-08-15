package com.fulaan.folder.dto;

import com.fulaan.homeschool.dto.FileDTO;

import java.util.List;

/**
 * Created by wang_xinxin on 2016/12/29.
 */
public class FileDto {
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

    private List<FileDto> fileDtos;

    private String schoolId;

    private String dirId;

    private String dirName;

    private String orgFileId;

    private String newFileId;

    private int platform;

    private long size;

    private String realPath;

    private String key;

    public FileDto() {

    }

    public FileDto(String name,String filePath,String type,String imageUrl,String userId,String schoolId) {
        this.name = name;
        this.filePath = filePath;
        this.type = Integer.valueOf(type);
        this.imageUrl = imageUrl;
        this.schoolId = schoolId;
        this.userId = userId;
    }

    public FileDto(String name,String filePath,String schoolId,String userId,String dirId,int type,int platform) {
        this.name = name;
        this.filePath = filePath;
        this.schoolId = schoolId;
        this.userId = userId;
        this.dirId = dirId;
        this.type = type;
        this.platform = platform;
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

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public List<FileDto> getFileDtos() {
        return fileDtos;
    }

    public void setFileDtos(List<FileDto> fileDtos) {
        this.fileDtos = fileDtos;
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

    public String getOrgFileId() {
        return orgFileId;
    }

    public void setOrgFileId(String orgFileId) {
        this.orgFileId = orgFileId;
    }

    public String getNewFileId() {
        return newFileId;
    }

    public void setNewFileId(String newFileId) {
        this.newFileId = newFileId;
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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
