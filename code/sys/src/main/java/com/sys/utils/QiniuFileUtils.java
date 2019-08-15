package com.sys.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;

import com.fasterxml.jackson.databind.ObjectMapper;


import com.qiniu.api.auth.digest.Mac;
import com.qiniu.api.io.IoApi;
import com.qiniu.api.io.PutExtra;
import com.qiniu.api.io.PutRet;
import com.qiniu.api.net.EncodeUtils;
import com.qiniu.api.resumableio.ResumeableIoApi;
import com.qiniu.api.rs.PutPolicy;
import com.qiniu.api.rs.RSClient;
import com.sys.constants.Constant;
import com.sys.exceptions.IllegalParamException;
import com.sys.props.Resources;
import com.sys.utils.RespObj;

/**
 * 七牛云存储操作类
 * 统一处理文件，图片上传问题
 * @author fourer
 *
 */
public class QiniuFileUtils {

	private static final Logger logger =Logger.getLogger(QiniuFileUtils.class);
	private static final String FILE_PATH_FORMAT="http://{0}/{1}";
	 
	
	
	/**
	 * 图片
	 */
	public static final int TYPE_IMAGE=1;
	/**
	 * 文档
	 */
	public static final int TYPE_DOCUMENT=2;
	/**
	 * 声音
	 */
	public static final int TYPE_SOUND=3;
	/**
	 * 视频
	 */
	public static final int TYPE_VIDEO=4;
	/**
	 * 用户视频
	 */
	public static final int TYPE_USER_VIDEO=5;

    /**/
    public static final int TYPE_FLASH = 6;
	
	
	private static final Mac MAC ;
	private static final RSClient CLIENT;
	private static final Map<Integer, BucketInfo> BUCKET_MAP =new HashMap<Integer, BucketInfo>();

	
	private static final ExecutorService pool = Executors.newFixedThreadPool(Constant.FIVE); 
	
	static
	{
		logger.info("qiniu.ACCESS_KEY="+com.sys.props.Resources.getProperty("qiniu.ACCESS_KEY"));
		logger.info("qiniu.SECRET_KEY="+com.sys.props.Resources.getProperty("qiniu.SECRET_KEY"));
		
		
		MAC = new Mac(
				      com.sys.props.Resources.getProperty("qiniu.ACCESS_KEY"),
				      com.sys.props.Resources.getProperty("qiniu.SECRET_KEY")
				     );
		
		CLIENT=new RSClient(MAC);
		
		String bucketConf=com.sys.props.Resources.getProperty("qiniu.bucket.conf");
		
		if(StringUtils.isNotBlank(bucketConf))
		{
			String[] bucketArr=bucketConf.split(Constant.SEMICOLON);
			BucketInfo info;
			for(String bucketStr:bucketArr)
			{
				String[] bucketInfoArr=bucketStr.split(Constant.COMMA);
				info=new BucketInfo(Integer.valueOf(bucketInfoArr[0]), bucketInfoArr[1], bucketInfoArr[2]);
				BUCKET_MAP.put(info.getType(),info);
			}
		}
		
		logger.info("bucket conf:"+BUCKET_MAP);
	}
	
