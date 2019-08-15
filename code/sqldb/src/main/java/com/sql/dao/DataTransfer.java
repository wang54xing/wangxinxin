package com.sql.dao;

import com.sql.oldDataTransfer.*;


/**
 * 数据迁移，
 * 顺序：
 * 学校（含地区）（验证）
 * 用户 （验证，头像）
 * 学校年级 （验证）
 * 学校学科（验证）
 * 班级（验证)
 * 兴趣班
 * 联盟
 * 课后作业，考试
 * 云课程，视频
 * 课程，目录，
 * 宠物, 经验值
 * 私信
 * 微校园，家园
 * 作业
 * 通知
 * 玩伴
 * 群组
 * 视频观看记录
 *
 * 电子超市
 *
 *
 * 日历（mongo)
 * 投票(mongo)
 * 问卷(mongo)
 * 新闻(mongo)
 *
 *
 *
 */
public class DataTransfer {
    public static void main(String[] args) throws Exception{

        TransferSchool transferSchool = new TransferSchool();
        transferSchool.transferRegion();
        transferSchool.transferSchool();

        System.out.println("school transferd:" + TransferSchool.schoolMap.size());

        TransferUser transferUser = new TransferUser();
        transferUser.transfer();
        System.out.println("user transferd:" + TransferUser.userMap.size());


        transferSchool.transferGrade();
        System.out.println("grade transferd:" + TransferSchool.gradeMap.size());

        transferSchool.transferSubject();

        System.out.println("subject transferd:" +TransferSchool.subjectMap.size());


        TransferClass transferClass=new TransferClass();
        transferClass.transferClasses(TransferSchool.schoolMap,TransferSchool.gradeMap,TransferUser.userMap);

        System.out.println("class transfered:"+TransferClass.classMap.size());

        transferClass.transferInterestClass();
        System.out.println("interest class transfered:"+TransferClass.interestClassMap.size());


        TransferLeague transferLeague = new TransferLeague();
        transferLeague.transfer();
        System.out.println("league transfered");

        TransferExercise transferExercise = new TransferExercise();
        transferExercise.transfer();
        System.out.println("exercise transfered");



        TransferCloudLesson transfer = new TransferCloudLesson();
        transfer.transferCloudLesson(TransferUser.userMap);

        System.out.println("cloud lesson finished");


        TransferLesson transferLesson = new TransferLesson();
        transferLesson.transferTeacherClassSubject();
        transferLesson.transferDir();

        transferLesson.transfer();


        System.out.println("lesson finished");

        TransferUserPets transferUserPets = new TransferUserPets();
        transferUserPets.transferUserPetInfo(TransferUser.userMap);

        System.out.println("userPets finished");


        TransferExperienceLogs transferUserExperience = new TransferExperienceLogs();
        transferUserExperience.transferUserExperienceLogs(TransferUser.userMap);

        System.out.println("userScoreLogs finished");


        TransferLetter transferLetter = new TransferLetter();
        transferLetter.transfer();

        TransferMicroBlog transferMicroBlog = new TransferMicroBlog();
        transferMicroBlog.transferMicroBlogInfo(TransferUser.userMap,TransferSchool.schoolMap);


        System.out.println("microblog finished");
        //作业

        TransferHomework transferHomework = new TransferHomework();
        transferHomework.transfer();

        System.out.println("homowork finished");

        TransferNotice transferNotice = new TransferNotice();
        transferNotice.transfer();
        System.out.println("notice finished");

        TransferActivity transferActivity = new TransferActivity();
        transferActivity.transfer();

        System.out.println("activity finished");

        TransferGroup transferGroup = new TransferGroup();
        transferGroup.transfer();
        System.out.println("group finished");


        TransferVideoRecord transferVideoRecord = new TransferVideoRecord();
        transferVideoRecord.transfer();
        System.out.println("video record finished");

//        TransferItemPool transferItemPool = new TransferItemPool();
//        transferItemPool.transfer();
//        System.out.println("item pool finished");

        TransferEmarket transferEmarket = new TransferEmarket();
        transferEmarket.transfer();
        System.out.println("emarket finished");


        TransferElects transferElects = new TransferElects();
        transferElects.transfer();
        System.out.println("elects finished");


        TransferQuestionnaire transferQuestionnaire = new TransferQuestionnaire();
        transferQuestionnaire.transfer();
        System.out.println("questionnaire finished");


    }
    
        
}
