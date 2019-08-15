package com.db.user;

import com.db.base.SynDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.app.FieldValuePair;
import com.pojo.app.IdValuePair;
import com.pojo.user.UserEntry;
import com.pojo.user.UserRole;
import com.pojo.user.UserEntry.Binds;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import com.sys.exceptions.IllegalParamException;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;

import java.util.*;


/**
 * 用户Dao ,包括学生和老师等信息
 * index:nm_chatid_cid_si_ir
 * {"nm":1,"chatid":1,"cid":1,"si":1,"ir":1}
 *
 * @author fourer
 */

public class UserDao extends SynDao {

    /**
     * 添加用户
     *
     * @param e
     * @return
     */
    public ObjectId addUserEntry(UserEntry e) {
        /**
         * 如果没有聊天账号，则_id代替
         */
        if (StringUtils.isBlank(e.getChatId())) {
            if (null == e.getID()) {
                e.setID(new ObjectId());
            }
            e.setChatId(e.getID().toString());
        }
        if (e.getAvatar().equals("")) {
            e.setAvatar("head-default-head.jpg");
        }


        if (e.getRole() == UserRole.HEADMASTER.getRole()) {
            e.setRole(UserRole.HEADMASTER.getRole() | UserRole.TEACHER.getRole());
        }
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, e.getBaseEntry());
        return e.getID();
    }


    /**
     * 更新字段值
     *
     * @param userId
     * @param field     nm和sex不允许更新
     * @param value
     * @param isNeedSyn 是否需要同步
     * @throws IllegalParamException
     */
    public void update(ObjectId userId, String field, Object value, boolean isNeedSyn) throws IllegalParamException {
        if (StringUtils.isBlank(field))
            throw new IllegalParamException();
        BasicDBObject query = new BasicDBObject(Constant.ID, userId);
        BasicDBObject valueDBO = new BasicDBObject(field, value);
        if (isNeedSyn) {
            valueDBO.append(Constant.FIELD_SYN, Constant.SYN_YES_NEED);
        }
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, valueDBO);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, updateValue);
    }

    /**
     * 更新多个字段值
     *
     * @param userId
     * @param pairs
     */
    public void update(ObjectId userId, FieldValuePair... pairs) {
        BasicDBObject query = new BasicDBObject(Constant.ID, userId);
        BasicDBObject valueDBO = new BasicDBObject();
        for (FieldValuePair pair : pairs) {
            valueDBO.append(pair.getField(), pair.getValue());
        }
        valueDBO.append(Constant.FIELD_SYN, Constant.SYN_YES_NEED);
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, valueDBO);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, updateValue);
    }


    public void update(ObjectId userId, UserEntry entry) {
        BasicDBObject query = new BasicDBObject(Constant.ID, userId);
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, entry.getBaseEntry());
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, updateValue);
    }


    /**
     * 根据用户id查询
     *
     * @param userId
     * @return
     */
    public UserEntry getUserEntry(ObjectId userId, DBObject fields) {
        BasicDBObject query = new BasicDBObject(Constant.ID, userId);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, fields);
        if (null != dbo) {
            return new UserEntry((BasicDBObject) dbo);
        }
        return null;
    }

    /**
     * 根据用户id查询
     *
     * @param userId
     * @return
     */
    public List<UserEntry> getUserEntrysByChatids(List<String> chatids, DBObject fields) {
        List<UserEntry> retList = new ArrayList<UserEntry>();
        BasicDBObject query = new BasicDBObject("chatid", new BasicDBObject(Constant.MONGO_IN, chatids)).append("ir", Constant.ZERO);
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, fields);
        if (null != list && !list.isEmpty()) {
            for (DBObject dbo : list) {
                retList.add(new UserEntry((BasicDBObject) dbo));
            }
        }
        return retList;

    }

    public List<UserEntry> getUserEntrysByLogn(DBObject fields) {
        List<UserEntry> retList = new ArrayList<UserEntry>();
        BasicDBObject query = new BasicDBObject("logn", new BasicDBObject("$exists", true)).append("ir", Constant.ZERO);
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, fields);
        if (null != list && !list.isEmpty()) {
            for (DBObject dbo : list) {
                retList.add(new UserEntry((BasicDBObject) dbo));
            }
        }
        return retList;

    }


    /**
     * 根据用户id查询
     *
     * @param userId
     * @return
     */
    public UserEntry getUserEntryByChatid(String userId, DBObject fields) {
        BasicDBObject query = new BasicDBObject("chatid", userId);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, fields);
        if (null != dbo) {
            return new UserEntry((BasicDBObject) dbo);
        }
        return null;
    }


    /**
     * 根据家长ID查询
     *
     * @param parentId
     * @return
     */
    public UserEntry getUserEntryByParentId(ObjectId parentId) {
        BasicDBObject query = new BasicDBObject("cid", parentId);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, Constant.FIELDS);
        if (null != dbo) {
            return new UserEntry((BasicDBObject) dbo);
        }
        return null;
    }

    /**
     * 根据id集合查询多个用户
     *
     * @param ids
     * @param fields 需要用到的字段
     * @return
     */
    public List<UserEntry> getUserEntryList(Collection<ObjectId> ids, DBObject fields) {
        List<UserEntry> retList = new ArrayList<UserEntry>();
        BasicDBObject query = new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_IN, ids)).append("ir", Constant.ZERO);
        BasicDBObject order = new BasicDBObject("lt", 1);
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, fields, order);
        if (null != list && !list.isEmpty()) {
            for (DBObject dbo : list) {
                retList.add(new UserEntry((BasicDBObject) dbo));
            }
        }
        return retList;
    }

    /**
     * 根据id集合查询多个用户map
     *
     * @param ids
     * @param fields 需要用到的字段
     * @return
     */
    public Map<ObjectId, UserEntry> getUserEntryMap(Collection<ObjectId> ids, DBObject fields) {
        Map<ObjectId, UserEntry> retMap = new HashMap<ObjectId, UserEntry>();
        BasicDBObject query = new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_IN, ids));
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, fields);
        if (null != list && !list.isEmpty()) {
            UserEntry e;
            for (DBObject dbo : list) {
                e = new UserEntry((BasicDBObject) dbo);
                retMap.put(e.getID(), e);
            }
        }
        return retMap;
    }

    /**
     * 根据id集合查询多个用户map
     *
     * @param ids
     * @param fields 需要用到的字段
     * @return
     */
    public Map<ObjectId, UserEntry> getUserEntryMap(Collection<ObjectId> ids, String userName, DBObject fields) {
        Map<ObjectId, UserEntry> retMap = new HashMap<ObjectId, UserEntry>();
        BasicDBObject query = new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_IN, ids));
        if (!StringUtils.isEmpty(userName)) {
            query.append("nm", MongoUtils.buildRegex(userName));
        }
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, fields);
        if (null != list && !list.isEmpty()) {
            UserEntry e;
            for (DBObject dbo : list) {
                e = new UserEntry((BasicDBObject) dbo);
                retMap.put(e.getID(), e);
            }
        }
        return retMap;
    }

    public List<UserEntry> getUserEntryMap(List<ObjectId> ids, int sex, List<Integer> roles, DBObject fields, ObjectId sid) {
        List<UserEntry> retMap = new ArrayList<UserEntry>();
        BasicDBObject query = new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_IN, ids)).append("si", sid);
        if (sex != -1) {
//
            query.append("sex", sex);
        }
//        if(roles!=null){
//            query.append("r",new BasicDBObject(Constant.MONGO_IN,roles));
//        }
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, fields);
        if (null != list && !list.isEmpty()) {
            for (DBObject dbo : list) {
                retMap.add(new UserEntry((BasicDBObject) dbo));
            }
        }
        return retMap;
    }

    public Map<ObjectId, UserEntry> getUserEntryMapt(List<ObjectId> ids, int sex, List<Integer> roles, DBObject fields, ObjectId sid) {
        Map<ObjectId, UserEntry> retMap = new HashMap<ObjectId, UserEntry>();
        BasicDBObject query = new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_IN, ids)).append("si", sid);
        if (sex != -1) {
//
            query.append("sex", sex);
        }
