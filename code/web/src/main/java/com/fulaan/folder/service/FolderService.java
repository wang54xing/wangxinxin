package com.fulaan.folder.service;

import com.fulaan.folder.dto.DirDTO;
import com.fulaan.folder.dto.FileDto;
import com.fulaan.homeschool.dto.FileDTO;
import com.fulaan.utils.FileType;
import com.fulaan.utils.Platform;
import com.fulaan.utils.QiniuFileUtils;
import com.fulaan.utils.RESTAPI.bo.notice.NoticeBaseAPI;
import com.sys.constants.Constant;
import com.sys.exceptions.IllegalParamException;
import com.sys.utils.JsonUtil;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by wang_xinxin on 2017/1/3.
 */

@Service
public class FolderService {

    private static final Logger logger= Logger.getLogger(FolderService.class);

    /**
     * 目录下的文件
     * @param dirId
     * @return
     */
    public Map getFolderList(String dirId,int sort) {
        Map map = new HashMap();
        String resultStr = NoticeBaseAPI.getFolderList(dirId,sort);
        List<FileDto> fileDTOs = new ArrayList<FileDto>();
        try {
            JSONObject dataJson = new JSONObject(resultStr);
            JSONObject json = dataJson.getJSONObject("message");
            JSONArray infos = json.getJSONArray("rows");
            JSONArray infos2 = json.getJSONArray("ids");
            JSONArray infos3 = json.getJSONArray("paths");
            if(infos!=null&&infos.length()>0) {
                for (int j = 0; j < infos.length(); j++) {
                    JSONObject info = infos.getJSONObject(j);
                    FileDto dto = (FileDto) JsonUtil.JSONToObj(info.toString(), FileDto.class);
                    dto.setFileType(getFileType(dto.getFileType()));
                    dto.setRealPath(dto.getFilePath().substring(dto.getFilePath().lastIndexOf('/') + 1));
                    fileDTOs.add(dto);
                }
            }
            map.put("rows",fileDTOs);
            List<String> idList = new ArrayList<String>();
            if (infos2!=null && infos2.length()>0) {
                for (int i = 0; i < infos2.length(); i++) {
                    idList.add(infos2.get(i).toString());
                }
            }
            map.put("ids",idList);
            List<String> pathList = new ArrayList<String>();
            if (infos3!=null && infos3.length()>0) {
                for (int z = 0; z < infos3.length(); z++) {
                    pathList.add(infos3.get(z).toString());
                }
            }
            map.put("paths",pathList);
        } catch (Exception e) {
            e.getMessage();
        }

        return map;
    }

    /**
     * 文件目录ID
     * @param schoolId
     * @param userId
     * @param type 1校级文档  2 我的文档
     * @return
     */
    public String getFolderDir(ObjectId schoolId, ObjectId userId, int type) {
        String dirId = "";
        String resultStr = NoticeBaseAPI.getFolderDir(schoolId, userId, Platform.FILE.getType(), type);
        try {
            JSONObject dataJson = new JSONObject(resultStr);
            dirId = dataJson.get("message").toString();
        } catch (Exception e) {
            e.getMessage();
        }
        return dirId;
    }

    /**
     * 上传文件
     * @param fileDto
     */
    public void addFile(FileDTO fileDto) {
        NoticeBaseAPI.addFile(fileDto);
    }

    /**
     *
     * @param dto
     */
    public String fileRename(FileDTO dto) {
        return NoticeBaseAPI.fileRename(dto);
    }

    /**
     * 删除文件
     * @param fileIds
     */
    public boolean delFileByIds(String fileIds) {
        boolean flag = true;
        String resultStr = NoticeBaseAPI.getFilesByParentIds(fileIds);
        try {
            JSONObject dataJson = new JSONObject(resultStr);
            JSONArray infos = dataJson.getJSONArray("message");
            if(infos!=null&&infos.length()>0) {
                flag = false;
            }
        } catch (Exception e) {
            e.getMessage();
        }
        if (flag) {
            NoticeBaseAPI.delFileByIds(fileIds);
        }
        return flag;

    }

    /**
     * 模糊查询
     * @param schoolId
     * @param fileId
     * @param keyword
     * @return
     */
    public List<FileDto> getFileByKeyWord(ObjectId schoolId, String fileId, String keyword) {
        String resultStr = NoticeBaseAPI.getFileByKeyWord(schoolId, fileId,keyword);
        List<FileDto> fileDTOs = new ArrayList<FileDto>();
        try {
            JSONObject dataJson = new JSONObject(resultStr);
            JSONArray infos = dataJson.getJSONArray("message");
            if(infos!=null&&infos.length()>0) {
                for (int j = 0; j < infos.length(); j++) {
                    JSONObject info = infos.getJSONObject(j);
                    FileDto dto = (FileDto) JsonUtil.JSONToObj(info.toString(), FileDto.class);
                    dto.setFileType(getFileType(dto.getFileType()));
                    fileDTOs.add(dto);
                }
            }
        } catch (Exception e) {
            e.getMessage();
        }

        return fileDTOs;
    }

