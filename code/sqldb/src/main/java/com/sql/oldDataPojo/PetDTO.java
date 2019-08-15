package com.sql.oldDataPojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pojo.pet.PetInfo;
import com.sys.utils.CustomDateSerializer;
import org.bson.types.ObjectId;

import java.util.Date;

/**
 * Created by guojing on 2015/3/26.
 */
public class PetDTO {

    private String id;

    /**
     * 宠物 id
     */
    private String petid;
    /**
     * 宠物名称
     */
    private String petname;
    /**
     * 当前宠物
     */
    private int selecttype;
    /**
     * 创建时间
     */
    @JsonSerialize(using = CustomDateSerializer.class)
    private Date createdate;
    /**
     * 是否孵化
     */
    private int ishatch;
    /**
     * 更新时间
     */
    @JsonSerialize(using = CustomDateSerializer.class)
    private Date updatedate;
    /**
     * 宠物说明
     */
    private String petexplain;
    /**
     * 宠物标准图片
     */
    private String petimage;
    /**
     * 宠物小图片
     */
    private String minpetimage;
    /**
     * 宠物大图片
     */
    private String maxpetimage;
    /**
     * 宠物中等图片
     */
    private String middlepetimage;

    public PetDTO(){

    }
    public PetDTO(PetInfo petInfo) {
        this.id= petInfo.getId().toString();
        this.petid = petInfo.getPetid().toString();
        this.petname = petInfo.getPetname();
        this.selecttype = petInfo.getSelecttype();
        this.createdate = new Date(petInfo.getCreatedate());
        this.ishatch = petInfo.getIshatch();
        this.updatedate = new Date(petInfo.getUpdatedate());
        /*,PetTypeEntry petTypeEntry
        this.petexplain=petTypeEntry.getPetexplain();
        this.petimage=petTypeEntry.getPetimage();
        this.minpetimage=petTypeEntry.getMinpetimage();
        this.maxpetimage=petTypeEntry.getMaxpetimage();
        this.middlepetimage=petTypeEntry.getMiddlepetimage();*/
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPetid() {
        return petid;
    }

    public void setPetid(String petid) {
        this.petid = petid;
    }

    public String getPetname() {
        return petname;
    }

    public void setPetname(String petname) {
        this.petname = petname;
    }

    public int getSelecttype() {
        return selecttype;
    }

    public void setSelecttype(int selecttype) {
        this.selecttype = selecttype;
    }

    public Date getCreatedate() {
        return createdate;
    }

    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }

    public int getIshatch() {
        return ishatch;
    }

    public void setIshatch(int ishatch) {
        this.ishatch = ishatch;
    }

    public Date getUpdatedate() {
        return updatedate;
    }

    public void setUpdatedate(Date updatedate) {
        this.updatedate = updatedate;
    }

    public String getPetexplain() {
        return petexplain;
    }

    public void setPetexplain(String petexplain) {
        this.petexplain = petexplain;
    }

    public String getPetimage() {
        return petimage;
    }

    public void setPetimage(String petimage) {
        this.petimage = petimage;
    }

    public String getMinpetimage() {
        return minpetimage;
    }

    public void setMinpetimage(String minpetimage) {
        this.minpetimage = minpetimage;
    }

    public String getMaxpetimage() {
        return maxpetimage;
    }

    public void setMaxpetimage(String maxpetimage) {
        this.maxpetimage = maxpetimage;
    }

    public String getMiddlepetimage() {
        return middlepetimage;
    }

    public void setMiddlepetimage(String middlepetimage) {
        this.middlepetimage = middlepetimage;
    }



    public PetInfo buildPetEntry()
    {
        ObjectId petid=this.getPetid()==null?null:new ObjectId(this.getPetid());
        PetInfo petInfo =new PetInfo(
                petid,
                this.getPetname(),
                this.getSelecttype(),
                this.getCreatedate().getTime(),
                this.getIshatch(),
                this.getUpdatedate()==null?0:this.getUpdatedate().getTime()
        );
         return petInfo;
    }

    public PetInfo buildPetEntryAdd()
    {
        PetInfo petInfo =new PetInfo(
                this.getSelecttype(),
                this.getCreatedate().getTime(),
                this.getIshatch()
        );
        return petInfo;
    }

    public PetInfo buildPetEntryUpd()
    {
        PetInfo petInfo =new PetInfo(
                new ObjectId(this.getId()),
                new ObjectId(this.getPetid()),
                this.getPetname(),
                this.getSelecttype(),
                this.getIshatch(),
                this.getUpdatedate().getTime()
        );
        return petInfo;
    }
}
