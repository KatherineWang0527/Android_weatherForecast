package com.example.csci571hw9yuan;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.navigation.ui.AppBarConfiguration;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.csci571hw9yuan.dao.Data;
import com.example.csci571hw9yuan.dao.Intervals;
import com.example.csci571hw9yuan.dao.TempRange;
import com.example.csci571hw9yuan.dao.Timelines;
import com.example.csci571hw9yuan.dao.Values;
import com.example.csci571hw9yuan.dao.Weather1day;
import com.example.csci571hw9yuan.databinding.ActivitySearchableBinding;
import com.example.csci571hw9yuan.detail.threeCharts;
import com.example.csci571hw9yuan.http.HttpHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SearchActivity extends AppCompatActivity {
    private AppBarConfiguration appBarConfiguration;
    private ActivitySearchableBinding binding;
    TextView location;
    String latLng;
    String cityState;
    String currentDate;
    String currentTemp;
    String currentTempStatus;
    Values currentWeatherValue;
    TempRange[] tempRange;
    String searchLocation = null;
    String locationLatLng = null;

    private ProgressBar progressBar;
    public HashTables hashTables;
    private TableLayout tableLayout;

    ProgressDialog progressDialog;
    private View currentWeatherCard;
    RequestQueue queue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchableBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        queue = Volley.newRequestQueue(this);
        hashTables = new HashTables();
        progressBar = findViewById(R.id.progressBar);
        tableLayout = findViewById(R.id.whetherTables);
        tableLayout.setStretchAllColumns(true);
        currentWeatherCard = findViewById(R.id.cardView1);
        Intent intent = getIntent();
        searchLocation = intent.getStringExtra("searchLocation");
        setTitle(searchLocation);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle(searchLocation);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getWeatherDetails(searchLocation);
        RelativeLayout card_view = findViewById(R.id.cardClick); // creating a CardView and assigning a value.

        card_view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), threeCharts.class);
                Bundle b = new Bundle();
                b.putSerializable("weatherData", currentWeatherValue);
                b.putSerializable("tempRange", tempRange);
                b.putString("cityState", searchLocation);
                intent.putExtras(b);
                startActivity(intent);
                finish();
            }
        });
        FloatingActionButton fab = findViewById(R.id.fab1);
        SharedPreferences preferences = getSharedPreferences("MyData", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

//        Set<String> previousS = new HashSet<String>(preferences.getStringSet("favorites", new HashSet<String>()));
        String favoriteList = preferences.getString("favoriteList","");
        if (favoriteList.isEmpty() || !(new ArrayList<>(Arrays.asList(favoriteList.split(";")))).contains(searchLocation)) {
            fab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.map_marker_plus));
        } else {
            fab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.map_marker_minus));
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //judge whether it is in the favarotes
                String favoriteList = preferences.getString("favoriteList","");
                List<String> arr = new ArrayList<>(Arrays.asList(favoriteList.split(";")));
                System.out.println("fav:"+arr);
                if (favoriteList.isEmpty() || !arr.contains(searchLocation)) {
                    //add to favorites and change the icon of the floating action bar
                    fab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.map_marker_minus));
                    String toastText = searchLocation + " was added to favorites";
                    Toast.makeText(getBaseContext(), toastText,
                            Toast.LENGTH_SHORT).show();


//                    if(!arr.isEmpty()) {
//                        arr.add(";");
//                    }
                    if(favoriteList.isEmpty()) { //iniital no favorite , cannot be juded by arr.isEmpty()
                        arr = new ArrayList<>();
                    }
                    arr.add(searchLocation);

//                    previousS.add(searchLocation);
                } else {
                    //just remove favorites and change the icon of floating icon
                    fab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.map_marker_plus));
                    String toastText = searchLocation + " was removed from favorites";
                    Toast.makeText(getBaseContext(), toastText,
                            Toast.LENGTH_SHORT).show();
//                    previousS.remove(searchLocation);
                    arr.remove(searchLocation);
                }

                editor.putString("favoriteList", String.join(";", arr));

                editor.apply();

//                getDataNotification(int position);
            }
        });
    }
