package com.pojo.app;

/**
 * Created by fl on 2016/1/26.
 */
public class NameValuePairDTO {
    private String name;
    private Object value;

    public NameValuePairDTO(){}

    public NameValuePairDTO(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    public NameValuePairDTO(NameValuePair nameValuePair){
        this.name = nameValuePair.getName();
        this.value = nameValuePair.getValue();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
