package com.fulaan.new33.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

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
import com.fulaan.new33.dto.isolate.N33_KSDTO;
import com.fulaan.new33.dto.isolate.N33_TeaDTO;
import com.fulaan.new33.dto.paike.N33_JXBDTO;
import com.fulaan.new33.service.N33_FenBanInfoSetService;
import com.fulaan.new33.service.N33_StudentTagService;
import com.fulaan.new33.service.isolate.IsolateUserService;
import com.fulaan.user.service.UserService;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;

@Controller
@RequestMapping("/n33_fenbaninfoset")
public class N33_FenBanInfoSetController extends BaseController {
    @Autowired
    private N33_FenBanInfoSetService n33_fenBanInfoSetService;

    @Autowired
    private N33_StudentTagService tagService;

    @Autowired
    private IsolateUserService userService;

    @ResponseBody
    @RequestMapping("/getSubjectByGradeId")
    public List<N33_KSDTO> getSubjectByGradeId(@ObjectIdType ObjectId gid){
        return n33_fenBanInfoSetService.getSubjectByGradeId(getSchoolId(),gid);
    }
    @UserDataCollection(pageTag = "n33", params = {"gradeId","subType"}, isAllCollection = false)
    @ResponseBody
    @RequestMapping("/getSubjectByType")
    public List<N33_KSDTO> getSubjectByType(@ObjectIdType ObjectId gradeId,@RequestParam String subType){
       return n33_fenBanInfoSetService.getSubjectByType(gradeId,getSchoolId(),subType);
    }
    @ResponseBody
    @RequestMapping("/getJxbInfo")
    public Map<String,Object> getJxbInfo(@ObjectIdType ObjectId gradeId,@RequestParam int subType,@RequestBody List<String> subjectIds){
        return n33_fenBanInfoSetService.getJxbInfo(gradeId, MongoUtils.convertToObjectIdList(subjectIds),getSchoolId(), subType);
    }

    @UserDataCollection(pageTag = "n33", params = {"gradeId","subType","subjectId"}, isAllCollection = false)
    @ResponseBody
    @RequestMapping("/getJxbInfoExcptZX")
    public Map<String,Object> getJxbInfoExcptZX(@ObjectIdType ObjectId gradeId,@RequestParam String subType,@RequestParam String subjectId){
        return n33_fenBanInfoSetService.getJxbInfoExcptZX(gradeId, MongoUtils.convertToObjectIdList(Arrays.asList(subjectId)),getSchoolId(),subType);
    }


    @ResponseBody
    @RequestMapping("/getTeacherList")
    public List<Map<String,Object>> getTeacherList(@ObjectIdType ObjectId gradeId,@ObjectIdType ObjectId subjectId){
        return n33_fenBanInfoSetService.getTeacherList(getSchoolId(),gradeId,subjectId);
    }

    @ResponseBody
    @RequestMapping("/getTeacherList2")
    public List<Map<String,Object>> getTeacherList2(int week,@ObjectIdType ObjectId gradeId,@ObjectIdType ObjectId subjectId,String index){
        return n33_fenBanInfoSetService.getTeacherList2(getDefauleTermId(),getSchoolId(),gradeId,subjectId,index,week);
    }

    @ResponseBody
    @RequestMapping("/getClassRoomList")
    public List<Map<String,Object>> getClassRoomList(@ObjectIdType ObjectId gradeId){
        return n33_fenBanInfoSetService.getClassRoomList(getSchoolId(),gradeId);
    }

