package com.fulaan.new33.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.db.new33.afresh.*;
import com.fulaan.new33.dto.*;
import com.fulaan.utils.RESTAPI.bo.group.GroupAPI;
import com.fulaan.utils.StringUtil;
import com.pojo.new33.afresh.*;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.Region;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import com.db.new33.ExamXYZLDao;
import com.db.new33.SchoolSelectLessonSetDao;
import com.db.new33.isolate.ClassDao;
import com.db.new33.isolate.GradeDao;
import com.db.new33.isolate.N33_ClassDao;
import com.db.new33.isolate.N33_StudentDao;
import com.db.school.NewClassDao;
import com.fulaan.new33.dto.isolate.ClassInfoDTO;
import com.fulaan.new33.dto.isolate.GradeDTO;
import com.fulaan.new33.dto.isolate.N33_KSDTO;
import com.fulaan.new33.service.isolate.IsolateClassService;
import com.fulaan.new33.service.isolate.IsolateSubjectService;
import com.mongodb.DBObject;
import com.pojo.new33.ExamXYZLEntry;
import com.pojo.new33.SchoolSelectLessonSetEntry;
import com.pojo.new33.isolate.ClassEntry;
import com.pojo.new33.isolate.Grade;
import com.pojo.new33.isolate.StudentEntry;
import com.pojo.utils.MongoUtils;
import com.sys.utils.CombineUtils;

/**
 * Created by James on 2019-05-11.
 */
@Service
public class N33_AfreshService {

    private ExamXYZLDao examXYZLDao = new ExamXYZLDao();

    private AnalyzeUtils analyzeUtils = new AnalyzeUtils();

    private SchoolSelectLessonSetDao schoolSelectLessonSetDao =new SchoolSelectLessonSetDao();
    private IsolateSubjectService subjectService = new IsolateSubjectService();
    public static final Map<String,Integer> subject_sort = new HashMap<String, Integer>();
    public static final Map<String,Integer> subjectHead_sort = new HashMap<String, Integer>();
    private N33_StudentDao N33_StudentDao = new N33_StudentDao();
    private ClassDao classDao = new ClassDao();
    private GradeDao gradeDao = new GradeDao();
    private N33_ClassDao n33ClassDao = new N33_ClassDao();
    private NewClassDao newClassDao = new NewClassDao();

    private AfreshTypeDao afreshTypeDao = new AfreshTypeDao();

    private AfreshSubRuleDao afreshSubRuleDao = new AfreshSubRuleDao();

    private AfreshClassDao afreshClassDao = new AfreshClassDao();

    private AfreshClassRuleDao afreshClassRuleDao = new AfreshClassRuleDao();

    private AfreshClassSetDao afreshClassSetDao = new AfreshClassSetDao();

    private PerformanceXYZLDao performanceXYZLDao = new PerformanceXYZLDao();

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

    public void addClass(ObjectId schoolId, ObjectId gradeId, ObjectId termId) {
        AfreshClassEntry afreshClassEntry = new AfreshClassEntry();
        Grade gradeEntry = gradeDao.findIsolateGradeEntryByGradeId(gradeId, termId);
        GradeDTO gradeDTO  = new GradeDTO(gradeEntry);
        List<AfreshClassEntry> list = afreshClassDao.getClassEntries(schoolId, gradeId, termId);
        afreshClassEntry.setXQId(termId);
        afreshClassEntry.setSchoolId(schoolId);
        afreshClassEntry.setGradeId(gradeId);
        afreshClassEntry.setType(1);
        afreshClassEntry.setRuleId(list.get(0).getRuleId());
        afreshClassDao.addEntry(afreshClassEntry);
    }

