package com.sql.oldDataPojo;

import java.io.Serializable;

/**
 * Created by qinbo on 15/8/17.
 */
public class ResourceKPDicInfo implements Serializable{


    private String category;
    private String code;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getKlpoint() {
        return klpoint;
    }

    public void setKlpoint(String klpoint) {
        this.klpoint = klpoint;
    }

    private String klpoint;

}
