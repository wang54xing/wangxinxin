package com.sql.oldDataTransfer;

import com.db.groups.GroupsDao;
import com.pojo.groups.GroupsChatEntry;
import com.pojo.groups.GroupsEntry;
import com.pojo.groups.GroupsFileEntry;
import com.pojo.groups.GroupsUser;
import com.pojo.utils.DeleteState;
import com.sql.dao.RefactorMapper;
import com.sql.oldDataPojo.GroupChatInfo;
import com.sql.oldDataPojo.GroupFileInfo;
import com.sql.oldDataPojo.GroupInfo;
import com.sql.oldDataPojo.RefGroupUser;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by qinbo on 15/4/14.
 */
public class TransferGroup {

    private List<RefGroupUser> groupUserList = null;
    private List<GroupFileInfo> groupFileInfoList = null;
    private List<GroupChatInfo> groupChatInfoList = null;

    public static Map<String,ObjectId> groupMap = new HashMap<String, ObjectId>();
    private GroupsDao groupsDao = new GroupsDao();
    private SqlSessionFactory getSessionFactory() {
        SqlSessionFactory sessionFactory = null;
        String resource = "configuration.xml";
        try {
            sessionFactory = new SqlSessionFactoryBuilder().build(Resources.getResourceAsReader(resource));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sessionFactory;
    }


    public void transfer() throws Exception {
        SqlSession sqlSession = getSessionFactory().openSession();
        RefactorMapper refactorMapper = sqlSession.getMapper(RefactorMapper.class);


        List<GroupInfo> groupInfoList = refactorMapper.getGroupInfo();

        groupUserList = refactorMapper.getGroupUserInfo();
        groupFileInfoList = refactorMapper.getGroupFileInfo();
        groupChatInfoList = refactorMapper.getGroupChatInfo();

        for(GroupInfo groupInfo:groupInfoList){
            GroupsEntry groupsEntry = new GroupsEntry(
                    groupInfo.getRoomid(),
                    String.valueOf(groupInfo.getMaingroup()),
                    groupInfo.getGroupname(),
                    groupInfo.getCreateDate().getTime(),
                    DeleteState.NORMAL.getState(),
                    getGroupUserList(groupInfo.getRoomid())

            );



            groupsEntry.setID(new ObjectId(groupInfo.getCreateDate()));
            groupMap.put(groupInfo.getRoomid(),groupsEntry.getID());

            groupsDao.addGroups(groupsEntry);

        }


        sqlSession.close();


        transferGroupFile();
        transferGroupChat();
    }


    //todo file location
    private void transferGroupFile(){

        for(GroupFileInfo groupFileInfo:groupFileInfoList){
            GroupsFileEntry groupsFileEntry = new GroupsFileEntry(
                    groupFileInfo.getRoomid(),
                    groupFileInfo.getFilePath(),
                    String.valueOf(groupFileInfo.getUploadUserid()),
                    groupFileInfo.getFileName(),
                    groupFileInfo.getUploadDate().getTime(),
                    groupFileInfo.getCount(),
                    groupFileInfo.getFilesize(),
                    groupFileInfo.getDelflg()
            );
            groupsFileEntry.setID(new ObjectId(groupFileInfo.getUploadDate()));
            groupsDao.insertGroupFile(groupsFileEntry);
        }

    }

    private void transferGroupChat(){

        for(GroupChatInfo groupChatInfo:groupChatInfoList){
            GroupsChatEntry groupsChatEntry = new GroupsChatEntry(
                    groupChatInfo.getRoomid(),
                    String.valueOf(groupChatInfo.getGroupUserid()),
                    groupChatInfo.getChatContent(),
                    groupChatInfo.getSendDate().getTime()
            );
            groupsChatEntry.setID(new ObjectId(groupChatInfo.getSendDate()));
            groupsDao.addGroupsChat(groupsChatEntry);
        }
    }


    private List<GroupsUser> getGroupUserList(String roomid){
        List<GroupsUser> userList = null;
        for(RefGroupUser refGroupUser:groupUserList){
            if(roomid.equals(refGroupUser.getRoomid())){

                if(TransferUser.userMap.get(refGroupUser.getUserid())!=null){
                    if(userList == null){
                        userList = new ArrayList<GroupsUser>();
                    }
                    userList.add(new GroupsUser(
                            String.valueOf(refGroupUser.getUserid()),
                            refGroupUser.getIsread(),
                            refGroupUser.getUpdatetime().getTime()
                    ));
                }
            }
        }
        return  userList;
    }

}
