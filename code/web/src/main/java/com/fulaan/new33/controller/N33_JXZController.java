package com.fulaan.new33.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fulaan.new33.service.N33_ZKBService;
import com.fulaan.new33.service.PaiKeService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fulaan.base.controller.BaseController;
import com.fulaan.new33.dto.isolate.N33_JXZDTO;
import com.fulaan.new33.dto.isolate.TermDTO;
import com.fulaan.new33.service.N33_JXZService;
import com.fulaan.new33.service.isolate.IsolateTermService;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;

@Controller
@RequestMapping("/n33_jxz")
public class N33_JXZController extends BaseController {

	private IsolateTermService isolateTermService = new IsolateTermService();
	private N33_JXZService N33_JXZService = new N33_JXZService();
	@Autowired
	private N33_ZKBService zkbService;
	
	/**
	 * @param year
	 * @return
	 */
	@RequestMapping("/getTermByYear")
	@ResponseBody
	public List<TermDTO> getTermByYear(Integer year) {
		return isolateTermService.getTermByYear(getSchoolId(), year);
	}
	
	@RequestMapping("/saveTerm")
	@ResponseBody
	public RespObj saveTerm(Integer year,String start,String end,String start2,String end2,String yearName) {
		RespObj resp = new RespObj(Constant.SUCCESS_CODE, "成功");
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			N33_JXZService.saveTerm(getSchoolId(), year, start, end, start2, end2,yearName);
			
		} catch (Exception e) {
			e.printStackTrace();
			resp = new RespObj(Constant.FAILD_CODE, "失败");
		}
		return resp;
	}
	
	/**
	 * 根据学期获得教学周
	 * @param xqid
	 * @return
	 */
	@RequestMapping("/getListByXq")
	@ResponseBody
	public List<N33_JXZDTO> getListByXq(@RequestParam String xqid){
		List<N33_JXZDTO> list = new ArrayList<N33_JXZDTO>();
		try {
			 list = N33_JXZService.getListByXq(getSchoolId(), new ObjectId(xqid));
		}catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 获取教学周
	 * @return
     */
	@RequestMapping("/getTermByXq")
	@ResponseBody
	public List<N33_JXZDTO> getTermByXq(){
		List<N33_JXZDTO> list = new ArrayList<N33_JXZDTO>();
		try {
			list = N33_JXZService.getTermByXq(getSchoolId(), getDefauleTermId());
		}catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 获取当前周
	 * @return
     */
	@RequestMapping("/getCurrentJXZ")
	@ResponseBody
	public N33_JXZDTO getCurrentJXZ(){
		N33_JXZDTO jxz = new N33_JXZDTO();
		try {
			jxz = N33_JXZService.getCurrentJXZ(getSchoolId(), getDefauleTermId());
		}catch (Exception e) {
			e.printStackTrace();
		}
		return jxz;
	}


	/**
	 * 获取当前周
	 * @return
	 */
	@RequestMapping("/getGradeWeek")
	@ResponseBody
	public Map<String,Integer> getCurrentJXZ(@RequestParam String xqid){
		Map<String,Integer> map = new HashMap<String, Integer>();
		try {
			Integer serial = null;
			serial = zkbService.getGradeWeek(getSchoolId(), new ObjectId(xqid));
			map.put("serial",serial);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
}
