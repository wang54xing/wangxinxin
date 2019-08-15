package com.fulaan.new33.controller.autopk;

import com.fulaan.base.controller.BaseController;
import com.fulaan.new33.service.autopk.N33_ZouBanSetService;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/pkRulesZouBan")
public class N33_ZouBanSetController extends BaseController {
	@Autowired
	private N33_ZouBanSetService zouBanSetService;

	/**
	 * 增加走班规则
	 * @param ciId
	 * @param flag
	 * @param gradeId
	 * @param type
	 * @param count
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/addZouBanSet")
	public RespObj addZouBanSet(@RequestParam String ciId, @RequestParam boolean flag, @RequestParam String gradeId, @RequestParam Integer type, @RequestParam(defaultValue = "-1") Integer count) {
		RespObj resp = new RespObj(Constant.SUCCESS_CODE, "成功");
		try {
			zouBanSetService.addZouBanSet(flag, new ObjectId(gradeId),getSchoolId(),new ObjectId(ciId),type,count);
		} catch (Exception e) {
			resp = new RespObj(Constant.FAILD_CODE, "失败");
			e.printStackTrace();
		}
		return resp;
	}

	/**
	 * 按照年级查询走班设置
	 * @param ciId
	 * @param gradeId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getZouBanSet")
	public RespObj getZouBanSet(@RequestParam String ciId, @RequestParam String gradeId) {
		RespObj resp = new RespObj(Constant.SUCCESS_CODE, "成功");
		try {
			resp.setMessage(zouBanSetService.getZouBanSet(new ObjectId(gradeId), getSchoolId(),new ObjectId(ciId)));
		} catch (Exception e) {
			resp = new RespObj(Constant.FAILD_CODE, "失败");
			e.printStackTrace();
		}
		return resp;
	}
}