    @ResponseBody
    @RequestMapping("/setJxbTeacher")
    public RespObj setJxbTeacher(@ObjectIdType ObjectId jxbId,@ObjectIdType ObjectId teacherId){
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE,"成功");
        try {
            n33_fenBanInfoSetService.setJxbTeacher(jxbId,getSchoolId(),teacherId);
        }catch (Exception e){
            e.printStackTrace();
            respObj = new RespObj(Constant.FAILD_CODE,"失败");
        }
        return respObj;
    }

    @ResponseBody
    @RequestMapping("/setJxbRoom")
    public RespObj setJxbRoom(@ObjectIdType ObjectId jxbId,@ObjectIdType ObjectId roomId){
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE,"成功");
        try {
            n33_fenBanInfoSetService.setJxbRoom(jxbId,getSchoolId(),roomId);
        }catch (Exception e){
            e.printStackTrace();
            respObj = new RespObj(Constant.FAILD_CODE,"失败");
        }
        return respObj;
    }

    @ResponseBody
    @RequestMapping("/setNickName")
    public RespObj setNickName(@ObjectIdType ObjectId jxbId,String nickname){
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE,"成功");
        try {
            n33_fenBanInfoSetService.setNickName(jxbId,nickname);
        }catch (Exception e){
            e.printStackTrace();
            respObj = new RespObj(Constant.FAILD_CODE,"失败");
        }
        return respObj;
    }
    @ResponseBody
    @RequestMapping("/getConflictCollection")
    public Map<String,Object> getConflictCollection(@ObjectIdType ObjectId gradeId,@ObjectIdType ObjectId subid1,@ObjectIdType ObjectId subid2,Integer type){
        return n33_fenBanInfoSetService.getConflictCollection(getSchoolId(),gradeId,subid1,subid2,type);
    }
    @ResponseBody
    @RequestMapping("/getStudents")
    public List<Map<String,Object>> getStudents(@RequestBody  List<String> ids,@ObjectIdType ObjectId gradeId){
        return n33_fenBanInfoSetService.getStudents(ids,getSchoolId(),gradeId);
    }
    @ResponseBody
    @RequestMapping("/exportJXBinfo")
    public void exportJXBinfo(HttpServletResponse response,String gradeId){
         n33_fenBanInfoSetService.exportJXBinfoTwo(getSchoolId(),gradeId,response);
    }

    /**
     * 生成冲突
     * @param gradeId
     * @param cid
     */
    @ResponseBody
    @RequestMapping("/detactConfliction")
    public RespObj detactConfliction(@ObjectIdType ObjectId gradeId,@ObjectIdType ObjectId cid){
        RespObj resp = new RespObj(Constant.SUCCESS_CODE,"成功");
        try{
            tagService.conflictDetection(getSchoolId(), gradeId, cid);
            n33_fenBanInfoSetService.detactConfliction(getSchoolId());
        }
        catch (Exception e){
            e.printStackTrace();
            resp = new RespObj(Constant.FAILD_CODE,"失败");
        }
        return resp;
    }

    @ResponseBody
    @RequestMapping("/getjxbStudentByJxbId")
    public RespObj getjxbStudentByJxbId(@RequestParam String id, @RequestParam String gid){
        RespObj resp = new RespObj(Constant.SUCCESS_CODE,"成功");
        try {
         resp.setMessage(n33_fenBanInfoSetService.getjxbStudentByJxbId( new ObjectId(id),getSchoolId(),new ObjectId(gid)));
        }
        catch (Exception e){
            e.printStackTrace();
            resp = new RespObj(Constant.FAILD_CODE,"失败");
        }
        return resp;
    }

