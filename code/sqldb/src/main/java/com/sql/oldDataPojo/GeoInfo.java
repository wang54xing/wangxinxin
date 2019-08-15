package com.sql.oldDataPojo;

import java.io.Serializable;

/**
 * Created by qinbo on 15/3/18.
 */
public class GeoInfo implements Serializable{

    private static final long serialVersionUID = 1120573452052993682L;
    private int id;
    private String city;
    private String province;
    private String country;
    private int channel;
    private String channelName;
    private int videoServerId;
    private int provinceid;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public int getVideoServerId() {
        return videoServerId;
    }

    public void setVideoServerId(int videoServerId) {
        this.videoServerId = videoServerId;
    }

    public int getProvinceid() {
        return provinceid;
    }

    public void setProvinceid(int provinceid) {
        this.provinceid = provinceid;
    }
}
