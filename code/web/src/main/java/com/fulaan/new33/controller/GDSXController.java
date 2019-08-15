package com.fulaan.new33.controller;

import com.fulaan.base.controller.BaseController;
import com.fulaan.new33.dto.isolate.N33_GDSXDTO;
import com.fulaan.new33.service.N33_GDSXService;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/gdsx")
public class GDSXController extends BaseController {

    @Autowired
    private N33_GDSXService gdsxService;

    @ResponseBody
    @RequestMapping("/updateGDSX")
    public RespObj updateGDSX(@RequestBody List<N33_GDSXDTO> gdsxdtos) {
        RespObj resp = new RespObj(Constant.SUCCESS_CODE);
        try {
            //N33_GDSXDTO gdsxdto = gdsxdtos.get(0);
            gdsxService.updateGDSX(gdsxdtos, getSchoolId());
        } catch (Exception e) {
            resp = new RespObj(Constant.FAILD_CODE);
            e.printStackTrace();
        }
        return resp;
    }

    @ResponseBody
    @RequestMapping("/getGDSXByXqid")
    public RespObj getGDSXByXqid(@RequestParam String xqid, @RequestParam String gid) {
        RespObj resp = new RespObj(Constant.SUCCESS_CODE);
        try {
            resp.setMessage(gdsxService.getGDSXByXqid(new ObjectId(xqid), getSchoolId(), new ObjectId(gid)));
        } catch (Exception e) {
            resp = new RespObj(Constant.FAILD_CODE);
            e.printStackTrace();
        }
        return resp;
    }

    @ResponseBody
    @RequestMapping("/getGDSXBySidAndXqid")
    public RespObj getGDSXBySidAndXqid(@RequestParam String xqid, @RequestParam String gid, @RequestParam String teaId, @RequestParam String ciId) {
        RespObj resp = new RespObj(Constant.SUCCESS_CODE);
        try {
            resp.setMessage(gdsxService.getGDSXBySidAndXqidAndTeaId(new ObjectId(xqid), getSchoolId(), new ObjectId(gid), new ObjectId(teaId), new ObjectId(ciId)));
        } catch (Exception e) {
            resp = new RespObj(Constant.FAILD_CODE);
            e.printStackTrace();
        }
        return resp;
    }

    @ResponseBody
    @RequestMapping("/getGDSXBySidAndXqidTea")
    public RespObj getGDSXBySidAndXqidTea(@RequestParam String xqid, @RequestParam String userId) {
        RespObj resp = new RespObj(Constant.SUCCESS_CODE);
        try {
            resp.setMessage(gdsxService.getGDSXBySidAndXqidTea(new ObjectId(xqid), getSchoolId(), new ObjectId(userId)));
        } catch (Exception e) {
            resp = new RespObj(Constant.FAILD_CODE);
            e.printStackTrace();
        }
        return resp;
    }

    @ResponseBody
    @RequestMapping("/deleteGDSX")
    public RespObj deleteGDSX(@RequestParam String xqid, @RequestParam String gradeId) {
        RespObj resp = new RespObj(Constant.SUCCESS_CODE);
        try {
            gdsxService.deleteGDSX(new ObjectId(xqid), getSchoolId(), new ObjectId(gradeId));
        } catch (Exception e) {
            resp = new RespObj(Constant.FAILD_CODE);
            e.printStackTrace();
        }
        return resp;
    }

    @ResponseBody
    @RequestMapping("/getShiWuConflict")
    public RespObj getShiWuConflict(@RequestParam String paikeci, @RequestParam String gradeId) {
        RespObj resp = new RespObj(Constant.SUCCESS_CODE);
        try {
            List<Map<String,Object>> result = gdsxService.getShiWuConflict(new ObjectId(paikeci),new ObjectId(gradeId),getSchoolId());
            resp.setMessage(result);
        } catch (Exception e) {
            resp = new RespObj(Constant.FAILD_CODE);
            e.printStackTrace();
        }
        return resp;
    }


}
