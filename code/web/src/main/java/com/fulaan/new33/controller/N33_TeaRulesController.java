package com.fulaan.new33.controller;

import com.db.new33.paike.N33_TeaRulesDao;
import com.fulaan.base.controller.BaseController;
import com.fulaan.new33.dto.isolate.N33_TeaRulesDTO;
import com.fulaan.new33.service.N33_TeaRulesService;
import com.fulaan.new33.service.isolate.IsolateSubjectService;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by albin on 2018/3/16.
 */
@Controller
@RequestMapping("/teaRules")
public class N33_TeaRulesController extends BaseController {
	@Autowired
	private N33_TeaRulesService teaRulesService;
	@Autowired
	private IsolateSubjectService subjectService;


	/**
	 * 保存教师需求
	 * @throws Exception
	 */
	@RequestMapping("/addTeaRules")
	@ResponseBody
	public RespObj addTeaRules(@RequestBody N33_TeaRulesDTO dto){
		RespObj obj = new RespObj(Constant.SUCCESS_CODE);
		try {
			dto.setSid(getSchoolId().toString());
			dto.setTeaId(getUserId().toString());
			dto.setLev(1);
			teaRulesService.saveTeaRulesDTO(dto);
			obj.setMessage("保存成功");
		} catch (Exception e) {
			e.printStackTrace();
			obj.setCode(Constant.FAILD_CODE);
			obj.setMessage("服务器正忙");
		}
		return obj;
	}

	/**
	 * 保存教师需求
	 * @throws Exception
	 */
	@RequestMapping("/addTeaRulesByTeaId")
	@ResponseBody
	public RespObj addTeaRulesByTeaId(@RequestBody N33_TeaRulesDTO dto){
		RespObj obj = new RespObj(Constant.SUCCESS_CODE);
		try {
			dto.setSid(getSchoolId().toString());
			dto.setLev(1);
			teaRulesService.addTeaRulesByTeaId(dto);
			obj.setMessage("保存成功");
		} catch (Exception e) {
			e.printStackTrace();
			obj.setCode(Constant.FAILD_CODE);
			obj.setMessage("服务器正忙");
		}
		return obj;
	}

	/**
	 * 保存教师需求
	 * @throws Exception
	 */
	@RequestMapping("/addTeaRulesByUserId")
	@ResponseBody
	public RespObj addTeaRulesByUserId(@RequestBody N33_TeaRulesDTO dto){
		RespObj obj = new RespObj(Constant.SUCCESS_CODE);
		try {
			dto.setSid(getSchoolId().toString());
			dto.setTeaId(getUserId().toString());
			dto.setLev(1);
			teaRulesService.addTeaRulesByTeaId(dto);
			obj.setMessage("保存成功");
		} catch (Exception e) {
			e.printStackTrace();
			obj.setCode(Constant.FAILD_CODE);
			obj.setMessage("服务器正忙");
		}
		return obj;
	}

	@RequestMapping("/getTeaRules")
	@ResponseBody
	public RespObj getTeaRules(@RequestBody Map<String,Object> map){
		RespObj obj = new RespObj(Constant.SUCCESS_CODE);
		try {
			List<String> gradeList = (List<String>)map.get("gradeList");
			ObjectId xqid = new ObjectId((String) map.get("xqid"));
			obj.setMessage(teaRulesService.getTeaRulesDTOByXqidAndTeaId(gradeList,xqid,getUserId()));
		} catch (Exception e) {
			e.printStackTrace();
			obj.setCode(Constant.FAILD_CODE);
			obj.setMessage("服务器正忙");
		}
		return obj;
	}

