package com.fulaan.new33.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.db.new33.N33_StudentTagDao;
import com.db.new33.N33_VirtualClassDao;
import com.db.new33.paike.N33_JXBDao;
import com.db.new33.paike.N33_TurnOffDao;
import com.fulaan.new33.dto.autopk.N33_PartClassSetDTO;
import com.fulaan.new33.dto.autopk.N33_ZouBanSetDTO;
import com.fulaan.new33.dto.isolate.N33_KSDTO;
import com.fulaan.new33.service.autopk.N33_AutoTeaSetService;
import com.fulaan.new33.service.autopk.N33_PartClassSetService;
import com.fulaan.new33.service.autopk.N33_ZouBanSetService;
import com.fulaan.new33.service.isolate.BaseService;
import com.fulaan.new33.service.isolate.IsolateSubjectService;
import com.fulaan.new33.service.isolate.N33_TurnOffService;
import com.fulaan.new33.service.isolate.N33_VirtualClassService;
import com.pojo.new33.N33_StudentTagEntry;
import com.pojo.new33.N33_XuNiBanEntry;
import com.pojo.new33.paike.N33_JXBEntry;
import com.pojo.new33.paike.N33_TurnOff;
import com.sys.constants.Constant;
import com.sys.utils.CombineUtils;

/**
 * Created by albin on 2018/4/9.
 * 自动分班组合优先模式
 */
@Service
public class N33_AutoFenBanService extends BaseService {

    @Autowired
    private SchoolSelectLessonSetService lessonSetService;

    private N33_StudentTagDao tagDao = new N33_StudentTagDao();
    private N33_JXBDao jxbDao = new N33_JXBDao();

    private N33_TurnOffDao turnOffDao = new N33_TurnOffDao();

    //当前年级下对应的分段班级
    private List<Map> fenDuanList = null;

    //对应学科下标签的当前数量
    private Integer count = 1;
    //对应学科下标签的学生
    private List<Map<String, String>> stuIds = new ArrayList<Map<String, String>>();

    //对应学科下标签的班级学生
    private Map<String, List<Map<String, String>>> clsStusMap = null;

    //对应标签下班级的学生数量
    private List<Map<String, String>> clsIds = new ArrayList<Map<String, String>>();

    //对应标签下班级的学生数量
    private Map<String, Map<String, String>> clsMap = null;


    private N33_VirtualClassDao virtualClassDao = new N33_VirtualClassDao();
    @Autowired
    private N33_VirtualClassService virtualClassService;
    @Autowired
    private IsolateSubjectService subjectService;

    @Autowired
    private N33_PartClassSetService partClassSetService;

    @Autowired
    private N33_AutoTeaSetService autoTeaSetService;

    @Autowired
    private N33_ZouBanSetService zouBanSetService;

    @Autowired
    private N33_TurnOffService turnOffService;

    /**
     * 自动标签生成
     *
     * @param sid
     * @param ciId
     * @param gradeId 1.查询是否存在标签（存在了就不生成）
     *                2.查询对应年级的的选课组合以及组合下的人
     */
    public String autoFenBanPingTag(ObjectId sid, ObjectId ciId, ObjectId gradeId) {
        //标签存在否
        List<N33_StudentTagEntry> tagEntries = tagDao.getTagListByxqid(ciId, gradeId);
        if (tagEntries.size() > 0) {
            return "已存在标签，无法自动创建!";
        }

        //教学班
        //List<N33_JXBEntry> jxbEntries = jxbDao.getJXBsBySubIdByDeng(ciId, sid, gradeId);

        /*if (jxbEntries.size() == 0) {
            return "请先生成走班教学班之后重试!";
        }*/

        int acClass = 0;
        N33_TurnOff turnOff = turnOffDao.getTurnOffBySidAndCiIdAndGradeId(sid,gradeId,ciId,1);
        if(null!=turnOff){
            acClass = turnOff.getAcClass();
        }

        //选课组合TagList
        Map<String, Object> tagList = lessonSetService.getStuSelectResultByClass(ciId, sid, gradeId, 3);

        //每个组合
        List<Map<String, Object>> content = (List<Map<String, Object>>) tagList.get("content");



        //标签容量
        int rl = 0;
        //差值
        int chaZhi=0;

        String periodCheck = "";

        if(acClass==0) {
            Map<Integer, N33_ZouBanSetDTO> zouBanSetMap = zouBanSetService.getZouBanSetMap(gradeId, sid, ciId);
            N33_ZouBanSetDTO volumeSetDto = zouBanSetMap.get(3);
            if (null != volumeSetDto) {
                rl = volumeSetDto.getCount();
            }
            if (rl == 0) {
                return "请先设置教学班人数上限之后重试!";
            }

            N33_ZouBanSetDTO chaZhiSetDto = zouBanSetMap.get(4);
            if (null != chaZhiSetDto) {
                chaZhi = chaZhiSetDto.getCount();
            }

            Map autoOtherSet = autoTeaSetService.getAutoOtherSet(sid, gradeId, ciId);
            periodCheck = autoOtherSet.get("periodCheck") == null ? "" : autoOtherSet.get("periodCheck").toString();
            if ("1".equals(periodCheck)) {
                fenDuanList = partClassSetService.getPartClassList(sid, gradeId, ciId);
            }
        }

        //Integer rl = jxbEntries.get(0).getRongLiang();

        //差值
        //Integer chaZhi = (rl * 7 / 100);

        List<N33_StudentTagEntry> tagEntryList = new ArrayList<N33_StudentTagEntry>();
        //选课组合遍历
        for (Map<String, Object> tag : content) {
            String subName = (String) tag.get("subName");
            //重置tag
            count = 1;
            //学生
            stuIds = (List<Map<String, String>>) tag.get("stuList");
            clsStusMap = new HashMap<String, List<Map<String, String>>>();
            for (Map<String, String> stu : stuIds) {
                String classId = stu.get("clsId");
                List<Map<String, String>> clsStus = clsStusMap.get(classId);
                if(null==clsStus){
                    clsStus = new ArrayList<Map<String, String>>();
                }
                clsStus.add(stu);
                clsStusMap.put(classId, clsStus);
            }

            //班级
            clsIds = (List<Map<String, String>>) tag.get("list");

            //对应标签下班级的学生数量
            clsMap = new HashMap<String, Map<String, String>>();
            //计算班级能否单独创建
            for (Map<String, String> cls : clsIds) {
                String classId = cls.get("id");
                clsMap.put(classId, cls);
            }
            if(acClass==1){
                //行政班标签
                tagEntryList.addAll(classXingZhengClassTag(subName, ciId, sid, gradeId));
            }else{
                if("1".equals(periodCheck)&&null!=fenDuanList&&fenDuanList.size()>1) {
                    //分段选课能够构成标签的班级学生
                    tagEntryList.addAll(classIsFenDuanTag(subName, rl, chaZhi, ciId, sid, gradeId));
                }else{
                    //非分段选课能够构成标签的班级学生
                    tagEntryList.addAll(classIsNoTFenDuanTag(subName, rl, chaZhi, ciId, sid, gradeId));
                    //过滤单个班级选课能够构成标签的班级
                    //tagEntryList.addAll(classIsDanDuTag(subName, rl, chaZhi, ciId, sid, gradeId));
                    //剩下的构成标签
                    //tagEntryList.addAll(addTagByStudent(subName, rl, ciId, sid, gradeId));
                }
            }
        }

        tagDao.add(tagEntryList);
        return "ok";
    }

