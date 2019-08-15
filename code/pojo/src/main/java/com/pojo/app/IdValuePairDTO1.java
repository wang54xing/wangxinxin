package com.pojo.app;

import org.bson.types.ObjectId;

/**
 * Created by fl on 2016/2/2.
 */
public class IdValuePairDTO1 {
    private String id;
    private Object value;

    public IdValuePairDTO1(){}

    public IdValuePairDTO1(String id, Object value){
        this.id = id;
        this.value = value;
    }

    public IdValuePairDTO1(IdValuePair ip) {
        super();
        this.id =ip.getId().toString();
        this.value = ip.getValue();
    }

    public IdValuePair exportEntry(){
        return new IdValuePair(new ObjectId(id), value);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
