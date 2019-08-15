package com.db.new33.paike;

/**
 * Created by albin on 2018/7/9.
 */
public class PaiKeXyDto {
    public PaiKeXyDto(Integer x, Integer y) {
        this.x = x;
        this.y = y;
    }

    public PaiKeXyDto() {
    }

    private Integer x;
    private Integer y;

    private Integer count;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof PaiKeXyDto){
            PaiKeXyDto o = (PaiKeXyDto) obj;
            if(getX() == o.getX() && getY() == o.getY()){
                return true;
            }else {
                return false;
            }
        }else {
            return false;
        }
    }
}
