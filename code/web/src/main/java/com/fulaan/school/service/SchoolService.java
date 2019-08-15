package com.fulaan.school.service;

import com.db.school.*;
import com.db.user.UserDao;
import com.fulaan.myschool.controller.GradeView;
import com.fulaan.myschool.controller.SubjectView;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.app.SimpleDTO;
import com.pojo.school.*;
import com.pojo.user.UserDetailInfoDTO;
import com.pojo.user.UserEntry;
import com.pojo.user.UserRole;
import com.sys.constants.Constant;
import com.sys.exceptions.IllegalParamException;
import com.sys.utils.MD5Utils;
import com.sys.utils.ValidationUtils;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;


/**
 * Created by hao on 2015/2/27.
 */
@Service
public class SchoolService {

    private static final Logger logger = Logger.getLogger(SchoolService.class);

    private SchoolDao schoolDao = new SchoolDao();

    private ClassDao classDao = new ClassDao();

    private UserDao userDao = new UserDao();


    private TeacherDao teacherDao = new TeacherDao();


    private TeacherClassSubjectDao teacherClassSubjectDao = new TeacherClassSubjectDao();

    private DepartmentDao departmentDao = new DepartmentDao();




    /**
     * 根据学校名和初始密码获取学校，用于导出学校成员列表
     * @param name
     * @return
     */
    public SchoolEntry getSchoolEntry(String name,String initPwd)
    {
        return	schoolDao.getSchoolEntry(name, initPwd);
    }

    /**
     * 根据绑定查找学校
     * @param type
     * @param value
     * @return
     */
    public SchoolEntry getSchoolEntryByBind(int type,String value)
    {
        return schoolDao.getSchoolEntryByBind(type,value);
    }

    /**
     * 添加一个学校
     *
     * @param e
     * @return
     */
    public ObjectId addSchoolEntry(SchoolEntry e) {
        return schoolDao.addSchoolEntry(e);
    }

    /**
     * 根据学校ID查询学校
     *
     * @param id
     * @return
     */
    public SchoolEntry getSchoolEntry(ObjectId id,DBObject fields) {
        return schoolDao.getSchoolEntry(id,fields);
    }


    /**
     * 得到学生年级信息
     *
     * @param studentId
     * @return
     */
    public Grade getStudentGrade(ObjectId studentId) {
        ClassEntry e = classDao.getClassEntryByStuId(studentId, Constant.FIELDS);
        if (null != e) {
            SchoolEntry se = schoolDao.getSchoolEntry(e.getSchoolId(),Constant.FIELDS);
            if (null != se.getGradeList()) {
                for (Grade g : se.getGradeList()) {
                    if (e.getGradeId().equals(g.getGradeId())) {
                        return g;
                    }
                }
            }
        }
        return null;
    }

    /**
     * 得到老师年级
     *
     * @param teacherId
     * @return
     * @throws IllegalParamException
     */
    public List<Grade> getTeacherGrades(ObjectId teacherId) throws IllegalParamException {
        UserEntry ue = userDao.getUserEntry(teacherId, new BasicDBObject("si", Constant.ONE));
        if (null == ue) {
            throw new IllegalParamException();
        }
        SchoolEntry se = schoolDao.getSchoolEntry(ue.getSchoolID(),Constant.FIELDS);
        if (null == se) {
            throw new IllegalParamException();
        }
        return se.getGradeList();
    }


    /**
     * 通过用户ID查询用户的学校
     *
     * @param userId
     * @return
     * @throws IllegalParamException
     */
    public SchoolEntry getSchoolEntryByUserId(ObjectId userId) throws IllegalParamException {
        UserEntry userEntry = userDao.getUserEntry(userId, new BasicDBObject().append("si", 1));
        if (null == userEntry) {
            throw new IllegalParamException("Can not find UserEntry for user:" + userId.toString());
        }
        SchoolEntry schoolEntry = schoolDao.getSchoolEntry(userEntry.getSchoolID(),Constant.FIELDS);
        return schoolEntry;
    }


    /**
     * 给一个年级增加组长
     *
     * @param schoolId 学校ID
     * @param gradeId  年级ID
     * @param leaderId 组长ID
     */
    public void addGradeLeader(ObjectId schoolId, ObjectId gradeId, ObjectId leaderId) {
        schoolDao.addGradeLeader(schoolId, gradeId, leaderId);
    }

