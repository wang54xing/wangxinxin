package com.sys.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.sys.props.Resources;


/**
 * 通过http,调用服务器接口，实现文件转化
 * @author fourer
 *
 */
public class HttpFileConvertUtils {

	 private static  String ipAndPort;

	    static{
	            ipAndPort=Resources.getProperty("http.file.convert.path", "114.215.186.62:8080");
	          
	    }
	   

	    /**
	     * 将word转变成pdf文件
	     * @param wordFilePath 
	     * @param swfFilePath 
	     */
	    public static void convertWord2Swf(String wordFilePath,String swfFilePath) {
	        FileOutputStream fileOutputStream = null;
	        CloseableHttpClient httpClient = HttpClients.createDefault();
	        CloseableHttpResponse response;
	        HttpPost httppost = new HttpPost("http://"+ipAndPort+"/word2pdf/convert.do");

	        File file = new File(wordFilePath);
	        MultipartEntity reqEntity =
	                new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE,
	                        System.currentTimeMillis() + "",
	                        Charset.forName("utf-8"));
	        String fileType = file.getName();
	        fileType = fileType.substring(fileType.lastIndexOf('.') + 1);
	        reqEntity.addPart("file", new FileBody(file,file.getName(),fileType,"UTF-8"));
	        httppost.setEntity(reqEntity);



