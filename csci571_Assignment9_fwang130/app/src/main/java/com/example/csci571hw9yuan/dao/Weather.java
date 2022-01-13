package com.example.csci571hw9yuan.dao;

//import com.google.gson.annotations.SerializedName;

public class Weather { // in the values json

    private String startTime;
    private String status;

    private Integer statusImg;

    private Integer weatherCode;

    private String tempMax;

    private String tempMin;

    private String temp;

//    private String sunriseTime;
//
//    private String sunsetTime;

    private String humidity;

    private String windSpeed;

    private String visibility;

    private String cloudCover;

    private String uvIndex;

    private String pressure;

    private String precipitation;

    public Weather(String startTime, String status, Integer statusImg, Integer weatherCode, String tempMax, String tempMin, String temp, String humidity, String windSpeed, String visibility, String cloudCover, String uvIndex, String pressure, String precipitation) {
        this.startTime = startTime;
        this.status = status;
        this.statusImg = statusImg;
        this.weatherCode = weatherCode;
        this.tempMax = tempMax;
        this.tempMin = tempMin;
        this.temp = temp;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.visibility = visibility;
        this.cloudCover = cloudCover;
        this.uvIndex = uvIndex;
        this.pressure = pressure;
        this.precipitation = precipitation;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public String getPrecipitation() {
        return precipitation;
    }

    public void setPrecipitation(String precipitation) {
        this.precipitation = precipitation;
    }

    public String getUvIndex() {
        return uvIndex;
    }


    public void setUvIndex(String uvIndex) {
        this.uvIndex = uvIndex;
    }



    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public Integer getWeatherCode() {
        return weatherCode;
    }

    public void setWeatherCode(Integer weatherCode) {
        this.weatherCode = weatherCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getStatusImg() {
        return statusImg;
    }

    public void setStatusImg(Integer statusImg) {
        this.statusImg = statusImg;
    }

    public void setCloudCover(String cloudCover) {
        this.cloudCover = cloudCover;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }


    public String getTempMax() {
        return tempMax;
    }

    public void setTempMax(String tempMax) {
        this.tempMax = tempMax;
    }

    public String getTempMin() {
        return tempMin;
    }

    public void setTempMin(String tempMin) {
        this.tempMin = tempMin;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public String getCloudCover() {
        return cloudCover;
    }
}