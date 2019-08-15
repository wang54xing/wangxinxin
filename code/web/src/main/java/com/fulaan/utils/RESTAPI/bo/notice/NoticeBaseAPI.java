package com.fulaan.utils.RESTAPI.bo.notice;

import com.fulaan.homeschool.dto.FileDTO;
import com.fulaan.schoolbase.dto.*;
import com.fulaan.utils.RESTAPI.base.BaseAPI;
import com.pojo.utils.MongoUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by guojing on 2017/3/2.
 */
public class NoticeBaseAPI extends BaseAPI {


    /***************************************文件开始***********************************************/
    /**
     * 获取目录下的文件
     *
     * @param dirId
     * @return
     */
    public static String getFolderList(String dirId, int sort) {
        String resoureUrl = "/files/get/" + dirId + "/" + sort;
        String resultStr = getForObject(resoureUrl);
        return resultStr;
    }

    /**
     * 文件目录ID
     *
     * @param schoolId
     * @param userId
     * @param platform
     * @param type     1校级文档  2 我的文档
     */
    public static String getFolderDir(ObjectId schoolId, ObjectId userId, int platform, int type) {
        String resoureUrl = "/files/get/" + schoolId.toString() + "/" + userId.toString() + "/" + platform + "/" + type;
        String resultStr = getForObject(resoureUrl);
        return resultStr;
    }

    /**
     * 添加文件
     *
     * @param fileDto
     */
    public static void addFile(FileDTO fileDto) {
        String resoureUrl = "/files/addFiles";
        postForObject(resoureUrl, fileDto);
    }

    /**
     * 改名
     *
     * @param dto
     * @return
     */
    public static String fileRename(FileDTO dto) {
        String resoureUrl = "/files/rename";
        String resultStr = postForObject(resoureUrl, dto);
        return resultStr;
    }

    /**
     * 删除文件
     *
     * @param fileIds
     */
    public static void delFileByIds(String fileIds) {
        String resoureUrl = "/files/delete/" + fileIds;
        delete(resoureUrl);
    }

    /**
     * 关键字检索
     *
     * @param schoolId
     * @param fileId
     * @param keyword
     * @return
     */
    public static String getFileByKeyWord(ObjectId schoolId, String fileId, String keyword) {
        String resoureUrl = "/files/get/" + schoolId.toString() + "/" + fileId + "/" + keyword;
        String resultStr = getForObject(resoureUrl);
        return resultStr;
    }

    /**
     * 检查文件目录下是否有文件
     *
     * @param fileIds
     * @return
     */
    public static String getFilesByParentIds(String fileIds) {
        String resoureUrl = "/files/get/" + fileIds;
        String resultStr = getForObject(resoureUrl);
        return resultStr;
    }

    /**
     * 检查文件目录下是否有文件
     *
     * @param fileIds
     * @return
     */
    public static String getFilesByFileIds(String fileIds) {
        String resoureUrl = "/files/get/files/" + fileIds;
        String resultStr = getForObject(resoureUrl);
        return resultStr;
    }

    /**
     * 添加学校
     *
     * @param schoolDTO
     * @return
     */
    public static String addSchool(SchoolDTO schoolDTO) {
        String resoureUrl = "/school/post";
        String resultStr = postForObject(resoureUrl, schoolDTO);
        return resultStr;
    }

    /**
     * 学校列表
     *
     * @param id
     * @return
     */
    public static String getSchool(ObjectId id) {
        String resoureUrl = "/school/id/" + id;
        String resultStr = getForObject(resoureUrl);
        return resultStr;
    }


    /**
     * 删除学校
     *
     * @param id
     */
    public static void delSchool(ObjectId id) {
        String resoureUrl = "/school/delete/" + id;
        delete(resoureUrl);
    }

    /**
     * 更新学校
     *
     * @param schoolDTO
     */
    public static void updateSchool(SchoolDTO schoolDTO) {
        String resoureUrl = "/school/put";
        put(resoureUrl, schoolDTO);
    }

