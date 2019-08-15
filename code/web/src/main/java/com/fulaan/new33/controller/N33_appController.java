package com.fulaan.new33.controller;

import com.db.new33.N33_GDSXDao;
import com.db.new33.isolate.N33_StudentDao;
import com.db.user.UserDao;
import com.fulaan.annotation.ObjectIdType;
import com.fulaan.annotation.SessionNeedless;
import com.fulaan.annotation.UserDataCollection;
import com.fulaan.new33.dto.isolate.*;
import com.fulaan.new33.dto.paike.N33_JXBDTO;
import com.fulaan.new33.service.*;
import com.fulaan.new33.service.isolate.*;
import com.pojo.new33.isolate.StudentEntry;
import com.pojo.new33.paike.N33_GDSXEntry;
import com.pojo.user.UserEntry;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by albin on 2018/8/16.
 */
@RestController
@RequestMapping(value = "/n33App")
public class N33_appController {
    @Autowired
    private N33_DefaultTermService termService;

    private CourseRangeService courseRangeService = new CourseRangeService();

    private N33_StudentDao studentDao = new N33_StudentDao();

    @Autowired
    private IsolateTermService weekService;

    @Autowired
    private N33_SWService swService;

    @Autowired
    private N33_ZKBService zkbService;

    @Autowired
    private N33_ClassroomService classroomService;

    private N33_JXZService n33_jxzService = new N33_JXZService();

    @Autowired
    private SchoolSelectLessonSetService schoolSelectLessonSetService;

    private UserDao userDao = new UserDao();

    @Autowired
    private N33_JXBService jxbService;

    @Autowired
    private N33_StudentService studentService;

    @Autowired
    private N33_GDSXService gdsxService;

    @Autowired
    private IsolateUserService userService;

    @Autowired
    private IsolateClassService classService;

    @SessionNeedless
    @RequestMapping(value = "/getDefaultTerm/{sid}", method = RequestMethod.GET)
    @ResponseBody
    public RespObj getDefaultTerm(@PathVariable ObjectId sid) {
        RespObj resp = new RespObj(Constant.SUCCESS_CODE);
        try {
            resp.setMessage(termService.getDefaultTerm(sid));
        } catch (Exception e) {
            resp.setCode(Constant.FAILD_CODE);
                resp.setMessage("出错了！");
        }
        return resp;
    }

    @SessionNeedless
    @RequestMapping(value = "/getStudent", method= RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public List<StudentDTO> getStudent(@RequestBody Map map) {
        List<String> stuIds = (List<String>) map.get("stuIds");
        ObjectId schoolId = new ObjectId((String)map.get("sid"));
        List<StudentDTO> studentDTOList = studentService.getStudentDTO(stuIds,schoolId);
        return studentDTOList;
    }

    /**
     * 查询行政班课表
     *
     * @return
     */
    @SessionNeedless
    @RequestMapping(value = "/GetClassSettledPositionsByWeek/{classId}/{xqid}/{gradeId}/{sid}/{week}", method = RequestMethod.GET)
    @ResponseBody
    public RespObj GetClassSettledPositionsByWeek(@PathVariable String classId,@PathVariable String xqid, @PathVariable String gradeId,@PathVariable String sid,@PathVariable Integer week) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(zkbService.GetClassSettledPositionsByWeek(new ObjectId(classId), new ObjectId(xqid), new ObjectId(gradeId), new ObjectId(sid), week));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    /**
     * 查询所有次的排课教室
     */
    @SessionNeedless
    @RequestMapping("/getRoomEntryListByXqGradeAndTerm/{sid}/{xqid}")
    @ResponseBody
    public RespObj getRoomEntryListByXqGradeAndTerm(@PathVariable String sid,@PathVariable String xqid) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(classroomService.getRoomEntryListByXqGradeAndTermAllGrade(new ObjectId(xqid), new ObjectId(sid), 1));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    /**
     * 查询所有次的排课教室
     */
    @SessionNeedless
    @RequestMapping("/getSubjectDAndH/{sid}/{gid}/{time}/{subId}")
    @ResponseBody
    public Map<String,Object> getSubjectDAndH(@PathVariable String sid,@PathVariable String gid,@PathVariable long time,@PathVariable String subId) {

        try {
           return jxbService.getSubjectDAndH(new ObjectId(sid), new ObjectId(gid), time,new ObjectId(subId));
        } catch (Exception e) {
            List<String> hgStuIds = new ArrayList<String>();
            List<String> djStuIds = new ArrayList<String>();
            Map<String,Object> map = new HashMap<String, Object>();
            map.put("hgStuIds",hgStuIds);
            map.put("djStuIds",djStuIds);
            return map;
        }
    }

    /**
     * 加载课表list(包括课表结构，内容)
     *
     * @return
     */
    @SessionNeedless
    @RequestMapping("/getKeBiaoListZhou/{xqid}/{classRoomId}/{week}/{sid}")
    @ResponseBody
    public RespObj getKeBiaoListZhou(@PathVariable String xqid,@PathVariable String classRoomId, @PathVariable Integer week,@PathVariable String sid) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            Map map = zkbService.getKeBiaoListByClassRoomId(new ObjectId(xqid),"",new ObjectId(classRoomId),"", "", new ObjectId(sid), week);
            obj.setMessage(map);
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }


    @SessionNeedless
    @RequestMapping("/getListByXq/{xqid}/{sid}")
    @ResponseBody
    public List<N33_JXZDTO> getListByXq(@PathVariable ObjectId xqid, @PathVariable ObjectId sid) {
        List<N33_JXZDTO> list = new ArrayList<N33_JXZDTO>();
        try {
            list = n33_jxzService.getListByXq(sid, xqid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

	@SessionNeedless
	@RequestMapping("/getListByXqForCX/{xqid}/{sid}")
	@ResponseBody
	public RespObj getListByXqForCX(@PathVariable ObjectId xqid, @PathVariable ObjectId sid) {
		RespObj resp = new RespObj(Constant.SUCCESS_CODE);
		List<N33_JXZDTO> list = new ArrayList<N33_JXZDTO>();
		try {
			list = n33_jxzService.getListByXq(sid, xqid);
			resp.setMessage(list);
		} catch (Exception e) {
			resp.setCode(Constant.FAILD_CODE);
			resp.setMessage("服务器正忙");
		}
		return resp;
	}

    @SessionNeedless
    @RequestMapping(value = "/getKeJieList/{xqid}/{sid}", method = RequestMethod.GET)
    @ResponseBody
    public RespObj getListBySchoolIdZKB(@PathVariable String xqid, @PathVariable String sid) {
        RespObj resp = new RespObj(Constant.SUCCESS_CODE);
        try {
            resp.setMessage(courseRangeService.getListBySchoolIdZKB(sid, new ObjectId(xqid)));
        } catch (Exception e) {
            resp.setCode(Constant.FAILD_CODE);
            resp.setMessage("服务器正忙");
        }
        return resp;
    }

    @SessionNeedless
    @RequestMapping(value = "/getStuDto/{xqid}/{userId}", method = RequestMethod.GET)
    @ResponseBody
    public RespObj getStuDto(@PathVariable String xqid, @PathVariable String userId) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            StudentEntry entry = studentDao.findStudent(new ObjectId(xqid), new ObjectId(userId));
            obj.setMessage(new StudentDTO(entry));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    /**
     * 默认周
     *
     * @return
     */
    @SessionNeedless
    @RequestMapping(value = "/getDefWeek/{sid}", method = RequestMethod.GET)
    @ResponseBody
    public RespObj getDefWeek(@PathVariable String sid) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(weekService.getDateByOr(sid));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    //获得某个学生的已排课表（x, y）list周课表
    @SessionNeedless
    @RequestMapping(value = "/GetStudentSettledPositionsByWeek/{studentId}/{week}/{gid}/{sid}", method = RequestMethod.GET)
    @ResponseBody
    public RespObj GetStudentSettledPositionsByWeek(@PathVariable String studentId, @PathVariable Integer week, @PathVariable String gid, @PathVariable String sid) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(zkbService.GetStudentSettledPositionsByWeek(new ObjectId(studentId), new ObjectId(sid), week, new ObjectId(gid)));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    /**
     * 老师某个时间段是否有课
     * @param teaId
     * @param sid
     * @param time
     * @return
     */
    @SessionNeedless
    @RequestMapping(value = "/checkTeachersByTime/{teaId}/{sid}/{date}/{time}", method = RequestMethod.GET)
    @ResponseBody
    public RespObj checkTeachersByTime(@PathVariable ObjectId teaId, @PathVariable ObjectId sid,@PathVariable String date, @PathVariable String time) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(zkbService.checkTeachersByTime(teaId, sid,date, time));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    @SessionNeedless
    @RequestMapping(value = "/GetTeachersSettledPositionsByWeek/{teaId}/{sid}/{week}", method = RequestMethod.GET)
    @ResponseBody
    public RespObj GetTeachersSettledPositions(@PathVariable ObjectId teaId, @PathVariable ObjectId sid, @PathVariable Integer week) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(zkbService.GetTeachersSettledPositionsByWeek(teaId, sid, week));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    @SessionNeedless
    @RequestMapping(value = "/GetTeachersSettledPositionsByWeek2", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public RespObj GetTeachersSettledPositions2(@RequestBody Map map) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(zkbService.GetTeachersSettledPositionsByWeek2(map));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    /**
     * 查询某个学期的所有老師事务
     */
    @SessionNeedless
    @RequestMapping(value = "/getTeaShiWuByXqid/{xqid}/{teaId}", method = RequestMethod.GET)
    @ResponseBody
    public RespObj getTeaShiWuByXqid(@PathVariable ObjectId xqid, @PathVariable ObjectId teaId) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(swService.getSwByXqidAndUserId(xqid, teaId));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    /**
     * 查询某个学期的所有固定事务
     */
    @SessionNeedless
    @RequestMapping(value = "/getGuDingShiWuByXqid/{xqid}", method = RequestMethod.GET)
    @ResponseBody
    public RespObj getGuDingShiWuByXqid(@PathVariable String xqid) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(swService.getGuDingShiWuByXqid(new ObjectId(xqid)));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    @SessionNeedless
    @RequestMapping(value = "/getCurrentSubjectGroup/{userId}/{sid}", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getCurrentSubjectGroup(@PathVariable String userId,@PathVariable String sid) {
        try {
            return schoolSelectLessonSetService.getCurrentSubjectGroup(new ObjectId(userId), new ObjectId(sid));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

	@SessionNeedless
	@RequestMapping(value = "/getCurrentSubjectGroupForCX/{userId}/{sid}", method = RequestMethod.GET)
	@ResponseBody
	public RespObj getCurrentSubjectGroupForCX(@PathVariable String userId,@PathVariable String sid) {
		RespObj resp = new RespObj(Constant.SUCCESS_CODE);
		try {
			resp.setMessage(schoolSelectLessonSetService.getCurrentSubjectGroup(new ObjectId(userId), new ObjectId(sid)));
		} catch (Exception e) {
			e.printStackTrace();
			resp.setCode(Constant.FAILD_CODE);
			resp.setMessage("服务器正忙");
		}
		return resp;
	}

    @SessionNeedless
    @RequestMapping(value = "/studentSelectSubjectGroup/{sid}/{userId}/{xqid}/{sub1}/{sub2}/{sub3}/{name}", method = RequestMethod.GET)
    @ResponseBody
    public RespObj studentSelectSubjectGroup(@PathVariable String sid,@PathVariable String userId,@PathVariable String xqid, @PathVariable String sub1, @PathVariable String sub2, @PathVariable String sub3, @PathVariable String name) {
        RespObj resp = new RespObj(Constant.FAILD_CODE);
        try {
            String result = schoolSelectLessonSetService.studentSelectSubjectGroup(new ObjectId(xqid), new ObjectId(userId),new ObjectId(sid), new ObjectId(sub1), new ObjectId(sub2), new ObjectId(sub3), name);
            if("选科成功".equals(result)){
                resp.setCode(Constant.SUCCESS_CODE);
            }
            resp.setMessage(result);
        } catch (Exception e) {
            e.printStackTrace();
            resp.setCode(Constant.FAILD_CODE);
            resp.setMessage("服务器正忙");
        }
        return resp;
    }


    @SessionNeedless
    @RequestMapping(value = "/getUserByChaoXin/{uidStr}", method = RequestMethod.GET)
    @ResponseBody
    public RespObj getUserByChaoXin(@PathVariable String uidStr) {
        RespObj resp = new RespObj(Constant.SUCCESS_CODE);
        try {
            UserEntry entry = userDao.searchUserByUserBind(9,uidStr);
            Map<String,String> retMap = new HashMap<String, String>();
            retMap.put("sid",entry.getSchoolID().toString());
            retMap.put("userId",entry.getID().toString());
            resp.setMessage(retMap);
        } catch (Exception e) {
            e.printStackTrace();
            resp.setCode(Constant.FAILD_CODE);
            resp.setMessage("服务器正忙");
        }
        return resp;
    }


    /**
     * 加载课表list(包括课表结构，内容)
     *
     * @return
     */
    @SessionNeedless
    @RequestMapping("/getKeBiaoListZhouForFuLaan/{classId}/{sid}")
    @ResponseBody
    public RespObj getKeBiaoListZhouForFuLaan(@PathVariable String classId,@PathVariable String sid) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            Map map = zkbService.getKeBiaoListByClassRoomIdForFulaan("",new ObjectId(classId),"", "", new ObjectId(sid));
            obj.setMessage(map);
            if(map.get("message") != null){
	            obj.setCode(Constant.FAILD_CODE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    /**
     * 加载课表list(包括课表结构，内容)
     *
     * @return
     */
    @SessionNeedless
    @RequestMapping("/saveStudentDaKa/{zkbId}/{studentId}/{daKaFlag}")
    @ResponseBody
    public RespObj saveStudentDaKa(@PathVariable String zkbId,@PathVariable String studentId,@PathVariable int daKaFlag){
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            zkbService.saveStudentDaKa(new ObjectId(zkbId),new ObjectId(studentId),daKaFlag);
            obj.setMessage("打卡成功");
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    /**
     * 更新超星用户Id
     *
     * @return
     */
    @SessionNeedless
    @RequestMapping(value = "/getStudentCode/{zkbId}/{sid}",method = RequestMethod.GET)
    @ResponseBody
    public RespObj getStudentCode(@PathVariable String zkbId,@PathVariable String sid){
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            List<Map<String,Object>> retList = zkbService.getJXBstudent(new ObjectId(zkbId),new ObjectId(sid));
            obj.setMessage(retList);
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    /**
     * 超星查询固定事项
     *
     * @return
     */
    @SessionNeedless
    @RequestMapping(value = "/getGDSX/{xqid}/{sid}/{gradeId}",method = RequestMethod.GET)
    @ResponseBody
    public RespObj getGDSX(@PathVariable String xqid,@PathVariable String sid,@PathVariable String gradeId){
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            List<N33_GDSXDTO > gdsxdtos = gdsxService.getGDSXBySidAndXqid(new ObjectId(xqid), new ObjectId(sid), new ObjectId(gradeId));
            obj.setMessage(gdsxdtos);
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    /**
     * 超星查询老师事务
     *
     * @return
     */
    @SessionNeedless
    @RequestMapping(value = "/getTeaSW/{xqid}/{userId}",method = RequestMethod.GET)
    @ResponseBody
    public RespObj getTeaSW(@PathVariable String xqid,@PathVariable String userId){
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            List<Map<String,Object>> gdsxdtos = swService.getSwByXqidAndUserIdReverseXY(new ObjectId(xqid), new ObjectId(userId));
            obj.setMessage(gdsxdtos);
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    /**
     * 超星查询固定事务
     *
     * @return
     */
    @SessionNeedless
    @RequestMapping(value = "/getGDSW/{xqid}/{sid}",method = RequestMethod.GET)
    @ResponseBody
    public RespObj getGDSW(@PathVariable String xqid,@PathVariable String sid){
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            List<Map<String,Object>> swList = swService.getGuDingShiWuBySchoolIdReverseXY(new ObjectId(xqid), new ObjectId(sid));
            obj.setMessage(swList);
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    /**
     * 超星查询固定事务
     *
     * @return
     */
    @SessionNeedless
    @RequestMapping(value = "/getTeaGradeList/{userId}/{xqid}",method = RequestMethod.GET)
    @ResponseBody
    public RespObj getTeaGradeList(@PathVariable String userId,@PathVariable String xqid){
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            List<String> gradeList = userService.getTeaGradeList(new ObjectId(userId),new ObjectId(xqid));
            obj.setMessage(gradeList);
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }


    /**
     * 更新超星用户Id
     *
     * @return
     */
    @SessionNeedless
    @RequestMapping(value = "/updateUser/{type}/{content}",method = RequestMethod.GET)
    @ResponseBody
    public RespObj updateUser(@PathVariable Integer type,@PathVariable String content){
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            if(type == 1){
                String [] strArray = content.split("\\|\\|");
                String oldUserId = strArray[0];
                String newUserId = strArray[1];
                UserEntry entry = userDao.searchUserByUserBind(9,oldUserId);
                entry.getUserBind().setBindValue(newUserId);
                userDao.update(entry.getID(),entry);
                obj.setMessage("更新成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    /**
     * 查询年级的教学班
     *
     * @return
     */
    @SessionNeedless
    @RequestMapping(value = "/getGradeJXB/{tid}/{index}/{gradeId}/{week}/{schoolId}",method = RequestMethod.GET)
    @ResponseBody
    public RespObj getGradeJXB(@PathVariable String tid,@PathVariable Integer index,@PathVariable String gradeId,@PathVariable Integer week,@PathVariable String schoolId){
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            List<N33_JXBDTO> jxbdtoList = jxbService.getGradeJXB(new ObjectId(tid),index,new ObjectId(gradeId),week,new ObjectId(schoolId));
            obj.setMessage(jxbdtoList);
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    /**
     * 查询班级信息
     *
     * @return
     */
    @SessionNeedless
    @RequestMapping(value = "/getClassInfoByClientId")
    public RespObj getClassInfoByClientId(@RequestParam String clientId){
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            ClassInfoDTO classDto = classService.getClassInfoByClientId(clientId);
            obj.setMessage(classDto);
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    /**
     * 查询班级课表信息
     *
     * @return
     */
    @SessionNeedless
    @RequestMapping(value = "/getZhouKeBiaoListByClientId")
    public RespObj getZhouKeBiaoListByClientId(@RequestParam String clientId){
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            Map<String,Object> map = classService.getZhouKeBiaoListByClientId(clientId);
            obj.setMessage(map);
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }


    /**
     * 查询班级信息
     *
     * @return
     */
    @SessionNeedless
    @RequestMapping(value = "/getClassInfo")
    public RespObj getClassInfo(
            @ObjectIdType ObjectId schoolId,
            @ObjectIdType ObjectId classId
    ){
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            ClassInfoDTO classDto = classService.getClassInfo(schoolId, classId);
            obj.setMessage(classDto);
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    /**
     * 查询班级课表信息
     *
     * @return
     */
    @SessionNeedless
    @RequestMapping(value = "/getZhouKeBiaoList")
    public RespObj getZhouKeBiaoList(
            @ObjectIdType ObjectId schoolId,
            @ObjectIdType ObjectId classId
    ){
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            Map<String,Object> map = classService.getZhouKeBiaoList(schoolId, classId);
            obj.setMessage(map);
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    public static void main(String[] args){
        N33_GDSXDao gdsxDao = new N33_GDSXDao();
        N33_GDSXEntry gdsxEntry = new N33_GDSXEntry();
        gdsxEntry.setID(new ObjectId());
        gdsxEntry.setDesc("固定事项");
        gdsxEntry.setGradeId(new ObjectId("59a38d00734b391548548ae7"));
        gdsxEntry.setTermId(new ObjectId("5b45a79c8fb25a6d70d341ff"));
        gdsxEntry.setSchoolId(new ObjectId("586dbe1fc706c822b7ded0e5"));
        gdsxEntry.setX(1);
        gdsxEntry.setY(2);
        gdsxDao.addGDSXEntry(gdsxEntry);
    }
}
