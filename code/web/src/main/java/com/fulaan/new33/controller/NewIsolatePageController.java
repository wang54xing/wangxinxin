package com.fulaan.new33.controller;

import com.db.new33.isolate.N33_ManagerDao;
import com.fulaan.base.controller.BaseController;
import com.pojo.new33.isolate.N33_ManagerEntry;
import com.pojo.user.UserRole;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RequestMapping("/newisolatepage")
@Controller
public class NewIsolatePageController extends BaseController {

    private N33_ManagerDao managerDao = new N33_ManagerDao();

    @RequestMapping("/stuRelation")
    public String stuRelation(Model model) {
        getAdmin();
        model.addAttribute("type", 9);
        return "/new33/stuRelation";
    }

    @RequestMapping("/jxbcombine")
    public String jxbcombine(Model model) {
        getAdmin();
        model.addAttribute("type", 2);
        return "/new33/jxbcombine";
    }

    @RequestMapping("/jxbTimeCombine")
    public String jxbTimeCombine(Model model) {
        getAdmin();
        model.addAttribute("type", 2);
        return "/new33/jxbTimeCombine";
    }

    @RequestMapping("/jxbAutoTimeCombine")
    public String jxbAutoTimeCombine(
            String gradeId,
            Model model
    ) {
        getAdmin();
        model.addAttribute("type", 3);
        model.addAttribute("gradeId", gradeId);
        return "/new33/jxbAutoTimeCombine";
    }

    @RequestMapping("/autoTimeCombKeBiao")
    public String autoTimeCombKeBiao(
            String gradeId,
            Model model
    ) {
        getAdmin();
        model.addAttribute("type", 3);
        model.addAttribute("gradeId", gradeId);
        return "/new33/autoTimeCombKeBiao";
    }


    @RequestMapping("/stuSign")
    public String stuSign(Model model) {
        getAdmin();
        model.addAttribute("type", 9);
        return "/new33/stuSign";
    }

    @RequestMapping("/jxbRelation")
    public String jxbRelation(Model model) {
        getAdmin();
        model.addAttribute("type", 9);
        return "/new33/jxbRelation";
    }

    @RequestMapping("/allTable")
    public String allTable(Model model) {
        getAdmin();
        model.addAttribute("type", 5);
        return "/new33/allTable";
    }

    @RequestMapping("/placementSet")
    public String placementSet(Model model) {
        getAdmin();
        model.addAttribute("type", 9);
        return "/new33/placementSet";
    }

    @RequestMapping("/set")
    public String set(Model model) {
        getAdmin();
        model.addAttribute("type", 6);
        return "/new33/set";
    }
    @RequestMapping("/setMm")
    public String setMm(Model model) {
        getAdmin();
        model.addAttribute("type", 6);
        return "/new33/setMm";
    }

    @RequestMapping("/jSubCLassTime")
    public String jSubCLassTime(Model model) {
        getAdmin();
        model.addAttribute("type", 5);
        return "/new33/jSubCLassTime";
    }

    @RequestMapping("/pStuClassTime")
    public String pStuClassTime(Model model) {
        getAdmin();
        model.addAttribute("type", 5);
        return "/new33/pStuClassTime";
    }

    @RequestMapping("/pStuClassTimeStu")
    public String pStuClassTimeStu(Model model) {
        getAdmin();
        model.addAttribute("type", 5);
        return "/new33/pStuClassTimeStu";
    }

    @RequestMapping("/teaCLassTime")
    public String teaCLassTime(Model model) {
        getAdmin();
        model.addAttribute("type", 3);
        return "/new33/teaCLassTime";
    }

    @RequestMapping("/pTeaClassTime")
    public String pTeaClassTime(Model model) {
        getAdmin();
        model.addAttribute("type", 5);
        return "/new33/pTeaClassTime";
    }

    @RequestMapping("/subTable")
    public String subTable(Model model) {
        getAdmin();
        model.addAttribute("type", 5);
        return "/new33/subTable";
    }

    @RequestMapping("/MyTimetable")
    public String MyTimetable(Model model) {
        getAdmin();
        model.addAttribute("type", 1);
        return "/new33/MyTimetable";
    }