    /** 上传视频（其他普通文件使用普通文件上传）
     * @param videoId
     * @param fileKey
     * @param stream
     * @param type
     * @return
     * @throws IllegalParamException
     * @throws IOException
     * @throws ExecutionException 
     * @throws InterruptedException 
     */
    public static RespObj uploadVideoFile(final ObjectId videoId,String fileKey,InputStream stream, int type) throws IllegalParamException,IOException, InterruptedException, ExecutionException{
        if(type != TYPE_VIDEO && type != TYPE_USER_VIDEO){
            throw new IllegalParamException(" please use QiniuFileUtils.uploadFile to upload normal file(not video)");
        }


        String persistentBuckeName = BUCKET_MAP.get(TYPE_VIDEO).bucket;
        if(type == TYPE_USER_VIDEO){
            persistentBuckeName = Resources.getProperty("qiniu.store.bucket");
        }

        String m3u8_save_as = persistentBuckeName + ":m3u8/" + fileKey + ".m3u8";
        m3u8_save_as = EncodeUtils.urlsafeEncode(m3u8_save_as);
        String persistentOps = "avthumb/m3u8/preset/video_640k|saveas/" + m3u8_save_as;

        final String finalPersistentOps = persistentOps;
        final String finalBucketName = BUCKET_MAP.get(type).getBucket();
        final String finalBucketSaveKey = fileKey;
        final InputStream inputStream = stream;
        final PutPolicy putPolicy = new PutPolicy(finalBucketName);
        putPolicy.persistentOps = finalPersistentOps;
        putPolicy.persistentNotifyUrl = Resources.getProperty("domain") + "/video/persistentNotify.do";
        logger.info(putPolicy.persistentNotifyUrl);
        
        
        VideoCall vc =new VideoCall(putPolicy, inputStream, finalBucketSaveKey);
        Future<PutRet> f1 = pool.submit(vc);
        ObjectMapper objectMapper = new ObjectMapper();
        @SuppressWarnings("rawtypes")
		HashMap retMap = objectMapper.readValue(f1.get().getResponse(), HashMap.class);
        String persistentId = (String)retMap.get("persistentId");
        RespObj obj =new RespObj(Constant.SUCCESS_CODE,persistentId);
        return obj;
       
    }
    
    
    /**
     * 将amr音频转化成mp3
     * @param fileKey
     * @param saveFileKey
     * @param stream
     * @return
     * @throws IllegalParamException
     * @throws IOException
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public static RespObj convertAmrToMp3(String fileKey, String saveFileKey,InputStream stream) throws IllegalParamException,IOException, InterruptedException, ExecutionException{
        String persistentBuckeName = BUCKET_MAP.get(TYPE_DOCUMENT).bucket;
        String m3u8_save_as = persistentBuckeName + ":"+saveFileKey;
        m3u8_save_as = EncodeUtils.urlsafeEncode(m3u8_save_as);
        String persistentOps = "avthumb/mp3/ab/64k/ar/22050|saveas/" + m3u8_save_as;

        final String finalPersistentOps = persistentOps;
        final String finalBucketName = persistentBuckeName;
        final String finalBucketSaveKey = fileKey;
        final InputStream inputStream = stream;
        final PutPolicy putPolicy = new PutPolicy(finalBucketName);
        putPolicy.persistentOps = finalPersistentOps;
       // putPolicy.persistentNotifyUrl = Resources.getProperty("domain") + "/video/persistentNotify.do";
        logger.info(putPolicy.persistentNotifyUrl);
        
        
        VideoCall vc =new VideoCall(putPolicy, inputStream, finalBucketSaveKey);
        Future<PutRet> f1 = pool.submit(vc);
        ObjectMapper objectMapper = new ObjectMapper();
        @SuppressWarnings("rawtypes")
		HashMap retMap = objectMapper.readValue(f1.get().getResponse(), HashMap.class);
        String persistentId = (String)retMap.get("persistentId");
        
        System.out.println("persistentId="+persistentId);
        RespObj obj =new RespObj(Constant.SUCCESS_CODE,persistentId);
        return obj;
       
    }
    

    /** 普通文件上传（不包括视频)
     * @param fileKey
     * @param stream
     * @param type
     * @return
     * @throws IllegalParamException
     */
    public static RespObj uploadFile(String fileKey, InputStream stream,
			 int type) throws IllegalParamException {

        if(type == TYPE_VIDEO || type == TYPE_USER_VIDEO){
            throw new IllegalParamException(" please use QiniuFileUtils.uploadVideoFile to upload video file");
        }


		BucketInfo info =BUCKET_MAP.get(type);
		if(null==info)
			throw new IllegalParamException();
		RespObj obj =new RespObj(Constant.FAILD_CODE);
		try {
			PutRet ret =uploadToQiNiu(stream,info.getBucket(),fileKey);
			logger.info(ret);
			String code=Constant.SUCCESS_CODE;
			if(!ret.ok())
			{
				code=Constant.FAILD_CODE;
			}
			obj =new RespObj(code, ret.getResponse());
			return obj;
		} catch (IOException e) {
			obj.setMessage(e.getMessage());
			return obj;
		} catch (Exception e) {
			obj.setMessage(e.getMessage());
			return obj;
		}
	}


	
	
