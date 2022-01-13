package com.example.csci571hw9yuan.dao;

import com.example.csci571hw9yuan.HashTables;

import java.io.Serializable;

public class ExtraFuncs{
    HashTables hashTables;
    public ExtraFuncs() {
        hashTables = new HashTables();
    }

    public String getStatusInfo(int weatherCode) {

        String[] s = hashTables.weatherCodeMap.get(weatherCode);
        return s != null ? s[1] : null;
    }

    public String getStatusImgSrc(int weatherCode) {
        String[] s = hashTables.weatherCodeMap.get(weatherCode);
        return s != null ? s[0] : null;
    }

}
