package com.example.csci571hw9yuan.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.ui.AppBarConfiguration;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.csci571hw9yuan.HashTables;
import com.example.csci571hw9yuan.MainActivity;
import com.example.csci571hw9yuan.R;
import com.example.csci571hw9yuan.adapter.TabLayoutAdapter;
import com.example.csci571hw9yuan.dao.Data;
import com.example.csci571hw9yuan.dao.Intervals;
import com.example.csci571hw9yuan.dao.TempRange;
import com.example.csci571hw9yuan.dao.Timelines;
import com.example.csci571hw9yuan.dao.Values;
import com.example.csci571hw9yuan.dao.Weather1day;

//import com.example.csci571hw9yuan.databinding.ActivityMainBinding;
import com.example.csci571hw9yuan.detail.threeCharts;
import com.example.csci571hw9yuan.http.HttpHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
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

public class FavTempleteFragment extends Fragment {
    String cityState;
    Values currentWeatherValue;
    TempRange[] tempRange;
    ArrayAdapter<String> searchAdapter;
    int position;


    public static Fragment getInstance(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt("favPosition", position);
        FavTempleteFragment favTempleteFragment = new FavTempleteFragment();
        favTempleteFragment.setArguments(bundle);
        return favTempleteFragment;
    }

    private ProgressBar progressBar;
    public HashTables hashTables;
    private TableLayout tableLayout;
    private View currentWeatherCard;
    RequestQueue queue;
    String favoriteLoc;
    private TabLayout tabLayout;
    private TabLayoutAdapter adapter;
    ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        position = getArguments().getInt("favPosition") - 1;
        //according to position get the location name in local storag
        SharedPreferences preferences = getActivity().getSharedPreferences("MyData", Context.MODE_PRIVATE);

//        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
//        Set<String> set = preferences.getStringSet("favorites", new HashSet<>());
//        int i = 0;
//        if (!set.isEmpty()) {
//            for (String s : set) {
//                if (i == position) {
//                    favoriteLoc = s;
//                    break;
//                }
//                i++;
//            }
//        }
        String fvo = preferences.getString("favoriteList", "");
        String[] s = fvo.split(";");
        int i = 0;

        for(String sub: s) {
            if(i == position) {
                favoriteLoc = sub;
            }
            i++;
        }

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_fav_templete, container, false); //??inflate to which layout? create new fragment with new added layout?
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        hashTables = new HashTables();
        progressBar = view.findViewById(R.id.progressBar);
        tableLayout = view.findViewById(R.id.whetherTables);
        tableLayout.setStretchAllColumns(true);
        currentWeatherCard = view.findViewById(R.id.cardView1);
        tabLayout =  getActivity().findViewById(R.id.pageTabs);
//        adapter = new TabLayoutAdapter(getActivity().getSupportFragmentManager());

        getWeatherDetails(favoriteLoc, view);

        RelativeLayout card_view = (RelativeLayout) view.findViewById(R.id.cardClick); // creating a CardView and assigning a value.

        card_view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), threeCharts.class);
                Bundle b = new Bundle();
                b.putSerializable("weatherData", currentWeatherValue);
                b.putSerializable("tempRange", tempRange);
                b.putString("cityState", favoriteLoc);
                intent.putExtras(b);
                startActivity(intent);
                getActivity().finish();
            }
        });

        FloatingActionButton fab = view.findViewById(R.id.fab1);
        SharedPreferences preferences = getActivity().getSharedPreferences("MyData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        fab.setImageDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.map_marker_minus));

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String favoriteList = preferences.getString("favoriteList","");

                List<String> arr = new ArrayList<>(Arrays.asList(favoriteList.split(";")));

                //judge whether it is in the favarotes
                arr.remove(favoriteLoc);
                editor.putString("favoriteList", String.join(";", arr));
//                editor.remove("favorites");
//                editor.putStringSet("favorites", previous);

                editor.apply();
//                adapter.notifyDataSetChanged();
//                tabLayout.removeTabAt(position+1);
                //just remove favorites and change the icon of floating icon
                fab.setImageDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.map_marker_plus));
                String toastText = favoriteLoc + " was removed from favorites";
                Toast toast = Toast.makeText(getActivity(), toastText,
                        Toast.LENGTH_SHORT);
                toast.show();
                ((MainActivity)getActivity()).getDataNotification(position + 1);

            }
        });
    }

    public void removeTab(int position) {
        if (tabLayout.getTabCount() >= 1 && position<tabLayout.getTabCount()) {
            tabLayout.removeTabAt(position);
//            adapter.removeTabPage(position);
        }
    }

    public void getWeatherDetails(String loc, View v) {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress);
        if (loc != null && !loc.isEmpty()) {
            getLatLng(loc,v);
        }
    }

    private void getLatLng(String loc, View v) {
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
                            String latLng = location.getString("lat") + "," + location.getString("lng");

                            //call weather tomorrow api
                            getWeatherByLatLng(latLng, v);
//                System.out.println("lat. lng is:" + latLng1);

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

    private void getWeatherByLatLng(String latLng1, View v) {
        if (latLng1 == null || latLng1.isEmpty()) {
            return;
        }
        String url = HttpHelper.getWeather(latLng1);
//        System.out.println("going to getWeatherByLatLng" + latLng1);
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
                                TextView cityState = v.findViewById(R.id.cityState);
                                cityState.setText(favoriteLoc);

                                ImageView imageView = v.findViewById(R.id.currentWhetherImg);
//                              imageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), statusImg));
                                imageView.setImageResource(statusImg);

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
// Access the RequestQueue through your singleton class.
        queue.add(stringRequest);
        //click the current weather card, show more details
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
