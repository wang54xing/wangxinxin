package com.fulaan.new33.service;

import com.db.new33.isolate.N33_StudentDao;
import com.db.new33.paike.N33_JXBDao;
import com.hankcs.hanlp.dependency.nnparser.util.math;
import com.pojo.new33.isolate.StudentEntry;
import com.pojo.new33.paike.N33_JXBEntry;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by albin on 2018/4/9.
 */
public class FenBanTestService {

    private static N33_StudentDao studentDao = new N33_StudentDao();
    private static N33_JXBDao jxbDao = new N33_JXBDao();

    private static ObjectId gradeId = new ObjectId("5805835471f0563b1d9a8c0b");
    private static ObjectId sid = new ObjectId("5805835471f0563b1d9a8c07");
    private static ObjectId xqid = new ObjectId("5afe6af95563fa2068267fe8");
    private static List<ObjectId> subjectId = new ArrayList<ObjectId>();
    private static List<String> subjectName = new ArrayList<String>();

//    /***
//     * 随机分配选课组合
//     * @param args
//     */
//    public static void main(String[] args) {
//        initSubject();
//        List<StudentEntry> studentList = studentDao.getStudentByXqidAndGradeId(gradeId, xqid);
//        Random random = new Random();
//        for (StudentEntry student : studentList) {
//            Integer size = 6;
//            String subjectNames = "";
//            for (int i = 1; i <= 3; i++) {
//                //随机数
//                int rand = random.nextInt(size);
//                subjectNames += subjectName.get(rand);
//                if (i == 1) {
//                    student.setSubjectId1(subjectId.get(rand));
//                }
//                if (i == 2) {
//                    student.setSubjectId2(subjectId.get(rand));
//                }
//                if (i == 3) {
//                    student.setSubjectId3(subjectId.get(rand));
//                }
//                subjectId.remove(subjectId.get(rand));
//                subjectName.remove(subjectName.get(rand));
//                size--;
//            }
//            student.setCombiname(subjectNames);
//            studentDao.updateStudentEntry(student);
//            subjectId = new ArrayList<ObjectId>();
//            subjectName = new ArrayList<String>();
//            initSubject();
//        }
//        System.out.println("执行完成");
//    }

//    /**
//     * 测试分班
//     *
//     * @param args
//     */
//    public static void main(String[] args) {
//        //年级学生
//        List<StudentEntry> studentList = studentDao.getStudentByXqidAndGradeId(gradeId, xqid);
//        //年级教学班
//        List<N33_JXBEntry> jxbEntryList = jxbDao.getJXBsBySubIdS(xqid, sid, gradeId);
//
//    }

//    public static void initSubject() {
//        subjectId.add(new ObjectId("5805835471f0563b1d9a8c47"));
//        subjectName.add("政");
//        subjectId.add(new ObjectId("5805835571f0563b1d9a8c58"));
//        subjectName.add("物");
//        subjectId.add(new ObjectId("5805835571f0563b1d9a8c70"));
//        subjectName.add("化");
//        subjectId.add(new ObjectId("5805835571f0563b1d9a8c82"));
//        subjectName.add("历");
//        subjectId.add(new ObjectId("5805835571f0563b1d9a8c9f"));
//        subjectName.add("生");
//        subjectId.add(new ObjectId("5805835571f0563b1d9a8ccb"));
//        subjectName.add("地");
//    }

    //    public static void main(String[] args) {
//        N33_AutoFenBanService fenBanService = new N33_AutoFenBanService();
//        fenBanService.autoFenBanPingTag(new ObjectId("5ad40ca73328be801d6ed2f5"), new ObjectId("5b1b74d2a0e65035ac6c9ab8"), new ObjectId("5ad42678384ba0aa56c6eb0c"));
//    }
    //随机
    public static void main(String[] args) {

    }
}
