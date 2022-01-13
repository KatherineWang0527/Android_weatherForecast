package com.example.csci571hw9yuan;

import java.util.HashMap;
import java.util.Map;

public class HashTables {
    public static Map<Integer,  String[]> weatherCodeMap ;
    public static Map<Integer, String> dayMap;
    public static Map<Integer, String> monMap;
    public HashTables(){
        weatherCodeMap = new HashMap<>();
        dayMap = new HashMap<>();
        monMap = new HashMap<>();
        this.weatherCodeTable();
        this.dayMapTable();
       this.monMapTable();
    }


    public void weatherCodeTable() {
            weatherCodeMap.put(4201, new String[]{"ic_rain_heavy", "Heavy Rain"})
            ;

            weatherCodeMap.put(4001,new String[]{"ic_rain", "Rain"})
            ;
            weatherCodeMap.put(4200,new String[]{"ic_rain_light", "Light Rain"})
            ;
            weatherCodeMap.put(6201,new String[]{"ic_freezing_rain_heavy", "Heavy Freezing Rain"})
            ;
            weatherCodeMap.put(6001,new String[]{"ic_freezing_rain", "Freezing Rain"})
            ;
            weatherCodeMap.put(6200,new String[]{"ic_freezing_rain_light", "Light Freezing Rain"})
            ;
            weatherCodeMap.put(6000,new String[]{"ic_freezing_drizzle", "Freezing Drizzle"})
            ;
            weatherCodeMap.put(4000,new String[]{"ic_drizzle", "Drizzle"})
            ;
            weatherCodeMap.put(7101,new String[]{"ic_ice_pellets_heavy", "Heavy Ice Pallets"})
            ;
            weatherCodeMap.put(7000,new String[]{"ic_ice_pellets", "Ice pallets"})
            ;
            weatherCodeMap.put(7102,new String[]{"ic_ice_pellets_light", "Light Ice Pallets"})
            ;
            weatherCodeMap.put(5101,new String[]{"ic_snow_heavy", "Heavy Snow"})
            ;
            weatherCodeMap.put(5000,new String[]{"ic_snow", "Snow"})
            ;
            weatherCodeMap.put(5100,new String[]{"ic_snow_light", "Light Snow"})
            ;
            weatherCodeMap.put(5001,new String[]{"ic_flurries", "Flurries"})
            ;
            weatherCodeMap.put(8000,new String[]{"ic_tstorm", "Thundersotrm"})
            ;
            weatherCodeMap.put(2100,new String[]{"ic_fog_light", "Light Fog"})
            ;
            weatherCodeMap.put(2000,new String[]{"ic_fog", "Fog"})
            ;
            weatherCodeMap.put(1001,new String[]{"ic_cloudy", "Cloudy"})
            ;
            weatherCodeMap.put(1102,new String[]{"ic_mostly_cloudy", "Mostly Cloudy"})
            ;
            weatherCodeMap.put(1101,new String[]{"ic_partly_cloudy_day", "Partly Cloudy"})
            ;
            weatherCodeMap.put(1100,new String[]{"ic_mostly_clear_day", "Mostly Clear"})
            ;
            weatherCodeMap.put(1000,new String[]{"ic_clear_day", "Clear, Sunny"})
            ;

    }

    public void  dayMapTable() {
        dayMap.put(1, "Monday");
        dayMap.put(2, "Tuesday");
        dayMap.put(3, "Wednesday");
        dayMap.put(4, "Thursday");
        dayMap.put(5, "Friday");
        dayMap.put(6, "Saturday");
        dayMap.put(0, "Sunday");

    }
    public void monMapTable() {
        monMap.put(1, "Jan");
        monMap.put(2, "Feb");
        monMap.put(3, "Mar");
        monMap.put(4, "Apr");
        monMap.put(5, "May");
        monMap.put(6, "Jun");
        monMap.put(7, "Jul");
        monMap.put(8, "Aug");
        monMap.put(0, "Sep");
        monMap.put(10, "Oct");
        monMap.put(11, "Nov");
        monMap.put(12, "Dec");

    }

}
