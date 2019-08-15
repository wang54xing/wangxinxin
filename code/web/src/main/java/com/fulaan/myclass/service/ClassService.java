package com.fulaan.myclass.service;

import com.db.school.*;
import com.db.user.UserDao;
import com.db.user.UserSchoolYearExperienceDao;
import com.db.video.VideoViewRecordDao;
import com.fulaan.myclass.controller.TeacherInfoView;
import com.fulaan.myschool.controller.GradeView;
import com.fulaan.myschool.controller.TeacherSubjectView;
import com.fulaan.school.service.SchoolService;
import com.fulaan.utils.SpringContextUtil;
import com.fulaan.utils.TermUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.app.FieldValuePair;
import com.pojo.app.IdNameValuePairDTO;
import com.pojo.app.IdValuePair;
import com.pojo.app.IdValuePairDTO;
import com.pojo.app.SimpleDTO;
import com.pojo.school.*;
import com.pojo.user.*;
import com.pojo.video.VideoViewRecordEntry;
import com.sys.constants.Constant;
import com.sys.exceptions.IllegalParamException;
import com.sys.utils.AvatarUtils;

import net.sourceforge.pinyin4j.PinyinHelper;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.Collator;
import java.util.*;

/**
 * Created by hao on 2015/2/27.
 */
@Service
public class ClassService {

    private ClassDao classDao = new ClassDao();
    private UserDao userDao = new UserDao();
    private SchoolDao schoolDao = new SchoolDao();
    private TeacherClassSubjectDao teacherClassSubjectDao = new TeacherClassSubjectDao();
    private VideoViewRecordDao videoViewRecordDao = new VideoViewRecordDao();
    private UserSchoolYearExperienceDao userSchoolYearExperienceDao = new UserSchoolYearExperienceDao();


    /**
     * 根据学生ID查找所在班级，仅返回名字,ID，班级类型
     *
     * @param studentId
     * @return
     */
    public List<ClassEntry> getStudentClassList(ObjectId studentId) {
        return classDao.getClassEntryListByStudentId(studentId, new BasicDBObject("nm", 1).append("cty", 1));
    }

    /**
     * 根据ID查询详情
     *
     * @param objectId
     * @param fields
     * @return
     */
    public ClassEntry getClassEntryById(ObjectId objectId, DBObject fields) {
        return classDao.getClassEntryById(objectId, fields);
    }



    /**
     * 根据学生ID查询学生主班级
     *
     * @param studentId
     * @param fields
     * @return
     */
    public ClassEntry getClassEntryByStuId(ObjectId studentId, DBObject fields) {
        return classDao.getClassEntryByStuId(studentId, fields);
    }


    /**
     * 添加一个老师
     *
     * @param id
     * @param teacherId
     */
    public void addTeacher(ObjectId id, ObjectId teacherId) {
        classDao.addTeacher(id, teacherId);
    }


    /**
     * 添加一个学生
     *
     * @param id
     * @param studentId
     */
    public void addStudent(ObjectId id, ObjectId studentId) {
        classDao.addStudent(id, studentId);
    }


    /**
     * 更新班主任
     *
     * @param id
     * @param masterid
     */
    public void updateMaster(ObjectId id, ObjectId masterid) {
        classDao.updateMaster(id, masterid);
    }

    /**
     * 添加一个ClassEntry
     *
     * @param e
     * @return
     */
    public ObjectId addClassEntry(ClassEntry e) {
        return classDao.addClassEntry(e);
    }

    /**
     * 根据班级ID查找ClassEntry
     *
     * @param cos
     * @param fields
     * @return
     */
    public Map<ObjectId, ClassEntry> getClassEntryMap(Collection<ObjectId> cos, DBObject fields) {
        return classDao.getClassEntryMap(cos, fields);
    }

    /**
     * 得到班级
     *
     * @param teacherId
     * @param schoolId
     * @return
     * @throws IllegalParamException
     */
    public List<ClassInfoDTO> getSimpleClassInfoDTOs(ObjectId teacherId, ObjectId schoolId) throws IllegalParamException {
        List<ClassInfoDTO> classInfoDTOList = new ArrayList<ClassInfoDTO>();
        List<ClassEntry> classEntryList = classDao.getClassEntryByTeacherId(teacherId, Constant.FIELDS);

        SchoolEntry se = schoolDao.getSchoolEntry(schoolId, Constant.FIELDS);
        if (null == se)
            throw new IllegalParamException();
        Map<ObjectId, Grade> gradeMap = new HashMap<ObjectId, Grade>();
        for (Grade g : se.getGradeList()) {
            gradeMap.put(g.getGradeId(), g);
        }
        ClassInfoDTO dto = null;
        Grade g = null;
        for (ClassEntry ce : classEntryList) {
            dto = new ClassInfoDTO(ce);
            g = gradeMap.get(ce.getGradeId());
            if (null != g) {
                dto.setGradeName(g.getName());
                dto.setGradeType(g.getGradeType());
            }
            classInfoDTOList.add(dto);
        }

        return classInfoDTOList;

    }

