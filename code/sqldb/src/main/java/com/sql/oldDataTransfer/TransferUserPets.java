package com.sql.oldDataTransfer;

import com.db.pet.PetTypeDao;
import com.db.pet.UserPetDao;
import com.pojo.pet.PetTypeEntry;
import com.sql.dao.RefactorMapper;
import com.sql.oldDataPojo.PetDTO;
import com.sql.oldDataPojo.PetInfo;
import com.sql.oldDataPojo.UserPetDTO;
import com.sql.oldDataPojo.UserPetInfo;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by qinbo on 15/3/20.
 */
public class TransferUserPets {

    UserPetDao userPetDao=new UserPetDao();
    PetTypeDao petTypeDao=new PetTypeDao();

    public static Map<Integer,ObjectId> userPetsMap=new HashMap<Integer, ObjectId>();

    public static Map<Integer,ObjectId> petInfoMap=new HashMap<Integer, ObjectId>();

    private SqlSessionFactory getSessionFactory() {
        SqlSessionFactory sessionFactory = null;
        String resource = "configuration.xml";
        try {
            sessionFactory = new SqlSessionFactoryBuilder().build(Resources.getResourceAsReader(resource));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sessionFactory;
    }

    private RefactorMapper getRefactorMapper(){
        SqlSession sqlSession = getSessionFactory().openSession();
        RefactorMapper refactorMapper = sqlSession.getMapper(RefactorMapper.class);
        return refactorMapper;
    }

    private List<UserPetInfo> userPetsList = null;
    private List<PetInfo> petTypeList = null;
    private Map<Integer,UserPetDTO> petdtoMap=new HashMap<Integer, UserPetDTO>();

    public void transferUserPetInfo(Map<Integer,ObjectId> userMap){
        transferPetTypeInfo();
        userPetsList =  getRefactorMapper().getUserPetInfo();
        for(UserPetInfo userPetInfo  :userPetsList){
            UserPetDTO userpetDTO=petdtoMap.get(userPetInfo.getUserid());
            if(userpetDTO==null){
                userpetDTO=new UserPetDTO();
                String userid=userMap.get(userPetInfo.getUserid())==null?null:userMap.get(userPetInfo.getUserid()).toString();
                userpetDTO.setUserid(userid);
                List<PetDTO> pets=new ArrayList<PetDTO>();
                PetDTO petDTO = new PetDTO();
                String petId=petInfoMap.get(userPetInfo.getPetid())==null?null:petInfoMap.get(userPetInfo.getPetid()).toString();
                petDTO.setPetid(petId);
                petDTO.setPetname(userPetInfo.getPetname());
                petDTO.setSelecttype(userPetInfo.getSelecttype());
                petDTO.setCreatedate(userPetInfo.getCreatedate());
                petDTO.setIshatch(userPetInfo.getIshatch());
                petDTO.setUpdatedate(userPetInfo.getUpdatedate());

                pets.add(petDTO);
                userpetDTO.setPets(pets);
                petdtoMap.put(userPetInfo.getUserid(),userpetDTO);
            }else{
                List<PetDTO> pets=userpetDTO.getPets();
                PetDTO petDTO = new PetDTO();
                String petId=petInfoMap.get(userPetInfo.getPetid())==null?null:petInfoMap.get(userPetInfo.getPetid()).toString();
                petDTO.setPetid(petId);
                petDTO.setPetname(userPetInfo.getPetname());
                petDTO.setSelecttype(userPetInfo.getSelecttype());
                petDTO.setCreatedate(userPetInfo.getCreatedate());
                petDTO.setIshatch(userPetInfo.getIshatch());
                petDTO.setUpdatedate(userPetInfo.getUpdatedate());
                pets.add(petDTO);
                userpetDTO.setPets(pets);
                petdtoMap.put(userPetInfo.getUserid(),userpetDTO);
            }

        }

        for (UserPetDTO userpet : petdtoMap.values()) {
            dealUserPetInfo(userpet);
        }
        System.out.println("UserPetInf size:"+userPetsList.size());

    }

    private void dealUserPetInfo(UserPetDTO userpet) {
        userPetDao.addUserPetEntry(userpet.buildUserPetEntry());

    }

    public void transferPetTypeInfo(){
        petTypeList =  getRefactorMapper().getPetTypeInfo();
        for(PetInfo petInfo  :petTypeList){
            dealPetTypeInfo(petInfo);
        }
        System.out.println("PetInfo size:" + petTypeList.size());
    }

    private void dealPetTypeInfo(PetInfo petInfo) {
        String petEnName=petInfo.getPetimage().replace("/pet/images/","").replace(".png","");
        petInfo.setPetimage(petInfo.getPetimage().replace("/pet/images","/img/pet"));
        petInfo.setMinpetimage(petInfo.getMinpetimage().replace("/pet/images", "/img/pet"));
        petInfo.setMaxpetimage(petInfo.getMaxpetimage().replace("/pet/images", "/img/pet"));
        petInfo.setMiddlepetimage(petInfo.getMiddlepetimage().replace("/pet/images","/img/pet"));
        PetTypeEntry petTypeEntry = new PetTypeEntry(
                petInfo.getPetname(),
                petEnName,
                petInfo.getPetexplain(),
                1,
                0,
                petInfo.getPetimage(),
                petInfo.getMinpetimage(),
                petInfo.getMaxpetimage(),
                petInfo.getMiddlepetimage()

        );
        petTypeDao.addPetTypeEntry(petTypeEntry);
        petInfoMap.put(petInfo.getId(),petTypeEntry.getID());
    }

     public static void main(String[] args) throws Exception{
         TransferUserPets transfer = new TransferUserPets();
         transfer.transferUserPetInfo(new HashMap<Integer, ObjectId>());
     }
}
