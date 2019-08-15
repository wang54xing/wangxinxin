package com.fulaan.new33.service;

import com.db.new33.N33_StudentTagDao;
import com.db.new33.paike.N33_JXBDao;
import com.pojo.new33.N33_StudentTagEntry;
import com.pojo.new33.paike.N33_JXBEntry;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FenBanService {

    private N33_JXBDao n33_jxbDao = new N33_JXBDao();
    private N33_StudentTagDao n33_studentTagDao = new N33_StudentTagDao();

    /* *
     * 检测冲突
     * @param orginalIds
     * @param waitForAdd
     * @return List<ObjectId> null无冲突 冲突的学生
     */
    public List<ObjectId> isStudentsConflicted(N33_JXBEntry jxbEntry,N33_StudentTagEntry n33_studentTagEntry){
        List<ObjectId> orginalIds = jxbEntry.getStudentIds();
        List<N33_StudentTagEntry.StudentInfoEntry> waitForAdd = n33_studentTagEntry.getStudents();
        if(orginalIds==null||waitForAdd==null||orginalIds.size()==0||waitForAdd.size()==0){
            return null;
        }
        List<ObjectId> repetitiveIds = new ArrayList<ObjectId>();
        for(N33_StudentTagEntry.StudentInfoEntry entry:waitForAdd){
            if(orginalIds.contains(entry.getStuId())){
                repetitiveIds.add(entry.getStuId());
            }
        }
        if(repetitiveIds.isEmpty()){
            return null;
        }
        else{
            return repetitiveIds;
        }

    }
    /* *
     * 给教学班添加学生标签(检测冲突)
     * @param jxbId
     * @param tagId
     * @return java.util.Map<java.lang.String,java.lang.Object>
     */
    public Map<String,Object> addTagToJxb(ObjectId jxbId, ObjectId tagId){
        Map<String,Object> result = new HashMap<String, Object>();
        N33_JXBEntry jxbEntry = n33_jxbDao.getJXBById(jxbId);
        N33_StudentTagEntry n33_studentTagEntry = n33_studentTagDao.getTagById(tagId);
        List<ObjectId> repetitiveIds = isStudentsConflicted(jxbEntry,n33_studentTagEntry);
        if(repetitiveIds!=null){
            result.put("conflict",1);
            return result;
        }
        else{
            List<ObjectId> tagIds = new ArrayList<ObjectId>();
            tagIds.add(tagId);
            tagIds.addAll(jxbEntry.getTagIds());

            List<ObjectId> stuIds = new ArrayList<ObjectId>();
            stuIds.addAll(jxbEntry.getStudentIds());
            for(N33_StudentTagEntry.StudentInfoEntry entry:n33_studentTagEntry.getStudents()){
                stuIds.add(entry.getStuId());
            }
            n33_jxbDao.updateTagStus(jxbEntry.getID(),tagIds,stuIds);
            result.put("conflict",0);
            return result;
        }
    }
    /* *
     *  从教学班中删除标签
     * @param jxbId
     * @param tagId
     * @return void
     */
    public void removeTagFromJxb(ObjectId jxbId, ObjectId tagId){
        N33_JXBEntry jxbEntry = n33_jxbDao.getJXBById(jxbId);
        N33_StudentTagEntry n33_studentTagEntry = n33_studentTagDao.getTagById(tagId);
        jxbEntry.getTagIds().remove(tagId);
        List<ObjectId> stuIds = new ArrayList<ObjectId>();
        for(N33_StudentTagEntry.StudentInfoEntry entry:n33_studentTagEntry.getStudents()){
            stuIds.add(entry.getStuId());
        }
        jxbEntry.getStudentIds().removeAll(stuIds);
        n33_jxbDao.updateTagStus(jxbId,jxbEntry.getTagIds(),jxbEntry.getStudentIds());
    }
    /* *
     *  更新学生数量
     * @param jxbId
     * @param studentIds
     * @param flag   0新增  1减少
     * @return void
     */
    public void updateJxbStus(ObjectId jxbId,List<ObjectId> studentIds,Integer flag){
        N33_JXBEntry jxbEntry = n33_jxbDao.getJXBById(jxbId);
        if(flag==1){
            List<ObjectId> waitForAdd = new ArrayList<ObjectId>();
            for(ObjectId id:studentIds){
                if(!jxbEntry.getStudentIds().contains(id)){
                    waitForAdd.add(id);
                }
            }
            jxbEntry.getStudentIds().addAll(waitForAdd);
        }
        else{
            jxbEntry.getStudentIds().removeAll(studentIds);
        }
        n33_jxbDao.updateN33_JXB(jxbEntry);
    }


//    public static void main(String[] args){
//        ObjectId id1 = new ObjectId("5bf26dea81e7b9550c791a3c");
//        ObjectId id2 = new ObjectId("5bf26dea81e7b9550c791a3c");
//        System.out.println(id1.equals(id2));
//    }

}
