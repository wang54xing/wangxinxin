package com.fulaan.schoolbase.service;

import com.fulaan.schoolbase.dto.ClassRoomDTO;
import com.fulaan.schoolbase.dto.SchoolLoopDTO;
import com.fulaan.utils.RESTAPI.bo.expand.ExpandClassAPI;
import com.fulaan.utils.RESTAPI.bo.notice.NoticeBaseAPI;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sys.utils.JsonUtil;
import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wang_xinxin on 2017/1/18.
 */
@Service
public class ClassRoomService {

    private NotClassroomReserveService classroomReserveService = new NotClassroomReserveService();


    /**
     * 添加教学楼
     * @param schoolLoopDTO
     */
    public String addSchoolLoop(SchoolLoopDTO schoolLoopDTO) {
        return NoticeBaseAPI.addSchoolLoop(schoolLoopDTO);
    }

    /**
     * 删除教学楼
     * @param id
     */
    public void delSchoolLoop(String id) {
        NoticeBaseAPI.delSchoolLoop(new ObjectId(id));
    }

    /**
     * 查询教学楼
     * @param id
     * @return
     */
    public SchoolLoopDTO getSchoolLoop(ObjectId id) {
        String result = NoticeBaseAPI.getSchoolLoop(id);
        SchoolLoopDTO dto = new SchoolLoopDTO();
        try {
            JSONObject dataJson = new JSONObject(result);
            JSONObject rows = dataJson.getJSONObject("message");
            dto = (SchoolLoopDTO) JsonUtil.JSONToObj(rows.toString(), SchoolLoopDTO.class);
        } catch (Exception e) {
            e.getMessage();
        }
        return dto;
    }

    /**
     * 查询教学楼列表
     * @param schoolId
     * @return
     */
    public List<SchoolLoopDTO> getSchoolLoopList(ObjectId schoolId) {
        String result = NoticeBaseAPI.getSchoolLoopList(schoolId);
        List<SchoolLoopDTO> dtos = new ArrayList<SchoolLoopDTO>();
        try {
            JSONObject dataJson = new JSONObject(result);
            JSONArray rows = dataJson.getJSONArray("message");
            if(rows!=null&&rows.length()>0) {
                for (int j = 0; j < rows.length(); j++) {
                    JSONObject info = rows.getJSONObject(j);
                    SchoolLoopDTO dto = (SchoolLoopDTO) JsonUtil.JSONToObj(info.toString(), SchoolLoopDTO.class);
                    dtos.add(dto);
                }
            }

        } catch (Exception e) {
            e.getMessage();
        }
        return dtos;
    }

    /**
     * 更新教学楼
     * @param schoolLoopDTO
     * @return
     */
    public void updSchoolLoop(SchoolLoopDTO schoolLoopDTO) {
        NoticeBaseAPI.updSchoolLoop(schoolLoopDTO);
    }

    /**
     * 添加教室
     * @param dto
     * @return
     */
    public String addClassRoom(ClassRoomDTO dto) {
        return NoticeBaseAPI.addClassRoom(dto);
    }

    /**
     * 更新教室
     * @param dto
     */
    public void updClassRoom(ClassRoomDTO dto) {
        NoticeBaseAPI.updClassRoom(dto);
    }

    /**
     * 删除教室
     * @param id
     */
    public void delClassRoom(String id) {
        NoticeBaseAPI.delClassRoom(new ObjectId(id));
    }

    /**
     * 查询单条教室
     * @param id
     * @return
     */
    public ClassRoomDTO getClassRoom(ObjectId id) {
        String result = NoticeBaseAPI.getClassRoom(id);
        ClassRoomDTO dto = new ClassRoomDTO();
        try {
            JSONObject dataJson = new JSONObject(result);
            JSONObject rows = dataJson.getJSONObject("message");
            dto = (ClassRoomDTO) JsonUtil.JSONToObj(rows.toString(), ClassRoomDTO.class);
        } catch (Exception e) {
            e.getMessage();
        }
        return dto;
    }

