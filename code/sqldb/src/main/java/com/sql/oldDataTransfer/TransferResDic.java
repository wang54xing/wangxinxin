package com.sql.oldDataTransfer;

import com.db.resources.ResourceDictionaryDao;
import com.pojo.app.IdValuePair;
import com.pojo.resources.ResourceDictionaryEntry;
import com.sql.dao.RefactorMapper;
import com.sql.oldDataPojo.ResourceKPDicInfo;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.bson.types.ObjectId;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by qinbo on 15/11/11.
 */
public class TransferResDic {

    private Map<String,ResourceDictionaryEntry> resourceDicInfoMap = new HashMap<String, ResourceDictionaryEntry>();

    private Map<ObjectId,ResourceDictionaryEntry> resourceDictionaryEntryMap = new HashMap<ObjectId, ResourceDictionaryEntry>();



    private List<ResourceKPDicInfo> resourceKPDicInfoList;
    private List<ResourceKPDicInfo> resourceNodeInfoList;


    private Map<String,ResourceKPDicInfo> resourceNodeInfoMap = new HashMap<String, ResourceKPDicInfo>();
    private Map<String,ResourceKPDicInfo> resourceKPDicInfoMap = new HashMap<String, ResourceKPDicInfo>();


    private ResourceDictionaryDao resourceDictionaryDao = new ResourceDictionaryDao();


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
    public void transfer()  throws  IOException{

        SqlSession sqlSession = getSessionFactory().openSession();
        RefactorMapper refactorMapper = sqlSession.getMapper(RefactorMapper.class);


        resourceKPDicInfoList = refactorMapper.getResouceDicInfo();

        resourceNodeInfoList = refactorMapper.getResouceDicInfo2();


        sqlSession.close();

        for(ResourceKPDicInfo rinfo:resourceNodeInfoList){
            resourceNodeInfoMap.put(rinfo.getCode(),rinfo);
        }
        for(ResourceKPDicInfo rinfo:resourceKPDicInfoList){
            resourceKPDicInfoMap.put(rinfo.getCode(),rinfo);
        }


        List<ResourceDictionaryEntry> existDicList = null;

        for(ResourceDictionaryEntry rentry : existDicList){
            if(rentry.getSort()!=0 && rentry.getType()!=7 && rentry.getType()!=8 && rentry.getType()!=9){
                resourceDicInfoMap.put(rentry.getSort()+"",rentry);
                resourceDictionaryEntryMap.put(rentry.getID(),rentry);

            }
        }

        InputStreamReader iReader = new InputStreamReader(new FileInputStream("/Users/qinbo/mulu.txt"));

        BufferedReader bReader = new BufferedReader(iReader);

        List<String> muluList = new ArrayList<String>();
        String str ="";
        while ((str = bReader.readLine()) != null) {

            if(str.contains("|")){
                String[] sList = str.split("\\|");
                for(String sstr:sList){
                    muluList.add(sstr);
                }
            }
            else{

                muluList.add(str);
            }

        }

        bReader.close();
        for(String mulustr:muluList){
            if(resourceDicInfoMap.containsKey(mulustr)) {
                System.out.println("exist:"+mulustr);
            }
            else{
                System.out.println("not exist:"+mulustr);
                if(resourceNodeInfoMap.containsKey(mulustr)){
                    ResourceKPDicInfo nodeInfo = resourceNodeInfoMap.get(mulustr);
                    if(mulustr.length()!=15){

                        System.out.println("length not 15:"+mulustr);
                    }
                    ResourceDictionaryEntry jiaocaiEntry = null;
                    //教材版本6位,已经存在
                    if(resourceDicInfoMap.containsKey(nodeInfo.getCode().substring(0,6))){
                        //System.out.println("jiaocai exist :"+mulustr.substring(0,6));
                        jiaocaiEntry = resourceDicInfoMap.get(mulustr.substring(0,6));
                    }
                    else{//教材版本不存在
                        //System.out.println("jiaocai not exist :"+mulustr.substring(0,6));
                        ResourceDictionaryEntry rex = resourceDicInfoMap.get(mulustr.substring(0,3));
                        List<IdValuePair> pnlist = new ArrayList<IdValuePair>();
                        pnlist.addAll(rex.getParentInfos());
                        pnlist.add(new IdValuePair(rex.getID(),rex.getName()));

                        ResourceKPDicInfo jiaocaiinfo = resourceNodeInfoMap.get(mulustr.substring(0,6));
                        jiaocaiEntry = new ResourceDictionaryEntry(3,
                                jiaocaiinfo.getKlpoint(),rex.getID(),pnlist);

                        jiaocaiEntry.setSort(Long.valueOf(jiaocaiinfo.getCode()));

                        jiaocaiEntry.setID(new ObjectId());
                        resourceDictionaryDao.addResourceDictionaryEntry(jiaocaiEntry);
                        resourceDictionaryEntryMap.put(jiaocaiEntry.getID(),jiaocaiEntry);
                        resourceDicInfoMap.put(jiaocaiEntry.getSort()+"",jiaocaiEntry);
                    }

                    ResourceDictionaryEntry gradeEntry = null;
                    //年级9位,已经存在
                    if(resourceDicInfoMap.containsKey(nodeInfo.getCode().substring(0,9))&&
                            resourceDicInfoMap.get(nodeInfo.getCode().substring(0,9)).getType()==4){
                        //System.out.println("grade  exist :"+mulustr.substring(0,9));
                        gradeEntry = resourceDicInfoMap.get(mulustr.substring(0,9));
                    }
                    else{//年级不存在
                        //System.out.println("grade not exist :"+mulustr.substring(0,9));


                        ResourceDictionaryEntry rex = resourceDicInfoMap.get(mulustr.substring(0,6));
                        List<IdValuePair> pnlist = new ArrayList<IdValuePair>();
                        pnlist.addAll(rex.getParentInfos());
                        pnlist.add(new IdValuePair(rex.getID(),rex.getName()));

                        ResourceKPDicInfo gradeinfo = resourceNodeInfoMap.get(mulustr.substring(0,9));
                        gradeEntry = new ResourceDictionaryEntry(4,
                                gradeinfo.getKlpoint(),rex.getID(),pnlist);

                        gradeEntry.setSort(Long.valueOf(gradeinfo.getCode()));

                        gradeEntry.setID(new ObjectId());

                        resourceDictionaryDao.addResourceDictionaryEntry(gradeEntry);
                        resourceDictionaryEntryMap.put(gradeEntry.getID(),gradeEntry);
                        resourceDicInfoMap.put(gradeEntry.getSort()+"",gradeEntry);

                    }
                    ResourceDictionaryEntry danyuanEntry = null;
                    //单元12位,已经存在
                    if(resourceDicInfoMap.containsKey(nodeInfo.getCode().substring(0,12))&&
                            resourceDicInfoMap.get(nodeInfo.getCode().substring(0,12)).getType()==5){
                        //System.out.println("danyuan  exist :"+mulustr.substring(0,12));
                        danyuanEntry = resourceDicInfoMap.get(mulustr.substring(0,12));
                    }
                    else{//单元不存在
                        //System.out.println("danyuan not  exist :"+mulustr.substring(0,12));


                        ResourceDictionaryEntry rex = resourceDicInfoMap.get(mulustr.substring(0,9));
                        List<IdValuePair> pnlist = new ArrayList<IdValuePair>();
                        pnlist.addAll(rex.getParentInfos());
                        pnlist.add(new IdValuePair(rex.getID(),rex.getName()));

                        ResourceKPDicInfo danyuaninfo = resourceNodeInfoMap.get(mulustr.substring(0,12));
                        danyuanEntry = new ResourceDictionaryEntry(5,
                                danyuaninfo.getKlpoint(),rex.getID(),pnlist);

                        danyuanEntry.setSort(Long.valueOf(danyuaninfo.getCode()));

                        danyuanEntry.setID(new ObjectId());

                        resourceDictionaryDao.addResourceDictionaryEntry(danyuanEntry);
                        resourceDictionaryEntryMap.put(danyuanEntry.getID(),danyuanEntry);
                        resourceDicInfoMap.put(danyuanEntry.getSort()+"",danyuanEntry);
                    }

                    //课15位不存在
                    ResourceDictionaryEntry keEntry = null;


                    ResourceDictionaryEntry rex = resourceDicInfoMap.get(mulustr.substring(0,12));
                    List<IdValuePair> pnlist = new ArrayList<IdValuePair>();
                    pnlist.addAll(rex.getParentInfos());
                    pnlist.add(new IdValuePair(rex.getID(),rex.getName()));

                    ResourceKPDicInfo keinfo = resourceNodeInfoMap.get(mulustr.substring(0,15));
                    keEntry = new ResourceDictionaryEntry(6,
                            keinfo.getKlpoint(),rex.getID(),pnlist);

                    keEntry.setSort(Long.valueOf(keinfo.getCode()));

                    keEntry.setID(new ObjectId());

                    resourceDictionaryDao.addResourceDictionaryEntry(keEntry);
                    resourceDictionaryEntryMap.put(keEntry.getID(),keEntry);
                    resourceDicInfoMap.put(keEntry.getSort()+"",keEntry);


                    //deal find resource


                }
                else{
                    //System.out.println("notfind:"+mulustr);
                }
            }
        }

    }

    public static void main(String[] args) throws IOException{
        TransferResDic transferResDic = new TransferResDic();
        transferResDic.transfer();
    }
}