    /**
     * 修改某一个年级的班级
     *
     * @param id
     * @param subjectId
     * @param gradeids
     */
    public void updateGradeForSubject(ObjectId id, ObjectId subjectId, List<ObjectId> gradeids) {
        schoolDao.updateGradeForSubject(id, subjectId, gradeids);
    }

    /**
     * 部门详情
     *
     * @param id
     * @return
     */
    public DepartmentEntry getDepartmentEntry(ObjectId id) {
        return departmentDao.getDepartmentEntry(id);
    }


    /**
     * 根据部门文件查询部门
     *
     * @param fileId
     * @return
     */
    public DepartmentEntry getDepartmentEntryByFileId(ObjectId fileId) {
        return departmentDao.getDepartmentEntryByFileId(fileId);
    }


    /**
     * 得到学校的部门
     *
     * @param schoolId
     * @return
     */
    public List<SimpleDTO> getDepartments(ObjectId schoolId) {
        List<SimpleDTO> retList = new ArrayList<SimpleDTO>();
        List<DepartmentEntry> dboList = departmentDao.getDepartmentEntrys(schoolId);
        for (DepartmentEntry e : dboList) {
            retList.add(new SimpleDTO(e));
        }
        return retList;
    }

    /**
     * 添加学校的部门
     *
     * @param schoolId
     * @param name
     */
    public ObjectId addDepartment(ObjectId schoolId, String name, String des) {
        DepartmentEntry e = new DepartmentEntry(schoolId, name, des, null, null);
        return departmentDao.addDepartmentEntry(e);
    }


    /**
     * 删除部门
     */
    public void removeDepartment(ObjectId id) {
        departmentDao.removeDepartmentEntry(id);
    }

    /**
     * 部门添加成员
     */
    public void addMember(ObjectId id, ObjectId userID) {
        departmentDao.addMember(id, userID);
    }

    /**
     * 删除成员
     *
     * @param id
     * @param userID
     */
    public void removeMember(ObjectId id, ObjectId userID) {
        departmentDao.deleteMember(id, userID);
    }


    public String findSchoolNameByUserId(String userId) {
        UserEntry userEntry = userDao.getUserEntry(new ObjectId(userId), new BasicDBObject().append("si", 1));
        SchoolEntry schoolEntry = schoolDao.getSchoolEntry(userEntry.getSchoolID(),Constant.FIELDS);
        return schoolEntry.getName();
    }


    /*
    *
    * 主键查询学校
    *
    *
    * */
    public SchoolDTO findSchoolById(String schoolID) {
        SchoolEntry schoolEntry = schoolDao.getSchoolEntry(new ObjectId(schoolID),Constant.FIELDS);
        SchoolDTO schoolDTO = new SchoolDTO(schoolEntry);
        return schoolDTO;
    }

    /*
    *
    * 查询学校科目列表
    *
    *
    * */
    public List<SubjectView> findSubjectList(String schoolID) {
        SchoolDTO schoolDTO = findSchoolById(schoolID);
        List<Subject> subjects = schoolDTO.getSubjectList();
        List<SubjectView> subjectViews = new ArrayList<SubjectView>();
        if (subjects != null) {
            for (Subject subject : subjects) {
                List<ObjectId> userIds = subject.getUserIds();
                SubjectView subjectView = new SubjectView(subject);
                String userName = "";
                if (userIds!=null && userIds.size()!=0) {
                    userName = userDao.getUserEntry(userIds.get(0),null).getUserName();
                    subjectView.setUserId(userIds.get(0).toString());
                }
                subjectView.setUserName(userName);
                subjectViews.add(subjectView);
            }
        }
        return subjectViews;
    }