    /**
     * 得到班级
     *
     * @param userId
     * @param schoolId
     * @return
     */
    public ClassInfoDTO getClassInfoDTOByStuId(ObjectId userId, ObjectId schoolId) throws IllegalParamException {

        ClassEntry ce = classDao.getClassEntryByStuId(userId, Constant.FIELDS);
        SchoolEntry se = schoolDao.getSchoolEntry(schoolId, Constant.FIELDS);
        if (null == se)
            throw new IllegalParamException();
        Map<ObjectId, Grade> gradeMap = new HashMap<ObjectId, Grade>();
        for (Grade g : se.getGradeList()) {
            gradeMap.put(g.getGradeId(), g);
        }
        ClassInfoDTO dto = new ClassInfoDTO(ce);
        Grade g = gradeMap.get(ce.getGradeId());
        if (null != g) {
            dto.setGradeName(g.getName());
            dto.setGradeType(g.getGradeType());
        }
        return dto;
    }

    /*
    *
    * 根据班级id 和关键字查找班级学生
    *
    * */
    public List<UserDetailInfoDTO> findStuByClassIdAndKeyword(String classId, String keyword) {
        ClassEntry classEntry = classDao.getClassEntryById(new ObjectId(classId), Constant.FIELDS);
        List<ObjectId> objectIdList = classEntry.getStudents();
        List<UserEntry> userEntryList = userDao.findUserEntriesLimitRoleAndKeyword(objectIdList, keyword, Constant.FIELDS);
        List<UserDetailInfoDTO> userInfoDTO4WBList = new ArrayList<UserDetailInfoDTO>();
        for (UserEntry userEntry : userEntryList) {
        	try
        	{
	            UserDetailInfoDTO userInfoDTO4WB = new UserDetailInfoDTO(userEntry);
	            userInfoDTO4WBList.add(userInfoDTO4WB);
        	}catch(Exception ex)
        	{
        		
        	}
        }
        return userInfoDTO4WBList;
    }
    
    
    /**
     * 得到班级学生的头像，用户名字，以及第一个字母
     * @param cids
     * @param keyword
     * @return
     */
    public List<SimpleDTO> getClassStudents( List<ObjectId> cids,String keyword)
    {
    	List<ClassEntry> classEntrys = classDao.getClassEntryByIds(cids, new BasicDBObject("stus",1));
        List<ObjectId> objectIdList = new ArrayList<ObjectId>();
         
         for(ClassEntry ce:classEntrys)
         {
        	 objectIdList.addAll(ce.getStudents());
         }
         List<UserEntry> userEntryList = userDao.findUserEntriesLimitRoleAndKeyword(objectIdList, keyword,new BasicDBObject("nm",1).append("avt", 1).append("r", 1).append("sex", 1));
         List<SimpleDTO> retList = new ArrayList<SimpleDTO>();
         for (UserEntry userEntry : userEntryList) {
         	try
         	{
         		String[] arr=	PinyinHelper.toHanyuPinyinStringArray(userEntry.getUserName().charAt(0));
                String shou = "";
                if (arr==null) {
                    shou = String.valueOf(userEntry.getUserName().charAt(0));
                } else {
                    shou = arr[0].substring(0,1);
                }
         		String firstChar =shou.toUpperCase();
         		
         		String avator=AvatarUtils.getAvatar(userEntry.getAvatar(), userEntry.getRole(), userEntry.getSex());
         		SimpleDTO dto =new SimpleDTO(userEntry.getID().toString(), userEntry.getUserName(), avator, firstChar);
         		
         		retList.add(dto);
         		
         	}catch(Exception ex)
         	{
         		
         	}
         }
         return retList;
    }


    /*
    * 班级经验值排名前五的学生
    *
    * */
    public List<UserDetailInfoDTO> findStudentTop5ByClassId(String classId) {
        List<ObjectId> objectIdList = classDao.getClassEntryById(new ObjectId(classId), Constant.FIELDS).getStudents();
        List<UserEntry> userEntryList = userDao.findUserEntriesLimitRole(objectIdList, Constant.FIELDS);
        Collections.sort(userEntryList, new Comparator<UserEntry>() {
            @Override
            public int compare(UserEntry o1, UserEntry o2) {
                return o2.getExperiencevalue() - o1.getExperiencevalue();
            }
        });
        if (userEntryList.size() > 5) {
            userEntryList = userEntryList.subList(0, 5);
        }
        List<UserDetailInfoDTO> userInfoDTO4WBList = new ArrayList<UserDetailInfoDTO>();
        for (UserEntry userEntry : userEntryList) {
            UserDetailInfoDTO userInfoDTO4WB = new UserDetailInfoDTO(userEntry);
            userInfoDTO4WBList.add(userInfoDTO4WB);
        }
        return userInfoDTO4WBList;
    }

    /*
    * 查找学生主要班级名称
    *
    * */
    public String findMainClassNameByUserId(String userId) {
        ClassEntry classEntry = classDao.getClassEntryByStuId(new ObjectId(userId), Constant.FIELDS);
        if (classEntry == null) {
            return null;
        }
        return classEntry.getName();
    }

