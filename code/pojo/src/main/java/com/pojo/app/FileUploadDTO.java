package com.pojo.app;
/**
 * 文件上传结果
 * @author fourer
 *
 */
public class FileUploadDTO {

	private String id;
	private String fileKey;
	private String fileName;
	private String path;
	private long size;
	private String packageName;
	private String versionName;
	private String versionCode;
	private String imgurl;
	
	
	public FileUploadDTO(String id, String fileKey, String fileName, String path) {
		super();
		this.id = id;
		this.fileKey = fileKey;
		this.fileName = fileName;
		this.path = path;
	}

	public FileUploadDTO(String id, String fileKey, String fileName, String path,long size) {
		super();
		this.id = id;
		this.fileKey = fileKey;
		this.fileName = fileName;
		this.path = path;
		this.size = size;
	}

	public FileUploadDTO(String id, String fileKey, String fileName, String path,long size,String packageName,String versionCode,String versionName) {
		super();
		this.id = id;
		this.fileKey = fileKey;
		this.fileName = fileName;
		this.path = path;
		this.size = size;
		this.packageName = packageName;
		this.versionCode = versionCode;
		this.versionName = versionName;
	}


	public String getImgurl() {
		return imgurl;
	}

	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFileKey() {
		return fileKey;
	}
	public void setFileKey(String fileKey) {
		this.fileKey = fileKey;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public String getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(String versionCode) {
		this.versionCode = versionCode;
	}

	@Override
	public String toString() {
		return "FileUploadDTO [id=" + id + ", fileKey=" + fileKey
				+ ", fileName=" + fileName + ", path=" + path + "]";
	}
	
	
	
}
