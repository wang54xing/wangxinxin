package com.fulaan.utils;

import com.db.new33.isolate.ClassDao;
import com.db.new33.isolate.GradeDao;
import com.db.new33.isolate.N33_StudentDao;
import com.db.user.UserDao;
import com.mongodb.BasicDBObject;
import com.pojo.new33.isolate.ClassEntry;
import com.pojo.new33.isolate.Grade;
import com.pojo.new33.isolate.StudentEntry;
import com.pojo.user.UserEntry;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * Created by wang_xinxin on 2015/11/9.
 */
public class Test {
    private static String[] is = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13"};
    private static int total;
    private static int m = 3;

    public static void main(String[] args) {
        ClassDao classDao = new ClassDao();
        N33_StudentDao studentDao = new N33_StudentDao();
        GradeDao grade = new GradeDao();
        List<Grade> gradeList = grade.findGradeListBySchoolId(new ObjectId("5bbc235e46d697312ff1ece7"), new ObjectId("5ba354db46d6977cd9ce306f"));
        for (Grade gradeImp : gradeList) {
            List<ClassEntry> clsList = classDao.findByGradeIdId(new ObjectId("5ba354db46d6977cd9ce306f"), gradeImp.getGradeId(), new ObjectId("5bbc235e46d697312ff1ece7"));
            for (ClassEntry en : clsList) {
                List<ObjectId> ids=new ArrayList<ObjectId>();
                List<StudentEntry> stu=  studentDao.getStudentByXqidAndClassId(en.getClassId(), new ObjectId("5bbc235e46d697312ff1ece7"));
                for (StudentEntry e:stu){
                    ids.add(e.getUserId());
                }
                en.setUserList(ids);
                classDao.updateIsolateClassEntry(en);
            }
        }
//        UserDao userDao = new UserDao();
//        List<UserEntry> entries = userDao.getUserEntrysByLogn(new BasicDBObject("_id", -1).append("logn", -1).append("nm", -1).append("nnm", -1));
//        for (UserEntry entry:entries){
//            entry.setUserName(entry.getLoginName());
//            userDao.update(entry.getID(),entry);
//        }

//        List<Integer> iL = new ArrayList<Integer>();
//        List<String> arglist = new ArrayList<String>();
//        List<String> arglist2 = new ArrayList<String>();
//        List<Integer> integer = new ArrayList<Integer>();
//        new Test().plzh("", iL,  m,arglist);
//        for (String s : arglist) {
//            integer = new ArrayList<Integer>();
//            String[] cls = s.split(",");
//            for (String s2 : cls) {
//                integer.add(Integer.valueOf(s2));
//                Collections.sort(integer);
//            }
//            String str = "";
//            for (Integer s4 : integer) {
//                str += s4.toString() + ",";
//            }
//            arglist2.add(str);
//        }
//        HashSet<String> hs = new HashSet<String>(arglist2);
//        for (String s7:hs) {
//            System.out.println(s7);
//        }
//        System.out.println("total : " + hs.size());
    }

    private void plzh(String s, List<Integer> iL, int m, List<String> arglist) {
        if (m == 0) {
            arglist.add(s);
            total++;
            return;
        }
        List<Integer> iL2;
        for (int i = 0; i < is.length; i++) {
            iL2 = new ArrayList<Integer>();
            iL2.addAll(iL);
            if (!iL.contains(i)) {
                String str = s + is[i] + ",";
                iL2.add(i);
                plzh(str, iL2, m - 1, arglist);
            }
        }
    }


}
