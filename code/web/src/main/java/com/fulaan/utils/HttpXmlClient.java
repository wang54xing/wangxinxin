package com.fulaan.utils;

import java.io.IOException;  
import java.io.UnsupportedEncodingException;  
import java.util.ArrayList;  
import java.util.HashMap;
import java.util.List;  
import java.util.Map;  
import java.util.Set;  
  







import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpEntity;  
import org.apache.http.HttpResponse;  
import org.apache.http.NameValuePair;  
import org.apache.http.ParseException;  
import org.apache.http.client.ClientProtocolException;  
import org.apache.http.client.entity.UrlEncodedFormEntity;  
import org.apache.http.client.methods.HttpGet;  
import org.apache.http.client.methods.HttpPost;  
import org.apache.http.client.methods.HttpUriRequest;  
import org.apache.http.impl.client.DefaultHttpClient;  
import org.apache.http.message.BasicNameValuePair;  
import org.apache.http.protocol.HTTP;  
import org.apache.http.util.EntityUtils;  
import org.apache.log4j.Logger;  
public class HttpXmlClient {

	 private static Logger log = Logger.getLogger(HttpXmlClient.class);  
     
	    public static List<HttpFulaanCookie> post(String url, Map<String, String> params) {  
	        DefaultHttpClient httpclient = new DefaultHttpClient();  
	        String body = null;  
	          
	        log.info("create httppost:" + url);  
	        HttpPost post = postForm(url, params);  
	          
	        List<HttpFulaanCookie> cks = invoke(httpclient, post);  
	          
	        httpclient.getConnectionManager().shutdown();  
	          
	        return cks;  
	    }  
	      
	   
	          
	      
	    private static List<HttpFulaanCookie> invoke(DefaultHttpClient httpclient,  
	            HttpUriRequest httpost) {  
	          
	        HttpResponse response = sendRequest(httpclient, httpost);  
	        
	        try {
				return printResponse(response);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
	       // String body = paseResponse(response);  
	          
	        return null;  
	    }  
	  
	    private static String paseResponse(HttpResponse response) {  
	        log.info("get response from http server..");  
	        HttpEntity entity = response.getEntity();  
	          
	        log.info("response status: " + response.getStatusLine());  
	        String charset = EntityUtils.getContentCharSet(entity);  
	        log.info(charset);  
	          
	        String body = null;  
	        try {  
	            body = EntityUtils.toString(entity);  
	            log.info(body);  
	        } catch (ParseException e) {  
	            e.printStackTrace();  
	        } catch (IOException e) {  
	            e.printStackTrace();  
	        }  
	          
	        return body;  
	    }  
	  
	    private static HttpResponse sendRequest(DefaultHttpClient httpclient,  
	            HttpUriRequest httpost) {  
	        log.info("execute post...");  
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
	  
	    private static HttpPost postForm(String url, Map<String, String> params){  
	          
	        HttpPost httpost = new HttpPost(url);  
	        List<NameValuePair> nvps = new ArrayList <NameValuePair>();  
	          
	        Set<String> keySet = params.keySet();  
	        for(String key : keySet) {  
	            nvps.add(new BasicNameValuePair(key, params.get(key)));  
	        }  
	          
	        try {  
	            log.info("set utf-8 form entity to httppost");  
	            httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));  
	        } catch (UnsupportedEncodingException e) {  
	            e.printStackTrace();  
	        }  
	          
	        return httpost;  
	    }  
	    
	    
	    
	    public static List<HttpFulaanCookie> printResponse(HttpResponse httpResponse)
	  	      throws ParseException, IOException {
	    	
	    	List<HttpFulaanCookie> cks =new ArrayList<HttpXmlClient.HttpFulaanCookie>();
	    	
	    	
//	  	    // 获取响应消息实体
//	  	    HttpEntity entity = httpResponse.getEntity();
//	  	    // 响应状态
//	  	    System.out.println("status:" + httpResponse.getStatusLine());
//	  	    System.out.println("headers:");
//	  	    HeaderIterator iterator = httpResponse.headerIterator();
//	  	    while (iterator.hasNext()) {
//	  	      System.out.println("\t" + iterator.next());
//	  	    }
//	  	    // 判断响应实体是否为空
//	  	    if (entity != null) {
//	  	      String responseString = EntityUtils.toString(entity);
//	  	      System.out.println("response length:" + responseString.length());
//	  	      System.out.println("response content:"
//	  	          + responseString.replace("\r\n", ""));
//	  	    }
//	    	
//	    	
	  	
	    	
	    	Header[] heads= httpResponse.getHeaders("Set-Cookie");
	    	
	    	for(Header h:heads)
	    	{
	    		HeaderElement[] hes= h.getElements();
	    		for(HeaderElement he:hes)
	    		{
	    			
	    			String name=he.getName();
	    			if(StringUtils.isNotBlank(name) && ("ASP.NET_SessionId".equalsIgnoreCase(name) || "auth".equalsIgnoreCase(name)))
	    			{
	    				
	    				String value=he.getValue();
	    				
	    				cks.add(new HttpFulaanCookie(name, value));
	    				
	    			}
	    			
	    			
	    		}
	    	}
	    	
	    	
	    	return cks;
	    	
	    	
	  	  }
	    
	    public static void main(String[] args) {
	    	Map<String, String> params = new HashMap<String, String>();  
			params.put("tbUserName", "乌市教育局");
			params.put("tbUserPwd", "wsjyj");
			params.put("__VIEWSTATE", "/wEPDwUKMTEzODU1MTU2Ng9kFgICAw9kFgICAQ9kFgQCCQ8PZBYCHgZvbmxvYWQFR3RvcC50b3BGcmFtZS5kb2N1bWVudC5nZXRFbGVtZW50QnlJZCgncGVyc2VuSW5mb3InKS5ocmVmPSdqYXZhc2NyaXB0OjsnZAILDw8WAh4HVmlzaWJsZWdkZBgBBR5fX0NvbnRyb2xzUmVxdWlyZVBvc3RCYWNrS2V5X18WAgUJaWJ0bkxvZ2luBQdpYnRuUmVn/lFKKkSMQPjc2dKEuUIWRRoyJ0Q=");
			params.put("__EVENTVALIDATION", "/wEWBQLS9Oa9DQLyj/OQAgKVqs78BwKBo5SvBQKcscC4BhU5e5zO2q516OaO+EX6bwTrjioz");
			params.put("ibtnLogin.x", "20");
			params.put("ibtnLogin.y", "12");
			List<HttpFulaanCookie>	cks = HttpXmlClient.post("http://xjwlmqtest1.jintizi.com/zyzx/user_win.aspx", params); 
			
			System.out.println(cks);
		}
	    
	    
	    
	    public static class HttpFulaanCookie
	    {
	    	private String name;
	    	private String value;
	    	
	    	
	    	
	    	
			public HttpFulaanCookie(String name, String value) {
				super();
				this.name = name;
				this.value = value;
			}
			public String getName() {
				return name;
			}
			public void setName(String name) {
				this.name = name;
			}
			public String getValue() {
				return value;
			}
			public void setValue(String value) {
				this.value = value;
			}
			@Override
			public String toString() {
				return "HttpFulaanCookie [name=" + name + ", value=" + value
						+ "]";
			}
	    	
	    	
	    }
	    
	    
	   
}