    /*
    *
    * 学校年级列表 并构建view层显示对象返回
    *
    * */
    public List<GradeView> findGradeList(String schoolID) {
        SchoolDTO schoolDTO = findSchoolById(schoolID);
        List<Grade> idValuePairList = schoolDTO.getGradeList();
        List<GradeView> gradeViews = new ArrayList<GradeView>();
        List<ObjectId> objectIdList = new ArrayList<ObjectId>();
        if (idValuePairList != null) {
            for (Grade grade : idValuePairList) {
                if (grade.getGradeType() != -1) {
                    gradeViews.add(new GradeView(grade));
                    objectIdList.add(grade.getLeader());
                    objectIdList.add(grade.getCleader());
                }

            }
        }
        Map<ObjectId, UserEntry> userEntryMap = userDao.getUserEntryMap(objectIdList, new BasicDBObject("nm", 1));
        for (GradeView gradeView : gradeViews) {
            String leaderId = gradeView.getLeader();
            if (leaderId != null) {
                ObjectId leadObjId = new ObjectId(leaderId);
                UserEntry userEntry = userEntryMap.get(leadObjId);
                if (userEntry != null) {
                    gradeView.setLeaderName(userEntry.getUserName());
                }
            }
            String cleaderId = gradeView.getCleader();
            if (cleaderId != null) {
                ObjectId cleadObjId = new ObjectId(cleaderId);
                UserEntry userEntry = userEntryMap.get(cleadObjId);
                if (userEntry != null) {
                    gradeView.setCleaderName(userEntry.getUserName());
                }
            }
        }
        return gradeViews;
    }

    /*
    * 更新科目名称和年级
    *
    * */
    public boolean updateSubjectNameAndGrade(String subjectId, String schoolId, String newSubjectName, String gradeArray,String subjectTeacher) {
        ObjectId schoolID = new ObjectId(schoolId);
        SchoolEntry schoolEntry = schoolDao.getSchoolEntry(schoolID,Constant.FIELDS);
        List<Subject> subjectList = schoolEntry.getSubjects();
        Subject sub = null;
        if (subjectList != null) {
            for (Subject subject : subjectList) {
                if (subject.getSubjectId().toString().equals(subjectId)) {
                    sub = subject;
                    break;
                }
            }
        }
        //deleteSubject(schoolId,subjectId);
        //因为已经有老师选择该科目，故用deleteSubject方法无法删除
        deleteSubjectUnJudge(schoolId,subjectId);
        List<ObjectId> objectIdList = new ArrayList<ObjectId>();
        String[] ids = gradeArray.split(",");
        for (int i = 0; i < ids.length; i++) {
            ObjectId gradeId = new ObjectId(ids[i]);
            objectIdList.add(gradeId);
        }
//        ObjectId userId = null;
//        if (sub.getUserIds()!=null && sub.getUserIds().size()!=0) {
//            userId = sub.getUserIds().get(0);
//        }
//        if (userId!=null && !userId.toString().equals(subjectTeacher)) {
//            UserEntry user = userDao.getUserEntry(userId,new BasicDBObject("r",1));
//            userDao.updateUserRole(userId,user.getRole()-UserRole.LEADER_OF_SUBJECT.getRole());

//        }
        List<ObjectId> teacherIds = new ArrayList<ObjectId>();
        if (!subjectTeacher.equals("0")) {
            UserEntry user2 = userDao.getUserEntry(new ObjectId(subjectTeacher),new BasicDBObject("r",1));
            if (!UserRole.isLeaderOfSubject(user2.getRole())) {
                userDao.updateUserRole(new ObjectId(subjectTeacher),user2.getRole()+UserRole.LEADER_OF_SUBJECT.getRole());
            }

            teacherIds.add(new ObjectId(subjectTeacher));
        }
        sub.setUserIds(teacherIds);
        sub.setName(newSubjectName);
        sub.setGradeIds(objectIdList);
        addSubject2School(schoolID, sub);
        return true;
    }

    /*
    *
    * 为学校添加科目
    *
    * */
    public void addSubject2School(ObjectId schoolId, Subject sub) {
        //  schoolDao.updateSubject(schoolId, sub);
        schoolDao.addSubject(schoolId, sub);
    }

