package com.sql.oldDataTransfer;

import com.db.itempool.ItemPoolDao;
import com.db.resources.ResourceDictionaryDao;
import com.pojo.app.IdValuePair;
import com.pojo.exercise.ExerciseItemType;
import com.pojo.itempool.*;
import com.pojo.resources.ResourceDictionaryEntry;
import com.pojo.school.GradeType;
import com.sql.dao.RefactorMapper;
import com.sql.oldDataPojo.ItemPoolInfo;
import com.sql.oldDataPojo.ResourceKPDicInfo;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.util.*;

public class TransferItemPool1 {

	    private List<ItemPoolInfo> itemPoolInfoList;
	    private List<ResourceKPDicInfo> resourceKPDicInfoList;
	    private List<ResourceKPDicInfo> resourceNodeInfoList;



	    private Map<String,ResourceDictionaryEntry> resourceDicInfoMap = new HashMap<String, ResourceDictionaryEntry>();

	    private Map<ObjectId,ResourceDictionaryEntry> resourceDictionaryEntryMap = new HashMap<ObjectId, ResourceDictionaryEntry>();

	    private List<ObjectId> itemDicIdList = new ArrayList<ObjectId>();
	    //private Map<String,ObjectId> ex

	    private ItemPoolDao itemPoolDao = new ItemPoolDao();
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

	    public void transfer() {
	        SqlSession sqlSession = getSessionFactory().openSession();
	        RefactorMapper refactorMapper = sqlSession.getMapper(RefactorMapper.class);



	        resourceKPDicInfoList = refactorMapper.getResouceDicInfo();

	        resourceNodeInfoList = refactorMapper.getResouceDicInfo2();

	        transerResourceDic();


	        itemPoolInfoList = refactorMapper.getItemPool1();
	        translateitems(itemPoolInfoList);
//	        itemPoolInfoList_middleschool = refactorMapper.getItemPool1();
//	        itemPoolInfoList_primary = refactorMapper.getItemPool2();

	        //itemPoolInfoList_gk = refactorMapper.getItemPool3();
	        sqlSession.close();


	        deleteOtherDic();

//	        transferKnowledgePoint(itemPoolInfoList_gk);
//	        transferBookSelection(itemPoolInfoList_gk);
//	        translateitems(itemPoolInfoList_gk);


//	        transferKnowledgePoint(itemPoolInfoList_middleschool);
//	        transferBookSelection(itemPoolInfoList_middleschool);
	//
	//
//	        transferKnowledgePoint(itemPoolInfoList_primary);
//	        transferBookSelection(itemPoolInfoList_primary);
	//
	//
	//
	//
	//
//	        translateitems(itemPoolInfoList_primary);
//	        translateitems(itemPoolInfoList_middleschool);



	    }

