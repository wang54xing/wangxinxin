package com.fulaan.user.service;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.Collator;
import java.util.*;

import javax.servlet.http.HttpServletResponse;

import com.db.app.RegionDao;
import com.fulaan.utils.RESTAPI.bo.schoolbase.GradeAPI;
import com.sys.utils.DateTimeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.bson.types.ObjectId;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.db.school.ClassDao;
import com.db.school.DepartmentDao;
import com.db.school.SchoolDao;
import com.db.school.TeacherClassSubjectDao;
import com.db.user.UserDao;
import com.fulaan.examresult.controller.IdNameDTO;
import com.fulaan.groupdiscussion.service.EaseMobService;
import com.fulaan.school.service.SchoolService;
import com.fulaan.schoolbase.dto.GradeDTO;
import com.fulaan.schoolbase.dto.SubjectDTO;
import com.fulaan.utils.RESTAPI.bo.notice.NoticeBaseAPI;
import com.fulaan.utils.RESTAPI.bo.user.UserAPI;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.app.FieldValuePair;
import com.pojo.app.IdNameValuePairDTO;
import com.pojo.app.IdValuePair;
import com.pojo.app.RegionEntry;
import com.pojo.school.ClassEntry;
import com.pojo.school.ClassInfoDTO;
import com.pojo.school.DepartmentEntry;
import com.pojo.school.Grade;
import com.pojo.school.InterestClassEntry;
import com.pojo.school.SchoolEntry;
import com.pojo.school.TeacherClassSubjectEntry;
import com.pojo.user.UserDetailInfoDTO;
import com.pojo.user.UserEntry;
import com.pojo.user.UserInfoDTO;
import com.pojo.user.UserRole;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import com.sys.exceptions.IllegalParamException;
import com.sys.utils.JsonUtil;
import com.sys.utils.MD5Utils;
import com.sys.utils.ValidationUtils;


/**
 * Created by yan on 2015/2/27.
 */
@Service
public class UserService {

    private static final Logger logger = Logger.getLogger(UserService.class);
    /**
     * 用户一般性字段
     */
    public static final DBObject COMMON_FIELDS = new BasicDBObject("nm", 1).append("avt", 1);


    private UserDao userDao = new UserDao();
    private SchoolDao schoolDao = new SchoolDao();
    private ClassDao classDao = new ClassDao();
    private TeacherClassSubjectDao teacherClassSubjectDao = new TeacherClassSubjectDao();
    private RegionDao regionDao = new RegionDao();
    @Autowired
    EaseMobService easeMobService;
    @Autowired
    SchoolService schoolService;

    private DepartmentDao departmentDao = new DepartmentDao();



    /**
     * 通过用户id获取用户所在学校的注册地址id
     *
     * @param id
     * @return
     */
    public String findGeoIdByUserId(String id) {
        UserEntry userEntry = userDao.getUserEntry(new ObjectId(id), Constant.FIELDS);
        ObjectId schoolId = userEntry.getSchoolID();
        SchoolEntry schoolEntry = schoolDao.getSchoolEntry(schoolId, Constant.FIELDS);
        ObjectId regionId = schoolEntry.getRegionId();
        return regionId.toString();
    }

    /**
     * 通过用户id获取用户信息
     *
     * @param id
     * @return
     */
    public UserDetailInfoDTO findUserInfoHasCityName(String id) {
        UserEntry userEntry = userDao.getUserEntry(new ObjectId(id), Constant.FIELDS);
        ObjectId schoolId = userEntry.getSchoolID();
        SchoolEntry schoolEntry = schoolDao.getSchoolEntry(schoolId, Constant.FIELDS);
        ObjectId regionId = schoolEntry.getRegionId();
        RegionEntry regionEntry = regionDao.getRegionById(regionId);
        UserDetailInfoDTO userInfoDTO = new UserDetailInfoDTO(userEntry);
        userInfoDTO.setCityName(regionEntry.getName());
        return userInfoDTO;
    }

    /*
    * 主键查询
    *
    * */
    public UserDetailInfoDTO getUserInfoById(String id) {
        UserEntry userEntry = userDao.getUserEntry(new ObjectId(id), Constant.FIELDS);
        if (userEntry == null) return null;
        return new UserDetailInfoDTO(userEntry);
    }

    /**
     * 查询用户信息
     *
     * @param uids
     * @return
     */
    public List<UserDetailInfoDTO> findUserInfoByUserIds(List<String> uids) {
        List<ObjectId> objectIdList = new ArrayList<ObjectId>();
        for (String sid : uids) {
            objectIdList.add(new ObjectId(sid));
        }
        List<UserEntry> userEntryList = userDao.getUserEntryList(objectIdList, Constant.FIELDS);
        List<UserDetailInfoDTO> userInfoDTO4WBs = new ArrayList<UserDetailInfoDTO>();
        for (UserEntry userEntry : userEntryList) {
            UserDetailInfoDTO userInfoDTO4WB = new UserDetailInfoDTO(userEntry);
            userInfoDTO4WBs.add(userInfoDTO4WB);
        }
        return userInfoDTO4WBs;
    }

    /**
     * 查询用户信息
     *
     * @param uids
     * @return
     */
    public List<UserDetailInfoDTO> findUserInfoByIds(List<ObjectId> uids) {
        List<UserEntry> userEntryList = userDao.getUserEntryList(uids, Constant.FIELDS);
        List<UserDetailInfoDTO> userInfoDTO4WBs = new ArrayList<UserDetailInfoDTO>();
        for (UserEntry userEntry : userEntryList) {
            UserDetailInfoDTO userInfoDTO4WB = new UserDetailInfoDTO(userEntry);
            userInfoDTO4WBs.add(userInfoDTO4WB);
        }
        sortList(userInfoDTO4WBs);
        return userInfoDTO4WBs;
    }

    /**
     * 获取用户信息
     *
     * @param userIds
     * @return
     */
    public List<IdNameDTO> findUserIdNameByIds(List<ObjectId> userIds) {
        List<UserEntry> userEntryList = userDao.getUserEntryList(userIds, Constant.FIELDS);
        Map<ObjectId, String> userNameMap = new HashMap<ObjectId, String>();

        for (UserEntry userEntry : userEntryList) {
            userNameMap.put(userEntry.getID(), userEntry.getUserName());
        }

        List<IdNameDTO> userList = new ArrayList<IdNameDTO>();
        for (ObjectId userId : userIds) {
            if (userNameMap.containsKey(userId)) {
                userList.add(new IdNameDTO(userId.toString(), userNameMap.get(userId)));
            }
        }
        return userList;
    }


    /**
     * 对list进行排序
     *
     * @param list
     */
    public void sortList(List<UserDetailInfoDTO> list) {
        Collections.sort(list, new Comparator<UserDetailInfoDTO>() {
            public int compare(UserDetailInfoDTO obj1, UserDetailInfoDTO obj2) {
                int flag = Collator.getInstance(Locale.CHINESE).compare(obj1.getUserName(), obj2.getUserName());

                return flag;
            }
        });
    }