    /**
     * @param xqid
     * @param sid
     * @param gradeId 可以单独创建标签的班级
     * @return
     */
    public List<N33_StudentTagEntry> classXingZhengClassTag(String subName, ObjectId xqid, ObjectId sid, ObjectId gradeId) {
        List<N33_StudentTagEntry> studentTagEntries = new ArrayList<N33_StudentTagEntry>();
        //计算班级能否单独创建
        for (Map<String, String> classMap : clsIds) {
            String classId = classMap.get("id");
            //对应选课的学生人数
            Integer clsCount = 0;
            if(null!=classMap.get("num")){
                clsCount = Integer.parseInt(classMap.get("num"));
            }
            if(clsCount>0) {
                //班级的学生
                List<N33_StudentTagEntry.StudentInfoEntry> studentInfoEntries = new ArrayList<N33_StudentTagEntry.StudentInfoEntry>();
                //删除后的
                List<Map<String, String>> removeStuIds = new ArrayList<Map<String, String>>();
                for (Map<String, String> map : stuIds) {
                    if (!map.get("clsId").equals(classId)) {
                        removeStuIds.add(map);
                    } else {
                        ObjectId stuId = new ObjectId(map.get("stuId"));
                        ObjectId clsId = new ObjectId(classId);
                        String stuName = map.get("stuName");
                        N33_StudentTagEntry.StudentInfoEntry studentInfoEntry = new N33_StudentTagEntry.StudentInfoEntry(stuId, clsId, stuName);
                        studentInfoEntries.add(studentInfoEntry);
                    }
                }
                //重置学生
                stuIds = removeStuIds;
                //放入教学班
                studentTagEntries.add(addTagEntry(studentInfoEntries, xqid, sid, subName + count, gradeId, Constant.ONE));
                //选课教学班数量++
                count++;
            }
        }
        return studentTagEntries;
    }

    /**
     * @param rl      容量
     * @param chaZhi  差值
     * @param xqid
     * @param sid
     * @param gradeId 可以单独创建标签的班级
     * @return
     */
    public List<N33_StudentTagEntry> classIsFenDuanTag(String subName, Integer rl, Integer chaZhi, ObjectId xqid, ObjectId sid, ObjectId gradeId) {
        List<N33_StudentTagEntry> studentTagEntries = new ArrayList<N33_StudentTagEntry>();
        for(Map fdCls:fenDuanList){
            int fenDuan = (Integer) fdCls.get("fenDuan");
            List<N33_PartClassSetDTO> classList = (List<N33_PartClassSetDTO>) fdCls.get("classList");
            List<String> classIds = new ArrayList<String>();
            for(N33_PartClassSetDTO pcDto:classList) {
                if (!classIds.contains(pcDto.getClassId())) {
                    classIds.add(pcDto.getClassId());
                }
            }
            List<String> finshClassIds = new ArrayList<String>();
            //可以合为一个标签的班级id集合
            List<List<String>> hbClsIdsList = new ArrayList<List<String>>();
            getHeBanHandle(classIds, rl, chaZhi, hbClsIdsList);
            for(List<String> hbClassIds:hbClsIdsList){
                //班级的学生
                List<N33_StudentTagEntry.StudentInfoEntry> studentInfoEntries = new ArrayList<N33_StudentTagEntry.StudentInfoEntry>();
                for(String hbClassId:hbClassIds){
                    finshClassIds.add(hbClassId);
                    List<Map<String, String>> clsStus = clsStusMap.get(hbClassId);
                    if(null==clsStus){
                        continue;
                    }
                    for (Map<String, String> map : clsStus) {
                        ObjectId stuId = new ObjectId(map.get("stuId"));
                        ObjectId clsId = new ObjectId(hbClassId);
                        String stuName = map.get("stuName");
                        N33_StudentTagEntry.StudentInfoEntry studentInfoEntry = new N33_StudentTagEntry.StudentInfoEntry(stuId, clsId, stuName);
                        studentInfoEntries.add(studentInfoEntry);
                    }
                    //移除完成班级学生
                    clsStusMap.remove(hbClassId);
                }
                //放入教学班
                studentTagEntries.add(addTagEntry(studentInfoEntries, xqid, sid, subName + count, gradeId, fenDuan));

                //选课教学班数量++
                count++;
            }
            //未完成班级id
            List<String> noFinshClassIds = new ArrayList<String>();
            List<Map<String, String>> niFinshStuList = new ArrayList<Map<String, String>>();
            //未完成学生数量
            int noFinshStuCount = 0;
            for(String classId:classIds){
                if(!finshClassIds.contains(classId)){
                    noFinshClassIds.add(classId);
                    //取的班级信息
                    Map<String, String> cls = clsMap.get(classId);
                    //对应选课的学生人数
                    Integer clsCount = Integer.parseInt(cls.get("num"));
                    noFinshStuCount+=clsCount;
                    List<Map<String, String>> clsStus = clsStusMap.get(classId);
                    if(null!=clsStus){
                        niFinshStuList.addAll(clsStus);
                    }
                }
            }

            if(noFinshStuCount>0){
                //需要循环的次数
                Integer xunHuanCount = noFinshStuCount % rl == 0 ? noFinshStuCount / rl : noFinshStuCount / rl + 1;
                for (Integer st = 1; st <= xunHuanCount; st++) {
                    //班级的学生
                    List<N33_StudentTagEntry.StudentInfoEntry> studentInfoEntries = new ArrayList<N33_StudentTagEntry.StudentInfoEntry>();
                    //删除后的
                    List<Map<String, String>> removeStuIds = new ArrayList<Map<String, String>>();
                    for (Map<String, String> map : niFinshStuList) {
                        if (map == null) {
                            break;
                        }
                        if(studentInfoEntries.size()<=rl + chaZhi) {
                            ObjectId stuId = new ObjectId(map.get("stuId"));
                            ObjectId clsId = new ObjectId(map.get("clsId"));
                            String stuName = map.get("stuName");
                            N33_StudentTagEntry.StudentInfoEntry studentInfoEntry = new N33_StudentTagEntry.StudentInfoEntry(stuId, clsId, stuName);
                            studentInfoEntries.add(studentInfoEntry);
                        }else{
                            removeStuIds.add(map);
                        }
                    }
                    //重置学生
                    niFinshStuList = removeStuIds;

                    //放入教学班
                    studentTagEntries.add(addTagEntry(studentInfoEntries, xqid, sid, subName + count, gradeId, fenDuan));
                    //选课教学班数量++
                    count++;
                }
            }
        }
        return studentTagEntries;
    }

