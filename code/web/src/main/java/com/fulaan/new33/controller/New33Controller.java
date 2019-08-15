package com.fulaan.new33.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.fulaan.annotation.UserDataCollection;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fulaan.annotation.ObjectIdType;
import com.fulaan.annotation.SessionNeedless;
import com.fulaan.base.controller.BaseController;
import com.fulaan.new33.dto.isolate.N33_KSDTO;
import com.fulaan.new33.service.SchoolSelectLessonSetService;
import com.fulaan.new33.service.isolate.IsolateSubjectService;
import com.fulaan.school.service.SchoolService;
import com.pojo.app.IdNameValuePair;
import com.pojo.app.IdValuePairDTO;
import com.pojo.school.Subject;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;

/**
 * new33父类controller
 * @author fourer
 */
@Controller
@RequestMapping("/new33")
public class New33Controller extends BaseController {

	 private static final Logger logger=Logger.getLogger(New33Controller.class);
	
	 private SchoolService schoolService =new SchoolService();
	 @Autowired
	 private IsolateSubjectService subjectService;
	 /**
	  * 页面跳转
	  * @param p
	  * @return
	  */
	 @RequestMapping("/{p}")
	 @ResponseBody
	 public ModelAndView pages(@PathVariable("p") String p)
	 {
		   logger.info(p);
		   if(p.equalsIgnoreCase("set"))
		   {
	           return new ModelAndView("new33/schoolSelectLessonSet");
		   }
		   return new ModelAndView("new33/"+p);
	 }
	
	/**
	 * 得到学期集合
	 * @param schoolId
	 * @return
	 */
	@RequestMapping("/terms")
	@ResponseBody
	public RespObj getTerms()
	{
		List<IdNameValuePair> list =new ArrayList<IdNameValuePair>();
		list.add(new IdNameValuePair(new ObjectId("5a7cfd14afbb8a2330946ae5"), "2017学年第一学期", ""));
		list.add(new IdNameValuePair(new ObjectId("5a5eac4a0d206833a8b09f54"), "2017学年第二学期", ""));
		
		return new RespObj(Constant.SUCCESS_CODE, list);
	}
	
	/**
	 * 学校科目集合
	 * @return
	 */
	@UserDataCollection(pageTag = "n33", params = {"gradeId"}, isAllCollection = false)
	@RequestMapping("/subjects")
	@ResponseBody
	public RespObj getSubjects(String termId,String gradeId)
	{
		
	    List<String> subjectNames = new ArrayList<String>();
	    Collections.addAll(subjectNames, "物理", "化学", "生物", "政治", "历史", "地理");
		List<IdValuePairDTO> list =new ArrayList<IdValuePairDTO>();
		List<N33_KSDTO> subjectList = subjectService.getIsolateSubjectByGradeId(new ObjectId(termId), getSchoolId(), gradeId);
		for(N33_KSDTO dto:subjectList) {
			if(subjectNames.contains(dto.getSnm())) {
				list.add(new IdValuePairDTO(new ObjectId(dto.getSubid()), dto.getSnm()));
			}
		}
		Collections.sort(list, new Comparator<IdValuePairDTO>() {
			@Override
			public int compare(IdValuePairDTO o1, IdValuePairDTO o2) {
				
				return SchoolSelectLessonSetService.subject_sort.get(o1.getValue())-SchoolSelectLessonSetService.subject_sort.get(o2.getValue());
			}
			
		});
		return new RespObj(Constant.SUCCESS_CODE, list);
	}
}