    /**
     * 查找全校学生
     *
     * @return
     */
    public Map<String, Object> getSchoolAllStudent(String schoolId) {
        Map<String, Object> model = new HashMap<String, Object>();
        List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
        List<UserEntry> userEntryList = userDao.getStudentEntryBySchoolId(new ObjectId(schoolId), new BasicDBObject("nm", 1));
        if (userEntryList != null && userEntryList.size() > 0) {
            for (UserEntry userEntry : userEntryList) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("nickName", userEntry.getUserName());
                map.put("id", userEntry.getID().toString());
                rows.add(map);
            }
        }
        model.put("rows", rows);
        return model;
    }

    /**
     * 查找特定年级的学生
     *
     * @return
     */
    public Map<String, Object> getGradeStudent(List<String> gradeList, String classId) {
        List<Map<String, Object>> classList = new ArrayList<Map<String, Object>>();
        List<ObjectId> studentIdList = new ArrayList<ObjectId>();
        List<ObjectId> gradeIdList = new ArrayList<ObjectId>();
        for (String gradeId : gradeList) {
            gradeIdList.add(new ObjectId(gradeId));
        }
        List<ClassEntry> classEntryList = classDao.findClassEntryByGradeIds(gradeIdList);

        if (null != classEntryList) {
            for (ClassEntry classEntry : classEntryList) {
                studentIdList.addAll(classEntry.getStudents());
                Map<String, Object> info = new HashMap<String, Object>();
                info.put("classId", classEntry.getID().toString());
                info.put("className", classEntry.getName());
                classList.add(info);
            }
        }
        if (null != classId && "" != classId) {
            ClassEntry classEntry = classDao.getClassEntryById(new ObjectId(classId), Constant.FIELDS);
            if (null != classEntry) {
                studentIdList.clear();
                studentIdList.addAll(classEntry.getStudents());
            }
        }
        Map<String, Object> model = new HashMap<String, Object>();
        List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
        List<UserEntry> userEntryList = userDao.getUserEntryList(studentIdList, new BasicDBObject("nm", 1));
        if (userEntryList != null && userEntryList.size() > 0) {
            for (UserEntry userEntry : userEntryList) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("nickName", userEntry.getUserName());
                map.put("id", userEntry.getID().toString());
                rows.add(map);
            }
        }
        model.put("rows", rows);
        model.put("classList", classList);
        return model;
    }

    /*
    *
    * 通过父ID 查询孩子信息
    *
    * */
    public UserDetailInfoDTO findStuInfoByParentId(String id) {
        UserEntry userInfo = userDao.getUserEntryByParentId(new ObjectId(id));
        if (userInfo == null) return null;
        return new UserDetailInfoDTO(userInfo);
    }

    /**
     * 查询用户信息
     *
     * @param userId
     * @return
     */
    public UserDetailInfoDTO selectUserInfoHasClassName(String userId) {
        UserEntry userEntry = userDao.getUserEntry(new ObjectId(userId), Constant.FIELDS);
        UserDetailInfoDTO userInfoDTO4WB = new UserDetailInfoDTO(userEntry);
        //查找 地区名称
        SchoolEntry schoolEntry = schoolDao.getSchoolEntry(userEntry.getSchoolID(), Constant.FIELDS);
        ObjectId regionId = schoolEntry.getRegionId();
        RegionEntry regionEntry = regionDao.getRegionById(regionId);
        userInfoDTO4WB.setCityName(regionEntry.getName());
        //查找班级名称
        ClassEntry classEntry = classDao.getClassEntryByStuId(userEntry.getID(), new BasicDBObject("nm", 1));
        userInfoDTO4WB.setMainClassName(classEntry.getName());
        return userInfoDTO4WB;
    }

    /**
     * 根据用户名精确查询
     *
     * @param userName
     * @return
     */
    public UserEntry searchUserByUserName(String userName) {
        return userDao.searchUserByUserName(userName);
    }


    /**
     * 根据用户登录名精确查询
     *
     * @param userLoginName
     * @return
     */
    public UserEntry searchUserByUserLoginName(String userLoginName) {
        return userDao.searchUserByLoginName(userLoginName);
    }


    /**
     * 根据用户身份证查询
     *
     * @param sid
     * @return
     */
    public UserEntry searchUserBySid(String sid) {
        return userDao.searchUserBySid(sid);
    }

    /**
     * 根据用户手机登录
     *
     * @param mobile
     * @return
     */
    public UserEntry searchUserByMobile(String mobile) {
        return userDao.searchUserByMobile(mobile);
    }

    /**
     * 根据用户邮箱
     *
     * @return
     */
    public UserEntry searchUserByEmail(String email) {
        return userDao.searchUserByEmail(email);
    }

    /**
     * 根据ID查询
     *
     * @param id
     * @return
     */
    public UserEntry searchUserId(ObjectId id) {
        return userDao.getUserEntry(id, Constant.FIELDS);
    }

    /**
     * 根据CHATID查询
     *
     * @param id
     * @return
     */
    public UserEntry searchUserChatId(String id) {
        return userDao.getUserEntryByChatid(id, Constant.FIELDS);
    }


    /**
     * 通过绑定用户查询
     *
     * @param type
     * @param bindValue
     * @return
     */
    public UserEntry searchUserByUserBind(int type, String bindValue) {
        return userDao.searchUserByUserBind(type, bindValue);
    }


    /**
     * 查找没有绑定学生
     *
     * @param schoolId
     * @return
     */
    public UserEntry getUserByNoBind(ObjectId schoolId) {
        return userDao.getUserByNoBind(schoolId);
    }

    /**
     * 根据用户ID查询，返回用户的map
     *
     * @param ids
     * @param fields
     * @return
     */
    public Map<ObjectId, UserEntry> getUserEntryMap(Collection<ObjectId> ids, DBObject fields) {
        return userDao.getUserEntryMap(ids, fields);
    }

    /**
     * 根据用户ID查询，返回用户的map
     *
     * @param ids
     * @param fields
     * @return
     */
    public Map<ObjectId, UserEntry> getUserEntryMapByUserIds(Collection<ObjectId> ids, String userName, DBObject fields) {
        return userDao.getUserEntryMap(ids, userName, fields);
    }

    public List<UserEntry> getUserEntryMapByUserIds(List<ObjectId> ids, int sex, List<Integer> roles, DBObject fields, ObjectId sid) {
        return userDao.getUserEntryMap(ids, sex, roles, fields, sid);
    }

    /**
     * 根据用户ID查询，返回用户的map
     *
     * @param ids
     * @param fields
     * @return
     */
    public Map<ObjectId, UserEntry> getUserEntryMap2(Collection<ObjectId> ids, DBObject fields) {
        return userDao.getUserEntryMap2(ids, fields);
    }


    /**
     * 根据用户名查询，返回用户的id
     *
     * @return
     */
    public List<ObjectId> getUserIdsByName(Collection<String> userNames) {
        return userDao.getUserIdsByUserName(userNames);
    }


    /**
     * 添加用户
     */
    public ObjectId addUser(UserEntry e) {
        return userDao.addUserEntry(e);
    }


    /**
     * 删除用户
     *
     * @param uid
     */
    public void deleteUser(ObjectId uid) {
        userDao.logicRemoveUser(uid);
    }

    /**
     * 通过学校ID查找用户信息
     *
     * @param schoolID
     * @param fields
     * @return
     */
    public Map<ObjectId, UserEntry> getUserEntryMapBySchoolid(ObjectId schoolID, DBObject fields) {
        Map<ObjectId, UserEntry> retMap = new HashMap<ObjectId, UserEntry>();
        List<UserEntry> list = userDao.getUserInfoBySchoolid(schoolID, fields);
        for (UserEntry ue : list) {
            retMap.put(ue.getID(), ue);
        }
        return retMap;
    }

    /**
     * 得到一个学校的老师
     *
     * @param schoolId
     * @param fields
     * @return
     */
    public List<UserEntry> getTeacherEntryBySchoolId(ObjectId schoolId, BasicDBObject fields) {
        return userDao.getTeacherEntryBySchoolId(schoolId, fields);
    }

    /**
     * 得到一个学校的老师
     *
     * @param schoolId
     * @param fields
     * @return
     */
    public List<UserEntry> getTeacherEntryBySchoolId(ObjectId schoolId, String teaName, BasicDBObject fields) {
        return userDao.getTeacherEntryBySchoolId(schoolId, teaName, fields);
    }

    /**
     * 得到一个学校的学生
     *
     * @param schoolId
     * @param fields
     * @return
     */
    public List<UserEntry> getStudentEntryBySchoolId(ObjectId schoolId, BasicDBObject fields) {
        return userDao.getStudentEntryBySchoolId(schoolId, fields);
    }

    /**
     * 得到用户学校信息
     *
     * @param users        必须包含_id和si两个字段
     * @param schoolFields 学校中的字段
     * @return key为用户的ID
     */
    public Map<ObjectId, SchoolEntry> getUserSchoolInfo(Collection<UserEntry> users, DBObject schoolFields) {
        Map<ObjectId, SchoolEntry> retMap = new HashMap<ObjectId, SchoolEntry>();

        if (null != users) {
            List<ObjectId> schoolsId = MongoUtils.getFieldObjectIDs(users, "si");
            Map<ObjectId, SchoolEntry> schoolInfoMap = schoolDao.getSchoolMap(schoolsId, schoolFields);
            SchoolEntry se;
            for (UserEntry ue : users) {
                se = schoolInfoMap.get(ue.getSchoolID());
                if (null != se) {
                    retMap.put(ue.getID(), se);
                }
            }
        }
        return retMap;
    }

    /**
     * 通过学校查询老师信息，并且按照 <li>学科</li>
     * <li>班级</li>
     * <li>班主任</li>
     * 进行分组
     *
     * @param schoolId
     * @param type     1学科 2 班级 3班主任
     * @return
     */
    public Map<IdNameValuePairDTO, Set<IdNameValuePairDTO>> getTeacherMap(ObjectId schoolId, int type) {
        Map<IdNameValuePairDTO, Set<IdNameValuePairDTO>> retMap = new HashMap<IdNameValuePairDTO, Set<IdNameValuePairDTO>>();
        //如果学校人数过大，会有性能影响
        //todo by fourer
        List<UserEntry> userEntryList = userDao.getUserEntryBySchoolIdList(schoolId, new BasicDBObject("r", 1).append("nnm", 1));
        Map<ObjectId, UserEntry> userMap = new HashMap<ObjectId, UserEntry>();
        for (UserEntry e : userEntryList) {
            if (UserRole.isTeacher(e.getRole())) {
                userMap.put(e.getID(), e);
            }
        }

        UserEntry e;
        if (type == 1 || type == 2)//学科 班级
        {
            List<TeacherClassSubjectEntry> tcjList = teacherClassSubjectDao.findSubjectByTeacherIds(userMap.keySet());
            for (TeacherClassSubjectEntry tcj : tcjList) {
                IdNameValuePairDTO dto = new IdNameValuePairDTO(type == 1 ? tcj.getSubjectInfo() : tcj.getClassInfo());
                if (!retMap.containsKey(dto)) {
                    retMap.put(dto, new HashSet<IdNameValuePairDTO>());
                }
                e = userMap.get(tcj.getTeacherId());
                if (null != e) {
                    retMap.get(dto).add(new IdNameValuePairDTO(e));
                }
            }
        }

        if (type == 3)//班主任
        {
            List<ClassEntry> ceList = classDao.findClassInfoBySchoolId(schoolId, Constant.FIELDS);
            for (ClassEntry ce : ceList) {
                IdNameValuePairDTO dto = new IdNameValuePairDTO(ce);
                if (!retMap.containsKey(dto)) {
                    retMap.put(dto, new HashSet<IdNameValuePairDTO>());
                }
                e = userMap.get(ce.getMaster());
                if (null != e) {
                    retMap.get(dto).add(new IdNameValuePairDTO(e));
                }
            }
        }
        return retMap;
    }


    /**
     * 通过学校查询学生或家长信息，并且按照 <li>班级</li>进行分组
     *
     * @param schoolId
     * @param type     1学生 2家长
     * @return
     */
    public Map<IdNameValuePairDTO, Set<IdNameValuePairDTO>> getStudentOrParentMap(ObjectId schoolId, int type) {
        Map<IdNameValuePairDTO, Set<IdNameValuePairDTO>> retMap = new HashMap<IdNameValuePairDTO, Set<IdNameValuePairDTO>>();
        List<ClassEntry> ceList = classDao.findClassInfoBySchoolId(schoolId, Constant.FIELDS);

        Set<ObjectId> totalSet = new HashSet<ObjectId>();

        for (ClassEntry ce : ceList) {
            IdNameValuePairDTO dto = new IdNameValuePairDTO(ce);
            if (!retMap.containsKey(dto)) {
                retMap.put(dto, new HashSet<IdNameValuePairDTO>());
            }

            for (ObjectId stuId : ce.getStudents()) {
                retMap.get(dto).add(new IdNameValuePairDTO(stuId));
                totalSet.add(stuId);
            }
        }

        Map<ObjectId, UserEntry> userMap = userDao.getUserEntryMap(totalSet, new BasicDBObject().append("nm", 1).append("cid", 1));
        UserEntry e;
        for (Map.Entry<IdNameValuePairDTO, Set<IdNameValuePairDTO>> entry : retMap.entrySet()) {
            for (IdNameValuePairDTO dto : entry.getValue()) {
                e = userMap.get(dto.getId());
                if (null != e) {
                    if (Constant.ONE == type) {
                        dto.setValue(e.getUserName());
                    }
                    if (Constant.TWO == type) {
                        //todo 如果有学生没有关联家长，则创建一个objectid，这样系统可以运行，只是增加一点垃圾数据
                        dto.setId(e.getConnectIds().size() > 0 ? e.getConnectIds().get(0) : new ObjectId());
                        dto.setValue(e.getUserName() + "家长");
                    }
                }
            }
        }
        return retMap;
    }


    /**
     * 得到部门人员
     *
     * @param schoolId
     * @return
     */
    public Map<IdNameValuePairDTO, Set<IdNameValuePairDTO>> getDepartmemtMembersMap(ObjectId schoolId) {
        Map<IdNameValuePairDTO, Set<IdNameValuePairDTO>> retMap = new HashMap<IdNameValuePairDTO, Set<IdNameValuePairDTO>>();

        List<DepartmentEntry> depList = departmentDao.getDepartmentEntrys(schoolId);

        Set<ObjectId> totalSet = new HashSet<ObjectId>();

        for (DepartmentEntry ce : depList) {
            IdNameValuePairDTO dto = new IdNameValuePairDTO(ce);
            if (!retMap.containsKey(dto)) {
                retMap.put(dto, new HashSet<IdNameValuePairDTO>());
            }

            for (ObjectId stuId : ce.getMembers()) {
                retMap.get(dto).add(new IdNameValuePairDTO(stuId));
                totalSet.add(stuId);
            }
        }

        Map<ObjectId, UserEntry> userMap = userDao.getUserEntryMap(totalSet, new BasicDBObject().append("nm", 1).append("cid", 1));
        UserEntry e;
        for (Map.Entry<IdNameValuePairDTO, Set<IdNameValuePairDTO>> entry : retMap.entrySet()) {
            for (IdNameValuePairDTO dto : entry.getValue()) {
                e = userMap.get(dto.getId());
                if (null != e) {
                    dto.setValue(e.getUserName());
                }
            }
        }
        return retMap;
    }

    /**
     * 查找校领导
     *
     * @param schoolId
     * @return
     */
    public List<UserEntry> getSchoolLeader(ObjectId schoolId) {
        return userDao.getSchoolLeader(schoolId);
    }

    /**
     * 添加用户
     *
     * @param userInfoDTO4WB
     * @return
     */
    public String addUserInfo(UserDetailInfoDTO userInfoDTO4WB) {
        UserEntry userEntry = userInfoDTO4WB.exportEntry();
        userDao.addUserEntry(userEntry);
        //添加一个用户，增加环信功能
        try {
            easeMobService.createNewUser(userEntry.getChatId().toString());
        } catch (Exception ex) {
            logger.error("", ex);
        }
        return userEntry.getID().toString();
    }

    /**
     * 重置密码
     *
     * @param id
     * @param initPwd
     * @return
     */
    public void resetPwd(ObjectId id, String initPwd) {
        try {
            userDao.update(id, "pw", MD5Utils.getMD5String(initPwd), false);
        } catch (IllegalParamException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }





    public List<UserEntry> findUserRoleInfoBySchoolId(String schoolId) {
        BasicDBObject fields = new BasicDBObject("r", Constant.ONE);
        List<UserEntry> userEntryList = userDao.getUserEntryBySchoolIdList(new ObjectId(schoolId), fields);
        return userEntryList;
    }


    /**
     * 根据学校id查询学校全部人员信息
     *
     * @param schoolId
     * @return
     */
    public List<UserDetailInfoDTO> findUserInfoBySchoolId(String schoolId, int role) {

        List<UserEntry> userEntryList = userDao.getUserBySchoolIdAndRole(new ObjectId(schoolId), role, new BasicDBObject("nm", 1));
        List<UserDetailInfoDTO> userInfoDTOList = new ArrayList<UserDetailInfoDTO>();
        for (UserEntry userEntry : userEntryList) {
            UserDetailInfoDTO userInfoDTO = new UserDetailInfoDTO(userEntry.getID(), userEntry.getRealUserName());
            userInfoDTOList.add(userInfoDTO);
        }
        return userInfoDTOList;
    }


    public List<UserDetailInfoDTO> findUserBySchoolId2(String schoolId) {
        List<UserEntry> userEntryList = userDao.getUserEntryBySchoolIdList(new ObjectId(schoolId), new BasicDBObject(Constant.ID, 1).append("chatid", 1).append("avt", 1).append("nnm", 1).append("nm", 1).append("r", 1));
        List<UserDetailInfoDTO> userInfoDTOList = new ArrayList<UserDetailInfoDTO>();
        for (UserEntry userEntry : userEntryList) {
            UserDetailInfoDTO userInfoDTO = new UserDetailInfoDTO(userEntry, 1);
            userInfoDTOList.add(userInfoDTO);
        }
        return userInfoDTOList;
    }

    /**
     * 根据学校id查询学校全部人员信息
     *
     * @param schoolID
     * @return
     */
    public List<UserDetailInfoDTO> getUserInfoBySchoolid(ObjectId schoolID) {
        List<UserEntry> userEntryList = userDao.getTeacherEntryBySchoolId(schoolID, Constant.FIELDS);
        List<UserDetailInfoDTO> userlist = new ArrayList<UserDetailInfoDTO>();
        if (userEntryList != null && userEntryList.size() != 0) {
            for (UserEntry user : userEntryList) {
                if (UserRole.isHeadmaster(user.getRole())) {
                    userlist.add(new UserDetailInfoDTO(user));
                }
            }
        }
        return userlist;
    }

    public Map<String, Object> getPresTerBySchoolid(ObjectId id, ObjectId schoolID, Map<String, Object> map) {
        List<UserEntry> userEntryList = userDao.getTeacherEntryBySchoolId(schoolID, Constant.FIELDS);
        List<UserDetailInfoDTO> presidentList = new ArrayList<UserDetailInfoDTO>();
        List<UserDetailInfoDTO> teachersList = new ArrayList<UserDetailInfoDTO>();
        if (userEntryList != null && userEntryList.size() != 0) {
            for (UserEntry user : userEntryList) {
                if (UserRole.isHeadmaster(user.getRole())) {
                    presidentList.add(new UserDetailInfoDTO(user));
                }
                if (UserRole.isTeacher(user.getRole()) || UserRole.isManager(user.getRole())) {
                    if (!user.getID().equals(id)) {
                        teachersList.add(new UserDetailInfoDTO(user));
                    }
                }
            }
            map.put("presidentList", presidentList);
            map.put("teachersList", teachersList);
        }
        return map;
    }

    /**
     * 查询用户信息
     *
     * @param userName
     * @return
     */
    public List<UserDetailInfoDTO> findUserInfoByUserName(String userName) {
        List<UserEntry> userEntryList = userDao.findEntryByUserName(userName);
        List<UserDetailInfoDTO> userDetailInfoDTOList = new ArrayList<UserDetailInfoDTO>();
        if (userEntryList != null) {
            for (UserEntry userEntry : userEntryList) {
                UserDetailInfoDTO userDetailInfoDTO = new UserDetailInfoDTO(userEntry);
                userDetailInfoDTOList.add(userDetailInfoDTO);
            }
        }
        return userDetailInfoDTOList;
    }

    /**
     * 更新用户头像
     *
     * @param userId
     * @param avatar
     * @throws Exception
     */
    public void updateAvatar(String userId, String avatar) throws Exception {
        if (!ObjectId.isValid(userId)) {
            throw new IllegalParamException("the id [" + userId + " ] is valid!!");
        }
        FieldValuePair fvp = new FieldValuePair("avt", avatar);
        userDao.update(new ObjectId(userId), fvp);
    }

    /**
     * 取得用户密码
     *
     * @param userId
     * @return
     * @throws Exception
     */
    public String getUserPassword(String userId) throws Exception {
        if (!ObjectId.isValid(userId)) {
            throw new IllegalParamException("the id [" + userId + " ] is valid!!");
        }
        return userDao.getUserEntry(new ObjectId(userId), Constant.FIELDS).getPassword();

    }

    /**
     * 修改用户密码
     *
     * @param userId
     * @return
     * @throws Exception
     */
    public void updatePassword(String userId, String password) throws Exception {
        if (!ObjectId.isValid(userId)) {
            throw new IllegalParamException("the id [" + userId + " ] is valid!!");
        }
        FieldValuePair fvp = new FieldValuePair("pw", password);
        userDao.update(new ObjectId(userId), fvp);
    }

    public String updateUserInfo(String userId, Integer sex, String nm, String logn, String mn, String son) throws Exception {
        if (!ObjectId.isValid(userId)) {
            throw new IllegalParamException("the id [" + userId + " ] is valid!!");
        }
        UserEntry entry = userDao.getUserEntry(new ObjectId(userId), Constant.FIELDS);

        if (entry != null) {
            List<String> logName = new ArrayList<String>();
            logName.add(logn.toLowerCase());
            List<ObjectId> us = new ArrayList<ObjectId>();
            us.add(new ObjectId(userId));
            List<String> Mns = new ArrayList<String>();
            List<String> Mnst = new ArrayList<String>();
            Mns.add(logn.toLowerCase());
            List<UserEntry> e = userDao.searchUserByUserName(logName, us);
            List<UserEntry> es = userDao.searchUserByUserNames(Mnst, us);
            List<UserEntry> ms = new ArrayList<UserEntry>();
            if (!mn.equals("")) {
                ms = userDao.searchUserByUserMn(Mns, us);
            }
            if (e.size() == 0 && es.size() == 0 && ms.size() == 0) {
                entry.setSex(sex);
                entry.setUserName(entry.getLoginName().toLowerCase());
                entry.setMobileNumber(mn);
                entry.setLoginName(logn);
                entry.setLoginName(entry.getLoginName().toLowerCase());
                entry.setNickName(nm);
                if (!mn.equals("")) {
                    boolean md = ValidationUtils.isRequestModile(mn);
                    if (md == false) {
                        return "on";
                    }
                }
            } else {
                return "on";
            }
        }
        if (UserRole.isStudent(entry.getRole())) {
            entry.setStudyNum(son);
        } else {
            entry.setJobnumber(son);
        }
        userDao.update(new ObjectId(userId), entry);
        return "ok";
    }


    public void updateUserGroupList(List<String> userlist, IdValuePair idvalue) {
        for (String userid : userlist) {
            userDao.updateUserGroupList(userid, idvalue);
        }
    }

    public void deleteUserGroupList(List<String> memberList, String roomid) {
        for (String userid : memberList) {
            UserEntry userentry = userDao.getUserEntryByChatid(userid, Constant.FIELDS);
            for (IdValuePair idvaluepair : userentry.getGroupInfoList()) {
                if (idvaluepair.getBaseEntry().getString("id").equals(roomid)) {
                    userDao.deleteUserGroupList(userid, idvaluepair);
                }
            }

        }
    }


    /**
     * 得到用户所在的班级列表
     *
     * @param userId
     * @return
     */
    public List<ClassInfoDTO> getClassDTOList(ObjectId userId, int userRole) {
        List<ClassInfoDTO> classInfoDTOList = new ArrayList<ClassInfoDTO>();

        if (UserRole.isStudent(userRole) || UserRole.isParent(userRole)) {

            if (UserRole.isParent(userRole)) {
                UserDetailInfoDTO stuinfo = findStuInfoByParentId(userId.toString());
                if (stuinfo != null) {
                    userId = new ObjectId(stuinfo.getId());
                } else {
                    userId = null;
                }
            }

            if (userId != null) {
                List<ClassEntry> classEntryList = classDao.getClassEntryListByStudentId(userId,
                        Constant.FIELDS);
                for (ClassEntry classEntry : classEntryList) {

                    ClassInfoDTO classInfoDTO = new ClassInfoDTO();
                    classInfoDTO.setClassName(classEntry.getName());
                    classInfoDTO.setId(classEntry.getID().toString());
                    classInfoDTOList.add(classInfoDTO);
                }


            }


        } else if (UserRole.isHeadmaster(userRole)) {
            String schoolId = getUserInfoById(userId.toString()).getSchoolID();
            List<ClassEntry> classEntryList = classDao.findClassInfoBySchoolId(new ObjectId(schoolId), Constant.FIELDS);
            //todo : 兴趣班先不加
            for (ClassEntry classEntry : classEntryList) {

                ClassInfoDTO classInfoDTO = new ClassInfoDTO();
                classInfoDTO.setClassName(classEntry.getName());
                classInfoDTO.setId(classEntry.getID().toString());
                classInfoDTOList.add(classInfoDTO);
            }


        } else if (UserRole.isTeacher(userRole)) {
            List<ClassEntry> classEntryList = classDao.getClassEntryByTeacherId(userId, Constant.FIELDS);
            //todo : 兴趣班先不加
            for (ClassEntry classEntry : classEntryList) {

                ClassInfoDTO classInfoDTO = new ClassInfoDTO();
                classInfoDTO.setClassName(classEntry.getName());
                classInfoDTO.setId(classEntry.getID().toString());
                classInfoDTOList.add(classInfoDTO);
            }



        }


        return classInfoDTOList;
    }

    /**
     * 查询学生家长信息
     *
     * @param studentId
     * @return
     */
    public List<UserDetailInfoDTO> findParentByStuId(String studentId) {
        List<UserDetailInfoDTO> userDetailInfoDTOs = new ArrayList<UserDetailInfoDTO>();
        if (null != studentId && ObjectId.isValid(studentId)) {
            List<UserEntry> userEntryList = userDao.findParentEntryByStuId(new ObjectId(studentId));
            for (UserEntry user : userEntryList) {
                userDetailInfoDTOs.add(new UserDetailInfoDTO(user));
            }
        }
        return userDetailInfoDTOs;
    }

    public void updateIntroduction(String introduce, String userId) {
        userDao.updateIntroduction(introduce, new ObjectId(userId));
    }

    /*
    *更新用户角色
    * */
    public boolean updateRole(String teacherId, int role) {
        if (null == teacherId || !ObjectId.isValid(teacherId)) {
            return false;
        }
        try {
            userDao.update(new ObjectId(teacherId), "r", role, true);
        } catch (IllegalParamException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    /**
     * 查询薪资制表所有的老师
     *
     * @param @param  schoolId
     * @param @return 设定文件
     * @return List<UserInfoDTO>    返回类型
     * @throws
     * @Title: findTeatherBySchoolId
     * @Description: TODO(这里用一句话描述这个方法的作用)
     */
    public List<UserInfoDTO> findTeatherBySchoolId(String schoolId) {
        List<UserEntry> userEntryList = userDao.getTeacherEntryBySchoolId(new ObjectId(schoolId), Constant.FIELDS);
        List<UserInfoDTO> userInfoDTOList = new ArrayList<UserInfoDTO>();
        for (UserEntry userEntry : userEntryList) {
            UserInfoDTO userInfoDTO = new UserInfoDTO(userEntry);
            userInfoDTOList.add(userInfoDTO);
        }
        return userInfoDTOList;
    }


    /**
     * 更新微校园时间
     *
     * @param id
     */
    public void updSchoolHomeDate(ObjectId id) {
        userDao.updSchoolHomeDate(id);
    }

    /**
     * 更新微家园时间
     *
     * @param id
     */
    public void updFamilyHomeDate(ObjectId id) {
        userDao.updFamilyHomeDate(id);
    }

    /**
     * 更新最后登录时间
     *
     * @param ui
     * @throws IllegalParamException
     */
    public void updateLastActiveDate(ObjectId ui) throws IllegalParamException {
        userDao.update(ui, "lad", System.currentTimeMillis(), true);
    }


    /**
     * 更新用户字段
     *
     * @param userId
     * @param field
     * @param value
     * @throws IllegalParamException
     */
    public void update(ObjectId userId, String field, Object value) throws IllegalParamException {
        userDao.update(userId, field, value, false);
    }


    public List<UserEntry> findUserRoleInfoBySchoolIds(List<ObjectId> schoolIds) {
        List<UserEntry> userEntryList = userDao.findUserRoleInfoBySchoolIds(schoolIds);
        return userEntryList;
    }

    /*
   * 检查用户名是否重复
   *
   * */
    public boolean existUserInfo(String userName) {
        boolean k = userDao.existUserInfo(userName);
        return k;
    }

    /**
     * 根据学校id查询老师信息
     *
     * @param schoolId
     * @return
     */
    public List<UserDetailInfoDTO> findTeacherInfoBySchoolId(String schoolId) {
        List<UserDetailInfoDTO> userInfoDTOList = new ArrayList<UserDetailInfoDTO>();

        List<UserEntry> list = getTeacherEntryBySchoolId(new ObjectId(schoolId), Constant.FIELDS);
        for (UserEntry ue : list) {
            try {
                userInfoDTOList.add(new UserDetailInfoDTO(ue));
            } catch (Exception ex) {
            }
        }


        List<UserDetailInfoDTO> teacherList = new ArrayList<UserDetailInfoDTO>();
        for (UserDetailInfoDTO userInfo : userInfoDTOList) {
            if (UserRole.isTeacher(userInfo.getRole())) {
                teacherList.add(userInfo);
            } else if (UserRole.isHeadmaster(userInfo.getRole())) {
                teacherList.add(userInfo);
            } else if (UserRole.isLeaderClass(userInfo.getRole())) {
                teacherList.add(userInfo);
            } else if (UserRole.isLeaderOfGrade(userInfo.getRole())) {
                teacherList.add(userInfo);
            } else if (UserRole.isLeaderOfSubject(userInfo.getRole())) {
                teacherList.add(userInfo);
            }
        }
        return teacherList;
    }


    /**
     * 根据chatids查询
     *
     * @param chatids
     * @param fields
     * @return
     */
    public List<UserEntry> getUserEntrysByChatids(List<String> chatids, DBObject fields) {
        return userDao.getUserEntrysByChatids(chatids, fields);
    }

    /**
     * 根据条件查询用户
     *
     * @param role
     * @param noUserIds
     * @return
     */
    public List<UserInfoDTO> getUserListByParam(int role, String userName, Set<ObjectId> noUserIds) {
        List<UserEntry> userEntryList = userDao.getUserListByParam(role, userName, noUserIds);
        List<UserInfoDTO> dtolist = new ArrayList<UserInfoDTO>();
        for (UserEntry userEntry : userEntryList) {
            UserInfoDTO userInfoDTO = new UserInfoDTO(userEntry);
            dtolist.add(userInfoDTO);
        }
        return dtolist;
    }

    /**
     * k6kt增加经验值
     *
     * @param name
     */
    public void updateExp(String name) {
        UserEntry user = userDao.getUserEntryByName(name);
        userDao.updateExperenceValue(user.getID());
    }


    /**
     * 根据用户名精确查询
     *
     * @param page
     * @param pageSize
     * @return
     */
    public List<UserEntry> searchUser(String schoolId, String name, int jinyan, int page, int pageSize) {
        return userDao.searchUser(schoolId, name, jinyan, page < 1 ? 0 : ((page - 1) * pageSize), pageSize);
    }

    public int searchUserCount(String schoolId, String name, int jinyan) {
        return userDao.searchUserCount(schoolId, name, jinyan);
    }

    public List<UserDetailInfoDTO> getK6ktEntryByRoles() {
        List<UserEntry> userEntryList = userDao.getK6ktEntryByRoles(Constant.FIELDS);
        List<UserDetailInfoDTO> userInfoDTOs = new ArrayList<UserDetailInfoDTO>();
        List<ObjectId> schoolids = new ArrayList<ObjectId>();
        for (UserEntry user : userEntryList) {
            schoolids.add(user.getSchoolID());
        }
        Map<ObjectId, SchoolEntry> schoolEntryMap = schoolService.getSchoolEntryMap(schoolids);
        for (UserEntry user : userEntryList) {
            UserDetailInfoDTO userDetailInfoDTO = new UserDetailInfoDTO(user);
            userDetailInfoDTO.setSchoolName(schoolEntryMap.get(user.getSchoolID()).getName());
            userInfoDTOs.add(userDetailInfoDTO);
        }
        return userInfoDTOs;
    }

    public List<UserDetailInfoDTO> getK6ktEntryByRoles2() {
        List<UserEntry> userEntryList = userDao.getK6ktEntryByRoles2(Constant.FIELDS);
        List<UserDetailInfoDTO> userInfoDTOs = new ArrayList<UserDetailInfoDTO>();
        List<ObjectId> schoolids = new ArrayList<ObjectId>();
        for (UserEntry user : userEntryList) {
            schoolids.add(user.getSchoolID());
        }
        Map<ObjectId, SchoolEntry> schoolEntryMap = schoolService.getSchoolEntryMap(schoolids);
        for (UserEntry user : userEntryList) {
            UserDetailInfoDTO userDetailInfoDTO = new UserDetailInfoDTO(user);
            userDetailInfoDTO.setSchoolName(schoolEntryMap.get(user.getSchoolID()).getName());
            userInfoDTOs.add(userDetailInfoDTO);
        }
        return userInfoDTOs;
    }

    public ObjectId randomAnId() {
        List<ObjectId> userIdList = userDao.getUserForEBusiness(new ObjectId("55934c14f6f28b7261c19c62"), Constant.FIELDS);
        Random random = new Random();
        int j = random.nextInt(400);
        return userIdList.get(j);
    }

    public void updateJinYanTime(String userid) {
        userDao.updateJinYanTime(new ObjectId(userid));

    }


    /**
     * 通过并定用户名登录
     *
     * @return
     */
    public UserEntry getUserEntryByBindName(String bindName, int Type) {
        return userDao.getUserEntryByBindName(bindName, Type);
    }

    /**
     * 得到
     *
     * @return
     */
    public Map<String, RegionEntry> getRegionEntryMap() {
        return regionDao.getRegionEntryMap(null);
    }

    /**
     * 新注册商城用户赠送购物券
     */
    public void giveVouchers(ObjectId userId) {
        // voucherService.addEVoucher(userId,1000);
        // voucherService.addEVoucher(userId,1000);
    }

    /**
     * 学生爸爸
     */
    public String getFather(ObjectId stuId) {
        UserEntry student = userDao.getUserEntry(stuId, Constant.FIELDS);
        List<ObjectId> parents = student.getConnectIds();
        if (parents.size() == 2) {//新数据
            return parents.get(0).toString();
        }
        return parents.get(1).toString();
    }

    /**
     * 学生妈妈
     */
    public String getMother(ObjectId stuId) {
        UserEntry student = userDao.getUserEntry(stuId, Constant.FIELDS);
        List<ObjectId> parents = student.getConnectIds();
        if (parents.size() == 2) {//新数据
            return parents.get(1).toString();
        }
        return parents.get(2).toString();
    }

//    public List<UserDetailInfoDTO> findUserHuanXin(ObjectId schoolId) {
//        List<UserEntry> userEntryList = userDao.getUserEntryHuanXin(schoolId, Constant.FIELDS);
//        List<UserDetailInfoDTO> userInfoDTOList = new ArrayList<UserDetailInfoDTO>();
//        for (UserEntry userEntry : userEntryList) {
//            UserDetailInfoDTO userInfoDTO = new UserDetailInfoDTO(userEntry);
//            userInfoDTOList.add(userInfoDTO);
//        }
//        return userInfoDTOList;
//    }


    /**
     * 通过学校id查询学校的老师和校长
     *
     * @param schoolIds
     * @param fields
     * @return
     */
    public List<UserEntry> getTeacherEntryBySchoolIds(Collection<ObjectId> schoolIds, BasicDBObject fields) {
        return userDao.getTeacherEntryBySchoolIds(schoolIds, fields);
    }

    /**
     * 根据学校id查询班级中的老师、学生、家长的userId
     *
     * @param schoolId
     * @return
     */
    public Map<String, List<ObjectId>> getRoleUserIdMapBySchoolId(String schoolId) {
        Map<String, List<ObjectId>> uisMap = userDao.getTeacherIdMapBySchoolId(new ObjectId(schoolId), new BasicDBObject("r", 1));
        return uisMap;
    }

    /**
     * 更新是否领优惠券
     *
     * @param userId
     */
    public void updateUserCoupon(ObjectId userId) {
        userDao.updateUserCoupon(userId);
    }

    /**
     * 得到所有部门人员
     *
     * @param schoolId
     * @return
     */
    public Map<IdNameValuePairDTO, Set<IdNameValuePairDTO>> getDepartmemtMembersMap2(ObjectId schoolId) {
        Map<IdNameValuePairDTO, Set<IdNameValuePairDTO>> retMap = new HashMap<IdNameValuePairDTO, Set<IdNameValuePairDTO>>();

        List<DepartmentEntry> depList = departmentDao.getDepartmentEntrys(schoolId);

        List<UserEntry> userList = userDao.getTeacherEntryBySchoolId(schoolId, "", new BasicDBObject("nm", 1));

        List<ObjectId> userids = new ArrayList<ObjectId>();
        for (UserEntry user : userList) {
            userids.add(user.getID());
        }
        for (DepartmentEntry ce : depList) {
            for (ObjectId stuId : ce.getMembers()) {
                userids.remove(stuId);
            }
        }
        depList.add(new DepartmentEntry(schoolId, "未部门", "", null, userids));
        Set<ObjectId> totalSet = new HashSet<ObjectId>();

        for (DepartmentEntry ce : depList) {
            IdNameValuePairDTO dto = new IdNameValuePairDTO(ce);
            if (!retMap.containsKey(dto)) {
                retMap.put(dto, new HashSet<IdNameValuePairDTO>());
            }

            for (ObjectId stuId : ce.getMembers()) {
                retMap.get(dto).add(new IdNameValuePairDTO(stuId));
                totalSet.add(stuId);
            }
        }

        Map<ObjectId, UserEntry> userMap = userDao.getUserEntryMap(totalSet, new BasicDBObject().append("nm", 1).append("cid", 1));
        UserEntry e;
        for (Map.Entry<IdNameValuePairDTO, Set<IdNameValuePairDTO>> entry : retMap.entrySet()) {
            for (IdNameValuePairDTO dto : entry.getValue()) {
                e = userMap.get(dto.getId());
                if (null != e) {
                    dto.setValue(e.getUserName());
                }
            }
        }
        return retMap;
    }

    /**
     * 得到年级和班级的对应关系，以map返回
     *
     * @param userid
     * @return
     */
    public Map<IdNameValuePairDTO, List<IdNameValuePairDTO>> getGradeClassesMap(ObjectId userid, ObjectId shoolId) {


        Map<ObjectId, String> gradeNameMap = new HashMap<ObjectId, String>();
        SchoolEntry se = schoolDao.getOldSchoolEntry(shoolId);

        for (Grade g : se.getGradeList()) {
            gradeNameMap.put(g.getGradeId(), g.getName());
        }


        Map<IdNameValuePairDTO, List<IdNameValuePairDTO>> retMap = new HashMap<IdNameValuePairDTO, List<IdNameValuePairDTO>>();
        List<TeacherClassSubjectEntry> tcsList = teacherClassSubjectDao.getTeacherClassSubjectEntryList(userid, null, Constant.FIELDS);

        List<ObjectId> classIds = new ArrayList<ObjectId>();

        for (TeacherClassSubjectEntry tcs : tcsList) {
            classIds.add(tcs.getClassInfo().getId());
        }
        List<ClassEntry> classList = classDao.getClassEntryByIds(classIds, new BasicDBObject("gid", 1).append("nm", 1));
        IdNameValuePairDTO dto;
        for (ClassEntry ce : classList) {
            dto = new IdNameValuePairDTO(ce.getGradeId().toString(), gradeNameMap.get(ce.getGradeId()), "");
            if (!retMap.containsKey(dto)) {
                retMap.put(dto, new ArrayList<IdNameValuePairDTO>());
            }

            retMap.get(dto).add(new IdNameValuePairDTO(ce.getID().toString(), ce.getName(), ""));
        }


        return retMap;
    }


    public Map<IdNameValuePairDTO, List<IdNameValuePairDTO>> getGradeClassesMapS(ObjectId userid, ObjectId shoolId) throws JSONException {
        //加载老师所带班
        String pz = GradeAPI.getClassInfo(userid.toString(), shoolId.toString());
        JSONObject pzj = new JSONObject(pz);
        Map map1 = (Map) JsonUtil.JSONToObj(pzj.toString(), Map.class);
        List<Map<String, Object>> dtoList1 = (List<Map<String, Object>>) map1.get("message");
        //加载年级
        IdNameValuePairDTO dto;
        Map<IdNameValuePairDTO, List<IdNameValuePairDTO>> retMap = new HashMap<IdNameValuePairDTO, List<IdNameValuePairDTO>>();
//        sortGradde(dtoList1);
        int count=0;
        for (Map<String, Object> ce : dtoList1) {
            pz = GradeAPI.getGradeInfoG((String) ce.get("gradeId"));
            pzj = new JSONObject(pz);
            map1 = (Map) JsonUtil.JSONToObj(pzj.toString(), Map.class);
            Map<String, Object> map = (Map<String, Object>) map1.get("message");
            dto = new IdNameValuePairDTO(map.get("id").toString(), map.get("gradeName").toString(), "");
            dto.setNun(DateTimeUtils.getStrToLongTime((String) map.get("admissionDate"), "yyyy-MM-dd"));
            count++;
            if (!retMap.containsKey(dto)) {
                retMap.put(dto, new ArrayList<IdNameValuePairDTO>());
            }
            retMap.get(dto).add(new IdNameValuePairDTO(ce.get("id"), (String) ce.get("className"), ""));
        }
        return retMap;
    }

    public Map<IdNameValuePairDTO, List<IdNameValuePairDTO>> getGradeClassesMapSId(ObjectId shoolId) throws JSONException {
        //加载老师所带班
        String pz = GradeAPI.getClassInfo(shoolId.toString());
        JSONObject pzj = new JSONObject(pz);
        Map map1 = (Map) JsonUtil.JSONToObj(pzj.toString(), Map.class);
        List<Map<String, Object>> dtoList1 = (List<Map<String, Object>>) map1.get("message");
        //加载年级
        IdNameValuePairDTO dto;
        Map<IdNameValuePairDTO, List<IdNameValuePairDTO>> retMap = new HashMap<IdNameValuePairDTO, List<IdNameValuePairDTO>>();
//        sortGradde(dtoList1);
        int count=0;
        for (Map<String, Object> ce : dtoList1) {
            pz = GradeAPI.getGradeInfoG((String) ce.get("gradeId"));
            pzj = new JSONObject(pz);
            map1 = (Map) JsonUtil.JSONToObj(pzj.toString(), Map.class);
            Map<String, Object> map = (Map<String, Object>) map1.get("message");
            dto = new IdNameValuePairDTO(map.get("id").toString(), map.get("gradeName").toString(), "");
            dto.setNun(DateTimeUtils.getStrToLongTime((String) map.get("admissionDate"), "yyyy-MM-dd"));
            count++;
            if (!retMap.containsKey(dto)) {
                retMap.put(dto, new ArrayList<IdNameValuePairDTO>());
            }
            IdNameValuePairDTO dto1=   new IdNameValuePairDTO(ce.get("id"), (String) ce.get("className"), ce.get("classNumber"));
            retMap.get(dto).add(dto1);
//            sortListFen( retMap.get(dto));
        }
        return retMap;
    }
//    private void sortListFen(  List<IdNameValuePairDTO> scoreList) {
//        Collections.sort(scoreList, new Comparator<IdNameValuePairDTO>() {
//            @Override
//            public int compare(IdNameValuePairDTO arg0, IdNameValuePairDTO arg1) {
//                if (arg0.getNun() >  arg0.getNun())
//                    return -1;
//                if ( arg0.getNun() < arg0.getNun())
//                    return 1;
//                return 0;
//            }
//        });
//    }

    /**
     * @param schoolId
     * @return
     */
    public String getStaffUsers(ObjectId schoolId) {
        return UserAPI.getStaffUsers(schoolId.toString());
    }

    //返回年级数字列表
    public String[] getGradeType(List<GradeDTO> gradeType) {
        String[] gra = new String[gradeType.size()];
        int i = 0;
        for (GradeDTO grade : gradeType) {
            gra[i] = grade.getGradeName();
            i++;
        }
        return gra;
    }

    public String[] getSubjectList(List<SubjectDTO> subjectList) {
        String[] sub = new String[subjectList.size()];
        int i = 0;
        for (SubjectDTO subject : subjectList) {
            sub[i] = subject.getSubjectName();
            i++;
        }
        return sub;
    }


    /**
     * 导出模板
     *
     * @param response
     */
    public void addUserImp(HttpServletResponse response, String[] subjectList, String[] gradeList) throws JSONException {
        HSSFWorkbook wb = new HSSFWorkbook();
        //生成一个sheet1
        HSSFSheet sheet = wb.createSheet("用户列表");
        String[] list = new String[2];
        list[0] = "男";
        list[1] = "女";
        //性别
        CellRangeAddressList regions = new CellRangeAddressList(1, 1000, 2, 2);
        DVConstraint constraint = DVConstraint.createExplicitListConstraint(list);
        HSSFDataValidation data_validation = new HSSFDataValidation(regions, constraint);
        sheet.addValidationData(data_validation);
        //权限
        list = new String[6];
        list[0] = "老师";
        list[1] = "学生";
        list[2] = "管理员";
        list[3] = "校长";
        list[4] = "职工";
        list[5] = "其他";
        //身份
        regions = new CellRangeAddressList(1, 1000, 3, 3);
        constraint = DVConstraint.createExplicitListConstraint(list);
        data_validation = new HSSFDataValidation(regions, constraint);
        sheet.addValidationData(data_validation);
        //年级
        regions = new CellRangeAddressList(1, 1000, 6, 6);
        constraint = DVConstraint.createExplicitListConstraint(gradeList);
        data_validation = new HSSFDataValidation(regions, constraint);
        sheet.addValidationData(data_validation);
        //学科
        regions = new CellRangeAddressList(1, 1000, 5, 5);
        constraint = DVConstraint.createExplicitListConstraint(subjectList);
        data_validation = new HSSFDataValidation(regions, constraint);
        sheet.addValidationData(data_validation);

        //为sheet1生成第一行，用于放表头信息
        HSSFRow row = sheet.createRow(0);
        HSSFCell cell = row.createCell(0);
        cell.setCellValue("用户名");
        cell = row.createCell(1);
        cell.setCellValue("工号/学籍号");
        cell = row.createCell(2);
        cell.setCellValue("用户性别");
        cell = row.createCell(3);
        cell.setCellValue("用户身份");
        cell = row.createCell(4);
        cell.setCellValue("手机号");
        cell = row.createCell(5);
        cell.setCellValue("学科");
        cell = row.createCell(6);
        cell.setCellValue("年级(学生填写)");
        cell = row.createCell(7);
        cell.setCellValue("班级(学生填写:如一班填写1)");
        for (int a = 1; a <= 4000; a++) {
            row = sheet.createRow(a);
            cell = row.createCell(3);
            RichTextString textString = new HSSFRichTextString("");
            cell.setCellValue(textString);
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
            response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("用户导入模板.xls", "UTF-8"));
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


    /**
     * 预检测
     *
     * @param inputStream
     * @param type
     * @param sta
     * @param end
     * @return
     * @throws IOException
     * @throws JSONException
     */
    public List<Map<String, Object>> importUser(InputStream inputStream, Integer type, String sta, String end) throws IOException, JSONException {
        HSSFWorkbook workbook = null;
        workbook = new HSSFWorkbook(inputStream);
        HSSFSheet UserSheet = workbook.getSheet("用户列表");
        int rowNum = UserSheet.getLastRowNum();
        List<Map<String, Object>> dtos = new ArrayList<Map<String, Object>>();
        for (int i = 1; i <= rowNum; i++) {
            Map<String, Object> dto = new HashMap<String, Object>();
            //身份
            HSSFCell cell = UserSheet.getRow(i).getCell(0);
            //姓名
            if (getStringCellValue(cell) != "") {
                String name = UserSheet.getRow(i).getCell(0).getStringCellValue();
                dto.put("name", name);
            } else {
                continue;
            }
            //工号学籍号
            cell = UserSheet.getRow(i).getCell(1);
            if (getStringCellValue(cell) != "") {
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                String son = UserSheet.getRow(i).getCell(1).getStringCellValue();
                dto.put("son", son);
            } else {
                dto.put("son", "");
            }
            //性别
            cell = UserSheet.getRow(i).getCell(2);
            if (getStringCellValue(cell) != "") {
                String sex = UserSheet.getRow(i).getCell(2).getStringCellValue();
                dto.put("sex", sex.equals("男") ? 1 : 0);
            } else {
                continue;
            }

            cell = UserSheet.getRow(i).getCell(3);
            if (getStringCellValue(cell) != "") {
                String role = UserSheet.getRow(i).getCell(3).getStringCellValue();
                dto.put("role", role);
            }
            //手机
            cell = UserSheet.getRow(i).getCell(4);
            if (getStringCellValue(cell) != "") {
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                String mn = UserSheet.getRow(i).getCell(4).getStringCellValue();
                dto.put("mn", mn);
            } else {
                dto.put("mn", "");
            }
            //学科
            cell = UserSheet.getRow(i).getCell(5);
            if (getStringCellValue(cell) != "") {
                String sub = UserSheet.getRow(i).getCell(5).getStringCellValue();
                dto.put("sub", sub);
            } else {
                dto.put("sub", "");
            }
            //年级
            cell = UserSheet.getRow(i).getCell(6);
            if (getStringCellValue(cell) != "") {
                String gra = UserSheet.getRow(i).getCell(6).getStringCellValue();
                dto.put("gra", gra);
            } else {
                dto.put("gra", "");
            }
            //班级
            cell = UserSheet.getRow(i).getCell(7);
            if (getStringCellValue(cell) != "") {
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                String cls = UserSheet.getRow(i).getCell(7).getStringCellValue();
                dto.put("cls", cls);
            } else {
                dto.put("cls", "");
            }
            //登陆名
            if (type == 0) {
                dto.put("logn", sta + "" + dto.get("name") + "" + end);
            } else {
                String lognName = (String) dto.get("name");
                String losName = (String) dto.get("name");
                Integer count = getRightUserNameCount(lognName);
                for (Map<String, Object> map : dtos) {
                    String logName = (String) map.get("name");
                    if (losName.equals(logName)) {
                        count++;
                        if (count == 2 || count == 44 || count == 24 || count == 34 || count == 54 || count == 64 || count == 74 || count == 84 || count == 94 || count == 4 || count == 13 || count == 14 || count == 38 || count == 250) {
                            count++;
                        }
                    }
                }
                if (count != 0) {
                    lognName = lognName + String.valueOf(count);
                }
                dto.put("logn", lognName);
            }
            dtos.add(dto);
        }
        return getJianCha(dtos, type);
    }


    public String importUser(InputStream inputStream) throws IOException, JSONException {
        HSSFWorkbook workbook = null;
        workbook = new HSSFWorkbook(inputStream);
        HSSFSheet UserSheet = workbook.getSheet("用户列表");
        int rowNum = UserSheet.getLastRowNum();
        List<Map<String, Object>> dtos = new ArrayList<Map<String, Object>>();
        for (int i = 1; i <= rowNum; i++) {
            Map<String, Object> dto = new HashMap<String, Object>();
            //身份
            HSSFCell cell = UserSheet.getRow(i).getCell(3);
            if (getStringCellValue(cell) != "") {
                String role = UserSheet.getRow(i).getCell(3).getStringCellValue();
                dto.put("role", role);
            }
            //学科
            cell = UserSheet.getRow(i).getCell(5);
            if (getStringCellValue(cell) != "") {
                String sub = UserSheet.getRow(i).getCell(5).getStringCellValue();
                dto.put("sub", sub);
            } else {
                dto.put("sub", "");
            }
            //年级
            cell = UserSheet.getRow(i).getCell(6);
            if (getStringCellValue(cell) != "") {
                String gra = UserSheet.getRow(i).getCell(6).getStringCellValue();
                dto.put("gra", gra);
            } else {
                dto.put("gra", "");
            }
            //班级
            cell = UserSheet.getRow(i).getCell(7);
            if (getStringCellValue(cell) != "") {
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                String cls = UserSheet.getRow(i).getCell(7).getStringCellValue();
                dto.put("cls", cls);
            } else {
                dto.put("cls", "");
            }
            //性别
            cell = UserSheet.getRow(i).getCell(2);
            if (getStringCellValue(cell) != "") {
                String sex = UserSheet.getRow(i).getCell(2).getStringCellValue();
                dto.put("sex", sex.equals("男") ? 1 : 0);
            } else {
                continue;
            }
            dtos.add(dto);
        }
        return getWanZheng(dtos);
    }

    /**
     * 信息完整性检测
     */
    public String getWanZheng(List<Map<String, Object>> dots) {
        String st = "";
        Integer it = 2;
        for (Map<String, Object> maps : dots) {
            if (maps.get("role").equals("老师")) {
//                if (maps.get("sub").equals("")) {
//                    st += it + ",";
//                }
            }
            if (maps.get("role").equals("学生")) {
                if (maps.get("gra").equals("") || maps.get("cls").equals("")) {
                    st += it + ",";
                }
            }
            it++;
        }
        if (st.equals("")) {
            st = "ok";
        }
        return st;
    }

    /**
     * 重名检测
     *
     * @param dots
     * @return
     */
    public List<Map<String, Object>> getJianCha(List<Map<String, Object>> dots, Integer type) {
        //重复的
        List<Map<String, Object>> reUserList = new ArrayList<Map<String, Object>>();
        //所有登录名
        List<String> logName = new ArrayList<String>();
        int count = 0;
        //获得所有登录名
        for (Map<String, Object> maps : dots) {
            String nm = (String) maps.get("logn");
            logName.add(nm.toLowerCase());
        }
        //数据库是否存在重名的
        List<UserEntry> e = userDao.searchUserByUserName(logName);
        List<UserEntry> entries = userDao.searchUserByUserNames(logName);
        //检测导入对象重名情况
        if (dots.size() > 1) {
            for (Map<String, Object> map : dots) {
                int rcount = 0;
                Boolean bf = true;
                for (Map<String, Object> maps : dots) {
                    if (rcount == count) {
                        rcount++;
                        continue;
                    } else {
                        if (!map.get("mn").equals("")) {
                            String mn = (String) map.get("mn");
                            boolean md = ValidationUtils.isRequestModile(mn);
                            if (map.get("logn").equals(maps.get("logn")) || map.get("mn").equals(maps.get("mn"))) {
                                reUserList.add(maps);
                                bf = true;
                                break;
                            } else if (md == false) {
                                bf = false;
                            }
                        } else {
                            if (map.get("logn").equals(maps.get("logn"))) {
                                reUserList.add(maps);
                            }
                        }
                        rcount++;
                    }
                }
                if (bf == false) {
                    reUserList.add(map);
                }
                count++;
            }
        }
        if (e != null) {
            for (UserEntry en : e) {
                for (Map<String, Object> maps : dots) {
                    boolean xun = true;
                    if (en.getLoginName().equals(maps.get("logn"))) {
                        if (reUserList.size() != 0) {
                            for (Map<String, Object> mt : reUserList) {
                                if (mt.get("logn").equals(maps.get("logn")) &&
                                        mt.get("mn").equals(maps.get("mn")) &&
                                        mt.get("name").equals(maps.get("name")) &&
                                        mt.get("sex").equals(maps.get("sex"))) {
                                    xun = false;
                                    break;
                                } else {
                                    xun = true;
                                }
                            }
                        } else {
                            reUserList.add(maps);
                            xun = false;
                        }
                        if (xun == false) {
                            break;
                        } else {
                            reUserList.add(maps);
                        }
                    }
                }
            }
        }
        if (entries != null) {
            for (UserEntry en : entries) {
                for (Map<String, Object> maps : dots) {
                    boolean xun = true;
                    if (en.getRealUserName().equals(maps.get("logn"))) {
                        if (reUserList.size() != 0) {
                            for (Map<String, Object> mt : reUserList) {
                                if (mt.get("logn").equals(maps.get("logn")) &&
                                        mt.get("mn").equals(maps.get("mn")) &&
                                        mt.get("name").equals(maps.get("name")) &&
                                        mt.get("sex").equals(maps.get("sex"))) {
                                    xun = false;
                                    break;
                                } else {
                                    xun = true;
                                }
                            }
                        } else {
                            reUserList.add(maps);
                            xun = false;
                        }
                        if (xun == false) {
                            break;
                        } else {
                            reUserList.add(maps);
                        }
                    }
                }
            }
        }
        //手机号
        List<String> mds = new ArrayList<String>();
        for (Map<String, Object> maps : dots) {
            String mn = (String) maps.get("mn");
            if (!mn.equals("")) {
                mds.add((String) maps.get("mn"));
            }
        }
        List<UserEntry> es = userDao.searchUserByUserMn(mds);
        if (es != null && es.size() > 0) {
            for (UserEntry en : es) {
                for (Map<String, Object> maps : dots) {
                    boolean xun = true;
                    if (en.getMobileNumber().equals(maps.get("mn"))) {
                        if (reUserList.size() != 0) {
                            for (Map<String, Object> mt : reUserList) {
                                if (mt.get("logn").equals(maps.get("logn")) &&
                                        mt.get("mn").equals(maps.get("mn")) &&
                                        mt.get("name").equals(maps.get("name")) &&
                                        mt.get("sex").equals(maps.get("sex"))) {
                                    xun = false;
                                    break;
                                } else {
                                    xun = true;
                                }
                            }
                        } else {
                            reUserList.add(maps);
                            xun = false;
                        }
                        if (xun == false) {
                            break;
                        } else {
                            reUserList.add(maps);
                        }
                    }
                }
            }
        }
        return reUserList;
    }

    private String getStringCellValue(HSSFCell cell) {
        if (cell == null) return Constant.EMPTY;
        String strCell;

        switch (cell.getCellType()) {
            case HSSFCell.CELL_TYPE_STRING:
                strCell = cell.getStringCellValue();
                break;
            case HSSFCell.CELL_TYPE_NUMERIC:
                strCell = String.valueOf(cell.getNumericCellValue());
                break;
            case HSSFCell.CELL_TYPE_BOOLEAN:
                strCell = String.valueOf(cell.getBooleanCellValue());
                break;
            default:
                strCell = Constant.EMPTY;
                break;
        }

        return StringUtils.isBlank(strCell) ? Constant.EMPTY : strCell;
    }


    /**
     * 导入数据
     *
     * @param inputStream
     * @param type
     * @param sta
     * @param end
     * @return
     * @throws IOException
     * @throws JSONException
     */
    public String importUserList(InputStream inputStream, Integer type, String sta, String end, String sid) throws IOException, JSONException {
        HSSFWorkbook workbook = null;
        workbook = new HSSFWorkbook(inputStream);
        HSSFSheet UserSheet = workbook.getSheet("用户列表");
        int rowNum = UserSheet.getLastRowNum();
        List<Map<String, Object>> dtos = new ArrayList<Map<String, Object>>();
        for (int i = 1; i <= rowNum; i++) {
            Map<String, Object> dto = new HashMap<String, Object>();
            //身份
            HSSFCell cell = UserSheet.getRow(i).getCell(0);
            //姓名
            if (getStringCellValue(cell) != "") {
                String name = UserSheet.getRow(i).getCell(0).getStringCellValue();
                dto.put("name", name);
            } else {
                continue;
            }
            //工号学籍号
            cell = UserSheet.getRow(i).getCell(1);
            if (getStringCellValue(cell) != "") {
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                String son = UserSheet.getRow(i).getCell(1).getStringCellValue();
                dto.put("son", son);
            } else {
                dto.put("son", "");
            }
            //性别
            cell = UserSheet.getRow(i).getCell(2);
            if (getStringCellValue(cell) != "") {
                String sex = UserSheet.getRow(i).getCell(2).getStringCellValue();
                dto.put("sex", sex.equals("男") ? 1 : 0);
            } else {
                continue;
            }

            cell = UserSheet.getRow(i).getCell(3);
            if (getStringCellValue(cell) != "") {
                String role = UserSheet.getRow(i).getCell(3).getStringCellValue();
                dto.put("role", role);
            }
            //手机
            cell = UserSheet.getRow(i).getCell(4);
            if (getStringCellValue(cell) != "") {
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                String mn = UserSheet.getRow(i).getCell(4).getStringCellValue();
                dto.put("mn", mn);
            } else {
                dto.put("mn", "");
            }
            //学科
            cell = UserSheet.getRow(i).getCell(5);
            if (getStringCellValue(cell) != "") {
                String sub = UserSheet.getRow(i).getCell(5).getStringCellValue();
                dto.put("sub", sub);
            } else {
                dto.put("sub", "");
            }
            //年级
            cell = UserSheet.getRow(i).getCell(6);
            if (getStringCellValue(cell) != "") {
                String gra = UserSheet.getRow(i).getCell(6).getStringCellValue();
                dto.put("gra", gra);
            } else {
                dto.put("gra", "");
            }
            //班级
            cell = UserSheet.getRow(i).getCell(7);
            if (getStringCellValue(cell) != "") {
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                String cls = UserSheet.getRow(i).getCell(7).getStringCellValue();
                dto.put("cls", cls);
            } else {
                dto.put("cls", "");
            }
            //登陆名
            if (type == 0) {
                String lognName = sta + "" + dto.get("name") + "" + end;
                String losName = sta + "" + dto.get("name") + "" + end;
                Integer count = getRightUserNameCount(lognName);
                for (Map<String, Object> map : dtos) {
                    String logName = (String) map.get("logn");
                    if (losName.equals(logName)) {
                        count++;
                        if (count == 2 || count == 44 || count == 24 || count == 34 || count == 54 || count == 64 || count == 74 || count == 84 || count == 94 || count == 4 || count == 13 || count == 14 || count == 38 || count == 250) {
                            count++;
                        }
                    }
                }
                if (count != 0) {
                    lognName = lognName + String.valueOf(count);
                }
                dto.put("logn", lognName);
                if (dto.get("mn").equals("")) {
                } else {
                    Boolean bf = ValidationUtils.isRequestModile((String) dto.get("mn"));
                    if (bf == false) {
                        return "请输入正确的手机号!";
                    }
                }
            } else {
                String lognName = (String) dto.get("name");
                String losName = (String) dto.get("name");
                Integer count = getRightUserNameCount(lognName);
                for (Map<String, Object> map : dtos) {
                    String logName = (String) map.get("name");
                    if (losName.equals(logName)) {
                        count++;
                        if (count == 2 || count == 44 || count == 24 || count == 34 || count == 54 || count == 64 || count == 74 || count == 84 || count == 94 || count == 4 || count == 13 || count == 14 || count == 38 || count == 250) {
                            count++;
                        }
                    }
                }
                if (count != 0) {
                    lognName = lognName + String.valueOf(count);
                }
                dto.put("logn", lognName);
                if (dto.get("mn").equals("")) {
                } else {
                    boolean md = ValidationUtils.isRequestModile((String) dto.get("mn"));
                    if (md == false) {
                        return "请输入正确的手机号!";
                    }
                }
            }
            dto.put("sid", sid);
            dtos.add(dto);
        }
        List<Map<String, Object>> list = getJianCha(dtos, type);
        if (list.size() > 0) {
            String md = "";
            for (Map<String, Object> mt : list) {
                md += mt.get("name") + ",";
            }
            return "导入的数据当中存在被注册的手机号:(" + md + ")";
        } else {
            String lt = NoticeBaseAPI.addOrUdpUserBase(dtos);
            JSONObject pzj = new JSONObject(lt);
            Map map1 = (Map) JsonUtil.JSONToObj(pzj.toString(), Map.class);
            List<Map<String, Object>> dtoList1 = (List<Map<String, Object>>) map1.get("message");
            if (dtoList1.size() > 0) {
                return lt;
            } else {
                return "ok";
            }
        }
    }

    private Integer getRightUserNameCount(String name) {
//        UserEntry e = userDao.searchUserByLognName(name);
        UserEntry e1 = userDao.searchUserByUserName(name);
//        && e == null
        if (e1 == null) {
            return 0;
        }
//        if (e == null && e1 != null) {
//            for (int i = 1; i < Integer.MAX_VALUE; i++) {
//                if (i == 2 || i == 44 || i == 24 || i == 34 || i == 54 || i == 64 || i == 74 || i == 84 || i == 94 || i == 4 || i == 13 || i == 14 || i == 38 || i == 250) {
//                    continue;
//                }
//                UserEntry en = userDao.searchUserByUserName(e1.getUserName() + "" + i);
//                if (en != null) {
//                    en.setLoginName(name + "" + i);
//                    en.setUserName(name);
//                    userDao.update(en.getID(), en);
//                }
//                if (en == null) {
//                    break;
//                }
//            }
//        }
        for (int i = 1; i < Integer.MAX_VALUE; i++) {
            if (i == 2 || i == 44 || i == 24 || i == 34 || i == 54 || i == 64 || i == 74 || i == 84 || i == 94 || i == 4 || i == 13 || i == 14 || i == 38 || i == 250) {
                continue;
            }
            String newName = name + String.valueOf(i);

            if (name.indexOf("4") >= 0) {
                continue;
            }

//            e = userDao.searchUserByLognName(newName);
            UserEntry en = userDao.searchUserByUserName(newName);
//            if (e == null && en == null) {
//                return i;
//            } else {
//                if(en!=null) {
//                    en.setLoginName(newName);
//                    en.setUserName(name);
//                    userDao.update(en.getID(), en);
//                }
//            }
            if (en == null) {
                return i;
            }
        }
        return null;
    }


    public void upsUserImp(HttpServletResponse response, String[] subjectList, String[] gradeList, List<Map<String, Object>> lists) throws JSONException {
        HSSFWorkbook wb = new HSSFWorkbook();
        //生成一个sheet1
        HSSFSheet sheet = wb.createSheet("用户列表");
        String[] list = new String[2];
        list[0] = "男";
        list[1] = "女";
        //性别
        CellRangeAddressList regions = new CellRangeAddressList(1, 1000, 2, 2);
        DVConstraint constraint = DVConstraint.createExplicitListConstraint(list);
        HSSFDataValidation data_validation = new HSSFDataValidation(regions, constraint);
        sheet.addValidationData(data_validation);
        //权限
        list = new String[6];
        list[0] = "老师";
        list[1] = "学生";
        list[2] = "管理员";
        list[3] = "校长";
        list[4] = "职工";
        list[5] = "其他";
        //身份
        regions = new CellRangeAddressList(1, 1000, 3, 3);
        constraint = DVConstraint.createExplicitListConstraint(list);
        data_validation = new HSSFDataValidation(regions, constraint);
        sheet.addValidationData(data_validation);
        //年级
        regions = new CellRangeAddressList(1, 1000, 6, 6);
        constraint = DVConstraint.createExplicitListConstraint(gradeList);
        data_validation = new HSSFDataValidation(regions, constraint);
        sheet.addValidationData(data_validation);
        //学科
        regions = new CellRangeAddressList(1, 1000, 5, 5);
        constraint = DVConstraint.createExplicitListConstraint(subjectList);
        data_validation = new HSSFDataValidation(regions, constraint);
        sheet.addValidationData(data_validation);

        //为sheet1生成第一行，用于放表头信息
        HSSFRow row = sheet.createRow(0);
        HSSFCell cell = row.createCell(0);
        cell.setCellValue("用户名");
        cell = row.createCell(1);
        cell.setCellValue("工号/学籍号");
        cell = row.createCell(2);
        cell.setCellValue("用户性别");
        cell = row.createCell(3);
        cell.setCellValue("用户身份");
        cell = row.createCell(4);
        cell.setCellValue("手机号");
        cell = row.createCell(5);
        cell.setCellValue("学科");
        cell = row.createCell(6);
        cell.setCellValue("年级(学生填写)");
        cell = row.createCell(7);
        cell.setCellValue("班级(学生填写:如一班填写1)");
        for (int a = 1; a <= 4000; a++) {
            row = sheet.createRow(a);
            cell = row.createCell(3);
            RichTextString textString = new HSSFRichTextString("");
            cell.setCellValue(textString);
        }
        int i = 1;
        for (Map<String, Object> mp : lists) {
            row = sheet.createRow(i);
            cell = row.createCell(0);
            cell.setCellValue((String) mp.get("name"));
            cell = row.createCell(1);
            cell.setCellValue((String) mp.get("son"));
            cell = row.createCell(2);
            cell.setCellValue((Integer) mp.get("sex") == 0 ? "女" : "男");
            cell = row.createCell(3);
            cell.setCellValue((String) mp.get("role"));
            cell = row.createCell(4);
            cell.setCellValue((String) mp.get("mn"));
            cell = row.createCell(5);
            cell.setCellValue((String) mp.get("sub"));
            cell = row.createCell(6);
            cell.setCellValue((String) mp.get("gra"));
            cell = row.createCell(7);
            cell.setCellValue((String) mp.get("cls"));
            i++;
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
            response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("这些账户存在问题未被导入，请处理后重试.xls", "UTF-8"));
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


    public List<UserEntry> getUserEntryListByRole(String role, ObjectId sid) {
        List<UserEntry> entries = null;
        if (role.equals("all")) {
            entries = userDao.getUserEntryBySchoolIdList(sid, new BasicDBObject("nm", -1).append("logn", -1).append("r", -1).append("sex", -1).append("mn", -1).append("jnb", -1).append("sn", -1));
        }
        if (role.equals("学生")) {
            entries = userDao.getUserList(sid, new BasicDBObject("nm", -1).append("logn", -1).append("r", -1).append("sex", -1).append("mn", -1).append("jnb", -1).append("sn", -1), UserRole.STUDENT.getRole());
        }
        if (role.equals("职工")) {
            entries = userDao.getUserList(sid, new BasicDBObject("nm", -1).append("logn", -1).append("r", -1).append("sex", -1).append("mn", -1).append("jnb", -1).append("sn", -1), UserRole.STAFF.getRole());
        }
        if (role.equals("管理员")) {
            entries = userDao.getUserList(sid, new BasicDBObject("nm", -1).append("logn", -1).append("r", -1).append("sex", -1).append("mn", -1).append("jnb", -1).append("sn", -1), UserRole.ADMIN.getRole());
        }
        if (role.equals("校长")) {
            entries = userDao.getUserList(sid, new BasicDBObject("nm", -1).append("logn", -1).append("r", -1).append("sex", -1).append("mn", -1).append("jnb", -1).append("sn", -1), UserRole.HEADMASTER.getRole());
        }
        if (role.equals("其他")) {
            entries = userDao.getUserList(sid, new BasicDBObject("nm", -1).append("logn", -1).append("r", -1).append("sex", -1).append("mn", -1).append("jnb", -1).append("sn", -1), UserRole.OTHER.getRole());
        }
        if (role.equals("老师")) {
            entries = userDao.getUserList(sid, new BasicDBObject("nm", -1).append("logn", -1).append("r", -1).append("sex", -1).append("mn", -1).append("jnb", -1).append("sn", -1), UserRole.TEACHER.getRole());
        }
        return entries;
    }


    public void addUserImp(HttpServletResponse response, List<UserEntry> entries) throws JSONException {
        HSSFWorkbook wb = new HSSFWorkbook();
        //生成一个sheet1
        HSSFSheet sheet = wb.createSheet("用户列表");
        //为sheet1生成第一行，用于放表头信息
        HSSFRow row = sheet.createRow(0);
        HSSFCell cell = row.createCell(0);
        cell.setCellValue("用户名");
        cell = row.createCell(1);
        cell.setCellValue("姓名");
        cell = row.createCell(2);
        cell.setCellValue("工号/学籍号");
        cell = row.createCell(3);
        cell.setCellValue("用户性别");
        cell = row.createCell(4);
        cell.setCellValue("用户身份");
        cell = row.createCell(5);
        cell.setCellValue("手机号");
        int i = 1;
        for (UserEntry mp : entries) {
            row = sheet.createRow(i);
            cell = row.createCell(0);
            cell.setCellValue(mp.getLoginName().equals("") ? mp.getRealUserName() : mp.getLoginName());
            cell = row.createCell(1);
            cell.setCellValue(mp.getUserName());
            cell = row.createCell(2);
            cell.setCellValue(UserRole.isStudent(mp.getRole()) ? mp.getStudyNum() : mp.getJobnumber());
            cell = row.createCell(3);
            cell.setCellValue(mp.getSex() == 0 ? "女" : "男");
            cell = row.createCell(4);
            cell.setCellValue(UserRole.getRoleDescription(mp.getRole()));
            cell = row.createCell(5);
            cell.setCellValue(mp.getMobileNumber());
            i++;
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
            response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("用户导入模板.xls", "UTF-8"));
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


    /**
     * @param userId
     * @return
     */
    public String getUserById(ObjectId userId) {
        return UserAPI.getUserInfoById(userId.toString());
    }

    /**
     * @param userId
     * @param userName
     * @param telphone
     * @param email
     * @return
     */
    public String updateUserInfo(ObjectId userId, String userName, String telphone, String email, int sex) {
        return UserAPI.updateUserInfo(userId.toString(), userName, telphone, email, sex);
    }

    /**
     * @param userId
     * @param orgpwd
     * @param pwd
     * @return
     */
    public String updatePassword(ObjectId userId, String orgpwd, String pwd) {
        return UserAPI.updatePassword(userId.toString(), orgpwd, pwd);
    }

    public String getUsersByUserName(ObjectId schoolId, String keyword) {
        return UserAPI.getUsersByUserName(schoolId.toString(),keyword);
    }

    public UserEntry searchUserByJNB(String name) {
        return userDao.searchUserByJNB(name);
    }

    public UserEntry searchUserByStuCode(String name) {
        return userDao.searchUserByStuCode(name);
    }
}