    /**
     * 添加教学楼
     *
     * @param dto
     */
    public static String addSchoolLoop(SchoolLoopDTO dto) {
        String resoureUrl = "/schoolLoop/post";
        String resultStr = postForObject(resoureUrl, dto);
        return resultStr;
    }

    /**
     * 删除教学楼
     *
     * @param id
     */
    public static void delSchoolLoop(ObjectId id) {
        String resoureUrl = "/schoolLoop/delete/" + id;
        delete(resoureUrl);
    }

    /**
     * 查询教学楼
     *
     * @param id
     * @return
     */
    public static String getSchoolLoop(ObjectId id) {
        String resoureUrl = "/schoolLoop/id/" + id;
        String resultStr = getForObject(resoureUrl);
        return resultStr;
    }

    /**
     * 查询教学楼列表
     *
     * @param schoolId
     * @return
     */
    public static String getSchoolLoopList(ObjectId schoolId) {
        String resoureUrl = "/schoolLoop/get/" + schoolId;
        String resultStr = getForObject(resoureUrl);
        return resultStr;
    }

    /**
     * 跟新教学楼
     *
     * @param dto
     */
    public static void updSchoolLoop(SchoolLoopDTO dto) {
        String resoureUrl = "/schoolLoop/put";
        put(resoureUrl, dto);
    }

    /**
     * 添加教室
     *
     * @param dto
     * @return
     */
    public static String addClassRoom(ClassRoomDTO dto) {
        String resoureUrl = "/classroom/post";
        String resultStr = postForObject(resoureUrl, dto);
        return resultStr;
    }

    /**
     * 更新教室
     *
     * @param dto
     */
    public static void updClassRoom(ClassRoomDTO dto) {
        String resoureUrl = "/classroom/put";
        put(resoureUrl, dto);
    }

    /**
     * 删除教室
     *
     * @param id
     */
    public static void delClassRoom(ObjectId id) {
        String resoureUrl = "/classroom/delete/" + id;
        delete(resoureUrl);
    }

    /**
     * 获取单条教室
     *
     * @param id
     * @return
     */
    public static String getClassRoom(ObjectId id) {
        String resoureUrl = "/classroom/id/" + id;
        String resultStr = getForObject(resoureUrl);
        return resultStr;
    }

    /**
     * 查询教学楼下的教室
     *
     * @param loopId
     * @return
     */
    public static String getClassRoomList(ObjectId loopId, int page, int pageSize) {
        String resoureUrl = "/classroom/get/" + loopId + "/" + page + "/" + pageSize;
        String resultStr = getForObject(resoureUrl);
        return resultStr;
    }

    
    
    
    /**
     * 添加课表
     *
     * @param dto
     * @return
     */
    public static String addTimeTable(TimeTableDTO dto) {
        String resoureUrl = "/schedule/post";
        String resultStr = postForObject(resoureUrl, dto);
        return resultStr;
    }

    /**
     * 更新课表
     *
     * @param dto
     */
    public static void updTimeTable(TimeTableDTO dto) {
        String resoureUrl = "/schedule/put";
        put(resoureUrl, dto);
    }

    /**
     * 删除课表
     *
     * @param id
     */
    public static void delTimeTable(ObjectId id) {
        String resoureUrl = "/schedule/delete/" + id;
        delete(resoureUrl);
    }

    /**
     * 查询课表
     *
     * @param id
     * @return
     */
    public static String getTimeTable(ObjectId id) {
        String resoureUrl = "/schedule/id/" + id;
        String resultStr = getForObject(resoureUrl);
        return resultStr;
    }

    /**
     * 查询课表s
     *
     * @param schoolId
     * @return
     */
    public static String getTimeTableList(ObjectId schoolId) {
        String resoureUrl = "/schedule/get/" + schoolId;
        String resultStr = getForObject(resoureUrl);
        return resultStr;
    }