    /**
     * @param rl      容量
     * @param chaZhi  差值
     * @param xqid
     * @param sid
     * @param gradeId 可以单独创建标签的班级
     * @return
     */
    public List<N33_StudentTagEntry> classIsNoTFenDuanTag(String subName, Integer rl, Integer chaZhi, ObjectId xqid, ObjectId sid, ObjectId gradeId){
        List<N33_StudentTagEntry> studentTagEntries = new ArrayList<N33_StudentTagEntry>();

        List<String> classIds = new ArrayList<String>();
        //计算班级能否单独创建
        for (Map<String, String> classMap : clsIds) {
            String classId = classMap.get("id");
            if (!classIds.contains(classId)) {
                classIds.add(classId);
            }
        }

        List<String> finshClassIds = new ArrayList<String>();
        //可以合为一个标签的班级id集合
        List<List<String>> hbClsIdsList = new ArrayList<List<String>>();
        getHeBanHandle(classIds, rl, chaZhi, hbClsIdsList);

        int allUpLimitCount = 0;
        for(List<String> hbClassIds:hbClsIdsList){
            //班级的学生
            List<N33_StudentTagEntry.StudentInfoEntry> studentInfoEntries = new ArrayList<N33_StudentTagEntry.StudentInfoEntry>();
            for(String hbClassId:hbClassIds){
                finshClassIds.add(hbClassId);
                List<Map<String, String>> clsStus = clsStusMap.get(hbClassId);
                if(null==clsStus){
                    continue;
                }
                for (Map<String, String> map : clsStus) {
                    ObjectId stuId = new ObjectId(map.get("stuId"));
                    ObjectId clsId = new ObjectId(hbClassId);
                    String stuName = map.get("stuName");
                    N33_StudentTagEntry.StudentInfoEntry studentInfoEntry = new N33_StudentTagEntry.StudentInfoEntry(stuId, clsId, stuName);
                    studentInfoEntries.add(studentInfoEntry);
                }
                //移除完成班级学生
                clsStusMap.remove(hbClassId);
            }
            int upLimitCount = rl+chaZhi-studentInfoEntries.size();
            if(upLimitCount>0){
                allUpLimitCount +=upLimitCount;
            }
            //放入教学班
            studentTagEntries.add(addTagEntry(studentInfoEntries, xqid, sid, subName + count, gradeId, Constant.ONE));

            //选课教学班数量++
            count++;
        }

        //未完成班级id
        List<String> noFinshClassIds = new ArrayList<String>();
        List<Map<String, String>> niFinshStuList = new ArrayList<Map<String, String>>();
        //未完成学生数量
        int noFinshStuCount = 0;
        for(String classId:classIds){
            if(!finshClassIds.contains(classId)){
                noFinshClassIds.add(classId);
                //取的班级信息
                Map<String, String> cls = clsMap.get(classId);
                //对应选课的学生人数
                Integer clsCount = Integer.parseInt(cls.get("num"));
                noFinshStuCount+=clsCount;
                List<Map<String, String>> clsStus = clsStusMap.get(classId);
                if(null!=clsStus){
                    niFinshStuList.addAll(clsStus);
                }
            }
        }

        if(noFinshStuCount>0){
            //需要循环的次数
            Integer xunHuanCount = noFinshStuCount % rl == 0 ? noFinshStuCount / rl : noFinshStuCount / rl + 1;
            for (Integer st = 1; st <= xunHuanCount; st++) {
                //班级的学生
                List<N33_StudentTagEntry.StudentInfoEntry> studentInfoEntries = new ArrayList<N33_StudentTagEntry.StudentInfoEntry>();
                //删除后的
                List<Map<String, String>> removeStuIds = new ArrayList<Map<String, String>>();
                for (Map<String, String> map : niFinshStuList) {
                    if (map == null) {
                        break;
                    }
                    if(studentInfoEntries.size()<rl + chaZhi) {
                        ObjectId stuId = new ObjectId(map.get("stuId"));
                        ObjectId clsId = new ObjectId(map.get("clsId"));
                        String stuName = map.get("stuName");
                        N33_StudentTagEntry.StudentInfoEntry studentInfoEntry = new N33_StudentTagEntry.StudentInfoEntry(stuId, clsId, stuName);
                        studentInfoEntries.add(studentInfoEntry);
                    }else{
                        removeStuIds.add(map);
                    }
                }

                //放入教学班
                studentTagEntries.add(addTagEntry(studentInfoEntries, xqid, sid, subName + count, gradeId, Constant.ONE));

                //重置学生
                niFinshStuList = removeStuIds;
                if(niFinshStuList.size()<rl-chaZhi) {
                    if (allUpLimitCount >= niFinshStuList.size()) {
                        break;
                    }
                }
                //选课教学班数量++
                count++;
            }
            if(niFinshStuList.size()>0) {
                for (N33_StudentTagEntry tagEntry:studentTagEntries) {
                    //班级的学生
                    List<N33_StudentTagEntry.StudentInfoEntry> studentInfos = tagEntry.getStudents();
                    int upLimitCount = rl + chaZhi - studentInfos.size();
                    if(upLimitCount>0){
                        //删除后的
                        List<Map<String, String>> rmvStuIds = new ArrayList<Map<String, String>>();
                        for (Map<String, String> map : niFinshStuList) {
                            if (map == null) {
                                break;
                            }
                            if(studentInfos.size()<rl + chaZhi) {
                                ObjectId stuId = new ObjectId(map.get("stuId"));
                                ObjectId clsId = new ObjectId(map.get("clsId"));
                                String stuName = map.get("stuName");
                                N33_StudentTagEntry.StudentInfoEntry studentInfo = new N33_StudentTagEntry.StudentInfoEntry(stuId, clsId, stuName);
                                studentInfos.add(studentInfo);
                            }else{
                                rmvStuIds.add(map);
                            }
                        }
                        tagEntry.setStudents(studentInfos);
                        //重置学生
                        niFinshStuList = rmvStuIds;
                    }
                }
            }
        }
        return studentTagEntries;
    }

    /**
     * 合为一个标签的班级id集合
     * @param classIds 分段班级集合
     * @param volume 容量
     * @param chaZhi 差值
     * @return
     */
    public void getHeBanHandle(List<String> classIds, Integer volume, Integer chaZhi, List<List<String>> hbClsIdsList) {
        //优先取等于容量
        getHeBanClassIds(classIds, classIds.size(), volume, chaZhi, "==", hbClsIdsList, new ArrayList<String>());
        if(chaZhi>1) {
            for (int cursor = 1; cursor <= chaZhi; cursor++) {
                //其次取小于等于容量上限
                getHeBanClassIds(classIds, classIds.size(), volume, cursor, ">", hbClsIdsList, new ArrayList<String>());
                //最后取大于等于容量下限
                getHeBanClassIds(classIds, classIds.size(), volume, cursor, "<", hbClsIdsList, new ArrayList<String>());
            }
        }
    }

    /**
     * 合为一个标签的班级id集合
     * @param classIds 分段班级集合
     * @param classSize 班级数量
     * @param volume 容量
     * @param chaZhi 差值
     * @param type 合班类型
     * @return
     */
    public List<String> getHeBanClassIds(List<String> classIds, int classSize, int volume, int chaZhi, String type, List<List<String>> hbClsIdsList, List<String> hbClassIds) {
        boolean isEnd = true;
        for(String classId:classIds){
            List<String> finshClassIds = new ArrayList<String>();
            for(List<String> hbOkClsIds:hbClsIdsList){
                finshClassIds.addAll(hbOkClsIds);
            }
            if(finshClassIds.contains(classId)){
                continue;
            }
            if(!hbClassIds.contains(classId)) {
                //取的班级信息
                Map<String, String> cls = clsMap.get(classId);
                if(null==cls){
                    continue;
                }
                isEnd = false;
                //对应选课的学生人数
                Integer clsCount = Integer.parseInt(cls.get("num"));

                //上一次递归的班级学生总数
                int prveClsCount = 0;
                for(String hbClassId:hbClassIds){
                    //取的班级信息
                    Map<String, String> pcls = clsMap.get(hbClassId);
                    if(null!=pcls) {
                        //对应选课的学生人数
                        Integer pclsCount = Integer.parseInt(pcls.get("num"));
                        prveClsCount += pclsCount;
                    }
                }
                Boolean chuangJian = false;

                int stuAllCount = clsCount + prveClsCount;
                if("==".equals(type)){
                    if (stuAllCount == volume) {
                        chuangJian = true;
                    }
                }

                if(">".equals(type)){
                    if (stuAllCount > volume) {
                        chuangJian = stuAllCount - volume <= chaZhi;
                        if(!chuangJian){
                            isEnd = true;
                            continue;
                        }
                    }
                }

                if("<".equals(type)){
                    if (stuAllCount < volume) {
                        chuangJian = volume - stuAllCount <= chaZhi;
                    }
                }

                if (stuAllCount < volume) {
                    if (!chuangJian) {
                        hbClassIds.add(classId);
                    }
                }

                if (chuangJian) {
                    hbClassIds.add(classId);
                    List<String> hbOkClsIds = new ArrayList<String>();
                    for(String hbClassId:hbClassIds){
                        hbOkClsIds.add(hbClassId);
                        clsMap.remove(hbClassId);
                    }
                    hbClsIdsList.add(hbOkClsIds);
                    hbClassIds = new ArrayList<String>();
                    break;
                }else{
                    if(classIds.size()>0) {
                        hbClassIds = getHeBanClassIds(classIds.subList(1, classIds.size()), classSize, volume, chaZhi, type, hbClsIdsList, hbClassIds);
                        if(hbClassIds.size()==0){
                            if(classSize>classIds.size()){
                                return hbClassIds;
                            }
                        }
                    }else{
                        hbClassIds = new ArrayList<String>();
                    }
                }
            }
        }
        if(isEnd){
            hbClassIds = new ArrayList<String>();
        }
        return hbClassIds;
    }

