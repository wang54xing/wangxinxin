package com.pojo.app;

import com.pojo.user.AvatarType;
import com.pojo.user.UserRole;
import com.sys.constants.Constant;
import com.sys.utils.AvatarUtils;

import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * session value
 *
 * @author fourer
 *
 */
public class SessionValue implements Serializable{

	/**
	 *
	 */
	private static final long serialVersionUID = -7786199032928967400L;

	private static final String DEFAULT_SCHOOL_NAVS_INT="0";
	private static final String DEFAULT_SCHOOL_NAVS="navs";

	public static final String SCHOOL_ID="si";//学校ID
	public static final String SCHOOL_NAME="snm";//学校名称
	public static final String USER_ID="ui";  //用户ID
	public static final String USER_NAME="un";//用户名称
	public static final String USER_RELAL_NAME="nnm";//用户名称
	public static final String USER_ROLE="ur";//用户角色
    public static final String USER_AVATAR ="avt";//用户头像
    public static final String USER_EXP = "exp";//经验值
    public static final String USER_PERMISSION="up";//用户额外具有的权限
    public static final String USER_REMOVE_PERMISSION="urp";//用户被删除的权限
	public static final String USER_CHATID="chatid";
    public static final String SCHOOL_LOGO="slogo";
    public static final String SCHOOL_NAVS="snv";//学校自制导航
	public static final String EDUCATION_LOGO="elogo";
	public static final String CLOUD_URL="curl";
	public static final String USER_SSO="sso";
	public static final String COUPON="coupon";
	public static final String USER_SEX="sex";
	public static final String EXPAND_ADMIN="epdadmin";
	public static final String EXPAND_XUNTANG="epdxt";
	public static final String SALARY_ADMIN="salaryadmin";
	public static final String CHALLENGE_EVAL_ROLE ="chlevalrole";
	public static final String STAFF_EVAL_ROLE ="staffevalrole";
	public static final String EQU_EVAL_ROLE ="equipmentRole";
	private static final String OVERALL_ADMIN="overallAdmin";
	public static final String N33_ADMIN="n33Admin";

	public static final String CLSRES_ADMIN="clsResadmin";

	private Map<String,String> map =new HashMap<String, String>();



	public Map<String, String> getMap() {
		return map;
	}
	public void setMap(HashMap<String, String> map) {
		this.map = map;
	}

    public String getSchoolNavs() {
		String nv= get(SCHOOL_NAVS);
		if(StringUtils.isBlank(nv) || DEFAULT_SCHOOL_NAVS_INT.equals(nv))
		{
			return DEFAULT_SCHOOL_NAVS;
		}
		return DEFAULT_SCHOOL_NAVS+nv;
	}
	public void setSchoolNavs(int schoolNavs) {
		put(SCHOOL_NAVS, String.valueOf(schoolNavs));
	}
	public String getId() {
		return get(USER_ID);
	}


	public void setId(String id) {
		put(USER_ID, id);
	}


	public String getUserName() {
		return get(USER_NAME);
	}


	public void setUserName(String userName) {
		put(USER_NAME, userName);
	}

	public String getRealName() {
		return get(USER_RELAL_NAME);
	}


	public void setRealName(String realName) {
		put(USER_RELAL_NAME, realName);
	}

	public int getK6kt(){
		try {
			return Integer.parseInt(get("k6kt"));
		} catch (Exception e){
			return 1;
		}
	}

	public void setK6kt(Integer k6kt){
		put("k6kt", k6kt.toString());
	}

	public String getChatid() {
		return get(USER_CHATID);
	}

	public void setChatid(String chatid) {
		put(USER_CHATID,chatid);
	}

	public int getUserRole() {
		String strInt =get(USER_ROLE);
		if(StringUtils.isNotBlank(strInt))
		{
			return Integer.valueOf(strInt);
		}
		return Constant.DEFAULT_VALUE_INT;
	}





	public void setUserRole(long userRole) {
		put(USER_ROLE, String.valueOf(userRole));
	}


	public String getSchoolId() {
		return get(SCHOOL_ID);
	}


	public void setSchoolId(String schoolId) {
		put(SCHOOL_ID, schoolId);
	}

