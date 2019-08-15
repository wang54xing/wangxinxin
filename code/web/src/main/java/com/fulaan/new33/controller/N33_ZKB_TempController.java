package com.fulaan.new33.controller;

import com.fulaan.base.controller.BaseController;
import com.fulaan.new33.service.N33_ZKB_TempService;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("/n33_zkbtemp")
public class N33_ZKB_TempController extends BaseController {
	@Autowired
	private N33_ZKB_TempService zkb_tempService;

	/**
	 * 课表插课
	 *
	 * @param termId
	 * @param week
	 * @return
	 */
	@RequestMapping("/initN33_ZKBTemp")
	@ResponseBody
	public RespObj initN33_ZKBTemp(@RequestParam String termId, @RequestParam Integer week) {
		RespObj obj = new RespObj(Constant.SUCCESS_CODE);
		try {
			zkb_tempService.initN33_ZKBTemp(new ObjectId(termId),getSchoolId(),week);
		} catch (Exception e) {
			e.printStackTrace();
			obj.setCode(Constant.FAILD_CODE);
			obj.setMessage("服务器正忙");
		}
		return obj;
	}

	/**
	 * 保存课表
	 *
	 * @param termId
	 * @param week
	 * @return
	 */
	@RequestMapping("/saveKeBiao")
	@ResponseBody
	public RespObj saveKeBiao(@RequestParam String termId, @RequestParam Integer week,@RequestParam Integer tkType,@RequestParam Integer eWeek,HttpServletRequest request) {
		RespObj obj = new RespObj(Constant.SUCCESS_CODE);
		try {
			Map<String,Object> result= zkb_tempService.saveKeBiao(new ObjectId(termId),getSchoolId(),week,tkType,getUserId(),eWeek,request);
			obj.setMessage(result);
		} catch (Exception e) {
			e.printStackTrace();
			obj.setCode(Constant.FAILD_CODE);
			obj.setMessage("服务器正忙");
		}
		return obj;
	}

	/**
	 * 加载课表list(包括课表结构，内容)
	 *
	 * @param gradeId     //年级集合
	 * @param classRoomId //教室集合
	 * @return
	 */
	@RequestMapping("/getKeBiaoListTemp")
	@ResponseBody
	public RespObj getKeBiaoListTemp(@RequestParam String gradeId, @RequestParam String classRoomId, @RequestParam String termId,@RequestParam Integer week) {
		RespObj obj = new RespObj(Constant.SUCCESS_CODE);
		try {
			Map map = zkb_tempService.getKeBiaoList(new ObjectId(termId), gradeId, classRoomId, getSchoolId(),week);
			obj.setMessage(map);
		} catch (Exception e) {
			e.printStackTrace();
			obj.setCode(Constant.FAILD_CODE);
			obj.setMessage("服务器正忙");
		}
		return obj;
	}

	/**
	 * 撤回周课表
	 *
	 * @param zkbId
	 * @return
	 */
	@RequestMapping("/undoZKB_Temp")
	@ResponseBody
	public RespObj undoZKB_Temp(@RequestParam String zkbId,@RequestParam Integer type) {
		RespObj obj = new RespObj(Constant.SUCCESS_CODE);
		try {
			zkb_tempService.undoZKB_Temp(zkbId,type);
			obj.setMessage("撤销成功");
		} catch (Exception e) {
			e.printStackTrace();
			obj.setCode(Constant.FAILD_CODE);
			obj.setMessage("服务器正忙");
		}
		return obj;
	}

	/**
	 *
	 * @param week
	 * @param classRoomId
	 * @param gradeId
	 * @return
	 */
	@RequestMapping("/getJXBList")
	@ResponseBody
	public RespObj getJXBList(@RequestParam Integer week, @RequestParam String classRoomId,@RequestParam String gradeId,@RequestParam String zhenXqid,@RequestParam Integer type) {
		RespObj obj = new RespObj(Constant.SUCCESS_CODE);
		try {
			obj.setMessage(zkb_tempService.getJXBList(week,new ObjectId(classRoomId),new ObjectId(gradeId),getSchoolId(),new ObjectId(zhenXqid),type));
		} catch (Exception e) {
			e.printStackTrace();
			obj.setCode(Constant.FAILD_CODE);
			obj.setMessage("服务器正忙");
		}
		return obj;
	}

	/**
	 *
	 * @param week
	 * @param classRoomId
	 * @param jxbId
	 * @return
	 */
	@RequestMapping("/getConflictedSettledJXBByRoomId")
	@ResponseBody
	public RespObj getConflictedSettledJXBByRoomId(@RequestParam Integer week, @RequestParam String classRoomId,@RequestParam String jxbId,@RequestParam String zhenXqid) {
		RespObj obj = new RespObj(Constant.SUCCESS_CODE);
		try {
			obj.setMessage(zkb_tempService.getConflictedSettledJXBByRoomId(new ObjectId(zhenXqid),jxbId,classRoomId,getSchoolId(),week));
		} catch (Exception e) {
			e.printStackTrace();
			obj.setCode(Constant.FAILD_CODE);
			obj.setMessage("服务器正忙");
		}
		return obj;
	}

