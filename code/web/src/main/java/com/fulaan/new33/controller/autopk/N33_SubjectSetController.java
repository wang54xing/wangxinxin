package com.fulaan.new33.controller.autopk;

import com.fulaan.base.controller.BaseController;
import com.fulaan.new33.service.autopk.N33_SubjectSetService;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping("/pkRulesSubject")
public class N33_SubjectSetController extends BaseController {
	@Autowired
	private N33_SubjectSetService subjectSetService;

	/**
	 * 设置类型是1和2的学科规则
	 * @param map
	 */
	@ResponseBody
	@RequestMapping("/addSubjectSetTypeIs1Or2")
	public void addSubjectSet(@RequestBody Map<String,Object> map){
		map.put("sid",getSchoolId().toString());
		subjectSetService.addSubjectSet(map);
	}

	/**
	 * 设置类型是3的学科规则
	 * @param map
	 */
	@ResponseBody
	@RequestMapping("/addSubjectSetType3")
	public void addSubjectSetType3(@RequestBody Map<String,Object> map){
		map.put("sid",getSchoolId().toString());
		subjectSetService.addSubjectSet(map);
	}

	/**
	 * 設置類型是4的学科规则
	 * @param map
	 */
	@ResponseBody
	@RequestMapping("/addSubjectSetType4")
	public void addSubjectSetType4(@RequestBody Map<String,Object> map){
		map.put("sid",getSchoolId().toString());
		subjectSetService.addSubjectSet(map);
	}

	/**
	 * 查找年级所有的学科设置的规则
	 * @param ciId
	 * @param gradeId
	 */
	@ResponseBody
	@RequestMapping("/getSubjectSetDTOByCiIdAndGradeId")
	public RespObj getSubjectSetDTOByCiIdAndGradeId(@RequestParam String ciId, @RequestParam String gradeId){
		RespObj obj = new RespObj(Constant.SUCCESS_CODE);
		try {
			obj.setMessage(subjectSetService.getSubjectSetDTOByCiIdAndGradeId(getSchoolId(),new ObjectId(gradeId),new ObjectId(ciId)));
		} catch (Exception e) {
			e.printStackTrace();
			obj.setCode(Constant.FAILD_CODE);
			obj.setMessage("服务器正忙");
		}
		return obj;
	}
}