    /*
    *
    * 根据 年级 科目名称 创建该科目并添加到学校
    *
    * */
    public boolean addSubject2School(String schoolId, String gradeIds, String subjectName,String subjectTeacher) {
        Subject subject = new Subject(new BasicDBObject());
        subject.setSubjectId(new ObjectId());
        subject.setName(subjectName);

        List<ObjectId> objectIdList = new ArrayList<ObjectId>();
        String[] ids = gradeIds.split(",");
        for (int i = 0; i < ids.length; i++) {
            ObjectId gradeId = new ObjectId(ids[i]);
            objectIdList.add(gradeId);
        }
        subject.setGradeIds(objectIdList);
        if (!StringUtils.isEmpty(subjectTeacher) && !"0".equals(subjectTeacher)) {
            List<ObjectId> subjectTeacherId = new ArrayList<ObjectId>();
            subjectTeacherId.add(new ObjectId(subjectTeacher));
            subject.setUserIds(subjectTeacherId);
            UserEntry user = userDao.getUserEntry(new ObjectId(subjectTeacher), new BasicDBObject("r", 1));
            userDao.updateUserRole(new ObjectId(subjectTeacher),user.getRole()+UserRole.LEADER_OF_SUBJECT.getRole());
        }
        schoolDao.addSubject(new ObjectId(schoolId), subject);
        return true;
    }
    /**
     * 直接删除学科，不判断是否有老师已经选择，用于修改学科时，删除原有的，继而添加新的，id不变
     * add by miaoqiang
     * @param schoolId
     * @param subjectId
     * @return
     */
    public Map<String, Object> deleteSubjectUnJudge(String schoolId, String subjectId) {
        Map<String, Object> map = new HashMap<String, Object>();
        Subject subject = new Subject(new BasicDBObject());
        subject.setSubjectId(new ObjectId(subjectId));
        schoolDao.deleteSubject(new ObjectId(schoolId), subject);
        map.put("result", true);
        return map;
    }
    /*
    *
    *删除学校科目
    * */
    public Map<String, Object> deleteSubject(String schoolId, String subjectId) {
        Map<String, Object> map = new HashMap<String, Object>();
        //先检查是否已经有老师选了该科目，已选科目则不可删除
        List<TeacherClassSubjectEntry> subjectEntryList = teacherClassSubjectDao.findTeacherClassSubjectBySubjectId(new ObjectId(subjectId));
        if (subjectEntryList.size() == 0) {
            Subject subject = new Subject(new BasicDBObject());
            subject.setSubjectId(new ObjectId(subjectId));
            schoolDao.deleteSubject(new ObjectId(schoolId), subject);
            map.put("result", true);
            return map;
        } else {
            List<String> result = new ArrayList<String>();
            List<ObjectId> classList = new ArrayList<ObjectId>();
            for (TeacherClassSubjectEntry tcsubject : subjectEntryList) {
                classList.add(tcsubject.getClassInfo().getId());
            }

            Map<ObjectId, ClassEntry> classEntryMap = classDao.getClassEntryMap(classList, Constant.FIELDS);


            SchoolEntry schoolEntry = schoolDao.getSchoolEntry(new ObjectId(schoolId),Constant.FIELDS);//获取当前学校

            for (Map.Entry<ObjectId, ClassEntry> entry : classEntryMap.entrySet()) {
                for (Grade grade : schoolEntry.getGradeList()) {
                    if (grade.getGradeId().equals(entry.getValue().getGradeId())) {
                        result.add(grade.getName() + entry.getValue().getName());
                        break;
                    }
                }
            }

            map.put("result", false);
            map.put("reason", result);
            return map;
        }
    }

    /*
    * 添加老师
    *
    * */
    public String addTeacher(String schoolId, String teacherName, String jobNum, String permission) {
        SchoolEntry schoolEntry = schoolDao.getSchoolEntry(new ObjectId(schoolId),Constant.FIELDS);
        String pwd = schoolEntry.getInitialPassword();
        String k = teacherDao.addTeacher(new ObjectId(schoolId), teacherName, jobNum, permission, pwd);
        return k;
    }

    /*
    *
    * 通过主键 更新老师信息
    * */
    public boolean updateTeacher(String teacherId, String teacherName, String jobNum, String permission) {
        UserEntry user = userDao.getUserEntry(new ObjectId(teacherId), Constant.FIELDS);
        int role = user.getRole();
        String roleStr = "";
        //如果原来是校长或老师，修改为校长，则增加角色(modify by miaoqiang)
        if ((UserRole.isHeadmaster(role) || UserRole.isTeacher(role)) && (Integer.parseInt(permission) == 8 ||
                Integer.parseInt(permission) == 72)) {
            role = Integer.parseInt(permission) | role;
            roleStr = Integer.toString(role);
        } else {
            roleStr = permission;
        }
        boolean k = teacherDao.updateTeacher(new ObjectId(teacherId), teacherName, jobNum, roleStr);
        return k;
    }
    /*
    *
    * 通过主键 更新老师信息
    * */
    public void updateTeacherCheck(String teacherId, String teacherName, String jobNum, String permission,Map<String,Object> model) {
        boolean flg = true;
        List<UserEntry> userEntryList=userDao.findEntryByUserName(teacherName);
        UserEntry uentry = userDao.searchUserByLoginName(teacherName);
        if (ValidationUtils.isRequestModile(teacherName)) {
            flg = false;
            model.put("mesg","用户名不能为手机号，请重新输入！");
        }
        if ((userEntryList!=null && userEntryList.size()!=0) || uentry!=null) {
            flg = false;
            model.put("mesg","用户名重复，请重新输入！");
        }
        if (flg) {
            UserEntry user = userDao.getUserEntry(new ObjectId(teacherId), Constant.FIELDS);
            int role = user.getRole();
            String roleStr = "";
            //如果原来是校长或老师，修改为校长，则增加角色(modify by miaoqiang)
            if ((UserRole.isHeadmaster(role) || UserRole.isTeacher(role)) && (Integer.parseInt(permission) == 8 ||
                    Integer.parseInt(permission) == 72)) {
                role = Integer.parseInt(permission) | role;
                roleStr = Integer.toString(role);
            } else {
                roleStr = permission;
            }
            teacherDao.updateTeacher(new ObjectId(teacherId), teacherName, jobNum, roleStr);
        }
        model.put("flg",flg);
    }