    /**
     * 获取文件类型
     *
     */
    private String getFileType(String fileType) {
        String type = "0";
        if (StringUtils.isEmpty(fileType)) {
            type = "0";
        } else {
            if (FileType.getDocument().contains(fileType)) {
                type = "2";
            } else if (FileType.getVideo().contains(fileType)) {
                type = "5";
            } else if(FileType.getPic().contains(fileType)) {
                type = "1";
            } else if(FileType.getAudio().contains(fileType)) {
                type = "3";
            } else {
                type = "7";
            }
        }

        return type;
    }

    /**
     *
     * @param fileUrl
     * @param request
     * @param response
     */
    public String m3u8ToMp4DownLoad(String fileUrl,String name, HttpServletRequest request, HttpServletResponse response) throws IOException, IllegalParamException {
        String filePath = fileUrl.split("#")[0];
        String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        if (".m3u8".equals(suffix)) {
            InputStream in = null;
            InputStream subIn = QiniuFileUtils.downFileByUrl(filePath);
            int i = 0;
            String filePathsStr = "";
            boolean flag = false;
            while ((i = subIn.read()) != -1) {
                String sbuStr = (char) i + "";
                if ("\n".equals(sbuStr) && flag) {
                    filePathsStr += ",";
                    flag = false;
                }
                if ("h".equals(sbuStr) || "/".equals(sbuStr) || flag) {
                    flag = true;
                    filePathsStr += sbuStr;
                }
            }
            String[] filePaths = filePathsStr.split(",");
            if (filePaths[0].contains("http://7sbrbl.com1.z0.glb.clouddn.com")) {
                in = QiniuFileUtils.downFileByUrl(filePaths[0]);
            } else {
                in = QiniuFileUtils.downFileByUrl("http://7sbrbl.com1.z0.glb.clouddn.com/" + filePaths[0]);
            }

            if (filePaths.length > 1) {
                for (int k = 1; k < filePaths.length; k++) {
                    String tempPath = "";
                    if (filePaths[k].contains("http://7sbrbl.com1.z0.glb.clouddn.com")) {
                        tempPath = filePaths[k];
                    } else {
                        tempPath = "http://7sbrbl.com1.z0.glb.clouddn.com/" + filePaths[k];
                    }
                    //tempPath=filePaths[k];
                    InputStream in2 = QiniuFileUtils.downFileByUrl(tempPath);
                    in = new SequenceInputStream(in, in2);
                }
            }

            BufferedInputStream bins = new BufferedInputStream(in);// 放到缓冲流里面
            OutputStream outs = response.getOutputStream();// 获取文件输出IO流
            BufferedOutputStream bouts = new BufferedOutputStream(outs);
            response.setContentType("application/x-download");// 设置response内容的类型
            response.setHeader("Content-disposition", "attachment; filename=" + new String(name.getBytes("utf-8"), "ISO8859-1"));// 设置头部信息

            int bytesRead = 0;
            byte[] buffer = new byte[2014];
            // 开始向网络传输文件流
            while ((bytesRead = bins.read(buffer, 0, buffer.length)) != -1) {
                bouts.write(buffer, 0, bytesRead);
            }
            bouts.flush();// 这里一定要调用flush()方法
            in.close();
            bins.close();
            outs.close();
            bouts.close();

            return "";
        }
        return "";
    }

    public List<DirDTO> getFolderList(String dirId) {
        List<DirDTO> dirDTOs = new ArrayList<DirDTO>();
        String resultStr = NoticeBaseAPI.getFolderList(dirId);
        List<FileDto> fileDTOs = new ArrayList<FileDto>();
        try {
            JSONObject dataJson = new JSONObject(resultStr);
            JSONArray infos = dataJson.getJSONArray("message");
            if(infos!=null&&infos.length()>0) {
                for (int j = 0; j < infos.length(); j++) {
                    JSONObject info = infos.getJSONObject(j);
                    DirDTO dto = (DirDTO) JsonUtil.JSONToObj(info.toString(), DirDTO.class);
                    dto.setOpen(true);
                    dirDTOs.add(dto);
                }
            }
        } catch (Exception e) {
            e.getMessage();
        }

        return dirDTOs;
    }