    /**
     * 添加编辑学期
     *
     * @param dto
     * @return
     */
    public static String addOrUdpTerm(TermDTO dto) {
        String resoureUrl = "/term/post";
        String resultStr = postForObject(resoureUrl, dto);
        return resultStr;
    }

    /**
     * 删除学期
     *
     * @param id
     */
    public static void delTerm(ObjectId id) {
        String resoureUrl = "/term/delete/" + id;
        delete(resoureUrl);
    }

    /**
     * 查询学期
     *
     * @param id
     * @return
     */
    public static String getTerm(ObjectId id) {
        String resoureUrl = "/term/get/" + id;
        String resultStr = getForObject(resoureUrl);
        return resultStr;
    }

    /**
     * 查询学期列表
     *
     * @param schoolId
     * @param keyword
     * @param page
     * @param pageSize
     * @return
     */
    public static String getTermList(ObjectId schoolId, String keyword, int page, int pageSize) {
        if (StringUtils.isEmpty(keyword)) {
            keyword = "keyword";
        }
        String resoureUrl = "/term/get/" + schoolId + "/" + page + "/" + pageSize + "/" + keyword;
        String resultStr = getForObject(resoureUrl);
        return resultStr;
    }

    /**
     * 查询学校及分校
     *
     * @param schoolId
     * @return
     */
    public static String selSchoolList(ObjectId schoolId, String keyword) {
        if (StringUtils.isEmpty(keyword)) {
            keyword = "keyword";
        }
        String resoureUrl = "/school/all/" + schoolId + "/" + keyword;
        String resultStr = getForObject(resoureUrl);
        return resultStr;
    }

    /**
     * 添加老师信息
     *
     * @param dto
     * @return
     */
    public static String addOrUdpTeacherBase(ResumeDTO dto) {
        String resoureUrl = "/teacher/post";
        String resultStr = postForObject(resoureUrl, dto);
        return resultStr;
    }

    public static String addStaffBase(ResumeDTO dto) {
        String resoureUrl = "/staff/post";
        String resultStr = postForObject(resoureUrl, dto);
        return resultStr;
    }

    public static String addOrUdpTeacherBase(List<ResumeDTO> dto) {
        String resoureUrl = "/teacher/post/list";
        String resultStr = postForObject(resoureUrl, dto);
        return resultStr;
    }

    public static String addOrUdpUserBase(List<Map<String,Object>> dto) {
        String resoureUrl = "/users/post/list";
        String resultStr = postForObject(resoureUrl, dto);
        return resultStr;
    }

    /**
     * 添加学科
     *
     * @param dto
     * @return
     */
    public static String addOrUdpSubject(SubjectDTO dto) {
        String resoureUrl = "/subject/post";
        String resultStr = postForObject(resoureUrl, dto);
        return resultStr;
    }

    /**
     * 删除学科
     *
     * @param id
     */
    public static void delSubject(ObjectId id) {
        String resoureUrl = "/subject/delete/" + id;
        delete(resoureUrl);
    }

    /**
     * 检查用户名是否存在
     *
     * @param schoolId
     * @param userName
     * @return
     */
    public static String checkUserName(ObjectId schoolId, String userName) {
        String resoureUrl = "/users/get/" + schoolId + "/" + userName;
        String resultStr = getForObject(resoureUrl);
        return resultStr;
    }

    /**
     * 查询学科列表
     *
     * @param schoolId
     * @return
     */
    public static String getSubjectList(ObjectId schoolId) {
        String resoureUrl = "/subject/get/" + schoolId;
        String resultStr = getForObject(resoureUrl);
        return resultStr;
    }


    public static String getSubjectInfos(String schoolId) {
        String resoureUrl = "/subject/get/1/" + schoolId;
        String resultStr = getForObject(resoureUrl);
        return resultStr;
    }

