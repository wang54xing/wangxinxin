
package com.fulaan.new33.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fulaan.annotation.ObjectIdType;
import com.fulaan.annotation.UserDataCollection;
import com.fulaan.new33.service.N33_FenBanInfoSetService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fulaan.base.controller.BaseController;
import com.fulaan.new33.dto.paike.N33_JXBDTO;
import com.fulaan.new33.service.N33_JXBService;
import com.fulaan.new33.service.isolate.IsolateSubjectService;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/n33_setJXB")
public class N33_DivideJXBController extends BaseController {

    @Autowired
    private IsolateSubjectService subjectService;
    @Autowired
    private N33_JXBService jxbService;

    @Autowired
    private N33_FenBanInfoSetService infoSetService;

    /**
     * 查询对应年级对应学期对应类型的学科
     *
     * @param xqid
     * @param gid
     * @param subType
     * @return
     */
    @RequestMapping("/getSubjectByType")
    @ResponseBody
    public RespObj getSubjectByType(@RequestParam String xqid, @RequestParam String gid, @RequestParam String subType) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(subjectService.getSubjectByType(new ObjectId(xqid), new ObjectId(gid), getSchoolId(), subType));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    @RequestMapping("/reXingZengBan")
    @ResponseBody
    public RespObj reXingZengBan(@RequestParam String xqid, @RequestParam String gradeId) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
          subjectService.reXingZengBan(new ObjectId(xqid), new ObjectId(gradeId), getSchoolId());
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    @RequestMapping("/updateJXBKsList")
    @ResponseBody
    public RespObj updateJXBKsList(@RequestParam String xqid, @RequestParam String gradeId) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            subjectService.updateJXBKsList(new ObjectId(xqid), new ObjectId(gradeId), getSchoolId());
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }
    /**
     * 查询对应年级对应学期对应类型的学科
     *
     * @param xqid
     * @param gradeId
     * @param subType
     * @return
     */
    @UserDataCollection(pageTag = "n33", params = {"gradeId"}, isAllCollection = false)
    @RequestMapping("/getSubjectByTypeExitZX")
    @ResponseBody
    public RespObj getSubjectByTypeExitZX(@RequestParam String xqid, @RequestParam String gradeId, @RequestParam String subType) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(subjectService.getSubjectByTypeExitZX(new ObjectId(xqid), new ObjectId(gradeId), getSchoolId(), subType));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    /**
     * 创建专项类型教学班
     *
     * @return
     */
    @RequestMapping("/addZhuanXiang")
    @ResponseBody
    public RespObj addZhuanXiang(@RequestBody Map map) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            jxbService.addZhuanXiang(map, getSchoolId());
            obj.setMessage("保存成功");
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    @RequestMapping("/upZhuan")
    @ResponseBody
    public RespObj upZhuan(@RequestBody Map map) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            jxbService.upZhuan(map);
            obj.setMessage("保存成功");
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    @RequestMapping("/delZhanXu")
    @ResponseBody
    public RespObj delZhanXu(@RequestParam String id) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            jxbService.delZhanXu(new ObjectId(id));
            obj.setMessage("保存成功");
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    @RequestMapping("/delZu")
    @ResponseBody
    public RespObj delZu(@RequestParam String id) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            jxbService.delZu(new ObjectId(id));
            obj.setMessage("保存成功");
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    @UserDataCollection(pageTag = "n33", params = {"gradeId"}, isAllCollection = false)
    @RequestMapping("/getZhuanXiangList")
    @ResponseBody
    public RespObj getZhuanXiangList(@RequestParam String xqid, @RequestParam String gradeId, @RequestParam String subId) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(jxbService.getZhuanXiangList(new ObjectId(xqid), new ObjectId(gradeId), subId, getSchoolId()));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    @RequestMapping("/addZhuanXiangZu")
    @ResponseBody
    public RespObj addZhuanXiangZu(@RequestParam String xqid, @RequestParam String jxbId, @RequestParam String name, @RequestParam String roomId, @RequestParam String teacherId) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            jxbService.addZhuanXiangZu(new ObjectId(xqid), new ObjectId(jxbId), name, new ObjectId(roomId), new ObjectId(teacherId), getSchoolId());
            obj.setMessage("");
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    @RequestMapping("/upZhuanXiangZu")
    @ResponseBody
    public RespObj upZhuanXiangZu(@RequestParam String xqid, @RequestParam String jxbId, @RequestParam String name, @RequestParam String roomId, @RequestParam String teacherId, @RequestParam String id) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            jxbService.upZhuanXiangZu(new ObjectId(xqid), new ObjectId(jxbId), name, new ObjectId(roomId), new ObjectId(teacherId), getSchoolId(), new ObjectId(id));
            obj.setMessage("");
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    @RequestMapping("/getZhuanXiangZuList")
    @ResponseBody
    public RespObj getZhuanXiangZuList(@RequestParam String xqid, @RequestParam String jxbId) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(jxbService.getZhuanXiangZuList(new ObjectId(xqid), new ObjectId(jxbId)));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    @RequestMapping("/getZhuan")
    @ResponseBody
    public RespObj getZhuan(@RequestParam String id) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(jxbService.getZhuan(new ObjectId(id)));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    @RequestMapping("/updateZhuanName")
    @ResponseBody
    public RespObj updateZhuanName(@RequestParam String id, @RequestParam String name) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            jxbService.updateZhuanName(new ObjectId(id), name);
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    @RequestMapping("/updateZhuanKS")
    @ResponseBody
    public RespObj updateZhuanKS(@RequestParam String id, @RequestParam Integer ks) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            jxbService.updateZhuanKS(new ObjectId(id), ks);
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }
    /**
     * 创建走班教学班
     *
     * @param xqid
     * @param gid
     * @return
     */
    @RequestMapping("/createJXB")
    @ResponseBody
    public RespObj createJXB(@ObjectIdType ObjectId xqid, @ObjectIdType ObjectId gid, @RequestParam Integer rl) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            String msg = jxbService.createJXB(getSchoolId(), gid, xqid, rl);
            obj.setMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    /**
     * 创建非走班教学班
     *
     * @param xqid
     * @param gid
     * @return
     */
    @RequestMapping("/createJXBIsNotZouBan")
    @ResponseBody
    public RespObj createJXBIsNotZouBan(@ObjectIdType ObjectId xqid, @ObjectIdType ObjectId gid) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            String msg = jxbService.createFeiZouBanJXB(getSchoolId(), gid, xqid);
            obj.setMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    /**
     * 查询所有的教学班
     *
     * @param xqid
     * @param gradeId
     * @param subjectId
     * @return
     */
    @UserDataCollection(pageTag = "n33", params = {"gradeId", "subjectId", "subType2"}, isAllCollection = false)
    @RequestMapping("/getAllJXBList")
    @ResponseBody
    public RespObj getAllJXBList(@RequestParam String xqid, @RequestParam String gradeId, @RequestParam String subjectId, @RequestParam Integer subType2) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(jxbService.getAllJXBList(new ObjectId(xqid), new ObjectId(gradeId), getSchoolId(), new ObjectId(subjectId), subType2));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    /**
     * 查询教学班学生数量
     *
     * @param id
     * @return
     */
    @RequestMapping("/getStuCount")
    @ResponseBody
    public RespObj getStuCount(@RequestParam String id) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(jxbService.getStuCount(new ObjectId(id)));

        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    /**
     * 删除教学班
     *
     * @param id
     * @return
     */
    @RequestMapping("/removeJXB")
    @ResponseBody
    public RespObj removeJXB(@RequestParam String id) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            jxbService.deleteJXB(new ObjectId(id));
            obj.setMessage("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    /**
     * 更新教学班
     *
     * @param map
     * @return
     */
    @RequestMapping("/updateJXBList")
    @ResponseBody
    public RespObj updateJXBList(@RequestBody Map<String, Object> map) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            ObjectId xqid = new ObjectId((String) map.get("xqid"));
            jxbService.updateJXB((List<Map<String, String>>) map.get("dtos"), xqid, getSchoolId(), new ObjectId((String) map.get("gid")));
            obj.setMessage("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    /**
     * 查询选该以下科目有多少人等级考，有多少人合格考
     *
     * @param
     * @return
     */
    @RequestMapping("/getCount")
    @ResponseBody
    public RespObj getCount(@RequestParam String xqid, @RequestParam String gid, @RequestParam String subid) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(jxbService.getCount(new ObjectId(xqid), getSchoolId(), new ObjectId(gid), new ObjectId(subid)));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    /**
     * 自建教学班
     *
     * @param
     * @return
     */
    @RequestMapping("/addJXBDto")
    @ResponseBody
    public RespObj getCount(@RequestBody N33_JXBDTO par) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            par.setSchoolId(getSchoolId().toString());
            obj.setMessage(jxbService.addJXBDto(par));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }


    @RequestMapping("/exportTemplate")
    @ResponseBody
    public void exportTemplate(HttpServletResponse response, @RequestParam String gradeId, @RequestParam String id) throws Exception {
        try {
            String[] teaList = infoSetService.getTeacherLists(getSchoolId(), new ObjectId(gradeId), new ObjectId(id));
            String[] roomList = infoSetService.getClassRoomLists(getSchoolId(), new ObjectId(gradeId));
            String[] classList = jxbService.getZhuanList(new ObjectId(id));
            jxbService.addUserImp(response, teaList, roomList, classList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ResponseBody
    @RequestMapping("/getStatusPK")
    public Map<String, Object> getStatusPK(HttpServletRequest request) {
        return jxbService.getStatus(request);
    }

    @RequestMapping("/addUserImpYu")
    @ResponseBody
    public RespObj addUserImpYu(@RequestParam("file") MultipartFile file, @RequestParam ObjectId xqid, @RequestParam ObjectId zuHeId, @RequestParam ObjectId gradeId,HttpServletResponse response,HttpServletRequest request) {

        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try {
            String fileName = file.getOriginalFilename();
            if (!fileName.endsWith(".xls") || !fileName.endsWith(".xls")) {
                respObj.setMessage("文件格式不正确");
            } else {
                jxbService.importUser(response,file.getInputStream(), xqid, getSchoolId(), zuHeId, gradeId,request);
                respObj.setCode(Constant.SUCCESS_CODE);
            }
        } catch (Exception e) {
            HttpSession session = request.getSession();
            Map<String, Object> status = new HashMap<String, Object>();
            status.put("st", -1);
            session.setAttribute("zhuangxiangStatus", status);
            e.printStackTrace();
            respObj.setMessage(e.getMessage());
        }
        return respObj;
    }
}
