package com.sql.oldDataTransfer;

import com.db.school.HomeWorkDao;
import com.pojo.app.IdNameValuePair;
import com.pojo.app.IdValuePair;
import com.pojo.school.HomeWorkEntry;
import com.pojo.school.StudentSubmitHomeWork;
import com.sql.dao.RefactorMapper;
import com.sql.oldDataPojo.*;

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
 * Created by qinbo on 15/4/7.
 */
public class TransferHomework {

	private List<MesgInfo> homeworkInfoList = null;
	private List<MsgReplyInfo> homeworkSubmitInfoList = null;
	private List<RefMesgclass> refMesgclassList = null;
	private  List<MsgAttachInfo> attachInfoList = null;
	private  List<MsgVoiceInfo> voiceInfoList = null;


    private  List<MsgAttachInfo> submitAttachInfoList = null;
    private  List<MsgVoiceInfo> submitVoiceInfoList = null;

	public static Map<Integer, HomeWorkEntry> homeWorkEntryMap = new HashMap<Integer, HomeWorkEntry>();
	private HomeWorkDao homeWorkDao = new HomeWorkDao();

	// private List<>

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

	public void transfer() {

		SqlSession sqlSession = getSessionFactory().openSession();
		RefactorMapper refactorMapper = sqlSession
				.getMapper(RefactorMapper.class);

		homeworkInfoList = refactorMapper.getHomeworkInfo();
		homeworkSubmitInfoList = refactorMapper.getHomeworkSubmitInfo();
		attachInfoList = refactorMapper.getHomeworkAttachInfo();
		voiceInfoList = refactorMapper.getHomeworkVoiceInfo();
		refMesgclassList = refactorMapper.getRefMesgclass();
        submitAttachInfoList = refactorMapper.getHomeworkSubmitAttachInfo();
        submitVoiceInfoList  =refactorMapper.getHomeworkSubmitVoiceInfo();
        homeworkSubmitInfoList = refactorMapper.getHomeworkSubmitInfo();

		for (MesgInfo homeworkInfo : homeworkInfoList) {
            if(TransferUser.userMap.get(homeworkInfo.getUserid())!=null) {
                HomeWorkEntry homeWorkEntry = new HomeWorkEntry(
                        TransferUser.userMap.get(homeworkInfo.getUserid()), null,
                        homeworkInfo.getMesgname(), homeworkInfo.getMesgcontent(),
                        null, null, new ArrayList<StudentSubmitHomeWork>());
                homeWorkEntry.setID(new ObjectId(homeworkInfo.getCreatetime()));
                homeWorkEntryMap.put(homeworkInfo.getId(), homeWorkEntry);
            }
		}

		transferClasses();
		// transfer voice file

		// List<IdValuePair> voiceFile = null;

		transferVoiceFile();

        transferAttachFile();

        transferHomeworkSubmit();


        for(HomeWorkEntry homeWorkEntry:homeWorkEntryMap.values()){
            homeWorkDao.addHomeWorkEntry(homeWorkEntry);
        }

		sqlSession.close();

	}



	private void transferClasses() {
		// transfer classes
		List<IdValuePair> classes = null;
		int curMsgId = -1;
		for (RefMesgclass refMesgclass : refMesgclassList) {
			if (refMesgclass.getMessageid() != curMsgId) {
				if (curMsgId > 0) {
					if (homeWorkEntryMap.get(curMsgId) != null) {
						homeWorkEntryMap.get(curMsgId).setClasses(classes);
					}
				}
				classes = new ArrayList<IdValuePair>();
				curMsgId = refMesgclass.getMessageid();
			}

            ObjectId classId = TransferClass.classMap
                    .get(refMesgclass.getClassid());
            String className = TransferClass.classNameMap.get(refMesgclass.getClassid());
            if(classId == null){
                classId = TransferClass.interestClassMap
                        .get(refMesgclass.getClassid());
                className = TransferClass.interestClassNameMap.get(classId);
            }

			classes.add(new IdValuePair(classId,className));
		}
		if (homeWorkEntryMap.get(curMsgId) != null) {
			homeWorkEntryMap.get(curMsgId).setClasses(classes);
		}

	}

	private void transferVoiceFile() {

		List<IdNameValuePair> voiceFile = null;
		int curMsgId = -1;
		for (MsgVoiceInfo voiceInfo : voiceInfoList) {
			if (voiceInfo.getMesgid() != curMsgId) {
				if (curMsgId > 0) {
					if (homeWorkEntryMap.get(curMsgId) != null) {
						homeWorkEntryMap.get(curMsgId).setVoiceFile(voiceFile);
					}
				}
				voiceFile = new ArrayList<IdNameValuePair>();
				curMsgId = voiceInfo.getMesgid();
			}

			// todo : voice path ,用upload/voice , 把音频都拷贝到这里
			voiceFile.add(new IdNameValuePair(new ObjectId(), voiceInfo
					.getVoicename(), "/upload/voice/"+voiceInfo.getVoicename()));
		}
		if (homeWorkEntryMap.get(curMsgId) != null) {
			homeWorkEntryMap.get(curMsgId).setVoiceFile(voiceFile);
		}

	}

