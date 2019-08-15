package com.fulaan.video.service;

import com.db.video.VideoDao;
import com.fulaan.screenshot.Encoder;
import com.fulaan.utils.QiniuFileUtils;
import com.mongodb.DBObject;
import com.pojo.video.VideoEntry;
import com.pojo.video.VideoSourceType;
import com.sys.constants.Constant;
import com.sys.exceptions.IllegalParamException;
import com.sys.props.Resources;
import com.sys.utils.RespObj;
import org.apache.commons.io.FilenameUtils;
import org.bson.types.ObjectId;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 视频服务
 * @author fourer
 *
 */
public class VideoService {

	private VideoDao videoDao =new VideoDao();
	
	
	public ObjectId addVideoEntry(VideoEntry e)
	{
		return videoDao.addVideoEntry(e);
	}
	/**
	 * 
	 * @param id
	 * @return
	 */
	public VideoEntry getVideoEntryById(ObjectId id)
	{
		return videoDao.getVideoEntryById(id);
	}
	
	
	public VideoEntry getVideoEntryByPersistentId(String perId){

        VideoEntry ve = videoDao.getVideoEntryByPersistentId(perId);
        return ve;
    }
	
	/**
	 * 根据ID集合查询
	 * @param col
	 * @param fields
	 * @return
	 */
	public Map<ObjectId, VideoEntry> getVideoEntryMap(Collection<ObjectId> col,DBObject fields)
	{
		return videoDao.getVideoEntryMap(col, fields);
	}

    public void updateVideoUpdateStatus(ObjectId videoId,int uploadStatus) {
        try {
            videoDao.update(videoId, "us", uploadStatus);
        }
        catch (IllegalParamException il){

        }
    }

    public void updatePersistentId(ObjectId videoId,String persistentId){
        try {
            videoDao.update(videoId, "pid", persistentId);
        }
        catch (IllegalParamException il){

        }
    }

	public List<String> uploadVideo(HttpServletRequest request, String fileField) throws IOException, IllegalParamException {
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		List<MultipartFile> multipartFiles = multipartRequest.getFiles(fileField);
		List<String> resource = new ArrayList<String>(multipartFiles.size());
		for (MultipartFile multipartFile : multipartFiles) {
			String fileName = multipartFile.getOriginalFilename();
			//视频filekey
			String videoFilekey =new ObjectId().toString()+ Constant.POINT+ FilenameUtils.getExtension(fileName);
			String bathPath= Resources.getProperty("upload.file");
			File dir =new File(bathPath);
			if(!dir.exists())
			{
				dir.mkdir();
			}

			File savedFile = new File(bathPath, videoFilekey);

			OutputStream stream =new FileOutputStream(savedFile);
			stream.write(multipartFile.getBytes());
			stream.flush();
			stream.close();

			String coverImage = new ObjectId().toString() + ".jpg";
			Encoder encoder = new Encoder();
			File screenShotFile = new File(bathPath, coverImage);
			long videoLength = 60000;//缺省一分钟
			//是否生成了图片
			boolean isCreateImage=false;
			try
			{
				encoder.getImage(savedFile, screenShotFile, 1, 480, 270);
				videoLength = encoder.getInfo(savedFile).getDuration();
				isCreateImage=true;
			}catch(Exception ex)
			{
				ex.printStackTrace();
			}
			if(videoLength==-1){
				videoLength = 60000;//获取不到时间就设为1分钟
			}
			//String imageFilePath = null;
			//上传图片
			if(isCreateImage && screenShotFile.exists())
			{
				RespObj obj= QiniuFileUtils.uploadFile(coverImage, new FileInputStream(screenShotFile), QiniuFileUtils.TYPE_IMAGE);
				if(!obj.getCode().equals(Constant.SUCCESS_CODE))
				{
					QiniuFileUtils.deleteFile(QiniuFileUtils.TYPE_VIDEO, videoFilekey);
				}
			}
			VideoEntry ve =new VideoEntry(fileName, videoLength, VideoSourceType.USER_VIDEO.getType(),videoFilekey);
			ve.setID(new ObjectId());
			QiniuFileUtils.uploadVideoFile(ve.getID(),videoFilekey, multipartFile.getInputStream(), QiniuFileUtils.TYPE_USER_VIDEO);
			ObjectId videoId=addVideoEntry(ve);
			resource.add(videoId.toString());
			//删除临时文件
			try
			{
				savedFile.delete();
				screenShotFile.delete();
			}catch(Exception ex)
			{

			}
		}
		return resource;
	}
}
