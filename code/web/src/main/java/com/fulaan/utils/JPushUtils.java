package com.fulaan.utils;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


import com.fulaan.user.service.UserService;
import com.pojo.user.UserEntry;
import org.bson.types.ObjectId;

import cn.jpush.api.JPushClient;
import cn.jpush.api.common.APIConnectionException;
import cn.jpush.api.common.APIRequestException;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import cn.jpush.api.push.model.notification.WinphoneNotification;



import com.mongodb.BasicDBObject;
import com.pojo.app.IdValuePair;
import com.pojo.school.ClassEntry;
import com.pojo.school.InterestClassEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import com.sys.props.Resources;


/**
 * 
 * @author fourer
 * mType 1:作业 2：通知
 */
public class JPushUtils {
	   
	    private Boolean apnsProduction=Boolean.valueOf(Resources.getProperty("JPush.apnsProduction"));
	    
	    
	    
	    private String masterSecret_android=Resources.getProperty("JPush.masterSecret.android");
	    private String appKey_android=Resources.getProperty("JPush.appKey.android");
	    
	    private String masterSecret_ios=Resources.getProperty("JPush.masterSecret.ios");
	    private String appKey_ios=Resources.getProperty("JPush.appKey.ios");
	    
	    
	    
	    


	    private PushPayload buildPushObject_all_tag_alert_Android(Audience audience,
	                                                                     String mesgname, String userName,String title,Map<String,String> parms) {
	        return PushPayload.newBuilder()
	                .setAudience(audience)
	                .setPlatform(Platform.android())
	                .setMessage(Message.newBuilder()
	                        .addExtra("title", title)
	                        .addExtra("value", mesgname)
	                        .addExtra("username", userName)
	                        .addExtras(parms)
	                        .setMsgContent(mesgname).build()).build();
	    }
	    

	    private PushPayload buildPushObject_all_tag_alert_IOS(Audience audience, String mesgname,Map<String,String> parms) {
	    	
	    	
	    	
	    	IosNotification ifc=	IosNotification.newBuilder()
            .setAlert(mesgname)
            .setSound("default")
            .addExtras(parms)
            .build();
	    	
	        return PushPayload.newBuilder().setPlatform(Platform.ios()).setAudience(audience)
	                .setNotification(
	                        Notification.newBuilder()
	                                .addPlatformNotification(
	                                		ifc
	                                ).build()
	                ).setOptions(Options.newBuilder().setApnsProduction(apnsProduction).build()).build();
	    }

	    private PushPayload buildPushObject_all_tag_alert_WindowsPhone(Audience audience, String mesgname) {
	        return PushPayload.newBuilder().setPlatform(Platform.winphone()).setAudience(audience)
	                .setNotification(
	                        Notification.newBuilder()
	                                .addPlatformNotification(
	                                        WinphoneNotification.newBuilder()
	                                                .setAlert(mesgname)
	                                                .setTitle("K6KT")
	                                                .build()
	                                ).build()
	                ).build();
	    }
	    
	    
	    

	    /**
	     * 极光推送
	     *
	     * @param mesgname
	     */
	    public void pushRestIos(Audience audience, String mesgname,Map<String,String> parms) {
	        JPushClient jpushClient = new JPushClient(masterSecret_ios, appKey_ios, 3);
	        PushPayload payload = buildPushObject_all_tag_alert_IOS(audience, mesgname,parms);
	        try {
	            PushResult result = jpushClient.sendPush(payload);
	            System.out.println(result);
	        } catch (APIConnectionException e) {
	            e.printStackTrace();
	        } catch (APIRequestException e) {
	            e.printStackTrace();
	        }
	    }

	    /**
	     * 极光推送
	     *
	     * @param mesgname
	     */
	    public void pushRestWinPhone(Audience audience, String mesgname) {
	        JPushClient jpushClient = new JPushClient(masterSecret_android, appKey_android,3 );
	        PushPayload payload = buildPushObject_all_tag_alert_WindowsPhone(audience, mesgname);
	        try {
	            PushResult result = jpushClient.sendPush(payload);
	            System.out.println(result);
	        } catch (APIConnectionException e) {
	            e.printStackTrace();
	        } catch (APIRequestException e) {
	            e.printStackTrace();
	        }
	    }

	    /**
	     * 极光推送
	     *
	     * @param mesgname
	     */
	    public void pushRestAndroid(Audience audience, String mesgname, String username,String title ,Map<String,String> parms) {
	        JPushClient jpushClient = new JPushClient(masterSecret_android, appKey_android, 3);
	        PushPayload payload = buildPushObject_all_tag_alert_Android(audience, mesgname, username,title,parms);
	        try {
	            PushResult result = jpushClient.sendPush(payload);
	            System.out.println(result);
	        } catch (APIConnectionException e) {
	            e.printStackTrace();
	        } catch (APIRequestException e) {
	            e.printStackTrace();
	        }
	    }
}
