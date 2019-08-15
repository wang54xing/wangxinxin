package com.fulaan.teacher.service;

import com.db.school.ClassDao;
import com.db.school.SchoolDao;
import com.db.school.TeacherDao;
import com.db.user.UserDao;
import com.fulaan.new33.service.PaiKeService;
import com.pojo.school.ClassEntry;
import com.pojo.school.SchoolEntry;
import com.pojo.school.Subject;
import com.pojo.user.UserDetailInfoDTO;
import com.pojo.user.UserEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by yan on 2015/2/26.
 */
@Service
public class TeacherService {

    private UserDao userDao = new UserDao();
    private TeacherDao teacherDao = new TeacherDao();
    private ClassDao classDao = new ClassDao();
    private SchoolDao schoolDao = new SchoolDao();


    public List<UserDetailInfoDTO> findTeacherInNeed(String gradeId, String subjectId) {
        List<ObjectId> objectIdList = null;


        if (null != gradeId && null != subjectId && ObjectId.isValid(gradeId) && ObjectId.isValid(subjectId)) {
            objectIdList = findTeacherByGradeIdAndSubjectId(new ObjectId(gradeId), new ObjectId(subjectId));
        } else if (null == gradeId || !ObjectId.isValid(gradeId)) {
            objectIdList = findTeacherBySubjectId(new ObjectId(subjectId));
        } else if (null == subjectId || !ObjectId.isValid(subjectId)) {
            objectIdList = findTeacherByGradeId(new ObjectId(gradeId));
        }

        List<UserEntry> userEntryList = userDao.getUserEntryList(objectIdList, Constant.FIELDS);
        List<UserDetailInfoDTO> userInfoDTOList = new ArrayList<UserDetailInfoDTO>();
        for (UserEntry userEntry : userEntryList) {
            UserDetailInfoDTO userInfoDTO = new UserDetailInfoDTO(userEntry);
            userInfoDTOList.add(userInfoDTO);
        }
        return userInfoDTOList;
    }

    private List<ObjectId> findTeacherByGradeId(ObjectId gradeId) {
        List<ClassEntry> classEntryList = classDao.findClassEntryByGradeId(gradeId);
        List<ObjectId> classIds = new ArrayList<ObjectId>();
        if (classEntryList != null) {
            for (ClassEntry classEntry : classEntryList) {
                classIds.add(classEntry.getID());
            }
        }
        return teacherDao.findTeacherByClassIds(classIds);
    }

    private List<ObjectId> findTeacherByGradeIdAndSubjectId(ObjectId gradeId, ObjectId subjectId) {
        List<ClassEntry> classEntryList = classDao.findClassEntryByGradeId(gradeId);
        List<ObjectId> classIds = new ArrayList<ObjectId>();
        if (classEntryList != null) {
            for (ClassEntry classEntry : classEntryList) {
                classIds.add(classEntry.getID());
            }
        }
        return teacherDao.findTeacherBySubjectIdAndClassIds(subjectId, classIds);
    }

    private List<ObjectId> findTeacherBySubjectId(ObjectId subjectId) {
        return teacherDao.findTeacherBySubjectId(subjectId);
    }

    public List<UserDetailInfoDTO> findTeacherOfSchool(String schoolId) {

        SchoolEntry schoolEntry = schoolDao.getSchoolEntry(new ObjectId(schoolId), Constant.FIELDS);
        List<Subject> subjectList = schoolEntry.getSubjects();
        List<ObjectId> subjectIds = new ArrayList<ObjectId>();
        if (subjectList != null) {
            for (Subject subject : subjectList) {
                subjectIds.add(subject.getSubjectId());
            }
        }
        List<ObjectId> userIds = teacherDao.findTeacherBySubjectIds(subjectIds);
        List<UserEntry> userEntryList = userDao.getUserEntryList(userIds, Constant.FIELDS);
        List<UserDetailInfoDTO> userInfoDTOList = new ArrayList<UserDetailInfoDTO>();
        for (UserEntry userEntry : userEntryList) {
            UserDetailInfoDTO userInfoDTO = new UserDetailInfoDTO(userEntry);
            userInfoDTOList.add(userInfoDTO);
        }
        return userInfoDTOList;
    }
}
