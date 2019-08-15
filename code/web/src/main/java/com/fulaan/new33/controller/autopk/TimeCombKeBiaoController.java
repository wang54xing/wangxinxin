package com.fulaan.new33.controller.autopk;

import com.fulaan.base.controller.BaseController;
import com.fulaan.new33.dto.autopk.N33_TimeCombKeBiaoDTO;
import com.fulaan.new33.dto.isolate.N33_SWDTO;
import com.fulaan.new33.service.autopk.N33_TimeCombKeBiaoService;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/n33TimeCombKeBiao")
public class TimeCombKeBiaoController extends BaseController {

	@Autowired
	private N33_TimeCombKeBiaoService timeCombKeBiaoService;

	/**
	 * 保存课表
	 * @param dto
	 * @return
	 */
	@RequestMapping("/saveTimeCombKeBiao")
	@ResponseBody
	public RespObj saveTimeCombKeBiao(@RequestBody N33_TimeCombKeBiaoDTO dto) {
		RespObj obj = new RespObj(Constant.SUCCESS_CODE);
		try {
			String schoolId = getSchoolId().toString();
			dto.setSchoolId(schoolId);
			obj.setMessage(timeCombKeBiaoService.saveTimeCombKeBiao(dto));
		} catch (Exception e) {
			e.printStackTrace();
			obj.setCode(Constant.FAILD_CODE);
			obj.setMessage("服务器正忙");
		}
		return obj;
	}

	/**
	 * 查询组合固定事务
	 * @param gradeId
	 * @param ciId
	 * @return
	 */
	@RequestMapping("/getTimeCombJxbCtList")
	@ResponseBody
	public RespObj getTimeCombJxbCtList(String gradeId, String xqId, String ciId, int type, int serial) {
		RespObj obj = new RespObj(Constant.SUCCESS_CODE);
		try {
			ObjectId schoolId = getSchoolId();
			List<Map<String, Object>> list =  timeCombKeBiaoService.getTimeCombJxbCtList(schoolId, new ObjectId(gradeId), new ObjectId(xqId), new ObjectId(ciId), type, serial);
			obj.setMessage(list);
		} catch (Exception e) {
			e.printStackTrace();
			obj.setCode(Constant.FAILD_CODE);
			obj.setMessage("服务器正忙");
		}
		return obj;
	}

	/**
	 * 自动创建课表
	 * @param gradeId
	 * @param ciId
	 * @return
	 */
	@RequestMapping("/autoBuildTimeCombKeBiao")
	@ResponseBody
	public RespObj autoBuildTimeCombKeBiao(String gradeId, String xqId, String ciId) {
		RespObj obj = new RespObj(Constant.SUCCESS_CODE);
		try {
			ObjectId schoolId = getSchoolId();
			timeCombKeBiaoService.autoBuildTimeCombKeBiao(schoolId, new ObjectId(gradeId), new ObjectId(xqId), new ObjectId(ciId));
			//obj.setMessage();
		} catch (Exception e) {
			e.printStackTrace();
			obj.setCode(Constant.FAILD_CODE);
			obj.setMessage("服务器正忙");
		}
		return obj;
	}

	/**
	 * 查询课表
	 *
	 * @param gradeId
	 * @param ciId
	 * @return
	 */
	@RequestMapping("/getTimeCombKeBiaoList")
	@ResponseBody
	public RespObj getTimeCombKeBiaoList(String gradeId, String ciId) {
		RespObj resp = new RespObj(Constant.SUCCESS_CODE, "成功");
		try {
			resp.setMessage(timeCombKeBiaoService.getTimeCombKeBiaoList(getSchoolId(), new ObjectId(gradeId), new ObjectId(ciId)));
		} catch (Exception e) {
			e.printStackTrace();
			resp = new RespObj(Constant.FAILD_CODE, "失败");
		}
		return resp;
	}

	/**
	 * 撤销课时排课课表
	 *
	 * @param gradeId
	 * @param ciId
	 * @return
	 */
	@RequestMapping("/cancelTimeCombKeBiao")
	@ResponseBody
	public RespObj cancelTimeCombKeBiao(String gradeId, String ciId, int x, int y) {
		RespObj resp = new RespObj(Constant.SUCCESS_CODE, "成功");
		try {
			timeCombKeBiaoService.cancelTimeCombKeBiao(getSchoolId(), new ObjectId(gradeId), new ObjectId(ciId), x, y);
		} catch (Exception e) {
			e.printStackTrace();
			resp = new RespObj(Constant.FAILD_CODE, "失败");
		}
		return resp;
	}
}
