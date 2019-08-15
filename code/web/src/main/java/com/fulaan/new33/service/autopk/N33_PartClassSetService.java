package com.fulaan.new33.service.autopk;

import com.db.new33.autopk.N33_PartClassFenDuanDao;
import com.db.new33.autopk.N33_PartClassSetDao;
import com.fulaan.new33.dto.autopk.N33_PartClassSetDTO;
import com.fulaan.new33.dto.isolate.ClassInfoDTO;
import com.fulaan.new33.service.isolate.BaseService;
import com.fulaan.new33.service.isolate.IsolateClassService;
import com.pojo.autoPK.N33_PartClassFenDuanEntry;
import com.pojo.autoPK.N33_PartClassSetEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class N33_PartClassSetService extends BaseService {

    private N33_PartClassFenDuanDao partClassFenDuanDao = new N33_PartClassFenDuanDao();

    private N33_PartClassSetDao partClassSetDao = new N33_PartClassSetDao();

    @Autowired
    private IsolateClassService classService;

    @Autowired
    private N33_AutoTeaSetService autoTeaSetService;


    /**
     * 查询最大分段信息
     * @param schoolId
     * @param gradeId
     * @param ciId
     * @return
     */
    public int getMaxFenDuan(ObjectId schoolId, ObjectId gradeId, ObjectId ciId){
        N33_PartClassFenDuanEntry entry = partClassFenDuanDao.getEntry(schoolId, gradeId, ciId);
        if(null==entry){
            entry = new N33_PartClassFenDuanEntry();
            entry.setID(new ObjectId());
            entry.setSchoolId(schoolId);
            entry.setCiId(ciId);
            entry.setGradeId(gradeId);
            entry.setMaxFenDuan(Constant.ONE);
            partClassFenDuanDao.addEntry(entry);
        }
        return entry.getMaxFenDuan();
    }

    /**
     * 查询使用中的最大分段信息
     * @param schoolId
     * @param gradeId
     * @param ciId
     * @return
     */
    public int getUsePartMaxFenDuan(ObjectId schoolId, ObjectId gradeId, ObjectId ciId){
        Map autoOtherSet = autoTeaSetService.getAutoOtherSet(schoolId, gradeId, ciId);
        String periodCheck = autoOtherSet.get("periodCheck")==null?"":autoOtherSet.get("periodCheck").toString();
        if("1".equals(periodCheck)) {
            N33_PartClassFenDuanEntry entry = partClassFenDuanDao.getEntry(schoolId, gradeId, ciId);
            if (null == entry) {
                entry = new N33_PartClassFenDuanEntry();
                entry.setID(new ObjectId());
                entry.setSchoolId(schoolId);
                entry.setCiId(ciId);
                entry.setGradeId(gradeId);
                entry.setMaxFenDuan(Constant.ONE);
                partClassFenDuanDao.addEntry(entry);
            }
            return entry.getMaxFenDuan();
        }else{
            return Constant.ONE;
        }
    }

    /**
     * 更新最大分段信息
     * @param schoolId
     * @param gradeId
     * @param ciId
     * @param maxFenDuan
     */
    public void updateMaxFenDuanSet(ObjectId schoolId, ObjectId gradeId, ObjectId ciId, int maxFenDuan) {
        N33_PartClassFenDuanEntry entry = partClassFenDuanDao.getEntry(schoolId, gradeId, ciId);
        if(null==entry){
            entry = new N33_PartClassFenDuanEntry();
            entry.setID(new ObjectId());
            entry.setSchoolId(schoolId);
            entry.setCiId(ciId);
            entry.setGradeId(gradeId);
            entry.setMaxFenDuan(maxFenDuan);
            partClassFenDuanDao.addEntry(entry);
        }else{
            entry.setMaxFenDuan(maxFenDuan);
            partClassFenDuanDao.updEntry(entry);
            updatePartClassFenDuan(maxFenDuan);
        }
    }

    /**
     * 查询年级下的班级分段信息
     * @param schoolId
     * @param gradeId
     * @param ciId
     * @return
     */
    public Map getPartClassInfo(ObjectId schoolId, ObjectId gradeId, ObjectId ciId) {
        Map reMap = new HashMap();
        List<N33_PartClassSetEntry> entries = partClassSetDao.getPartClassEntries(schoolId, gradeId, ciId);
        Map<String, ClassInfoDTO> clsMap = new HashMap<String, ClassInfoDTO>();
        List<ClassInfoDTO> classList = classService.getClassByXqAndGrade(ciId, schoolId, gradeId);
        for(ClassInfoDTO clsDto:classList){
            clsMap.put(clsDto.getClassId(), clsDto);
        }
        if(entries.size()==0){
            entries = new ArrayList<N33_PartClassSetEntry>();
            for(ClassInfoDTO clsDto:classList){
                N33_PartClassSetEntry setEntry = new N33_PartClassSetEntry();
                setEntry.setID(new ObjectId());
                setEntry.setSchoolId(schoolId);
                setEntry.setCiId(ciId);
                setEntry.setGradeId(gradeId);
                setEntry.setFenDuan(Constant.ONE);
                setEntry.setClassId(new ObjectId(clsDto.getClassId()));
                entries.add(setEntry);
            }
            partClassSetDao.batchAddEntries(entries);
        }
        int maxFenDuan = getMaxFenDuan(schoolId, gradeId, ciId);
        reMap.put("maxFenDuan", maxFenDuan);

        Map<Integer, List<N33_PartClassSetDTO>> fdDtosMap = new HashMap<Integer, List<N33_PartClassSetDTO>>();
        for(N33_PartClassSetEntry entry:entries){
            N33_PartClassSetDTO setDto = new N33_PartClassSetDTO(entry);
            ClassInfoDTO clsDto = clsMap.get(setDto.getClassId());
            if(null!=clsDto){
                setDto.setClassName(clsDto.getClassName());
                setDto.setShowSlassName(clsDto.getGname()+"（"+clsDto.getXh()+"）班");
                setDto.setClassSort(clsDto.getXh());
                setDto.setGradeName(clsDto.getGname());
            }else{
                continue;
            }
            List<N33_PartClassSetDTO> setDtos = fdDtosMap.get(setDto.getFenDuan());
            if(null==setDtos){
                setDtos = new ArrayList<N33_PartClassSetDTO>();
            }
            setDtos.add(setDto);
            fdDtosMap.put(setDto.getFenDuan(), setDtos);
        }


        List<Map> fenDuanList = new ArrayList<Map>();

        for(int fenDuan = 1; fenDuan<=maxFenDuan; fenDuan++){
            Map fnMap = new HashMap();
            fnMap.put("fenDuan", fenDuan);
            fnMap.put("fenDuanName", "第"+fenDuan+"段");
            int clsCount = 0;
            List<N33_PartClassSetDTO> setDtos = fdDtosMap.get(fenDuan);
            if(null!=setDtos){
                clsCount = setDtos.size();
                sortPartClassSets(setDtos);
                fnMap.put("classList", setDtos);
                fnMap.put("firstClass", setDtos.get(0));
                List<N33_PartClassSetDTO> residueList = setDtos.subList(1, setDtos.size());
                fnMap.put("residueList", residueList);
            }
            fnMap.put("clsCount", clsCount);
            fenDuanList.add(fnMap);
        }
        reMap.put("fenDuanList", fenDuanList);
        return reMap;
    }

    public List<Map> getPartClassList(ObjectId schoolId, ObjectId gradeId, ObjectId ciId) {
        int maxFenDuan = getMaxFenDuan(schoolId, gradeId, ciId);
        List<N33_PartClassSetEntry> entries = partClassSetDao.getPartClassEntries(schoolId, gradeId, ciId);

        Map<String, ClassInfoDTO> clsMap = new HashMap<String, ClassInfoDTO>();
        List<ClassInfoDTO> classList = classService.getClassByXqAndGrade(ciId, schoolId, gradeId);
        for(ClassInfoDTO clsDto:classList){
            clsMap.put(clsDto.getClassId(), clsDto);
        }

        Map<Integer, List<N33_PartClassSetDTO>> fdDtosMap = new HashMap<Integer, List<N33_PartClassSetDTO>>();
        for(N33_PartClassSetEntry entry:entries){
            N33_PartClassSetDTO setDto = new N33_PartClassSetDTO(entry);
            ClassInfoDTO clsDto = clsMap.get(setDto.getClassId());
            if(null!=clsDto){
                setDto.setClassName(clsDto.getClassName());
                setDto.setShowSlassName(clsDto.getGname()+"（"+clsDto.getXh()+"）班");
                setDto.setClassSort(clsDto.getXh());
                setDto.setGradeName(clsDto.getGname());
            }else{
                continue;
            }
            List<N33_PartClassSetDTO> setDtos = fdDtosMap.get(setDto.getFenDuan());
            if(null==setDtos){
                setDtos = new ArrayList<N33_PartClassSetDTO>();
            }
            setDtos.add(setDto);
            fdDtosMap.put(setDto.getFenDuan(), setDtos);
        }

        List<Map> fenDuanList = new ArrayList<Map>();

        for(int fenDuan = 1; fenDuan<=maxFenDuan; fenDuan++){
            Map fnMap = new HashMap();
            fnMap.put("fenDuan", fenDuan);
            fnMap.put("fenDuanName", "第"+fenDuan+"段");
            List<N33_PartClassSetDTO> setDtos = fdDtosMap.get(fenDuan);
            if(null!=setDtos){
                sortPartClassSets(setDtos);
                fnMap.put("classList", setDtos);
            }
            fenDuanList.add(fnMap);
        }
        return fenDuanList;
    }

    /**
     * 按照班级序号排序
     * @param list
     */
    private void sortPartClassSets(List<N33_PartClassSetDTO> list) {
        Collections.sort(list, new Comparator<N33_PartClassSetDTO>() {
            public int compare(N33_PartClassSetDTO obj1, N33_PartClassSetDTO obj2) {
                int num1 = obj1.getClassSort();
                int num2 = obj2.getClassSort();
                return num1-num2;
            }
        });
    }


    public List<N33_PartClassSetDTO> getFenDuanPartClassInfo(ObjectId schoolId, ObjectId gradeId, ObjectId ciId, int fenDuan) {
        List<N33_PartClassSetDTO> setDtos = new ArrayList<N33_PartClassSetDTO>();
        List<N33_PartClassSetEntry> entries = partClassSetDao.getPartClassEntries(schoolId, gradeId, ciId, fenDuan);
        for(N33_PartClassSetEntry entry:entries) {
            N33_PartClassSetDTO setDto = new N33_PartClassSetDTO(entry);
            setDtos.add(setDto);
        }
        return setDtos;
    }

    /**
     * 修改班级分段
     * @param id
     * @param fenDuan
     */
    public void updatePartClassFenDuan(ObjectId id, int fenDuan) {
        partClassSetDao.updatePartClassFenDuan(id, fenDuan);
    }

    /**
     * 修改班级分段
     * @param maxFenDuan
     */
    public void updatePartClassFenDuan(int maxFenDuan) {
        partClassSetDao.updatePartClassFenDuan(maxFenDuan);
    }
}
