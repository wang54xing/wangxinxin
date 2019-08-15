package com.fulaan.dzbp.controller;

import com.fulaan.annotation.SessionNeedless;
import com.fulaan.base.controller.BaseController;
import com.sys.utils.DateTimeUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@RequestMapping("/dzbpPage")
@Controller
public class DzbpPageController extends BaseController {
    /**
     * 页面跳转
     * @param clientId
     * @return
     */
    @SessionNeedless
    @RequestMapping(value = "/classKeBiao")
    public String dzbpClassKeBiao(
            @RequestParam(value = "clientId", defaultValue = "") String clientId,
            Map<String,Object> model
    ){
        model.put("clientId", clientId);
        String currCnDate = DateTimeUtils.getChineseDate();
        model.put("currCnDate", currCnDate);
        String currDate = DateTimeUtils.getCurrDate();
        model.put("currDate", currDate);
        String currWeekDate = DateTimeUtils.getCurrWeekDate();
        model.put("currWeekDate", currWeekDate);
        int currWeekIndex = DateTimeUtils.getCurrWeekIndex();
        model.put("currWeekIndex", currWeekIndex);
        return "dzbp/classKeBiao";
    }

    /**
     * 页面跳转
     * @param classId
     * @return
     */
    @SessionNeedless
    @RequestMapping(value = "/2/classKeBiao")
    public String dzbpClassKeBiaoTwo(
            @RequestParam(value = "schoolId", defaultValue = "") String schoolId,
            @RequestParam(value = "classId", defaultValue = "") String classId,
            Map<String,Object> model
    ){
        model.put("schoolId", schoolId);
        model.put("classId", classId);
        String currCnDate = DateTimeUtils.getChineseDate();
        model.put("currCnDate", currCnDate);
        String currDate = DateTimeUtils.getCurrDate();
        model.put("currDate", currDate);
        String currWeekDate = DateTimeUtils.getCurrWeekDate();
        model.put("currWeekDate", currWeekDate);
        int currWeekIndex = DateTimeUtils.getCurrWeekIndex();
        model.put("currWeekIndex", currWeekIndex);
        return "dzbp/classKeBiaoTwo";
    }
}
