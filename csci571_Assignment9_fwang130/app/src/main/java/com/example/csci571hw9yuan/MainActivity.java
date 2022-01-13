package com.example.csci571hw9yuan;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.window.SplashScreen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.csci571hw9yuan.adapter.TabLayoutAdapter;
import com.example.csci571hw9yuan.dao.autocomplete.Auto;
import com.example.csci571hw9yuan.dao.autocomplete.Predictions;
import com.example.csci571hw9yuan.databinding.ActivityMainBinding;
import com.example.csci571hw9yuan.http.HttpHelper;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.gson.Gson;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    boolean clickSearchButton;
    private ProgressBar progressBar;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private TabLayout.Tab tab;
    private ViewPager viewPager;
    ArrayAdapter<String> searchAdapter;
    RequestQueue queue;
    TabLayoutAdapter adapter;
    ProgressDialog progressDialog;


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem menuItem = menu.findItem(R.id.searchBar);
        SearchView searchView = (SearchView) menuItem.getActionView();
        SearchView.SearchAutoComplete searchAutoComplete = (SearchView.SearchAutoComplete) searchView.findViewById(R.id.search_src_text);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                clickSearchButton = true;
                Bundle b = new Bundle();
                b.putString("searchLocation", query);
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                intent.putExtras(b);
                startActivity(intent);
                finish();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                //call autocomplete api to get the List
                List<String> cityStateOptions = new ArrayList<>();
                String autoUrl = HttpHelper.autoComplete(query);
                StringRequest stringRequest = new StringRequest(Request.Method.GET, autoUrl,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Gson gson = new Gson();
                                Auto auto = gson.fromJson(response, Auto.class);
                                List<Predictions> predictions = auto.getPredictions();

                                for (Predictions prediction : predictions) {
                                    cityStateOptions.add(prediction.getDescription());
                                }
                                searchAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.searchable, cityStateOptions);
                                SearchView.SearchAutoComplete searchAutoComplete = (SearchView.SearchAutoComplete) searchView.findViewById(R.id.search_src_text);
                                searchAutoComplete.setAdapter(searchAdapter);
                                searchAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        String p = parent.getItemAtPosition(position) + "";
                                        searchView.setQuery(p, false);
                                    }
                                });
                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
                queue.add(stringRequest);
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }


    public static int getResId(String resName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(resName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
    public  void getDataNotification(int position) {
//        SharedPreferences spf = getSharedPreferences("MyData",MODE_PRIVATE);
//        String favorite = spf.getString("favoriteList","");
//////        Set<String> favorite = spf.getStringSet("favorites", new HashSet<>());
//        adapter.setData(favorite);
        adapter.notifyDataSetChanged();

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("WeatherApp");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tabLayout = findViewById(R.id.pageTabs);
        adapter = new TabLayoutAdapter(getSupportFragmentManager());
        adapter.setContext(getApplicationContext());
        viewPager = (ViewPager) findViewById(R.id.pagesViewPager);
        viewPager.setAdapter(adapter);
        // set the current item as 0 (when app opens for first time)
        viewPager.setCurrentItem(0);
        viewPager.setOffscreenPageLimit(10);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setupWithViewPager(viewPager);
//        tabLayout.addTab(tabLayout.newTab()); //add home tab to tablayout

        queue = Volley.newRequestQueue(this);


//        SharedPreferences preferences = getSharedPreferences("MyData", Context.MODE_PRIVATE);
//        if (preferences != null) {
//            tabLayout.getTabAt(0).setIcon(statusImgSelected);
//            Set<String> previousS = new HashSet<String>(preferences.getStringSet("favorites", new HashSet<String>()));
//            if (!previousS.isEmpty()) {
//                for (String s : previousS) {
//                    tabLayout.addTab(tabLayout.newTab().setIcon(statusImgDefault));
//                }
//            }
//        }else{
//            tabLayout.setSelectedTabIndicatorHeight(0);
//        }
//        adapter.notifyDataSetChanged();
        // set the adapter


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
//                tab.setIcon(statusImgSelected);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
//                tab.setIcon(statusImgDefault);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}