    public void updateTeacherRole(String teacherId, String teacherName, String jobNum, String permission, int ismanage){
        UserEntry user = userDao.getUserEntry(new ObjectId(teacherId), Constant.FIELDS);
        int role = user.getRole();
        String roleStr = "";
        int int_permission= Integer.parseInt(permission);
        if(UserRole.isTeacher(int_permission)){
            if(ismanage==1){
                int_permission+=UserRole.ADMIN.getRole();
            }
        }

        //如果原来是校长或老师，修改为校长，则增加角色(modify by miaoqiang)
        if (UserRole.isTeacher(role)&& int_permission == 8) {
            role = int_permission | UserRole.TEACHER.getRole();
            roleStr = Integer.toString(role);
        } else {
            roleStr = Integer.toString(int_permission);
        }
        //roleStr = Integer.toString(int_permission);
        teacherDao.updateTeacher(new ObjectId(teacherId), teacherName, jobNum, roleStr);
    }
    /*
    *
    * 老师名称搜索并分页
    *
    * */
    public List<UserDetailInfoDTO> teacherList(String schoolId, String keyWord, int skip, int size) {
        List<UserDetailInfoDTO> userInfoDTOList = teacherDao.teacherList(new ObjectId(schoolId), keyWord, skip, size);
        return userInfoDTOList;
    }

    /*
    *
    * 初始化密码
    *
    *
    * */
    public boolean initPwdOfTeacher(String userId, String schoolID) {
        SchoolEntry schoolEntry = schoolDao.getSchoolEntry(new ObjectId(schoolID),Constant.FIELDS);
        boolean k = teacherDao.updatePwd(new ObjectId(userId), MD5Utils.getMD5String(schoolEntry.getInitialPassword()));
        return k;
    }

    /*
    *
    * 更新学校初始密码
    *
    * */
    public boolean updateNewInitPwd(String schoolID, String newPwd) {
        schoolDao.update(new ObjectId(schoolID), "inp", newPwd);
        return true;
    }


    public void update(ObjectId id,String field,Object value)
    {
        schoolDao.update(id, field, value);
    }
    /*
    *
    * 添加一个年级
    *
    * */
    public void addGrade(String schoolId, Grade grade) {
        schoolDao.addGrade(new ObjectId(schoolId), grade);
    }

    /*
    *
    * 通过学校id 更新年级信息
    *
    * */
    public void updateGradeById(String schoolId, Grade grade) {
        schoolDao.updateById(new ObjectId(schoolId), grade);
    }

    /*
    *
    * 删除某个学校的年级
    *
    * */
    public void deleteGradeById(String schoolId, String gradeId) {
        schoolDao.deleteGradeById(new ObjectId(schoolId), new ObjectId(gradeId));
    }

    /*
    *
    * 更新学校名称 和等级
    *
    * */
    public void updateSchoolNameAndLevel(String schoolId, String schoolName, int num) {
        schoolDao.updateSchoolNameAndLevel(new ObjectId(schoolId), schoolName, num);
    }