    /**
     * @param id
     * @param schoolId
     * @param keyword
     * @return
     */
    public static String getSubject(ObjectId id, ObjectId schoolId, String keyword, int page, int pageSize) {
        if (StringUtils.isEmpty(keyword)) {
            keyword = "keyword";
        }
        String resoureUrl = "/subject/get/" + id + "/" + schoolId + "/" + keyword + "/" + page + "/" + pageSize;
        return getForObject(resoureUrl);
    }

    /**
     * 添加老师工作经历
     *
     * @param dto
     * @return
     */
    public static String addTeacherJob(JobDTO dto) {
        String resoureUrl = "/teacher/job/post";
        return postForObject(resoureUrl, dto);
    }

    /**
     * 删除老师
     *
     * @param id
     */
    public static void delTeacher(String id) {
        String resoureUrl = "/teacher/delete/" + id;
        delete(resoureUrl);
    }

    public static void delStaff(String id) {
        String resoureUrl = "/staff/delete/" + id;
        delete(resoureUrl);
    }

    /**
     * 添加年级
     *
     * @param grade
     * @return
     */
    public static String addGrade(GradeDTO grade) {
        String resoureUrl = "/grade/post";
        return postForObject(resoureUrl, grade);
    }

    /**
     * 更新年级名称
     *
     * @param gradeDTO
     * @return
     */
    public static String updateGrades(GradeDTO gradeDTO) {
        String resoureUrl = "/grade/update/post";
        return postForObject(resoureUrl, gradeDTO);
    }

    /**
     * 更新年级
     *
     * @param grade
     * @return
     */
    public static void updateGrade(GradeDTO grade) {
        String resoureUrl = "/grade/put";
        put(resoureUrl, grade);
    }

    /**
     * 添加班级
     *
     * @param classInfoDTO
     * @return
     */
    public static String addClass(ClassInfoDTO classInfoDTO) {
        String resoureUrl = "/class/post";
        return postForObject(resoureUrl, classInfoDTO);

    }

    /**
     * 删除班级
     *
     * @param id
     */
    public static void delClass(ObjectId id) {
        String resoureUrl = "/class/delete/" + id;
        delete(resoureUrl);
    }

    /**
     * 更新班级
     *
     * @param classInfoDTO
     */
    public static void updateClass(ClassInfoDTO classInfoDTO) {
        String resoureUrl = "/class/updateClass";
        postForObject(resoureUrl, classInfoDTO);

    }

    /**
     * 查询班级通过年级
     *
     * @param gradeId
     * @return
     */
    public static String selClassByGradeId(String gradeId) {
        String resoureUrl = "/class/get/1/" + gradeId;
        return getForObject(resoureUrl);
    }
    /**
     * 查询班级通过年级
     *
     * @param gradeId
     * @return
     */
    public static String getClassByGradeId(String gradeId) {
        String resoureUrl = "/class/getClassByGradeId/2/" + gradeId;
        return getForObject(resoureUrl);
    }
    /**
     * 查询班级所有信息通过年级
     *
     * @param gradeId
     * @return
     */
    public static String selClassInfoByGradeId(String gradeId) {
        String resoureUrl = "/class/get/3/" + gradeId;
        return getForObject(resoureUrl);
    }


    /**
     * 查询老师工作经历
     *
     * @param id
     * @return
     */
    public static String selTeacherJob(ObjectId id) {
        String resoureUrl = "/teacher/get/1/" + id;
        return getForObject(resoureUrl);
    }

    /**
     * 查询年级
     *
     * @param schoolId
     * @return
     */
    public static String selGradeList(ObjectId schoolId) {
        String resoureUrl = "/grade/get/1/" + schoolId;
        String resultStr = getForObject(resoureUrl);
        return resultStr;
    }

    /**
     * 查询年级
     *
     * @param schoolId
     * @return
     */
    public static String getGradeInfoList(String schoolId, String startYear) {
        String resoureUrl = "/grade/get/2/" + schoolId +"/"+startYear;
        String resultStr = getForObject(resoureUrl);
        return resultStr;
    }


