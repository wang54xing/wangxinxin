package com.sql.oldDataTransfer;


import com.db.itempool.ItemPoolDao;
import com.db.resources.ResourceDictionaryDao;
import com.pojo.app.IdValuePair;
import com.pojo.exercise.ExerciseItemType;
import com.pojo.itempool.*;
import com.pojo.itempool.ItemPoolEntry.ObjectiveItem;
import com.pojo.resources.ResourceDictionaryEntry;
import com.pojo.school.GradeType;
import com.sql.dao.RefactorMapper;
import com.sql.oldDataPojo.ItemPoolInfo;
import com.sql.oldDataPojo.ResourceKPDicInfo;

import org.apache.commons.io.FileUtils;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.bson.types.ObjectId;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by qinbo on 15/5/8.
 */
public class TransferItemPool {

    private List<ItemPoolInfo> itemPoolInfoList;
    
    private List<ItemPoolInfo> itemPoolInfoList_gk;
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

        itemPoolInfoList_gk = refactorMapper.getItemPool3();
        
        try {
			//translateitems(itemPoolInfoList_gk);
		} catch (Exception e) {
			e.printStackTrace();
		}
        sqlSession.close();

    }

  
    
    
    
    private Map<String,ObjectId> getSubjectMap(ObjectId parentId,int type)
    {
    	Map<String,ObjectId> retMap =new HashMap<String, ObjectId>();
    	List<ResourceDictionaryEntry> list=resourceDictionaryDao.getResourceDictionaryEntrys(parentId, type);
    	
    	for(ResourceDictionaryEntry rde:list)
    	{
    		retMap.put(rde.getName(), rde.getID());
    	}
    	
    	return retMap;
    }
    
    
    
    //题型
    private Map<String,ObjectId> getItemType()
    {
    	Map<String,ObjectId> retMap =new HashMap<String, ObjectId>();
    	List<ResourceDictionaryEntry> list=resourceDictionaryDao.getResourceDictionaryEntry(10);
    	
    	for(ResourceDictionaryEntry rde:list)
    	{
    		retMap.put(rde.getName(), rde.getID());
    	}
    	return retMap;
    }
  
    
    
  
    
    
   
    
    

    
    /**
     * 先倒知识点和目录
     * @throws Exception 
     */
    private void transKwAndPs(List<ItemPoolInfo> itemPoolInfoList) throws Exception
    {
    	
    	for(ItemPoolInfo itemPoolInfo: itemPoolInfoList)
    	{
    		//学科
    		String xueke=itemPoolInfo.getSubject();
    		ResourceDictionaryEntry xuekeEntry=resourceDictionaryDao.getResourceDictionaryEntry(2, xueke,xueDuanPair.getId());
    		if(null==xuekeEntry)
    		{
    			throw new Exception("Can not find xueke:"+xuekeEntry);
    		}
    		
    		
    		
    		//教材版本
    		String jiaocai=itemPoolInfo.getBookedition();
    		ResourceDictionaryEntry jiaocaiEntry=resourceDictionaryDao.getResourceDictionaryEntry(3, jiaocai,xuekeEntry.getID());
    		if(null==jiaocaiEntry)
    		{
    			jiaocaiEntry =new ResourceDictionaryEntry(3, jiaocai, xuekeEntry.getID(), getIdValuePairList(xuekeEntry)) ;
    			jiaocaiEntry.setSort(Long.valueOf(199999));
    			resourceDictionaryDao.addResourceDictionaryEntry(jiaocaiEntry);
    			
    		}
    		
    		
    		//年级
    		String nianji=itemPoolInfo.getGrade();
    		ResourceDictionaryEntry nianjiEntry=resourceDictionaryDao.getResourceDictionaryEntry(4, nianji,jiaocaiEntry.getID());
    		if(null==nianjiEntry)
    		{
    			nianjiEntry =new ResourceDictionaryEntry(4, nianji, jiaocaiEntry.getID(), getIdValuePairList(jiaocaiEntry)) ;
    			nianjiEntry.setSort(Long.valueOf(199999999));
    			resourceDictionaryDao.addResourceDictionaryEntry(nianjiEntry);
    		}
    		
    	
    		//单元
    		String danyuan=itemPoolInfo.getDanyuan();
    		ResourceDictionaryEntry danyuanEntry=resourceDictionaryDao.getResourceDictionaryEntry(5, danyuan,nianjiEntry.getID());
    		if(null==danyuanEntry)
    		{
    			danyuanEntry =new ResourceDictionaryEntry(5, danyuan, nianjiEntry.getID(), getIdValuePairList(nianjiEntry)) ;
    			danyuanEntry.setSort(Long.valueOf("199999999999"));
    			resourceDictionaryDao.addResourceDictionaryEntry(danyuanEntry);
    		}
    		
    	
    		//课 (去查知识点)
    		String ke=itemPoolInfo.getSingleKw();
    		ResourceDictionaryEntry kwEntry=resourceDictionaryDao.getResourceDictionaryEntry(9, ke,null);
    		if(null==kwEntry)
    		{
    			throw new Exception("Can not find ke:"+ke);
    		}
    		ResourceDictionaryEntry keEntry=new ResourceDictionaryEntry(6, kwEntry.getName(),danyuanEntry.getID(),getIdValuePairList(danyuanEntry));
    		keEntry.setSort(kwEntry.getSort());
    		
    	}
    }
    
    
    
    private List<IdValuePair> getIdValuePairList(ResourceDictionaryEntry parentE)
    {
    	List<IdValuePair> list=parentE.getParentInfos();
    	list.add(new IdValuePair(parentE.getID(), parentE.getName()));
    	return list;
    }
    
     //高中
    private IdValuePair xueDuanPair =new IdValuePair(new ObjectId("55d41e47e0b064452581269e"), "高中");
    String prefix="1";
    String myGrard="";
    
    //OK
   // String tableName="试题表20140703";
  //  String imageDir="20140703";
    
    //OK
    // String tableName="试题表20140721";
    // String imageDir="20160101";
    
    
     String tableName="试题表";
     String imageDir="20160105";
    
     
     
    //初中
  //  private IdValuePair xueDuanPair =new IdValuePair(new ObjectId("55d41e47e0b064452581269c"), "初中");
  //  String prefix="2";
  //  String myGrard="中考";
    
    //第一次（OK）
    //String tableName="复兰试题表20150414";
    //String imageDir="20150104";
    
    //第二次 （没有图片）
    // String tableName="试题表20150805";
   //  String imageDir="20160104";
    
    
    
    
    String qiniuPath = "http://7xj25c.com1.z0.glb.clouddn.com/"+imageDir+"/";
                               
