package com.sql.oldDataTransfer;

import com.db.letter.LetterDao;
import com.db.letter.LetterRecordDao;
import com.pojo.letter.*;
import com.sql.dao.RefactorMapper;
import com.sql.oldDataPojo.LetterInfo;
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
 * Created by qinbo on 15/3/31.
 */
public class TransferLetter {


    public static Map<Integer,LetterEntry> letterMap = new HashMap<Integer, LetterEntry>();
    private LetterDao letterDao = new LetterDao();
    private LetterRecordDao letterRecordDao = new LetterRecordDao();
    private List<LetterEntry> exportedLetterList = new ArrayList<LetterEntry>();
    public static Map<ObjectId,LetterEntry> letterEntryMap = new HashMap<ObjectId, LetterEntry>();
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

    private static boolean isNumeric(String str){
        for (int i = str.length();--i>=0;){
            if (!Character.isDigit(str.charAt(i))){
                return false;
            }
        }
        return true;
    }

    public void transfer(){
        SqlSession sqlSession = getSessionFactory().openSession();
        RefactorMapper refactorMapper = sqlSession.getMapper(RefactorMapper.class);

        List<LetterInfo> letterInfoList = refactorMapper.selLetterInfo();
        sqlSession.close();

        for(LetterInfo letterInfo:letterInfoList){

            String content = "";
            String message = letterInfo.getMessage();
            int type = 1;
            if(message.contains("<div >")){
                content = message.substring(0,message.indexOf("<div"));
                type = LetterType.ACTIVITY_INVITE.getType();
            }
            else
            {
                content = message;
            }
            if(message.contains("已经取消了!"))
            {
                type = LetterType.ACTIVITY_CANCEL.getType();
            }

            int state = LetterState.LETTER_SEDND_SUCCESS.getState();
            //私信删除的不导入
            if(letterInfo.getSenderdelete() == 1){
                continue;
            }

            ObjectId senderId = null;
            if(letterInfo.getSender()!=null && isNumeric(letterInfo.getSender())){
                senderId = TransferUser.userMap.get(Integer.parseInt(
                        letterInfo.getSender()));
            }
            //发送者已不在不导入
            if(senderId == null){
                continue;
            }

            LetterEntry letterEntry = new LetterEntry(
                    senderId,
                    type,
                    state,
                    content,
                    null,
                    null//todo:exd 附加数据，需要活动的数据

            );
            letterEntry.setID(new ObjectId(letterInfo.getSendingtime()));

            letterMap.put(letterInfo.getId(),letterEntry);
            letterEntryMap.put(letterEntry.getID(),letterEntry);
        }

        for(LetterInfo letterInfo:letterInfoList) {


            if(letterMap.get(letterInfo.getId())==null) {
                continue;
            }
            int state = LetterState.LETTER_SEDND_SUCCESS.getState();
            if(letterInfo.getIsread() == 1){
                state = LetterState.LETTER_READED.getState();
            }
            if(letterInfo.getRecipientdelete() == 1){
                state = LetterState.LETTER_DELETED.getState();
                continue;
            }
            ObjectId replyId= null;
            //if(letterMap.get(letterInfo.getReplyId())!=null){
            //    replyId = letterMap.get(letterInfo.getReplyId()).getID();
            //}

            ObjectId recvId = null;
            if(letterInfo.getRecipient()!=null && isNumeric(letterInfo.getRecipient())){
                recvId = TransferUser.userMap.get(Integer.parseInt(
                        letterInfo.getRecipient()));
            }
            if(recvId ==null){
                continue;
            }
            ReceiveInfo receiveInfo = new ReceiveInfo(
                    recvId,
                    state,
                    replyId
            );
            List<ReceiveInfo> receiveInfoList = new ArrayList<ReceiveInfo>();
            receiveInfoList.add(receiveInfo);
            letterMap.get(letterInfo.getId()).setReceiveList(receiveInfoList);

            letterDao.addLetterEntry(
                    letterMap.get(letterInfo.getId())
            );
            exportedLetterList.add(letterMap.get(letterInfo.getId()));

        }
        //Map<ObjectId,ObjectId> senderRecvPair = new HashMap<ObjectId, ObjectId>();

        for(LetterEntry letterEntry:exportedLetterList){
            ObjectId senderId = letterEntry.getSenderId();
            List<ReceiveInfo> receiveInfoList = letterEntry.getReceiveList();
            ObjectId receiverId = receiveInfoList.get(0).getReceiverId();

            LetterRecordEntry letterRecordEntry = letterRecordDao.getLetterRecordEntry(senderId,receiverId);

            if(letterRecordEntry == null){
                if(letterEntry.getState()!=LetterState.LETTER_DELETED.getState()
                        ||receiveInfoList.get(0).getState()!=LetterState.LETTER_DELETED.getState()){
                    letterRecordEntry = new LetterRecordEntry(
                            senderId,receiverId,letterEntry.getID()
                    );
                    if(letterEntry.getState() == LetterState.LETTER_DELETED.getState()){
                        letterRecordEntry.setUserState(new LetterRecordEntry.LetterRecordState(null,0));
                    }
                    if(receiveInfoList.get(0).getState() == LetterState.LETTER_DELETED.getState()){
                        letterRecordEntry.setLetterUserState(new LetterRecordEntry.LetterRecordState(null,0));
                    }

                    LetterRecordEntry.LetterRecordState userState = letterRecordEntry.getUserState();
                    LetterRecordEntry.LetterRecordState letterUserState = letterRecordEntry.getLetterUserState();
                    userState.setUnRead(0);
                    letterUserState.setUnRead(0);
                    letterRecordEntry.setUserState(userState);
                    letterRecordEntry.setLetterUserState(letterUserState);

                    letterRecordDao.addLetterRecordEntry(letterRecordEntry);
                }

            }
            else
            {

            }

        }



    }


}
