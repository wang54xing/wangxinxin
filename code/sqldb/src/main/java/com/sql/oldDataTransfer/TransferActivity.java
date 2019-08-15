package com.sql.oldDataTransfer;

import com.db.activity.ActivityDao;
import com.db.activity.ActivityTrackDao;
import com.db.activity.FriendApplyDao;
import com.db.activity.FriendDao;
import com.db.school.SchoolDao;
import com.db.user.UserDao;
import com.mongodb.BasicDBObject;
import com.pojo.activity.*;
import com.pojo.activity.enums.ActTrackDevice;
import com.pojo.activity.enums.ActTrackType;
import com.pojo.school.SchoolEntry;
import com.pojo.user.UserEntry;
import com.sql.dao.RefactorMapper;
import com.sql.oldDataPojo.*;
import com.sys.constants.Constant;

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
 * Created by qinbo on 15/4/9.
 */
public class TransferActivity {

    public static Map<Integer,ActivityEntry> activityEntryMap =
            new HashMap<Integer, ActivityEntry>();

    private List<ActivityDiscussInfo> activityDiscussInfoList = null;
    private FriendDao friendDao = new FriendDao();

    private List<ActivityAttendInfo> activityAttendInfoList = null;
    private ActivityDao activityDao = new ActivityDao();

    private ActivityTrackDao activityTrackDao = new ActivityTrackDao();
    private UserDao userDao = new UserDao();
    private SchoolDao schoolDao = new SchoolDao();
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

    public void transfer(){
        SqlSession sqlSession = getSessionFactory().openSession();

        RefactorMapper refactorMapper = sqlSession.getMapper(RefactorMapper.class);


        List<ActivityInfo> activityInfoList = refactorMapper.getActivityInfo();

        activityDiscussInfoList =
                refactorMapper.getActivityDiscussInfo();

        activityAttendInfoList =
                refactorMapper.getActivityAttendInfo();

        for(ActivityInfo activityInfo:activityInfoList){


            ActivityEntry activityEntry = new ActivityEntry();
            activityEntry.setActName(activityInfo.getName());
            if(TransferUser.userMap.get(activityInfo.getOrganizer())!=null) {
                activityEntry.setOrganizerId(
                        TransferUser.userMap.get(activityInfo.getOrganizer())
                );
            }else
            {
                activityEntry.setOrganizerId(TransferUser.unkownUser.getID());
            }

            activityEntry.setCreateDate(activityInfo.getCreateDate().getTime());
            activityEntry.setActStartDate(activityInfo.getEventStartDate().getTime());
            activityEntry.setActEndDate(activityInfo.getEventEndDate().getTime());
            activityEntry.setLocation(activityInfo.getLocation());
            activityEntry.setCoverImage(activityInfo.getCoverImage());
            activityEntry.setActStatus(activityInfo.getStatus());
            int actVisible = activityInfo.getVisible()==null?2:activityInfo.getVisible();
            activityEntry.setActVisibility(actVisible);
            activityEntry.setMemberCount(activityInfo.getMemberCount());
            activityEntry.setDescription(activityInfo.getDescription());
            activityEntry.setID(new ObjectId(activityInfo.getCreateDate()));


            //学校id
                UserEntry userEntry =
                        userDao.getUserEntry(activityEntry.getOrganizerId(), new BasicDBObject("si", 1));


                if (userEntry != null) {
                    activityEntry.setSchoolId(userEntry.getSchoolID());


                    SchoolEntry schoolEntry =
                            schoolDao.getSchoolEntry(userEntry.getSchoolID(),Constant.FIELDS);

                    if(schoolEntry!=null) {
                        activityEntry.setRegionId(schoolEntry.getRegionId());
                    }

                }

            activityEntryMap.put(activityInfo.getId(), activityEntry);

            //图片数量
            //出席活动人数id
            //地区id
            //出席活动人数
            //学校id
        }

        //讨论内嵌
        //讨论数量
        transferDiscuss();

        //图片数量

        transferAttend();

        //图片
        List<ActivityImageCountInfo> activityImageCountInfoList=
                refactorMapper.getActivityImageCountInfo();
        for(ActivityImageCountInfo activityImageCountInfo:activityImageCountInfoList){
            activityEntryMap.get(activityImageCountInfo.getActId()).
                    setImageCount(activityImageCountInfo.getImc());
        }

        //地区id

        sqlSession.close();

        for(ActivityEntry activityEntry:activityEntryMap.values()){
            activityDao.insertActivity(activityEntry);
        }


        transferActTrack();
        transferFriend();

    }