    /**
     * 文件下载
     * @param fileIds
     * @param request
     * @param response
     */
    public void fileDownLoad(String fileIds, HttpServletRequest request, HttpServletResponse response) {
        String resultStr = NoticeBaseAPI.getFilesByFileIds(fileIds);
        try {
            JSONObject dataJson = new JSONObject(resultStr);
            JSONArray infos = dataJson.getJSONArray("message");
            List<FileDto> fileDTOs = new ArrayList<FileDto>();
            if(infos!=null&&infos.length()>0) {
                for (int j = 0; j < infos.length(); j++) {
                    JSONObject info = infos.getJSONObject(j);
                    FileDto dto = (FileDto) JsonUtil.JSONToObj(info.toString(), FileDto.class);
                    dto.setFileType(getFileType(dto.getFileType()));
                    dto.setRealPath(dto.getFilePath().substring(dto.getFilePath().lastIndexOf('/') + 1));
                    fileDTOs.add(dto);
                }
            }
            String realPath=request.getServletContext().getRealPath("/WEB-INF/download");
            String fileName=realPath+"/"+getFileName(request, "文件管理")+".zip";
            execute(fileName,fileDTOs,response);
        } catch (Exception e) {
            e.getMessage();
        }
    }

    /**
     * 生成的ZIP文件打包压缩
     */
    public String execute(String tmpFileName, List<FileDto> fileDtos, HttpServletResponse response) {
        try {
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(response.getOutputStream()));

            response.setContentType("application/x-download");// 设置response内容的类型
            String zipName=tmpFileName.substring(tmpFileName.lastIndexOf("/") + 1);
            zipName=new String(zipName.getBytes("iso8859-1"),"UTF-8");
            response.setHeader("Content-disposition","attachment; filename="+new String(zipName.getBytes("gb2312"),"iso8859-1"));// 设置头部信息

            byte[] buffer = new byte[8192];
            int len = 0;
            for (FileDto fileDto:fileDtos) {
                try {
                    String fileName=fileDto.getName();
                    String suffix=fileName.substring(fileName.lastIndexOf("."));
                    String prefix=fileName.substring(0,fileName.lastIndexOf("."));
                    String filePath="";
                    InputStream in =null;
                    if(("5").equals(fileDto.getFileType())){
                        filePath=fileDto.getFilePath();
                        InputStream subIn =QiniuFileUtils.downFileByUrl(filePath);
                        int i=0;
                        String filePathsStr="";
                        boolean flag = false;
                        while ((i = subIn.read()) != -1) {
                            String sbuStr = (char) i + "";
                            if ("\n".equals(sbuStr) && flag) {
                                filePathsStr += ",";
                                flag = false;
                            }
                            if ("h".equals(sbuStr)||"/".equals(sbuStr)|| flag) {
                                flag = true;
                                filePathsStr += sbuStr;
                            }
                        }
                        String[] filePaths = filePathsStr.split(",");
                        if(filePaths[0].contains("http://7sb")){
                            in = QiniuFileUtils.downFileByUrl(filePaths[0]);
                        }else{
                            in = QiniuFileUtils.downFileByUrl("http://7sbrbl.com1.z0.glb.clouddn.com/"+filePaths[0]);
                        }

                        if (filePaths.length > 1) {
                            for (int k = 1; k < filePaths.length; k++) {
                                String tempPath="";
                                if(filePaths[k].contains("http://7sb")){
                                    tempPath=filePaths[k];
                                }else{
                                    tempPath="http://7sbrbl.com1.z0.glb.clouddn.com/"+filePaths[k];
                                }
                                InputStream in2 = QiniuFileUtils.downFileByUrl(tempPath);
                                in = new SequenceInputStream(in, in2);
                            }
                        }
                    }else{
                        filePath=fileDto.getFilePath();
                        in = QiniuFileUtils.downFileByUrl(filePath);
                    }
                    if (in == null) {
                        continue;
                        //throw new IllegalParamException("未找到文件信息");
                    }

                    String targetPath=prefix+suffix;

                    ZipEntry zipEntry=new ZipEntry(targetPath);
                    out.putNextEntry(zipEntry);
                    //设置压缩文件内的字符编码，不然会变成乱码
                    //out.setEncoding("UTF-8");
                    logger.debug("文件"+ targetPath);
                    BufferedInputStream bis = new BufferedInputStream(in);
                    while ((len = bis.read(buffer))!=-1) {
                        out.write(buffer, 0, len);
                    }
                    bis.close();
                    out.closeEntry();
                    in.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error("文件下载出错", e);
                }

            }
            out.close();
            //this.downFile(response, tmpFileName);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("文件下载出错", e);
        }
        return null;
    }

    /**
     * 获取导出文件的名称
     *
     * @param request
     * @param fileName
     * @return
     * @throws UnsupportedEncodingException
     */
    private String getFileName(HttpServletRequest request, String fileName) throws UnsupportedEncodingException {
        String agent = request.getHeader("User-Agent");
        if (agent != null && agent.toLowerCase().indexOf("firefox") >= 0) {
            fileName = new String(fileName.getBytes(Constant.UTF_8), Constant.ISO);
        } else {
            fileName = java.net.URLEncoder.encode(fileName, Constant.UTF_8);
        }
        return fileName;
    }
}