	    private void transerResourceDic(){

	        //学段
	        ResourceDictionaryEntry resourceDictionaryEntry1 = new ResourceDictionaryEntry(
	                1,"小学",null,null
	        );
	        resourceDictionaryEntry1.setID(new ObjectId());
	        List<IdValuePair> rlist1 = new ArrayList<IdValuePair>();
	        rlist1.add(new IdValuePair(resourceDictionaryEntry1.getID(),"小学"));
	        resourceDictionaryEntry1.setSort(1);
	        resourceDictionaryEntry1.setID(new ObjectId());
	        //resourceDictionaryDao.addResourceDictionaryEntry(resourceDictionaryEntry1);
	        resourceDictionaryEntryMap.put(resourceDictionaryEntry1.getID(),resourceDictionaryEntry1);
	        resourceDicInfoMap.put("3",resourceDictionaryEntry1);

	        ResourceDictionaryEntry resourceDictionaryEntry2 = new ResourceDictionaryEntry(
	                1,"初中",null,null
	        );
	        resourceDictionaryEntry2.setID(new ObjectId());
	        List<IdValuePair> rlist2 = new ArrayList<IdValuePair>();
	        rlist2.add(new IdValuePair(resourceDictionaryEntry2.getID(),"初中"));
	        resourceDictionaryEntry2.setSort(2);

	        resourceDictionaryEntry2.setID(new ObjectId());
	        //resourceDictionaryDao.addResourceDictionaryEntry(resourceDictionaryEntry2);
	        resourceDictionaryEntryMap.put(resourceDictionaryEntry2.getID(),resourceDictionaryEntry2);
	        resourceDicInfoMap.put("1",resourceDictionaryEntry2);


	        ResourceDictionaryEntry resourceDictionaryEntry3 = new ResourceDictionaryEntry(
	                1,"高中",null,null
	        );
	        resourceDictionaryEntry3.setID(new ObjectId());
	        List<IdValuePair> rlist3 = new ArrayList<IdValuePair>();
	        rlist3.add(new IdValuePair(resourceDictionaryEntry3.getID(),"高中"));
	        resourceDictionaryEntry3.setSort(3);

	        resourceDictionaryEntry3.setID(new ObjectId());
	        //resourceDictionaryDao.addResourceDictionaryEntry(resourceDictionaryEntry3);
	        resourceDictionaryEntryMap.put(resourceDictionaryEntry3.getID(),resourceDictionaryEntry3);
	        resourceDicInfoMap.put("2",resourceDictionaryEntry3);


	        //小学学科
	        resourceDicInfoMap.put("301",new ResourceDictionaryEntry(
	                2,"语文",resourceDictionaryEntry1.getID(),rlist1
	        ));
	        resourceDicInfoMap.put("302",new ResourceDictionaryEntry(
	                2,"数学",resourceDictionaryEntry1.getID(),rlist1
	        ));
	        resourceDicInfoMap.put("303",new ResourceDictionaryEntry(
	                2,"英语",resourceDictionaryEntry1.getID(),rlist1
	        ));
	        resourceDicInfoMap.get("301").setSort(301);
	        resourceDicInfoMap.get("302").setSort(302);
	        resourceDicInfoMap.get("303").setSort(303);
	        resourceDicInfoMap.get("301").setID(new ObjectId());
	        resourceDicInfoMap.get("302").setID(new ObjectId());
	        resourceDicInfoMap.get("303").setID(new ObjectId());


	        //resourceDictionaryDao.addResourceDictionaryEntry(resourceDicInfoMap.get("301"));
	        //resourceDictionaryDao.addResourceDictionaryEntry(resourceDicInfoMap.get("302"));
	        //resourceDictionaryDao.addResourceDictionaryEntry(resourceDicInfoMap.get("303"));

	        resourceDictionaryEntryMap.put(resourceDicInfoMap.get("301").getID(),resourceDicInfoMap.get("301"));

	        resourceDictionaryEntryMap.put(resourceDicInfoMap.get("302").getID(),resourceDicInfoMap.get("302"));

	        resourceDictionaryEntryMap.put(resourceDicInfoMap.get("303").getID(),resourceDicInfoMap.get("303"));

	        //初中学科
	        resourceDicInfoMap.put("101",new ResourceDictionaryEntry(
	                2,"语文",resourceDictionaryEntry2.getID(),rlist2
	        ));
	        resourceDicInfoMap.put("102",new ResourceDictionaryEntry(
	                2,"数学",resourceDictionaryEntry2.getID(),rlist2
	        ));
	        resourceDicInfoMap.put("103",new ResourceDictionaryEntry(
	                2,"英语",resourceDictionaryEntry2.getID(),rlist2
	        ));
	        resourceDicInfoMap.put("104",new ResourceDictionaryEntry(
	                2,"物理",resourceDictionaryEntry2.getID(),rlist2
	        ));
	        resourceDicInfoMap.put("105",new ResourceDictionaryEntry(
	                2,"化学",resourceDictionaryEntry2.getID(),rlist2
	        ));
	        resourceDicInfoMap.put("106",new ResourceDictionaryEntry(
	                2,"生物",resourceDictionaryEntry2.getID(),rlist2
	        ));
	        resourceDicInfoMap.put("107",new ResourceDictionaryEntry(
	                2,"历史",resourceDictionaryEntry2.getID(),rlist2
	        ));
	        resourceDicInfoMap.put("108",new ResourceDictionaryEntry(
	                2,"地理",resourceDictionaryEntry2.getID(),rlist2
	        ));
	        resourceDicInfoMap.put("109",new ResourceDictionaryEntry(
	                2,"政治",resourceDictionaryEntry2.getID(),rlist2
	        ));
	        resourceDicInfoMap.get("101").setSort(101);
	        resourceDicInfoMap.get("102").setSort(102);
	        resourceDicInfoMap.get("103").setSort(103);

	        resourceDicInfoMap.get("104").setSort(104);
	        resourceDicInfoMap.get("105").setSort(105);
	        resourceDicInfoMap.get("106").setSort(106);

	        resourceDicInfoMap.get("107").setSort(107);
	        resourceDicInfoMap.get("108").setSort(108);
	        resourceDicInfoMap.get("109").setSort(109);


	        resourceDicInfoMap.get("101").setID(new ObjectId());
	        resourceDicInfoMap.get("102").setID(new ObjectId());
	        resourceDicInfoMap.get("103").setID(new ObjectId());
	        resourceDicInfoMap.get("104").setID(new ObjectId());
	        resourceDicInfoMap.get("105").setID(new ObjectId());
	        resourceDicInfoMap.get("106").setID(new ObjectId());
	        resourceDicInfoMap.get("107").setID(new ObjectId());
	        resourceDicInfoMap.get("108").setID(new ObjectId());
	        resourceDicInfoMap.get("109").setID(new ObjectId());


	        resourceDictionaryEntryMap.put(resourceDicInfoMap.get("101").getID(),resourceDicInfoMap.get("101"));
	        resourceDictionaryEntryMap.put(resourceDicInfoMap.get("102").getID(),resourceDicInfoMap.get("102"));
	        resourceDictionaryEntryMap.put(resourceDicInfoMap.get("103").getID(),resourceDicInfoMap.get("103"));
	        resourceDictionaryEntryMap.put(resourceDicInfoMap.get("104").getID(),resourceDicInfoMap.get("104"));
	        resourceDictionaryEntryMap.put(resourceDicInfoMap.get("105").getID(),resourceDicInfoMap.get("105"));
	        resourceDictionaryEntryMap.put(resourceDicInfoMap.get("106").getID(),resourceDicInfoMap.get("106"));
	        resourceDictionaryEntryMap.put(resourceDicInfoMap.get("107").getID(),resourceDicInfoMap.get("107"));
	        resourceDictionaryEntryMap.put(resourceDicInfoMap.get("108").getID(),resourceDicInfoMap.get("108"));
	        resourceDictionaryEntryMap.put(resourceDicInfoMap.get("109").getID(),resourceDicInfoMap.get("109"));

//	        resourceDictionaryDao.addResourceDictionaryEntry(resourceDicInfoMap.get("101"));
//	        resourceDictionaryDao.addResourceDictionaryEntry(resourceDicInfoMap.get("102"));
//	        resourceDictionaryDao.addResourceDictionaryEntry(resourceDicInfoMap.get("103"));
//	        resourceDictionaryDao.addResourceDictionaryEntry(resourceDicInfoMap.get("104"));
//	        resourceDictionaryDao.addResourceDictionaryEntry(resourceDicInfoMap.get("105"));
//	        resourceDictionaryDao.addResourceDictionaryEntry(resourceDicInfoMap.get("106"));
//	        resourceDictionaryDao.addResourceDictionaryEntry(resourceDicInfoMap.get("107"));
//	        resourceDictionaryDao.addResourceDictionaryEntry(resourceDicInfoMap.get("108"));
//	        resourceDictionaryDao.addResourceDictionaryEntry(resourceDicInfoMap.get("109"));


	        //高中学科
	        resourceDicInfoMap.put("201",new ResourceDictionaryEntry(
	                2,"语文",resourceDictionaryEntry3.getID(),rlist3
	        ));
	        resourceDicInfoMap.put("202",new ResourceDictionaryEntry(
	                2,"数学",resourceDictionaryEntry3.getID(),rlist3
	        ));
	        resourceDicInfoMap.put("203",new ResourceDictionaryEntry(
	                2,"英语",resourceDictionaryEntry3.getID(),rlist3
	        ));
	        resourceDicInfoMap.put("204",new ResourceDictionaryEntry(
	                2,"物理",resourceDictionaryEntry3.getID(),rlist3
	        ));
	        resourceDicInfoMap.put("205",new ResourceDictionaryEntry(
	                2,"化学",resourceDictionaryEntry3.getID(),rlist3
	        ));
	        resourceDicInfoMap.put("206",new ResourceDictionaryEntry(
	                2,"生物",resourceDictionaryEntry3.getID(),rlist3
	        ));
	        resourceDicInfoMap.put("207",new ResourceDictionaryEntry(
	                2,"历史",resourceDictionaryEntry3.getID(),rlist3
	        ));
	        resourceDicInfoMap.put("208",new ResourceDictionaryEntry(
	                2,"地理",resourceDictionaryEntry3.getID(),rlist3
	        ));
	        resourceDicInfoMap.put("209",new ResourceDictionaryEntry(
	                2,"政治",resourceDictionaryEntry3.getID(),rlist3
	        ));

	        resourceDicInfoMap.get("201").setSort(201);
	        resourceDicInfoMap.get("202").setSort(202);
	        resourceDicInfoMap.get("203").setSort(203);

	        resourceDicInfoMap.get("204").setSort(204);
	        resourceDicInfoMap.get("205").setSort(205);
	        resourceDicInfoMap.get("206").setSort(206);

	        resourceDicInfoMap.get("207").setSort(207);
	        resourceDicInfoMap.get("208").setSort(208);
	        resourceDicInfoMap.get("209").setSort(209);

	        resourceDicInfoMap.get("201").setID(new ObjectId());
	        resourceDicInfoMap.get("202").setID(new ObjectId());
	        resourceDicInfoMap.get("203").setID(new ObjectId());
	        resourceDicInfoMap.get("204").setID(new ObjectId());
	        resourceDicInfoMap.get("205").setID(new ObjectId());
	        resourceDicInfoMap.get("206").setID(new ObjectId());
	        resourceDicInfoMap.get("207").setID(new ObjectId());
	        resourceDicInfoMap.get("208").setID(new ObjectId());
	        resourceDicInfoMap.get("209").setID(new ObjectId());


	        resourceDictionaryEntryMap.put(resourceDicInfoMap.get("201").getID(),resourceDicInfoMap.get("201"));
	        resourceDictionaryEntryMap.put(resourceDicInfoMap.get("202").getID(),resourceDicInfoMap.get("202"));
	        resourceDictionaryEntryMap.put(resourceDicInfoMap.get("203").getID(),resourceDicInfoMap.get("203"));
	        resourceDictionaryEntryMap.put(resourceDicInfoMap.get("204").getID(),resourceDicInfoMap.get("204"));
	        resourceDictionaryEntryMap.put(resourceDicInfoMap.get("205").getID(),resourceDicInfoMap.get("205"));
	        resourceDictionaryEntryMap.put(resourceDicInfoMap.get("206").getID(),resourceDicInfoMap.get("206"));
	        resourceDictionaryEntryMap.put(resourceDicInfoMap.get("207").getID(),resourceDicInfoMap.get("207"));
	        resourceDictionaryEntryMap.put(resourceDicInfoMap.get("208").getID(),resourceDicInfoMap.get("208"));
	        resourceDictionaryEntryMap.put(resourceDicInfoMap.get("209").getID(),resourceDicInfoMap.get("209"));

//	        resourceDictionaryDao.addResourceDictionaryEntry(resourceDicInfoMap.get("201"));
//	        resourceDictionaryDao.addResourceDictionaryEntry(resourceDicInfoMap.get("202"));
//	        resourceDictionaryDao.addResourceDictionaryEntry(resourceDicInfoMap.get("203"));
//	        resourceDictionaryDao.addResourceDictionaryEntry(resourceDicInfoMap.get("204"));
//	        resourceDictionaryDao.addResourceDictionaryEntry(resourceDicInfoMap.get("205"));
//	        resourceDictionaryDao.addResourceDictionaryEntry(resourceDicInfoMap.get("206"));
//	        resourceDictionaryDao.addResourceDictionaryEntry(resourceDicInfoMap.get("207"));
//	        resourceDictionaryDao.addResourceDictionaryEntry(resourceDicInfoMap.get("208"));
//	        resourceDictionaryDao.addResourceDictionaryEntry(resourceDicInfoMap.get("209"));



	        //学科处理完毕，处理知识点
	        for(ResourceKPDicInfo resourceKPDicInfo : resourceKPDicInfoList){

	            if((!resourceDicInfoMap.containsKey(resourceKPDicInfo.getCode()))&&(!resourceKPDicInfo.getKlpoint().equals("已删除"))){
	                if(resourceKPDicInfo.getCode().length()==9) {//一级知识点
	                    String xdxk = resourceKPDicInfo.getCode().substring(0,3);
	                    if(resourceDicInfoMap.containsKey(xdxk)){//对应学科学段
	                        ResourceDictionaryEntry rex = resourceDicInfoMap.get(xdxk);
	                        List<IdValuePair> pnlist = new ArrayList<IdValuePair>();
	                        pnlist.addAll(rex.getParentInfos());
	                        pnlist.add(new IdValuePair(rex.getID(),rex.getName()));

	                        ResourceDictionaryEntry ren = new ResourceDictionaryEntry(7,
	                                resourceKPDicInfo.getKlpoint(),rex.getID(),pnlist);
	                        ren.setSort(Long.valueOf(resourceKPDicInfo.getCode()));

	                        ren.setID(new ObjectId());
	                        resourceDictionaryEntryMap.put(ren.getID(),ren);

	                        //resourceDictionaryDao.addResourceDictionaryEntry(ren);
	                        resourceDicInfoMap.put(resourceKPDicInfo.getCode(),ren);

	                    }
	                }
	                if(resourceKPDicInfo.getCode().length()==12) {//二级知识点
	                    String xdxk = resourceKPDicInfo.getCode().substring(0,9);
	                    if(resourceDicInfoMap.containsKey(xdxk)){//对应上级知识点
	                        ResourceDictionaryEntry rex = resourceDicInfoMap.get(xdxk);
	                        List<IdValuePair> pnlist = new ArrayList<IdValuePair>();
	                        pnlist.addAll(rex.getParentInfos());
	                        pnlist.add(new IdValuePair(rex.getID(),rex.getName()));

	                        ResourceDictionaryEntry ren = new ResourceDictionaryEntry(8,
	                                resourceKPDicInfo.getKlpoint(),rex.getID(),pnlist);

	                        ren.setSort(Long.valueOf(resourceKPDicInfo.getCode()));

	                        ren.setID(new ObjectId());
	                        resourceDictionaryEntryMap.put(ren.getID(),ren);
	                        //resourceDictionaryDao.addResourceDictionaryEntry(ren);
	                        resourceDicInfoMap.put(resourceKPDicInfo.getCode(),ren);

	                    }
	                }
	                if(resourceKPDicInfo.getCode().length()==15) {//二级知识点
	                    String xdxk = resourceKPDicInfo.getCode().substring(0,12);
	                    if(resourceDicInfoMap.containsKey(xdxk)){//对应上级知识点
	                        ResourceDictionaryEntry rex = resourceDicInfoMap.get(xdxk);
	                        List<IdValuePair> pnlist = new ArrayList<IdValuePair>();
	                        pnlist.addAll(rex.getParentInfos());
	                        pnlist.add(new IdValuePair(rex.getID(),rex.getName()));

	                        ResourceDictionaryEntry ren = new ResourceDictionaryEntry(9,
	                                resourceKPDicInfo.getKlpoint(),rex.getID(),pnlist);

	                        ren.setSort(Long.valueOf(resourceKPDicInfo.getCode()));

	                        ren.setID(new ObjectId());
	                        resourceDictionaryEntryMap.put(ren.getID(),ren);
	                        //resourceDictionaryDao.addResourceDictionaryEntry(ren);
	                        resourceDicInfoMap.put(resourceKPDicInfo.getCode(),ren);

	                    }
	                }
	            }
	        }



	        //处理教材章节
	        for(ResourceKPDicInfo resourceKPDicInfo : resourceNodeInfoList){

	            if(!resourceDicInfoMap.containsKey("node"+resourceKPDicInfo.getCode())){
	                if(resourceKPDicInfo.getCode().length()==6) {//教材版本
	                    String xdxk = resourceKPDicInfo.getCode().substring(0,3);
	                    if(resourceDicInfoMap.containsKey(xdxk)){//对应学科学段
	                        ResourceDictionaryEntry rex = resourceDicInfoMap.get(xdxk);
	                        List<IdValuePair> pnlist = new ArrayList<IdValuePair>();
	                        pnlist.addAll(rex.getParentInfos());
	                        pnlist.add(new IdValuePair(rex.getID(),rex.getName()));

	                        ResourceDictionaryEntry ren = new ResourceDictionaryEntry(3,
	                                resourceKPDicInfo.getKlpoint(),rex.getID(),pnlist);

	                        ren.setSort(Long.valueOf(resourceKPDicInfo.getCode()));

	                        ren.setID(new ObjectId());
	                        resourceDictionaryEntryMap.put(ren.getID(),ren);
	                        //resourceDictionaryDao.addResourceDictionaryEntry(ren);
	                        resourceDicInfoMap.put("node"+resourceKPDicInfo.getCode(),ren);
	                    }
	                }
	                if(resourceKPDicInfo.getCode().length()==9) {//年级
	                    String xdxk = "node"+resourceKPDicInfo.getCode().substring(0,6);
	                    if(resourceDicInfoMap.containsKey(xdxk)){//对应教材版本
	                        ResourceDictionaryEntry rex = resourceDicInfoMap.get(xdxk);
	                        List<IdValuePair> pnlist = new ArrayList<IdValuePair>();
	                        pnlist.addAll(rex.getParentInfos());
	                        pnlist.add(new IdValuePair(rex.getID(),rex.getName()));

	                        ResourceDictionaryEntry ren = new ResourceDictionaryEntry(4,
	                                resourceKPDicInfo.getKlpoint(),rex.getID(),pnlist);

	                        ren.setSort(Long.valueOf(resourceKPDicInfo.getCode()));

	                        ren.setID(new ObjectId());
	                        resourceDictionaryEntryMap.put(ren.getID(),ren);
	                        //resourceDictionaryDao.addResourceDictionaryEntry(ren);
	                        resourceDicInfoMap.put("node"+resourceKPDicInfo.getCode(),ren);

	                    }
	                }
	                if(resourceKPDicInfo.getCode().length()==12) {//单元
	                    String xdxk = "node"+resourceKPDicInfo.getCode().substring(0,9);
	                    if(resourceDicInfoMap.containsKey(xdxk)){//对应上级知识点
	                        ResourceDictionaryEntry rex = resourceDicInfoMap.get(xdxk);
	                        List<IdValuePair> pnlist = new ArrayList<IdValuePair>();
	                        pnlist.addAll(rex.getParentInfos());
	                        pnlist.add(new IdValuePair(rex.getID(),rex.getName()));

	                        ResourceDictionaryEntry ren = new ResourceDictionaryEntry(5,
	                                resourceKPDicInfo.getKlpoint(),rex.getID(),pnlist);

	                        ren.setSort(Long.valueOf(resourceKPDicInfo.getCode()));

	                        ren.setID(new ObjectId());
	                        resourceDictionaryEntryMap.put(ren.getID(),ren);
	                        //resourceDictionaryDao.addResourceDictionaryEntry(ren);
	                        resourceDicInfoMap.put("node"+resourceKPDicInfo.getCode(),ren);

	                    }
	                }
	                if(resourceKPDicInfo.getCode().length()==15) {//课
	                    String xdxk = "node"+resourceKPDicInfo.getCode().substring(0,12);
	                    if(resourceDicInfoMap.containsKey(xdxk)){//对应上级知识点
	                        ResourceDictionaryEntry rex = resourceDicInfoMap.get(xdxk);
	                        List<IdValuePair> pnlist = new ArrayList<IdValuePair>();
	                        pnlist.addAll(rex.getParentInfos());
	                        pnlist.add(new IdValuePair(rex.getID(),rex.getName()));

	                        ResourceDictionaryEntry ren = new ResourceDictionaryEntry(6,
	                                resourceKPDicInfo.getKlpoint(),rex.getID(),pnlist);

	                        ren.setSort(Long.valueOf(resourceKPDicInfo.getCode()));

	                        ren.setID(new ObjectId());
	                        resourceDictionaryEntryMap.put(ren.getID(),ren);
	                        //resourceDictionaryDao.addResourceDictionaryEntry(ren);
	                        resourceDicInfoMap.put("node"+resourceKPDicInfo.getCode(),ren);

	                    }
	                }
	            }
	        }



	    }