    private void transferFriend(){
        SqlSession sqlSession = getSessionFactory().openSession();

        RefactorMapper refactorMapper = sqlSession.getMapper(RefactorMapper.class);

        List<FriendInfo> friendInfoList = refactorMapper.getFriendInfo();
        Map<Integer,List<Integer>> friendrels = new HashMap<Integer, List<Integer>>();
        for(FriendInfo friendInfo:friendInfoList){
            if(friendrels.get(friendInfo.getUserId1())==null){
                friendrels.put(friendInfo.getUserId1(),new ArrayList<Integer>());
            }
            friendrels.get(friendInfo.getUserId1()).add(friendInfo.getUserId2());

            if(friendrels.get(friendInfo.getUserId2())==null){
                friendrels.put(friendInfo.getUserId2(),new ArrayList<Integer>());
            }
            friendrels.get(friendInfo.getUserId2()).add(friendInfo.getUserId1());
        }

        List<FriendApplyInfo> friendApplyInfoList = refactorMapper.getFriendApplyInfo();

        for(Integer user1:friendrels.keySet()){
            FriendEntry friendEntry = new FriendEntry();
            friendEntry.setUserId(TransferUser.userMap.get(user1));
            List<ObjectId> friendIdList = new ArrayList<ObjectId>();
            for(Integer user2:friendrels.get(user1)){
                friendIdList.add(TransferUser.userMap.get(user2));
            }
            friendEntry.setFriendIds(friendIdList);

            //friendDao.(friendEntry);

            friendDao.addFriendEntry(friendEntry.getUserId(),friendIdList);

        }

        FriendApplyDao friendApplyDao = new FriendApplyDao();

        for(FriendApplyInfo friendApplyInfo:friendApplyInfoList){
            FriendApplyEntry friendApplyEntry = new FriendApplyEntry();
            friendApplyEntry.setUserId(TransferUser.userMap.
                    get(friendApplyInfo.getUserId()));
            friendApplyEntry.setRespond(TransferUser.userMap.
                    get(friendApplyInfo.getRespondent()));
            friendApplyEntry.setApplyDate(friendApplyInfo.getApplyDate().getTime());
            if(friendApplyInfo.getRespondDate()!=null){
                friendApplyEntry.setRespondDate(friendApplyInfo.getRespondDate().getTime());
            }

            friendApplyEntry.setAccepted(friendApplyInfo.getAccepted());
            friendApplyEntry.setContent(friendApplyInfo.getContent());

            if(friendApplyEntry.getUserId()!=null && friendApplyEntry.getRespondent()!=null) {
                friendApplyDao.insertApply(friendApplyEntry);
            }
        }




        sqlSession.close();

    }

    private void transferActTrack(){
        SqlSession sqlSession = getSessionFactory().openSession();

        RefactorMapper refactorMapper = sqlSession.getMapper(RefactorMapper.class);


        List<ActivityTrackInfo> activityTrackInfoList = refactorMapper.getActivityTrackInfo();

        for(ActivityTrackInfo activityTrackInfo: activityTrackInfoList){
            ActTrackEntry actTrackEntry = new ActTrackEntry();
            actTrackEntry.setCreateTime(activityTrackInfo.getCreateTime().getTime());
            switch (activityTrackInfo.getType()){
                case 0:
                    actTrackEntry.setActTrackType(ActTrackType.PROMOTE);
                    if(activityEntryMap.get(activityTrackInfo.getRelateId())!=null) {
                        actTrackEntry.setRelatedId(activityEntryMap.
                                get(activityTrackInfo.getRelateId()).getID());
                    }
                    break;
                case 1:
                    actTrackEntry.setActTrackType(ActTrackType.FRIEND);
                    TransferUser.userMap.get(activityTrackInfo.getRelateId());
                    break;
                case 2:
                    actTrackEntry.setActTrackType(ActTrackType.ATTEND);
                    if(activityEntryMap.get(activityTrackInfo.getRelateId())!=null) {
                        actTrackEntry.setRelatedId(activityEntryMap.
                                get(activityTrackInfo.getRelateId()).getID());
                    }
                    break;
                case 3:
                    actTrackEntry.setActTrackType(ActTrackType.REPLY);
                    if(activityEntryMap.get(activityTrackInfo.getRelateId())!=null) {
                        actTrackEntry.setRelatedId(activityEntryMap.
                                get(activityTrackInfo.getRelateId()).getID());
                    }
                    break;
            }
            if(TransferUser.userMap.get(activityTrackInfo.getUserId())!=null){
                actTrackEntry.setUserId(TransferUser.userMap.get(activityTrackInfo.getUserId()));

            }
            else
            {
                actTrackEntry.setUserId(TransferUser.unkownUser.getID());
            }

            switch (activityTrackInfo.getFromDevice()){
                case 0:
                    actTrackEntry.setActTrackDevice(ActTrackDevice.FromPC);
                    break;
                case 1:
                    actTrackEntry.setActTrackDevice(ActTrackDevice.FromAndroid);
                    break;
                case 2:
                    actTrackEntry.setActTrackDevice(ActTrackDevice.FromIOS);
                    break;

            }
            if(actTrackEntry.getRelatedId()!=null) {
                activityTrackDao.insertActTrack(actTrackEntry);
            }
        }



        sqlSession.close();
    }

