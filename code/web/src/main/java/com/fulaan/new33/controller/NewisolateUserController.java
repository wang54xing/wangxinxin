package com.fulaan.new33.controller;


import com.alibaba.fastjson.JSON;
import com.db.new33.isolate.N33_StudentDao;
import com.fulaan.annotation.UserDataCollection;
import com.fulaan.folder.dto.DirDTO;
import com.fulaan.new33.dto.isolate.*;
import com.fulaan.new33.service.isolate.*;
import com.fulaan.utils.RESTAPI.bo.new33.isolate.isolateAPI;
import com.pojo.new33.isolate.StudentEntry;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fulaan.base.controller.BaseController;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/new33isolateMange")
public class NewisolateUserController extends BaseController {
    @Autowired
    private IsolateTermService termService;
    @Autowired
    private IsolateUserService userService;
    @Autowired
    private IsolateSubjectService subjectService;

    @Autowired
    private IsolateGradeService gradeService;

    @Autowired
    private N33_SWService swService;

    @Autowired
    private N33_StudentService studentService;

    private N33_StudentDao studentDao = new N33_StudentDao();

//    @Autowired
//    private DeleteCheckService deleteCheckService ;

    @RequestMapping(value = "/base")
    public String isolate() {
        return "/isolate/isolateEduManage";
    }


    @RequestMapping("/getIsolate")
    @ResponseBody
    public RespObj getIsolate() {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            if (termService.getCount(getSchoolId()) == 0) {
                termService.addIsolateTermEntrys(getSchoolId());
                obj.setMessage("ok");
            } else {
                obj.setMessage("no");
            }

        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }


