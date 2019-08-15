package com.sql.oldDataTransfer;

import com.db.lesson.DirDao;
import com.db.lesson.LessonDao;
import com.db.school.ExerciseItemDao;
import com.db.school.TeacherClassSubjectDao;
import com.pojo.app.IdValuePair;
import com.pojo.emarket.Comment;
import com.pojo.exercise.ExerciseMixItem;
import com.pojo.lesson.DirEntry;
import com.pojo.lesson.DirType;
import com.pojo.lesson.LessonEntry;
import com.pojo.lesson.LessonType;
import com.pojo.lesson.LessonWare;
import com.pojo.school.TeacherClassSubjectEntry;
import com.sql.dao.RefactorMapper;
import com.sql.oldDataPojo.*;

import com.sys.constants.Constant;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.util.*;

/**
 * Created by qinbo on 15/3/24.
 */
public class TransferLesson {

    private DirDao dirDao = new DirDao();

    private LessonDao lessonDao = new LessonDao();
    private ExerciseItemDao exerciseItemDao = new ExerciseItemDao();
    private TeacherClassSubjectDao teacherClassLessonDao =
            new TeacherClassSubjectDao();

    public static Map<Integer,ObjectId> teacherClassLessonMap =
            new HashMap<Integer, ObjectId>();
    public static Map<Integer ,DirEntry> dirMap = new HashMap<Integer,DirEntry>();
    public static Map<Integer,ObjectId> teacherClassLessMap =
            new HashMap<Integer, ObjectId>();

    private Map<ObjectId,LessonEntry> lessonEntryMap =
            new HashMap<ObjectId, LessonEntry>();

    public static Map<Integer,ObjectId> lessonMap = new HashMap<Integer, ObjectId>();

    public static Map<ObjectId,ObjectId> tcsubjectDirMap = new HashMap<ObjectId, ObjectId>();



    List<DirInfo> dirInfoList = null;

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



        List<LessonInfo> lessonInfoList = refactorMapper.selLessonInfo();






        for (LessonInfo lessonInfo:lessonInfoList){

            String imgUrl = lessonInfo.getImageUrl();


            Date createDate = new Date();
            long lut = System.currentTimeMillis();
            if(lessonInfo.getCreatetime()!=null){
                lut = lessonInfo.getCreatetime().getTime();
                createDate = lessonInfo.getCreatetime();
            }

            ObjectId dirId = null;
            ObjectId userId = null;
            LessonType lessonType = LessonType.UNKWON;
            if(dirMap.get(lessonInfo.getDirId())!=null){
                dirId = dirMap.get(lessonInfo.getDirId()).getID();
                lessonType.setType(dirMap.get(lessonInfo.getDirId())
                        .getType());
                userId = dirMap.get(lessonInfo.getDirId()).getOwerId();
            }

            
            LessonEntry lessonEntry = new LessonEntry(
                    lessonInfo.getCourseName(),
                    lessonInfo.getCourseContent(),
                    lessonType,//
                    null,//用户
                    dirId,
                    imgUrl,
                    null,//videoIds
                    lut,
                    0,//video count,
                    0,//document count,
                    getExerciseCount(TransferExercise.lessonExerciseMap.get(lessonInfo.getId())),//exerciseCount
                    null,//lesson ware
                    TransferExercise.lessonExerciseMap.get(lessonInfo.getId()),//exercise id
                    null,// source id
                    lessonInfo.getIsFromCloud() //
            );
            lessonEntry.setID(new ObjectId(createDate));

            if(lessonType.getType()==1){
                lessonEntry.setUserId(userId);
            }
            //lessonDao.addLessonEntry(lessonEntry);
            lessonEntryMap.put(lessonEntry.getID(), lessonEntry);
            lessonMap.put(lessonInfo.getId(), lessonEntry.getID());

        }

        //电子超市的课程单独导入，上面的导入没有电子超市课程,因为dirid的原因
        List<EmarketLessonInfo> emarketLessonInfoList = refactorMapper.getEmarketLesson();

        for(EmarketLessonInfo emarketLessonInfo:emarketLessonInfoList) {
            long lut = System.currentTimeMillis();

            Date createDate = new Date();
            if(emarketLessonInfo.getCreatetime()!=null){
                lut = emarketLessonInfo.getCreatetime().getTime();

                createDate = emarketLessonInfo.getCreatetime();
            }
            ObjectId dirId = null;
            if(TransferLesson.dirMap.get(emarketLessonInfo.getDirId())!=null) {
                dirId = TransferLesson.dirMap.get(emarketLessonInfo.getDirId()).getID();
            }
            //电子超市的课程在transfer lesson中选不出来，因为dirid的原因
            LessonEntry lessonEntry = new LessonEntry(
                    emarketLessonInfo.getCourseName(),
                    emarketLessonInfo.getCourseContent(),
                    LessonType.EMARKET_LESSON,//
                    TransferUser.userMap.get(emarketLessonInfo.getOwner()),//用户
                    dirId,
                    emarketLessonInfo.getImageUrl(),
                    null,//videoIds
                    lut,
                    0,//video count,
                    0,//document count,
                    getExerciseCount(TransferExercise.lessonExerciseMap.get(emarketLessonInfo.getId())),//exerciseCount
                    null,//lesson ware
                    TransferExercise.lessonExerciseMap.get(emarketLessonInfo.getId()),//exercise id
                    null,// source id
                    emarketLessonInfo.getIsFromCloud() //
            );
            lessonEntry.setID(new ObjectId(createDate));

            lessonDao.addLessonEntry(lessonEntry);

            lessonEntryMap.put(lessonEntry.getID(), lessonEntry);
            lessonMap.put(emarketLessonInfo.getId(), lessonEntry.getID());
        }

