package com.fulaan.new33.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.Session;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.db.new33.isolate.N33_TeaDao;
import com.db.new33.isolate.SubjectDao;
import com.db.new33.paike.N33_ExClassRecordDao;
import com.db.new33.paike.N33_JXBDao;
import com.db.new33.paike.N33_YKBDao;
import com.db.new33.paike.N33_ZIXIKEDao;
import com.db.new33.paike.PaiKeXyDto;
import com.fulaan.alipay.util.httpClient.HttpRequest;
import com.fulaan.annotation.SessionNeedless;
import com.fulaan.annotation.UserDataCollection;
import com.fulaan.base.controller.BaseController;
import com.fulaan.new33.dto.N33_TiaoKeShengQingDTO;
import com.fulaan.new33.dto.isolate.N33_SWDTO;
import com.fulaan.new33.dto.isolate.N33_TeaDTO;
import com.fulaan.new33.dto.paike.N33_AutoPaiKeDTO;
import com.fulaan.new33.dto.paike.N33_JXBDTO;
import com.fulaan.new33.service.N33_AutoPaiKeByGroupService;
import com.fulaan.new33.service.N33_AutoPkService;
import com.fulaan.new33.service.N33_AutoPkZuHeService;
import com.fulaan.new33.service.N33_ExClassRecordService;
import com.fulaan.new33.service.N33_JXBService;
import com.fulaan.new33.service.N33_NameKBService;
import com.fulaan.new33.service.N33_ZKBService;
import com.fulaan.new33.service.PaiKeService;
import com.fulaan.new33.service.isolate.IsolateUserService;
import com.fulaan.new33.service.isolate.N33_ClassroomService;
import com.fulaan.user.service.UserService;
import com.pojo.new33.isolate.N33_KSEntry;
import com.pojo.new33.isolate.N33_TeaEntry;
import com.pojo.new33.paike.N33_ExClassRecordEntry;
import com.pojo.new33.paike.N33_JXBEntry;
import com.pojo.new33.paike.N33_YKBEntry;
import com.pojo.user.UserEntry;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;

/**
 * Created by wang_xinxin on 2018/3/12.
 */
@Controller
@RequestMapping("/paike")
public class PaiKeController extends BaseController {

    @Autowired
    private N33_ZKBService zkbService;
    @Autowired
    private N33_NameKBService n33_nameKBService;

    @Autowired
    private N33_ClassroomService classroomService;

    @Autowired
    private IsolateUserService teaService;

    @Autowired
    private PaiKeService paiKeService;

    @Autowired
    private N33_AutoPkService autoPkService;


    @Autowired
    private N33_AutoPkZuHeService autoPkZuHeService;

    private N33_ZIXIKEDao n33_zixikeDao = new N33_ZIXIKEDao();

    @Autowired
    private N33_AutoPaiKeByGroupService autoPaiKeByGroupService;

    @Autowired
    private N33_JXBService jxbService;
    
    @Autowired
    private UserService userService;
    
    private SubjectDao subjectDao = new SubjectDao();

    private N33_YKBDao ykbDao = new N33_YKBDao();
    private N33_ExClassRecordDao recordDao = new N33_ExClassRecordDao();

    private N33_JXBDao jxbDao = new N33_JXBDao();

    private N33_TeaDao teaDao = new N33_TeaDao();

    @Autowired
    private N33_ExClassRecordService exClassRecordService;