	    private void translateitems(List<ItemPoolInfo> itemPoolInfoList){

	        for(ItemPoolInfo itemPoolInfo: itemPoolInfoList) {

	            ObjectId xueduan = null;
	            if(itemPoolInfo.getBooknode()!=null &&itemPoolInfo.getBooknode().length()>1 &&
	                    resourceDicInfoMap.containsKey(itemPoolInfo.getBooknode().substring(0,1))){

	                xueduan = resourceDicInfoMap.get(itemPoolInfo.getBooknode().substring(0,1)).getID();
	            }
	            ObjectId xueke = null;
	            List<IdValuePair> xuekepar = null;
	            if(itemPoolInfo.getBooknode()!=null &&itemPoolInfo.getBooknode().length()>3 &&
	                    resourceDicInfoMap.containsKey(itemPoolInfo.getBooknode().substring(0,3)) ){
	                xueke = resourceDicInfoMap.get(itemPoolInfo.getBooknode().substring(0,3)).getID();
	                xuekepar = resourceDicInfoMap.get(itemPoolInfo.getBooknode().substring(0,3)).getParentInfos();
	            }

	            int level= 0;
	            String difficultyStr = itemPoolInfo.getDifficulty();
	            if(difficultyStr.equals("易")){
	                level = 1;
	            }else if(difficultyStr.equals("较易")){
	                level = 2;
	            }else if(difficultyStr.equals("中")){
	                level = 3;
	            }else if(difficultyStr.equals("较难")){
	                level = 4;
	            }else if(difficultyStr.equals("难")){
	                level = 5;
	            }

	            ObjectId type = null;//注意这里类型在字典表创建


	            if(xueke!=null &&itemPoolInfo.getItemtype()!=null && itemPoolInfo.getItemtype().length()>0) {
	                if (resourceDicInfoMap.containsKey(itemPoolInfo.getBooknode().substring(0,3)+itemPoolInfo.getItemtype())) {
	                    type = resourceDicInfoMap.get(itemPoolInfo.getBooknode().substring(0,3)+itemPoolInfo.getItemtype()).getID();
	                } else {
	                    //创建新题型
	                    List<IdValuePair> typepar = null;
	                    if(xuekepar!=null){
	                        typepar = new ArrayList<IdValuePair>();
	                        typepar.addAll(xuekepar);
	                        typepar.add(new IdValuePair(xueke,resourceDicInfoMap.get(itemPoolInfo.getBooknode().substring(0,3)).getName()));
	                    }
	                    resourceDicInfoMap.put(itemPoolInfo.getBooknode().substring(0,3)+itemPoolInfo.getItemtype(), new ResourceDictionaryEntry(
	                            10, itemPoolInfo.getItemtype(), xueke, typepar
	                    ));
	                    resourceDictionaryDao.addResourceDictionaryEntry(resourceDicInfoMap.get(itemPoolInfo.getBooknode().substring(0,3)+itemPoolInfo.getItemtype()));
	                    type = resourceDicInfoMap.get(itemPoolInfo.getBooknode().substring(0,3)+itemPoolInfo.getItemtype()).getID();
	                }
	            }

	            ObjectId scope = null; //知识面
	            if(itemPoolInfo.getKnowledgePointId()!=null && itemPoolInfo.getKnowledgePointId().length()>11 &&
	                    resourceDicInfoMap.containsKey(itemPoolInfo.getKnowledgePointId().substring(0,12))){
	                scope = resourceDicInfoMap.get(itemPoolInfo.getKnowledgePointId().substring(0,12)).getID();
	            }

	            ObjectId clty = null; // 知识点
	            if(itemPoolInfo.getKnowledgePointId()!=null && itemPoolInfo.getKnowledgePointId().length()>14 &&
	                    resourceDicInfoMap.containsKey(itemPoolInfo.getKnowledgePointId())){
	                clty = resourceDicInfoMap.get(itemPoolInfo.getKnowledgePointId()).getID();
	            }

	            String question = dealHtml(itemPoolInfo.getItemcontent()); //question
	            String answer = dealHtml(itemPoolInfo.getAnswer()); //answer
	            String paranswer = "";

	            ItemPoolEntry.ObjectiveItem objectiveItem = new ItemPoolEntry.ObjectiveItem(
	                    itemPoolInfo.getObjectiveanswer(),
	                    itemPoolInfo.getAnswercount(),
	                    itemPoolInfo.getSelectcount()
	            );

	            ObjectId zhangjie1 = null;//单元
	            if(itemPoolInfo.getBooknode()!=null && itemPoolInfo.getBooknode().length()>11&&
	                    resourceDicInfoMap.containsKey("node"+itemPoolInfo.getBooknode().substring(0,12))){
	                zhangjie1 = resourceDicInfoMap.get("node"+itemPoolInfo.getBooknode().substring(0,12)).getID();
	            }

	            ObjectId zhangjie2 = null; //课
	            if(itemPoolInfo.getBooknode()!=null && itemPoolInfo.getBooknode().length()>14&&
	                    resourceDicInfoMap.containsKey("node"+itemPoolInfo.getBooknode().substring(0,15))){
	                zhangjie2 = resourceDicInfoMap.get("node"+itemPoolInfo.getBooknode().substring(0,15)).getID();
	            }


	            List<Integer> gradeList = getgradetype(itemPoolInfo.getGrade());


	            //题型
	            ExerciseItemType exerciseItemType = ExerciseItemType.SINGLECHOICE;
	            if(itemPoolInfo.getItemtype().contains("判断题")){
	                exerciseItemType = ExerciseItemType.TRUE_OR_FALSE;
	            }else if(itemPoolInfo.getItemtype().contains("填空题")){
	                exerciseItemType = ExerciseItemType.GAP;
	            }else if(itemPoolInfo.getObjective()!=null && itemPoolInfo.getObjective()==0) {
	                exerciseItemType = ExerciseItemType.SUBJECTIVE;
	            }



	            if(xueduan!=null && xueke!=null){
//	                ItemPoolEntry itemPoolEntry = new ItemPoolEntry(
//	                    xueduan, //grades
//	                    xueke, //subject
//	                    level,  //level
//	                    type, //type
//	                    scope,
//	                    clty,
//	                    (double)itemPoolInfo.getPoint(), //score
//	                    question,
//	                    answer,
//	                        paranswer,//parse answer
//	                    objectiveItem,//item
//	                    zhangjie1,zhangjie2,gradeList,
//	                        exerciseItemType.getType()
//	                );
//	                itemPoolDao.addItemPoolEntry(itemPoolEntry);
//	                itemDicIdList.add(zhangjie2);
//	                itemDicIdList.add(clty);
	            }

	//

	        }
	    }
	    //解析并上传图片
	    private String dealHtml(String html){


	        while(html.indexOf("Mypicpath")!=-1){
	            String qiniuPath = "http://7xj25c.com1.z0.glb.clouddn.com/";
	            html = html.substring(0,html.indexOf("Mypicpath"))
	                    +qiniuPath
	                    +html.substring(html.indexOf("Mypicpath")+10);
	        }
	        html = html.replace('\\','/');

	        return html;
	    }