    /*
    * 依据 学校id 和 科目Id 查找科目信息
    *
    * */
    public SubjectView findSubjectBySchoolIdAndSubId(String schoolId, String subjectId) {
        SchoolEntry schoolEntry = schoolDao.getSchoolEntry(new ObjectId(schoolId),Constant.FIELDS);
        List<Subject> subjectList = schoolEntry.getSubjects();
        Subject sub = null;
        if (subjectList != null) {
            for (Subject subject : subjectList) {
                if (subject.getSubjectId().toString().equals(subjectId)) {
                    sub = subject;
                    break;
                }
            }
        }
        if (sub != null) {
            SubjectView subjectView = new SubjectView(sub);
            if (sub.getUserIds()!=null &&sub.getUserIds().size()!=0) {
                subjectView.setUserId(sub.getUserIds().get(0).toString());
                UserEntry user = userDao.getUserEntry(sub.getUserIds().get(0), null);
                subjectView.setUserName(user != null ? user.getUserName():"");
            }
            return subjectView;
        }
        return null;
    }

    /*
    *
    * 查询年级的科目信息
    *
    * */
    public List<SubjectView> findSubjectListBySchoolIdAndGradeId(String schoolId, String gradeId) {
        SchoolEntry schoolEntry = schoolDao.getSchoolEntry(new ObjectId(schoolId),Constant.FIELDS);
        List<Subject> subjectList = schoolEntry.getSubjects();
        List<SubjectView> subjectViewList = new ArrayList<SubjectView>();
        if (subjectList != null) {
            for (Subject subject : subjectList) {
                List<ObjectId> gradIds = subject.getGradeIds();
                if (gradIds != null) {
                    for (ObjectId gid : gradIds) {
                        if (gid != null && gid.toString().equals(gradeId)) {
                            SubjectView subjectView = new SubjectView();
                            subjectView.setId(subject.getSubjectId().toString());
                            subjectView.setName(subject.getName());
                            subjectViewList.add(subjectView);
                            break;
                        }
                    }
                }
            }
        }
        return subjectViewList;
    }

    public Map<String, SubjectView> findSubjectViewMapBySchoolIdAndGradeId(String schoolId, String gradeId) {
        Map<String, SubjectView> map = new HashMap<String, SubjectView>();
        SchoolEntry schoolEntry = schoolDao.getSchoolEntry(new ObjectId(schoolId),Constant.FIELDS);
        List<Subject> subjectList = schoolEntry.getSubjects();
        if (subjectList != null) {
            for (Subject subject : subjectList) {
                List<ObjectId> gradIds = subject.getGradeIds();
                if (gradIds != null) {
                    for (ObjectId gid : gradIds) {
                        if (gid != null && gid.toString().equals(gradeId)) {
                            SubjectView subjectView = new SubjectView();
                            subjectView.setId(subject.getSubjectId().toString());
                            subjectView.setName(subject.getName());
                            map.put(subjectView.getId(), subjectView);
                            break;
                        }
                    }
                }
            }
        }
        return map;
    }

    /*
    *
    *搜索学校年级列表
    *
    * */
    public List<GradeView> searchSchoolGradeList(String schoolId) {
        List<Grade> grades = schoolDao.findSchoolInfoBySchoolId(new ObjectId(schoolId));
        List<GradeView> gradeViews = new ArrayList<GradeView>();
        if (grades != null) {
            for (Grade grade : grades) {
                gradeViews.add(new GradeView(grade));
            }
        }
        return gradeViews;
    }

    /*
    *
    *搜索学校年级列表
    *
    * */
    public List<GradeView> searchSchoolGradeList(ObjectId schoolId) {
        List<Grade> grades = schoolDao.findSchoolInfoBySchoolId(schoolId);
        List<GradeView> gradeViews = new ArrayList<GradeView>();
        if (grades != null) {
            for (Grade grade : grades) {
                if (grade.getGradeType() != -1) {
                    gradeViews.add(new GradeView(grade));
                }
            }
        }
        return gradeViews;
    }
    /*
    *
    * 统计学校老师数量 依据keyWord ==>userName
    * */
    public int countTeacher(String schoolId, String keyWord) {
        int count = teacherDao.countTeacher(new ObjectId(schoolId), keyWord);
        return count;
    }

    /*
    *
    * 查询多个学校信息的list
    *
    * */
    public List<SchoolDTO> findSchoolInfoBySchoolIds(List<ObjectId> schoolIds) {
        List<SchoolDTO> list = new ArrayList<SchoolDTO>();
        List<SchoolEntry> SEList = schoolDao.getSchoolEntryList(schoolIds);
        for (SchoolEntry schoolEntry : SEList) {
            SchoolDTO schoolDTO = new SchoolDTO(schoolEntry);
            list.add(schoolDTO);
        }
        return list;
    }

