package com.pojo.school;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.user.UserEntry.Binds;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;

import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * 学校，年级信息
 * <pre>
 * collectionName:schools
 * </pre>
 * <pre>
 * {
 *   sty:学校类型 ,详见SchoolType
 *   nm:学校名称
 *   enm:学校英文名称
 *   int:简介
 *   det:详情
 *   dom:主页，域名
 *   pc:邮编
 *   logo: logo图像地址;长方形图片，供k6kt使用;
 *   logos:其他地方使用logo,目前只使用第一个
 *   [
 *    "logo1",云平台空间使用的正方形logo
 *   ]
 *   tp:电话
 *   so:仅仅用于排序
 *   inp:初始密码
 *   ir:地区ID
 *   add:地址
 *   vid:视频Id
 *   grs: 参见Grade
     [
        {
		 *  gid:年级ID
		 *  nm:年级名称
		 *  ty:年级类型
		 *  ld:年级组长
		}
		.....
     ]
     termType-->tt  学期  用于开始新学期按钮
     subs:学校科目列表; Subject
     [
       {
         si:科目ID
 *       nm:名字
 *       gis:年级ID
 *       [
 *        
 *       ]
 *       uis:学科组长
 *       [
 *       ]
 *     }
     ]
     nv:自定义导航 0默认 
     
     sis:其他校区集合.用于学校分校区情况，每个校区都单独看作一个学校;
     [
      
     ]
     
     bindId:绑定ID 与ahedu.com学校绑定ID
     yhbd:有鸿学校绑定
     
     
     binds:绑定；参见UserEntry.UserBind
 *   {
 *    ty: 1安徽省平台绑定  2 有红用户绑定 3 高新一中用户
	  bv:
 *   }
     
     
     mods:该学校所拥有的板块名称
     isf:是否需要火热活动推荐;页面是否有活动 banner;0有  1没有
 * }
 * </pre>
 * @author fourer
 */
public class SchoolEntry extends BaseDBObject {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1047019243277687889L;
	
	
	public SchoolEntry(BasicDBObject baseEntry) {
		super(baseEntry);
	}

	public SchoolEntry(int schoolType, String name, ObjectId regionId,String initialPassword) {
		this(schoolType,
				name,
				Constant.EMPTY, //englishName
				Constant.EMPTY,
				Constant.EMPTY,
				Constant.EMPTY,
				Constant.EMPTY,
				Constant.EMPTY,
				Constant.ONE,   //sort
				initialPassword,
				regionId,
				Constant.EMPTY,
				new ArrayList<Subject>()
				);
	}
	
	
	public SchoolEntry(int schoolType, String name, String englishName,
			String introduce, String detail, String domain, String postCode,
			String telephone, int sort, String initialPassword,
			ObjectId regionId, String address,List<Subject> subs) {
		super();
		
		BasicDBObject baseEntry =new BasicDBObject()
		.append("sty", schoolType)
		.append("nm", name)
		.append("enm", englishName)
		.append("int", introduce)
		.append("det", detail)
		.append("dom", domain)
		.append("pc", postCode).append("tp",telephone)
		.append("so", sort)
		.append("inp", initialPassword)
		.append("ir", regionId)
		.append("add", address)
		.append("tt", 1)//默认第一个学期
		.append("grs", new BasicDBList())
		.append("subs", MongoUtils.convert(MongoUtils.fetchDBObjectList(subs)))
        .append("logo","")
        .append("logos", new ArrayList<String>())
        .append("nv", 1);
        ;
		;
		setBaseEntry(baseEntry);
		
	}
	
	public List<String> getLogos() {
		List<String> retList =new ArrayList<String>();
		BasicDBList list =(BasicDBList)getSimpleObjectValue("logos");
		if(null!=list && !list.isEmpty())
		{
			for(Object o:list)
			{
				retList.add(o.toString());
			}
		}
		return retList;
	}

	public void setLogos(List<String> logos) {
		setSimpleValue("logos", MongoUtils.convert(logos));
	}


	public List<Subject> getSubjects() {
		
		List<Subject> retList =new ArrayList<Subject>();
		BasicDBList list =(BasicDBList)getSimpleObjectValue("subs");
		if(null!=list && !list.isEmpty())
		{
			for(Object o:list)
			{
				retList.add(new Subject((BasicDBObject)o));
			}
		}
		return retList;
	}
	

	public int getSchoolNavs() {
		return getSimpleIntegerValueDef("nv", 0);
	}

	public void setSchoolNavs(int schoolNavs) {
		setSimpleValue("nv", schoolNavs);
	}

	public void setSubjects(List<Subject> subjects) {
		List<DBObject> list =MongoUtils.fetchDBObjectList(subjects);
		setSimpleValue("sub", MongoUtils.convert(list));
	}

