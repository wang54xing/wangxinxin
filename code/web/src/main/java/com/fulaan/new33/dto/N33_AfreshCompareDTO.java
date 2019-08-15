package com.fulaan.new33.dto;

/**
 * Created by James on 2019-05-14.
 *
 *   二次分配记录
 *
 */
public class N33_AfreshCompareDTO {
    //应有12 种组合

    /*   物理

     物化生
     物化地
     物生地
     物化政
     物生政
     物政地
     */

    /*  历史

     历生政
     历化地
     历生地
     历化生
     历政地
     历化政
    */
    private int count;

    private int volume;

    private int swim;

    private String compose;

    private int n1;

    private int n2;

    private int n3;

    private int zu;  //0  组   1 拆

    private int remove;//  0  存在   1 移除

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public int getSwim() {
        return swim;
    }

    public void setSwim(int swim) {
        this.swim = swim;
    }

    public String getCompose() {
        return compose;
    }

    public void setCompose(String compose) {
        this.compose = compose;
    }

    public int getN1() {
        return n1;
    }

    public void setN1(int n1) {
        this.n1 = n1;
    }

    public int getN2() {
        return n2;
    }

    public void setN2(int n2) {
        this.n2 = n2;
    }

    public int getN3() {
        return n3;
    }

    public void setN3(int n3) {
        this.n3 = n3;
    }

    public int getZu() {
        return zu;
    }

    public void setZu(int zu) {
        this.zu = zu;
    }

    public int getRemove() {
        return remove;
    }

    public void setRemove(int remove) {
        this.remove = remove;
    }
}
