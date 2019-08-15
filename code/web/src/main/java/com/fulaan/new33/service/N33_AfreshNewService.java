package com.fulaan.new33.service;

import com.db.new33.ExamXYZLDao;
import com.db.new33.SchoolSelectLessonSetDao;
import com.db.new33.afresh.*;
import com.db.new33.isolate.ClassDao;
import com.db.new33.isolate.GradeDao;
import com.db.new33.isolate.N33_StudentDao;
import com.fulaan.new33.dto.N33_AfreshCompareDTO;
import com.fulaan.new33.dto.N33_AfreshLinDTO;
import com.fulaan.new33.dto.N33_AfreshMapDTO;
import com.fulaan.new33.dto.isolate.ClassInfoDTO;
import com.fulaan.new33.dto.isolate.N33_KSDTO;
import com.fulaan.new33.service.isolate.IsolateClassService;
import com.fulaan.new33.service.isolate.IsolateSubjectService;
import com.mongodb.DBObject;
import com.pojo.new33.ExamXYZLEntry;
import com.pojo.new33.SchoolSelectLessonSetEntry;
import com.pojo.new33.afresh.*;
import com.pojo.new33.isolate.ClassEntry;
import com.pojo.new33.isolate.Grade;
import com.pojo.new33.isolate.StudentEntry;
import com.pojo.utils.MongoUtils;
import com.sys.utils.CombineUtils;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by James on 2019-05-11.
 */
@Service
public class N33_AfreshNewService {

    private ExamXYZLDao examXYZLDao = new ExamXYZLDao();

    private AnalyzeUtils analyzeUtils = new AnalyzeUtils();

    private SchoolSelectLessonSetDao schoolSelectLessonSetDao =new SchoolSelectLessonSetDao();
    private IsolateSubjectService subjectService = new IsolateSubjectService();
    public static final Map<String,Integer> subject_sort = new HashMap<String, Integer>();
    public static final Map<String,Integer> subjectHead_sort = new HashMap<String, Integer>();
    private N33_StudentDao N33_StudentDao = new N33_StudentDao();
    private ClassDao classDao = new ClassDao();
    private GradeDao gradeDao = new GradeDao();

    private AfreshClassDao afreshClassDao = new AfreshClassDao();

    private AfreshClassRuleDao afreshClassRuleDao = new AfreshClassRuleDao();

    private AfreshClassSetDao afreshClassSetDao = new AfreshClassSetDao();

    private PerformanceXYZLDao performanceXYZLDao = new PerformanceXYZLDao();

    private AfreshTypeDao afreshTypeDao = new AfreshTypeDao();

    private AfreshSubRuleDao afreshSubRuleDao = new AfreshSubRuleDao();

    private IsolateClassService classService = new IsolateClassService();
    static {
        subject_sort.put("物理", 1);
        subject_sort.put("化学", 2);
        subject_sort.put("生物", 3);
        subject_sort.put("政治", 4);
        subject_sort.put("历史", 5);
        subject_sort.put("地理", 6);

        subjectHead_sort.put("物", 1);
        subjectHead_sort.put("化", 2);
        subjectHead_sort.put("生", 3);
        subjectHead_sort.put("政", 4);
        subjectHead_sort.put("历", 5);
        subjectHead_sort.put("地", 6);
    }

    //选择成绩单
    public String addAfreshSetEntry(ObjectId xqid,ObjectId gradeId,ObjectId reporId,String setId){
        if(StringUtils.isEmpty(setId)){
            //新建

        }else{
            //修改

        }
        return "";
    }


    public List<Map<String,Object>> getAfreshSubjectList(ObjectId xqid,ObjectId schoolId,ObjectId gradeId){
        List<Map<String,Object>> result = new ArrayList<Map<String, Object>>();
        List<StudentEntry> studentEntries = N33_StudentDao.getStudentByXqidAndGradeId(gradeId,xqid);
        Map<String,Integer> countMap = new HashMap<String, Integer>();
        for(StudentEntry studentEntry:studentEntries){
            if(!"".equals(studentEntry.getCombiname()) && studentEntry.getCombiname() != null){
                Integer integer = countMap.get(studentEntry.getCombiname())==null?0:countMap.get(studentEntry.getCombiname());
                integer++;
                countMap.put(studentEntry.getCombiname(),integer);
            }
        }
        List<String> combinationNames = generateCombination(xqid, schoolId, gradeId, 3);
        for(String str : combinationNames){
            Integer integer = countMap.get(str);
            if(integer!=null){
                Map<String,Object> map = new HashMap<String, Object>();
                map.put("name",str+"("+integer+")");
                map.put("subName",str);
                map.put("sum",integer);
                map.put("type",3);
                map.put("list",new ArrayList<Map<String,String>>());
                map.put("stuList",new ArrayList<Map<String,String>>());
                result.add(map);
            }
        }
        return result;
    }

    public String getNewString(String str){
        String strings = "!物历化生政地";
        if(str!=null && str.length()>2){
            String s1 = str.substring(0,1);
            String s2 = str.substring(1,2);
            String s3 = str.substring(2,3);
            int a1 = strings.indexOf(s1);
            int a2 = strings.indexOf(s2);
            int a3 = strings.indexOf(s3);
            if(a1>a2 && a2>a3){
                return s3+s2+s1;
            }else if(a1>a3 && a3>a2){
                return s2+s3+s1;
            }else if(a2>a1 && a1>a3){
                return s3+s1+s2;
            }else if(a2>a3 && a3>a1){
                return s1+s3+s2;
            }else if(a3>a2 && a2>a1){
                return s1+s2+s3;
            }else if(a3>a1 && a1>a2){
                return s2+s1+s3;
            }
        }
        return str;
    }


    public void createAfreshClassEntry(ObjectId xqid, ObjectId schoolId, ObjectId gradeId, String reportId, List<Map<String, Object>> ruleMapList) throws Exception {
        AfreshTypeEntry useEntry = afreshTypeDao.getUseAfreshTypeEntry(schoolId, gradeId, xqid);
        if(null!=useEntry&&useEntry.getAfreshType()==2){
            if(reportId==null || "".equals(reportId)){
                goAfreshClassEntry(xqid, schoolId, gradeId, reportId, ruleMapList);
            }else{
                dkyxAfreshClassEntry(xqid, schoolId, gradeId, reportId, ruleMapList, useEntry.getAfreshType());
            }
        }else {
            goAfreshClassEntry(xqid, schoolId, gradeId, reportId, ruleMapList);
        }
    }

    public void dkyxAfreshClassEntry(ObjectId ciId, ObjectId schoolId, ObjectId gradeId, String reportId, List<Map<String, Object>> ruleMapList, int afreshType) throws Exception {
        List<AfreshSubRuleEntry> asrEntries = afreshSubRuleDao.getEntries(schoolId, gradeId, ciId, afreshType);
        List<N33_KSDTO> ksList = subjectService.getIsolateSubjectByGradeIdIncludeMain(ciId, schoolId, gradeId.toString(),0);

        Map<ObjectId, AfreshSubRuleEntry> asrMap = new HashMap<ObjectId, AfreshSubRuleEntry>();
        List<ObjectId> yxSubIds = new ArrayList<ObjectId>();
        for(AfreshSubRuleEntry entry:asrEntries){
            yxSubIds.add(entry.getSubId());
            asrMap.put(entry.getSubId(), entry);
        }

        List<ObjectId> baseSubIds = new ArrayList<ObjectId>();
        for(N33_KSDTO ksDto:ksList){
            if (ksDto.getSnm().equals("语文") || ksDto.getSnm().equals("数学") || ksDto.getSnm().equals("英语")) {
                baseSubIds.add(new ObjectId(ksDto.getSubid()));
            }
        }

        //获取成绩单
        ObjectId examId = new ObjectId(reportId);
        Map<ObjectId, Map<ObjectId, Double>> stuSubScoreMMap = getStuSubScoreMMap(examId);

        //所有学生选课
        List<StudentEntry> stuEntries = N33_StudentDao.getStudentByXqidAndGradeId(gradeId, ciId);
        Map<ObjectId, StudentEntry> stuMap = new HashMap<ObjectId, StudentEntry>();
        Map<ObjectId, List<Map>> subStusMap = new HashMap<ObjectId, List<Map>>();

        for(StudentEntry stu:stuEntries){
            stuMap.put(stu.getUserId(), stu);
            List<ObjectId> stuSubIds = new ArrayList<ObjectId>();
            stuSubIds.add(stu.getSubjectId1());
            stuSubIds.add(stu.getSubjectId2());
            stuSubIds.add(stu.getSubjectId3());
            ObjectId yxSubId = null;
            for(ObjectId subId:yxSubIds){
                if(stuSubIds.contains(subId)){
                    yxSubId = subId;
                }
            }
            if(null==yxSubId){
                continue;
            }
            Map stuScoreMap = new HashMap();
            Map<ObjectId, Double> subScoreMap = stuSubScoreMMap.get(stu.getUserId());
            double stuTotalScore = 0.0;
            if(null!=subScoreMap){
                for(ObjectId subId:baseSubIds){
                    double subScore = subScoreMap.get(subId)==null?0.0:subScoreMap.get(subId);
                    stuTotalScore += subScore;
                }
                double subScore = subScoreMap.get(yxSubId)==null?0.0:subScoreMap.get(yxSubId);
                stuTotalScore += subScore;
            }
            stuScoreMap.put("stuId", stu.getUserId());
            stuScoreMap.put("totalScore", stuTotalScore);
            List<Map> subStus = subStusMap.get(yxSubId);
            if(null==subStus){
                subStus = new ArrayList<Map>();
            }
            subStus.add(stuScoreMap);
            subStusMap.put(yxSubId, subStus);
        }

        Grade grade = gradeDao.findIsolateGradeEntryByGradeId(gradeId, ciId);
        AfreshClassSetEntry afreshClassSetEntry = new AfreshClassSetEntry(ciId, schoolId, gradeId, examId,1,0, new ArrayList<ObjectId>());
        afreshClassSetEntry.setIsRemove(1);
        afreshClassSetDao.addEntry(afreshClassSetEntry);
        ObjectId setId = afreshClassSetEntry.getID();

        List<ObjectId> yxStuIds = new ArrayList<ObjectId>();

        int classIndex = 1;
        //分配学生，生成班级
        List<DBObject> addEntries = new ArrayList<DBObject>();

        for(ObjectId subId:yxSubIds){
            AfreshSubRuleEntry asrEntry = asrMap.get(subId);
            if(null!=asrEntry) {
                int yxStuCount = asrEntry.getNumber()*asrEntry.getVolume();
                List<Map> yxSubStus = new ArrayList<Map>();
                List<Map> subStus = subStusMap.get(subId);
                if(null!=subStus){
                    stuScoreListSort(subStus);
                    for(Map stu:subStus){
                        if(yxStuCount>yxSubStus.size()){
                            yxSubStus.add(stu);
                        }else{
                            break;
                        }
                    }
                    List<StudentEntry> yxStuEntries = new ArrayList<StudentEntry>();
                    for(Map stu: yxSubStus){
                        ObjectId stuId = (ObjectId)stu.get("stuId");
                        StudentEntry stuEntry = stuMap.get(stuId);
                        yxStuEntries.add(stuEntry);
                        yxStuIds.add(stuId);
                    }
                    classIndex = createYxClass(schoolId, ciId, setId, grade, yxStuEntries, addEntries, classIndex, asrEntry.getVolume());
                }
            }
        }

        List<StudentEntry> fyxStuEntries = new ArrayList<StudentEntry>();
        for(StudentEntry stu:stuEntries){
            if(!yxStuIds.contains(stu.getUserId())){
                fyxStuEntries.add(stu);
            }
        }

        createFYxClass(schoolId, ciId, setId, reportId, grade, fyxStuEntries, addEntries, classIndex, ruleMapList);

        //保存分班结果
        afreshClassDao.addAfreshClassEntry(addEntries);
        //修改为最新分班结果
        afreshClassSetDao.updateOld(ciId, schoolId, gradeId);
        afreshClassSetDao.updateNew(setId);
        System.out.println("分班结束！");
    }