    /*
    *
    * 依据主键查找班级信息
    *
    * */
    public ClassInfoDTO findClassInfoByClassId(String classId) {
        ClassEntry classEntry = classDao.getClassEntryById(new ObjectId(classId), Constant.FIELDS);
        if (classEntry == null)
            return null;
        ClassInfoDTO classInfoDTO = new ClassInfoDTO();
        classInfoDTO.setClassName(classEntry.getName());
        classInfoDTO.setId(classEntry.getID().toString());
        classInfoDTO.setIntroduce(classEntry.getIntroduce());
        classInfoDTO.setStudentIds(classEntry.getStudents());
        classInfoDTO.setTeacherIds(classEntry.getTeachers());
        classInfoDTO.setGradeId(classEntry.getGradeId().toString());
        List<ObjectId> ids = classEntry.getTeachers();
//        if(ids!=null && !ids.isEmpty()){
        classInfoDTO.setMainTeacherId(classEntry.getMaster() == null ? "0" : classEntry.getMaster().toString());
//        }

        SchoolEntry se = schoolDao.getSchoolEntry(classEntry.getSchoolId(), Constant.FIELDS);
        if (null != se) {
            Map<ObjectId, Grade> gradeMap = new HashMap<ObjectId, Grade>();
            for (Grade g : se.getGradeList()) {
                gradeMap.put(g.getGradeId(), g);
            }
            Grade g = gradeMap.get(classEntry.getGradeId());
            if (null != g) {
                classInfoDTO.setGradeName(g.getName());
                classInfoDTO.setGradeType(g.getGradeType());
            }
        }

        return classInfoDTO;
    }

    /**
     * 根据学校id查询年级下的班级信息
     *
     * @param schoolID
     * @return
     */
    public List<ClassInfoDTO> findClassInfoBySchoolId(String schoolID) {
        List<ClassEntry> classEntryList = classDao.findClassInfoBySchoolId(new ObjectId(schoolID), Constant.FIELDS);

        List<ClassInfoDTO> classInfoDTOs = new ArrayList<ClassInfoDTO>();
        for (ClassEntry classEntry : classEntryList) {
            ClassInfoDTO classInfoDTO = new ClassInfoDTO();

            classInfoDTO.setClassName(classEntry.getName());
            classInfoDTO.setId(classEntry.getID().toString());
            classInfoDTO.setIntroduce(classEntry.getIntroduce());
            classInfoDTO.setStudentIds(classEntry.getStudents());
            if (null != classEntry.getGradeId()) {
                classInfoDTO.setGradeId(classEntry.getGradeId().toString());
            }
            classInfoDTOs.add(classInfoDTO);
        }
        return classInfoDTOs;
    }

    /*
    *
    * 查询年级下是否存在班级
    *
    * */
    public boolean existClass(String gradeId) {
        List<ClassEntry> classEntryList = classDao.findClassEntryByGradeId(new ObjectId(gradeId));
        if (classEntryList == null || classEntryList.isEmpty()) {
            return false;
        }
        return true;
    }

    /**
     * 根据年级id查询年级下的班级信息
     *
     * @param gradeid
     * @return
     */
    public List<ClassInfoDTO> findClassByGradeId(String gradeid) {
        List<ClassEntry> classEntryList = classDao.findClassEntryByGradeId(new ObjectId(gradeid));
        List<ClassInfoDTO> classInfoDTOList = new ArrayList<ClassInfoDTO>();

        if (classEntryList == null || classEntryList.size() == 0) {
            return classInfoDTOList;
        }


        List<ObjectId> objectIdList = new ArrayList<ObjectId>();

        for (ClassEntry classEntry : classEntryList) {
            ClassInfoDTO classInfoDTO = new ClassInfoDTO();
            classInfoDTO.setClassName(classEntry.getName());
            classInfoDTO.setId(classEntry.getID().toString());
            classInfoDTO.setIntroduce(classEntry.getIntroduce());
            classInfoDTO.setStudentIds(classEntry.getStudents());
            classInfoDTO.setTeacherIds(classEntry.getTeachers());
            if (null != classEntry.getMaster()) {
                classInfoDTO.setMainTeacherId(classEntry.getMaster().toString());
                objectIdList.add(classEntry.getMaster());
            }

            classInfoDTOList.add(classInfoDTO);
        }
        List<UserEntry> userEntryList = userDao.getUserEntryList(objectIdList, new BasicDBObject("nm", 1));
        Map<ObjectId, UserEntry> map = new HashMap<ObjectId, UserEntry>();
        for (UserEntry userEntry : userEntryList) {
            map.put(userEntry.getID(), userEntry);
        }
        for (ClassInfoDTO classInfoDTO : classInfoDTOList) {
            String teacherId = classInfoDTO.getMainTeacherId();
            if (!StringUtils.isBlank(teacherId)) {
                UserEntry userEntry = map.get(new ObjectId(teacherId));
                if (userEntry != null) {
                    classInfoDTO.setMainTeacher(userEntry.getUserName());
                }
            }

        }

        //对班级列表按顺序排序
        ClassInfoDTO classInfoDTO = classInfoDTOList.get(0);
        if ((classInfoDTO.getClassName().contains("(") && classInfoDTO.getClassName().contains(")")) ||
                (classInfoDTO.getClassName().contains("（") && classInfoDTO.getClassName().contains("）"))) {
            Collections.sort(classInfoDTOList, new Comparator<ClassInfoDTO>() {
                @Override
                public int compare(ClassInfoDTO o1, ClassInfoDTO o2) {
                    try {
                        String name1 = o1.getClassName();
                        String name2 = o2.getClassName();

                        String n1 = "";
                        String n2 = "";

                        if (name1.contains("(")) {
                            n1 = name1.substring(name1.indexOf("(") + 1, name1.indexOf(")"));
                        } else {
                            n1 = name1.substring(name1.indexOf("（") + 1, name1.indexOf("）"));
                        }

                        if (name2.contains("(")) {
                            n2 = name2.substring(name2.indexOf("(") + 1, name2.indexOf(")"));
                        } else {
                            n2 = name2.substring(name2.indexOf("（") + 1, name2.indexOf("）"));
                        }

                        return Integer.parseInt(n1) - Integer.parseInt(n2);
                    } catch (Exception ex) {

                    }
                    return 0;
                }
            });
        }
        return classInfoDTOList;
    }

