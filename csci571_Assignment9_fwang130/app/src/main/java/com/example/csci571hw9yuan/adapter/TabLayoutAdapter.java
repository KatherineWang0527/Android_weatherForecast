package com.example.csci571hw9yuan.adapter;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

import com.example.csci571hw9yuan.Fragments.FavTempleteFragment;
import com.example.csci571hw9yuan.Fragments.HomeFragment;
import com.example.csci571hw9yuan.MainActivity;
import com.example.csci571hw9yuan.R;
import com.google.android.material.tabs.TabLayout;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TabLayoutAdapter extends FragmentPagerAdapter {
    Context mContext;
    SharedPreferences preferences;
    String previous;

    public void setContext(Context context) {
        mContext = context;
        preferences = mContext.getSharedPreferences("MyData", 0);
        if (preferences != null) {
            previous = preferences.getString("favoriteList","");
//            previousS = new HashSet<String>(preferences.getStringSet("favorites", new HashSet<String>()));
        }
    }

    public TabLayoutAdapter(FragmentManager manager) {
        super(manager);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return HomeFragment.getInstance(position);
        } else {
            return FavTempleteFragment.getInstance(position);
        }
    }


    //this is called when notifyDataSetChanged() is called
    @Override
    public int getItemPosition(Object object) {
        // refresh all fragments when data set changed
        return PagerAdapter.POSITION_NONE;
    }


    @Override
    public int getCount() {
        previous = preferences.getString("favoriteList","");
//        previousS = new HashSet<String>(preferences.getStringSet("favorites", new HashSet<String>()));
//        return !previousS.isEmpty() ? previousS.length() + 1 : 1;
        if(previous.isEmpty()) {
            return 1;
        }else{
            String[] arr = previous.split(";");
            return arr.length + 1;
        }
    }

}
