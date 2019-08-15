package com.fulaan.simplelog.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;

import com.db.log.SimpleLogDao;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.app.IdNameValuePairDTO;
import com.pojo.app.ModuleApps;
import com.pojo.log.SimpleLogEntry;
import com.pojo.log.SimpleLogLoginStatDTO;
import com.pojo.log.SimpleLogStatDTO;
import com.pojo.log.SimpleLogTimes;
import com.pojo.user.UserRole;
import com.sys.constants.Constant;
import com.sys.utils.DateTimeUtils;

/**
 * simple log service
 * @author fourer
 *
 */
public class SimpleLogService {

	private SimpleLogDao simpleLogDao =new SimpleLogDao();
	
	/**
	 * 批量插入
	 * @param list
	 */
	public void addSimpleLogs(List<SimpleLogEntry> list)
	{
		simpleLogDao.addSimpleLogs(list);
	}
	
	/**
	 * 
	 * @param beginTime 查询开始时间
	 * @param endTime 查询结束时间
	 * @param userIds 用户
	 * @param schoolIds 学校
	 * @param pfs 平台
	 * @param apps 应用
	 * @return
	 */
	public List<SimpleLogEntry> getSimpleLogEntry(Long beginTime,Long endTime, Collection<ObjectId> userIds,Collection<ObjectId> schoolIds, Collection<Integer> pfs, Collection<Integer> apps,DBObject fields)
	{
		return simpleLogDao.getSimpleLogEntry(beginTime, endTime, userIds, schoolIds, pfs, apps,fields);
	}
	
	
	
	/**
	 * 第一种柱状图
	 * @param type 查询类型 1本周 2本月 3半年 4一年 100全部
	 * @param apps
	 * @return
	 */
	public List<IdNameValuePairDTO> getAppStat(ObjectId schoolId,         int type,String apps)
	{
		List<Integer> appList=      getApps(apps);
		long endTime =System.currentTimeMillis();
		long beginTime =getBeginTime(type);
		List<SimpleLogEntry> list=getSimpleLogEntry(beginTime, endTime, null, Arrays.asList(schoolId), null, appList,new BasicDBObject("app",1).append("_id", 0));
		Map<Integer, Integer> countMap =new HashMap<Integer, Integer>();
		for(Integer i:appList)
		{
			countMap.put(i, 0);
		}
		
		for(SimpleLogEntry se:list)
		{
			if(!countMap.containsKey(se.getApp()))
			{
				countMap.put(se.getApp(), 0);
			}
			countMap.put(se.getApp(), countMap.get(se.getApp())+1);
		}
		
		List<IdNameValuePairDTO> retList =new ArrayList<IdNameValuePairDTO>();
		
		for(Map.Entry<Integer, Integer> entry:countMap.entrySet())
		{
			ModuleApps app=ModuleApps.getModuleApps(entry.getKey());
			
			IdNameValuePairDTO dto =new IdNameValuePairDTO(new ObjectId(), app.getName(), entry.getValue());
			retList.add(dto);
		}
		
		return retList;
	}
	
	
	

	
	