    @RequestMapping("/getIsolateList")
    @ResponseBody
    public RespObj getIsolateList(@RequestParam String xqid
    ) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
//            int count = gradeService.getCountByXqid(new ObjectId(xqid), getSchoolId());
            gradeService.getIsolateGradeBySid(getSchoolId(), new ObjectId(xqid));
            obj.setMessage("ok");
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    /**
     * f 没法  t 发了
     *
     * @param ciId
     * @return
     */
    @RequestMapping("/getCiIdIsFaBu")
    @ResponseBody
    public RespObj getCiIdIsFaBu(
            @RequestParam String ciId
    ) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(termService.getCiIdIsFaBu(new ObjectId(ciId)));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    @RequestMapping("/getXqIdIsFaBu")
    @ResponseBody
    public RespObj getXqIdIsFaBu(
            @RequestParam String xqid
    ) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(termService.getXqIdIsFaBu(new ObjectId(xqid)));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }


    //选择学期
    @RequestMapping("/getTermList")
    @ResponseBody
    public RespObj getTermList(
    ) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(termService.getTermList(getSchoolId()));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    //选择学期
    @RequestMapping("/getYearList")
    @ResponseBody
    public RespObj getYearList(
    ) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(termService.getYearList(getSchoolId()));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    //选择学期
    @RequestMapping("/getAllTermTimesList")
    @ResponseBody
    public RespObj getAllTermTimesList(
    ) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(termService.getAllTermList(getSchoolId()));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    //选择学期
    @RequestMapping("/getTermPaikeTimes")
    @ResponseBody
    public RespObj getTermPaikeTimes(
    ) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(termService.getTermPaikeTimes(getSchoolId()));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    //选择年级
    @RequestMapping("/getGradList")
    @ResponseBody
    public RespObj getGradList(@RequestParam String xqid) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(gradeService.getGradeListByXqid(new ObjectId(xqid), getSchoolId()));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }
    //选择年级
    @RequestMapping("/getNewGradList")
    @ResponseBody
    public RespObj getNewGradList(@RequestParam String xqid) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(gradeService.getNewGradeListByXqid(new ObjectId(xqid), getSchoolId()));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    //选择年级
    @RequestMapping("/getGradListBySid")
    @ResponseBody
    public RespObj getGradListBySid() {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(gradeService.getGradeListByXqid(getSchoolId()));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    //选择年级
    @RequestMapping("/getGradListOrigin")
    @ResponseBody
    public RespObj getGradList() {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            List<Map<String, Object>> list = gradeService.getGradeList(getSchoolId());
            obj.setMessage(list);
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
    @RequestMapping("/getDefWeek")
    @ResponseBody
    public RespObj getDefWeek() {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(termService.getDateByOr(getSchoolId().toString()));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }


    //选择年级
    @RequestMapping("/getCurrentGradList")
    @ResponseBody
    public RespObj getCurrentGradList(
    ) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(gradeService.getGradeListByXqid(getSchoolId()));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    /**
     * 查询对应年级的学科课时
     *
     * @param xqid
     * @param gradeId
     * @return
     */
    @UserDataCollection(pageTag = "n33", params = {"gradeId"}, isAllCollection = false)
    @RequestMapping("/getSubjectByGradeId")
    @ResponseBody
    public RespObj getSubjectByGradeId(@RequestParam String xqid, @RequestParam String gradeId) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(subjectService.getIsolateSubjectByGradeId(new ObjectId(xqid), getSchoolId(), gradeId));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    /**
     * 批量新增修改
     *
     * @param dtos
     * @return
     */
    @RequestMapping("/addSubjectList")
    @ResponseBody
    public RespObj addSubjectList(@RequestBody List<N33_KSDTO> dtos, @RequestParam String gid) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        if (dtos.size() == 0) {
            subjectService.delSubject(getSchoolId(), getDefaultPaiKeTerm().getPaikeci(), new ObjectId(gid));
            obj.setMessage("操作成功");
            return obj;
        }
        try {
            List<String> stbStr = new ArrayList<String>();
            List<N33_KSDTO> stbList = new ArrayList<N33_KSDTO>();
            for (N33_KSDTO dto : dtos) {
                if (dto.getId().equals("*")) {
                    dto.setId(new ObjectId().toString());
                }
                dto.setSid(getSchoolId().toString());
                if (!stbStr.contains(dto.getSubid())) {
                    stbList.add(dto);
                    stbStr.add(dto.getSubid());
                } else {
                    //逻辑处理课时
                    for (N33_KSDTO sub : stbList) {
                        if (sub.getSubid().toString().equals(dto.getSubid())) {
                            sub.setdTime(dto.getTime());
                            sub.setType1(dto.getType());
                        }
                    }
                }
            }
            subjectService.addIsolateSubjectList(stbList);
            obj.setMessage("新增成功");
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }


    @RequestMapping("/updateDanShuangZhou")
    @ResponseBody
    public RespObj updateDanShuangZhou(@RequestParam String id, @RequestParam Integer ty, @RequestParam Integer type) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            subjectService.updateDanShuangZhou(id, ty, type);
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    @RequestMapping("/updateZhuanDan")
    @ResponseBody
    public RespObj updateZhuanDan(@RequestParam String id, @RequestParam Integer dan) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            subjectService.updateZhuanDan(id, dan);
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    /**
     * 加载全部学科 从2.0取
     *
     * @return
     */
    @RequestMapping("/selSubjectList")
    @ResponseBody
    public RespObj selSubjectList() {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            String res = isolateAPI.selSubjectList(getSchoolId().toString());
            obj = JSON.parseObject(res,RespObj.class);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

    //新增老师

    /**
     * 批量新增修改
     *
     * @param dto
     * @return
     */
    @RequestMapping("/addTeaDto")
    @ResponseBody
    public RespObj addTeaDto(@RequestBody N33_TeaDTO dto) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            if (dto.getId().equals("*")) {
                dto.setId(new ObjectId().toString());
            }
            if (dto.getUid().equals("*")) {
                dto.setUid(new ObjectId().toString());
            }
            dto.setSid(getSchoolId().toString());
            userService.addIsolateTeaOrUpdate(dto);
            obj.setMessage("新增成功");
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    @RequestMapping("/getTeaListSub")
    @ResponseBody
    public RespObj getTeaList(@RequestBody Map map) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(userService.getTeaByGradeAndSubject(map, getSchoolId()));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    @RequestMapping("/addTeaDtoList")
    @ResponseBody
    public RespObj addTeaDtoList(@RequestBody List<N33_TeaDTO> dtos, @RequestParam String gid) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            for (N33_TeaDTO dto : dtos) {
                if (dto.getId().equals("*")) {
                    dto.setId(new ObjectId().toString());
                }
                if (dto.getUid().equals("*")) {
                    dto.setUid(new ObjectId().toString());
                }
                dto.setSid(getSchoolId().toString());
            }
            userService.addIsolateTeaList(dtos, new ObjectId(gid), getSchoolId(), getDefaultPaiKeTerm().getPaikeci());
            obj.setMessage("新增成功");
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    /**
     * 加载单一学科
     *
     * @param xqid
     * @param gid
     * @param subid
     * @return
     */
    @RequestMapping("/getIsolateSubjectByGradeId")
    @ResponseBody
    public RespObj getIsolateSubjectByGradeId(@RequestParam String xqid, @RequestParam String gid, @RequestParam String subid) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(subjectService.getIsolateSubjectByGradeId(new ObjectId(xqid), getSchoolId(), new ObjectId(gid), new ObjectId(subid)));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    @RequestMapping("/getGradeListByXqidList")
    @ResponseBody
    public List<GradeDTO> getGradeListByXqidList(@RequestParam String xqid) {
        List<GradeDTO> gradeList = gradeService.getGradeListByXqidList(new ObjectId(xqid), getSchoolId());
        return gradeList;
    }

    /**
     * 加载学期的年级学科树
     *
     * @param xqid
     * @return
     */
    @RequestMapping("/getSubjectListByXqId")
    @ResponseBody
    public List<DirDTO> getSubjectListByXqId(@RequestParam String xqid) {
        List<DirDTO> dtos = new ArrayList<DirDTO>();
        try {
            List<GradeDTO> gradeList = gradeService.getGradeListByXqidList(new ObjectId(xqid), getSchoolId());
            for (GradeDTO dto : gradeList) {
                DirDTO dirDTO = new DirDTO();
                dirDTO.setId(dto.getGid());
                dirDTO.setParentId("-1");
                dirDTO.setName(dto.getGnm());
                dirDTO.setOpen(true);
                dirDTO.setSort(0);
                dtos.add(dirDTO);
            }
            List<N33_KSDTO> n33Ksdtos = subjectService.getIsolateSubjectListByList(new ObjectId(xqid), getSchoolId());
            for (N33_KSDTO dto : n33Ksdtos) {
                DirDTO dirDTO = new DirDTO();
                dirDTO.setId(dto.getSubid());
                dirDTO.setParentId(dto.getGid());
                dirDTO.setName(dto.getSnm());
                dirDTO.setOpen(true);
                dirDTO.setSort(0);
                dtos.add(dirDTO);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dtos;
    }


    /**
     * 加载学期的年级学科树
     *
     * @param cid
     * @return
     */
    @RequestMapping("/getSubjectListByCid")
    @ResponseBody
    public List<DirDTO> getSubjectListByCid(@RequestParam String cid) {
        List<DirDTO> dtos = new ArrayList<DirDTO>();
        try {
            List<GradeDTO> gradeList = gradeService.getGradeListByCid(new ObjectId(cid), getSchoolId());
            for (GradeDTO dto : gradeList) {
                DirDTO dirDTO = new DirDTO();
                dirDTO.setId(dto.getGid());
                dirDTO.setParentId("-1");
                dirDTO.setName(dto.getGnm());
                dirDTO.setOpen(true);
                dirDTO.setSort(0);
                dtos.add(dirDTO);
            }
            List<N33_KSDTO> n33Ksdtos = subjectService.getIsolateSubjectListByCid(new ObjectId(cid), getSchoolId());
            for (N33_KSDTO dto : n33Ksdtos) {
                DirDTO dirDTO = new DirDTO();
                dirDTO.setId(dto.getSubid());
                dirDTO.setParentId(dto.getGid());
                dirDTO.setName(dto.getSnm());
                dirDTO.setOpen(true);
                dirDTO.setSort(0);
                dtos.add(dirDTO);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dtos;
    }

    /**
     * 加载老师数据
     *
     * @param xqid
     * @param gid
     * @param subid
     * @return
     */
    @RequestMapping("/getTeaList")
    @ResponseBody
    public RespObj getTeaList(@RequestParam String xqid, @RequestParam String gid, @RequestParam String subid, @RequestParam(defaultValue = "*") String name) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(userService.getTeaByGradeAndSubject(new ObjectId(xqid), getSchoolId(), gid, subid, name));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    @RequestMapping("/getTeaListKS")
    @ResponseBody
    public RespObj getTeaListKS(@RequestParam String xqid, @RequestParam String gid, @RequestParam String subid, @RequestParam(defaultValue = "*") String name) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(userService.getTeaByGradeAndSubjectCount(new ObjectId(xqid), getSchoolId(), gid, subid, name));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }


    /**
     * 加载老师数据
     *
     * @param xqid
     * @param gid
     * @param subid
     * @return
     */
    @RequestMapping("/getTeaListByXQID")
    @ResponseBody
    public RespObj getTeaListByXQID(@RequestParam String xqid, @RequestParam String gid, @RequestParam String subid, @RequestParam(defaultValue = "*") String name,@RequestParam Integer week) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(userService.getTeaByGradeAndSubjectByXQID(new ObjectId(xqid), getSchoolId(), gid, subid, name,week));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    @RequestMapping("/getTeaListByXqid")
    @ResponseBody
    public RespObj getTeaListByXqid(@RequestParam String xqid, @RequestParam String gid, @RequestParam String subid, @RequestParam(defaultValue = "*") String name) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(userService.getTeaListByXqid(new ObjectId(xqid), getSchoolId(), gid, subid, name));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }


    @RequestMapping("/getTea")
    @ResponseBody
    public RespObj getTea(@RequestParam String id, @RequestParam String gid) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(userService.getTea(new ObjectId(id), new ObjectId(gid)));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    @RequestMapping("/deleteTea")
    @ResponseBody
    public RespObj deleteTea(@RequestParam String id) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            userService.deleteTea(new ObjectId(id));
            obj.setMessage("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    /**
     * 加载2.0对应学科老师
     *
     * @return
     */
    @RequestMapping("/get2SubjectList")
    @ResponseBody
    public RespObj get2SubjectList(@RequestParam String subid) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        String res = "";
        try {
            res = isolateAPI.getSubjectEntry(subid);
            obj = JSON.parseObject(res,RespObj.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

    @RequestMapping("/getSwTypeListByXqid")
    @ResponseBody
    public RespObj getSwTypeListByXqid(@RequestParam String xqid) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            List<N33_SWLBDTO> en = swService.getSwLbDtoByXqid(getSchoolId(), new ObjectId(xqid));
            if (en.size() > 0) {
                obj.setMessage(en);
            } else {
                swService.addN33_SwLbList(getSchoolId(), new ObjectId(xqid));
                en = swService.getSwLbDtoByXqid(getSchoolId(), new ObjectId(xqid));
                obj.setMessage(en);
            }
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    /**
     * 新增/编辑事务
     *
     * @param dto
     * @return
     */
    @RequestMapping("/addSwDto")
    @ResponseBody
    public RespObj addSwDto(@RequestBody Map dto) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            ObjectId reId = new ObjectId();
            List<Integer> xIndexs = (List<Integer>) dto.get("xindex");
            List<Integer> yIndexs = (List<Integer>) dto.get("yindex");
            List<N33_SWDTO> swdtos = new ArrayList<N33_SWDTO>();
            String id = (String) dto.get("id");
            String swlbId = (String) dto.get("swlbId");
            String termId = (String) dto.get("termId");
            String desc = (String) dto.get("desc");
            Integer lev = Integer.parseInt((String) dto.get("level"));
            Integer sk = Integer.parseInt((String) dto.get("sk"));
            List<String> teaList = (List<String>) dto.get("tealist");
            List<N33_SWDTO> swdto1 = new ArrayList<N33_SWDTO>();
            //如果存在事务id，删除该事务，重新创建
            if (!id.equals("*")) {
                N33_SWDTO swdto = swService.getSwId(new ObjectId(id));
                swService.removeSw(new ObjectId(id));
                swdto1 = swService.getSw(new ObjectId(swdto.getReId()));
                if (xIndexs.size() > 0) {
                    id = "*";
                }
            }
            //可能存在多个时间节点，所以创建多条事务
            for (Integer i = 0; i < xIndexs.size(); i++) {
                N33_SWDTO sw = new N33_SWDTO(id, xIndexs.get(i), yIndexs.get(i), swlbId, desc, lev, 0L, 0L, termId, getSchoolId().toString(), teaList);
                sw.setSk(sk);
                sw.setReId(reId.toHexString());
                swdtos.add(sw);
            }
            if (swdto1.size() > 0) {
                for (N33_SWDTO swdto : swdto1) {
                    N33_SWDTO sw = new N33_SWDTO(swdto.getId(), swdto.getX(), swdto.getY(), swlbId, desc, lev, 0L, 0L, termId, getSchoolId().toString(), teaList);
                    sw.setSk(sk);
                    sw.setReId(reId.toHexString());
                    swService.updateSw(sw);
                }
            }
            //新增事务
            swService.addSwEntryList(swdtos);
            obj.setMessage("新增成功");
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    /**
     * 加载对应类型对应学期的事务
     *
     * @param xqid
     * @param typeId
     * @return
     */
    @RequestMapping("/getSwByXqidAndType")
    @ResponseBody
    public RespObj getSwByXqidAndType(@RequestParam String xqid, @RequestParam String typeId) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(swService.getSwByXqidAndType(new ObjectId(xqid), new ObjectId(typeId), getSchoolId()));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }


    /**
     * 加载老师学期的事务
     *
     * @param xqid
     * @param uid
     * @return
     */
    @RequestMapping("/getSwByXqidAndUserId")
    @ResponseBody
    public RespObj getSwByXqidAndUserId(@RequestParam String xqid, @RequestParam String uid) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(swService.getSwByXqidAndUserId(new ObjectId(xqid), new ObjectId(uid)));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    /**
     * 加载某个事物属性
     */
    @RequestMapping("/getSwById")
    @ResponseBody
    public RespObj getSwById(@RequestParam String id, @RequestParam String cid) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(swService.getSwById(new ObjectId(id), new ObjectId(cid)));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    /**
     * 删除某个事物属性
     */
    @RequestMapping("/delSwById")
    @ResponseBody
    public RespObj delSwById(@RequestParam String id) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            N33_SWDTO swdto = swService.getSwId(new ObjectId(id));
            swService.delSwById(new ObjectId(id));
//            List<N33_SWDTO> swdto1 = swService.getSw(new ObjectId(swdto.getReId()));
//            for (N33_SWDTO swdto2 : swdto1) {
//                swService.delSwById(new ObjectId(swdto2.getId()));
//            }
            obj.setMessage("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    @RequestMapping("/getSwTeaList")
    @ResponseBody
    public RespObj getSwTeaList(@RequestParam String id) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage( swService.getSwTeaList(new ObjectId(id)));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    @RequestMapping("/getIsolateSubjectListByXq")
    @ResponseBody
    public RespObj getIsolateSubjectListByXq(@RequestParam String xqid) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(subjectService.getIsolateSubjectListByXq(new ObjectId(xqid), getSchoolId()));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    @RequestMapping("/getIsolateSubjectListByXqAndGrade")
    @ResponseBody
    public RespObj getIsolateSubjectListByXqAndGrade(@RequestParam String xqid, @RequestParam String gid) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(subjectService.getIsolateSubjectListByXqAndGrade(new ObjectId(xqid), getSchoolId(), new ObjectId(gid)));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    @RequestMapping("/getTeaAndSubjectByGradeId")
    @ResponseBody
    public RespObj getTeaAndSubjectByGradeId(@RequestParam String xqid, @RequestParam String gradeId, @RequestParam String subId) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(userService.getTeaAndSubjectByGradeId(new ObjectId(xqid), getSchoolId(), new ObjectId(gradeId), new ObjectId(subId)));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    @RequestMapping("/getTeaAndSubjectByGradeId1")
    @ResponseBody
    public RespObj getTeaAndSubjectByGradeId1(@RequestParam String xqid, @RequestParam String gradeId, @RequestParam String subId) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(userService.getTeaAndSubjectByGradeId1(new ObjectId(xqid), getSchoolId(), new ObjectId(gradeId), new ObjectId(subId)));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    //周课表使用
    @RequestMapping("/getIsolateSubjectListByZKB")
    @ResponseBody
    public RespObj getIsolateSubjectListByZKB(@RequestParam String xqid) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(subjectService.getIsolateSubjectListByZKB(new ObjectId(xqid), getSchoolId()));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    @RequestMapping("/getIsolateSubjectListByZKBGrade")
    @ResponseBody
    public RespObj getIsolateSubjectListByZKBGrade(@RequestParam String xqid, @RequestParam String gradeId) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(subjectService.getIsolateSubjectListByZKB(new ObjectId(xqid), getSchoolId(), gradeId));
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
    @RequestMapping("/getGuDingShiWuByXqid")
    @ResponseBody
    public RespObj getGuDingShiWuByXqid(@RequestParam String xqid) {
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

    /**
     * 查询某个学期的所有老師事务
     */
    @RequestMapping("/getTeaShiWuByXqid")
    @ResponseBody
    public RespObj getTeaShiWuByXqid(@RequestParam String xqid, @RequestParam String teaId) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(swService.getSwByXqidAndUserId(new ObjectId(xqid), new ObjectId(teaId)));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    /**
     * 查询对应年级的学科课时
     *
     * @param xqid
     * @param gid
     * @return
     */
    @RequestMapping("/getSubjectByGradeIdType")
    @ResponseBody
    public RespObj getSubjectByGradeIdType(@RequestParam String xqid, @RequestParam String gid, @RequestParam int type) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(subjectService.getIsolateSubjectByGradeId(new ObjectId(xqid), getSchoolId(), gid, type));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    /**
     * 同步老师
     *
     * @param cid
     * @param ncid
     * @return
     */
    @RequestMapping("/IsolateTeaListByNewCi")
    @ResponseBody
    public RespObj IsolateTeaListByNewCi(@RequestParam String cid, @RequestParam String ncid) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            userService.IsolateTeaListByNewCi(new ObjectId(cid), new ObjectId(ncid));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }


    /**
     * *
     * 学科
     *
     * @param cid
     * @param ncid
     * @return
     */
    @RequestMapping("/IsolateSubListByNewCi")
    @ResponseBody
    public RespObj IsolateSubListByNewCi(@RequestParam String cid, @RequestParam String ncid) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            subjectService.IsolateSubListByNewCi(new ObjectId(cid), new ObjectId(ncid));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    /**
     * 查询没有在教学班中的学生
     *
     * @param studentName
     * @param gid
     * @param ciId
     * @return
     */
    @RequestMapping("/getStudentByStudentName")
    @ResponseBody
    public RespObj getStudentByStudentName(@RequestParam String studentName, @RequestParam String gid, @RequestParam String ciId) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(studentService.getStudentByStudentName(new ObjectId(ciId), new ObjectId(gid), studentName, getSchoolId()));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    @RequestMapping("/getStuDto")
    @ResponseBody
    public RespObj getStuDto(@RequestParam String ciId) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            StudentEntry entry = studentDao.findStudent(new ObjectId(ciId), getUserId());
            obj.setMessage(new StudentDTO(entry));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    @RequestMapping("/getUserId")
    @ResponseBody
    public RespObj getUserIds() {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(getUserId().toString());
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }
}
