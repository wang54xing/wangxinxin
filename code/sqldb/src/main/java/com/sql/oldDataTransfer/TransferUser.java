package com.sql.oldDataTransfer;

import com.db.user.UserDao;
import com.pojo.app.FieldValuePair;
import com.pojo.app.IdValuePair;
import com.pojo.user.UserEntry;
import com.pojo.user.UserRole;
import com.sql.dao.RefactorMapper;
import com.sql.oldDataPojo.RefParentStudent;
import com.sql.oldDataPojo.TeacherInfo;
import com.sql.oldDataPojo.UserInfo;
import com.sys.constants.Constant;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.bson.types.ObjectId;

import java.io.*;
import java.util.*;

/**
 * 迁移用户数据
 * Created by qinbo on 15/3/20.
 */
public class TransferUser {



    public static Map<Integer,ObjectId> userMap = new HashMap<Integer, ObjectId>();
    private static Map<Integer,String> userAvatarmap = new HashMap<Integer, String>();
    private static Map<Integer,String> userNewAvatarmap = new HashMap<Integer, String>();

    //public static Map<ObjectId,String> userNameMap = new HashMap<ObjectId, String>();

    public static Map<Integer,UserRole> roleMap=new HashMap<Integer, UserRole>();

    private List<Integer> headmasterwithclassList = null;

    
    static{
        roleMap.put(0,UserRole.STUDENT);//学生
        roleMap.put(1,UserRole.TEACHER);//老师
        roleMap.put(2,UserRole.HEADMASTER);//校长
        roleMap.put(3,UserRole.EDUCATION);//教育局
        roleMap.put(4,UserRole.PARENT);//家长
        roleMap.put(5,UserRole.LEADER_CLASS);//班主任
        roleMap.put(6,UserRole.LEADER_OF_SUBJECT);//学科组长
        roleMap.put(7,UserRole.ADMIN);//管理员
        roleMap.put(8,UserRole.ADMIN);//管理员
        roleMap.put(9,UserRole.K6KT_HELPER);//K6KT小助手
        roleMap.put(10,UserRole.LEADER_OF_GRADE);//年级组长
    }

    public static Map<Integer,ObjectId> teacherMap = new HashMap<Integer, ObjectId>();
    public static UserEntry unkownUser;
    private UserDao userDao = new UserDao();

    private List<UserInfo> userInfoList = null;

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
    private List<Integer> gradeMasterList =new ArrayList<Integer>();

    public void transfer() throws Exception{
        loadUserAvatarFromDisk();
        SqlSession sqlSession = getSessionFactory().openSession();
        RefactorMapper refactorMapper = sqlSession.getMapper(RefactorMapper.class);

        gradeMasterList =refactorMapper.getGradeMasterList();

        headmasterwithclassList = refactorMapper.selHeadmasterWithClass();




        //用户数不超1，000，000，每次1000条数据,
        int offset= 0;
        int length = 1000;
        int loop = 1000;
        for (int i = 0;i<loop;i++){
            userInfoList =  refactorMapper.selUserInfoByUserId(offset,length);
            offset += length;
            if(userInfoList.isEmpty()){
                break;
            }
            for (UserInfo userInfo : userInfoList){

                dealUser(userInfo,refactorMapper);
            }

        }

        //添加一个已删除用户-未知用户
        unkownUser = new UserEntry(
                "未知用户",
                "1",
                0,
                new ArrayList<IdValuePair>()
        );
        unkownUser.setSchoolID( TransferSchool.schoolMap.get(460));

        unkownUser.setAvatar("head-" + "default-head.jpg");
        userDao.addUserEntry(unkownUser);

        sqlSession.close();


        transferParent();

        transferTeacher();

        saveUserAvatarToDisk();
    }