    /**
     * @param rl      容量
     * @param chaZhi  差值
     * @param xqid
     * @param sid
     * @param gradeId 可以单独创建标签的班级
     * @return
     */
    public List<N33_StudentTagEntry> classIsDanDuTag(String subName, Integer rl, Integer chaZhi, ObjectId xqid, ObjectId sid, ObjectId gradeId) {

        List<N33_StudentTagEntry> studentTagEntries = new ArrayList<N33_StudentTagEntry>();

        //计算班级能否单独创建
        for (Map<String, String> classMap : clsIds) {

            //对应选课的学生人数
            Integer clsCount = Integer.parseInt(classMap.get("num"));

            String classId = classMap.get("id");
            Boolean chuangJian = false;
            if (clsCount > rl) {
                chuangJian = clsCount - rl <= chaZhi;
            } else {
                chuangJian = rl - clsCount <= chaZhi;
            }
            if (chuangJian) {
                //班级的学生
                List<N33_StudentTagEntry.StudentInfoEntry> studentInfoEntries = new ArrayList<N33_StudentTagEntry.StudentInfoEntry>();

                //删除后的
                List<Map<String, String>> removeStuIds = new ArrayList<Map<String, String>>();

                for (Map<String, String> map : stuIds) {

                    if (!map.get("clsId").equals(classId)) {
                        removeStuIds.add(map);
                    } else {
                        ObjectId stuId = new ObjectId(map.get("stuId"));
                        ObjectId clsId = new ObjectId(classId);
                        String stuName = map.get("stuName");
                        N33_StudentTagEntry.StudentInfoEntry studentInfoEntry = new N33_StudentTagEntry.StudentInfoEntry(stuId, clsId, stuName);
                        studentInfoEntries.add(studentInfoEntry);
                    }
                }
                //重置学生
                stuIds = removeStuIds;
                //放入教学班
                studentTagEntries.add(addTagEntry(studentInfoEntries, xqid, sid, subName + count, gradeId, Constant.ONE));
                //选课教学班数量++
                count++;
            }
        }
        return studentTagEntries;
    }


    /**
     * 生成无法构建教学班的学生的标签
     *
     * @param subName
     * @param rl
     * @param xqid
     * @param sid
     * @param gradeId
     * @return
     */
    public List<N33_StudentTagEntry> addTagByStudent(String subName, Integer rl, ObjectId xqid, ObjectId sid, ObjectId gradeId) {
        List<N33_StudentTagEntry> studentTagEntries = new ArrayList<N33_StudentTagEntry>();
        //排序学生
        Collections.sort(stuIds, new Comparator<Map<String, String>>() {
            @Override
            public int compare(Map<String, String> o1, Map<String, String> o2) {
                return Integer.parseInt(o2.get("clsXh")) - Integer.parseInt(o1.get("clsXh"));
            }
        });
        //需要循环的次数
        Integer xunHuanCount = stuIds.size() % rl == 0 ? stuIds.size() / rl : stuIds.size() / rl + 1;
        for (Integer st = 1; st <= xunHuanCount; st++) {
            //班级的学生
            List<N33_StudentTagEntry.StudentInfoEntry> studentInfoEntries = new ArrayList<N33_StudentTagEntry.StudentInfoEntry>();
            //删除后的
            List<Map<String, String>> removeStuIds = new ArrayList<Map<String, String>>();
            for (Integer sta = 0; sta < stuIds.size(); sta++) {
                if (sta <= rl - 1) {
                    Map<String, String> map = stuIds.get(sta);
                    if (map == null) {
                        break;
                    }
                    ObjectId stuId = new ObjectId(map.get("stuId"));
                    ObjectId clsId = new ObjectId(map.get("clsId"));
                    String stuName = map.get("stuName");
                    N33_StudentTagEntry.StudentInfoEntry studentInfoEntry = new N33_StudentTagEntry.StudentInfoEntry(stuId, clsId, stuName);
                    studentInfoEntries.add(studentInfoEntry);
                } else {
                    removeStuIds.add(stuIds.get(sta));
                }
            }
            //重置学生
            stuIds = removeStuIds;
            //放入教学班
            studentTagEntries.add(addTagEntry(studentInfoEntries, xqid, sid, subName + count, gradeId, Constant.ONE));
            //选课教学班数量++
            count++;
        }
        return studentTagEntries;
    }
    /**
     * 初始化
     *
     * @param studentInfoEntries
     * @param xqid
     * @param sid
     * @param subName
     * @param gradeId
     * @return
     */
    public N33_StudentTagEntry addTagEntry(List<N33_StudentTagEntry.StudentInfoEntry> studentInfoEntries, ObjectId xqid, ObjectId sid, String subName, ObjectId gradeId, int fenDuan) {
        return new N33_StudentTagEntry(xqid, sid, gradeId, subName, studentInfoEntries, new ArrayList<ObjectId>(), 0, fenDuan);
    }

    /**
     * 清除标签
     * @param schoolId
     * @param ciId
     * @param gradeId
     */
    public void cleanUpTagInfos(ObjectId schoolId, ObjectId ciId, ObjectId gradeId) {
        tagDao.cleanUpTagEntries(schoolId, ciId, gradeId);
    }

