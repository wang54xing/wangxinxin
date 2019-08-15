package com.fulaan.commonupload.controller;

import com.cloopen.rest.sdk.utils.encoder.BASE64Decoder;
import com.fulaan.annotation.SessionNeedless;
import com.fulaan.base.controller.BaseController;
import com.fulaan.folder.service.FolderService;
import com.fulaan.homeschool.dto.FileDTO;
import com.fulaan.screenshot.Encoder;
import com.fulaan.screenshot.MultimediaInfo;
import com.fulaan.utils.FileType;
import com.fulaan.utils.Platform;
import com.fulaan.utils.QiniuFileUtils;
import com.fulaan.utils.ReadUtil;
import com.fulaan.video.service.VideoService;
import com.pojo.app.FileUploadDTO;
import com.pojo.video.VideoDTO;
import com.pojo.video.VideoEntry;
import com.pojo.video.VideoSourceType;
import com.sys.constants.Constant;
import com.sys.exceptions.FileUploadException;
import com.sys.exceptions.IllegalParamException;
import com.sys.props.Resources;
import com.sys.utils.FileUtil;
import com.sys.utils.RespObj;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;






import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** 普通的文件，视频上传
 * Created by qinbo on 15/5/13.
 */

@Controller
@RequestMapping("/commonupload")
public class CommonUploadController extends BaseController {

	private static final Logger logger =Logger.getLogger(CommonUploadController.class);


    private VideoService videoService = new VideoService();

    private FolderService folderService = new FolderService();

    @RequestMapping("/video")
    @ResponseBody
    public Map<String, Object> uploadVideo(HttpServletRequest req,
                                           @RequestParam("Filedata") MultipartFile file, @RequestParam("type") String type) throws Exception {


        String fileName = FilenameUtils.getName(file.getOriginalFilename());


        String videoFilekey = new ObjectId().toString() + Constant.POINT + FilenameUtils.getExtension(fileName);
        String bathPath = Resources.getProperty("upload.file");
        File dir = new File(bathPath);
        if (!dir.exists()) {
            dir.mkdir();
        }

        File savedFile = new File(bathPath, videoFilekey);
        OutputStream stream = new FileOutputStream(savedFile);
        stream.write(file.getBytes());
        stream.flush();
        stream.close();

        String coverImage = new ObjectId().toString() + ".jpg";
        Encoder encoder = new Encoder();
        File screenShotFile = new File(bathPath, coverImage);
        //是否生成了图片
        boolean isCreateImage = false;
        try {
            encoder.getImage(savedFile, screenShotFile, 1, 480, 270);
            isCreateImage = true;
        } catch (Exception ex) {
            //logger.error("", ex);
        }

        //开始上传

        //上传图片
        String imgUrl = null;
        if (isCreateImage&&screenShotFile.exists()) {
            QiniuFileUtils.uploadFile(coverImage, new FileInputStream(screenShotFile), QiniuFileUtils.TYPE_IMAGE);

            imgUrl = QiniuFileUtils.getPath(QiniuFileUtils.TYPE_IMAGE, coverImage);
        }
        MultimediaInfo m=null;
        try {
           m= encoder.getInfo(savedFile);
        }catch (Exception e){

            logger.error("", e);
        }

        long ls = m.getDuration();
        VideoEntry ve = new VideoEntry(fileName, file.getSize(), VideoSourceType.USER_VIDEO.getType(), videoFilekey);
        ve.setVideLong(ls);
        ve.setVideoSourceType(VideoSourceType.VOTE_VIDEO.getType());


        if(StringUtils.isNotBlank(type))
        {
        	try
        	{
        		VideoSourceType vsType=VideoSourceType.getVideoSourceType(Integer.valueOf(type));
        		 ve.setVideoSourceType(vsType.getType());
        	}catch(Exception ex)
        	{
        		logger.error("", ex);
        	}
        }



        ve.setID(new ObjectId());
        QiniuFileUtils.uploadVideoFile(ve.getID(), videoFilekey, file.getInputStream(), QiniuFileUtils.TYPE_USER_VIDEO);
        if(imgUrl==null||imgUrl==""){
            imgUrl="/images/micro/video-img.png";
        }

        if (isCreateImage&&screenShotFile.exists()) {
            ve.setImgUrl(imgUrl);

        }

        ObjectId videoId = videoService.addVideoEntry(ve);


        Map<String, Object> retMap = new HashMap<String, Object>();

        VideoDTO videoDTO = new VideoDTO(ve);
        videoDTO.setId(videoId.toString());
        videoDTO.setImageUrl(ve.getImgUrl());

        retMap.put("uploadType", "视频上传成功！");
        retMap.put("result", true);
        retMap.put("videoInfo", videoDTO);


        return retMap;

    }

