package com.fulaan.new33.controller;

import com.fulaan.annotation.ObjectIdType;
import com.fulaan.base.controller.BaseController;
import com.fulaan.new33.service.N33_SyncPaikeDataService;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/n33_syndata")
public class N33_SyncPaikeDataController extends BaseController {

    @Autowired
    private N33_SyncPaikeDataService n33_syncPaikeDataService;

    @ResponseBody
    @RequestMapping("/synAllData")
    public RespObj synAllData(@RequestBody List<Integer> types, @ObjectIdType ObjectId oldCiId, @ObjectIdType ObjectId newCiId, HttpServletRequest request){
        RespObj resp = new RespObj(Constant.SUCCESS_CODE);
        try{
            n33_syncPaikeDataService.synAllData(types,getSchoolId(),oldCiId,newCiId,request);
            resp.setMessage("成功");
        }
        catch (Exception e){
            resp = new RespObj(Constant.FAILD_CODE,"失败");
            e.printStackTrace();
        }
        return resp;
    }
    @ResponseBody
    @RequestMapping("/getStatus")
    public Map<String,Object> getStatus(HttpServletRequest request){
        return n33_syncPaikeDataService.getStatus(request);
    }

    @ResponseBody
    @RequestMapping("/getSynRecord")
    public Map<String,Object> getSynRecord(@ObjectIdType ObjectId ciId){
        return n33_syncPaikeDataService.getSynRecord(ciId);
    }
}
