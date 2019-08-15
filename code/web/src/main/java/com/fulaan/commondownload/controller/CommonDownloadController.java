package com.fulaan.commondownload.controller;

import com.fulaan.base.controller.BaseController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by qinbo on 15/6/9.
 */
@Controller
@RequestMapping("/commondownload")
public class CommonDownloadController extends BaseController {
    @RequestMapping("/file")
    @ResponseBody
    public Map<String, Object> downloadFile(HttpServletRequest req,HttpServletResponse response,String url,
                                            @RequestParam(required = false) String name) throws Exception {

        //存在本地upload
        if(url.startsWith("/upload")){
            String uploadpath=req.getServletContext().getRealPath("/upload");
            String filepath = uploadpath+url.substring(7);//去掉upload
            File downFile = new File(filepath);
            if(downFile.exists()){
                response.setCharacterEncoding("utf-8");
                response.setContentType("multipart/form-data");
                String filename = name;
                if(filename==null){
                    filename = url.substring(url.lastIndexOf("/"));
                }
                response.setHeader( "Content-Disposition", "attachment;filename=" + new String(
                        filename.getBytes("utf-8"), "ISO8859-1" ) );

                try {
                    InputStream inputStream = new FileInputStream(downFile);
                    OutputStream os = response.getOutputStream();
                    byte[] b = new byte[2048];
                    int length;
                    while ((length = inputStream.read(b)) > 0) {
                        os.write(b, 0, length);
                    }
                    os.close();
                    inputStream.close();
                }  catch (IOException ex) {
                    //logger.error("", ex);
                }
            }
        }


        Map<String,Object> retMap = new HashMap<String, Object>();

        return retMap;

    }
}