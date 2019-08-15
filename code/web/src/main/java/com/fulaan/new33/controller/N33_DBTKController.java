package com.fulaan.new33.controller;

import com.fulaan.base.controller.BaseController;
import com.fulaan.new33.service.N33_ZKB_TempService;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/dbtk")
public class N33_DBTKController extends BaseController {

	@Autowired
	private N33_ZKB_TempService zkb_tempService;
	/**
	 * 获取教学班
	 *
	 * @param zkbId
	 * @return
	 */
	@RequestMapping("/getJXBById")
	@ResponseBody
	public RespObj getJXBById(String zkbId, String gradeId,Integer week) {
		RespObj obj = new RespObj(Constant.SUCCESS_CODE);
		try {
			obj.setMessage(zkb_tempService.getJXBById(zkbId, gradeId, getSchoolId(),week));
		} catch (Exception e) {
			e.printStackTrace();
			obj.setCode(Constant.FAILD_CODE);
			obj.setMessage("服务器正忙");
		}
		return obj;
	}

	/**
	 * 获取教学班
	 *
	 * @param
	 * @return
	 */
	@RequestMapping("/getJXBByXY")
	@ResponseBody
	public RespObj getJXBByXY(int x, int y, String xqid, String gradeId,Integer week) {
		RespObj obj = new RespObj(Constant.SUCCESS_CODE);
		try {
			obj.setMessage(zkb_tempService.getJXBByXY(x, y, new ObjectId(gradeId), new ObjectId(xqid), getSchoolId(),week));
		} catch (Exception e) {
			e.printStackTrace();
			obj.setCode(Constant.FAILD_CODE);
			obj.setMessage("服务器正忙");
		}
		return obj;
	}
}