//        if(roles!=null){
//            query.append("r",new BasicDBObject(Constant.MONGO_IN,roles));
//        }
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, fields);
        if (null != list && !list.isEmpty()) {
            UserEntry e;
            for (DBObject dbo : list) {
                e = new UserEntry((BasicDBObject) dbo);
                retMap.put(e.getID(), e);
            }
        }
        return retMap;
    }

    /**
     * 根据id集合查询多个用户map
     *
     * @param ids
     * @param fields 需要用到的字段
     * @return
     */
    public Map<ObjectId, UserEntry> getUserEntryMap2(Collection<ObjectId> ids, DBObject fields) {
        Map<ObjectId, UserEntry> retMap = new HashMap<ObjectId, UserEntry>();
        BasicDBObject query = new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_IN, ids));
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, fields);
        if (null != list && !list.isEmpty()) {
            UserEntry e;
            for (DBObject dbo : list) {
                e = new UserEntry((BasicDBObject) dbo);
                retMap.put(e.getID(), e);
            }
        }
        return retMap;
    }

    public Map<ObjectId,UserEntry> getUserMapBySchoolId(ObjectId schoolId,DBObject fields){
    	Map<ObjectId, UserEntry> retMap =new HashMap<ObjectId, UserEntry>();
        BasicDBObject query =new BasicDBObject("si",schoolId);
        List<DBObject> list=find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME,query, fields);
        if(null!=list && !list.isEmpty())
        {
            UserEntry e;
            for(DBObject dbo:list)
            {
                e=new UserEntry((BasicDBObject)dbo);
                retMap.put(e.getID(), e);
            }
        }
        return retMap;
    }

    /**
     * 根据用户名查id
     *
     * @return
     */
    public List<ObjectId> getUserIdsByUserName(Collection<String> userNames) {

        BasicDBObject query = new BasicDBObject("nm", new BasicDBObject(Constant.MONGO_IN, userNames));
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, new BasicDBObject(Constant.ID, 1));
        List<ObjectId> retList = new ArrayList<ObjectId>();
        if (null != list && !list.isEmpty()) {
            UserEntry e;
            for (DBObject dbo : list) {
                e = new UserEntry((BasicDBObject) dbo);
                retList.add(e.getID());
            }
        }
        return retList;
    }


    /**
     * 根据用户名查询，用于登录
     *
     * @param userName
     * @return
     */
    public List<UserEntry> searchUsers(String userName, int page, Integer pagesize) {
        BasicDBObject query = new BasicDBObject("nnm", userName).append("ir", Constant.ZERO);
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, Constant.FIELDS, Constant.MONGO_SORTBY_DESC, (page - 1) * pagesize, pagesize);
        List<UserEntry> userEntryList = new ArrayList<UserEntry>();
        for (DBObject dbObject : dbObjectList) {
            UserEntry userEntry = new UserEntry((BasicDBObject) dbObject);
            userEntryList.add(userEntry);
        }
        return userEntryList;
    }

    /**
     * 根据用户名统计总数
     *
     * @param userName
     * @return
     */
    public int countUsers(String userName) {
        BasicDBObject query = new BasicDBObject("nnm", userName).append("ir", Constant.ZERO);
        int total = count(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query);
        return total;
    }

    /**
     * 根据用户名精确查询，用于用户登录
     *
     * @param userName
     * @return
     */
    public UserEntry searchUserByUserName(String userName) {
        //BasicDBObject query=new BasicDBObject("nm",MongoUtils.buildRegex(userName)).append("ir", Constant.ZERO);
        BasicDBObject query = new BasicDBObject("nm", userName.toLowerCase()).append("ir", Constant.ZERO);
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, Constant.FIELDS);
        if (null != list && list.size() > 0) {
            UserEntry e = null;
            for (DBObject dbo : list) {
                e = new UserEntry((BasicDBObject) dbo);
                if (e.getRealUserName().equalsIgnoreCase(userName)) {
                    return e;
                }
            }
        }
        return null;
    }

    public UserEntry searchUserByLognName(String userName) {
        //BasicDBObject query=new BasicDBObject("nm",MongoUtils.buildRegex(userName)).append("ir", Constant.ZERO);
        BasicDBObject query = new BasicDBObject("logn", userName.toLowerCase()).append("ir", Constant.ZERO);
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, Constant.FIELDS);
        if (null != list && list.size() > 0) {
            UserEntry e = null;
            for (DBObject dbo : list) {
                e = new UserEntry((BasicDBObject) dbo);
                if (e.getLoginName().equalsIgnoreCase(userName)) {
                    return e;
                }
            }
        }
        return null;
    }

    /**
     * 返回重名的
     *
     * @param userName
     * @return
     */
    public List<UserEntry> searchUserByUserName(List<String> userName) {
        List<UserEntry> userEntryList = new ArrayList<UserEntry>();
        BasicDBObject query = new BasicDBObject("logn", new BasicDBObject(Constant.MONGO_IN, userName)).append("ir", Constant.ZERO);
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, Constant.FIELDS);
        if (null != list && list.size() > 0) {
            for (DBObject dbObject : list) {
                UserEntry userEntry = new UserEntry((BasicDBObject) dbObject);
                userEntryList.add(userEntry);
            }
        }
        return userEntryList;
    }
    public List<UserEntry> searchUserByUserMn(List<String> ms) {
        List<UserEntry> userEntryList = new ArrayList<UserEntry>();
        BasicDBObject query = new BasicDBObject("mn", new BasicDBObject(Constant.MONGO_IN, ms)).append("ir", Constant.ZERO);
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, Constant.FIELDS);
        if (null != list && list.size() > 0) {
            for (DBObject dbObject : list) {
                UserEntry userEntry = new UserEntry((BasicDBObject) dbObject);
                userEntryList.add(userEntry);
            }
        }
        return userEntryList;
    }

    public List<UserEntry> searchUserByUserMn(List<String> ms, List<ObjectId> us) {
        List<UserEntry> userEntryList = new ArrayList<UserEntry>();
        BasicDBObject query = new BasicDBObject("mn", new BasicDBObject(Constant.MONGO_IN, ms)).append("ir", Constant.ZERO).append("_id", new BasicDBObject(Constant.MONGO_NOTIN, us));
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, Constant.FIELDS);
        if (null != list && list.size() > 0) {
            for (DBObject dbObject : list) {
                UserEntry userEntry = new UserEntry((BasicDBObject) dbObject);
                userEntryList.add(userEntry);
            }
        }
        return userEntryList;
    }
    public List<UserEntry> searchUserByUserNames(List<String> userName) {
        List<UserEntry> userEntryList = new ArrayList<UserEntry>();
        BasicDBObject query = new BasicDBObject("nm", new BasicDBObject(Constant.MONGO_IN, userName)).append("ir", Constant.ZERO);
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, Constant.FIELDS);
        if (null != list && list.size() > 0) {
            for (DBObject dbObject : list) {
                UserEntry userEntry = new UserEntry((BasicDBObject) dbObject);

                userEntryList.add(userEntry);
            }
        }
        return userEntryList;
    }


    public List<UserEntry> searchUserByUserName(List<String> userName, List<ObjectId> us) {
        List<UserEntry> userEntryList = new ArrayList<UserEntry>();
        BasicDBObject query = new BasicDBObject("logn", new BasicDBObject(Constant.MONGO_IN, userName)).append("ir", Constant.ZERO).append("_id", new BasicDBObject(Constant.MONGO_NOTIN, us));
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, Constant.FIELDS);
        if (null != list && list.size() > 0) {
            for (DBObject dbObject : list) {
                UserEntry userEntry = new UserEntry((BasicDBObject) dbObject);
                userEntryList.add(userEntry);
            }
        }
        return userEntryList;
    }

    public List<UserEntry> searchUserByUserNames(List<String> userName, List<ObjectId> us) {
        List<UserEntry> userEntryList = new ArrayList<UserEntry>();
        BasicDBObject query = new BasicDBObject("nm", new BasicDBObject(Constant.MONGO_IN, userName)).append("ir", Constant.ZERO).append("_id", new BasicDBObject(Constant.MONGO_NOTIN, us));
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, Constant.FIELDS);
        if (null != list && list.size() > 0) {
            for (DBObject dbObject : list) {
                UserEntry userEntry = new UserEntry((BasicDBObject) dbObject);

                userEntryList.add(userEntry);
            }
        }
        return userEntryList;
    }


    /**
     * 根据用户登录名精确查询，用于用户登录
     *
     * @param userName
     * @return
     */
    public UserEntry searchUserByLoginName(String loginName) {
        BasicDBObject query = new BasicDBObject("logn", loginName.toLowerCase()).append("ir", Constant.ZERO);
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, Constant.FIELDS);
        if (null != list && list.size() > 0) {
            UserEntry e = null;
            for (DBObject dbo : list) {
                e = new UserEntry((BasicDBObject) dbo);
                if (e.getLoginName().equalsIgnoreCase(loginName)) {
                    return e;
                }
            }
        }
        return null;
    }


    /**
     * 根据用户身份证查询，用于用户登录
     *
     * @param userName
     * @return
     */
    public UserEntry searchUserBySid(String sid) {
        BasicDBObject query = new BasicDBObject("sid", sid.toLowerCase()).append("ir", Constant.ZERO);
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, Constant.FIELDS);
        if (null != list && list.size() > 0) {
            UserEntry e = null;
            for (DBObject dbo : list) {
                e = new UserEntry((BasicDBObject) dbo);
                return e;
            }
        }
        return null;
    }


    /**
     * 根据用户手机登录，用于用户登录
     *
     * @param userName
     * @return
     */
    public UserEntry searchUserByMobile(String mobile) {
        BasicDBObject query = new BasicDBObject("mn", mobile.toLowerCase()).append("ir", Constant.ZERO);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, Constant.FIELDS);
        if (null != dbo) {
            UserEntry e = new UserEntry((BasicDBObject) dbo);
            return e;

        }
        return null;
    }

    /**
     * 根据用户绑定来查询
     *
     * @param userName
     * @return
     */
    public UserEntry searchUserByUserBind(int type, String bindValue) {
        Binds ub = new Binds(type, bindValue);
        BasicDBObject query = new BasicDBObject("binds", ub.getBaseEntry()).append("ir", Constant.ZERO);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, Constant.FIELDS);
        if (null != dbo) {
            UserEntry e = new UserEntry((BasicDBObject) dbo);
            return e;
        }
        return null;
    }


    /**
     * 查询没有绑定的学生
     *
     * @return
     */
    public UserEntry getUserByNoBind(ObjectId schoolId) {
        BasicDBObject query = new BasicDBObject("binds", null).append("si", schoolId).append("r", 1).append("ir", 0);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, Constant.FIELDS);
        if (null != dbo) {
            UserEntry e = new UserEntry((BasicDBObject) dbo);
            return e;
        }
        return null;
    }


    /**
     * 根据用户邮箱，用于用户登录
     *
     * @param userName
     * @return
     */
    public UserEntry searchUserByEmail(String email) {
        BasicDBObject query = new BasicDBObject("e", email.toLowerCase()).append("ir", Constant.ZERO);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, Constant.FIELDS);
        if (null != dbo) {
            UserEntry e = new UserEntry((BasicDBObject) dbo);
            return e;

        }
        return null;
    }


    /**
     * 查询校领导
     *
     * @param schoolId
     * @return
     */
    public List<UserEntry> getSchoolLeader(ObjectId schoolId) {
        List<UserEntry> userEntryList = new ArrayList<UserEntry>();
        BasicDBObject query = new BasicDBObject("si", schoolId).append("r", new BasicDBObject(Constant.MONGO_GTE, UserRole.HEADMASTER.getRole()));
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, Constant.FIELDS);
        for (DBObject dbObject : dbObjectList) {
            UserEntry userEntry = new UserEntry((BasicDBObject) dbObject);
            if (UserRole.isHeadmaster(userEntry.getRole())) {
                userEntryList.add(userEntry);
            }
        }
        return userEntryList;
    }


    /**
     * 逻辑删除用户；
     * 此处必须是逻辑删除
     *
     * @param uid
     */
    public void logicRemoveUser(ObjectId uid) {
        BasicDBObject query = new BasicDBObject(Constant.ID, uid);
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("ir", Constant.ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, updateValue);
    }


    public List<UserEntry> searchUsersWithRole(String userName, Integer roleType, Integer page, Integer pagesize) {
        BasicDBObject query = new BasicDBObject("nnm", userName).append("r", roleType).append("ir", Constant.ZERO);
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, Constant.FIELDS, Constant.MONGO_SORTBY_DESC, (page - 1) * pagesize, pagesize);

        List<UserEntry> userEntryList = new ArrayList<UserEntry>();
        for (DBObject dbObject : dbObjectList) {
            UserEntry userEntry = new UserEntry((BasicDBObject) dbObject);
            userEntryList.add(userEntry);
        }
        return userEntryList;
    }

    /**
     * 根据用户名和角色统计人数
     *
     * @param userName
     * @param roleType
     * @return
     */
    public int countUsersWithRole(String userName, Integer roleType) {
        BasicDBObject query = new BasicDBObject("nnm", userName).append("r", roleType).append("ir", Constant.ZERO);
        int total = count(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query);
        return total;
    }

    public List<UserEntry> searchUsersWithSchool(String userName, ObjectId schoolId, Integer page, Integer pagesize) {
        BasicDBObject query = new BasicDBObject("nm", MongoUtils.buildRegex(userName)).append("si", schoolId).append("ir", Constant.ZERO);
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, Constant.FIELDS, Constant.MONGO_SORTBY_DESC, (page - 1) * pagesize, pagesize);

        List<UserEntry> userEntryList = new ArrayList<UserEntry>();
        for (DBObject dbObject : dbObjectList) {
            UserEntry userEntry = new UserEntry((BasicDBObject) dbObject);
            userEntryList.add(userEntry);
        }
        return userEntryList;
    }

    /**
     * 根据用户名和年级查找，此处已经获取到年级内所有用户
     *
     * @param userName
     * @param usersInGrade
     * @param page
     * @return
     */
    public List<UserEntry> searchUsersInGrade(String userName, List<ObjectId> usersInGrade, Integer page, Integer pagesize) {
        BasicDBObject query = new BasicDBObject("nm", MongoUtils.buildRegex(userName)).append(Constant.ID, new BasicDBObject(Constant.MONGO_IN, usersInGrade)).append("ir", Constant.ZERO);
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, Constant.FIELDS, Constant.MONGO_SORTBY_DESC, (page - 1) * pagesize, pagesize);

        List<UserEntry> userEntryList = new ArrayList<UserEntry>();
        for (DBObject dbObject : dbObjectList) {
            UserEntry userEntry = new UserEntry((BasicDBObject) dbObject);
            userEntryList.add(userEntry);
        }
        return userEntryList;
    }

    /**
     * 根据用户名和年级统计总数
     *
     * @param userName
     * @param usersInGrade
     * @return
     */
    public int countUsersInGrade(String userName, List<ObjectId> usersInGrade) {
        BasicDBObject query = new BasicDBObject("nm", MongoUtils.buildRegex(userName)).append(Constant.ID, new BasicDBObject(Constant.MONGO_IN, usersInGrade)).append("ir", Constant.ZERO);
        int total = count(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query);
        return total;
    }

    /**
     * 根据用户名和学校Id统计好友总数
     *
     * @param userName
     * @param schoolId
     * @return
     */
    public int countUsersWithSchool(String userName, ObjectId schoolId) {
        BasicDBObject query = new BasicDBObject("nm", MongoUtils.buildRegex(userName)).append("si", schoolId).append("ir", Constant.ZERO);
        int total = count(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query);
        return total;
    }

    public List<UserEntry> searchUsersWithSchoolAndRole(String userName, ObjectId schoolId, Integer roleType, Integer page, Integer pagesize) {
        BasicDBObject query = new BasicDBObject("nm", MongoUtils.buildRegex(userName)).append("r", roleType).append("si", schoolId).append("ir", Constant.ZERO);
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, Constant.FIELDS, Constant.MONGO_SORTBY_DESC, (page - 1) * pagesize, pagesize);

        List<UserEntry> userEntryList = new ArrayList<UserEntry>();
        for (DBObject dbObject : dbObjectList) {
            UserEntry userEntry = new UserEntry((BasicDBObject) dbObject);
            userEntryList.add(userEntry);
        }
        return userEntryList;
    }

    /**
     * 根据用户名、学校Id、角色类型统计好友总数
     *
     * @param userName
     * @param schoolId
     * @param roleType
     * @return
     */
    public int countUsersWithSchoolAndRole(String userName, ObjectId schoolId, Integer roleType) {
        BasicDBObject query = new BasicDBObject("nm", MongoUtils.buildRegex(userName)).append("r", roleType).append("si", schoolId).append("ir", Constant.ZERO);
        int total = count(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query);
        return total;
    }

    public List<UserEntry> findUserEntriesLimitRole(List<ObjectId> objectIdList, BasicDBObject fields) {
        List<UserEntry> retList = new ArrayList<UserEntry>();
        BasicDBObject query = new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_IN, objectIdList)).append("r", UserRole.STUDENT.getRole()).append("ir", Constant.ZERO);
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, fields);
        if (null != list && !list.isEmpty()) {
            for (DBObject dbo : list) {
                retList.add(new UserEntry((BasicDBObject) dbo));
            }
        }
        return retList;
    }


    public void resetPwd(ObjectId id, String initPwd) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        BasicDBObject update = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("pw", initPwd));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, update);
    }

    public void updateNickNameAndSexById(ObjectId objectId, String stnickname, int sex) {
        BasicDBObject query = new BasicDBObject(Constant.ID, objectId);
        BasicDBObject update = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("nnm", stnickname).append("sex", sex));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, update);
    }

    /**
     * 通过学校查询
     *
     * @param schoolId
     * @param fields
     * @return
     */
    public List<UserEntry> getUserEntryBySchoolIdList(ObjectId schoolId, BasicDBObject fields) {
        BasicDBObject query = new BasicDBObject("si", schoolId).append("ir", Constant.ZERO);
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, fields, new BasicDBObject("r", 1));
        List<UserEntry> userEntryList = new ArrayList<UserEntry>();
        for (DBObject dbObject : dbObjectList) {
            UserEntry userEntry = new UserEntry((BasicDBObject) dbObject);
            userEntryList.add(userEntry);
        }
        return userEntryList;
    }

    public List<UserEntry> getUserBySchoolIdAndRole(ObjectId schoolId, int role, BasicDBObject fields) {
        BasicDBObject query = new BasicDBObject("si", schoolId)
                .append("ir", Constant.ZERO);
        if (role != -1) {
            query.append("r", role);
        }
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, fields, new BasicDBObject("r", 1));
        List<UserEntry> userEntryList = new ArrayList<UserEntry>();
        for (DBObject dbObject : dbObjectList) {
            UserEntry userEntry = new UserEntry((BasicDBObject) dbObject);
            userEntryList.add(userEntry);
        }
        return userEntryList;
    }

    public List<UserEntry> getUserBySchoolIdAndNoRole(ObjectId schoolId, int noRole, BasicDBObject fields) {
        BasicDBObject query = new BasicDBObject("si", schoolId)
                .append("ir", Constant.ZERO);
        query.append("r", new BasicDBObject(Constant.MONGO_NE, noRole));
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, fields, new BasicDBObject("r", 1));
        List<UserEntry> userEntryList = new ArrayList<UserEntry>();
        for (DBObject dbObject : dbObjectList) {
            UserEntry userEntry = new UserEntry((BasicDBObject) dbObject);
            userEntryList.add(userEntry);
        }
        return userEntryList;
    }

    public List<UserEntry> getUserEntryBySchoolId(ObjectId schoolId, List<Integer> role, BasicDBObject fields, int skip, int limit) {
        BasicDBObject query = new BasicDBObject().append("ir", Constant.ZERO);
        if (schoolId != null) {
            query.append("si", schoolId);
        }
        if (role != null) {
            query.append("r", new BasicDBObject(Constant.MONGO_IN, role));
        }
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, fields, new BasicDBObject("r", 1), skip, limit);
        List<UserEntry> userEntryList = new ArrayList<UserEntry>();
        for (DBObject dbObject : dbObjectList) {
            UserEntry userEntry = new UserEntry((BasicDBObject) dbObject);
            userEntryList.add(userEntry);
        }
        return userEntryList;
    }

    public int countUserEntryBySchoolId(ObjectId schoolId, List<Integer> role) {
        BasicDBObject query = new BasicDBObject().append("ir", Constant.ZERO);
        if (schoolId != null) {
            query.append("si", schoolId);
        }
        if (role != null) {
            query.append("r", new BasicDBObject(Constant.MONGO_IN, role));
        }
        int count = count(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query);

        return count;
    }

    public List<UserEntry> findEntryByUserName(String userName) {
        BasicDBObject query = new BasicDBObject("nm", userName);
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, Constant.FIELDS);
        List<UserEntry> userEntryList = new ArrayList<UserEntry>();
        if (list != null) {
            for (DBObject dbObject : list) {
                UserEntry userEntry = new UserEntry((BasicDBObject) dbObject);
                userEntryList.add(userEntry);
            }
        }
        return userEntryList;
    }

    public List<UserEntry> getUserInfoBySchoolid(ObjectId schoolID, DBObject fields) {
        List<UserEntry> retList = new ArrayList<UserEntry>();
        BasicDBObject query = new BasicDBObject("si", schoolID).append("ir", Constant.ZERO);
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, fields, Constant.MONGO_SORTBY_ASC);
        if (null != list && !list.isEmpty()) {
            for (DBObject dbo : list) {
                retList.add(new UserEntry((BasicDBObject) dbo));
            }
        }
        return retList;
    }

    public void updateUserGroupList(String id, IdValuePair idvalue) {
        BasicDBObject query = new BasicDBObject("chatid", id);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_PUSH, new BasicDBObject("gli", idvalue.getBaseEntry()));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, updateValue);
    }

    public List<UserEntry> findParentEntryByStuId(ObjectId stuId) {
        List<UserEntry> userEntryList = new ArrayList<UserEntry>();
        BasicDBObject query = new BasicDBObject("cid", stuId);
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, Constant.FIELDS);
        if (dbObjectList != null && dbObjectList.size() != 0) {
            for (DBObject dbObject : dbObjectList) {
                UserEntry userEntry = new UserEntry((BasicDBObject) dbObject);
                userEntryList.add(userEntry);
            }
        }
        return userEntryList;
    }

    public void deleteUserGroupList(String id, IdValuePair idvalue) {
        BasicDBObject query = new BasicDBObject("chatid", id);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_PULL, new BasicDBObject("gli", idvalue.getBaseEntry()));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, updateValue);
    }

    public void updateIntroduction(String introduce, ObjectId id) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        BasicDBObject update = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("int", introduce));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, update);
    }

    /**
     * 批量修改用户密码
     *
     * @param userIds
     * @param password
     */
    public boolean resetSelectPassword(List<ObjectId> userIds, String password) {
        BasicDBObject query = new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_IN, userIds));
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("pw", password));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, updateValue);
        return true;
    }

    /**
     * 批量修改一个班级的学生学号
     *
     * @param userEntries
     */
    public void updateStudents(List<UserEntry> userEntries) {
        for (UserEntry userEntry : userEntries) {
            BasicDBObject query = new BasicDBObject("nm", userEntry.getUserName());
            BasicDBObject update = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("sn", userEntry.getStudyNum()).append("jo", userEntry.getJob()));
            update(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, update);
        }

    }

    /**
     * 修改学号
     *
     * @param userId
     * @param stuNum
     */
    public void updateStudentNum(ObjectId userId, String stuNum) {
        BasicDBObject query = new BasicDBObject(Constant.ID, userId);
        BasicDBObject update = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("sn", stuNum));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, update);
    }

    /**
     * 修改职务
     *
     * @param userId
     * @param stuJob
     */
    public void updateStudentJob(ObjectId userId, String stuJob) {
        BasicDBObject query = new BasicDBObject(Constant.ID, userId);
        BasicDBObject update = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("jo", stuJob));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, update);
    }


    /**
     * 通过学校查询本学校的老师(在职老师、用于薪资制表数据)
     *
     * @param schoolId
     * @param fields
     * @return
     */
    public List<UserEntry> getTeacherEntryBySchoolId(ObjectId schoolId, String teaName, BasicDBObject fields) {
        BasicDBObject query = new BasicDBObject("si", schoolId)
                .append("r", new BasicDBObject(Constant.MONGO_GTE, UserRole.TEACHER.getRole())
                        .append(Constant.MONGO_NE, UserRole.PARENT.getRole()))
                .append("ir", Constant.ZERO);
        if (StringUtils.isNotEmpty(teaName)) {
            query.append("nm", MongoUtils.buildRegex(teaName));
        }
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, fields);
        List<UserEntry> userEntryList = new ArrayList<UserEntry>();
        for (DBObject dbObject : dbObjectList) {
            UserEntry userEntry = new UserEntry((BasicDBObject) dbObject);
            userEntryList.add(userEntry);
        }
        return userEntryList;
    }

    /**
     * 通过学校查询本学校的老师(在职老师、用于薪资制表数据)
     *
     * @param schoolId
     * @param fields
     * @return
     */
    public List<UserEntry> getTeacherEntryBySchoolId(ObjectId schoolId, BasicDBObject fields) {
        BasicDBObject query = new BasicDBObject("si", schoolId)
                .append("r", new BasicDBObject(Constant.MONGO_GTE, UserRole.TEACHER.getRole())
                        .append(Constant.MONGO_NE, UserRole.PARENT.getRole()))
                .append("ir", Constant.ZERO);
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, fields);
        List<UserEntry> userEntryList = new ArrayList<UserEntry>();
        for (DBObject dbObject : dbObjectList) {
            UserEntry userEntry = new UserEntry((BasicDBObject) dbObject);
            userEntryList.add(userEntry);
        }
        return userEntryList;
    }

    /**
     * 通过学校查询本学校的老师
     *
     * @param schoolId
     * @param skip
     * @param limit
     * @param fields
     * @return
     */
    public List<UserEntry> getTeacherEntryBySchoolId(ObjectId schoolId, int skip, int limit, BasicDBObject fields) {
        BasicDBObject query = new BasicDBObject("si", schoolId)
                .append("r", new BasicDBObject(Constant.MONGO_GTE, UserRole.TEACHER.getRole())
                        .append(Constant.MONGO_NE, UserRole.PARENT.getRole()))
                .append("ir", Constant.ZERO);
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, fields, new BasicDBObject(Constant.ID, Constant.ASC), skip, limit);
        List<UserEntry> userEntryList = new ArrayList<UserEntry>();
        for (DBObject dbObject : dbObjectList) {
            UserEntry userEntry = new UserEntry((BasicDBObject) dbObject);
            userEntryList.add(userEntry);
        }
        return userEntryList;
    }

    /**
     * 通过学校查询本学校的学生
     *
     * @param schoolId
     * @param fields
     * @return
     */
    public List<UserEntry> getStudentEntryBySchoolId(ObjectId schoolId, BasicDBObject fields) {
        BasicDBObject query = new BasicDBObject("si", schoolId).append("ir", Constant.ZERO)
                .append("r", UserRole.STUDENT.getRole());
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, fields, Constant.MONGO_SORTBY_ASC);
        List<UserEntry> userEntryList = new ArrayList<UserEntry>();
        for (DBObject dbObject : dbObjectList) {
            UserEntry userEntry = new UserEntry((BasicDBObject) dbObject);
            userEntryList.add(userEntry);
        }
        return userEntryList;
    }

    public void updSchoolHomeDate(ObjectId id) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("shd", System.currentTimeMillis()));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, updateValue);

    }

    public void updFamilyHomeDate(ObjectId id) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("fhd", System.currentTimeMillis()));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, updateValue);
    }

    /**
     * 根据学生Id列表查找所有的家长Id
     *
     * @param studentIds
     * @return
     */
    public List<ObjectId> getParentIds(List<ObjectId> studentIds) {
        BasicDBObject query = new BasicDBObject();
        query.append("cid", new BasicDBObject(Constant.MONGO_IN, studentIds));
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, Constant.FIELDS);
        List<ObjectId> userIdList = new ArrayList<ObjectId>();
        for (DBObject dbObject : dbObjectList) {
            UserEntry userEntry = new UserEntry((BasicDBObject) dbObject);
            userIdList.add(userEntry.getID());
        }
        return userIdList;
    }

    public List<UserEntry> getParentEntrys(List<ObjectId> studentIds, DBObject fields) {
        BasicDBObject query = new BasicDBObject();
        query.append("cid", new BasicDBObject(Constant.MONGO_IN, studentIds));
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, fields);
        List<UserEntry> userEntries = new ArrayList<UserEntry>();
        for (DBObject dbObject : dbObjectList) {
            UserEntry userEntry = new UserEntry((BasicDBObject) dbObject);
            userEntries.add(userEntry);
        }
        return userEntries;
    }


    /**
     * 改方法只用于统计使用；
     *
     * @param skip
     * @param limit
     * @return
     */
    @Deprecated
    public List<UserEntry> getAllEntrys(int skip, int limit) {
        List<UserEntry> retList = new ArrayList<UserEntry>();
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, new BasicDBObject("lad", new BasicDBObject(Constant.MONGO_GT, 100)), Constant.FIELDS, Constant.MONGO_SORTBY_DESC, skip, limit);
        if (dbObjectList != null && dbObjectList.size() > 0) {

            for (DBObject dbo : dbObjectList) {
                UserEntry userEntry = new UserEntry((BasicDBObject) dbo);
                retList.add(userEntry);
            }
        }

        return retList;
    }

    public List<UserEntry> findUserRoleInfoBySchoolIds(List<ObjectId> schoolIds) {
        BasicDBObject query = new BasicDBObject("si", new BasicDBObject(Constant.MONGO_IN, schoolIds)).append("ir", Constant.ZERO);
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, new BasicDBObject("r", Constant.ONE));
        List<UserEntry> userEntryList = new ArrayList<UserEntry>();
        for (DBObject dbObject : dbObjectList) {
            UserEntry userEntry = new UserEntry((BasicDBObject) dbObject);
            userEntryList.add(userEntry);
        }
        return userEntryList;
    }


    public List<UserEntry> findUserEntriesLimitRoleAndKeyword(List<ObjectId> objectIdList, String keyword, BasicDBObject fields) {
        List<UserEntry> retList = new ArrayList<UserEntry>();
        BasicDBObject query = new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_IN, objectIdList)).append("r", UserRole.STUDENT.getRole());
        if (!StringUtils.isBlank(keyword)) {
            query.append("nm", MongoUtils.buildRegex(keyword));
        }
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, fields);
        if (null != list && !list.isEmpty()) {
            for (DBObject dbo : list) {
                retList.add(new UserEntry((BasicDBObject) dbo));
            }
        }
        return retList;
    }


    public boolean existUserInfo(String userName) {
        BasicDBObject query = new BasicDBObject("nm", userName);
        int count = count(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query);
        if (count > 0) return true;
        return false;
    }


    public List<UserEntry> selUserEntryList(Set<ObjectId> usIds, int skip, int limit, String orderBy) {
        List<UserEntry> retList = new ArrayList<UserEntry>();
        BasicDBObject query = new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_IN, usIds));
        BasicDBObject sort = null;
        if (!"".equals(orderBy)) {
            sort = new BasicDBObject(orderBy, Constant.DESC);
        } else {
            sort = new BasicDBObject("_id", Constant.ASC);
        }
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, Constant.FIELDS, sort, skip, limit);
        if (null != list && !list.isEmpty()) {
            for (DBObject dbo : list) {
                retList.add(new UserEntry((BasicDBObject) dbo));
            }
        }
        return retList;
    }


    public void updateExperenceValue(ObjectId userId) {
        DBObject query = new BasicDBObject(Constant.ID, userId);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_INC, new BasicDBObject("exp", 20));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, updateValue);
    }

    /**
     * 通过学校查询本学校的老师
     *
     * @param schoolIds
     * @param fields
     * @return
     */
    public List<UserEntry> getTeacherEntryBySchoolIds(Collection<ObjectId> schoolIds, BasicDBObject fields) {
        BasicDBObject query = new BasicDBObject("si", new BasicDBObject(Constant.MONGO_IN, schoolIds))
                .append("r", new BasicDBObject(Constant.MONGO_GTE, UserRole.TEACHER.getRole())
                        .append(Constant.MONGO_NE, UserRole.PARENT.getRole()))
                .append("ir", Constant.ZERO);
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, new BasicDBObject("_id", 1).append("nm", 1));
        List<UserEntry> userids = new ArrayList<UserEntry>();
        for (DBObject dbObject : dbObjectList) {
            UserEntry userEntry = new UserEntry((BasicDBObject) dbObject);
            userids.add(userEntry);
        }
        return userids;
    }


    /**
     * 通过学校,名字查询本学校的老师
     *
     * @param fields
     * @return
     */
    public List<UserEntry> getTeacherEntryBySchoolIdUserName(ObjectId schoolId, String userName, BasicDBObject fields) {
        BasicDBObject query = new BasicDBObject("si", schoolId)
                .append("r", new BasicDBObject(Constant.MONGO_GTE, UserRole.TEACHER.getRole())
                        .append(Constant.MONGO_NE, UserRole.PARENT.getRole()))
                .append("ir", Constant.ZERO);
        if (!StringUtils.isEmpty(userName)) {
            query.append("nm", MongoUtils.buildRegex(userName));
        }
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, fields);
        List<UserEntry> userids = new ArrayList<UserEntry>();
        for (DBObject dbObject : dbObjectList) {
            UserEntry userEntry = new UserEntry((BasicDBObject) dbObject);
            userids.add(userEntry);
        }
        return userids;
    }


    /**
     * 根据条件查询用户
     *
     * @param role
     * @param noUserIds
     * @return
     */
    public List<UserEntry> getUserListByParam(int role, String userName, Set<ObjectId> noUserIds) {
        List<UserEntry> retList = new ArrayList<UserEntry>();
        BasicDBObject query = new BasicDBObject();
        if (UserRole.isEducation(role)) {
            query.append("r", new BasicDBObject(Constant.MONGO_GTE, role));
        }
        if (noUserIds.size() > 0) {
            query.append(Constant.ID, new BasicDBObject(Constant.MONGO_NOTIN, noUserIds));
        }
        if (StringUtils.isNotBlank(userName)) {
            // query.append("nm", MongoUtils.buildRegex(userName));
            query.append("nnm", userName);
        }
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, Constant.FIELDS);
        if (null != list && !list.isEmpty()) {
            for (DBObject dbo : list) {
                retList.add(new UserEntry((BasicDBObject) dbo));
            }
        }
        return retList;
    }


    /**
     * 只做统计用，可以删除
     *
     * @param role
     * @param fields
     * @return
     */
    @Deprecated
    public List<UserEntry> getUserList(ObjectId si, BasicDBObject fields) {

        List<UserEntry> retList = new ArrayList<UserEntry>();
        BasicDBObject query = new BasicDBObject("si", si).append("r", 1).append("ir", 1);

        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, fields);
        if (null != list && !list.isEmpty()) {
            for (DBObject dbo : list) {
                retList.add(new UserEntry((BasicDBObject) dbo));
            }
        }
        return retList;
    }

    /**
     * 只做统计用，可以删除
     *
     * @param userName
     * @param fields
     * @return
     */
    @Deprecated
    public List<UserEntry> getUserList(String userName, BasicDBObject fields) {

        List<UserEntry> retList = new ArrayList<UserEntry>();
        BasicDBObject query = new BasicDBObject("nm", MongoUtils.buildRegex(userName)).append("ir", 0);

        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, fields);
        if (null != list && !list.isEmpty()) {
            for (DBObject dbo : list) {
                retList.add(new UserEntry((BasicDBObject) dbo));
            }
        }
        return retList;
    }

    /**
     * 根据用户名获取userInfo
     *
     * @param name
     * @return
     */
    public UserEntry getUserEntryByName(String name) {
        BasicDBObject query = new BasicDBObject("nm", name);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, null);
        if (null != dbo) {
            return new UserEntry((BasicDBObject) dbo);
        }
        return null;
    }


    /**
     * 根据绑定用户名获取userInfo
     *
     * @param name
     * @return
     */
    public UserEntry getUserEntryByBindName(String bindName) {
        BasicDBObject query = new BasicDBObject("bnm", bindName);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, null);
        if (null != dbo) {
            return new UserEntry((BasicDBObject) dbo);
        }
        return null;
    }

    /**
     * @param bindName
     * @param Type     1:省平台 2有鸿
     * @return
     */
    public UserEntry getUserEntryByBindName(String bindName, int type) {

        String field = "bnm";
        if (type == 2) {
            field = "yhbnm";
        }
        BasicDBObject query = new BasicDBObject(field, bindName);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, null);
        if (null != dbo) {
            return new UserEntry((BasicDBObject) dbo);
        }
        return null;
    }

    @Deprecated
    public UserEntry getUserEntryByName(String name, ObjectId sid) {
        BasicDBObject query = new BasicDBObject("nm", name).append("si", sid);

        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, Constant.FIELDS);
        if (null != dbo) {
            return new UserEntry((BasicDBObject) dbo);
        }
        return null;
    }


    @Deprecated
    public List<ObjectId> getUserForEBusiness(ObjectId schoolId, BasicDBObject fields) {
        BasicDBObject query = new BasicDBObject("si", schoolId);
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, fields, new BasicDBObject("si", 1), 1, 420);
        List<ObjectId> userids = new ArrayList<ObjectId>();
        for (DBObject dbObject : dbObjectList) {
            UserEntry userEntry = new UserEntry((BasicDBObject) dbObject);
            userids.add(userEntry.getID());
        }
        return userids;
    }

    /**
     * 增加减少经验值
     *
     * @param userId
     * @param exp
     */
    public void updateExperenceValue(ObjectId userId, int exp) {
        DBObject query = new BasicDBObject(Constant.ID, userId);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_INC, new BasicDBObject("exp", exp));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, updateValue);
    }

    /**
     * @param skip
     * @param limit
     * @return
     */
    public List<UserEntry> searchUser(String schoolId, String name, int jinyan, int skip, int limit) {
        List<UserEntry> retList = new ArrayList<UserEntry>();
        BasicDBObject query = new BasicDBObject("ir", Constant.ZERO);
        if (StringUtils.isNotEmpty(name)) {
            query.append("nm", MongoUtils.buildRegex(name));

//        	 query.append("nnm", name);
        }
        if (StringUtils.isNotEmpty(schoolId)) {
            query.append("si", new ObjectId(schoolId));
        }
        if (jinyan != 2) {
            query.append("ijy", jinyan);
        }
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, null, new BasicDBObject("_id", -1), skip, limit);
        if (null != list && !list.isEmpty()) {
            for (DBObject dbo : list) {
                retList.add(new UserEntry((BasicDBObject) dbo));
            }
        }
        return retList;

    }


    /**
     * @param skip
     * @param limit
     * @return
     */
    @Deprecated
    public List<UserEntry> searchUserByChatid(int skip, int limit) {
        List<UserEntry> retList = new ArrayList<UserEntry>();

        BasicDBList ll = new BasicDBList();
        ll.add(new BasicDBObject("chatid", ""));

        ll.add(new BasicDBObject("chatid", null));


        BasicDBObject query = new BasicDBObject(Constant.MONGO_OR, ll);
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, new BasicDBObject("_id", 1).append("chatid", 1), new BasicDBObject("_id", -1), skip, limit);
        if (null != list && !list.isEmpty()) {
            for (DBObject dbo : list) {
                retList.add(new UserEntry((BasicDBObject) dbo));
            }
        }
        return retList;

    }

    /**
     * count
     *
     * @param name
     * @return
     */
    public int searchUserCount(String schoolId, String name, int jinyan) {
        BasicDBObject query = new BasicDBObject("ir", Constant.ZERO);
        if (StringUtils.isNotEmpty(name)) {
            query.append("nm", MongoUtils.buildRegex(name));
//        	 query.append("nnm", name);
        }
        if (StringUtils.isNotEmpty(schoolId)) {
            query.append("si", new ObjectId(schoolId));
        }
        if (jinyan != 2) {
            query.append("ijy", jinyan);
        }
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query);
    }

    /**
     * 通过角色查询K6KT
     *
     * @param fields
     * @return
     */
    public List<UserEntry> getK6ktEntryByRoles(BasicDBObject fields) {
//        BasicDBObject query=new BasicDBObject("r", new BasicDBObject(Constant.MONGO_GTE, UserRole.K6KT_HELPER.getRole()))
//                .append("ir", Constant.ZERO);
        BasicDBObject query = new BasicDBObject("ir", Constant.ZERO);
        query.append("r", UserRole.K6KT_HELPER.getRole());

        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, fields, new BasicDBObject("_id", -1));
        List<UserEntry> userlist = new ArrayList<UserEntry>();
        for (DBObject dbObject : dbObjectList) {
            UserEntry userEntry = new UserEntry((BasicDBObject) dbObject);
            userlist.add(userEntry);
        }
        return userlist;
    }

    /**
     * 通过角色查询K6KT
     *
     * @param fields
     * @return
     */
    public List<UserEntry> getK6ktEntryByRoles2(BasicDBObject fields) {
//        BasicDBObject query=new BasicDBObject("r", new BasicDBObject(Constant.MONGO_GTE, UserRole.K6KT_HELPER.getRole()))
//                .append("ir", Constant.ZERO);
        BasicDBObject query = new BasicDBObject("ir", Constant.ZERO);
        query.append("nnm", "k6kt小助手");

        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, fields, new BasicDBObject("_id", -1));
        List<UserEntry> userlist = new ArrayList<UserEntry>();
        for (DBObject dbObject : dbObjectList) {
            UserEntry userEntry = new UserEntry((BasicDBObject) dbObject);
            userlist.add(userEntry);
        }
        return userlist;
    }

    /**
     * @param id
     * @param jinyan
     */
    public void isJinyan(ObjectId id, int jinyan) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("ijy", jinyan).append("jydt", new Date().getTime()));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, updateValue);
    }

    /**
     * 解除禁言
     *
     * @param id
     */
    public void updateJinYanTime(ObjectId id) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        query.append("jydt", new BasicDBObject(Constant.MONGO_LTE, new Date().getTime() - 604800000));
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("ijy", 0));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, updateValue);
    }

    public List<ObjectId> findIdListByUserName(String userName) {
        BasicDBObject query = new BasicDBObject("nnm", userName);
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, Constant.FIELDS);
        List<ObjectId> useridList = new ArrayList<ObjectId>();
        if (list != null) {
            for (DBObject dbObject : list) {
                UserEntry userEntry = new UserEntry((BasicDBObject) dbObject);
                useridList.add(userEntry.getID());
            }
        }
        return useridList;
    }

    public List<ObjectId> findIdListByUserName(List<ObjectId> userids, String userName) {
        BasicDBObject query = new BasicDBObject();
        query.append(Constant.ID, new BasicDBObject(Constant.MONGO_IN, userids));
        query.append("nm", MongoUtils.buildRegex(userName));
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, Constant.FIELDS);
        List<ObjectId> useridList = new ArrayList<ObjectId>();
        if (list != null) {
            for (DBObject dbObject : list) {
                UserEntry userEntry = new UserEntry((BasicDBObject) dbObject);
                useridList.add(userEntry.getID());
            }
        }
        return useridList;
    }

    public List<UserEntry> findIdListByUserNames(List<ObjectId> userids, String userName) {
        BasicDBObject query = new BasicDBObject();
        query.append(Constant.ID, new BasicDBObject(Constant.MONGO_IN, userids));
        query.append("nm", MongoUtils.buildRegex(userName));
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, Constant.FIELDS);
        List<UserEntry> useridList = new ArrayList<UserEntry>();
        if (list != null) {
            for (DBObject dbObject : list) {
                UserEntry userEntry = new UserEntry((BasicDBObject) dbObject);
                useridList.add(userEntry);
            }
        }
        return useridList;
    }
    public List<UserEntry> getUserInfoList(String keyword, int postion, String schoolId, List<ObjectId> userids) {
        BasicDBObject query = new BasicDBObject("si", new ObjectId(schoolId)).append("r", new BasicDBObject(Constant.MONGO_GTE, UserRole.TEACHER.getRole())
                .append(Constant.MONGO_NE, UserRole.PARENT.getRole()))
                .append("ir", Constant.ZERO);
        if (!StringUtils.isEmpty(keyword)) {
            query.append("nm", MongoUtils.buildRegex(keyword));
        }
        if (postion != 0) {
            query.append("pos", postion);
        }
        if (userids != null && userids.size() != 0) {
            query.append(Constant.ID, new BasicDBObject(Constant.MONGO_IN, userids));
        }
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, Constant.FIELDS, new BasicDBObject("lt", 1));
        List<UserEntry> useridList = new ArrayList<UserEntry>();
        if (list != null) {
            for (DBObject dbObject : list) {
                UserEntry userEntry = new UserEntry((BasicDBObject) dbObject);
                useridList.add(userEntry);
            }
        }
        return useridList;
    }

    public void updateChat(String chatId) {
        BasicDBObject query = new BasicDBObject(Constant.ID, new ObjectId(chatId));
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("ic", 1));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, updateValue);
    }

    /**
     * 通过学校查询
     *
     * @param schoolId
     * @param fields
     * @return
     */
    public List<UserEntry> getUserEntryHuanXin(ObjectId schoolId, BasicDBObject fields) {
        BasicDBObject query = new BasicDBObject("si", schoolId).append("ir", Constant.ZERO).append("ic", 0);
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, fields);
        List<UserEntry> userEntryList = new ArrayList<UserEntry>();
        for (DBObject dbObject : dbObjectList) {
            UserEntry userEntry = new UserEntry((BasicDBObject) dbObject);
            userEntryList.add(userEntry);
        }
        return userEntryList;
    }

    public void updateChat2(String chatId) {
        BasicDBObject query = new BasicDBObject("chatid", chatId);
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("ic", 1));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, updateValue);
    }

    /**
     * 获取学校下面的管理员以及校长
     *
     * @param schoolIds
     * @param fields
     * @return
     */
    public List<UserEntry> getHeadmasterBySchoolIdsList(List<ObjectId> schoolIds, BasicDBObject fields) {
        BasicDBObject query = new BasicDBObject("si", new BasicDBObject(Constant.MONGO_IN, schoolIds)).append("ir", Constant.ZERO)
                .append("r", new BasicDBObject(Constant.MONGO_GTE, UserRole.HEADMASTER.getRole()));
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, fields);
        List<UserEntry> userEntryList = new ArrayList<UserEntry>();
        for (DBObject dbObject : dbObjectList) {
            UserEntry userEntry = new UserEntry((BasicDBObject) dbObject);
            userEntryList.add(userEntry);
        }
        return userEntryList;
    }


    public List<UserEntry> getUserEntryList(Collection<ObjectId> schoolIds, List<Integer> roles, String name, DBObject fields, int skip, int limit) throws Exception {
        BasicDBObject query = new BasicDBObject().append("ir", Constant.ZERO);

        if (null != schoolIds && !schoolIds.isEmpty()) {
            query.append("si", new BasicDBObject(Constant.MONGO_IN, schoolIds));
        }

        if (null != roles && !roles.isEmpty()) {
            query.append("r", new BasicDBObject(Constant.MONGO_IN, roles));
        }

        if (query.keySet().size() == 1) {
            throw new Exception("param is wrong!");
        }

        if (StringUtils.isNotBlank(name)) {
            query.append("nm", MongoUtils.buildRegex(name));
        }
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, fields, Constant.MONGO_SORTBY_DESC, skip, limit);
        List<UserEntry> userEntryList = new ArrayList<UserEntry>();
        for (DBObject dbObject : dbObjectList) {
            UserEntry userEntry = new UserEntry((BasicDBObject) dbObject);
            userEntryList.add(userEntry);
        }
        return userEntryList;
    }

    public void updateUserNameAndSexById(ObjectId id, String stname, int sex) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        BasicDBObject update = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("nnm", stname).append("sex", sex));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, update);
    }

    public Map<String, List<ObjectId>> getTeacherIdMapBySchoolId(ObjectId schoolId, BasicDBObject fields) {
        BasicDBObject query = new BasicDBObject("si", schoolId)
                .append("r", new BasicDBObject(Constant.MONGO_GTE, UserRole.TEACHER.getRole())
                        .append(Constant.MONGO_NE, UserRole.PARENT.getRole()))
                .append("ir", Constant.ZERO);
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, fields);
        Map<String, List<ObjectId>> uisMap = new HashMap<String, List<ObjectId>>();
        List<ObjectId> teaIds = new ArrayList<ObjectId>();
        for (DBObject dbObject : dbObjectList) {
            UserEntry userEntry = new UserEntry((BasicDBObject) dbObject);
            teaIds.add(userEntry.getID());
        }
        uisMap.put("teaIds", teaIds);
        return uisMap;
    }

    /**
     * 更新是否领优惠券
     *
     * @param userId
     */
    public void updateUserCoupon(ObjectId userId) {
        BasicDBObject query = new BasicDBObject(Constant.ID, userId);
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("coupon", Constant.ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, updateValue);

    }

    /**
     * @param ids
     * @param roomType
     * @param fields
     * @return
     */
    public List<UserEntry> getUserEntryListBySex(Collection<ObjectId> ids, int roomType, DBObject fields) {
        List<UserEntry> retList = new ArrayList<UserEntry>();
        BasicDBObject query = new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_IN, ids)).append("ir", Constant.ZERO);
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, fields);
        if (null != list && !list.isEmpty()) {
            for (DBObject dbo : list) {
                retList.add(new UserEntry((BasicDBObject) dbo));
            }
        }
        return retList;
    }

    /**
     * @param schoolId
     * @param fields
     * @return
     */
    public List<UserEntry> getDutyUser(ObjectId schoolId, BasicDBObject fields) {
        BasicDBObject query = new BasicDBObject("si", schoolId).append("ir", Constant.ZERO)
                .append("r", UserRole.DOORKEEPER.getRole());
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, fields);
        List<UserEntry> userEntryList = new ArrayList<UserEntry>();
        for (DBObject dbObject : dbObjectList) {
            UserEntry userEntry = new UserEntry((BasicDBObject) dbObject);
            userEntryList.add(userEntry);
        }
        return userEntryList;
    }


    public List<UserEntry> getUserList(ObjectId schoolId, BasicDBObject fields,Integer role) {
        BasicDBObject query = new BasicDBObject("si", schoolId).append("ir", Constant.ZERO).append("r",role);
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, fields);
        List<UserEntry> userEntryList = new ArrayList<UserEntry>();
        for (DBObject dbObject : dbObjectList) {
            UserEntry userEntry = new UserEntry((BasicDBObject) dbObject);
            userEntryList.add(userEntry);
        }
        return userEntryList;
    }

    /**
     * @param userName
     * @param schoolId
     * @param fields
     * @return
     */
    public List<UserEntry> searchUsersWithSchool(String userName, ObjectId schoolId, BasicDBObject fields) {
        BasicDBObject query = new BasicDBObject("si", schoolId).append("ir", Constant.ZERO);
        if (!StringUtils.isEmpty(userName)) {
            query.append("nm", MongoUtils.buildRegex(userName));
        }
        query.append("r", new BasicDBObject(Constant.MONGO_GTE, UserRole.TEACHER.getRole())
                .append(Constant.MONGO_NE, UserRole.PARENT.getRole()));
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, fields, Constant.MONGO_SORTBY_DESC);

        List<UserEntry> userEntryList = new ArrayList<UserEntry>();
        for (DBObject dbObject : dbObjectList) {
            UserEntry userEntry = new UserEntry((BasicDBObject) dbObject);
            userEntryList.add(userEntry);
        }
        return userEntryList;
    }

    /**
     * 通过学校查询限制
     *
     * @param schoolId
     * @param fields
     * @return
     */
    public List<UserEntry> getUserEntryBySchoolIdListByLimit(ObjectId schoolId, BasicDBObject fields) {
        BasicDBObject query = new BasicDBObject("si", schoolId).append("ir", Constant.ZERO).append("lt", "").append("nm", new BasicDBObject(Constant.MONGO_NE, ""));
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, fields, new BasicDBObject("r", 1), 0, 200);
        List<UserEntry> userEntryList = new ArrayList<UserEntry>();
        for (DBObject dbObject : dbObjectList) {
            UserEntry userEntry = new UserEntry((BasicDBObject) dbObject);
            userEntryList.add(userEntry);
        }
        return userEntryList;
    }

    /**
     * 更新权限
     *
     * @param id
     */
    public void updateUserRole(ObjectId id, int role) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("r", role));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, updateValue);
    }
    
    
    
    public UserEntry searchBeichengUser(String userName) {
        BasicDBObject query = new BasicDBObject("si", new ObjectId("59a6160071f0564dbc41f4a6")).append("ir", Constant.ZERO);
        if (!StringUtils.isEmpty(userName)) {
            query.append("nm", MongoUtils.buildRegex(userName));
        }
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, new BasicDBObject("_id",1));
        if (null!=dbObject) {
            UserEntry userEntry = new UserEntry((BasicDBObject) dbObject);
            return userEntry;
        }
        return null;
    }


    public UserEntry searchUserByJNB(String name) {
        BasicDBObject query = new BasicDBObject("jnb", name).append("ir", Constant.ZERO);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, Constant.FIELDS);
        if (null != dbo) {
            UserEntry e = new UserEntry((BasicDBObject) dbo);
            return e;

        }
        return null;
    }

    public UserEntry searchUserByStuCode(String name) {
        BasicDBObject query = new BasicDBObject("sn", name).append("ir", Constant.ZERO);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, Constant.FIELDS);
        if (null != dbo) {
            UserEntry e = new UserEntry((BasicDBObject) dbo);
            return e;

        }
        return null;
    }

}
