package com.fulaan.log.service;

import com.db.log.LogDao;
import com.fulaan.log.dto.LogDTO;
import com.fulaan.myclass.service.ClassService;
import com.mongodb.BasicDBObject;
import com.pojo.app.Platform;
import com.pojo.log.LogEntry;
import com.pojo.log.LogType;
import com.pojo.school.ClassInfoDTO;
import com.pojo.user.UserEntry;
import com.pojo.user.UserRole;
import com.sys.constants.Constant;
import com.sys.exceptions.IllegalParamException;
import com.sys.exceptions.ResultTooManyException;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class LogService {

    @Autowired
    private ClassService classService;

    LogDao logDao=new LogDao();

    private final String SUCCESS = "success";
    private final String FAIL = "fail";
    /**
     * 查询全部日志信息
     * @return
     */
    public List<LogEntry> findAll() {
        List<LogEntry> list = null;
        list = logDao.findAll();
        return list;
    }

    /**
     * 保存日志信息
     * @param logDto
     * @return
     */
    public String insert(LogDTO logDto) {

        Object obj = logDao.insert(logDto.buildLogEntry());
        if (null == obj) {
            return FAIL;
        }
        return SUCCESS;
    }

    /**
     * 根据查询条件获取日志信息
     * @param usIds
     * @param actionType
     * @param dsl
     * @param del
     * @return
     * @throws ResultTooManyException
     */
    public List<LogDTO> getLogEntryByParamList(
            List<ObjectId> usIds,
            int actionType,
            long dsl,
            long del) throws ResultTooManyException {
        List<LogEntry> logEntryList=logDao.getLogEntryByParamList(usIds,actionType,dsl,del, Constant.FIELDS);
        List<LogDTO> logDTOList=new ArrayList<LogDTO>();
        for(LogEntry logEntry:logEntryList){
            LogDTO logDTO=new LogDTO(logEntry);
            logDTOList.add(logDTO);
        }
        return logDTOList;
    }

    /**
     * 根据查询条件获取日志数量
     * @param usIds
     * @param actionType
     * @param dsl
     * @param del
     * @return
     * @throws ResultTooManyException
     */
    public int getLogEntryByParamCount(
            List<ObjectId> usIds,
            int actionType,
            long dsl,
            long del) throws ResultTooManyException {
        int count=logDao.getLogEntryByParamCount(usIds, actionType, dsl, del);
        return count;
    }

    /**
     * 根据查询条件获取日志数量
     * @param schoolId
     * @param gradeId
     * @param classId
     * @param userRole
     * @param actionType
     * @param dsl
     * @param del
     * @return
     */
    public int getLogEntryByParamCount(
            ObjectId schoolId,
            ObjectId gradeId,
            ObjectId classId,
            int userRole,
            int noUserRole,
            int actionType,
            long dsl,
            long del){
        int count=logDao.getLogEntryByParamCount(schoolId,gradeId,classId,userRole,noUserRole,actionType,dsl,del);
        return count;
    }

    /**
     * 根据查询条件获取日志信息
     * @param schoolId
     * @param gradeId
     * @param classId
     * @param userRole
     * @param actionType
     * @param dsl
     * @param del
     * @return
     */
    public List<LogDTO> getLogEntryByParamList(
            ObjectId schoolId,
            ObjectId gradeId,
            ObjectId classId,
            int userRole,
            int noUserRole,
            int actionType,
            long dsl,
            long del){
        List<LogEntry> logEntryList=logDao.getLogEntryByParamList(schoolId,gradeId,classId,userRole,noUserRole,actionType,dsl,del, Constant.FIELDS);
        List<LogDTO> logDTOList=new ArrayList<LogDTO>();
        for(LogEntry logEntry:logEntryList){
            LogDTO logDTO=new LogDTO(logEntry);
            logDTOList.add(logDTO);
        }
        return logDTOList;
    }

    /**
     * 根据查询条件获取日志数量
     * @param schoolIds
     * @param gradeType
     * @param userRole
     * @param actionType
     * @param dsl
     * @param del
     * @return
     */
    public int getLogEntryByParamCount(
            List<ObjectId> schoolIds,
            int gradeType,
            int userRole,
            int actionType,
            long dsl,
            long del){
        int count=logDao.getLogEntryByParamCount(schoolIds,gradeType,userRole, actionType, dsl, del);
        return count;
    }

    /**
     * 根据查询条件获取日志信息
     * @param schoolIds
     * @param gradeType
     * @param userRole
     * @param actionType
     * @param dsl
     * @param del
     * @return
     */
    public List<LogDTO> getLogEntryByParamList(
            List<ObjectId> schoolIds,
            int gradeType,
            int userRole,
            int actionType,
            long dsl,
            long del){
        List<LogEntry> logEntryList=logDao.getLogEntryByParamList(schoolIds,gradeType,userRole,actionType,dsl,del, Constant.FIELDS);
        List<LogDTO> logDTOList=new ArrayList<LogDTO>();
        for(LogEntry logEntry:logEntryList){
            LogDTO logDTO=new LogDTO(logEntry);
            logDTOList.add(logDTO);
        }
        return logDTOList;
    }

    /**
     * 保存日志信息
     *
     * @return
     */
    /**
     *
     * @param userEntry
     * @param pf
     * @param clickLogin
     * @param actionName
     */
    public void insertLog(UserEntry userEntry,Platform pf, LogType clickLogin, String actionName) throws IllegalParamException {
        Set<ObjectId> gradeIds=new HashSet<ObjectId>();
        Set<ObjectId> classIds=new HashSet<ObjectId>();
        Set<Integer> gradeTys=new HashSet<Integer>();
        ObjectId userId=userEntry.getID();
        int userRole=userEntry.getRole();
        if(UserRole.isStudentOrParent(userRole)){
            if(UserRole.isParent(userRole)){
                userId=userEntry.getConnectIds().get(0);
            }
            userRole=userEntry.getRole();
            ClassInfoDTO dto=classService.getClassInfoDTOByStuId(userId, userEntry.getSchoolID());

            gradeIds.add(new ObjectId(dto.getGradeId()));
            classIds.add(new ObjectId(dto.getId()));
            gradeTys.add(dto.getGradeType());
        }else if(UserRole.isNotStudentAndParent(userRole)){
            userRole=UserRole.TEACHER.getRole();
            List<ClassInfoDTO> classDtos=classService.getSimpleClassInfoDTOs(userId, userEntry.getSchoolID());
            for(ClassInfoDTO dto:classDtos){
                gradeIds.add(new ObjectId(dto.getGradeId()));
                classIds.add(new ObjectId(dto.getId()));
                gradeTys.add(dto.getGradeType());
            }
        }
        LogDTO log = new LogDTO();
        log.setSchoolId(userEntry.getSchoolID());
        log.setGradeIds(gradeIds);
        log.setGradeTys(gradeTys);
        log.setClassIds(classIds);
        log.setUserId(userEntry.getID().toString());
        log.setUserRole(userRole);
        log.setPlatformType(pf.getType());
        log.setActionType(clickLogin.getCode());
        log.setActionName(actionName);
        log.setActionTime(new Date());
        insert(log);
    }

    public Map<ObjectId,List<LogEntry>>  getLogEntryMapByParam(int actionType, long dateStart, long dateEnd, BasicDBObject fields) {
        Map<ObjectId,List<LogEntry>>  map=logDao.getLogEntryMapByParam(actionType, dateStart,dateEnd, fields);
        return map;
    }
}