    /**
     * 查询历史年级
     *
     * @param schoolId
     * @return
     */
    public static String selHistroyGradeList(ObjectId schoolId) {
        String resoureUrl = "/grade/get/histroy/" + schoolId;
        String resultStr = getForObject(resoureUrl);
        return resultStr;
    }

    /**
     * 查询年级
     *
     * @param id
     * @return
     */
    public static String selGradeInfo(ObjectId id) {
        String resoureUrl = "/grade/get/" + id;
        String resultStr = getForObject(resoureUrl);
        return resultStr;
    }

    /**
     * 教师列表
     *
     * @param schoolId
     * @param keyword
     * @param page
     * @param pageSize
     * @return
     */
    public static String selTeacherList(ObjectId schoolId, String keyword, int page, int pageSize) {
        if (StringUtils.isEmpty(keyword)) {
            keyword = "keyword";
        }
        String resoureUrl = "/teacher/get/" + schoolId + "/" + page + "/" + pageSize + "/" + keyword;
        return getForObject(resoureUrl);
    }

    public static String selStaffList(ObjectId schoolId, String keyword, int page, int pageSize) {
        if (StringUtils.isEmpty(keyword)) {
            keyword = "keyword";
        }
        String resoureUrl = "/staff/get/" + schoolId + "/" + page + "/" + pageSize + "/" + keyword;
        return getForObject(resoureUrl);
    }

    public static String selUserList(ObjectId schoolId, String keyword, int page, int pageSize) {
        if (StringUtils.isEmpty(keyword)) {
            keyword = "*";
        }
        String resoureUrl = "/teacher/selUserList/" + schoolId + "/" + page + "/" + pageSize + "/" + keyword;
        return getForObject(resoureUrl);
    }

    /**
     * 老师信息
     *
     * @param id
     * @return
     */
    public static String selTeacherInfo(ObjectId id) {
        String resoureUrl = "/teacher/get/" + id;
        return getForObject(resoureUrl);
    }

    public static String selStaffInfo(ObjectId id) {
        String resoureUrl = "/staff/get/" + id;
        return getForObject(resoureUrl);
    }

    /**
     * @param id
     * @param schoolId
     * @param keyword
     * @param page
     * @param pageSize
     * @return
     */
    public static String getGradeTeacher(ObjectId id, ObjectId schoolId, String keyword, int page, int pageSize) {
        if (StringUtils.isEmpty(keyword)) {
            keyword = "keyword";
        }
        String resoureUrl = "/grade/get/" + id + "/" + schoolId + "/" + keyword + "/" + page + "/" + pageSize;
        return getForObject(resoureUrl);
    }

    /**
     * @param schoolId
     * @param keyword
     * @param gradeId
     * @param subjectId
     * @return
     */
    public static String selTeachersByGradeSubject(ObjectId schoolId, String keyword, ObjectId gradeId, String subjectId) {
        if (StringUtils.isEmpty(keyword)) {
            keyword = "keyword";
        }
        String resoureUrl = "/grade/get/" + schoolId + "/" + keyword + "/" + gradeId + "/" + subjectId;
        return getForObject(resoureUrl);
    }

    /**
     * 删除年级老师
     *
     * @param id
     */
    public static void delGradeTeacher(ObjectId id) {
        String resoureUrl = "/grade/delete/gt/" + id;
        delete(resoureUrl);
    }

    /**
     * 添加年级老师
     *
     * @param grade
     */
    public static String addGradeTeacher(GradeDTO grade) {
        String resoureUrl = "/grade/post/at";
        return postForObject(resoureUrl, grade);
    }

    /**
     * 删除学科老师
     *
     * @param userId
     * @param subjectId
     */
    public static void delSubjectUser(ObjectId userId, ObjectId subjectId) {
        String resoureUrl = "/subject/delete/" + userId + "/" + subjectId;
        delete(resoureUrl);
    }