	public String getSchoolName() {
		return get(SCHOOL_NAME);
	}

	public String getUserRoleDes() {
		return UserRole.getRoleDescription(getUserRole());
	}
	public void setUserRoleDes(String userRoleDes) {
		//this.userRoleDes = userRoleDes;
	}
	public void setSchoolName(String schoolName) {
		put(SCHOOL_NAME, schoolName);
	}


	public int getCoupon() {
		String strInt =get(COUPON);
		if(StringUtils.isNotBlank(strInt))
		{
			return Integer.valueOf(strInt);
		}
		return Constant.DEFAULT_VALUE_INT;
	}

	public void setCoupon(int coupon) {
		put(COUPON, String.valueOf(coupon));
	}

    public String getSchoolLogo(){return get(SCHOOL_LOGO);}
    public void setSchoolLogo(String schoolLogo){put(SCHOOL_LOGO,schoolLogo);}

	public String getEducationLogo(){return get(EDUCATION_LOGO);}
	public void setEducationLogo(String educationLogo){put(EDUCATION_LOGO, educationLogo);}

	public String getUserPermission() {
		return get(USER_PERMISSION);
	}
	public void setUserPermission(String userPermission) {
		put(USER_PERMISSION, userPermission);
	}
	public String getUserRemovePermission() {
		return get(USER_REMOVE_PERMISSION);
	}
	public void setUserRemovePermission(String userRemovePermission) {
		put(USER_REMOVE_PERMISSION, userRemovePermission);
	}

	public String getCloudUrl() {
		return get(CLOUD_URL);
	}

	public void setCloudUrl(String cloudUrl) {
		put(CLOUD_URL, cloudUrl);
	}

	public String getAvatar() {
		return get(USER_AVATAR);
	}

	public void setAvatar(String avatar) {
		put(USER_AVATAR, avatar);
	}



	public String getMinAvatar() {
		String av =get(USER_AVATAR);
		return AvatarUtils.getAvatar(av, AvatarType.MIN_AVATAR.getType());
	}


	public void setMinAvatar(String minAvatar) {
	}


	public String getMidAvatar() {
		String av =get(USER_AVATAR);
		return AvatarUtils.getAvatar(av, AvatarType.MIDDLE_AVATAR.getType());
	}


	public void setMidAvatar(String midAvatar) {
	}


	public String getMaxAvatar() {
		String av =get(USER_AVATAR);
		return AvatarUtils.getAvatar(av, AvatarType.MAX_AVATAR.getType());
	}


	public void setMaxAvatar(String maxAvatar) {
	}

	public int getExperience() {
        String strInt =get(USER_EXP);
        if(StringUtils.isNotBlank(strInt))
        {
            return Integer.valueOf(strInt);
        }
        return Constant.DEFAULT_VALUE_INT;
    }

	public void setExperience(int experience) {
        put(USER_EXP, String.valueOf(experience));
    }




	public SessionValue(){
    }


    public SessionValue(String key, String value){
        put(key, value);
    }


    public SessionValue(Map<String,String> m) {
       this.map=m;
    }

    public String getSso() {
    	String sso= get(USER_SSO);
    	if(StringUtils.isNotBlank(sso))
    	{
    		return sso;
    	}
    	return Constant.EMPTY;
	}

	public void setSso() {
		 put(USER_SSO, String.valueOf(Constant.ONE));
	}



	public String removeField( String key ){
        return map.remove( key );
    }


    public boolean containsField( String field ){
        return map.containsKey(field);
    }



    public String get( String key ){
        String value= map.get(key);
        if(StringUtils.isBlank(value))
        {
         value=Constant.EMPTY;
        }
        return value;
    }


    public boolean isEmpty()
    {
    	return map.isEmpty();
    }



    @SuppressWarnings("unused")
	private  String getString( String key ){
        Object foo = get( key );
        if ( foo == null )
            return null;
        return foo.toString();
    }

    @SuppressWarnings("unused")
    private String getString( String key, final String def ) {
        Object foo = get( key );
        if ( foo == null )
            return def;
        return foo.toString();
    }