/*
    private void translateitems(List<ItemPoolInfo> itemPoolInfoList) throws Exception{

    	File f=new File("D:\\res1215\\itempool_"+tableName+".txt");
    	f.createNewFile();
    	
    	Map<String,ObjectId> itemTypeMap =	getItemType();
    	
        for(ItemPoolInfo itemPoolInfo: itemPoolInfoList) {
        	try
        	{
        	   transSingleItem(itemTypeMap, itemPoolInfo,f);
        	}catch(Exception ex)
        	{
        		 FileUtils.write(f, "\r\n",true);
				 FileUtils.write(f,itemPoolInfo.getTid() +":"+ex.getMessage(),true);
        	}
        }
    }

<<<<<<< HEAD
<<<<<<< HEAD
=======
            ObjectId xueduan = null;
            if(itemPoolInfo.getBooknode()!=null &&itemPoolInfo.getBooknode().length()>1 &&
                    resourceDicInfoMap.containsKey(itemPoolInfo.getBooknode().substring(0,1))){
=======
>>>>>>> origin/feature/itempool

//            ObjectId xueduan = null;
//            if(itemPoolInfo.getBooknode()!=null &&itemPoolInfo.getBooknode().length()>1 &&
//                    resourceDicInfoMap.containsKey(itemPoolInfo.getBooknode().substring(0,1))){
//
//                xueduan = resourceDicInfoMap.get(itemPoolInfo.getBooknode().substring(0,1)).getID();
//            }
//            ObjectId xueke = null;
//            List<IdValuePair> xuekepar = null;
//            if(itemPoolInfo.getBooknode()!=null &&itemPoolInfo.getBooknode().length()>3 &&
//                    resourceDicInfoMap.containsKey(itemPoolInfo.getBooknode().substring(0,3)) ){
//                xueke = resourceDicInfoMap.get(itemPoolInfo.getBooknode().substring(0,3)).getID();
//                xuekepar = resourceDicInfoMap.get(itemPoolInfo.getBooknode().substring(0,3)).getParentInfos();
//            }
//
//            int level= 0;
//            String difficultyStr = itemPoolInfo.getDifficulty();
//            if(difficultyStr.equals("易")){
//                level = 1;
//            }else if(difficultyStr.equals("较易")){
//                level = 2;
//            }else if(difficultyStr.equals("中")){
//                level = 3;
//            }else if(difficultyStr.equals("较难")){
//                level = 4;
//            }else if(difficultyStr.equals("难")){
//                level = 5;
//            }
//
//            ObjectId type = null;//注意这里类型在字典表创建
//
//
//            if(xueke!=null &&itemPoolInfo.getItemtype()!=null && itemPoolInfo.getItemtype().length()>0) {
//                if (resourceDicInfoMap.containsKey(itemPoolInfo.getBooknode().substring(0,3)+itemPoolInfo.getItemtype())) {
//                    type = resourceDicInfoMap.get(itemPoolInfo.getBooknode().substring(0,3)+itemPoolInfo.getItemtype()).getID();
//                } else {
//                    //创建新题型
//                    List<IdValuePair> typepar = null;
//                    if(xuekepar!=null){
//                        typepar = new ArrayList<IdValuePair>();
//                        typepar.addAll(xuekepar);
//                        typepar.add(new IdValuePair(xueke,resourceDicInfoMap.get(itemPoolInfo.getBooknode().substring(0,3)).getName()));
//                    }
//                    resourceDicInfoMap.put(itemPoolInfo.getBooknode().substring(0,3)+itemPoolInfo.getItemtype(), new ResourceDictionaryEntry(
//                            10, itemPoolInfo.getItemtype(), xueke, typepar
//                    ));
//                    resourceDictionaryDao.addResourceDictionaryEntry(resourceDicInfoMap.get(itemPoolInfo.getBooknode().substring(0,3)+itemPoolInfo.getItemtype()));
//                    type = resourceDicInfoMap.get(itemPoolInfo.getBooknode().substring(0,3)+itemPoolInfo.getItemtype()).getID();
//                }
//            }
//
//            ObjectId scope = null; //知识面
//            if(itemPoolInfo.getKnowledgePointId()!=null && itemPoolInfo.getKnowledgePointId().length()>11 &&
//                    resourceDicInfoMap.containsKey(itemPoolInfo.getKnowledgePointId().substring(0,12))){
//                scope = resourceDicInfoMap.get(itemPoolInfo.getKnowledgePointId().substring(0,12)).getID();
//            }
//
//            ObjectId clty = null; // 知识点
//            if(itemPoolInfo.getKnowledgePointId()!=null && itemPoolInfo.getKnowledgePointId().length()>14 &&
//                    resourceDicInfoMap.containsKey(itemPoolInfo.getKnowledgePointId())){
//                clty = resourceDicInfoMap.get(itemPoolInfo.getKnowledgePointId()).getID();
//            }
//
//            String question = dealHtml(itemPoolInfo.getItemcontent()); //question
//            String answer = dealHtml(itemPoolInfo.getAnswer()); //answer
//            String paranswer = "";
//
//            ItemPoolEntry.ObjectiveItem objectiveItem = new ItemPoolEntry.ObjectiveItem(
//                    itemPoolInfo.getObjectiveanswer(),
//                    itemPoolInfo.getAnswercount(),
//                    itemPoolInfo.getSelectcount()
//            );
//
//            ObjectId zhangjie1 = null;//单元
//            if(itemPoolInfo.getBooknode()!=null && itemPoolInfo.getBooknode().length()>11&&
//                    resourceDicInfoMap.containsKey("node"+itemPoolInfo.getBooknode().substring(0,12))){
//                zhangjie1 = resourceDicInfoMap.get("node"+itemPoolInfo.getBooknode().substring(0,12)).getID();
//            }
//
//            ObjectId zhangjie2 = null; //课
//            if(itemPoolInfo.getBooknode()!=null && itemPoolInfo.getBooknode().length()>14&&
//                    resourceDicInfoMap.containsKey("node"+itemPoolInfo.getBooknode().substring(0,15))){
//                zhangjie2 = resourceDicInfoMap.get("node"+itemPoolInfo.getBooknode().substring(0,15)).getID();
//            }
//
//
//            List<Integer> gradeList = getgradetype(itemPoolInfo.getGrade());
//
//
//            //题型
//            ExerciseItemType exerciseItemType = ExerciseItemType.SINGLECHOICE;
//            if(itemPoolInfo.getItemtype().contains("判断题")){
//                exerciseItemType = ExerciseItemType.TRUE_OR_FALSE;
//            }else if(itemPoolInfo.getItemtype().contains("填空题")){
//                exerciseItemType = ExerciseItemType.GAP;
//            }else if(itemPoolInfo.getObjective()!=null && itemPoolInfo.getObjective()==0) {
//                exerciseItemType = ExerciseItemType.SUBJECTIVE;
//            }



//            if(xueduan!=null && xueke!=null){
//                ItemPoolEntry itemPoolEntry = new ItemPoolEntry(
//                    xueduan, //grades
//                    xueke, //subject
//                    level,  //level
//                    type, //type
//                    scope,
//                    clty,
//                    (double)itemPoolInfo.getPoint(), //score
//                    question,
//                    answer,
//                        paranswer,//parse answer
//                    objectiveItem,//item
//                    zhangjie1,zhangjie2,gradeList,
//                        exerciseItemType.getType()
//                );
//                itemPoolDao.addItemPoolEntry(itemPoolEntry);
//                itemDicIdList.add(zhangjie2);
//                itemDicIdList.add(clty);
<<<<<<< HEAD
            }
>>>>>>> 636d4098386ddda4506f70e85fbf7421543b3e4e
=======
//            }

>>>>>>> origin/feature/itempool


	private void transSingleItem(Map<String, ObjectId> itemTypeMap,
			ItemPoolInfo itemPoolInfo,File f) throws Exception {
		////////////////////////////////导入章节/////////////////////////////////
		//学科
		String xueke=itemPoolInfo.getSubject();
		ResourceDictionaryEntry xuekeEntry=resourceDictionaryDao.getResourceDictionaryEntry(2, xueke,xueDuanPair.getId());
		if(null==xuekeEntry)
		{
			throw new Exception("Can not find xueke:"+xuekeEntry);
		}
		
		
		
		//教材版本
		String jiaocai=itemPoolInfo.getBookedition();
		ResourceDictionaryEntry jiaocaiEntry=resourceDictionaryDao.getResourceDictionaryEntry(3, jiaocai,xuekeEntry.getID());
		if(null==jiaocaiEntry)
		{
			jiaocaiEntry =new ResourceDictionaryEntry(3, jiaocai, xuekeEntry.getID(), getIdValuePairList(xuekeEntry)) ;
			jiaocaiEntry.setSort(Long.valueOf(prefix+99999));
			resourceDictionaryDao.addResourceDictionaryEntry(jiaocaiEntry);
		}
		
		//年级
		String nianji=itemPoolInfo.getGrade();
		ResourceDictionaryEntry nianjiEntry=resourceDictionaryDao.getResourceDictionaryEntry(4, nianji,jiaocaiEntry.getID());
		if(null==nianjiEntry)
		{
			nianjiEntry =new ResourceDictionaryEntry(4, nianji, jiaocaiEntry.getID(), getIdValuePairList(jiaocaiEntry)) ;
			nianjiEntry.setSort(Long.valueOf(prefix+99999999));
			resourceDictionaryDao.addResourceDictionaryEntry(nianjiEntry);
		}
		
  	
		//单元
		String danyuan=itemPoolInfo.getDanyuan();
		ResourceDictionaryEntry danyuanEntry=resourceDictionaryDao.getResourceDictionaryEntry(5, danyuan,nianjiEntry.getID());
		if(null==danyuanEntry)
		{
			danyuanEntry =new ResourceDictionaryEntry(5, danyuan, nianjiEntry.getID(), getIdValuePairList(nianjiEntry)) ;
			danyuanEntry.setSort(Long.valueOf(prefix+"99999999999"));
			resourceDictionaryDao.addResourceDictionaryEntry(danyuanEntry);
		}
		
  	
		//课 (去查知识点)
		String ke=itemPoolInfo.getSingleKw();
		ResourceDictionaryEntry kwEntry=resourceDictionaryDao.getResourceDictionaryEntryBySort(Long.valueOf(ke), 9);
		if(null==kwEntry)
		{
			throw new Exception("Can not find ke:"+ke);
		}
		
		
		
		
		ResourceDictionaryEntry keEntry=resourceDictionaryDao.getResourceDictionaryEntry(6, kwEntry.getName(),danyuanEntry.getID());
		if(null==keEntry)
		{
		 keEntry=new ResourceDictionaryEntry(6, kwEntry.getName(),danyuanEntry.getID(),getIdValuePairList(danyuanEntry));
		 keEntry.setSort(kwEntry.getSort());
		 resourceDictionaryDao.addResourceDictionaryEntry(keEntry);
		}
		
		
		////////////////////////////////导入章节结束/////////////////////////////////
		
		

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
		
		ObjectId type =   itemTypeMap.get(itemPoolInfo.getItemtype());
		
		if(null==type)
		{
			ResourceDictionaryEntry thistype =new ResourceDictionaryEntry(10, itemPoolInfo.getItemtype(), xuekeEntry.getID(), getIdValuePairList(xuekeEntry)) ;
			type=resourceDictionaryDao.addResourceDictionaryEntry(thistype);
		}

		//ObjectId scope =getkwMian(itemPoolInfo.getKnowledgeAreadName());
         // ObjectId clty = getkwdian(itemPoolInfo.getKnowledgePointName());
		ObjectId scope = kwEntry.getParentId();
		ObjectId clty =kwEntry.getID();
		

		String question = dealHtml(itemPoolInfo.getItemcontent()); //question
		String answer = dealHtml(itemPoolInfo.getAnswer()); //answer
		String paranswer = "";

		ItemPoolEntry.ObjectiveItem objectiveItem = new ItemPoolEntry.ObjectiveItem(
		        itemPoolInfo.getObjectiveanswer(),
		        itemPoolInfo.getAnswercount(),
		        itemPoolInfo.getSelectcount()
		);



		itemPoolInfo.setGrade(myGrard);;
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

		
         // ObjectId xueduan, ObjectId subject,int level, ObjectId type,ObjectId sco,
		//ObjectId clty, double score, String question, String answer,String parseAnser,
		//ObjectiveItem item,ObjectId zhangjie1,ObjectId zhangjie2,List<Integer> gradeList,Integer origItemType
		
//		    ItemPoolEntry itemPoolEntry = new ItemPoolEntry(
//		    		xueDuanPair.getId(), //xueduan
//		    		xuekeEntry.getID(), //subject
//		        level,  //level
//		        type, //type
//		        scope,
//		        clty,
//		        (double)itemPoolInfo.getPoint(), //score
//		        question,
//		        answer,
//		        paranswer,//parse answer
//		        objectiveItem,//item
//		        danyuanEntry.getID(),
//		        keEntry.getID(),
//		        gradeList,
//		        exerciseItemType.getType()
//		    );
//
//
//		    ObjectId insertId=   itemPoolDao.addItemPoolEntry(itemPoolEntry);
//		    FileUtils.write(f, "\r\n",true);
//			FileUtils.write(f,itemPoolInfo.getTid() +":"+insertId.toString(),true);
	}
    //解析并上传图片
    private String dealHtml(String html){
        
        while(html.indexOf("Mypicpath")!=-1){
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

    //试题表20140703 
    public static void main(String[] args){
        TransferItemPool transferItemPool = new TransferItemPool();
        transferItemPool.transfer();
    }*/
}