	        try {
	            response = httpClient.execute(httppost);
	            InputStream inputStream = response.getEntity().getContent();
	            File swfFile = new File(swfFilePath);
	            if (!swfFile.exists()) {
	                swfFile.createNewFile();
	            }
	            fileOutputStream = new FileOutputStream(swfFile);
	            int k=-1;
	            while((k=inputStream.read())!=-1){
	                fileOutputStream.write(k);
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        } finally {

	            try {
	                httpClient.close();
	                if (fileOutputStream != null) {
	                    fileOutputStream.close();
	                }
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }

	    }
	    /*
	    * 三个文件的路径必须为绝对路径
	    * pdfPath 新文件即将存放的位置
	    * swfPath 新文件即将存放的位置
	    * */
	    public static void word2PdfAndSwf(String wordPath,String swfPath,String pdfPath) {
	        FileOutputStream swfOutputStream = null;
	        FileOutputStream pdfOutputStream = null;
	        InputStream inputStream=null;
	        CloseableHttpClient httpClient = HttpClients.createDefault();
	        CloseableHttpResponse response;
	        HttpPost httppost = new HttpPost("http://"+ipAndPort+"/word2pdf/convert2.do");

	        File file = new File(wordPath);
	        MultipartEntity reqEntity =
	                new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE,
	                        System.currentTimeMillis() + "",
	                        Charset.forName("utf-8"));
	        String fileType = file.getName();
	        fileType = fileType.substring(fileType.lastIndexOf('.') + 1);
	        reqEntity.addPart("file", new FileBody(file,file.getName(),fileType,"UTF-8"));
	        httppost.setEntity(reqEntity);
	        try {
	            response = httpClient.execute(httppost);
	             inputStream = response.getEntity().getContent();
	            //separate是分隔符的位置
	            long separate=0;
	            Header[] headers=response.getAllHeaders();
	            for(Header header:headers){
	                if(header.getName().equals("separate")){
	                    separate=Long.parseLong(header.getValue());
	                }
	            }
	            File swfFile = new File(swfPath);
	            File pdfFile = new File(pdfPath);
	            if (!swfFile.exists()) {
	                swfFile.createNewFile();
	            }
	            if (!pdfFile.exists()) {
	                pdfFile.createNewFile();
	            }
	            swfOutputStream = new FileOutputStream(swfFile);
	            pdfOutputStream=new FileOutputStream(pdfFile);
	            long length=response.getEntity().getContentLength();
	            int k=-1;
	            long i=0;//记录即将要读取的位置
	            while((k=inputStream.read())!=-1){
	                i+=1;
	                if(i==separate){ break; }
	                swfOutputStream.write(k);
	            }
	            int x=-1;
	            pdfOutputStream.write(k);
	            while((x=inputStream.read())!=-1){
	                pdfOutputStream.write(x);
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        } finally {
	            try {
	                httpClient.close();
	                if (swfOutputStream != null) {
	                    swfOutputStream.flush();
	                    swfOutputStream.close();
	                }
	                if (pdfOutputStream != null) {
	                    pdfOutputStream.flush();
	                    pdfOutputStream.close();
	                }
	                if(inputStream!=null){
	                    inputStream.close();
	                }
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }

	    }

	    
	    
	    
	    public static void word2PdfAndSwf(File file,File swfFile,File pdfFile) {
	        FileOutputStream swfOutputStream = null;
	        FileOutputStream pdfOutputStream = null;
	        InputStream inputStream=null;
	        CloseableHttpClient httpClient = HttpClients.createDefault();
	        CloseableHttpResponse response;
	        HttpPost httppost = new HttpPost("http://"+ipAndPort+"/word2pdf/convert2.do");

	        MultipartEntity reqEntity =
	                new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE,
	                        System.currentTimeMillis() + "",
	                        Charset.forName("utf-8"));
	        String fileType = file.getName();
	        fileType = fileType.substring(fileType.lastIndexOf('.') + 1);
	        reqEntity.addPart("file", new FileBody(file,file.getName(),fileType,"UTF-8"));
	        httppost.setEntity(reqEntity);
	        try {
	            response = httpClient.execute(httppost);
	            inputStream = response.getEntity().getContent();
	            long  length =response.getEntity().getContentLength();
	            //separate是分隔符的位置
	            long separate=0;
	            Header[] headers=response.getAllHeaders();
	            for(Header header:headers){
	                if(header.getName().equals("separate")){
	                    separate=Long.parseLong(header.getValue());
	                }
	            }

	            System.out.println(swfFile.getAbsolutePath());
	            if (!swfFile.exists()) {
	                swfFile.createNewFile();
	            }
	            if (!pdfFile.exists()) {
	                pdfFile.createNewFile();
	            }
	            swfOutputStream = new FileOutputStream(swfFile);
	            pdfOutputStream=new FileOutputStream(pdfFile);
	            int k=-1;
	            long i=0;//记录即将要读取的位置
	            while((k=inputStream.read())!=-1){
	                i+=1;
	                if(i==separate){ break; }
	                swfOutputStream.write(k);
	            }
	            int x=-1;
	            pdfOutputStream.write(k);
	            while((x=inputStream.read())!=-1){
	                pdfOutputStream.write(x);
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        } finally {
	            try {
	                if (swfOutputStream != null) {
	                    swfOutputStream.flush();
	                    swfOutputStream.close();
	                }
	                if (pdfOutputStream != null) {
	                    pdfOutputStream.flush();
	                    pdfOutputStream.close();
	                }
	                if(inputStream!=null){
	                    inputStream.close();
	                }
	                httpClient.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }

	    }
	    
	    
	    
	    
	    public static void convertPdfToSwf(File file,String swfPath) {
	        FileOutputStream swfOutputStream = null;
	        FileOutputStream pdfOutputStream = null;
	        InputStream inputStream=null;
	        CloseableHttpClient httpClient = HttpClients.createDefault();
	        CloseableHttpResponse response;
	        HttpPost httppost = new HttpPost("http://"+ipAndPort+"/word2pdf/pdftoswf.do");
	                                         
	        MultipartEntity reqEntity =
	                new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE,
	                        System.currentTimeMillis() + "",
	                        Charset.forName("utf-8"));
	        String fileType = file.getName();
	        fileType = fileType.substring(fileType.lastIndexOf('.') + 1);
	        reqEntity.addPart("file", new FileBody(file,file.getName(),fileType,"UTF-8"));
	        httppost.setEntity(reqEntity);
	        try {
	            response = httpClient.execute(httppost);
	            inputStream = response.getEntity().getContent();
	            long  length =response.getEntity().getContentLength();
	            //separate是分隔符的位置
	            long separate=0;
	            Header[] headers=response.getAllHeaders();
	            for(Header header:headers){
	                if(header.getName().equals("separate")){
	                    separate=Long.parseLong(header.getValue());
	                }
	            }
	            File swfFile = new File(swfPath);
	           

	            System.out.println(swfFile.getAbsolutePath());
	            if (!swfFile.exists()) {
	                swfFile.createNewFile();
	            }
	            
	            swfOutputStream = new FileOutputStream(swfFile);
	          
	            int k=-1;
	            long i=0;//记录即将要读取的位置
	            while((k=inputStream.read())!=-1){
	                i+=1;
	                if(i==separate){ break; }
	                swfOutputStream.write(k);
	            }
	          
	        } catch (IOException e) {
	            e.printStackTrace();
	        } finally {
	            try {
	                if (swfOutputStream != null) {
	                    swfOutputStream.flush();
	                    swfOutputStream.close();
	                }
	               
	                if(inputStream!=null){
	                    inputStream.close();
	                }
	                httpClient.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }

	    }
	    
	    
	    
	    
	    public static void convertPPToSwf(File file,String swfPath) {
	        FileOutputStream swfOutputStream = null;
	        FileOutputStream pdfOutputStream = null;
	        InputStream inputStream=null;
	        CloseableHttpClient httpClient = HttpClients.createDefault();
	        CloseableHttpResponse response;
	        HttpPost httppost = new HttpPost("http://"+ipAndPort+"/word2pdf/ppttoswf.do");
	                                         
	        MultipartEntity reqEntity =
	                new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE,
	                        System.currentTimeMillis() + "",
	                        Charset.forName("utf-8"));
	        String fileType = file.getName();
	        fileType = fileType.substring(fileType.lastIndexOf('.') + 1);
	        reqEntity.addPart("file", new FileBody(file,file.getName(),fileType,"UTF-8"));
	        httppost.setEntity(reqEntity);
	        try {
	            response = httpClient.execute(httppost);
	            inputStream = response.getEntity().getContent();
	            long  length =response.getEntity().getContentLength();
	            //separate是分隔符的位置
	            long separate=0;
	            Header[] headers=response.getAllHeaders();
	            for(Header header:headers){
	                if(header.getName().equals("separate")){
	                    separate=Long.parseLong(header.getValue());
	                }
	            }
	            File swfFile = new File(swfPath);
	           

	            System.out.println(swfFile.getAbsolutePath());
	            if (!swfFile.exists()) {
	                swfFile.createNewFile();
	            }
	            
	            swfOutputStream = new FileOutputStream(swfFile);
	          
	            int k=-1;
	            long i=0;//记录即将要读取的位置
	            while((k=inputStream.read())!=-1){
	                i+=1;
	                if(i==separate){ break; }
	                swfOutputStream.write(k);
	            }
	          
	        } catch (IOException e) {
	            e.printStackTrace();
	        } finally {
	            try {
	                if (swfOutputStream != null) {
	                    swfOutputStream.flush();
	                    swfOutputStream.close();
	                }
	               
	                if(inputStream!=null){
	                    inputStream.close();
	                }
	                httpClient.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }

	    }
	    
	    
	    
	    
	    
	    public static void convertPPToPdf(File file,String pdfPath) {
	        FileOutputStream swfOutputStream = null;
	        FileOutputStream pdfOutputStream = null;
	        InputStream inputStream=null;
	        CloseableHttpClient httpClient = HttpClients.createDefault();
	        CloseableHttpResponse response;
	        HttpPost httppost = new HttpPost("http://"+ipAndPort+"/word2pdf/ppttopdf.do");
	                                         
	        MultipartEntity reqEntity =
	                new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE,
	                        System.currentTimeMillis() + "",
	                        Charset.forName("utf-8"));
	        String fileType = file.getName();
	        fileType = fileType.substring(fileType.lastIndexOf('.') + 1);
	        reqEntity.addPart("file", new FileBody(file,file.getName(),fileType,"UTF-8"));
	        httppost.setEntity(reqEntity);
	        try {
	            response = httpClient.execute(httppost);
	            inputStream = response.getEntity().getContent();
	            long  length =response.getEntity().getContentLength();
	            //separate是分隔符的位置
	            long separate=0;
	            Header[] headers=response.getAllHeaders();
	            for(Header header:headers){
	                if(header.getName().equals("separate")){
	                    separate=Long.parseLong(header.getValue());
	                }
	            }
	            File swfFile = new File(pdfPath);
	           

	            System.out.println(swfFile.getAbsolutePath());
	            if (!swfFile.exists()) {
	                swfFile.createNewFile();
	            }
	            
	            swfOutputStream = new FileOutputStream(swfFile);
	          
	            int k=-1;
	            long i=0;//记录即将要读取的位置
	            while((k=inputStream.read())!=-1){
	                i+=1;
	                if(i==separate){ break; }
	                swfOutputStream.write(k);
	            }
	          
	        } catch (IOException e) {
	            e.printStackTrace();
	        } finally {
	            try {
	                if (swfOutputStream != null) {
	                    swfOutputStream.flush();
	                    swfOutputStream.close();
	                }
	               
	                if(inputStream!=null){
	                    inputStream.close();
	                }
	                httpClient.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }

	    }
	    
	    
	    
	    public static void main(String[] args) {
	    	//convertPPToPdf(new File("D:\\test.ppt"),"d:\\ggg.pdf");
	    	//convertWord2Swf("D:\\11.doc","d:\\666444.swf");
	    	//convertPdfToSwf(new File("D:\\ggg.pdf"), "d:\\ggg.swf");
	    	
	    	word2PdfAndSwf(("D:\\1附件试卷.doc"),"d:\\gggaaa.swf","d:\\gggaaa.pdf");
		}
	    
}
