package com.fulaan.new33.controller;

import com.fulaan.base.controller.BaseController;
import com.fulaan.new33.service.N33_JXBService;
import com.fulaan.new33.service.isolate.IsolateSubjectService;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
@RequestMapping("/studentJXB")
public class N33_StudentJXBController extends BaseController {

	@Autowired
	private IsolateSubjectService subjectService;

	@Autowired
	private N33_JXBService jxbService;

	/**
	 * 查询走班的学科
	 */
	@RequestMapping("/getSubjectZouBan")
	@ResponseBody
	public RespObj getSubjectCanZouBan(@RequestParam String xqid,@RequestParam String gid){
		RespObj obj = new RespObj(Constant.SUCCESS_CODE);
		try {
			obj.setMessage(subjectService.getSubjectZouBan(getSchoolId(),new ObjectId(xqid),new ObjectId(gid)));
		} catch (Exception e) {
			e.printStackTrace();
			obj.setCode(Constant.FAILD_CODE);
			obj.setMessage("服务器正忙");
		}
		return obj;
	}

	/**
	 * 查询班级学生所在的教学班
	 */
	@RequestMapping("/getStuJXB")
	@ResponseBody
	public RespObj getStuJXB(@RequestParam String xqid,@RequestParam String gid,@RequestParam String classId){
		RespObj obj = new RespObj(Constant.SUCCESS_CODE);
		try {
			obj.setMessage(jxbService.getStuJXB(new ObjectId(xqid),new ObjectId(classId),getSchoolId(),new ObjectId(gid)));
		} catch (Exception e) {
			e.printStackTrace();
			obj.setCode(Constant.FAILD_CODE);
			obj.setMessage("服务器正忙");
		}
		return obj;
	}

	/**
	 * 查询走班的学科
	 */
	@RequestMapping("/exportByClass")
	@ResponseBody
	public RespObj exportByClass(@RequestParam String xqid,@RequestParam String gid,@RequestParam String classId,HttpServletResponse response){
		RespObj obj = new RespObj(Constant.SUCCESS_CODE);
		try {
			jxbService.exportByClass(getSchoolId(),new ObjectId(xqid),new ObjectId(gid),new ObjectId(classId),response);
		} catch (Exception e) {
			e.printStackTrace();
			obj.setCode(Constant.FAILD_CODE);
			obj.setMessage("服务器正忙");
		}
		return obj;
	}

	/**
	 * 查询走班的学科
	 */
	@RequestMapping("/exportByGrade")
	@ResponseBody
	public RespObj exportByGrade(@RequestParam String xqid, @RequestParam String gid, HttpServletResponse response, HttpServletRequest request){
		RespObj obj = new RespObj(Constant.SUCCESS_CODE);
		try {
			jxbService.exportByGrade(getSchoolId(),new ObjectId(xqid),new ObjectId(gid),response,request);
		} catch (Exception e) {
			e.printStackTrace();
			obj.setCode(Constant.FAILD_CODE);
			obj.setMessage("服务器正忙");
		}
		return obj;
	}

	@ResponseBody
	@RequestMapping("/getStatusDC")
	public Map<String, Object> getStatusDC(HttpServletRequest request) {
		return jxbService.getStatusDC(request);
	}

	/**
	 * 调教学班学生
	 * @param studentIds
	 * @param orgJxbId
	 * @param newJxbId
	 * @return
	 */
	@RequestMapping("/udpStudentByJxb")
	@ResponseBody
	public RespObj udpStudentByJxb(String studentIds, String orgJxbId,String newJxbId) {
		RespObj resp = new RespObj(Constant.SUCCESS_CODE, "成功");
		try {
			jxbService.udpStudentByJxb(studentIds, new ObjectId(orgJxbId),new ObjectId(newJxbId));
		} catch (Exception e) {
			e.printStackTrace();
			resp = new RespObj(Constant.FAILD_CODE, "失败");
		}
		return resp;
	}
}
