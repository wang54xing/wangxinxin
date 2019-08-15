package com.sys.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.client.methods.HttpPost;  
import org.apache.http.NameValuePair; 
import org.apache.http.message.BasicNameValuePair; 
import org.apache.http.client.entity.UrlEncodedFormEntity;  
import org.apache.http.client.methods.HttpUriRequest; 
import org.apache.http.util.EntityUtils;  

/**
 * 处理rest风格请求
 * @author fourer
 *
 */
@SuppressWarnings("all")
public class HttpClientUtils {



	public static String get(String url) throws ClientProtocolException, IOException
	{
		
		HttpClient http = new DefaultHttpClient(); 
		 HttpGet get = new HttpGet(url);  
		 HttpResponse response = http.execute(get);  
		 if (response.getStatusLine().getStatusCode() == 200) {  
		    HttpEntity entity = response.getEntity();  
		    String res=   convertStreamToString(entity.getContent());
		    return res;
		} 
		return null;
	}
	
	
	public static String get(String url,String token) throws ClientProtocolException, IOException
	{
		 HttpClient http = new DefaultHttpClient(); 
		 HttpGet get = new HttpGet(url);  
		 get.addHeader("Accept", "application/json, text/plain, */*");
		 get.addHeader("Accept-Language", "zh-CN,zh;");
		 
		 get.addHeader("Authorization", "Bearer "+token+"");
		 HttpResponse response = http.execute(get);  
		 if (response.getStatusLine().getStatusCode() == 200) {  
		    HttpEntity entity = response.getEntity();  
		   // String res=   convertStreamToString(entity.getContent());
		    String res=  EntityUtils.toString(entity, "utf-8");
		    return res;
		} 
		return null;
	}
	
	
	public static String convertStreamToString(java.io.InputStream inputStream) {      
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));      
        StringBuilder sb = new StringBuilder();      
       
        String line = null;      
        try {      
            while ((line = reader.readLine()) != null) {  
                sb.append(line);      
            }      
        } catch (IOException e) {      
            e.printStackTrace();      
        } finally {      
            try {      
                inputStream.close();      
            } catch (IOException e) {      
               e.printStackTrace();      
            }      
        }      
        return sb.toString();      
    }  
	
	
	
	public static String post(String url, Map<String, String> params) {  
        DefaultHttpClient httpclient = new DefaultHttpClient();  
        String body = null;  
          
      
        HttpPost post = postForm(url, params);  
          
        body = invoke(httpclient, post);  
          
        httpclient.getConnectionManager().shutdown();  
          
        return body;  
    }  
	
	
	
	private static HttpPost postForm(String url, Map<String, String> params){  
        
        HttpPost httpost = new HttpPost(url);  
        List<NameValuePair> nvps = new ArrayList <NameValuePair>();  
          
        Set<String> keySet = params.keySet();  
        for(String key : keySet) {  
            nvps.add(new BasicNameValuePair(key, params.get(key)));  
        }  
          
        try {  
            httpost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
          
        return httpost;  
    }  
	
	
	private static String invoke(DefaultHttpClient httpclient,  
            HttpUriRequest httpost) {  
          
        HttpResponse response = sendRequest(httpclient, httpost);  
        String body = paseResponse(response);  
          
        return body;  
    }
	
	
	
	private static HttpResponse sendRequest(DefaultHttpClient httpclient,  
            HttpUriRequest httpost) {  
        HttpResponse response = null;  
          
        try {  
            response = httpclient.execute(httpost);  
        } catch (ClientProtocolException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        return response;  
    }  
	
	
	private static String paseResponse(HttpResponse response) {  
        HttpEntity entity = response.getEntity();  
          
        String charset = EntityUtils.getContentCharSet(entity);  
        String body = null;  
        try {  
            body = EntityUtils.toString(entity);  
         
        } catch (Exception e) {  
            e.printStackTrace();  
        } 
          
        return body;  
    }  
	
	public static void main(String[] args) {
	try {
			
			for(int i=0;i<20;i++)
			{
			String id=HttpClientUtils.get("http://midong.k6kt.com:80/user/check.do?key=56679ab30cf217c1b9213375");
			System.out.println(i+":"+id);
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
