package com.fulaan.new33.controller;

import com.fulaan.annotation.ObjectIdType;
import com.fulaan.annotation.UserDataCollection;
import com.fulaan.base.controller.BaseController;
import com.fulaan.new33.dto.isolate.ClassInfoDTO;
import com.fulaan.new33.service.N33_JXBService;
import com.fulaan.new33.service.isolate.IsolateClassService;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/new33classManage")
public class N33_ClassController extends BaseController {

    @Autowired
    private IsolateClassService classService;

    @Autowired
    private N33_JXBService jxbService;

    /**
     * 下载导入学生的模板
     *
     * @param response
     * @throws Exception
     */
    @RequestMapping("/exportTemplate")
    public void exportTemplate(HttpServletResponse response) throws Exception {
        try {
            classService.exportTemplate(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 导入班级列表
     *
     * @param file
     * @param xqid
     * @return
     */
    @RequestMapping("/addClassImpYu")
    @ResponseBody
    public RespObj addClassImpYu(@RequestParam("file") MultipartFile file,
                                 @RequestParam ObjectId xqid) {
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try {
            String fileName = file.getOriginalFilename();
            if (!fileName.endsWith(".xls") || !fileName.endsWith(".xls")) {
                respObj.setMessage("文件格式不正确");
            } else {
                classService.importClass(file.getInputStream(), xqid,
                        getSchoolId());
                respObj.setCode(Constant.SUCCESS_CODE);
                respObj.setMessage("成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return respObj;
    }

    /**
     * 新增班级
     *
     * @param dto
     * @return
     */
    @RequestMapping("/addClassDto")
    @ResponseBody
    public RespObj addClassDto(@RequestBody ClassInfoDTO dto) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            dto.setId(new ObjectId().toString());
            dto.setClassId(new ObjectId().toString());
            dto.setSchoolId(getSchoolId().toString());
            dto.setBuid(new ObjectId().toString());
            dto.setType(1);
            classService.addClassDto(dto);
            obj.setMessage("新增成功");
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    /**
     * 根据id删除班级
     *
     * @param id
     * @return
     */
    @RequestMapping("/deleteClass")
    @ResponseBody
    public RespObj deleteClass(@RequestParam String id, @RequestParam Integer stuCount) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            if (stuCount > 0) {
                obj.setMessage("该班级存在学生，不允许删除");
            } else {
                classService.deleteClass(new ObjectId(id));
                obj.setMessage("删除成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    /**
     * 修改班级
     *
     * @param dto
     * @return
     */
    @RequestMapping("/updateClassDto")
    @ResponseBody
    public RespObj updateClassDto(@RequestBody ClassInfoDTO dto) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            dto.setSchoolId(getSchoolId().toString());
            classService.updateClass(dto);
            obj.setMessage("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    /**
     * 加载班级数据
     *
     * @param xqid
     * @param gradeId
     * @return
     */
    @UserDataCollection(pageTag = "n33", params = {"gradeId"}, isAllCollection = false)
    @RequestMapping("/getClassList")
    @ResponseBody
    public RespObj getClassList(@RequestParam String xqid,
                                @RequestParam(defaultValue = "*") String gradeId) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            if ("*".equals(gradeId)) {
                gradeId = new ObjectId().toString();
            }
            List<ClassInfoDTO> result  = classService.getClassByXqAndGrade(
                    new ObjectId(xqid), getSchoolId(), new ObjectId(gradeId));
            obj.setMessage(result);
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    @RequestMapping("/getClassListByJxbId")
    @ResponseBody
    public RespObj getClassListByJxbId(@RequestParam String jxbId) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(classService.getClassListByJxbId(new ObjectId(jxbId)));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }


    @RequestMapping("/getClassStudentListByJxbId")
    @ResponseBody
    public RespObj getClassStudentListByJxbId(@RequestParam String jxbId, @RequestParam String classId) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(classService.getClassStudentListByJxbId(new ObjectId(jxbId), new ObjectId(classId)));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    @RequestMapping("/getZbJxbClassList")
    @ResponseBody
    public RespObj getZbJxbClassList(
            @ObjectIdType ObjectId jxbId
    ) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(classService.getZbJxbClassList(jxbId));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }


    @RequestMapping("/getZbJxbClassStuList")
    @ResponseBody
    public RespObj getZbJxbClassStuList(
            @ObjectIdType ObjectId jxbId,
            @ObjectIdType ObjectId classId
    ) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(classService.getZbJxbClassStuList(jxbId, classId));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    @RequestMapping("/getClassListZKB")
    @ResponseBody
    public RespObj getClassListZKB(@RequestParam String xqid,
                                   @RequestParam(defaultValue = "*") String gid) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            if ("*".equals(gid)) {
                gid = new ObjectId().toString();
            }
            obj.setMessage(classService.getClassByXqAndGradeZKB(
                    new ObjectId(xqid), getSchoolId(), new ObjectId(gid)));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    /**
     * 根据id查找班级
     *
     * @param id
     * @return
     */
    @RequestMapping("/getClassDto")
    @ResponseBody
    public RespObj getClassDto(@RequestParam String id) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(classService.getClassById(new ObjectId(id)));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    @RequestMapping("/addJXBStu")
    @ResponseBody
    public RespObj addJXBStu(@RequestBody Map map) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            String jxbId = (String) map.get("jxbId");
            List<String> stuId = (List<String>) map.get("stuIds");
            jxbService.addjxbStu(new ObjectId(jxbId), MongoUtils.convertToObjectIdList(stuId));
            obj.setMessage("保存成功");
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    /**
     * 判断是否已从2.0导入班级
     *
     * @param xqid
     * @return
     */
    @RequestMapping("/getIsolateClass")
    @ResponseBody
    public RespObj getIsolateClass(@RequestParam String xqid) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            if (classService.getCountByXqId(new ObjectId(xqid), getSchoolId()) == 0) {
                classService.getClassBySid(getSchoolId(), new ObjectId(xqid));
            }
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }
}