    @RequestMapping("/base64image")
    @ResponseBody
    public Map<String, Object> uploadBase64Image(String base64ImgData,HttpServletRequest req)throws Exception {



        BASE64Decoder d = new BASE64Decoder();
        byte[] bs = d.decodeBuffer(base64ImgData);
        String filekey = new ObjectId()+".png";

        InputStream byteStream = new ByteArrayInputStream(bs);
        String parentPath = req.getServletContext().getRealPath("/upload")+"/homework";
        File parentFile = new File(parentPath);
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
        req.getServletContext().getRealPath("/upload");
        String urlPath = "/upload/homework/"+filekey;
        File attachFile =new File(parentFile, filekey);
        try {
            FileUtils.copyInputStreamToFile(byteStream, attachFile);
        }
        catch (Exception ioe){

        }

        Map<String, Object> retMap = new HashMap<String, Object>();

        retMap.put("name",filekey);
        retMap.put("path",urlPath);
        return retMap;
    }

    /**
     * 一般性文档上传
     *
     * @param request
     * @param req
     * @return
     */
    @RequestMapping("/doc/upload")
    @ResponseBody
    public RespObj uploadDocFile(MultipartRequest request,HttpServletRequest req,
                                 @RequestParam(required = false, defaultValue = "0") int useName){
        List<FileUploadDTO> fileInfos = new ArrayList<FileUploadDTO>();
        Map<String, MultipartFile> fileMap = request.getFileMap();
        for (MultipartFile file : fileMap.values()) {
        	try
        	{
        		String dirPath=req.getServletContext().getRealPath("/upload/commondoc");
        		File fileDir =new File(dirPath);
        		if(!fileDir.exists())
        		{
        			fileDir.mkdir();
        		}

        		File destFile=new File(fileDir, file.getOriginalFilename());
        		file.transferTo(destFile);

        		ObjectId id =new ObjectId();

	            String fileKey = id.toString()+Constant.POINT+FilenameUtils.getExtension(file.getOriginalFilename());
                if(useName == 1){
                    String fileName = file.getOriginalFilename();
                    fileKey = fileName.substring(0, fileName.lastIndexOf(".")) + "-" + id.toString() + "." + FilenameUtils.getExtension(fileName);
                }

	            logger.info("User:["+getUserId()+"] try upload file:"+fileKey);




	            InputStream inputStream =new FileInputStream(destFile);


	            String extName =FilenameUtils.getExtension(file.getOriginalFilename());
	            String path="";
	            if(extName.equalsIgnoreCase("amr"))
	            {
	            	String saveFileKey=new ObjectId().toString()+".mp3";
	            	com.sys.utils.QiniuFileUtils.convertAmrToMp3(fileKey, saveFileKey, inputStream);
	            	path =QiniuFileUtils.getPath(QiniuFileUtils.TYPE_DOCUMENT, saveFileKey);
	            }
	            else
	            {
	            	QiniuFileUtils.uploadFile(fileKey,inputStream,QiniuFileUtils.TYPE_DOCUMENT);
		            path  =QiniuFileUtils.getPath(QiniuFileUtils.TYPE_DOCUMENT, fileKey);
	            }

	            FileUploadDTO dto =new FileUploadDTO(id.toString(), fileKey, file.getOriginalFilename(), path);
	            fileInfos.add(dto);
        	}catch(Exception ex)
        	{
        		logger.error("", ex);
        	}
        }
        RespObj obj =new RespObj(Constant.SUCCESS_CODE,fileInfos);
        return obj;
    }


    /**
     * 文件下载
     * @param type
     * @param fileKey
     * @param response
     * @return
     */
    @RequestMapping("/doc/down")
    public String downFile(HttpServletRequest request, int type,String fileKey,HttpServletResponse response, @RequestParam(required = false, defaultValue = "") String fileName)
    {
        try {
            if (fileName.equals("")) {
                fileName = fileKey;
            }

            String qiniuPath = QiniuFileUtils.getPath(type, fileKey);
            
            fileName = URLEncoder.encode(fileName, "UTF-8");
//            if(request.getHeader("User-Agent").toUpperCase().indexOf("MSIE") > 0){
//            } else {
//            	fileName = new String(fileName.getBytes("utf-8"), "ISO8859-1");
//            }
            
            response.setCharacterEncoding("utf-8");
            response.setContentType("multipart/form-data");
            response.setHeader("Content-Disposition", "attachment;fileName=" + fileName);

            try {
                InputStream inputStream = QiniuFileUtils.downFileByUrl(qiniuPath);
                OutputStream os = response.getOutputStream();
                byte[] b = new byte[2048];
                int length;
                while ((length = inputStream.read(b)) > 0) {
                    os.write(b, 0, length);
                }
                os.close();
                inputStream.close();
            } catch (IOException ex) {
                logger.error("", ex);
            }
        }catch (Exception e){

        }

        return null;
    }

