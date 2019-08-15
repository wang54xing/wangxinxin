package com.fulaan.new33.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.fulaan.annotation.UserDataCollection;
import com.fulaan.new33.service.isolate.CourseRangeService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fulaan.base.controller.BaseController;
import com.fulaan.new33.dto.isolate.StudentDTO;
import com.fulaan.new33.service.SchoolSelectLessonSetService;
import com.fulaan.new33.service.SchoolSelectLessonSetService.SchoolLesson33DTO;
import com.fulaan.new33.service.SchoolSelectLessonSetService.lesson33DTO;
import com.fulaan.new33.service.isolate.N33_StudentService;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;

/**
 * Created by albin on 2018/3/16.
 */
@Controller
@RequestMapping("/IsolateStudent")
public class N33_StudentController extends BaseController {
    @Autowired
    private N33_StudentService studentService;
    @Autowired
    private SchoolSelectLessonSetService schoolSelectLessonSetService;

    /**
     * 下载导入学生的模板
     * @param response
     * @throws Exception
     */
	@RequestMapping("/exportTemplate")
	public void exportTemplate(HttpServletResponse response) throws Exception {
		try {
			studentService.exportTemplate(response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	 /**
     * 根据学生姓名查找学生（模糊查询)
     * @throws Exception
     */
	@RequestMapping("/getStudentByName")
	@ResponseBody
	public RespObj getStudentByName(@RequestParam String xqid,@RequestParam String cid,@RequestParam String name){
		RespObj obj = new RespObj(Constant.SUCCESS_CODE);
		try {
			obj.setMessage(studentService.getStudentByName(new ObjectId(xqid),new ObjectId(cid),getSchoolId(),name));
		} catch (Exception e) {
			e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
		}
		return obj;
	}

    /**
     * 比较两次学生选科的不同
     * @throws Exception
     */
    @RequestMapping("/getDifferenceXQ")
    @ResponseBody
    public RespObj getDifferenceXQ(@RequestParam String ciId1,@RequestParam String ciId2){
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(studentService.getDifferenceXQ(new ObjectId(ciId1),new ObjectId(ciId2),getSchoolId()));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    /**
     * 比较两次学生选科的不同
     * @throws Exception
     */
    @RequestMapping("/getDifference")
    @ResponseBody
    public RespObj getDifference(@RequestParam String ciId1,@RequestParam String ciId2,@RequestParam String classId){
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(studentService.getDifference(new ObjectId(ciId1),new ObjectId(ciId2),new ObjectId(classId),getSchoolId()));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }
	
    /**
     * 查询选课组合
     * @param termId
     * @param gradeId
     * @return
     */
    @RequestMapping("/getSelectLessonsList")
    @ResponseBody
    public RespObj getSchoolList(@RequestParam String termId,@RequestParam String gradeId) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
        	obj.setMessage(studentService.getAllSelectLessons(new ObjectId(termId),getSchoolId(),new ObjectId(gradeId)));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }
    
    @RequestMapping("/getIsolateStudent")
    @ResponseBody
    public RespObj getIsolateStudent(@RequestParam String xqid) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            if (studentService.getCountByXqId(new ObjectId(xqid), getSchoolId()) == 0) {
                studentService.getStudentBySid(getSchoolId(), new ObjectId(xqid));
            }
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    @RequestMapping("/synchronizedStudent")
    @ResponseBody
    public RespObj synchronizedStudent(@RequestParam String xqid,@RequestParam String gradeId) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            studentService.synchronizedStudent(getSchoolId(), new ObjectId(xqid),gradeId);
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }
    
    @RequestMapping("/addStudentList")
    @ResponseBody
    public RespObj addStudentList(@RequestBody List<StudentDTO> studentList) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
        	if(studentList.size() == 0){
        		obj.setMessage("请选择学生");
        		return obj;
        	}
        	for (StudentDTO dto : studentList) {
                if ("*".equals(dto.getId())) {
                    dto.setId(new ObjectId().toString());
                }
                dto.setSchoolId(getSchoolId().toHexString());
                dto.setCombiname("");
                dto.setType(0);
            }
        	studentService.addStudentList(studentList);
            obj.setMessage("新增成功");
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        } 
        return obj;
    }
    
    /**
     * 根据学生姓名或者学籍号从2.0查询学生
     * @param userName
     * @return
     */
    @RequestMapping("/getIsolateStudentByNameOrCn")
    @ResponseBody
	public RespObj getIsolateStudentByNameOrCn(@RequestParam String userName,
			@RequestParam String xqid,
			@RequestParam(defaultValue = "*") String gid,
			@RequestParam(defaultValue = "*") String classId) {
		RespObj obj = new RespObj(Constant.SUCCESS_CODE);
		try {
			if("*".equals(gid)){
				gid = new ObjectId().toString();
			}
			if("*".equals(classId)){
				classId = new ObjectId().toString();
			}
			obj.setMessage(studentService.getIsolateStudentByNameOrCn(userName,
					getSchoolId(), new ObjectId(xqid), new ObjectId(gid),
					new ObjectId(classId)));
		} catch (Exception e) {
			e.printStackTrace();
			obj.setCode(Constant.FAILD_CODE);
			obj.setMessage("服务器正忙");
		}
		return obj;
	}