//    @ResponseBody
//    @RequestMapping("/autoSetTeacherAndClassRoom")
//    public RespObj autoSetTeacherAndClassRoom(@RequestBody Map<String,Object> data,@ObjectIdType ObjectId ciId,@ObjectIdType ObjectId subId,@ObjectIdType ObjectId gid){
//        RespObj resp = new RespObj(Constant.SUCCESS_CODE);
//        try {
//            List<Integer> indexes = (List<Integer>) data.get("indexes");
//            List<String> jxbIds = (List<String>) data.get("jxbIds");
//            Map<String,Object> map =n33_fenBanInfoSetService.autoSetTeacherAndClassRoom(indexes,jxbIds,ciId,getSchoolId(),subId,gid);
//            resp.setMessage(map);
//        }
//        catch (Exception e){
//            e.printStackTrace();
//            resp = new RespObj(Constant.FAILD_CODE);
//        }
//        return resp;
//    }
    @ResponseBody
    @RequestMapping("/autoSetTeacherAndClassRoom")
    public RespObj autoSetTeacherAndClassRoom(@RequestBody Map<String,Object> data,@ObjectIdType ObjectId ciId,@ObjectIdType ObjectId gid){
        RespObj resp = new RespObj(Constant.SUCCESS_CODE);
        try {
            List<Integer> indexes = (List<Integer>) data.get("indexes");
            List<String> subjectIds = (List<String>) data.get("subjectIds");
            n33_fenBanInfoSetService.autoSetTeachersAndRoomAll(ciId,getSchoolId(),subjectIds,gid,indexes);
            resp.setMessage("成功");
        }
        catch (Exception e){
            e.printStackTrace();
            resp = new RespObj(Constant.FAILD_CODE);
        }
        return resp;
    }

    @ResponseBody
    @RequestMapping("/clearSet")
    public RespObj clearSet(@RequestBody Map<String,Object> data){
        RespObj resp = new RespObj(Constant.SUCCESS_CODE,"成功");
        try {
            List<String> jxbIds = (List<String>) data.get("jxbIds");
            n33_fenBanInfoSetService.clearSet(jxbIds);
        }
        catch (Exception e){
            e.printStackTrace();
            resp = new RespObj(Constant.FAILD_CODE,"失败");
        }
        return resp;
    }
    
    //信息设置 - 自动设置- 教师分班
    @ResponseBody
    @RequestMapping("/teaAutoSet")
    public RespObj teaAutoSet(@RequestBody Map repMap){
    	ObjectId xqid = new ObjectId(repMap.get("xqid").toString());
    	String gid = repMap.get("gid").toString();
    	String subid = repMap.get("subid").toString();
    	String name = repMap.get("name").toString();
    	String subType = repMap.get("subType").toString();
        RespObj resp = new RespObj(Constant.SUCCESS_CODE,"成功");
        try {
            //获取此学科的所有教师
        	List<N33_TeaDTO> teacherList = new ArrayList<N33_TeaDTO>();
        	teacherList = userService.getTeaListByXqid(xqid, getSchoolId(), gid, subid, name);
        	//获取此学科的所有行政班
        	List<N33_JXBDTO> jxbdtos = new ArrayList<N33_JXBDTO>();
        	Map<String, Object> jxbMap = new HashMap<String, Object>();
        	jxbMap = n33_fenBanInfoSetService.getJxbInfoExcptZX(new ObjectId(gid), MongoUtils.convertToObjectIdList(Arrays.asList(subid)),getSchoolId(),subType);
        	jxbdtos = (List<N33_JXBDTO>) jxbMap.get("list");
        	//行政班平均分配教师
        	List eTeaList = new ArrayList();//已经排课的教师
        	int i = 0;
        	for(N33_JXBDTO jxbDto : jxbdtos){
        		ObjectId teacherId = null;
        		for(N33_TeaDTO teaDto : teacherList){
        			if(!eTeaList.contains(teaDto.getUid())){
        				eTeaList.add(teaDto.getUid());
        				teacherId =  new ObjectId(teaDto.getUid());
        				n33_fenBanInfoSetService.setJxbTeacher(new ObjectId(jxbDto.getId()),getSchoolId(), teacherId);
        			}
        		}
        		if(teacherId == null){//班级数量大于教师数量
        			if(i >= teacherList.size()){
        				i = 0;
        				teacherId =  new ObjectId(teacherList.get(i).getUid());
            			n33_fenBanInfoSetService.setJxbTeacher(new ObjectId(jxbDto.getId()),getSchoolId(), teacherId);
            			i++;
        			}else{
        				teacherId =  new ObjectId(teacherList.get(i).getUid());
            			n33_fenBanInfoSetService.setJxbTeacher(new ObjectId(jxbDto.getId()),getSchoolId(), teacherId);
            			i++;
        			}
        		}
        	}
        }
        catch (Exception e){
            e.printStackTrace();
            resp = new RespObj(Constant.FAILD_CODE,"失败");
        }
        return resp;
    }


    /**
     * 获取教学班
     * @param gradeId
     * @param ciId
     */
    @ResponseBody
    @RequestMapping("/getZuoBanJxbInfo")
    public RespObj getZuoBanJxbInfo(
            @ObjectIdType ObjectId gradeId,
            @ObjectIdType ObjectId ciId,
            @RequestParam int jxbType
    ){
        RespObj resp = new RespObj(Constant.SUCCESS_CODE,"成功");
        try{
            Map<String, Object> map = n33_fenBanInfoSetService.getZuoBanJxbInfo(getSchoolId(), gradeId, ciId, jxbType);
            resp.setMessage(map);
        }catch (Exception e){
            e.printStackTrace();
            resp = new RespObj(Constant.FAILD_CODE,"失败");
        }
        return resp;
    }


    @ResponseBody
    @RequestMapping("/getZbJxbSerialTeaList")
    public RespObj getZbJxbSerialTeaList(
            @ObjectIdType ObjectId gradeId,
            @ObjectIdType ObjectId subId,
            @ObjectIdType ObjectId ciId,
            @ObjectIdType ObjectId currJxbId,
            @RequestParam int serial,
            @RequestParam int jxbType
    ) {
        RespObj resp = new RespObj(Constant.SUCCESS_CODE,"成功");
        try{
            List<Map<String, Object>> list = n33_fenBanInfoSetService.getZbJxbSerialTeaList(getSchoolId(), gradeId, subId, ciId, currJxbId, serial, jxbType);
            resp.setMessage(list);
        }catch (Exception e){
            e.printStackTrace();
            resp = new RespObj(Constant.FAILD_CODE,"失败");
        }
        return resp;
    }


    @ResponseBody
    @RequestMapping("/getZbJxbSerialClsRmList")
    public RespObj getZbJxbSerialClsRmList(
            @ObjectIdType ObjectId gradeId,
            @ObjectIdType ObjectId ciId,
            @ObjectIdType ObjectId currJxbId,
            @RequestParam int serial,
            @RequestParam int jxbType
    ){
        RespObj resp = new RespObj(Constant.SUCCESS_CODE,"成功");
        try{
            List<Map<String,Object>> list = n33_fenBanInfoSetService.getZbJxbSerialClsRmList(getSchoolId(), gradeId, ciId, currJxbId, serial, jxbType);
            resp.setMessage(list);
        }catch (Exception e){
            e.printStackTrace();
            resp = new RespObj(Constant.FAILD_CODE,"失败");
        }
        return resp;
    }

    /**
     * 获取教学班
     * @param gradeId
     * @param ciId
     */
    @ResponseBody
    @RequestMapping("/getNoSerialZbJxbList")
    public RespObj getNoSerialZbJxbList(
            @ObjectIdType ObjectId gradeId,
            @ObjectIdType ObjectId ciId,
            @RequestParam int jxbType
    ){
        RespObj resp = new RespObj(Constant.SUCCESS_CODE,"成功");
        try{
            List<N33_JXBDTO> list = n33_fenBanInfoSetService.getNoSerialZbJxbList(getSchoolId(), gradeId, ciId, jxbType);
            resp.setMessage(list);
        }catch (Exception e){
            e.printStackTrace();
            resp = new RespObj(Constant.FAILD_CODE,"失败");
        }
        return resp;
    }

    @ResponseBody
    @RequestMapping("/setZbJxbSerial")
    public RespObj setZbJxbSerial(
            @ObjectIdType ObjectId jxbId,
            @RequestParam int serial
    ){
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE,"成功");
        try {
            n33_fenBanInfoSetService.setZbJxbSerial(jxbId, serial);
        }catch (Exception e){
            e.printStackTrace();
            respObj = new RespObj(Constant.FAILD_CODE,"失败");
        }
        return respObj;
    }

}