    private void dealUser(UserInfo userInfo, RefactorMapper refactorMapper) throws Exception{
//        List<UserRole> urList=new ArrayList<UserRole>();
//
//
//        int index=0;
//        for(Integer uid: gradeMasterList){
//            if(uid.equals(userInfo.getId())){
//                urList.add(roleMap.get(10));
//                gradeMasterList.remove(index);
//                break;
//            }
//            index++;
//        }
//
//
//
//        Integer role=userInfo.getRole();
//
//        Integer isAdmin=userInfo.getIsmanage();
//
//        if(role==8||isAdmin==1)
//        {
//            urList.add(roleMap.get(8));
//        }
//        if(role == 2 && headmasterwithclassList.contains(userInfo.getId())){
//            urList.add(roleMap.get(1));
//        }
//
//        urList.add(roleMap.get(role));
//
//
//        String permission ="";
//        String removePermission ="";
//        ObjectId schoolId = TransferSchool.schoolMap.get(userInfo.getSchoolID());
//
//
//        ObjectId connectId = null;
//
//
//        String avatar = null;
//
//
//        long registerTime = 0;
//        if(userInfo.getRegisterTime()!=null){
//            registerTime = userInfo.getRegisterTime().getTime();
//        }
//
//        long birthDate = 0;
//        if(userInfo.getBirthDate()!=null){
//            //birthDate = userInfo.getBirthDate()
//        }
//
//        long lastActiveDate = 0;
//        if(userInfo.getLastActiveDate()!=null){
//            lastActiveDate = userInfo.getLastActiveDate().getTime();
//        }
//
//        String jobnumber = null;
////        UserEntry userEntry = new UserEntry(
////                userInfo.getUserName(),
////                userInfo.getPassword(),
////                userInfo.getSex(),
////                userInfo.getNickName(),
////                UserRole.getRole(new HashSet<UserRole>(urList)),
////                permission,
////                removePermission,
////                userInfo.getMobileNumber(),
////                userInfo.getPhoneNumber(),
////                userInfo.getEmail(),
////                userInfo.getQQ(),
////                Constant.EMPTY, // weixin
////                Constant.EMPTY, //weibo
////                userInfo.getAddress(),
////                userInfo.getPostCode(),
////                userInfo.getBloodType(),
////                registerTime,
////                Constant.EMPTY, // registerip
////                schoolId,
////                birthDate, //birthday
////                lastActiveDate,
////                userInfo.getExperiencevalue(),
////                userInfo.getStudentid(),
////                userInfo.getStudentNum(),
////                userInfo.getProfile(),
////                jobnumber,
////                Constant.SYN_YES_NEED,
////                connectId,
////                avatar,
////                new ArrayList<IdValuePair>()
////
////
////        );
//        userEntry.setChatId(userInfo.getId()+"");
//
//        transferHeadIcon(userEntry, userInfo);
//
//        ObjectId id = userDao.addUserEntry(userEntry);
//
//        refactorMapper.insertUserBalance(userInfo.getUserName(),userInfo.getPassword(),userInfo.getBalance(),id.toString());
//
//        userMap.put(userInfo.getId(),userEntry.getID());


        //userNameMap.put(userEntry.getID(),userEntry.getUserName());
    }

    private void loadUserAvatarFromDisk() throws Exception{
        BufferedReader bufferedReaderAvatarMap = new BufferedReader(new FileReader("/Users/qinbo/k6kt-temp/avatarmap.txt"));
        while(bufferedReaderAvatarMap.ready()){
            String line = bufferedReaderAvatarMap.readLine();

            if(!line.isEmpty()&&line!=null) {
                String[] linedata = line.split(",");
                userAvatarmap.put(Integer.parseInt(linedata[0]), linedata[1]);
            }

        }
        bufferedReaderAvatarMap.close();

        bufferedReaderAvatarMap = new BufferedReader(new FileReader("/Users/qinbo/k6kt-temp/newavatarmap.txt"));
        while(bufferedReaderAvatarMap.ready()){
            String line = bufferedReaderAvatarMap.readLine();

            if(!line.isEmpty()&&line!=null) {
                String[] linedata = line.split(",");

                userNewAvatarmap.put(Integer.parseInt(linedata[0]), linedata[1]);
            }

        }
        bufferedReaderAvatarMap.close();




    }
    private void saveUserAvatarToDisk() throws Exception{
        BufferedWriter bufferedWriterrAvatarMap = new BufferedWriter(new FileWriter("/Users/qinbo/k6kt-temp/avatarmap.txt",false));
        for(Integer key:userAvatarmap.keySet()){
            String line = key+","+userAvatarmap.get(key);
            bufferedWriterrAvatarMap.write(line);
            bufferedWriterrAvatarMap.newLine();


        }
        bufferedWriterrAvatarMap.close();

        bufferedWriterrAvatarMap = new BufferedWriter(new FileWriter("/Users/qinbo/k6kt-temp/newavatarmap.txt",false));
        for(Integer key:userNewAvatarmap.keySet()){
            String line = key+","+userNewAvatarmap.get(key);
            bufferedWriterrAvatarMap.write(line);
            bufferedWriterrAvatarMap.newLine();


        }
        bufferedWriterrAvatarMap.close();
    }

