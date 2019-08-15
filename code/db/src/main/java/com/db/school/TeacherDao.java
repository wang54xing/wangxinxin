package com.db.school;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.school.TeacherClassSubjectEntry;
import com.pojo.user.UserDetailInfoDTO;
import com.pojo.user.UserEntry;
import com.pojo.user.UserRole;
import com.sys.constants.Constant;
import com.sys.utils.MD5Utils;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by yan on 2015/3/16.
 */
public class TeacherDao extends BaseDao {
    public String addTeacher(ObjectId schoolId, String teacherName, String jobNum, String permission, String pwd) {
        UserEntry userEntry = new UserEntry(teacherName, pwd, -1, null);
        userEntry.setUserName(teacherName);
        userEntry.setJobnumber(jobNum);
        userEntry.setSchoolID(schoolId);
        userEntry.setNickName(teacherName);
        userEntry.setPassword(MD5Utils.getMD5String(pwd));
        userEntry.setRole(Integer.parseInt(permission));
        String headPrefix = "head-";
        userEntry.setAvatar(headPrefix + "default-head.jpg");
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, userEntry.getBaseEntry());
        return userEntry.getChatId();
    }

    public boolean updateTeacher(ObjectId objectId, String teacherName, String jobNum, String permission) {
        BasicDBObject query = new BasicDBObject();
        query.append(Constant.ID, objectId);
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET,
                new BasicDBObject("nm", teacherName).append("jnb", jobNum).append("r", Integer.parseInt(permission)));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, updateValue);
        return true;
    }

    /*
    *
    * 包括校领导
    * */
    public List<UserDetailInfoDTO> teacherList(ObjectId objectId, String keyWord, int skip, int size) {
        BasicDBObject query=new BasicDBObject("si",objectId).append("ir", Constant.ZERO);
        if(!StringUtils.isBlank(keyWord)){
            Pattern pattern = Pattern.compile(keyWord, Pattern.CASE_INSENSITIVE);
            query.append("nm",pattern);
        }
        
       // String func="function (){return (this.r & 2) == 2 || (this.r & 8) == 8  || (this.r & 64) == 64  ;};";
       // query.append("$where",func);
       // BasicDBList list=new BasicDBList();
       // list.add(new BasicDBObject());
        
        query.append("r", new BasicDBObject(Constant.MONGO_NOTIN,new Integer[]{UserRole.STUDENT.getRole(),UserRole.PARENT.getRole()}));

        List<DBObject> dbObjects=MongoFacroty.getAppDB().getCollection(Constant.COLLECTION_USER_NAME).find(query).skip(skip).limit(size).toArray();
        List<UserDetailInfoDTO> userInfoDTOList=new ArrayList<UserDetailInfoDTO>();
        for(DBObject dbObject:dbObjects){
            UserEntry userEntry=new UserEntry((BasicDBObject)dbObject);
            UserDetailInfoDTO userInfoDTO=new UserDetailInfoDTO(userEntry);
            userInfoDTOList.add(userInfoDTO);
        }
        return userInfoDTOList;
    }

    public boolean updatePwd(ObjectId userId, String initPassword) {
        BasicDBObject query = new BasicDBObject(Constant.ID, userId);
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("pw", initPassword));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, updateValue);
        return true;
    }

    public List<ObjectId> findTeacherBySubjectId(ObjectId subjectId) {
        BasicDBObject query = new BasicDBObject().append("sui.id", subjectId);
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_TEACHERCLASSSUBJECT_NAME, query, new BasicDBObject("ti", 1));

        List<ObjectId> objectIdList = new ArrayList<ObjectId>();
        for (DBObject dbObject : dbObjectList) {
            TeacherClassSubjectEntry teacherClassLessionEntry = new TeacherClassSubjectEntry((BasicDBObject) dbObject);
            ObjectId teacherId = teacherClassLessionEntry.getTeacherId();
            objectIdList.add(teacherId);
        }
        return objectIdList;
    }

    public List<ObjectId> findTeacherBySubjectIdAndClassIds(ObjectId subjectId, List<ObjectId> classIds) {
        BasicDBObject query = new BasicDBObject().append("sui.id", subjectId).append("cli.id", new BasicDBObject(Constant.MONGO_IN, classIds));
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_TEACHERCLASSSUBJECT_NAME, query, new BasicDBObject("ti", 1));

        List<ObjectId> objectIdList = new ArrayList<ObjectId>();
        for (DBObject dbObject : dbObjectList) {
            TeacherClassSubjectEntry teacherClassLessionEntry = new TeacherClassSubjectEntry((BasicDBObject) dbObject);
            ObjectId teacherId = teacherClassLessionEntry.getTeacherId();
            objectIdList.add(teacherId);
        }
        return objectIdList;
    }

    public List<ObjectId> findTeacherByClassIds(List<ObjectId> classIds) {
        BasicDBObject query = new BasicDBObject().append("cli.id", new BasicDBObject(Constant.MONGO_IN, classIds));
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_TEACHERCLASSSUBJECT_NAME, query, new BasicDBObject("ti", 1));

        List<ObjectId> objectIdList = new ArrayList<ObjectId>();
        for (DBObject dbObject : dbObjectList) {
            TeacherClassSubjectEntry teacherClassLessionEntry = new TeacherClassSubjectEntry((BasicDBObject) dbObject);
            ObjectId teacherId = teacherClassLessionEntry.getTeacherId();
            objectIdList.add(teacherId);
        }
        return objectIdList;
    }

    public List<ObjectId> findTeacherBySubjectIds(List<ObjectId> subjectIds) {
        BasicDBObject query = new BasicDBObject("sui.id", new BasicDBObject(Constant.MONGO_IN, subjectIds));
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_TEACHERCLASSSUBJECT_NAME, query, new BasicDBObject("ti", 1));
        List<ObjectId> objectIdList = new ArrayList<ObjectId>();
        for (DBObject dbObject : dbObjectList) {
            TeacherClassSubjectEntry teacherClassLessionEntry = new TeacherClassSubjectEntry((BasicDBObject) dbObject);
            ObjectId teacherId = teacherClassLessionEntry.getTeacherId();
            objectIdList.add(teacherId);
        }
        return objectIdList;
    }

    public int countTeacher(ObjectId objectId, String keyWord) {
        BasicDBObject query=new BasicDBObject("si",objectId).append("ir", Constant.ZERO);
        if(!StringUtils.isBlank(keyWord)){
            Pattern pattern = Pattern.compile(keyWord, Pattern.CASE_INSENSITIVE);
            query.append("nm",pattern);
        }
        //String func="function (){return (this.r & 2) == 2 || (this.r & 8) == 8  || (this.r & 64) == 64  ;};";
        //query.append("$where",func);
        query.append("r", new BasicDBObject(Constant.MONGO_NOTIN,new Integer[]{UserRole.STUDENT.getRole(),UserRole.PARENT.getRole()}));
        
        int count=count(MongoFacroty.getAppDB(),Constant.COLLECTION_USER_NAME,query);
        return count;
    }

    public boolean updatePwdBySchoolId(ObjectId schoolId, String initPassword) {
        BasicDBObject query = new BasicDBObject("si", schoolId).append("r", new BasicDBObject(Constant.MONGO_GTE, UserRole.TEACHER.getRole())
                .append(Constant.MONGO_NE, UserRole.PARENT.getRole()))
                .append("ir", Constant.ZERO);
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("pw", initPassword));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, updateValue);
        return true;
    }

    public UserEntry addTeacherInfo(ObjectId schoolId, int userRole, int sex,String teacherName,String realName, String pwd,int postion,String teachernumber,String postionDec) {
        UserEntry userEntry = new UserEntry(teacherName, pwd, -1, null);
        userEntry.setUserName(teacherName);
        userEntry.setSchoolID(schoolId);
        userEntry.setRole(userRole);
        userEntry.setSex(sex);
        userEntry.setNickName(realName);
        userEntry.setPassword(MD5Utils.getMD5String(pwd));
        userEntry.setPostion(postion);
        userEntry.setPostionDec(postionDec);
        userEntry.setJobnumber(teachernumber);
        String headPrefix = "head-";
        userEntry.setAvatar(headPrefix + "default-head.jpg");
        
        
        
        if(StringUtils.isBlank(userEntry.getChatId()))
        {
            if(null==userEntry.getID())
            {
            	userEntry.setID(new ObjectId());
            }
            userEntry.setChatId(userEntry.getID().toString());
        }
        if(userEntry.getAvatar().equals(""))
        {
        	userEntry.setAvatar("head-default-head.jpg");
        }
        
        if(userEntry.getRole()==UserRole.HEADMASTER.getRole())
        {
        	userEntry.setRole(UserRole.HEADMASTER.getRole() | UserRole.TEACHER.getRole() );
        }
        
        
        
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, userEntry.getBaseEntry());
        return userEntry;
    }
}
