package com.fulaan.new33.controller;

import com.fulaan.annotation.ObjectIdType;
import com.fulaan.base.controller.BaseController;
import com.fulaan.new33.service.N33_SelectProgressService;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@RequestMapping("/n33_slectprogress")
@Controller
public class N33_SelectProgressContoller extends BaseController {
    private N33_SelectProgressService n33_selectProgressService = new N33_SelectProgressService();

    @RequestMapping("/getGradeProgress")
    @ResponseBody
    public Map<String,Object> getGradeProgress(@ObjectIdType ObjectId ciId,@ObjectIdType ObjectId gradeId){
        return n33_selectProgressService.getGradeProgress(getSchoolId(),ciId,gradeId);
    }
    @RequestMapping("/getStudentsByClassId")
    @ResponseBody
    public List<Map<String,Object>> getStudentsByClassId(@ObjectIdType ObjectId ciId, String classId, @ObjectIdType ObjectId gradeId){
        return n33_selectProgressService.getStudentsByClassId(ciId,classId,gradeId);
    }

    @RequestMapping("/getStudentsByName")
    @ResponseBody
    public List<Map<String,Object>> getStudentsByName(@ObjectIdType ObjectId ciId,@ObjectIdType ObjectId gradeId,String name){
        return n33_selectProgressService.getStudentsByName(ciId,name,gradeId);
    }

    @RequestMapping("/export")
    public void export(HttpServletResponse response,@ObjectIdType ObjectId ciId,@ObjectIdType ObjectId gradeId) throws Exception {
        try {
            n33_selectProgressService.export(response,ciId,gradeId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