    private void transferAttachFile(){
        List<IdNameValuePair> attachFile = null;
        int curMsgId = -1;
        for (MsgAttachInfo attachInfo : attachInfoList) {
            if (attachInfo.getMessageid() != curMsgId) {
                if (curMsgId > 0) {
                    if (homeWorkEntryMap.get(curMsgId) != null) {
                        homeWorkEntryMap.get(curMsgId).setDocFile(attachFile);
                    }
                }
                attachFile = new ArrayList<IdNameValuePair>();
                curMsgId = attachInfo.getMessageid();
            }

            // todo : attach path
            attachFile.add(new IdNameValuePair(new ObjectId(), attachInfo.getName()
                    , attachInfo.getPathname()));
        }
        if (homeWorkEntryMap.get(curMsgId) != null) {
            homeWorkEntryMap.get(curMsgId).setDocFile(attachFile);
        }
    }

    private void transferHomeworkSubmit(){

        int curMsgId = -1;
        List<StudentSubmitHomeWork> studentSubmitHomeWorkList = null;
        for(MsgReplyInfo msgReplyInfo:homeworkSubmitInfoList){
            if(msgReplyInfo.getMesgid()!=curMsgId){
                if(curMsgId>0){
                    if(homeWorkEntryMap.get(curMsgId)!=null){

                        homeWorkEntryMap.get(curMsgId).setSubmitList(studentSubmitHomeWorkList);
                    }
                }
                studentSubmitHomeWorkList = new ArrayList<StudentSubmitHomeWork>();
                curMsgId = msgReplyInfo.getMesgid();

            }

            List<IdNameValuePair> voiceSubmitList = getVoiceSubmit(msgReplyInfo.getId());
            List<IdNameValuePair> attachSubmitList = getAttachSubmit(msgReplyInfo.getId());



            ObjectId classId = TransferClass.classMap.get(msgReplyInfo.getClassid());
            if(classId ==null){
                classId = TransferClass.interestClassMap.get(msgReplyInfo.getClassid());
            }
            StudentSubmitHomeWork studentSubmitHomeWork = new StudentSubmitHomeWork(

                    TransferUser.userMap.get(msgReplyInfo.getUserid()),
                    classId,
                    msgReplyInfo.getCreatetime().getTime(),
                    msgReplyInfo.getHomeworkcontent()
            );
            studentSubmitHomeWork.setDocFile(attachSubmitList);
            studentSubmitHomeWork.setVoiceFile(voiceSubmitList);

            studentSubmitHomeWorkList.add(studentSubmitHomeWork);


        }
        if(homeWorkEntryMap.get(curMsgId)!=null){

            homeWorkEntryMap.get(curMsgId).setSubmitList(studentSubmitHomeWorkList);
        }


    }

    private List<IdNameValuePair> getVoiceSubmit(int msgreplyid){
        List<IdNameValuePair> retList  = null;

        boolean begin = false;
        for(MsgVoiceInfo msgVoiceInfo:submitVoiceInfoList){
            if(msgVoiceInfo.getMesgreplyid() == msgreplyid){
                begin = true;
            }
            if(begin)
            {
                if(msgVoiceInfo.getMesgreplyid() == msgreplyid) {
                    if (retList == null) {
                        retList = new ArrayList<IdNameValuePair>();
                    }
                    //todo path
                    retList.add(new IdNameValuePair(new ObjectId(), msgVoiceInfo
                            .getVoicename(), msgVoiceInfo.getVoicename()));
                }
                else
                {
                    break;
                }
            }
        }

        return retList;
    }

    private List<IdNameValuePair> getAttachSubmit(int msgreplyid){
        List<IdNameValuePair> retList  = null;
        boolean begin = false;
        for(MsgAttachInfo attachInfo:submitAttachInfoList){
            if(attachInfo.getMesgreplyid() == msgreplyid){
                begin = true;
            }
            if(begin)
            {
                if(attachInfo.getMesgreplyid() == msgreplyid) {
                    if (retList == null) {
                        retList = new ArrayList<IdNameValuePair>();
                    }
                    //todo path
                    retList.add(new IdNameValuePair(new ObjectId(), attachInfo.getName()
                            , attachInfo.getPathname()));
                }
                else
                {
                    break;
                }
            }
        }
        return retList;
    }


    public static void main(String[] args){
        TransferHomework transferHomework = new TransferHomework();
        transferHomework.transfer();
    }



}
