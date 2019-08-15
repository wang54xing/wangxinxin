package com.pojo.school;

import java.util.ArrayList;
import java.util.List;
import org.bson.types.ObjectId;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;

/**
 * 学校导航模块
 * @author fourer
 * <pre>
 * collectionName:navs
 * </pre>
 * <pre>
 * {
 *   ty:1导航块  2导航条 3磁贴导航
 *   mid:对应的模块Id号，和移动端设置一样
 *   id:"nav_left_1"
 *   nm:名字
 *   img:对应的图片
 *   cls:对应css class
 *   sort:排序
 *   rlink：权限很对应的连接;RoleLink
 *   [
 *      {
 *        rs:权限拥有者:[]
 *        link:对应的连接
 *      }
 *   ]
 *   sids:学校定制
 *   [
 *    sid1,
 *    sid2
 *   ]
 *   rsids:去除某个学校；比如：一般学校拥有某个导航块下面的5个导航，某导航只需要其中2个，另外三个则去掉
 *   [
 *     sid1,
 *     sid2
 *   ]
 * }
 * </pre>
 */
public class SchoolNavigationEntry extends BaseDBObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8566777496338012052L;

	
	public SchoolNavigationEntry(BasicDBObject baseEntry) {
		super(baseEntry);
	}


	public SchoolNavigationEntry(String id, int type, int modleId, String name,
			String cssClassName, int sort, List<RoleLink> list,
			List<ObjectId> schoolIds,String image) {
		BasicDBObject dbo =new BasicDBObject()
		.append("id", id)
		.append("ty", type)
		.append("mid", modleId)
		.append("nm", name)
		.append("cls", cssClassName)
		.append("sort", sort)
		.append("rlink", MongoUtils.convert(MongoUtils.fetchDBObjectList(list)))
		.append("sids", MongoUtils.convert(schoolIds))
		.append("rsids", new BasicDBList())
		.append("img", image)
		;
		setBaseEntry(dbo);
	}
	
	
	public String getId() {
		return getSimpleStringValue("id");
	}
	public void setId(String id) {
		setSimpleValue("id", id);
	}
	public int getType() {
		return getSimpleIntegerValue("ty");
	}
	public void setType(int type) {
		setSimpleValue("ty", type);
	}
	public int getModleId() {
		return getSimpleIntegerValue("mid");
	}
	public void setModleId(int modleId) {
		setSimpleValue("mid", modleId);
	}
	public String getName() {
		return getSimpleStringValue("nm");
	}
	public void setName(String name) {
		setSimpleValue("nm", name);
	}
	public String getCssClassName() {
		return getSimpleStringValue("cls");
	}
	public void setCssClassName(String cssClassName) {
		setSimpleValue("cls", cssClassName);
	}
	public int getSort() {
		return getSimpleIntegerValue("sort");
	}
	public void setSort(int sort) {
		setSimpleValue("sort", sort);
	}
	public List<RoleLink> getList() {
		List<RoleLink> retList =new ArrayList<RoleLink>();
		BasicDBList list =(BasicDBList)getSimpleObjectValue("rlink");
		if(null!=list && !list.isEmpty())
		{
			for(Object o:list)
			{
				retList.add(new RoleLink((BasicDBObject)o));
			}
		}
		return retList;
	}
	public void setList(List<RoleLink> list) {
		List<DBObject> ls = MongoUtils.fetchDBObjectList(list);
		setSimpleValue("rlink",  MongoUtils.convert(ls));
	}
	
	
	public List<ObjectId> getSchoolIds() {
		List<ObjectId> retList =new ArrayList<ObjectId>();
		BasicDBList list =(BasicDBList)getSimpleObjectValue("sids");
		if(null!=list && !list.isEmpty())
		{
			for(Object o:list)
			{
				retList.add((ObjectId)o);
			}
		}
		return retList;
	}
	public void setSchoolIds(List<ObjectId> list) {
		setSimpleValue("sids",  MongoUtils.convert(list));
	}
	
	
	
	public List<ObjectId> getRemoveSchoolIds() {
		List<ObjectId> retList =new ArrayList<ObjectId>();
		BasicDBList list =(BasicDBList)getSimpleObjectValue("rsids");
		if(null!=list && !list.isEmpty())
		{
			for(Object o:list)
			{
				retList.add((ObjectId)o);
			}
		}
		return retList;
	}
	public void setRemoveSchoolIds(List<ObjectId> list) {
		setSimpleValue("rsids",  MongoUtils.convert(list));
	}
	
	
	public String getImage() {
		return getSimpleStringValue("img");
	}
	public void setImage(String name) {
		setSimpleValue("img", name);
	}

	/**
	 * @see com.pojo.school.SchoolNavigationEntry
	 * @author fourer
	 *
	 */
	public static class RoleLink extends BaseDBObject
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 2498528684219115981L;
		
		
		public RoleLink(BasicDBObject baseEntry) {
			super(baseEntry);
		}
		
		
		public RoleLink(List<Integer> roles,String link) {
			BasicDBObject dbo =new BasicDBObject()
			.append("rs", MongoUtils.convert(roles))
			.append("link", link);
			setBaseEntry(dbo);
		}
		
		
		public List<Integer> getRoles() {
			List<Integer> retList =new ArrayList<Integer>();
			BasicDBList list =(BasicDBList)getSimpleObjectValue("rs");
			if(null!=list && !list.isEmpty())
			{
				for(Object o:list)
				{
					retList.add(((Integer)o));
				}
			}
			return retList;
		}
		public void setRoles(List<Integer> roles) {
			setSimpleValue("rs",  MongoUtils.convert(roles));
		}
		public String getLink() {
			return getSimpleStringValue("link");
		}
		public void setLink(String link) {
			setSimpleValue("link", link);
		}
		
	}

}
