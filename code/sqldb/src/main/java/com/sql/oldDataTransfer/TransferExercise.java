package com.sql.oldDataTransfer;

import com.db.school.ExerciseAnswerDao;
import com.db.school.ExerciseDao;
import com.db.school.ExerciseItemDao;
import com.pojo.app.IdValuePair;
import com.pojo.exercise.ExerciseAnswerEntry;
import com.pojo.exercise.ExerciseEntry;
import com.pojo.exercise.ExerciseItemEntry;
import com.pojo.exercise.ExerciseItemType;
import com.sql.dao.RefactorMapper;
import com.sql.oldDataPojo.*;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.bson.types.ObjectId;

import java.io.*;
import java.util.*;

/**
 * Created by qinbo on 15/3/27.
 */
public class TransferExercise {

    private ExerciseDao exerciseDao =
            new ExerciseDao();
    private ExerciseItemDao exerciseItemDao =
            new ExerciseItemDao();

    private ExerciseAnswerDao exerciseAnswerDao =
            new ExerciseAnswerDao();
    private List<WordExerciseInfo> wordExerciseInfoList = null;
    private HashMap<ObjectId,ExerciseEntry> exerciseEntryMap
            = new HashMap<ObjectId, ExerciseEntry>();

    public static Map<Integer,ObjectId> exerciseMap = new HashMap<Integer,ObjectId>();
    public static Map<Integer,ObjectId> exerciseItemMap = new HashMap<Integer, ObjectId>();

    private List<RefWordClassInfo> refWordClassInfoList = null;

    public static Map<Integer,ObjectId> lessonExerciseMap = new HashMap<Integer, ObjectId>();

    private static Map<String,String> examfileMap = new HashMap<String, String>();

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

    private  void loadfilemapfromdisk() throws IOException{
        BufferedReader bufferedReaderMap = new BufferedReader(new FileReader("/Users/qinbo/k6kt-temp/examfilemap.txt"));
        while(bufferedReaderMap.ready()){
            String line = bufferedReaderMap.readLine();

            if(!line.isEmpty()&&line!=null) {
                String[] linedata = line.split(",");
                examfileMap.put(linedata[0], linedata[1]);
            }
        }
        bufferedReaderMap.close();

    }
    private void saveUserAvatarToDisk() throws Exception {
        BufferedWriter bufferedWriterrMap = new BufferedWriter(new FileWriter("/Users/qinbo/k6kt-temp/examfilemap.txt", false));
        for (String key : examfileMap.keySet()) {
            String line = key + "," + examfileMap.get(key);
            bufferedWriterrMap.write(line);
            bufferedWriterrMap.newLine();

        }
        bufferedWriterrMap.close();
    }