    public String put( String key , String val ){
        return map.put(key, val);
    }
    public String toString(){
        return com.mongodb.util.JSON.serialize(this.map);
    }

	public int getSex() {
		String strInt =get(USER_SEX);
		if(StringUtils.isNotBlank(strInt))
		{
			return Integer.valueOf(strInt);
		}
		return Constant.DEFAULT_VALUE_INT;
	}

	public void setSex(int sex) {
		put(USER_SEX, String.valueOf(sex));
	}

	public int getExpandAdmin() {
		String strInt =get(EXPAND_ADMIN);
		if(StringUtils.isNotBlank(strInt))
		{
			return Integer.valueOf(strInt);
		}
		return Constant.DEFAULT_VALUE_INT;
	}

	public int getClsResAdmin() {
		String strInt =get(CLSRES_ADMIN);
		if(StringUtils.isNotBlank(strInt))
		{
			return Integer.valueOf(strInt);
		}
		return Constant.DEFAULT_VALUE_INT;
	}

	public void setSalaryAdmin(int salaryAdmin) {
		put(SALARY_ADMIN, String.valueOf(salaryAdmin));
	}

    public int getSalaryAdmin() {
        String strInt =get(SALARY_ADMIN);
        if(StringUtils.isNotBlank(strInt))
        {
            return Integer.valueOf(strInt);
        }
        return Constant.DEFAULT_VALUE_INT;
    }

	public void setChallengeEvalRole(int challengeEvalRole) {
		put(CHALLENGE_EVAL_ROLE, String.valueOf(challengeEvalRole));
	}

	public int getChallengeEvalRole() {
		String strInt =get(CHALLENGE_EVAL_ROLE);
		if(StringUtils.isNotBlank(strInt))
		{
			return Integer.valueOf(strInt);
		}
		return Constant.DEFAULT_VALUE_INT;
	}

	public void setStaffEvalRole(int staffEvalRole) {
		put(STAFF_EVAL_ROLE, String.valueOf(staffEvalRole));
	}

	public int getStaffEvalRole() {
		String strInt =get(STAFF_EVAL_ROLE);
		if(StringUtils.isNotBlank(strInt))
		{
			return Integer.valueOf(strInt);
		}
		return Constant.DEFAULT_VALUE_INT;
	}

	public void setEquipmentRole(int equipmentRole) {
		put(EQU_EVAL_ROLE, String.valueOf(equipmentRole));
	}

	public int getEquipmentRole() {
		String strInt =get(EQU_EVAL_ROLE);
		if(StringUtils.isNotBlank(strInt))
		{
			return Integer.valueOf(strInt);
		}
		return Constant.DEFAULT_VALUE_INT;
	}

    public void setExpandAdmin(int expandAdmin) {
        put(EXPAND_ADMIN, String.valueOf(expandAdmin));
    }
	public void setClcResAdmin(int clsResAdmin) {
		put(CLSRES_ADMIN, String.valueOf(clsResAdmin));
	}


	public int getExpandXunTang() {
		String strInt =get(EXPAND_XUNTANG);
		if(StringUtils.isNotBlank(strInt))
		{
			return Integer.valueOf(strInt);
		}
		return Constant.DEFAULT_VALUE_INT;
	}

	public void setExpandXunTang(int expandXunTang) {
		put(EXPAND_XUNTANG, String.valueOf(expandXunTang));
	}


	public int getOverallAdmin() {
		String strInt =get(OVERALL_ADMIN);
		if(StringUtils.isNotBlank(strInt))
		{
			return Integer.valueOf(strInt);
		}
		return Constant.DEFAULT_VALUE_INT;
	}

	public void setOverallAdmin(int overallAdmin) {
		put(OVERALL_ADMIN, String.valueOf(overallAdmin));
	}


	public void setN33Admin(int n33Admin) {
		put(N33_ADMIN, String.valueOf(n33Admin));
	}

	public int getN33Admin() {
		String strInt =get(N33_ADMIN);
		if(StringUtils.isNotBlank(strInt))
		{
			return Integer.valueOf(strInt);
		}
		return Constant.DEFAULT_VALUE_INT;
	}
}
