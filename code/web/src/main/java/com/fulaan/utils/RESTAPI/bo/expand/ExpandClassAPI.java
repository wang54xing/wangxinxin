package com.fulaan.utils.RESTAPI.bo.expand;

import com.fulaan.extensionclass.dto.ExpandAttendanceDTO;
import com.fulaan.extensionclass.dto.ExpandDTO;
import com.fulaan.extensionclass.dto.ExpandOpenDTO;
import com.fulaan.utils.RESTAPI.base.BaseAPI;
import com.pojo.app.IdValuePairDTO1;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;

import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * added by xusy
 */
public class ExpandClassAPI extends BaseAPI {

    /**
     * 添加或者更新拓展课信息
     *
     * @param expandDto
     * @return
     */
    public static String addOrEditExpandClass(ExpandDTO expandDto) {
        String resourceUrl = "/expand/post";
        String result = postForObject(resourceUrl, expandDto);
        return result;
    }
    public static String getClsBySid(String schoolId,Integer page,Integer pagesize) {
        String reUrl = "/class/ get/2/" + schoolId+"/"+page+"/"+pagesize;
        return getForObject(reUrl);
    }


    /**
     * 根据关键字分页查询拓展课
     *
     * @param schoolId
     * @param keyword
     * @param page
     * @param pageSize
     * @return
     */
    public static String getExpandByKeyword(String schoolId, String keyword, int page, int pageSize) {
        String resourceUrl = "/expand/get/" + schoolId + "/key/" + keyword + "/" + page + "/" + pageSize;
        String result = getForObject(resourceUrl);
        return result;
    }
    public static String getlr(String id) {
        String resourceUrl = "/expand/lr/" + id;
        String result = getForObject(resourceUrl);
        return result;
    }

    public static String jv(String uid) {
        String resourceUrl = "/expand/jv/" + uid;
        String result = getForObject(resourceUrl);
        return result;
    }

    public static String getioc(String uid,String sid) {
        String resourceUrl = "/expand/getioc/" + uid+"/"+sid;
        String result = getForObject(resourceUrl);
        return result;
    }

    public static String upzt(String id, int zt) {
        String resourceUrl = "/expand/upid/" + id + "/" + zt;
        String result = getForObject(resourceUrl);
        return result;
    }

    public static String upzt(String id, int zt, String ly,String uid) {
        String resourceUrl = "/expand/upids/" + id + "/" + zt + "/" + ly+"/"+uid;
        String result = getForObject(resourceUrl);
        return result;
    }

    /**
     * 根据关键字分页查询拓展课
     *
     * @param schoolId
     * @param keyword
     * @param page
     * @param pageSize
     * @return
     */
    public static String getExpandByKeywordRolo(String schoolId, String keyword, int page, int pageSize, String uid, Integer rolo) {
        String resourceUrl = "/expand/gets/" + schoolId + "/key/" + keyword + "/" + page + "/" + pageSize + "/" + uid + "/" + rolo;
        String result = getForObject(resourceUrl);
        return result;
    }

    /**
     * 根据标签分页查询拓展课
     *
     * @param schoolId
     * @param keyword
     * @param page
     * @param pageSize
     * @return
     */
    public static String getExpandByTips(String schoolId, String tips, int page, int pageSize) {
        String resourceUrl = "/expand/get/" + schoolId + "/" + tips + "/" + page + "/" + pageSize;
        String result = getForObject(resourceUrl);
        return result;
    }

    public static String getExpandByTipsRolo(String schoolId, String tips, int page, int pageSize, String uid, Integer rolo) {
        String resourceUrl = "/expand/gets/" + schoolId + "/" + tips + "/" + page + "/" + pageSize + "/" + uid + "/" + rolo;
        String result = getForObject(resourceUrl);
        return result;
    }

    /**
     * 根据关键字和标签分页查询拓展课
     *
     * @param schoolId
     * @param keyword
     * @param page
     * @param pageSize
     * @return
     */
    public static String getExpandByKeywordAndTips(String schoolId, String tips, String keyword, int page, int pageSize) {
        String resourceUrl = "/expand/get/" + schoolId + "/" + tips + "/" + keyword + "/" + page + "/" + pageSize;
        String result = getForObject(resourceUrl);
        return result;
    }

