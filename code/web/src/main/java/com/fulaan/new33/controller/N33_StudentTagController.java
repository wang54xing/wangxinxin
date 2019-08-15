package com.fulaan.new33.controller;

import com.fulaan.base.controller.BaseController;
import com.fulaan.new33.dto.N33_StudentTagDTO;
import com.fulaan.new33.service.N33_AutoFenBanService;
import com.fulaan.new33.service.N33_FenBanInfoSetService;
import com.fulaan.new33.service.N33_JXBService;
import com.fulaan.new33.service.N33_StudentTagService;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by albin on 2018/3/29.
 */
@Controller
@RequestMapping("/studentTag")
public class N33_StudentTagController extends BaseController {
    @Autowired
    private N33_StudentTagService tagService;

    @Autowired
    private N33_JXBService jxbService;

    @Autowired
    private N33_AutoFenBanService fenBanService;

    @RequestMapping("/getStudentList")
    @ResponseBody
    public RespObj getStudentList(@RequestBody Map map) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(tagService.getStudentList(map, getSchoolId()));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }


    @RequestMapping("/getStudentListByDingErZouYi")
    @ResponseBody
    public RespObj getStudentListByDingErZouYi(@RequestBody Map map) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(tagService.getStudentListByDingErZouYi(map, getSchoolId()));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    @RequestMapping("/addStudentTag")
    @ResponseBody
    public RespObj addStudentTag(@RequestBody N33_StudentTagDTO dto) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            dto.setJxbIds(new ArrayList<String>());
            dto.setSchoolId(getSchoolId().toString());
            tagService.addStudentTag(dto);
            obj.setMessage("新增成功");
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    @RequestMapping("/conflictDetection")
    @ResponseBody
    public RespObj conflictDetection(String gradeId, String cid) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            tagService.detactConfliction(getSchoolId(), new ObjectId(gradeId), new ObjectId(cid));
            obj.setMessage("创建成功");
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    @RequestMapping("/removeTag")
    @ResponseBody
    public RespObj removeTag(String id) {
        RespObj resp = new RespObj(Constant.SUCCESS_CODE, "删除成功");
        try {
            tagService.removeTag(new ObjectId(id),getSchoolId());
        } catch (Exception e) {
            e.printStackTrace();
            resp = new RespObj(Constant.FAILD_CODE, "删除失败");
        }
        return resp;
    }

    @RequestMapping("/delStudentByTagId")
    @ResponseBody
    public RespObj delStudentByTagId(String tagId, String stuId) {
        RespObj resp = new RespObj(Constant.SUCCESS_CODE, "删除成功");
        try {
            tagService.delStudentByTagId(new ObjectId(tagId), new ObjectId(stuId));
        } catch (Exception e) {
            e.printStackTrace();
            resp = new RespObj(Constant.FAILD_CODE, "删除失败");
        }
        return resp;
    }


    //学生分班
    @RequestMapping("/getJiaoXueBanList")
    @ResponseBody
    public RespObj getJiaoXueBanList(@RequestBody Map map) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(tagService.getJiaoXueBanList(map, getSchoolId()));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    /**
     * 显示标签
     *
     * @param xqid
     * @param gradeId
     * @return
     */
    @RequestMapping("/getStudentViewTagByGradeId")
    @ResponseBody
    public RespObj getStudentViewTagByGradeId(String xqid, String gradeId) {
        RespObj resp = new RespObj(Constant.SUCCESS_CODE, "成功");
        try {
            resp.setMessage(tagService.getStudentViewTagByGradeId(new ObjectId(xqid), new ObjectId(gradeId)));
        } catch (Exception e) {
            e.printStackTrace();
            resp = new RespObj(Constant.FAILD_CODE, "失败");
        }
        return resp;
    }

    @RequestMapping("/TongBuStuByClass")
    @ResponseBody
    public RespObj TongBuStuByClass(String xqid, String gradeId) {
        RespObj resp = new RespObj(Constant.SUCCESS_CODE, "成功");
        try {
            tagService.TongBuStuByClass(new ObjectId(xqid), new ObjectId(gradeId),getSchoolId());
        } catch (Exception e) {
            e.printStackTrace();
            resp = new RespObj(Constant.FAILD_CODE, "失败");
        }
        return resp;
    }

    @RequestMapping("/addStuJinTag")
    @ResponseBody
    public RespObj addStuJinTag(@RequestBody Map map) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            tagService.addStuJinTag(map);
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    @RequestMapping("/getNotTagStudentBySubject")
    @ResponseBody
    public RespObj getNotTagStudentBySubject(String xqid, String gradeId, String subName) {
        RespObj resp = new RespObj(Constant.SUCCESS_CODE, "成功");
        try {
            resp.setMessage(tagService.getNotTagStudentBySubject(subName, new ObjectId(gradeId), new ObjectId(xqid)));
        } catch (Exception e) {
            e.printStackTrace();
            resp = new RespObj(Constant.FAILD_CODE, "失败");
        }
        return resp;
    }

    @RequestMapping("/getTagList")
    @ResponseBody
    public RespObj getTagList(String xqid, String gradeId) {
        RespObj resp = new RespObj(Constant.SUCCESS_CODE, "成功");
        try {
            resp.setMessage(tagService.getTagList(new ObjectId(xqid), new ObjectId(gradeId)));
        } catch (Exception e) {
            e.printStackTrace();
            resp = new RespObj(Constant.FAILD_CODE, "失败");
        }
        return resp;
    }

    @RequestMapping("/getTagListKS")
    @ResponseBody
    public RespObj getTagListKS(String xqid, String gradeId) {
        RespObj resp = new RespObj(Constant.SUCCESS_CODE, "成功");
        try {
            resp.setMessage(tagService.getTagListKS(new ObjectId(xqid), new ObjectId(gradeId), getSchoolId()));
        } catch (Exception e) {
            e.printStackTrace();
            resp = new RespObj(Constant.FAILD_CODE, "失败");
        }
        return resp;
    }

    @RequestMapping("/getStuSign")
    @ResponseBody
    public RespObj getStuSign(String id) {
        RespObj resp = new RespObj(Constant.SUCCESS_CODE, "成功");
        try {
            resp.setMessage(tagService.getStuSign(new ObjectId(id)));
        } catch (Exception e) {
            e.printStackTrace();
            resp = new RespObj(Constant.FAILD_CODE, "失败");
        }
        return resp;
    }

    @RequestMapping("/updateTagView")
    @ResponseBody
    public RespObj updateTagView(@RequestBody Map map) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            tagService.updateTagView(map);
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    @RequestMapping("/updateViewAll")
    @ResponseBody
    public RespObj updateViewAll(String xqid, String gradeId) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            tagService.updateView(new ObjectId(xqid), new ObjectId(gradeId));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    @RequestMapping("/updateView")
    @ResponseBody
    public RespObj updateView(String id) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            tagService.updateView(id);
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    /**
     * 增加学生至教学班
     *
     * @return
     */
    @RequestMapping("/addJxbStudentByTagId")
    @ResponseBody
    public RespObj addJxbStudentByTagId(String jxbId, String tagid) {
        RespObj resp = new RespObj(Constant.SUCCESS_CODE, "成功");
        try {
            resp.setMessage(tagService.addJxbStudentByTagId(new ObjectId(jxbId), new ObjectId(tagid), getDefaultPaiKeTerm().getPaikeci()));
        } catch (Exception e) {
            e.printStackTrace();
            resp = new RespObj(Constant.FAILD_CODE, "失败");
        }
        return resp;
    }

    /***
     *
     */
    @RequestMapping("/getStudentListByNoAdd")
    @ResponseBody
    public RespObj getStudentListByNoAdd(String xqid, String classId, String jxbid) {
        RespObj resp = new RespObj(Constant.SUCCESS_CODE, "成功");
        try {
            resp.setMessage(tagService.getStudentListByNoAdd(new ObjectId(xqid), new ObjectId(classId), new ObjectId(jxbid)));
        } catch (Exception e) {
            e.printStackTrace();
            resp = new RespObj(Constant.FAILD_CODE, "失败");
        }
        return resp;
    }

    @RequestMapping("/getStudentListByIsNotTag")
    @ResponseBody
    public RespObj getStudentListByIsNotTag(String xqid, String classId, String jxbid) {
        RespObj resp = new RespObj(Constant.SUCCESS_CODE, "成功");
        try {
            resp.setMessage(tagService.getStudentListByIsNotTag(new ObjectId(xqid), new ObjectId(classId), new ObjectId(jxbid)));
        } catch (Exception e) {
            e.printStackTrace();
            resp = new RespObj(Constant.FAILD_CODE, "失败");
        }
        return resp;
    }

    /**
     * 增加单个学生
     *
     * @param uid
     * @param jxbid
     * @return
     */
    @RequestMapping("/addStu")
    @ResponseBody
    public RespObj addStu(String uid, String jxbid) {
        RespObj resp = new RespObj(Constant.SUCCESS_CODE, "成功");
        try {
            resp.setMessage(tagService.addStu(new ObjectId(jxbid), new ObjectId(uid), getDefaultPaiKeTerm().getPaikeci()));
        } catch (Exception e) {
            e.printStackTrace();
            resp = new RespObj(Constant.FAILD_CODE, "失败");
        }
        return resp;
    }

    @RequestMapping("/delStu")
    @ResponseBody
    public RespObj delStu(String uid, String jxbid) {
        RespObj resp = new RespObj(Constant.SUCCESS_CODE, "成功");
        try {
            resp.setMessage(tagService.delStu(new ObjectId(jxbid), new ObjectId(uid), getDefaultPaiKeTerm().getPaikeci()));
        } catch (Exception e) {
            e.printStackTrace();
            resp = new RespObj(Constant.FAILD_CODE, "失败");
        }
        return resp;
    }

    /**
     * 根据tag删除学生
     *
     * @param tagid
     * @param jxbid
     * @return
     */
    @RequestMapping("/removeTagByBan")
    @ResponseBody
    public RespObj removeTagByBan(String tagid, String jxbid) {
        RespObj resp = new RespObj(Constant.SUCCESS_CODE, "成功");
        try {
            resp.setMessage(tagService.removeTagByBan(new ObjectId(jxbid), new ObjectId(tagid), getDefaultPaiKeTerm().getPaikeci()));
        } catch (Exception e) {
            e.printStackTrace();
            resp = new RespObj(Constant.FAILD_CODE, "失败");
        }
        return resp;
    }
    //单双周

    @RequestMapping("/setClassListDanShuangZhou")
    @ResponseBody
    public RespObj setClassListDanShuangZhou(@RequestBody Map map) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(jxbService.setClassListDanShuangZhou(map));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }

    @RequestMapping("/setClassListDanShuangZhou1")
    @ResponseBody
    public RespObj setClassListDanShuangZhou1(@RequestBody Map map) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(jxbService.setClassListDanShuangZhou1(map));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }


    @RequestMapping("/getClassListDanShuangZhou")
    @ResponseBody
    public RespObj getClassListDanShuangZhou(@RequestBody Map map) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(jxbService.getClassListDanShuangZhou(map));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }


    @RequestMapping("/HuanDanShuang")
    @ResponseBody
    public RespObj HuanDanShuang(String dan, String shuang) {
        RespObj resp = new RespObj(Constant.SUCCESS_CODE, "成功");
        try {
            jxbService.HuanDanShuang(new ObjectId(dan), new ObjectId(shuang));
        } catch (Exception e) {
            e.printStackTrace();
            resp = new RespObj(Constant.FAILD_CODE, "失败");
        }
        return resp;
    }

    @RequestMapping("/updateDanShuang")
    @ResponseBody
    public RespObj updateDanShuang(String xqid, String dan, String shuang, String cid) {
        RespObj resp = new RespObj(Constant.SUCCESS_CODE, "成功");
        try {
            resp.setMessage(jxbService.updateDanShuang(new ObjectId(xqid), new ObjectId(dan), new ObjectId(shuang), new ObjectId(cid)));
        } catch (Exception e) {
            e.printStackTrace();
            resp = new RespObj(Constant.FAILD_CODE, "失败");
        }
        return resp;
    }

    @RequestMapping("/removeDanShuang")
    @ResponseBody
    public RespObj removeDanShuang(String xqid, String dan, String shuang, String cid) {
        RespObj resp = new RespObj(Constant.SUCCESS_CODE, "成功");
        try {
            jxbService.removeDanShuang(new ObjectId(xqid), new ObjectId(dan), new ObjectId(shuang), new ObjectId(cid));
        } catch (Exception e) {
            e.printStackTrace();
            resp = new RespObj(Constant.FAILD_CODE, "失败");
        }
        return resp;
    }


    @RequestMapping("/AutoTagList")
    @ResponseBody
    public RespObj AutoTagList(String xqid, String gradeId) {
        RespObj resp = new RespObj(Constant.SUCCESS_CODE, "成功");
        try {
            resp.setMessage(fenBanService.autoFenBanPingTag(getSchoolId(), new ObjectId(xqid), new ObjectId(gradeId)));
        } catch (Exception e) {
            e.printStackTrace();
            resp = new RespObj(Constant.FAILD_CODE, "失败");
        }
        return resp;
    }

    @RequestMapping("/cleanUpTagInfos")
    @ResponseBody
    public RespObj cleanUpTagInfos(String xqid, String gradeId) {
        RespObj resp = new RespObj(Constant.SUCCESS_CODE, "成功");
        try {
            fenBanService.cleanUpTagInfos(getSchoolId(), new ObjectId(xqid), new ObjectId(gradeId));
        } catch (Exception e) {
            e.printStackTrace();
            resp = new RespObj(Constant.FAILD_CODE, "失败");
        }
        return resp;
    }

    @RequestMapping("/delStudentByJxb")
    @ResponseBody
    public RespObj delStudentByJxb(String studentId, String jxbId) {
        RespObj resp = new RespObj(Constant.SUCCESS_CODE, "成功");
        try {
            tagService.updateJxb(new ObjectId(studentId), new ObjectId(jxbId));
        } catch (Exception e) {
            e.printStackTrace();
            resp = new RespObj(Constant.FAILD_CODE, "失败");
        }
        return resp;
    }


    @RequestMapping("/getJxbByStudentSubject")
    @ResponseBody
    public RespObj delStudentByJxb(String combiname, String gradeId, String ciId, String cid, Integer fabu) {
        RespObj resp = new RespObj(Constant.SUCCESS_CODE, "成功");
        try {
            resp.setMessage(jxbService.getJxbByStudentSubject(combiname, new ObjectId(gradeId), new ObjectId(ciId), getSchoolId(), new ObjectId(cid), fabu));
        } catch (Exception e) {
            e.printStackTrace();
            resp = new RespObj(Constant.FAILD_CODE, "失败");
        }
        return resp;
    }


    @RequestMapping("/addStudentByXingZheng")
    @ResponseBody
    public RespObj addStudentByXingZheng(String gradeId, String ciId, String cid, String userId) {
        RespObj resp = new RespObj(Constant.SUCCESS_CODE, "成功");
        try {
            resp.setMessage(jxbService.addStudentByXingZheng(new ObjectId(cid), new ObjectId(ciId), new ObjectId(userId), new ObjectId(gradeId)));
        } catch (Exception e) {
            e.printStackTrace();
            resp = new RespObj(Constant.FAILD_CODE, "失败");
        }
        return resp;
    }

    @RequestMapping("/addStudentZhuanXiangKe")
    @ResponseBody
    public RespObj addStudentZhuanXiangKe(String gradeId, String ciId,String cid) {
        RespObj resp = new RespObj(Constant.SUCCESS_CODE, "成功");
        try {
            resp.setMessage(jxbService.addStudentZhuanXiangKe(getSchoolId(),new ObjectId(ciId), new ObjectId(gradeId),new ObjectId(cid)));
        } catch (Exception e) {
            e.printStackTrace();
            resp = new RespObj(Constant.FAILD_CODE, "失败");
        }
        return resp;
    }

    @RequestMapping("/addStudentByJxb")
    @ResponseBody
    public RespObj addStudentByJxb(@RequestBody Map map) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            ObjectId userId = new ObjectId((String) map.get("uid"));
            List<String> jxbIds = (List<String>) map.get("jxbIds");
            List<ObjectId> jIds = MongoUtils.convertToObjectIdList(jxbIds);
            jxbService.addStudentByJxb(userId, jIds);
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }
}