    /***
     * 自动分班
     * 1.查询所有的等级虚拟班
     * 2.
     * @param schoolId
     * @param gradeId
     */
    public Map<String,Object> AutoFenBanBySubject(ObjectId schoolId, ObjectId gradeId,ObjectId xqid) {
        Map<ObjectId,N33_XuNiBanEntry> xuNiBanEntryMap = virtualClassDao.getN33_VirtualClassEntryByXqidAndGradeForMap(xqid, schoolId, gradeId, 0);
        //对应次的所有虚拟班
        List<N33_XuNiBanEntry> virtualClassList = virtualClassDao.getN33_VirtualClassEntryByXqidAndGrade(xqid, schoolId, gradeId, 0);
        String s = null;

        //用于标记虚拟班是否已经拼过     0   未拼      1       已拼
        Map<ObjectId,Integer> isPinMap = new HashMap<ObjectId, Integer>();
        for(N33_XuNiBanEntry xuNiBanEntry : virtualClassList){
            isPinMap.put(xuNiBanEntry.getID(),0);
        }
        for (N33_XuNiBanEntry xuNiBanEntry : virtualClassList) {
            if(isPinMap.get(xuNiBanEntry.getID()) == 0){
                List<N33_XuNiBanEntry.StudentTag> studentTagList = xuNiBanEntry.getTagList();

                //教学班容量
                Map<String,Object> rlMap = getJXBRl(xqid,schoolId,gradeId);
                Integer rl = null;
                if(rlMap.get("jxbRl") != null){
                    rl = (Integer) rlMap.get("jxbRl");
                }else{
                    Map<String,Object> map = new HashMap<String, Object>();
                    map.put("aware",(String)rlMap.get("aware"));
                    return map;
                }
                //为解决引用传递的问题
                List<N33_XuNiBanEntry.StudentTag> replaceStudentTag = new ArrayList<N33_XuNiBanEntry.StudentTag>();
                for (N33_XuNiBanEntry.StudentTag studentTag : studentTagList){
                    replaceStudentTag.add(studentTag);
                }
                while(true) {
                    //用来找被拼班的虚拟班
                    List<N33_XuNiBanEntry> virtualClassListForPin = virtualClassDao.getN33_VirtualClassEntryByXqidAndGrade(xqid, schoolId, gradeId, 0);
                    List<Map<String,Object>> retList = new ArrayList<Map<String, Object>>();
                    if (replaceStudentTag.size() == 1) {
                        List<ObjectId> subIds = new ArrayList<ObjectId>();
                        N33_XuNiBanEntry.StudentTag studentTag = replaceStudentTag.get(0);
                        List<N33_XuNiBanEntry.SubjectInfo> subjectInfoList = studentTag.getSubjectList();
                        Map<ObjectId, Integer> stuCountMap = new HashMap<ObjectId, Integer>();
                        for (N33_XuNiBanEntry.SubjectInfo subjectInfo : subjectInfoList) {
                            subIds.add(subjectInfo.getSubId());
                            stuCountMap.put(subjectInfo.getSubId(), subjectInfo.getStuCount());
                        }
                        List<Map<String, Object>> xuNiBanListByThreeSubId = getCanPinBanListByThreeSubId(true,rl, xuNiBanEntry.getID(), subIds, virtualClassListForPin, stuCountMap);
                        if (xuNiBanListByThreeSubId == null || xuNiBanListByThreeSubId.size() == 0) {
                            //随机取两科
                            List<CombineUtils.CombineResult<ObjectId>> ll = new ArrayList<CombineUtils.CombineResult<ObjectId>>();
                            CombineUtils.combinerSelect(subIds, 2, ll); // 对现有学科进行组合与保存的进行比较
                            for (CombineUtils.CombineResult cr : ll) {
                                //遍历每一个两科，查询所有的虚拟班中包含这两科的组合
                                //String combineName=getCombineName(subjectMap,cr.getList());
                                List<Map<String, Object>> xuNiBanList = virtualClassService.judgeSelect(true,xuNiBanEntry.getID(), virtualClassListForPin, stuCountMap, rl, cr.getList());
                                retList.addAll(xuNiBanList);
                            }
                        }
                        if(xuNiBanListByThreeSubId != null && xuNiBanListByThreeSubId.size() > 0){
                            retList.addAll(xuNiBanListByThreeSubId);
                        }
                    } else if (replaceStudentTag.size() > 1) {
                        List<ObjectId> list = isDingErZouYi(replaceStudentTag);      //用于判断是否定二走一
                        if (list.size() == 0) {
                            List<ObjectId> subIds = new ArrayList<ObjectId>();
                            N33_XuNiBanEntry.StudentTag studentTag = replaceStudentTag.get(0);
                            List<N33_XuNiBanEntry.SubjectInfo> subjectInfoList = studentTag.getSubjectList();
                            for (N33_XuNiBanEntry.SubjectInfo subjectInfo : subjectInfoList) {
                                subIds.add(subjectInfo.getSubId());
                            }
                            Map<ObjectId, Integer> stuCountMap = new HashMap<ObjectId, Integer>();
                            stuCountMap = getStudentCountMap(replaceStudentTag);
                            List<Map<String, Object>> xuNiBanListByThreeSubId = getCanPinBanListByThreeSubId(true,rl, xuNiBanEntry.getID(), subIds, virtualClassListForPin, stuCountMap);
                            if (xuNiBanListByThreeSubId == null || xuNiBanListByThreeSubId.size() == 0) {
                                //随机取两科
                                List<CombineUtils.CombineResult<ObjectId>> ll = new ArrayList<CombineUtils.CombineResult<ObjectId>>();
                                CombineUtils.combinerSelect(subIds, 2, ll); // 对现有学科进行组合与保存的进行比较
                                for (CombineUtils.CombineResult cr : ll) {
                                    //遍历每一个两科，查询所有的虚拟班中包含这两科的组合
                                    //String combineName=getCombineName(subjectMap,cr.getList());
                                    List<Map<String, Object>> xuNiBanList = virtualClassService.judgeSelect(true,xuNiBanEntry.getID(), virtualClassListForPin, stuCountMap, rl, cr.getList());
                                    retList.addAll(xuNiBanList);
                                }
                            }
                            if(xuNiBanListByThreeSubId != null && xuNiBanListByThreeSubId.size() > 0){
                                retList.addAll(xuNiBanListByThreeSubId);
                            }
                        } else {
                            Map<ObjectId, Integer> stuCountMap = new HashMap<ObjectId, Integer>();
                            stuCountMap = getStudentCountMap(replaceStudentTag);
                            List<Map<String, Object>> xuNiBanList = virtualClassService.judgeSelect(true,xuNiBanEntry.getID(), virtualClassListForPin, stuCountMap, rl, list);
                            retList.addAll(xuNiBanList);
                        }
                    }
                    if (retList != null && retList.size() == 0) {
                        break;
                    } else {
                        //得到被拼班的虚拟班
                        N33_XuNiBanEntry pinByXuNiBan = getPinByVirtualClass(retList, xuNiBanEntryMap);
                        isPinMap.put(pinByXuNiBan.getID(),1);
                        replaceStudentTag.add(pinByXuNiBan.getTagList().get(0));
                        s = virtualClassService.puzzleClass(xuNiBanEntry.getID(),pinByXuNiBan.getID(),xqid,gradeId,schoolId);
                    }
                }
            }else{
                continue;
            }
        }
        Map<String,Object> map = new HashMap<String, Object>();
        if(s != null){
            map.put("result",s);
        }else{
            map.put("result","服务器异常");
        }
        return map;
    }

    private N33_XuNiBanEntry getPinByVirtualClass(List<Map<String,Object>> retList,Map<ObjectId,N33_XuNiBanEntry> xuNiBanEntryMap){
        double variance = 0.00;
        String id = "";
        for (Map<String, Object> map : retList) {
            if ((Integer)map.get("isBigger") == 0) {
                variance = (Double) map.get("variance");
                id = (String) map.get("id");
                break;
            }
        }

        for (Map<String, Object> map : retList) {
            if((Integer)map.get("isBigger") == 0){
                if ((Double) map.get("variance") <= variance) {
                    variance = (Double) map.get("variance");
                    id = (String) map.get("id");
                }
            }
        }
        N33_XuNiBanEntry xuNiBanEntry = xuNiBanEntryMap.get(new ObjectId(id));
        return xuNiBanEntry;
    }

    /**
     * 判断该虚拟班是否已经定二走一
     * @param studentTagList
     * @return
     */
    public List<ObjectId> isDingErZouYi(List<N33_XuNiBanEntry.StudentTag> studentTagList){
        List<ObjectId> list = new ArrayList<ObjectId>();
        for (N33_XuNiBanEntry.StudentTag studentTag : studentTagList) {
            for (N33_XuNiBanEntry.StudentTag studentTag1 : studentTagList) {
                List<ObjectId> subIds2 = new ArrayList<ObjectId>();
                List<N33_XuNiBanEntry.SubjectInfo> subjectInfoList2 = studentTag1.getSubjectList();
                for (N33_XuNiBanEntry.SubjectInfo subjectInfo : subjectInfoList2) {
                    subIds2.add(subjectInfo.getSubId());
                }
                List<ObjectId> subIds1 = new ArrayList<ObjectId>();
                List<N33_XuNiBanEntry.SubjectInfo> subjectInfoList = studentTag.getSubjectList();
                for (N33_XuNiBanEntry.SubjectInfo subjectInfo : subjectInfoList) {
                    subIds1.add(subjectInfo.getSubId());
                }
                if(studentTag.getTagId().toString() != studentTag1.getTagId().toString() && subIds1.size() == subIds2.size()){
                    subIds1.retainAll(subIds2);
                    if(subIds1.size() != subIds2.size()){
                        list.addAll(subIds1);
                        return list;
                    }
                }
            }
        }
        return list;
    }