    /**
     * 根据关键字和标签分页查询拓展课(申报)
     *
     * @param schoolId
     * @param keyword
     * @param page
     * @param pageSize
     * @return
     */
    public static String getExpandByKeywordAndTipsRolo(String schoolId, String tips, String keyword, int page, int pageSize, String uid, Integer rolo) {
        String resourceUrl = "/expand/gets/" + schoolId + "/" + tips + "/" + keyword + "/" + page + "/" + pageSize + "/" + uid + "/" + rolo;
        String result = getForObject(resourceUrl);
        return result;
    }

    /**
     * 分页查询学校所有拓展课
     *
     * @param schoolId
     * @param page
     * @param pageSize
     * @return
     */
    public static String getAllExpand(String schoolId, int page, int pageSize) {
        String resourceUrl = "/expand/get/" + schoolId + "/" + page + "/" + pageSize;
        String result = getForObject(resourceUrl);
        return result;
    }

    /**
     * 分页查询学校所有拓展课
     *
     * @param schoolId
     * @param page
     * @param pageSize
     * @return
     */
    public static String getAllExpandRolo(String schoolId, int page, int pageSize, String uid, int rolo) {
        String resourceUrl = "/expand/gets/" + schoolId + "/" + page + "/" + pageSize + "/" + uid + "/" + rolo;
        String result = getForObject(resourceUrl);
        return result;
    }

    /**
     * 根据状态获取开课数量
     *
     * @param expanIdList
     * @param status
     * @return
     */
    public static String getExpanOpenCount(List<String> expanIdList, int status) {
        String url = "/expandOpen/openCount/" + status;
        String result = postTaForObject(url, expanIdList);
        return result;
    }

    /**
     * 获取老师拓展课开课列表
     *
     * @param teacherId
     * @param eIds
     * @param page
     * @param pageSize
     * @return
     * @throws Exception
     */
    public static String getTeacherExpandOpen(String teacherId,
                                              String keyword, int page, int pageSize, String uid) throws Exception {
        String resoureUrl = "/expandOpen/getlist/" + teacherId + "/" + page + "/" + pageSize + "/" + uid;
        if (StringUtils.isNotBlank(keyword)) {
            keyword = URLEncoder.encode("uft-8", keyword);
            resoureUrl += "?keyword=" + keyword;
        }
        String result = getTaForObject(resoureUrl);
        return result;
    }

    public static String getTeacherExpandOpen(String teacherId,
                                              String keyword, int page, int pageSize, String uid,long st,long et) throws Exception {
        String resoureUrl = "/expandOpen/getlist/" + teacherId + "/" + page + "/" + pageSize + "/" + uid+"/"+st+"/"+et;
        if (StringUtils.isNotBlank(keyword)) {
            keyword = URLEncoder.encode("uft-8", keyword);
            resoureUrl += "?keyword=" + keyword;
        }
        String result = getTaForObject(resoureUrl);
        return result;
    }
    public static String getTeacherExpandOpen(String sid, int page, int pageSize) throws Exception {
        String resoureUrl = "/expandOpen/getlistd/" + sid + "/" + page + "/" + pageSize ;
        String result = getTaForObject(resoureUrl);
        return result;
    }

    public static String getTeacherExpandOpen(String sid, int page, int pageSize,long st,long et) throws Exception {
        String resoureUrl = "/expandOpen/getlistdst/" + sid + "/" + page + "/" + pageSize+"/"+st+"/"+et ;
        String result = getTaForObject(resoureUrl);
        return result;
    }
    public static String getCs(String asdid, String time, String st,String ed,String eid) throws Exception {
        String resoureUrl = "/expandOpen/openCls/" + asdid + "/" + time + "/" + st + "/" + ed+"/"+eid;
        String result = getTaForObject(resoureUrl);
        return result;
    }

    /**
     * 获取学校拓展课开课列表
     *
     * @param schoolId
     * @param eIds
     * @param page
     * @param pageSize
     * @return
     */
    public static String getSchoolExpandOpen(String schoolId, String keyword, int page, int pageSize) {
        String resoureUrl = "/expandOpen/getlistBys/" + schoolId + "/" + page + "/" + pageSize;
        if (StringUtils.isNotBlank(keyword)) {
            resoureUrl += "?keyword=" + keyword;
        }
        String result = getTaForObject(resoureUrl);
        return result;
    }