    private void transferParent(){
        SqlSession sqlSession = getSessionFactory().openSession();
        RefactorMapper refactorMapper = sqlSession.getMapper(RefactorMapper.class);
        List<RefParentStudent> parentStudentsList =refactorMapper.getRefParentStudent();


        sqlSession.close();
        for(RefParentStudent refParentStudent:parentStudentsList){
            if(userMap.containsKey(refParentStudent.getParentId()) &&
                    userMap.containsKey(refParentStudent.getStudentId())){
                ObjectId parentId = userMap.get(refParentStudent.getParentId());
                ObjectId studentId = userMap.get(refParentStudent.getStudentId());

                FieldValuePair fvpCid = new FieldValuePair("cid",parentId);
                userDao.update(studentId,fvpCid);
                fvpCid = new FieldValuePair("cid",studentId);
                userDao.update(parentId,fvpCid);
                //userDao.update(userMap.get());
            }
        }


    }

    private void transferTeacher(){
        SqlSession sqlSession = getSessionFactory().openSession();
        RefactorMapper refactorMapper = sqlSession.getMapper(RefactorMapper.class);

        List<TeacherInfo> teacherInfoList = refactorMapper.selTeacher();
        for(TeacherInfo teacherInfo:teacherInfoList){

            teacherMap.put(teacherInfo.getId(),userMap.get(teacherInfo.getUserid()));
        }

        sqlSession.close();
    }

    private void transferHeadIcon(UserEntry userEntry,UserInfo userInfo) throws Exception{
        String headPrefix =  "head-";



        if(userInfo.getMaxImageURL() == null ||
                userInfo.getMaxImageURL() .contains("fc_student.jpg")){

            userEntry.setAvatar(headPrefix + "default-head.jpg");
        }
        else
        {
            String fileExt = userInfo.getMaxImageURL().substring(userInfo.getMaxImageURL().lastIndexOf("."));

            //todo : 文件上传
            String avatarStr = headPrefix+new ObjectId().toString()+fileExt;

            if((!userAvatarmap.containsKey(userInfo.getId())) ||
                    (!userAvatarmap.get(userInfo.getId()).equals(userInfo.getMaxImageURL()))){
                FileUtil.downloadFromUrl(userInfo.getMaxImageURL(),avatarStr);
                String localFileMin =  "/Users/qinbo/k6kt-temp/"+avatarStr;
                //here
                FileUtil.uploadToQiNiu(localFileMin,"k6kt-doc",avatarStr);

                userEntry.setAvatar(avatarStr);

                userAvatarmap.put(userInfo.getId(),userInfo.getMaxImageURL());
                userNewAvatarmap.put(userInfo.getId(),avatarStr);

            }
            else{
                userEntry.setAvatar(userNewAvatarmap.get(userInfo.getId()));
            }


            //FileUtil.downloadFromUrl(userInfo.getMaxImageURL(),"max-"+avatarStr);
            //FileUtil.downloadFromUrl(userInfo.getMiddleImageURL(),"mid-"+avatarStr);
            //FileUtil.downloadFromUrl(userInfo.getMinImageURL(),"min-"+avatarStr);
            //String localFileMin =  "/Users/qinbo/k6kt-image/min-"+avatarStr;
            //String localFileMid =  "/Users/qinbo/k6kt-image/mid-"+avatarStr;
            //String localFileMax =  "/Users/qinbo/k6kt-image/max-"+avatarStr;
            //FileUtil.uploadToQiNiu(localFileMin,"min-"+avatarStr);
            //FileUtil.uploadToQiNiu(localFileMid,"mid-"+avatarStr);
            //FileUtil.uploadToQiNiu(localFileMax,"max-"+avatarStr);

        }



    }


}