    /**
     * 根据年级id查询年级下的班级id
     *
     * @param gradeids
     * @return
     */
    public List<ClassInfoDTO> findClassByGradeIdList(List<ObjectId> gradeids,String name) {
        List<ClassEntry> classEntryList = classDao.findClassEntryByGradeIds(gradeids,name);
        if (classEntryList == null)
            return null;

        List<ClassInfoDTO> classInfoDTOList = new ArrayList<ClassInfoDTO>();

        for (ClassEntry classEntry : classEntryList) {
            ClassInfoDTO classInfoDTO = new ClassInfoDTO();
            classInfoDTO.setId(classEntry.getID().toString());
            classInfoDTO.setClassName(classEntry.getName());
            classInfoDTOList.add(classInfoDTO);
        }
        return classInfoDTOList;
    }
    public List<ClassInfoDTO> findClassByGradeIdList(List<ObjectId> gradeids) {
        List<ClassEntry> classEntryList = classDao.findClassEntryByGradeIds(gradeids);
        if (classEntryList == null)
            return null;

        List<ClassInfoDTO> classInfoDTOList = new ArrayList<ClassInfoDTO>();

        for (ClassEntry classEntry : classEntryList) {
            ClassInfoDTO classInfoDTO = new ClassInfoDTO();
            classInfoDTO.setId(classEntry.getID().toString());
            classInfoDTO.setClassName(classEntry.getName());
            classInfoDTOList.add(classInfoDTO);
        }
        return classInfoDTOList;
    }

    /**
     * 根据班级id集合查询年级下的班级
     *
     * @param classids
     * @return
     */
    public List<ClassEntry> findClassByClassIdList(List<ObjectId> classids) {
        List<ClassEntry> list = classDao.findClassByClassIdList(classids);
        return list;
    }


    /*
    *
    * 添加班级信息
    *
    * */
    public void addClassInfo(ClassInfoDTO classInfoDTO) {
        ClassEntry classEntry = classInfoDTO.exportEntry();
        classDao.addClassEntry(classEntry);
    }

    /*
    *
    * 删除某个班级 前提是该班级下没有老师
    *
    * */
    public boolean deleteClassInfoById(String classid) {
        ClassEntry classEntry = classDao.getClassEntryById(new ObjectId(classid), Constant.FIELDS);
        List<ObjectId> teacherIds = classEntry.getTeachers();
        List<ObjectId> studentIds = classEntry.getStudents();
        boolean t = teacherIds == null || teacherIds.isEmpty();
        boolean s = studentIds == null || studentIds.isEmpty();
        if (s && t) {
            classDao.deleteById(new ObjectId(classid));
            return true;
        }
        if (s) {
            if (teacherIds.size() == 1 && teacherIds.get(0).equals(classEntry.getMaster())) {
                //只有一个班主任时 也可以删除
                classDao.deleteById(new ObjectId(classid));
                return true;
            }
        }
        return false;
    }

    /*
    *
    * 查找班级老师id
    * */
    public List<ObjectId> findTeacherIdsById(ObjectId objectId) {
        ClassEntry classEntry = classDao.getClassEntryById(objectId, Constant.FIELDS);
        List<ObjectId> teachers = classEntry.getTeachers();
        return teachers;
    }

    /*
    * 查找班级老师信息
    *
    * */
    public List<TeacherInfoView> findTeachersByClassId(ObjectId classId) {
        List<ObjectId> uids = findTeacherIdsById(classId);
        List<UserEntry> userEntryList = userDao.getUserEntryList(uids, Constant.FIELDS);

        List<TeacherClassSubjectEntry> teacherClassSubjectEntryList = teacherClassSubjectDao.findSubjectByTeacherIdAndClassId(uids, classId);
        //构建map
        Map<ObjectId, List<String>> subjectMap = new HashMap<ObjectId, List<String>>();
        for (TeacherClassSubjectEntry teacherClassSubjectEntry : teacherClassSubjectEntryList) {
            List<String> subjectList = subjectMap.get(teacherClassSubjectEntry.getTeacherId());
            if (subjectList == null) {
                subjectList = new ArrayList<String>();
            }
            subjectList.add(teacherClassSubjectEntry.getSubjectInfo().getValue().toString());
            subjectMap.put(teacherClassSubjectEntry.getTeacherId(), subjectList);
        }
        List<TeacherInfoView> teacherInfoViewArrayList = new ArrayList<TeacherInfoView>();
        for (UserEntry userEntry : userEntryList) {
            TeacherInfoView teacherInfoView = new TeacherInfoView();
            teacherInfoView.setId(userEntry.getID().toString());
            teacherInfoView.setUserName(userEntry.getUserName());
            teacherInfoView.setImageUrl(AvatarUtils.getAvatar(userEntry.getAvatar(), 1));
            teacherInfoView.setRole(userEntry.getRole());
            teacherInfoView.setSubjectNameList(subjectMap.get(userEntry.getID()));

            teacherInfoViewArrayList.add(teacherInfoView);
        }
        return teacherInfoViewArrayList;
    }

