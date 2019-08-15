package com.fulaan.dzbp.service;

import com.db.dzbp.ClientClassRelaDao;
import com.pojo.dzbp.ClientClassRelaEntry;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClientClassRelaService {

    private ClientClassRelaDao clientClassRelaDao = new ClientClassRelaDao();

    public ClientClassRelaEntry getClientClassRelaEntry(String clientId) {
        ClientClassRelaEntry entry = clientClassRelaDao.getClientClassRelaByClientId(clientId);
        return entry;
    }

    public static void main(String[] args) {
        ClientClassRelaDao dao = new ClientClassRelaDao();
        ObjectId schoolId = new ObjectId("57d8e3c7b0fbeb38c4dd71ad");
        List<String> clientIds = new ArrayList<String>();

        clientIds.add("AS-LZ0CD0183604FA");
        clientIds.add("AS-LZ0CD0183604B6");
        clientIds.add("AS-LZ0CD0183604D4");
        clientIds.add("AS-LZ0CD0183604F7");
        clientIds.add("AS-LZ0CD0183605A1");

        clientIds.add("AS-LZ0CD0183604FC");
        clientIds.add("AS-LZ0CD018360516");
        clientIds.add("AS-LZ0CD0183604F9");
        clientIds.add("AS-LZ0CD01836050E");
        clientIds.add("AS-LZ0CD0183605E5");

        clientIds.add("AS-LZ0CD0183602ED");
        clientIds.add("AS-LZ0CD0183605CB");
        clientIds.add("AS-LZ0CD018360289");
        clientIds.add("AS-LZ0CD0183604B9");
        clientIds.add("AS-LZ0CD0183604BD");

        clientIds.add("AS-LZ0CD0183604B8");
        clientIds.add("AS-LZ0CD0183604F1");
        clientIds.add("AS-LZ0CD0183604C6");
        clientIds.add("AS-LZ0CD0183604E5");
        clientIds.add("AS-LZ0CD0183605C0");


        List<ObjectId> classIds = new ArrayList<ObjectId>();
        classIds.add(new ObjectId("5d2d339e6de1d24999094630"));
        classIds.add(new ObjectId("5d2d339e6de1d24999094631"));
        classIds.add(new ObjectId("5d2d339e6de1d24999094632"));
        classIds.add(new ObjectId("5d2d339e6de1d24999094633"));
        classIds.add(new ObjectId("5d2d339e6de1d24999094634"));

        classIds.add(new ObjectId("5d2d339e6de1d24999094635"));
        classIds.add(new ObjectId("5d2d339e6de1d24999094636"));
        classIds.add(new ObjectId("5d2d339e6de1d24999094637"));
        classIds.add(new ObjectId("5d2d339e6de1d24999094638"));
        classIds.add(new ObjectId("5d2d339e6de1d24999094639"));

        classIds.add(new ObjectId("5d2d339e6de1d2499909463a"));
        classIds.add(new ObjectId("5d2d339e6de1d2499909463b"));
        classIds.add(new ObjectId("5d2d339e6de1d2499909463c"));
        classIds.add(new ObjectId("5d2d339e6de1d2499909463d"));
        classIds.add(new ObjectId("5d2d339e6de1d2499909463e"));

        classIds.add(new ObjectId("5d2d339e6de1d2499909463f"));
        classIds.add(new ObjectId("5d2d339e6de1d24999094640"));
        classIds.add(new ObjectId("5d2d339e6de1d24999094641"));
        classIds.add(new ObjectId("5d2d339e6de1d24999094642"));
        classIds.add(new ObjectId("5d2d339e6de1d24999094643"));

        List<ClientClassRelaEntry> addEntries = new ArrayList<ClientClassRelaEntry>();

        int index = 0;
        for(String clientId: clientIds) {
            ClientClassRelaEntry entry = dao.getClientClassRelaByClientId(clientId);
            if(null==entry) {
                entry = new ClientClassRelaEntry(schoolId, clientId, classIds.get(index));
                addEntries.add(entry);
            }else{
                entry.setClassId(classIds.get(index));
                dao.updateClientClassRelaEntry(entry);
            }
            index++;
        }
        if(addEntries.size()>0) {
            dao.addClientClassRelaEntries(addEntries);
        }
    }
}