    public void transfer() throws Exception{

        loadfilemapfromdisk();
        SqlSession sqlSession = getSessionFactory().openSession();
        RefactorMapper refactorMapper = sqlSession.getMapper(RefactorMapper.class);

        wordExerciseInfoList= refactorMapper.selWordExercise();

        refWordClassInfoList = refactorMapper.selRefWordlClassInfo();





        for (WordExerciseInfo wordExerciseInfo:wordExerciseInfoList){
            int type = 0;
            if(wordExerciseInfo.getWordtype()==1)//课后练习
            {
                type = 2;
            }else //考试
            {
                type = 1;
            }

            //todo: qiniu 上传



            //42演示平台
            String domainUrl = "http://www.k6kt.com";
            String rename = null;
            String localpath = "exam/";
            if(wordExerciseInfo.getQuestwordpath()!=null&&!wordExerciseInfo.getQuestwordpath().isEmpty()) {

                if(examfileMap.get(wordExerciseInfo.getQuestwordpath())!=null)
                {
                    rename = examfileMap.get(wordExerciseInfo.getQuestwordpath());
                }
                else {
                    rename = new ObjectId().toString();
                    FileUtil.downloadFromUrl(domainUrl + wordExerciseInfo.getQuestwordpath(), localpath + rename + ".doc");
                    FileUtil.downloadFromUrl(domainUrl + wordExerciseInfo.getQuestpdfpath(), localpath + rename + ".pdf");
                    FileUtil.downloadFromUrl(domainUrl + wordExerciseInfo.getQuestswfpath(), localpath + rename + ".swf");
                    examfileMap.put(wordExerciseInfo.getQuestwordpath(),rename);
                }
            }




            String rename2 = null;
            if(wordExerciseInfo.getAnswerwordpath()!=null&&!wordExerciseInfo.getAnswerwordpath().isEmpty()) {

                if (examfileMap.get(wordExerciseInfo.getAnswerwordpath()) != null) {
                    rename2 = examfileMap.get(wordExerciseInfo.getAnswerwordpath());
                } else {
                    rename2 = new ObjectId().toString();
                    FileUtil.downloadFromUrl(domainUrl + wordExerciseInfo.getAnswerwordpath(), localpath + rename2 + ".doc");
                    FileUtil.downloadFromUrl(domainUrl + wordExerciseInfo.getAnswerpdfpath(), localpath + rename2 + ".pdf");
                    FileUtil.downloadFromUrl(domainUrl + wordExerciseInfo.getAnswerswfpath(), localpath + rename2 + ".swf");
                    examfileMap.put(wordExerciseInfo.getAnswerwordpath(), rename2);
                }

            }
            //todo: here
            String docPath = rename;
            String answerPath = rename2;


            long updateTime = System.currentTimeMillis();
            Date uploadDate = new Date();

            if(wordExerciseInfo.getUploadTime()!=null){
                updateTime = wordExerciseInfo.getUploadTime().getTime();
                uploadDate = wordExerciseInfo.getUploadTime();
            }

            ObjectId userId = TransferUser.unkownUser.getID();
            if(TransferUser.userMap.get(wordExerciseInfo.getUserId())!=null){
                userId = TransferUser.userMap.get(wordExerciseInfo.getUserId());
            }
            ExerciseEntry exerciseEntry = new ExerciseEntry(
                    type,
                    userId,
                    null,
                    wordExerciseInfo.getPractisename(),
                    docPath,
                    answerPath,
                    wordExerciseInfo.getIsConfiging(),
                    wordExerciseInfo.getTotalScore(),
                    wordExerciseInfo.getExerciseTime(),
                    updateTime,
                    null
            );

            exerciseEntry.setID(new ObjectId(uploadDate));

            if(wordExerciseInfo.getLessionid()!=null){
                lessonExerciseMap.put(wordExerciseInfo.getLessionid(),
                        exerciseEntry.getID());
            }
            //exerciseDao.add(exerciseEntry);

            exerciseEntryMap.put(exerciseEntry.getID(), exerciseEntry);

            exerciseMap.put(wordExerciseInfo.getId(), exerciseEntry.getID());


        }


        int curWordId = -1;
        List<ObjectId> classIds = null;
        for(RefWordClassInfo refWordClassInfo:refWordClassInfoList){
            if(curWordId!= refWordClassInfo.getWordId()){
                if(curWordId>0){

                    if(exerciseEntryMap.get(exerciseMap.get(curWordId))!=null) {
                        exerciseEntryMap.get(exerciseMap.get(curWordId)).setClassIds(
                                classIds

                        );
                    }
                }
                classIds = new ArrayList<ObjectId>();
                curWordId = refWordClassInfo.getWordId();
            }

            if(TransferClass.classMap.get(refWordClassInfo.getClassId())!=null)
            {
                classIds.add(TransferClass.classMap.get(refWordClassInfo.getClassId()));
            }
            if(TransferClass.interestClassMap.get(refWordClassInfo.getClassId())!=null)
            {
                classIds.add(TransferClass.interestClassMap.get(refWordClassInfo.getClassId()));
            }



        }
        if(exerciseEntryMap.get(exerciseMap.get(curWordId))!=null) {
            exerciseEntryMap.get(exerciseMap.get(curWordId)).setClassIds(
                    classIds

            );
        }


        //计算提交数

        List<WordExerciseSubmitInfo> wordExerciseSubmitInfoList =
                refactorMapper.selWordExerciseSubmit();

        for(WordExerciseSubmitInfo wordExerciseSubmitInfo:wordExerciseSubmitInfoList){
        	
        	 //结构变化，需要将提交作业的学生ID导入
//            if(exerciseEntryMap.get(exerciseMap.get(wordExerciseSubmitInfo.getWordId()))!=null) {
//                exerciseEntryMap.get(exerciseMap.get(wordExerciseSubmitInfo.getWordId())).
//                        setSubmitStudent(wordExerciseSubmitInfo.getSubmit());
//            }
        }

        for(ExerciseEntry exerciseEntry : exerciseEntryMap.values()){
            exerciseDao.add(exerciseEntry);

        }


        transferItem();

        transferAnswer();



        sqlSession.close();

        saveUserAvatarToDisk();


    }
    public void transferItem(){



        SqlSession sqlSession = getSessionFactory().openSession();
        RefactorMapper refactorMapper = sqlSession.getMapper(RefactorMapper.class);

        List<WordExerciseBigQuestInfo> wordExerciseBigQuestInfoList =
                refactorMapper.selExerciseBigQuest();
        List<WordExerciseSmallQuestInfo> wordExerciseSmallQuestInfoList =
                refactorMapper.selExerciseSmallQuest();


        sqlSession.close();

        Map<Integer,ExerciseItemEntry> exerciseItemEntryMap =
                new HashMap<Integer, ExerciseItemEntry>();

        for (WordExerciseBigQuestInfo wordExerciseBigQuestInfo:wordExerciseBigQuestInfoList){
            int answerTime = -1;
            if(wordExerciseBigQuestInfo.getAnswerTime()>0){
                answerTime = wordExerciseBigQuestInfo.getAnswerTime();
            }
            ExerciseItemEntry exerciseItemEntry = new ExerciseItemEntry(
                    exerciseMap.get(wordExerciseBigQuestInfo.getWordexerciseId()),
                    "",//名字
                    wordExerciseBigQuestInfo.getQuestNum()+"",
                    answerTime,
                    null
            );

            exerciseItemEntryMap.put(wordExerciseBigQuestInfo.getId(),
                    exerciseItemEntry);

        }
        int curBigQuestId = -1;
        List<ExerciseItemEntry.Item> itemList = null;
        for(WordExerciseSmallQuestInfo wordExerciseSmallQuestInfo:wordExerciseSmallQuestInfoList){
            if(curBigQuestId!= wordExerciseSmallQuestInfo.getBigQuestId()){
                if(curBigQuestId>0){
                    if(exerciseItemEntryMap.get(curBigQuestId)!=null){
                        exerciseItemEntryMap.get(curBigQuestId).setItemList(itemList);
                    }

                }

                itemList = new ArrayList<ExerciseItemEntry.Item>();
                curBigQuestId = wordExerciseSmallQuestInfo.getBigQuestId();
            }

            int type = 2;
            switch (wordExerciseSmallQuestInfo.getType()){
                case 2:
                    type = ExerciseItemType.SINGLECHOICE.getType();
                    break;
                case 3:
                    type = ExerciseItemType.TRUE_OR_FALSE.getType();
                    break;
                case 4:
                    type = ExerciseItemType.GAP.getType();
                    break;
                case 5:
                    type = ExerciseItemType.SUBJECTIVE.getType();
                    break;
            }
            ExerciseItemEntry.Item item = new ExerciseItemEntry.Item(
                    wordExerciseSmallQuestInfo.getQuestNum() + "",
                    type,
                    wordExerciseSmallQuestInfo.getScore(),
                    wordExerciseSmallQuestInfo.getAnswer(),
                    wordExerciseSmallQuestInfo.getOptionNum(),
                    null
            );
            itemList.add(item);
            exerciseItemMap.put(wordExerciseSmallQuestInfo.getId(),item.getId());

        }
        if(exerciseItemEntryMap.get(curBigQuestId)!=null) {
            exerciseItemEntryMap.get(curBigQuestId).setItemList(itemList);
        }

        for(ExerciseItemEntry exerciseItemEntry:exerciseItemEntryMap.values()){
            exerciseItemDao.addEntry(exerciseItemEntry);
        }


    }