    /*
    *
    * 依据班级id 查询老师科目信息Entry
    * */
    public List<TeacherSubjectView> findTeacherByIds(List<ObjectId> objectIdList, String classId) {
        List<TeacherClassSubjectEntry> teacherClassLessonEntryList =
                teacherClassSubjectDao.findSubjectByTeacherIdAndClassId(objectIdList, new ObjectId(classId));

        Map<ObjectId, UserEntry> userMap = new HashMap<ObjectId, UserEntry>();
        List<TeacherSubjectView> teacherSubjectViewList = new ArrayList<TeacherSubjectView>();
        List<UserEntry> userEntryList = userDao.getUserEntryList(objectIdList, Constant.FIELDS);
        for (UserEntry userEntry : userEntryList) {
            userMap.put(userEntry.getID(), userEntry);
        }
        for (TeacherClassSubjectEntry teacherClassLessonEntry : teacherClassLessonEntryList) {

            TeacherSubjectView teacherSubjectView = new TeacherSubjectView();
            teacherSubjectView.setTclId(teacherClassLessonEntry.getID().toString());
            teacherSubjectView.setTeacherId(teacherClassLessonEntry.getTeacherId().toString());
            teacherSubjectView.setSubjectId(teacherClassLessonEntry.getSubjectInfo().getId().toString());
            teacherSubjectView.setSubjectName(teacherClassLessonEntry.getSubjectInfo().getValue().toString());

            UserEntry userEntry = userMap.get(teacherClassLessonEntry.getTeacherId());
            if (userEntry != null) {
                teacherSubjectView.setUserName(userEntry.getUserName());
            }
            teacherSubjectViewList.add(teacherSubjectView);
        }

        return teacherSubjectViewList;
    }



    /*
    *
    * 更新老师科目信息
    *
    * */
    public void updateTeacherSubject(String tclid, String teacherid, String subjectid, String subjectName) {
        ObjectId tcsId = new ObjectId(tclid);
        ObjectId teacherId = new ObjectId(teacherid);
        //更新teacherClassEntry 集合
        //因为老师课程 关系在班级之下  所以班级年级信息不做修改
        teacherClassSubjectDao.updateTeacherAndSubject(tcsId, new ObjectId(teacherid), new ObjectId(subjectid), subjectName);
        //保证同步  如果有必要  更新classEntry中的老师id 数组
        teacherClassSubjectDao.getTeacherClassSubjectEntry(new ObjectId(tclid));
        TeacherClassSubjectEntry subjectEntry = teacherClassSubjectDao.getTeacherClassSubjectEntry(tcsId);
        ObjectId classId = subjectEntry.getClassInfo().getId();
        ClassEntry classEntry = classDao.getClassEntryById(classId, Constant.FIELDS);
        List<ObjectId> teacherIds = classEntry.getTeachers();
        boolean k = false;//表示classEntry中不包含当前参数teacherid
        if (teacherIds != null) {
            for (ObjectId objectId : teacherIds) {
                if (objectId.toString().equals(teacherid)) {
                    k = true;
                    break;
                }
            }
        }
        if (!k) {
            classDao.addTeacher(classId, teacherId);
        }

    }

    /*
    *
    * 依据主键删除  老师科目信息记录
    *
    * */
    public void deleteTeacherSubjectById(String tclId) {
        //级联  判断是否删除classEntry中的 tid
        ObjectId teacherClassSubjectId = new ObjectId(tclId);
        //先查找 再删除
        TeacherClassSubjectEntry subjectEntry = teacherClassSubjectDao.getTeacherClassSubjectEntry(teacherClassSubjectId);
        teacherClassSubjectDao.deleteById(teacherClassSubjectId);
        ObjectId classId = subjectEntry.getClassInfo().getId();
        List<ObjectId> classIdList = new ArrayList<ObjectId>();
        classIdList.add(classId);
        List<TeacherClassSubjectEntry> subjectEntryList = teacherClassSubjectDao.findEntryByClassIds(classIdList);
        boolean k = false;//默认不存在
        if (subjectEntryList != null) {
            for (TeacherClassSubjectEntry teacherClassSubjectEntry : subjectEntryList) {
                k = teacherClassSubjectEntry.getTeacherId().equals(subjectEntry.getTeacherId());
                if (k) break;
            }
        }
        if (!k) {//确实不存在
            //删除classEntry中的tid
            classDao.deleteTeacherByIdAndTeacherId(classId, subjectEntry.getTeacherId());
        }
    }

    /*
    * 班级添加学生
    * */
    public void addStudentId(String classId, String stuId) {
        classDao.addStudent(new ObjectId(classId), new ObjectId(stuId));
    }

    /*
    * 班级删除学生
    * */
    public void deleteStuFromClass(String classId, String studentid) {
        classDao.deleteStuById(new ObjectId(classId), new ObjectId(studentid));
    }

    /*
    *
    * 学生调换班级
    *
    * */
    public void updateStuClass(String oldClassId, String newClassId, String studentId) {
        classDao.deleteStuById(new ObjectId(oldClassId), new ObjectId(studentId));
        classDao.addStudent(new ObjectId(newClassId), new ObjectId(studentId));
    }