	/**
	 * 第二种折线图
	 * @param schoolId
	 * @param type
	 * @param apps
	 * @return
	 * @throws ParseException 
	 */
	public List<SimpleLogStatDTO> getSimpleLogStatDTO (ObjectId schoolId,   int type,String apps) throws ParseException
	{
		List<Integer> appList= getApps(apps);
		long endTime =System.currentTimeMillis();
		List<SimpleLogTimes> times=calSimpleLogTimes(type);
		long beginTime =times.get(0).getBeginTime();
		
		Map<Integer, SimpleLogStatDTO> dtoMap =new HashMap<Integer, SimpleLogStatDTO>();
		
		for(Integer i:appList)
		{
			ModuleApps app=ModuleApps.getModuleApps(i);
			SimpleLogStatDTO dto =new SimpleLogStatDTO(app, times);
			dtoMap.put(i, dto);
		}
		
		List<SimpleLogEntry> list=getSimpleLogEntry(beginTime, endTime, null, Arrays.asList(schoolId), null, appList,new BasicDBObject("app",1));
		
		SimpleLogStatDTO dto;
		for(SimpleLogEntry s:list)
		{
			dto=dtoMap.get(s.getApp());
			if(null!=dto)
			{
				dto.increase(s.getID().getDate().getTime());
			}
		}
		return new ArrayList<SimpleLogStatDTO>(dtoMap.values());
	}
	
	
	/**
	 * 登录统计（只统计登录情况）
	 * @param schoolId
	 * @param type
	 * @param apps
	 * @return
	 */
	public SimpleLogLoginStatDTO getLoginStat(Collection<ObjectId> schoolIds, int type)
	{
		
		List<Integer> appList=  Arrays.asList(0);
		long endTime =System.currentTimeMillis();
		long beginTime =getBeginTime(type);
		
		Map<String, IdNameValuePairDTO> map=new HashMap<String, IdNameValuePairDTO>();
		long calTime=beginTime;
		while(true)
		{
			if(calTime<=endTime)
			{
				String key=DateTimeUtils.convert(calTime, DateTimeUtils.DATE_YYYY_MM_DD);
				
				IdNameValuePairDTO dto=new IdNameValuePairDTO(new ObjectId(), key, 0);
				dto.setValue1(calTime);
				
				map.put(key,dto );
				
				calTime+=Constant.MS_IN_DAY;
			}
			else
			{
				break;
			}
		}
		
		int teacherCount=0;
		int studentCount=0;
	    int parentCount=0;
		List<SimpleLogEntry> list=getSimpleLogEntry(beginTime, endTime, null, schoolIds, null, appList,new BasicDBObject("r",1));
		for(SimpleLogEntry se:list)
		{
			if(UserRole.isStudent(se.getRole()))
			{
				studentCount++;
			}
			else if(UserRole.isParent(se.getRole()))
			{
				parentCount++;
			}else
			{
				teacherCount++;
			}
			String key=DateTimeUtils.convert(se.getID().getDate().getTime(), DateTimeUtils.DATE_YYYY_MM_DD);
			IdNameValuePairDTO dto=	map.get(key);
			if(null!=dto)
			{
					dto.setValue((Integer)dto.getValue()+1);
			}
		}
        List<IdNameValuePairDTO> statList=new ArrayList<IdNameValuePairDTO>(map.values());
        
        
        Collections.sort(statList, new Comparator<IdNameValuePairDTO>() {
			@Override
			public int compare(IdNameValuePairDTO arg0, IdNameValuePairDTO arg1) {

				 long l=(Long)arg0.getValue1()-(Long)arg1.getValue1();
				 if(l>0)
				 {
					 return 1;
				 }
				 if(l<0)
				 {
					 return -1;
				 }
				 return 0;
			}
		});
        
		SimpleLogLoginStatDTO dto =new SimpleLogLoginStatDTO(teacherCount, studentCount, parentCount, statList);
		return dto;
	}
	
	
	
