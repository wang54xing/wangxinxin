package com.sql.oldDataTransfer;

import com.db.emarket.GoodsDao;
import com.db.lesson.LessonDao;
import com.db.school.ExerciseItemDao;
import com.pojo.emarket.GoodsEntry;
import com.pojo.emarket.GoodsState;
import com.pojo.emarket.GoodsType;
import com.pojo.exercise.ExerciseMixItem;
import com.pojo.lesson.LessonEntry;
import com.pojo.lesson.LessonType;
import com.sql.dao.RefactorMapper;
import com.sql.oldDataPojo.EmarketLessonInfo;
import com.sql.oldDataPojo.RefLessonGradeInfo;
import com.sql.oldDataPojo.RefLessonSubjectInfo;
import com.sys.constants.Constant;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.util.*;

/**
 * Created by qinbo on 15/5/10.
 */
public class TransferEmarket {

    private GoodsDao goodsDao = new GoodsDao();
    private List<RefLessonGradeInfo> refLessonGradeInfoList;
    private List<RefLessonSubjectInfo> refLessonSubjectInfoList;
    ExerciseItemDao exerciseItemDao = new ExerciseItemDao();


    private LessonDao lessonDao = new LessonDao();
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

    public void transfer() {
        SqlSession sqlSession = getSessionFactory().openSession();
        RefactorMapper refactorMapper = sqlSession.getMapper(RefactorMapper.class);


        refLessonGradeInfoList = refactorMapper.getRefLessonGrade();
        refLessonSubjectInfoList = refactorMapper.getRefLessonSubject();





        List<EmarketLessonInfo> emarketLessonInfoList = refactorMapper.getEmarketLesson();

        for(EmarketLessonInfo emarketLessonInfo:emarketLessonInfoList){


            List<Integer> subjectList = getSubjectList(emarketLessonInfo.getId());
            List<Integer> gradeList = getGradeList(emarketLessonInfo.getId());



            long stime = System.currentTimeMillis()+6*30*24*60*60*1000;
            if(emarketLessonInfo.getCreatetime()!=null){
                stime = emarketLessonInfo.getCreatetime().getTime()+6*30*24*60*60*1000;
            }


            GoodsEntry goodsEntry = new GoodsEntry(
                    subjectList,
                    gradeList,
                    GoodsType.LESSON.getType(),
                    GoodsState.SHELVE.getType(),
                    TransferUser.userMap.get(emarketLessonInfo.getOwner()),
                    emarketLessonInfo.getCourseName(),
                    TransferLesson.lessonMap.get(emarketLessonInfo.getId()),
                    new ObjectId(),
                    emarketLessonInfo.getPrice(),
                    0,//todo sellcount
                    6,
                    emarketLessonInfo.getCourseContent(),
                    stime,
                    emarketLessonInfo.getIsopen()

            );
            goodsEntry.setID(new ObjectId(new Date()));

            goodsDao.addGoodsEntry(goodsEntry);
        }









        sqlSession.close();
    }
    public int getExerciseCount(ObjectId exerciseId){
        if(exerciseId==null){
            return 0;
        }

        List<ExerciseMixItem> items =  exerciseItemDao.getExerciseMixItems(exerciseId, Constant.FIELDS);

        if(items!=null){
            return items.size();
        }
        return 0;
    }
    private List<Integer> getSubjectList(int lessonid){


        List<Integer> subjectList = new ArrayList<Integer>();


        for(RefLessonSubjectInfo refLessonSubjectInfo:refLessonSubjectInfoList){
            if(refLessonSubjectInfo.getLessonId()==lessonid){
                if (TransferCloudLesson.subjectMap.get(refLessonSubjectInfo.getSubjectId())!=null)
                {
                    subjectList.add(TransferCloudLesson.subjectMap.get(refLessonSubjectInfo.getSubjectId()));
                }
            }
        }

        return subjectList;
    }

    private List<Integer> getGradeList(int lessonid){
        List<Integer> gradeList = new ArrayList<Integer>();

        for(RefLessonGradeInfo refLessonGradeInfo:refLessonGradeInfoList){
            if(refLessonGradeInfo.getLessonId()==lessonid){
                if (TransferCloudLesson.gradeMap.get(refLessonGradeInfo.getGradeId())!=null)
                {
                    gradeList.add(TransferCloudLesson.gradeMap.get(refLessonGradeInfo.getGradeId()));
                }
            }
        }
        return gradeList;
    }

}
