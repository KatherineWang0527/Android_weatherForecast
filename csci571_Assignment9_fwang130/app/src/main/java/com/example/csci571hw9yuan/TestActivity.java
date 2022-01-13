package com.example.csci571hw9yuan;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashSet;
import java.util.Set;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        SharedPreferences spf = getSharedPreferences("MyData",MODE_PRIVATE);
//        Set<String> favorite = spf.getStringSet("favorites", new HashSet<>());
//        if (favorite == null) {
//            Log.d("favorite", "isNull");
//        } else {
//            Log.d("favorite-len", String.valueOf(favorite.size()));
//        }
//
//        favorite = new HashSet<>();
//
        SharedPreferences.Editor editor = spf.edit();
        editor.putString("favoriteList", "");
        editor.apply();

    }
}