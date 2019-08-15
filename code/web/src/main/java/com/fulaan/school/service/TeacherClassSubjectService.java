package com.fulaan.school.service;


import com.db.school.TeacherClassSubjectDao;
import com.db.user.UserDao;
import com.mongodb.BasicDBObject;

import com.pojo.school.TeacherClassSubjectDTO;
import com.pojo.school.TeacherClassSubjectEntry;
import com.pojo.user.UserDetailInfoDTO;
import com.pojo.user.UserEntry;
import com.sys.constants.Constant;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 老师班级课程
 * @author fourer
 *
 */
@Service
public class TeacherClassSubjectService {

	private TeacherClassSubjectDao teacherClassSubjectDao =new TeacherClassSubjectDao();
	
	/**
	 * 添加
	 * @param e
	 * @return
	 */
	public ObjectId addTeacherClassSubjectEntry(TeacherClassSubjectEntry e)
	{
		return teacherClassSubjectDao.addTeacherClassSubjectEntry(e);
	}
	
	/**
	 * 详情
	 * @param id
	 * @return
	 */
	public TeacherClassSubjectEntry getTeacherClassSubjectEntry(ObjectId id) {
		return teacherClassSubjectDao.getTeacherClassSubjectEntry(id);
	}
	
	/**
	 * 更新班级名字
	 * @param classId
	 * @param name
	 */
	 public void updateClassName(ObjectId classId,String name)
	 {
		 teacherClassSubjectDao.updateClassName(classId, name);
	 }
	/**
	 * 得到班级课程DTO list
	 * @param teacherId
	 * @return
	 */
	public List<TeacherClassSubjectDTO> getTeacherClassSubjectDTOList(ObjectId teacherId,Collection<ObjectId> classids )
	{
		List<TeacherClassSubjectDTO> retList =new ArrayList<TeacherClassSubjectDTO>();
		List<TeacherClassSubjectEntry> list=teacherClassSubjectDao.getTeacherClassSubjectEntryList(teacherId,classids,Constant.FIELDS);
		for(TeacherClassSubjectEntry e:list)
		{
			retList.add(new TeacherClassSubjectDTO(e));
		}
		return retList;
	}
	
	
	public List<TeacherClassSubjectEntry> getTeacherClassSubjectEntryList(ObjectId teacherId,Collection<ObjectId> classids)
	{
		List<TeacherClassSubjectEntry> list=teacherClassSubjectDao.getTeacherClassSubjectEntryList(teacherId,classids,Constant.FIELDS);
		return list;
	}
	
	
	
	

	public List<TeacherClassSubjectEntry> findEntryByClassId(ObjectId classId) {
		List<TeacherClassSubjectEntry> list=teacherClassSubjectDao.findEntryByClassId(classId);
		return list;
	}
	
	public List<TeacherClassSubjectDTO> findTeacherClassSubjectByClassIds(List<ObjectId> classIds){
		List<TeacherClassSubjectDTO> retList =new ArrayList<TeacherClassSubjectDTO>();
		List<TeacherClassSubjectEntry> list=teacherClassSubjectDao.findEntryByClassIds(classIds);
		for(TeacherClassSubjectEntry e:list)
		{
			retList.add(new TeacherClassSubjectDTO(e));
		}
		return retList;
	}


	public List<UserDetailInfoDTO> findTeacherClassSubjectBySubjectId(String subjectId) {
		ObjectId subjectID=new ObjectId( subjectId);
		List<TeacherClassSubjectEntry> entries=teacherClassSubjectDao.findTeacherClassSubjectBySubjectId(subjectID);
		List<UserDetailInfoDTO> userDetailInfoDTOList=null;
		if(entries!=null){
			List<ObjectId> teacherIds=new ArrayList<ObjectId>();
			for(TeacherClassSubjectEntry entry:entries){
				ObjectId teacherId=entry.getTeacherId();
				teacherIds.add(teacherId);
			}
			UserDao userDao=new UserDao();
			List<UserEntry>  userEntryList=userDao.getUserEntryList(teacherIds,Constant.FIELDS);
			if(userEntryList!=null){
				userDetailInfoDTOList=new ArrayList<UserDetailInfoDTO>();
				for(UserEntry userEntry:userEntryList){
					UserDetailInfoDTO userDetailInfoDTO=new UserDetailInfoDTO(userEntry);
					userDetailInfoDTOList.add(userDetailInfoDTO);
				}
			}
		}
		return userDetailInfoDTOList;
	}

    public List<TeacherClassSubjectDTO> getTeacherClassSubjectDTOByUidsList(List<ObjectId> usIds) {
        List<TeacherClassSubjectDTO> retList =new ArrayList<TeacherClassSubjectDTO>();
        List<TeacherClassSubjectEntry> list=teacherClassSubjectDao.getTeacherClassSubjectEntryByUidsList(usIds,Constant.FIELDS);
        for(TeacherClassSubjectEntry e:list)
        {
            retList.add(new TeacherClassSubjectDTO(e));
        }
        return retList;
    }

	public Map<ObjectId, Set<ObjectId>> getClassLessonSet(ObjectId teacherId) {
		return teacherClassSubjectDao.getClassLessonSet(teacherId);
	}

	/**
	 * 获取老师代班的集合
	 * @param ids
	 * @return
	 */
	public List<TeacherClassSubjectEntry> getTeacherClassSubjectEntry(Collection<ObjectId> ids) {
		return teacherClassSubjectDao.getTeacherClassSubjectEntry(ids);
	}

	/**
	 * 根据班级和学科获取课
	 * @param classId
	 * @param subjectId
	 * @return
	 */
	public TeacherClassSubjectEntry findTeacherClassSubjectByCIdSId(ObjectId classId, ObjectId subjectId) {
		return teacherClassSubjectDao.findTeacherClassSubjectByCIdSId(classId,subjectId);
	}


	public List<TeacherClassSubjectEntry> findTeacherClassSubjectByParam(List<ObjectId> classIds, ObjectId subjectId) {
		List<TeacherClassSubjectEntry> list=teacherClassSubjectDao.findEntryByParams(classIds, subjectId);
		return list;
	}

	/**
	 * 根据学科集合来查询
	 * @param subjectIDs
	 * @return
	 */
	public List<TeacherClassSubjectEntry> findTeacherClassSubjectBySubjectIds(Collection<ObjectId> subjectIds, BasicDBObject fields) {
		List<TeacherClassSubjectEntry> list=teacherClassSubjectDao.findTeacherClassSubjectBySubjectIds(subjectIds, fields);
		return list;
	}
	
	
	/**
	 * 查找某一个老师的学科
	 * @param teacherID
	 * @return
	 */
	public TeacherClassSubjectEntry findTeacherClassSubjectByTeacherID(ObjectId teacherID) {
		return teacherClassSubjectDao.findTeacherClassSubjectByTeacherID(teacherID);
	}
	
	
}