    /**
     * 查询学生所在班级
     * @param studentId
     * @return
     */
    public List<ClassEntry> findClassEntryByStuId(ObjectId studentId) {
        return classDao.findClassEntryByStuId(studentId);
    }


    /**
     * 对list进行排序
     *
     * @param list
     */
    public void sortList(List<StudentStat> list) {
        Collections.sort(list, new Comparator<StudentStat>() {
            public int compare(StudentStat obj1, StudentStat obj2) {
                int flag = obj1.getGradeName().compareTo(obj2.getGradeName());
                if (flag == 0) {
                    flag = obj1.getClassName().compareTo(obj2.getClassName());
                }
                if (flag == 0) {
                    flag = Collator.getInstance(Locale.CHINESE).compare(obj1.getUserName(), obj2.getUserName());
                    //flag=obj1.getUserName().compareTo(obj2.getUserName());
                }
                return flag;
            }
        });
    }



    public static List<ObjectId> collectObjectId(List<InterestClassStudent> studentList) {
        List<ObjectId> objectIdList = new ArrayList<ObjectId>();
        for (InterestClassStudent interestClassStudent : studentList) {
            objectIdList.add(interestClassStudent.getStudentId());
        }
        return objectIdList;
    }


    private List<InterestClassDTO> interestClassEntryList2ModelList(List<InterestClassEntry> interestClassEntries) {
        List<InterestClassDTO> list = new ArrayList<InterestClassDTO>();
        Set<ObjectId> teacherIds = new HashSet<ObjectId>();

        for (InterestClassEntry interestClassEntry : interestClassEntries) {
            InterestClassDTO interestClassDTO = new InterestClassDTO(interestClassEntry);
            list.add(interestClassDTO);
            teacherIds.add(new ObjectId(interestClassDTO.getTeacherId()));
        }
        Map<ObjectId, UserEntry> userMap = userDao.getUserEntryMap(teacherIds, Constant.FIELDS);
        for (InterestClassDTO interestClassDTO : list) {
            String teacherId = interestClassDTO.getTeacherId();
            UserEntry userEntry = userMap.get(new ObjectId(teacherId));
            if (userEntry != null) {
                interestClassDTO.setTeacherName(userEntry.getUserName());
            }
        }
        return list;
    }

    /**
     * 根据学生id查询班级信息
     *
     * @param stuId
     * @return
     */
    public ClassEntry searchClassEntryByStuId(String stuId) {
        ClassEntry classEntry = classDao.getClassEntryByStuId(new ObjectId(stuId), Constant.FIELDS);
        return classEntry;
    }

    /**
     * 通过班级查找班级的年级信息；用于学生筛选拓展课
     *
     * @param classEntry
     * @return
     */
    public Grade getClassGradeInfo(ClassEntry classEntry) {
        SchoolEntry se = schoolDao.getSchoolEntry(classEntry.getSchoolId(), Constant.FIELDS);
        if (null != se) {
            for (Grade g : se.getGradeList()) {
                if (g.getGradeId().equals(classEntry.getGradeId())) {
                    return g;
                }
            }
        }
        return null;
    }


    /*
    *
    * 获得年级的所有班级信息
    *
    * */
    public List<ClassInfoDTO> getGradeClassesInfo(String gradeId) {
        List<ClassEntry> classEntryList = classDao.findClassEntryByGradeId(new ObjectId(gradeId));
        List<ClassInfoDTO> classInfoDTOList = new ArrayList<ClassInfoDTO>();
        if (classEntryList != null) {
            for (ClassEntry classEntry : classEntryList) {
                ClassInfoDTO classInfoDTO = new ClassInfoDTO();
                classInfoDTO.setClassName(classEntry.getName());
                classInfoDTO.setId(classEntry.getID().toString());
                classInfoDTO.setIntroduce(classEntry.getIntroduce());
                classInfoDTOList.add(classInfoDTO);
            }
        }
        return classInfoDTOList;
    }

    /*
*
* 获得年级的所有班级信息
*
* */
    public List<ClassInfoDTO> getGradeClassesInfo(String gradeId, ObjectId schoolId) {
        List<ClassEntry> classEntryList = classDao.findClassEntryByGradeId(gradeId, schoolId);
        List<ClassInfoDTO> classInfoDTOList = new ArrayList<ClassInfoDTO>();
        if (classEntryList != null) {
            for (ClassEntry classEntry : classEntryList) {
                ClassInfoDTO classInfoDTO = new ClassInfoDTO();
                classInfoDTO.setClassName(classEntry.getName());
                classInfoDTO.setId(classEntry.getID().toString());
                classInfoDTO.setIntroduce(classEntry.getIntroduce());
                classInfoDTOList.add(classInfoDTO);
            }
        }
        return classInfoDTOList;
    }

