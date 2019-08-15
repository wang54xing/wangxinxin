package com.db.new33.afresh;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.pojo.new33.afresh.AfreshClassRuleEntry;
import com.sys.constants.Constant;

/**
 * Created by James on 2019-05-13.
 */
public class AfreshClassRuleDao extends BaseDao {
    public void addEntry(AfreshClassRuleEntry entry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_AFRESH_RULE_CLASS, entry.getBaseEntry());
    }
}