    public void createFYxClass(
            ObjectId schoolId,
            ObjectId ciId,
            ObjectId setId,
            String reportId,
            Grade grade,
            List<StudentEntry> fyxStuEntries,
            List<DBObject> addEntries,
            Integer classIndex,
            List<Map<String, Object>> ruleMapList
    ) throws Exception {
        Map<String, Integer> countMap = new HashMap<String, Integer>();
        Map<String, List<StudentEntry>> studentMap = new HashMap<String, List<StudentEntry>>();
        for(StudentEntry studentEntry:fyxStuEntries){
            if(!"".equals(studentEntry.getCombiname()) && studentEntry.getCombiname() != null){
                //数量
                String co = getNewString(studentEntry.getCombiname());
                studentEntry.setCombiname(co);
                Integer integer = countMap.get(co)==null?0:countMap.get(co);
                integer++;
                countMap.put(co,integer);
                //学生分布
                List<StudentEntry> entryList = studentMap.get(co)==null?new ArrayList<StudentEntry>():studentMap.get(co);
                entryList.add(studentEntry);
                studentMap.put(co, entryList);
            }
        }
        Map<String, List<N33_AfreshMapDTO>> ruleMap = new HashMap<String, List<N33_AfreshMapDTO>>();
        //1.设置分段
        //保存每种组合的容量浮动信息（以最终数据为准）
        Map<String, Map<String, Integer>> volumeMap = new HashMap<String, Map<String, Integer>>();
        Collections.shuffle(ruleMapList);
        for(Map<String,Object> map :ruleMapList){
            //第一次分配，优先保持班级数量成功实现
            //班级数量
            String allNumber = (String)map.get("number");
            //班级容量
            String allVolume = (String)map.get("volume");
            //班级浮动
            String allSwim = (String)map.get("swim");
            //分班类型
            String allType = (String)map.get("type");
            //组合
            String compose = getNewString((String)map.get("compose"));

            AfreshClassRuleEntry afreshClassRuleEntry = new AfreshClassRuleEntry(setId,allType,allNumber,allVolume,allSwim,compose);
            afreshClassRuleDao.addEntry(afreshClassRuleEntry);

            Integer count = countMap.get(compose)==null?0:countMap.get(compose);

            getFirstRuleType(count, allNumber, allVolume, allSwim, allType, compose, ruleMap, volumeMap);
        }
        //2.最小余量分班(重点班优先分配)
        //余量统计
        Map<String,Integer> exMap = new HashMap<String, Integer>();

        for(Map.Entry<String,List<N33_AfreshMapDTO>> ma : ruleMap.entrySet()){
            String com= ma.getKey();
            List<N33_AfreshMapDTO> duan = ma.getValue();
            Integer  allStudent = countMap.get(com)==null?0:countMap.get(com);
            int shen = allStudent;
            //首先满额分配获得 剩余的人数
            for(N33_AfreshMapDTO dto : duan){
                List<N33_AfreshLinDTO> classDtoList = new ArrayList<N33_AfreshLinDTO>();
                int duanMin = dto.getVolume() - dto.getSwim();
                //组装端班级
                for(int n = 0; n < dto.getNumber() ; n++){
                    //判断本次是否满足
                    int min  = dto.getVolume()-dto.getSwim();
                    int max  = dto.getVolume()+dto.getSwim();

                    if(shen <= min){
                        //剩余量小于最小容量，待走班人数
                        exMap.put(dto.getCompose(),shen);
                        break;
                    }
                    N33_AfreshLinDTO n33_afreshLinDTO = new N33_AfreshLinDTO();
                    n33_afreshLinDTO.setType(dto.getType());
                    n33_afreshLinDTO.setOrder(dto.getOrder());
                    n33_afreshLinDTO.setMainLevel(getStringIndex(dto.getCompose(),2));
                    n33_afreshLinDTO.setLevel(getStringIndex(dto.getCompose(),3));
                    n33_afreshLinDTO.setSubjectType(getStringIndex(dto.getCompose(),1));
                    n33_afreshLinDTO.setMin(min);
                    n33_afreshLinDTO.setMax(max);

                    if(shen > dto.getVolume()){
                        //剩余量大于容量，满额分配
                        n33_afreshLinDTO.setNumber(dto.getVolume());
                        n33_afreshLinDTO.setStringList(dto.getCompose()+"#"+dto.getVolume());

                        classDtoList.add(n33_afreshLinDTO);
                    }else if(shen > min){
                        //剩余量大于最小容量，非满额分配
                        n33_afreshLinDTO.setNumber(shen);
                        exMap.put(dto.getCompose(),0);
                        n33_afreshLinDTO.setStringList(dto.getCompose()+"#"+shen);
                        classDtoList.add(n33_afreshLinDTO);
                        break;
                    }

                    //更新剩余量
                    shen = shen - dto.getVolume();
                }
                dto.setClassList(classDtoList);
                if(shen <= duanMin){
                    //剩余量小于最小容量，待走班人数
                    exMap.put(dto.getCompose(),shen);
                    break;
                }
            }
        }

        //3.剩余走班
        //物理走班
        List<N33_AfreshCompareDTO> wuMapList = new ArrayList<N33_AfreshCompareDTO>();

        //历史走班
        List<N33_AfreshCompareDTO> liMapList = new ArrayList<N33_AfreshCompareDTO>();

        //统计走班
        for(Map.Entry<String,Integer> entry : exMap.entrySet()){
            String string = entry.getKey();
            for(Map.Entry<String,Map<String,Integer>> vo : volumeMap.entrySet()){
                String string2 = vo.getKey();
                Map<String,Integer> voKey = vo.getValue();
                if(string!=null && string.equals(string2)){
                    N33_AfreshCompareDTO m = new N33_AfreshCompareDTO();
                    m.setCount(entry.getValue());
                    m.setVolume(voKey.get("volume"));
                    m.setSwim(voKey.get("swim"));
                    m.setCompose(string);
                    int type = getStringIndex(string, 1);
                    m.setN1(type);
                    m.setN2(getStringIndex(string,2));
                    m.setN3(getStringIndex(string, 3));
                    if(type==1){
                        wuMapList.add(m);
                    }else if(type==2){
                        liMapList.add(m);
                    }
                }
            }
        }

        //最终结果
        List<N33_AfreshLinDTO> resultList = new ArrayList<N33_AfreshLinDTO>();

        //物理分班
        getSecondRuleType(wuMapList, ruleMap, resultList);

        //历史分班
        getSecondRuleType(liMapList, ruleMap, resultList);

        //分班
        if(reportId!=null && !reportId.equals("")){
            //获取成绩单
            ObjectId rid = new ObjectId(reportId);
            Map<ObjectId, Double> map= new HashMap<ObjectId, Double>();
            getThreeRuleType(rid, map);
            //按成绩排名
            for(Map.Entry<String,List<StudentEntry>> m:studentMap.entrySet()){
                //排序
                try{
                    List<StudentEntry> studentEntryList =m.getValue();
                    for(StudentEntry s:studentEntryList){
                        Double d  = map.get(s.getUserId());
                        if(d!=null){
                            int c = (int)(double)d;
                            s.setLevel(c);
                        }else{
                            System.out.print("233");
                        }
                    }
                    Collections.sort(studentEntryList, new Comparator<StudentEntry>() {
                        @Override
                        public int compare(StudentEntry o1, StudentEntry o2) {
                            if (o1.getLevel() > o2.getLevel()) {
                                return -1;
                            } else if ((o1.getLevel() == o2.getLevel())){
                                return 0;
                            } else {
                                return 1;
                            }
                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            //段数分配
            for(Map.Entry<String,List<N33_AfreshMapDTO>> entry  : ruleMap.entrySet()){
                List<N33_AfreshMapDTO> dtos = entry.getValue();
                for(N33_AfreshMapDTO dto : dtos){
                    List<N33_AfreshLinDTO> list = dto.getClassList();
                    Map<String,Integer> stringIntegerMap = new HashMap<String, Integer>();
                    for(N33_AfreshLinDTO dto1 : list) {
                        String cs = dto1.getStringList();
                        if (cs != null && !cs.equals("")) {
                            String[] comList = cs.split(",");
                            for (String ct : comList) {
                                String[] cosList2 = ct.split("#");
                                String  newCom = cosList2[0];
                                int  newNum = Integer.parseInt(cosList2[1]);
                                Integer integer = stringIntegerMap.get(newCom)==null?0:stringIntegerMap.get(newCom);
                                integer += newNum;
                                stringIntegerMap.put(newCom,integer);
                            }
                        }
                    }
                    //取人到对应段
                    List<StudentEntry> studentEntries0 = new ArrayList<StudentEntry>();
                    for(Map.Entry<String,Integer> ism : stringIntegerMap.entrySet()){
                        Integer integer = ism.getValue();
                        String com = ism.getKey();
                        List<StudentEntry> studentEntryList = studentMap.get(com);
                        int index = 0;
                        for(StudentEntry studentEntry: studentEntryList){
                            if(index<integer && studentEntry.getSex()!=100){
                                //表明已被使用
                                studentEntry.setSex(100);
                                studentEntries0.add(studentEntry);
                                index++;
                            }
                        }
                    }
                    dto.setStudentEntryList(studentEntries0);
                }
            }

            //段数蛇形
            for(Map.Entry<String,List<N33_AfreshMapDTO>> entry  : ruleMap.entrySet()){
                List<N33_AfreshMapDTO>  dtos = entry.getValue();
                for(N33_AfreshMapDTO dto : dtos){
                    List<N33_AfreshLinDTO> list = dto.getClassList();
                    //分配
                    if(list.size()>0){
                        getSheClass(dto.getStudentEntryList(),list);
                    }
                }
            }

            //剩余班直接分配
            for(N33_AfreshLinDTO linDTO : resultList){
                String cs = linDTO.getStringList();
                if (cs != null && !cs.equals("")) {
                    String[] comList = cs.split(",");
                    List<StudentEntry> studentEntries0 = new ArrayList<StudentEntry>();
                    for (String ct : comList) {
                        String[] cosList2 = ct.split("#");
                        String  newCom = cosList2[0];
                        int  newNum = Integer.parseInt(cosList2[1]);
                        List<StudentEntry> studentEntries1 = studentMap.get(newCom);
                        int index = 0;
                        for(StudentEntry studentEntry:studentEntries1){
                            if(index<newNum && studentEntry.getSex()!=100){
                                //表明已被使用
                                studentEntry.setSex(100);
                                studentEntries0.add(studentEntry);
                                index++;
                            }
                        }
                    }
                    linDTO.setStudentEntryList(studentEntries0);
                }
            }

        }else{
            //随机
            //段数分配
            for(Map.Entry<String,List<N33_AfreshMapDTO>> entry  : ruleMap.entrySet()){
                List<N33_AfreshMapDTO> dtos = entry.getValue();
                for(N33_AfreshMapDTO dto : dtos){
                    System.out.println(dto.toString());
                    List<N33_AfreshLinDTO> list = dto.getClassList();
                    for(N33_AfreshLinDTO dto1 : list) {
                        String cs = zuheXuanze(dto1.getStringList());
                        if (cs != null && !cs.equals("")) {
                            String[] comList = cs.split(",");
                            List<StudentEntry> studentEntries0 = new ArrayList<StudentEntry>();
                            for (String ct : comList) {
                                String[] cosList2 = ct.split("#");
                                String  newCom = cosList2[0];
                                int  newNum = Integer.parseInt(cosList2[1]);
                                List<StudentEntry> studentEntries1 = studentMap.get(newCom);
                                int index = 0;
                                for(StudentEntry studentEntry:studentEntries1){
                                    if(index<newNum && studentEntry.getSex()!=100){
                                        //表明已被使用
                                        studentEntry.setSex(100);
                                        studentEntries0.add(studentEntry);
                                        index++;
                                    }
                                }
                            }
                            dto1.setStudentEntryList(studentEntries0);
                        }
                    }
                }
            }

            //临时分配
            for(N33_AfreshLinDTO linDTO : resultList){
                String cs = linDTO.getStringList();
                if (cs != null && !cs.equals("")) {
                    String[] comList = cs.split(",");
                    List<StudentEntry> studentEntries0 = new ArrayList<StudentEntry>();
                    for (String ct : comList) {
                        String[] cosList2 = ct.split("#");
                        String  newCom = cosList2[0];
                        int  newNum = Integer.parseInt(cosList2[1]);
                        List<StudentEntry> studentEntries1 = studentMap.get(newCom);
                        int index = 0;
                        for(StudentEntry studentEntry :studentEntries1){
                            if(index<newNum && studentEntry.getSex()!=100){
                                //表明已被使用
                                studentEntry.setSex(100);
                                studentEntries0.add(studentEntry);
                                index++;
                            }
                        }
                    }
                    linDTO.setStudentEntryList(studentEntries0);
                }
            }
        }

        for(Map.Entry<String, List<N33_AfreshMapDTO>> entry  : ruleMap.entrySet()){
            List<N33_AfreshMapDTO> dtos = entry.getValue();
            for(N33_AfreshMapDTO dto : dtos){
                //System.out.println(dto.toString());
                List<N33_AfreshLinDTO> list = dto.getClassList();
                for(N33_AfreshLinDTO dto1 : list){
                    List<StudentEntry> studentEntries1 = dto1.getStudentEntryList();
                    List<ObjectId> objectIdList = new ArrayList<ObjectId>();
                    for(StudentEntry s:studentEntries1){
                        objectIdList.add(s.getUserId());
                    }
                    if(objectIdList.size()>0){
                        String className = grade.getName() + "(" + classIndex + ")" + "班";
                        AfreshClassEntry afreshClassEntry =  new AfreshClassEntry(
                                ciId,
                                schoolId,
                                className,
                                objectIdList,
                                "",
                                null,
                                1,
                                grade.getGradeId(),
                                "",
                                classIndex,
                                setId);
                        addEntries.add(afreshClassEntry.getBaseEntry());
                        classIndex++;
                    }
                }
            }
        }

        for(N33_AfreshLinDTO dto1 : resultList){
            List<StudentEntry> studentEntries1 = dto1.getStudentEntryList();
            List<ObjectId> objectIdList = new ArrayList<ObjectId>();
            for(StudentEntry s:studentEntries1){
                objectIdList.add(s.getUserId());
            }
            if(objectIdList.size()>0){
                String className = grade.getName() + "(" + classIndex + ")" + "班";
                AfreshClassEntry afreshClassEntry =  new AfreshClassEntry(
                        ciId,
                        schoolId,
                        className,
                        objectIdList,
                        "",
                        null,
                        1,
                        grade.getGradeId(),
                        "",
                        classIndex,
                        setId);
                addEntries.add(afreshClassEntry.getBaseEntry());
                classIndex++;
            }
        }
    }

    public int createYxClass(
            ObjectId schoolId,
            ObjectId ciId,
            ObjectId setId,
            Grade grade,
            List<StudentEntry> yxStuEntries,
            List<DBObject> addEntries,
            int classIndex,
            int volume
    ) throws Exception {
        List<List<ObjectId>> stuIdsList = new ArrayList<List<ObjectId>>();
        int index = 0;
        for(StudentEntry stu:yxStuEntries){
            List<ObjectId> stuIds = null;
            if(stuIdsList.size()>index) {
                stuIds = stuIdsList.get(index);
            }
            if(null==stuIds){
                stuIds = new ArrayList<ObjectId>();
                stuIdsList.add(stuIds);
            }
            if(volume>stuIds.size()){
                stuIds.add(stu.getUserId());
            }else{
                index++;
            }
        }
        for(List<ObjectId> stuIds:stuIdsList) {
            String className = grade.getName() + "(" + classIndex + ")" + "班";
            AfreshClassEntry entry = new AfreshClassEntry(
                    ciId,
                    schoolId,
                    className,
                    stuIds,
                    "",
                    null,
                    1,
                    grade.getGradeId(),
                    "",
                    classIndex,
                    setId
            );
            addEntries.add(entry.getBaseEntry());
            classIndex++;
        }
        return classIndex;
    }


    /**
     * 根据教学班学生密度排序 高->低
     * @param subMiDuList
     */
    public void stuScoreListSort(List<Map> subMiDuList) {
        Collections.sort(subMiDuList, new Comparator<Map>() {
            @Override
            public int compare(Map arg0, Map arg1) {
                Double totalScore0 = arg0.get("totalScore") == null ? 0.0 : (Double) arg0.get("totalScore");
                Double totalScore1 = arg1.get("totalScore") == null ? 0.0 : (Double) arg1.get("totalScore");
                return totalScore1.compareTo(totalScore0);
            }
        });
    }


    public Map<ObjectId, Map<ObjectId, Double>> getStuSubScoreMMap(ObjectId examId) {
        Map<ObjectId, Map<ObjectId, Double>> stuSubScoreMMap = new HashMap<ObjectId, Map<ObjectId, Double>>();
        List<PerformanceXYZLEntry> entries = performanceXYZLDao.findPerformanceList(examId);
        for(PerformanceXYZLEntry entry : entries){
            Map<ObjectId, Double> subScoreMap = stuSubScoreMMap.get(entry.getStudentId());
            if(null==subScoreMap){
                subScoreMap = new HashMap<ObjectId, Double>();
            }
            List<Score> scoreList =  entry.getScoreList();
            for(Score score:scoreList){
                //
                subScoreMap.put(score.getSubjectId(), score.getSubjectScore());
            }
            stuSubScoreMMap.put(entry.getStudentId(), subScoreMap);
        }
        return stuSubScoreMMap;
    }


    public void goAfreshClassEntry(ObjectId xqid, ObjectId schoolId,ObjectId gradeId,String reportId,List<Map<String,Object>> ruleMapList) throws Exception{
        //所有学生选课
        List<StudentEntry> studentEntries = N33_StudentDao.getStudentByXqidAndGradeId(gradeId,xqid);
        Grade grade = gradeDao.findIsolateGradeEntryByGradeId(gradeId,xqid);
        Map<String, Integer> countMap = new HashMap<String, Integer>();
        Map<String, List<StudentEntry>> studentMap = new HashMap<String, List<StudentEntry>>();
        for(StudentEntry studentEntry:studentEntries){
            if(!"".equals(studentEntry.getCombiname()) && studentEntry.getCombiname() != null){
                    //数量
                String co = getNewString(studentEntry.getCombiname());
                studentEntry.setCombiname(co);
                Integer integer = countMap.get(co)==null?0:countMap.get(co);
                integer++;
                countMap.put(co,integer);
                //学生分布
                List<StudentEntry> entryList = studentMap.get(co)==null?new ArrayList<StudentEntry>():studentMap.get(co);
                entryList.add(studentEntry);
                studentMap.put(co, entryList);
            }
        }
        Map<String, List<N33_AfreshMapDTO>> ruleMap = new HashMap<String, List<N33_AfreshMapDTO>>();
        //解析并保存规则
        ObjectId reId = StringUtils.isEmpty(reportId)?null:new ObjectId(reportId);
        AfreshClassSetEntry afreshClassSetEntry = new AfreshClassSetEntry(xqid, schoolId,gradeId,reId,1,0, new ArrayList<ObjectId>());
        afreshClassSetEntry.setIsRemove(1);
        afreshClassSetDao.addEntry(afreshClassSetEntry);
        ObjectId setId = afreshClassSetEntry.getID();
        //1.设置分段
        //保存每种组合的容量浮动信息（以最终数据为准）
        Map<String, Map<String, Integer>> volumeMap = new HashMap<String, Map<String, Integer>>();
        Collections.shuffle(ruleMapList);
        for(Map<String,Object> map :ruleMapList){
            //第一次分配，优先保持班级数量成功实现
            //班级数量
            String allNumber = (String)map.get("number");
            //班级容量
            String allVolume = (String)map.get("volume");
            //班级浮动
            String allSwim = (String)map.get("swim");
            //分班类型
            String allType = (String)map.get("type");
            //组合
            String compose = getNewString((String)map.get("compose"));

            AfreshClassRuleEntry afreshClassRuleEntry = new AfreshClassRuleEntry(setId,allType,allNumber,allVolume,allSwim,compose);
            afreshClassRuleDao.addEntry(afreshClassRuleEntry);

            Integer count = countMap.get(compose)==null?0:countMap.get(compose);

            getFirstRuleType(count, allNumber, allVolume, allSwim, allType, compose, ruleMap, volumeMap);
        }
        //2.最小余量分班(重点班优先分配)
        //余量统计
        Map<String,Integer> exMap = new HashMap<String, Integer>();

        for(Map.Entry<String,List<N33_AfreshMapDTO>> ma : ruleMap.entrySet()){
            String com= ma.getKey();
            List<N33_AfreshMapDTO> duan = ma.getValue();
            Integer  allStudent = countMap.get(com)==null?0:countMap.get(com);
            int shen = allStudent;
            //首先满额分配获得 剩余的人数
            for(N33_AfreshMapDTO dto : duan){
                System.out.print(dto.toString());
                List<N33_AfreshLinDTO> classDtoList = new ArrayList<N33_AfreshLinDTO>();
                int duanMin = dto.getVolume() - dto.getSwim();
                //组装端班级
                for(int n = 0; n < dto.getNumber() ; n++){
                    //判断本次是否满足
                    int min  = dto.getVolume()-dto.getSwim();
                    int max  = dto.getVolume()+dto.getSwim();

                    if(shen <= min){
                        //剩余量小于最小容量，待走班人数
                        exMap.put(dto.getCompose(),shen);
                        break;
                    }
                    N33_AfreshLinDTO n33_afreshLinDTO = new N33_AfreshLinDTO();
                    n33_afreshLinDTO.setType(dto.getType());
                    n33_afreshLinDTO.setOrder(dto.getOrder());
                    n33_afreshLinDTO.setMainLevel(getStringIndex(dto.getCompose(),2));
                    n33_afreshLinDTO.setLevel(getStringIndex(dto.getCompose(),3));
                    n33_afreshLinDTO.setSubjectType(getStringIndex(dto.getCompose(),1));
                    n33_afreshLinDTO.setMin(min);
                    n33_afreshLinDTO.setMax(max);

                    if(shen > dto.getVolume()){
                        //剩余量大于容量，满额分配
                        n33_afreshLinDTO.setNumber(dto.getVolume());
                        n33_afreshLinDTO.setStringList(dto.getCompose()+"#"+dto.getVolume());

                        classDtoList.add(n33_afreshLinDTO);
                    }else if(shen > min){
                        //剩余量大于最小容量，非满额分配
                        n33_afreshLinDTO.setNumber(shen);
                        exMap.put(dto.getCompose(),0);
                        n33_afreshLinDTO.setStringList(dto.getCompose()+"#"+shen);
                        classDtoList.add(n33_afreshLinDTO);
                        break;
                    }

                    //更新剩余量
                    shen = shen - dto.getVolume();
                }
                dto.setClassList(classDtoList);
                if(shen <= duanMin){
                    //剩余量小于最小容量，待走班人数
                    exMap.put(dto.getCompose(),shen);
                    break;
                }
            }
        }

        //3.剩余走班
        //物理走班
        List<N33_AfreshCompareDTO> wuMapList = new ArrayList<N33_AfreshCompareDTO>();

        //历史走班
        List<N33_AfreshCompareDTO> liMapList = new ArrayList<N33_AfreshCompareDTO>();

        //统计走班
        for(Map.Entry<String,Integer> entry : exMap.entrySet()){
            String string = entry.getKey();
            for(Map.Entry<String,Map<String,Integer>> vo : volumeMap.entrySet()){
                String string2 = vo.getKey();
                Map<String,Integer> voKey = vo.getValue();
                if(string!=null && string.equals(string2)){
                    N33_AfreshCompareDTO m = new N33_AfreshCompareDTO();
                    m.setCount(entry.getValue());
                    m.setVolume(voKey.get("volume"));
                    m.setSwim(voKey.get("swim"));
                    m.setCompose(string);
                    int type = getStringIndex(string, 1);
                    m.setN1(type);
                    m.setN2(getStringIndex(string,2));
                    m.setN3(getStringIndex(string, 3));
                    if(type==1){
                        wuMapList.add(m);
                    }else if(type==2){
                        liMapList.add(m);
                    }
                }
            }
        }

        //最终结果
        List<N33_AfreshLinDTO> resultList = new ArrayList<N33_AfreshLinDTO>();

        //物理分班
        getSecondRuleType(wuMapList, ruleMap, resultList);

        //历史分班
        getSecondRuleType(liMapList, ruleMap, resultList);

        //分班
        if(reportId!=null && !reportId.equals("")){
            //获取成绩单
            ObjectId rid = new ObjectId(reportId);
            Map<ObjectId, Double> map= new HashMap<ObjectId, Double>();
            getThreeRuleType(rid, map);
            //按成绩排名
            for(Map.Entry<String,List<StudentEntry>> m:studentMap.entrySet()){
                //排序
                try{
                    List<StudentEntry> studentEntryList =m.getValue();
                    for(StudentEntry s:studentEntryList){
                        Double d  = map.get(s.getUserId());
                        if(d!=null){
                            int c = (int)(double)d;
                            s.setLevel(c);
                        }else{
                            System.out.print("233");
                        }
                    }
                    Collections.sort(studentEntryList, new Comparator<StudentEntry>() {
                        @Override
                        public int compare(StudentEntry o1, StudentEntry o2) {
                            if (o1.getLevel() > o2.getLevel()) {
                                return -1;
                            } else if ((o1.getLevel() == o2.getLevel())){
                                return 0;
                            } else {
                                return 1;
                            }
                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            //段数分配
            for(Map.Entry<String,List<N33_AfreshMapDTO>> entry  : ruleMap.entrySet()){
                List<N33_AfreshMapDTO> dtos = entry.getValue();
                for(N33_AfreshMapDTO dto : dtos){
                    List<N33_AfreshLinDTO> list = dto.getClassList();
                    Map<String,Integer> stringIntegerMap = new HashMap<String, Integer>();
                    for(N33_AfreshLinDTO dto1 : list) {
                        String cs = dto1.getStringList();
                        if (cs != null && !cs.equals("")) {
                            String[] comList = cs.split(",");
                            for (String ct : comList) {
                                String[] cosList2 = ct.split("#");
                                String  newCom = cosList2[0];
                                int  newNum = Integer.parseInt(cosList2[1]);
                                Integer integer = stringIntegerMap.get(newCom)==null?0:stringIntegerMap.get(newCom);
                                integer += newNum;
                                stringIntegerMap.put(newCom,integer);
                            }
                        }
                    }
                    //取人到对应段
                    List<StudentEntry> studentEntries0 = new ArrayList<StudentEntry>();
                    for(Map.Entry<String,Integer> ism : stringIntegerMap.entrySet()){
                        Integer integer = ism.getValue();
                        String com = ism.getKey();
                        List<StudentEntry> studentEntryList = studentMap.get(com);
                        int index = 0;
                        for(StudentEntry studentEntry: studentEntryList){
                            if(index<integer && studentEntry.getSex()!=100){
                                //表明已被使用
                                studentEntry.setSex(100);
                                studentEntries0.add(studentEntry);
                                index++;
                            }
                        }
                    }
                    dto.setStudentEntryList(studentEntries0);
                }
            }

            //段数蛇形
            for(Map.Entry<String,List<N33_AfreshMapDTO>> entry  : ruleMap.entrySet()){
                List<N33_AfreshMapDTO>  dtos = entry.getValue();
                for(N33_AfreshMapDTO dto : dtos){
                    List<N33_AfreshLinDTO> list = dto.getClassList();
                    //分配
                    if(list.size()>0){
                        getSheClass(dto.getStudentEntryList(),list);
                    }
                }
            }

            //剩余班直接分配
            for(N33_AfreshLinDTO linDTO : resultList){
                String cs = linDTO.getStringList();
                if (cs != null && !cs.equals("")) {
                    String[] comList = cs.split(",");
                    List<StudentEntry> studentEntries0 = new ArrayList<StudentEntry>();
                    for (String ct : comList) {
                        String[] cosList2 = ct.split("#");
                        String  newCom = cosList2[0];
                        int  newNum = Integer.parseInt(cosList2[1]);
                        List<StudentEntry> studentEntries1 = studentMap.get(newCom);
                        int index = 0;
                        for(StudentEntry studentEntry:studentEntries1){
                            if(index<newNum && studentEntry.getSex()!=100){
                                //表明已被使用
                                studentEntry.setSex(100);
                                studentEntries0.add(studentEntry);
                                index++;
                            }
                        }
                    }
                    linDTO.setStudentEntryList(studentEntries0);
                }
            }

        }else{
            //随机
            //段数分配
            for(Map.Entry<String,List<N33_AfreshMapDTO>> entry  : ruleMap.entrySet()){
                List<N33_AfreshMapDTO> dtos = entry.getValue();
                for(N33_AfreshMapDTO dto : dtos){
                    System.out.println(dto.toString());
                    List<N33_AfreshLinDTO> list = dto.getClassList();
                    for(N33_AfreshLinDTO dto1 : list) {
                        String cs = zuheXuanze(dto1.getStringList());
                        if (cs != null && !cs.equals("")) {
                            String[] comList = cs.split(",");
                            List<StudentEntry> studentEntries0 = new ArrayList<StudentEntry>();
                            for (String ct : comList) {
                                String[] cosList2 = ct.split("#");
                                String  newCom = cosList2[0];
                                int  newNum = Integer.parseInt(cosList2[1]);
                                List<StudentEntry> studentEntries1 = studentMap.get(newCom);
                                int index = 0;
                                for(StudentEntry studentEntry:studentEntries1){
                                    if(index<newNum && studentEntry.getSex()!=100){
                                        //表明已被使用
                                        studentEntry.setSex(100);
                                        studentEntries0.add(studentEntry);
                                        index++;
                                    }
                                }
                            }
                            dto1.setStudentEntryList(studentEntries0);
                        }
                    }
                }
            }

            //临时分配
            for(N33_AfreshLinDTO linDTO : resultList){
                String cs = linDTO.getStringList();
                if (cs != null && !cs.equals("")) {
                    String[] comList = cs.split(",");
                    List<StudentEntry> studentEntries0 = new ArrayList<StudentEntry>();
                    for (String ct : comList) {
                        String[] cosList2 = ct.split("#");
                        String  newCom = cosList2[0];
                        int  newNum = Integer.parseInt(cosList2[1]);
                        List<StudentEntry> studentEntries1 = studentMap.get(newCom);
                        int index = 0;
                        for(StudentEntry studentEntry :studentEntries1){
                            if(index<newNum && studentEntry.getSex()!=100){
                                //表明已被使用
                                studentEntry.setSex(100);
                                studentEntries0.add(studentEntry);
                                index++;
                            }
                        }
                    }
                    linDTO.setStudentEntryList(studentEntries0);
                }
            }
        }

        int classIndex = 1;
        //分配学生，生成班级
        List<DBObject> afreshClassEntryList = new ArrayList<DBObject>();
        for(Map.Entry<String, List<N33_AfreshMapDTO>> entry  : ruleMap.entrySet()){
            List<N33_AfreshMapDTO> dtos = entry.getValue();
            for(N33_AfreshMapDTO dto : dtos){
                //System.out.println(dto.toString());
                List<N33_AfreshLinDTO> list = dto.getClassList();
                for(N33_AfreshLinDTO dto1 : list){
                    List<StudentEntry> studentEntries1 = dto1.getStudentEntryList();
                    List<ObjectId> objectIdList = new ArrayList<ObjectId>();
                    for(StudentEntry s:studentEntries1){
                        objectIdList.add(s.getUserId());
                    }
                    if(objectIdList.size()>0){
                        AfreshClassEntry afreshClassEntry =  new AfreshClassEntry(xqid,
                                schoolId,grade.getName()+"("+classIndex+")"+"班",objectIdList,"",null,1,gradeId,"",classIndex, setId);
                        afreshClassEntryList.add(afreshClassEntry.getBaseEntry());
                        classIndex++;
                    }
                }
            }
        }

        for(N33_AfreshLinDTO dto1 : resultList){
            List<StudentEntry> studentEntries1 = dto1.getStudentEntryList();
            List<ObjectId> objectIdList = new ArrayList<ObjectId>();
            for(StudentEntry s:studentEntries1){
                objectIdList.add(s.getUserId());
            }
            if(objectIdList.size()>0){
                AfreshClassEntry afreshClassEntry =  new AfreshClassEntry(xqid,
                        schoolId,grade.getName()+"("+classIndex+")"+"班",objectIdList,"",null,1,gradeId,"",classIndex,setId);
                afreshClassEntryList.add(afreshClassEntry.getBaseEntry());
                classIndex++;
            }
        }

        //保存分班结果
        afreshClassDao.addAfreshClassEntry(afreshClassEntryList);
        //修改为最新分班结果
        afreshClassSetDao.updateOld(xqid,schoolId,gradeId);
        afreshClassSetDao.updateNew(setId);
        System.out.println("分班结束！");
    }

    public String  zuheXuanze(String cs){
        String[] comList = cs.split(",");
        Map<String,Integer> map  = new HashMap<String, Integer>();
        for (String ct : comList) {
            String[] cosList2 = ct.split("#");
            String  newCom = cosList2[0];
            int  newNum = Integer.parseInt(cosList2[1]);
            Integer i = map.get(newCom)==null?0: map.get(newCom);
            map.put(newCom,i+newNum);
        }
        StringBuffer sb = new StringBuffer();
        for(Map.Entry<String,Integer> m : map.entrySet()){
            String s = m.getKey();
            int d  = m.getValue();
            sb.append(s);
            sb.append("#");
            sb.append(d);
            sb.append(",");
        }
        return sb.toString();
    }

    /**
     * 第三次按成绩分班
     */
    public void getThreeRuleType(ObjectId exid, Map<ObjectId,Double> map) {
        //总成绩计算
        List<PerformanceXYZLEntry> en = performanceXYZLDao.findPerformanceList(exid);
        for(PerformanceXYZLEntry p : en){
            Double d = map.get(p.getStudentId())==null?0:map.get(p.getStudentId());
            List<Score> scoreList =  p.getScoreList();
            if(scoreList!=null){
                for(Score score:scoreList){
                    d = add(d, score.getSubjectScore());
                }
            }
            map.put(p.getStudentId(), d);
        }
        //排序

    }

    /**
     * 蛇形分班
     */
    public void getSheClass(List<StudentEntry> studentEntryList,List<N33_AfreshLinDTO> dtos) {
        //所有班级标号
        //该类型是否还有剩余
        Map<Integer,Map<String,Integer>> shenMap = new HashMap<Integer, Map<String, Integer>>();
        Map<Integer,N33_AfreshLinDTO> dtoMap = new HashMap<Integer, N33_AfreshLinDTO>();
        int index = 0;
        for(N33_AfreshLinDTO dto:dtos){
            String cs = dto.getStringList();
            Map<String,Integer> dMap = new HashMap<String, Integer>();
            if (cs != null && !cs.equals("")) {
                String[] comList = cs.split(",");
                for (String ct : comList) {
                    String[] cosList2 = ct.split("#");
                    String  newCom = cosList2[0];
                    int  newNum = Integer.parseInt(cosList2[1]);
                    Integer integer = dMap.get(newCom)==null?0:dMap.get(newCom);
                    dMap.put(newCom,newNum+integer);
                }
            }
            dto.setIndex(index);
            shenMap.put(index,dMap);
            dtoMap.put(index,dto);
            index++;
        }
        //从第0个开始分配
        int order = 0;
        //是否正序
        boolean f1 = true;
        for(StudentEntry s: studentEntryList){
            boolean falg = true;
            //顺序来一遍
            int fangzhi = 0;
            while(falg){
                //取值
                N33_AfreshLinDTO n33_afreshLinDTO = dtos.get(order);
                Map<String,Integer> map =  shenMap.get(order);
                //是否有剩余
                Integer integer = map.get(s.getCombiname())==null?0:map.get(s.getCombiname());
                if(integer>0){
                    List<StudentEntry> studentEntryList1 = n33_afreshLinDTO.getStudentEntryList()==null?new ArrayList<StudentEntry>():n33_afreshLinDTO.getStudentEntryList();
                    //放入
                    studentEntryList1.add(s);
                    integer--;
                    map.put(s.getCombiname(),integer);
                    falg = false;
                }
                //控制读取 - 不论是否成功
                if(f1){
                    //正序
                    if(order==dtos.size()-1){
                        //最后一次，order 不变 开始倒序
                        f1 = false;
                    }else{
                        order++;
                    }
                }else{
                    if(order==0){
                        //逆序最后一次，order 不变 开始倒序
                        f1 = true;
                    }else{
                        order--;
                    }
                }
                //防止死循环
                if(fangzhi>dtos.size()+2){
                    falg = false;
                }
                fangzhi++;
            }
        }
    }

    public static final Boolean order =true;

    public static void main(String[] args) {

        //把分班后数据存入二位数组
        int test[][] = divideByClass(64,8,order);
        String str = "顺序";
        if(!order){
            str = "倒序";
        }

        for(int i=0;i<8;i++){
            for(int j=0;j<test[i].length;j++){
                int x = test[i][j];
                if(x != 0){
                    System.out.println("进行"+str+"排列的结果：第"+(i+1)+"班被分配的第"+(j+1)+"位学生总体排名为："+x+"");
                }
            }
        }

    }

    public static int[][] divideByClass(int stuNum,int classNum,boolean type){

        //定义一个二维数组，初始化容量为 stuNum=64 classNum=8 ,int[8][9]
        int[][] rs = new int[classNum][stuNum/classNum+1];

        for(int i=0;i<stuNum;i++){//i<64

            int x = i%classNum;//求余 64/8  0 1 2 3 4 5 6 7   横坐标
            int y = i/classNum;//整除 64/8  0 1 2 3 4 5 6 7 8  纵坐标

            //求余x=0，整除y！=0的时候，type取反
            if(x==0 && y!=0){  // 8-false 16-true 24-false 32-true 40-false 48-true 56-true 64-false
                type=!type;
            }

            if(!type){
                x = classNum-i%classNum-1; //班级数-序号%班级数-1
                y = i/classNum;//序号/班级数
            }
            rs[x][y]=i+1;

            /** x求余  y整除  rs数组
             *  i=0;x=0;y=0;type=true,rs[0][0]=1;
             *  i=1;x=1;y=0;type=true,rs[1][0]=2;
             *  i=2;x=2;y=0;type=true,rs[2][0]=3;
             *  i=3;x=3;y=0;type=true,rs[3][0]=4;
             *  i=4;x=4;y=0;type=true,rs[4][0]=5;
             *  i=5;x=5;y=0;type=true,rs[5][0]=6;
             *  i=6;x=6;y=0;type=true,rs[6][0]=7;
             *  i=7;x=7;y=0;type=true,rs[7][0]=8;
             *  i=8;x=6;y=1;type=false,rs[6][1]=9;
             *  i=9;x=6;y=1;type=false,rs[7][1]=10;
             *  i=10;x=
             */
        }
        return rs;
    }



    
    
    
    /**
     * 第二次浮动分班
     */
    public void getSecondRuleType(List<N33_AfreshCompareDTO> wuMapList, Map<String,List<N33_AfreshMapDTO>> ruleMap,List<N33_AfreshLinDTO> resultList){

        //分班开始(定班级)

        //可组成的班级应在剩余类型之下
        //int wuClassCount = wuMapList.size()-1;
        int allStudent =0;
        int allVolume =0;
        int allSwim =0;
        for(N33_AfreshCompareDTO wuDto : wuMapList){
            allStudent += wuDto.getCount();
            allVolume += wuDto.getVolume();
            allSwim += wuDto.getSwim();
        }
        //平均容量
        double av = div(allVolume,wuMapList.size());
        //平均浮动
        double sv = div(allSwim,wuMapList.size());

        //理论最小容量
        int smin =  (int) Math.ceil(sub(av, sv));
        //理论最大容量
        int smax =  (int) Math.ceil(add(av, sv));

        //可组成的班级：最小容量下的最大班级数量
        double shiClassCount =  div(allStudent,sub(av, sv));
        if(shiClassCount<1){
            //所有剩余人都不够组成一个班
        }
        //方案一  定班级数量 多分少 剩余冗余
        Collections.shuffle(wuMapList);//保证随机性
        //3. 尝试两两组合结果
        for(N33_AfreshCompareDTO wuDto : wuMapList){
            if(wuDto.getRemove()==1 || wuDto.getCount()==0){
                continue;
            }
            //当前数量
            int count = wuDto.getCount();
            for(N33_AfreshCompareDTO wuDto2 : wuMapList){
                if(wuDto2.getRemove()== 1|| wuDto.getCount()==0){
                    continue;
                }
                if(!wuDto.getCompose().equals(wuDto2.getCompose())){
                    if(wuDto.getN2()==wuDto2.getN2()){
                        //匹配上
                        int count2 = wuDto2.getCount() + count;
                        if(count2>=smin  && count2<=smax){
                            //两个已符合 投掷到
                            N33_AfreshLinDTO dto = new N33_AfreshLinDTO();
                            dto.setNumber(count2);
                            dto.setLevel(wuDto.getN3());
                            dto.setMainLevel(wuDto.getN2());
                            dto.setSubjectType(wuDto.getN1());
                            dto.setMin(smin);
                            dto.setMax(smax);
                            dto.setType(0);
                            dto.setStringList(wuDto.getCompose() + "#" + count + "," + wuDto2.getCompose() + "#" + wuDto2.getCount());
                            wuDto.setRemove(1);
                            wuDto2.setRemove(1);
                            wuDto.setCount(0);
                            wuDto2.setCount(0);
                            resultList.add(dto);
                            break;
                        }
                    }else if(wuDto.getN2()==wuDto2.getN3()){
                        //匹配上
                        int count2 = wuDto2.getCount() + count;
                        if(count2>=smin  && count2<=smax){
                            //两个已符合 投掷到
                            N33_AfreshLinDTO dto = new N33_AfreshLinDTO();
                            dto.setNumber(count2);
                            dto.setLevel(wuDto.getN3());
                            dto.setMainLevel(wuDto.getN2());
                            dto.setSubjectType(wuDto.getN1());
                            dto.setMin(smin);
                            dto.setMax(smax);
                            dto.setType(0);
                            dto.setStringList(wuDto.getCompose() + "#" + count + "," + wuDto2.getCompose()+"#"+wuDto2.getCount());
                            wuDto.setRemove(1);
                            wuDto2.setRemove(1);
                            wuDto.setCount(0);
                            wuDto2.setCount(0);
                            resultList.add(dto);
                            break;
                        }
                    }else if(wuDto.getN3()==wuDto2.getN2()){
                        //匹配上
                        int count2 = wuDto2.getCount() + count;
                        if(count2>=smin  && count2<=smax){
                            //两个已符合 投掷到
                            N33_AfreshLinDTO dto = new N33_AfreshLinDTO();
                            dto.setNumber(count2);
                            dto.setLevel(wuDto.getN2());
                            dto.setMainLevel(wuDto.getN3());
                            dto.setSubjectType(wuDto.getN1());
                            dto.setMin(smin);
                            dto.setMax(smax);
                            dto.setType(0);
                            dto.setStringList(wuDto.getCompose() + "#" + count + "," + wuDto2.getCompose()+"#"+wuDto2.getCount());
                            wuDto.setRemove(1);
                            wuDto2.setRemove(1);
                            wuDto.setCount(0);
                            wuDto2.setCount(0);
                            resultList.add(dto);
                            break;
                        }
                    }else if(wuDto.getN3()==wuDto2.getN3()){
                        //匹配上
                        int count2 = wuDto2.getCount() + count;
                        if(count2>=smin  && count2<=smax){
                            //两个已符合 投掷到
                            N33_AfreshLinDTO dto = new N33_AfreshLinDTO();
                            dto.setNumber(count2);
                            dto.setLevel(wuDto.getN2());
                            dto.setMainLevel(wuDto.getN3());
                            dto.setSubjectType(wuDto.getN1());
                            dto.setMin(smin);
                            dto.setMax(smax);
                            dto.setType(0);
                            dto.setStringList(wuDto.getCompose() + "#" + count + "," + wuDto2.getCompose()+"#"+wuDto2.getCount());
                            wuDto.setRemove(1);
                            wuDto2.setRemove(1);
                            wuDto.setCount(0);
                            wuDto2.setCount(0);
                            resultList.add(dto);
                            break;
                        }
                    }
                }
            }
        }
        //4.1 定二走一  定一容二  是否可行 尝试拆除组合
        Collections.shuffle(wuMapList);//保证随机性
        for(N33_AfreshCompareDTO wuDto : wuMapList){
            if(wuDto.getRemove()==1 || wuDto.getCount()==0){
                continue;
            }
            Map<Integer,Integer>  oneResultMap  = new HashMap<Integer, Integer>();
            //重新判断
            booleanChai(oneResultMap,wuMapList);
            int n2 = wuDto.getN2();
            Integer integer = oneResultMap.get(n2)==null?0:oneResultMap.get(n2);
            if(integer==1){
                //可组合尝试组合
                //人数
                int count = wuDto.getCount();
                //最大容量
                int zmax = wuDto.getVolume() + wuDto.getSwim();
                int zmin = wuDto.getVolume() - wuDto.getSwim();
                for(N33_AfreshCompareDTO wuDto2 : wuMapList){
                    if(wuDto2.getRemove()==1 || wuDto.getCount()==0){
                        continue;
                    }
                    int count2  = count + wuDto2.getCount();
                    //非自己
                    if(!wuDto.getCompose().equals(wuDto2.getCompose())) {
                        if (n2 == wuDto2.getN2()) {
                            if (count2 >= zmin) {
                                //满足最小余量
                                //扣除
                                if (count2 <= wuDto.getVolume()) {
                                    //全部扣除
                                    N33_AfreshLinDTO dto = new N33_AfreshLinDTO();
                                    dto.setNumber(count2);
                                    dto.setLevel(wuDto.getN3());
                                    dto.setMainLevel(wuDto.getN2());
                                    dto.setSubjectType(wuDto.getN1());
                                    dto.setMin(zmin);
                                    dto.setMax(zmax);
                                    dto.setType(0);
                                    dto.setStringList(wuDto.getCompose() + "#" + count + "," + wuDto2.getCompose() + "#" + wuDto2.getCount());
                                    wuDto.setRemove(1);
                                    wuDto2.setRemove(1);
                                    wuDto.setCount(0);
                                    wuDto2.setCount(0);
                                    resultList.add(dto);
                                    break;
                                } else {
                                    //部分扣除(放入容量大小)
                                    N33_AfreshLinDTO dto = new N33_AfreshLinDTO();
                                    dto.setNumber(wuDto.getVolume());
                                    dto.setLevel(wuDto.getN3());
                                    dto.setMainLevel(wuDto.getN2());
                                    dto.setSubjectType(wuDto.getN1());
                                    dto.setMin(zmin);
                                    dto.setMax(zmax);
                                    dto.setType(0);
                                    int linCount = wuDto.getVolume() - wuDto.getCount();
                                    dto.setStringList(wuDto.getCompose() + "#" + count + "," + wuDto2.getCompose() + "#" + linCount);
                                    wuDto.setCount(0);
                                    wuDto.setRemove(1);
                                    wuDto2.setCount(wuDto2.getCount() - linCount);
                                    resultList.add(dto);
                                    break;
                                }
                            }
                        } else if (n2 == wuDto2.getN3()) {
                            if (count2 >= zmin) {
                                //满足最小余量
                                //扣除
                                if (count2 <= wuDto.getVolume()) {
                                    //全部扣除
                                    N33_AfreshLinDTO dto = new N33_AfreshLinDTO();
                                    dto.setNumber(count2);
                                    dto.setLevel(wuDto.getN3());
                                    dto.setMainLevel(wuDto.getN2());
                                    dto.setSubjectType(wuDto.getN1());
                                    dto.setMin(zmin);
                                    dto.setMax(zmax);
                                    dto.setType(0);
                                    dto.setStringList(wuDto.getCompose() + "#" + count + "," + wuDto2.getCompose() + "#" + wuDto2.getCount());
                                    wuDto.setRemove(1);
                                    wuDto2.setRemove(1);
                                    wuDto.setCount(0);
                                    wuDto2.setCount(0);
                                    resultList.add(dto);
                                    break;
                                } else {
                                    //部分扣除(放入容量大小)
                                    N33_AfreshLinDTO dto = new N33_AfreshLinDTO();
                                    dto.setNumber(wuDto.getVolume());
                                    dto.setLevel(wuDto.getN3());
                                    dto.setMainLevel(wuDto.getN2());
                                    dto.setSubjectType(wuDto.getN1());
                                    dto.setMin(zmin);
                                    dto.setMax(zmax);
                                    dto.setType(0);
                                    int linCount = wuDto.getVolume() - wuDto.getCount();
                                    dto.setStringList(wuDto.getCompose() + "#" + count + "," + wuDto2.getCompose() + "#" + linCount);
                                    wuDto.setRemove(1);
                                    wuDto.setCount(0);
                                    wuDto2.setCount(wuDto2.getCount() - linCount);
                                    resultList.add(dto);
                                    break;
                                }
                            }
                        }
                    }
                }
            }else{
                int n3 = wuDto.getN3();
                Integer integer2 = oneResultMap.get(n3)==null?0:oneResultMap.get(n3);
                if(integer2==1){
                    //可组合尝试组合
                    //人数
                    int count = wuDto.getCount();
                    //最大容量
                    int zmax = wuDto.getVolume() + wuDto.getSwim();
                    int zmin = wuDto.getVolume() - wuDto.getSwim();
                    for(N33_AfreshCompareDTO wuDto2 : wuMapList){
                        if(wuDto2.getRemove()==1 || wuDto.getCount()==0){
                            continue;
                        }
                        int count2  = count + wuDto2.getCount();
                        //非自己
                        if(!wuDto.getCompose().equals(wuDto2.getCompose())) {
                            if (n3 == wuDto2.getN2()) {
                                if (count2 >= zmin) {
                                    //满足最小余量
                                    //扣除
                                    if (count2 <= wuDto.getVolume()) {
                                        //全部扣除
                                        N33_AfreshLinDTO dto = new N33_AfreshLinDTO();
                                        dto.setNumber(count2);
                                        dto.setLevel(wuDto.getN2());
                                        dto.setMainLevel(wuDto.getN3());
                                        dto.setSubjectType(wuDto2.getN1());
                                        dto.setMin(zmin);
                                        dto.setMax(zmax);
                                        dto.setType(0);
                                        dto.setStringList(wuDto.getCompose() + "#" + count + "," + wuDto2.getCompose() + "#" + wuDto2.getCount());
                                        wuDto.setRemove(1);
                                        wuDto2.setRemove(1);
                                        wuDto.setCount(0);
                                        wuDto2.setCount(0);
                                        resultList.add(dto);
                                        break;
                                    } else {
                                        //部分扣除(放入容量大小)
                                        N33_AfreshLinDTO dto = new N33_AfreshLinDTO();
                                        dto.setNumber(wuDto.getVolume());
                                        dto.setLevel(wuDto.getN2());
                                        dto.setMainLevel(wuDto.getN3());
                                        dto.setSubjectType(wuDto2.getN1());
                                        dto.setMin(zmin);
                                        dto.setMax(zmax);
                                        dto.setType(0);
                                        int linCount = wuDto.getVolume() - wuDto.getCount();
                                        dto.setStringList(wuDto.getCompose() + "#" + count + "," + wuDto2.getCompose() + "#" + linCount);
                                        wuDto.setCount(0);
                                        wuDto.setRemove(1);
                                        wuDto2.setCount(wuDto2.getCount() - linCount);
                                        resultList.add(dto);
                                        break;
                                    }
                                }
                            } else if (n3 == wuDto2.getN3()) {
                                if (count2 >= zmin) {
                                    //满足最小余量
                                    //扣除
                                    if (count2 <= wuDto.getVolume()) {
                                        //全部扣除
                                        N33_AfreshLinDTO dto = new N33_AfreshLinDTO();
                                        dto.setNumber(count2);
                                        dto.setLevel(wuDto.getN2());
                                        dto.setMainLevel(wuDto.getN3());
                                        dto.setSubjectType(wuDto2.getN1());
                                        dto.setMin(zmin);
                                        dto.setMax(zmax);
                                        dto.setType(0);
                                        dto.setStringList(wuDto.getCompose() + "#" + count + "," + wuDto2.getCompose() + "#" + wuDto2.getCount());
                                        wuDto.setRemove(1);
                                        wuDto2.setRemove(1);
                                        wuDto.setCount(0);
                                        wuDto2.setCount(0);
                                        resultList.add(dto);
                                        break;
                                    } else {
                                        //部分扣除(放入容量大小)
                                        N33_AfreshLinDTO dto = new N33_AfreshLinDTO();
                                        dto.setNumber(wuDto.getVolume());
                                        dto.setLevel(wuDto.getN2());
                                        dto.setMainLevel(wuDto.getN3());
                                        dto.setSubjectType(wuDto2.getN1());
                                        dto.setMin(zmin);
                                        dto.setMax(zmax);
                                        dto.setType(0);
                                        int linCount = wuDto.getVolume() - wuDto.getCount();
                                        dto.setStringList(wuDto.getCompose() + "#" + count + "," + wuDto2.getCompose() + "#" + linCount);
                                        wuDto.setRemove(1);
                                        wuDto.setCount(0);
                                        wuDto2.setCount(wuDto2.getCount() - linCount);
                                        resultList.add(dto);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        //4.2 定二走一  定一容三  是否可行 尝试拆除组合
        Collections.shuffle(wuMapList);//保证随机性
        for(N33_AfreshCompareDTO wuDto : wuMapList){
            if(wuDto.getRemove()==1 || wuDto.getCount()==0){
                continue;
            }
            Map<Integer,Integer>  oneResultMap  = new HashMap<Integer, Integer>();
            //重新判断
            booleanChai(oneResultMap,wuMapList);
            int n2 = wuDto.getN2();
            Integer integer = oneResultMap.get(n2)==null?0:oneResultMap.get(n2);
            if(integer==1){
                //可组合尝试组合
                //最大容量
                int zmax = wuDto.getVolume() + wuDto.getSwim();
                int zmin = wuDto.getVolume() - wuDto.getSwim();
                //人数
                int maxCount = wuDto.getCount();
                StringBuffer restr = new StringBuffer();
                restr.append(wuDto.getCompose());
                restr.append("#");
                restr.append(wuDto.getCount());
                for(N33_AfreshCompareDTO wuDto2 : wuMapList){
                    if(wuDto2.getRemove()==1 || wuDto.getCount()==0){
                        continue;
                    }
                    //非自己
                    if(!wuDto.getCompose().equals(wuDto2.getCompose())) {
                        if (n2 == wuDto2.getN2()) {
                            int zx = wuDto2.getCount() + maxCount;
                            if(zx<zmin){
                                //全放入 继续
                                maxCount += wuDto2.getCount();
                                restr.append(",");
                                restr.append(wuDto2.getCompose());
                                restr.append("#");
                                restr.append(wuDto2.getCount());
                                wuDto2.setRemove(1);
                                wuDto2.setCount(0);
                            }else if(zx>zmax){
                                //部分放入 停止
                                int fr =  zmax-maxCount;
                                maxCount += fr;
                                restr.append(",");
                                restr.append(wuDto2.getCompose());
                                restr.append("#");
                                restr.append(fr);
                                wuDto2.setCount(wuDto2.getCount()-fr);

                                break;
                            }else{
                                //全放入 停止
                                maxCount += wuDto2.getCount();
                                restr.append(",");
                                restr.append(wuDto2.getCompose());
                                restr.append("#");
                                restr.append(wuDto2.getCount());
                                wuDto2.setRemove(1);
                                wuDto2.setCount(0);
                                break;
                            }
                        } else if (n2 == wuDto2.getN3()) {
                            int zx = wuDto2.getCount() + maxCount;
                            if(zx<zmin){
                                //全放入 继续
                                maxCount += wuDto2.getCount();
                                restr.append(",");
                                restr.append(wuDto2.getCompose());
                                restr.append("#");
                                restr.append(wuDto2.getCount());
                                wuDto2.setRemove(1);
                                wuDto2.setCount(0);
                            }else if(zx>zmax){
                                //部分放入 停止
                                int fr =  zmax-maxCount;
                                maxCount += fr;
                                restr.append(",");
                                restr.append(wuDto2.getCompose());
                                restr.append("#");
                                restr.append(fr);
                                wuDto2.setCount(wuDto2.getCount()-fr);
                                break;
                            }else{
                                //全放入 停止
                                maxCount += wuDto2.getCount();
                                restr.append(",");
                                restr.append(wuDto2.getCompose());
                                restr.append("#");
                                restr.append(wuDto2.getCount());
                                wuDto2.setRemove(1);
                                wuDto2.setCount(0);
                                break;
                            }
                        }
                    }
                }
                //尝试组合班级
                N33_AfreshLinDTO dto = new N33_AfreshLinDTO();
                dto.setNumber(maxCount);
                dto.setLevel(wuDto.getN3());
                dto.setMainLevel(wuDto.getN2());
                dto.setSubjectType(wuDto.getN1());
                dto.setMin(zmin);
                dto.setMax(zmax);
                dto.setType(0);
                dto.setStringList(restr.toString());
                resultList.add(dto);
            }else{
                int n3 = wuDto.getN3();
                Integer integer2 = oneResultMap.get(n3)==null?0:oneResultMap.get(n3);
                if(integer2==1){
                    //可组合尝试组合
                    //人数
                    int maxCount = wuDto.getCount();
                    //最大容量
                    int zmax = wuDto.getVolume() + wuDto.getSwim();
                    int zmin = wuDto.getVolume() - wuDto.getSwim();
                    StringBuffer restr = new StringBuffer();
                    restr.append(wuDto.getCompose());
                    restr.append("#");
                    restr.append(wuDto.getCount());
                    for(N33_AfreshCompareDTO wuDto2 : wuMapList){
                        if(wuDto2.getRemove()==1 || wuDto.getCount()==0){
                            continue;
                        }
                        //非自己
                        if(!wuDto.getCompose().equals(wuDto2.getCompose())) {
                            if (n3 == wuDto2.getN2()) {
                                int zx = wuDto2.getCount() + maxCount;
                                if(zx<zmin){
                                    //全放入
                                    maxCount += wuDto2.getCount();
                                    restr.append(",");
                                    restr.append(wuDto2.getCompose());
                                    restr.append("#");
                                    restr.append(wuDto2.getCount());
                                    wuDto2.setRemove(1);
                                    wuDto2.setCount(0);
                                }else if(zx>zmax){
                                    //部分放入
                                    int fr =  zmax-maxCount;
                                    maxCount += fr;
                                    restr.append(",");
                                    restr.append(wuDto2.getCompose());
                                    restr.append("#");
                                    restr.append(fr);
                                    wuDto2.setCount(wuDto2.getCount()-fr);
                                    break;
                                }else{
                                    //全放入
                                    maxCount += wuDto2.getCount();
                                    restr.append(",");
                                    restr.append(wuDto2.getCompose());
                                    restr.append("#");
                                    restr.append(wuDto2.getCount());
                                    wuDto2.setRemove(1);
                                    wuDto2.setCount(0);
                                    break;
                                }
                            } else if (n3 == wuDto2.getN3()) {
                                int zx = wuDto2.getCount() + maxCount;
                                if(zx<zmin){
                                    //全放入
                                    maxCount += wuDto2.getCount();
                                    restr.append(",");
                                    restr.append(wuDto2.getCompose());
                                    restr.append("#");
                                    restr.append(wuDto2.getCount());
                                    wuDto2.setRemove(1);
                                    wuDto2.setCount(0);
                                }else if(zx>zmax){
                                    //部分放入
                                    int fr =  zmax-maxCount;
                                    maxCount += fr;
                                    restr.append(",");
                                    restr.append(wuDto2.getCompose());
                                    restr.append("#");
                                    restr.append(fr);
                                    wuDto2.setCount(wuDto2.getCount()-fr);
                                    break;
                                }else{
                                    //全放入
                                    maxCount += wuDto2.getCount();
                                    restr.append(",");
                                    restr.append(wuDto2.getCompose());
                                    restr.append("#");
                                    restr.append(wuDto2.getCount());
                                    wuDto2.setRemove(1);
                                    wuDto2.setCount(0);
                                    break;
                                }
                            }
                        }
                    }
                    //尝试组合班级
                    N33_AfreshLinDTO dto = new N33_AfreshLinDTO();
                    dto.setNumber(maxCount);
                    dto.setLevel(wuDto.getN2());
                    dto.setMainLevel(wuDto.getN3());
                    dto.setSubjectType(wuDto.getN1());
                    dto.setMin(zmin);
                    dto.setMax(zmax);
                    dto.setType(0);
                    dto.setStringList(restr.toString());
                    resultList.add(dto);
                }
            }
        }


        //5.1 仍有冗余,向上冗余(优先同组合） -容量满足冗余
        Collections.shuffle(wuMapList);//保证随机性
        for(N33_AfreshCompareDTO wuDto : wuMapList){
            if(wuDto.getRemove()==1 || wuDto.getCount()==0){
                continue;
            }
            List<N33_AfreshMapDTO> list = ruleMap.get(wuDto.getCompose());
            for(N33_AfreshMapDTO dto : list){
                List<N33_AfreshLinDTO> linDTOList = dto.getClassList();
                if(linDTOList!=null){
                    for(N33_AfreshLinDTO linDTO : linDTOList){
                        //可冗余量
                        int reCount = linDTO.getMax()-linDTO.getNumber();
                        if(reCount>0){
                            //仍有冗余量
                            if(wuDto.getCount()<=reCount){
                                //全扣除
                                linDTO.setNumber(linDTO.getNumber()+wuDto.getCount());
                                linDTO.setStringList(linDTO.getStringList()+","+wuDto.getCompose() +"#"+wuDto.getCount());
                                wuDto.setCount(0);
                                wuDto.setRemove(1);
                                continue;
                            }else{
                                //部分扣除
                                linDTO.setNumber(linDTO.getMax());
                                linDTO.setStringList(linDTO.getStringList()+","+wuDto.getCompose() +"#"+reCount);
                                wuDto.setCount(wuDto.getCount()-reCount);
                            }
                        }
                    }
                }
            }
        }

        //5.2 向下冗余(优先同组合）
        Collections.shuffle(wuMapList);//保证随机性
        for(N33_AfreshCompareDTO wuDto : wuMapList){
            if(wuDto.getRemove()==1 || wuDto.getCount()==0){
                continue;
            }
            //判断是否可以达到最低容量   smin
            int maxCount2 = wuDto.getCount();
            int maxCount3 = wuDto.getCount();
            int nn2 = wuDto.getN2();
            int nn3 = wuDto.getN3();
            for(Map.Entry<String,List<N33_AfreshMapDTO>> ma : ruleMap.entrySet()){
                String  string = ma.getKey();
                int n1 = getStringIndex(string,1);
                //非同一类
                if(n1!=wuDto.getN1()){
                    continue;
                }
                int n2  = getStringIndex(string,2);
                int n3  = getStringIndex(string,3);
                if(nn2==n2 || nn2 ==n3){
                    List<N33_AfreshMapDTO> list = ma.getValue();
                    for(N33_AfreshMapDTO dto : list) {
                        List<N33_AfreshLinDTO> linDTOList = dto.getClassList();
                        if (linDTOList != null) {
                            for (N33_AfreshLinDTO linDTO : linDTOList) {
                                //冗余量
                                int rong =  linDTO.getNumber() - linDTO.getMin();
                                maxCount2+= rong;
                            }
                        }
                    }
                }
                if(nn3==n2 || nn3 ==n3){
                    List<N33_AfreshMapDTO> list = ma.getValue();
                    for(N33_AfreshMapDTO dto : list) {
                        List<N33_AfreshLinDTO> linDTOList = dto.getClassList();
                        if (linDTOList != null) {
                            for (N33_AfreshLinDTO linDTO : linDTOList) {
                                //冗余量
                                int rong =  linDTO.getNumber() - linDTO.getMin();
                                maxCount3+= rong;
                            }
                        }
                    }
                }
            }
            //向下冗余量满足
            if(maxCount2 >=smin){
                int maxCount4 = wuDto.getCount();
                StringBuffer restr = new StringBuffer();
                restr.append(wuDto.getCompose());
                restr.append("#");
                restr.append(wuDto.getCount());
                for(Map.Entry<String,List<N33_AfreshMapDTO>> ma : ruleMap.entrySet()){
                    String  string = ma.getKey();
                    int n1 = getStringIndex(string,1);
                    //非同一类
                    if(n1!=wuDto.getN1()){
                        continue;
                    }
                    int n2  = getStringIndex(string,2);
                    int n3  = getStringIndex(string,3);
                    if(nn2==n2 || nn2 ==n3){
                        List<N33_AfreshMapDTO> list = ma.getValue();
                        for(N33_AfreshMapDTO dto : list) {
                            List<N33_AfreshLinDTO> linDTOList = dto.getClassList();
                            if (linDTOList != null) {
                                for (N33_AfreshLinDTO linDTO : linDTOList) {
                                    //冗余量
                                    int cat =  linDTO.getNumber() - linDTO.getMin();
                                    int zx = maxCount4 + cat;
                                    if(smin >= zx){
                                        //全扣除
                                        maxCount4 += cat;
                                        restr.append(",");
                                        restr.append(dto.getCompose());
                                        restr.append("#");
                                        linDTO.setNumber(linDTO.getMin());
                                        linDTO.setStringList(linDTO.getStringList()+","+dto.getCompose()+"#"+"-"+cat);
                                        restr.append(cat);

                                    }else {
                                        //部分扣除
                                        int shen = smin - maxCount4;
                                        maxCount4 += shen;
                                        restr.append(",");
                                        restr.append(dto.getCompose());
                                        restr.append("#");
                                        linDTO.setNumber(linDTO.getNumber()-shen);
                                        linDTO.setStringList(linDTO.getStringList()+","+dto.getCompose()+"#"+"-"+shen);
                                        restr.append(shen);
                                    }

                                }
                            }
                        }
                    }
                }
                //尝试组合班级
                N33_AfreshLinDTO dto = new N33_AfreshLinDTO();
                dto.setNumber(maxCount4);
                dto.setLevel(wuDto.getN3());
                dto.setMainLevel(wuDto.getN2());
                dto.setSubjectType(wuDto.getN1());
                dto.setMin(smin);
                dto.setMax(smax);
                dto.setType(0);
                dto.setStringList(restr.toString());
                resultList.add(dto);
                wuDto.setCount(0);
                wuDto.setRemove(1);

            }else if(maxCount3 >=smin){
                int maxCount4 = wuDto.getCount();
                StringBuffer restr = new StringBuffer();
                restr.append(wuDto.getCompose());
                restr.append("#");
                restr.append(wuDto.getCount());
                for(Map.Entry<String,List<N33_AfreshMapDTO>> ma : ruleMap.entrySet()){
                    String  string = ma.getKey();
                    int n1 = getStringIndex(string,1);
                    //非同一类
                    if(n1!=wuDto.getN1()){
                        continue;
                    }
                    int n2  = getStringIndex(string,2);
                    int n3  = getStringIndex(string,3);
                    if(nn3==n2 || nn3 ==n3){
                        List<N33_AfreshMapDTO> list = ma.getValue();
                        for(N33_AfreshMapDTO dto : list) {
                            List<N33_AfreshLinDTO> linDTOList = dto.getClassList();
                            if (linDTOList != null) {
                                for (N33_AfreshLinDTO linDTO : linDTOList) {
                                    //冗余量
                                    int cat =  linDTO.getNumber() - linDTO.getMin();
                                    int zx = maxCount4 + cat;
                                    if(smin >= zx){
                                        //全扣除
                                        maxCount4 += cat;
                                        restr.append(",");
                                        restr.append(dto.getCompose());
                                        restr.append("#");
                                        linDTO.setNumber(linDTO.getMin());
                                        linDTO.setStringList(linDTO.getStringList()+","+dto.getCompose()+"#"+"-"+cat);
                                        restr.append(cat);

                                    }else {
                                        //部分扣除
                                        int shen = smin - maxCount4;
                                        maxCount4 += shen;
                                        restr.append(",");
                                        restr.append(dto.getCompose());
                                        restr.append("#");
                                        linDTO.setNumber(linDTO.getNumber()-shen);
                                        linDTO.setStringList(linDTO.getStringList()+","+dto.getCompose()+"#"+"-"+shen);
                                        restr.append(shen);
                                    }
                                }
                            }
                        }
                    }
                }
                //尝试组合班级
                N33_AfreshLinDTO dto = new N33_AfreshLinDTO();
                dto.setNumber(maxCount4);
                dto.setLevel(wuDto.getN2());
                dto.setMainLevel(wuDto.getN3());
                dto.setSubjectType(wuDto.getN1());
                dto.setMin(smin);
                dto.setMax(smax);
                dto.setType(0);
                dto.setStringList(restr.toString());
                resultList.add(dto);
                wuDto.setCount(0);
                wuDto.setRemove(1);
            }
        }

        //5.3 仍有冗余,向上冗余(优先同组合） -容量满足冗余
        Collections.shuffle(wuMapList);//保证随机性
        for(N33_AfreshCompareDTO wuDto : wuMapList){
            if(wuDto.getRemove()==1 || wuDto.getCount()==0){
                continue;
            }
            List<N33_AfreshMapDTO> list = ruleMap.get(wuDto.getCompose());
            for(N33_AfreshMapDTO dto : list){
                List<N33_AfreshLinDTO> linDTOList = dto.getClassList();
                if(linDTOList!=null){
                    for(N33_AfreshLinDTO linDTO : linDTOList){
                        //可冗余量
                        int reCount = linDTO.getMax()-linDTO.getNumber();
                        if(reCount>0){
                            //仍有冗余量
                            if(wuDto.getCount()<=reCount){
                                //全扣除
                                linDTO.setNumber(linDTO.getNumber()+wuDto.getCount());
                                linDTO.setStringList(linDTO.getStringList()+","+wuDto.getCompose() +"#"+wuDto.getCount());
                                wuDto.setCount(0);
                                wuDto.setRemove(1);
                                continue;
                            }else{
                                //部分扣除
                                linDTO.setNumber(linDTO.getMax());
                                linDTO.setStringList(linDTO.getStringList()+","+wuDto.getCompose() +"#"+reCount);
                                wuDto.setCount(wuDto.getCount()-reCount);
                            }
                        }
                    }
                }
            }
        }

        //6 冗余完成仍有剩余，三和一冗余
        Collections.shuffle(wuMapList);//保证随机性
        for(N33_AfreshCompareDTO wuDto : wuMapList){
            if(wuDto.getRemove()==1 || wuDto.getCount()==0){
                continue;
            }
            //冗余到临时班级
            for(N33_AfreshLinDTO linDTO:resultList){
                if(linDTO.getSubjectType()!=wuDto.getN1()){
                    continue;
                }
                if(wuDto.getN2()==linDTO.getMainLevel()){
                    int reCount = linDTO.getMax()-linDTO.getNumber();
                    if(reCount>0){
                        //仍有冗余量
                        if(wuDto.getCount()<=reCount) {
                            //全扣除
                            linDTO.setNumber(linDTO.getNumber()+wuDto.getCount());
                            linDTO.setStringList(linDTO.getStringList()+","+wuDto.getCompose() +"#"+wuDto.getCount());
                            wuDto.setCount(0);
                            wuDto.setRemove(1);
                        }else{
                            //部分扣除
                            linDTO.setNumber(linDTO.getMax());
                            linDTO.setStringList(linDTO.getStringList()+","+wuDto.getCompose() +"#"+reCount);
                            wuDto.setCount(wuDto.getCount()-reCount);
                        }
                    }
                } else if(wuDto.getN3()== linDTO.getMainLevel()){
                    int reCount = linDTO.getMax()-linDTO.getNumber();
                    if(reCount>0){
                        //仍有冗余量
                        if(wuDto.getCount()<=reCount) {
                            //全扣除
                            linDTO.setNumber(linDTO.getNumber()+wuDto.getCount());
                            linDTO.setStringList(linDTO.getStringList()+","+wuDto.getCompose() +"#"+wuDto.getCount());
                            wuDto.setCount(0);
                            wuDto.setRemove(1);
                        }else{
                            //部分扣除
                            linDTO.setNumber(linDTO.getMax());
                            linDTO.setStringList(linDTO.getStringList()+","+wuDto.getCompose() +"#"+reCount);
                            wuDto.setCount(wuDto.getCount()-reCount);
                        }
                    }
                }
            }
            if(wuDto.getRemove()==1 || wuDto.getCount()==0){
                continue;
            }
            //冗余到上级班级
            for(Map.Entry<String,List<N33_AfreshMapDTO>> ma : ruleMap.entrySet()){
                String  string = ma.getKey();
                int n1 = getStringIndex(string,1);
                //非同一类
                if(n1!=wuDto.getN1()){
                    continue;
                }
                int n2  = getStringIndex(string,2);
                if(wuDto.getN2()==n2 || wuDto.getN3() == n2){
                    List<N33_AfreshMapDTO> list = ma.getValue();
                    for(N33_AfreshMapDTO dto : list){
                        List<N33_AfreshLinDTO> linDTOList = dto.getClassList();
                        if(linDTOList!=null){
                            for(N33_AfreshLinDTO linDTO : linDTOList){
                                if(wuDto.getCount()==0){
                                    break;
                                }
                                int reCount = linDTO.getMax()-linDTO.getNumber();
                                if(reCount>0){
                                    //仍有冗余量
                                    if(wuDto.getCount()<=reCount){
                                        //全扣除
                                        linDTO.setNumber(linDTO.getNumber()+wuDto.getCount());
                                        linDTO.setStringList(linDTO.getStringList()+","+wuDto.getCompose() +"#"+wuDto.getCount());
                                        wuDto.setCount(0);
                                        wuDto.setRemove(1);
                                    }else{
                                        //部分扣除
                                        linDTO.setNumber(linDTO.getMax());
                                        linDTO.setStringList(linDTO.getStringList()+","+wuDto.getCompose() +"#"+reCount);
                                        wuDto.setCount(wuDto.getCount()-reCount);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        System.out.print(wuMapList);

        //7 同类冗余仍有剩余，直接放入  尝试组合成一个班
        int allNumber = 0;
        for(N33_AfreshCompareDTO wuDto : wuMapList){
            if(wuDto.getRemove()==1 || wuDto.getCount()==0){
                continue;
            }
            allNumber += wuDto.getCount();
        }
        //平均最小容量
        double min2 = sub(av,sv);
        //平均最大容量
        double max2 = add(av, sv);

        int  min3 = (int) Math.ceil(min2);
        int  max3 = (int) Math.ceil(max2);

        //剩余可分班级
        double c = div(allNumber,min2);
        //向下取整
        int d = (int) Math.floor(c);
        //余量大于一个班级
        if(d>1){
            //组成多个班级
           for(int k = 0;k < d; k++){
               int maxNumber = 0;
               StringBuffer restr = new StringBuffer();
               for(N33_AfreshCompareDTO wuDto : wuMapList){
                   if(wuDto.getRemove()==1 || wuDto.getCount()==0){
                       continue;
                   }
                   int xz = min3-maxNumber;
                   //还有余量取值
                   if(xz > 0){
                       if(xz>=wuDto.getCount()){
                           //全取
                           maxNumber += wuDto.getCount();
                           restr.append(wuDto.getCompose());
                           restr.append("#");
                           restr.append(wuDto.getCount());
                           restr.append(",");
                           wuDto.setRemove(1);
                           wuDto.setCount(0);
                       }else{
                           //部分
                           int qu = wuDto.getCount() - xz;
                           maxNumber += qu;
                           restr.append(wuDto.getCompose());
                           restr.append("#");
                           restr.append(qu);
                           restr.append(",");
                       }
                   }else{
                       break;
                   }
               }

               //尝试组合班级
               N33_AfreshLinDTO dto = new N33_AfreshLinDTO();
               dto.setNumber(min3);
               dto.setLevel(0);
               dto.setMainLevel(0);
               dto.setSubjectType(0);
               dto.setMin(min3);
               dto.setMax(max3);
               dto.setType(0);
               dto.setStringList(restr.toString());
               resultList.add(dto);
           }

        }else{
            int maxNumber = 0;
            StringBuffer restr = new StringBuffer();
            for(N33_AfreshCompareDTO wuDto : wuMapList){
                if(wuDto.getRemove()==1 || wuDto.getCount()==0){
                    continue;
                }
                maxNumber += wuDto.getCount();
                restr.append(wuDto.getCompose());
                restr.append("#");
                restr.append(wuDto.getCount());
                restr.append(",");
            }
            if(maxNumber>0){
                //尝试组合成一个班
                N33_AfreshLinDTO dto = new N33_AfreshLinDTO();
                dto.setNumber(maxNumber);
                dto.setLevel(0);
                dto.setMainLevel(0);
                dto.setSubjectType(0);
                dto.setMin(maxNumber);
                dto.setMax(maxNumber);
                dto.setType(0);
                dto.setStringList(restr.toString());
                resultList.add(dto);
            }
        }
    }

//    public static void main(String[] args){
//        String allNumber = "5,";
//        String allVolume = "60,";
//        String allSwim = "5,";
//        String allType = "1,";
//        String compose = "物化生";
//        Map<String,List<N33_AfreshMapDTO>> map = new HashMap<String, List<N33_AfreshMapDTO>>();
//        Map<String,Integer> exMap = new HashMap<String, Integer>();
//        Map<String,Map<String,Integer>> volumeMap = new HashMap<String, Map<String,Integer>>();
//        try{
//            getFirstRuleType(301,allNumber,allVolume,allSwim,allType,compose,map,volumeMap);
//            System.out.println(map.get(compose).toString());
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//
//        //System.out.println(getStringIndex("物化生", 2));
//
//    }

    //判断是否可继续分
    public  void   booleanChai(Map<Integer,Integer> twoChangeOne,List<N33_AfreshCompareDTO> wuMapList){
        for(N33_AfreshCompareDTO wuDto : wuMapList) {
            if (wuDto.getRemove() == 1 || wuDto.getCount() == 0) {
                continue;
            }
            //最大容量
            int zmax = wuDto.getVolume() + wuDto.getSwim();
            //最小容量
            int zmin = wuDto.getVolume() - wuDto.getSwim();

            Integer n2 = wuDto.getN2();
            if(twoChangeOne.get(n2)==null){
                Integer twoCount2  = 0;
                for(N33_AfreshCompareDTO wuDto2 : wuMapList) {
                    if (wuDto.getRemove() == 1 || wuDto.getCount() == 0) {
                        continue;
                    }
                    Integer nn2 = wuDto2.getN2();
                    Integer nn3 = wuDto2.getN3();
                    if(n2==nn2){
                        //符合
                        twoCount2 += wuDto2.getCount();
                    }else if(n2==nn3){
                        //符合
                        twoCount2 += wuDto2.getCount();
                    }
                }
                if(twoCount2>=zmin){
                    twoChangeOne.put(n2,1);
                }else{
                    twoChangeOne.put(n2,0);
                }

            }
            Integer n3 = wuDto.getN3();
            if(twoChangeOne.get(n2)==null){
                Integer twoCount3  = 0;
                for(N33_AfreshCompareDTO wuDto2 : wuMapList) {
                    if (wuDto.getRemove() == 1 || wuDto.getCount() == 0) {
                        continue;
                    }
                    Integer nn2 = wuDto2.getN2();
                    Integer nn3 = wuDto2.getN3();
                    if(n3==nn2){
                        //符合
                        twoCount3 += wuDto2.getCount();
                    }else if(n3==nn3){
                        //符合
                        twoCount3 += wuDto2.getCount();
                    }
                }
                if(twoCount3>=zmin){
                    twoChangeOne.put(n3,1);
                }else{
                    twoChangeOne.put(n3,0);
                }
            }
        }
    }


    /**
     * todo




     */

    private static int getStringIndex(String str,int number){
        String s = str.substring(number-1,number);
        // 1  物理    2  历史    3  化    4  生   5  政    6 地
        String strings = "!物历化生政地";
        return strings.indexOf(s);
    }


    /**
     * 第一次分段(定量)
     * @param count
     * @param allNumber
     * @param allVolume
     * @param allSwim
     */
    private static void getFirstRuleType(Integer count,
                                         String allNumber,
                                         String allVolume,
                                         String allSwim,
                                         String allType,
                                         String compose,
                                         Map<String,List<N33_AfreshMapDTO>> ruleMap,
                                         Map<String,Map<String,Integer>> volumeMap) throws Exception{
        //此时map 应给出每段 班级数量  班级容量  以及 多余数量  （可为0）

        String[] numberStr = allNumber.split(",");
        String[] volumeStr = allVolume.split(",");
        String[] swimStr = allSwim.split(",");
        //分班类型
        String[] typeStr = allType.split(",");

        if(numberStr.length>0 && volumeStr.length == swimStr.length && numberStr.length== volumeStr.length && numberStr.length== typeStr.length){
            //定容量法
            List<N33_AfreshMapDTO>  dtoList = new ArrayList<N33_AfreshMapDTO>();
            //第一次循环判断是否规则成立
            int order1 = 0;
            int studenCount = 0;
            int classCount = 0;
            //上一容量（为下段默认容量）
            int lastVs = 0;
            //上一浮动（为下段默认浮动）
            int lastSs = 0;
            for(int i =1;i <= numberStr.length;i++){
                int k = i-1;
                int ns  = "".equals(numberStr[k])?0:Integer.parseInt(numberStr[k]);
                int vs  = "".equals(volumeStr[k])?0:Integer.parseInt(volumeStr[k]);
                int ss  = "".equals(swimStr[k])?0:Integer.parseInt(swimStr[k]);
                int ts  = "".equals(typeStr[k])?0:Integer.parseInt(typeStr[k]);
                if(ss==0 && lastSs != 0 ){
                    ss = lastSs;
                }
                //非最后一段，不得全空
                if(i != numberStr.length && vs == 0 && ns == 0){
                    throw new Exception("分班规则有误，班级容量和班级数量至少需要填下一个,请重新设置");
                }
                order1++;
                if(count-studenCount<0){
                    //已超出
                    break;
                }
                //最后一段
                if(ns==0 && vs ==0){
                    //使用默认数据
                    //第一种情况   班级数量不限  容量默认  循环终止
                    int d = 0;
                    if(lastVs!=0){
                        double c = div(count-studenCount,lastVs);
                        //向上封顶
                        d = (int) Math.ceil(c);
                    }
                    //本端实体
                    N33_AfreshMapDTO n33_afreshMapDTO = new N33_AfreshMapDTO();
                    n33_afreshMapDTO.setNumber(d);
                    n33_afreshMapDTO.setVolume(lastVs);
                    n33_afreshMapDTO.setSwim(ss);
                    n33_afreshMapDTO.setCompose(compose);
                    n33_afreshMapDTO.setOrder(order1);
                    n33_afreshMapDTO.setType(ts);
                    dtoList.add(n33_afreshMapDTO);

                    studenCount = studenCount + d * lastVs;
                    classCount = classCount + d ;
                    if(vs!=0){
                        lastVs = vs;
                    }
                    if(ss!=0){
                        lastSs = ss;
                    }

                    break;
                }else if(ns==0){
                    //使用默认数据
                    //第二种情况   班级数量不限  容量限制  循环终止
                    double c = div(count-studenCount,vs);
                    //向上封顶
                    int d = (int) Math.ceil(c);

                    //本端实体
                    N33_AfreshMapDTO n33_afreshMapDTO = new N33_AfreshMapDTO();
                    n33_afreshMapDTO.setNumber(d);
                    n33_afreshMapDTO.setVolume(vs);
                    n33_afreshMapDTO.setSwim(ss);
                    n33_afreshMapDTO.setCompose(compose);
                    n33_afreshMapDTO.setOrder(order1);
                    n33_afreshMapDTO.setType(ts);
                    dtoList.add(n33_afreshMapDTO);

                    studenCount = studenCount + d * vs;
                    classCount = classCount + d ;

                    if(vs!=0){
                        lastVs = vs;
                    }
                    if(ss!=0){
                        lastSs = ss;
                    }

                    break;
                }else if(vs==0){
                    //第三种情况   班级容量不限  数量限制  循环终止
                    double v = div(count-studenCount,ns);
                    //向上封顶
                    int d = (int) Math.ceil(v);

                    //本端实体
                    N33_AfreshMapDTO n33_afreshMapDTO = new N33_AfreshMapDTO();
                    n33_afreshMapDTO.setNumber(ns);
                    n33_afreshMapDTO.setVolume(d);
                    n33_afreshMapDTO.setSwim(ss);
                    n33_afreshMapDTO.setCompose(compose);
                    n33_afreshMapDTO.setOrder(order1);
                    n33_afreshMapDTO.setType(ts);
                    dtoList.add(n33_afreshMapDTO);

                    studenCount = studenCount + ns * d;
                    classCount = classCount + ns ;

                    if(vs!=0){
                        lastVs = vs;
                    }
                    if(ss!=0){
                        lastSs = ss;
                    }

                    break;
                }else{
                    //第四种情况   班级容量限制  数量限制  循环继续
                    //本端实体
                    N33_AfreshMapDTO n33_afreshMapDTO = new N33_AfreshMapDTO();
                    n33_afreshMapDTO.setNumber(ns);
                    n33_afreshMapDTO.setVolume(vs);
                    n33_afreshMapDTO.setSwim(ss);
                    n33_afreshMapDTO.setCompose(compose);
                    n33_afreshMapDTO.setOrder(order1);
                    n33_afreshMapDTO.setType(ts);
                    dtoList.add(n33_afreshMapDTO);

                    studenCount = studenCount + ns * vs;
                    classCount = classCount + ns;

                    if(vs!=0){
                        lastVs = vs;
                    }
                    if(ss!=0){
                        lastSs = ss;
                    }
                }
                order1++;
            }
            //是否冗余
            if(count-studenCount>0){
                if(lastVs==0){
                    throw new Exception("分班规则有误，请重新设置");
                }
                double c = div(count-studenCount,lastVs);
                //向上封顶
                int d = (int) Math.ceil(c);
                N33_AfreshMapDTO n33_afreshMapDTO = new N33_AfreshMapDTO();
                n33_afreshMapDTO.setNumber(d);
                n33_afreshMapDTO.setVolume(lastVs);
                n33_afreshMapDTO.setSwim(lastSs);
                n33_afreshMapDTO.setCompose(compose);
                n33_afreshMapDTO.setOrder(order1);
                n33_afreshMapDTO.setType(0);
                dtoList.add(n33_afreshMapDTO);
            }

            //采用对象方法实现
            ruleMap.put(compose, dtoList);
            //保存容量与浮动
            Map<String,Integer> integerMap = new HashMap<String, Integer>();
            integerMap.put("volume",lastVs);
            integerMap.put("swim",lastSs);
            volumeMap.put(compose,integerMap);
        }else{
            throw new Exception("分班规则有误，请重新设置");
        }
    }






    /**
     * * 两个Double数相除 *
     *
     * @param v1 *
     * @param v2 *
     * @return Double
     */
    public static double div(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(v1+"");
        if(v2 == 0)
        	v2 = Double.parseDouble("1.00");
        BigDecimal b2 = new BigDecimal(v2+"");
        return b1.divide(b2, 6, BigDecimal.ROUND_HALF_UP)
                .doubleValue();
    }


    /**
     * @Description 两个Double数相加
     *
     * @param d1
     * @param d2
     * @return Double
     */
    public static Double add(Double d1,Double d2){
        BigDecimal b1 = new BigDecimal(d1.toString());
        BigDecimal b2 = new BigDecimal(d2.toString());
        return b1.add(b2).doubleValue();
    }


    /**
     * @Description 两个Double数相减
     *
     * @param d1
     * @param d2
     * @return Double
     */
    public static Double sub(Double d1,Double d2){
        BigDecimal b1 = new BigDecimal(d1.toString());
        BigDecimal b2 = new BigDecimal(d2.toString());
        return b1.subtract(b2).doubleValue();
    }





    //查找成绩单
    public List<Map<String,String>> getExamListByGradeTime2(List<String> gradeIds,List<Map<String,String>> timespan) throws ParseException {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        List<Map<String,String>> result = new ArrayList<Map<String,String>>();
        List<ExamXYZLEntry> entrylist = examXYZLDao.getExamByGradeId(MongoUtils.convertToObjectIdList(gradeIds));
        analyzeUtils.sortExamByDate2(entrylist);
        if(entrylist!=null) {
            for(ExamXYZLEntry entry:entrylist) {
                if(entry.getDate()!=null&&!"".equals(entry.getDate())){
                    String time = entry.getDate();
                    long date = sf.parse(time).getTime();
                    for(Map<String,String> map:timespan) {
                        long start = sf.parse(map.get("startDate")).getTime();
                        long end = sf.parse(map.get("endDate")).getTime();
                        if(date>=start&&date<=end) {
                            Map<String,String> temp = new HashMap<String,String>();
                            temp.put("name", entry.getName());
                            temp.put("id", entry.getID().toString());
                            result.add(temp);
                        }
                    }
                }
            }
        }
        return result;
    }

    public void updateClass(ObjectId userId,ObjectId oldClassId,ObjectId newClassId){
        AfreshClassEntry oldAfreshClassEntry = afreshClassDao.getEntry(oldClassId);
        AfreshClassEntry newAfreshClassEntry = afreshClassDao.getEntry(newClassId);
        if(oldAfreshClassEntry!=null && newAfreshClassEntry!=null){
            List<ObjectId> oldIdList = oldAfreshClassEntry.getStudentList();
            List<ObjectId> newIdList = newAfreshClassEntry.getStudentList();
            if(oldClassId!=null && oldIdList.contains(userId)){
                newIdList.add(userId);
                newAfreshClassEntry.setUserList(newIdList);
                afreshClassDao.addEntry(newAfreshClassEntry);
                oldIdList.remove(userId);
                oldAfreshClassEntry.setUserList(oldIdList);
                afreshClassDao.addEntry(oldAfreshClassEntry);
            }
        }
    }

    public Map<String,Object> getNewStuSelectResultByClass(ObjectId xqid,ObjectId schoolId,ObjectId gradeId,Integer type){
        Map<String,Object> result = new HashMap<String, Object>();

        SchoolLesson33DTO schoolLesson33DTO = getSchoolSelects(xqid,schoolId,gradeId);
        List<lesson33DTO> listLesson33DTO = schoolLesson33DTO.getList();
        //查询最近的一条分班结果
        AfreshClassSetEntry afreshClassSetEntry = afreshClassSetDao.getEntryBySchoolId(xqid,schoolId,gradeId);
        List<AfreshClassEntry> entrylist = new ArrayList<AfreshClassEntry>();

        if(afreshClassSetEntry!=null){
            entrylist = afreshClassDao.findByGradeIdId(afreshClassSetEntry.getID());
        }
        Map<ObjectId,ObjectId> studentClassMap = new HashMap<ObjectId, ObjectId>();
        for(AfreshClassEntry afreshClassEntry:entrylist){
            List<ObjectId> slist = afreshClassEntry.getStudentList();
            if(slist!=null){
                for(ObjectId os : slist){
                    studentClassMap.put(os,afreshClassEntry.getID());
                }
            }

        }

        Grade grade = gradeDao.findIsolateGradeEntryByGradeId(gradeId,xqid);
        List<ObjectId> studentIds = new ArrayList<ObjectId>();
        List<Map<String,String>> head = new ArrayList<Map<String,String>>();// 标题list
        for(AfreshClassEntry classEntry:entrylist){
            Map<String,String> map = new HashMap<String, String>();
            map.put("name", classEntry.getName());
            map.put("classId",classEntry.getID().toString());
            if(type == 1){
                map.put("dh","等级/合格");
                map.put("type",String.valueOf(1));
            }else{
                map.put("type",String.valueOf(2));
            }
            head.add(map);
            studentIds.addAll(classEntry.getStudentList());
        }

        List<StudentEntry> studentEntries = N33_StudentDao.getStudentByXqidAndGradeId(gradeId, xqid);
        Integer count = 0;
        for (StudentEntry studentEntry : studentEntries) {
            if(!"".equals(studentEntry.getCombiname()) && studentEntry.getCombiname() != null){
                count ++;
            }
        }

        List<StudentEntry> studentList = N33_StudentDao.getStuList(studentIds, xqid);// 查询各个班级的所有学生
        Map<ObjectId,List<StudentEntry>> classId_stus = new HashMap<ObjectId, List<StudentEntry>>();

        //放入新班级id
        if(studentList!=null) {
            for (StudentEntry entry : studentList) {
                ObjectId oid = studentClassMap.get(entry.getUserId());
                if(oid!=null){
                    entry.setClassId(oid);
                }
            }
        }

        if(studentList!=null) {
            for(StudentEntry entry:studentList) {
                List<StudentEntry> templist = classId_stus.get(entry.getClassId())==null?new ArrayList<StudentEntry>():classId_stus.get(entry.getClassId());
                templist.add(entry);
                classId_stus.put(entry.getClassId(), templist);
            }
        }
        List<String> combinationNames = generateCombination(xqid, schoolId, gradeId, type);
        Map<String,Map<ObjectId,Integer>> name_class_num = new HashMap<String, Map<ObjectId,Integer>>();
        Map<String,List<Map<String,Object>>> name_StudentList=new HashMap<String, List<Map<String, Object>>>();
        for(String name:combinationNames) {
            List<Map<String,Object>> stuIds=new ArrayList<Map<String, Object>>();
            Map<ObjectId,Integer> class_num = name_class_num.get(name)==null?new HashMap<ObjectId, Integer>():name_class_num.get(name);
            for(AfreshClassEntry klass:entrylist) {
                ObjectId klassId = klass.getID();
                Integer i = class_num.get(klassId)==null?0:class_num.get(klassId);
                List<StudentEntry> sutdentList = classId_stus.get(klassId);
                if(sutdentList!=null) {
                    for(StudentEntry stu:sutdentList) {
                        Map<String,Object> map=new HashMap<String, Object>();
                        String combiname = stu.getCombiname();
                        List<String> belongNames = judgeSelect(combinationNames, combiname, type);
                        if(belongNames.contains(name)) {
                            map.put("stuId",stu.getUserId().toString());
                            map.put("clsXh",klass.getXh()+"");
                            map.put("clsId",klassId.toString());
                            map.put("stuName",stu.getUserName());
                            map.put("stuNum",stu.getStudyNum());
                            map.put("className",grade.getName());
                            if(stu.getSex() == 0){
                                map.put("sex","女");
                            }else if(stu.getSex() == 1){
                                map.put("sex","男");
                            }else{
                                map.put("sex","未知");
                            }
                            if(stu.getLevel() == 0 || stu.getLevel() == null){
                                map.put("level","无");
                            }else{
                                map.put("level",stu.getLevel());
                            }
                            if(stu.getCombiname() != null){
                                map.put("combineName",stu.getCombiname());
                            }else{
                                map.put("combineName","");
                            }
                            stuIds.add(map);
                            i++;
                        }
                    }

                }
                class_num.put(klassId, i);
                name_StudentList.put(name,stuIds);
            }
            name_class_num.put(name, class_num);
        }
        List<Map<String,Object>> content = new ArrayList<Map<String,Object>>();

        for(String name:combinationNames) {
            Map<String,Object> map = new HashMap<String, Object>();

            List<Map<String,String>> list = new ArrayList<Map<String,String>>();
            Map<ObjectId,Integer> class_num = name_class_num.get(name)==null?new HashMap<ObjectId, Integer>():name_class_num.get(name);
            Integer sum=0;
            for(AfreshClassEntry klass:entrylist) {
                Map<String,String> newmap = new HashMap<String, String>();
                ObjectId cid = klass.getID();
                String i = class_num.get(cid)==null?"":String.valueOf(class_num.get(cid));
                sum+=class_num.get(cid)==null?0:class_num.get(cid);
                newmap.put("num", i);
                if(type == 1){
                    Integer xkStudent = 0;    //已经选科学生
                    for (StudentEntry studentEntry : classId_stus.get(cid)) {
                        if(!"".equals(studentEntry.getCombiname()) && studentEntry.getCombiname() != null){
                            xkStudent ++;
                        }
                    }
                    newmap.put("hgnum",String.valueOf(xkStudent - Integer.valueOf(i)));
                    newmap.put("type",String.valueOf(1));
                }else{
                    newmap.put("type",String.valueOf(2));
                }
                newmap.put("id", cid.toString());
                list.add(newmap);
            }

            boolean flag = false;
            flag = judgeIsPrevious(type,sum,listLesson33DTO,name);

            if(flag){
                map.put("sum",sum);
                if(type == 1){
                    map.put("name", name+"("+sum+"/" + (count - sum) + ")");
                    map.put("type",String.valueOf(1));
                }else{
                    map.put("name", name+"("+sum+")");
                    map.put("type",String.valueOf(2));
                }
                map.put("subName", name);
                map.put("list", list);
                map.put("stuList",name_StudentList.get(name)==null?new ArrayList<Map<String,String>>():name_StudentList.get(name));
                content.add(map);
            }else{
                continue;
            }
        }
        result.put("head", head);
        result.put("content", content);
        Collections.sort(content, new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                return (Integer) o2.get("sum") - (Integer) o1.get("sum");
            }
        });
        return result;
    }

    public Map<String,Object> selectClassLsit(ObjectId xqid,
                                ObjectId schoolId,
                                ObjectId gradeId,
                                ObjectId classId,
                                ObjectId userId){
        AfreshClassSetEntry afreshClassSetEntry = afreshClassSetDao.getEntryBySchoolId(xqid, schoolId, gradeId);
        List<AfreshClassEntry> entrylist = afreshClassDao.findByGradeIdId(afreshClassSetEntry.getID());
        Map<String,Object> map = new HashMap<String, Object>();
        List<Map<String,Object>> mapList = new ArrayList<Map<String, Object>>();
        Map<String,Object> nowDto = new HashMap<String, Object>();
        for(AfreshClassEntry afreshClassEntry:entrylist){
            if(!afreshClassEntry.getID().equals(classId)){
                Map<String,Object> dto = new HashMap<String, Object>();
                dto.put("classId",afreshClassEntry.getID().toString());
                dto.put("className",afreshClassEntry.getClassName().toString());
                mapList.add(dto);
            }else{
                nowDto.put("classId",afreshClassEntry.getID().toString());
                nowDto.put("className",afreshClassEntry.getClassName().toString());
            }
        }
        map.put("list",mapList);
        map.put("now",nowDto);
        return map;
    }


    //生成列表
    public Map<String,Object> getStuSelectResultByClass(ObjectId xqid,ObjectId schoolId,ObjectId gradeId,Integer type){

        SchoolLesson33DTO schoolLesson33DTO = getSchoolSelects(xqid,schoolId,gradeId);
        List<lesson33DTO> listLesson33DTO = schoolLesson33DTO.getList();

        Map<String,Object> result = new HashMap<String, Object>();
        //N33_ClassController.getIsolateClass(xqid.toString());// TODO
        List<ClassEntry> entrylist = classDao.findByGradeIdId(schoolId, gradeId, xqid);
        List<ClassInfoDTO> classInfoDTOList = classService.getClassByXqAndGrade(xqid,schoolId,gradeId);
        Grade grade = gradeDao.findIsolateGradeEntryByGradeId(gradeId,xqid);
        List<ObjectId> studentIds = new ArrayList<ObjectId>();
        List<Map<String,String>> head = new ArrayList<Map<String,String>>();// 标题list
        for(ClassInfoDTO classInfoDTO:classInfoDTOList){
            Map<String,String> map = new HashMap<String, String>();
            map.put("name", classInfoDTO.getGname());
            map.put("classId",classInfoDTO.getClassId());
            if(type == 1){
                map.put("dh","等级/合格");
                map.put("type",String.valueOf(1));
            }else{
                map.put("type",String.valueOf(2));
            }
            head.add(map);
            studentIds.addAll(MongoUtils.convertToObjectIdList(classInfoDTO.getStus()));
        }
        List<StudentEntry> studentEntries = N33_StudentDao.getStudentByXqidAndGradeId(gradeId, xqid);
        Integer count = 0;
        for (StudentEntry studentEntry : studentEntries) {
            if(!"".equals(studentEntry.getCombiname()) && studentEntry.getCombiname() != null){
                count ++;
            }
        }
        List<StudentEntry> studentList = N33_StudentDao.getStuList(studentIds, xqid);// 查询各个班级的所有学生
        Map<ObjectId,List<StudentEntry>> classId_stus = new HashMap<ObjectId, List<StudentEntry>>();
        if(studentList!=null) {
            for(StudentEntry entry:studentList) {
                List<StudentEntry> templist = classId_stus.get(entry.getClassId())==null?new ArrayList<StudentEntry>():classId_stus.get(entry.getClassId());
                templist.add(entry);
                classId_stus.put(entry.getClassId(), templist);
            }
        }
        List<String> combinationNames = generateCombination(xqid, schoolId, gradeId, type);
        Map<String,Map<ObjectId,Integer>> name_class_num = new HashMap<String, Map<ObjectId,Integer>>();
        Map<String,List<Map<String,Object>>> name_StudentList=new HashMap<String, List<Map<String, Object>>>();
        for(String name:combinationNames) {
            List<Map<String,Object>> stuIds=new ArrayList<Map<String, Object>>();
            Map<ObjectId,Integer> class_num = name_class_num.get(name)==null?new HashMap<ObjectId, Integer>():name_class_num.get(name);
            for(ClassEntry klass:entrylist) {
                ObjectId klassId = klass.getClassId();
                Integer i = class_num.get(klassId)==null?0:class_num.get(klassId);
                List<StudentEntry> sutdentList = classId_stus.get(klass.getClassId());
                if(sutdentList!=null) {
                    for(StudentEntry stu:sutdentList) {
                        Map<String,Object> map=new HashMap<String, Object>();
                        String combiname = stu.getCombiname();
                        List<String> belongNames = judgeSelect(combinationNames, combiname, type);
                        if(belongNames.contains(name)) {
                            map.put("stuId",stu.getUserId().toString());
                            map.put("clsXh",klass.getXh()+"");
                            map.put("clsId",klass.getClassId().toString());
                            map.put("stuName",stu.getUserName());
                            map.put("stuNum",stu.getStudyNum());
                            map.put("className",grade.getName());
                            if(stu.getSex() == 0){
                                map.put("sex","女");
                            }else if(stu.getSex() == 1){
                                map.put("sex","男");
                            }else{
                                map.put("sex","未知");
                            }
                            if(stu.getLevel() == 0 || stu.getLevel() == null){
                                map.put("level","无");
                            }else{
                                map.put("level",stu.getLevel());
                            }
                            if(stu.getCombiname() != null){
                                map.put("combineName",stu.getCombiname());
                            }else{
                                map.put("combineName","");
                            }
                            stuIds.add(map);
                            i++;
                        }
                    }

                }
                class_num.put(klassId, i);
                name_StudentList.put(name,stuIds);
            }
            name_class_num.put(name, class_num);
        }
        List<Map<String,Object>> content = new ArrayList<Map<String,Object>>();

        for(String name:combinationNames) {
            Map<String,Object> map = new HashMap<String, Object>();

            List<Map<String,String>> list = new ArrayList<Map<String,String>>();
            Map<ObjectId,Integer> class_num = name_class_num.get(name)==null?new HashMap<ObjectId, Integer>():name_class_num.get(name);
            Integer sum=0;
            for(ClassEntry klass:entrylist) {
                Map<String,String> newmap = new HashMap<String, String>();
                String i = class_num.get(klass.getClassId())==null?"":String.valueOf(class_num.get(klass.getClassId()));
                sum+=class_num.get(klass.getClassId())==null?0:class_num.get(klass.getClassId());
                newmap.put("num", i);
                if(type == 1){
                    Integer xkStudent = 0;    //已经选科学生
                    for (StudentEntry studentEntry : classId_stus.get(klass.getClassId())) {
                        if(!"".equals(studentEntry.getCombiname()) && studentEntry.getCombiname() != null){
                            xkStudent ++;
                        }
                    }
                    newmap.put("hgnum",String.valueOf(xkStudent - Integer.valueOf(i)));
                    newmap.put("type",String.valueOf(1));
                }else{
                    newmap.put("type",String.valueOf(2));
                }
                newmap.put("id", klass.getClassId().toString());
                list.add(newmap);
            }

            boolean flag = false;
            flag = judgeIsPrevious(type,sum,listLesson33DTO,name);

            if(flag){
                map.put("sum",sum);
                if(type == 1){
                    map.put("name", name+"("+sum+"/" + (count - sum) + ")");
                    map.put("type",String.valueOf(1));
                }else{
                    map.put("name", name+"("+sum+")");
                    map.put("type",String.valueOf(2));
                }
                map.put("subName", name);
                map.put("list", list);
                map.put("stuList",name_StudentList.get(name)==null?new ArrayList<Map<String,String>>():name_StudentList.get(name));
                content.add(map);
            }else{
                continue;
            }
        }
        result.put("head", head);
        result.put("content", content);
        Collections.sort(content, new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                return (Integer) o2.get("sum") - (Integer) o1.get("sum");
            }
        });
        return result;
    }

    /**
     * 得到所有选课组合
     * @param schoolId
     * @return
     */
    public SchoolLesson33DTO getSchoolSelects(ObjectId term,ObjectId schoolId,ObjectId gradeId)
    {
        List<lesson33DTO> retList =new ArrayList<lesson33DTO>();
        List<SchoolSelectLessonSetEntry> list=schoolSelectLessonSetDao.getSchoolSelectLessonSetEntrys(term, schoolId, gradeId);
        String SchoolLesson33DTOId="";
        Long st = null;
        Long ed = null;
        List<N33_KSDTO> N33_KSDTOList = subjectService.getIsolateSubjectByGradeIdIncludeMain(term, schoolId, gradeId.toString(),1);
        List<String> subjectNames = new ArrayList<String>();
        Collections.addAll(subjectNames, "物理", "化学", "生物", "政治", "历史", "地理");
        final Map<ObjectId, String> subjectMap = new HashMap<ObjectId, String>();
        for(N33_KSDTO dto:N33_KSDTOList) {
            if(subjectNames.contains(dto.getSnm()))
            {
                subjectMap.put(new ObjectId(dto.getSubid()),dto.getSnm());
            }
        }
        if(list.size()>=1)
        {
            SchoolSelectLessonSetEntry e=list.get(0);
            SchoolLesson33DTOId =e.getID().toString();
            st = e.getStart();
            ed = e.getEnd();
            List<ObjectId> subIds = new ArrayList<ObjectId>(subjectMap.keySet());
            List<CombineUtils.CombineResult<ObjectId>>  ll=  new ArrayList<CombineUtils.CombineResult<ObjectId>>();
            CombineUtils.combinerSelect(subIds, 3,ll); // 对现有学科进行组合与保存的进行比较
            List<SchoolSelectLessonSetEntry.SelectLessons> waitForAdd = new ArrayList<SchoolSelectLessonSetEntry.SelectLessons>();
            // 遍历新增的
            for(CombineUtils.CombineResult<ObjectId> cb:ll) {
                List<ObjectId> sList=cb.getList();
                Collections.sort(sList);
                String nid = sList.get(0).toString().substring(sList.get(0).toString().toString().length()-5)+"_"
                        +sList.get(1).toString().substring(sList.get(1).toString().toString().length()-5)+"_"
                        +sList.get(2).toString().substring(sList.get(2).toString().toString().length()-5);
                SchoolSelectLessonSetEntry.SelectLessons lessions = new SchoolSelectLessonSetEntry.SelectLessons(nid, getCombineName(sList.get(0),sList.get(1),sList.get(2), subjectMap), sList.get(0),sList.get(1),sList.get(2), 0);

                for(SchoolSelectLessonSetEntry.SelectLessons sls:e.getSets())
                {
                    if(sls.getId().equals(nid)) {
                        lessions.setState(sls.getState());
                        lessions.setNum(sls.getNum());
                        break;
                    }
                }
                waitForAdd.add(lessions);

            }

            schoolSelectLessonSetDao.updateLessionSet(e.getID(), waitForAdd);
            for(SchoolSelectLessonSetEntry.SelectLessons lesson:waitForAdd) {
                retList.add(new lesson33DTO(lesson));
            }
        }
        else
        {
            List<SchoolSelectLessonSetEntry.SelectLessons> sets =new ArrayList<SchoolSelectLessonSetEntry.SelectLessons>();

            List<ObjectId> subjectList =new ArrayList<ObjectId>(subjectMap.keySet());
            List<CombineUtils.CombineResult<ObjectId>>  ll=  new ArrayList<CombineUtils.CombineResult<ObjectId>>();
            CombineUtils.combinerSelect(subjectList, 3,ll);

            for(CombineUtils.CombineResult<ObjectId> cb:ll)
            {
                try
                {
                    List<ObjectId> sList=cb.getList();
                    Collections.sort(sList);// 保证顺序相同学科

                    lesson33DTO dto =new lesson33DTO();
                    dto.setSubjectId1(sList.get(0));
                    dto.setSubjectId2(sList.get(1));
                    dto.setSubjectId3(sList.get(2));

                    String combineName=getCombineName(dto.subjectId1,dto.subjectId2,dto.subjectId3, subjectMap);
                    String id=dto.getId();
                    dto.setId(id);
                    dto.setName(combineName);
                    dto.setState(0);


                    sets.add(dto.convert());
                    retList.add(dto);
                }catch(Exception ex)
                {
                }
            }


            SchoolSelectLessonSetEntry e=new SchoolSelectLessonSetEntry(term, schoolId, gradeId, sets);

            ObjectId insertId= schoolSelectLessonSetDao.addSchoolSelectLessonSetEntry(e);
            SchoolLesson33DTOId=insertId.toString();
            st = e.getStart();
            ed = e.getEnd();
        }
        Collections.sort(retList, new Comparator<lesson33DTO>() {
            @Override
            public int compare(lesson33DTO o1, lesson33DTO o2) {
                String o1a = o1.getName().substring(0, 1);
                String o1b = o1.getName().substring(1, 2);
                String o1c = o1.getName().substring(2, 3);
                String o2a = o2.getName().substring(0, 1);
                String o2b = o2.getName().substring(1, 2);
                String o2c = o2.getName().substring(2, 3);
                if (!o1a.equals(o2a)) {
                    return subjectHead_sort.get(o1a) - subjectHead_sort.get(o2a);
                }
                if (!o1b.equals(o2b)) {
                    return subjectHead_sort.get(o1b) - subjectHead_sort.get(o2b);
                }
                return subjectHead_sort.get(o1c) - subjectHead_sort.get(o2c);
            }

        });
        return new SchoolLesson33DTO(SchoolLesson33DTOId,st,ed, retList);
    }

    /**
     * 生成学科组合
     * @param xqid
     * @param schoolId
     * @param gradeId
     * @param type
     * @return
     */
    private List<String> generateCombination(ObjectId xqid,ObjectId schoolId,ObjectId gradeId,Integer type){
        List<String> result = new ArrayList<String>();
        List<N33_KSDTO> N33_KSDTOList = subjectService.getIsolateSubjectByGradeIdIncludeMain(xqid, schoolId, gradeId.toString(), 1);
        List<String> subjectNames = new ArrayList<String>();
        Collections.addAll(subjectNames, "物理", "化学", "生物", "政治", "历史", "地理");
        final Map<ObjectId, String> subjectMap = new HashMap<ObjectId, String>();
        for(N33_KSDTO dto:N33_KSDTOList) {
            if(subjectNames.contains(dto.getSnm()))
            {
                subjectMap.put(new ObjectId(dto.getSubid()),dto.getSnm());
            }
        }
        List<ObjectId> subIds = new ArrayList<ObjectId>(subjectMap.keySet());
        List<CombineUtils.CombineResult<ObjectId>>  ll=  new ArrayList<CombineUtils.CombineResult<ObjectId>>();
        CombineUtils.combinerSelect(subIds, type,ll); // 对现有学科进行组合与保存的进行比较
        for(CombineUtils.CombineResult<ObjectId> cr:ll) {
            String combineName=getCombineName(subjectMap,cr.getList());
            result.add(combineName);
        }
        return result;
    }

    private String getCombineName(Map<ObjectId, String> subjectMap,List<ObjectId> values)
    {
        List<String> templist = new ArrayList<String>();
        for(ObjectId id:values) {
            templist.add(subjectMap.get(id));
        }

        Collections.sort(templist,new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return subject_sort.get(o1)-subject_sort.get(o2);
            }

        });
        String result="";
        for(String s:templist) {
            result+=s.substring(0, 1);
        }
        return result;
    }

    private String getCombineName(ObjectId id1,ObjectId id2,ObjectId id3,Map<ObjectId, String> subjectMap)
    {

        String s1=subjectMap.get(id1);
        String s2 = subjectMap.get(id2);
        String s3=subjectMap.get(id3);

        List<String> templist = Arrays.asList(s1,s2,s3);
        Collections.sort(templist,new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return subject_sort.get(o1)-subject_sort.get(o2);
            }

        });
        String result="";
        for(String s:templist) {
            result+=s.substring(0, 1);
        }
        return result;
    }

    private List<String> judgeSelect(List<String> combinationNames,String combiname,Integer type) {
        List<String> result = new ArrayList<String>();
        if(combiname!=null) {
            for(String name:combinationNames) {
                if(type==3&&name.equals(combiname)) {
                    result.add(name);
                }
                if(type==2) {
                    if(combiname.indexOf(name.substring(0, 1))>=0&&combiname.indexOf(name.substring(1, 2))>=0) {
                        result.add(name);
                    }
                }
                if(type==1&&combiname.indexOf(name)>=0) {
                    result.add(name);
                }
            }
        }
        return result;
    }

    private boolean judgeIsPrevious(Integer type,Integer sum,List<lesson33DTO> list,String name){
        for (lesson33DTO dto:list) {
            if(type == 3){
                if(name.equals(dto.getName()) && (sum != 0 || dto.getState() != 0)){
                    return true;
                }
            }
            if(type == 2){
                if(dto.getName().indexOf(name.substring(0, 1))>=0 && dto.getName().indexOf(name.substring(1, 2))>=0 && (sum != 0 || dto.getState() != 0)){
                    return true;
                }
            }
            if(type == 1){
                if(dto.getName().indexOf(name)>=0 && (sum != 0 || dto.getState() != 0)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static class SchoolLesson33DTO
    {
        private String id;

        private String start;
        private Long startTime;
        private String end;
        private Long endTime;


        private List<lesson33DTO> list =new ArrayList<lesson33DTO>();

        public SchoolLesson33DTO(String id, List<lesson33DTO> list) {
            super();
            this.id = id;
            this.list = list;
        }
        public SchoolLesson33DTO(String id,Long st,Long ed, List<lesson33DTO> list) {
            super();
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            if(st!=null){
                start = sf.format(new Date(st));
            }
            else{
                start = "";
            }
            if(ed!=null){
                end = sf.format(new Date(ed));
            }
            else{
                end = "";
            }
            this.id = id;
            this.list = list;
            this.startTime = st;
            this.endTime = ed;
        }
        public String getStart() {
            return start;
        }

        public void setStart(String start) {
            this.start = start;
        }

        public String getEnd() {
            return end;
        }

        public void setEnd(String end) {
            this.end = end;
        }

        public String getId() {
            return id;
        }
        public void setId(String id) {
            this.id = id;
        }
        public List<lesson33DTO> getList() {
            return list;
        }
        public void setList(List<lesson33DTO> list) {
            this.list = list;
        }
        public Long getStartTime() {
            return startTime;
        }
        public void setStartTime(Long startTime) {
            this.startTime = startTime;
        }
        public Long getEndTime() {
            return endTime;
        }
        public void setEndTime(Long endTime) {
            this.endTime = endTime;
        }

    }

    /**
     * 排列组合
     * @author fourer
     *
     */
    public static class lesson33DTO
    {
        private String id;
        private String name;
        public ObjectId subjectId1;
        public ObjectId subjectId2;
        public ObjectId subjectId3;


        public String subjectId1Str;
        public String subjectId2Str;
        public String subjectId3Str;
        private int state;
        private Integer num;
        private String sub1Str;
        private String sub2Str;
        private String sub3Str;


        public lesson33DTO(){}

        public lesson33DTO(SchoolSelectLessonSetEntry.SelectLessons ses){
            this.id=ses.getId();
            this.name=ses.getName();
            this.subjectId1=ses.getSubject1();
            this.subjectId2=ses.getSubject2();
            this.subjectId3=ses.getSubject3();
            this.subjectId1Str=ses.getSubject1().toHexString();
            this.subjectId2Str=ses.getSubject2().toHexString();
            this.subjectId3Str=ses.getSubject3().toHexString();
            this.state=ses.getState();
            this.num = ses.getNum();
        }
        public SchoolSelectLessonSetEntry.SelectLessons convert()
        {
            SchoolSelectLessonSetEntry.SelectLessons ses=new SchoolSelectLessonSetEntry.SelectLessons(getId(), name, subjectId1, subjectId2, subjectId3, state,num);

            return ses;
        }


        public String getName() {
            return name;
        }


        public void setName(String name) {
            this.name = name;
        }


        public ObjectId getSubjectId1() {
            return subjectId1;
        }


        public void setSubjectId1(ObjectId subjectId1) {
            this.subjectId1 = subjectId1;
        }


        public ObjectId getSubjectId2() {
            return subjectId2;
        }


        public void setSubjectId2(ObjectId subjectId2) {
            this.subjectId2 = subjectId2;
        }


        public ObjectId getSubjectId3() {
            return subjectId3;
        }


        public void setSubjectId3(ObjectId subjectId3) {
            this.subjectId3 = subjectId3;
        }



        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }



        public String getSubjectId1Str() {
            return subjectId1Str;
        }

        public void setSubjectId1Str(String subjectId1Str) {
            this.subjectId1Str = subjectId1Str;
        }

        public String getSubjectId2Str() {
            return subjectId2Str;
        }

        public void setSubjectId2Str(String subjectId2Str) {
            this.subjectId2Str = subjectId2Str;
        }

        public String getSubjectId3Str() {
            return subjectId3Str;
        }

        public void setSubjectId3Str(String subjectId3Str) {
            this.subjectId3Str = subjectId3Str;
        }

        public String getId() {

            if(StringUtils.isNotBlank(this.id))
                return this.id;
            String str=  String.valueOf(this.subjectId1.toString().substring(this.subjectId1.toString().length()-5));
            if(null!=this.subjectId2)
            {
                str+="_"+this.subjectId2.toString().substring(this.subjectId2.toString().length()-5);
            }
            if(null!=this.subjectId3)
            {
                str+="_"+this.subjectId3.toString().substring(this.subjectId3.toString().length()-5);
            }
            return str;
        }

        public void setId(String id) {
            this.id=id;
        }

        public Integer getNum() {
            return num;
        }

        public void setNum(Integer num) {
            this.num = num;
        }

        public String getSub1Str() {
            if(null!=this.subjectId1)
            {
                return this.subjectId1.toString();
            }
            return "";
        }

        public void setSub1Str(String sub1Str) {
        }

        public String getSub2Str() {
            if(null!=this.subjectId2)
            {
                return this.subjectId2.toString();
            }
            return "";
        }

        public void setSub2Str(String sub2Str) {
        }

        public String getSub3Str() {
            if(null!=this.subjectId3)
            {
                return this.subjectId3.toString();
            }
            return "";
        }

        public void setSub3Str(String sub3Str) {
            this.sub3Str = sub3Str;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result
                    + ((subjectId1 == null) ? 0 : subjectId1.hashCode());
            result = prime * result
                    + ((subjectId2 == null) ? 0 : subjectId2.hashCode());
            result = prime * result
                    + ((subjectId3 == null) ? 0 : subjectId3.hashCode());
            return result;
        }


        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            lesson33DTO other = (lesson33DTO) obj;
            if (subjectId1 == null) {
                if (other.subjectId1 != null)
                    return false;
            } else if (!subjectId1.equals(other.subjectId1))
                return false;
            if (subjectId2 == null) {
                if (other.subjectId2 != null)
                    return false;
            } else if (!subjectId2.equals(other.subjectId2))
                return false;
            if (subjectId3 == null) {
                if (other.subjectId3 != null)
                    return false;
            } else if (!subjectId3.equals(other.subjectId3))
                return false;
            return true;
        }

        @Override
        public String toString() {
            return "lesson33DTO [id=" + id + ", name=" + name + ", subjectId1="
                    + subjectId1 + ", subjectId2=" + subjectId2
                    + ", subjectId3=" + subjectId3 + ", state=" + state + "]";
        }


    }
    
    public void addAfrClass(ObjectId xqid, ObjectId schoolId, ObjectId gradeId) {
    	AfreshClassEntry afreshClassEntry =  new AfreshClassEntry();
    	afreshClassEntry.setXQId(xqid);
    	afreshClassEntry.setSchoolId(schoolId);
    	afreshClassEntry.setGradeId(gradeId);
    	AfreshClassSetEntry afreshClassSetEntry = afreshClassSetDao.getEntryBySchoolId(xqid,schoolId,gradeId);
    	afreshClassEntry.setRuleId(afreshClassSetEntry.getID());
    	List<AfreshClassEntry> entrylist = new ArrayList<AfreshClassEntry>();
    	if(afreshClassSetEntry!=null){
            entrylist = afreshClassDao.findByGradeIdId(afreshClassSetEntry.getID());
        }
    	if(entrylist.size() > 0){
    		afreshClassEntry.setXh(entrylist.size() + 1);
    		afreshClassEntry.setName("");
    		afreshClassEntry.setType(entrylist.get(0).getType());
    		afreshClassDao.addEntry(afreshClassEntry);
    	}
    }
    
    
}
