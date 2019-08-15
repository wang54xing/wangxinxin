package com.fulaan.new33.controller;

import com.fulaan.base.controller.BaseController;
import com.fulaan.new33.service.N33_AutoPaiKeService;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by wang_xinxin on 2018/4/24.
 */
@Controller
@RequestMapping("/autoPK")
public class N33_AutoPaiKeController extends BaseController {

    @Autowired
    private N33_AutoPaiKeService autoPaiKeService;

    //教学班信息完备性检查
    @RequestMapping("/checkJXBInfo")
    @ResponseBody
    public RespObj checkJXBInfo(String gradeId) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            autoPaiKeService.checkJXBInfo(getSchoolId(),getDefauleTermId(),gradeId);
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }

        return obj;
    }

    //教室/教师的课时合理性（增加页面）
    //教学班信息完备性检查


    //获得跨头教师列表

    //获得教师的教学班列表

    //按照教师总课时数排序、总事务数

}
