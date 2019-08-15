package com.sql.oldDataTransfer;

import com.db.school.ClassDao;
import com.db.school.InterestClassDao;
import com.pojo.school.*;
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
 * Created by qinbo on 15/3/20.
 */
public class TransferClass {
    private List<ClassesInfo> clsInfoList = null;
    public static Map<Integer,ObjectId> classMap = new HashMap<Integer, ObjectId>();
    public static Map<Integer,ObjectId> interestClassMap = new HashMap<Integer, ObjectId>();
    public static Map<ObjectId,String> classNameMap = new HashMap<ObjectId, String>();
    public static Map<ObjectId,String> interestClassNameMap = new HashMap<ObjectId, String>();
    public static Map<ObjectId,ClassEntry> stuClassMap = new HashMap<ObjectId, ClassEntry>();

    private static Map<ObjectId,Integer> studentTermTypeMap = new HashMap<ObjectId, Integer>();
    //private
    private ClassDao classDao = new ClassDao();
    private InterestClassDao interestClassDao = new InterestClassDao();
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

    public void transferClasses(Map<Integer,ObjectId> schoolMap,Map<Integer,ObjectId> gradeMap,Map<Integer,ObjectId> userMap){
        clsInfoList =  getRefactorMapper().getClassInfo();
        System.out.println("classes size:"+clsInfoList.size());
        List<RefUserClassesInfo> students= getRefactorMapper().getRefUserClassesInfo();
        List<RefUserClassesInfo> masterList= getRefactorMapper().getRefClassMasterInfo();
        List<RefUserClassesInfo> teachers = getRefactorMapper().getRefTeacherClass();

        for(ClassesInfo classesInfo  :clsInfoList){
            int classId=classesInfo.getId();
            List<ObjectId> teacherIds=new ArrayList<ObjectId>();
            List<ObjectId> studentIds=new ArrayList<ObjectId>();
            getUserIds(classId, studentIds, students, userMap);

            classesInfo.setMasterId(getMasterId(classId,masterList));
            getUserIds(classId,teacherIds,teachers,userMap);
            //teacher

            classesInfo.setStudentId(studentIds);
            classesInfo.setTeacherId(teacherIds);
            dealClasses(classesInfo,schoolMap,gradeMap);
            //System.out.println(classesInfo.toString());
        }

    }