    public void delClassById(String  classIds) {
        List<ObjectId> classIdList = new ArrayList<ObjectId>();
        classIdList = MongoUtils.convert(classIds);
        for(ObjectId classId : classIdList){
            afreshClassDao.delClassById(classId);
        }
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

    public List<Map> getNewAfreshSubList(ObjectId xqid,ObjectId schoolId,ObjectId gradeId,String type){
    	List<Map> dataList = new ArrayList<Map>();
        //查询是否已经建立设置规定
    	List<ChooseSetEntry> chooseSetList =  new ArrayList<ChooseSetEntry>();
    	chooseSetList = afreshClassDao.getChooseSetEntries( schoolId,  gradeId,  xqid);
    	if(chooseSetList.size() == 0){//没有设置规定 初始化
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
                	ChooseSetEntry chooSetEntry = new ChooseSetEntry();
                	chooSetEntry.setCiId(xqid);
                	chooSetEntry.setSchoolId(schoolId);
                	chooSetEntry.setGradeId(gradeId);
                	chooSetEntry.setType("3");
                	chooSetEntry.setSubComName(str+"("+integer+")");
                	chooSetEntry.setClassContain(0);
                	chooSetEntry.setFloatNum(0);
                	chooSetEntry.setClassNum(0);
                	afreshClassDao.addChooseEntry(chooSetEntry);
                }
            }
    	}
    	chooseSetList = afreshClassDao.getChooseSetEntries( schoolId,  gradeId,  xqid);
    	for(ChooseSetEntry chooSetEntry : chooseSetList){
        	Map dataMap = new HashMap();
        	dataMap.put("id", chooSetEntry.getID().toString());
        	dataMap.put("name", chooSetEntry.getSubComName());
        	dataMap.put("floatNum", chooSetEntry.getFloatNum());
        	dataMap.put("classNum", chooSetEntry.getClassNum());
        	dataMap.put("classContain", chooSetEntry.getClassContain());
        	String str = chooSetEntry.getSubComName();
        	dataMap.put("subName", str.substring(0 , 3));
        	dataList.add(dataMap);
        }
        return dataList;
    }
    
    
    public void updSubList( List<Map> chooseSetList){
    	
        for(Map chooSetEntry : chooseSetList){
        	afreshClassDao.updSubList(new ObjectId(chooSetEntry.get("id").toString()),Integer.parseInt(chooSetEntry.get("classContain").toString()),Integer.parseInt(chooSetEntry.get("classNum").toString()),Integer.parseInt(chooSetEntry.get("floatNum").toString()));
        }
    }
    
    
    
    public void goAfreshClassEntry(ObjectId xqid,ObjectId schoolId,ObjectId gradeId,String reportId,List<Map<String,Object>> ruleMapList) throws Exception{
        //所有学生选课
        List<StudentEntry> studentEntries = N33_StudentDao.getStudentByXqidAndGradeId(gradeId,xqid);
        Grade grade = gradeDao.findIsolateGradeEntryByGradeId(gradeId,xqid);
        Map<String,Integer> countMap = new HashMap<String, Integer>();
        Map<String,List<StudentEntry>> studentMap = new HashMap<String, List<StudentEntry>>();
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
                studentMap.put(co,entryList);

            }
        }
        Map<String,List<N33_AfreshMapDTO>> ruleMap = new HashMap<String, List<N33_AfreshMapDTO>>();
        //解析并保存规则
        ObjectId reId = StringUtils.isEmpty(reportId)?null:new ObjectId(reportId);
        AfreshClassSetEntry afreshClassSetEntry = new AfreshClassSetEntry(xqid,
                schoolId,gradeId,reId,1,0,new ArrayList<ObjectId>());
        afreshClassSetEntry.setIsRemove(1);
        afreshClassSetDao.addEntry(afreshClassSetEntry);
        ObjectId setId = afreshClassSetEntry.getID();
        //1.设置分段
        //保存每种组合的容量浮动信息（以最终数据为准）
        Map<String,Map<String,Integer>> volumeMap = new HashMap<String, Map<String,Integer>>();
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

            getFirstRuleType(count,allNumber,allVolume,allSwim,allType,compose,ruleMap,volumeMap);

        }
        //2.满额分班(重点班优先分配)
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
                if(shen <= duanMin){
                    //剩余量小于最小容量，待走班人数
                    exMap.put(dto.getCompose(),shen);
                    break;
                }
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
        getSecondRuleType(wuMapList,ruleMap,resultList);
        //历史分班
        getSecondRuleType(liMapList,ruleMap,resultList);

        //分班
        if(reportId!=null && !reportId.equals("")){
            //获取成绩单
            ObjectId rid = new ObjectId(reportId);
            Map<ObjectId,Double> map= new HashMap<ObjectId, Double>();
            getThreeRuleType(rid,map);
            //按成绩排名
            for(Map.Entry<String,List<StudentEntry>> m:studentMap.entrySet()){
                //排序
                try{
                    List<StudentEntry> studentEntryList = m.getValue();
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
                        String cs = dto1.getStringList();
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
        for(Map.Entry<String,List<N33_AfreshMapDTO>> entry  : ruleMap.entrySet()){
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
                                schoolId,grade.getName()+"("+classIndex+")"+"班",objectIdList,"",null,1,gradeId,"",classIndex,setId);
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

    /**
     * 第三次按成绩分班
     */
    public void getThreeRuleType(ObjectId exid,Map<ObjectId,Double> map) {
        //总成绩计算
        List<PerformanceXYZLEntry> en = performanceXYZLDao.findPerformanceList(exid);
        for(PerformanceXYZLEntry p : en){
            Double d = map.get(p.getStudentId())==null?0:map.get(p.getStudentId());
            List<Score> scoreList =  p.getScoreList();
            if(scoreList!=null){
                for(Score score:scoreList){
                    d = add(d,score.getSubjectScore());
                }
            }
            map.put(p.getStudentId(),d);
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
        //数量排序
        Collections.sort(wuMapList, new Comparator<N33_AfreshCompareDTO>() {
            @Override
            public int compare(N33_AfreshCompareDTO o1, N33_AfreshCompareDTO o2) {
                if (o1.getCount() > o2.getCount()) {
                    return -1;
                } else if ((o1.getCount() == o2.getCount())){
                    return 0;
                } else {
                    return 1;
                }
            }
        });
        Collections.shuffle(wuMapList);
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
                            dto.setSubjectType(wuDto2.getN1());
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
                            dto.setSubjectType(wuDto2.getN1());
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
                            dto.setLevel(wuDto.getN3());
                            dto.setMainLevel(wuDto.getN2());
                            dto.setSubjectType(wuDto2.getN1());
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
                            dto.setLevel(wuDto.getN3());
                            dto.setMainLevel(wuDto.getN2());
                            dto.setSubjectType(wuDto2.getN1());
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

        // 新组成的班级(所有符合条件的组成不了一个班级的只能选择被拆除后冗余)
        Map<String,Integer> newCount = new HashMap<String, Integer>();
        for(N33_AfreshCompareDTO wuDto : wuMapList){
            if(wuDto.getRemove()==1 || wuDto.getCount()==0){
                continue;
            }
            Integer i = newCount.get(wuDto.getCompose())==null?0: newCount.get(wuDto.getCompose());
            for(N33_AfreshCompareDTO wuDto2 : wuMapList){
                if(wuDto2.getRemove()==1 || wuDto2.getCount()==0){
                    continue;
                }
                if(!wuDto.getCompose().equals(wuDto2.getCompose())){
                    if(wuDto.getN2()==wuDto2.getN2()){
                        i += wuDto2.getCount();
                    }else if(wuDto.getN2()==wuDto2.getN3()){
                        i += wuDto2.getCount();
                    }else if(wuDto.getN3()==wuDto2.getN2()){
                        i += wuDto2.getCount();
                    }else if(wuDto.getN3()==wuDto2.getN3()){
                        i += wuDto2.getCount();
                    }
                }
            }
            i += wuDto.getCount();
            newCount.put(wuDto.getCompose(),i);
            if(wuDto.getCount()==0){
                wuDto.setRemove(1);
            }
        }
        //判断是否可拆
        for(N33_AfreshCompareDTO wuDto : wuMapList){
            Integer i =  newCount.get(wuDto.getCompose());
            int minCount = wuDto.getVolume() - wuDto.getSwim();
            //所有符合条件的都组成不了一个班
            if(i!=null && i < minCount){
                wuDto.setZu(1);
            }else{
                wuDto.setZu(0);
            }
        }
        //4. 尝试拆除组合
        for(N33_AfreshCompareDTO wuDto : wuMapList){
            if(wuDto.getRemove()==1 || wuDto.getCount()==0){
                continue;
            }
            if(wuDto.getZu()==0 && wuDto.getCount()>0){
                //可组合尝试组合
                //人数
                int count = wuDto.getCount();
                //最大容量
                int zmax = wuDto.getVolume() + wuDto.getSwim();
                int zmin = wuDto.getVolume() - wuDto.getSwim();
                //倒序循环获得
                for(int i =  wuMapList.size()-1; i < 0; i--){
                    N33_AfreshCompareDTO wuDto2 = wuMapList.get(i);
                    if(wuDto2.getRemove()==1){
                        continue;
                    }
                    int count2  = count + wuDto2.getCount();
                    //非自己
                    if(!wuDto.getCompose().equals(wuDto2.getCompose())){
                        if(wuDto.getN2()==wuDto2.getN2()){
                            if(count2>=zmin){
                                //满足最小余量
                                //扣除
                                if(count2 <= wuDto.getVolume()){
                                    //全部扣除
                                    N33_AfreshLinDTO dto = new N33_AfreshLinDTO();
                                    dto.setNumber(count2);
                                    dto.setLevel(wuDto.getN3());
                                    dto.setMainLevel(wuDto.getN2());
                                    dto.setSubjectType(wuDto2.getN1());
                                    dto.setMin(zmin);
                                    dto.setMax(zmax);
                                    dto.setType(0);
                                    dto.setStringList(wuDto.getCompose() + "#" + count + "," + wuDto2.getCompose()+"#"+wuDto2.getCount());
                                    wuDto.setRemove(1);
                                    wuDto2.setRemove(1);
                                    wuDto.setCount(0);
                                    wuDto2.setCount(0);
                                    resultList.add(dto);
                                    break;
                                }else{
                                    //部分扣除(放入容量大小)
                                    N33_AfreshLinDTO dto = new N33_AfreshLinDTO();
                                    dto.setNumber(wuDto.getVolume());
                                    dto.setLevel(wuDto.getN3());
                                    dto.setMainLevel(wuDto.getN2());
                                    dto.setSubjectType(wuDto2.getN1());
                                    dto.setMin(zmin);
                                    dto.setMax(zmax);
                                    dto.setType(0);
                                    int linCount = wuDto.getVolume() - wuDto.getCount();
                                    dto.setStringList(wuDto.getCompose() + "#" + count + "," + wuDto2.getCompose()+"#"+linCount);
                                    wuDto.setCount(0);
                                    wuDto.setRemove(1);
                                    wuDto2.setCount(wuDto2.getCount()-linCount);
                                    resultList.add(dto);
                                    break;
                                }
                            }
                        }else if(wuDto.getN2()==wuDto2.getN3()){
                            if(count2>=zmin){
                                //满足最小余量
                                //扣除
                                if(count2 <= wuDto.getVolume()){
                                    //全部扣除
                                    N33_AfreshLinDTO dto = new N33_AfreshLinDTO();
                                    dto.setNumber(count2);
                                    dto.setLevel(wuDto.getN3());
                                    dto.setMainLevel(wuDto.getN2());
                                    dto.setSubjectType(wuDto2.getN1());
                                    dto.setMin(zmin);
                                    dto.setMax(zmax);
                                    dto.setType(0);
                                    dto.setStringList(wuDto.getCompose() + "#" + count + "," + wuDto2.getCompose()+"#"+wuDto2.getCount());
                                    wuDto.setRemove(1);
                                    wuDto2.setRemove(1);
                                    wuDto.setCount(0);
                                    wuDto2.setCount(0);
                                    resultList.add(dto);
                                    break;
                                }else{
                                    //部分扣除(放入容量大小)
                                    N33_AfreshLinDTO dto = new N33_AfreshLinDTO();
                                    dto.setNumber(wuDto.getVolume());
                                    dto.setLevel(wuDto.getN3());
                                    dto.setMainLevel(wuDto.getN2());
                                    dto.setSubjectType(wuDto2.getN1());
                                    dto.setMin(zmin);
                                    dto.setMax(zmax);
                                    dto.setType(0);
                                    int linCount = wuDto.getVolume() - wuDto.getCount();
                                    dto.setStringList(wuDto.getCompose() + "#" + count + "," + wuDto2.getCompose()+"#"+linCount);
                                    wuDto.setRemove(1);
                                    wuDto.setCount(0);
                                    wuDto2.setCount(wuDto2.getCount()-linCount);
                                    resultList.add(dto);
                                    break;
                                }
                            }
                        }else if(wuDto.getN3()==wuDto2.getN2()){
                            if(count2>=zmin){
                                //满足最小余量
                                //扣除
                                if(count2 <= wuDto.getVolume()){
                                    //全部扣除
                                    N33_AfreshLinDTO dto = new N33_AfreshLinDTO();
                                    dto.setNumber(count2);
                                    dto.setLevel(wuDto.getN3());
                                    dto.setMainLevel(wuDto.getN2());
                                    dto.setSubjectType(wuDto2.getN1());
                                    dto.setMin(zmin);
                                    dto.setMax(zmax);
                                    dto.setType(0);
                                    dto.setStringList(wuDto.getCompose() + "#" + count + "," + wuDto2.getCompose()+"#"+wuDto2.getCount());
                                    wuDto.setRemove(1);
                                    wuDto.setCount(0);
                                    wuDto2.setRemove(1);
                                    resultList.add(dto);
                                    break;
                                }else{
                                    //部分扣除(放入容量大小)
                                    N33_AfreshLinDTO dto = new N33_AfreshLinDTO();
                                    dto.setNumber(wuDto.getVolume());
                                    dto.setLevel(wuDto.getN3());
                                    dto.setMainLevel(wuDto.getN2());
                                    dto.setSubjectType(wuDto2.getN1());
                                    dto.setMin(zmin);
                                    dto.setMax(zmax);
                                    dto.setType(0);
                                    int linCount = wuDto.getVolume() - wuDto.getCount();
                                    dto.setStringList(wuDto.getCompose() + "#" + count + "," + wuDto2.getCompose()+"#"+linCount);
                                    wuDto.setRemove(1);
                                    wuDto.setCount(0);
                                    wuDto2.setCount(wuDto2.getCount()-linCount);
                                    resultList.add(dto);
                                    break;
                                }
                            }
                        }else if(wuDto.getN3()==wuDto2.getN3()){
                            if(count2>=zmin){
                                //满足最小余量
                                //扣除
                                if(count2 <= wuDto.getVolume()){
                                    //全部扣除
                                    N33_AfreshLinDTO dto = new N33_AfreshLinDTO();
                                    dto.setNumber(count2);
                                    dto.setLevel(wuDto.getN3());
                                    dto.setMainLevel(wuDto.getN2());
                                    dto.setSubjectType(wuDto2.getN1());
                                    dto.setMin(zmin);
                                    dto.setMax(zmax);
                                    dto.setType(0);
                                    dto.setStringList(wuDto.getCompose() + "#" + count + "," + wuDto2.getCompose()+"#"+wuDto2.getCount());
                                    wuDto.setRemove(1);
                                    wuDto2.setRemove(1);
                                    resultList.add(dto);
                                    break;
                                }else{
                                    //部分扣除(放入容量大小)
                                    N33_AfreshLinDTO dto = new N33_AfreshLinDTO();
                                    dto.setNumber(wuDto.getVolume());
                                    dto.setLevel(wuDto.getN3());
                                    dto.setMainLevel(wuDto.getN2());
                                    dto.setSubjectType(wuDto2.getN1());
                                    dto.setMin(zmin);
                                    dto.setMax(zmax);
                                    dto.setType(0);
                                    int linCount = wuDto.getVolume() - wuDto.getCount();
                                    dto.setStringList(wuDto.getCompose() + "#" + count + "," + wuDto2.getCompose()+"#"+linCount);
                                    wuDto.setRemove(1);
                                    wuDto2.setCount(wuDto2.getCount()-linCount);
                                    resultList.add(dto);
                                    break;
                                }
                            }
                        }
                    }
                    //所有组合都不够

                }
            }
        }
        //放入临时班级
        //Map<String,List<N33_AfreshMapDTO>> ruleMap

        //5 仍有冗余,向上冗余(优先同组合）
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
                }else if(wuDto.getN2()==linDTO.getLevel()){
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
                }else if(wuDto.getN3()==linDTO.getMainLevel()){
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
                }else if(wuDto.getN3()==linDTO.getLevel()){
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
                int n3  = getStringIndex(string,3);
                if(wuDto.getN2()==n2 || wuDto.getN2() == n3 || wuDto.getN3() == n2 || wuDto.getN3() ==n3){
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
        //7 同大组放入
        for(N33_AfreshCompareDTO wuDto : wuMapList) {
            if (wuDto.getRemove() == 1 || wuDto.getCount() == 0) {
                continue;
            }
            //冗余临时班级
            for(N33_AfreshLinDTO linDTO:resultList){
                if(linDTO.getSubjectType()!=wuDto.getN1()){
                    continue;
                }
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
            //冗余上级班级
            for(Map.Entry<String,List<N33_AfreshMapDTO>> ma : ruleMap.entrySet()) {
                String string = ma.getKey();
                int n1 = getStringIndex(string, 1);
                //非同一类
                if (n1 != wuDto.getN1()) {
                    continue;
                }
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


        //8 同类冗余仍有剩余，直接放入  尝试组合成一个班
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
            ruleMap.put(compose,dtoList);
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

    public Map<String, Object> updateClassXh(ObjectId classId, int xh) {
        Map<String, Object> reMap = new HashMap<String, Object>();
        int stateCode=0;
        AfreshClassEntry e = afreshClassDao.getEntry(classId);
        Grade grade = gradeDao.findIsolateGradeEntryByGradeId(e.getGradeId(), e.getXQId());
        String className = grade.getName()+"("+xh+")"+"班";
        e.setXh(xh);
        e.setClassName(className);
        List<AfreshClassEntry> list = afreshClassDao.getEntries(e.getSchoolId(), e.getGradeId(), e.getXQId(), e.getRuleId(), xh);
        if(list.size()==0){
            afreshClassDao.updateClassEntry(e);
        }else{
            List<ObjectId> classIds = MongoUtils.getFieldObjectIDs(list);
            if(classIds.size()==1&&classIds.contains(classId)){
                afreshClassDao.updateClassEntry(e);
            }else{
                stateCode=1;
            }
        }
        reMap.put("stateCode", stateCode);
        reMap.put("className", className);
        return reMap;
    }

    public void updateStusClass(List<Map<String, Object>> stuInfos) {
        for(Map<String, Object> stu:stuInfos){
            String stuId = stu.get("stuId").toString();
            String oldClassId = stu.get("oldClassId").toString();
            String newClassId = stu.get("newClassId").toString();
            updateClass(new ObjectId(stuId), new ObjectId(oldClassId), new ObjectId(newClassId));
        }
    }

    public void updateClass(ObjectId userId, ObjectId oldClassId, ObjectId newClassId){
        AfreshClassEntry oldEntry = afreshClassDao.getEntry(oldClassId);
        AfreshClassEntry newEntry = afreshClassDao.getEntry(newClassId);
        if(oldEntry!=null && newEntry!=null){
            StudentEntry stuEntry = N33_StudentDao.getStudentEntry(oldEntry.getGradeId(), oldEntry.getXQId(), oldClassId, userId);
            if(null!=stuEntry){
                stuEntry.setClassId(newClassId);
                N33_StudentDao.updateStudentEntry(stuEntry);
            }
            List<ObjectId> oldIdList = oldEntry.getStudentList();
            List<ObjectId> newIdList = newEntry.getStudentList();
            if(oldClassId!=null && oldIdList.contains(userId)){
                if(!newIdList.contains(userId)) {
                    newIdList.add(userId);
                    newEntry.setUserList(newIdList);
                    afreshClassDao.addEntry(newEntry);
                }
                oldIdList.remove(userId);
                oldEntry.setUserList(oldIdList);
                afreshClassDao.addEntry(oldEntry);
            }
        }
    }

    //todo   导出分班结果
    public  void  exportStuSelectResult(ObjectId xqid,ObjectId schoolId,ObjectId gradeId,HttpServletResponse response){
       // List<StudentEntry> studentEntryList = N33_StudentDao.getStudentByXqidAndGradeId(gradeId,xqid);
      //  Map<ObjectId, ClassEntry> classEntryMap = N33_ClassDao.findClassEntryMap(gradeId, xqid);
        HSSFWorkbook wb = new HSSFWorkbook();

        HSSFCellStyle bodyStyle = wb.createCellStyle();
        bodyStyle.setWrapText(true);
        bodyStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中
        //生成一个sheet1
        HSSFSheet sheet = wb.createSheet("导入");
        //为sheet1生成第一行，用于放表头信息
        HSSFRow row = sheet.createRow(0);
        sheet.setColumnWidth(0, 3500);
        sheet.setColumnWidth(1, 1500);
        sheet.setColumnWidth(2, 3500);
        sheet.setColumnWidth(3, 3500);
        HSSFCell cell = row.createCell(0);
        cell.setCellStyle(bodyStyle);
        cell.setCellValue("班级");
        cell = row.createCell(1);
        cell.setCellStyle(bodyStyle);
        cell.setCellValue("人数");
        cell = row.createCell(2);
        cell.setCellStyle(bodyStyle);
        cell.setCellValue("走班课程");
        cell = row.createCell(3);
        cell.setCellStyle(bodyStyle);
        cell.setCellValue("组合");
        //查询最近的一条分班结果
        AfreshClassSetEntry afreshClassSetEntry = afreshClassSetDao.getEntryBySchoolId(xqid,schoolId,gradeId);
        List<AfreshClassEntry> entrylist = new ArrayList<AfreshClassEntry>();

        if(afreshClassSetEntry!=null){
            entrylist = afreshClassDao.findByGradeIdId(afreshClassSetEntry.getID());
        }
        List<StudentEntry> studentEntries = N33_StudentDao.getStudentByXqidAndGradeId(gradeId, xqid);
        Map<ObjectId,StudentEntry> map  = new HashMap<ObjectId, StudentEntry>();
        for(StudentEntry studentEntry : studentEntries){
            map.put(studentEntry.getUserId(),studentEntry);
        }
        int index = 0;
        String strings = "!物历化生政地";
        for(AfreshClassEntry afreshClassEntry:entrylist){
            List<ObjectId> objectIdList = afreshClassEntry.getStudentList()==null?new ArrayList<ObjectId>():afreshClassEntry.getStudentList();
            Map<String,Integer> map1 = new HashMap<String, Integer>();
            for(ObjectId oid:objectIdList){
                StudentEntry studentEntry = map.get(oid);
                if(studentEntry!=null){
                    Integer integer = map1.get(studentEntry.getCombiname())==null?0:map1.get(studentEntry.getCombiname());
                    integer++;
                    map1.put(studentEntry.getCombiname(),integer);
                }
            }
            StringBuffer sb = new StringBuffer();
            int count = 0;
            Map<Integer,Integer> map3 = new HashMap<Integer, Integer>();
            for(Map.Entry<String,Integer> m : map1.entrySet()){
                sb.append(m.getKey());
                sb.append("(");
                sb.append(m.getValue()+"");
                sb.append(")");
                sb.append("\r\n");
                count++;
                String s = m.getKey();
                int n1 = getStringIndex(s, 1);
                Integer integer1 = map3.get(n1)==null?0:map3.get(n1);
                integer1++;
                map3.put(n1,integer1);
                int n2 = getStringIndex(s, 2);
                Integer integer2 = map3.get(n2)==null?0:map3.get(n2);
                integer2++;
                map3.put(n2,integer2);
                int n3 = getStringIndex(s, 3);
                Integer integer3 = map3.get(n3)==null?0:map3.get(n3);
                integer3++;
                map3.put(n3,integer3);
            }
            String str = sb.substring(0,sb.length()-2);

            String str2 = "";
            List<Integer> list = new ArrayList<Integer>();
            int number = 0;
            for(Map.Entry<Integer,Integer> p : map3.entrySet()){
                if(p.getValue()==count){
                    list.add(p.getKey());
                }
                number++;
            }
            //当自己调整了，就无法统计了
            if(number>3){
                //走班
                for(Integer i:list){
                    str2 =  str2 + strings.substring(i,i+1) + "、" ;
                }
                if(!"".equals(str2)){
                    str2 = str2.substring(0,str2.length()-1);
                }
            }

            HSSFRow row1 = sheet.createRow(index + 1);
            HSSFCell cell1 = row1.createCell(0);
            cell1.setCellValue(afreshClassEntry.getClassName());
            cell1.setCellStyle(bodyStyle);
            cell1 = row1.createCell(1);
            cell1.setCellValue(objectIdList.size());
            cell1.setCellStyle(bodyStyle);
            cell1 = row1.createCell(2);
            cell1.setCellValue(str2);
            cell1.setCellStyle(bodyStyle);
            cell1 = row1.createCell(3);
            cell1.setCellStyle(bodyStyle);
            cell1.setCellValue(new HSSFRichTextString(str));
            index++;
        }


        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            wb.write(os);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] content = os.toByteArray();
        OutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            response.setContentType("application/force-download");
            response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("导出分班结果.xls", "UTF-8"));
            response.setContentLength(content.length);
            outputStream.write(content);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }


    public Map<String,Object> getNewStuSelectResultByClass(ObjectId xqid,ObjectId schoolId,ObjectId gradeId,Integer type,String reportId){
        Map<String,Object> result = new HashMap<String, Object>();

        SchoolLesson33DTO schoolLesson33DTO = getSchoolSelects(xqid,schoolId,gradeId);
        List<lesson33DTO> listLesson33DTO = schoolLesson33DTO.getList();
        //查询最近的一条分班结果
        AfreshClassSetEntry afreshClassSetEntry = afreshClassSetDao.getEntryBySchoolId(xqid,schoolId,gradeId);
        List<AfreshClassEntry> entrylist = new ArrayList<AfreshClassEntry>();

        if(afreshClassSetEntry!=null){
            entrylist = afreshClassDao.findByGradeIdId(afreshClassSetEntry.getID());
        }
        int ind = entrylist.size();
        Map<ObjectId,ObjectId> studentClassMap = new HashMap<ObjectId, ObjectId>();
        Map<ObjectId,String> studentClassNameMap = new HashMap<ObjectId, String>();
        for(AfreshClassEntry afreshClassEntry:entrylist){
            List<ObjectId> slist = afreshClassEntry.getStudentList();
            if(slist!=null){
                for(ObjectId os : slist){
                    studentClassMap.put(os, afreshClassEntry.getID());
                    studentClassNameMap.put(os, afreshClassEntry.getClassName());
                }
            }

        }

        Grade grade = gradeDao.findIsolateGradeEntryByGradeId(gradeId,xqid);
        List<ObjectId> studentIds = new ArrayList<ObjectId>();
        List<Map<String,String>> head = new ArrayList<Map<String,String>>();// 标题list
        for(AfreshClassEntry classEntry:entrylist){
            Map<String,String> map = new HashMap<String, String>();
            map.put("zuHeType", type+"");
            map.put("name", classEntry.getName());
            map.put("xh", classEntry.getXh()+"");
            map.put("classId", classEntry.getID().toString());
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
        Map<String,Integer> stringMap = new HashMap<String, Integer>();
        List<ObjectId> classIds = new ArrayList<ObjectId>();
        for (StudentEntry studentEntry : studentEntries) {
        	if(!classIds.contains(studentEntry.getClassId())){
                classIds.add(studentEntry.getClassId());
            }
            if(!"".equals(studentEntry.getCombiname()) && studentEntry.getCombiname() != null){
                count ++;
            }
            if(ind==0){
                String str  = studentEntry.getCombiname();
                Integer integer1 =  stringMap.get(str)==null?0:stringMap.get(str);
                integer1++;
                stringMap.put(str,integer1);
            }
        }

        List<StudentEntry> studentList = N33_StudentDao.getStuList(studentIds, xqid);// 查询各个班级的所有学生
        Map<ObjectId, List<StudentEntry>> classId_stus = new HashMap<ObjectId, List<StudentEntry>>();

        List<String> stuIds = new ArrayList<String>();
        //放入新班级id
        if(studentList!=null) {
            for (StudentEntry entry : studentList) {
                ObjectId oid = studentClassMap.get(entry.getUserId());
                if(oid!=null){
                    entry.setClassId(oid);
                }
                if(!stuIds.contains(entry.getUserId().toString())){
                    stuIds.add(entry.getUserId().toString());
                }
                List<StudentEntry> templist = classId_stus.get(entry.getClassId())==null?new ArrayList<StudentEntry>():classId_stus.get(entry.getClassId());
                templist.add(entry);
                classId_stus.put(entry.getClassId(), templist);
            }
        }
        Map<String, String> stuClassMap = GroupAPI.getStuClassMap(schoolId.toString(), stuIds);
        for(String stuClassId:stuClassMap.values()){
            if(!classIds.contains(new ObjectId(stuClassId))){
                classIds.add(new ObjectId(stuClassId));
            }
        }
        List<String> combinationNames = generateCombination(xqid, schoolId, gradeId, type);
        Map<String,Map<ObjectId,Integer>> name_class_num = new HashMap<String, Map<ObjectId,Integer>>();
        Map<String,List<Map<String,Object>>> name_StudentList=new HashMap<String, List<Map<String, Object>>>();

        Map<ObjectId, com.pojo.newschool.ClassEntry> clsMap = newClassDao.getClassMapByIds(classIds);

        for(String name:combinationNames) {
            List<Map<String,Object>> stuInfos = new ArrayList<Map<String, Object>>();
            Map<ObjectId,Integer> class_num = name_class_num.get(name)==null?new HashMap<ObjectId, Integer>():name_class_num.get(name);
            for(AfreshClassEntry klass:entrylist) {
                ObjectId klassId = klass.getID();
                Integer i = class_num.get(klassId)==null?0:class_num.get(klassId);
                List<StudentEntry> sutdentList = classId_stus.get(klassId);
                if(sutdentList!=null) {
                    for(StudentEntry stu:sutdentList) {
                    	//获取基础数据中的班级信息
                        Map<String,Object> map=new HashMap<String, Object>();
                        String combiname = stu.getCombiname();
                        List<String> belongNames = judgeSelect(combinationNames, combiname, type);
                        if(belongNames.contains(name)) {
                            map.put("stuId",stu.getUserId().toString());
                            map.put("clsXh",klass.getXh()+"");
                            map.put("clsId",klassId.toString());
                            map.put("stuName",stu.getUserName());
                            map.put("stuNum",stu.getStudyNum());
                            map.put("className",studentClassNameMap.get(stu.getUserId()));
                            com.pojo.newschool.ClassEntry classE = null;
                            if(null!=stu.getClassId()){
                                classE = clsMap.get(stu.getClassId());
                            }
                            if(null==classE){
                                String stuClassId = stuClassMap.get(stu.getUserId().toString());
                                if(StringUtil.isEmpty(stuClassId)){
                                    classE = clsMap.get(new ObjectId(stuClassId));
                                }
                            }
                            if(null!=classE){
                                map.put("oldClassName",grade.getName() + "(" + classE.getName() + ")" + "班");
                            }else {
                                map.put("oldClassName", "");
                            }
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
                            map.put("num","");
                            stuInfos.add(map);
                            i++;
                        }
                    }

                }
                class_num.put(klassId, i);
                if(!reportId.equals("")){
	                //获取成绩单
	                ObjectId rid = new ObjectId(reportId);
	                Map<ObjectId,Double> stuCJmap = new HashMap<ObjectId, Double>();
	                getThreeRuleType(rid,stuCJmap);
	                stuCJmap = sortDescend(stuCJmap); // Map的value值降序排序
	                Map<ObjectId,Integer> stuNummap = new HashMap<ObjectId, Integer>();
	                int num = 1;
	                ObjectId prevKey = null;
	                for (ObjectId key : stuCJmap.keySet()) {
	                	if(num == 1){
	                		prevKey = key;
	                		stuNummap.put(key, num);
	                	}
	                	if(num != 1 && stuCJmap.get(key).equals(stuCJmap.get(prevKey))){
	                		stuNummap.put(key, num);
	                	}else{
	                		num++;
	                		stuNummap.put(key, num);
	                	}
	                	prevKey = key;
	                }
	                //按成绩排名
	                for(Map<String,Object> stuMap : stuInfos){
	                	stuMap.put("num", stuNummap.get(new ObjectId(stuMap.get("stuId").toString())));
	                    //排序
	                    Double d  = stuCJmap.get(new ObjectId(stuMap.get("stuId").toString()));
	                    if(d!=null){
	                        int c = (int)(double)d;
	                        stuMap.put("cj", c + "");
	                    }else{
	                    	stuMap.put("cj", "0");
	                    }
	                  }
	                Collections.sort(stuInfos, new Comparator<Map<String,Object>>(){
	                    @Override
	                    public int compare(Map<String,Object> m1, Map<String,Object> m2) {
	
	                    	int m1Key = Integer.parseInt(m1.get("cj").toString());
	                        int m2Key = Integer.parseInt(m2.get("cj").toString());
	                    	
	                        if (m1Key > m2Key) {
	                            return -1;
	                        } else if ((m1Key == m2Key)){
	                            return 0;
	                        } else {
	                            return 1;
	                        }
	                    }
	                });
                }
                name_StudentList.put(name, stuInfos);
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
            if(entrylist.size()==0){
                //无分班结果
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
                    if(ind==0){
                        int sumStr = 0;
                        for(Map.Entry<String,Integer> m : stringMap.entrySet()){
                            String string = m.getKey();
                            Integer integer = m.getValue();
                            if(type==3 && name.equals(string)){
                                sumStr += integer;
                            }
                            if(type==2 && string.indexOf(name.substring(0, 1))>=0&&string.indexOf(name.substring(1, 2))>=0) {
                                sumStr += integer;
                            }
                        }
                        map.put("name", name+"("+sumStr+")");
                    }
                }
                map.put("subName", name);
                map.put("list", list);
                map.put("stuList",name_StudentList.get(name)==null?new ArrayList<Map<String,String>>():name_StudentList.get(name));                              
                content.add(map);
            }else{
                continue;
            }
        }
        if(type==3) {
            for (Map<String, String> hd : head) {
                int stuCount = 0;
                if(null!=hd.get("classId")) {
                    ObjectId classId = new ObjectId(hd.get("classId"));
                    for (String name : combinationNames) {
                        Map<ObjectId, Integer> class_num = name_class_num.get(name) == null ? new HashMap<ObjectId, Integer>() : name_class_num.get(name);
                        stuCount+=class_num.get(classId)==null?0:class_num.get(classId);
                    }
                }
                if(stuCount==0){
                    hd.put("noStu", "true");
                }else{
                    hd.put("noStu", "false");
                }
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

    public Map<String,Object> selectClassLsit(
            ObjectId xqid,
            ObjectId schoolId,
            ObjectId gradeId,
            String classId
    ){
        AfreshClassSetEntry afreshClassSetEntry = afreshClassSetDao.getEntryBySchoolId(xqid,schoolId,gradeId);
        List<AfreshClassEntry> entrylist = afreshClassDao.findByGradeIdId(afreshClassSetEntry.getID());
        Map<String,Object> map = new HashMap<String, Object>();
        List<Map<String,Object>> mapList = new ArrayList<Map<String, Object>>();
        Map<String,Object> nowDto = new HashMap<String, Object>();
        for(AfreshClassEntry afreshClassEntry:entrylist){
            Map<String, Object> dto = new HashMap<String, Object>();
            dto.put("classId", afreshClassEntry.getID().toString());
            dto.put("className", afreshClassEntry.getClassName());
            if(StringUtil.isEmpty(classId)) {
                if (classId.equals(afreshClassEntry.getID().toString())) {
                    nowDto.put("classId", afreshClassEntry.getID().toString());
                    nowDto.put("className", afreshClassEntry.getClassName());
                } else {
                    mapList.add(dto);
                }
            }else{
                mapList.add(dto);
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
        Collections.sort(retList,new Comparator<lesson33DTO>() {
            @Override
            public int compare(lesson33DTO o1, lesson33DTO o2) {
                String o1a = o1.getName().substring(0, 1);
                String o1b = o1.getName().substring(1, 2);
                String o1c = o1.getName().substring(2, 3);
                String o2a = o2.getName().substring(0, 1);
                String o2b = o2.getName().substring(1, 2);
                String o2c = o2.getName().substring(2, 3);
                if(!o1a.equals(o2a)) {
                    return subjectHead_sort.get(o1a)-subjectHead_sort.get(o2a);
                }
                if(!o1b.equals(o2b)) {
                    return subjectHead_sort.get(o1b)-subjectHead_sort.get(o2b);
                }
                return subjectHead_sort.get(o1c)-subjectHead_sort.get(o2c);
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
    
    //导出行政班学生数据 conn
    public void exportClasStu(ObjectId xqid, ObjectId schoolId, ObjectId gradeId, Integer type, HttpServletResponse response){
    	List<Map> dataList = new ArrayList<Map>();
    	//走班学科
    	List<ObjectId> ksOids = new ArrayList<ObjectId>();//走班学科ids
    	List<N33_KSDTO> ksList = subjectService.getSubjectCanZouBan(schoolId,xqid,gradeId);
    	Map idNameMap = new HashMap();
    	for(N33_KSDTO kSDTO : ksList){
    		idNameMap.put(kSDTO.getSubid(), kSDTO.getSnm());
    		ksOids.add(new ObjectId(kSDTO.getSubid()));
    	}
    	//获取选课组合
    	SchoolLesson33DTO schoolLesson33DTO = getSchoolSelects(xqid,schoolId,gradeId);
        List<lesson33DTO> listLesson33DTOs = schoolLesson33DTO.getList();
        Map combinMap = new HashMap();//选课组合名称所对应的学科id
        for(lesson33DTO dTO : listLesson33DTOs){
        	String cId = dTO.getSub1Str() + "," + dTO.getSub2Str() + "," + dTO.getSub3Str();
        	combinMap.put(dTO.getName(), cId);
        }
        //查询最近的一条分班结果
        AfreshClassSetEntry afreshClassSetEntry = afreshClassSetDao.getEntryBySchoolId(xqid,schoolId,gradeId);
        List<AfreshClassEntry> entrylist = new ArrayList<AfreshClassEntry>();
        if(afreshClassSetEntry!=null){
            entrylist = afreshClassDao.findByGradeIdId(afreshClassSetEntry.getID());
        }
        for(AfreshClassEntry klass : entrylist){//以班为单位遍历数据
        	Map classMap = new HashMap();//等级考封装数据
        	classMap.put("className", klass.getName());
        	classMap.put("stuNum", klass.getStudentList().size());//学生人数
        	List<StudentEntry> studentList = N33_StudentDao.getStuList(klass.getStudentList(), xqid);// 查询各个班级的所有学生
        	Map combin_conutMap = new HashMap();//组合人数集合
        	Map<String,Object> combin_subMap = new HashMap<String,Object>();//组合里包含的学科
        	String subIds = "";//选课组合中其中一个组合的学科ids
        	for(StudentEntry studentEntry : studentList){
        		if(combin_conutMap.containsKey(studentEntry.getCombiname())){
        			int count = (Integer) combin_conutMap.get(studentEntry.getCombiname());
        			combin_conutMap.put(studentEntry.getCombiname(), count+1);
        		}else{
        			combin_conutMap.put(studentEntry.getCombiname(), 1);//选科组合学生人数
        			String ids = studentEntry.getSubjectId1().toString() + "," + studentEntry.getSubjectId2().toString() + "," + studentEntry.getSubjectId3().toString();
        			combin_subMap.put(studentEntry.getCombiname(), ids);//选课组合学科id
        			subIds = ids;
        		}
        	}
        	classMap.put("combinMap", combin_conutMap);//学科组合
        	String aId = "";//共同科目
        	List<ObjectId> subIdList = MongoUtils.convert(subIds);
        	List<ObjectId> subList = new ArrayList<ObjectId>();
        	for(ObjectId subOId : subIdList){
        		boolean flag = true;
        		for (Map.Entry entry : combin_subMap.entrySet()) { 
          		  String sIds = (String) entry.getValue();
          		  subList = MongoUtils.convert(sIds);
          		  if(!subList.contains(subOId))
          			  flag = false;
          		}
        		if(flag)
        			aId = aId + "," + subOId.toString();
        	}
        	List<ObjectId> djSubIds = new ArrayList<ObjectId>();//等级考选科组合里的共同科目
        	djSubIds = MongoUtils.convert(aId);
        	String gtName = "";//共同科目名称
        	for(ObjectId sId : djSubIds){
        		gtName = gtName + idNameMap.get(sId.toString()).toString().subSequence(0, 1);
        	}
        	classMap.put("gtName", gtName);
        	//走班学科名称
        	Map combin_nameMap = new HashMap();
        	List<ObjectId> fList = new ArrayList<ObjectId>();
        	for (Map.Entry entry : combin_subMap.entrySet()) { 
        	  String name = "";
      		  String sIds = (String) entry.getValue();
      		  fList = MongoUtils.convert(sIds);
      		  for(ObjectId fId : fList){
      			if(!djSubIds.contains(fId)){
      				name = name + idNameMap.get(fId.toString()).toString().subSequence(0, 1) + " " ;
      			  }
      		  }
      		combin_nameMap.put(entry.getKey().toString(), name);
      		}
        	classMap.put("zbxk", combin_nameMap);//等级考走班学科
        	//合格考选科组合-共同科目-走班学科
        	int fg = 0;
        	Map unionMap = new HashMap();//key等级考选课组合名称——value合格考选课组合map对象
        	List<ObjectId> hgTList = new ArrayList<ObjectId>();//合格考第一个组合的科目ids
        	for (Map.Entry entry : combin_subMap.entrySet()) {//遍历等级考选科组合反推合格考的科目
        		Map hg_combinMap = new HashMap();//合格考选课组合
        		String hgName = "";
        		String hgIds = "";
        		List<ObjectId> hzList = new ArrayList<ObjectId>();
        		String sIds = (String) entry.getValue();
        		fList = MongoUtils.convert(sIds);
				for(ObjectId rId : ksOids){//将所有的走班学科中的等级考的科目去除就是对应的合格考的科目
					if(!fList.contains(rId)){
						hgName = hgName + idNameMap.get(rId.toString()).toString().subSequence(0, 1);
						hgIds = hgIds + "," + rId.toString();
						hzList.add(rId);
						if(fg == 0)
							hgTList.add(rId);
					}
				}
				fg++;
				//合格考共同科目
				String hgGtId = "";
				for(ObjectId hId : hgTList){//判断学科是否每个组合都包含
					if(hzList.contains(hId))
						hgGtId = hgGtId + "," + hId.toString();
				}
				hgTList = MongoUtils.convert(hgGtId);
				hg_combinMap.put("hgName", hgName);
				hg_combinMap.put("hgIds", hgIds);
				unionMap.put((String) entry.getKey(), hg_combinMap);
        	}
        	//合格考共同科目
        	if(hgTList.size() > 0){
        		String hggtName = "";
        		for(ObjectId hId : hgTList){
        			hggtName = hggtName + idNameMap.get(hId.toString()).toString().subSequence(0, 1);
				}
        		classMap.put("hggtName", hggtName);
        	}else{
        		classMap.put("hggtName", "");
        	}
        	//走班学科
        	for (Map.Entry entry : combin_subMap.entrySet()) {//遍历等级考选科组合
        		Map hgzbMap =  (Map) unionMap.get(entry.getKey().toString());
        		String zbxkName = "";
        		List<ObjectId> zbIdList = new ArrayList<ObjectId>();//走班学科id
        		List<ObjectId> hzrmList = new ArrayList<ObjectId>();
        		hzrmList = MongoUtils.convert(hgzbMap.get("hgIds").toString());//合格考组合学科科目id
        		if(hgTList.size() > 0){
        			for(ObjectId ggId : hzrmList)
        				if(!hgTList.contains(ggId))
        					zbxkName = zbxkName + idNameMap.get(ggId.toString()).toString().subSequence(0, 1) + " ";
        			hgzbMap.put("zbkmName", zbxkName);//合格考走班科目名称
        		}else{
        			hgzbMap.put("zbkmName", hgzbMap.get("hgName"));//合格考走班科目名称
        		}
        		unionMap.put(entry.getKey().toString(), hgzbMap);
        	}
        	classMap.put("unionMap", unionMap);
        	dataList.add(classMap);//等级考学生走班数据
        }
        
        //生成表格
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFCellStyle bodyStyle = wb.createCellStyle();
        bodyStyle.setWrapText(true);
        bodyStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中
        //生成一个sheet1
        HSSFSheet sheet = wb.createSheet("走班人数数据");
        //为sheet1生成第一行，用于放表头信息
        sheet.addMergedRegion(new Region(0,(short)0,0,(short)10));
        HSSFRow row1 = sheet.createRow(0);
        HSSFCell cell = row1.createCell(0);
        cell.setCellStyle(bodyStyle);
        cell.setCellValue("等级考");
        
        sheet.addMergedRegion(new Region(0,(short)12,0,(short)22));
        cell = row1.createCell(12);
        cell.setCellStyle(bodyStyle);
        cell.setCellValue("合格考");
        
        HSSFRow row2 = sheet.createRow(1);
        cell = row2.createCell(0);
        cell.setCellStyle(bodyStyle);
        cell.setCellValue("班级");
        
        cell = row2.createCell(1);
        cell.setCellStyle(bodyStyle);
        cell.setCellValue("人数");
        
        cell = row2.createCell(2);
        cell.setCellStyle(bodyStyle);
        cell.setCellValue("共同科目");
        
        cell = row2.createCell(3);
        cell.setCellStyle(bodyStyle);
        cell.setCellValue("走班学科");
        
        cell = row2.createCell(4);
        cell.setCellStyle(bodyStyle);
        cell.setCellValue("选科组合");
        
        cell = row2.createCell(5);
        cell.setCellStyle(bodyStyle);
        cell.setCellValue("选科人数");
        
        cell = row2.createCell(6);
        cell.setCellStyle(bodyStyle);
        cell.setCellValue("走班人数");
        
        //等级考走班学科人数展示
        cell = row2.createCell(7);
        cell.setCellStyle(bodyStyle);
        cell.setCellValue("化");
        
        cell = row2.createCell(8);
        cell.setCellStyle(bodyStyle);
        cell.setCellValue("生");
        
        cell = row2.createCell(9);
        cell.setCellStyle(bodyStyle);
        cell.setCellValue("政");
        
        cell = row2.createCell(10);
        cell.setCellStyle(bodyStyle);
        cell.setCellValue("地");
        
        
        //合格考
        cell = row2.createCell(11);
        cell.setCellStyle(bodyStyle);
        cell.setCellValue("班级");
        
        cell = row2.createCell(12);
        cell.setCellStyle(bodyStyle);
        cell.setCellValue("人数");
        
        cell = row2.createCell(13);
        cell.setCellStyle(bodyStyle);
        cell.setCellValue("共同科目");
        
        cell = row2.createCell(14);
        cell.setCellStyle(bodyStyle);
        cell.setCellValue("走班学科");
        
        cell = row2.createCell(15);
        cell.setCellStyle(bodyStyle);
        cell.setCellValue("选科组合");
        
        cell = row2.createCell(16);
        cell.setCellStyle(bodyStyle);
        cell.setCellValue("选科人数");
        
        cell = row2.createCell(17);
        cell.setCellStyle(bodyStyle);
        cell.setCellValue("走班人数");
        
        //合格考走班学科人数展示
        cell = row2.createCell(18);
        cell.setCellStyle(bodyStyle);
        cell.setCellValue("化");
        
        cell = row2.createCell(19);
        cell.setCellStyle(bodyStyle);
        cell.setCellValue("生");
        
        cell = row2.createCell(20);
        cell.setCellStyle(bodyStyle);
        cell.setCellValue("政");
        
        cell = row2.createCell(21);
        cell.setCellStyle(bodyStyle);
        cell.setCellValue("地");
//        
        int index = 2;
        for(Map djMap : dataList){
        	
        	Map<String,Object> zbxkMap = (Map) djMap.get("zbxk");//等级考走班学科
        	
            
        	int rowNum = -1;
        	for (Map.Entry entry : zbxkMap.entrySet()) {
        		rowNum = rowNum + 1;
        	}
        	rowNum = rowNum + index;
        	
        	
        	
            //走班科目
            for (Map.Entry entry : zbxkMap.entrySet()) {//遍历等级考选科组合
            	
            	//走班学科
            	HSSFRow zbxkRow = sheet.createRow(index);
            	
            	//班级名称
            	sheet.addMergedRegion(new Region(index,(short)0,rowNum,(short)0));
                HSSFCell cell1 = zbxkRow.createCell(0);
                cell1.setCellValue(djMap.get("className").toString());
                cell1.setCellStyle(bodyStyle);
                
                //人数
                sheet.addMergedRegion(new Region(index,(short)1, rowNum,(short)1));
                cell1 = zbxkRow.createCell(1);
                cell1.setCellValue(djMap.get("stuNum").toString());
                cell1.setCellStyle(bodyStyle);
                
                //共同科目
                sheet.addMergedRegion(new Region(index,(short)2,rowNum,(short)2));
                cell1 = zbxkRow.createCell(2);
                cell1.setCellValue(djMap.get("gtName").toString());
                cell1.setCellStyle(bodyStyle);
                
                //=========================== 合格考信息 ==================
                
                //班级名称
                sheet.addMergedRegion(new Region(index, (short)11, rowNum, (short)11));
                cell1 = zbxkRow.createCell(11);
                cell1.setCellValue(djMap.get("className").toString());
                cell1.setCellStyle(bodyStyle);
                
                //人数
                sheet.addMergedRegion(new Region(index,(short)12, rowNum,(short)12));
                cell1 = zbxkRow.createCell(12);
                cell1.setCellValue(djMap.get("stuNum").toString());
                cell1.setCellStyle(bodyStyle);
                
                //共同科目
                sheet.addMergedRegion(new Region(index,(short)13, rowNum,(short)13));
                cell1 = zbxkRow.createCell(13);
                cell1.setCellValue(djMap.get("hggtName").toString());
                cell1.setCellStyle(bodyStyle);
            	
            	//走班科目
            	cell1 = zbxkRow.createCell(3);
                cell1.setCellValue(entry.getValue().toString());
                cell1.setCellStyle(bodyStyle);
                
                //选科组合
                cell1 = zbxkRow.createCell(4);
                cell1.setCellValue(entry.getKey().toString());
                cell1.setCellStyle(bodyStyle); 
                
                //选课人数
                Map<String,Object> combinMapxr = (Map) djMap.get("combinMap");//学科组合
                cell1 = zbxkRow.createCell(5);
                cell1.setCellValue(combinMapxr.get(entry.getKey().toString()).toString());
                cell1.setCellStyle(bodyStyle);
                
                //走班人数
                cell1 = zbxkRow.createCell(6);
                cell1.setCellValue(combinMapxr.get(entry.getKey().toString()).toString());
                cell1.setCellStyle(bodyStyle);
                
                String djzbName = entry.getValue().toString();//走班学科
                if(!djzbName.equals("")){
                	String[] ss = djzbName.split(" ");
                	
            		//等级考化学
                    cell1 = zbxkRow.createCell(7);
                    for(String sNm : ss){
	                    if(sNm.equals("化")){
	                    	cell1.setCellValue(combinMapxr.get(entry.getKey().toString()).toString());
	                    	break;
	                    }else{
	                    	cell1.setCellValue("");
	                    }
                    }
                    cell1.setCellStyle(bodyStyle);
                  
                    //等级考生物
                    cell1 = zbxkRow.createCell(8);
                    for(String sNm : ss){
	                    if(sNm.equals("生")){
	                    	cell1.setCellValue(combinMapxr.get(entry.getKey().toString()).toString());
	                    	break;
	                    }else{
	                    	cell1.setCellValue("");
	                    }
                    }
                    cell1.setCellStyle(bodyStyle);
                  
                    //等级考政治
                    cell1 = zbxkRow.createCell(9);
                    for(String sNm : ss){
	                    if(sNm.equals("政")){
	                    	cell1.setCellValue(combinMapxr.get(entry.getKey().toString()).toString());
	                    	break;
	                    }else{
	                    	cell1.setCellValue("");
	                    }
                    }
                  	cell1.setCellStyle(bodyStyle);
                  	
                  	//等级考地理
                  	cell1 = zbxkRow.createCell(10);
                  	for(String sNm : ss){
	                  	if(sNm.equals("地")){
	                    	cell1.setCellValue(combinMapxr.get(entry.getKey().toString()).toString());
	                    	break;
	                    }else{
	                    	cell1.setCellValue("");
	                    }
                  	}
                  	cell1.setCellStyle(bodyStyle);
                }else{
                	//等级考化学
                    cell1 = zbxkRow.createCell(7);
                    cell1.setCellValue("");
                    cell1.setCellStyle(bodyStyle);
                  
                    //等级考生物
                    cell1 = zbxkRow.createCell(8);
                    cell1.setCellValue("");
                    cell1.setCellStyle(bodyStyle);
                  
                    //等级考政治
                    cell1 = zbxkRow.createCell(9);
                    cell1.setCellValue("");
                  	cell1.setCellStyle(bodyStyle);
                  	
                  	//等级考地理
                  	cell1 = zbxkRow.createCell(10);
                  	cell1.setCellValue("");
                  	cell1.setCellStyle(bodyStyle);
                }

                //合格考信息
                Map<String,Object> unionMap = (Map) djMap.get("unionMap");//学科组合
                Map<String,Object> hgzbMap = (Map<String, Object>) unionMap.get(entry.getKey().toString());
                //走班学科
            	cell1 = zbxkRow.createCell(14);
                cell1.setCellValue(hgzbMap.get("zbkmName").toString());
                cell1.setCellStyle(bodyStyle);
                
                //选科组合
                cell1 = zbxkRow.createCell(15);
                cell1.setCellValue(hgzbMap.get("hgName").toString());
                cell1.setCellStyle(bodyStyle); 
                
                //选课人数
                cell1 = zbxkRow.createCell(16);
                cell1.setCellValue(combinMapxr.get(entry.getKey().toString()).toString());
                cell1.setCellStyle(bodyStyle);
                
                //走班人数
                cell1 = zbxkRow.createCell(17);
                cell1.setCellValue(combinMapxr.get(entry.getKey().toString()).toString());
                cell1.setCellStyle(bodyStyle);
              
                
                String hgzbName = hgzbMap.get("zbkmName").toString();//走班学科
                if(!hgzbName.equals("")){
                	String[] ss = hgzbName.split(" ");
                	
            		//合格考化学
                  	cell1 = zbxkRow.createCell(18);
                  	for(String sNm : ss){
                  		if(sNm.equals("化")){
                  			cell1.setCellValue(combinMapxr.get(entry.getKey().toString()).toString());
                  			break;
                  		}else{
                  			cell1.setCellValue("");
                  		}
                  	}
                  	cell1.setCellStyle(bodyStyle);
                  
                  	//合格考生物
                  	cell1 = zbxkRow.createCell(19);
                  	for(String sNm : ss){
                  		if(sNm.equals("生")){
                  			cell1.setCellValue(combinMapxr.get(entry.getKey().toString()).toString());
                  			break;
                  		}else{
                  			cell1.setCellValue("");
                  		}
                  	}
                  	cell1.setCellStyle(bodyStyle);
                  
                  	//合格考政治
                  	cell1 = zbxkRow.createCell(20);
                  	for(String sNm : ss){
                  		if(sNm.equals("政")){
                  			cell1.setCellValue(combinMapxr.get(entry.getKey().toString()).toString());
                  			break;
                  		}else{
                  			cell1.setCellValue("");
                  		}
                  	}
                  	cell1.setCellStyle(bodyStyle);
                  
                  	//合格考地理
                  	cell1 = zbxkRow.createCell(21);
                  	for(String sNm : ss){
                  		if(sNm.equals("地")){
                  			cell1.setCellValue(combinMapxr.get(entry.getKey().toString()).toString());
                  			break;
                  		}else{
                  			cell1.setCellValue("");
                  		}
                  	}
                  	cell1.setCellStyle(bodyStyle);
                }else{
                	//合格考化学
                  	cell1 = zbxkRow.createCell(18);
                  	cell1.setCellValue("");
                  	cell1.setCellStyle(bodyStyle);
                  
                  	//合格考生物
                  	cell1 = zbxkRow.createCell(19);
                  	cell1.setCellValue("");
                  	cell1.setCellStyle(bodyStyle);
                  
                  	//合格考政治
                  	cell1 = zbxkRow.createCell(20);
                  	cell1.setCellValue("");
                  	cell1.setCellStyle(bodyStyle);
                  
                  	//合格考地理
                  	cell1 = zbxkRow.createCell(21);
                  	cell1.setCellValue("");
                  	cell1.setCellStyle(bodyStyle);
                }
                
                index++;
            }

            
        }

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            wb.write(os);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] content = os.toByteArray();
        OutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            response.setContentType("application/force-download");
            response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("导出分班结果.xls", "UTF-8"));
            response.setContentLength(content.length);     
            outputStream.write(content);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    //学生选科重分行政班 教学班
    public Map<String, Object> getClassSubsInfoMap(ObjectId ciId, ObjectId schoolId, ObjectId gradeId){
        Map<String, Object> reMap = new HashMap<String, Object>();
        //基础学科
        List<ObjectId> baseSubIds = new ArrayList<ObjectId>();//基础学科ids
    	//走班学科
    	List<ObjectId> zbSubIds = new ArrayList<ObjectId>();//走班学科ids
    	List<N33_KSDTO> ksList = subjectService.getIsolateSubjectList(schoolId, gradeId, ciId);
        //学科map
    	Map<String, N33_KSDTO> subMap = new HashMap<String, N33_KSDTO>();
    	for(N33_KSDTO kSDTO : ksList){
            subMap.put(kSDTO.getSubid(), kSDTO);
            if(kSDTO.getIsZouBan()==1){
                zbSubIds.add(new ObjectId(kSDTO.getSubid()));
            }else{
                baseSubIds.add(new ObjectId(kSDTO.getSubid()));
            }
    	}

    	/*AfreshClassSetEntry afreshClassSetEntry = afreshClassSetDao.getEntryBySchoolId(ciId, schoolId, gradeId);
        List<AfreshClassEntry> entries = new ArrayList<AfreshClassEntry>();
        if(afreshClassSetEntry!=null){
            entries = afreshClassDao.findByGradeIdId(afreshClassSetEntry.getID());
        }*/

        //查询最近的一条分班结果
        List<ClassEntry> entries = n33ClassDao.findClassEntryBySchoolIdAndGradeId(ciId, schoolId, gradeId);

        List<ObjectId> stuIds = new ArrayList<ObjectId>();
        for(ClassEntry entry : entries) {
            for(ObjectId stuId:entry.getStudentList()) {
                if (!stuIds.contains(stuId)) {
                    stuIds.add(stuId);
                }
            }
        }

        Grade grade = gradeDao.findIsolateGradeByGradeId(ciId, schoolId, gradeId);
        Map<ObjectId, StudentEntry> stuMap = N33_StudentDao.getStuMap(stuIds, ciId);


        //只有一门等级行政学科的学科ids
        List<ObjectId> ooDjXzSubIds = new ArrayList<ObjectId>();
        //不能作为等级行政学科的学科ids
        List<ObjectId> noDjXzSubIds = new ArrayList<ObjectId>();

        //只有一门合格行政学科的学科ids
        List<ObjectId> ooHgXzSubIds = new ArrayList<ObjectId>();
        //不能作为合格行政学科的学科ids
        List<ObjectId> noHgXzSubIds = new ArrayList<ObjectId>();

        Map<ObjectId, Map<ObjectId, List<ObjectId>>> clsDjSubStuIdsMMap = new HashMap<ObjectId, Map<ObjectId, List<ObjectId>>>();
        Map<ObjectId, Map<ObjectId, List<ObjectId>>> clsHgSubStuIdsMMap = new HashMap<ObjectId, Map<ObjectId, List<ObjectId>>>();

        List<Map> classList = new ArrayList<Map>();
        for(ClassEntry klass : entries){//以班为单位遍历数据
        	Map classMap = new HashMap();//等级考封装数据
        	classMap.put("classId", klass.getClassId());

            String className = grade.getName() + "(" + klass.getXh() + ")";
            classMap.put("className", className);
            classMap.put("xh", klass.getXh());
            int stuNum = klass.getStudentList().size();
        	classMap.put("stuNum", stuNum);//学生人数
            classMap.put("stuIds", klass.getStudentList());

            //等级学科ids
            List<ObjectId> djSubIds = new ArrayList<ObjectId>();
            //合格学科ids
            List<ObjectId> hgSubIds = new ArrayList<ObjectId>();

            Map<ObjectId, List<ObjectId>> djSubStuIdsMap = new HashMap<ObjectId, List<ObjectId>>();
            Map<ObjectId, List<ObjectId>> hgSubStuIdsMap = new HashMap<ObjectId, List<ObjectId>>();

            for(ObjectId stuId:klass.getStudentList()) {
                StudentEntry stuEntry = stuMap.get(stuId);
                if(null!=stuEntry){
                    List<ObjectId> djStuSubIds = new ArrayList<ObjectId>();
                    List<ObjectId> hgStuSubIds = new ArrayList<ObjectId>();
                    ObjectId subId1 = stuEntry.getSubjectId1();
                    ObjectId subId2 = stuEntry.getSubjectId2();
                    ObjectId subId3 = stuEntry.getSubjectId3();
                    djStuSubIds.add(subId1);
                    djStuSubIds.add(subId2);
                    djStuSubIds.add(subId3);
                    for(ObjectId subId:zbSubIds){
                        if(!djStuSubIds.contains(subId)){
                            hgStuSubIds.add(subId);
                            if(!hgSubIds.contains(subId)){
                                hgSubIds.add(subId);
                            }
                        }else{
                            if(!djSubIds.contains(subId)){
                                djSubIds.add(subId);
                            }
                        }
                    }
                    for(ObjectId subId:djStuSubIds){
                        List<ObjectId> subStuIds = djSubStuIdsMap.get(subId)==null?new ArrayList<ObjectId>():djSubStuIdsMap.get(subId);
                        if(!subStuIds.contains(stuId)){
                            subStuIds.add(stuId);
                            djSubStuIdsMap.put(subId, subStuIds);
                        }
                    }
                    for(ObjectId subId:hgStuSubIds){
                        List<ObjectId> subStuIds = hgSubStuIdsMap.get(subId)==null?new ArrayList<ObjectId>():hgSubStuIdsMap.get(subId);
                        if(!subStuIds.contains(stuId)){
                            subStuIds.add(stuId);
                            hgSubStuIdsMap.put(subId, subStuIds);
                        }
                    }
                }
            }

            //classMap.put("djSubStuIdsMap", djSubStuIdsMap);
            clsDjSubStuIdsMMap.put(klass.getClassId(), djSubStuIdsMap);

            //classMap.put("hgSubStuIdsMap", hgSubStuIdsMap);
            clsHgSubStuIdsMMap.put(klass.getClassId(), hgSubStuIdsMap);

            //等级行政学科ids
            List<ObjectId> djXzSubIds = new ArrayList<ObjectId>();
            //等级走班学科ids
            List<ObjectId> djZbSubIds = new ArrayList<ObjectId>();
            sortedXzAndZbSubIds(djSubIds, djXzSubIds, djZbSubIds, djSubStuIdsMap, stuNum);
            sortedOnlyOneXzSubIds(ooDjXzSubIds, djXzSubIds, noDjXzSubIds, djZbSubIds);

            classMap.put("djXzSubIds", djXzSubIds);
            classMap.put("djZbSubIds", djZbSubIds);
            if(djZbSubIds.size()==0){
                classMap.put("compAdminClass", 1);
            }else{
                classMap.put("compAdminClass", 0);
            }

            //合格行政学科ids
            List<ObjectId> hgXzSubIds = new ArrayList<ObjectId>();
            //合格走班学科ids
            List<ObjectId> hgZbSubIds = new ArrayList<ObjectId>();
            sortedXzAndZbSubIds(hgSubIds, hgXzSubIds, hgZbSubIds, hgSubStuIdsMap, stuNum);
            sortedOnlyOneXzSubIds(ooHgXzSubIds, hgXzSubIds, noHgXzSubIds, hgZbSubIds);
            classMap.put("hgXzSubIds", hgXzSubIds);
            classMap.put("hgZbSubIds", hgZbSubIds);
            classList.add(classMap);
        }

        for(Map cls:classList){
            int compAdminClass = cls.get("compAdminClass") == null ? 0 : (Integer) cls.get("compAdminClass");
            if (compAdminClass == 0) {
                List<ObjectId> djXzSubIds = cls.get("djXzSubIds") == null ? new ArrayList<ObjectId>() : (List<ObjectId>) cls.get("djXzSubIds");
                List<ObjectId> djZbSubIds = cls.get("djZbSubIds") == null ? new ArrayList<ObjectId>() : (List<ObjectId>) cls.get("djZbSubIds");

                List<ObjectId> hgXzSubIds = cls.get("hgXzSubIds") == null ? new ArrayList<ObjectId>() : (List<ObjectId>) cls.get("hgXzSubIds");
                List<ObjectId> hgZbSubIds = cls.get("hgZbSubIds") == null ? new ArrayList<ObjectId>() : (List<ObjectId>) cls.get("hgZbSubIds");

                if(listRetainAll(djXzSubIds, ooDjXzSubIds)||listRetainAll(hgXzSubIds, ooHgXzSubIds)) {
                    sortedXzSubIdToZbSubIds(djXzSubIds, djZbSubIds, ooDjXzSubIds, noDjXzSubIds);
                    cls.put("djXzSubIds", djXzSubIds);
                    cls.put("djZbSubIds", djZbSubIds);

                    sortedXzSubIdToZbSubIds(hgXzSubIds, hgZbSubIds, ooHgXzSubIds, noHgXzSubIds);
                    cls.put("hgXzSubIds", hgXzSubIds);
                    cls.put("hgZbSubIds", hgZbSubIds);
                }
            }
        }

        for(Map cls:classList) {
            int compAdminClass = cls.get("compAdminClass") == null ? 0 : (Integer) cls.get("compAdminClass");
            if (compAdminClass == 0) {
                List<ObjectId> djXzSubIds = cls.get("djXzSubIds") == null ? new ArrayList<ObjectId>() : (List<ObjectId>) cls.get("djXzSubIds");
                List<ObjectId> djZbSubIds = cls.get("djZbSubIds") == null ? new ArrayList<ObjectId>() : (List<ObjectId>) cls.get("djZbSubIds");

                List<ObjectId> hgXzSubIds = cls.get("hgXzSubIds") == null ? new ArrayList<ObjectId>() : (List<ObjectId>) cls.get("hgXzSubIds");
                List<ObjectId> hgZbSubIds = cls.get("hgZbSubIds") == null ? new ArrayList<ObjectId>() : (List<ObjectId>) cls.get("hgZbSubIds");

                if(listRetainAll(djXzSubIds, noDjXzSubIds)||listRetainAll(hgXzSubIds, noHgXzSubIds)) {
                    sortedXzSubIdToZbSubIds(djXzSubIds, djZbSubIds, noDjXzSubIds);
                    cls.put("djXzSubIds", djXzSubIds);
                    cls.put("djZbSubIds", djZbSubIds);

                    sortedXzSubIdToZbSubIds(hgXzSubIds, hgZbSubIds, noHgXzSubIds);
                    cls.put("hgXzSubIds", hgXzSubIds);
                    cls.put("hgZbSubIds", hgZbSubIds);
                }
            }
        }

        reMap.put("baseSubIds", baseSubIds);
        reMap.put("zbSubIds", zbSubIds);
        reMap.put("subMap", subMap);
        reMap.put("classList", classList);
        reMap.put("clsDjSubStuIdsMMap", clsDjSubStuIdsMMap);
        reMap.put("clsHgSubStuIdsMMap", clsHgSubStuIdsMMap);
        return reMap;
    }

    /**
     * 判断俩个集合是否有交集
     * @param list1
     * @param list2
     * @return
     */
    public boolean listRetainAll(List<ObjectId> list1, List<ObjectId> list2){
        List<ObjectId> one = new ArrayList<ObjectId>(list1);
        List<ObjectId> two = new ArrayList<ObjectId>(list2);
        one.retainAll(two);
        if(one.size()>0){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 重行政 教学班
     * @param xzSubIds
     * @param zbSubIds
     * @param ooXzSubIds
     * @param noXzSubIds
     */
    public void sortedXzSubIdToZbSubIds(
            List<ObjectId> xzSubIds,
            List<ObjectId> zbSubIds,
            List<ObjectId> ooXzSubIds,
            List<ObjectId> noXzSubIds
    ) {
        if(listRetainAll(xzSubIds, ooXzSubIds)){
            for(ObjectId subId:xzSubIds){
                if(!ooXzSubIds.contains(subId)){
                    zbSubIds.add(subId);
                    if(!noXzSubIds.contains(subId)) {
                        noXzSubIds.add(subId);
                    }
                }
            }
            for(ObjectId subId:zbSubIds){
                if(xzSubIds.contains(subId)){
                    xzSubIds.remove(subId);
                }
            }
        }
    }

    /**
     * 重行政 教学班
     * @param xzSubIds
     * @param zbSubIds
     * @param noXzSubIds
     */
    public void sortedXzSubIdToZbSubIds(
            List<ObjectId> xzSubIds,
            List<ObjectId> zbSubIds,
            List<ObjectId> noXzSubIds
    ) {
        if(listRetainAll(xzSubIds, noXzSubIds)){
            for(ObjectId subId:xzSubIds){
                if(noXzSubIds.contains(subId)){
                    zbSubIds.add(subId);
                }
            }
            for(ObjectId subId:zbSubIds){
                if(xzSubIds.contains(subId)){
                    xzSubIds.remove(subId);
                }
            }
        }
    }

    /**
     * 重行政 教学班
     * @param ooXzSubIds
     * @param xzSubIds
     * @param noXzSubIds
     * @param zbSubIds
     */
    public void sortedOnlyOneXzSubIds(
            List<ObjectId> ooXzSubIds,
            List<ObjectId> xzSubIds,
            List<ObjectId> noXzSubIds,
            List<ObjectId> zbSubIds
    ) {
        if(xzSubIds.size()==1){
            for(ObjectId subId:xzSubIds){
                if(!ooXzSubIds.contains(subId)){
                    ooXzSubIds.add(subId);
                }
            }
            for(ObjectId subId:zbSubIds){
                if(!noXzSubIds.contains(subId)){
                    noXzSubIds.add(subId);
                }
            }
        }
    }

    /**
     * 重行政 教学班
     * @param subIds
     * @param xzSubIds
     * @param zbSubIds
     * @param subStuIdsMap
     * @param stuNum
     */
    public void sortedXzAndZbSubIds(
            List<ObjectId> subIds,
            List<ObjectId> xzSubIds,
            List<ObjectId> zbSubIds,
            Map<ObjectId, List<ObjectId>> subStuIdsMap,
            int stuNum
    ) {
        for(ObjectId subId:subIds){
            List<ObjectId> subStuIds = subStuIdsMap.get(subId)==null?new ArrayList<ObjectId>():subStuIdsMap.get(subId);
            int subStuCount = subStuIds.size();
            if(stuNum==subStuCount){
                xzSubIds.add(subId);
            }else{
                zbSubIds.add(subId);
            }
        }
    }
    
    // Map的value值降序排序
    public  <K, V extends Comparable<? super V>> Map<K, V> sortDescend(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new ArrayList<Map.Entry<K, V>>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
            @Override
            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
                int compare = (o1.getValue()).compareTo(o2.getValue());
                return -compare;
            }
        });
 
        Map<K, V> returnMap = new LinkedHashMap<K, V>();
        for (Map.Entry<K, V> entry : list) {
            returnMap.put(entry.getKey(), entry.getValue());
        }
        return returnMap;
    }

    public void editAfreshType(AfreshTypeDTO dto) {
        AfreshTypeEntry entry = dto.buildEntry();
        AfreshTypeEntry oldEntry = afreshTypeDao.getAfreshTypeEntry(entry.getSchoolId(), entry.getGradeId(), entry.getCiId(), entry.getAfreshType());
        afreshTypeDao.updAfreshTypeUseIsNotUse(entry.getSchoolId(), entry.getGradeId(), entry.getCiId());
        if(null!=oldEntry){
            entry.setID(oldEntry.getID());
            afreshTypeDao.updAfreshTypeEntry(entry);
        }else{
            afreshTypeDao.addAfreshTypeEntry(entry);
        }
    }

    public List<AfreshTypeDTO> getAfreshTypeList(ObjectId schoolId, ObjectId gradeId, ObjectId ciId) {
        List<AfreshTypeDTO> list = new ArrayList<AfreshTypeDTO>();
        List<AfreshTypeEntry> entries = afreshTypeDao.getAfreshTypeEntries(schoolId, gradeId, ciId);
        for(AfreshTypeEntry entry:entries){
            AfreshTypeDTO dto = new AfreshTypeDTO(entry);
            list.add(dto);
        }
        return list;
    }

    public void editAfreshSubRuleList(List<AfreshSubRuleDTO> dtos, String schoolId) {
        for(AfreshSubRuleDTO dto:dtos){
            dto.setSchoolId(schoolId);
            AfreshSubRuleEntry entry = dto.buildEntry();
            AfreshSubRuleEntry oldEntry = afreshSubRuleDao.getAfreshSubRuleEntry(entry.getSchoolId(), entry.getGradeId(), entry.getCiId(), entry.getSubId(), entry.getAfreshType());
            if(null!=oldEntry){
                entry.setID(oldEntry.getID());
                afreshSubRuleDao.updEntry(entry);
            }else{
                afreshSubRuleDao.addEntry(entry);
            }
        }
    }

    public List<AfreshSubRuleDTO> getDkyxSubList(ObjectId schoolId, ObjectId gradeId, ObjectId ciId, int afreshType) {
        List<AfreshSubRuleDTO> reList = new ArrayList<AfreshSubRuleDTO>();
        List<N33_KSDTO> ksList = subjectService.getIsolateSubjectList(schoolId, gradeId, ciId);
        List<AfreshSubRuleDTO> subList = new ArrayList<AfreshSubRuleDTO>();
        for(N33_KSDTO ksDto : ksList){
            AfreshSubRuleDTO asrDto = new AfreshSubRuleDTO();
            if("物理".equals(ksDto.getSnm())){
                asrDto.setSubId(ksDto.getSubid());
                asrDto.setSubName(ksDto.getSnm());
                subList.add(0, asrDto);
            }
            if("历史".equals(ksDto.getSnm())){
                asrDto.setSubId(ksDto.getSubid());
                asrDto.setSubName(ksDto.getSnm());
                subList.add(asrDto);
            }
        }
        Map<ObjectId, AfreshSubRuleEntry> asrMap = afreshSubRuleDao.getAfreshSubRuleMap(schoolId, gradeId, ciId, afreshType);
        for(AfreshSubRuleDTO sub:subList){
            AfreshSubRuleEntry entry = asrMap.get(new ObjectId(sub.getSubId()));
            if(null!=entry){
                String subName = sub.getSubName();
                sub = new AfreshSubRuleDTO(entry);
                sub.setSubName(subName);
            }
            reList.add(sub);
        }
        return reList;
    }
}
