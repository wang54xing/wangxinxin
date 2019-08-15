package com.fulaan.new33.controller;

import com.fulaan.annotation.UserDataCollection;
import com.fulaan.base.controller.BaseController;
import com.fulaan.new33.service.N33_AutoFenBanService;
import com.fulaan.new33.service.N33_AutoPkService;
import com.fulaan.new33.service.isolate.N33_VirtualClassService;
import com.pojo.utils.MongoUtils;
import com.qiniu.api.net.Http;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/virtualClass")
public class N33_VirtualClassController extends BaseController {

    @Autowired
    private N33_VirtualClassService virtualClassService;
    @Autowired
    private N33_AutoFenBanService autoFenBanService;

    @Autowired
    private N33_AutoPkService pkService;

    @ResponseBody
    @RequestMapping("/getStatusSetJXB")
    public Map<String, Object> getStatusSetJXB(HttpServletRequest request) {
        return virtualClassService.getStatus(request);
    }

    @RequestMapping("/initVirturlClass")
    @ResponseBody
    public RespObj initVirturlClass(@RequestParam String xqid, @RequestParam String gradeId) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            virtualClassService.initVirtualClass(new ObjectId(gradeId), new ObjectId(xqid), getSchoolId());
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    @UserDataCollection(pageTag = "n33", params = {"gradeId", "zouBanType"}, isAllCollection = false)
    @RequestMapping("/getVirturlClass")
    @ResponseBody
    public RespObj getVirturlClass(@RequestParam String xqid, @RequestParam String gradeId, @RequestParam Integer zouBanType, @RequestParam Integer isHide) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(virtualClassService.getVirtualClassByXqidAndGradeId(new ObjectId(xqid), getSchoolId(), new ObjectId(gradeId), zouBanType, isHide));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    @RequestMapping("/finishFenBan")
    @ResponseBody
    public RespObj finishFenBan(@RequestParam String xqid, @RequestParam String subId, @RequestParam String id, @RequestParam Integer isFinish) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            virtualClassService.finishFenBan(new ObjectId(xqid), new ObjectId(subId), new ObjectId(id), isFinish);
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    @RequestMapping("/shiFangXuNiBan")
    @ResponseBody
    public RespObj shiFangXuNiBan(@RequestParam String id) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            virtualClassService.shiFangXuNiBan(new ObjectId(id), getDefaultPaiKeTerm().getPaikeci());
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    @RequestMapping("/selectJXB")
    @ResponseBody
    public RespObj selectJXB(@RequestParam String xuNiBanId, @RequestParam String subId, @RequestParam String jxbId, @RequestParam String oJxbId) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(virtualClassService.selectJXB(new ObjectId(xuNiBanId), new ObjectId(jxbId), new ObjectId(subId), oJxbId, getSchoolId()));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    @RequestMapping("/getVirtualClassForCombineClass")
    @ResponseBody
    public RespObj getVirtualClassForCombineClass(@RequestParam String virtualClassId, @RequestParam Integer type, @RequestParam String xqid, @RequestParam String gradeId) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(virtualClassService.getVirtualClassForCombineClass(new ObjectId(virtualClassId), type, new ObjectId(xqid), getSchoolId(), new ObjectId(gradeId)));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    /**
     * 拼班操作
     *
     * @param pinId
     * @param pinById
     * @param xqid
     * @param gradeId
     * @return
     */
    @RequestMapping("/puzzleClass")
    @ResponseBody
    public RespObj puzzleClass(@RequestParam String pinId, @RequestParam String pinById, @RequestParam String xqid, @RequestParam String gradeId) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            virtualClassService.puzzleClass(new ObjectId(pinId), new ObjectId(pinById), new ObjectId(xqid), new ObjectId(gradeId), getSchoolId());
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    @RequestMapping("/clearJXB")
    @ResponseBody
    public RespObj clearJXB(@RequestParam String xuNiBanId, @RequestParam String subId, @RequestParam String jxbId) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            virtualClassService.clearJXB(new ObjectId(xuNiBanId), new ObjectId(jxbId), new ObjectId(subId), getSchoolId());
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    @RequestMapping("/clearAllJxb")
    @ResponseBody
    public RespObj clearAllJxb(@RequestParam String xqid, @RequestParam String gradeId, @RequestParam Integer type) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            virtualClassService.clearAllJxb(getSchoolId(), new ObjectId(xqid), new ObjectId(gradeId), 0);
            virtualClassService.clearAllJxb(getSchoolId(), new ObjectId(xqid), new ObjectId(gradeId), 1);
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    @RequestMapping("/AutoFenBanBySubject")
    @ResponseBody
    public RespObj AutoFenBanBySubject(@RequestParam String xqid, @RequestParam String gradeId) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            autoFenBanService.AutoFenBanBySubject(getSchoolId(), new ObjectId(gradeId), new ObjectId(xqid));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    @RequestMapping("/AutoSetJXB")
    @ResponseBody
    public RespObj AutoSetJXB(@RequestParam String xqid, @RequestParam String gradeId, HttpServletRequest request) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            autoFenBanService.AutoSelectJXB(getSchoolId(), new ObjectId(gradeId), new ObjectId(xqid), request);
        } catch (Exception e) {
            HttpSession session = request.getSession();
            session.removeAttribute("autoSetJXB");
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    /***
     * 教学班列表是否存在冲突
     * @param dto
     * @return
     */
    @RequestMapping("/getJxbListIsChongTu")
    @ResponseBody
    public RespObj getJxbListIsChongTu(@RequestBody Map dto) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            List<String> jxbIds = (List<String>) dto.get("jxbIds");
            ObjectId jxbId = new ObjectId((String)dto.get("jxbId"));
            List<ObjectId> list = MongoUtils.convertToObjectIdList(jxbIds);
            obj.setMessage(pkService.get教学班列表是否存在冲突(list,jxbId));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }
}