	/**
	 * 1当日 2本周 3 本月 4半年 全部
	 * @param type
	 * @return
	 */
	private Long getBeginTime(int type)
	{
		 Calendar cal = Calendar.getInstance();  
		 
		 if(1==type) //当日
		 {
			 cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
			 return  cal.getTime().getTime();
		 }
		 if(2==type) //本周
		 {
		     cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);  
		     cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);  
		     return cal.getTime().getTime();
		 }
		 if(3==type) //本月
		 {
		      cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);  
		      cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));  
		      return cal.getTime().getTime();  
		 }
		 
		 if(4==type) //半年
		 {
		    return  System.currentTimeMillis()-Constant.MS_IN_DAY*180;
		 }
		 
		 return  System.currentTimeMillis()-Constant.MS_IN_DAY*365;
	}
	
	
	private List<Integer> getApps(String apps) {
		List<Integer> appList =new ArrayList<Integer>();
		String[] appArr=apps.split(Constant.COMMA);
		for(String app:appArr)
		{
			appList.add(Integer.valueOf(app));
		}
		return appList;
	}
	
	

	//1本周 2本月 3半年 4一年 
	public List<SimpleLogTimes> calSimpleLogTimes(int type) throws ParseException
	{
		List<SimpleLogTimes> times=new ArrayList<SimpleLogTimes>();
		if(type==1) //本周
		{
			 Calendar cal = Calendar.getInstance();  
		     cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);  
			 for(int i=1;i<=7;i++)
			 {
				 cal.set(Calendar.DAY_OF_WEEK, i);  
			     long beginTime=  cal.getTime().getTime();
			     long endTime=beginTime+Constant.MS_IN_DAY;
			    // if(endTime<System.currentTimeMillis())
			     {
			    	 times.add(new SimpleLogTimes(beginTime, endTime, DateTimeUtils.convert(beginTime, DateTimeUtils.FORMAT)));
			     }
			 }
		}
		if(type==2) //本月
		{
		     Calendar   cal_1=Calendar.getInstance();
		     int  weekIndex=1;
		     SimpleLogTimes sts=null;
		     for(int i=1;i<=31;i++)
		     {
		        cal_1.set(Calendar.DAY_OF_MONTH,i);
		        cal_1.set(Calendar.HOUR_OF_DAY,0);
		        cal_1.set(Calendar.MINUTE,0);
		      
		        if(cal_1.getTime().getTime()>System.currentTimeMillis())
		        {
		        	SimpleLogTimes	newSts =new SimpleLogTimes(sts.getBeginTime(),System.currentTimeMillis() , sts.getName());
		        	times.add(newSts);
		        	break;
		        }
		        
		        
		        if(i==1)
		        {
		        	sts =new SimpleLogTimes(cal_1.getTime().getTime(),cal_1.getTime().getTime() , "第"+weekIndex+"周");
		        	continue;
		        }
		        int  thisWeekIndex = cal_1.get(Calendar.WEEK_OF_MONTH);
		        if(thisWeekIndex!=weekIndex)
		        {
		        	SimpleLogTimes	newSts =new SimpleLogTimes(sts.getBeginTime(),cal_1.getTime().getTime() , sts.getName());
		        	times.add(newSts);
		        	weekIndex=thisWeekIndex;
		        	sts =new SimpleLogTimes(cal_1.getTime().getTime(),cal_1.getTime().getTime() , "第"+weekIndex+"周");
		        }
		     }
		}
		
		if(type==3) //本月
		{
			
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM"); 
	        
	        for(int i=-5;i<=0;i++)
	        {
	        	 
	 			//获取当前月第一天：
	 	        Calendar c = Calendar.getInstance();   
	 	        c.add(Calendar.MONTH, i);
	 	        c.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天
	 	        c.set(Calendar.HOUR_OF_DAY,0);
		        c.set(Calendar.MINUTE,0);
	 	        
	 	       
	 	        //获取当前月最后一天
	 	        Calendar ca = Calendar.getInstance();   
	 	        ca.add(Calendar.MONTH, i);
	 	        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH)); 
	 	        ca.set(Calendar.HOUR_OF_DAY,0);
		        ca.set(Calendar.MINUTE,0);
	 	        SimpleLogTimes slt =new SimpleLogTimes(c.getTime().getTime(), ca.getTime().getTime()+Constant.MS_IN_DAY, format.format(c.getTime()));
	 	        times.add(slt);
	        }

		}
		
		
		if(type==4) //一年
		{
			
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM"); 
	        
	        for(int i=-11;i<=0;i++)
	        {
	        	 
	 			//获取当前月第一天：
	 	        Calendar c = Calendar.getInstance();   
	 	        c.add(Calendar.MONTH, i);
	 	        c.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天
	 	        c.set(Calendar.HOUR_OF_DAY,0);
		        c.set(Calendar.MINUTE,0);
	 	        //获取当前月最后一天
	 	        Calendar ca = Calendar.getInstance();   
	 	        ca.add(Calendar.MONTH, i);
	 	        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH)); 
	 	        ca.set(Calendar.HOUR_OF_DAY,0);
		        ca.set(Calendar.MINUTE,0);
	 	        SimpleLogTimes slt =new SimpleLogTimes(c.getTime().getTime(), ca.getTime().getTime()+Constant.MS_IN_DAY, format.format(c.getTime()));
	 	        times.add(slt);
	        }

		}
		
		
		return times;
	}
	
	
	
	
	
	
	
	public static void main(String[] args) throws ParseException {
		
		SimpleLogService ss =new SimpleLogService();
		
		List<SimpleLogTimes> list =ss.calSimpleLogTimes(1);
		for(int i=0;i<list.size();i++)
		{
			System.out.println(list.get(i));
		}
		
	}
}