    private void transferAttend(){

        int curActId = -1;
        List<ObjectId> userIds= null;
        for(ActivityAttendInfo activityAttendInfo:activityAttendInfoList){
            if(curActId!=activityAttendInfo.getActivityId()){
                if(curActId>0){
                    if(activityEntryMap.containsKey(curActId)) {
                        activityEntryMap.get(curActId).setAttendIds(userIds);
                        activityEntryMap.get(curActId).setAttendCount(userIds.size());
                    }
                }
                userIds = new ArrayList<ObjectId>();
                curActId = activityAttendInfo.getActivityId();
            }
            if(TransferUser.userMap.get(activityAttendInfo.getUserId())!=null) {
                userIds.add(TransferUser.userMap.get(activityAttendInfo.getUserId()));
            }
        }
        if(curActId>0){
            if(activityEntryMap.containsKey(curActId)) {
                activityEntryMap.get(curActId).setAttendIds(userIds);
                activityEntryMap.get(curActId).setAttendCount(userIds.size());
            }
        }

    }


    private void transferDiscuss(){
        int curActId = -1;
        List<ActivityDiscuss> activityDiscussList = null;
        Map<Integer,ObjectId> discussMap = new HashMap<Integer, ObjectId>();
        Map<ObjectId,ActivityDiscussInfo> discussInfoMap = new HashMap<ObjectId, ActivityDiscussInfo>();
        Map<ObjectId,List<ActivityDiscuss>> activityDiscussMap = new HashMap<ObjectId, List<ActivityDiscuss>>();


        for(ActivityDiscussInfo activityDiscussInfo:activityDiscussInfoList){
            if(activityDiscussInfo.getActId()!=curActId){
                if(curActId>0){
                    if(activityEntryMap.containsKey(curActId)){

                        //set rid
                        activityDiscussMap.put(activityEntryMap.get(curActId).getID(),activityDiscussList);

                        //activityEntryMap.get(curActId).setActDiscusses(activityDiscussList);
                        activityEntryMap.get(curActId).setDiscussCount(activityDiscussList.size());

                    }

                }
                activityDiscussList = new ArrayList<ActivityDiscuss>();
                curActId = activityDiscussInfo.getActId();
            }
            ActivityDiscuss activityDiscuss = new ActivityDiscuss();
            activityDiscuss.setId(new ObjectId(activityDiscussInfo.getT()));
            if(TransferUser.userMap.get(activityDiscussInfo.getUserId())!=null) {
                activityDiscuss.setUserId(TransferUser.userMap.get(activityDiscussInfo.getUserId()));
            }
            else
            {
                activityDiscuss.setUserId(TransferUser.unkownUser.getID());
            }
            activityDiscuss.setContent(activityDiscussInfo.getContent());
            activityDiscuss.setDate(activityDiscussInfo.getT().getTime());
            if(activityDiscussInfo.getImage()!=null){
                //todo : 图片
                List<String> imgList = new ArrayList<String>();
                imgList.add(activityDiscussInfo.getImage());
                activityDiscuss.setImageList(imgList);
            }
            activityDiscussList.add(activityDiscuss);
            discussMap.put(activityDiscussInfo.getId(),activityDiscuss.getId());
            discussInfoMap.put(activityDiscuss.getId(),activityDiscussInfo);

        }

        if(activityEntryMap.containsKey(curActId)){

            //set rid
            //activityEntryMap.get(curActId).setActDiscusses(activityDiscussList);
            activityEntryMap.get(curActId).setDiscussCount(activityDiscussList.size());
        }

        for(Integer infoId:activityEntryMap.keySet()){//
            ActivityEntry activityEntry = activityEntryMap.get(infoId);
            List<ActivityDiscuss> discussList = activityDiscussMap.get(activityEntry.getID());

            if(discussList!=null) {
                for (ActivityDiscuss discuss : discussList) {
                    ActivityDiscussInfo activityDiscussInfo = discussInfoMap.get(discuss.getId());

                    if(activityDiscussInfo!=null) {
                        discuss.setRepId(discussMap.get(activityDiscussInfo.getRepId()));
                    }
                }
                activityEntry.setActDiscusses(discussList);
            }
        }


    }

    public static void main(String[] args){
        TransferActivity transferActivity = new TransferActivity();
        transferActivity.transfer();
    }
}