    @RequestMapping("/MyClassTi")
    public String MyClassTi(Model model) {
        getAdmin();
        model.addAttribute("type", 3);
        return "/new33/MyClassTi";
    }

    @RequestMapping("/MyClass")
    public String MyClass(HttpServletRequest request, Model model) {
        getAdmin();
        String jxbId = request.getParameter("jxbId");
        model.addAttribute("jxbId", jxbId);
        model.addAttribute("type", 2);
        return "/new33/MyClass";
    }

    @RequestMapping("/classpage")
    public String classPage(Model model) {
        getAdmin();
        model.addAttribute("type", 7);
        return "/new33/baseClassroom";
    }

    @RequestMapping("/courseRangePage")
    public String courseRangePage(Model model) {
        getAdmin();
        model.addAttribute("type", 7);
        return "/new33/baseTable";
    }

    @RequestMapping("/classManage")
    public String classManage(Model model) {
        getAdmin();
        model.addAttribute("type", 8);
        return "/new33/stuGrade";
    }

    @RequestMapping("/studentManage")
    public String studentManage(Model model) {
        getAdmin();
        model.addAttribute("type", 8);
        return "/new33/stuAdmini";
    }

    @RequestMapping("/baseTeacher")
    public String baseTeacher(Model model) {
        getAdmin();
        model.addAttribute("type", 7);
        return "/new33/baseTeacher";
    }

    @RequestMapping("/classTime")
    public String classTime(Model model) {
        getAdmin();
        model.addAttribute("type", 7);
        return "/new33/classTime";
    }

    @RequestMapping("/pkTable")
    public String pkTable(Model model) {
        getAdmin();
        model.addAttribute("type", 3);
        return "/new33/pkTable11";
    }

    @RequestMapping("/pkTableTiao")
    public String pkTableTiao(Model model) {
        getAdmin();
        model.addAttribute("type", 3);
        return "/new33/pkTable";
    }

    @RequestMapping("/classTable")
    public String classTable(Model model) {
        getAdmin();
        model.addAttribute("type", 3);
        return "/new33/classTable";
    }

    @RequestMapping("/pkTableAll")
    public String pkTableAll(Model model) {
        getAdmin();
        model.addAttribute("type", 3);
        return "/new33/pkTableAll";
    }

    @RequestMapping("/baseAffair")
    public String baseAffair(Model model) {
        getAdmin();
        model.addAttribute("type", 7);
        return "/new33/baseAffair";
    }

    @RequestMapping("/baseGuiZe")
    public String baseGuiZe(Model model) {
        getAdmin();
        model.addAttribute("type", 7);
        return "/new33/baseGuiZe";
    }

    @RequestMapping("/baseWeek")
    public String baseWeek(Model model) {
        getAdmin();
        model.addAttribute("type", 7);
        return "/new33/baseWeek";
    }

    @RequestMapping("/schoolSelectLessonSet")
    public String schoolSelectLessonSet(Model model) {
        getAdmin();
        model.addAttribute("type", 1);
        return "/new33/schoolSelectLessonSet";
    }

    @RequestMapping("/stuselect")
    public String stuselect(Model model) {
        getAdmin();
        model.addAttribute("type", 1);
        return "/new33/stuselect";
    }

    @RequestMapping("/selectResult")
    public String selectResult(Model model) {
        getAdmin();
        int role = getSessionValue().getUserRole();
        Integer n33Admin= getSessionValue().getN33Admin();
        if(n33Admin==1){
            model.addAttribute("type", 1);
            return "/new33/selectResult";
        }
        if (UserRole.isStudent(role)) {
            return "/new33/stuselect";
        } else if (UserRole.isTeacherOnly(role)) {
            model.addAttribute("type", 2);
            return "/new33/MyTimetable";
        } else {
            model.addAttribute("type", 1);
            return "/new33/selectResult";
        }

    }

    //重分行政班
    @RequestMapping("/afreshClass")
    public String afreshClass(Model model) {
        getAdmin();
        model.addAttribute("type", 1);
        return "/new33/afreshClass";
    }

