package com.fulaan.new33.controller.autopk;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.fulaan.base.controller.BaseController;
import com.fulaan.new33.service.autopk.N33_AutoLevelService;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;

@Controller
@RequestMapping("/autoPk/autolevel")
public class N33_AutoLevelController extends BaseController {
	
	@Autowired
	private N33_AutoLevelService	autoLevelService;
	
	@ResponseBody
	@RequestMapping("/autoLabelLevel")
	public String autoLabelLevel(@RequestBody Map repMap) {
		RespObj resp = new RespObj(Constant.SUCCESS_CODE, "成功");
		try {
			repMap.put("schoolId", getSchoolId());
			autoLevelService.autoLabelLevel(repMap);
		} catch (Exception e) {
			resp = new RespObj(Constant.FAILD_CODE, "失败");
			e.printStackTrace();
		}
		return JSON.toJSONString(resp);
	}

	
}