    /**
     * 查询楼里面的教室
     * @param loopId
     * @return
     */
    public Map getClassRoomList(ObjectId loopId, int page, int pageSize) {
        Map map = new HashMap();
        String result = NoticeBaseAPI.getClassRoomList(loopId,page,pageSize);
        List<ClassRoomDTO> dtos = new ArrayList<ClassRoomDTO>();
        try {
            JSONObject dataJson = new JSONObject(result);
            JSONObject reMap = dataJson.getJSONObject("message");
            JSONArray rows = reMap.getJSONArray("rows");
            if(rows!=null&&rows.length()>0) {
                for (int j = 0; j < rows.length(); j++) {
                    JSONObject info = rows.getJSONObject(j);
                    ClassRoomDTO dto = (ClassRoomDTO) JsonUtil.JSONToObj(info.toString(), ClassRoomDTO.class);
                    dtos.add(dto);
                }
            }
            map.put("rows",dtos);
            map.put("count",reMap.getInt("count"));
            map.put("page", page);
            map.put("pageSize", pageSize);
        } catch (Exception e) {
            e.getMessage();
        }
        return map;
    }
    
    
    
    
    /**
     * 通过学校ID 查找教室
     * @param schooId
     * @return
     */
    public Map<ObjectId,ClassRoomDTO> getClassRoomListByID(ObjectId schooId) {
    	Map<ObjectId,ClassRoomDTO> retMap =new HashMap<ObjectId, ClassRoomDTO>();
        String result = ExpandClassAPI.getClassRoomBysid(schooId.toString());
        try {
        	
        	Gson gson =new Gson();
        	List<ClassRoomDTO> dtoList =gson.fromJson(result, new TypeToken<List<ClassRoomDTO>>(){}.getType());
     
            if(dtoList!=null && dtoList.size()>0) {
            	for(ClassRoomDTO dto:dtoList)
            	{
            		 retMap.put(new ObjectId(dto.getId()), dto);
            	}
            }
        } catch (Exception e) {
            e.getMessage();
        }
        return retMap;
    }
    /**
     * 查询教室类型
     * @param schoolId
     * @return
     */
    public String getClassTypeList(ObjectId schoolId) {
        return NoticeBaseAPI.getClassTypeList(schoolId.toString());
    }

    /**
     * 添加教室类型
     * @param name
     * @param schoolId
     * @return
     */
    public String addClassType(String name, ObjectId schoolId) {
        return NoticeBaseAPI.addClassType(schoolId.toString(),name);
    }

    /**
     * 更新教室类型
     * @param id
     * @param name
     */
    public void updClassType(String id, String name) {
        NoticeBaseAPI.updClassType(id,name);
    }

    /**
     * 删除教室类型
     * @param id
     */
    public void delClassType(String id) {
        NoticeBaseAPI.delClassType(id);
    }

    /**
     *
     * @param loopId
     * @param ceng
     * @param typeId
     * @return
     */
    public Map getClassRooms(ObjectId schoolId, String loopId, int ceng, String typeId, String keyword) {
        Map map = new HashMap();
        List<Map<String, Object>> classroomList = classroomReserveService.getNotClassroomReserveEntry(schoolId,2);
        List<ObjectId> classRoomIds = new ArrayList<ObjectId>();
        if (classroomList!=null && classroomList.size()!=0) {
            for (Map<String,Object> classroom : classroomList) {
                classRoomIds.add(new ObjectId(classroom.get("rid").toString()));
            }
        }
        String result = NoticeBaseAPI.getClassRooms(loopId,ceng,typeId,keyword,classRoomIds);
        List<ClassRoomDTO> dtos = new ArrayList<ClassRoomDTO>();
        try {
            JSONObject dataJson = new JSONObject(result);
            JSONArray rows = dataJson.getJSONArray("message");
            if(rows!=null&&rows.length()>0) {
                for (int j = 0; j < rows.length(); j++) {
                    JSONObject info = rows.getJSONObject(j);
                    ClassRoomDTO dto = (ClassRoomDTO) JsonUtil.JSONToObj(info.toString(), ClassRoomDTO.class);
                    dtos.add(dto);
                }
            }
            map.put("rows",dtos);
        } catch (Exception e) {
            e.getMessage();
        }
        return map;
    }
}
