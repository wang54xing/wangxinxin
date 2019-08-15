package com.pojo.navigation;

/**
 * Created by wang_xinxin on 2017/5/4.
 */
public class NameValueDTO {
    private String name;

    private Object value;

    public NameValueDTO() {

    }

    public NameValueDTO(String name, Object value) {
        this.name = name;
        this.value = value;
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