    /**
     * 设置年级组长
     *
     * @param dto
     */
    public static String setGradeLeader(GradeDTO dto) {
        String resoureUrl = "/grade/post/set";
        return postForObject(resoureUrl, dto);
    }

    /**
     * 添加更新学生
     *
     * @param dto
     */
    public static String addUdpStudent(StudentDTO dto) {
        String resoureUrl = "/student/post";
        return postForObject(resoureUrl, dto);
    }

    /**
     * 添加更新学生
     */
    public static String getMembershipTypeList() {
        String resoureUrl = "/student/getMembershipTypeList";
        return getForObject(resoureUrl);
    }

    /**
     * 查询班级学生
     *
     * @param classId
     * @param keyword
     * @param page
     * @param pageSize
     * @return
     */
    public static String selStudentListByClassId(ObjectId classId, String keyword, int page, int pageSize) {
        if (StringUtils.isEmpty(keyword)) {
            keyword = "keyword";
        }
        String resoureUrl = "/student/get/" + classId + "/" + page + "/" + pageSize + "/" + keyword;
        return getForObject(resoureUrl);
    }


    public static String getAuditingList(String loopId, String typeId, Integer loop) {
        String resoureUrl = "/classroom/getClassRoomsList/" + loopId + "/" + loop + "/" + typeId;
        return getForObject(resoureUrl);
    }

    /**
     * 删除学生
     *
     * @param classId
     */
    public static void delStudent(String classId, String userId) {
        String resoureUrl = "/student/delete/" + new ObjectId(classId) + "/" + new ObjectId(userId);
        delete(resoureUrl);
    }

    /**
     * 查询学生
     *
     * @param userId
     * @return
     */
    public static String selStudentInfo(ObjectId userId, ObjectId classId) {
        String resoureUrl = "/student/get/" + userId + "/" + classId;
        return getForObject(resoureUrl);
    }

    public static String updateStuCode(String userId, String code) {
        String resoureUrl = "/student/updateStuCode/" + userId + "/" + code;
        return getForObject(resoureUrl);
    }

    /**
     * 查询班级
     *
     * @param id
     * @return
     */
    public static String selClass(ObjectId id) {
        String resoureUrl = "/class/get/" + id;
        return getForObject(resoureUrl);
    }

    /**
     * 设置班级关联教室
     *
     * @param id
     * @param roomId
     * @return
     */
    public static String setClazzRoom(ObjectId id, ObjectId roomId) {
        String url = "/class/" + id + "/setRoom/" + roomId;
        return getForObject(url);
    }

    /**
     * 删除老师工作经历
     *
     * @param jobId
     */
    public static void delTeacherJob(String jobId) {
        String resoureUrl = "/teacher/delete/job/" + new ObjectId(jobId);
        delete(resoureUrl);
    }

    /**
     * 查询工作经历
     *
     * @param jobId
     * @return
     */
    public static String getTeacherJob(String jobId) {
        String resoureUrl = "/teacher/get/job/" + new ObjectId(jobId);
        return getForObject(resoureUrl);
    }

    /**
     * 文件目录
     *
     * @param dirId
     * @return
     */
    public static String getFolderList(String dirId) {
        String resoureUrl = "/files/get/folders/" + dirId;
        String resultStr = getForObject(resoureUrl);
        return resultStr;
    }


    /**
     * 删除班级多个学生
     *
     * @param id
     * @param userIds
     */
    public static void delStudents(String id, String userIds) {
        String resoureUrl = "/student/delete/1/" + id + "/" + userIds;
        delete(resoureUrl);
    }

    /**
     * 查询教室类型
     *
     * @param schoolId
     * @return
     */
    public static String getClassTypeList(String schoolId) {
        String resourceUrl = "/classroom/selClassTypeList/" + schoolId;
        String result = getForObject(resourceUrl);
        return result;
    }

