package com.example.csci571hw9yuan.detail;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.example.csci571hw9yuan.MainActivity;
import com.example.csci571hw9yuan.R;
import com.example.csci571hw9yuan.adapter.ViewPagerAdapter;
import com.example.csci571hw9yuan.dao.ExtraFuncs;
import com.example.csci571hw9yuan.dao.TempRange;
import com.example.csci571hw9yuan.dao.Values;
import com.google.android.material.tabs.TabLayout;

public class threeCharts extends AppCompatActivity {
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    Values values;
    TempRange[] tempRange;
    String cityState;
    ExtraFuncs extraFuncs;

    public Values getValues() {
        return values;
    }

    public void setValues(Values values) {
        this.values = values;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        extraFuncs = new ExtraFuncs();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_three_charts);
        values = (Values) getIntent().getSerializableExtra("weatherData");
        setValues(values);
        tempRange = (TempRange[]) getIntent().getSerializableExtra("tempRange");
        cityState = getIntent().getStringExtra("cityState");
        setTitle(cityState);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.calendar_today);
        tabLayout.getTabAt(1).setIcon(R.drawable.trending_up);
        tabLayout.getTabAt(2).setIcon(R.drawable.thermometer_low);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        if (item.getItemId() == android.R.id.home) {
//            if(clickSearchButton) {
//                intent = new Intent(getApplicationContext(), SearchActivity.class);
//            }else{
            intent = new Intent(getApplicationContext(), MainActivity.class);
//            }
            startActivity(intent);
            finish();
        }
        return true;
    }

    public void goToTwitter(View view) {
        String twitterText = "The temperature in " + cityState + " on " + tempRange[0].getStartTime()
                + " is " + values.getTemperature() + "°F. The weather conditions " +
                "are mostly " + extraFuncs.getStatusInfo(Integer.parseInt(values.getWeatherCode()));
        String twitterHref = "https://twitter.com/intent/tweet?text=" + twitterText + "&hashtags=CSCI571WeatherForecast";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(twitterHref));
        startActivity(i);
    }


}




