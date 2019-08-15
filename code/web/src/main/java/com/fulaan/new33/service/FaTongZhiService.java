package com.fulaan.new33.service;

import com.fulaan.utils.RESTAPI.ta.notice.NoticeAPI;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by albin on 2018/8/28.
 */
@Service
public class FaTongZhiService {

    /***
     *
     * @param title  标题
     * @param sid
     * @param uid
     * @param receiverIds 发通知对象
     */
    public void faTongZhi(String title,String sid, String uid, List<String> receiverIds) {
        //发通知
        ObjectId id = new ObjectId();
        Map<String, Object> NoticeDto = new HashMap<String, Object>();
        NoticeDto.put("id", id.toString());
        NoticeDto.put("schoolId", sid.toString());
        NoticeDto.put("userId", uid);
        NoticeDto.put("title", title);
        NoticeDto.put("content", title);
        NoticeDto.put("contentTxt", title);
        NoticeDto.put("platform", 1);
        NoticeDto.put("tag", 1);
        NoticeDto.put("priority", 2);
        NoticeDto.put("begin", System.currentTimeMillis());
        NoticeDto.put("end", System.currentTimeMillis() + 1L);
        //ta
        Map<String, Object> NoticeRelationDto = new HashMap<String, Object>();
        NoticeRelationDto.put("noticeId", id.toString());
        NoticeRelationDto.put("senderId", uid);
        NoticeRelationDto.put("readerIds", new ArrayList<String>());
        NoticeRelationDto.put("receiverIds", receiverIds);
        NoticeRelationDto.put("groupIds", new ArrayList<String>());
        NoticeRelationDto.put("status", 0);
        NoticeRelationDto.put("platform", 1);
        NoticeRelationDto.put("tag", 1);
        NoticeAPI.addRelation(NoticeRelationDto);
        NoticeAPI.addBoRelation(NoticeDto);
    }
}
