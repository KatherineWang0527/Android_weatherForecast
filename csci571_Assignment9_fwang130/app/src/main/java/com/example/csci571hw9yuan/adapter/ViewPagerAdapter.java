package com.example.csci571hw9yuan.adapter;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.csci571hw9yuan.detail.todayFragment;
import com.example.csci571hw9yuan.detail.weatherDataFragment;
import com.example.csci571hw9yuan.detail.weeklyFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private String title[] = {"TODAY", "WEEKLY", "WEATHER DATA"};

    public ViewPagerAdapter(FragmentManager manager) {
        super(manager);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return todayFragment.getInstance(position);
        } else if (position == 1) {
            return weeklyFragment.getInstance(position);
        } else {
            return weatherDataFragment.getInstance(position);
        }
    }

    @Override
    public int getCount() {
        return title.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return title[position];
    }
}
