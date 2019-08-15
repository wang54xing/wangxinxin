package com.db.dzbp;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.dzbp.ClientClassRelaEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.List;

public class ClientClassRelaDao extends BaseDao {

    public void addClientClassRelaEntry(ClientClassRelaEntry entry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_DZBP_CLIENT_CLASS_RELA, entry.getBaseEntry());
    }

    public void addClientClassRelaEntries(List<ClientClassRelaEntry> entries) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_DZBP_CLIENT_CLASS_RELA, MongoUtils.fetchDBObjectList(entries));
    }

    public void updateClientClassRelaEntry(ClientClassRelaEntry entry) {
        DBObject query = new BasicDBObject(Constant.ID, entry.getID());
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, entry.getBaseEntry());
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_DZBP_CLIENT_CLASS_RELA, query, updateValue);
    }

    public ClientClassRelaEntry getClientClassRelaByClientId(String clientId) {
        BasicDBObject query = new BasicDBObject("clientId", clientId);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_DZBP_CLIENT_CLASS_RELA, query, Constant.FIELDS);
        if (dbObject != null) {
            return new ClientClassRelaEntry((BasicDBObject) dbObject);
        } else {
            return null;
        }
    }
}
