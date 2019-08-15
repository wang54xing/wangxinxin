package com.sql.oldDataTransfer;

import com.db.school.LeagueDao;
import com.pojo.app.IdValuePair;
import com.pojo.school.LeagueEnrty;
import com.sql.dao.RefactorMapper;
import com.sql.oldDataPojo.SchoolLeagueInfo;
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
 * Created by qinbo on 15/4/13.
 */
public class TransferLeague {

    private LeagueDao leagueDao = new LeagueDao();
    public static Map<Integer ,ObjectId> leagueMap = new HashMap<Integer ,ObjectId>();
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

    //todo ： school name
    public void transfer(){
        List<SchoolLeagueInfo> schoolLeagueInfoList = getRefactorMapper().getSchoolLeagueInfo();


        int curLeague = -1;
        List<IdValuePair> sidList = null;
        LeagueEnrty leagueEnrty = null;

        for(SchoolLeagueInfo schoolLeagueInfo:schoolLeagueInfoList){
            if(schoolLeagueInfo.getLeagueId()!=curLeague){
                if(curLeague>0){

                    leagueEnrty.setSchools(sidList);
                    leagueDao.add(leagueEnrty);
                    leagueMap.put(curLeague,leagueEnrty.getID());
                }
                leagueEnrty = new LeagueEnrty(schoolLeagueInfo.getName(),
                        TransferUser.userMap.get(schoolLeagueInfo.getUserId()),
                        null);
                sidList = new ArrayList<IdValuePair>();
                curLeague = schoolLeagueInfo.getLeagueId();
            }
            if(TransferSchool.schoolMap.get(schoolLeagueInfo.getSchoolId())!=null) {
                sidList.add(new IdValuePair(TransferSchool.schoolMap.get(schoolLeagueInfo.getSchoolId()),
                        TransferSchool.schoolNameMap.get(schoolLeagueInfo.getSchoolId())));
            }

        }
        leagueEnrty.setSchools(sidList);
        leagueDao.add(leagueEnrty);

        leagueMap.put(curLeague,leagueEnrty.getID());

    }
}