    public void transferAnswer(){


        SqlSession sqlSession = getSessionFactory().openSession();
        RefactorMapper refactorMapper = sqlSession.getMapper(RefactorMapper.class);

        List<WordExerciseAnswerInfo> wordExerciseBigQuestInfoList =
                refactorMapper.selExerciseAnswer();


        sqlSession.close();



        int lastQuestId = -1;
        int lastUserId = -1;


        for(WordExerciseAnswerInfo wordExerciseAnswerInfo:wordExerciseBigQuestInfoList){


            //去掉重复,丢掉多张图片的情况
            if(lastQuestId!=wordExerciseAnswerInfo.getQuestId() &&
                    lastUserId!=wordExerciseAnswerInfo.getUserId()) {

                lastQuestId = wordExerciseAnswerInfo.getQuestId();
                lastUserId = wordExerciseAnswerInfo.getUserId();

                double userScore = -1;

                if (wordExerciseAnswerInfo.getScore() != null) {
                    userScore = (double) wordExerciseAnswerInfo.getScore();
                }

                List<IdValuePair> answerPic = null;

                if(wordExerciseAnswerInfo.getAnswerPic()!=null){

                    String newPicPath = wordExerciseAnswerInfo.getAnswerPic();
                    //todo : image path
                            answerPic  = new ArrayList<IdValuePair>();
                    answerPic.add(new IdValuePair(new ObjectId(),newPicPath
                            ));
                }

                ExerciseAnswerEntry exerciseAnswerEntry = new ExerciseAnswerEntry(
                        exerciseMap.get(wordExerciseAnswerInfo.getWordExerciseId()),
                        exerciseItemMap.get(wordExerciseAnswerInfo.getQuestId()),
                        TransferUser.userMap.get(wordExerciseAnswerInfo.getUserId()),
                        wordExerciseAnswerInfo.getAnswer(),
                        userScore,
                        wordExerciseAnswerInfo.getIsCorrect(),
                        answerPic
                );
                exerciseAnswerDao.addEntry(exerciseAnswerEntry);
            }

        }



    }
    private void ReTransferDocumentToQiniu(String oldUrl,
                                             String fileName,
                                             String prefix) throws Exception{

        if(oldUrl == null)
        {
            return;
        }
        String fileExt = oldUrl.substring(oldUrl.indexOf("."));
        //String prefix = "exercise/";
        FileUtil.downloadFromUrl(oldUrl,fileName.toString());
        FileUtil.uploadToQiNiu(FileUtil.tempFilePath+fileName,
                "k6kt-doc",prefix+fileName+fileExt);
        //return "http://7xiclj.com1.z0.glb.clouddn.com/"+prefix+fileName;
    }




}
