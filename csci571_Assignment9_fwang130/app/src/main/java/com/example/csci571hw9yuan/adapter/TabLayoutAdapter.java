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
    private long baseId = 0;
    String previous;
    List<String> list = new ArrayList<>();

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

//    @Override
//    public int getItemPosition (Object object)
//    {
////        int i = 0;
////        for(String s: previousS) {
////            if(s.equals(object)) {
////                return i;
////            }
////            i++;
////        }
//        return POSITION_NONE;
//    }
//        int index = views.indexOf (object);
//        if (index == -1)
//            return POSITION_NONE;
//        else
//            return index;
//    }
    //this is called when notifyDataSetChanged() is called
    @Override
    public int getItemPosition(Object object) {
        // refresh all fragments when data set changed
        return PagerAdapter.POSITION_NONE;
    }
//
//    @Override
//    public long getItemId(int position) {
//        // give an ID different from position when position has been changed
//        return baseId + position;
//    }


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

//
//    @Override
//    public CharSequence getPageTitle(int position) {
//        if(preferences == null) {
//            return "";
//
////            tabLayout.getTabAt(0).setIcon(statusImgDefault);
//        }else{
//
//            return position+"";
//        }
//
//    }
public void setData(String s) {
        this.previous = s;
}

//    public void setData(Set<String> favorite) {
//        this.previousS = favorite;
//    }
}