        //

        transferLessonVideo();


        transferLessonWare();


        transferComment();


        for(LessonEntry lessonEntry:lessonEntryMap.values()){
            lessonDao.addLessonEntry(lessonEntry);
        }



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

    public void transferLessonVideo(){
        SqlSession sqlSession = getSessionFactory().openSession();
        RefactorMapper refactorMapper = sqlSession.getMapper(RefactorMapper.class);


        List<RefLessonVideos> refLessonVideosList =
                refactorMapper.selRefLessonVideos();
        sqlSession.close();



        int curLesson = -1;
        List<ObjectId> videoIds = null;
        for(RefLessonVideos refLessonVideos:refLessonVideosList){
            if(curLesson!= refLessonVideos.getLessonId()){
                if(curLesson>0){


                    if(lessonEntryMap.get(lessonMap.get(curLesson))!=null) {

                        lessonEntryMap.get(
                                lessonMap.get(curLesson)).setVideoIds(
                                videoIds);
                        lessonEntryMap.get(
                                lessonMap.get(curLesson)).setVideoCount(
                                videoIds.size());
                    }
                }
                videoIds = new ArrayList<ObjectId>();
                curLesson = refLessonVideos.getLessonId();
            }

            videoIds.add(TransferCloudLesson.videoMap.get(refLessonVideos.getVideoId()));



        }
        if(lessonEntryMap.get(lessonMap.get(curLesson))!=null) {

            lessonEntryMap.get(lessonMap.get(curLesson)).setVideoIds(
                    videoIds
            );
            lessonEntryMap.get(
                    lessonMap.get(curLesson)).setVideoCount(
                    videoIds.size());
        }

    }

    public void transferComment(){
        SqlSession sqlSession = getSessionFactory().openSession();
        RefactorMapper refactorMapper = sqlSession.getMapper(RefactorMapper.class);


        List<LessonCommentInfo> lessonCommentInfoList =
                refactorMapper.getLessonComment();
        sqlSession.close();

        int curLesson = -1;
        List<Comment> commentList = null;
        for(LessonCommentInfo lessonCommentInfo:lessonCommentInfoList) {
            if(curLesson!= lessonCommentInfo.getOutId()){
                if(curLesson>0){

                    if(lessonEntryMap.get(lessonMap.get(curLesson))!=null) {

                        lessonEntryMap.get(
                                lessonMap.get(curLesson)).setCommentList(
                                commentList);
                        lessonEntryMap.get(
                                lessonMap.get(curLesson)).setCommentCount(
                                commentList.size());
                    }
                }
                commentList = new ArrayList<Comment>();
                curLesson = lessonCommentInfo.getOutId();
            }

            Comment comment = new Comment(TransferUser.userMap.get(lessonCommentInfo.getUserId()),
                    lessonCommentInfo.getContent());
            comment.setTime(lessonCommentInfo.getLastModified().getTime());

            commentList.add(comment);
        }
        if(lessonEntryMap.get(lessonMap.get(curLesson))!=null) {

            lessonEntryMap.get(
                    lessonMap.get(curLesson)).setCommentList(
                    commentList);
            lessonEntryMap.get(
                    lessonMap.get(curLesson)).setCommentCount(
                    commentList.size());
        }

    }

    public void transferLessonWare(){

        SqlSession sqlSession = getSessionFactory().openSession();
        RefactorMapper refactorMapper = sqlSession.getMapper(RefactorMapper.class);


        List<LessonCourseWareInfo> lessonCourseWareInfoList =
                refactorMapper.seleLessonCourseWareInfo();
        sqlSession.close();

        List<LessonWare> lessonWareList = null;

        int curLesson = -1;
        for(LessonCourseWareInfo lessonCourseWareInfo:lessonCourseWareInfoList){
            if(curLesson!= lessonCourseWareInfo.getLessonId()){
                if(curLesson>0){

                    if(lessonEntryMap.get(lessonMap.get(curLesson))!=null) {

                        lessonEntryMap.get(
                                lessonMap.get(curLesson)).setLessonWareList(
                                lessonWareList);
                        lessonEntryMap.get(
                                lessonMap.get(curLesson)).setLessonWareCount(
                                lessonWareList.size());
                    }
                }
                lessonWareList = new ArrayList<LessonWare>();
                curLesson = lessonCourseWareInfo.getLessonId();
            }
            LessonWare lessonWare = new LessonWare(lessonCourseWareInfo.getFiletype(),
                    lessonCourseWareInfo.getName(),lessonCourseWareInfo.getPath());

            lessonWareList.add(lessonWare);


        }
        if(lessonEntryMap.get(lessonMap.get(curLesson))!=null) {

            lessonEntryMap.get(lessonMap.get(curLesson)).setLessonWareList(
                    lessonWareList
            );
            lessonEntryMap.get(
                    lessonMap.get(curLesson)).setLessonWareCount(
                    lessonWareList.size());
        }




    }




