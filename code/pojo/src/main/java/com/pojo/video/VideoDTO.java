package com.pojo.video;

import com.pojo.app.SimpleDTO;

public class VideoDTO extends SimpleDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1085751543276800705L;
	
	private long length;
	private int uploadState; //0:正在上传 1：上传成功
    private String url;
    private String imageUrl;
    
    
	public VideoDTO(VideoEntry e) {
		super(e);
		this.length=e.getLength();
		this.uploadState=e.getUpdateState();
        this.imageUrl = e.getImgUrl();
        //this.url = e.ge

        
	}

	
	

	public int getUploadState() {
		return uploadState;
	}

	public void setUploadState(int uploadState) {
		this.uploadState = uploadState;
	}

	public long getLength() {
		return length;
	}

	public void setLength(long length) {
		this.length = length;
	}



    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