	    private List<Integer> getgradetype(String gradeStr){
	        GradeType gradeType = null;

	        List<Integer> retList = new ArrayList<Integer>();

	        if(gradeStr.contains("七年级")){
	            gradeType = GradeType.CHU1;
	            retList.add(gradeType.getId());
	        }else if(gradeStr.contains("八年级")){
	            gradeType = GradeType.CHU2;

	            retList.add(gradeType.getId());
	        }else if(gradeStr.contains("九年级")){
	            gradeType = GradeType.CHU3;

	            retList.add(gradeType.getId());
	        }else if(gradeStr.contains("中考")){
	            gradeType = GradeType.CHU3;

	            retList.add(gradeType.getId());
	        }
	        else if(gradeStr.contains("一年级")){
	            gradeType = GradeType.XIAO1;

	            retList.add(gradeType.getId());
	        }else if(gradeStr.contains("二年级")){
	            gradeType = GradeType.XIAO2;

	            retList.add(gradeType.getId());
	        }else if(gradeStr.contains("三年级")){
	            gradeType = GradeType.XIAO3;

	            retList.add(gradeType.getId());
	        }else if(gradeStr.contains("四年级")){
	            gradeType = GradeType.XIAO4;

	            retList.add(gradeType.getId());
	        }
	        else if(gradeStr.contains("五年级")){
	            gradeType = GradeType.XIAO5;

	            retList.add(gradeType.getId());
	        }
	        else if(gradeStr.contains("六年级")){
	            gradeType = GradeType.XIAO6;

	            retList.add(gradeType.getId());
	        }
	        else{

	            retList.add(GradeType.GAO1.getId());

	            retList.add(GradeType.GAO2.getId());

	            retList.add(GradeType.GAO3.getId());

	            retList.add(GradeType.GAO_FUXI.getId());
	        }
	        return  retList;

	    }



