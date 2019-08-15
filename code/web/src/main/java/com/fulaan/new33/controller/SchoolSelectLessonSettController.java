package com.fulaan.new33.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.fulaan.annotation.UserDataCollection;
import com.fulaan.new33.dto.isolate.StudentDTO;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fulaan.annotation.ObjectIdType;
import com.fulaan.new33.service.SchoolSelectLessonSetService;
import com.fulaan.new33.service.SchoolSelectLessonSetService.SchoolLesson33DTO;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;

/**
 * 学校选课设置
 *
 * @author fourer
 */
@Controller
@RequestMapping("/new33school/set")
public class SchoolSelectLessonSettController extends New33Controller {

    private static final Logger logger = Logger.getLogger(SchoolSelectLessonSettController.class);
    @Autowired
    private SchoolSelectLessonSetService schoolSelectLessonSetService;
    @Autowired
    private com.fulaan.new33.service.StudentSelectLessonsService StudentSelectLessonsService;

    /**
     * 得到初始组合
     *
     * @return
     */
    @UserDataCollection(pageTag = "n33", params = {"gradeId"}, isAllCollection = false)
    @RequestMapping("/list")
    @ResponseBody
    public RespObj initSelectCombiners(@ObjectIdType(isRequire = false) ObjectId termId, @ObjectIdType(isRequire = false) ObjectId gradeId) {
        SchoolLesson33DTO dto = schoolSelectLessonSetService.getSchoolSelects(termId, getSchoolId(), gradeId);
        return new RespObj(Constant.SUCCESS_CODE, dto);
    }


    /**
     * 设定选课组合
     *
     * @param
     * @return
     */
    @RequestMapping("/update")
    @ResponseBody
    public RespObj setSelectCombine(@ObjectIdType ObjectId id, String selectId, int state) {
        schoolSelectLessonSetService.update(id, selectId, state);
        return RespObj.SUCCESS;
    }

    /**
     * 设定选科人数
     *
     * @param
     * @return
     */
    @RequestMapping("/updateNum")
    @ResponseBody
    public RespObj updateNum(@ObjectIdType ObjectId id, String selectId, int num) {
        schoolSelectLessonSetService.updateNum(id, selectId, num);
        return RespObj.SUCCESS;
    }

    @RequestMapping("/updateStartTime")
    @ResponseBody
    public RespObj updateStartTime(String id, String start, String end) {
        RespObj resp = new RespObj(Constant.SUCCESS_CODE, "成功");
        try {
            schoolSelectLessonSetService.updateStartTime(id, start, end);
        } catch (Exception e) {
            e.printStackTrace();
            resp = new RespObj(Constant.SUCCESS_CODE, "失败");
        }
        return resp;
    }

    @RequestMapping("/getCurrentSubjectGroup")
    @ResponseBody
    public Map<String, Object> getCurrentSubjectGroup() {
        try {
            return schoolSelectLessonSetService.getCurrentSubjectGroup(getUserId(), getSchoolId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping("/studentSelectSubjectGroup")
    @ResponseBody
    public RespObj studentSelectSubjectGroup(@ObjectIdType ObjectId xqid, @ObjectIdType ObjectId sub1, @ObjectIdType ObjectId sub2, @ObjectIdType ObjectId sub3, String name) {
        RespObj resp = new RespObj(Constant.SUCCESS_CODE);
        try {
            resp.setMessage(schoolSelectLessonSetService.studentSelectSubjectGroup(xqid, getUserId(), getSchoolId(), sub1, sub2, sub3, name));
        } catch (Exception e) {
            e.printStackTrace();
            resp.setCode(Constant.FAILD_CODE);
            resp.setMessage("服务器正忙");
        }
        return resp;
    }

    @UserDataCollection(pageTag = "n33", params = {"gradeId","type"}, isAllCollection = false)
    @RequestMapping("/getStuSelectResultByClass")
    @ResponseBody
    public Map<String, Object> getStuSelectResultByClass(@ObjectIdType ObjectId xqid, @ObjectIdType ObjectId gradeId, Integer type) {
        return schoolSelectLessonSetService.getStuSelectResultByClass(xqid, getSchoolId(), gradeId, type);
    }

    @UserDataCollection(pageTag = "n33", params = {"gradeId","type"}, isAllCollection = false)
    @RequestMapping("/getStuSelectResultByClassForZhuanXiang")
    @ResponseBody
    public Map<String, Object> getStuSelectResultByClassForZhuanXiang(@ObjectIdType ObjectId xqid, @ObjectIdType ObjectId gradeId, Integer type) {
        return schoolSelectLessonSetService.getStuSelectResultByClassForZhuanXiang(xqid, getSchoolId(), gradeId, type);
    }

    @RequestMapping("/getHGStuIds")
    @ResponseBody
    public List<Map<String,Object>> getHGStuIds(@RequestBody Map map) {
        List<Map<String,Object>> retList = new ArrayList<Map<String,Object>>();
        List<String> stuIds = (List<String>) map.get("stuIds");
        String classId = "";
        if(map.get("classId") != null){
            classId = (String) map.get("classId");
        }
        String xqid = (String)map.get("xqid");
        String gradeId = (String) map.get("gradeId");
        retList = schoolSelectLessonSetService.getHGOrDJStuIds(stuIds,classId,xqid,gradeId);
        return retList;
    }

    @RequestMapping("/addUserImpYu")
    @ResponseBody
    public RespObj importSelectData(@RequestParam("file") MultipartFile file, ObjectId xqid) {

        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        try {
            String fileName = file.getOriginalFilename();
            if (!fileName.endsWith(".xls") || !fileName.endsWith(".xls")) {
                respObj.setMessage("文件格式不正确");
            } else {

                respObj.setCode(Constant.FAILD_CODE);
                respObj.setMessage("成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return respObj;
    }

    @RequestMapping("/exportTemplate")
    public void exportTemplate(HttpServletResponse response) throws Exception {
        try {
            StudentSelectLessonsService.exportTemplate(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @RequestMapping("/importUser")
    @ResponseBody
    public RespObj importUser(@RequestParam("file") MultipartFile file, @ObjectIdType ObjectId xqid) {

        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        try {
            String fileName = file.getOriginalFilename();
            if (!fileName.endsWith(".xls") || !fileName.endsWith(".xls")) {
                respObj.setMessage("文件格式不正确");
            } else {
                List<Map<String, String>> result = StudentSelectLessonsService.importUser(file.getInputStream(), xqid, getSchoolId());
                respObj.setCode(Constant.SUCCESS_CODE);
                respObj.setMessage(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return respObj;
    }
    @UserDataCollection(pageTag = "n33", params = {"gradeId","classId"}, isAllCollection = false)
    @RequestMapping("/getStuSelectByClass")
    @ResponseBody
    public List<Map<String, String>> getStuSelectByClass(@ObjectIdType ObjectId xqid, @ObjectIdType ObjectId gradeId, @ObjectIdType ObjectId classId, @RequestParam(defaultValue = "*") String name) {
        return StudentSelectLessonsService.getStuSelectByClass(xqid, gradeId, classId, name);
    }

    @RequestMapping("/exportStuSelectResult")
    @ResponseBody
    public void exportStuSelectResult(@ObjectIdType ObjectId xqid, @ObjectIdType ObjectId gradeId,HttpServletResponse response){
        StudentSelectLessonsService.exportStuSelectResult(xqid,gradeId,response);
    }
}