    /**
     * 添加教室类型
     *
     * @param schoolId
     * @param name
     */
    public static String addClassType(String schoolId, String name) {
        String resourceUrl = "/classroom/saveClassType";
        Map map = new HashMap();
        map.put("schoolId", schoolId);
        map.put("name", name);
        String result = postForObject(resourceUrl, map);
        return result;
    }

    /**
     * 更新教室类型
     *
     * @param id
     * @param name
     */
    public static void updClassType(String id, String name) {
        String resourceUrl = "/classroom/updateClassType";
        Map map = new HashMap();
        map.put("id", id);
        map.put("name", name);
        postForObject(resourceUrl, map);
    }

    /**
     * 删除教室类型
     *
     * @param id
     */
    public static void delClassType(String id) {
        String resourceUrl = "/classroom/delete/classType/" + id;
        delete(resourceUrl);
    }

    /**
     * 添加文件apk
     *
     * @param navId
     * @param navName
     * @param filePath
     * @param fileSize
     * @param fileName
     */
    public static String addAppApk(String navId, String navName, String filePath, String fileSize, String fileName, String version, ObjectId schoolId, ObjectId userId, String content, String desc, String images, String packageName, String versionName) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("navId", navId);
        map.put("navName", navName);
        map.put("filePath", filePath);
        map.put("fileSize", fileSize);
        map.put("fileName", fileName);
        map.put("schoolId", schoolId.toString());
        map.put("userId", userId.toString());
        map.put("version", version);
        map.put("content", content);
        map.put("desc", desc);
        map.put("images", images);
        map.put("packageName", packageName);
        map.put("versionName", versionName);
        String resoureUrl = "/files/addAppApk";
        return postForObject(resoureUrl, map);
    }

    public static String upgrade(ObjectId schoolId) {
        String resoureUrl = "/grade/upgrade/" + schoolId;
        String resultStr = getForObject(resoureUrl);
        return resultStr;
    }


    public static String getDutyANDJobLevel(ObjectId schoolId) {
        String resourceUrl = "/teacher/dutyandjoblevel/" + schoolId;
        String result = getForObject(resourceUrl);
        return result;
    }

    public static String delTissue(String id) {
        String resoureUrl = "/schoolTissue/delete/" + id;
        String resultStr = getForObject(resoureUrl);
        return resultStr;
    }

    public static String addTissue(Map dto) {
        String resourceUrl = "/schoolTissue/post";
        return postForObject(resourceUrl, dto);
    }

    public static String addTissues(Map dto) {
        String resourceUrl = "/schoolTissue/posts";
        return postForObject(resourceUrl, dto);
    }

    public static String getTissue(ObjectId schoolId) {
        String url = "/schoolTissue/brothers/" + schoolId.toString();
        String result = getForObject(url);
        return result;
    }

    /**
     * @param schoolId
     * @param id
     * @param type
     */
    public static void updateIndex(String schoolId, String id, int type) {
        String url = "/class/updateIndex/" + schoolId.toString() + "/" + id + "/" + type;
        getForObject(url);
    }


    public static void updateSubjectIndex(String schoolId, String id, int type) {
        String url = "/subject/updateIndex/" + schoolId.toString() + "/" + id + "/" + type;
        getForObject(url);
    }

    /**
     *
     * @param loopId
     * @param ceng
     * @param typeId
     * @return
     */
    public static String getClassRooms(String loopId, int ceng, String typeId,String keyword,List<ObjectId> classRoomIds) {
        Map map = new HashMap();
        map.put("loopId",loopId);
        map.put("ceng",String.valueOf(ceng));
        map.put("typeId",typeId);
        map.put("keyword",keyword);
        map.put("classRoomIds", MongoUtils.convertToStr(classRoomIds));
        String resoureUrl = "/classroom/get/rooms";
        String resultStr = postForObject(resoureUrl,map);
        return resultStr;
    }
}
