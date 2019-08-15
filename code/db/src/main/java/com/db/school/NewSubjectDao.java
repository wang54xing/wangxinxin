package com.db.school;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.pojo.newschool.SubjectEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by wang_xinxin on 2017/2/11.
 */
public class NewSubjectDao extends BaseDao {

    /**
     * 增加一个学科
     * @param e
     * @return
     */
    public ObjectId addSubjectEntry(SubjectEntry e)
    {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_SUBJECT, e.getBaseEntry());
        return e.getID();
    }
}
