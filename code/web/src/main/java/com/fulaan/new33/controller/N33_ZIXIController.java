package com.fulaan.new33.controller;

import com.fulaan.annotation.ObjectIdType;
import com.fulaan.base.controller.BaseController;
import com.fulaan.new33.service.N33_ZIXIService;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
@Controller
@RequestMapping("/n33_zixi")
public class N33_ZIXIController extends BaseController {

    private N33_ZIXIService n33_zixiService = new N33_ZIXIService();

    @RequestMapping("/getTable")
    @ResponseBody
    public RespObj getTable(@ObjectIdType ObjectId ciId,@ObjectIdType ObjectId gradeId){
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            Map<String,Object> result = n33_zixiService.getTable(ciId,gradeId,getSchoolId());
            obj.setMessage(result);
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }
    @RequestMapping("/getStudents")
    @ResponseBody
    public RespObj getStudents(@ObjectIdType ObjectId gradeId,@ObjectIdType ObjectId ciId, @RequestBody List<String> studentIds,Integer x,Integer y){
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            List<Map<String,Object>> result = n33_zixiService.getStudents(getSchoolId(),gradeId,ciId,studentIds,x,y);
            obj.setMessage(result);
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    @RequestMapping("/createZiXiBan")
    @ResponseBody
    public RespObj createZiXiBan(String name,@ObjectIdType ObjectId ykbId,@ObjectIdType ObjectId roomId,@ObjectIdType ObjectId ciId,
                              @ObjectIdType ObjectId gid,@ObjectIdType ObjectId tid){
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            n33_zixiService.createZiXiBan(getSchoolId(),name,ykbId,roomId,ciId,gid,tid);
            obj.setMessage("成功");
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;

    }

    @RequestMapping("/getRoomByXY")
    @ResponseBody
    public RespObj getRoomByXY(@ObjectIdType ObjectId ciId,@ObjectIdType ObjectId gid,Integer x,Integer y){
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            List<Map<String,Object>> list = n33_zixiService.getRoomByXY(getSchoolId(),ciId,gid,x,y);
            obj.setMessage(list);
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;

    }

    @RequestMapping("/getTeachersByXY")
    @ResponseBody
    public RespObj getTeachersByXY(@ObjectIdType ObjectId ciId,@ObjectIdType ObjectId gid,Integer x,Integer y){
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            List<Map<String,Object>> list = n33_zixiService.getTeachersByXY(getSchoolId(),ciId,gid,x,y);
            obj.setMessage(list);
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;

    }

    @RequestMapping("/getZiXiBanByXY")
    @ResponseBody
    public RespObj getZiXiBanByXY(@ObjectIdType ObjectId ciId,@ObjectIdType ObjectId gid,Integer x,Integer y){
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            List<Map<String,Object>> list = n33_zixiService.getZiXiBanByXY(getSchoolId(),ciId,gid,x,y);
            obj.setMessage(list);
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;

    }

    @RequestMapping("/getZiXiBan")
    @ResponseBody
    public RespObj getZiXiBan(@ObjectIdType ObjectId ciId,@ObjectIdType ObjectId gid){
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            List<Map<String,Object>> list = n33_zixiService.getZiXiBan(getSchoolId(),ciId,gid);
            obj.setMessage(list);
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;

    }

    @RequestMapping("/addStudentsToZiXi")
    @ResponseBody
    public RespObj addStudentsToZiXi(@ObjectIdType ObjectId ciId,@ObjectIdType ObjectId id,@ObjectIdType ObjectId gid,@RequestBody List<String> students,Integer x,Integer y){
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            n33_zixiService.addStudentsToZiXi(ciId,getSchoolId(),id,gid,students,x,y);
            obj.setMessage("成功");
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    @RequestMapping("/removeZiXiBan")
    @ResponseBody
    public RespObj removeZiXiBan(@ObjectIdType ObjectId ciId,@ObjectIdType ObjectId id){
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            n33_zixiService.removeZiXiBan(ciId,getSchoolId(),id);
            obj.setMessage("成功");
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;

    }
    @RequestMapping("/removeZiXiBanGid")
    @ResponseBody
    public RespObj removeZiXiBanByGid(@ObjectIdType ObjectId ciId,@ObjectIdType ObjectId gid){
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            n33_zixiService.removeZiXiBanByGid(ciId,getSchoolId(),gid);
            obj.setMessage("成功");
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;

    }

    @RequestMapping("/removeZiXiBanGidAndCids")
    @ResponseBody
    public RespObj removeZiXiBanGidAndCids(@RequestBody Map map){
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            List<String> ciIds = (List<String>) map.get("ciIds");
            List<ObjectId> ciIdList = MongoUtils.convertToObjectIdList(ciIds);
            ObjectId gid = new ObjectId((String)map.get("gid"));
            for (ObjectId ciId : ciIdList) {
                n33_zixiService.removeZiXiBanByGid(ciId,getSchoolId(),gid);
            }
            obj.setMessage("成功");
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;

    }

    @RequestMapping("/updateRoomTeacher")
    @ResponseBody
    public RespObj updateRoomTeacher(@ObjectIdType ObjectId ciId,@ObjectIdType ObjectId id,@ObjectIdType ObjectId roomId,@ObjectIdType ObjectId teaId){
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            n33_zixiService.updateRoomTeacher(ciId,getSchoolId(),id,roomId,teaId);
            obj.setMessage("成功");
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;

    }
    
    //导出自习课
    @RequestMapping("/exportZiXiBan")
    @ResponseBody
    public RespObj exportZiXiBan(@ObjectIdType ObjectId ciId,@ObjectIdType ObjectId gid,HttpServletResponse response){
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            //n33_zixiService.exportZiXiBan(getSchoolId(),ciId,gid,response);
        	n33_zixiService.exportZiXiBanTwo(getSchoolId(),ciId,gid,response);
            obj.setMessage("成功");
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;

    }

    @RequestMapping("/autoArranage")
    @ResponseBody
    public RespObj autoArranage(@ObjectIdType ObjectId ciId,@ObjectIdType ObjectId gradeId,@RequestBody Map<String,Object> table,int cap,int type){
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            List<Map<String,Object>> list = n33_zixiService.autoArranage(ciId,gradeId,getSchoolId(),table,cap,type);
            obj.setMessage(list);
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;

    }
}
