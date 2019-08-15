package com.fulaan.new33.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.fulaan.new33.dto.AfreshSubRuleDTO;
import com.fulaan.new33.dto.AfreshTypeDTO;
import com.fulaan.new33.dto.isolate.N33_KSDTO;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fulaan.annotation.ObjectIdType;
import com.fulaan.annotation.UserDataCollection;
import com.fulaan.base.controller.BaseController;
import com.fulaan.new33.service.N33_AfreshNewService;
import com.fulaan.new33.service.N33_AfreshService;
import com.fulaan.new33.service.isolate.N33_DefaultTermService;
import com.pojo.new33.afresh.ChooseSetEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;

/**
 * Created by James on 2019-05-10.
 *  重分行政班
 */
@Controller
@RequestMapping("afreshClass")
public class N33_AfreshController extends BaseController {
    @Autowired
    private N33_AfreshService n33_afreshService;
    @Autowired
    private N33_DefaultTermService termService;

    //新
    @Autowired
    private N33_AfreshNewService n33_afreshNewService;

    //查询成绩单
    @RequestMapping(value = "/getExamListByGradeTime")
    @ResponseBody
    public List<Map<String,String>> getExamListByGradeTime(@RequestBody Map<String,Object> map){
        try {
            List<String> gradeIds = (List<String>) map.get("gradeIds");
            List<Map<String,String>> timespan = (List<Map<String, String>>) map.get("timespan");
            return n33_afreshService.getExamListByGradeTime2(gradeIds, timespan);
        }
        catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 查询默认学期
     * @param
     * @return
     */
    @RequestMapping("/getDefaultTerm")
    @ResponseBody
    public RespObj getDefaultTerm() {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(termService.getDefaultTermAndTime(getSchoolId()));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    /**
     * 查询分班结果
     * @param xqid
     * @param gradeId
     * @param type
     * @return
     */
    @UserDataCollection(pageTag = "n33", params = {"gradeId","type"}, isAllCollection = false)
    @RequestMapping("/getStuSelectResultByClass")
    @ResponseBody
    public Map<String, Object> getStuSelectResultByClass(@ObjectIdType ObjectId xqid, @ObjectIdType ObjectId gradeId, Integer type, String reportId) {
        return n33_afreshService.getNewStuSelectResultByClass(xqid, getSchoolId(), gradeId, type, reportId);
    }

    /**
     * 导出分班结果
     * @param xqid
     * @param gradeId
     * @param response
     */
    @RequestMapping("/exportStuSelectResult")
    @ResponseBody
    public void exportStuSelectResult(@ObjectIdType ObjectId xqid, @ObjectIdType ObjectId gradeId,HttpServletResponse response){
        n33_afreshService.exportStuSelectResult(xqid,getSchoolId(), gradeId, response);
    }


    @RequestMapping("/addAfreshSetEntry")
    @ResponseBody
    public RespObj addAfreshSetEntry(@ObjectIdType ObjectId xqid,
                                     @ObjectIdType ObjectId gradeId,
                                     @ObjectIdType ObjectId reporId,
                                     @RequestParam(value = "setId",defaultValue = "") String setId){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage("保存失败");
        }catch (Exception e){
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setMessage("保存失败");
        }
        return respObj;
    }

    //james
    @RequestMapping("/getAfreshSubjectList") 
    @ResponseBody
    public RespObj getAfreshSubjectList(@ObjectIdType ObjectId xqid,
                                     @ObjectIdType ObjectId gradeId){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            List<Map<String,Object>> result = n33_afreshService.getAfreshSubjectList(xqid, getSchoolId(), gradeId);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(result);
        }catch (Exception e){
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setMessage("保存失败");
        }
        return respObj;
    }
    