    /*
*
* 获得年级的所有班级信息
*
* */
    public List<ClassInfoDTO> getGradeClassesInfo(ObjectId gradeId, ObjectId teacherId) {
        List<ClassEntry> classEntryList = classDao.getGradeClassesInfo(gradeId, teacherId);
        List<ClassInfoDTO> classInfoDTOList = new ArrayList<ClassInfoDTO>();
        if (classEntryList != null) {
            for (ClassEntry classEntry : classEntryList) {
                ClassInfoDTO classInfoDTO = new ClassInfoDTO();
                classInfoDTO.setClassName(classEntry.getName());
                classInfoDTO.setId(classEntry.getID().toString());
                classInfoDTO.setIntroduce(classEntry.getIntroduce());
                classInfoDTOList.add(classInfoDTO);
            }
        }
        return classInfoDTOList;
    }

    /**
     * 查询学校下所有班级
     *
     * @return
     */
    public List<ClassEntry> findClassInfoBySchoolId(ObjectId schoolId, DBObject field) {
        return classDao.findClassInfoBySchoolId(schoolId, field);
    }

    public List<ClassEntry> findClassInfoByTeacherId(ObjectId teacherId, DBObject field) {
        List<ClassEntry> list = classDao.getClassEntryByTeacherId(teacherId, field);
        return list;
    }

    public TeacherClassSubjectDTO findTeacherClassSubjectInfo(String tcsId) {
        if (null == tcsId || !ObjectId.isValid(tcsId)) {
            return null;
        }
        ObjectId teacherClassSubjectId = new ObjectId(tcsId);
        TeacherClassSubjectEntry entry = teacherClassSubjectDao.findEntryByPrimaryKey(teacherClassSubjectId);
        if (entry == null) return null;
        return new TeacherClassSubjectDTO(entry);
    }

    public List<TeacherClassSubjectDTO> findTeacherCLassSubjectByTeacherId(String teacherId) {
        if (null == teacherId || !ObjectId.isValid(teacherId)) {
            return null;
        }
        List<TeacherClassSubjectEntry> entryList = teacherClassSubjectDao.findTeacherCLassSubjectByTeacherId(new ObjectId(teacherId));
        List<TeacherClassSubjectDTO> dtoList = new ArrayList<TeacherClassSubjectDTO>();
        if (entryList != null) {
            for (TeacherClassSubjectEntry entry : entryList) {
                dtoList.add(new TeacherClassSubjectDTO(entry));
            }
        }
        return dtoList;
    }

    public List<ClassEntry> findClassEntryByMasterId(ObjectId masterId) {
        return classDao.findClassEntryByMasterId(masterId);
    }

    //*********************************考勤及学号管理*********************************************************


    /**
     * 根据班级id查询班级中的学生的userId
     *
     * @param classId
     * @return
     */
    public Map<String, List<ObjectId>> getRoleUserIdByClassId(String classId) {
        Map<String, List<ObjectId>> uisMap = new HashMap<String, List<ObjectId>>();
        //查询班级信息
        ClassInfoDTO classInfo = findClassInfoByClassId(classId);
        //取得班级下的全部学生id
        List<ObjectId> stuIds = classInfo.getStudentIds();
        if (stuIds == null) {
            stuIds = new ArrayList<ObjectId>();
        }
        uisMap.put("stuIds", stuIds);
        return uisMap;
    }

    /**
     * 根据年级id查询班级中的学生的userId
     *
     * @param gradeId
     * @return
     */
    public Map<String, List<ObjectId>> getRoleUserIdByGradeId(String gradeId) {
        Map<String, List<ObjectId>> uisMap = new HashMap<String, List<ObjectId>>();
        //根据年级id查询年级下的班级信息
        List<ClassInfoDTO> clsLis = findClassByGradeId(gradeId);
        List<ObjectId> stuIds = new ArrayList<ObjectId>();
        //从班级信息下获取学生id和教师id
        for (ClassInfoDTO classInfo : clsLis) {
            if (classInfo.getStudentIds() != null) {
                stuIds.addAll(classInfo.getStudentIds());
            }
        }
        uisMap.put("stuIds", stuIds);
        return uisMap;
    }

    /**
     * 根据学校id查询班级中的学生的userId
     *
     * @param schoolId
     * @return
     */
    public Map<String, List<ObjectId>> getRoleUserIdBySchoolId(String schoolId) {
        Map<String, List<ObjectId>> uisMap = new HashMap<String, List<ObjectId>>();
        //根据学校id查询学校的班级信息
        List<ClassInfoDTO> clsLis = findClassInfoBySchoolId(schoolId);
        List<ObjectId> stuIds = new ArrayList<ObjectId>();
        //从班级信息下获取学生id
        for (ClassInfoDTO classInfo : clsLis) {
            if (classInfo.getStudentIds() != null) {
                stuIds.addAll(classInfo.getStudentIds());
            }
        }
        uisMap.put("stuIds", stuIds);
        return uisMap;
    }


    /**
     * 更新班级年级ID
     *
     * @param cid
     * @param gid
     */
    public void updateGrade(ObjectId cid, ObjectId gid) {
        classDao.updateGrade(cid, gid);
    }

    /**
     * 更新班级名称
     *
     * @param id
     * @param name
     */
    public void updateClassName(ObjectId id, String name) {
        classDao.updateName(id, name);
    }


