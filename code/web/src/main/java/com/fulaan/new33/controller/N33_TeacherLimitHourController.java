package com.fulaan.new33.controller;

import com.fulaan.annotation.ObjectIdType;
import com.fulaan.base.controller.BaseController;
import com.fulaan.new33.service.N33_TeacherLimitHourService;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;
@Controller
@RequestMapping("/n33_teacherlimithour")
public class N33_TeacherLimitHourController  extends BaseController{

    private N33_TeacherLimitHourService n33_TeacherLimitHourService = new N33_TeacherLimitHourService();

    @ResponseBody
    @RequestMapping("/getTeacherLimitEntryByXqid")
    public Map<String,Object> getTeacherLimitEntryByXqid(@ObjectIdType ObjectId xqid,@ObjectIdType ObjectId gid) {
        return n33_TeacherLimitHourService.getTeacherLimitEntryByXqid(xqid,getSchoolId(),gid);
    }
    @ResponseBody
    @RequestMapping("/getTeacherLimitListByXqid")
    public List<Map<String,Object>> getTeacherLimitListByXqid(@RequestBody Map<String,ObjectId> param) {
        return n33_TeacherLimitHourService.getTeacherLimitEntryByXqid(param.get("xqid"),param.get("sid"));
    }
    @ResponseBody
    @RequestMapping("/addLimitEntry")
    public RespObj addGradeWeekRangeEntry(@RequestBody Map<String,Object> param){
        RespObj resp = new RespObj(Constant.SUCCESS_CODE,"成功");
        try {
            n33_TeacherLimitHourService.addLimitEntry(param,getSchoolId());
        }
        catch (Exception e){
            e.printStackTrace();
            resp = new RespObj(Constant.FAILD_CODE,"失败");
        }
        return resp;
    }
//    @ResponseBody
//    @RequestMapping("/saveList")
//    public RespObj saveList(@RequestBody List<Map<String,Object>> paramList){
//        RespObj resp = new RespObj(Constant.SUCCESS_CODE,"成功");
//        try {
//            n33_TeacherLimitHourService.saveList(paramList);
//        }
//        catch (Exception e){
//            e.printStackTrace();
//            resp = new RespObj(Constant.FAILD_CODE,"失败");
//        }
//        return resp;
//    }
    @ResponseBody
    @RequestMapping("/updateHour")
    public RespObj updateHour(@ObjectIdType ObjectId id,  int hour){
        RespObj resp = new RespObj(Constant.SUCCESS_CODE,"成功");
        try {
            n33_TeacherLimitHourService.updateHour(id,hour);
        }
        catch (Exception e){
            e.printStackTrace();
            resp = new RespObj(Constant.FAILD_CODE,"失败");
        }
        return resp;
    }
}
