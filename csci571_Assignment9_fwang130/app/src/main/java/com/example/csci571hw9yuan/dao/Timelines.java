package com.example.csci571hw9yuan.dao;

import java.util.List;

public class Timelines {
    String startTime;
    String endTime;
    List<Intervals> intervals;

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public List<Intervals> getIntervals() {
        return intervals;
    }

    public void setIntervals(List<Intervals> intervals) {
        this.intervals = intervals;
    }
}
