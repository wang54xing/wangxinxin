package com.fulaan.new33.controller;

import com.fulaan.base.controller.BaseController;
import com.fulaan.new33.dto.N33_ZjkjDTO;
import com.fulaan.new33.service.N33_ZjkjService;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/n33zjkj")
public class N33_ZjkjController extends BaseController {

    @Autowired
    private N33_ZjkjService n33_zjkjService;

    /**
     * 减调课
     * @param zkbId
     * @return
     */
    @RequestMapping("/setJkj")
    @ResponseBody
    public RespObj setJkj(String zkbId) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            n33_zjkjService.setJkj(zkbId,getUserId());
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    /**
     * 减调课
     * @param zkbId
     * @return
     */
    @RequestMapping("/setZkj")
    @ResponseBody
    public RespObj setZkj(String zkbId,String jxbId) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            n33_zjkjService.setZkj(zkbId,jxbId,getUserId());
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    /**
     *
     * @param xqId
     * @param gradeId
     * @return
     */
    @RequestMapping("/getZjkjList")
    @ResponseBody
    public RespObj getZjkjList(String xqId,String ciId,String gradeId) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            List<N33_ZjkjDTO> zjkjDTOS = n33_zjkjService.getZjkjList(xqId,ciId,gradeId,getSchoolId());
            obj.setMessage(zjkjDTOS);
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    /**
     *
     * @param zkbId
     * @return
     */
    @RequestMapping("/getJXBList")
    @ResponseBody
    public RespObj getJXBList(String zkbId,String ciId) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            List<N33_ZjkjDTO> zjkjDTOS = n33_zjkjService.getJXBList(zkbId,ciId);
            obj.setMessage(zjkjDTOS);
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }
}
