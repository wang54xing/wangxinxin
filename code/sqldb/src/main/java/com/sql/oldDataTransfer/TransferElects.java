package com.sql.oldDataTransfer;

import com.db.elect.ElectDao;
import com.pojo.elect.Candidate;
import com.pojo.elect.ElectEntry;
import com.pojo.elect.OldCandidate;
import com.pojo.elect.OldElectEntry;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by qinbo on 15/6/11.
 */
public class TransferElects {


    public static void main(String[] args){
        TransferElects transferElects = new TransferElects();
        transferElects.transfer();
    }
    public void transfer(){
        ElectDao electDao = new ElectDao();
        List<OldElectEntry> oldElectEntryList = electDao.getOldElectEntryList();

        for(OldElectEntry oldElectEntry:oldElectEntryList){

            boolean needadded = true;
            List<ObjectId> classIdList = new ArrayList<ObjectId>();
            if(oldElectEntry.getClassIds()!=null) {
                for (Integer oclassId : oldElectEntry.getClassIds()) {
                    ObjectId classId = TransferClass.classMap.get(oclassId);
                    if (classId != null) {
                        classIdList.add(classId);
                    }
                }
            }
            if(classIdList.isEmpty()){
                classIdList = null;
            }
            long startTime = System.currentTimeMillis();
            if(oldElectEntry.getStartDate()!=null){
                startTime = ((Date)oldElectEntry.getStartDate()).getTime();
            }
            long endTime = System.currentTimeMillis();
            if(oldElectEntry.getEndDate()!=null){
                endTime = ((Date)oldElectEntry.getEndDate()).getTime();
            }
            long publishTime = System.currentTimeMillis();
            if(oldElectEntry.getPublishDate()!=null){
                publishTime = ((Date)oldElectEntry.getPublishDate()).getTime();
            }
            long modifyTime = System.currentTimeMillis();
            if(oldElectEntry.getModifyTime()!=null){
                modifyTime = ((Date)oldElectEntry.getModifyTime()).getTime();
            }

            ObjectId pbId = TransferUser.userMap.get(oldElectEntry.getPublisher());
            if(pbId ==null)
            {
                //pbId = TransferUser.unkownUser.getID();
                needadded = false;
            }

            int parentEligible = 1;
            if(oldElectEntry.getParentEligible()!=null){
                parentEligible = (Boolean)oldElectEntry.getParentEligible()?1:0;

            }
            else
            {
                needadded = false;
            }
            int studentEligible = 1;
            if(oldElectEntry.getStudentEligible()!=null){
                studentEligible = (Boolean)oldElectEntry.getStudentEligible()?1:0;

            }
            else
            {
                needadded = false;
            }


            int teacherEligible = 1;
            if(oldElectEntry.getTeacherEligible()!=null){
                teacherEligible = (Boolean)oldElectEntry.getTeacherEligible()?1:0;

            }
            else
            {
                needadded = false;
            }

            int parentVotable = 1;
            if(oldElectEntry.getParentVotable()!=null){
                parentVotable = (Boolean)oldElectEntry.getParentVotable() ?1:0;

            }
            else
            {
                needadded = false;
            }
            int teacherVotable = 1;
            if(oldElectEntry.getTeacherVotable()!=null){
                teacherVotable = (Boolean)oldElectEntry.getTeacherVotable()?1:0;

            }
            else
            {
                needadded = false;
            }
            int studentVotable = 1;
            if(oldElectEntry.getStudentVotable()!=null){
                studentVotable = (Boolean)oldElectEntry.getStudentVotable()?1:0;

            }
            else
            {
                needadded = false;
            }



            List<Candidate> candidateList = new ArrayList<Candidate>();
            if(oldElectEntry.getCandidates()!=null) {
                for (OldCandidate oldCandidate : oldElectEntry.getCandidates()) {
                    ObjectId candidateId = TransferUser.userMap.get(oldCandidate.getId());


                    long apptime = System.currentTimeMillis();
                    if (oldCandidate.getSignTime() != null) {
                        apptime = ((Date) oldCandidate.getSignTime()).getTime();
                    }

                    List<ObjectId> ballotIds = new ArrayList<ObjectId>();

                    if(oldCandidate.getBallots()!=null) {
                        for (Integer ocid : oldCandidate.getBallots()) {
                            if (TransferUser.userMap.get(ocid) != null)
                                ballotIds.add(TransferUser.userMap.get(ocid));
                        }
                    }
                    if (candidateId != null) {
                        Candidate candidate = new Candidate(
                                candidateId,
                                oldCandidate.getName(),
                                oldCandidate.getManifesto(),
                                null,//音频
                                oldCandidate.getPicUrls(),
                                null,//视频
                                apptime,
                                ballotIds

                        );
                        candidateList.add(candidate);
                    }

                }
            }


//                ElectEntry electEntry = new ElectEntry(
//                        oldElectEntry.getName(),
//                        oldElectEntry.getDescription(),
//                        classIdList,
//                        TransferSchool.schoolMap.get(oldElectEntry.getSchoolId()),
//                        startTime,
//                        endTime,
//                        publishTime,
//                        modifyTime,
//                        pbId,
//                        parentEligible,
//                        studentEligible,
//                        teacherEligible,
//                        0,
//                        parentVotable,
//                        studentVotable,
//                        teacherVotable,
//                        0,
//                        oldElectEntry.getBallotCount(),
//                        candidateList
//
//                );
//            if(needadded) {
//                    electDao.add(electEntry);
//            }

        }
    }
}