    public String getLogo() {
        return getSimpleStringValue("logo");
    }
    public void setLogo(String logo) {
        setSimpleValue("logo", logo);
    }
	public int getSchoolType() {
		return getSimpleIntegerValue("sty");
	}
	public void setSchoolType(int schoolType) {
		setSimpleValue("sty", schoolType);
	}
	public String getName() {
		return getSimpleStringValue("nm");
	}
	public void setName(String name) {
		setSimpleValue("nm", name);
	}
	public String getEnglishName() {
		return getSimpleStringValue("enm");
	}
	public void setEnglishName(String englishName) {
		setSimpleValue("enm", englishName);
	}
	public String getIntroduce() {
		return getSimpleStringValue("int");
	}
	public void setIntroduce(String introduce) {
		setSimpleValue("int", introduce);
	}
	
	
	public String getDetail() {
		return getSimpleStringValue("det");
	}
	public void setDetail(String detail) {
		setSimpleValue("det", detail);
	}
	
	
	
	public String getDomain() {
		return getSimpleStringValue("dom");
	}
	public void setDomain(String domain) {
		setSimpleValue("dom", domain);
	}
	public String getPostCode() {
		return getSimpleStringValue("pc");
	}
	public void setPostCode(String postCode) {
		setSimpleValue("pc", postCode);
	}
	public String getTelephone() {
		return getSimpleStringValue("tp");
	}
	public void setTelephone(String telephone) {
		setSimpleValue("tp", telephone);
	}
	
	
	public int getSort() {
		return getSimpleIntegerValue("so");
	}
	public void setSort(int sort) {
		setSimpleValue("so", sort);
	}
	public String getInitialPassword() {
		return getSimpleStringValue("inp");
	}
	public void setInitialPassword(String initialPassword) {
		setSimpleValue("inp", initialPassword);
	}

	public ObjectId getRegionId() {
		return getSimpleObjecIDValue("ir");
	}
	public void setRegionId(ObjectId regionId) {
		setSimpleValue("ir", regionId);
	}
	public String getAddress() {
		return getSimpleStringValue("add");
	}
	public void setAddress(String address) {
		setSimpleValue("add", address);
	}
	
	
	public List<Grade> getGradeList() {
		List<Grade> gradeList =new ArrayList<Grade>(Constant.FIVE);
		BasicDBList list =(BasicDBList)getSimpleObjectValue("grs");
		if(null!=list && !list.isEmpty())
		{
			for(Object o:list)
			{
				gradeList.add(new Grade((BasicDBObject)o));
			}
		}
		return gradeList;
	}
	public void setGradeList(Collection<Grade> gradeList) {
		List<DBObject> list=MongoUtils.fetchDBObjectList(gradeList);
		setSimpleValue("grs", MongoUtils.convert(list));
	}

    public int getTermType(){
        return getSimpleIntegerValue("tt");
    }
    public void setTermType(int termType){
        setSimpleValue("tt",termType);
    }
    
    
    
    public String getBindId() {
		return getSimpleStringValue("bindId");
	}
	public void setBindId(String bindId) {
		setSimpleValue("bindId", bindId);
	}
	
	
	
	public String getYhBindId() {
		return getSimpleStringValue("yhbd");
	}
	public void setYhBindId(String bindId) {
		setSimpleValue("yhbd", bindId);
	}
	

	public List<ObjectId> getSchoolIds() {
		List<ObjectId> retList =new ArrayList<ObjectId>();
		BasicDBList list =(BasicDBList)getSimpleObjectValue("sis");
		if(null!=list && !list.isEmpty())
		{
			for(Object o:list)
			{
				retList.add(((ObjectId)o));
			}
		}
		return retList;
	}

	public void setSchoolIds(List<ObjectId> schoolIds) {
		setSimpleValue("sis",  MongoUtils.convert(schoolIds));
	}
	
	public String getModuleStr() {
		return getSimpleStringValue("mods");
	}

	public void setModuleStr(String moduleStr) {
		setSimpleValue("mods", moduleStr);
	}

	public int getIsFieryactivity() {
		return getSimpleIntegerValueDef("isf", 0);
	}
	
	public void setIsFieryactivity(int isFieryactivity) {
		setSimpleValue("isf", isFieryactivity);
	}
	
	
	public Binds getUserBind() {
		BasicDBObject dbo=(BasicDBObject)getSimpleObjectValue("binds");
		
		if(null!=dbo)
		{
			return new Binds(dbo);
		}
		return null;
	}

	public void setUserBind(Binds userBind) {
		setSimpleValue("binds", userBind.getBaseEntry());
	}
}
