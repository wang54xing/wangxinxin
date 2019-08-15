package com.sql.oldDataTransfer;

import com.db.app.RegionDao;
import com.db.school.SchoolDao;
import com.pojo.app.RegionEntry;
import com.pojo.school.Grade;
import com.pojo.school.SchoolEntry;
import com.pojo.school.SchoolType;
import com.pojo.school.Subject;
import com.pojo.utils.MongoUtils;
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
 * 迁移学校数据，一起迁移的数据包括
 * 学校
 * 年级
 * 学科
 * 地区
 * Created by qinbo on 15/3/20.
 */
public class TransferSchool {


    private RegionDao regionDao = new RegionDao();
    private SchoolDao schoolDao = new SchoolDao();

    //geoid , region objectid
    public static Map<Integer,ObjectId> regionMap = new HashMap<Integer,ObjectId>();
    public static Map<Integer,ObjectId> schoolMap = new HashMap<Integer, ObjectId>();
    public static Map<Integer,String> schoolNameMap = new HashMap<Integer, String>();
    public static Map<Integer,ObjectId> gradeMap = new HashMap<Integer, ObjectId>();
    public static Map<Integer,ObjectId> subjectMap = new HashMap<Integer, ObjectId>();
    public static Map<ObjectId,String> subjectNameMap = new HashMap<ObjectId, String>();


    private List<GeoInfo> geoInfoList= null;
    private List<SchoolInfo> schInfoList = null;

    private List<RegionEntry> regionEntryList = new ArrayList<RegionEntry>();

    private List<GradeInfo> gradeInfoList = null;
    private List<SubjectInfo> subjectInfoList = null;

    private static Map<Integer,Integer> gradeLevelMap = new HashMap<Integer, Integer>();

    static{
        gradeLevelMap.put(11,1);
        gradeLevelMap.put(12,2);
        gradeLevelMap.put(13,3);
        gradeLevelMap.put(14,4);
        gradeLevelMap.put(15,5);
        gradeLevelMap.put(16,6);
        gradeLevelMap.put(21,7);
        gradeLevelMap.put(22,8);
        gradeLevelMap.put(23,9);
        gradeLevelMap.put(31,10);
        gradeLevelMap.put(32,11);
        gradeLevelMap.put(33,12);
        gradeLevelMap.put(34,13);
    }

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



    public void transferSchool(){
        SqlSession sqlSession = getSessionFactory().openSession();
        RefactorMapper refactorMapper = sqlSession.getMapper(RefactorMapper.class);


        schInfoList =  refactorMapper.selSchool();



        for(SchoolInfo schoolInfo  :schInfoList){
            dealSchool(schoolInfo);
        }


        System.out.println("school size:"+schInfoList.size());

    }

    public void transferSubject(){
        SqlSession sqlSession = getSessionFactory().openSession();
        RefactorMapper refactorMapper = sqlSession.getMapper(RefactorMapper.class);

        subjectInfoList  = refactorMapper.selSubject();

        System.out.println("subject size:"+subjectInfoList.size());
        int curSchoolId = -1;
        int curSubjectId = -1;
        List<Subject> subjectList = null;
        Subject currentSubject = null;
        for(SubjectInfo subjectInfo:subjectInfoList){
            if(curSchoolId!= subjectInfo.getSchoolId()){
                if(curSchoolId>0){
                    schoolDao.update(schoolMap.get(curSchoolId),"subs",MongoUtils.fetchDBObjectList(subjectList));
                }
                subjectList = new ArrayList<Subject>();
                curSchoolId = subjectInfo.getSchoolId();
                //curSubjectId = subjectInfo.getGradeId();
            }

            if(curSubjectId != subjectInfo.getSubjectId() ){



                curSubjectId = subjectInfo.getSubjectId();

                currentSubject = new Subject(
                        subjectInfo.getName(),
                        gradeMap.get(subjectInfo.getGradeId())
                );
                subjectList.add(currentSubject);

                subjectMap.put(subjectInfo.getSubjectId(),currentSubject.getSubjectId());

                subjectNameMap.put(currentSubject.getSubjectId(),currentSubject.getName());
            }
            else
            {
                List<ObjectId> gradeIdList = currentSubject.getGradeIds();
                gradeIdList.add(gradeMap.get(subjectInfo.getGradeId()));
                currentSubject.setGradeIds(gradeIdList);
            }





        }
        schoolDao.update(schoolMap.get(curSchoolId),"subs",MongoUtils.fetchDBObjectList(subjectList));



    }
    public void transferGrade() throws Exception{
        SqlSession sqlSession = getSessionFactory().openSession();
        RefactorMapper refactorMapper = sqlSession.getMapper(RefactorMapper.class);


        gradeInfoList =  refactorMapper.selGrade();


        System.out.println("grade size:"+gradeInfoList.size());


        int curSchoolId = -1;
        List<Grade> gradeList = null;
        for(GradeInfo gradeInfo:gradeInfoList){
            if(curSchoolId!= gradeInfo.getSchoolId()){
                if(curSchoolId>0){
                    schoolDao.update(schoolMap.get(curSchoolId),"grs",MongoUtils.fetchDBObjectList(gradeList));
                }
                gradeList = new ArrayList<Grade>();
                curSchoolId = gradeInfo.getSchoolId();
            }

            if(gradeLevelMap.get(gradeInfo.getTypeId())!=null) {
                Grade grade = new Grade(
                        gradeInfo.getGradeName(),
                        gradeLevelMap.get(gradeInfo.getTypeId()),
                        TransferUser.userMap.get(gradeInfo.getUserid()),new ObjectId()
                );
                gradeList.add(grade);

                gradeMap.put(gradeInfo.getId(), grade.getGradeId());
            }

        }
        schoolDao.update(schoolMap.get(curSchoolId),"grs",MongoUtils.fetchDBObjectList(gradeList));


    }