	private static PutRet uploadToQiNiu(InputStream reader, String bucketName,
			String fileKey) throws Exception {
		PutRet ret =null;
		try
		{
			PutPolicy putPolicy = new PutPolicy(bucketName);
			String uptoken = putPolicy.token(MAC);
			PutExtra extra = new PutExtra();
			String key = fileKey;
			ret = IoApi.Put(uptoken, key, reader, extra);
		}catch(Exception ex)
		{
			logger.error("", ex);
		}
		return ret;
	}
	
	
	/**
	 * 得到七牛完整的URL
	 * @param type
	 * @param fileKey
	 * @return
	 */
	public static String getPath(int type,String fileKey)
	{
//		 int index=fileKey.toLowerCase().indexOf("upload");
//		 if(index>=Constant.ZERO)
//		 {
//			 return "http://7xjfbm.com1.z0.glb.clouddn.com"+fileKey;
//		 }
		BucketInfo info =BUCKET_MAP.get(type);
		if(null!=info && StringUtils.isNotBlank(fileKey))
		{
            if(type==TYPE_VIDEO||type==TYPE_USER_VIDEO){
                fileKey = "m3u8/"+fileKey+".m3u8";
            }
			return MessageFormat.format(FILE_PATH_FORMAT, info.getUrl(),fileKey);
		}
		return Constant.EMPTY;
	}
	
	
	/**
	 * 删除文件
	 * 
	 * @param path
	 * @throws IllegalParamException 
	 */
	public static void deleteFile(int type,String fileKey) throws IllegalParamException {
		BucketInfo info =BUCKET_MAP.get(type);
		if(null==info)
		{
			throw new IllegalParamException();
		}
		CLIENT.delete(info.getBucket(), fileKey);
	}
	
	
	/**
	 * 下载文件
	 * @param type
	 * @param fileKey
	 * @return
	 */
   public static InputStream downFile(int type,String fileKey){
	   String path =getPath(type, fileKey);
	   return downFileByUrl(path);
   }
	
	
	 /**
	  * 下载文件
	  * @param strUrl 
	  * @return
	  */
    public static InputStream downFileByUrl(String strUrl){
	   try {
	            URL url = new URL(strUrl);
	            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
	            conn.setRequestMethod("GET");
	            conn.setConnectTimeout(5 * 1000);
	            return conn.getInputStream();//通过输入流获取图片数据
	        } catch (Exception e) {
	        	logger.error("", e);
	        }
	        return null;
	  }
	    
    
    
    public static void main(String[] args) throws Exception {
		
    	InputStream stream=	QiniuFileUtils.downFileByUrl("http://7sbrbm.com1.z0.glb.clouddn.com/cloud/flash/20140923031228NTCogG.swf");
    	
        
    	File f =new File("d:\\20140923031228NTCogG.swf");
    	
    	
    	OutputStream os = new FileOutputStream(f);
    	   int bytesRead = 0;
    	   byte[] buffer = new byte[8192];
    	   while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) {
    	      os.write(buffer, 0, bytesRead);
    	   }
    	   os.close();
    	   stream.close();
    	
    	
    		
    	///FileInputStream 
    		
	}
    
    static class BucketInfo
	{
		private int type;
		private String bucket;
		private String url;
		
		
		
		public BucketInfo(int type, String bucket, String url) {
			super();
			this.type = type;
			this.bucket = bucket;
			this.url = url;
		}
		public int getType() {
			return type;
		}
		public void setType(int type) {
			this.type = type;
		}
		public String getBucket() {
			return bucket;
		}
		public void setBucket(String bucket) {
			this.bucket = bucket;
		}
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
		@Override
		public String toString() {
			return "BucketInfo [type=" + type + ", bucket=" + bucket + ", url="
					+ url + "]";
		}
		
	}
    
    static class VideoCall implements Callable<PutRet>
    {

    	private PutPolicy putPolicy;
    	private InputStream stream;
    	private String finalBucketSaveKey;
    	
    	public VideoCall( PutPolicy putPolicy,InputStream stream, String finalBucketSaveKey)
    	{
    		this.putPolicy=putPolicy;
    		this.stream=stream;
    		this.finalBucketSaveKey=finalBucketSaveKey;
    	}
		@Override
		public PutRet call() throws Exception {
            String token = putPolicy.token(MAC);
            PutRet ret = ResumeableIoApi.put(this.stream, token, this.finalBucketSaveKey, null);
            return ret;
		}
    	
    }
    
}
