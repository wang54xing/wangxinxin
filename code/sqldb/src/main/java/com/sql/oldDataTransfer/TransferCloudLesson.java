package com.sql.oldDataTransfer;

import com.db.cloudlesson.CloudLessonDao;
import com.db.cloudlesson.CloudLessonTypeDao;
import com.db.video.VideoDao;
import com.pojo.cloudlesson.CloudLessonEntry;
import com.pojo.cloudlesson.CloudLessonTypeEntry;
import com.pojo.video.VideoEntry;
import com.sql.dao.RefactorMapper;
import com.sql.oldDataPojo.CloudLessonInfo;
import com.sql.oldDataPojo.CloudLessonTypeInfo;
import com.sql.oldDataPojo.VideoInfo;
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
 * Created by qinbo on 15/3/20.
 */
public class TransferCloudLesson {
    private List<CloudLessonInfo> cllInfoList = null;
    private List<CloudLessonTypeInfo> clltypeInfoList = null;
    private List<VideoInfo> videoInfoList = null;

    public static Map<Integer,ObjectId> cloudLessonMap = new HashMap<Integer, ObjectId>();
    public static Map<Integer,Integer> subjectMap=new HashMap<Integer, Integer>();
    public static Map<Integer,Integer> gradeMap=new HashMap<Integer, Integer>();


    public static Map<Integer,ObjectId> videoMap=new HashMap<Integer, ObjectId>();
    public static Map<Integer,String> videoNameMap = new HashMap<Integer, String>();

    public static Map<Integer,ObjectId> lessonTypeMap=new HashMap<Integer, ObjectId>();

    static{
        subjectMap.put(31,1);//语文
        subjectMap.put(32,2);//数学
        subjectMap.put(33,3);//英语
        subjectMap.put(34,4);//物理
        subjectMap.put(35,5);//化学
        subjectMap.put(44,6);//生物
        subjectMap.put(36,7);//地理
        subjectMap.put(37,8);//历史
        subjectMap.put(39,9);//政治
        subjectMap.put(45,10);//名人演讲
        subjectMap.put(46,11);//音体美
        subjectMap.put(47,12);//科学
        subjectMap.put(48,13);//奥数
        subjectMap.put(51,14);//课外英语
        subjectMap.put(52,15);//海外学习推荐
        subjectMap.put(53,16);//计算机
        subjectMap.put(50,17);//大学风采

        gradeMap.put(11,1);//一年级
        gradeMap.put(12,2);//二年级
        gradeMap.put(13,3);//三年级
        gradeMap.put(14,4);//四年级
        gradeMap.put(15,5);//五年级
        gradeMap.put(16,6);//六年级

        gradeMap.put(21,7);//初一
        gradeMap.put(22,8);//初二
        gradeMap.put(23,9);//初三

        gradeMap.put(31,10);//高一
        gradeMap.put(32,11);//高二
        gradeMap.put(33,12);//高三
        gradeMap.put(100,13);//高考总复习
    }
    private CloudLessonDao cloudLessonDao = new CloudLessonDao();
    private CloudLessonTypeDao cloudLessonTypeDao=new CloudLessonTypeDao();
    private VideoDao videoDao=new VideoDao();
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

    private RefactorMapper getRefactorMapper(){
        SqlSession sqlSession = getSessionFactory().openSession();
        RefactorMapper refactorMapper = sqlSession.getMapper(RefactorMapper.class);
        return refactorMapper;
    }

    public void transferCloudLesson(Map<Integer,ObjectId> userMap){
        transferCloudLessonType();
        transferVideos();
        cllInfoList =  getRefactorMapper().getCloudLessonInfo();
        SqlSession sqlSession = getSessionFactory().openSession();
        RefactorMapper refactorMapper = sqlSession.getMapper(RefactorMapper.class);

        for(CloudLessonInfo cloudLessonInfo  :cllInfoList){

            List<Integer> gradeList=refactorMapper.getCloudLessonGradeId(cloudLessonInfo.getId());
            getHandleGradeId(cloudLessonInfo,gradeList);
            List<Integer> lessonList=refactorMapper.getCloudLessonTypeId(cloudLessonInfo.getId());
            getHandleLessonType(cloudLessonInfo,lessonList);
            dealCloudLesson(cloudLessonInfo,userMap);
        }
        System.out.println("CloudLessonInfo size:"+cllInfoList.size());

    }

    private void getHandleGradeId(CloudLessonInfo cloudLessonInfo,List<Integer> gradeList){
        List<Integer> tempList=new ArrayList<Integer>();
        for(Integer gradeId:gradeList){
            tempList.add(gradeMap.get(gradeId));
        }
        cloudLessonInfo.setGradeList(tempList);
    }