	@RequestMapping("/getTeaRulesByTeaId")
	@ResponseBody
	public RespObj getTeaRulesByTeaId(@RequestBody Map<String,Object> map){
		RespObj obj = new RespObj(Constant.SUCCESS_CODE);
		try {
			List<String> gradeList = (List<String>)map.get("gradeList");
			ObjectId xqid = new ObjectId((String) map.get("xqid"));
			ObjectId userId = new ObjectId((String)map.get("teaId"));
			obj.setMessage(teaRulesService.getTeaRulesDTOByXqidAndTeaId(gradeList,xqid,userId));
		} catch (Exception e) {
			e.printStackTrace();
			obj.setCode(Constant.FAILD_CODE);
			obj.setMessage("服务器正忙");
		}
		return obj;
	}

	@RequestMapping("/getTeaRulesList")
	@ResponseBody
	public RespObj getTeaRulesList(@RequestBody Map<String,Object> map){
		RespObj obj = new RespObj(Constant.SUCCESS_CODE);
		try {
			List<String> teaList = (List<String>)map.get("teaList");
			if (teaList.size() == 0) {
				List<N33_TeaRulesDTO> retList = new ArrayList<N33_TeaRulesDTO>();
				obj.setMessage(retList);
				return obj;
			}
			ObjectId xqid = new ObjectId((String) map.get("xqid"));
			Integer status = new Integer((String) map.get("status"));
			obj.setMessage(teaRulesService.getTeaRulesDTOListByXqidAndTeaId(teaList,xqid,status));
		} catch (Exception e) {
			e.printStackTrace();
			obj.setCode(Constant.FAILD_CODE);
			obj.setMessage("服务器正忙");
		}
		return obj;
	}

	@RequestMapping("/getTeaRulesByXy")
	@ResponseBody
	public RespObj getTeaRulesByXy(@RequestBody Map<String,Object> map){
		RespObj obj = new RespObj(Constant.SUCCESS_CODE);
		try {
			Integer x = new Integer((String) map.get("x"));
			Integer y = new Integer((String) map.get("y"));
			List<String> teaList = (List<String>)map.get("teaList");
			if (teaList.size() == 0) {
				List<N33_TeaRulesDTO> retList = new ArrayList<N33_TeaRulesDTO>();
				obj.setMessage(retList);
				return obj;
			}
			ObjectId xqid = new ObjectId((String) map.get("xqid"));
			obj.setMessage(teaRulesService.getTeaRulesByXy(teaList,xqid,x,y));
		} catch (Exception e) {
			e.printStackTrace();
			obj.setCode(Constant.FAILD_CODE);
			obj.setMessage("服务器正忙");
		}
		return obj;
	}

	@RequestMapping("/syncLastTermTeaRules")
	@ResponseBody
	public RespObj syncLastTermTeaRules(@RequestParam String xqid){
		RespObj obj = new RespObj(Constant.SUCCESS_CODE);
		try {
			teaRulesService.syncLastTermTeaRules(getSchoolId().toString(), xqid,getUserId());
		} catch (Exception e) {
			e.printStackTrace();
			obj.setCode(Constant.FAILD_CODE);
			obj.setMessage("服务器正忙");
		}
		return obj;
	}

	@RequestMapping("/getSubjectByXQIDAndGid")
	@ResponseBody
	public RespObj getSubjectByXQIDAndGid(@RequestParam String xqid,@RequestParam String gid){
		RespObj obj = new RespObj(Constant.SUCCESS_CODE);
		try {
			obj.setMessage(subjectService.getIsolateSubjectListByGradeIdAndXqid(new ObjectId(xqid),getSchoolId(),new ObjectId(gid)));
		} catch (Exception e) {
			e.printStackTrace();
			obj.setCode(Constant.FAILD_CODE);
			obj.setMessage("服务器正忙");
		}
		return obj;
	}

	@RequestMapping("/getTea")
	@ResponseBody
	public RespObj getTea(@RequestParam String xqid,@RequestParam String gid,@RequestParam String subId,@RequestParam String name){
		RespObj obj = new RespObj(Constant.SUCCESS_CODE);
		try {
			obj.setMessage(teaRulesService.getTea(new ObjectId(xqid),new ObjectId(gid),new ObjectId(subId),name));
		} catch (Exception e) {
			e.printStackTrace();
			obj.setCode(Constant.FAILD_CODE);
			obj.setMessage("服务器正忙");
		}
		return obj;
	}