    public static String getSchoolExpandOpen(String schoolId, String keyword, int page, int pageSize,long st,long et) {
        String resoureUrl = "/expandOpen/getlistBys/" + schoolId + "/" + page + "/" + pageSize+"/"+st+"/"+et;
        if (StringUtils.isNotBlank(keyword)) {
            resoureUrl += "?keyword=" + keyword;
        }
        String result = getTaForObject(resoureUrl);
        return result;
    }
    /**
     * 获取拓展课开课信息
     *
     * @param expandOpenId
     * @return
     */
    public static String getExpandOpen(String expandOpenId,long st,long et) {
        String resourceUrl = "/expandOpen/load/" + expandOpenId+"/"+st+"/"+et;
        String result = getTaForObject(resourceUrl);
        return result;
    }

    public static String getExpandOpen(String expandOpenId) {
        String resourceUrl = "/expandOpen/load/" + expandOpenId;
        String result = getTaForObject(resourceUrl);
        return result;
    }
    /**
     * 添加拓展课评论
     *
     * @param eId
     * @param comment
     * @return
     */
    public static String addExpandOpenComment(String eId, IdValuePairDTO1 comment) {
        String resourceUrl = "/expandOpen/" + eId + "/comment";
        String result = postTaForObject(resourceUrl, comment);
        return result;
    }

    /**
     * 更新拓展课开课状态
     *
     * @param expandOpenId
     * @param status
     * @return
     */
    public static String updateExpandOpenStatus(String expandOpenId, int status) {
        String resourceUrl = "/expandOpen/status/" + expandOpenId + "/" + status;
        String result = getTaForObject(resourceUrl);
        return result;
    }

    /**
     * 根据id获取拓展课详情
     *
     * @param id
     * @return
     */
    public static String getExpandInfo(String id) {
        String resourceUrl = "/expand/getExpandById/" + id;
        String result = getForObject(resourceUrl);
        return result;
    }

    public static String getExpandInfoTerm(String id,long st,long et) {
        String resourceUrl = "/expand/getExpandById/" + id+"/"+st+"/"+et;
        String result = getForObject(resourceUrl);
        return result;
    }
    /**
     * 根据id删除拓展课
     *
     * @param id
     * @return
     */
    public static void delExpandById(String id) {
        String resourceUrl = "/expand/delete/" + id;
        delete(resourceUrl);
    }

    /**
     * 删除拓展课开课
     *
     * @param expandOpenId
     */
    public static void delExpanOpen(String expandOpenId) {
        String resourceUrl = "/expandOpen/del/" + expandOpenId;
        taDelete(resourceUrl);
    }

    /**
     * 添加或更新拓展课开课信息
     *
     * @param expandOpenDto
     * @return
     */
    public static String arrOrEditExpandOpen(ExpandOpenDTO expandOpenDto) {
        String resourceUrl = "/expandOpen/addOrUpdate";
        String result = postTaForObject(resourceUrl, expandOpenDto);
        return result;
    }

    /**
     * 获取最近拓展课开课信息
     *
     * @param eId
     * @return
     */
    public static String getLateExpandOpen(String eId) {
        String resourceUrl = "/expandOpen/" + eId + "/late";
        String result = getTaForObject(resourceUrl);
        return result;
    }

    /**
     * 添加拓展课设置
     *
     * @param paramMap
     * @return
     */
    public static String addOrUpdSetting(Map paramMap) {
        String resourceUrl = "/expandSet/addOrUpd";
        String result = postForObject(resourceUrl, paramMap);
        return result;
    }
    public static String addExpandRole(String uid,String sid,String name) {
        String resourceUrl = "/expandRole/role/"+uid+"/"+sid+"/"+name;
        String result = getTaForObject(resourceUrl);
        return result;
    }
    public static String addExpandTable(String schoolId,String name,String st,String lt,String z) {
        String resourceUrl = "/expandTable/addExpandTable/"+schoolId+"/"+name+"/"+st+"/"+lt+"/"+z;
        String result = getForObject(resourceUrl);
        return result;
    }
    public static String addExpandCls(String schoolId,String name,String dz,Integer sl) {
        String resourceUrl = "/expandCls/addExpandClass/"+schoolId+"/"+name+"/"+dz+"/"+sl;
        String result = getForObject(resourceUrl);
        return result;
    }