    private void getHandleLessonType(CloudLessonInfo cloudLessonInfo,List<Integer> lessonList){
        List<ObjectId> tempList=new ArrayList<ObjectId>();
        for(Integer lessonTypeId:lessonList){
            tempList.add(lessonTypeMap.get(lessonTypeId));
        }
        cloudLessonInfo.setLessonList(tempList);
    }

    public void transferCloudLessonType(){
        clltypeInfoList =  getRefactorMapper().getCloudLessonTypeInfo();
        for(CloudLessonTypeInfo cloudLessonTypeInfo  :clltypeInfoList){
            dealCloudLessonType(cloudLessonTypeInfo);
        }
         System.out.println("CloudLessonTypeInfo size:"+clltypeInfoList.size());

    }

    private void dealCloudLessonType(CloudLessonTypeInfo cloudLessonTypeInfo) {

        if(subjectMap.get(cloudLessonTypeInfo.getSubjectId())!=null) {
            CloudLessonTypeEntry cloudLessonEntry = new CloudLessonTypeEntry(
                    cloudLessonTypeInfo.getSchoolType(),
                    subjectMap.get(cloudLessonTypeInfo.getSubjectId()),
                    gradeMap.get(cloudLessonTypeInfo.getGradeId()),
                    cloudLessonTypeInfo.getChapter(),
                    cloudLessonTypeInfo.getDescription(),
                    0
            );
            cloudLessonTypeDao.addCloudLessonTypeEntry(cloudLessonEntry);
            lessonTypeMap.put(cloudLessonTypeInfo.getId(), cloudLessonEntry.getID());
        }
    }

    public void transferVideos(){
        videoInfoList =  getRefactorMapper().getVideoInfo();
        for(VideoInfo videoInfo  :videoInfoList){
            dealVideoInfo(videoInfo);
        }
        System.out.println("VideoInfo size:"+videoInfoList.size());

    }
    private void dealVideoInfo(VideoInfo videoInfo) {

        long uploadDate = System.currentTimeMillis();
        if(videoInfo.getUploadDate()!=null)
        {
            uploadDate = videoInfo.getUploadDate().getTime();
        }

        //transfer key
        String filekey = videoInfo.getBucketkey();

        int videosourcetype = videoInfo.getVideoSourceType();

        if(videosourcetype ==9){//废弃视频不处理
            return;
        }

        if(videosourcetype == 0){//flash
            filekey = "cloud/flash/"+filekey+".swf";
        }else if(videosourcetype == 1){//cloud
            filekey = "cloud/"+filekey;
        }else if(videosourcetype == 2){//user
            filekey = "user/"+filekey;
        }else {
            filekey="other/"+filekey;
        }


        String videoimage = videoInfo.getImageUrl();
        if(videoimage!=null){
            //todo :  图片路径不修改
            //videoimage = "videoimage"+videoimage.substring(videoimage.lastIndexOf('/'));
        }

        VideoEntry videoEntry = new VideoEntry(
                videoInfo.getName(),
                uploadDate,
                videoInfo.getLength(),
                videoimage,
                videoInfo.getViewNumber(),
                videoInfo.getUploadState(),
                videoInfo.getVideoType(),
                videoInfo.getDeleteFlag(),
                videoInfo.getTemporaryName(),
                filekey,
                videoInfo.getPersistentId(),
                videoInfo.getVideoSourceType()
        );

        videoDao.addVideoEntry(videoEntry);
        videoMap.put(videoInfo.getId(),videoEntry.getID());
        videoNameMap.put(videoInfo.getId(),videoInfo.getName());
    }

    private void dealVideoImg(){

    }

    private void dealCloudLesson(CloudLessonInfo cloudLessonInfo,Map<Integer,ObjectId> userMap) {
        List<ObjectId> videoList=new ArrayList<ObjectId>();
        videoList.add(videoMap.get(cloudLessonInfo.getVideoId()));

        String videoimage = cloudLessonInfo.getImageUrl();
        if(videoimage!=null){
            //videoimage = "cloudcourseimage"+videoimage.substring(videoimage.lastIndexOf('/'));
        }

        CloudLessonEntry cloudLessonEntry = new CloudLessonEntry(
                cloudLessonInfo.getLessonName(),
                null,
                cloudLessonInfo.getLessonContent(),
                0,//order
                videoimage,
                videoList,
                subjectMap.get(cloudLessonInfo.getSubjectId()),
                cloudLessonInfo.getGradeList(),
                cloudLessonInfo.getLessonList()
                ,0,1,0,null,new ArrayList<ObjectId>(),"资源板块后端"
        );
        cloudLessonDao.addCloudLessonEntry(cloudLessonEntry);
        cloudLessonMap.put(cloudLessonInfo.getId(),cloudLessonEntry.getID());
    }
    public static void main(String[] args) throws Exception{
        TransferCloudLesson transfer = new TransferCloudLesson();
        transfer.transferCloudLesson(new HashMap<Integer, ObjectId>());
    }
}
