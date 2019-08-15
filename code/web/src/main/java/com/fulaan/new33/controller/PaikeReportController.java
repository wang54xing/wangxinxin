package com.fulaan.new33.controller;

import com.fulaan.annotation.ObjectIdType;
import com.fulaan.base.controller.BaseController;
import com.fulaan.new33.service.PaikeReportService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping("/n33PaikeReport")
public class PaikeReportController extends BaseController {

    @Autowired
    private PaikeReportService paikeReportService;

    @RequestMapping("/getReport")
    @ResponseBody
    public Map<String,Object> getReport(@ObjectIdType ObjectId ciId,@ObjectIdType ObjectId gid){
        return paikeReportService.getReport(ciId,getSchoolId(),gid);
    }
}