    /**
     * 按照三门学科相同找对应可能拼班的虚拟班
     * @param virtualClassId    要拼班的虚拟班Id
     * @param pinSubIds    要拼班的虚拟班学科id集合（包含一个标签或者多个标签学科相同的情况）
     * @param xuNiBanEntries    对应的可能拼班的未筛选之前的虚拟班
     * @param stuCountMap       虚拟班对应的学科和人生
     * @return
     */
    public List<Map<String,Object>> getCanPinBanListByThreeSubId(boolean isAuto,Integer rl,ObjectId virtualClassId,List<ObjectId> pinSubIds,List<N33_XuNiBanEntry> xuNiBanEntries,Map<ObjectId,Integer> stuCountMap){
        List<Map<String, Object>> retList = new ArrayList<Map<String, Object>>();
        //已经加入返回的List中，用于在再次遍历的时候去除已经返回的记录
        List<N33_XuNiBanEntry> ycXuNiBanList = new ArrayList<N33_XuNiBanEntry>();
        for (N33_XuNiBanEntry xuNiBanEntry : xuNiBanEntries) {
            if (xuNiBanEntry.getTagList().size() == 1 && xuNiBanEntry.getJxbIds().size() == 0 && !xuNiBanEntry.getID().toString().equals(virtualClassId.toString())) {
                Map<String, Object> map = new HashMap<String, Object>();
                Map<ObjectId,Integer> replaceMap = new HashMap<ObjectId, Integer>();
                for (Map.Entry<ObjectId,Integer> entry : stuCountMap.entrySet()) {
                    replaceMap.put(entry.getKey(),entry.getValue());
                }
                List<N33_XuNiBanEntry.SubjectInfo> subjectInfoList = xuNiBanEntry.getTagList().get(0).getSubjectList();
                List<ObjectId> subIds = new ArrayList<ObjectId>();
                for (N33_XuNiBanEntry.SubjectInfo subjectInfo : subjectInfoList) {
                    subIds.add(subjectInfo.getSubId());
                }
                //方差
                double variance = 0.00;
                double sum = 0.00;
                if(pinSubIds.size() == subIds.size()){
                    subIds.retainAll(pinSubIds);
                    if(pinSubIds.size() == subIds.size()){
                        ycXuNiBanList.add(xuNiBanEntry);
                        for (N33_XuNiBanEntry.SubjectInfo subjectInfo : subjectInfoList) {
                            Integer stuCount = replaceMap.get(subjectInfo.getSubId());
                            stuCount += subjectInfo.getStuCount();
                            replaceMap.put(subjectInfo.getSubId(), stuCount);
                        }
                    }else{
                        continue;
                    }
                }

                BigDecimal bd=new BigDecimal(rl * 0.05).setScale(0, BigDecimal.ROUND_HALF_UP);
                Integer maxCount = Integer.parseInt(bd.toString()) + rl;

                for (ObjectId id : subIds) {
                    sum += Math.pow((replaceMap.get(id) - rl), 2);
                }
                if((Integer)replaceMap.get(subIds.get(0)) > maxCount){
                    if(isAuto == true){
                        continue;
                    }else{
                        variance = sum / subIds.size();
                        map.put("variance", variance);
                        map.put("id", xuNiBanEntry.getID().toString());
                        map.put("name", xuNiBanEntry.getTagList().get(0).getTagName());
                        map.put("bqName",xuNiBanEntry.getTagList().get(0).getBqName());
                        map.put("stuCount",xuNiBanEntry.getTagList().get(0).getTagCount());
                        //如果被拼班的人数加上拼班的人数大于容量的标记        0  小于容量   1   大于容量
                        map.put("isBigger",1);
                        //map.put("stuCount",xuNiBanEntry.getTagList().get(0).getSubjectList().get(0).getStuCount());
                        retList.add(map);
                    }
                }else{
                    variance = sum / subIds.size();
                    map.put("variance", variance);
                    map.put("id", xuNiBanEntry.getID().toString());
                    map.put("name", xuNiBanEntry.getTagList().get(0).getTagName());
                    map.put("bqName",xuNiBanEntry.getTagList().get(0).getBqName());
                    map.put("stuCount",xuNiBanEntry.getTagList().get(0).getTagCount());
                    map.put("isBigger",0);
                    //map.put("stuCount",xuNiBanEntry.getTagList().get(0).getSubjectList().get(0).getStuCount());
                    retList.add(map);
                }
            }
        }
        xuNiBanEntries.removeAll(ycXuNiBanList);
        //对应次的所有虚拟班
        return retList;
    }

    /**
     * 计算对应虚拟班学科和人数的map
     * @param studentTagList
     * @return
     */
    public Map<ObjectId,Integer> getStudentCountMap(List<N33_XuNiBanEntry.StudentTag> studentTagList){
        Map<ObjectId,Integer> map = new HashMap<ObjectId, Integer>();
        for (N33_XuNiBanEntry.StudentTag studentTag : studentTagList) {
            List<N33_XuNiBanEntry.SubjectInfo> subjectInfoList = studentTag.getSubjectList();
            for (N33_XuNiBanEntry.SubjectInfo subjectInfo : subjectInfoList) {
                if(map.get(subjectInfo.getSubId()) != null){
                    Integer count = map.get(subjectInfo.getSubId());
                    count += subjectInfo.getStuCount();
                    map.put(subjectInfo.getSubId(),count);
                }else{
                    map.put(subjectInfo.getSubId(),subjectInfo.getStuCount());
                }
            }
        }
        return map;
    }

    /**
     * 查询教学班的容量
     * @param xqid
     * @param schoolId
     * @param gradeId
     * @return
     */
    private Map<String, Object> getJXBRl(ObjectId xqid,ObjectId schoolId,ObjectId gradeId){
        Map<String, Object> map = new HashMap<String, Object>();
        List<N33_KSDTO> N33_KSDTOList = subjectService.getIsolateSubjectByGradeIdIncludeMain(xqid, schoolId, gradeId.toString(), 1);
        int acClassType = 0;
        N33_TurnOff turnOff = turnOffDao.getTurnOffBySidAndCiIdAndGradeId(schoolId, gradeId, xqid,1);
        if(null!=turnOff){
            acClassType = turnOff.getAcClass();
        }
        N33_KSDTO ksDto = null;
        if(N33_KSDTOList != null && N33_KSDTOList.size() > 0){
            ksDto = N33_KSDTOList.get(0);
        }
        List<N33_JXBEntry> jxbEntries = new ArrayList<N33_JXBEntry>();
        if(ksDto != null){
            jxbEntries = jxbDao.getJXBList(schoolId, gradeId, new ObjectId(ksDto.getSubid()), xqid, acClassType);
        }
        Integer rl = null;
        if (jxbEntries.size() > 0) {
            rl = jxbEntries.get(0).getRongLiang();
        } else {
            map.put("aware", "请创建走班课教学班");
            return map;
        }
        map.put("jxbRl",rl);
        return map;
    }

    /**
     * 设置教学班
     * @param schoolId
     * @param gradeId
     * @param xqid
     * @return
     */
    public String AutoSelectJXB(ObjectId schoolId, ObjectId gradeId, ObjectId xqid, HttpServletRequest request){
        HttpSession session = request.getSession();
        Map<String,Object> status = new HashMap<String, Object>();
        status.put("st",1);
        session.setAttribute("autoSetJXB",status);
        String result = AutoSelectJXBByType(schoolId,gradeId,xqid,0);
        String result1 = AutoSelectJXBByType(schoolId,gradeId,xqid,1);
        Map<String,Object> sessionStatus = (Map<String, Object>) session.getAttribute("autoSetJXB");
        sessionStatus.put("st",-1);
        session.setAttribute("autoSetJXB",sessionStatus);
        if(!"操作成功".equals(result)){
            return result;
        }else{
            return result1;
        }
    }

