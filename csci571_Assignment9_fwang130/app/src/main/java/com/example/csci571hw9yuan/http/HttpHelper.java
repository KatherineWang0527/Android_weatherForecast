package com.example.csci571hw9yuan.http;

public class HttpHelper {

    public static String autoComplete(String city) {
        return "https://csci571yuanbackend.wl.r.appspot.com/auto?id=" + city;
//        return "https://maps.googleapis.com/maps/api/place/autocomplete/json?types=(cities)&input=" + city + "&key=AIzaSyBhurAdAyM56-tvBQikX8JnUE-c9cYr-b8";
    }

    public static String getLatLng(String location) { //location is the cityState after auto complete
        return "https://csci571yuanbackend.wl.r.appspot.com/location?id="+location;
//        return "https://maps.googleapis.com/maps/api/geocode/json?address=" + location + "&key=AIzaSyBhurAdAyM56-tvBQikX8JnUE-c9cYr-b8";
    }

    public static String getWeather(String latLng) {
        return "https://csci571yuanbackend.wl.r.appspot.com/api?id="+latLng;
//        return "https://weatherhw8970516.wl.r.appspot.com/test1";
//        return "https://weatherhw8970516.wl.r.appspot.com/weather/34.0522,-118.2437";
}


}
