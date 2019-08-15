package com.sql.oldDataTransfer;

import com.db.resources.ResourceDao;
import com.db.resources.ResourceDictionaryDao;
import com.db.video.VideoDao;
import com.pojo.app.FileType;
import com.pojo.resources.ResourceDictionaryEntry;
import com.pojo.resources.ResourceEntry;
import com.pojo.video.VideoEntry;
import com.pojo.video.VideoSourceType;
import com.sys.constants.Constant;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 将视频转变成资源
 * @author fourer
 *
 */
public class TransferVideoResource {
	

	public static void main(String[] args) throws Exception {
	
		File f= new File("D:\\0519\\TransferVideo1117.txt");
		f.createNewFile();
		
		VideoDao videoDao =new VideoDao();
		ResourceDao resourceDao =new ResourceDao();
		ResourceDictionaryDao resourceDictionaryDao =new ResourceDictionaryDao();
		
		File address=new File("D:\\0519\\file0805.txt");
		File fileKws=new File("D:\\0519\\kws0805.txt");
		File filemulus=new File("D:\\0519\\mulu0805.txt");
	
		
		
		List<String> stringNames =FileUtils.readLines(address,"utf-8");
		List<String> stringKws =FileUtils.readLines(fileKws,"utf-8");
		List<String> stringmulus =FileUtils.readLines(filemulus,"utf-8");
		
		
		
		if(stringNames.size()!=stringKws.size() || stringKws.size()!=stringmulus.size())
		{
			throw new Exception("数据错误");
		}
		
		
		for(int i=118;i<stringNames.size();i++)
		{
			//72 95 110 111 112
			 String videoAddress=stringNames.get(i);
			 if(videoAddress.endsWith(".jpg"))
			 {
				 continue;
			 }
			
			 System.out.println(i);
			File videoFile =new File(videoAddress);
			FileUtils.write(f, "文件："+videoAddress, true);
			FileUtils.write(f, "\r\n", true);
			if(!videoFile.exists())
			{
				videoFile =new File(videoAddress.substring(1));
				if(!videoFile.exists())
				{
					
					FileUtils.write(f, "不存在："+videoAddress, true);
					FileUtils.write(f, "\r\n", true);
					continue;
				}
			}
			
			
			
			String imageAddress=videoFile.getAbsolutePath().replace(".mp4", ".jpg");
			
			
           File imageFile =new File(imageAddress);
			
			if(!imageFile.exists())
			{
				FileUtils.write(f, "不存在："+imageAddress, true);
				FileUtils.write(f, "\r\n", true);
				continue;
			}
			
		
			String qiniuImagekey=new ObjectId().toString()+".jpg";
			FileUtils.write(f, "qiniuImagekey:"+qiniuImagekey, true);
			FileUtils.write(f, "\r\n", true);
			
			
			com.sys.utils.QiniuFileUtils.uploadFile(qiniuImagekey, new FileInputStream(imageFile), com.sys.utils.QiniuFileUtils.TYPE_IMAGE);
			
			String videoFilekey =new ObjectId().toString()+Constant.POINT+"mp4";
			VideoEntry ve =new VideoEntry(FilenameUtils.getBaseName(videoAddress), videoFile.length(), VideoSourceType.USER_VIDEO.getType(),videoFilekey);
	       
			ObjectId videoId=new ObjectId();
			ve.setID(videoId);
			
	        FileUtils.write(f, "qiniuVideokey:"+videoFilekey, true);
			FileUtils.write(f, "\r\n", true);
			
			
			FileUtils.write(f, "videoEntryKey:"+videoId, true);
			FileUtils.write(f, "\r\n", true);
			
			
			com.sys.utils.QiniuFileUtils.uploadVideoFile(ve.getID(),videoFilekey, new FileInputStream(videoFile), com.sys.utils.QiniuFileUtils.TYPE_USER_VIDEO);
			if(imageFile.exists())
			{
			   ve.setImgUrl(com.sys.utils.QiniuFileUtils.getPath(com.sys.utils.QiniuFileUtils.TYPE_IMAGE,qiniuImagekey));
			}
			
			
			
			videoDao.addVideoEntry(ve);
			
			
			try
			{
					String kwId =stringKws.get(i);
					String zhangjieId = stringmulus.get(i);
					
				    VideoEntry e=videoDao.getVideoEntryById(videoId);
				    if(null==e)
				    {
				    	System.out.println("没有找到视频;"+i+","+stringNames.get(i));
						continue;
				    }
				    
				    String tn=e.getTemporaryName();
				    String buckName =e.getBucketkey();
				    FileType ty=null;
				    //
				    boolean isFt=false;
				    if(StringUtils.isNotBlank(tn))
				    {
				    	String extendTn=FilenameUtils.getExtension(tn);
				    	ty=FileType.getFileType(extendTn);
				    	if(null!=ty)
				    	{
				    		isFt=true;
				    	}
				    }
				    
				    if(!isFt && StringUtils.isNotBlank(buckName))
				    {
				    	String extendbucket=FilenameUtils.getExtension(buckName);
				    	ty=FileType.getFileType(extendbucket);
				    	if(null!=ty)
				    	{
				    		isFt=true;
				    	}
				    }
				    
				    
				    if(!isFt)
				    {
				    	ty=FileType.OTHER_VIDEO;
				    }
				    
				    
				    //知识点
				    List<ObjectId> kwList =new ArrayList<ObjectId>();
				    
				    
				    if(StringUtils.isNotBlank(kwId) && !"null".equalsIgnoreCase(kwId))
				    {
				    	try
				    	{
				    		String[] kwIdArr=kwId.split("\\|");
				    		for(String kwi:kwIdArr)
				    		{
						    	ResourceDictionaryEntry rde=resourceDictionaryDao.getResourceDictionaryEntryBySort(Long.valueOf(kwi.trim()),8);
						    	if(null==rde)
						    	{
						    		System.out.println("没有相应的知识点;"+i+","+stringKws.get(i));
									continue;
						    	}
						    	kwList.add(rde.getID());
				    		}
				    	}catch(Exception ex)
				    	{
				    		System.out.println("处理知识点报错;"+i+","+stringKws.get(i));
							continue;
				    	}
				    }
				    
				    //章节
				    List<ObjectId> psbList =new ArrayList<ObjectId>();
				    if(StringUtils.isNotBlank(zhangjieId) && !"null".equalsIgnoreCase(zhangjieId))
				    {
				    	try
				    	{
				    		String[] zhangjieIdArr=zhangjieId.split("\\|");
				    		for(String zj:zhangjieIdArr)
				    		{
						    	ResourceDictionaryEntry rde=resourceDictionaryDao.getResourceDictionaryEntryBySort(Long.valueOf(zj.trim()),6);
						    	if(null==rde)
						    	{
						    		System.out.println("没有相应的章节;"+i+","+stringmulus.get(i));
									continue;
						    	}
						    	psbList.add(rde.getID());
				    		}
				    	}catch(Exception ex)
				    	{
				    		System.out.println("没有相应的章节;"+i+","+stringmulus.get(i));
							continue;
				    	}
				    }
				    
				  
				    
				    ObjectId id =new ObjectId();
				    
				    ResourceEntry re=new ResourceEntry(ty.getType(), 
				    		e.getName(),
				    		e.getUpdateDate(),
				    		e.getLength(),
				    		e.getImgUrl(),
				    		e.getUpdateState(),
				    		e.getDeleteFlag(), 
				    		e.getTemporaryName(),
				    		e.getBucketkey(),
				    		e.getPersistentId(),
				    		e.getViewNumber(),
				    		e.getVideoSourceType(),
				    		e.getVideoType(),
				    		null,
				    		null,
				    		0, 
				    		kwList,
				    		psbList,
							0,1,0,null,new ArrayList<ObjectId>(),"资源板块后端"
							);
				    re.setID(id);
				    
				    resourceDao.addResource(re);
				    
				    FileUtils.write(f, "\r\n",true);
					FileUtils.write(f, i+"="+id.toString(),true);
					FileUtils.write(f, "\r\n", true);
					FileUtils.write(f, "\r\n", true);
					
			}catch(Exception ex)
			{
				System.out.println("出错;"+i+","+stringNames.get(i));
			}
			
		}
	}
}