    public List<ClassInfoDTO> findClassByGradeIds(List<ObjectId> gradeIds) {
        List<ClassEntry> classEntryList = classDao.findClassEntryByGradeIds(gradeIds);
        if (classEntryList == null) return null;

        List<ClassInfoDTO> classInfoDTOList = new ArrayList<ClassInfoDTO>();
        List<ObjectId> objectIdList = new ArrayList<ObjectId>();

        for (ClassEntry classEntry : classEntryList) {
            ClassInfoDTO classInfoDTO = new ClassInfoDTO();
            classInfoDTO.setClassName(classEntry.getName());
            classInfoDTO.setId(classEntry.getID().toString());
            classInfoDTO.setIntroduce(classEntry.getIntroduce());
            classInfoDTO.setStudentIds(classEntry.getStudents());
            classInfoDTO.setTeacherIds(classEntry.getTeachers());
            if (null != classEntry.getMaster()) {
                classInfoDTO.setMainTeacherId(classEntry.getMaster().toString());
                objectIdList.add(classEntry.getMaster());
            }

            classInfoDTOList.add(classInfoDTO);
        }
        List<UserEntry> userEntryList = userDao.getUserEntryList(objectIdList, Constant.FIELDS);
        Map<ObjectId, UserEntry> map = new HashMap<ObjectId, UserEntry>();
        for (UserEntry userEntry : userEntryList) {
            map.put(userEntry.getID(), userEntry);
        }
        for (ClassInfoDTO classInfoDTO : classInfoDTOList) {
            String teacherId = classInfoDTO.getMainTeacherId();
            if (!StringUtils.isBlank(teacherId)) {
                UserEntry userEntry = map.get(new ObjectId(teacherId));
                if (userEntry != null) {
                    classInfoDTO.setMainTeacher(userEntry.getUserName());
                }
            }

        }
        return classInfoDTOList;
    }

    /**
     * 查找班主任所在的行政班
     *
     * @param masterId
     * @return
     * @author shanchao
     */
    public List<ClassEntry> findClassByMasterId(ObjectId masterId) {
        return classDao.findClassByMasterId(masterId);
    }


    /**
     * 查找学生对应的班级
     *
     * @param stuIds
     * @param
     * @return
     */
    public Map<ObjectId, ClassEntry> getClassEntryByStuIds(List<ObjectId> stuIds) {
        Map<ObjectId, ClassEntry> retMap = new HashMap<ObjectId, ClassEntry>();
        List<ClassEntry> cList = classDao.getClassEntryByStuIds(stuIds, new BasicDBObject("nm", 1).append("stus", 1).append("gid",1));
        for (ClassEntry ce : cList) {
            for (ObjectId stuId : ce.getStudents()) {
                retMap.put(stuId, ce);
            }
        }
        return retMap;
    }


    /**
     * 查询班级成员
     *
     * @param ui
     * @param
     * @return
     */
    public Map<ObjectId, UserEntry> getClassMembersByStudentId(ObjectId ui) {
        UserEntry ue = userDao.getUserEntry(ui, Constant.FIELDS);
        if (null != ue) {
            ObjectId searchUI = ue.getID();
            if (UserRole.isParent(ue.getRole())) {
                UserEntry parent = userDao.getUserEntry(ui, Constant.FIELDS);
                searchUI = parent.getConnectIds().get(0);
            }
            ClassEntry ce = classDao.getClassEntryByStuId(searchUI, Constant.FIELDS);
            Set<ObjectId> idset = new HashSet<ObjectId>();
            idset.addAll(ce.getStudents());
            idset.addAll(ce.getTeachers());

            Map<ObjectId, UserEntry> userMap = userDao.getUserEntryMap(idset, new BasicDBObject("nm", 1).append("r", 1));
            return userMap;
        }
        return new HashMap<ObjectId, UserEntry>();
    }




    private Map<ObjectId, List<InterestClassEntry>> formateStuInterestClassMap(List<ObjectId> studentIds, List<InterestClassEntry> interestClassEntryList, int termType) {
        Map<ObjectId, List<InterestClassEntry>> map = new HashMap<ObjectId, List<InterestClassEntry>>();
        for (InterestClassEntry interestClassEntry : interestClassEntryList) {
            List<InterestClassStudent> interestClassStudents = interestClassEntry.getInterestClassStudentsByTermType(termType);
            for (InterestClassStudent interestClassStudent : interestClassStudents) {
                ObjectId stuId = interestClassStudent.getStudentId();
                if (studentIds.contains(stuId)) {
                    List<InterestClassEntry> list = map.get(stuId);
                    if (list != null) {
                        list.add(interestClassEntry);
                    } else {
                        list = new ArrayList<InterestClassEntry>();
                        list.add(interestClassEntry);
                        map.put(stuId, list);
                    }
                }

            }
        }
        return map;
    }



    private String buildScoreInfo(int score) {
        if (score >= 5) {
            return "优秀";
        } else if (score == 4) {
            return "良好";
        } else if (score == 3) {
            return "及格";
        } else {
            return "需努力";
        }
    }

    private void appendRow(HSSFSheet sheet, int rowNum, Object... data) {
        HSSFRow row = sheet.createRow(rowNum);
        for (int i = 0, j = data.length; i < j; i++) {
            Cell cell = row.createCell(i);
            sheet.setColumnWidth(i, 3200);
            cell.setCellValue(data[i] == null ? "" : data[i].toString());
        }
    }

    private void outPutWorkBook(HSSFWorkbook wb, HttpServletResponse response, String schoolName) {
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
            response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(schoolName + "拓展课学生总评.xls", "UTF-8"));
            response.setContentLength(content.length);
            outputStream.write(content);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                    outputStream = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
