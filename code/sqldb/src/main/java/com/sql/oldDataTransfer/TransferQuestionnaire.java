package com.sql.oldDataTransfer;

import com.db.questionnaire.QuestionnaireDao;
import com.pojo.questionnaire.OldQuestionnaireEntry;
import com.pojo.questionnaire.QuestionnaireEntry;
import org.bson.types.ObjectId;

import java.util.*;

/**
 * Created by qinbo on 15/6/11.
 */
public class TransferQuestionnaire {

    public void transfer(){


        QuestionnaireDao questionnaireDao = new QuestionnaireDao();
        List<OldQuestionnaireEntry> oldQuestionnaireEntryList = questionnaireDao.getOldElectEntryList();

        for(OldQuestionnaireEntry oldQuestionnaireEntry:oldQuestionnaireEntryList){


            long pbtime = System.currentTimeMillis();
            if(oldQuestionnaireEntry.getPublishDate()!=null){
                pbtime = ((Date)oldQuestionnaireEntry.getPublishDate()).getTime();
            }
            long endtime = System.currentTimeMillis();
            if(oldQuestionnaireEntry.getEndDate()!=null){
                endtime = ((Date)oldQuestionnaireEntry.getEndDate()).getTime();
            }
            List<ObjectId> classIdList = new ArrayList<ObjectId>();
            if(oldQuestionnaireEntry.getClassIds()!=null) {
                for (Integer oclassId : oldQuestionnaireEntry.getClassIds()) {
                    ObjectId classId = TransferClass.classMap.get(oclassId);
                    if (classId != null) {
                        classIdList.add(classId);
                    }
                }
            }
            if(classIdList.isEmpty()){
                classIdList = null;
            }
            int parentRespondent = 1;
            if(oldQuestionnaireEntry.getParentRespondent()!=null){
                parentRespondent = (Boolean)oldQuestionnaireEntry.getParentRespondent() ?1:0;
            }
            int studentRespondent = 1;
            if(oldQuestionnaireEntry.getStudentRespondent()!=null){
                studentRespondent = (Boolean)oldQuestionnaireEntry.getStudentRespondent()?1:0;
            }
            int teacherRespondent = 1;
            if(oldQuestionnaireEntry.getTeacherRespondent()!=null){
                teacherRespondent = (Boolean)oldQuestionnaireEntry.getTeacherRespondent()?1:0;
            }
            int headmasterRespondent = 1;
            Map<String, List<Object>> respondents = (Map<String, List<Object>>)oldQuestionnaireEntry.getRespondents();
            Map<String,List<Object>> newRes = new HashMap<String, List<Object>>();
            if(respondents!=null) {
                for (String resKey : respondents.keySet()) {
                    Integer userKey = Integer.parseInt(resKey);
                    if (TransferUser.userMap.get(userKey) != null) {
                        newRes.put(TransferUser.userMap.get(userKey).toString(),
                                respondents.get(resKey));
                    }
                }
            }

            ObjectId pbId =
                    TransferUser.userMap.get(oldQuestionnaireEntry.getPublisher());

            ObjectId schoolId = TransferSchool.schoolMap.get(oldQuestionnaireEntry.getSchoolId());
            if(pbId!=null && schoolId!=null){
//                QuestionnaireEntry questionnaireEntry = new QuestionnaireEntry(
//                        oldQuestionnaireEntry.getName(),
//                        pbId,
//                        pbtime,
//                        endtime,
//                        TransferSchool.schoolMap.get(oldQuestionnaireEntry.getSchoolId()),
//                        classIdList,
//                        parentRespondent,
//                        studentRespondent,
//                        teacherRespondent,
//                        headmasterRespondent,
//                        oldQuestionnaireEntry.getDocUrl(),
//                        oldQuestionnaireEntry.getAnswerSheet(),
//                        newRes
//                );
//                questionnaireDao.add(questionnaireEntry);
            }



        }
    }

    public static void main(String[] args){
        TransferQuestionnaire transferQuestionnaire = new TransferQuestionnaire();
        transferQuestionnaire.transfer();
    }
}