    /**
     * 自动选择教学班通过类型
     * @param schoolId
     * @param gradeId
     * @param xqid
     * @return
     */
    public String AutoSelectJXBByType(ObjectId schoolId, ObjectId gradeId,ObjectId xqid,Integer type){
        Integer jxbType = null;
        if(type == 0){
            jxbType = 1;
        }else{
            jxbType = 2;
        }
        Map<String,Object> jxbRl = getJXBRl(xqid,schoolId,gradeId);
        Integer rl = null;
        if(jxbRl.get("jxbRl") != null){
            rl = (Integer) jxbRl.get("jxbRl");
        }else{
            return (String)jxbRl.get("aware");
        }
        //对应次的所有虚拟班
        List<N33_XuNiBanEntry> virtualClassList = virtualClassDao.getN33_VirtualClassEntryByXqidAndGrade(xqid, schoolId, gradeId,type);
        //标记哪些学科已经设置过教学班（0  未设置过        1   已设置过）
        Map<ObjectId,Map<ObjectId,Integer>> isSelectJXBMap = markIsOrNotSelectJXB(virtualClassList,xqid);
        for (N33_XuNiBanEntry xuNiBanEntry : virtualClassList) {
            //得到虚拟班中每一个学科的人数
            Map<ObjectId,Integer> subStuCountMap = getSubStudentCount(xuNiBanEntry);
            for (Map.Entry<ObjectId,Integer> entry : subStuCountMap.entrySet()){
                //若为true，则该虚拟班已经选择过教学班
                /*boolean isSelectedJxb = isSelectJXB(jxbEntryList,entry.getKey());
                if(isSelectedJxb){
                    continue;
                    continue;
                }else{
                    //判断该虚拟班该学科在本次是否设置过教学班
                    if(isSelectJXBMap.get(xuNiBanEntry.getID()).get(entry.getKey()) == 0){
                        //查找可以拼在一起的虚拟班的相同学科
                        while(true){

                        }
                    }
                }*/
                //判断该虚拟班该学科在本次是否设置过教学班
                if(isSelectJXBMap.get(xuNiBanEntry.getID()).get(entry.getKey()) == 0){

                    //查找可以拼在一起的虚拟班的相同学科
                    while(true){
                        //该虚拟班的教学班ID集合
                        List<ObjectId> jxbIds = xuNiBanEntry.getJxbIds();
                        //对应虚拟班关联的教学班
                        List<N33_JXBEntry> jxbEntryList = jxbDao.getJXBListByIds(jxbIds, xqid);
                        N33_JXBEntry jxbEntry = getSelectJXB(jxbEntryList,entry.getKey());
                        //如果对应的学科已经选中了某教学班
                        if(isSelectJXB(jxbEntryList,entry.getKey())){
                            Integer stuCount = jxbEntry.getStudentIds().size();
                            //获取所有可能拼教学班的组合
                            List<Map<String,Object>> retList = getCanPinJXBList(xqid,schoolId,gradeId,entry.getKey(),rl,stuCount,xuNiBanEntry.getID(),type);
                            if (retList != null && retList.size() == 0) {
                                break;
                            } else {
                                //得到循环的虚拟班中的学科和哪一个虚拟班的相同学科进行拼班
                                Map<String,Object> pinJXBMap = getPinJXBEntry(retList);
                                Map<ObjectId,Integer> isSubjectPin = isSelectJXBMap.get((ObjectId) pinJXBMap.get("id"));
                                //将拼过教学班的学科标记为1（循环到该学科时则不拼班）
                                isSubjectPin.put((ObjectId) ((Map)pinJXBMap.get("retMap")).get("subId"),1);
                                isSelectJXBMap.put((ObjectId) pinJXBMap.get("id"),isSubjectPin);
                                ObjectId jxbId = getSelectJXBID(jxbEntryList,entry.getKey());
                                if(jxbId != null){
                                    ObjectId xuNiBanId = (ObjectId)pinJXBMap.get("id");
                                    String s = virtualClassService.selectJXB(xuNiBanId,jxbId,entry.getKey(),"*",schoolId);
                                    if(!s.equals("成功选择教学班")){
                                        break;
                                    }
                                }else{
                                    break;
                                }
                            }
                        }else{
                            Integer stuCount = entry.getValue();
                            List<Map<String,Object>> retList = getCanPinJXBList(xqid,schoolId,gradeId,entry.getKey(),rl,stuCount,xuNiBanEntry.getID(),type);
                            if (retList != null && retList.size() == 0) {
                                ObjectId jxbId = getAnyJXBBySubId(xqid,schoolId,gradeId, entry.getKey(),jxbType);
                                if(jxbId != null){
                                    String s = virtualClassService.selectJXB(xuNiBanEntry.getID(),jxbId,entry.getKey(),"*",schoolId);
                                    if(!s.equals("成功选择教学班")){
                                        break;
                                    }
                                }else{
                                    jxbId = getCanSelectJXBBySubId(xqid,schoolId,gradeId, entry.getKey(),stuCount,rl,jxbType);
                                    if(jxbId != null){
                                        String s = virtualClassService.selectJXB(xuNiBanEntry.getID(),jxbId,entry.getKey(),"*",schoolId);
                                        if(!s.equals("成功选择教学班")){
                                            break;
                                        }
                                    }
                                }
                                break;
                            } else {
                                //得到循环的虚拟班中的学科和哪一个虚拟班的相同学科进行拼班
                                Map<String,Object> pinJXBMap = getPinJXBEntry(retList);
                                Map<ObjectId,Integer> isSubjectPin = isSelectJXBMap.get((ObjectId) pinJXBMap.get("id"));

                                //将拼过教学班的学科标记为1（循环到该学科时则不拼班）
                                isSubjectPin.put((ObjectId) ((Map)pinJXBMap.get("retMap")).get("subId"),1);
                                isSelectJXBMap.put((ObjectId) pinJXBMap.get("id"),isSubjectPin);
                                ObjectId jxbId = getAnyJXBBySubId(xqid,schoolId,gradeId, entry.getKey(),jxbType);
                                if(jxbId == null){
                                    jxbId = getCanSelectJXBBySubId(xqid,schoolId,gradeId, entry.getKey(),stuCount,rl,jxbType);
                                }
                                if(jxbId != null){
                                    ObjectId xuNiBanId = (ObjectId)pinJXBMap.get("id");

                                    String s = virtualClassService.selectJXB(xuNiBanId,jxbId,entry.getKey(),"*",schoolId);
                                    if(!s.equals("成功选择教学班")){
                                        break;
                                    }

                                    //设置虚拟班的教学班，控制该教学班的该学科已经设置过教学班
                                    List<ObjectId> jxbIds1 = xuNiBanEntry.getJxbIds();
                                    jxbIds1.add(jxbId);
                                    xuNiBanEntry.setJxbIds(jxbIds1);
                                    String s1 = virtualClassService.selectJXB(xuNiBanEntry.getID(),jxbId,entry.getKey(),"*",schoolId);
                                    if(!s1.equals("成功选择教学班")){
                                        break;
                                    }
                                }else{
                                    break;
                                }
                            }
                        }
                    }
                }else{
                    continue;
                }
            }
        }
        return "操作成功";
    }

    /**
     * 获得该学科的任意一个空教学班
     * @return
     */
    private ObjectId getAnyJXBBySubId(ObjectId xqid,ObjectId sid,ObjectId gradeId,ObjectId subId,Integer jxbType){
    	//jxb add
        ObjectId ciId = getDefaultPaiKeTerm(sid).getPaikeci();
        int acClassType = turnOffService.getAcClassType(sid, gradeId, ciId,1);
        //jxb add
    	List<N33_JXBEntry> jxbEntryList = jxbDao.getJXBList(sid, gradeId, subId, xqid, jxbType, acClassType);
        ObjectId jxbId = null;
        for (N33_JXBEntry jxbEntry : jxbEntryList) {
            if(jxbEntry.getStudentIds().size() == 0){
                jxbId = jxbEntry.getID();
            }
        }
        return jxbId;
    }


