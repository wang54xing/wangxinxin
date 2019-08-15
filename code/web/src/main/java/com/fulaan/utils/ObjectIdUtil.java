package com.fulaan.utils;

import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hao on 2015/4/20.
 */
public class ObjectIdUtil {
    public static List<String> objectIdList2StringList(List<ObjectId> objectIdList){
        List<String> stringList=new ArrayList<String>();
        if(objectIdList!=null){
            for(ObjectId objectId:objectIdList){
                stringList.add(objectId.toString());
            }
        }
        return  stringList;
    }


    public static  List<ObjectId> stringList2ObjectIdList(List<String> stringList){
        List<ObjectId> objectIdList=new ArrayList<ObjectId>();
        if(stringList!=null){
            for(String str:stringList){
                objectIdList.add(new ObjectId(str));
            }
        }
        return objectIdList;
    }
}