	    private void deleteOtherDic() {
	        //知识点,课删除完毕

	        List<ObjectId> kp3par = new ArrayList<ObjectId>();
	        List<ObjectId> toDelete = new ArrayList<ObjectId>();
	        for (ObjectId kpId : resourceDictionaryEntryMap.keySet()) {
	            ResourceDictionaryEntry resourceDictionaryEntry = resourceDictionaryEntryMap.get(kpId);
	            if (resourceDictionaryEntry.getType() == 9) {// 小知识点
	                if (!itemDicIdList.contains(kpId)) {//不包含这个知识点
	                    //resourceDictionaryEntryMap.remove(kpId);
	                    toDelete.add(kpId);
	                    continue;
	                } else {
	                    kp3par.add(resourceDictionaryEntry.getParentId());
	                }
	            }
	            if (resourceDictionaryEntry.getType() == 6) {//课
	                if (!itemDicIdList.contains(kpId)) {//不包含这个知识点
	                    //resourceDictionaryEntryMap.remove(kpId);
	                    toDelete.add(kpId);
	                    continue;
	                } else {
	                    kp3par.add(resourceDictionaryEntry.getParentId());
	                }
	            }
	        }

	        for(ObjectId kpId :toDelete){
	            resourceDictionaryEntryMap.remove(kpId);
	        }
	        toDelete.clear();
	        //二级知识点,下面没有3级知识点，删除,单元下面没有课，删除

	        List<ObjectId> kp2par = new ArrayList<ObjectId>();
	        for (ObjectId kpId : resourceDictionaryEntryMap.keySet()) {

	            ResourceDictionaryEntry resourceDictionaryEntry = resourceDictionaryEntryMap.get(kpId);
	            if (resourceDictionaryEntry.getType() == 8) {// 二级知识点
	                if (!kp3par.contains(kpId)) {//不包含这个二级知识点
	                    //resourceDictionaryEntryMap.remove(kpId);
	                    toDelete.add(kpId);
	                    continue;
	                } else {
	                    kp2par.add(resourceDictionaryEntry.getParentId());
	                }
	            }
	            if (resourceDictionaryEntry.getType() == 5) {//单元
	                if (!kp3par.contains(kpId)) {//不包含这个单元
	                    //resourceDictionaryEntryMap.remove(kpId);
	                    toDelete.add(kpId);
	                    continue;
	                } else {
	                    kp2par.add(resourceDictionaryEntry.getParentId());
	                }
	            }
	        }
	        for(ObjectId kpId :toDelete){
	            resourceDictionaryEntryMap.remove(kpId);
	        }
	        toDelete.clear();
	        //一级知识点，年级
	        List<ObjectId> kp1par = new ArrayList<ObjectId>();
	        for (ObjectId kpId : resourceDictionaryEntryMap.keySet()) {

	            ResourceDictionaryEntry resourceDictionaryEntry = resourceDictionaryEntryMap.get(kpId);
	            if (resourceDictionaryEntry.getType() == 7) {// 一级知识点
	                if (!kp2par.contains(kpId)) {//不包含这个一级知识点
	                    //resourceDictionaryEntryMap.remove(kpId);
	                    toDelete.add(kpId);
	                    continue;
	                } else {
	                    //kp1par.add(resourceDictionaryEntry.getParentId());
	                }
	            }
	            if (resourceDictionaryEntry.getType() == 4) {//年级
	                if (!kp2par.contains(kpId)) {//不包含这个年级
	                    //resourceDictionaryEntryMap.remove(kpId);
	                    toDelete.add(kpId);
	                    continue;
	                } else {
	                    kp1par.add(resourceDictionaryEntry.getParentId());
	                }
	            }
	        }
	        for(ObjectId kpId :toDelete){
	            resourceDictionaryEntryMap.remove(kpId);
	        }
	        toDelete.clear();

	        //一级知识点，年级
	        for (ObjectId kpId : resourceDictionaryEntryMap.keySet()) {

	            ResourceDictionaryEntry resourceDictionaryEntry = resourceDictionaryEntryMap.get(kpId);

	            if (resourceDictionaryEntry.getType() == 3) {//版本
	                if (!kp1par.contains(kpId)) {//不包含这个版本
	                    //resourceDictionaryEntryMap.remove(kpId);
	                    toDelete.add(kpId);
	                    continue;
	                } else {
	                    //kp1par.add(resourceDictionaryEntry.getParentId());
	                }
	            }
	        }
	        for(ObjectId kpId :toDelete){
	            resourceDictionaryEntryMap.remove(kpId);
	        }

	        //把剩下的字典加进去
	        for (ResourceDictionaryEntry resourceDictionaryEntry : resourceDictionaryEntryMap.values()) {
	            resourceDictionaryDao.addResourceDictionaryEntry(resourceDictionaryEntry);
	        }
	    }


	    public static void main(String[] args){
	        TransferItemPool transferItemPool = new TransferItemPool();
	        //transferItemPool.transfer();
	        transferItemPool.transfer();
	    }
}
