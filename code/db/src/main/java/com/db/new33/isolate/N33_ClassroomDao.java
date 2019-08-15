package com.db.new33.isolate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pojo.new33.isolate.Grade;
import org.bson.types.ObjectId;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.new33.isolate.N33_ClassroomEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;

public class N33_ClassroomDao extends BaseDao {

    public void save(N33_ClassroomEntry classroomEntry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_CLASSROOM, classroomEntry.getBaseEntry());
    }

    public void add(Collection<N33_ClassroomEntry> list) {
        if (list.size() == 0) {
            return;
        }
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_CLASSROOM, MongoUtils.fetchDBObjectList(list));
    }

    public void add(List<DBObject> list) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_CLASSROOM, list);
    }

    public void updateClassNameId(ObjectId id, String className, ObjectId classId) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        BasicDBObject updatevalue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject().append("classid", classId).append("classname", className));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_CLASSROOM, query, updatevalue);
    }

    public void updateRoomNameId(ObjectId id, String roomName) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        BasicDBObject updatevalue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject().append("roomname", roomName));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_CLASSROOM, query, updatevalue);
    }

    public void updateDesc(ObjectId id, String description, Integer cap, Integer type) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        BasicDBObject updatevalue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("desc", description).append("capacity", cap).append("type", type));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_CLASSROOM, query, updatevalue);
    }

    public void updateArrange(ObjectId id, Integer arranged) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        BasicDBObject updatevalue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("arrange", arranged));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_CLASSROOM, query, updatevalue);
    }


    public List<N33_ClassroomEntry> getRoomEntryListByXqGrade(ObjectId xqid, ObjectId sid, ObjectId gradeId) {
        List<N33_ClassroomEntry> entries = new ArrayList<N33_ClassroomEntry>();
        BasicDBObject query = new BasicDBObject("xqid", xqid).append("sid", sid).append("grade", gradeId).append("arrange", 1);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_CLASSROOM, query, Constant.FIELDS);
        if (null != dbObject && !dbObject.isEmpty()) {
            for (DBObject dbo : dbObject) {
                N33_ClassroomEntry entry = new N33_ClassroomEntry((BasicDBObject) dbo);
                entries.add(entry);
            }
        }
        return entries;
    }

    public N33_ClassroomEntry getRoomEntryListByXqGrade(ObjectId xqid, ObjectId sid, ObjectId gradeId, String roomName) {
        BasicDBObject query = new BasicDBObject("xqid", xqid).append("sid", sid).append("grade", gradeId).append("arrange", 1).append("roomname", roomName);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_CLASSROOM, query, Constant.FIELDS);
        if (dbObject != null) {
            return new N33_ClassroomEntry((BasicDBObject) dbObject);
        } else {
            return null;
        }
    }

    public N33_ClassroomEntry getRoomEntryListByXqClass(ObjectId xqid, ObjectId classId) {
        BasicDBObject query = new BasicDBObject("xqid", xqid).append("classid", classId);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_CLASSROOM, query, Constant.FIELDS);
        if (dbObject != null) {
            return new N33_ClassroomEntry((BasicDBObject) dbObject);
        } else {
            return null;
        }
    }

    public List<N33_ClassroomEntry> getRoomEntryListByXqGradeIso(ObjectId xqid, ObjectId sid, ObjectId gradeId) {
        List<N33_ClassroomEntry> entries = new ArrayList<N33_ClassroomEntry>();
        BasicDBObject query = new BasicDBObject("xqid", xqid).append("sid", sid).append("grade", gradeId);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_CLASSROOM, query, Constant.FIELDS);
        if (null != dbObject && !dbObject.isEmpty()) {
            for (DBObject dbo : dbObject) {
                N33_ClassroomEntry entry = new N33_ClassroomEntry((BasicDBObject) dbo);
                entries.add(entry);
            }
        }
        return entries;
    }

    /**
     * 班级对应的教室年级列表
     *
     * @param xqid
     * @param gradeId
     * @return
     */
    public Map<ObjectId, N33_ClassroomEntry> getRoomEntryListByXqGradeMap(ObjectId xqid, ObjectId gradeId) {
        Map<ObjectId, N33_ClassroomEntry> entries = new HashMap<ObjectId, N33_ClassroomEntry>();
        BasicDBObject query = new BasicDBObject("xqid", xqid).append("grade", gradeId);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_CLASSROOM, query, Constant.FIELDS);
        if (null != dbObject && !dbObject.isEmpty()) {
            for (DBObject dbo : dbObject) {
                N33_ClassroomEntry entry = new N33_ClassroomEntry((BasicDBObject) dbo);
                if (entry.getClassId() != null) {
                    entries.put(entry.getClassId(), entry);
                }
            }
        }
        return entries;
    }

    /**
     * 班级对应的教室年级列表
     *
     * @param xqid
     * @param gradeId
     * @return
     */
    public Map<ObjectId, N33_ClassroomEntry> getRoomEntryListByXqGradeMapOnlyArranged(ObjectId xqid, ObjectId gradeId) {
        Map<ObjectId, N33_ClassroomEntry> entries = new HashMap<ObjectId, N33_ClassroomEntry>();
        BasicDBObject query = new BasicDBObject("xqid", xqid).append("grade", gradeId).append("arrange", 1);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_CLASSROOM, query, Constant.FIELDS);
        if (null != dbObject && !dbObject.isEmpty()) {
            for (DBObject dbo : dbObject) {
                N33_ClassroomEntry entry = new N33_ClassroomEntry((BasicDBObject) dbo);
                if (entry.getClassId() != null) {
                    entries.put(entry.getClassId(), entry);
                }else{
                    entries.put(entry.getID(), entry);
                }
            }
        }
        return entries;
    }

    public Map<ObjectId, N33_ClassroomEntry> getRoomEntryListByXqClassMap(ObjectId xqid, List<ObjectId> clIds) {
        Map<ObjectId, N33_ClassroomEntry> entries = new HashMap<ObjectId, N33_ClassroomEntry>();
        BasicDBObject query = new BasicDBObject("xqid", xqid).append("classid", new BasicDBObject(Constant.MONGO_IN, clIds));
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_CLASSROOM, query, Constant.FIELDS);
        if (null != dbObject && !dbObject.isEmpty()) {
            for (DBObject dbo : dbObject) {
                N33_ClassroomEntry entry = new N33_ClassroomEntry((BasicDBObject) dbo);
                if (entry.getClassId() != null) {
                    entries.put(entry.getClassId(), entry);
                }
            }
        }
        return entries;
    }

    public Map<ObjectId, N33_ClassroomEntry> getRoomEntryListByXqClassMapS(List<ObjectId> clIds, ObjectId gradeId) {
        Map<ObjectId, N33_ClassroomEntry> entries = new HashMap<ObjectId, N33_ClassroomEntry>();
        BasicDBObject query = new BasicDBObject("xqid", new BasicDBObject(Constant.MONGO_IN, clIds)).append("grade", gradeId);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_CLASSROOM, query, Constant.FIELDS);
        if (null != dbObject && !dbObject.isEmpty()) {
            for (DBObject dbo : dbObject) {
                N33_ClassroomEntry entry = new N33_ClassroomEntry((BasicDBObject) dbo);
                entries.put(entry.getRoomId(), entry);
            }
        }
        return entries;
    }

    public Map<ObjectId, N33_ClassroomEntry> getRoomEntryListByXqRoomMap(ObjectId xqid, List<ObjectId> roomIds) {
        Map<ObjectId, N33_ClassroomEntry> entries = new HashMap<ObjectId, N33_ClassroomEntry>();
        BasicDBObject query = new BasicDBObject("xqid", xqid).append("roomid", new BasicDBObject(Constant.MONGO_IN, roomIds));
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_CLASSROOM, query, Constant.FIELDS);
        if (null != dbObject && !dbObject.isEmpty()) {
            for (DBObject dbo : dbObject) {
                N33_ClassroomEntry entry = new N33_ClassroomEntry((BasicDBObject) dbo);
                if (entry.getClassId() != null) {
                    entries.put(entry.getRoomId(), entry);
                }
            }
        }
        return entries;
    }

    public Map<ObjectId, N33_ClassroomEntry> getRoomEntryListByXqRoomMapRoom(ObjectId xqid, List<ObjectId> roomIds) {
        Map<ObjectId, N33_ClassroomEntry> entries = new HashMap<ObjectId, N33_ClassroomEntry>();
        BasicDBObject query = new BasicDBObject("xqid", xqid).append("roomid", new BasicDBObject(Constant.MONGO_IN, roomIds));
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_CLASSROOM, query, Constant.FIELDS);
        if (null != dbObject && !dbObject.isEmpty()) {
            for (DBObject dbo : dbObject) {
                N33_ClassroomEntry entry = new N33_ClassroomEntry((BasicDBObject) dbo);
                entries.put(entry.getRoomId(), entry);
            }
        }
        return entries;
    }

    public Map<ObjectId, N33_ClassroomEntry> getRoomEntryListByXqRoomMap(ObjectId xqid, List<ObjectId> roomIds, ObjectId gradeId) {
        Map<ObjectId, N33_ClassroomEntry> entries = new HashMap<ObjectId, N33_ClassroomEntry>();
        BasicDBObject query = new BasicDBObject("xqid", xqid).append("roomid", new BasicDBObject(Constant.MONGO_IN, roomIds)).append("grade", gradeId).append("arrange", 1);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_CLASSROOM, query, Constant.FIELDS);
        if (null != dbObject && !dbObject.isEmpty()) {
            for (DBObject dbo : dbObject) {
                N33_ClassroomEntry entry = new N33_ClassroomEntry((BasicDBObject) dbo);
                if (entry.getClassId() != null) {
                    entries.put(entry.getRoomId(), entry);
                }
                entries.put(entry.getRoomId(), entry);
            }
        }
        return entries;
    }

    public Map<ObjectId, N33_ClassroomEntry> getRoomEntryListByXqRoomForMap(ObjectId xqid, List<ObjectId> roomIds) {
        Map<ObjectId, N33_ClassroomEntry> entries = new HashMap<ObjectId, N33_ClassroomEntry>();
        BasicDBObject query = new BasicDBObject("xqid", xqid).append("roomid", new BasicDBObject(Constant.MONGO_IN, roomIds));
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_CLASSROOM, query, Constant.FIELDS);
        if (null != dbObject && !dbObject.isEmpty()) {
            for (DBObject dbo : dbObject) {
                N33_ClassroomEntry entry = new N33_ClassroomEntry((BasicDBObject) dbo);
                if (entry.getClassId() != null) {
                    entries.put(entry.getRoomId(), entry);
                }
                entries.put(entry.getRoomId(), entry);
            }
        }
        return entries;
    }


    /**
     * 班级对应的教室年级列表
     *
     * @param xqid
     * @param gradeId
     * @return
     */
    public Map<ObjectId, N33_ClassroomEntry> getRoomMapByXqGradeMap(ObjectId xqid, ObjectId gradeId) {
        Map<ObjectId, N33_ClassroomEntry> entries = new HashMap<ObjectId, N33_ClassroomEntry>();
        BasicDBObject query = new BasicDBObject("xqid", xqid).append("grade", gradeId);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_CLASSROOM, query, Constant.FIELDS);
        if (null != dbObject && !dbObject.isEmpty()) {
            for (DBObject dbo : dbObject) {
                N33_ClassroomEntry entry = new N33_ClassroomEntry((BasicDBObject) dbo);
                if (entry.getRoomId() != null) {
                    entries.put(entry.getRoomId(), entry);
                }
            }
        }
        return entries;
    }

    public Map<ObjectId, N33_ClassroomEntry> getRoomEntryListByXqGradeForMap(ObjectId xqid, ObjectId sid) {
        Map<ObjectId, N33_ClassroomEntry> entries = new HashMap<ObjectId, N33_ClassroomEntry>();
        BasicDBObject query = new BasicDBObject("xqid", xqid).append("sid", sid);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_CLASSROOM, query, Constant.FIELDS);
        if (null != dbObject && !dbObject.isEmpty()) {
            for (DBObject dbo : dbObject) {
                N33_ClassroomEntry entry = new N33_ClassroomEntry((BasicDBObject) dbo);
                if (entry.getRoomId() != null) {
                    entries.put(entry.getRoomId(), entry);
                }
            }
        }
        return entries;
    }

    public List<N33_ClassroomEntry> getRoomEntryListByXqGrade(ObjectId xqid, ObjectId sid) {
        List<N33_ClassroomEntry> entries = new ArrayList<N33_ClassroomEntry>();
        BasicDBObject query = new BasicDBObject("xqid", xqid).append("sid", sid);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_CLASSROOM, query, Constant.FIELDS);
        if (null != dbObject && !dbObject.isEmpty()) {
            for (DBObject dbo : dbObject) {
                N33_ClassroomEntry entry = new N33_ClassroomEntry((BasicDBObject) dbo);
                entries.add(entry);
            }
        }
        return entries;
    }


    public List<N33_ClassroomEntry> getRoomEntryListByXqGrade(List<ObjectId> xqid, ObjectId sid) {
        List<N33_ClassroomEntry> entries = new ArrayList<N33_ClassroomEntry>();
        BasicDBObject query = new BasicDBObject("xqid", new BasicDBObject(Constant.MONGO_IN, xqid)).append("sid", sid);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_CLASSROOM, query, Constant.FIELDS);
        if (null != dbObject && !dbObject.isEmpty()) {
            for (DBObject dbo : dbObject) {
                N33_ClassroomEntry entry = new N33_ClassroomEntry((BasicDBObject) dbo);
                entries.add(entry);
            }
        }
        return entries;
    }

    public Map<ObjectId, N33_ClassroomEntry> getRoomEntryListByXqGradeForMap(List<ObjectId> xqid, ObjectId sid) {
        Map<ObjectId, N33_ClassroomEntry> entries = new HashMap<ObjectId, N33_ClassroomEntry>();
        BasicDBObject query = new BasicDBObject("xqid", new BasicDBObject(Constant.MONGO_IN, xqid)).append("sid", sid);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_CLASSROOM, query, Constant.FIELDS);
        if (null != dbObject && !dbObject.isEmpty()) {
            for (DBObject dbo : dbObject) {
                N33_ClassroomEntry entry = new N33_ClassroomEntry((BasicDBObject) dbo);
                if (entry.getRoomId() != null) {
                    entries.put(entry.getRoomId(), entry);
                }
            }
        }
        return entries;
    }

    /**
     * 查询所有次的可排课教室
     *
     * @param cid
     * @param sid
     * @return
     */
    public List<N33_ClassroomEntry> getIsolateClassRoomEntrys(List<ObjectId> cid, ObjectId sid, ObjectId gradeId, Integer arrange) {
        List<N33_ClassroomEntry> entries = new ArrayList<N33_ClassroomEntry>();
        BasicDBObject query = new BasicDBObject("xqid", new BasicDBObject(Constant.MONGO_IN, cid)).append("sid", sid).append("grade", gradeId).append("arrange", arrange);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_CLASSROOM, query, Constant.FIELDS);
        if (null != dbObject && !dbObject.isEmpty()) {
            for (DBObject dbo : dbObject) {
                N33_ClassroomEntry entry = new N33_ClassroomEntry((BasicDBObject) dbo);
                entries.add(entry);
            }
        }
        return entries;
    }

    public List<N33_ClassroomEntry> getIsolateClassRoomEntrysAllGrade(List<ObjectId> cid, ObjectId sid, Integer arrange) {
        List<N33_ClassroomEntry> entries = new ArrayList<N33_ClassroomEntry>();
        BasicDBObject query = new BasicDBObject("xqid", new BasicDBObject(Constant.MONGO_IN, cid)).append("sid", sid).append("arrange", arrange);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_CLASSROOM, query, Constant.FIELDS);
        if (null != dbObject && !dbObject.isEmpty()) {
            for (DBObject dbo : dbObject) {
                N33_ClassroomEntry entry = new N33_ClassroomEntry((BasicDBObject) dbo);
                entries.add(entry);
            }
        }
        return entries;
    }

    public List<N33_ClassroomEntry> getRoomEntryListByXqGrade(ObjectId xqid, ObjectId sid, ObjectId gradeId, Integer arrange) {
        List<N33_ClassroomEntry> entries = new ArrayList<N33_ClassroomEntry>();
        BasicDBObject query = new BasicDBObject("xqid", xqid).append("sid", sid).append("grade", gradeId).append("arrange", arrange);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_CLASSROOM, query, Constant.FIELDS);
        if (null != dbObject && !dbObject.isEmpty()) {
            for (DBObject dbo : dbObject) {
                N33_ClassroomEntry entry = new N33_ClassroomEntry((BasicDBObject) dbo);
                entries.add(entry);
            }
        }
        return entries;
    }

    public List<N33_ClassroomEntry> getRoomEntryListByXq(ObjectId xqid, ObjectId sid, Integer arrange) {
        List<N33_ClassroomEntry> entries = new ArrayList<N33_ClassroomEntry>();
        BasicDBObject query = new BasicDBObject("xqid", xqid).append("sid", sid).append("arrange", arrange);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_CLASSROOM, query, Constant.FIELDS);
        if (null != dbObject && !dbObject.isEmpty()) {
            for (DBObject dbo : dbObject) {
                N33_ClassroomEntry entry = new N33_ClassroomEntry((BasicDBObject) dbo);
                entries.add(entry);
            }
        }
        return entries;
    }

    public List<N33_ClassroomEntry> getRoomEntryListByXqGradeAndRoomIds(List<ObjectId> roomIds, ObjectId xqid, ObjectId sid, ObjectId gradeId, Integer arrange) {
        List<N33_ClassroomEntry> entries = new ArrayList<N33_ClassroomEntry>();
        BasicDBObject query = new BasicDBObject("xqid", xqid).append("sid", sid).append("grade", gradeId).append("arrange", arrange).append("roomid", new BasicDBObject(Constant.MONGO_IN, roomIds));
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_CLASSROOM, query, Constant.FIELDS);
        if (null != dbObject && !dbObject.isEmpty()) {
            for (DBObject dbo : dbObject) {
                N33_ClassroomEntry entry = new N33_ClassroomEntry((BasicDBObject) dbo);
                entries.add(entry);
            }
        }
        return entries;
    }


    public N33_ClassroomEntry findIsolateRoomEntryByRidAndXqid(ObjectId rid, ObjectId xqid, ObjectId schoolId) {
        BasicDBObject query = new BasicDBObject("rid", rid).append("xqid", xqid).append("sid", schoolId);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_CLASSROOM, query, Constant.FIELDS);
        if (dbObject != null) {
            return new N33_ClassroomEntry((BasicDBObject) dbObject);
        } else {
            return null;
        }
    }

    public void updateIsolateRoomEntry(N33_ClassroomEntry entry) {
        DBObject query = new BasicDBObject(Constant.ID, entry.getID());
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, entry.getBaseEntry());
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_CLASSROOM, query, updateValue);
    }

    public void updateIsolateRoomEntry(ObjectId id, int iss) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("iss", iss));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_CLASSROOM, query, updateValue);
    }


    public void updateName(ObjectId xiqd, ObjectId rid, String name) {
        DBObject query = new BasicDBObject("rid", rid).append("xqid", xiqd);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("bn", name));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_CLASSROOM, query, updateValue);
    }

    public void addIsolateRoomEntry(N33_ClassroomEntry entry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_CLASSROOM, entry.getBaseEntry());
    }


    public void remove(ObjectId id) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_CLASSROOM, query);
    }

    public void removeByCiId(ObjectId ciId) {
        BasicDBObject query = new BasicDBObject("xqid", ciId);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_CLASSROOM, query);
    }


}
