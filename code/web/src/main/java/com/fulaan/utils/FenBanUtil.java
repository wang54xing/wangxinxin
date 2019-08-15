package com.fulaan.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
//import java.util.stream.Stream;

/**
 * Created by wang_xinxin on 2017/3/20.
 */
public class FenBanUtil {
    //用户选课班
    public static Map<String,List<Integer>> userChooseMap = new HashMap<String, List<Integer>>();
    //用户已选的学科
    public static Map<String,List<String>> userChooseSubjectMap = new HashMap<String, List<String>>();
    //每个学科班级
    public static Map<String,Map<Integer,String>> subjectClassMap = new HashMap<String, Map<Integer,String>>();
    //班级人员
    public static Map<String,List<String>> classUserMap = new HashMap<String, List<String>>();
    //班级分数
    public static Map<String,Map<String,Double>> classUserScoreMap = new HashMap<String, Map<String,Double>>();
    //班级分数
    public static Map<String,Double> classAvgScoreMap = new HashMap<String, Double>();
    //学科平均分
    public static Map<String,Double> subjectAvgScoreMap = new HashMap<String, Double>();
    //没分好班的
    public static Map<String,List<String>> noFenBanMap = new HashMap<String, List<String>>();
    //人 学科 分数
    public static Map<String,Map<String,Double>> userSubjectNumMap = new HashMap<String, Map<String, Double>>();

    private static final int CLASS_MAX_AMOUNT = 40;//每个班最大人数

    private static final double AVG_ERROR = 0.5;//误差

    public static void main(String[] args) throws Exception {

        File excelFile = new File("E:\\成绩.xls");
        HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(excelFile));
        HSSFSheet sheet = wb.getSheetAt(2);
        int rowNum = sheet.getLastRowNum();
        //学科名称
        List<String> subjectNameList = new ArrayList<String>();
        //人 学科集合
        Map<String,List<String>> userSubjectMap = new HashMap<String, List<String>>();
        //学科总分
        Map<String,Double> subjectTotalMap = new HashMap<String, Double>();
        //学科人数
        Map<String,Integer> subjectCountMap = new HashMap<String, Integer>();
        //学科班级数目
        Map<String,Integer> subjectClassCountMap = new HashMap<String, Integer>();
        //3门学科所选多个学科人
        Map<String,List<String>> subjectUserMap = new HashMap<String, List<String>>();
        //3门学科所选多个学科人数
        Map<String,Integer> subjectUserCountMap = new HashMap<String, Integer>();


