package com.sql.oldDataTransfer;

import com.db.video.VideoViewRecordDao;
import com.pojo.app.IdValuePair;
import com.pojo.school.ClassEntry;
import com.pojo.video.VideoViewRecordEntry;
import com.sql.dao.RefactorMapper;
import com.sql.oldDataPojo.VideoViewInfo;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by qinbo on 15/4/22.
 */
public class TransferVideoRecord {


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
        

        List<VideoViewInfo> videoViewInfoList = refactorMapper.getVideoViewInfo();

        Map<String,VideoViewRecordEntry> studentsViewVideoMap =
                new HashMap<String, VideoViewRecordEntry>();
        for(VideoViewInfo videoViewInfo:videoViewInfoList){

            int videoId = 0;
            if(videoViewInfo.getPlayVideo()!=0){
                videoId = videoViewInfo.getPlayVideo();
            }else
            {
                videoId = videoViewInfo.getVideoId();
            }


            String studentViewKey =
                    videoViewInfo.getUserID()+""+videoId;
            if(studentsViewVideoMap.containsKey(studentViewKey)){

                VideoViewRecordEntry videoViewRecordEntry = studentsViewVideoMap.get(
                        studentViewKey);
                videoViewRecordEntry.setLastViewTime(videoViewInfo.getPlayTime().getTime());
                videoViewRecordEntry.setCount(videoViewRecordEntry.getCount()+1);
                if(videoViewInfo.getEndViewType() == 1){
                    videoViewRecordEntry.setState(1);
                }
            }
            else
            {
                IdValuePair vd = new IdValuePair(
                        TransferCloudLesson.videoMap.get(videoId),
                        TransferCloudLesson.videoNameMap.get(videoId)
                );
                IdValuePair stu = new IdValuePair(
                        TransferUser.userMap.get(videoViewInfo.getUserID()),
                        videoViewInfo.getUserName()
                );

                ClassEntry classEntry =
                        TransferClass.stuClassMap.get(TransferUser.userMap.get(videoViewInfo.getUserID()));

                if(classEntry!=null)
                {
                    //todo
                    IdValuePair cls = new IdValuePair(
                            classEntry.getID(),classEntry.getName()
                    );

                    if(vd.getId()!=null && stu.getId()!=null) {

                        VideoViewRecordEntry videoViewRecordEntry = new VideoViewRecordEntry(
                                vd, stu, cls, videoViewInfo.getEndViewType(),
                                null, 1, videoViewInfo.getPlayTime().getTime()
                        );
                        studentsViewVideoMap.put(studentViewKey,
                                videoViewRecordEntry);
                    }
                }


            }
        }

        sqlSession.close();

        VideoViewRecordDao videoViewRecordDao = new VideoViewRecordDao();
        for(VideoViewRecordEntry videoViewRecordEntry:studentsViewVideoMap.values()){
            videoViewRecordDao.addVideoViewRecord(videoViewRecordEntry);
        }
    }

}