    @RequestMapping("/addStudentDto")
    @ResponseBody
    public RespObj addStudentDto(@RequestBody StudentDTO dto) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            if (dto.getId().equals("*")) {
                dto.setId(new ObjectId().toString());
            }
            if (dto.getUserId().equals("*")) {
                dto.setUserId(new ObjectId().toString());
            }
            dto.setSchoolId(getSchoolId().toString());
            dto.setType(1);
            studentService.addStudent(dto);
            obj.setMessage("新增成功");
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }


    @RequestMapping("/updateDto")
    @ResponseBody
    public RespObj updateDto(@RequestBody StudentDTO dto) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            if (dto.getId().equals("*")) {
                dto.setId(new ObjectId().toString());
            }
            if (dto.getUserId().equals("*")) {
                dto.setUserId(new ObjectId().toString());
            }
            dto.setSchoolId(getSchoolId().toString());
            studentService.addStudent(dto);
            obj.setMessage("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }
    
    @RequestMapping("/updateLevel")
    @ResponseBody
    public RespObj updateLevel(@RequestParam Integer level, @RequestParam String id) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
        	studentService.updateLevel(level,id);
            obj.setMessage("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }
    
    @RequestMapping("/updateStudent")
    @ResponseBody
    public RespObj updateStudent(@RequestParam String id, @RequestParam String subjectId1,@RequestParam String subjectId2,@RequestParam String subjectId3,@RequestParam String name) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
        	studentService.updateStudent(new ObjectId(id),new ObjectId(subjectId1),new ObjectId(subjectId2),new ObjectId(subjectId3),name);
            obj.setMessage("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    @RequestMapping("/updateStudentAndJXB")
    @ResponseBody
    public RespObj updateStudentAndJXB(@RequestParam String id, @RequestParam String subjectId1,@RequestParam String subjectId2,@RequestParam String subjectId3,@RequestParam String name) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(studentService.updateStudentAndJXB(new ObjectId(id),new ObjectId(subjectId1),new ObjectId(subjectId2),new ObjectId(subjectId3),name));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    @RequestMapping("/deleteStudent")
    @ResponseBody
    public RespObj deleteStudent(@RequestParam String id) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            studentService.deleteStudent(new ObjectId(id));
            obj.setMessage("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }


    @RequestMapping("/getStudent")
    @ResponseBody
    public RespObj getStudent(@RequestParam String id) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(studentService.getStudentDto(new ObjectId(id)));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }


    @RequestMapping("/getStudentByUserId")
    @ResponseBody
    public RespObj getStudentByUserId(@RequestParam String uid, @RequestParam String xqid) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(studentService.getStudentDto(new ObjectId(xqid), new ObjectId(uid)));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    @UserDataCollection(pageTag = "n33", params = {"gradeId","classId"}, isAllCollection = false)
    @RequestMapping("/getStudentDtoByClassId")
    @ResponseBody
    public RespObj getStudentDtoByClassId(@RequestParam(defaultValue = "*") String classId, @RequestParam String xqid) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
        	if("*".equals(classId)){
        		classId = new ObjectId().toString();
        	}
            List<StudentDTO> dtos = studentService.getStudentDtoByClassId(new ObjectId(xqid), new ObjectId(classId));
            obj.setMessage(dtos);
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    @UserDataCollection(pageTag = "n33", params = {"gradeId","classId"}, isAllCollection = false)
    @RequestMapping("/getStudentDtoByClassIdForZhuanXiang")
    @ResponseBody
    public RespObj getStudentDtoByClassIdForZhuanXiang(@RequestParam(defaultValue = "*") String classId, @RequestParam String xqid,@RequestParam Integer sex,@RequestParam Integer level,@RequestParam String combine) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            if("*".equals(classId)){
                classId = new ObjectId().toString();
            }
            List<StudentDTO> dtos= studentService.getStudentDtoByClassIdForZhuanXiang(new ObjectId(xqid), new ObjectId(classId),sex,level,combine);
            obj.setMessage(dtos);
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }


    @RequestMapping("/getStudentDtoByClassIdZKB")
    @ResponseBody
    public RespObj getStudentDtoByClassIdZKB(@RequestParam(defaultValue = "*") String classId, @RequestParam String xqid) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            if("*".equals(classId)){
                classId = new ObjectId().toString();
            }
            List<StudentDTO> dtos= studentService.getStudentDtoByClassIdZKB(new ObjectId(xqid), new ObjectId(classId));
            obj.setMessage(dtos);
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    @RequestMapping("/getStudentDtoByClassIdZKBForExport")
    @ResponseBody
    public RespObj getStudentDtoByClassIdZKBForExport(@RequestParam String gid,@RequestParam(defaultValue = "*") String classId, @RequestParam String xqid) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            List<StudentDTO> dtos= studentService.getStudentDtoByClassIdZKBForExport(new ObjectId(xqid),classId,new ObjectId(gid));
            obj.setMessage(dtos);
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }


    @RequestMapping("/getStudentDtoByGradeId")
    @ResponseBody
    public RespObj getStudentDtoByGradeId(@RequestParam String gradeId, @RequestParam String xqid) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(studentService.getStudentDtoByGradeId(new ObjectId(xqid), new ObjectId(gradeId)));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }


    @RequestMapping("/addUserImpYu")
    @ResponseBody
    public RespObj addUserImpYu(@RequestParam("file") MultipartFile file, @RequestParam ObjectId xqid) {

        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try {
            String fileName = file.getOriginalFilename();
            if (!fileName.endsWith(".xls") || !fileName.endsWith(".xls")) {
                respObj.setMessage("文件格式不正确");
            } else {
                    List<Map<String,Object>> returnList = studentService.importUser(file.getInputStream(),xqid,getSchoolId());
                    respObj.setCode(Constant.SUCCESS_CODE);
                    respObj.setMessage(returnList);
            }
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setMessage(e.getMessage());
        }
        return respObj;
    }
}