	@RequestMapping("/agreeRules")
	@ResponseBody
	public RespObj agreeRules(@RequestParam String ruleId,@RequestParam String eachId){
		RespObj obj = new RespObj(Constant.SUCCESS_CODE);
		try {
			teaRulesService.agreeRules(new ObjectId(ruleId),new ObjectId(eachId));
		} catch (Exception e) {
			e.printStackTrace();
			obj.setCode(Constant.FAILD_CODE);
			obj.setMessage("服务器正忙");
		}
		return obj;
	}

	@RequestMapping("/refuseRules")
	@ResponseBody
	public RespObj refuseRules(@RequestParam String ruleId,@RequestParam String eachId){
		RespObj obj = new RespObj(Constant.SUCCESS_CODE);
		try {
			teaRulesService.refuseRules(new ObjectId(ruleId),new ObjectId(eachId));
		} catch (Exception e) {
			e.printStackTrace();
			obj.setCode(Constant.FAILD_CODE);
			obj.setMessage("服务器正忙");
		}
		return obj;
	}

	@RequestMapping("/deleteTeaRules")
	@ResponseBody
	public RespObj deleteTeaRules(@RequestBody Map<String,Object> map){
		RespObj obj = new RespObj(Constant.SUCCESS_CODE);
		try {
			List<String> gradeList = (List<String>) map.get("gradeList");
			ObjectId teaId = new ObjectId((String) map.get("teaId"));
			ObjectId xqid = new ObjectId((String)map.get("xqid"));
			ObjectId id = new ObjectId((String)map.get("id"));
			teaRulesService.deleteTeaRules(gradeList,teaId,xqid,id);
		} catch (Exception e) {
			e.printStackTrace();
			obj.setCode(Constant.FAILD_CODE);
			obj.setMessage("服务器正忙");
		}
		return obj;
	}

	@RequestMapping("/deleteTeaRulesByUserId")
	@ResponseBody
	public RespObj deleteTeaRulesByUserId(@RequestBody Map<String,Object> map){
		RespObj obj = new RespObj(Constant.SUCCESS_CODE);
		try {
			List<String> gradeList = (List<String>) map.get("gradeList");
			ObjectId xqid = new ObjectId((String)map.get("xqid"));
			ObjectId id = new ObjectId((String)map.get("id"));
			teaRulesService.deleteTeaRules(gradeList,getUserId(),xqid,id);
		} catch (Exception e) {
			e.printStackTrace();
			obj.setCode(Constant.FAILD_CODE);
			obj.setMessage("服务器正忙");
		}
		return obj;
	}

	@RequestMapping("/addOrUpdateRequireTime")
	@ResponseBody
	public RespObj addOrUpdateRequireTime(@RequestParam String xqid, @RequestParam String start, @RequestParam String end) {
		RespObj obj = new RespObj(Constant.SUCCESS_CODE);
		try {
			teaRulesService.updateRequireTime(getSchoolId(),new ObjectId(xqid),start,end);
		} catch (Exception e) {
			e.printStackTrace();
			obj.setCode(Constant.FAILD_CODE);
			obj.setMessage("服务器正忙");
		}
		return obj;
	}

	@RequestMapping("/getRequireTime")
	@ResponseBody
	public RespObj getRequireTime(@RequestParam String xqid) {
		RespObj obj = new RespObj(Constant.SUCCESS_CODE);
		try {
			obj.setMessage(teaRulesService.getRequireTime(getSchoolId(),new ObjectId(xqid)));
		} catch (Exception e) {
			e.printStackTrace();
			obj.setCode(Constant.FAILD_CODE);
			obj.setMessage("服务器正忙");
		}
		return obj;
	}


}