    /**
     * 一般性文档上传
     *
     * @param request
     * @param req
     * @return
     */
    @SessionNeedless
    @RequestMapping("/upload")
    @ResponseBody
    public RespObj uploadFile(MultipartRequest request,HttpServletRequest req,@RequestParam(required = false, defaultValue = "") String dirId,@RequestParam(required = false, defaultValue = "2") int type){
        List<FileUploadDTO> fileInfos = new ArrayList<FileUploadDTO>();
        Map<String, MultipartFile> fileMap = request.getFileMap();
        for (MultipartFile file : fileMap.values()) {
            try
            {
                String imgUrl = "";
                String path = "";
                long size = file.getSize();
                String fileName = file.getOriginalFilename();
                String fileIndex = FilenameUtils.getExtension(fileName);
                String newFileIndex = fileIndex.toLowerCase();
                ObjectId id =new ObjectId();
                String fileKey =id+Constant.POINT+FilenameUtils.getExtension(file.getOriginalFilename());
                if (FileType.getDocument().contains(newFileIndex)) {
                    QiniuFileUtils.uploadFile(fileKey,file.getInputStream(),QiniuFileUtils.TYPE_DOCUMENT);
                    path  =QiniuFileUtils.getPath(QiniuFileUtils.TYPE_DOCUMENT, fileKey);
                } else if (FileType.getAudio().contains(newFileIndex)) {
//                    String saveFileKey=new ObjectId().toString()+".mp3";
//                    com.sys.utils.QiniuFileUtils.convertAmrToMp3(fileKey, saveFileKey, file.getInputStream());
//                    path =QiniuFileUtils.getPath(QiniuFileUtils.TYPE_SOUND, saveFileKey);
                    QiniuFileUtils.uploadFile(fileKey,file.getInputStream(),QiniuFileUtils.TYPE_SOUND);
                    path  =QiniuFileUtils.getPath(QiniuFileUtils.TYPE_SOUND, fileKey);
                }
                else if (FileType.getPic().contains(newFileIndex)) {
                   QiniuFileUtils.uploadFile(fileKey, file.getInputStream(), QiniuFileUtils.TYPE_IMAGE);
                    path  =QiniuFileUtils.getPath(QiniuFileUtils.TYPE_IMAGE, fileKey);
                } else if (FileType.getVideo().contains(newFileIndex)) {
                    //视频filekey
                    String bathPath= Resources.getProperty("upload.file");
                    File dir =new File(bathPath);
                    if(!dir.exists())
                    {
                        dir.mkdir();
                    }

                    File savedFile = new File(bathPath, fileKey);

                    OutputStream stream =new FileOutputStream(savedFile);
                    stream.write(file.getBytes());
                    stream.flush();
                    stream.close();

                    String coverImage = new ObjectId().toString() + ".jpg";
                    Encoder encoder = new Encoder();
                    File screenShotFile = new File(bathPath, coverImage);
//                    long videoLength = 60000;//缺省一分钟
                    //是否生成了图片
                    boolean isCreateImage=false;
                    try
                    {
                        encoder.getImage(savedFile, screenShotFile, 1, 480, 270);
//                        videoLength = encoder.getInfo(savedFile).getDuration();
                        isCreateImage=true;
                    }catch(Exception ex)
                    {
                        logger.error("", ex);
                    }
                    //上传图片
                    if(isCreateImage && screenShotFile.exists())
                    {
                        RespObj obj= QiniuFileUtils.uploadFile(coverImage, new FileInputStream(screenShotFile), QiniuFileUtils.TYPE_IMAGE);
                        if(!obj.getCode().equals(Constant.SUCCESS_CODE))
                        {
                            QiniuFileUtils.deleteFile(QiniuFileUtils.TYPE_VIDEO, fileKey);
                            obj =new RespObj(Constant.FAILD_CODE, "视频图片上传失败");
                            return obj;
                        }
                    }
                    QiniuFileUtils.uploadVideoFile(new ObjectId(),fileKey, file.getInputStream(), QiniuFileUtils.TYPE_USER_VIDEO);
                    if(isCreateImage&&screenShotFile.exists())
                    {
                        imgUrl = QiniuFileUtils.getPath(QiniuFileUtils.TYPE_IMAGE, coverImage);
                    }
                    //删除临时文件
                    try
                    {
                        savedFile.delete();
                        screenShotFile.delete();
                    }catch(Exception ex)
                    {
                        logger.error("", ex);
                    }
                    path = QiniuFileUtils.getPath(QiniuFileUtils.TYPE_USER_VIDEO, fileKey);
                } else {
                    QiniuFileUtils.uploadFile(fileKey,file.getInputStream(),QiniuFileUtils.TYPE_OTHER);
                    path  =QiniuFileUtils.getPath(QiniuFileUtils.TYPE_OTHER, fileKey);
                }
                if (!StringUtils.isEmpty(dirId)) {
                    folderService.addFile(new FileDTO(file.getOriginalFilename(),path,getSchoolId().toString(),getUserId().toString(),dirId,Constant.ONE, type,file.getSize()));
                }
                FileUploadDTO dto =new FileUploadDTO(id.toString(), fileKey, file.getOriginalFilename(), path);
                dto.setSize(size);
                dto.setImgurl(imgUrl);
                fileInfos.add(dto);
            }catch(Exception ex)
            {
                logger.error("", ex);
            }
        }
        RespObj obj =new RespObj(Constant.SUCCESS_CODE,fileInfos);
        return obj;
    }

