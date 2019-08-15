package com.fulaan.new33.controller.autopk;

import com.fulaan.base.controller.BaseController;
import com.fulaan.new33.dto.autopk.N33_PartClassSetDTO;
import com.fulaan.new33.service.autopk.N33_PartClassSetService;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;
import com.sys.utils.StringUtil;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/autoPk/partClassSet")
public class N33_PartClassSetController extends BaseController {

    @Autowired
    private N33_PartClassSetService partClassSetService;


    /**
     * 更新最大分段信息
     * @param gradeId
     * @param ciId
     * @param maxFenDuan
     */
    @RequestMapping("/updateMaxFenDuanSet")
    public RespObj updateMaxFenDuanSet(@RequestParam String gradeId, @RequestParam String ciId, @RequestParam int maxFenDuan) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            ObjectId schoolId = getSchoolId();
            partClassSetService.updateMaxFenDuanSet(schoolId, new ObjectId(gradeId), new ObjectId(ciId), maxFenDuan);
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    /**
     * 查询年级下的班级分段信息
     * @param gradeId
     * @param ciId
     * @return
     */
    @RequestMapping("/getPartMaxFenDuan")
    public RespObj getPartMaxFenDuan(@RequestParam String gradeId, @RequestParam String ciId) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            ObjectId schoolId = getSchoolId();
            if(StringUtil.isEmpty(gradeId)&&StringUtil.isEmpty(ciId)) {
                int maxFenDuan = partClassSetService.getMaxFenDuan(schoolId, new ObjectId(gradeId), new ObjectId(ciId));
                obj.setMessage(maxFenDuan);
            }else{
                obj.setMessage(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }
    /**
     * 查询使用中的年级下的班级分段信息
     * @param gradeId
     * @param ciId
     * @return
     */
    @RequestMapping("/getUsePartMaxFenDuan")
    public RespObj getUsePartMaxFenDuan(@RequestParam String gradeId, @RequestParam String ciId) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            ObjectId schoolId = getSchoolId();
            if(StringUtil.isEmpty(gradeId)&&StringUtil.isEmpty(ciId)) {
                int maxFenDuan = partClassSetService.getUsePartMaxFenDuan(schoolId, new ObjectId(gradeId), new ObjectId(ciId));
                obj.setMessage(maxFenDuan);
            }else{
                obj.setMessage(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }


    /**
     * 查询年级下的班级分段信息
     * @param gradeId
     * @param ciId
     * @return
     */
    @RequestMapping("/getPartClassInfo")
    public RespObj getPartClassInfo(@RequestParam String gradeId, @RequestParam String ciId) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            ObjectId schoolId = getSchoolId();
            Map reMap= partClassSetService.getPartClassInfo(schoolId, new ObjectId(gradeId), new ObjectId(ciId));
            obj.setMessage(reMap);
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }


    /**
     * 查询年级下的班级分段信息
     * @param gradeId
     * @param ciId
     * @return
     */
    @RequestMapping("/getFenDuanPartClassInfo")
    public RespObj getFenDuanPartClassInfo(
            @RequestParam String gradeId,
            @RequestParam String ciId,
            @RequestParam int fenDuan
    ) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            ObjectId schoolId = getSchoolId();
            List<N33_PartClassSetDTO> list = partClassSetService.getFenDuanPartClassInfo(schoolId, new ObjectId(gradeId), new ObjectId(ciId), fenDuan);
            obj.setMessage(list);
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    /**
     * 修改班级分段
     * @param id
     * @param fenDuan
     */
    @RequestMapping("/updatePartClassFenDuan")
    public RespObj updatePartClassFenDuan(@RequestParam String id, @RequestParam int fenDuan) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            partClassSetService.updatePartClassFenDuan(new ObjectId(id), fenDuan);
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }
}