    public  void transferRegion(){
        SqlSession sqlSession = getSessionFactory().openSession();
        RefactorMapper refactorMapper = sqlSession.getMapper(RefactorMapper.class);



        int level = 1;
        RegionEntry region1 = new RegionEntry(
                1,
                null,
                "中国",
                0
        );

        regionEntryList.add(region1);
        regionDao.addRegionEntry(region1);


        Map<Integer, String> provinceMap = ProvinceEnum.getMap();
        for(Integer provinceId :provinceMap.keySet()){
            RegionEntry regionPro = new RegionEntry(
                    2,
                    region1.getID(),
                    provinceMap.get(provinceId),
                    0
            );
            regionEntryList.add(regionPro);
            regionDao.addRegionEntry(regionPro);
            regionMap.put(provinceId,regionPro.getID());
        }


        geoInfoList=  refactorMapper.selGeo();

    }

    private GeoInfo findGeoInfoByGeoId(int geoid){
        for(GeoInfo geoInfo:geoInfoList){
            if(geoInfo.getId() == geoid)
            {
                return geoInfo;
            }
        }
        return null;
    }




    private void dealSchool(SchoolInfo schoolInfo){

        int schoolType = 0;
        if(schoolInfo.getIsPrimary() == 1){
            schoolType = schoolType | SchoolType.PRIMARY.getType();
        }
        if(schoolInfo.getIsMiddle() == 1){
            schoolType = schoolType | SchoolType.JUNIOR.getType();
        }
        if(schoolInfo.getIsHigh() == 1){
            schoolType = schoolType| SchoolType.SENIOR.getType();
        }

        ObjectId regionId = null;


        //
        GeoInfo geoInfo = findGeoInfoByGeoId(schoolInfo.getGeoId());

        if(regionMap.get(geoInfo.getId())!=null)
        {
            regionId = regionMap.get(geoInfo.getId());
        }
        else
        {

            RegionEntry regionEntry = new RegionEntry(
                    3,
                    regionMap.get(geoInfo.getProvinceid()),
                    geoInfo.getCity(),
                    0

            );
            regionEntryList.add(regionEntry);

            regionDao.addRegionEntry(regionEntry);
            regionMap.put(geoInfo.getId(),regionEntry.getID());
            regionId = regionEntry.getID();

        }





        SchoolEntry schoolEntry = new SchoolEntry(
                schoolType,
                schoolInfo.getName(),
                schoolInfo.getEnglishName(),
                schoolInfo.getBrief(),
                schoolInfo.getDetail(),
                schoolInfo.getDomain(),
                schoolInfo.getPostCode(),
                schoolInfo.getPhone(),
                0,//sort
                schoolInfo.getInitialPassword(),
                regionId,
                schoolInfo.getAddress(),
                null
        );
        schoolEntry.setLogo("/img/logofz"+schoolInfo.getId()+".png");

        schoolDao.addSchoolEntry(schoolEntry);


        schoolMap.put(schoolInfo.getId(),schoolEntry.getID());
        schoolNameMap.put(schoolInfo.getId(),schoolInfo.getName());

    }

}