    /**
     * 批量修改用户密码
     *
     * @param userIds
     * @param password
     */
    public boolean resetSelectPassword(List<ObjectId> userIds, String password) {
        boolean k = userDao.resetSelectPassword(userIds, password);
        return k;
    }


    /**
     * 通过用户ID查询用户的学校,不丢异常版
     *
     * @param userId
     * @return
     */
    public SchoolEntry getSchoolEntryByUserIdWithoutException(ObjectId userId) {
        UserEntry userEntry = userDao.getUserEntry(userId, new BasicDBObject().append("si", 1));
        if (null == userEntry) {
            return null;
        }
        SchoolEntry schoolEntry = schoolDao.getSchoolEntry(userEntry.getSchoolID(),Constant.FIELDS);
        return schoolEntry;
    }


    public List<Grade> findSchoolInfoByParams(List<ObjectId> schoolIds, int gradeType) {
        List<Grade> list = schoolDao.findSchoolInfoByParams(schoolIds, gradeType);
        return list;
    }

    public List<SchoolEntry> getSchoolEntryListByRegionForEdu() {
        return schoolDao.getSchoolEntryListByRegionForEdu();
    }

    /**
     * 根据学校id获取学科
     * @param schoolid
     * @return
     */
    public Map<ObjectId,Subject> getSubjectEntryMap(String schoolid) {
        Map<ObjectId, Subject> retMap =new HashMap<ObjectId, Subject>();
        SchoolDTO schoolDTO = findSchoolById(schoolid);
        List<Subject> subjects = schoolDTO.getSubjectList();
        if (subjects != null) {
            for (Subject subject : subjects) {
                retMap.put(subject.getSubjectId(),subject);
            }
        }
        return retMap;
    }
    
    
    public Map<ObjectId,Subject> getSubjectEntryMap(ObjectId schoolid) {
        Map<ObjectId, Subject> retMap =new HashMap<ObjectId, Subject>();
        
        SchoolEntry se=  schoolDao.getOldSchoolEntry(schoolid);
      
        List<Subject> subjects = se.getSubjects();
        if (subjects != null) {
            for (Subject subject : subjects) {
                retMap.put(subject.getSubjectId(),subject);
            }
        }
        return retMap;
    }

    /**
     * 根据学校名获取学校Ids
     * @param name
     * @return
     */
    public List<ObjectId> getSchoolIdByNames(String name) {
        return schoolDao.getSchoolIdByNames(name);
    }

    public List<SchoolDTO> getSchoolEntryByRegion(Set<ObjectId> regionIds, Set<ObjectId> noschoolIds,String schoolName) {
        List<SchoolDTO> list = new ArrayList<SchoolDTO>();
        DBObject fields =new BasicDBObject("nm", Constant.ONE);
        List<SchoolEntry> SEList = schoolDao.getSchoolEntryByRegion(regionIds, noschoolIds, schoolName, fields);
        for (SchoolEntry schoolEntry : SEList) {
            SchoolDTO schoolDTO = new SchoolDTO();
            schoolDTO.setSchoolId(schoolEntry.getID().toString());
            schoolDTO.setSchoolName(schoolEntry.getName());
            list.add(schoolDTO);
        }
        return list;
    }

    /**
     * 根据学校id获取学科
     * @param schoolidlist
     * @return
     */
    public Map<ObjectId,SchoolEntry> getSchoolEntryMap(List<ObjectId> schoolidlist) {
        Map<ObjectId, SchoolEntry> retMap =new HashMap<ObjectId, SchoolEntry>();
        List<SchoolEntry> schoolEntryList = schoolDao.getSchoolEntryList(schoolidlist);
        if (schoolEntryList != null) {
            for (SchoolEntry schoolEntry : schoolEntryList) {
                retMap.put(schoolEntry.getID(),schoolEntry);
            }
        }
        return retMap;
    }

    /**
     * 根据学校id 返回学校map
     * @param schoolIds
     * @param fields
     * @return
     */
    public Map<ObjectId, SchoolEntry> getSchoolMap(Collection<ObjectId> schoolIds,DBObject fields)
    {
        return schoolDao.getSchoolMap(schoolIds, fields);
    }

    public List<SchoolEntry> getAllSchoolEntryList(DBObject fields) {
        return schoolDao.getAllSchoolEntryList(fields);
    }
}
