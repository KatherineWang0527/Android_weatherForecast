package com.example.csci571hw9yuan.dao;

import java.io.Serializable;

public class Location implements Serializable {
     String city;
     String region;
     String loc;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }
}