    @RequestMapping("/selectClassresult")
    public String selectClassresult(Model model) {
        getAdmin();
        model.addAttribute("type", 1);
        return "new33/selectClassresult";
    }

    @RequestMapping("/selectProgress")
    public String selectProgress(Model model) {
        getAdmin();
        model.addAttribute("type", 1);
        return "/new33/selectProgress";
    }

    @RequestMapping("/fenbanInfoSet")
    public String fenbanInfoSet(Model model) {
        getAdmin();
        model.addAttribute("type", 2);
        return "/new33/fenbanInfoSet";
    }

    @RequestMapping("/danShuangZhou")
    public String danShuangZhou(Model model) {
        getAdmin();
        model.addAttribute("type", 2);
        return "/new33/danShuangZhou";
    }

    @RequestMapping("/zhuanXiang")
    public String zhuanXiang(Model model) {
        getAdmin();
        model.addAttribute("type", 2);
        return "/new33/zhuanXiang";
    }

    @RequestMapping("/paiKeZuHe")
    public String paiKeZuHe(Model model) {
        getAdmin();
        model.addAttribute("type", 2);
        return "/new33/paiKeZuHe";
    }

    @RequestMapping("/conflict")
    public String conflict(Model model) {
        getAdmin();
        model.addAttribute("type", 9);
        return "/new33/conflict";
    }

    @RequestMapping("/exchangeClass")
    public String exchangeClass(Model model) {
        getAdmin();
        model.addAttribute("type", 4);
        return "/new33/exchangeClass";
    }
    //快速调课
    @RequestMapping("/kstkClass")
    public String kstkClass(Model model) {
        getAdmin();
        model.addAttribute("type", 4);
        return "/new33/kstkClass";
    }
    //多步调课
    @RequestMapping("/dbtkClass")
    public String dbtkClass(Model model) {
        getAdmin();
        model.addAttribute("type", 4);
        return "/new33/dbtkClass";
    }
    //增减调课
    @RequestMapping("/zjtkClass")
    public String zjtkClass(Model model) {
        getAdmin();
        model.addAttribute("type", 4);
        return "/new33/zjtkClass";
    }
    @RequestMapping("/exClasslog")
    public String exClasslog(Model model) {
        getAdmin();
        model.addAttribute("type", 4);
        return "/new33/exClasslog";
    }

    @RequestMapping("/selectResultCharts")
    public String selectResultCharts(Model model) {
        getAdmin();
        model.addAttribute("type", 1);
        return "/new33/selectResultCharts";
    }

    @RequestMapping("/zixiclass")
    public String zixike() {
        getAdmin();
        return "/new33/zixiclass";
    }

    @RequestMapping("/paikereport")
    public String paikereport(String ciId, HttpServletRequest request) {
        getAdmin();
        request.setAttribute("ciId", ciId);
        return "/new33/paikereport";
    }

    @RequestMapping("/teaDemand")
    public String teaDemand(Model model) {
        getAdmin();
        model.addAttribute("type", 10);
        return "/new33/teaDemand";
    }

    @RequestMapping("/auditDemand")
    public String auditDemand() {
        return "/new33/auditDemand";
    }

    public void getAdmin() {
        N33_ManagerEntry entry = managerDao.getEntry(getSchoolId(), getUserId());
        if (entry==null) {
            getSessionValue().setN33Admin(0);
        } else {
            getSessionValue().setN33Admin(1);
        }
    }

    @RequestMapping("/compared")
    public String compared(Model model) {
        getAdmin();
        model.addAttribute("type", 1);
        return "/new33/compared";
    }


    @RequestMapping("/jxClass")
    public String jxb() {
        return "/new33/jxClass";
    }

    //新增排课规则设置
    @RequestMapping("/pkRuleSet")
    public String pkrule(Map model,String gradeId) {
    	model.put("gradeId", gradeId);
        return "/new33/pkRuleSet";
    }
    //新增排课规则-教师规则
    @RequestMapping("/pkTcRule")
    public String pkTcRule() {
        return "/new33/pkTcRule";
    }


}