    public static String upExpandCls(String schoolId,String name,String dz,Integer sl,String id) {
        String resourceUrl = "/expandCls/upExpandTable/"+schoolId+"/"+name+"/"+dz+"/"+sl+"/"+id;
        String result = getForObject(resourceUrl);
        return result;
    }
    public static String getCls(String schoolId) {
        String resourceUrl = "/classroom/getClassBysid/"+schoolId;
        String result = getForObject(resourceUrl);
        return result;
    }

    /**
     * 通过学校ID查找所有教室
     * @param schoolId
     * @return
     */
    public static String getClassRoomBysid(String schoolId) {
        String resourceUrl = "/classroom/getClassRoomBysid/"+schoolId;
        String result = getForObject(resourceUrl);
        return result;
    }


    public static String addExpandAppraise(String schoolId,String name,String scale) {
        String resourceUrl = "/expandAppraise/addExpandAppraise/"+schoolId+"/"+name+"/"+scale;
        String result = getForObject(resourceUrl);
        return result;
    }
    public static String addAppraiseTake(String eid, String uid, String sid, String pid, String sc,  String nm, String dt) {
        String resourceUrl = "/expandAppraiseTake/addTake/"+eid+"/"+uid+"/"+sid+"/"+pid+"/"+sc+"/"+dt+"/"+nm;
        String result = getTaForObject(resourceUrl);
        return result;
    }

    public static String upExpandTable(String schoolId,String name,String st,String lt,String id,String z) {
        String resourceUrl = "/expandTable/upExpandTable/"+schoolId+"/"+name+"/"+st+"/"+lt+"/"+id+"/"+z;
        String result = getForObject(resourceUrl);
        return result;
    }

    public static String upExpandAppraise(String schoolId,String name,String scale,String id) {
        String resourceUrl = "/expandAppraise/upExpandAppraise/"+schoolId+"/"+name+"/"+scale+"/"+id;
        String result = getForObject(resourceUrl);
        return result;
    }

    public static String delExpandTable(String id) {
        String resourceUrl = "/expandTable/delExpandTable/"+id;
        String result = getForObject(resourceUrl);
        return result;
    }
    public static String delcls(String id) {
        String resourceUrl = "/expandCls/delExpandTable/"+id;
        String result = getForObject(resourceUrl);
        return result;
    }
    public static String delExpandAppraise(String id) {
        String resourceUrl = "/expandAppraise/delExpandAppraise/"+id;
        String result = getForObject(resourceUrl);
        return result;
    }


    public static String delExpandRole(String uid) {
        String resourceUrl = "/expandRole/delExpandRole/"+uid;
        String result = getTaForObject(resourceUrl);
        return result;
    }

    public static String getList(String sid) {
        String resourceUrl = "/expandRole/roleList/"+sid;
        String result = getTaForObject(resourceUrl);
        return result;
    }
    public static String getTableList(String schoolId) {
        String resourceUrl = "/expandTable/findExpandTable/"+schoolId;
        String result = getForObject(resourceUrl);
        return result;
    }

    public static String getAppraiseList(String schoolId) {
        String resourceUrl = "/expandAppraise/findExpandAppraise/"+schoolId;
        String result = getForObject(resourceUrl);
        return result;
    }

    public static String getAppraiseTake(String eid,String uid,String date) {
        String resourceUrl = "/expandAppraiseTake/getTake/"+eid+"/"+uid+"/"+date;
        String result = getTaForObject(resourceUrl);
        return result;
    }



    /**
     * 查询拓展课设置
     *
     * @param schoolId
     * @return
     */
    public static String getExpandSets(String schoolId) {
        String resourceUrl = "/expandSet/getExpandSets/1/" + schoolId;
        String result = getForObject(resourceUrl);
        return result;
    }

