package com.sql.oldDataTransfer;

import com.db.user.UserExperienceLogDao;
import com.sql.dao.RefactorMapper;
import com.sql.oldDataPojo.ExperienceLogDTO;
import com.sql.oldDataPojo.ExperienceLogInfo;
import com.sql.oldDataPojo.UserExperienceLogDTO;
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
public class TransferExperienceLogs {
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

    UserExperienceLogDao userExperienceLogDao=new UserExperienceLogDao();

    public static Map<Integer,ObjectId> userPetsMap=new HashMap<Integer, ObjectId>();

    public static Map<Integer,ObjectId> petInfoMap=new HashMap<Integer, ObjectId>();

    private List<ExperienceLogInfo> experienceLogInfos = null;
    private Map<Integer,UserExperienceLogDTO> petdtoMap=new HashMap<Integer, UserExperienceLogDTO>();

    public void transferUserExperienceLogs(Map<Integer,ObjectId> userMap){
        experienceLogInfos =  getRefactorMapper().getUserScoreLogsInfo();
        for(ExperienceLogInfo experienceLogInfo  :experienceLogInfos){
            UserExperienceLogDTO userExperienceLogDTO=petdtoMap.get(experienceLogInfo.getUserid());

            if(userExperienceLogDTO==null){
                userExperienceLogDTO=new UserExperienceLogDTO();
                String userid=userMap.get(experienceLogInfo.getUserid())==null?null:userMap.get(experienceLogInfo.getUserid()).toString();
                if(userid==null){
                    continue;
                }
                userExperienceLogDTO.setUserid(userid);
                List<ExperienceLogDTO> list=new ArrayList<ExperienceLogDTO>();
                ExperienceLogDTO experienceLogDTO = new ExperienceLogDTO();
                experienceLogDTO.setExperiencename(experienceLogInfo.getExperiencename());
                experienceLogDTO.setExperience(experienceLogInfo.getExperience());
                experienceLogDTO.setCreatetime(experienceLogInfo.getCreatetime());
                experienceLogDTO.setScoreType(experienceLogInfo.getExperiencetype());
                //String relateId=getHandleRelateId(experienceLogInfo.getExperiencetype(),experienceLogInfo.getRelateId()):
                //experienceLogDTO.setRelateId(relateId);
                list.add(experienceLogDTO);
                userExperienceLogDTO.setExperienceLogs(list);
                petdtoMap.put(experienceLogInfo.getUserid(),userExperienceLogDTO);
            }else{
                List<ExperienceLogDTO> list=userExperienceLogDTO.getExperienceLogs();
                ExperienceLogDTO experienceLogDTO = new ExperienceLogDTO();
                experienceLogDTO.setExperiencename(experienceLogInfo.getExperiencename());
                experienceLogDTO.setExperience(experienceLogInfo.getExperience());
                experienceLogDTO.setCreatetime(experienceLogInfo.getCreatetime());
                experienceLogDTO.setScoreType(experienceLogInfo.getExperiencetype());
                //String relateId=getHandleRelateId(experienceLogInfo.getExperiencetype(),experienceLogInfo.getRelateId()):
                //experienceLogDTO.setRelateId(relateId);
                list.add(experienceLogDTO);
                userExperienceLogDTO.setExperienceLogs(list);
                petdtoMap.put(experienceLogInfo.getUserid(),userExperienceLogDTO);
            }

        }

        for (UserExperienceLogDTO userExperienceLogDTO : petdtoMap.values()) {
            dealUserExperienceLog(userExperienceLogDTO);
        }
        System.out.println("UserPetInf size:"+experienceLogInfos.size());

    }

    private void dealUserExperienceLog(UserExperienceLogDTO userExperienceLog) {
        userExperienceLogDao.addUserExperienceLog(userExperienceLog.buildUserExperienceLogEntry());
        /*UserDao userDao=new UserDao();
        if(userExperienceLog.getExperienceLogs()!=null&&userExperienceLog.getExperienceLogs().size()>0) {
            int totalExp=0;
            for (ExperienceLogDTO expLog : userExperienceLog.getExperienceLogs()) {
                totalExp += expLog.getExperience();
            }
            try{
                userDao.update(new ObjectId(userExperienceLog.getUserid()),"exp", totalExp,true);
            }catch (Exception e){
            }
        }*/
    }

     /*public static void main(String[] args) throws Exception{
         TransferExperienceLogs transfer = new TransferExperienceLogs();
         transfer.transferUserExperienceLogs(new HashMap<Integer, ObjectId>());
     }*/
}
