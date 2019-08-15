package com.pojo.app;

import org.bson.types.ObjectId;

import com.sys.constants.Constant;

/**
 * 存储字段与字段值
 *
 * @author fourer
 */
public class IdValuePairDTO {

    private ObjectId id;
    private Object value;
    //作用于教材（是否是公开的）
    private int  is;

    public IdValuePairDTO(ObjectId id, Object value) {
        super();
        this.id = id;
        this.value = value;
    }


    public IdValuePairDTO(IdValuePair ip) {
        super();
        this.id = ip.getId();
        this.value = ip.getValue();
    }

    public IdValuePair exportEntry() {
        return new IdValuePair(id, value);
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }


    public String getIdStr() {
        if (null != this.id) {
            return this.id.toString();
        }
        return Constant.EMPTY;
    }
    
    public void setIdStr(String idStr) {
		
	}



    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        IdValuePairDTO other = (IdValuePairDTO) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }


    public IdValuePairDTO() {
    }

    public int getIs() {
        return is;
    }

    public void setIs(int is) {
        this.is = is;
    }
    
    
   

	
    
}
