package com.fulaan.new33.controller;

import java.text.ParseException;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fulaan.base.controller.BaseController;
import com.fulaan.new33.dto.isolate.CourseRangeDTO;
import com.fulaan.new33.dto.isolate.DayRangeDTO;
import com.fulaan.new33.service.isolate.CourseRangeService;
import com.fulaan.new33.service.isolate.DayRangeService;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;
@Controller
@RequestMapping("/courseset")
public class CourseSetController extends BaseController {

	private CourseRangeService courseRangeService = new CourseRangeService();
	private DayRangeService dayRangeService = new DayRangeService();

	/**
	 * 根据学期id获取课表结构
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getListBySchoolId")
	public List<CourseRangeDTO> getListBySchoolId(@RequestParam String xqid){
		return courseRangeService.getListBySchoolId(getSchoolId().toString(), new ObjectId(xqid));
	}


	@ResponseBody
	@RequestMapping("/getListBySchoolIdZKB")
	public List<CourseRangeDTO> getListBySchoolIdZKB(@RequestParam String xqid){
		return courseRangeService.getListBySchoolIdZKB(getSchoolId().toString(), new ObjectId(xqid));
	}

	@ResponseBody
	@RequestMapping("/getListBySchoolIdForKB")
	public List<CourseRangeDTO> getListBySchoolIdForKB(@RequestParam String xqid,@RequestParam Integer week,@RequestParam String gradeId){
		return courseRangeService.getListBySchoolIdForKB(getSchoolId().toString(), new ObjectId(xqid),week,new ObjectId(gradeId));
	}
	@ResponseBody
	@RequestMapping("/syncLastTermRange")
	public RespObj syncLastTermRange(String xqid) {
		RespObj resp = new RespObj(Constant.SUCCESS_CODE, "成功");
		try {
			courseRangeService.syncLastTermRange(getSchoolId().toString(), xqid);
		} catch (Exception e) {
			resp = new RespObj(Constant.FAILD_CODE, "失败");
			e.printStackTrace();
		}
		return resp;
	}
	@RequestMapping("/saveCourseRange")
	@ResponseBody
	public RespObj saveCourseRange(@RequestBody CourseRangeDTO dto) {
		RespObj resp = new RespObj(Constant.SUCCESS_CODE, "成功");
		dto.setSchoolId(getSchoolId().toString());
		try {
			courseRangeService.save(dto);
		} catch (ParseException e) {
			resp = new RespObj(Constant.FAILD_CODE, "保存失败");
			e.printStackTrace();
		}
		return resp;
	}
	@RequestMapping("/removeCourseRnage")
	@ResponseBody
	public RespObj removeCourseRnage(String id) {
		RespObj resp = new RespObj(Constant.SUCCESS_CODE, "成功");
		try {
			courseRangeService.removeById(id);
		} catch (Exception e) {
			resp = new RespObj(Constant.FAILD_CODE, "保存失败");
			e.printStackTrace();
		}
		return resp;
	}
	@RequestMapping("/getDayRange")
	@ResponseBody
	public DayRangeDTO getDayRange() {
		return dayRangeService.getBySchoolId(getSchoolId().toString());
	}

	@RequestMapping("/saveDayRange")
	@ResponseBody
	public RespObj saveDayRange(@RequestBody DayRangeDTO dto) {
		RespObj resp = new RespObj(Constant.SUCCESS_CODE, "成功");
		try {
			dto.setSchoolId(getSchoolId().toString());
			dayRangeService.save(dto);
		} catch (Exception e) {
			resp = new RespObj(Constant.FAILD_CODE, "保存失败");
			e.printStackTrace();
		}
		return resp;
	}

	/**
	 * 设置走班时间
	 * @param xqid
	 * @param gid
	 * @param type
	 * @param x
	 * @param y
	 * @return
	 */
	@RequestMapping("/setZouBanTime")
	@ResponseBody
	public RespObj setZouBanTime(@RequestParam String xqid,@RequestParam String gid,@RequestParam Integer type,@RequestParam Integer x,@RequestParam Integer y) {
		RespObj resp = new RespObj(Constant.SUCCESS_CODE, "成功");
		try {
			courseRangeService.setZouBanTime(getSchoolId(),new ObjectId(xqid),new ObjectId(gid),type,x,y);
		} catch (Exception e) {
			resp = new RespObj(Constant.FAILD_CODE, "保存失败");
			e.printStackTrace();
		}
		return resp;
	}

	/**
	 * 设置走班时间
	 * @param
	 * @return
	 */
	@RequestMapping("/getZouBanTime")
	@ResponseBody
	public RespObj getZouBanTime(@RequestParam String xqid,@RequestParam String gid) {
		RespObj resp = new RespObj(Constant.SUCCESS_CODE, "成功");
		try {
			resp.setMessage(courseRangeService.getZouBanTime(getSchoolId(),new ObjectId(xqid),new ObjectId(gid)));
		} catch (Exception e) {
			resp = new RespObj(Constant.FAILD_CODE, "失败");
			e.printStackTrace();
		}
		return resp;
	}

}