//    public  void getDataNotification(int position) {
////        Log.d("position",String.valueOf(position));
//
//        SharedPreferences spf = getSharedPreferences("MyData",MODE_PRIVATE);
//        Set<String> favorite = spf.getStringSet("favorites", new HashSet<>());
//
//        adapter.setData(favorite);
//        adapter.notifyDataSetChanged();
//        tabLayout.removeTabAt(position);
////        viewPager.setAdapter(adapter);
////        viewPager.addOnPageChangeListener(tabLayout));
//
////        adapter.notifyDataSetChanged();
//    }


    //use search
    public void getWeatherDetails(String loc) {
        progressDialog = new ProgressDialog(SearchActivity.this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress);
        if (loc != null && !loc.isEmpty()) {
            getLatLng(loc);
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        if (item.getItemId() == android.R.id.home) {
            intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
        return true;
    }

    private void getWeatherByLatLng(String latLng1) {
        if (latLng1 == null || latLng1.isEmpty()) {
            return;
        }
        String url = HttpHelper.getWeather(latLng1);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Gson gson = new Gson();
                        Weather1day weather1day = gson.fromJson(response, Weather1day.class);
                        Data data = weather1day.getData();
                        Timelines timeline = data.getTimelines().get(0);
                        List<Intervals> intervals = timeline.getIntervals();
                        tableLayout.setStretchAllColumns(true);
                        tableLayout.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT, 1.0f));

                        tempRange = new TempRange[intervals.size()];
                        for (int i = 0; i < intervals.size(); i++) {
                            Intervals interval = intervals.get(i);
                            Values value = interval.getValues();
                            View view = getLayoutInflater().inflate(R.layout.weatheritem, null, false);

                            TextView t1 = view.findViewById(R.id.date);
                            ImageView v2 = view.findViewById(R.id.weatherImg);
                            TextView t3 = view.findViewById(R.id.minTemp);
                            TextView t4 = view.findViewById(R.id.maxTemp);
                            TableRow tableRow = new TableRow(SearchActivity.this);// !!!
                            tableRow.setLayoutParams(new TableRow.LayoutParams(
                                    TableRow.LayoutParams.WRAP_CONTENT,
                                    TableRow.LayoutParams.WRAP_CONTENT));

                            String startTime = getDate(interval.getStartTime());
                            String weatherCode = value.getWeatherCode();
                            String statusImgSrc = getStatusImgSrc(Integer.parseInt(weatherCode));
                            int statusImg = getResId(statusImgSrc, R.drawable.class);
                            String minTemp = Math.round(Float.parseFloat(value.getTemperatureMin())) + "";
                            String maxTemp = Math.round(Float.parseFloat(value.getTemperatureMax())) + "";
                            String status = getStatusInfo(Integer.parseInt(weatherCode));
                            t1.setText(startTime);
                            v2.setImageResource(statusImg);
                            t3.setText(minTemp);
                            t4.setText(maxTemp);

                            tableLayout.addView(view, i);
                            tempRange[i] = new TempRange(startTime, minTemp, maxTemp);

                            //current weather card
                            if (i == 0) {
                                TextView cityState = findViewById(R.id.cityState);
                                cityState.setText(searchLocation);
                                ImageView imageView = findViewById(R.id.currentWhetherImg);
//                              imageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), statusImg));
                                imageView.setImageResource(statusImg);

                                TextView tempNumView = findViewById(R.id.tempNum);
                                tempNumView.setText(Math.round(Float.parseFloat(value.getTemperature())) + "°F");

                                TextView tempStatusView = findViewById(R.id.tempStatus);
                                tempStatusView.setText(status);

                                TextView humidityView = findViewById(R.id.humidity);
                                humidityView.setText(value.getHumidity() + "%");

                                TextView windSpeedView = findViewById(R.id.windSpeed);
                                windSpeedView.setText(value.getWindSpeed() + "mph");

                                TextView visibilityView = findViewById(R.id.visibility);
                                visibilityView.setText(value.getVisibility() + "mi");

                                TextView pressureView = findViewById(R.id.pressure);
                                pressureView.setText(value.getPressureSeaLevel() + "inHg");
                                currentWeatherValue = value;
                            }
                        }
                        progressDialog.cancel();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO: Handle error
            }
        });
// Access the RequestQueue through your singleton class.
        queue.add(stringRequest);
        //click the current weather card, show more details
    }


    private void getLatLng(String loc) {
        String googleApiUrl = HttpHelper.getLatLng(loc);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, googleApiUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject res = new JSONObject(response);
                            JSONArray jsonArray = res.getJSONArray("results");
                            JSONObject jsonObject = (JSONObject) jsonArray.get(0);
                            JSONObject geo = jsonObject.getJSONObject("geometry");
                            JSONObject location = geo.getJSONObject("location");
                            locationLatLng = location.getString("lat") + "," + location.getString("lng");
                            System.out.println("lat lng: int search ACTIVITY: " + locationLatLng);

                            //call weather tomorrow api
                            getWeatherByLatLng(locationLatLng);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        queue.add(stringRequest);
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

    private String getDate(String date) {
        if (date != null) {
            return date.substring(0, 10);
        }
        return null;
    }

    private String getStatusInfo(int weatherCode) {
        String[] s = hashTables.weatherCodeMap.get(weatherCode);
        return s != null ? s[1] : null;
    }

    private String getStatusImgSrc(int weatherCode) {
        String[] s = hashTables.weatherCodeMap.get(weatherCode);
        return s != null ? s[0] : null;
    }
}