    /**
     * 获取学生选取的拓展课id列表
     *
     * @param userId
     * @param schoolId
     * @return
     */
    public static String getExpandOpenIdList(String userId, String schoolId,long st,long et) {
        String resourceUrl = "/groupUser/getGroupIdsByUserId/1/" + userId + "/" + schoolId + "/expandClass"+"/"+st+"/"+et;
        String result = getTaForObject(resourceUrl);
        return result;
    }

    public static String getExpandOpenIdList(String userId, String schoolId) {
        String resourceUrl = "/groupUser/getGroupIdsByUserId/1/" + userId + "/" + schoolId + "/expandClass";
        String result = getTaForObject(resourceUrl);
        return result;
    }

    /**
     * 获取学生选取的拓展课id列表
     *
     * @return
     */
    public static String gets(String id) {
        String resourceUrl = "/stuManages/stu/" + id;
        String result = getTaForObject(resourceUrl);
        return result;
    }

    /**
     * 添加拓展课巡堂老师
     *
     * @param paramMap
     * @return
     */
    public static String addXunTangTea(Map paramMap) {
        String resourceUrl = "/expandTeacher/addXunTangTea";
        String result = postTaForObject(resourceUrl, paramMap);
        return result;
    }

    /**
     * 查询年级
     *
     * @param gradeId
     * @return
     */
    public static String getClass(String gradeId) {
        String resourceUrl = "/class/get/3/" + gradeId;
        String result = getForObject(resourceUrl);
        return result;
    }

    public static String editXunTangTimeArea(Map paramMap) {
        String resourceUrl = "/expandTeacher/editXunTangTimeArea";
        String result = postTaForObject(resourceUrl, paramMap);
        return result;
    }


    /**
     * 查询拓展课巡堂老师
     *
     * @param schoolId
     * @return
     */
    public static String getXunTangTeaList(String schoolId) {
        String resourceUrl = "/expandTeacher/getXunTangTeaList/" + schoolId;
        String result = getTaForObject(resourceUrl);
        return result;
    }


    public static void delXunTangTea(String id) {
        String reUrl = "/expandTeacher/delXunTangTea/" + id;
        taDelete(reUrl);
    }

    /**
     * 添加或修改状态
     *
     * @param dto
     * @return
     */
    public static String attendOperation(ExpandAttendanceDTO dto) {
        String url = "/expandAttendance/operation";
        String result = postTaForObject(url, dto);
        return result;
    }

    /**
     * 批量考勤设置
     *
     * @param expandOpenId
     * @param date
     * @param status
     * @return
     */
    public static String batchAttend(ObjectId expandOpenId, String date, int status) {
        String url = "/expandAttendance/" + expandOpenId + "/" + date + "/" + status + "/batch";
        String result = getTaForObject(url);
        return result;
    }

    /**
     * 获取考勤列表
     *
     * @param expandOpenId
     * @param date
     * @return
     */
    public static String attendanceInfos(ObjectId expandOpenId, ObjectId schoolId, String date) {
        String url = "/expandAttendance/" + expandOpenId + "/" + schoolId + "/" + date + "/infos";
        String result = getTaForObject(url);
        return result;
    }

    /**
     * 获取学生考勤列表
     *
     * @param expandOpenId
     * @param userId
     * @param startDate
     * @param endDate
     * @return
     */
    public static String stuAttendanceInfos(
            ObjectId expandOpenId, ObjectId userId,
            String startDate, String endDate) {
        String url = "/expandAttendance/" + expandOpenId + "/"
                + userId + "/" + startDate + "/" + endDate + "/infos";
        String result = getTaForObject(url);
        return result;
    }

    public static String getImp(
            ObjectId sid, long st, long et) {
        String url = "/expandOpen/openDao/" + sid+"/"+st+"/"+et ;
        String result = getTaForObject(url);
        return result;
    }

    public static String getImpTT(
            ObjectId id) {
        String url = "/expandGrabClass/getstcls/" + id ;
        String result = getTaForObject(url);
        return result;
    }
    public static String studentImp(
            String eid) {
        String url = "/evaluateResult/studentImp/" + eid ;
        String result = getTaForObject(url);
        return result;
    }


}
