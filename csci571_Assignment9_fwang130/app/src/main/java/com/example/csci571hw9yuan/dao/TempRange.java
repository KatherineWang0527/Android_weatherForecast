package com.example.csci571hw9yuan.dao;

import java.io.Serializable;

public class TempRange implements Serializable {
    private String startTime;
    private String minTemp;
    private String maxTemp;

    public TempRange(String startTime, String minTemp, String maxTemp) {
        this.startTime = startTime;
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(String minTemp) {
        this.minTemp = minTemp;
    }

    public String getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(String maxTemp) {
        this.maxTemp = maxTemp;
    }
}