    @RequestMapping("/m3u8ToMp4DownLoad")
    @ResponseBody
    public Map m3u8ToMp4DownLoad(String filePath,String fileName,HttpServletRequest request, HttpServletResponse response) throws IOException, IllegalParamException {
        Map<String, Object> model = new HashMap<String, Object>();
        folderService.m3u8ToMp4DownLoad(filePath,fileName, request, response);
        return model;
    }

    /**
     * 上传APK
     * @param request
     * @param req
     * @return
     */
    @SessionNeedless
    @RequestMapping("/app/upload")
    @ResponseBody
    public Map uploadFile(MultipartRequest request,HttpServletRequest req){
        Map map = new HashMap();
//        List<FileUploadDTO> fileInfos = new ArrayList<FileUploadDTO>();
        Map<String, MultipartFile> fileMap = request.getFileMap();
        String path  = "";
        for (MultipartFile file : fileMap.values()) {
            try
            {
                ObjectId id =new ObjectId();
                map.put("id",id.toString());
                String fileKey =id+Constant.POINT+FilenameUtils.getExtension(file.getOriginalFilename());
                map.put("fileKey",fileKey);
                map.put("fileName",file.getOriginalFilename());
                map.put("size",file.getSize());
                QiniuFileUtils.uploadFile(fileKey,file.getInputStream(),QiniuFileUtils.TYPE_DOCUMENT);
                path  =QiniuFileUtils.getPath(QiniuFileUtils.TYPE_DOCUMENT, fileKey);
                map.put("path",path);
                String dirPath=req.getServletContext().getRealPath("/upload/apk");
                System.out.println(dirPath);
                File fileDir =new File(dirPath);
                if(!fileDir.exists())
                {
                    fileDir.mkdir();
                }

                File destFile=new File(fileDir, file.getOriginalFilename());
                file.transferTo(destFile);
                Map<String,Object> mapApk = ReadUtil.readAPK(dirPath+"/"+file.getOriginalFilename());
                map.put("packageName", mapApk.get("package"));
                map.put("versionCode", mapApk.get("versionCode"));
                map.put("versionName", mapApk.get("versionName"));
            } catch (Exception e) {
                logger.error("error", e);
                System.out.println(e.getMessage());
            }

        }
//        RespObj obj =new RespObj(Constant.SUCCESS_CODE,fileInfos);
        return map;
    }
    
    
    /**
     * 上传单个图片
     * @param file
     * @return
     * @throws IOException 
     * @throws IllegalParamException 
     * @throws FileUploadException 
     */
    @RequestMapping(value = "/uploadSingleFile")
    @ResponseBody
    public  RespObj uploadSingleFile(MultipartFile file) throws IllegalParamException, IOException, FileUploadException {
        Map<String,String> result = new HashMap<String,String>();
        
        String key=new ObjectId().toString();
        String examName =key+"."+FilenameUtils.getExtension(file.getOriginalFilename());
        RespObj upladTestPaper= QiniuFileUtils.uploadFile(examName, file.getInputStream(), QiniuFileUtils.TYPE_DOCUMENT);
        if(upladTestPaper.getCode()!= Constant.SUCCESS_CODE)
        {
            throw new FileUploadException();
        }
        result.put("key", key);
        result.put("name", file.getOriginalFilename());
        result.put("path", QiniuFileUtils.getPath(QiniuFileUtils.TYPE_IMAGE,examName));
        
        
        return new RespObj(Constant.SUCCESS_CODE, result);
    }

}