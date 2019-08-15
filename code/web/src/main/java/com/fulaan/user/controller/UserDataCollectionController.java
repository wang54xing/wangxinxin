package com.fulaan.user.controller;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fulaan.base.controller.BaseController;
import com.fulaan.cache.CacheHandler;
import com.pojo.school.GradeType;
import com.pojo.school.SchoolType;
import com.pojo.school.SubjectType;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;

/**
 * 用户数据收集
 * @author fourer
 *
 */
@Controller
@RequestMapping("/udc")
public class UserDataCollectionController extends BaseController {

	private static final String html="<option value={0} selected='selected'>{1}</option>";
	/**
	 * 获取页面数据
	 * @param pageTag
	 * @return
	 */
	@RequestMapping("/data")
	@ResponseBody
	public RespObj data(String pageTag)
	{
		String key=MessageFormat.format(CacheHandler.K6KT2_USER_DATA_COLLECTION, getUserId().toString(),pageTag);
		Map<String,String> cacheMap= CacheHandler.getMapValue(key);
		List<Map.Entry<String,String>> ll= new ArrayList<Entry<String, String>>(cacheMap.entrySet());
		return new RespObj(Constant.SUCCESS_CODE, ll);
	}
	
	
	
	
	/**
	 * 获取页面数据
	 * @param pageTag
	 * @return
	 */
	@RequestMapping("/html/data")
	@ResponseBody
	public RespObj htmlData(String pageTag)
	{
		String key=MessageFormat.format(CacheHandler.K6KT2_USER_DATA_COLLECTION, getUserId().toString(),pageTag);
		Map<String,String> cacheMap= CacheHandler.getMapValue(key);
		List<Map.Entry<String,String>> ll= new ArrayList<Entry<String, String>>(cacheMap.entrySet());
		
		
		for(Map.Entry<String,String> e:ll)
		{
			
			if(e.getKey().equalsIgnoreCase("schoolType"))
			{
				SchoolType sch =SchoolType.getSchoolTypeByInt(Integer.valueOf(e.getValue()));
				if(null!=sch)
				{
					String ht=MessageFormat.format(html, String.valueOf(sch.getType()),sch.getName());
					e.setValue(ht);
				}
			}
			
			if(e.getKey().equalsIgnoreCase("grade"))
			{
				GradeType gr =GradeType.getGradeType(Integer.valueOf(e.getValue()));
				if(null!=gr)
				{
					String ht=MessageFormat.format(html, String.valueOf(gr.getId()),gr.getName());
					e.setValue(ht);
				}
			}
			if(e.getKey().equalsIgnoreCase("subject"))
			{
				SubjectType sub =SubjectType.getSubjectType(Integer.valueOf(e.getValue()));
				if(null!=sub)
				{
					String ht=MessageFormat.format(html, String.valueOf(sub.getType()),sub.getName());
					e.setValue(ht);
				}
			}
		}
		return new RespObj(Constant.SUCCESS_CODE, ll);
	}
	
	
}