    public void transferInterestClass(){

        List<InterestClassInfo> interestClassInfos = getRefactorMapper().getIntrestClassInfo();
        List<RefUserClassesInfo> students= getRefactorMapper().getRefUserClassesInfo();

        for(RefUserClassesInfo refUserClassesInfo:students){
            if(TransferUser.userMap.get(refUserClassesInfo.getUserId())!=null)
            studentTermTypeMap.put(TransferUser.userMap.get(refUserClassesInfo.getUserId()),
                    refUserClassesInfo.getTeamtype());
        }

        for(InterestClassInfo interestClassInfo : interestClassInfos){
            InterestClassEntry interestClassEntry = new InterestClassEntry();

            interestClassEntry.setSchoolId(TransferSchool.schoolMap.get(interestClassInfo.getSchoolid()));
            interestClassEntry.setClassName(interestClassInfo.getClassname());
            interestClassEntry.setClassContent(interestClassInfo.getClasscontent());
            interestClassEntry.setTeacherId(TransferUser.teacherMap.get(interestClassInfo.getTeacherId()));
            interestClassEntry.setStudentCount(interestClassInfo.getActualcount());
            interestClassEntry.setSubjectId(TransferSchool.subjectMap.get(interestClassInfo.getSubjectId()));
            if(interestClassInfo.getOpentime()!=null){
                interestClassEntry.setOpenTime(interestClassInfo.getOpentime().getTime());
            }
            if(interestClassInfo.getClosetime()!=null){
                interestClassEntry.setCloseTime(interestClassInfo.getClosetime().getTime());
            }
            interestClassEntry.setFirstTerm(interestClassInfo.getFirstteam());
            interestClassEntry.setSecondTerm(interestClassInfo.getSecondteam());

            if(interestClassInfo.getGradeArry()!=null){
                String[] gradeStrList = interestClassInfo.getGradeArry().split(",");
                List<ObjectId> gradeIdList = new ArrayList<ObjectId>();
                for (String gradeStr  :gradeStrList){
                    gradeIdList.add(TransferSchool.gradeMap.get(Integer.parseInt(gradeStr)));
                }
                interestClassEntry.setGradeIds(gradeIdList);
            }
            interestClassEntry.setIsLongCourse(interestClassInfo.getCoursetype()==1?1:0);
            interestClassEntry.setState(interestClassInfo.getIsopen());
            interestClassEntry.setTermType(interestClassInfo.getTermType());
            interestClassEntry.setTotalCount(interestClassInfo.getStudentcount());

            List<ObjectId> studentsId = new ArrayList<ObjectId>();
            getUserIds(interestClassInfo.getId(), studentsId,students,TransferUser.userMap);
            List<InterestClassStudent> studentList=new ArrayList<InterestClassStudent>();
            for(ObjectId objectId:studentsId){
                InterestClassStudent interestClassStudent =new InterestClassStudent();
                interestClassStudent.setStudentId(objectId);
                if(interestClassInfo.getCoursetype() == 1){
                    interestClassStudent.setCourseType(0);
                }
                else{
                    int studentTermType = studentTermTypeMap.get(objectId);
                    interestClassStudent.setCourseType(studentTermType);
                }
                studentList.add(interestClassStudent);
            }
            interestClassEntry.setInterestClassStudents(studentList);


            interestClassDao.addExpandClass(interestClassEntry);

            interestClassMap.put(interestClassInfo.getId(),
                    interestClassEntry.getID());
            interestClassNameMap.put(interestClassEntry.getID(),
                    interestClassEntry.getClassName());

        }


        //transfer interest class other info


        List<InterestClassLessonScoreInfo> interestClassLessonScoreInfoList =
                getRefactorMapper().getInterestClassScoreInfo();


        for(InterestClassLessonScoreInfo interestClassLessonScoreInfo:interestClassLessonScoreInfoList){
            InterestClassLessonScoreEntry interestClassLessonScoreEntry =
                    new InterestClassLessonScoreEntry();
            interestClassLessonScoreEntry.setClassId(
                    interestClassMap.get(interestClassLessonScoreInfo.getClassId())
            );
            interestClassLessonScoreEntry.setUserId(
                    TransferUser.userMap.get(interestClassLessonScoreInfo.getUserId())
            );
            interestClassLessonScoreEntry.setLessonIndex(interestClassLessonScoreInfo.getLessonIndex());
            interestClassLessonScoreEntry.setStudentScore(interestClassLessonScoreInfo.getStuScore());

            interestClassDao.saveLessonScore(interestClassLessonScoreEntry);
        }

        List<InterestClassTranscriptInfo> interestClassTranscriptInfoList =
                getRefactorMapper().getInterestClassTranscriptInfo();

        for(InterestClassTranscriptInfo interestClassTranscriptInfo:interestClassTranscriptInfoList){
            InterestClassTranscriptEntry interestClassTranscriptEntry = new InterestClassTranscriptEntry();
            interestClassTranscriptEntry.setUserId(
                    TransferUser.userMap.get(interestClassTranscriptInfo.getUserId())
            );
            interestClassTranscriptEntry.setClassId(
                    interestClassMap.get(interestClassTranscriptInfo.getClassId())
            );
            interestClassTranscriptEntry.setFinalResult(interestClassTranscriptInfo.getFinalResult());
            interestClassTranscriptEntry.setResultPictureUrl(interestClassTranscriptInfo.getResultsPicSrc());//todo picture
            interestClassTranscriptEntry.setTeacherComment(interestClassTranscriptInfo.getTeacherComments());
            interestClassTranscriptEntry.setTotalLessonScore(interestClassTranscriptInfo.getSemesterScore());

            interestClassDao.saveTranscript(interestClassTranscriptEntry);
        }


    }

    private ObjectId getMasterId(int classId,List<RefUserClassesInfo> refUsers){
        for(RefUserClassesInfo ref:refUsers){
            int refClassId=ref.getClassId();
            if(classId==refClassId){

                ObjectId userId=TransferUser.userMap.get(ref.getUserId());
                if(userId!=null){
                    return userId;
                }
            }
        }
        return null;
    }



    private void getUserIds(int classId,List<ObjectId> userIds,List<RefUserClassesInfo> refUsers,Map<Integer,ObjectId> userMap){
        for(RefUserClassesInfo ref:refUsers){
            int refClassId=ref.getClassId();
            if(classId==refClassId){
                ObjectId userId=userMap.get(ref.getUserId());
                if(userId!=null){
                    userIds .add(userId);
                }
            }
        }
    }

    private void dealClasses(ClassesInfo classesInfo,Map<Integer,ObjectId> schoolMap,Map<Integer,ObjectId> gradeMap) {
        ClassEntry classEntry = new ClassEntry(
                schoolMap.get(classesInfo.getSchoolId()),
                gradeMap.get(classesInfo.getGradeId()),
                classesInfo.getClassName(),
                classesInfo.getClassDetail(),
                classesInfo.getMasterId(), //master
                classesInfo.getTeacherId(),
                classesInfo.getStudentId(),
                classesInfo.getStudentId().size()
        );
        classDao.addClassEntry(classEntry);
        classMap.put(classesInfo.getId(),classEntry.getID());
        classNameMap.put(classEntry.getID(),classesInfo.getClassName());

        for(ObjectId stuId:classEntry.getStudents()){
            stuClassMap.put(stuId,classEntry);
        }
    }
    public static void main(String[] args) throws Exception{
        TransferClass transfer = new TransferClass();
        transfer.transferInterestClass();
   }
}