    //conn
    @RequestMapping("/getNewAfreshSubList")
    @ResponseBody
    public RespObj getNewAfreshSubList(@ObjectIdType ObjectId xqid,
                                     @ObjectIdType ObjectId gradeId,
                                     @RequestParam String type){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            List<Map> result = n33_afreshService.getNewAfreshSubList(xqid, getSchoolId(), gradeId,type);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(result);
        }catch (Exception e){
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setMessage("保存失败");
        }
        return respObj;
    }
    
    @RequestMapping("/updSubList")
    @ResponseBody
    public RespObj updSubList(@RequestBody Map repMap){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
        	List<Map> chooseSetList = new ArrayList<Map>();
        	chooseSetList = (List<Map>) repMap.get("chooseSetList");
            n33_afreshService.updSubList(chooseSetList);
            respObj.setCode(Constant.SUCCESS_CODE);
        }catch (Exception e){
        	e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setMessage("保存失败");
        }
        return respObj;
    }

    @RequestMapping("/goAfreshClassEntry")
    @ResponseBody
    public RespObj goAfreshClassEntry(@RequestBody Map<String,Object> map){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            String xqid = (String) map.get("xqid");
            String gradeId = (String) map.get("gradeId");
            String reportId = (String) map.get("reportId");
            List<Map<String,Object>> ruleMapList = (List<Map<String, Object>>) map.get("ruleMapList");
            n33_afreshNewService.createAfreshClassEntry(new ObjectId(xqid), getSchoolId(), new ObjectId(gradeId), reportId, ruleMapList);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage("重分班成功");
        }catch (Exception e){
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setMessage(e.getMessage());
            e.printStackTrace();
        }
        return respObj;
    }
    
    @RequestMapping("/addAfrClass")
    @ResponseBody
    public RespObj addAfrClass(@RequestBody Map<String,Object> map){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            String termId = (String) map.get("termId");
            String gradeId = (String) map.get("gradeId");
            n33_afreshNewService.addAfrClass(new ObjectId(termId), getSchoolId(), new ObjectId(gradeId));
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage("重分班成功");
        }catch (Exception e){
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setMessage(e.getMessage());
            e.printStackTrace();
        }
        return respObj;
    }
    

    @RequestMapping("/updateClassXh")
    @ResponseBody
    public RespObj updateClassXh(
            @ObjectIdType ObjectId classId,
            @RequestParam(value = "xh", defaultValue = "0") int xh
    ){
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        try{
            Map<String, Object> reMap = n33_afreshService.updateClassXh(classId, xh);
            respObj.setMessage(reMap);
        }catch (Exception e){
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setMessage("修改失败");
        }
        return respObj;
    }

    @RequestMapping("/updateClass")
    @ResponseBody
    public RespObj updateClass(
            @RequestBody List<Map<String, Object>> stuInfos
    ){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
        	n33_afreshService.updateStusClass(stuInfos);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage("调班成功");
        }catch (Exception e){
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setMessage("调班失败");
        }
        return respObj;
    }

    @RequestMapping("/selectClassLsit")
    @ResponseBody
    public RespObj selectClassLsit(
            @ObjectIdType ObjectId oldClassId,
            @ObjectIdType ObjectId newClassId,
            @ObjectIdType ObjectId userId
    ){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{

            n33_afreshService.updateClass(userId,oldClassId,newClassId);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage("调班成功");
        }catch (Exception e){
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setMessage("调班失败");
        }
        return respObj;
    }

    @RequestMapping("/tongBuClass")
    @ResponseBody
    public RespObj tongBuClass(
            @ObjectIdType ObjectId xqid,
            @ObjectIdType ObjectId gradeId,
            @RequestParam(value = "classId", defaultValue = "") String classId
    ){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            Map<String,Object> result = n33_afreshService.selectClassLsit(xqid, getSchoolId(), gradeId, classId);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(result);
        }catch (Exception e){
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setMessage("查询失败");
        }
        return respObj;
    }
    
    //导出重分行政班数据
    @RequestMapping("/exportClasStu")
    @ResponseBody
    public void exportClasStu(@ObjectIdType ObjectId xqid, @ObjectIdType ObjectId gradeId, Integer type,HttpServletResponse response) {
         n33_afreshService.exportClasStu(xqid, getSchoolId(), gradeId, type, response);
    }

    //删除班级
    @RequestMapping("/delClassById")
    @ResponseBody
    public RespObj delClassById(String  classIds){
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        try{
            n33_afreshService.delClassById(classIds);
        }catch (Exception e){
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setMessage("修改失败");
        }
        return respObj;
    }
    
    @RequestMapping("/addClass")
    @ResponseBody
    public RespObj addClass(
            @ObjectIdType ObjectId gradeId,
            @ObjectIdType ObjectId termId){
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        try{
            n33_afreshService.addClass(getSchoolId(), gradeId, termId);
        }catch (Exception e){
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setMessage("修改失败");
        }
        return respObj;
    }

    @RequestMapping("/editAfreshType")
    @ResponseBody
    public RespObj editAfreshType(
            @RequestBody AfreshTypeDTO dto
    ){
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        try{
            String schoolId = getSchoolId().toString();
            dto.setSchoolId(schoolId);
            n33_afreshService.editAfreshType(dto);
            respObj.setMessage("设置成功");
        }catch (Exception e){
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setMessage("设置失败");
        }
        return respObj;
    }

    @RequestMapping("/getAfreshTypeList")
    @ResponseBody
    public RespObj getAfreshTypeList(
            @ObjectIdType ObjectId gradeId,
            @ObjectIdType ObjectId ciId
    ){
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        try{
            ObjectId schoolId = getSchoolId();
            List<AfreshTypeDTO> list = n33_afreshService.getAfreshTypeList(schoolId, gradeId, ciId);
            respObj.setMessage(list);
        }catch (Exception e){
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setMessage("查询失败");
        }
        return respObj;
    }

    @RequestMapping("/editAfreshSubRuleList")
    @ResponseBody
    public RespObj editAfreshSubRuleList(
            @RequestBody List<AfreshSubRuleDTO> dtos
    ){
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        try{
            String schoolId = getSchoolId().toString();
            n33_afreshService.editAfreshSubRuleList(dtos, schoolId);
            respObj.setMessage("设置成功");
        }catch (Exception e){
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setMessage("设置失败");
        }
        return respObj;
    }

    @RequestMapping("/getDkyxSubList")
    @ResponseBody
    public RespObj getDkyxSubList(
            @ObjectIdType ObjectId gradeId,
            @ObjectIdType ObjectId ciId,
            @RequestParam(value = "afreshType") int afreshType

    ){
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        try{
            ObjectId schoolId = getSchoolId();
            List<AfreshSubRuleDTO> list = n33_afreshService.getDkyxSubList(schoolId, gradeId, ciId, afreshType);
            respObj.setMessage(list);
        }catch (Exception e){
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setMessage("查询失败");
        }
        return respObj;
    }
}
