package com.fulaan.new33.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fulaan.annotation.UserDataCollection;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fulaan.annotation.ObjectIdType;
import com.fulaan.base.controller.BaseController;
import com.fulaan.new33.dto.isolate.N33_ClassroomDTO;
import com.fulaan.new33.service.isolate.N33_ClassService;
import com.fulaan.new33.service.isolate.N33_ClassroomService;
import com.fulaan.schoolbase.dto.ClassInfoDTO;
import com.fulaan.schoolbase.dto.ClassRoomDTO;
import com.fulaan.schoolbase.service.ClassRoomService;
import com.fulaan.schoolbase.service.SchoolClassService;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;

@Controller
@RequestMapping("/classset")
public class N33_ClassSetController extends BaseController {

	private N33_ClassroomService classroomService = new N33_ClassroomService();
	private SchoolClassService schoolClassService = new SchoolClassService();
	private ClassRoomService oldclassroomservice = new ClassRoomService();
	private N33_ClassService  N33_ClassService = new N33_ClassService();

	@UserDataCollection(pageTag = "n33", params = {"gradeId"}, isAllCollection = false)
	@ResponseBody
	@RequestMapping("/getListByXqGrade")
	public List<N33_ClassroomDTO> getListByXqGrade(String xqid,String gradeId){
		List<N33_ClassroomDTO> list = new ArrayList<N33_ClassroomDTO>();
		try{
			list = classroomService.getListByXqGrade(getSchoolId(), xqid, gradeId);
		}
		catch (Exception e){
			e.printStackTrace();
		}
		return list;
	}
	@ResponseBody
	@RequestMapping("/saveClassRoom")
	public RespObj saveClassRoom(@RequestBody N33_ClassroomDTO dto) {
		RespObj resp = new RespObj(Constant.SUCCESS_CODE,"成功");
		try {
			dto.setSchoolId(getSchoolId().toString());
			classroomService.saveClassRoom(dto);
		}
		catch (Exception e) {
			resp = new RespObj(Constant.FAILD_CODE,"失败");
		}
		return resp;
	}
	@ResponseBody
	@RequestMapping("/removeClassRoom")
	public RespObj removeClassRoom(String id) {
		RespObj resp = new RespObj(Constant.SUCCESS_CODE,"成功");
		try {
			classroomService.removeClassRoom(id);
		}
		catch (Exception e) {
			resp = new RespObj(Constant.FAILD_CODE,"失败");
		}
		return resp;
	}
	
	/**
	 * 复选框选择教室更新
	 * @param xqid
	 * @param gradeId
	 * @param dtos
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updateClassRoomList")
	public RespObj updateClassRoomList(String xqid,String gradeId,@RequestBody List<N33_ClassroomDTO> dtos) {
		RespObj resp = new RespObj(Constant.SUCCESS_CODE,"成功");
		try {
			classroomService.updateClassRoomList(getSchoolId(), xqid, gradeId, dtos);
		}
		catch (Exception e) {
			resp = new RespObj(Constant.FAILD_CODE,"失败");
		}
		return resp;
	}
	
	/**
	 * 根据年级差班级
	 * @param gradeId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getClassByGradeId")
	public List<com.fulaan.new33.dto.isolate.ClassInfoDTO> getClassByGradeId(@ObjectIdType ObjectId gradeId,@ObjectIdType ObjectId xqid){
		return N33_ClassService.findByGradeIdSid(getSchoolId(), xqid, gradeId);
	}
	
	/**
	 * 查某个学校所有教室
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getRoomListBySchoolId")
	public Map<String,List<ClassRoomDTO>> getRoomListBySchoolId(){
		return classroomService.getRoomListBySchoolId(getSchoolId());
	}
	
	/**
	 * 查教学楼
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getSchoolLoopList")
	public RespObj getSchoolLoopList() {
		RespObj resp = new RespObj(Constant.SUCCESS_CODE,"成功");
		try {
			resp.setMessage(oldclassroomservice.getSchoolLoopList(getSchoolId()));
		}
		catch (Exception e) {
			resp = new RespObj(Constant.FAILD_CODE,"失败");
		}
		return resp;
	}
	
	/**
	 * 修改班级
	 * @param id
	 * @param className
	 * @param classId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updateClassNameId")
	public RespObj updateClassNameId(String id,String className,String classId) {
		RespObj resp = new RespObj(Constant.SUCCESS_CODE,"成功");
		try {
			classroomService.updateClassNameId(id, className, classId);
		}
		catch (Exception e) {
			resp = new RespObj(Constant.FAILD_CODE,"失败");
		}
		return resp;
	}
	
	/**
	 * 更新备注
	 * @param id
	 * @param description
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updateDesc")
	public RespObj updateDesc(String id,String description,Integer cap,Integer type) {
		RespObj resp = new RespObj(Constant.SUCCESS_CODE,"成功");
		try {
			classroomService.updateDesc(id, description,cap,type);
		}
		catch (Exception e) {
			resp = new RespObj(Constant.FAILD_CODE,"失败");
		}
		return resp;
	}
	
	/**
	 * 更新安排
	 * @param id
	 * @param
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updateArrange")
	public RespObj updateArrange(String id,Integer arranged) {
		RespObj resp = new RespObj(Constant.SUCCESS_CODE,"成功");
		try {
			classroomService.updateArrange(id, arranged);
		}
		catch (Exception e) {
			resp = new RespObj(Constant.FAILD_CODE,"失败");
		}
		return resp;
	}
	
}