        for (int j=0;j<6;j++) {
            subjectNameList.add(sheet.getRow(0).getCell(j+1).getStringCellValue());
            subjectTotalMap.put(sheet.getRow(0).getCell(j+1).getStringCellValue(),0d);
            subjectCountMap.put(sheet.getRow(0).getCell(j+1).getStringCellValue(),0);
        }
        for (int i = 1; i <= rowNum; i++) {
            try {
                String zongSubject = "";
                List<String> subjectNames = new ArrayList<String>();
                Map<String,Double> scoreMap = new HashMap<String, Double>();
                String name = sheet.getRow(i).getCell(0).getStringCellValue();
                for (int z=0;z<subjectNameList.size();z++) {
                    if (sheet.getRow(i).getCell(z + 1)!=null && StringUtils.isNotEmpty(sheet.getRow(i).getCell(z + 1).toString())) {
                        subjectNames.add(subjectNameList.get(z));
                        scoreMap.put(subjectNameList.get(z), Double.valueOf(sheet.getRow(i).getCell(z + 1).toString()));
                        Double score = subjectTotalMap.get(subjectNameList.get(z));
                        subjectTotalMap.put(subjectNameList.get(z),Double.valueOf(sheet.getRow(i).getCell(z + 1).toString()) + score);
                        int cnt = subjectCountMap.get(subjectNameList.get(z))+1;
                        subjectCountMap.put(subjectNameList.get(z),cnt);
                        zongSubject = zongSubject + subjectNameList.get(z) + ",";
                    }
                }
                List<String> userList = subjectUserMap.get(zongSubject)==null?new ArrayList<String>() : subjectUserMap.get(zongSubject);
                userList.add(name);
                subjectUserMap.put(zongSubject,userList);
                userSubjectMap.put(name,subjectNames);
                userSubjectNumMap.put(name,scoreMap);
                userChooseMap.put(name,new ArrayList<Integer>());
                userChooseSubjectMap.put(name,new ArrayList<String>());
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        for (Map.Entry<String, Integer> entry : subjectCountMap.entrySet()) {
            int sc = entry.getValue()/CLASS_MAX_AMOUNT;
            if (entry.getValue()%CLASS_MAX_AMOUNT>20) {
                sc = sc+1;
            }
            Map<Integer,String> classNames = new HashMap<Integer, String>();
            for (int k=0;k<sc;k++) {
                classNames.put(k+1,entry.getKey()+(k+1));
                classUserMap.put(entry.getKey()+(k+1),new ArrayList<String>());
                classUserScoreMap.put(entry.getKey()+(k+1),new HashMap<String, Double>());
                classAvgScoreMap.put(entry.getKey()+(k+1),0d);
            }
            subjectClassMap.put(entry.getKey(),classNames);
            subjectClassCountMap.put(entry.getKey(), sc);
        }
        sortByValue(subjectClassCountMap);

        for (Map.Entry<String, Double> entry : subjectTotalMap.entrySet()) {
            BigDecimal a = new BigDecimal(entry.getValue());
            BigDecimal b = new BigDecimal(subjectCountMap.get(entry.getKey()));
            subjectAvgScoreMap.put(entry.getKey(),Double.valueOf(a.divide(b, 2, RoundingMode.HALF_UP).toString()));
        }
        for (Map.Entry<String, List<String>> entry : subjectUserMap.entrySet()) {
            subjectUserCountMap.put(entry.getKey(),entry.getValue().size());
        }
        sortByValue(subjectUserCountMap);
        List<Map.Entry<String,Integer>> list =
                new ArrayList<Map.Entry<String,Integer>>(subjectUserCountMap.entrySet());

        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> o1,
                               Map.Entry<String, Integer> o2) {
                return (o1.getValue() - o2.getValue());
            }
        });
        for (Map.Entry<String, Integer> entry : list) {
            List<String> userList = subjectUserMap.get(entry.getKey());
            Map<String,Integer> subjectCntMap = new HashMap<String, Integer>();
            String[] subjectArg = entry.getKey().split(",");
            for (String sub : subjectArg) {
                subjectCntMap.put(sub,subjectCountMap.get(sub));
            }
            List<Map.Entry<String,Integer>> list2 =
                    new ArrayList<Map.Entry<String,Integer>>(subjectCntMap.entrySet());

            Collections.sort(list2, new Comparator<Map.Entry<String, Integer>>() {
                public int compare(Map.Entry<String, Integer> o1,
                                   Map.Entry<String, Integer> o2) {
                    return (o1.getValue() - o2.getValue());
                }
            });
            for (Map.Entry<String, Integer> entry2 : list2) {
                fenBan(userList, entry2.getKey(), userSubjectNumMap, new HashMap<String, Double>(), classUserMap, classUserScoreMap, subjectClassMap);
            }
        }
        fenBan2(userSubjectMap,false);
        fenBan2(userSubjectMap,true);
        Map<String,List<String>> noFenBan = new HashMap<String, List<String>>();
        for (Map.Entry<String,List<String>> entry : userChooseSubjectMap.entrySet()) {
            List<String> subjects = entry.getValue();
            if (subjects.size()<3) {
                List<String> subs =userSubjectMap.get(entry.getKey());
                subs.removeAll(entry.getValue());
                noFenBan.put(entry.getKey(),subs);
            }
        }

        System.out.println(classUserMap);
        System.out.println(noFenBanMap);
        int count  = 0;
        for (Map.Entry<String, List<String>> entry : classUserMap.entrySet()) {
            System.out.print(entry.getKey() + ":" + entry.getValue().size());
            count += entry.getValue().size();
        }
        System.out.println(count);
        System.out.println("-------------------------------------");
        for (Map.Entry<String, List<String>> entry : noFenBanMap.entrySet()) {
            System.out.print(entry.getKey() + ":" + entry.getValue().size());
        }
        System.out.println("-------------------------------------");
        for (Map.Entry<String, Map<Integer,String>> entry : subjectClassMap.entrySet()) {
            System.out.print(entry.getKey() + ":" + entry.getValue().size());
        }

        for (Map.Entry<String,List<Integer>> entry : userChooseMap.entrySet()) {
            System.out.print(entry.getKey() + ":" + entry.getValue().size());
        }


    }

    /**
     *
     * @param userSubjectMap
     */
    private static void fenBan2(Map<String, List<String>> userSubjectMap,boolean flag) {
        Map<String,List<String>> noFenBan = new HashMap<String, List<String>>();
        for (Map.Entry<String,List<String>> entry : userChooseSubjectMap.entrySet()) {
            List<String> subjects = entry.getValue();
            if (subjects.size()<3) {
                List<String> subs =userSubjectMap.get(entry.getKey());
                subs.removeAll(entry.getValue());
                noFenBan.put(entry.getKey(),subs);
            }
        }
        if (noFenBan.size()!=0) {
            for (Map.Entry<String,List<String>> entry : noFenBan.entrySet()) {
                List<Integer> indexs = userChooseMap.get(entry.getKey());
                for (String sub : entry.getValue()) {
                    Map<Integer,String> classNames = subjectClassMap.get(sub);
                    for (Map.Entry<Integer,String> entry2 : classNames.entrySet()) {
                        List<Integer> postions = userChooseMap.get(entry.getKey());
                        List<String> chooseSub = userChooseSubjectMap.get(entry.getKey());
                        Map<String, Double> classScore = classUserScoreMap.get(entry2.getValue());
                        List<String> classUser = classUserMap.get(entry2.getValue());
                        if (!indexs.contains(entry2.getKey()) && (flag || classUserMap.get(entry2.getValue()).size()<CLASS_MAX_AMOUNT)) {
                            classScore.put(entry.getKey(), userSubjectNumMap.get(entry.getKey()).get(sub));
                            classUser.add(entry.getKey());
                            postions.add(entry2.getKey());
                            chooseSub.add(sub);
                            userChooseSubjectMap.put(entry.getKey(),chooseSub);
                            userChooseMap.put(entry.getKey(), postions);
                            classUserMap.put(entry2.getValue(), classUser);
                            classUserScoreMap.put(entry2.getValue(), classScore);
                            break;
                        }
                    }
                }

            }
        }
    }

    /**
     *
     * @param userList
     * @param subject
     * @param userSubjectNumMap
     * @param score
     * @param classUserMap
     * @param classUserScoreMap
     */
    private static void fenBan(List<String> userList, String subject, Map<String, Map<String, Double>> userSubjectNumMap, Map<String, Double> score,
                               //班级人员              班级分数                 学科对应班级
                               Map<String, List<String>> classUserMap, Map<String, Map<String, Double>> classUserScoreMap,Map<String,Map<Integer,String>> subjectClassMap) {
        for (String user : userList) {
            score.put(user,userSubjectNumMap.get(user).get(subject));
        }
//        double avg = subjectAvgScoreMap.get(subject);
        List<String> noUser = new ArrayList<String>();
        sortByValue(score);
        Map<Integer,String> classNames = subjectClassMap.get(subject);
        if (classNames.size()==1) {
            List<String> user = classUserMap.get(classNames.get(1));
            user.addAll(userList);
            classUserMap.put(classNames.get(1),user);
        } else {
            int cnt2 = classNames.size();
            int cnt3 = userList.size()/cnt2;
            int parent3 = userList.size()%cnt2;
            List<String> exsitUser = new ArrayList<String>();
            for (int i=0;i<cnt2;i++) {
                List<String> classUser = classUserMap.get(classNames.get(i+1));
                if (classUser.size()<CLASS_MAX_AMOUNT) {
                    Map<String, Double> classScore = classUserScoreMap.get(classNames.get(i + 1));
                    for (int j = 0; j < cnt3; j++) {
                        List<Integer> postions = userChooseMap.get(userList.get(i + cnt2 * j));
                        List<String> chooseSub = userChooseSubjectMap.get(userList.get(i + cnt2 * j));
                        if (!postions.contains(i + 1)) {
                            classUser.add(userList.get(i + cnt2 * j));
                            classScore.put(userList.get(i + cnt2 * j), score.get(userList.get(i + cnt2 * j)));
                            postions.add(i + 1);
                            chooseSub.add(subject);
                            userChooseSubjectMap.put(userList.get(i + cnt2 * j),chooseSub);
                            userChooseMap.put(userList.get(i + cnt2 * j), postions);
                        } else {
                            exsitUser.add(userList.get(i + cnt2 * j));
                        }
                    }
                    classUserMap.put(classNames.get(i + 1), classUser);
                    classUserScoreMap.put(classNames.get(i + 1), classScore);
                }
            }
            for (int z=0;z<parent3;z++) {
                List<Integer> postions = userChooseMap.get(userList.get(cnt2*cnt3+z));
                List<String> chooseSub = userChooseSubjectMap.get(userList.get(cnt2*cnt3+z));
                List<String> classUser = classUserMap.get(classNames.get(z+1));
                if (classUser.size()<CLASS_MAX_AMOUNT) {
                    Map<String, Double> classScore = classUserScoreMap.get(classNames.get(z + 1));
                    if (!postions.contains(z + 1) && !classUser.contains(userList.get(cnt2 * cnt3 + z))) {
                        classUser.add(userList.get(cnt2 * cnt3 + z));
                        classScore.put(userList.get(cnt2 * cnt3 + z), score.get(userList.get(cnt2 * cnt3 + z)));
                        classUserMap.put(classNames.get(z + 1), classUser);
                        classUserScoreMap.put(classNames.get(z + 1), classScore);
                        postions.add(z + 1);
                        chooseSub.add(subject);
                        userChooseSubjectMap.put(userList.get(cnt2 * cnt3 + z),chooseSub);
                        userChooseMap.put(userList.get(cnt2 * cnt3 + z), postions);
                    } else {
                        exsitUser.add(userList.get(cnt2 * cnt3 + z));
                    }
                }
            }
            for (int k=0;k<exsitUser.size();k++) {
                boolean flag = true;
                for (int i=0;i<cnt2;i++) {
                    List<Integer> postions = userChooseMap.get(exsitUser.get(k));
                    List<String> classUser = classUserMap.get(classNames.get(i+1));
                    List<String> chooseSub = userChooseSubjectMap.get(exsitUser.get(k));
                    if (classUser.size()<CLASS_MAX_AMOUNT) {
                        Map<String, Double> classScore = classUserScoreMap.get(classNames.get(i + 1));
                        if (!postions.contains(i + 1) && flag && !classUser.contains(exsitUser.get(k))) {
                            classUser.add(exsitUser.get(k));
                            classScore.put(exsitUser.get(k), score.get(exsitUser.get(k)));
                            classUserMap.put(classNames.get(i + 1), classUser);
                            classUserScoreMap.put(classNames.get(i + 1), classScore);
                            postions.add(i + 1);
                            chooseSub.add(subject);
                            userChooseSubjectMap.put(exsitUser.get(k),chooseSub);
                            userChooseMap.put(exsitUser.get(k), postions);
                            flag = false;
                        }
                    }
                }
            }
        }
        noFenBanMap.put(subject,new ArrayList(new HashSet(noUser)));
    }

    /**
     * 分学生
     * @param user1
     * @param user2
     * @param score
     * @param s1
     * @param s2
     */
    private static void fenStudent(List<String> user1, List<String> user2, Map<String, Double> score, String s1, String s2) {
        int i=0;
        Map<String,Double> sc1 = classUserScoreMap.get(s1);
        Map<String,Double> sc2 = classUserScoreMap.get(s2);
        for (Map.Entry<String, Double> entry : score.entrySet()) {
            if (i/2!=0) {
                user1.add(entry.getKey());
                sc1.put(entry.getKey(),entry.getValue());
            } else {
                user2.add(entry.getKey());
                sc2.put(entry.getKey(),entry.getValue());
            }
            i++;
        }
        Double total1 = 0d;
        Double total2 = 0d;
        for (Map.Entry<String, Double> entry : sc1.entrySet()) {
            total1 += entry.getValue();
        }
        for (Map.Entry<String, Double> entry : sc2.entrySet()) {
            total2 += entry.getValue();
        }
        classUserMap.put(s1,user1);
        classUserMap.put(s2,user2);
        classUserScoreMap.put(s1,sc1);
        classUserScoreMap.put(s2,sc2);
        BigDecimal a = new BigDecimal(total1);
        BigDecimal b = new BigDecimal(user1.size());
        classAvgScoreMap.put(s1,Double.valueOf(a.divide(b, 2, RoundingMode.HALF_UP).toString()));
        BigDecimal a2 = new BigDecimal(total2);
        BigDecimal b2 = new BigDecimal(user2.size());
        classAvgScoreMap.put(s2,Double.valueOf(a2.divide(b2, 2, RoundingMode.HALF_UP).toString()));
    }
    /**
     * map排序
     * @param map
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
            @Override
            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });
        Map<K, V> result = new LinkedHashMap<K, V>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    /**
     * 每个班的平均成绩
     * @param userList
     * @return
     */
    public static Double classAvgScore(List<String> userList,String subject,Map<String,Map<String,Double>> userSubjectNumMap) {
        Double sum = 0d;
        for (String user : userList) {
            sum += userSubjectNumMap.get(user).get(subject);
        }
        BigDecimal a = new BigDecimal(sum);
        BigDecimal b = new BigDecimal(userList.size());
        Double avg = Double.valueOf(a.divide(b, 2, RoundingMode.HALF_UP).toString());
        return avg;
    }
}