    public void transferTeacherClassSubject(){

        SqlSession sqlSession = getSessionFactory().openSession();
        RefactorMapper refactorMapper = sqlSession.getMapper(RefactorMapper.class);

        List<TeacherClassLessonInfo> teacherClassLessonInfoList =
                refactorMapper.selTeacherClassSubject();


        for(TeacherClassLessonInfo teacherClassLessonInfo : teacherClassLessonInfoList){
            ObjectId classId = TransferClass.classMap.get(teacherClassLessonInfo.getClassId());
            String className = TransferClass.classNameMap.get(classId);
            //使用兴趣班
            if(classId == null){
                classId = TransferClass.interestClassMap.get(teacherClassLessonInfo.getClassId());
                className = TransferClass.interestClassNameMap.get(classId);
            }
            IdValuePair classInfo = new IdValuePair(classId,
                    className
            );

            ObjectId subjectId = TransferSchool.subjectMap.get(teacherClassLessonInfo.getSubjectId());
            IdValuePair subjectInfo = new IdValuePair(subjectId,
                    TransferSchool.subjectNameMap.get(subjectId));
            TeacherClassSubjectEntry teacherClassLessonEntry = new TeacherClassSubjectEntry(
                    TransferUser.teacherMap.get(teacherClassLessonInfo.getTeacherId()),
                    classInfo,
                    subjectInfo
            );

            if(teacherClassLessonEntry.getTeacherId()!=null &&
                    teacherClassLessonEntry.getSubjectInfo().getId()!=null &&
                    teacherClassLessonEntry.getClassInfo().getId()!=null) {

                teacherClassLessonDao.addTeacherClassSubjectEntry(teacherClassLessonEntry);
                teacherClassLessMap.put(teacherClassLessonInfo.getId(),
                        teacherClassLessonEntry.getID());


                DirEntry dirEntry = new DirEntry(
                        teacherClassLessonEntry.getID(), teacherClassLessonEntry.getClassInfo().getValue() + "" +
                        teacherClassLessonEntry.getSubjectInfo().getValue(), null, 0, DirType.CLASS_LESSON
                );

                dirDao.addDirEntry(dirEntry);
                tcsubjectDirMap.put(teacherClassLessonEntry.getID(), dirEntry.getID());
            }
            //dirMap.put(dirInfo.getId(),dirEntry);

        }




        sqlSession.close();

    }
    public void transferDir(){

        SqlSession sqlSession = getSessionFactory().openSession();
        RefactorMapper refactorMapper = sqlSession.getMapper(RefactorMapper.class);

        dirInfoList = refactorMapper.selDir();

        for(DirInfo dirInfo : dirInfoList){

            ObjectId owner = null;
            int type = 0;
            ObjectId pairId = null;

            switch (dirInfo.getType()){
                case 0://备课空间
                    type=1;
                    owner = TransferUser.teacherMap.get(dirInfo.getOwner());

                    break;
                case 1: //班级课程
                    type = 2;
                    //owner不是班级id，而是以前的 classsubjects id
                    //这里生成一个文件夹
                    owner = TransferLesson.teacherClassLessMap.get(dirInfo.getOwner());
                    pairId = tcsubjectDirMap.get(owner);
                    break;
                case 2: //校本资源
                    type = 3;
                    owner = TransferSchool.schoolMap.get(dirInfo.getOwner());
                    break;
                case 3://微课评比
                    type = 5;
                    owner = TransferClass.classMap.get(dirInfo.getOwner());
                    break;
                case 4://电子超市
                    type = 6;
                    owner = TransferUser.userMap.get(dirInfo.getOwner());

                    break;
                case 5://联盟资源
                    type = 4;
                    owner = TransferLeague.leagueMap.get(dirInfo.getOwner());
                    break;

            }
            DirEntry dirEntry = new DirEntry(
                owner,dirInfo.getName(),pairId,dirInfo.getSort(),DirType.getDirType(type)
            );

            dirDao.addDirEntry(dirEntry);
            dirMap.put(dirInfo.getId(),dirEntry);

        }


        sqlSession.close();

        transferDirPar();
    }

    private void transferDirPar(){

        for(DirInfo dirInfo:dirInfoList){
            if(dirInfo.getParent()!=0){
                if(dirMap.get(dirInfo.getId())!=null &&
                        dirMap.get(dirInfo.getParent())!=null) {
                    dirDao.update(dirMap.get(dirInfo.getId()).getID(), "pi",
                            dirMap.get(dirInfo.getParent()).getID());
                }

            }

        }

    }

    public static void main(String[] args)throws Exception
    {
        TransferLesson transferLesson = new TransferLesson();
        transferLesson.transferTeacherClassSubject();
        transferLesson.transferDir();

        transferLesson.transfer();
    }


}