    /**
     * 下载导入教学班的模板
     *
     * @param response
     * @throws Exception
     */
    @RequestMapping("/exportTemplate")
    public void exportTemplate(HttpServletResponse response) throws Exception {
        try {
            paiKeService.exportTemplate(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 导入教学班
     *
     * @param file
     * @return
     */
    @SessionNeedless
    @RequestMapping("/importJXBExcel")
    @ResponseBody
    public RespObj importJXBExcel(@RequestParam("file") MultipartFile file, @RequestParam String xqid) {
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        String fileName = file.getOriginalFilename();
        if (!fileName.endsWith(".xls") || !fileName.endsWith(".xls")) {
            respObj.setMessage("文件格式错误，请选择Excel文件");
        } else {
            try {
                List<Map<String, Object>> returnList = paiKeService.importJXBExcel(xqid, getSchoolId().toString(), file.getInputStream());
                respObj.setCode(Constant.SUCCESS_CODE);
                respObj.setMessage(returnList);
            } catch (Exception e) {
                e.printStackTrace();
                respObj.setMessage(e.getMessage());
            }
        }
        return respObj;
    }

    /**
     * 加载课表list(包括课表结构，内容)
     *
     * @param gradeId      //年级集合
     * @param classRoomIds //教室集合
     * @param weeks        //周集合 周一-周五
     * @param indexs       // 课节集合
     * @return
     */
    @RequestMapping("/getKeBiaoList")
    @ResponseBody
    public RespObj getKeBiaoList(@RequestParam String gradeId, @RequestParam String classRoomIds, @RequestParam String weeks, @RequestParam String indexs, String termId) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            Map map = paiKeService.getKeBiaoList(new ObjectId(termId), gradeId, classRoomIds, weeks, indexs, getSchoolId());
            obj.setMessage(map);
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    /**
     * 查询无课教师
     *
     * @param map
     * @return
     */
    @RequestMapping("/getFreeTeacher")
    @ResponseBody
    public RespObj getFreeTeacher(@RequestBody Map<String, Object> map) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            ObjectId xqid = new ObjectId((String) map.get("xqid"));
            List<Map<String, String>> xyList = (List<Map<String, String>>) map.get("xyList");
            if (xyList.size() == 0) {
                List<N33_TeaDTO> tea = new ArrayList<N33_TeaDTO>();
                obj.setMessage(tea);
                return obj;
            }
            String subId = (String) map.get("subId");
            String gradeId = (String) map.get("gradeId");
            Integer week = new Double((String) map.get("week")).intValue();
            obj.setMessage(zkbService.getFreeTeacher(xqid, xyList, getSchoolId(), week, subId, gradeId));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    /**
     * 查询无课教师
     *
     * @param map
     * @return
     */
    @RequestMapping("/getTeaIdsKPXy")
    @ResponseBody
    public RespObj getTeaIdsKPXy(@RequestBody Map<String, Object> map) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            ObjectId xqid = new ObjectId((String) map.get("xqid"));
            List<String> teaList = (List<String>) map.get("teaIds");
            if (teaList.size() == 0) {
                List<PaiKeXyDto> dto = new ArrayList<PaiKeXyDto>();
                obj.setMessage(dto);
                return obj;
            }
            Integer week = new Double((String) map.get("week")).intValue();
            obj.setMessage(zkbService.getTeaIdsKPXy(teaList, xqid, getSchoolId(), week));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }


    /**
     * 检查课表存在，不存在创建
     *
     * @param gradeId
     * @param termId
     * @return
     */
    @UserDataCollection(pageTag = "n33", params = {"gradeId"}, isAllCollection = false)
    @RequestMapping("/checkYKB")
    @ResponseBody
    public RespObj checkYKB(@RequestParam String gradeId, String termId) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            paiKeService.checkYKB(new ObjectId(termId), gradeId, getSchoolId());
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
     * @param gradeId     //年级集合
     * @param classRoomId //教室集合
     * @return
     */
//    @UserDataCollection(pageTag = "n33", params = {"gradeId"}, isAllCollection = false)
    @RequestMapping("/getKeBiaoListByCrmid")
    @ResponseBody
    public RespObj getKeBiaoListByCrmid(@RequestParam String gradeId, @RequestParam String classRoomId, String termId) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            Map map = paiKeService.getKeBiaoList(new ObjectId(termId), gradeId, classRoomId, getSchoolId());
            obj.setMessage(map);
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

	@RequestMapping("/getZhuanXiangYKBIds")
	@ResponseBody
	public RespObj getZhuanXiangYKBIds(@RequestBody Map dto) {
		RespObj obj = new RespObj(Constant.SUCCESS_CODE);
		try {
		    List<String> ykbIds = (List<String>) dto.get("zhuanXiangIds");
		    ObjectId ciId = new ObjectId((String)dto.get("ciId"));
			obj.setMessage(paiKeService.getZhuanXiangYKBIds(ykbIds,getSchoolId(),ciId));
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
     * @param gradeId      //年级集合
     * @param classRoomIds //教室集合
     * @param weeks        //周集合 周一-周五
     * @param indexs       // 课节集合
     * @return
     */
    @RequestMapping("/getKeBiaoListZhou")
    @ResponseBody
    public RespObj getKeBiaoListZhou(@RequestParam String gradeId, @RequestParam String classRoomIds, @RequestParam String weeks, @RequestParam String indexs, @RequestParam Integer week) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            Map map = zkbService.getKeBiaoList(getDefauleTermId(), gradeId, classRoomIds, weeks, indexs, getSchoolId(), week);
            obj.setMessage(map);
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    /**
     * 课表插课
     *
     * @param ykbId
     * @param jxbId
     * @return
     */
    @RequestMapping("/saveKeBiaoInfo")
    @ResponseBody
    public RespObj saveKeBiaoInfo(String ykbId, String jxbId, String oykbId) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            paiKeService.saveKeBiaoInfo(ykbId, jxbId, oykbId);
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }


    /**
     * 课表插课根据教师
     *
     * @param jxbId
     * @return
     */
    @RequestMapping("/saveKeBiaoInfoByTeacher")
    @ResponseBody
    public RespObj saveKeBiaoInfoByTeacher(@RequestParam String xqid, @RequestParam String x, @RequestParam String y, @RequestParam String jxbId) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            paiKeService.saveKeBiaoInfoByTeacher(xqid, x, y, jxbId);
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    /**
     * 调课
     *
     * @param ykbId    //放到哪里
     * @param orgYkbId //源
     * @return
     */
    @RequestMapping("/exchangeJXBByZkb")
    @ResponseBody
    public RespObj exchangeJXBByZkb(String ykbId, String orgYkbId, int sweek, int eweek, int week) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(zkbService.exchangeJXBByZkb(ykbId, orgYkbId, sweek, eweek, week, getUserId()));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    /**
     * 学科可排教学班
     *
     * @param gradeId
     * @return
     */
    @RequestMapping("/getkpjxb")
    @ResponseBody
    public RespObj getkpjxb(String gradeId, String subjectId, int type, String termId) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            List<N33_JXBDTO> n33_jxbdtos = paiKeService.getkpjxb(new ObjectId(termId), gradeId, subjectId, getSchoolId(), type);
            obj.setMessage(n33_jxbdtos);
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    /**
     * 获得某个教学班的已排课时以及计划课时
     *
     * @param jxbId
     * @return
     */
    @RequestMapping("/getjxbks")
    @ResponseBody
    public RespObj getjxbks(String jxbId) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            N33_JXBDTO n33_jxbdto = paiKeService.getjxbks(jxbId);
            obj.setMessage(n33_jxbdto);
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }


    /**
     * 更新某个教学班的已排课时及计划课时
     *
     * @param jxbId
     * @param type  1 添加 2 移除
     * @return
     */
    @RequestMapping("/updatejxbks")
    @ResponseBody
    public RespObj updatejxbks(String jxbId, int type) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            paiKeService.updatejxbks(jxbId, type);
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    /**
     * 获得某个学科总的已排课时
     *
     * @param subjectId
     * @return
     */
    @RequestMapping("/getypksbysuid")
    @ResponseBody
    public RespObj getypksbysuid(String gradeId, String subjectId) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(paiKeService.getypksbysuid(getDefauleTermId(), gradeId, subjectId, getSchoolId()));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    /**
     * 删除教学班课时信息
     *
     * @param jxbId
     * @return
     */
    @RequestMapping("/deljxbks")
    @ResponseBody
    public RespObj deljxbks(String jxbId) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            paiKeService.deljxbks(jxbId);
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    /**
     * 清除教学班
     *
     * @param gradeId
     * @return
     */
    @RequestMapping("/clearKB")
    @ResponseBody
    public RespObj clearKB(String gradeId, String xqcid) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            paiKeService.clearKB(new ObjectId(xqcid), gradeId, getSchoolId());
            n33_zixikeDao.removeN33_ZIXIKEEntryByXqGiD(new ObjectId(xqcid), new ObjectId(gradeId));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    /**
     * 清除教学班
     *
     * @param gradeId
     * @return
     */
    @RequestMapping("/clearKBByCid")
    @ResponseBody
    public RespObj clearKBByCid(String gradeId, String xqcid,String classRoomId) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            paiKeService.clearKBByClassRoom(new ObjectId(xqcid), gradeId, getSchoolId(),new ObjectId(classRoomId));
            n33_zixikeDao.removeN33_ZIXIKEEntryByXqGiDByClassRoom(new ObjectId(xqcid), new ObjectId(gradeId),new ObjectId(classRoomId));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    /**
     * 清除教学班
     *
     * @param gradeId
     * @return
     */
    @RequestMapping("/clearKBByTea")
    @ResponseBody
    public RespObj clearKBByTea(String gradeId, String xqcid,String teaId) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            paiKeService.clearKBByTea(new ObjectId(xqcid), gradeId, getSchoolId(),new ObjectId(teaId));
            n33_zixikeDao.removeN33_ZIXIKEEntryByXqGiDByTeaId(new ObjectId(xqcid), new ObjectId(gradeId),new ObjectId(teaId));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    /**
     * 查询源课表中老师的事务
     *
     * @param ykbId
     * @return
     */
    @RequestMapping("/getTeaShiWu")
    @ResponseBody
    public RespObj getTeaShiWu(@RequestParam String ykbId,@RequestParam String classRoomId,@RequestParam int x,@RequestParam int y) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            List<N33_SWDTO> swdtoList = paiKeService.getTeaShiWu(ykbId,classRoomId,x,y);
            obj.setMessage(swdtoList);
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    /**
     * 获得源课表某个位置的教学班
     *
     * @param gradeId
     * @param classroomId
     * @param x
     * @param y
     * @return
     */
    @RequestMapping("/getJXB")
    @ResponseBody
    public RespObj getJXB(String gradeId, String classroomId, int x, int y) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(paiKeService.getJXB(getDefauleTermId(), gradeId, classroomId, x, y, getSchoolId()));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    //1.获得当前已排的教学班列表 		GetSettledJXBList(sch, term, grade)

    /**
     * 获得当前已排的教学班列表
     *
     * @param gradeId
     * @return
     */
    @RequestMapping("/GetSettledJXBinSourceKB")
    @ResponseBody
    public RespObj GetSettledJXBinSourceKB(String gradeId) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(paiKeService.GetSettledJXBinSourceKB(getDefauleTermId(), gradeId, getSchoolId()));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    /**
     * 获得所有已排教学班的占位
     *
     * @param gradeId
     * @return
     */
    @RequestMapping("/getPositionofSettledJXB")
    @ResponseBody
    public RespObj getPositionofSettledJXB(String gradeId) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(paiKeService.getPositionofSettledJXB(getDefauleTermId(), gradeId, getSchoolId()));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    /**
     * 获得与待排教学班有冲突的所有已排教学班的位置
     *
     * @param jxbId
     * @return
     */
    @RequestMapping("/getConflictedSettledJXB")
    @ResponseBody
    public RespObj getConflictedSettledJXB(String jxbId, String termId) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(paiKeService.getConflictedSettledJXB(new ObjectId(termId), jxbId, getSchoolId()));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    /**
     * @param jxbId
     * @param termId
     * @param classroomId
     * @return
     */
    @RequestMapping("/getConflictedSettledJXBByRoomId")
    @ResponseBody
    public RespObj getConflictedSettledJXBByRoomId(String jxbId, String termId, String classroomId) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(paiKeService.getConflictedSettledJXBByRoomId(new ObjectId(termId), jxbId, classroomId, getSchoolId()));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }


    /**
     * 查询排课教室
     */
    @UserDataCollection(pageTag = "n33", params = {"gradeId"}, isAllCollection = false)
    @RequestMapping("/getRoomEntryListByXqGrade")
    @ResponseBody
    public RespObj getRoomEntryListByXqGrade(@RequestParam String gradeId, @RequestParam String xqid) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(classroomService.getRoomEntryListByXqGrade(new ObjectId(xqid), getSchoolId(), new ObjectId(gradeId), 1));
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
    @UserDataCollection(pageTag = "n33", params = {"gradeId"}, isAllCollection = false)
    @RequestMapping("/getRoomEntryListByXqGradeAndTerm")
    @ResponseBody
    public RespObj getRoomEntryListByXqGradeAndTerm(@RequestParam String gradeId, @RequestParam String xqid) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(classroomService.getRoomEntryListByXqGradeAndTerm(new ObjectId(xqid), getSchoolId(), new ObjectId(gradeId), 1));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }


    //获得某个老师的已排课表（x, y）list
    @RequestMapping("/GetTeachersSettledPositions")
    @ResponseBody
    public RespObj GetTeachersSettledPositions(String teacherId) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(paiKeService.GetTeachersSettledPositions(new ObjectId(teacherId), getSchoolId()));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    //获得某个老师的已排课表（x, y）list
    @RequestMapping("/GetTeachersSettledPositionsByGrade")
    @ResponseBody
    public RespObj GetTeachersSettledPositionsByGrade(String teacherId, String gid) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(paiKeService.GetTeachersSettledPositionsByGrade(new ObjectId(gid), new ObjectId(teacherId), getSchoolId()));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    @RequestMapping("/GetTeachersSettledPositionsTag")
    @ResponseBody
    public RespObj GetTeachersSettledPositionsTag(String tagId) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(paiKeService.GetTeachersSettledPositionsTag(new ObjectId(tagId), getSchoolId()));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }


    //获得某个老师的已排课表（x, y）list周课表
    @RequestMapping("/GetTeachersSettledPositionsByWeek")
    @ResponseBody
    public RespObj GetTeachersSettledPositions(String teacherId, Integer week) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(zkbService.GetTeachersSettledPositionsByWeek(new ObjectId(teacherId), getSchoolId(), week));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    @RequestMapping("/getGradeSubjectTeaTable")
    @ResponseBody
    public RespObj getGradeSubjectTeaTable(String xqid,String gradeId,String subjectId, Integer week) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(zkbService.getGradeSubjectTeaTable(new ObjectId(xqid),gradeId,new ObjectId(subjectId), week));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }
    
    //oz获取学科课表
    @RequestMapping("/getGradeSubTable")
    @ResponseBody
    public RespObj getGradeSubTable(String xqid,String gradeId,String subjectId, Integer week) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(zkbService.getGradeSubTeaKb(new ObjectId(xqid),gradeId,new ObjectId(subjectId), week,getSchoolId()));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    @RequestMapping("/getZhuanXiangZuHeXianQing")
    @ResponseBody
    public RespObj getZhuanXiangZuHeXianQing(String xqid,String gradeId,String subjectId) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(zkbService.getZhuanXiangZuHeXianQing(new ObjectId(xqid), gradeId, new ObjectId(subjectId)));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    @RequestMapping("/getZhuanXiangZuHeXianQingAll")
    @ResponseBody
    public RespObj getZhuanXiangZuHeXianQingAll(String xqid,String gradeId) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(zkbService.getZhuanXiangZuHeXianQingAll(new ObjectId(xqid),gradeId));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }


    //获得某个老师的已排课表（x, y）list周课表
    @RequestMapping("/GetStudentSettledPositionsByWeek")
    @ResponseBody
    public RespObj GetStudentSettledPositionsByWeek(String studentId, Integer week, String gid) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(zkbService.GetStudentSettledPositionsByWeek(new ObjectId(studentId), getSchoolId(), week, new ObjectId(gid)));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    /**
     * 教师课表老师列表
     *
     * @return
     */
    @RequestMapping("/getTeaListBySubjectIdIsTeaTable")
    @ResponseBody
    public RespObj getTeaListBySubjectIdIsTeaTable(String subid, @RequestParam(defaultValue = "*") String name, @RequestParam(defaultValue = "0") Integer week) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(teaService.getTeaListBySubjectIdIsTeaTable(new ObjectId(subid), getSchoolId(), name, week));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }


    @RequestMapping("/getTeaListBySubjectIdIsTeaTableWeek")
    @ResponseBody
    public RespObj getTeaListBySubjectIdIsTeaTableWeek(String subid, @RequestParam(defaultValue = "*") String name, @RequestParam(defaultValue = "0") Integer week,@RequestParam String gid) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(teaService.getTeaListBySubjectIdIsTeaTableWeek(new ObjectId(subid), getSchoolId(), name, week,new ObjectId(gid)));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    /**
     * 查询行政班课表
     *
     * @return
     */
    @RequestMapping("/GetClassSettledPositionsByWeek")
    @ResponseBody
    public RespObj GetClassSettledPositionsByWeek(String classId, Integer week, String gradeId) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(zkbService.GetClassSettledPositionsByWeek(new ObjectId(classId), getDefauleTermId(), new ObjectId(gradeId), getSchoolId(), week));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    /**
     * 查询行政班课表详情
     *
     * @return
     */
    @RequestMapping("/GetClassSettledPositionsByWeekXyInfo")
    @ResponseBody
    public RespObj GetClassSettledPositionsByWeekXyInfo(@RequestBody Map<String, Object> map) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(zkbService.GetClassSettledPositionsByWeekXyInfo(map, getSchoolId()));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    @RequestMapping("/exportJXBinfo")
    @ResponseBody
    public void exportJXBinfo(String name, Integer week, Integer x, Integer y, String classId, String gradeId, String xqid, HttpServletResponse response) {
        zkbService.exportJXBinfo(name, week, y - 1, x - 1, new ObjectId(xqid), new ObjectId(gradeId), new ObjectId(classId), getSchoolId(), response);
    }

    @RequestMapping("/GetClassSettledPositionsWeekJxb")
    @ResponseBody
    public RespObj GetClassSettledPositionsWeekJxb(Integer week, Integer x, Integer y, String classId, String gradeId, String xqid) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(zkbService.exportJXBinfo(week, y - 1, x - 1, new ObjectId(xqid), new ObjectId(gradeId), new ObjectId(classId), getSchoolId()));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    /**
     * 查询行政班课表
     *
     * @return
     */
    @RequestMapping("/GetClassSettledPositions")
    @ResponseBody
    public RespObj GetClassSettledPositions(String classId, String gradeId, String xqid) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(paiKeService.GetClassSettledPositions(new ObjectId(classId), new ObjectId(xqid), new ObjectId(gradeId), getSchoolId()));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    /**
     * 查询行政班课表
     *
     * @return
     */
    @RequestMapping("/GetPositionsByWeek")
    @ResponseBody
    public RespObj GetClassSettledPositionsByWeek(@RequestParam String xqid, String gid, Integer week) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(zkbService.getN33_ZKBByWeek(new ObjectId(xqid), new ObjectId(gid), week));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    /**
     * 发布课表
     *
     * @return
     */
    @RequestMapping("/faBuKeBiao")
    @ResponseBody
    public RespObj faBuKfaBuKeBiaoeBiao(Integer staWeek, Integer endWeek, String gid, String cid, HttpServletRequest request) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            zkbService.CopySourceKB2WeekKB(getDefauleTermId(), getSchoolId(), getUserId(), staWeek, endWeek, new ObjectId(gid), new ObjectId(cid), request);
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }


    /**
     * 发布课表
     *
     * @return
     */
    @RequestMapping("/ZiDongPaiKeZuHe")
    @ResponseBody
    public RespObj ZiDongPaiKeZuHe(String gradeId, String ciId, String xqid, HttpServletRequest request) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            autoPkZuHeService.autoPaiKe(getSchoolId(), new ObjectId(gradeId), new ObjectId(ciId), new ObjectId(xqid), request);
        } catch (Exception e) {
            HttpSession session = request.getSession();
            Map<String, Object> status = new HashMap<String, Object>();
            status.put("st", -1);
            session.setAttribute("paike", status);
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    @RequestMapping("/ZiDongPaiKe")
    @ResponseBody
    public RespObj ZiDongPaiKe(String gradeId, String ciId, String xqid, HttpServletRequest request) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            autoPkService.autoPaiKe(getSchoolId(), new ObjectId(gradeId), new ObjectId(ciId), new ObjectId(xqid), request);
        } catch (Exception e) {
            HttpSession session = request.getSession();
            Map<String, Object> status = new HashMap<String, Object>();
            status.put("st", -1);
            session.setAttribute("paike", status);
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    @RequestMapping("/checkJXBInfo")
    @ResponseBody
    public RespObj checkJXBInfo(String gradeId, String ciId) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(autoPkService.checkJXBInfo(getSchoolId(), new ObjectId(ciId), gradeId));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }


    @ResponseBody
    @RequestMapping("/getStatus")
    public Map<String, Object> getStatus(HttpServletRequest request) {
        return zkbService.getStatus(request);
    }

    @ResponseBody
    @RequestMapping("/getStatusPK")
    public Map<String, Object> getStatusPK(HttpServletRequest request) {
        return autoPkService.getStatus(request);
    }

    @ResponseBody
    @RequestMapping("/getStatusDC")
    public Map<String, Object> getStatusDC(HttpServletRequest request) {
        return zkbService.getStatusDC(request);
    }

    /**
     * 取消发布课表
     *
     * @return
     */
    @RequestMapping("/delZhouKeBiao")
    @ResponseBody
    public RespObj delZhouKeBiao(Integer staWeek, Integer endWeek, String gid, String cid,String xqid) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            zkbService.removeKBByXqid(new ObjectId(xqid), getSchoolId(), staWeek, endWeek, new ObjectId(gid), new ObjectId(cid));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    /**
     * 取消发布课表
     *
     * @return
     */
    @RequestMapping("/getCiIds")
    @ResponseBody
    public RespObj getCiIds(@RequestParam Integer staWeek,@RequestParam Integer endWeek, @RequestParam String gid, @RequestParam String cid,@RequestParam String xqid) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(zkbService.getCiIds(staWeek,endWeek,new ObjectId(gid),new ObjectId(xqid)));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }


    /**
     * 保存课表
     */
    @RequestMapping("/MingMingKeBiao")
    @ResponseBody
    public RespObj MingMingKeBiao(String name, String gid) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            n33_nameKBService.addNameKB(getDefaultPaiKeTerm().getPaikeci(), getSchoolId(), name, new ObjectId(gid), getUserId());
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    /**
     * 加载全部命名课表
     */
    @RequestMapping("/getDtoType")
    @ResponseBody
    public RespObj getDtoType(String gid) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            List<Map> dtos = n33_nameKBService.getDtoType(getDefaultPaiKeTerm().getPaikeci(), getSchoolId(), new ObjectId(gid));
            obj.setMessage(dtos);
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    /**
     * 加载某个命名课表
     */
    @RequestMapping("/getDtoByNid")
    @ResponseBody
    public RespObj getDtoByNid(String nid, String gid) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            n33_nameKBService.getDtoByNid(new ObjectId(nid), getDefaultPaiKeTerm().getPaikeci(), new ObjectId(gid));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }


    /**
     * 获得与待排教学班有冲突的所有已排教学班的位置
     *
     * @param zkbId
     * @return
     */
    @RequestMapping("/getConflictedSettledJXBByZKB")
    @ResponseBody
    public RespObj getConflictedSettledJXBByZKBId(String zkbId) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(zkbService.getConflictedSettledJXBByZKBId(zkbId));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    /**
     * 撤销操作
     *
     * @param ykbIds
     * @return
     */
    @RequestMapping("/undoYKB")
    @ResponseBody
    public RespObj undoYKB(String ykbIds) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            paiKeService.undoYKB(ykbIds, getSchoolId());
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    /**
     * @return
     */
    @RequestMapping("exportPKData")
    @ResponseBody
    public boolean exportPKData(@RequestParam String gradeId, @RequestParam String classRoomIds, @RequestParam String weeks, @RequestParam String indexs, HttpServletResponse response) {
        paiKeService.exportPKData(getSchoolId(), getDefauleTermId(), gradeId, classRoomIds, weeks, indexs, response);
        return true;
    }

    /**
     * 导出教师课表
     *
     * @param
     * @return
     */
    @RequestMapping("exportTeaKB")
    @ResponseBody
    public void exportTeaKB(@RequestParam String userName, @RequestParam String teacherId, @RequestParam Integer week, HttpServletResponse response) {
        try {
            zkbService.exportTeaKB(userName, new ObjectId(teacherId), getSchoolId(), week, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 批量导出老师课表
     *
     * @param
     * @return
     */
    @RequestMapping("exportTeaKBByGroup")
    @ResponseBody
    public void exportTeaKBByGroup(@RequestParam String dataMap,HttpServletResponse response,HttpServletRequest request) {
        Map<String, Object> parm = JSON.parseObject(dataMap, Map.class);
        try {
            zkbService.exportTeaKBByGroup(parm,getSchoolId(),response,request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 导出教师课表
     *
     * @param
     * @return
     */
    @RequestMapping("exportTeaKB1")
    @ResponseBody
    public void exportTeaKB1(HttpServletResponse response) {

        try {
            zkbService.daochu(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/getGradeSubjectTeaTableImp")
    @ResponseBody
    public void getGradeSubjectTeaTableIsavmp(String xqid,String gradeId,String subjectId, Integer week, HttpServletResponse response) {
        try {
            zkbService.getGradeSubjectTeaTableImp(new ObjectId(xqid), gradeId, new ObjectId(subjectId), week,getSchoolId(),response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 导出学生课表
     *
     * @param
     * @return
     */
    @RequestMapping("exportStuKB")
    @ResponseBody
    public void exportStuKB(@RequestParam String userName, @RequestParam String studentId, @RequestParam Integer week, String gid,String className, HttpServletResponse response) {
        try {
            zkbService.exportStuKB(userName, new ObjectId(studentId), getSchoolId(), className,week, new ObjectId(gid), response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 导出行政班课表
     *
     * @param
     * @return
     */
    @RequestMapping("exportXZBKB")
    @ResponseBody
    public void exportXZBKB(@RequestParam String className, @RequestParam String classId, @RequestParam String xqid, @RequestParam String gradeId, @RequestParam Integer week,@RequestParam Boolean isChecked, HttpServletResponse response) {
        try {
            zkbService.exportXZBKB(className, new ObjectId(classId), new ObjectId(xqid), new ObjectId(gradeId), getSchoolId(), week,isChecked, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断是否可以导出课表
     *
     * @param
     * @return
     */
    @RequestMapping("isCanExport")
    @ResponseBody
    public RespObj isCanExport(@RequestParam String xqid, @RequestParam String gradeId, @RequestParam Integer week) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(zkbService.isCanExport(new ObjectId(xqid), getSchoolId(), week, new ObjectId(gradeId)));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    /**
     * 导出总课表
     *
     * @param
     * @return
     */
    @RequestMapping("exportZKBDataByClass")
    @ResponseBody
    public void exportZKBDataByClass(@RequestParam String xqid, @RequestParam String gradeId, @RequestParam Integer week, @RequestParam String gradeName, HttpServletResponse response,@RequestParam boolean isChecked) {
        try {
            zkbService.exportZKBDataByClass(new ObjectId(xqid), new ObjectId(gradeId), getSchoolId(), week, gradeName, response,isChecked);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 导出总课表
     *
     * @param
     * @return
     */
    @RequestMapping("exportZKBData")
    @ResponseBody
    public void exportZKBData(@RequestParam String dataMap, HttpServletResponse response) {
        try {
            @SuppressWarnings("unchecked")
            Map<String, List<String>> parm = JSON.parseObject(dataMap, Map.class);
            //页面带过来的所有课节
            List<String> kjList = (List<String>) parm.get("kjList");

            List<String> weekList = (List<String>) parm.get("weekList");
            Integer week = new Integer(weekList.get(0));

            List<String> gradeList = (List<String>) parm.get("gradeList");
            String gradeId = gradeList.get(0);

            List<String> classRoomList = (List<String>) parm.get("classRoomList");

            List<String> jxrList = (List<String>) parm.get("jxrList");

            zkbService.exportZKBData(getDefauleTermId(), gradeId, classRoomList, jxrList, kjList, getSchoolId(), week, response);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 代课老师
     *
     * @param ykbId
     * @param teacherId
     * @return
     */
    @RequestMapping("/dkJXB")
    @ResponseBody
    public RespObj dkJXB(String ykbId,String orgTeacherId, String teacherId, int sweek, int eweek, int week,String index) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(zkbService.dkJXB(ykbId,orgTeacherId, getSchoolId(), teacherId, sweek, eweek, week,index, getUserId()));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    /**
     * 获取教学班
     *
     * @param
     * @return
     */
    @RequestMapping("/getJXBByXY")
    @ResponseBody
    public RespObj getJXBByXY(int x, int y, String xqid, String gradeId) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(zkbService.getJXBByXY(x, y, new ObjectId(gradeId), new ObjectId(xqid), getSchoolId()));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    /**
     * 获取教学班
     *
     * @param ykbId
     * @return
     */
    @RequestMapping("/getJXBById")
    @ResponseBody
    public RespObj getJXBById(String ykbId, String gradeId) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(zkbService.getJXBById(ykbId, gradeId, getSchoolId()));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    @RequestMapping("/getTeacherKbByZKB")
    @ResponseBody
    public RespObj getTeacherKbByZKB(String zkbId,String teacherId,String pkci) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(zkbService.getTeacherKbByZKB(getDefauleTermId(),zkbId, getSchoolId(),new ObjectId(teacherId),pkci));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    /**
     * @param gradeId
     * @param subjectId
     * @param type
     * @param termId
     * @param teaId
     * @return
     */
    @RequestMapping("/getkpjxbByTeaId")
    @ResponseBody
    public RespObj getkpjxbByTeaId(@RequestParam String gradeId, @RequestParam String subjectId, @RequestParam int type, @RequestParam String termId, @RequestParam String teaId) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            List<N33_JXBDTO> n33_jxbdtos = paiKeService.getkpjxbByTeaId(new ObjectId(termId), new ObjectId(gradeId), new ObjectId(subjectId), getSchoolId(), type, new ObjectId(teaId));
            obj.setMessage(n33_jxbdtos);
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    @RequestMapping("/getRoomJXBCount")
    @ResponseBody
    public RespObj getRoomJXBCount(@RequestParam String xqid, @RequestParam String roomId, @RequestParam String gid, @RequestParam Integer type) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            Integer count = paiKeService.getRoomJXBCount(new ObjectId(xqid), getSchoolId(), new ObjectId(roomId), new ObjectId(gid), type);
            obj.setMessage(count);
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage(0);
        }
        return obj;
    }

    @RequestMapping("/getTeacherJXBCount")
    @ResponseBody
    public RespObj getTeacherJXBCount(@RequestParam String xqid, @RequestParam String teaId, @RequestParam String gid, @RequestParam Integer type) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            Integer count = paiKeService.getTeacherJXBCount(new ObjectId(xqid), getSchoolId(), new ObjectId(teaId), new ObjectId(gid), type);
            obj.setMessage(count);
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage(0);
        }
        return obj;
    }

    @RequestMapping("/getkpjxbByTag")
    @ResponseBody
    public RespObj getkpjxbByTag(@RequestParam String type, @RequestParam String tagId, @RequestParam String gid) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            List<N33_JXBDTO> n33_jxbdtos = paiKeService.getkpjxbByTag(type, new ObjectId(tagId), getSchoolId(), new ObjectId(gid));
            obj.setMessage(n33_jxbdtos);
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    /**
     * @param gradeId
     * @param type
     * @param termId
     * @param classroomId
     * @return
     */
    @RequestMapping("/getkpjxbByCrmid")
    @ResponseBody
    public RespObj getkpjxbByCrmid(String gradeId, int type, String termId, String classroomId) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            List<N33_JXBDTO> n33_jxbdtos = paiKeService.getkpjxbByCrmid(new ObjectId(termId), gradeId, getSchoolId(), type, classroomId);
            obj.setMessage(n33_jxbdtos);
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    /**
     * 连堂课
     *
     * @param jxbId
     * @return
     */
    @RequestMapping("/lianTangs")
    @ResponseBody
    public RespObj lianTangs(String jxbId) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(paiKeService.lianTangs(jxbId, getSchoolId()));
            obj.setMessage(paiKeService.lianTangs(jxbId, getSchoolId()));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    /**
     * 调课记录
     *
     * @param gradeId
     * @param termId
     * @return
     */
    @UserDataCollection(pageTag = "n33", params = {"gradeId"}, isAllCollection = false)
    @RequestMapping("/getTiaoKeLog")
    @ResponseBody
    public RespObj getTiaoKeLog(String gradeId, String termId, String ci, int page, int pageSize, String userName) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(zkbService.getTiaoKeLog(gradeId, termId, ci, getUserId(), getSessionValue().getUserRole(), getSchoolId(), userName, page, pageSize));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    /**
     * 发布记录
     *
     * @param gradeId
     * @param termId
     * @param ci
     * @return
     */
    @RequestMapping("/getFaBuLogs")
    @ResponseBody
    public RespObj getFaBuLogs(String gradeId, String termId, String ci) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(paiKeService.getFaBuLogs(gradeId, termId, ci, getSchoolId()));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    @RequestMapping("/addOrUpdateGuiZe")
    @ResponseBody
    public RespObj addOrUpdateGuiZe(@RequestBody N33_AutoPaiKeDTO map) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            map.setSid(getSchoolId().toString());
            autoPkService.addOrUpdateGuiZe(map);
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    @RequestMapping("/getGuiZeList")
    @ResponseBody
    public RespObj getGuiZeList(String gid, String ciId, Integer type) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(autoPkService.getGuiZeList(new ObjectId(ciId), new ObjectId(gid), type, getSchoolId()));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    @RequestMapping("/delGuiZe")
    @ResponseBody
    public RespObj delGuiZe(String id) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            autoPkService.delGuiZe(new ObjectId(id));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    @RequestMapping("/getGuiZe")
    @ResponseBody
    public RespObj getGuiZe(String id) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(autoPkService.getGuiZe(new ObjectId(id)));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    @RequestMapping("/getTeaJxbListByXqid")
    @ResponseBody
    public RespObj getTeaJxbListByXqid(String xqid) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(jxbService.getTeaJxbList(new ObjectId(xqid), getUserId()));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    @RequestMapping("/getJxb")
    @ResponseBody
    public RespObj getJxb(String jxbId) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(jxbService.getJxbEntry(new ObjectId(jxbId)));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }


    @RequestMapping("/getZkbByJxbId")
    @ResponseBody
    public RespObj getZkbByJxbId(String jxbId,int x,int y, Integer week) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(zkbService.getZkbByJxbId(new ObjectId(jxbId),x,y, week));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    @RequestMapping("/getZkbByJxbIdYKB")
    @ResponseBody
    public RespObj getZkbByJxbIdYKB(String jxbId) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(zkbService.getZkbByJxbIdYKB(new ObjectId(jxbId)));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    @RequestMapping("/getAllCTXY")
    @ResponseBody
    public RespObj getAllCTXY(@RequestParam String jxbId,@RequestParam String classRoomId,@RequestParam(defaultValue = "0") Integer type,@RequestParam Integer x,@RequestParam Integer y,@RequestParam String tid) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(zkbService.getAllCTXY(new ObjectId(jxbId),new ObjectId(classRoomId),type,x,y,new ObjectId(tid)));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    @RequestMapping("/getShiBuShiKeTiaoKe")
    @ResponseBody
    public RespObj getShiBuShiKeTiaoKe(String jxbId1, String jxbId2, @RequestParam(defaultValue = "0") Integer X1, @RequestParam(defaultValue = "0") Integer X2, @RequestParam(defaultValue = "0") Integer Y1, @RequestParam(defaultValue = "0") Integer Y2, String xqid, Integer week) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            ObjectId jid = null;
            ObjectId jid2 = null;
            if (!jxbId1.equals("")) {
                jid = new ObjectId(jxbId1);
            }
            if (!jxbId2.equals("")) {
                jid2 = new ObjectId(jxbId2);
            }
            obj.setMessage(zkbService.getShiBuShiKeTiaoKe(jid, jid2, X1, X2, Y1, Y2, new ObjectId(xqid), week));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    @RequestMapping("/getShiBuShiKeTiaoKeYKB")
    @ResponseBody
    public RespObj getShiBuShiKeTiaoKeYKB(@RequestParam(defaultValue = "*") String ybkId,@RequestParam String ybkId1,@RequestParam Integer x,@RequestParam Integer y,@RequestParam String ciId) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            if(!"*".equals(ybkId)){
                N33_YKBEntry ykbEntry = ykbDao.getN33_YKBById(new ObjectId(ybkId));
                N33_YKBEntry ykbEntry1 = ykbDao.getN33_YKBById(new ObjectId(ybkId1));
                //记录调课
                N33_ExClassRecordEntry entry = new N33_ExClassRecordEntry(ykbEntry.getClassroomId(),System.currentTimeMillis(),ykbEntry.getSchoolId(),ykbEntry.getTermId(),ykbEntry.getX(),ykbEntry.getY(),ykbEntry1.getX(),ykbEntry1.getY(),new ObjectId(ybkId),new ObjectId(ybkId1),ykbEntry.getGradeId());
                if(ykbEntry.getType() == 6){
                    N33_JXBEntry jxbEntry = jxbDao.getJXBById(ykbEntry.getJxbId());
                    N33_JXBEntry jxbEntry1 = jxbDao.getJXBById(ykbEntry.getNJxbId());
                    String jxbAndTeaName = "";
                    if(jxbEntry.getNickName() != null && !"".equals(jxbEntry.getNickName())){
                        jxbAndTeaName += jxbEntry.getNickName();
                        jxbAndTeaName += "（单）";
                    }else{
                        jxbAndTeaName += jxbEntry.getName();
                        jxbAndTeaName += "（单）";
                    }
                    N33_TeaEntry teaEntry = teaDao.findIsolateN33_TeaEntryById(jxbEntry.getTercherId(),jxbEntry.getTermId());
                    if(teaEntry != null){
                        jxbAndTeaName += "</br>";
                        jxbAndTeaName += teaEntry.getUserName();
                        jxbAndTeaName += "（单）";
                    }
                    if(jxbEntry1.getNickName() != null && !"".equals(jxbEntry1.getNickName())){
                        jxbAndTeaName += "</br>";
                        jxbAndTeaName += jxbEntry1.getNickName();
                        jxbAndTeaName += "（双）";
                    }else{
                        jxbAndTeaName += jxbEntry1.getName();
                        jxbAndTeaName += "（双）";
                    }
                    N33_TeaEntry teaEntry1 = teaDao.findIsolateN33_TeaEntryById(jxbEntry1.getTercherId(),jxbEntry1.getTermId());
                    if(teaEntry1 != null){
                        jxbAndTeaName += "</br>";
                        jxbAndTeaName += teaEntry1.getUserName();
                        jxbAndTeaName += "（双）";
                    }
                    entry.setJXBANDTeaName(jxbAndTeaName);
                }else{
                    N33_JXBEntry jxbEntry = jxbDao.getJXBById(ykbEntry.getJxbId());
                    String jxbAndTeaName = "";
                    if(jxbEntry.getNickName() != null && !"".equals(jxbEntry.getNickName())){
                        jxbAndTeaName += jxbEntry.getNickName();
                    }else{
                        jxbAndTeaName += jxbEntry.getName();
                    }
                    N33_TeaEntry teaEntry = teaDao.findIsolateN33_TeaEntryById(jxbEntry.getTercherId(),jxbEntry.getTermId());
                    if(teaEntry != null){
                        jxbAndTeaName += "</br>";
                        jxbAndTeaName += teaEntry.getUserName();
                    }
                    entry.setJXBANDTeaName(jxbAndTeaName);
                }

                if(ykbEntry1.getType() == 6){
                    N33_JXBEntry jxbEntry = jxbDao.getJXBById(ykbEntry1.getJxbId());
                    N33_JXBEntry jxbEntry1 = jxbDao.getJXBById(ykbEntry1.getNJxbId());
                    String oJxbAndTeaName = "";
                    if(jxbEntry.getNickName() != null && !"".equals(jxbEntry.getNickName())){
                        oJxbAndTeaName += jxbEntry.getNickName();
                        oJxbAndTeaName += "（单）";
                    }else{
                        oJxbAndTeaName += jxbEntry.getName();
                        oJxbAndTeaName += "（单）";
                    }
                    N33_TeaEntry teaEntry = teaDao.findIsolateN33_TeaEntryById(jxbEntry.getTercherId(),jxbEntry.getTermId());
                    if(teaEntry != null){
                        oJxbAndTeaName += "</br>";
                        oJxbAndTeaName += teaEntry.getUserName();
                        oJxbAndTeaName += "（单）";
                    }
                    if(jxbEntry1.getNickName() != null && !"".equals(jxbEntry1.getNickName())){
                        oJxbAndTeaName += "</br>";
                        oJxbAndTeaName += jxbEntry1.getNickName();
                        oJxbAndTeaName += "（双）";
                    }else{
                        oJxbAndTeaName += jxbEntry1.getName();
                        oJxbAndTeaName += "（双）";
                    }
                    N33_TeaEntry teaEntry1 = teaDao.findIsolateN33_TeaEntryById(jxbEntry1.getTercherId(),jxbEntry1.getTermId());
                    if(teaEntry1 != null){
                        oJxbAndTeaName += "</br>";
                        oJxbAndTeaName += teaEntry1.getUserName();
                        oJxbAndTeaName += "（双）";
                    }
                    entry.setoJXBANDTeaName(oJxbAndTeaName);
                }else if(ykbEntry1.getType() == 1 || ykbEntry1.getType() == 2 || ykbEntry1.getType() == 3 || ykbEntry1.getType() == 4 || ykbEntry1.getType() == 5){
                    N33_JXBEntry jxbEntry = jxbDao.getJXBById(ykbEntry1.getJxbId());
                    String oJxbAndTeaName = "";
                    if(jxbEntry.getNickName() != null && !"".equals(jxbEntry.getNickName())){
                        oJxbAndTeaName += jxbEntry.getNickName();
                    }else{
                        oJxbAndTeaName += jxbEntry.getName();
                    }
                    N33_TeaEntry teaEntry = teaDao.findIsolateN33_TeaEntryById(jxbEntry.getTercherId(),jxbEntry.getTermId());
                    if(teaEntry != null){
                        oJxbAndTeaName += "</br>";
                        oJxbAndTeaName += teaEntry.getUserName();
                    }
                    entry.setoJXBANDTeaName(oJxbAndTeaName);
                }else{
                    entry.setoJXBANDTeaName("");
                }
                recordDao.addN33_ExClassRecordEntry(entry);
                tiaokeLuo(ybkId,ybkId1);
            }else{
                N33_YKBEntry ykbEntry1 = ykbDao.getN33_YKBById(new ObjectId(ybkId1));
	            N33_YKBEntry ykbEntry = ykbDao.getN33_YKBByClassRoom(x,y,ykbEntry1.getClassroomId(),new ObjectId(ciId));
	            tiaokeLuo(ykbEntry.getID().toString(),ykbEntry1.getID().toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    @RequestMapping("/getRecord")
    @ResponseBody
    public RespObj getN33_ExClassRecord(@RequestParam String xqid){
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(exClassRecordService.getN33_ExClassRecord(new ObjectId(xqid),getSchoolId()));
        }catch (Exception e){
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    @RequestMapping("/cancelRecord")
    @ResponseBody
    public RespObj cancelRecord(@RequestParam String xqid){
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            exClassRecordService.cancelRecord(new ObjectId(xqid),getSchoolId());
        }catch (Exception e){
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    @RequestMapping("/deleteRecord")
    @ResponseBody
    public RespObj deleteRecord(@RequestParam String xqid){
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            exClassRecordService.deleteRecord(new ObjectId(xqid),getSchoolId());
        }catch (Exception e){
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

//    @RequestMapping("/chexiao")
//    @ResponseBody
//    public RespObj chexiao() {
//        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
//        try {
//            if(!"*".equals(ybkId)){
//                N33_YKBEntry ykbEntry = ykbDao.getN33_YKBById(new ObjectId(ybkId));
//                N33_YKBEntry ykbEntry1 = ykbDao.getN33_YKBById(new ObjectId(ybkId1));
//                //记录调课
//                N33_ExClassRecordEntry entry = new N33_ExClassRecordEntry(ykbEntry.getClassroomId(),System.currentTimeMillis(),ykbEntry.getSchoolId(),ykbEntry.getTermId(),ykbEntry.getX(),ykbEntry.getY(),ykbEntry1.getX(),ykbEntry1.getY(),new ObjectId(ybkId),new ObjectId(ybkId1),ykbEntry.getGradeId());
//                recordDao.addN33_ExClassRecordEntry(entry);
//                tiaokeLuo(ybkId,ybkId1);
//            }else{
//                N33_YKBEntry ykbEntry1 = ykbDao.getN33_YKBById(new ObjectId(ybkId1));
//                N33_YKBEntry ykbEntry = ykbDao.getN33_YKBByClassRoom(x,y,ykbEntry1.getClassroomId(),new ObjectId(ciId));
//                tiaokeLuo(ykbEntry.getID().toString(),ykbEntry1.getID().toString());
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            obj.setCode(Constant.FAILD_CODE);
//            obj.setMessage("服务器正忙");
//        }
//        return obj;
//    }

    @RequestMapping("/tiaokeLuo")
    @ResponseBody
    public RespObj tiaokeLuo(String ybkId,String ybkId1) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            zkbService.tiaokeLuo(new ObjectId(ybkId),new ObjectId(ybkId1));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }
    /**
     * 新增
     *
     * @return
     */
    @RequestMapping("/addOrUpdateShengQing")
    @ResponseBody
    public RespObj addOrUpdateShengQing(@RequestBody N33_TiaoKeShengQingDTO dto) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            dto.setSid(getSchoolId().toString());
            dto.setTeaId(getUserId().toHexString());
            paiKeService.addOrUpdate(dto);
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }


    @RequestMapping("/getShengQingList")
    @ResponseBody
    public RespObj getShengQingList(String xqid,Integer week) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(paiKeService.getDtoList(new ObjectId(xqid), getUserId(),week));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    @RequestMapping("/getShengQingListALL")
    @ResponseBody
    public RespObj getShengQingListALL(String xqid) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(paiKeService.getShengQingListALL(new ObjectId(xqid), getUserId()));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    /***
     *    2同意 3拒绝
     * @param id
     * @param sta
     * @return
     */
    @RequestMapping("/updateStaTiao")
    @ResponseBody
    public RespObj updateStaTiao(String id, Integer sta) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            paiKeService.updateSta(new ObjectId(id), sta);
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    @RequestMapping("/QuXiaoShengQing")
    @ResponseBody
    public RespObj QuXiaoShengQing(String id) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
           paiKeService.delShengQing(new ObjectId(id));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }


    /**
     *
     * @param id
     * @param type
     * @return
     */
    @RequestMapping("/tkChangeType")
    @ResponseBody
    public RespObj tkChangeType(String id, Integer type) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            zkbService.tkChangeType(new ObjectId(id), type);
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    /**
     * 导出调课报告
     *
     * @param response
     * @throws Exception
     */
    @RequestMapping("/exportTKReport")
    public void exportTKReport(String gradeId, String termId, String ci, String userName,HttpServletResponse response) throws Exception {
        try {
            zkbService.exportTiaoKeLog(gradeId, termId, ci, getSchoolId(), userName,response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     *
     * @param gradeId
     * @param termId
     * @param ci
     * @return
     */
    @RequestMapping("/getTKTotal")
    @ResponseBody
    public RespObj getTKTotal(String gradeId, String termId, String ci) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            Map map = zkbService.getTKTotal(gradeId, termId, ci, getSchoolId());
            obj.setMessage(map);
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }
    
    /**
	 * 批量导出学生课表
	 * oz
	 */
    @RequestMapping("expStusKB")
	@ResponseBody
	public String exportStusKB(@RequestBody Map repMap,HttpServletRequest request){
    	String stuIds = repMap.get("stuIds").toString();
    	int week = Integer.parseInt(repMap.get("week").toString());
    	String gid = repMap.get("gid").toString();
    	HttpSession session = request.getSession();
    	session.setAttribute("stuIds", stuIds);
    	session.setAttribute("week", week);
    	session.setAttribute("gid", gid);
    	return "200";
    }
    
    
	/**
	 * 批量导出学生课表
	 * oz
	 * @param
	 * @return
	 */
	@RequestMapping("exportStusKB")
	@ResponseBody
	public void exportStuKB(HttpServletRequest request, HttpServletResponse response){
	//public void exportStusKB(@RequestParam String stuIds, @RequestParam Integer week, String gid, HttpServletResponse response) {
		HttpSession session = request.getSession();
		String stuIds = session.getAttribute("stuIds").toString();
    	int week = Integer.parseInt(session.getAttribute("week").toString());
    	String gid = session.getAttribute("gid").toString();
    	session.removeAttribute("stuIds");
		session.removeAttribute("week");
	    session.removeAttribute("gid");
		try {
		  List<Map> dataList = new ArrayList<Map>();
		  Map dataMap = new HashMap();
		  String[] studentIds = stuIds.split(",");
		  for(int i=0; i< studentIds.length - 1; i++){
			  UserEntry userEntry = userService.searchUserId(new ObjectId(studentIds[i+1])); 
			  dataMap = zkbService.getStusKB(userEntry.getUserName(), new ObjectId(studentIds[i+1]), getSchoolId(), week, new ObjectId(gid));
			  if(dataMap != null){
				  dataMap.put("userName", userEntry.getUserName());
				  dataList.add(dataMap);
			  }
		  }
		  zkbService.exportStusKB(response,dataList,getSchoolId());
		} catch (Exception e) {
			 e.printStackTrace();
		}
	}
	
	
	/**
	 * 查看课表模块-学科课表某块-导出学科课表
	 * oz
	 * @param
	 * @return
	 */
	@RequestMapping("/expSubKB")
    @ResponseBody
    public void expSubKB(String xqid,String gradeId,String subjectId, Integer week, HttpServletResponse response) {
        try {
        	//zkbService.getGradeSubjectTeaTableImp(new ObjectId(xqid), gradeId, new ObjectId(subjectId), week,getSchoolId(),response);
        	zkbService.expSubKB(getSchoolId(),new ObjectId(xqid), gradeId, new ObjectId(subjectId), week,getSchoolId(),response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	/**
	 * 查看课表模块-学科课表某块-批量导出学科课表
	 * oz
	 * @param
	 * @return
	 */
	@RequestMapping("exportSubsKB")
	@ResponseBody
	public void exportSubsKB(String xqid,String gradeId,String subjectIds, Integer week, HttpServletResponse response) {
		try {
		  List<Map> dataList = new ArrayList<Map>();
		  Map dataMap = new HashMap();
		  String[] stubjectIds = subjectIds.split(",");
		  for(int i=0; i< stubjectIds.length - 1; i++){
			  //dataMap =  zkbService.getGradeSubjectsKB(new ObjectId(xqid), gradeId, new ObjectId(stubjectIds[i+1]), week,getSchoolId());
			  dataMap =  zkbService.getSubKB(getSchoolId(),new ObjectId(xqid), gradeId, new ObjectId(stubjectIds[i+1]), week,getSchoolId());
			  if(dataMap != null){
				  Map cM = (Map) dataMap.get("cid");
				  String cid = cM.get("cid").toString();
				  N33_KSEntry subEntry = subjectDao.findIsolateSubjectEntryById(new ObjectId(cid),getSchoolId(),new ObjectId(stubjectIds[i+1]),new ObjectId(gradeId));
				  if(subEntry != null){
					  dataMap.put("subjectName", subEntry.getSubjectName());
				  }else{
					  dataMap.put("subjectName", "");
				  }
				  dataList.add(dataMap);
			  }
		  }
		  //导出excel
		  zkbService.expSubKBExcels(dataList,new ObjectId(xqid),gradeId,week,getSchoolId(),response);
		} catch (Exception e) {
			 e.printStackTrace();
		}
	}


    /**
     * 自动排课（非走班）
     * @param gradeId
     * @param xqId
     * @return
     */
    @RequestMapping("/autoPaiKeFeiZouBan")
    @ResponseBody
    public RespObj autoPaiKeFeiZouBan(@RequestParam String gradeId, @RequestParam String xqId, @RequestParam String ciId) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            paiKeService.autoPaiKeFeiZouBan(getSchoolId(), new ObjectId(gradeId), new ObjectId(xqId), new ObjectId(ciId));
            obj.setMessage("排课完成");
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    /**
     * 清除非走班课表
     * @return
     */
    @RequestMapping("/clearData")
    @ResponseBody
    public RespObj clearData(@RequestParam String gradeId, @RequestParam String ciId) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            paiKeService.clearData(getSchoolId(), new ObjectId(gradeId), new ObjectId(ciId));
            obj.setMessage("清除完成");
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

}
