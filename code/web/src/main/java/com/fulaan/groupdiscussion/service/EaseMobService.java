package com.fulaan.groupdiscussion.service;

import com.db.user.UserDao;
import com.easemob.server.example.httpclient.EasemobGroupMessage;
import com.easemob.server.example.httpclient.EasemobSendMessage;
import com.easemob.server.example.httpclient.EasemobUserAPI;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * Created by wang_xinxin on 2015/3/30.
 */
@Service
public class EaseMobService {

    private static final Logger logger = Logger.getLogger(EaseMobService.class);

    public String getAppKey() {
        return appKey;
    }

    private String appKey = com.sys.props.Resources.getProperty("EaseMob.appKey");

    private Calendar expireTime;
    private String easeMobDomain = "a1.easemob.com";
    private String authToken;
    private UserDao userdao = new UserDao();
    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    private boolean CheckLogin() {
        if (authToken == null || Calendar.getInstance().after(expireTime)) {
            try {
                Calendar now = Calendar.getInstance();
                HashMap result = EasemobSendMessage.Login(easeMobDomain, appKey, "admin", "fulankejiroot");
                authToken = (String)result.get("access_token");
                if (authToken != null) {
                    int duration = Integer.parseInt(result.get("expires_in").toString());
                    now.add(Calendar.SECOND, duration);
                    expireTime = now;
                    return true;
                } else
                    return false;
            } catch (Exception e) {
                logger.error(e.getMessage());
                return false;
            }
        }
        return true;
    }

    public void sendMessage(final String fromUserId, final List<String> recipients, final String message,final String nickname,final String maximageUrl) {

        taskExecutor.submit(new Callable() {

            @Override
            public Object call() {
                if (!CheckLogin())
                    return null;

                try {
                    Map<String, String> sendResult = EasemobSendMessage.sendTextMessage(appKey, authToken, message, fromUserId, recipients, nickname, maximageUrl);
            /*for (String toUsername : recipients) {

                String isSuccess = sendResult.get(toUsername);
                if (isSuccess.equals("success")) {
                    System.out.println("send message to " + toUsername
                            + " success!");
                } else {
                    System.out.println("send message to " + toUsername + " fail!");
                }
            }*/
                } catch (Exception e) {
                    logger.error(e.getMessage());
                }
                return null;
            }
        });
    }

    public void createNewUser(final String userId) {
        Future result = taskExecutor.submit(new Callable() {

            @Override
            public Object call() {
                if (!CheckLogin())
                    return null;

                Map<String, Object> createNewUserPostBody = new HashMap<String, Object>();
                createNewUserPostBody.put("username", userId);
                createNewUserPostBody.put("password", "fulankeji");
                String result = EasemobUserAPI.createNewUser(easeMobDomain, appKey, createNewUserPostBody, authToken);

                HashMap resultMap = null;
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    resultMap = objectMapper.readValue(result, HashMap.class);
                    if ("fulan".equals(resultMap.get("organization")) || "duplicate_unique_property_exists".equals(resultMap.get("error"))) {
                        // todo: 改进
//                        IUserMapper userMapper = (IUserMapper) SpringBeanUtil.getBean(IUserMapper.class);
//                        userMapper.setUserChatAccountInited(userId);
                        userdao.updateChat(userId);
//                        userdao.updateChat2(userId);
                        System.out.print("1");
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage());
                }
                return resultMap;
            }
        });
        //Object returnValue = result.get();
    }

    public void createAllUser(final List<String> userlist) {
        Future result = taskExecutor.submit(new Callable() {

            @Override
            public Object call() {
                if (!CheckLogin())
                    return null;

                Map<String, Object> createNewUserPostBody = new HashMap<String, Object>();
                List uesr = new ArrayList();
                for (String userid : userlist) {
                    createNewUserPostBody.put("username", userid);
                    createNewUserPostBody.put("password", "fulankeji");
                    uesr.add(createNewUserPostBody);
                }

                String result = EasemobUserAPI.createAllUser(easeMobDomain, appKey, uesr, authToken);

                HashMap resultMap = null;
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    resultMap = objectMapper.readValue(result, HashMap.class);
                    if ("fulan".equals(resultMap.get("organization")) || "duplicate_unique_property_exists".equals(resultMap.get("error"))) {
                        // todo: 改进
//                        IUserMapper userMapper = (IUserMapper) SpringBeanUtil.getBean(IUserMapper.class);
//                        userMapper.setUserChatAccountInited(userId);
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage());
                }
                return resultMap;
            }
        });
        //Object returnValue = result.get();
    }

    public Future createChatGroup(final String groupname,final String desc,final String owner,final List<String> members) {
        Future result = taskExecutor.submit(new Callable(){

            @Override
            public Object call() throws Exception {
                if (!CheckLogin())
                    return null;

                return EasemobGroupMessage.creatChatGroups(appKey, authToken, groupname, desc, true, owner, members);

            }

        });
        return result;
    }

    public void deleteChatGroups(final String groupid) {
        taskExecutor.submit(new Callable(){

            @Override
            public Object call() throws Exception {
                if (!CheckLogin())
                    return null;

                EasemobGroupMessage.deleteChatGroups(appKey, authToken, groupid);
                return null;

            }

        });
    }

    public void addUserToGroup(final String groupid,final String username) {
        taskExecutor.submit(new Callable(){

            @Override
            public Object call() throws Exception {
                if (!CheckLogin())
                    return null;

                EasemobGroupMessage.addUserToGroup(appKey, authToken, groupid, username);
                return null;

            }

        });
    }

    public void deleteUserFromGroup(final String groupid,final String username) {
        taskExecutor.submit(new Callable(){

            @Override
            public Object call() throws Exception {
                if (!CheckLogin())
                    return null;

                EasemobGroupMessage.deleteUserFromGroup(appKey, authToken, groupid, username);
                return null;

            }

        });
    }

    public void updateChatGroups(final String groupid,final String groupname) {
        taskExecutor.submit(new Callable(){

            @Override
            public Object call() throws Exception {
                if (!CheckLogin())
                    return null;

                EasemobGroupMessage.updateChatGroup(appKey, authToken, groupname, groupid);
                return null;

            }

        });
    }

    public void getChatGroups() {
        taskExecutor.submit(new Callable(){

            @Override
            public Object call() throws Exception {
                if (!CheckLogin())
                    return null;

                EasemobGroupMessage.getGroupsByAppKey(appKey, authToken);
                return null;

            }

        });
    }
}
