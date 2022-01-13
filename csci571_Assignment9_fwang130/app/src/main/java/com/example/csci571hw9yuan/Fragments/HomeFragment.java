package com.example.csci571hw9yuan.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.csci571hw9yuan.HashTables;
import com.example.csci571hw9yuan.R;
import com.example.csci571hw9yuan.dao.Data;
import com.example.csci571hw9yuan.dao.Intervals;
import com.example.csci571hw9yuan.dao.Location;
import com.example.csci571hw9yuan.dao.TempRange;
import com.example.csci571hw9yuan.dao.Timelines;
import com.example.csci571hw9yuan.dao.Values;
import com.example.csci571hw9yuan.dao.Weather1day;
import com.example.csci571hw9yuan.detail.threeCharts;
import com.example.csci571hw9yuan.http.HttpHelper;
import com.google.gson.Gson;

import java.lang.reflect.Field;
import java.util.List;

public class HomeFragment extends Fragment {
    TextView location;
    String latLng;
    String cityState;
    Values currentWeatherValue;
    TempRange[] tempRange;
    ArrayAdapter<String> searchAdapter;
    SearchView searchView;
    boolean clickSearchButton;

    int position;


    public static Fragment getInstance(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt("favPosition", position);
        HomeFragment tabFragment = new HomeFragment();
        tabFragment.setArguments(bundle);
        return tabFragment;
    }

    //    private ProgressBar progressBar;
    ProgressDialog progressDialog;

    public HashTables hashTables;
    private TableLayout tableLayout;
    private View currentWeatherCard;

    RequestQueue queue;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        position = getArguments().getInt("favPosition");

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        hashTables = new HashTables();
        tableLayout = view.findViewById(R.id.whetherTables);
        tableLayout.setStretchAllColumns(true);
        currentWeatherCard = view.findViewById(R.id.cardView1);

        getCurrentWeather(view);//get current weather
        RelativeLayout card_view = (RelativeLayout) view.findViewById(R.id.cardClick); // creating a CardView and assigning a value.

        card_view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), threeCharts.class);
                Bundle b = new Bundle();
                b.putSerializable("weatherData", currentWeatherValue);
                b.putSerializable("tempRange", tempRange);
                b.putString("cityState", cityState);
                intent.putExtras(b);
                startActivity(intent);
                getActivity().finish();
            }
        });
    }


    public void getCurrentWeather(View view) {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress);
//        https://ipinfo.io/?token=9f4945ee82ca76

        String localLocationUrl = "https://ipinfo.io/?token=585d57e2ef08f5";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, localLocationUrl,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        Location location = gson.fromJson(response, Location.class);
                        latLng = location.getLoc();
                        cityState = location.getCity() + "," + location.getRegion();
                        TextView cS = view.findViewById(R.id.cityState);
                        cS.setText(cityState);
                        getWeatherByLatLng(latLng, view);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        queue.add(stringRequest);
        //call getWeather tomorrow api and show the page
    }


    private void getWeatherByLatLng(String latLng1, View v) {
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
                            TableRow tableRow = new TableRow(getActivity());
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
                                ImageView imageView = v.findViewById(R.id.currentWhetherImg);
//                              imageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), statusImg));
//
                                imageView.setImageResource(statusImg);
//

                                TextView tempNumView = v.findViewById(R.id.tempNum);
                                tempNumView.setText(Math.round(Float.parseFloat(value.getTemperature())) + "°F");

                                TextView tempStatusView = v.findViewById(R.id.tempStatus);
                                tempStatusView.setText(status);

                                TextView humidityView = v.findViewById(R.id.humidity);
                                humidityView.setText(value.getHumidity() + "%");

                                TextView windSpeedView = v.findViewById(R.id.windSpeed);
                                windSpeedView.setText(value.getWindSpeed() + "mph");

                                TextView visibilityView = v.findViewById(R.id.visibility);
                                visibilityView.setText(value.getVisibility() + "mi");

                                TextView pressureView = v.findViewById(R.id.pressure);
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
        return s != null ? s[1]
                : null;
    }

    private String getStatusImgSrc(int weatherCode) {
        String[] s = hashTables.weatherCodeMap.get(weatherCode);
        return s != null ? s[0] : null;
    }
}
