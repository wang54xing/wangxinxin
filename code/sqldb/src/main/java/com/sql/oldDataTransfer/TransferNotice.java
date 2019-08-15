package com.sql.oldDataTransfer;

import com.db.notice.NoticeDao;
import com.db.school.ClassDao;
import com.db.school.InterestClassDao;
import com.db.user.UserDao;
import com.pojo.app.IdNameValuePair;
import com.pojo.app.IdValuePair;
import com.pojo.notice.NoticeEntry;
import com.pojo.school.ClassEntry;
import com.pojo.user.UserEntry;
import com.sql.dao.RefactorMapper;
import com.sql.oldDataPojo.MesgInfo;
import com.sql.oldDataPojo.MsgAttachInfo;
import com.sql.oldDataPojo.MsgVoiceInfo;
import com.sql.oldDataPojo.RefMesgclass;

import com.sys.constants.Constant;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by qinbo on 15/4/9.
 */
public class TransferNotice {


    private List<MesgInfo> noticeInfoList = null;
    private NoticeDao noticeDao = new NoticeDao();

    private List<RefMesgclass> refMesgclassList = null;
    private  List<MsgAttachInfo> attachInfoList = null;
    private  List<MsgVoiceInfo> voiceInfoList = null;
    private ClassDao classDao = new ClassDao();
    private UserDao userDao = new UserDao();
    private InterestClassDao interestClassDao = new InterestClassDao();
    private SqlSessionFactory getSessionFactory() {
        SqlSessionFactory sessionFactory = null;
        String resource = "configuration.xml";
        try {
            sessionFactory = new SqlSessionFactoryBuilder().build(Resources
                    .getResourceAsReader(resource));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sessionFactory;
    }

    public void transfer(){
        SqlSession sqlSession = getSessionFactory().openSession();
        RefactorMapper refactorMapper = sqlSession
                .getMapper(RefactorMapper.class);


        noticeInfoList = refactorMapper.getNoticeInfo();
        attachInfoList = refactorMapper.getHomeworkAttachInfo();
        voiceInfoList = refactorMapper.getHomeworkVoiceInfo();
        refMesgclassList = refactorMapper.getRefMesgclass();

        for(MesgInfo mesgInfo:noticeInfoList){

            List<IdValuePair> users = getClassesList(mesgInfo.getId());
            List<IdNameValuePair> voiceFile = getVoiceList(mesgInfo.getId());
            List<IdNameValuePair> attachFile = getAttachList(mesgInfo.getId());


            NoticeEntry noticeEntry = new NoticeEntry(
                    TransferUser.userMap.get(mesgInfo.getUserid()),
                    null,//all school
                    users,//users
                    mesgInfo.getMesgname(),
                    mesgInfo.getMesgcontent(),
                    voiceFile,//voice files
                    attachFile,//attach files
                    0L, //开始时间
                    0L,  //结束时间
                    0,    //是否需要同步到日历
                    0
            );
            if(mesgInfo.getCreatetime()!=null) {
                noticeEntry.setID(new ObjectId(mesgInfo.getCreatetime()));
            }
            noticeDao.addNoticeEntry(noticeEntry);
        }





        sqlSession.close();
    }

    //todo : 是否班级可以
    private List<IdValuePair> getClassesList(int msgId){
        List<IdValuePair> classes = null;
        for(RefMesgclass refMesgclass : refMesgclassList){
            if(msgId == refMesgclass.getMessageid())
            {

                ObjectId classId = TransferClass.classMap.get(refMesgclass.getClassid());
                if(classId == null){
                    classId = TransferClass.interestClassMap.get(refMesgclass.getClassid());

                }
                if(classId!=null) {
                    if(classes == null){
                        classes = new ArrayList<IdValuePair>();
                    }
                    classes.add(new IdValuePair(classId,"班级名称"));
                }


            }
        }
        return classes;
    }

    //todo :地址
    private List<IdNameValuePair> getVoiceList(int msgId){
        List<IdNameValuePair> voiceList = null;
        for(MsgVoiceInfo voiceInfo : voiceInfoList){
            if(msgId == voiceInfo.getMesgid())
            {


                if(voiceList == null){
                    voiceList = new ArrayList<IdNameValuePair>();
                }
                voiceList.add(new IdNameValuePair(new ObjectId(), voiceInfo
                        .getVoicename(), voiceInfo.getVoicename()));

            }
        }
        return voiceList;
    }

    //todo :地址
    private List<IdNameValuePair> getAttachList(int msgId){
        List<IdNameValuePair> attachList = null;
        for(MsgAttachInfo attachInfo : attachInfoList){
            if(msgId == attachInfo.getMessageid())
            {


                if(attachList == null){
                    attachList = new ArrayList<IdNameValuePair>();
                }
                attachList.add(new IdNameValuePair(new ObjectId(), attachInfo.getName()
                        , attachInfo.getPathname()));

            }
        }
        return attachList;
    }


    private void fixNoticeUser(){

        List<NoticeEntry> noticeEntryList = noticeDao.getAllNoticeEntry();

        for(NoticeEntry noticeEntry:noticeEntryList){
            List<IdValuePair> userList = noticeEntry.getUsers();
            List<IdValuePair> newUserList = new ArrayList<IdValuePair>();
            //把班级替换成学生
            List<ObjectId> classIdList = new ArrayList<ObjectId>();
            for(IdValuePair classIdp:userList){
                classIdList.add(classIdp.getId());
            }
            Map<ObjectId,ClassEntry> classEntryMap = classDao.getClassEntryMap(classIdList, Constant.FIELDS);
            for(ClassEntry classEntry:classEntryMap.values()){
                //
                List<ObjectId> studentIdList =  classEntry.getStudents();

                Map<ObjectId,UserEntry> userEntryMap =  userDao.getUserEntryMap(studentIdList,Constant.FIELDS);

                for(UserEntry userEntry:userEntryMap.values()){
                    newUserList.add(new IdValuePair(userEntry.getID(),userEntry.getUserName()));
                }

            }
            noticeEntry.setUsers(newUserList);
            noticeDao.deleteNotice(noticeEntry.getID());
            noticeDao.addNoticeEntry(noticeEntry);




        }
    }
    public static void main(String args[]){
        TransferNotice transferNotice = new TransferNotice();
        transferNotice.fixNoticeUser();
    }

}
