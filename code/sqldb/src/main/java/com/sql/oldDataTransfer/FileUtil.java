package com.sql.oldDataTransfer;

import com.qiniu.api.auth.digest.Mac;
import com.qiniu.api.io.IoApi;
import com.qiniu.api.io.PutExtra;
import com.qiniu.api.io.PutRet;
import com.qiniu.api.rs.BatchCallRet;
import com.qiniu.api.rs.EntryPath;
import com.qiniu.api.rs.PutPolicy;
import com.qiniu.api.rs.RSClient;
import com.qiniu.api.rsf.ListItem;
import com.qiniu.api.rsf.ListPrefixRet;
import com.qiniu.api.rsf.RSFClient;
import com.qiniu.api.rsf.RSFEofException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by qinbo on 15/3/20.
 */
public class FileUtil {

    public static String tempFilePath = "/Users/qinbo/k6kt-temp/";


    /**
     * 从输入流中获取数据
     * @param inStream 输入流
     * @return
     * @throws Exception
     */
    public static byte[] readInputStream(InputStream inStream) throws Exception{
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while( (len=inStream.read(buffer)) != -1 ){
            outStream.write(buffer, 0, len);
        }
        inStream.close();
        return outStream.toByteArray();
    }


    /**
     * 根据地址获得数据的字节流
     * @param strUrl 网络连接地址
     * @return
     */
    public static byte[] getFileFromNetByUrl(String strUrl){
        try {
            URL url = new URL(strUrl);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5 * 1000);
            InputStream inStream = conn.getInputStream();//通过输入流获取图片数据
            byte[] btFile = readInputStream(inStream);//得到文件的二进制数据
            return btFile;
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /** 保存路径 /Users/qinbo/k6kt-image/
     * 将图片写入到磁盘
     * @param fb 图片数据流
     * @param fileName 文件保存时的名称
     */
    public static void writeFileToDisk(byte[] fb, String fileName){
        try {
            File file = new File(tempFilePath + fileName);
            FileOutputStream fops = new FileOutputStream(file);
            fops.write(fb);
            fops.flush();
            fops.close();
            System.out.println("图片已经写入");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void downloadFromUrl(String url,String fileName){
        byte[] btImg = getFileFromNetByUrl(url);
        if(null != btImg && btImg.length > 0){
            System.out.println("读取到：" + btImg.length + " 字节");

            writeFileToDisk(btImg, fileName);
        }else{
            System.out.println("没有从该连接获得内容");
        }
    }

    public static void eraseQiniuImage(){
        Mac mac = new Mac(com.sys.props.Resources.getProperty("qiniu.ACCESS_KEY"),
                com.sys.props.Resources.getProperty("qiniu.SECRET_KEY"));
        String bucketName = "k6kt-image";

        RSFClient client = new RSFClient(mac);
        RSClient rs = new RSClient(mac);
        String marker = "";

        ListPrefixRet ret = null;

        while (true) {
            ret = client.listPrifix(bucketName, "", marker, 100);
            List<EntryPath> entries = new ArrayList<EntryPath>();
            for(ListItem li : ret.results)
            {
                EntryPath e1 = new EntryPath();
                e1.bucket = bucketName;
                e1.key = li.key;
                entries.add(e1);
            }
            marker = ret.marker;
            BatchCallRet bret = rs.batchDelete(entries);
            System.out.println("delete 100");
            if (!ret.ok()) {
                // no more items or error occurs
                break;
            }
        }
        if (ret.exception.getClass() != RSFEofException.class) {
            // error handler
        }

        System.out.println("delete finished");


    }





    public static void uploadToQiNiu(String filePath,String bucketName,
                                     String fileKey) throws Exception{
        Mac mac = new Mac(com.sys.props.Resources.getProperty("qiniu.ACCESS_KEY"),
                com.sys.props.Resources.getProperty("qiniu.SECRET_KEY"));
        //String bucketName = "k6kt-image";
        PutPolicy putPolicy = new PutPolicy(bucketName);
        String uptoken = putPolicy.token(mac);
        PutExtra extra = new PutExtra();
        String key = fileKey;
        String localFile = filePath;
        
        
       // PutRet ret = IoApi.putFile(uptoken, key, localFile, extra);
        InputStream reader =new FileInputStream(localFile);
        PutRet ret = IoApi.Put(uptoken, key, reader, extra);
        
       
        
        System.out.println("hash="+ret.getHash());
        System.out.println("key="+ret.getKey());
        System.out.println("response="+ret.getResponse());
        System.out.println("code="+ret.getStatusCode());
        
    }

    
    
    
    
    
    
    
    
    
    public static void main(String[] args){
        //eraseQiniuImage();
    	
    	String filePath="e:\\MsOYIWClVcfYOiKiPhZW.jpg";
    	String bucketName ="k6kt-test";
    	String fileKey="456.jpg";
    	
    	try {
			uploadToQiNiu(filePath,bucketName,fileKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    }


}
