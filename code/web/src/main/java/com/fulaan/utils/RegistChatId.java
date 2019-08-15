package com.fulaan.utils;

import com.db.user.UserDao;
import com.fulaan.groupdiscussion.service.EaseMobService;
import com.mongodb.BasicDBObject;
import com.pojo.user.UserEntry;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wang_xinxin on 2016/10/9.
 */
public class RegistChatId {

    public static void main(String[] args) {
        EaseMobService easeMobService = new EaseMobService();
        UserDao userDao = new UserDao();
        List<String> userlist = new ArrayList<String>();
        List<UserEntry> userEntryList = userDao.getUserEntryHuanXin(new ObjectId("57f9998bb0fbeb47e8958d07"), new BasicDBObject("chatid",1));
        if (userEntryList!=null && userEntryList.size()!=0) {
            for (UserEntry userEntry : userEntryList) {
                userlist.add(userEntry.getChatId());
            }
            for (int i=0;i<userlist.size();i++) {
                if (!StringUtils.isEmpty(userlist.get(i))) {
                    easeMobService.createNewUser(userlist.get(i));
                }
            }
        }

    }
}