    /**
     * 获得该学科可以放入的教学班
     * @return
     */
    private ObjectId getCanSelectJXBBySubId(ObjectId xqid,ObjectId sid,ObjectId gradeId,ObjectId subId,Integer stuCount,Integer rl,Integer jxbType){
    	//jxb add
    	 ObjectId ciId = getDefaultPaiKeTerm(sid).getPaikeci();
    	int acClassType = turnOffService.getAcClassType(sid, gradeId, ciId,1);
    	//jxb add
        List<N33_JXBEntry> jxbEntryList = jxbDao.getJXBList(sid, gradeId, subId, xqid, jxbType, acClassType);
        ObjectId jxbId = null;
        for (N33_JXBEntry jxbEntry : jxbEntryList) {
            if(jxbEntry.getStudentIds().size() + stuCount < rl){
                jxbId = jxbEntry.getID();
            }
        }
        return jxbId;
    }

    /**
     *  查找最适宜拼教学班的虚拟班中的学科
     * @param retList
     * @return
     */
    private Map<String,Object> getPinJXBEntry(List<Map<String,Object>> retList){
        Map<String,Object> pinJXBMap = new HashMap<String, Object>();
        double variance = 0.00;
        for (Map<String,Object> map : retList) {
            Map<String,Object> retMap = (Map<String, Object>) map.get("retMap");
            variance = (Double) retMap.get("variance");
            pinJXBMap = map;
            break;
        }
        for (Map<String,Object> map : retList) {
            Map<String,Object> retMap = (Map<String, Object>) map.get("retMap");
            if((Double)retMap.get("variance") <= variance){
                variance = (Double) retMap.get("variance");
                pinJXBMap = map;
            }
        }
        //Integer stuCount = (Integer) ((Map)pinJXBMap.get("retMap")).get("stuCount");
        //studentCount = stuCount + studentCount;
        return pinJXBMap;
    }

    /**
     * 查找可能拼在同一个教学班的学科
     * @param xqid
     * @param schoolId
     * @param gradeId
     * @param subId
     * @param rl
     * @return
     */
    private List<Map<String,Object>> getCanPinJXBList(ObjectId xqid,ObjectId schoolId,ObjectId gradeId,ObjectId subId,Integer rl,Integer studentCount,ObjectId xunibanID,Integer type){
        List<Map<String,Object>> retList = new ArrayList<Map<String, Object>>();
        //对应次的所有虚拟班
        List<N33_XuNiBanEntry> pinVirtualClassList = virtualClassDao.getN33_VirtualClassEntryByXqidAndGrade(xqid, schoolId, gradeId,type);

        for (N33_XuNiBanEntry xuNiBanEntry : pinVirtualClassList) {
            //该虚拟班的教学班ID集合
            List<ObjectId> jxbIds = xuNiBanEntry.getJxbIds();
            //对应虚拟班关联的教学班
            List<N33_JXBEntry> jxbEntryList = jxbDao.getJXBListByIds(jxbIds, xqid);
            //得到虚拟班中每一个学科的人数
            Map<ObjectId,Integer> subStuCountMap = getSubStudentCount(xuNiBanEntry);
            for (Map.Entry<ObjectId,Integer> entry : subStuCountMap.entrySet()){
                if(entry.getKey().toString().equals(subId.toString())){
                    if(entry.getValue() + studentCount > rl || isSelectJXB(jxbEntryList,entry.getKey()) || xuNiBanEntry.getID().toString().equals(xunibanID.toString())){
                        continue;
                    }else{
                        //该虚拟班中可以被拼班的某一个学科
                        Map<String,Object> map = new HashMap<String, Object>();
                        Map<String,Object> retMap = new HashMap<String, Object>();
                        //方差
                        double variance = Math.pow((entry.getValue() + studentCount - rl), 2);
                        map.put("subId",entry.getKey());
                        map.put("variance",variance);
                        map.put("stuCount",entry.getValue() + studentCount);
                        retMap.put("id",xuNiBanEntry.getID());
                        retMap.put("retMap",map);
                        retList.add(retMap);
                    }
                }
            }
        }
        return retList;

    }

    /**
     * 标记本次哪些虚拟班哪些学科已经设置过教学班
     * @param virtualClassList
     * @return
     */
    private Map<ObjectId, Map<ObjectId,Integer>> markIsOrNotSelectJXB(List<N33_XuNiBanEntry> virtualClassList,ObjectId xqid){
        //用于标记某个虚拟班中的某个学科本次是否已经分过教学班
        Map<ObjectId,Map<ObjectId,Integer>> map = new HashMap<ObjectId, Map<ObjectId, Integer>>();
        for (N33_XuNiBanEntry xuNiBanEntry : virtualClassList) {
            List<N33_XuNiBanEntry.StudentTag> studentTagList = xuNiBanEntry.getTagList();
            Map<ObjectId,Integer> subjectMap = new HashMap<ObjectId, Integer>();
            for (N33_XuNiBanEntry.StudentTag studentTag : studentTagList) {
                List<N33_XuNiBanEntry.SubjectInfo> subjectInfoList = studentTag.getSubjectList();
                for (N33_XuNiBanEntry.SubjectInfo subjectInfo : subjectInfoList) {
                    if(subjectMap.get(subjectInfo.getSubId()) == null){
                        subjectMap.put(subjectInfo.getSubId(),0);
                    }
                }
            }
            map.put(xuNiBanEntry.getID(),subjectMap);
        }
        return map;
    }

    /**
     * 检查某虚拟班的某一个学科是否设置过教学班
     * @param jxbEntryList  用来遍历的教学班，如果教学班集合中存在该学科的教学班，则说明该学科已经设置过教学班
     * @param subId     是否设置教学班的学科
     * @return
     */
    private boolean isSelectJXB(List<N33_JXBEntry> jxbEntryList,ObjectId subId){
        for (N33_JXBEntry jxbEntry : jxbEntryList) {
            if(jxbEntry.getSubjectId().toString().equals(subId.toString())){
                return true;
            }
        }
        return false;
    }

    /**
     * 如果已经选中了教学班，则获得该教学班的ID
     * @param jxbEntryList
     * @param subId
     * @return
     */
    private ObjectId getSelectJXBID(List<N33_JXBEntry> jxbEntryList,ObjectId subId){
        ObjectId jxbId = null;
        for (N33_JXBEntry jxbEntry : jxbEntryList) {
            if(jxbEntry.getSubjectId().toString().equals(subId.toString())){
                jxbId = jxbEntry.getID();
            }
        }
        return jxbId;
    }

    /**
     * 如果已经选中了教学班，则获得该教学班的ID
     * @param jxbEntryList
     * @param subId
     * @return
     */
    private N33_JXBEntry getSelectJXB(List<N33_JXBEntry> jxbEntryList,ObjectId subId){
        N33_JXBEntry jxbEntry = null;
        for (N33_JXBEntry jxbEntry1 : jxbEntryList) {
            if(jxbEntry1.getSubjectId().toString().equals(subId.toString())){
                jxbEntry = jxbEntry1;
            }
        }
        return jxbEntry;
    }


    /**
     * 获得某一个虚拟班的每一个学科的人数
     * @param xuNiBanEntry
     * @return
     */
    private Map<ObjectId,Integer> getSubStudentCount(N33_XuNiBanEntry xuNiBanEntry){
        Map<ObjectId,Integer> retMap = new HashMap<ObjectId, Integer>();
        List<N33_XuNiBanEntry.StudentTag> studentTagList = xuNiBanEntry.getTagList();
        for (N33_XuNiBanEntry.StudentTag studentTag : studentTagList) {
            List<N33_XuNiBanEntry.SubjectInfo> subjectInfoList = studentTag.getSubjectList();
            for (N33_XuNiBanEntry.SubjectInfo subjectInfo : subjectInfoList) {
                if(retMap.get(subjectInfo.getSubId()) == null){
                    retMap.put(subjectInfo.getSubId(),subjectInfo.getStuCount());
                }else{
                    Integer stuCount = retMap.get(subjectInfo.getSubId()) + subjectInfo.getStuCount();
                    retMap.put(subjectInfo.getSubId(),stuCount);
                }
            }
        }
        return retMap;
    }
}