	/**
	 * 设置走班时间
	 * @param
	 * @return
	 */
	@RequestMapping("/getZouBanTime")
	@ResponseBody
	public RespObj getZouBanTime(@RequestParam String jxbId,@RequestParam String gid) {
		RespObj resp = new RespObj(Constant.SUCCESS_CODE, "成功");
		try {
			resp.setMessage(zkb_tempService.getZouBanTime(getSchoolId(),new ObjectId(jxbId),new ObjectId(gid)));
		} catch (Exception e) {
			resp = new RespObj(Constant.FAILD_CODE, "失败");
			e.printStackTrace();
		}
		return resp;
	}

	/**
	 * 课表插课
	 *
	 * @param zkbId
	 * @param jxbId
	 * @return
	 */
	@RequestMapping("/saveZKB_Temp")
	@ResponseBody
	public RespObj saveZKB_Temp(String zkbId, String jxbId,Integer week,Integer type) {
		RespObj obj = new RespObj(Constant.SUCCESS_CODE);
		try {
			zkb_tempService.saveZKB_Temp(zkbId, jxbId,week,type);
		} catch (Exception e) {
			e.printStackTrace();
			obj.setCode(Constant.FAILD_CODE);
			obj.setMessage("服务器正忙");
		}
		return obj;
	}


	@RequestMapping("/reloadKB_Temp")
	@ResponseBody
	public RespObj reloadKB_Temp(String termId,Integer week,Integer type) {
		RespObj obj = new RespObj(Constant.SUCCESS_CODE);
		try {
			zkb_tempService.reloadKB_Temp(termId,getSchoolId(),week,type);
		} catch (Exception e) {
			e.printStackTrace();
			obj.setCode(Constant.FAILD_CODE);
			obj.setMessage("服务器正忙");
		}
		return obj;
	}

	@RequestMapping("/initIsSubmit")
	@ResponseBody
	public RespObj initIsSubmit(String termId,Integer week,Integer tk) {
		RespObj obj = new RespObj(Constant.SUCCESS_CODE);
		try {
			zkb_tempService.initIsSubmit(new ObjectId(termId),getSchoolId(),week,tk);
		} catch (Exception e) {
			e.printStackTrace();
			obj.setCode(Constant.FAILD_CODE);
			obj.setMessage("服务器正忙");
		}
		return obj;
	}

	/**
	 * 判斷該周多步调课是否提交
	 * @param termId
	 * @param week
	 * @return
	 */
	@RequestMapping("/getCount")
	@ResponseBody
	public RespObj getCount(String termId,Integer week,Integer tk) {
		RespObj obj = new RespObj(Constant.SUCCESS_CODE);
		try {
			obj.setMessage(zkb_tempService.getCount(new ObjectId(termId),getSchoolId(),week,tk));
		} catch (Exception e) {
			e.printStackTrace();
			obj.setCode(Constant.FAILD_CODE);
			obj.setMessage("服务器正忙");
		}
		return obj;
	}

	/**
	 * 判斷該周多步调课是否可以提交
	 * @param termId
	 * @param week
	 * @return
	 */
	@RequestMapping("/judgeIsCanSave")
	@ResponseBody
	public RespObj judgeIsCanSave(Integer week,String termId,Integer type) {
		RespObj obj = new RespObj(Constant.SUCCESS_CODE);
		try {
			obj.setMessage(zkb_tempService.judgeIsCanSave(new ObjectId(termId),getSchoolId(),week,type));
		} catch (Exception e) {
			e.printStackTrace();
			obj.setCode(Constant.FAILD_CODE);
			obj.setMessage("服务器正忙");
		}
		return obj;
	}


	/**
	 * 该周多步调课已经有更改，不能提交
	 * @param termId
	 * @param week
	 * @return
	 */
	@RequestMapping("/updateCount")
	@ResponseBody
	public RespObj updateCount(String termId,Integer week,Integer tk,boolean submit) {
		RespObj obj = new RespObj(Constant.SUCCESS_CODE);
		try {
			zkb_tempService.updateCount(new ObjectId(termId),getSchoolId(),week,tk,submit);
		} catch (Exception e) {
			e.printStackTrace();
			obj.setCode(Constant.FAILD_CODE);
			obj.setMessage("服务器正忙");
		}
		return obj;
	}

	@ResponseBody
	@RequestMapping("/getStatusSave")
	public Map<String, Object> getStatusSave(HttpServletRequest request) {
		return zkb_tempService.getStatus(request);
	}

}
