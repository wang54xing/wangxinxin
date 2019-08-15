package com.pojo.navigation;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wang_xinxin on 2017/1/9.
 * <pre>
 * collectionName:navs
 * </pre>
 * <pre>
 * {
 *   ty:1导航块  2导航条
 *   pid:对应的模块Id
 *   nm:名字
 *   img:对应的图片
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
public class NavigationEntry extends BaseDBObject {

    private static final long serialVersionUID = 6154118711081988345L;

    public NavigationEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public NavigationEntry(int type, ObjectId parentId, String name, int sort, List<RoleLink> list,
                                 List<SchoolCustomized> schoolCustomizedList,String image) {
        BasicDBObject dbo =new BasicDBObject()
                .append("ty", type)
                .append("pid", parentId)
                .append("nm", name)
                .append("sort", sort)
                .append("rlink", MongoUtils.convert(MongoUtils.fetchDBObjectList(list)))
                .append("scds", MongoUtils.convert(MongoUtils.fetchDBObjectList(schoolCustomizedList)))
                .append("rsids", new BasicDBList())
                .append("img", image)
                ;
        setBaseEntry(dbo);
    }

    public int getType() {
        return getSimpleIntegerValue("ty");
    }
    public void setType(int type) {
        setSimpleValue("ty", type);
    }
    public ObjectId getParentId() {
        return getSimpleObjecIDValue("pid");
    }
    public void setParentId(ObjectId parentId) {
        setSimpleValue("pid", parentId);
    }
    public String getName() {
        return getSimpleStringValue("nm");
    }
    public void setName(String name) {
        setSimpleValue("nm", name);
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

    public List<SchoolCustomized> getSchoolCustomizedList() {
        List<SchoolCustomized> retList =new ArrayList<SchoolCustomized>();
        BasicDBList list =(BasicDBList)getSimpleObjectValue("scds");
        if(null!=list && !list.isEmpty())
        {
            for(Object o:list)
            {
                retList.add(new SchoolCustomized((BasicDBObject)o));
            }
        }
        return retList;
    }
    public void setSchoolCustomizedList(List<SchoolCustomized> schoolCustomizedList) {
        List<DBObject> ls = MongoUtils.fetchDBObjectList(schoolCustomizedList);
        setSimpleValue("scds",  MongoUtils.convert(ls));
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
     * @author wang_xinxin
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

    /**
     * @see com.pojo.school.SchoolNavigationEntry
     * @author wang_xinxin
     *
     */
    public static class SchoolCustomized extends BaseDBObject
    {


        private static final long serialVersionUID = 6077063166040415625L;

        public SchoolCustomized(BasicDBObject baseEntry) {
            super(baseEntry);
        }


        public SchoolCustomized(ObjectId schoolId,String name,String image) {
            BasicDBObject dbo =new BasicDBObject()
                    .append("sid", schoolId)
                    .append("nm", name)
                    .append("img", image);
            setBaseEntry(dbo);
        }

        public ObjectId getSchoolId() {
            return getSimpleObjecIDValue("sid");
        }

        public void setSchoolId(ObjectId schoolId) {
            setSimpleValue("sid",schoolId);
        }

        public String getName() {
            return getSimpleStringValue("nm");
        }
        public void setName(String name) {
            setSimpleValue("nm", name);
        }

        public String getImage() {
            return getSimpleStringValue("img");
        }
        public void setImage(String image) {
            setSimpleValue("img", image);
        }
    }
}
