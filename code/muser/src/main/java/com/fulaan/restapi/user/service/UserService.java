package com.fulaan.restapi.user.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bson.types.ObjectId;

import com.db.user.UserDao;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.app.FieldValuePair;
import com.pojo.user.UserEntry;
import com.pojo.user.UserRole;
import com.sys.exceptions.IllegalParamException;
import com.sys.utils.ValidationUtils;

/**
 * 用户服务
 * @author fourer
 *
 */
public class UserService {

	
	private static final DBObject fields =new BasicDBObject("nm",1).append("r", 1).append("sid", 1);
	
			
	private UserDao userDao =new UserDao();
	
	
	/**
	 * 查询用户，用于用户登录
	 * @param name
	 * @return
	 */
	public UserEntry getUserEntry(String name) {
		UserEntry e=searchUserByUserName(name);
			
		if(null==e && ValidationUtils.isRequestModile(name) )
		{
			e=searchUserByMobile(name);
		}
		if(null==e && ValidationUtils.isEmail(name) )
		{
			e=searchUserByEmail(name);
		}
		if(null==e)
		{
			e=searchUserByUserLoginName(name);
		}
		if(null==e)
		{
			e=searchUserBySid(name);
		}
		return e;
	}
	
	
	 /**
     * 根据用户名精确查询
     * @param userName
     * @return
     */
    public UserEntry searchUserByUserName(String userName) {
    	return userDao.searchUserByUserName(userName);
    }
    
    
    /**
     * 根据用户手机登录
     * @param mobile
     * @return
     */
    public UserEntry searchUserByMobile(String mobile) 
    {
    	return userDao.searchUserByMobile(mobile);
    }
    
    /**
     * 根据用户邮箱
     * @param mobile
     * @return
     */
    public UserEntry searchUserByEmail(String email)
    {
    	return userDao.searchUserByEmail(email);
    }
    
    
    /**
     * 根据用户登录名精确查询
     * @param userLoginName
     * @return
     */
    public UserEntry searchUserByUserLoginName(String userLoginName) {
    	return userDao.searchUserByLoginName(userLoginName);
    }
    
   
    /**
     * 根据用户身份证查询
     * @param sid
     * @return
     */
    public UserEntry searchUserBySid(String sid) {
    	return userDao.searchUserBySid(sid);
    }
    
    /**
     * 更新用户字段
     * @param userId
     * @param field
     * @param value
     * @throws IllegalParamException
     */
    public void update(ObjectId userId,String field,Object value) 
    {
    	try {
			userDao.update(userId, field, value, false);
		} catch (IllegalParamException e) {
		}
    }
    
    
    /**
     * 更新多个用户字段
     * @param userId
     * @param pairs
     */
    public void update(ObjectId userId,FieldValuePair... pairs )
    {
    	userDao.update(userId, pairs);
    }
    
    /**
     * 根据用户ID查询用户entry
     * @param userId
     * @param fields
     * @return
     */
    public UserEntry getUserEntry(ObjectId userId,DBObject fields)
    {
    	return userDao.getUserEntry(userId, fields);
    }
    
    /**
     * 根据用户ID查找用户信息
     * @param ids
     * @param fields
     * @return
     */
    public List<UserEntry> getUserEntryList(Collection<ObjectId> ids,int field)
    {
    	return userDao.getUserEntryList(ids,getField(field));
    }
    
    
    /**
     * 
     * @param schoolId 
     * @param roleList
     * @param userName
     * @param field
     * @return
     * @throws Exception 
     */
    public List<UserEntry> getUserEntryList(Collection<ObjectId> schoolIds,  List<UserRole> roleList,String userName, int skip,int limit,int field) throws Exception
    {
    	List<Integer> roles =getUserRoleIntegers(roleList);
    	return userDao.getUserEntryList(schoolIds,roles,userName,getField(field),skip,limit);
    }
    
    
    
    /**
     * 得到Fields Object
     * @param field
     * @return
     */
    private DBObject getField(int field)
    {
    	return fields;
    }
    
    
  
   

    /**
     * 得到roleList 所有组合形式；比如 2,4,8 得到 2,4,6,8,10,12,14
     * @param roleList
     * @return
     */
    private List<Integer> getUserRoleIntegers(List<UserRole> roleList)
    {
    	List<Integer> list =new ArrayList<Integer>();
    	for(UserRole role:roleList)
    	{
    		list=getUserRoleIntegers(list,role);
    	}
    	return list;
    }
    
    
    private List<Integer> getUserRoleIntegers(List<Integer> roleInt,UserRole role)
    {
    	if(roleInt.size()==0)
    	{
    		roleInt.add(role.getRole());
    		return roleInt; 
    	}
    	List<Integer> newList =new ArrayList<Integer>(roleInt.size()*2+1);
    	newList.addAll(roleInt);
    	newList.add(roleInt.size(), role.getRole());
    	for(int i=0;i<roleInt.size();i++)
    	{
    		newList.add(roleInt.size()+1+i,roleInt.get(i)+ role.getRole());
    	}
    	return newList;
    }
    
    public static void main(String[] args) {
		
    	UserService us =new UserService();
    	List<UserRole> roleList=new ArrayList<UserRole>();
    	roleList.add(UserRole.STUDENT);
    	roleList.add(UserRole.TEACHER);
    	roleList.add(UserRole.PARENT);
    	roleList.add(UserRole.HEADMASTER);
    	
    	
    	List<Integer> ll=us.getUserRoleIntegers(roleList);
    	System.out.println(ll);
	}
    
}
