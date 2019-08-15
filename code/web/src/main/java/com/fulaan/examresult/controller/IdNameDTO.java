package com.fulaan.examresult.controller;

import com.pojo.app.IdValuePairDTO;

/**
 * Created by fl on 2015/7/8.
 */
public class IdNameDTO {
    private String id;
    private String name;

    public IdNameDTO() {
    }
    public IdNameDTO(IdValuePairDTO i) {
        this.id = i.getId().toString();
        this.name = (String)i.getValue();
    }

    public IdNameDTO(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public boolean equals(Object obj){
        IdNameDTO idNameDTO = (IdNameDTO)obj;
        if(this.id.equals(idNameDTO.getId())){
            return true;
        }
        return false;
    }
}
