package com.db.school;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.pojo.newschool.SchoolEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by wang_xinxin on 2017/2/11.
 */
public class NewSchoolDao extends BaseDao {
    /**
     * 添加
     * @param e
     * @return
     */
    public ObjectId addNewSchoolEntry(SchoolEntry e)
    {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_SCHOOL_NAME, e.getBaseEntry());
        return e.getID();
    }
}
