package com.sql.oldDataPojo;

import com.pojo.pet.PetInfo;
import com.pojo.pet.UserPetEntry;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class UserPetDTO {

    /**
     * 用户和宠物关联 id
     */
	private String id;
    /**
     * 用户 id
     */
	private String userid;

    private List<PetDTO> pets;



    public UserPetDTO(){

    }

    public UserPetDTO(UserPetEntry userPetEntry) {
        this.id = userPetEntry.getID().toString();
        this.userid=userPetEntry.getUserid().toString();
        // pets
        if(userPetEntry.getPetInfos()!=null && !userPetEntry.getPetInfos().isEmpty())
        {
            this.pets = new ArrayList<PetDTO>();
            for(PetInfo petInfo : userPetEntry.getPetInfos())
            {
                this.pets.add(new PetDTO(petInfo));
            }
        }
    }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

    public List<PetDTO> getPets() {
        return pets;
    }

    public void setPets(List<PetDTO> pets) {
        this.pets = pets;
    }

    /** 从当前传入的DTO产生Entry
     * @return
     */
    public UserPetEntry buildUserPetEntry(){
        List<PetInfo> petInfos =null;
        if(getPets()!=null && !getPets().isEmpty())
        {
            petInfos = new ArrayList<PetInfo>();
            for(PetDTO petDTO: getPets())
            {
                petInfos.add(petDTO.buildPetEntry());
            }
        }
        ObjectId userId = null;
        if(this.getUserid()!=null){
            userId = new ObjectId(this.getUserid());
        }
        UserPetEntry userPetEntry = new UserPetEntry(
                userId,
                petInfos
        );
        return userPetEntry;
    }
}
