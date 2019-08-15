package com.sql.oldDataPojo;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by qinbo on 15/3/19.
 */
public enum ProvinceEnum {
    SHANDONG(1,"山东省"),	 JIANGSU(2,"江苏省"), ANHUI(3,"安徽省"), ZHEJIANG(4,"浙江省"), FUJIAN(5,"福建省"), SHANGHAI(6,"上海市"),
    GUANGDONG(7,"广东省"), GUANGXI(8,"广西壮族自治区"), HAINAN(9,"海南省"),
    HUBEI(10,"湖北省"), HUNAN(11,"湖南省"), HENAN(12,"河南省"), JIANGXI(13,"江西省"),
    BEIJING(14,"北京市"), TIANJIN(15,"天津市"), HEBEI(16,"河北省"), SHANXI(17,"山西省"), NEIMENGGU(18,"内蒙古自治区"),
    NINGXIA(19,"宁夏回族自治区"), XINJIANG(20,"新疆维吾尔族自治区"), QINGHAI(21,"青海省"), SANXI(22,"陕西省"), GANSU(23,"甘肃省"),
    SICHUAN(24,"四川省"), YUNNAN(25,"云南省"), GUIZHOU(26,"贵州省"), XIZANG(27,"西藏自治区"), CHONGQING(28,"重庆市"),
    LIAONING(29,"辽宁省"), JILIN(30,"吉林省"), HEILONGJIANG(31,"黑龙江省"),
    TAIWAN(32,"台湾省"), XIANGGANG(33,"香港市"), GUOWAI(34,"国外");
	/*华东地区——山东、江苏、安徽、浙江、福建、上海
	华南地区——广东、广西、海南
	华中地区——湖北、湖南、河南、江西
	华北地区——北京、天津、河北、山西、内蒙古
	西北地区——宁夏、新疆、青海、陕西、甘肃
	西南地区——四川、云南、贵州、西藏、重庆
	东北地区——辽宁、吉林、黑龙江*/


    private int status;
    private String description;
    static Map<Integer, String> thismap = null;

    private ProvinceEnum(int status, String description) {
        this.status = status;
        this.description = description;
    }

    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public static Map<Integer, String> getMap(){
        if(null == thismap){
            thismap = new HashMap<Integer, String>();
            for(ProvinceEnum thisenum : ProvinceEnum.values()){
                thismap.put(thisenum.getStatus(), thisenum.getDescription());
            }
        }
        return thismap;
    }

    public static String getValueByKey(int key){
        return ProvinceEnum.getMap().get(key);
    }
}
