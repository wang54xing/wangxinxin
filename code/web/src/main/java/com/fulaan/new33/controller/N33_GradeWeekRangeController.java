package com.fulaan.new33.controller;

import com.fulaan.annotation.ObjectIdType;
import com.fulaan.base.controller.BaseController;
import com.fulaan.new33.service.N33_GradeWeekRangeService;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/n33_gradeweekrange")
public class N33_GradeWeekRangeController extends BaseController {

    private N33_GradeWeekRangeService n33_gradeWeekRangeService = new N33_GradeWeekRangeService();

    @ResponseBody
    @RequestMapping("/getGradeWeekRangeByXqid")
    public Map<String,Object> getGradeWeekRangeByXqid(@ObjectIdType ObjectId xqid,@ObjectIdType ObjectId gid) {
        return n33_gradeWeekRangeService.getGradeWeekRangeByXqid(xqid,getSchoolId(),gid);
    }
    @ResponseBody
    @RequestMapping("/getGradeWeekRangeListByXqid")
    public List<Map<String,Object>> getGradeWeekRangeListByXqid(@ObjectIdType ObjectId xqid) {
        return n33_gradeWeekRangeService.getGradeWeekRangeByXqid(xqid,getSchoolId());
    }

    @ResponseBody
    @RequestMapping("/getGradeWeekRangeByXqidForView")
    public List<Map<String,Object>> getGradeWeekRangeByXqidForView(@ObjectIdType ObjectId xqid) {
        return n33_gradeWeekRangeService.getGradeWeekRangeByXqidForView(xqid,getSchoolId());
    }

    @ResponseBody
    @RequestMapping("/addGradeWeekRangeEntry")
    public RespObj addGradeWeekRangeEntry(@RequestBody Map<String,Object> param){
        RespObj resp = new RespObj(Constant.SUCCESS_CODE,"成功");
        try {
            n33_gradeWeekRangeService.addGradeWeekRangeEntry(param,getSchoolId());
        }
        catch (Exception e){
            e.printStackTrace();
            resp = new RespObj(Constant.FAILD_CODE,"失败");
        }
        return resp;
    }
    @ResponseBody
    @RequestMapping("/saveList")
    public RespObj saveList(@RequestBody List<Map<String,Object>> paramList){
        RespObj resp = new RespObj(Constant.SUCCESS_CODE,"成功");
        try {
            n33_gradeWeekRangeService.saveList(paramList,getSchoolId());
        }
        catch (Exception e){
            e.printStackTrace();
            resp = new RespObj(Constant.FAILD_CODE,"失败");
        }
        return resp;
    }

    @ResponseBody
    @RequestMapping("/saveListByXqid")
    public RespObj saveListByXqid(@RequestBody Map dto){
        RespObj resp = new RespObj(Constant.SUCCESS_CODE,"成功");
        try {
            n33_gradeWeekRangeService.saveListByXqid(dto,getSchoolId());
        }
        catch (Exception e){
            e.printStackTrace();
            resp = new RespObj(Constant.FAILD_CODE,"失败");
        }
        return resp;
    }

    @ResponseBody
    @RequestMapping("/updateRange")
    public RespObj updateRange(@ObjectIdType ObjectId id,  int start, int end){
        RespObj resp = new RespObj(Constant.SUCCESS_CODE,"成功");
        try {
            n33_gradeWeekRangeService.updateRange(id,start,end);
        }
        catch (Exception e){
            e.printStackTrace();
            resp = new RespObj(Constant.FAILD_CODE,"失败");
        }
        return resp;
    }

    @ResponseBody
    @RequestMapping("/initGradeWeekRangeEntry")
    public RespObj initGradeWeekRangeEntry(@RequestParam String xqid){
        RespObj resp = new RespObj(Constant.SUCCESS_CODE,"成功");
        try {
            n33_gradeWeekRangeService.initGradeWeekRangeEntry(getSchoolId(),new ObjectId(xqid));
        }
        catch (Exception e){
            e.printStackTrace();
            resp = new RespObj(Constant.FAILD_CODE,"失败");
        }
        return resp;
    }
}
