package com.fulaan.restapi.client;

import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guojing on 2016/10/12.
 */

@Component
public class RESTClient {
//    private RestTemplate template = new RestTemplate();
//
//    private final static String url = "http://localhost:8081/";
//
//    public List<PetTypeDTO> getPetTypeList() {
//        return template.getForObject(url+"demo/get/", ArrayList.class);
//    }
//
//    public PetTypeDTO getPetById(ObjectId id) {
//        ResponseEntity<PetTypeDTO> response=template.getForEntity(url + "demo/{id}", PetTypeDTO.class, id);
//        if(response.getStatusCode() != HttpStatus.NOT_FOUND){
//            return response.getBody();
//        }
//        return new PetTypeDTO();
//    }
    /*public String show() {
        return template.getForObject(url , String.class, new String[]{});
    }

    public String addUser(String user) {
        return template.postForObject(url + "add.do?user={user}", null, String.class, user);
    }

    public String editUser(String user) {
        template.put(url + "edit.do?user={user}", null, user);
        return user;
    }

    public String removeUser(String id) {
        template.delete(url + "/remove/{id}.do", id);
        return id;
    }*/
}
